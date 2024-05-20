package com.dragon.constant;

/**
 * redis公共常用类
 */
public class RedisConstant {

    /**
     * 用户常用类
     */
    public static final String USER_LOGIN_KEY = "user_login:";
    public static final Long LOGIN_USER_TTL = 10L;

    /**
     * 推荐常用类
     */
    public static final String RECOMMEND = "recommend";

    /**
     * 高分影视常量类
     */
    public static final String HOT_RATING = "hot-rating:";
    public static final Long HOT_RATING_TIME = 1L;
}
