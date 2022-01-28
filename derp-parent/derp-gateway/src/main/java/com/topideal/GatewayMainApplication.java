package com.topideal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
/**
 * spring Cloud Gateway 网关
 * **/
/**
 * 禁用数据库自动装配
 * */
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class})
@EnableEurekaClient
public class GatewayMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayMainApplication.class, args);
        
    }
    
    /**实现GatewayFilter, Ordered接口方式过滤器*/
   /* @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route(r ->
        		    r.readBody(Object.class, requestBody -> {
                    //log.info("requestBody is {}", requestBody);
                    // 这里 r.readBody做了一个前置语言，这样就可以在filter中通过exchange.getAttribute("cachedRequestBodyObject"); 获取body体
                     return true;
                    }).and()
	                .path("/sys/**")
	                .uri("http://localhost:6320")
	                .filters(new SignFilter())
	                .id("apiweb"))
            .build();
    }*/

}