package com.topideal.service.api.sale;
/**
 * 出库明细回推接口 - 销售 更新出库时间，扣减库存
 */
public interface XSOutInventoryUpdateService {
	// 出库明细
	public boolean saveOutInventoryInfo(String json,String keys,String topics,String tags)throws Exception;

}
