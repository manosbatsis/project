package com.topideal.order.service.purchase;

import java.util.List;

import com.topideal.entity.dto.purchase.PurchasingReportsDTO;
/**
 * 
  * 采销计划报表
 *
 */
public interface PurchasingReportsService {
	/**
	  * 列表 分页
	 * @param model
	 * @return
	 */
	PurchasingReportsDTO listPurchasingReports(PurchasingReportsDTO dto) throws Exception;
	/**
	  * 获取导出数据
	 * @param model
	 * @return
	 */	
	List<PurchasingReportsDTO> exportList(PurchasingReportsDTO model) throws Exception;
	
	/**
	 * 获取采购计划报表数量
	 * @param dto
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	Integer getOrderCount(PurchasingReportsDTO dto) throws Exception;

}
