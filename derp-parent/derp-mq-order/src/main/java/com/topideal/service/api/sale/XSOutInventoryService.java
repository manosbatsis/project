package com.topideal.service.api.sale;
/**
 * 出库明细回推接口 —— 销售 生成出库单 
 */
public interface XSOutInventoryService {
	// 出库明细
	public boolean saveOutDepotInfo(String json,String keys,String topics,String tags)throws Exception;

}
