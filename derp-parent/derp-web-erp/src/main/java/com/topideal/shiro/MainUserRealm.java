package com.topideal.shiro;



import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.redis.RedisUtil;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.service.user.UserInfoService;



/**
 *
 */
public class MainUserRealm extends AuthorizingRealm {

	
	@Autowired
	public UserInfoService userInfoService;
	
	
	//获取用户的权限字符给到shiro进行权限验证
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("权限配置-->doGetAuthorizationInfo");
	    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
	    User loginUser= (User)principals.getPrimaryPrincipal();
	    //获取用户的按钮权限字符添加进去 可从数据库中获取
	    List<String> permissionList = userInfoService.getBtnPersonInfo(loginUser.getId());
	    if(permissionList==null) permissionList = new ArrayList<String>();
	    authorizationInfo.addStringPermissions(permissionList);
	    return authorizationInfo;
	}

	//获取用户信息方法
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("doGetAuthenticationInfo");
	    //获取用户的输入的账号.
	    String userName = (String)token.getPrincipal();
        String password = new String((char[])token.getCredentials()); //得到密码 
	    System.out.println(userName);
	    //通过username从数据库中查找 User对象，如果找到，没找到.
	    //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
	   UserInfoDTO loginUser = userInfoService.searchByUsername(userName);
	    if(loginUser == null) {
			throw new UnknownAccountException();// 没找到帐号
		}
		if(loginUser.getDisable()!=null&&loginUser.getDisable().equals("1")) {
			throw new LockedAccountException(); // 帐号已禁用
		}
		/*单点登录后这里不再验证密码
		String upwd = loginUser.getPassword();
		if(!upwd.equals(password)) {
            throw new IncorrectCredentialsException(); //密码错误
        } */
		//关联商家id值
		String relMerchantIds=null;
		List<String> permissionList = null;
		try{
//			relMerchantIds = userInfoService.searchRelMerchantIds(loginUser.getMerchantId());
			//获取用户的按钮权限字符添加进去 可从数据库中获取
		    permissionList = userInfoService.getBtnPersonInfo(loginUser.getId());
		    if(permissionList==null) permissionList = new ArrayList<String>();
		} catch (Exception e) {
			e.printStackTrace();
		}
		User user=new User();
		user.setId(loginUser.getId());
		user.setLogoImg(loginUser.getLogoImg());
		user.setName(loginUser.getName());
		user.setUsername(loginUser.getUsername());
		user.setPassword(password);
		user.setUserType(loginUser.getUserType());
		user.setCode(loginUser.getCode());
		
//		user.setMerchantId(loginUser.getMerchantId());
//		user.setMerchantName(loginUser.getMerchantName());
//		user.setMerchantCode(loginUser.getMerchantCode());
//		user.setTopidealCode(loginUser.getTopidealCode());
		user.setParentId(loginUser.getParentId());
//		user.setRelMerchantIds(relMerchantIds);
		user.setPermissionList(permissionList);
		return new SimpleAuthenticationInfo(user, password, getName());
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
