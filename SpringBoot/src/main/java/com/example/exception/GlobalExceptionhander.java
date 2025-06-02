package com.example.exception;

import  com.example.common.Result;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;

//全局异常捕获器
@ControllerAdvice("com.example.controller")
public class GlobalExceptionhander {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionhander.class);
  @ExceptionHandler(Exception.class)
  @ResponseBody //返回json串，将result对象转换成json的格式
  public Result error(Exception e) {
      log.error("系统出现以下异常",e);
      return Result.error("500","真倒霉呀!出现系统异常了！！！赶紧检查一下哪里出现错误啦");
  }

    @ExceptionHandler(CustomerException.class)
    @ResponseBody //返回json串，将result对象转换成json的格式
    public Result customererror(CustomerException e) {
        log.error("自定义错误",e);
        return Result.error(e.getCode(),e.getMessage());
    }
}
