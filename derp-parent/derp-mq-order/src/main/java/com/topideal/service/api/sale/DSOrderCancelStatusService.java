package com.topideal.service.api.sale;
/**
 * 订单取消状态回推接口
 */
public interface DSOrderCancelStatusService {
	/**
	 * 保存订单取消信息
	 * @param json
	 * @return
	 */
	public boolean saveOrderCancelInfo(String json,String keys,String topics,String tags)throws Exception;

}
