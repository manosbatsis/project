package com.topideal.api.ofc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.api.ofc.c01.OFCCurrentStoreRequest;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;

import net.sf.json.JSONObject;

/**
 * OFC海外仓接口调用类
 * @author 杨创
 *2018/6/25
 */
public class OFCUtils {
	/**
	 * 打印日志
	 */
	private static final Logger LOGGER= LoggerFactory.getLogger(OFCUtils.class);
	
	
	/**
	 * OFC实时库存查询
	 * @param model 
	 * @return
	 */
	public static String currentStoreQuery(OFCCurrentStoreRequest model){
		// 实体类转json
		JSONObject jSONObject = JSONObject.fromObject(model);		
		LOGGER.info("OFC实时库存查询请求", jSONObject);
		String json = jSONObject.toString();
		System.out.println("====ofc==="+json);
		// 实时库存查询用的是正式库的地址		
		String doPost = HttpClientUtil.doPost(ApolloUtil.ofcC29, json, "utf-8");		
		LOGGER.info("OFC实时库存查询响应"+doPost);
		return doPost;		
	}

}
