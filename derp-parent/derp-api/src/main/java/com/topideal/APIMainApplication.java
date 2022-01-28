package com.topideal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


//返回jsp页面必须继承SpringBootServletInitializer类重写里面的方法
@SpringBootApplication
@MapperScan("com.topideal.mapper")//扫描dao层
@EnableScheduling
public class APIMainApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(APIMainApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(APIMainApplication.class);
    }
}