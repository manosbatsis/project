package com.topideal.service.api.purchase;
/**
 * 出库明细回推接口
 */
public interface PurTOutInventoryService {
	// 出库明细
	public boolean savePurTOutInventoryInfo(String json,String keys,String topics,String tags)throws Exception;

}
