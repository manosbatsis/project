package com.topideal.filter;
/**已废弃*/
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.DPSign;
import com.topideal.common.tools.XmlConverUtil;
import com.topideal.mongo.dao.ApiExternalConfigMongoDao;
import com.topideal.mongo.dao.ApiSecretConfigMongoDao;
import com.topideal.mongo.entity.ApiExternalConfigMongo;
import com.topideal.mongo.entity.ApiSecretConfigMongo;
import com.topideal.system.SystemLog;
import com.topideal.tools.QimenSignUtils;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONObject;
import reactor.core.publisher.Mono;

@Component
public class RequestUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);
	
	@Autowired
	private  ApiSecretConfigMongoDao apiSecretConfigMongoDao;
	@Autowired
	private  ApiExternalConfigMongoDao apiExternalConfigMongoDao;
	
	@Autowired
	private SystemLog systemLog;
	
	public Mono verifySignForJson(ServerWebExchange exchange,String body) {
		ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders()); 
		String path = request.path();
		if(path.startsWith("/api/")) {
			return signVerifyKJB(exchange, body);
		}else if(path.startsWith("/report/api")){
			return signVerify(exchange, body);
		}else if(path.startsWith("/qimen")) {
			return signVerifyQM(exchange, body) ;
		}
		return Mono.just(body);
	}
	
	/**
	 * 验签-奇门
	 * @param exchange
	 * @param body
	 * @return
	 */
    public Mono signVerifyQM(ServerWebExchange exchange, String body) {
    	boolean signTag = true;
		try {
			ServerHttpRequest request = exchange.getRequest();
			
			String data = URLDecoder.decode(body, "UTF-8");
			
			String xml = null ;
			if (data.indexOf("<") >= 0) {
	            xml = data.substring(data.indexOf("<"), data.lastIndexOf(">") + 1);
	        }
			
			MultiValueMap<String, String> queryParams = request.getQueryParams();
			Map<String, String> map = queryParams.toSingleValueMap() ;
			
			String signTemp = map.remove("sign");
			
			String sign = QimenSignUtils.sign(map, xml, ApolloUtil.ywmsSecret);
			
			if(!sign.equals(signTemp)) {
				signTag = false ;
			}
			
		}catch (Exception e) {
			signTag = false;
			e.printStackTrace();
		}
		if(signTag!=true) {
			return errorQM(exchange,body,"验签失败");
		}
    	return Mono.just(body);
	}
	/**
     * 验签-跨境宝
     * */
    public Mono signVerifyKJB(ServerWebExchange exchange,String body) {
    	boolean signTag = true;
    	JSONObject jsonData=new JSONObject();
		try {
			jsonData = JSONObject.fromObject(body);
			String appkey = (String) jsonData.get("app_key");
			if(StringUtils.isBlank(appkey)){
				return errorKJB(exchange,body,"app_key为空");
			} 
			//获取秘钥
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("appKey", appkey);
			ApiSecretConfigMongo apiSecretConfigMongo = apiSecretConfigMongoDao.findOne(paramMap);
			if(apiSecretConfigMongo==null) {
				return errorKJB(exchange, body,"秘钥不存在");
			}
			String appSecret = apiSecretConfigMongo.getAppSecret();
			signTag = SignUtil.verifyForOuter(body, appSecret);
		}catch (Exception e) {
			signTag = false;
			e.printStackTrace();
		}
		if(signTag!=true) {
			return errorKJB(exchange,body,"验签失败");
		}
    	return Mono.just(body);
    }
    /**
     * 验签-其他
     * */
    public  Mono signVerify(ServerWebExchange exchange,String body) {
    	boolean signTag = true;
    	JSONObject jsonData=new JSONObject();
		try {
			jsonData = jsonData.fromObject(body);
			String appKey = (String)jsonData.get("appKey");//商家appkey
			if(StringUtils.isBlank(appKey)) {
				return error(exchange, body,"appKey为空");
			}
			//查询商家秘钥
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("appKey", appKey);
			paramMap.put("status", DERP_SYS.APIEXTERNALCONFIG_STATUS_1);//状态状态(1使用中,0已禁用)
			ApiExternalConfigMongo configModel = apiExternalConfigMongoDao.findOne(paramMap);
			if(configModel==null){
				return error(exchange, body,"秘钥不存在");
			}
			
			String sign = (String)jsonData.get("sign");
			signTag = DPSign.signVerify(sign, jsonData, configModel.getAppSecret(),"sign");
		}catch (Exception e) {
			signTag = false;
			e.printStackTrace();
		}
		
		if(signTag!=true) {
			return error(exchange,body,"验签失败");
		}
    	return Mono.just(body);
    }
    
    /**
     * 网关拦截
     * 1、中止路由
     * 2、返回错误信息
     * @param responseCodeEnum
     */
    public Mono<Void> errorQM(ServerWebExchange exchange,String body,String msg){
    	ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders()); 
    	JSONObject bodyJson = new JSONObject() ;
		bodyJson.put("flag", "failure") ;
		bodyJson.put("code", 400) ;
		bodyJson.put("message", msg) ;
		
		JSONObject responseJson = new JSONObject() ;
		responseJson.put("response", bodyJson) ;
		
		String xml = XmlConverUtil.JsonToXml(responseJson.toString());
    	
    	//发送日志
    	systemLog.sendLog(request, body, msg, responseJson.toString());
    	
        byte[] bits = xml.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bits);
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        //指定编码，否则在浏览器中会中文乱码
        exchange.getResponse().getHeaders().add("Content-Type", "text/xml;charset=UTF-8");
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    
    /**
     * 网关拦截
     * 1、中止路由
     * 2、返回错误信息
     * @param responseCodeEnum
     */
    public Mono<Void> errorKJB(ServerWebExchange exchange,String body,String msg){
    	ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders()); 
    	JSONObject jsonData=new JSONObject();
    	jsonData.put("status", "2");//1-提交成功 2-提交失败
    	jsonData.put("notes", msg);
    	
    	//发送日志
    	systemLog.sendLog(request, body,msg,jsonData.toString());
    	
        byte[] bits = jsonData.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bits);
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        //指定编码，否则在浏览器中会中文乱码
        exchange.getResponse().getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    /**
     * 网关拦截
     * 1、中止路由
     * 2、返回错误信息
     * @param responseCodeEnum
     */
    public Mono<Void> error(ServerWebExchange exchange,String body,String msg){
    	ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders()); 
    	JSONObject jsonData=new JSONObject();
    	jsonData.put("mCode", "9999");//0000-成功 9999-失败
    	jsonData.put("message", msg);
    	
    	//发送日志
    	systemLog.sendLog(request, body,msg,jsonData.toString());
    	
        byte[] bits = jsonData.toString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bits);
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        exchange.getResponse().getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
