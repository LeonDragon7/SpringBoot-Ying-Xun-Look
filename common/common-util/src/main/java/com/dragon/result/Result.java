package com.dragon.result;

import com.dragon.constant.MessageConstant;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result<T> implements Serializable {
    //返回状态码
    private Integer code;
    //返回信息
    private String message;
    //返回数据
    private T data;

    //操作方法
    public static <T> Result<T> build(T data){
        Result<T> result = new Result<>();
        if(result != null)
            result.setData(data);
        return result;
    }

    public static <T> Result<T> build(T data, MessageConstant messageConstant){
        Result<T> result = build(data);
        result.setCode(messageConstant.getCode());
        result.setMessage(messageConstant.getMessage());
        return result;
    }

    //操作成功
    public static <T> Result<T> success(){
        return build(null);
    }

    public static <T> Result<T> success(T data){
        return build(data,MessageConstant.SUCCESS);
    }

    //操作失败
    public static <T> Result<T> fail(){
        return build(null);
    }

    public static <T> Result<T> fail(T data){
        return build(data,MessageConstant.FAIL);
    }

    //全局异常初始化
    public Result<T> message(String msg){
        this.message = msg;
        return this;
    }

    public Result<T> code(Integer code){
        this.code = code;
        return this;
    }
}
