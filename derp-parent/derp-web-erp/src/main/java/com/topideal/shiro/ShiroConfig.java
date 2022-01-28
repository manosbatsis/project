package com.topideal.shiro;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
/**Shiro过滤器
 * */
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/resources/**", "anon");
        filterChainDefinitionMap.put("/index.jsp", "anon");
        filterChainDefinitionMap.put("/login/login.asyn", "anon");
        filterChainDefinitionMap.put("/login/submitUser.asyn", "anon");
        filterChainDefinitionMap.put("/login/validate.asyn", "anon");
        //sso
        filterChainDefinitionMap.put("/logout", "anon");

        //webapi
        filterChainDefinitionMap.put("/webapi/system/**", "anon");
        filterChainDefinitionMap.put("/derp-webapi.jsp", "anon");
        //swagger
        filterChainDefinitionMap.put("/doc.html*", "anon");
        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/v2/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**/**", "anon");

        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/login/logout", "logout");
        //<!-- 过滤链定义，从上向下顺序执行，一般将/**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
        //filterChainDefinitionMap.put("/**", "authc");//单点登录后shiro不再拦截
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login/toLogin.html");
        // 登录成功后要跳转的链接
        //shiroFilterFactoryBean.setSuccessUrl("/testsuccess");

        //未授权界面;
        //shiroFilterFactoryBean.setUnauthorizedUrl("/static/403");
       
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
    
    //未授权异常界面
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
    	SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
    	Properties properties =new Properties();
    	//properties.put("UnauthorizedException", "403");
    	properties.put("UnauthorizedException", "redirect:/403");
    	exceptionResolver.setExceptionMappings(properties);
        return exceptionResolver;
    }
}