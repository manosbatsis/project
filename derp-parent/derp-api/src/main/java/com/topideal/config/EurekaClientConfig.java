package com.topideal.config;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.discovery.EurekaClient;

@Component
public class EurekaClientConfig {
	
	/**
	 * 打印日志
	 */
	public static final Logger logger = LoggerFactory.getLogger(EurekaClientConfig.class);
    
	@Autowired
	private EurekaClient eurekaClient;
	
   /**
    * 注销客户端
    * 关闭应用时执行此方法
    * */
   @PreDestroy
   public void shutDown() {
	   logger.info("Eureka注销客户端..开始");
       eurekaClient.shutdown();
       logger.info("Eureka注销客户端..结束");
      
   }
	   
}
