package com.example.common;
import com.example.entity.Admin;
import java.util.List;

public class Result <T> {
    private String message;
    private String code;
    private T data;  // 统一使用泛型字段
    // 通用 Success 方法（支持任意类型）
    public static <T> Result<T> Success(T data) {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMessage("请求成功");
        result.setData(data);
        return result;
    }
//    验证添加数据成不成功的Success方法
    public static <T> Result<T> Success() {
        Result<T> result = new Result<>();
        result.setCode("200");
        result.setMessage("添加数据成功");
        return result;
    }

    public static Result<?>  error( String code,String message){
        Result<?>  result = new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }



    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
