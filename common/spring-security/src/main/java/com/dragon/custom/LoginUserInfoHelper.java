package com.dragon.custom;

import com.dragon.vo.UserVo;

/**
 * 获取当前用户信息帮助类
 */
public class LoginUserInfoHelper {
    private static final ThreadLocal<Long> userId = new ThreadLocal<Long>();
    private static final ThreadLocal<String> username = new ThreadLocal<String>();

    private static final ThreadLocal<UserVo> userVo = new ThreadLocal<>();

    public static void setUserId(Long _userId) {
        userId.set(_userId);
    }
    public static Long getUserId() {
        return userId.get();
    }
    public static void removeUserId() {
        userId.remove();
    }
    public static void setUsername(String _username) {
        username.set(_username);
    }
    public static String getUsername() {
        return username.get();
    }
    public static void removeUsername() {
        username.remove();
    }
    public static void saveUser(UserVo user){
        userVo.set(user);
    }
    public static UserVo getUser(){
        return userVo.get();
    }
    public static void removeUser(){
        userVo.remove();
    }
}
