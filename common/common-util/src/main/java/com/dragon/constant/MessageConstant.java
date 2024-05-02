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
    FAIL(201,"失败"),
    UPLOAD_FAIL(201,"文件上传失败"),
    UPLOAD_NO(201,"文件不存在");


    private Integer code;
    private String message;
}
