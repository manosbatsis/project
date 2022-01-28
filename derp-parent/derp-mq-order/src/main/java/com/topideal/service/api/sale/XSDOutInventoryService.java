package com.topideal.service.api.sale;
/**
 * 出库明细回推接口生成出库单-销售预申报
 */
public interface XSDOutInventoryService {
	// 出库明细
	public boolean saveOutDepotInfo(String json, String keys, String topics, String tags)throws Exception;

}
