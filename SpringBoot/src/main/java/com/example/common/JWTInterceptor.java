package com.example.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//对拦截器进行实现
@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Resource
    AdminService adminService;
    @Resource
    UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //1.从请求头拿到token
        String token = request.getHeader("token");
        if(StrUtil.isEmpty(token)){
            //如果没拿到。从参数里再拿一次
            token = request.getParameter("token");
        }
        //2.认证token
        if(StrUtil.isBlank(token)){
            throw new CustomerException("401","您没有权限进行此操作");
        }
        //拿到token的载荷数据
        Account account = null;
        try{
            String audience = JWT.decode(token).getAudience().get(0);
            String[] split = audience.split("-");
            String userId = split[0];
            String role = split[1];
            //根据token解析出来的userId去对应的表查询用户信息
            if("ADMIN".equals(role)){
                account = adminService.selectByID(userId);
            }else if("USER".equals(role)){
                account = userService.selectByID(userId);
            }
        }catch (Exception e){
            throw new CustomerException("401","您没有权限进行此操作");
        }
        if(account == null){
            throw new CustomerException("401","您没有权限进行此操作");
        }
        try{
            //验证签名
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            //通过verify方法验证token
            jwtVerifier.verify(token);
        }catch (Exception e){
            throw new CustomerException("401","您没有权限进行此操作");
        }
        return true;
    }

}
