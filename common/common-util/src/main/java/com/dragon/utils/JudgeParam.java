package com.dragon.utils;


import com.dragon.dto.UserLoginDTO;
import com.dragon.entity.User;

public class JudgeParam {

    /**
     * 验证是否为邮箱还是用户名
     * @param user
     * @return
     */

    public static String isUserNameOrEmail(User user){
        String username = user.getUsername();
        if(RegexUtil.isEmailInValid(username)) return username;
        return user.getEmail();
    }

    /**
     * 验证是否为邮箱还是用户名
     * @param userLoginDTO
     * @return
     */

    public static String isUserNameOrEmail(UserLoginDTO userLoginDTO){
        String username = userLoginDTO.getUsername();
        if(RegexUtil.isEmailInValid(username)) return username;
        return userLoginDTO.getEmail();
    }
}
