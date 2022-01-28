package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryDetailsDTO;
/**
 * 库存管理 -库存收发明细-service实现类
 */
public interface InventoryDetailsService {
	
	/**
	 * 库存收发明细列表（分页）
	 * @param model 
	 * @return
	 */
	InventoryDetailsDTO listInventoryDetails(InventoryDetailsDTO model) throws SQLException;
	
	
	/**
     * 导出商品收发明细
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> exportInventoryDetailsMap(InventoryDetailsDTO model) throws Exception;
	/**
	 * 
	 * @param codes
	 * @throws Exception
	 */
	void delInventoryDetail(String codes) throws Exception;
	
}
