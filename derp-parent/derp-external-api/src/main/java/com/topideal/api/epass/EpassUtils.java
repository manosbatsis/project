package com.topideal.api.epass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.api.epass.e03.SalesOutStoreOrderRequest;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.PropertiesUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.http.HttpClientUtil;
import com.topideal.http.HttpOPUtil;
import com.topideal.tools.EpassSignUtils;

import net.sf.json.JSONObject;

/**
 * 跨境宝接口工具类
 * @author zhanghx
 */
public class EpassUtils {
	
	
	private static final Logger LOGGER= LoggerFactory.getLogger(EpassUtils.class);


	public static void setBaseInfo(JSONObject jsonObject) {
		jsonObject.put("v","1.0");
		jsonObject.put("app_key", ApolloUtil.epassAppKey);
		jsonObject.put("timestamp", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		String json=jsonObject.toString();
		try {
			 json= URLDecoder.decode(json,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sign = EpassSignUtils.signForOuter(json, ApolloUtil.epassAppSecret);
		jsonObject.put("sign", sign);
	}
	
	/**
	 * 采购订单接口
	 * @param jsonObject
	 * @return
	 */
	public static String purchase(JSONObject jsonObject){
		jsonObject.put("method",ApolloUtil.epass001Method);
		// 设置头部信息
        setBaseInfo(jsonObject);
		LOGGER.info("采购订单请求", jsonObject);
		System.out.println("请求报文"+jsonObject);
		// 获取sign
		String result = HttpOPUtil.doPost(ApolloUtil.epassApi, jsonObject.toString(), "utf-8");
		LOGGER.info("采购订单响应"+result);
		return result;		
	}
	
	/**
	 * 理货确认接口
	 * @param jsonObject
	 * @return
	 */
	public static String tallyConfirm(JSONObject jsonObject){
		jsonObject.put("method",ApolloUtil.epass002Method);
		// 设置头部信息
		setBaseInfo(jsonObject);
		// 设置头部信息
		LOGGER.info("理货确认请求", jsonObject);
		String result = HttpOPUtil.doPost(ApolloUtil.epassApi, jsonObject.toString(), "utf-8");
		LOGGER.info("理货确认响应"+result);
		return result;		
	}

	/**
	 * 销售出仓订单(2B销售出库接口)
	 * @param jsonObject
	 * @return
	 */
	public static String salesOutStoreOrder(SalesOutStoreOrderRequest request){
		JSONObject jsonObject = JSONObject.fromObject(request);
		jsonObject.put("method", ApolloUtil.epass003Method);
		//设置头部信息
		setBaseInfo(jsonObject);
		LOGGER.info("销售出仓订单请求", jsonObject);
		String result = HttpOPUtil.doPost(ApolloUtil.epassApi, jsonObject.toString(), "utf-8");
		LOGGER.info("销售出仓订单响应"+result);		
		return result;
	}
	/**
	 * 调拨指令推送
	 * @param TransferOrderRequest
	 * @return
	 */
	public static String transferOrder(JSONObject jsonObject){
		//请求方法
		jsonObject.put("method", ApolloUtil.epass004Method);
		// 设置头部信息
		setBaseInfo(jsonObject);
		LOGGER.info("调拨指令推送请求", jsonObject);
		String result = HttpOPUtil.doPost(ApolloUtil.epassApi, jsonObject.toString(), "utf-8");
		LOGGER.info("调拨指令推送响应"+result);
		return result; 
	}
	/**
	 * 查询订单100
	 * @return
	 * 
	 */
	public static String getGrabEpassOrder100(JSONObject jsonObject){
				
		String result = HttpClientUtil.doPost(PropertiesUtil.getValue("epass.order100"), jsonObject.toString(), "utf-8");
		
		return result;
		
	}
}
