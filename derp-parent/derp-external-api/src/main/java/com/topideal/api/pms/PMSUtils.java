package com.topideal.api.pms;

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
 * PMS接口工具类
 * @author zhanghx
 */
public class PMSUtils {
	
	
	private static final Logger LOGGER= LoggerFactory.getLogger(PMSUtils.class);
    //PMS接口地址
    private static final String PMS_URL=ApolloUtil.pmsUrl;


	/**
	 * 商品信息查询接口
	 * @param jsonObject
	 * @return
	 */
	public static String getGoods(JSONObject jsonObject){

		LOGGER.info("商品信息查询接口请求", jsonObject);
		// 获取sign
		String result = HttpClientUtil.doPost(PMS_URL, jsonObject.toString(), "utf-8");
		LOGGER.info("商品信息查询接口响应"+result);
		return result;		
	}
	
	
}
