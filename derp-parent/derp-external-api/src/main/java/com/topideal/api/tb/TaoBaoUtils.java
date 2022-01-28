package com.topideal.api.tb;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.WlbWmsInventoryQueryRequest;
import com.taobao.api.response.WlbWmsInventoryQueryResponse;
import com.topideal.common.tools.ApolloUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaoBaoUtils {
	/**
	 * 打印日志
	 */
	private static final Logger LOGGER= LoggerFactory.getLogger(TaoBaoUtils.class);
	/**
	 * 请求阿里菜鸟库存接口
	 * @param model 
	 * @return
	 */
	public static WlbWmsInventoryQueryResponse executeTBInventoryQuery(WlbWmsInventoryQueryRequest wlbWmsInventoryQueryRequest,
									   String sessionKey,String appKey,String appSecret){
		LOGGER.info("阿里菜鸟仓实时库存查询请求sessionKey:", sessionKey+",appKey:"+appKey+",appSecret:"+appSecret);
		
		//菜鸟文档
		//https://open.taobao.com/api.htm?docId=10631&docType=2
		//https://open.taobao.com/api.htm?spm=a219a.15212433.0.0.4c65669aW4Z88m&source=search&docId=25720&docType=2
		
		/**
		 * 创建淘宝api客户端，线程安全，可复用
		 */
		TaobaoClient client = new DefaultTaobaoClient(ApolloUtil.taobaoApi, appKey, appSecret);
		WlbWmsInventoryQueryResponse response = new WlbWmsInventoryQueryResponse();
		try {
			response = client.execute(wlbWmsInventoryQueryRequest, sessionKey);
		} catch (ApiException e) {
			LOGGER.info("阿里菜鸟仓实时库存查询请求异常:"+e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("阿里菜鸟仓实时库存查询响应"+response);
		return response;
	}
}
