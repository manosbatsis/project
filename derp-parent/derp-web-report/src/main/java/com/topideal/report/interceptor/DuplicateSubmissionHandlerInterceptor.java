package com.topideal.report.interceptor;

import com.topideal.common.system.redis.RedisUtil;
import com.topideal.common.system.webapi.MessageEnum;
import com.topideal.common.system.webapi.ResponseBean;
import com.topideal.common.system.webapi.WebResponseFactory;
import com.topideal.common.tools.GetRequestJsonUtils;
import com.topideal.common.tools.MD5Utils;
import com.topideal.common.tools.wrapper.DerpHttpServletRequestWrapper;

import net.sf.json.JSONObject;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 重复提交拦截器
 */
public class DuplicateSubmissionHandlerInterceptor implements HandlerInterceptor {

    protected Logger LOGGER = LoggerFactory.getLogger(DuplicateSubmissionHandlerInterceptor.class);

    @Autowired
    private RedisUtil redisUtil;
    
    private static final String CHECK_METHORDS = "insert|save|add|del|update|modify|batch|confirm|audit|examine|create|generate" ;

    @SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
    	
    	String type = request.getMethod();
    	
    	/**避免跨域时，content-type:application/json时会转换成 OPTIONS请求方式，试探服务端允许的get post提交*/
    	if (type.toUpperCase().equals("OPTIONS")==true) {
    		return true ;
    	}
    	
    	
    	String url = request.getRequestURI();
    	
        //不拦截登录方法
        List<String> loginUrlList = new ArrayList<>();
        loginUrlList.add("/webapi/system/login/login.asyn");
        loginUrlList.add("/webapi/system/login/submitUser.asyn");
        
        if(loginUrlList.contains(url)){
            return true;
        }
        
        /**判断是否幂等方法，若查询，无需校验*/
        String methodName = url.substring(url.lastIndexOf("/") + 1) ;
        
        Pattern p = Pattern.compile(CHECK_METHORDS);
		Matcher matcher = p.matcher(methodName);
		boolean matches = matcher.lookingAt();
        
        if(!matches) {
        	return true ;
        }
        
        String contentType = request.getContentType();
        
        String jsonStr = "" ;

        /**判断前端提交content-type*/
        if(contentType.contains("application/json")) {

            DerpHttpServletRequestWrapper wrapper = new DerpHttpServletRequestWrapper(request) ;
            
            com.alibaba.fastjson.JSONObject requestJsonObject = GetRequestJsonUtils.getRequestJsonObject(wrapper);
			requestJsonObject.put("url", url) ;
			jsonStr = requestJsonObject.toJSONString();
            
        	
        }else if(contentType.contains("application/x-www-form-urlencoded")) {
        	
        	Map<String, String[]> parameterMap = request.getParameterMap();
            
            JSONObject paraJson = JSONObject.fromObject(parameterMap);
            paraJson.remove("t") ;
            paraJson.put("url", url) ;
            jsonStr = paraJson.toString();
            
        }
        
        String redisCode = MD5Utils.encode(jsonStr);

        if(redisUtil.hasKey(redisCode)) {
        	ResponseBean<String> responseBean = WebResponseFactory.responseBuild(MessageEnum.MESSAGE_10017, "请勿重复提交");
        	
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            
            PrintWriter writer = response.getWriter();
            writer.write(JSONObject.fromObject(responseBean).toString());
            
            writer.flush();
            writer.close();
            
            return false;
        }else {
        	redisUtil.set(redisCode, redisCode, 5) ;
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
    	
    }

}
