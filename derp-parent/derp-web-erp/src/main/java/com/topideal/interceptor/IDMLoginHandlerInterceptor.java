package com.topideal.interceptor;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.MD5Utils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.user.UserInfoModel;
import com.topideal.entity.vo.user.UserMerchantRelModel;
import com.topideal.service.main.MerchantInfoService;
import com.topideal.service.user.UserInfoService;
import com.topideal.shiro.ShiroUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

/**
 * IDM登录拦截器
 */
public class IDMLoginHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(IDMLoginHandlerInterceptor.class) ;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MerchantInfoService merchantInfoService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        /**start因本拦截器的顺序排在webmvc配置的允许跨域前面，所以这里需要添加允许跨域，否则会出现跨域异常*/

        logger.info("-----------IDM登录拦截器-----------------");
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8;");
            AttributePrincipal principal = null;
            try{
                 principal = (AttributePrincipal) request.getUserPrincipal();
            }catch (Exception e){}

            logger.info("principal = " + principal);
            if(principal == null) {
                return true;
            }
            logger.info("-----------初始化登录 start-----------------");
            String userCode = principal.getName();//工号

            //校验用户
            UserInfoModel userInfo = new UserInfoModel();
            userInfo.setCode(userCode);
            userInfo.setDisable(DERP_SYS.USERINFO_DISABLE_0);//启用
            List<UserInfoModel> userList = userInfoService.list(userInfo);
            if(userList == null||userList.size()<=0) {
                logger.info("账号不存在,工号:"+userCode);
                String js="<script language='JavaScript'>alert('账号不存在');window.location.href = '/logout';</script>";
                PrintWriter writer = response.getWriter();
                writer.write(js);
                writer.close();
                return false;
            }
            if(userList.size()>1) {
                logger.info("工号存在多个账号,工号:"+userCode);
                String js="<script language='JavaScript'>alert('工号存在多个账号');window.location.href = '/logout';</script>";
                PrintWriter writer = response.getWriter();
                writer.write(js);
                writer.close();
                return false;
            }
            UserInfoModel loginUser =userList.get(0);
            //查询用户绑定的公司
            List<MerchantInfoModel> merchantList = null;
            if (DERP_SYS.USERINFO_ACCOUNTTYPE_1.equals(loginUser.getAccountType())) {
                UserMerchantRelModel userMerchantRelModel = new UserMerchantRelModel();
                userMerchantRelModel.setUserId(loginUser.getId());
                merchantList = merchantInfoService.getUserMerchantList(userMerchantRelModel);
                if (merchantList == null||merchantList.size()<=0) {
                    String js="<script language='JavaScript'>alert('登录用户没有绑定公司');window.location.href='/logout';</script>";
                    PrintWriter writer = response.getWriter();
                    writer.write(js);
                    writer.close();
                    return false;
                }
            }

            //登录 不校验密码单点登录已验证过
            String passwordmd5 = MD5Utils.encode("123");
            UsernamePasswordToken upToken = new UsernamePasswordToken(loginUser.getUsername(), passwordmd5);
            Subject subject = SecurityUtils.getSubject();
            String errorName = "";
            try{
                subject.login(upToken);//验证角色和权限
            }catch(AuthenticationException e) {
                errorName = e.getClass().getName();
            }
            System.out.println(errorName);
            if (DERP_SYS.USERINFO_ACCOUNTTYPE_1.equals(loginUser.getAccountType())) {
                //默认选择第一个公司登录
                MerchantInfoModel merchantInfoModel = merchantInfoService.searchDetailModel(Long.valueOf(merchantList.get(0).getId()));
                ShiroUtils.setUser(merchantInfoModel);
            }
            //String token = request.getSession().getId();
            String token = UUID.randomUUID().toString();
            request.getSession().setAttribute(DERP.SESSION_TOKEN,token);
            User user= ShiroUtils.getUser();
            ShiroUtils.saveToken(token,user);
            logger.info("拦截器登录token:"+token);
            // 修改用户登入ip
            String ip = getIpAddr(request);
            UserInfoModel userModify=new UserInfoModel();
            userModify.setLastLoginIp(ip);
            userModify.setLastLoginDate(TimeUtils.getNow());
            userModify.setId(loginUser.getId());
            userModify.setModifyDate(TimeUtils.getNow());
            userInfoService.modifyUserInfo(userModify);
            System.out.println("-----------初始化登录 end-----------------");
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
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
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
