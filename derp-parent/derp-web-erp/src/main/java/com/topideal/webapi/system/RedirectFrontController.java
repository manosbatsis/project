package com.topideal.webapi.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topideal.common.constant.DERP;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.redis.RedisUtil;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.http.HttpClientUtil;
import com.topideal.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

/**
 * 生成令牌重定向到前端首页
 * */
@Controller
@RequestMapping(value="/")
@Api(tags = "登录模块")
public class RedirectFrontController {
    private static final Logger logger = Logger.getLogger(RedirectFrontController.class) ;

    @Autowired
    private RedisUtil redisUtil;
    /**
     *重定向到前端
     */
    @RequestMapping({"/","/index.html"})
    @ApiIgnore
    public String redirectFront(HttpSession session,HttpServletRequest request, HttpServletResponse response) {
        String url = "";
        try {
            //redis 获取登录用户信息
            //String token = session.getId();
            String token = (String) session.getAttribute(DERP.SESSION_TOKEN);
            String userJson = (String)redisUtil.get(token);
            if(StringUtils.isBlank(userJson)){
                logout(token,request, response);
            }
            //更换令牌
            token = UUID.randomUUID().toString();
            ObjectMapper objectMapper = new ObjectMapper();
            User user = objectMapper.readValue(userJson, User.class);
            ShiroUtils.saveToken(token,user);
            request.getSession().setAttribute(DERP.SESSION_TOKEN,token);
            url =  ApolloUtil.frontWebhost+"?token="+token;
            logger.info("url=" + url);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:"+url;
    }

    /**
     * 退出
     * @return
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true)
    })
    @GetMapping(value="/logout")
    public String logout(String token,HttpServletRequest request, HttpServletResponse response) {
        //清空单点cookie
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length>0){
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        request.getSession().invalidate();//清空session
        ShiroUtils.logout(token);//shiro
        String idmUrl = ApolloUtil.serverUrlPrefix+"/logout";
        return "redirect:"+idmUrl;
    }
}
