package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryGoodsSnapshotDTO;
import com.topideal.entity.vo.InventoryGoodsSnapshotModel;
/**
 * 库存管理 - 库存商品快照-service实现类
 */
public interface InventoryGoodsSnapshotService {
	
	/**
	 * 库存商品快照（分页）
	 * @param model 
	 * @return
	 */
	InventoryGoodsSnapshotDTO  listInventoryGoodsSnapshotModel(InventoryGoodsSnapshotDTO model) throws SQLException;
	
	
	   /**
     * 导出库存商品快照
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> exportInventoryGoodsSnapshotModelMap(InventoryGoodsSnapshotModel model) throws Exception;
	
}
