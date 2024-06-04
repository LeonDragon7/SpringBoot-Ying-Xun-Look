package com.dragon.filter;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.dragon.constant.MessageConstant;
import com.dragon.constant.RedisConstant;
import com.dragon.custom.LoginUserInfoHelper;
import com.dragon.jwt.JwtHelper;
import com.dragon.result.ResponseUtil;
import com.dragon.result.Result;
import com.dragon.vo.UserVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.dragon.constant.RedisConstant.LOGIN_USER_TTL;

/**
 * <p>
 * 认证解析token过滤器
 * </p>
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;
    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/client/user/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        logger.info("authentication:"+authentication);
        if(authentication != null){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response);
        }else {
            ResponseUtil.out(response, Result.build(null, MessageConstant.NO_PERMISSION));
        }

        //清理用户信息
        cleanUp();
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        //获取header中的token
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if(!StringUtils.isEmpty(token)){
            String username = JwtHelper.getUsername(token);
            logger.info("username:"+username);
            if(!StringUtils.isEmpty(username)){
                //基于token获取redis用户
                String key = RedisConstant.USER_LOGIN_KEY + token;
                Map<Object,Object> map = redisTemplate.opsForHash().entries(key);
                //判断用户是否存在
                if(map.isEmpty()) return new UsernamePasswordAuthenticationToken(username,null,new ArrayList<>());
                //将查询到hash数据转为UserVo
                UserVo userVo = BeanUtil.fillBeanWithMap(map, new UserVo(), false);
                //保存用户信息到 ThreadLocal
                LoginUserInfoHelper.saveUser(userVo);
                //刷新token有效期
                redisTemplate.expire(key,LOGIN_USER_TTL, TimeUnit.HOURS);

                // 获取用户权限信息并创建SimpleGrantedAuthority对象列表
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                // 获取用户权限信息
                List<String> authoritiesList = (List<String>) map.get("authorities");
                if (authoritiesList != null) {
                    for (String authority : authoritiesList) {
                        authorities.add(new SimpleGrantedAuthority(authority));
                    }
                }
                // 返回UsernamePasswordAuthenticationToken对象
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            }else {
                return new UsernamePasswordAuthenticationToken(username,null,new ArrayList<>());
            }
        }
        return null;
    }

    /**
     * 清理用户信息
     */
    private void cleanUp(){
        //删除保存的用户信息
        LoginUserInfoHelper.removeUser();
    }

}
