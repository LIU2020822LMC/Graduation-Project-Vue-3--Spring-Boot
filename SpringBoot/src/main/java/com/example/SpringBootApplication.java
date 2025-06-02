package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;



@org.springframework.boot.autoconfigure.SpringBootApplication
@MapperScan("com.example.mapper")
public class SpringBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringBootApplication.class, args);
    }

}
