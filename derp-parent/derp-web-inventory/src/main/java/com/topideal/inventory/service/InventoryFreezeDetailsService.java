package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.vo.InventoryFreezeDetailsModel;
/**
 * 库存管理 -库存冻结和解冻收发明细-service实现类
 */
public interface InventoryFreezeDetailsService {
	
	/**
	 * 库存冻结和解冻收发明细列表（分页）
	 * @param model 
	 * @return
	 */
	InventoryFreezeDetailsModel listInventoryFreezeDetails(InventoryFreezeDetailsModel model) throws SQLException;
	
	
	   /**
     * 导出库存冻结和解冻收发明细
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> exportInventoryFreezeDetailsMap(InventoryFreezeDetailsModel model) throws Exception;
	
}
