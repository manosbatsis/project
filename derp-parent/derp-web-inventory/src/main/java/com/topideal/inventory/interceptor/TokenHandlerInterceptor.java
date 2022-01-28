package com.topideal.inventory.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.redis.RedisUtil;
import com.topideal.inventory.shiro.ShiroUtils;

/**
 * 拦截器
 */
public class TokenHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token=request.getParameter("token");
        if(StringUtils.isNotBlank(token)){
        	//redis获取到用户信息
            String userJson = (String) redisUtil.get(token);
            ObjectMapper objectMapper = new ObjectMapper();
		    User user = objectMapper.readValue(userJson, User.class);
            if(StringUtils.isNotBlank(userJson)){
            	ShiroUtils.saveToken(user);
            	UsernamePasswordToken upToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());  
            	Subject subject = SecurityUtils.getSubject();  
            	subject.login(upToken);//初始化登录
            	
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
