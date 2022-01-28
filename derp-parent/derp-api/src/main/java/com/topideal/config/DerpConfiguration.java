package com.topideal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.system.mq.RMQProducer;

/**
 * 配置类
 * */
@Configuration
public class DerpConfiguration {
    
	/**注册bean*/
	@Bean(initMethod = "init",destroyMethod = "destroy")
	public RMQProducer getProducer(){
		return new RMQProducer();
	}
	@Bean(initMethod = "init",destroyMethod = "destroy")
	public RMQLogProducer getLogProducer(){
		return new RMQLogProducer();
	}
}
