package com.example.exception;
//自定义异常，运行时报错
public class CustomerException extends RuntimeException{
    private String code;
    private String message;

    //定义三个构造函数

    public CustomerException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public CustomerException(String message) {
        this.code = "500";
        this.message = message;
    }
    public CustomerException() {}
    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
