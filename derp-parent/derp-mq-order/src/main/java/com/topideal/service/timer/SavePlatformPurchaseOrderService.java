package com.topideal.service.timer;

/**
 * 生成/更新平台采购订单
 * @author 杨创
 *2020/08/11
 */
public interface SavePlatformPurchaseOrderService {
	/**
	 * 生成/更新平台采购订单(京东)
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean saveJDPlatformPurchaseOrder (String json,String keys,String topics,String tags) throws Exception;
	
	/**
	 * 生成/更新平台采购订单 (天猫)
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean saveTmallPlatformPurchaseOrder (String json,String keys,String topics,String tags) throws Exception;	
	
	

}
