package com.topideal.webapi.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.bean.SelectBean;
import com.topideal.common.system.web.ResponseFactory;
import com.topideal.common.system.web.StateCodeEnum;
import com.topideal.common.system.web.ViewResponseBean;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.MD5Utils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.dto.main.LoginResponseDTO;
import com.topideal.entity.dto.main.MerchantMinDTO;
import com.topideal.entity.dto.user.UserInfoDTO;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.user.UserInfoService;
import com.topideal.shiro.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**webapi
 * 登录相关
 */
@RestController
@RequestMapping("/webapi/system/login")
@Api(tags = "登录模块")
public class APILoginController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MerchantInfoService merchantInfoService;


    /** 
     * 验证用户名和密码
     */
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam( name = "password", value = "密码", required = true),
            @ApiImplicitParam( name = "remember", value = "记住账号密码 true-是 其他-否")
    })
    @PostMapping(value="/login.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<LoginResponseDTO> login(String username, String password, String remember,
                              HttpServletRequest request, HttpServletResponse response)  {
    	System.out.println("-----------login start-----------------");
    	try{
            LoginResponseDTO loginRespontDTO = new LoginResponseDTO();

            String passwordmd5 = MD5Utils.encode(password);
            //校验用户
            UserInfoDTO loginUser = userInfoService.searchByUsername(username);
            if(loginUser == null) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10001);//账号不存在
            }
            if(loginUser.getDisable()!=null&&loginUser.getDisable().equals("1")) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10002);//帐号已禁用
            }
            String upwd = loginUser.getPassword();
            if(!upwd.equals(passwordmd5)) {
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10003); //密码错误
            }
            List<MerchantInfoModel> result = null;
            if (DERP_SYS.USERINFO_ACCOUNTTYPE_1.equals(loginUser.getAccountType())) {
                UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
                userMerchantRelModel.setUserId(loginUser.getId());
                result = merchantInfoService.getUserMerchantList(userMerchantRelModel);
                if (result == null) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10004);//用户未关联公司
                }
            }

            //如果用户是后台管理员或者只有绑定一个公司则直接登录
            if(DERP_SYS.USERINFO_ACCOUNTTYPE_0.equals(loginUser.getAccountType()) || (result != null && result.size() == 1)) {
                UsernamePasswordToken upToken = new UsernamePasswordToken(username, passwordmd5);
                Subject subject = SecurityUtils.getSubject();
                String errorName = "";
                try{
                    //if(!subject.isAuthenticated()){//是否已经登录过
                        //upToken.setRememberMe(true);
                        subject.login(upToken);//验证角色和权限
                    //}
                }catch(AuthenticationException e) {
                    return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
                }
                String token = saveToken(request);

                if(result!=null&&result.size()==1) {
                    //查询公司信息保存到登录信息
                    MerchantInfoModel merchantInfoModel = merchantInfoService.searchDetailModel(Long.valueOf(result.get(0).getId()));
                    ShiroUtils.setUser1(merchantInfoModel,token);
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
                    if(cookies!=null){
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals("username")) {
                                usernameCk = cookie;
                            } else if (cookie.getName().equals("password")) {
                                passwordCk = cookie;
                            }
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
                //保存令牌
//                String token = saveToken(request);
                loginRespontDTO.setToken(token);
                loginRespontDTO.setMerchantList(new ArrayList<>());
                return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,loginRespontDTO);//登录成功
            }

            //用户关联了多个公司返回公司列表
            List<MerchantMinDTO> merchantList = new ArrayList<>();
            for(MerchantInfoModel merchant : result){
                MerchantMinDTO dto = new MerchantMinDTO();
                BeanUtils.copyProperties(merchant, dto);
                merchantList.add(dto);
            }

            loginRespontDTO.setMerchantList(merchantList);
            String ip = getIpAddr(request);
            
            // 修改用户登入ip
            UserInfoModel userInfo=new UserInfoModel();
            userInfo.setLastLoginIp(ip);
            userInfo.setLastLoginDate(TimeUtils.getNow());
            userInfo.setId(loginUser.getId());
            userInfo.setModifyDate(TimeUtils.getNow());
            userInfoService.modifyUserInfo(userInfo);
            
            //校验通过后若返回多个公司信息，则需要选择公司后再登录
            return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10007,merchantList);
        } catch (Exception ed) {
    	    ed.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999, ed);//未知异常
        }
    }
    
    /**
     * 获取登入ip
     * @param request
     * @return
     */
    public  String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
    /**
     * 选择登录公司(对接单点后此接口无用)
     * @param request
     * @throws IOException
     * @throws ServletException
     */
    @ApiOperation(value = "选择公司后登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam( name = "password", value = "密码", required = true),
            @ApiImplicitParam( name = "merchantId", value = "公司Id", required = true),
            @ApiImplicitParam( name = "remember", value = "记住账号密码 true-是 其他-否")
    })
    @PostMapping(value="/submitUser.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ApiIgnore
    public ResponseBean<String> submitUser(String username, String password, String remember, String merchantId,
                                                     @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) {
        System.out.println("-----------login start-----------------");
        String token = "";
        String passwordmd5 = MD5Utils.encode(password);
        try {
                //判断用户是否绑定该公司
                UserInfoDTO loginUser = userInfoService.searchByUsername(username);
                UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
                userMerchantRelModel.setUserId(loginUser.getId());
                List<SelectBean> result = merchantInfoService.getUserSelectBean(userMerchantRelModel);
                boolean flag = true;
                for (SelectBean selectBean : result) {
                    if (selectBean.getValue().equals(merchantId)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10005);//登录用户未绑定本公司
                }
                MerchantInfoModel merchantInfoModel = merchantInfoService.searchDetailModel(Long.valueOf(merchantId));
                UsernamePasswordToken upToken = new UsernamePasswordToken(username, passwordmd5);
                Subject subject = SecurityUtils.getSubject();
                String errorName = "";
                try{
                    //if(!subject.isAuthenticated()){//是否已经登录过
                        //upToken.setRememberMe(true);
                        subject.login(upToken);//验证角色和权限
                   // }
                }catch(AuthenticationException e) {
                    errorName = e.getClass().getName();
                }
                System.out.println(errorName);
                if (UnknownAccountException.class.getName().equals(errorName)) {
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10006); //用户名或密码不正确
                }else if(IncorrectCredentialsException.class.getName().equals(errorName)){
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10003);  //密码不正确
                }else if(LockedAccountException.class.getName().equals(errorName)){
                    return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10002); //用户已禁用";
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
                    if(cookies!=null) {
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals("username")) {
                                usernameCk = cookie;
                            } else if (cookie.getName().equals("password")) {
                                passwordCk = cookie;
                            }
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
                token = saveToken(request);
                ShiroUtils.setUser1(merchantInfoModel,token);
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }

        return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,token);//登录成功
    }

    
    
    
    @ApiIgnore
    public String saveToken(HttpServletRequest request){
        //保存登录令牌到redis
        //String token = request.getSession().getId();
        String token = UUID.randomUUID().toString();
        User user= ShiroUtils.getUser();
        ShiroUtils.saveToken(token,user);
        return token;
    }
    /**
     * 切换登录公司
     */
    @ApiOperation(value = "切换登录公司")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
            @ApiImplicitParam(name = "merchantId", value = "公司Id", required = true)
    })
    @PostMapping(value="/updateLoginMerchant.asyn",consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseBean<String> updateLoginMerchant(String token,String merchantId,HttpServletRequest request) {
        try {
            User user = ShiroUtils.getUserByToken(token);
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
                return WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10005);//登录用户未绑定本公司
            }
            //更换令牌、公司主体
            MerchantInfoModel merchantInfoModel = merchantInfoService.searchDetailModel(Long.valueOf(merchantId));
            user.setMerchantId(merchantInfoModel.getId());
            user.setMerchantCode(merchantInfoModel.getCode());
            user.setMerchantName(merchantInfoModel.getName());
            user.setTopidealCode(merchantInfoModel.getTopidealCode());
            token = UUID.randomUUID().toString();
            request.getSession().setAttribute(DERP.SESSION_TOKEN,token);
            ShiroUtils.saveToken(token,user);
            return WebResponseFactory.responseBuild(MessageEnum.SUCCESS_10000,token);//成功
        } catch (Exception e) {
            e.printStackTrace();
            return WebResponseFactory.responseBuild(MessageEnum.ERROR_99999,e);//未知异常
        }
    }

    /**
     * 校验令牌是否失效
     * @return
     */
    @PostMapping("/validate.asyn")
    @ApiIgnore
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

}
