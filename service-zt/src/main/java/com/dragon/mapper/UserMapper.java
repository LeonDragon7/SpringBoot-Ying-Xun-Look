package com.dragon.mapper;

import com.dragon.annotation.AutoFill;
import com.dragon.dto.UserLoginDTO;
import com.dragon.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dragon.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 注册用户
     * @param user
     */
    @Insert("INSERT INTO user(username, email, password, nickname, create_time, update_time) " +
            "values " +
            "(#{username},#{email},#{password},#{nickname},#{createTime},#{updateTime})")
    @AutoFill(value = OperationType.INSERT)
    void save(User user);

    /**
     * 根据账号或邮箱动态查找用户
     * @param userLoginDTO
     * @return
     */
    User getUserByEmailOrUsername(UserLoginDTO userLoginDTO);

}
