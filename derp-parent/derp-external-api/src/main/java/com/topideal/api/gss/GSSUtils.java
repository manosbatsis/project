package com.topideal.api.gss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.api.gss.g001.HisStockQueryRequest;
import com.topideal.api.gss.g002.ReadBatchStocksRequest;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;

import net.sf.json.JSONObject;

public class GSSUtils {
	/**
	 * 打印日志
	 */
	private static final Logger LOGGER= LoggerFactory.getLogger(GSSUtils.class);
	/**
	 * GSS菜鸟仓实时库存查询
	 * @param model 
	 * @return
	 */
	public static String currentStoreQuery(HisStockQueryRequest model){
		// 实体类转json
		JSONObject jSONObject = JSONObject.fromObject(model);		
		LOGGER.info("GSS菜鸟仓实时库存查询请求", jSONObject);
		String json = jSONObject.toString();
		String doPost = HttpClientUtil.doPost(ApolloUtil.gssG2Url, json, "utf-8");		
		LOGGER.info("GSS菜鸟仓实时库存查询响应"+doPost);
		return doPost;		
	}
	/**
	 * GSS批次库存查询
	 * @param model 
	 * @return
	 */
	public static String currentStoreQueryBatch(ReadBatchStocksRequest model){
		// 实体类转json
		JSONObject jSONObject = JSONObject.fromObject(model);	
		String json = jSONObject.toString();
		LOGGER.info("GSS批次库存请求地址:"+ApolloUtil.gssG2batchUrl);
		LOGGER.info("GSS批次库存请求参数:"+json);
		String doPost = HttpClientUtil.doPost(ApolloUtil.gssG2batchUrl, json, "utf-8");		
		LOGGER.info("GSS批次库存查询请求响应"+doPost);
		return doPost;		
	}
}
