package com.topideal.api.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.api.op.v2_14.CurrentStoreRequest;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpOPUtil;

import net.sf.json.JSONObject;

/**
 * op接口调用实体类
 * @author 杨创
 *2018/5/2
 */
public class OPUtils {
	
	private static final Logger logger= LoggerFactory.getLogger(OPUtils.class);
	
	
	/**
	 * 说明 1. 根据api 对字段进行必填校验
	 * 2.资金方 必填(如果非必填要探讨)
	 * 3.分页条数不填默认是100条(如果要取出全部数据要根据 totalCount 循环取出)
	 * 实时库存查询方法
	 * @param model 实时库存查询请求实体类
	 * @return
	 */
	public static String currentStoreQuery(CurrentStoreRequest model){
		// 实体类转json
		JSONObject jSONObject = JSONObject.fromObject(model);
		logger.info("op实时库存查询请求", jSONObject);
		String json = jSONObject.toString();
		System.out.println("===op===="+json);
		//实时库存查询用的是正式库的地址
		String doPost = HttpOPUtil.doPost(ApolloUtil.opV214, json, "utf-8");		
		logger.info("op实时库存查询响应"+doPost);
		return doPost;		
	}
	
	
	

}
