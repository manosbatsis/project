package com.topideal.service.api.purchase;
/**
 * 出库明细回推接口
 */
public interface PurTOutInventoryUpdateStatusService {
	// 出库明细
	public boolean updatePurTOutInventoryStatus(String json,String keys,String topics,String tags)throws Exception;

}
