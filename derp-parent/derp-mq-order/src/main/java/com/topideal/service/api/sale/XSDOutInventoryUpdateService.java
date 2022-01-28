package com.topideal.service.api.sale;
/**
 * 出库明细发货状态更新接口-销售预申报
 */
public interface XSDOutInventoryUpdateService {
	// 出库明细
	public boolean saveOutInventoryInfo(String json, String keys, String topics, String tags)throws Exception;

}
