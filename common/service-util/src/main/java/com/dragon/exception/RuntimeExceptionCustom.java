package com.dragon.exception;

import com.dragon.constant.MessageConstant;
import lombok.Data;

/**
 * 自定义异常
 */
@Data
public class RuntimeExceptionCustom extends RuntimeException{
    private Integer code;
    private String message;

    /**
     * 业务异常
     * @param messageConstant
     */
    public RuntimeExceptionCustom(MessageConstant messageConstant) {
        super(messageConstant.getMessage());
        this.code = messageConstant.getCode();
        this.message = messageConstant.getMessage();
    }
}
