package com.dragon.handler;

import com.dragon.exception.RuntimeExceptionCustom;
import com.dragon.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.geom.RectangularShape;

/*
全局异常处理
作用：将错误信息返回给前端
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        return Result.fail().message("全局异常处理：" + e.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        return Result.fail().message("特定异常处理：" + e.getMessage());
    }
    /**
     * 业务异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeExceptionCustom.class)
    @ResponseBody
    public Result error(RuntimeExceptionCustom e){
        return Result.fail().message(e.getMessage()).code(e.getCode());
    }
}
