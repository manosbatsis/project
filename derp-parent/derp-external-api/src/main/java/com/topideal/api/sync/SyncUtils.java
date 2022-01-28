package com.topideal.api.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.api.op.v2_14.CurrentStoreRequest;
import com.topideal.api.sync.sy01.SyncGoodsRequest;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;
import com.topideal.http.HttpOPUtil;

import net.sf.json.JSONObject;

/**
 * 推送主数据同步接口
 * @author 杨创
 *2020-08-05
 */
public class SyncUtils {
	
	private static final Logger logger= LoggerFactory.getLogger(SyncUtils.class);
	
	
	/**
	 * 说明 1. 根据api 对字段进行必填校验
	 * 2.资金方 必填(如果非必填要探讨)
	 * 3.分页条数不填默认是100条(如果要取出全部数据要根据 totalCount 循环取出)
	 * 实时库存查询方法
	 * @param model 实时库存查询请求实体类
	 * @return
	 */
	public static String syncGoods(SyncGoodsRequest  model){
		// 实体类转json
		JSONObject jSONObject = JSONObject.fromObject(model);
		logger.info("产品商品信息同步接口(推主数据)", jSONObject);
		String json = jSONObject.toString();
		System.out.println("===op===="+json);
		//产品商品信息同步接口(推主数据)
		String doPost = HttpClientUtil.doPost(ApolloUtil.opV214, json, "utf-8");		
		logger.info("产品商品信息同步接口(推主数据)"+doPost);
		return doPost;		
	}
	
	
	

}
