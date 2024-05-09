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
     *
     * @param userLoginDTO
     * @return
     */
    void register(UserLoginDTO userLoginDTO);
}
