package com.dragon.utils;

import com.google.common.base.Joiner;

/**
 * 推荐算法工具类
 */
public class RecommendUtil {
    public static final String SPLIT = ":";

    public static final Joiner JOINER = Joiner.on(SPLIT);

    public static String getKey(String... keys) {
        return JOINER.join(keys);
    }
}
