package com.topideal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/** 
 *Eureka 服务注册中心
 *页面访问地址：http://127.0.0.1:8761/
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaMainApplication {
	 
	public static void main(String[] args) {
	    SpringApplication.run(EurekaMainApplication.class, args);
	}
}
