package com.topideal.report.shiro;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.redis.RedisUtil;

import net.sf.json.JSONObject;

/**
 *  shiro 辅助工具类
 *
 */
@Component
public class ShiroUtils {
	
	public static RedisUtil redisUtil;
	
	private static int timeOut;
    /**
     * 从redis 中获取登录用户信息
     * @return
     */
    public static User getUser(){
    	Subject subject = SecurityUtils.getSubject();
    	String token = (String) subject.getSession().getId();
    	User user = null;
    	String userJson = (String) redisUtil.get(token);
    	if(userJson==null||userJson.equals("")) return user;
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			user = objectMapper.readValue(userJson, User.class);
		}catch(Exception e){}
        return user;
    }
	/**
	 * 通过令牌从redis获取登录用户
	 */
	public static User getUserByToken(String token) throws IOException {
		String userJson = (String) redisUtil.get(token);
		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(userJson, User.class);
		return user;
	}
    
    /**
     * 保存登录令牌
     * @return
     */
    public static void saveToken(User user){
    	Subject subject = SecurityUtils.getSubject();
    	String token = (String) subject.getSession().getId();
    	System.out.println("ordertoken="+token);
    	try{
			redisUtil.set(token, JSONObject.fromObject(user).toString(),timeOut);
		}catch(Exception e){
			e.printStackTrace();
		}
    	
    }
	/**
	 * 保存登录令牌
	 * @return
	 */
	public static String saveToken(String token,User user){
		try{
			redisUtil.set(token, JSONObject.fromObject(user).toString(),timeOut);
		}catch(Exception e){
			e.printStackTrace();
		}
		return token;
	}
    /**
     * 获取token
     * @return
     */
    public static String getToken(){
    	Subject subject = SecurityUtils.getSubject();
    	String token = (String) subject.getSession().getId();
        return token;
    }
    /**
     * 退出登录
     * */
    public static void logout(){
    	Subject subject = SecurityUtils.getSubject();
    	String token = (String) subject.getSession().getId();
    	redisUtil.del(token);
    	subject.logout();
    }
    
    @Autowired
	public void setRedisUtil(RedisUtil redisUtil) {
		ShiroUtils.redisUtil = redisUtil;
	}
    @Value("${token.timeout}")
	public void setTimeOut(int timeOut) {
		ShiroUtils.timeOut = timeOut;
	}
    
    
}
