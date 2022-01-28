package com.topideal.config;

import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.system.mq.RMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
