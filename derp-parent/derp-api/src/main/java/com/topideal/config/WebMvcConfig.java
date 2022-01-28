package com.topideal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
   
	 @Override
	   public void addViewControllers(ViewControllerRegistry registry) {
		   
		   //默认地址（可以是页面或后台请求接口）
		   registry.addViewController("/").setViewName("forward:/index.jsp");
		   //设置过滤优先级最高
		   registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	   }

}