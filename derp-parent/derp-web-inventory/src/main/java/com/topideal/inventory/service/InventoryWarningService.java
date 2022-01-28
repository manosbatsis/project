package com.topideal.inventory.service;

import java.sql.SQLException;

import com.topideal.entity.vo.InventoryWarningModel;
/**
 * 库存管理 -库存预警-service实现类
 */
public interface InventoryWarningService {
	
	/**
	 * 库存预警列表（分页）
	 * @param model 
	 * @return
	 */
	InventoryWarningModel listInventoryWarning(InventoryWarningModel model) throws SQLException;
	
}
