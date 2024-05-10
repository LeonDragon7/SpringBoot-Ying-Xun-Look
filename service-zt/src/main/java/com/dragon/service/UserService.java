package com.dragon.service;

import com.dragon.dto.UserLoginDTO;
import com.dragon.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author fzt
 * @since 2024-05-01
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userLoginDTO
     * @return
     */
    void register(UserLoginDTO userLoginDTO);

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    Map login(UserLoginDTO userLoginDTO);
}
