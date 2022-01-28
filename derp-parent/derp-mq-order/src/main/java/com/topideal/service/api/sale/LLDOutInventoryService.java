package com.topideal.service.api.sale;
/**
 * 出库明细回推接口 -领料单 生成出库单
 */
public interface LLDOutInventoryService {
	// 出库明细
	public boolean saveOutDepotInfo(String json,String keys,String topics,String tags)throws Exception;

}
