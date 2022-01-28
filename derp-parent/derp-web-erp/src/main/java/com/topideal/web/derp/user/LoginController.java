package com.topideal.web.derp.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.user.UserInfoService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.system.auth.User;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.tools.MD5Utils;
import com.topideal.shiro.ShiroUtils;

/**
 * Created by weixiaolei on 2018/8/7.
 */
@RequestMapping("/login")
@Controller
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MerchantInfoService merchantInfoService;
    /**
     * 校验令牌是否失效
     * @return
     */
	@RequestMapping("/validate.asyn")
    @ResponseBody
    private ViewResponseBean validate(HttpSession session){
    	
    	 //redis 获取登录用户信息
        User user= ShiroUtils.getUser();
        //主系统登陆用户失效
        if(user==null){
        	ShiroUtils.logout();//退出登录
            return ResponseFactory.error(StateCodeEnum.ERROR_801);
        }
        String token = session.getId();
        ShiroUtils.saveToken(token,user);
        return ResponseFactory.success(token);
    }

    /** 
     * 初始登陆界面 
     * @param request 
     * @return 
     */  
    @RequestMapping("/toLogin.html")  
    public String tologin(HttpServletRequest request){  
        System.out.println("来自IP[" + request.getRemoteHost() + "]的访问");  
        try{
        	 Subject subject = SecurityUtils.getSubject();  
        	 if(subject.isAuthenticated()){//已经登录过
        		return "redirect:/index.html"; 
        	 }
		}catch(Exception e){}
        return "login";  
    }  
    /** 
     * 验证用户名和密码 
     * @param request 
     * @return 
     * @throws IOException 
     * @throws ServletException 
     */  
    @RequestMapping("/login.asyn")  
    @ResponseBody  
    public ViewResponseBean login(HttpSession session
    		,HttpServletRequest request,HttpServletResponse response) throws Exception {
    	System.out.println("-----------login start-----------------");  
    	
        String username = request.getParameter("username");// 取得用户名
        String password = request.getParameter("password");//取得 密码，并用MD5加密
        String remember = request.getParameter("remember");//记住账号密码
        String passwordmd5 = MD5Utils.encode(password);
        //校验用户
        UserInfoDTO loginUser = userInfoService.searchByUsername(username);
        if(loginUser == null) {
            return ResponseFactory.error(StateCodeEnum.ERROR_308);// 没找到帐号
        }
        if(loginUser.getDisable()!=null&&loginUser.getDisable().equals("1")) {
            return ResponseFactory.error(StateCodeEnum.MESSAGE_10002);// 帐号已禁用
        }
        String upwd = loginUser.getPassword();
        if(!upwd.equals(passwordmd5)) {
            return ResponseFactory.error(StateCodeEnum.MESSAGE_10001); //密码错误
        }
        List<MerchantInfoModel> result = null;
        if (DERP_SYS.USERINFO_ACCOUNTTYPE_1.equals(loginUser.getAccountType())) {
            UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
            userMerchantRelModel.setUserId(loginUser.getId());
            result = merchantInfoService.getUserMerchantList(userMerchantRelModel);
            if (result == null) {
                return ResponseFactory.error(StateCodeEnum.ERROR_302);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(loginUser.getAccountType())) {
            return ResponseFactory.error(StateCodeEnum.ERROR_304);
        }
        //如果用户是后台管理员或者只有绑定一个公司则直接登录
        if (DERP_SYS.USERINFO_ACCOUNTTYPE_0.equals(loginUser.getAccountType())
                || (result != null && result.size() == 1)) {
            UsernamePasswordToken upToken = new UsernamePasswordToken(username, passwordmd5);
            Subject subject = SecurityUtils.getSubject();
            String errorName = "";
            try{
                if(!subject.isAuthenticated()){//是否已经登录过
                    //upToken.setRememberMe(true);
                    subject.login(upToken);//验证角色和权限
                }
            }catch(AuthenticationException e) {
                errorName = e.getClass().getName();
            }
            System.out.println(errorName);
            if (UnknownAccountException.class.getName().equals(errorName)) {
                //用户名或密码不正确！
                return ResponseFactory.error(StateCodeEnum.ERROR_308);
            }else if(IncorrectCredentialsException.class.getName().equals(errorName)){
                //密码不正确！
                return ResponseFactory.error(StateCodeEnum.MESSAGE_10001);
            }else if(LockedAccountException.class.getName().equals(errorName)){
                //用户已禁用";
                return ResponseFactory.error(StateCodeEnum.MESSAGE_10002);
            }
            if (result != null) {
                MerchantInfoModel merchantInfoModel = merchantInfoService.searchDetailModel(Long.valueOf(result.get(0).getId()));
                ShiroUtils.setUser(merchantInfoModel);
            }
            //记住密码
            if(remember.equals("true")) {
                Base64 base64 = new Base64();
                Cookie usernameCk = new Cookie("username", base64.encodeToString(username.getBytes()));
                usernameCk.setMaxAge(7 * 24 * 60 * 60);//设置cookie最长保存时间7天
                String str = new String(password.getBytes(), "UTF-8");
                Cookie passwordCk = new Cookie("password", base64.encodeToString(password.getBytes()));
                passwordCk.setMaxAge(7 * 24 * 60 * 60);
                //覆盖旧的cookie
                response.addCookie(usernameCk);
                response.addCookie(passwordCk);
            } else {
                Cookie[] cookies = request.getCookies();
                Cookie usernameCk = null;
                Cookie passwordCk = null;
                //寻找是否已经存在cookie
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("username")) {
                        usernameCk = cookie;
                    } else if (cookie.getName().equals("password")) {
                        passwordCk = cookie;
                    }
                }
                //若cookie存在则通过设置cookie保存时间为0的方法来删除cookie
                if (usernameCk != null) {
                    usernameCk.setMaxAge(0);
                    response.addCookie(usernameCk);
                }
                if (passwordCk != null) {
                    passwordCk.setMaxAge(0);
                    response.addCookie(passwordCk);
                }
            }
            return ResponseFactory.success();
        }
        if (result == null || result.size() == 0) {
            return ResponseFactory.error(StateCodeEnum.ERROR_317);
        }
        //登录成功
        return ResponseFactory.success(result);
    }  
    
    /** 
     * 退出 
     * @return 
     */  
    /*@RequestMapping(value = "/logout.asyn")
    public String logout() {  
    	ShiroUtils.logout(); 
        return "login";    
    }  */
   
    /** 
     * 测试页面
     * @return 
     */  
    @RequestMapping(value = "/testsuccess.html")    
    public String testsuccess(HttpServletRequest request) {    
        Subject subject = SecurityUtils.getSubject();    
        
        request.setAttribute("shiroseId", subject.getSession().getId());
        System.out.println(subject.getSession().getId());
        return "testsuccess";    
    }

    /**
     * 选择登陆公司
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/submitUser.asyn")
    @ResponseBody
    public ViewResponseBean submitUser(HttpServletRequest request,HttpServletResponse response) throws Exception {
        System.out.println("-----------login start-----------------");
        List<SelectBean> result = null;
        String username = request.getParameter("username");// 取得用户名
        String password = request.getParameter("password");//取得 密码，并用MD5加密
        String remember = request.getParameter("remember");//记住账号密码
        String merchantId = request.getParameter("merchantId");//绑定公司
        String passwordmd5 = MD5Utils.encode(password);
       try {
           //判断用户是否绑定该公司
           UserInfoDTO loginUser = userInfoService.searchByUsername(username);
           UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
           userMerchantRelModel.setUserId(loginUser.getId());
           result = merchantInfoService.getUserSelectBean(userMerchantRelModel);
           boolean flag = true;
           for (SelectBean selectBean : result) {
               if (selectBean.getValue().equals(merchantId)) {
                   flag = false;
                   break;
               }
           }
           if (flag) {
               return ResponseFactory.error(StateCodeEnum.ERROR_316);
           }
           MerchantInfoModel merchantInfoModel = merchantInfoService.searchDetailModel(Long.valueOf(merchantId));
           UsernamePasswordToken upToken = new UsernamePasswordToken(username, passwordmd5);
           Subject subject = SecurityUtils.getSubject();
           String errorName = "";
           try{
               if(!subject.isAuthenticated()){//是否已经登录过
                   //upToken.setRememberMe(true);
                   subject.login(upToken);//验证角色和权限
               }
           }catch(AuthenticationException e) {
               errorName = e.getClass().getName();
           }
           System.out.println(errorName);
           if (UnknownAccountException.class.getName().equals(errorName)) {
               //用户名或密码不正确！
               return ResponseFactory.error(StateCodeEnum.ERROR_308);
           }else if(IncorrectCredentialsException.class.getName().equals(errorName)){
               //密码不正确！
               return ResponseFactory.error(StateCodeEnum.MESSAGE_10001);
           }else if(LockedAccountException.class.getName().equals(errorName)){
               //用户已禁用";
               return ResponseFactory.error(StateCodeEnum.MESSAGE_10002);
           }

           ShiroUtils.setUser(merchantInfoModel);
       } catch (Exception e) {
           e.printStackTrace();
           return ResponseFactory.error(StateCodeEnum.ERROR_305,e);
       }
        //记住密码
        if(remember.equals("true")) {
            Base64 base64 = new Base64();
            Cookie usernameCk = new Cookie("username", base64.encodeToString(username.getBytes()));
            usernameCk.setMaxAge(7 * 24 * 60 * 60);//设置cookie最长保存时间7天
            String str = new String(password.getBytes(), "UTF-8");
            Cookie passwordCk = new Cookie("password", base64.encodeToString(password.getBytes()));
            passwordCk.setMaxAge(7 * 24 * 60 * 60);
            //覆盖旧的cookie
            response.addCookie(usernameCk);
            response.addCookie(passwordCk);
        } else {
            Cookie[] cookies = request.getCookies();
            Cookie usernameCk = null;
            Cookie passwordCk = null;
            //寻找是否已经存在cookie
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    usernameCk = cookie;
                } else if (cookie.getName().equals("password")) {
                    passwordCk = cookie;
                }
            }
            //若cookie存在则通过设置cookie保存时间为0的方法来删除cookie
            if (usernameCk != null) {
                usernameCk.setMaxAge(0);
                response.addCookie(usernameCk);
            }
            if (passwordCk != null) {
                passwordCk.setMaxAge(0);
                response.addCookie(passwordCk);
            }
        }
        return ResponseFactory.success();

    }

    /**
     * 切换登陆公司
     * @param request
     * @return
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/updateLoginMerchant.asyn")
    @ResponseBody
    public ViewResponseBean updateLoginMerchant(HttpServletRequest request,HttpServletResponse response) throws Exception {
        try {
            String merchantId = request.getParameter("merchantId");//绑定公司
            User user = ShiroUtils.getUser();
            UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
            userMerchantRelModel.setUserId(user.getId());
            List<SelectBean> merchantSelectBeans = merchantInfoService.getUserSelectBean(userMerchantRelModel);
            boolean flag = true;
            for (SelectBean selectBean : merchantSelectBeans) {
                if (selectBean.getValue().equals(merchantId)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                return ResponseFactory.error(StateCodeEnum.ERROR_316);
            }
            MerchantInfoModel merchantInfoModel = merchantInfoService.searchDetailModel(Long.valueOf(merchantId));
            ShiroUtils.setUser(merchantInfoModel);
            return ResponseFactory.success();
        } catch (Exception e) {
            return ResponseFactory.error(StateCodeEnum.ERROR_302, e);
        }
    }

}
