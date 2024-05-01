package com.dragon.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultEnum {
    SUCCESS(200,"成功"),
    FAIL(201,"失败");

    private Integer code;
    private String message;
}
