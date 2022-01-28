package com.topideal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


//返回jsp页面必须继承SpringBootServletInitializer类重写里面的方法
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.topideal.mapper")//扫描dao层
public class ReportApiMainApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ReportApiMainApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReportApiMainApplication.class);
    }
}