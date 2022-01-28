package com.topideal.storage.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.redis.RedisUtil;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.storage.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器
 */
public class WebApiTokenHandlerInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        /**start因本拦截器的顺序排在webmvc配置的允许跨域前面，所以这里需要添加允许跨域，否则会出现跨域异常*/
        response.setCharacterEncoding("UTF-8");//设置编码格式
        response.setContentType("application/json;charset=UTF-8");
        String originalURL = request.getHeader("Origin");
        if (originalURL != null) {
            logger.info(" Origin=", request.getHeader("Origin"));
            response.addHeader("Access-Control-Allow-Origin", originalURL);
        }
        response.addHeader("Access-Control-Allow-Credentials", "true");
        /**end*/

        String url = request.getRequestURI();
        //不拦截登录方法
        List<String> loginUrlList = new ArrayList<>();
        loginUrlList.add("/webapi/system/login/login.asyn");
        loginUrlList.add("/webapi/system/login/submitUser.asyn");
        if(loginUrlList.contains(url)){
            return true;
        }
        String token=request.getParameter("token");
        if(StringUtils.isBlank(token)){
            ResponseBean responseBean = WebResponseFactory.responseBuild(MessageEnum.LOGINTIMEOUT_99998);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.fromObject(responseBean).toString());
            writer.flush();
            writer.close();
            return false;
        }
        //redis获取到用户信息
        String userJson = (String) redisUtil.get(token);
        if(StringUtils.isBlank(userJson)){
            ResponseBean responseBean = WebResponseFactory.responseBuild(MessageEnum.LOGINTIMEOUT_99998);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.fromObject(responseBean).toString());
            writer.flush();
            writer.close();
            return false;
        }

        //刷新令牌
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class);
        ShiroUtils.saveToken(token,user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
