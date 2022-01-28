package com.topideal.config;

import com.topideal.common.tools.ApolloUtil;
import com.topideal.filter.MyCasAuthenticationFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CASAutoConfig {
    /**
     * ApolloUtil的注入有时比切面注入晚导致取不到属性问题在此注入一下，确保在切面的前面注入
     * */
    @Autowired
    private ApolloUtil apolloUtil;


    /**
     * 配置登出过滤器
     * 过滤器顺序有要求，先登出-》授权过滤器-》配置过滤验证器-》wraper过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterSingleRegistration() {
        final FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String>  initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", ApolloUtil.serverUrlPrefix);
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(0);
        return registration;
    }
    /**
     * 自定义的过滤器,目的是为了解决session过期或失效点击页面上的接口报异常的问题
     * 如果是location.href的页面跳转则没有问题，如果是ajax请求则会报接口异常跨域，是因为cas无法识别ajax请求
     **/
    @Bean
    public FilterRegistrationBean myfilterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new MyCasAuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", ApolloUtil.serverUrlPrefix);//服务端
        initParameters.put("serverName", ApolloUtil.clientHostUrl);//客户端ip端口

        //配置不需要拦截的url，"|"分隔多个url  与shiro配置的不需要拦截的地址一致
        //initParameters.put("ignorePattern", "/logout/success|/index");
        StringBuffer ignoreUrls = new StringBuffer();
        ignoreUrls.append("/resources/*|/index.jsp|/logout|/login/toLogin.html");
        //webapi
        ignoreUrls.append("|/webapi/system/*");

        //swagger
        ignoreUrls.append("|/derp-webapi.jsp|/doc.html*|/swagger-ui.html|/webjars/*|/v2/*|/swagger-resources/*");

        initParameters.put("ignorePattern", ignoreUrls.toString());
        initParameters.put("ignoreUrlPatternType", "org.jasig.cas.client.authentication.RegexUrlPatternMatcherStrategy");
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(1);
        return registration;
    }
    /**
     * 授权过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterAuthenticationRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        // 设定匹配的路径
        registration.addUrlPatterns("/*");
        Map<String,String> initParameters = new HashMap<>();
        initParameters.put("casServerUrlPrefix", ApolloUtil.serverUrlPrefix);//服务端
        initParameters.put("serverName", ApolloUtil.clientHostUrl);//客户端ip端口

        //配置不需要拦截的url，"|"分隔多个url  与shiro配置的不需要拦截的地址一致
        //initParameters.put("ignorePattern", "/logout/success|/index");
        StringBuffer ignoreUrls = new StringBuffer();
        ignoreUrls.append("/resources/*|/index.jsp|/logout|/login/toLogin.html");
        //webapi
        ignoreUrls.append("|/webapi/system/*");

        //swagger
        ignoreUrls.append("|/derp-webapi.jsp|/doc.html*|/swagger-ui.html|/webjars/*|/v2/*|/swagger-resources/*");

        initParameters.put("ignorePattern", ignoreUrls.toString());
        initParameters.put("ignoreUrlPatternType", "org.jasig.cas.client.authentication.RegexUrlPatternMatcherStrategy");
        registration.setInitParameters(initParameters);
        // 设定加载的顺序
        registration.setOrder(2);
        return registration;
    }

    /**
     * 票据校验过滤器
     * */
    @Bean
    public FilterRegistrationBean ValidationFilterRegistrationBean(){

        FilterRegistrationBean authenticationFilter = new FilterRegistrationBean();
        authenticationFilter.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("casServerUrlPrefix", ApolloUtil.serverUrlPrefix);//服务端根地址
        initParameters.put("serverName", ApolloUtil.clientHostUrl);//客户端ip端口

        authenticationFilter.setInitParameters(initParameters);
        authenticationFilter.setOrder(3);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");// 设置匹配的url
        authenticationFilter.setUrlPatterns(urlPatterns);
        return authenticationFilter;
    }

}
