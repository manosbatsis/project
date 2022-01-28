package com.topideal.order.shiro;


import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.redis.RedisUtil;

/**
 *
 */
public class UserRealm extends AuthorizingRealm {
    
	@Autowired
	private RedisUtil redisUtil;
	
	//获取用户的权限字符给到shiro进行权限验证
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("权限配置-->doGetAuthorizationInfo");
		User user = ShiroUtils.getUser();
		//获取主服务设置到session当前用户的权限字符初始化shiro权限
		List<String> permissionList = user.getPermissionList();
	    if(permissionList==null) permissionList = new ArrayList<String>();
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    authorizationInfo.addStringPermissions(permissionList);
	    return authorizationInfo;
	}

	//获取用户信息方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("doGetAuthenticationInfo");
	    //redis保存的登录用户
        User user = ShiroUtils.getUser();
		return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
	}

	/** 
     * 清除所有用户授权信息缓存. 
     */  
    public void clearCachedAuthorizationInfo(String principal) {  
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());  
        clearCachedAuthorizationInfo(principals);  
    }  
  
  
    /** 
     * 清除所有用户授权信息缓存. 
     */  
    public void clearAllCachedAuthorizationInfo() {  
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();  
        if (cache != null) {  
            for(Object key : cache.keys()) {  
                cache.remove(key);  
            }  
        }  
    }  

	
}
