package com.example.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtIntercepetor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login","/register","/files/download/**");//接口放行，不鉴权
    }

    @Bean
    public JWTInterceptor jwtIntercepetor(){
        return new JWTInterceptor();
    }
}
