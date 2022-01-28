package com.topideal.report.listener;


import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.topideal.report.shiro.UserRealm;
/**
 * shiro整合springboot因启动顺序问题导致个别service事务不起效处理
 * */
@Component
public class SpringEventListener {

	
	@Bean("authorityRealm")
    public UserRealm getUserRealm(){
		 UserRealm mainUserRealm = new UserRealm();
        return mainUserRealm;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(){
    	 DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
         manager.setRealm(getUserRealm());
         manager.setCacheManager(new MemoryConstrainedCacheManager());
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