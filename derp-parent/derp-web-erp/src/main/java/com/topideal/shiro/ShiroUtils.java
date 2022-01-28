package com.topideal.shiro;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.redis.RedisUtil;
import com.topideal.entity.vo.main.MerchantInfoModel;

import net.sf.json.JSONObject;

/**
 *  shiro 辅助工具类 
 * Created by weixiaolei on 2018/5/15.
 */
@Component
public class ShiroUtils {
	
	public static RedisUtil redisUtil;
	
	private static int timeOut;
	
	  /**
     * 从shiro 中获取用户信息
     * @return
     */
    public static User getUser(){
        //登陆用户信息
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        return user;
    }
	/**
	 * 通过令牌从redis获取登录用户
	 */
	public static User getUserByToken(String token) throws IOException {
		String userJson = (String) redisUtil.get(token);
		ObjectMapper objectMapper = new ObjectMapper();
		User user = objectMapper.readValue(userJson, User.class);
		// json把商商家id为空的数据转成0
		if(user!=null&&DERP_SYS.USERINFO_USERTYPE_1.equals(user.getUserType())&&user.getMerchantId().intValue()==0) {
			user.setMerchantId(null);
		}
		return user;
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
     * 退出登录
     * */
    public static void logout(){
    	Subject subject = SecurityUtils.getSubject();
    	String token = (String) subject.getSession().getId();
    	redisUtil.del(token);
    	subject.logout();
    }
	/**
	 * 退出登录
	 * */
	public static void logout(String token){
		Subject subject = SecurityUtils.getSubject();
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


	public static void setUser(MerchantInfoModel merchant){
		//登陆用户信息
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		user.setMerchantId(merchant.getId());
		user.setMerchantCode(merchant.getCode());
		user.setMerchantName(merchant.getName());
		user.setTopidealCode(merchant.getTopidealCode());
		
		PrincipalCollection principals = subject.getPrincipals() ;
		String realName= principals.getRealmNames().iterator().next();
		PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realName);
		subject.runAs(newPrincipalCollection);
	}
	public static void setUser1(MerchantInfoModel merchant,String token) throws Exception{
		//登陆用户信息
//		Subject subject = SecurityUtils.getSubject();
		User user = getUserByToken(token);
		user.setMerchantId(merchant.getId());
		user.setMerchantCode(merchant.getCode());
		user.setMerchantName(merchant.getName());
		user.setTopidealCode(merchant.getTopidealCode());
		
		redisUtil.set(token, JSONObject.fromObject(user).toString(),timeOut);
		
//		PrincipalCollection principals = subject.getPrincipals() ;
//		String realName= principals.getRealmNames().iterator().next();
//		PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realName);
//		subject.runAs(newPrincipalCollection);
	}
}
