package com.topideal.api.ews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;

/**
 * 第e仓接口工具类
 * @author 杨创
 */
public class EwsUtils {
	
	
	private static final Logger LOGGER= LoggerFactory.getLogger(EwsUtils.class);



	public static String getGrabEWSDeliveryOrder(JSONObject jsonObject) throws Exception{
		
		LOGGER.info("请求第e仓开始 returnJson:"+jsonObject);
		String  returnJson=HttpClientUtil.doPost(ApolloUtil.ewsW410, jsonObject.toString(), "utf-8");
		LOGGER.info("请求第e仓响应 returnJson:"+returnJson);
		//String  returnJson="{\"note\":\"成功\",\"order_list\":[{\"index\":1,\"logistics_code\":\"1000000239\",\"merchant_code\":\"1000000204\",\"order_id\":\"yangchuagn0921002\",\"order_paytime\":\"2017-11-20 14:55:58.0\",\"deliver_date\":\"2017-11-22 14:55:58.0\",\"warehouse_id\":\"WMS_360_04\",\"way_bill_no\":\"yundan0921001\",\"good_list\":[{\"good_name\":\"Huggies好奇 铂金装倍柔亲肤婴儿纸尿裤M72片*2\",\"goods_id\":\"080289001405N\",\"index1\":1,\"num\":1,\"price\":153.23,\"batch_id\":\"LT0216061400049\",\"production_time\":\"2018-01-01 00:00:00\",\"exp_date\":\"2018-11-01 00:00:00\"}]},{\"index\":1,\"logistics_code\":\"1000000239\",\"merchant_code\":\"1000000204\",\"order_id\":\"yangchuagn0921003\",\"order_paytime\":\"2017-11-20 14:55:58.0\",\"deliver_date\":\"2017-11-22 14:55:58.0\",\"warehouse_id\":\"WMS_360_04\",\"way_bill_no\":\"yundan0921002\",\"good_list\":[{\"good_name\":\"Huggies好奇 铂金装倍柔亲肤婴儿纸尿裤M72片*2\",\"goods_id\":\"080289001405N\",\"index1\":1,\"num\":1,\"price\":153.23,\"batch_id\":\"LT0216061400049\",\"production_time\":\"2018-01-01 00:00:00\",\"exp_date\":\"2018-11-01 00:00:00\"}]}],\"status\":\"1\",\"order_num\":\"2\",\"return_page\":\"1\",\"return_pagesize\":\"2\"}";
		//String  returnJson="{\"note\":\"成功\",\"order_list\":[{\"index\":1,\"logistics_code\":\"1000000239\",\"merchant_code\":\"1000000204\",\"order_id\":\"yangchuagn0921002\",\"order_paytime\":\"2017-11-20 14:55:58.0\",\"deliver_date\":\"2017-11-22 14:55:58.0\",\"warehouse_id\":\"WMS_360_04\",\"way_bill_no\":\"yundan0921001\",\"good_list\":[{\"good_name\":\"Huggies好奇 铂金装倍柔亲肤婴儿纸尿裤M72片*2\",\"goods_id\":\"gc20180723112\",\"index1\":1,\"num\":1,\"price\":153.23,\"batch_id\":\"LT0216061400049\",\"production_time\":\"2018-01-01 00:00:00\",\"exp_date\":\"2018-11-01 00:00:00\"}]},{\"index\":1,\"logistics_code\":\"1000000239\",\"merchant_code\":\"1000000204\",\"order_id\":\"yangchuagn0921003\",\"order_paytime\":\"2017-11-20 14:55:58.0\",\"deliver_date\":\"2017-11-22 14:55:58.0\",\"warehouse_id\":\"WMS_360_04\",\"way_bill_no\":\"yundan0921002\",\"good_list\":[{\"good_name\":\"Huggies好奇 铂金装倍柔亲肤婴儿纸尿裤M72片*2\",\"goods_id\":\"gc20180723112\",\"index1\":1,\"num\":1,\"price\":153.23,\"batch_id\":\"LT0216061400049\",\"production_time\":\"2018-01-01 00:00:00\",\"exp_date\":\"2018-11-01 00:00:00\"}]}],\"status\":\"1\",\"order_num\":\"2\",\"return_page\":\"1\",\"return_pagesize\":\"2\"}";		
		return returnJson;
	}
	
}
