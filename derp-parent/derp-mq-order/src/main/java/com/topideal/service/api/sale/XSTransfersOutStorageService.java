/*package com.topideal.service.api.sale;

import java.util.List;
import java.util.Map;

*//**
 * 调拨出库
 *//*
public interface XSTransfersOutStorageService {
	*//**
	 * 调拨出库
	 * @param json
	 * @return
	 * @throws Exception
	 *//*
	public Map<String,List<String>> saveTransfersOutStorageInfo(String json,String keys,String topics,String tags)throws Exception;
	*//**
	 * 推送库存MQ-解冻、扣减库存
	 * @param invetAddOrSubList
	 * @throws Exception
	 *//*
	public void pushInventoryMQ(Map<String,List<String>> inventoryMap) throws Exception;
}
*/