package com.topideal.storage.config;

import com.topideal.storage.interceptor.WebApiTokenHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.topideal.common.tools.ApolloUtil;
import com.topideal.storage.interceptor.DuplicateSubmissionHandlerInterceptor;
import com.topideal.storage.interceptor.TokenHandlerInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
	/**注入拦截器*/
	/*@Bean
	public TokenHandlerInterceptor tokenHandlerInterceptor(){
		return new TokenHandlerInterceptor();
	}*/

    /**webapi注入拦截器*/
    @Bean
    public WebApiTokenHandlerInterceptor webApiTokenHandlerInterceptor(){
        return new WebApiTokenHandlerInterceptor();
    }
    
    /**webapi注入重复提交拦截器*/
    @Bean
    public DuplicateSubmissionHandlerInterceptor duplicateSubmissionHandlerInterceptor(){
        return new DuplicateSubmissionHandlerInterceptor();
    }

	/**添加拦截器*/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(tokenHandlerInterceptor()).addPathPatterns("/**");//拦截所有路径
        registry.addInterceptor(webApiTokenHandlerInterceptor()).addPathPatterns("/webapi/**");//拦截路径
        registry.addInterceptor(duplicateSubmissionHandlerInterceptor()).addPathPatterns("/webapi/**");//拦截路径
    }
    /**
     * 允许CORS跨域请求
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 是否允许证书 不再默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}