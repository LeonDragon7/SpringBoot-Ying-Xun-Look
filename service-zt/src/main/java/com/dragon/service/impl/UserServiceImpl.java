package com.dragon.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.dragon.constant.MessageConstant;
import com.dragon.constant.RedisConstant;
import com.dragon.dto.UserLoginDTO;
import com.dragon.entity.User;
import com.dragon.exception.RuntimeExceptionCustom;
import com.dragon.jwt.JwtHelper;
import com.dragon.mapper.UserMapper;
import com.dragon.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dragon.utils.MD5;
import com.dragon.utils.RegexUtil;
import com.dragon.utils.UserUtil;
import com.dragon.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.mail-verify.ip}")
    private String prefix;

    Lock lock = new ReentrantLock();

    /**
     * 用户注册
     * @param userLoginDTO
     * @return
     */
    @Transactional
    @Override
    public void register(UserLoginDTO userLoginDTO) {
        //1. 校验邮箱格式
        String email = userLoginDTO.getEmail();
        if(RegexUtil.isEmailInValid(email))
            throw new RuntimeExceptionCustom(MessageConstant.EMAIL_FORMAT_ERROR);
        //2. 根据邮箱判断是否存在用户
        //select * from user where email = ?;
        User isExistUser = query().eq("email", email).one();
        //2.1 存在，用户已被注册
        if(isExistUser != null)
            throw new RuntimeExceptionCustom(MessageConstant.USER_EXIST);
        //3.不存在，发送邮箱验证链接
        //3.1 生成key - uuid，设置redis过期时间
        String key = UUID.randomUUID().toString(true);
        redisTemplate.opsForValue().set(key,email,15,TimeUnit.MINUTES);
        lock.lock();
        try {
            //3.2 根据邮箱接受者，设置文本
            Context context = new Context();
            context.setVariable("email",email);
            context.setVariable("activeUrl",prefix + key);
            //3.3 调用邮件发送模版页面，渲染email接收者、activeUrl激活链接
            String templateEmail = templateEngine.process("templateEmail", context);
            //3.4 创建可以html发送的邮件
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("影迅官网" + "<" + from + ">");
            helper.setTo(email);
            helper.setSubject("影迅官网激活邮件");
            helper.setText(templateEmail,true);
            //3.5 发送邮件
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            lock.unlock();
        }

        //4. 不存在且发送邮件成功，保存到数据库
        User user = new User();
        BeanUtils.copyProperties(userLoginDTO,user);

        //4.1 设置nickname名称
        String nickName = UUID.randomUUID().toString(true).substring(0,10);
        user.setNickname("yingxun" + nickName);
        //4.2 将密码MD5加密
        user.setPassword(MD5.encrypt(userLoginDTO.getPassword()));
        //4.3 设置唯一标签
        user.setSoleTag(MD5.encrypt(Long.toString(System.currentTimeMillis())));
        //4.4 设置随机头像
        user.setAvatar(UserUtil.getRandomAvatar(user.getEmail()));
        //4.5 设置创建更新时间
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        userMapper.save(user);
    }

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public Map login(UserLoginDTO userLoginDTO) {
        //1. 根据用户名/邮箱判断数据库是否存在用户
        String email = userLoginDTO.getEmail();
        String username = userLoginDTO.getUsername();
        User user = userMapper.getUserByEmailOrUsername(userLoginDTO);

        if(user == null) throw new RuntimeExceptionCustom(MessageConstant.USER_NOT_EXIST);

        //2. 如果用户存在，判断是否被禁用 - status
        if(user.getStatus().intValue() != 0) throw new RuntimeExceptionCustom(MessageConstant.DISABLE_LOGOUT);

        //3. 判断密码是否正确
        if(!MD5.encrypt(userLoginDTO.getPassword()).equals(user.getPassword())) throw new RuntimeExceptionCustom(MessageConstant.USER_PASSWORD_ERROR);

        //4. 登录之后保存登录状态和信息

        //4.1 生成token，登录令牌
        String token = null;
        if(!BeanUtil.isEmpty(email)) token = JwtHelper.createToken(user.getId().longValue(), email);
        if(!BeanUtil.isEmpty(username)) token = JwtHelper.createToken(user.getId().longValue(), username);

        //4.2 将User对象拷贝到UserVo
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);

        //4.3 将UserVo转换为HashMap
        Map<String, Object> map = BeanUtil.beanToMap(userVo, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((f1, f2) -> f2.toString()));

        //4.4 将token保存到redis，设置有效期
        String user_token = RedisConstant.USER_LOGIN_KEY + token;
        redisTemplate.opsForHash().putAll(user_token,map);
        redisTemplate.expire(user_token,RedisConstant.LOGIN_USER_TTL,TimeUnit.HOURS);

        //5. 返回token
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("token",token);
        return tokenMap;
    }
}
