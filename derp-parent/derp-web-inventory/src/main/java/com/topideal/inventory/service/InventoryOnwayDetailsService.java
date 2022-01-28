package com.topideal.inventory.service;

import java.sql.SQLException;

import com.topideal.entity.vo.InventoryOnwayDetailsModel;
/**
 * 库存管理 -库存在途明细-service实现类
 */
public interface InventoryOnwayDetailsService {
	
	/**
	 * 库存在途明细列表（分页）
	 * @param model 
	 * @return
	 */
	InventoryOnwayDetailsModel listInventoryOnwayDetails(InventoryOnwayDetailsModel model) throws SQLException;
	
}
