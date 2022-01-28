package com.topideal.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.topideal.system.SystemLog;

import net.sf.json.JSONObject;


/**
 * 自定义异常处理
 * */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler{
	
	@Autowired
	private SystemLog systemLog;

	public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
			ErrorProperties errorProperties, ApplicationContext applicationContext) {
		super(errorAttributes, resourceProperties, errorProperties, applicationContext);
	}
	/**
	 * 获取异常属性
	 */
	@Override
	protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
		 Map<String,Object> response=super.getErrorAttributes(request,includeStackTrace);
		
		Throwable error = super.getError(request);
		String notes = error.getMessage();
		if(StringUtils.isEmpty(notes)) {
			notes = error.toString();
		}
		Map<String, Object> retMap = response(request,notes);
		JSONObject jsonData=new JSONObject();
		jsonData = jsonData.fromObject(retMap);
		//记录异常日志
		systemLog.sendLog(request,"{}",notes,jsonData.toString());
		return retMap;
	}
	/**
	 * 构建返回的JSON数据格式
	 * @param status		状态码
	 * @param errorMessage  异常信息
	 * @return
	 */
	public static Map<String, Object> response(ServerRequest request, String notes) {
		String path = request.path();
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(path.startsWith("/api/")) {
			map.put("status","2");//1-提交成功 2-提交失败
			map.put("notes", notes);//返回消息
		}else if(path.startsWith("/report/api")){
			map.put("mCode", "9999");//返回码
			map.put("message", notes);//返回消息
		}
		return map;
	}

	/**
	 * 指定响应处理方法为JSON处理的方法
	 * @param errorAttributes
	 */
	@Override
	protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
		/*
		 * return RouterFunctions.route(acceptsTextHtml(), this::renderErrorView)
		 * .andRoute(RequestPredicates.all(), this::renderErrorResponse);
		 */
		return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
	}

	/**
	 * 根据code获取对应的HttpStatus
	 * @param errorAttributes
	 */
	@Override
	protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
		 //int statusCode = (int) errorAttributes.get("code");
		return HttpStatus.valueOf(500);
	}
	

}
