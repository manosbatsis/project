package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InitInventoryDTO;
import com.topideal.inventory.webapi.form.InitInventorySaveForm;


/**
 * 库存-期初库存service实现类
 */
public interface InitInventoryService {
	
	/**
	 * 期初库存列表（分页）
	 * @param model 
	 * @return
	 */
	InitInventoryDTO listInitInventory(InitInventoryDTO model) throws SQLException;
	
	
	/**
	 * 根据库存id删除期初库存(支持批量)
	 * @param ids
	 * @return
	 */
	boolean delInitInventory(List<Long> ids) throws SQLException;
	
	/**
	 * 导入库存期初（自己的商品）
	 * @param data 解析excel的库存信息数据
	 * @return
	 * @throws SQLException 
	 */
	Map importInitInventory(Map<Integer, List<List<Object>>> data,Long userId,Long merchantId,String merchantName,String topidealCode)throws Exception;
	
	/**
	 * 导入库存期初（自己的商品）
	 * @param data 解析excel的库存信息数据
	 * @return
	 * @throws SQLException 
	 */
	Map importInitInventory11(Map<Integer, List<List<Object>>> data,Long userId,Long merchantId,String merchantName,String topidealCode)throws Exception;
	
	
	/**
	 * 确认库存期初信息（保税仓等其他仓的商品）
	 */
	void confirmInitInventory()throws SQLException;
	
	
	/**
	 * 批量审核期初
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	boolean auditInitInventory(List<Long> list) throws SQLException;
	
	/**
	 * 确认和保存期初()后面删除
	 * @param jsonObj
	 * @return
	 * @throws SQLException
	 */
	Map  saveInitInventory(String jsonObj)throws SQLException;
	/**
	 * 确认和保存期初
	 * @param jsonObj
	 * @return
	 * @throws SQLException
	 */
	Map  saveInitInventory11(InitInventorySaveForm form)throws SQLException;
}
