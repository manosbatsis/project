package com.topideal.service.timer;

import java.util.List;

import net.sf.json.JSONObject;

/**
 * 抓取寄售商e仓发货订单
 * @author 杨创
 *2018/09/19
 */
public interface GrabEWSDeliveryOrderService {
	/**
	 * 分页抓取寄售商e仓发货订单
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	//public boolean getGrabEWSDeliveryOrder (String json,String keys,String topics,String tags) throws Exception;	
	/**
	 * 订单抓取数据过滤
	 * @param result
	 * @return
	 * @throws Exception
	 */
	//public void datafilterGrabEWSDeliveryOrder(String result) throws Exception;
	
	/**
	 * 向数据库插入第e仓发货数据数据
	 * @param listError 
	 * @param listOk
	 * @param listExternalCode
	 * @return
	 * @throws Exception
	 */
	//public void insertGrabEWSDeliveryOrder(List <JSONObject> listError,List <JSONObject> listOk,List <String> listExternalCode,List<String> listWayBillNo,Long merchantId)throws Exception;
	
	
	/**
	 * 订单订单100 单个数据过滤和插入
	 * @param jSONOrderObject
	 * @throws Exception
	 */
	public void insertDatafilterGrabEWSDeliveryOrder(String json,String keys,String topics,String tags)throws Exception;
	
	/**
	 * 插入错误日志
	 * @param jsonError
	 */
	public void  grabEWSDeliveryOrderError(JSONObject jsonError,JSONObject json,String orderNo);
	
	/**
	 * 单个
	 * @param jsonError
	 * @param json
	 * @param orderNo
	 */
	public void  getOneEWSDeliveryOrderError(JSONObject jsonError,JSONObject json,String orderNo);
	

}
