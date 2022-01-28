package com.topideal.listener;


import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.topideal.shiro.MainUserRealm;
import com.topideal.shiro.session.RedisManager;
import com.topideal.shiro.session.RedisSessionDAO;
/**
 * shiro整合springboot因启动顺序问题导致个别service事务不起效处理
 * */
//@Component
@Configuration
public class SpringEventListener {
	
	@Value("${spring.redis.host}")
    private String redisHost;
	@Value("${spring.redis.port}")
    private int redisPort;
	@Value("${spring.redis.password:}")
	private String redisPassword;
    
	@Value("${shiro.sesiontimeout}")
	private long globalSessionTimeout;
	
	@Bean("redisManager")
    public RedisManager getRedisManager(){
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(redisHost);
		redisManager.setPort(redisPort);
		redisManager.setPassword(redisPassword);
		redisManager.setGlobalSessionTimeout(globalSessionTimeout);
        return redisManager;
    }
	
	@Bean("redisSessionDAO")
    public RedisSessionDAO getRedisSessionDAO(){
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(getRedisManager());
        return redisSessionDAO;
    }
	
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		// 设置session过期时间3600s
		//sessionManager.setGlobalSessionTimeout(3600000L);
		sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
		sessionManager.setSessionDAO(getRedisSessionDAO());
		SimpleCookie sessionIdCookie = new SimpleCookie();
		sessionIdCookie.setName("JSID"); 
		sessionManager.setSessionIdCookie(sessionIdCookie);
		return sessionManager;
	}


	@Bean("authorityRealm")
    public MainUserRealm getUserRealm(){
		 MainUserRealm mainUserRealm = new MainUserRealm();
        return mainUserRealm;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(){
    	 DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
         manager.setRealm(getUserRealm());
         manager.setCacheManager(new MemoryConstrainedCacheManager());
         manager.setSessionManager(sessionManager());
         return manager;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        DefaultWebSecurityManager manager = (DefaultWebSecurityManager)context.getBean("securityManager");
        AuthorizingRealm realm = (AuthorizingRealm) context.getBean("authorityRealm");
        manager.setRealm(realm);
    }
    
   
}