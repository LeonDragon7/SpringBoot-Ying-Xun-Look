package com.dragon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 信息提示常量类
 */
@AllArgsConstructor
@Getter
public enum MessageConstant {

    SUCCESS(200,"成功"),
    UPLOAD_SUCCESS(200,"上传成功"),
    FAIL(201,"失败"),
    UPLOAD_FAIL(201,"文件上传失败"),
    MERGE_FAIL(201,"文件合并失败"),
    DELETE_FAIL(201,"文件删除失败"),
    FILE_NO(201,"文件不存在"),
    EMAIL_FORMAT_ERROR(201,"邮箱格式不正确"),
    USER_EXIST(201,"用户已被注册"),
    SEND_EMIL_FAIL(201,"发送邮件失败"),
    USER_NOT_EXIST(201,"用户不存在"),
    DISABLE_LOGOUT(201,"用户被禁用或注销"),
    USER_PASSWORD_ERROR(201,"用户密码错误");


    private Integer code;
    private String message;
}
