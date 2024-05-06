package com.dragon.utils;


import cn.hutool.core.util.StrUtil;
import com.dragon.constant.RegexConstant;

/**
 * 登录校验工具类
 */
public class RegexUtil {

    /**
     * 校验邮箱
     * @param str
     * @return
     */
    public static boolean isEmailInValid(String str){
        return matcher(str, RegexConstant.EMAIL_REGEX);
    }

    /**
     * 手机号码校验
     * @param str
     * @return
     */
    public static boolean isPhoneInValid(String str){
        return matcher(str, RegexConstant.PHONE_REGEX);
    }

    /**
     * 密码校验
     * @param str
     * @return
     */
    public static boolean isPasswordInValid(String str){
        return matcher(str, RegexConstant.PASSWORD_REGEX);
    }

    /**
     * 验证码校验
     * @param str
     * @return
     */
    public static boolean isCodeInValid(String str){
        return matcher(str, RegexConstant.VERIFY_CODE_REGEX);
    }

    /**
     * 根据正则表达式校验
     * @param str
     * @param regex
     * @return
     */
    public static boolean matcher(String str,String regex){
        if(StrUtil.isBlank(str)) return true;

        return !str.matches(regex);
    }
}
