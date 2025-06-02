package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    //这是一个get请求的接口
    @GetMapping("/")  //接口的路径,全局唯一的，当在网址栏里输入http://localhost:2020/的时候会返回以下内容
    public Result Hello() {
        return Result.Success("伟大的程序员！！！"); //接口的返回值
    }
    //这是一个login请求的接口
    @PostMapping("/login")  //当在网址栏里输入http://localhost:2020/login的时候会以下的代码返回相应的内容
    public Result Login(@RequestBody Account account) {
        Account dbAccount = null;
        if("ADMIN".equals(account.getRole())){
            dbAccount = adminService.login(account);
        }
        else if("USER".equals(account.getRole())){
            dbAccount = userService.login(account);
        }
        else{
            throw new CustomerException("非法请求");
        }
        return Result.Success(dbAccount);
    }
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.Success();
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        //判断一下用户输入的新密码和确认密码是否一致
        if("ADMIN".equals(account.getRole())){
            adminService.updatePassword(account);
        }
        if("USER".equals(account.getRole())){
            userService.updatePassword(account);
        }

        return Result.Success();
    }
}

