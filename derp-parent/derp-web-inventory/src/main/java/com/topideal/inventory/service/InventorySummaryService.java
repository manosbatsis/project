package com.topideal.inventory.service;

import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventorySummaryDTO;
/**
 * 库存管理 -商品收发汇总-service实现类
 */
public interface InventorySummaryService {
	

	/**
	 * 商品收发汇总 
	 * @param inventorySummaryModel
	 * @return
	 * @throws Exception
	 */
	InventorySummaryDTO listInventorySummaryByPage(InventorySummaryDTO inventorySummaryModel) throws Exception;

	
	/**
	 * 导出商品收发汇总
	 * @param obj
	 * @return
	 * @throws Exception
	 */
    List<Map<String,Object>> exportInventorySummaryMap(Long merchantId,Long depotId,String goodsNo,String currentMonth)throws Exception;
	
}
