package com.topideal.service.timer;

import java.util.Map;

/**
 * 生成/更新平台采购订单
 * @author 杨创
 *2020/08/27
 */
public interface SavePlatformCostOrderService {
	/**
	 * 生成平台费用订单
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean savePlatformCostOrder (String json,String keys,String topics,String tags,Map<String, Object> map) throws Exception;
	

}
