package com.topideal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.topideal.mapper")//扫描dao层
public class ErpMainApplication {
	 
	public static void main(String[] args) {
	     SpringApplication.run(ErpMainApplication.class, args);
	}
}
