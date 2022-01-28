package com.topideal.config;

import com.topideal.interceptor.DuplicateSubmissionHandlerInterceptor;
import com.topideal.interceptor.IDMLoginHandlerInterceptor;
import com.topideal.interceptor.WebApiTokenHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**idm登录-注入拦截器*/
    @Bean
    public IDMLoginHandlerInterceptor idmLoginHandlerInterceptor(){
        return new IDMLoginHandlerInterceptor();
    }
	/**后端接口-注入拦截器*/
	@Bean
	public WebApiTokenHandlerInterceptor tokenHandlerInterceptor(){
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
        //单点拦截器
        registry.addInterceptor(idmLoginHandlerInterceptor())
                .addPathPatterns("/**")//拦截路径
                .excludePathPatterns("/resources/**","/webjars/**","/v2/**","/swagger-resources/**/**");//不拦截的路径
        //webapi拦截器
        registry.addInterceptor(tokenHandlerInterceptor())
                .addPathPatterns("/webapi/**");//拦截路径
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
                .allowedHeaders("*")
                // 是否允许证书 不再默认开启
                .allowCredentials(true)
                // 设置允许的方法
                .allowedMethods("*")
                // 跨域允许时间
                .maxAge(3600);
    }

}