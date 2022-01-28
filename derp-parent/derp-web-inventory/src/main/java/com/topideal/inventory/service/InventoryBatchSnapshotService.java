package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryBatchSnapshotDTO;
import com.topideal.entity.vo.InventoryBatchSnapshotModel;
/**
 * 库存管理 -库存批次快照-service实现类
 */
public interface InventoryBatchSnapshotService {
	
	/**
	 * 库存批次快照列表（分页）
	 * @param model 
	 * @return
	 */
	InventoryBatchSnapshotDTO listInventoryBatchSnapshotModel(InventoryBatchSnapshotDTO model) throws SQLException;
	
	
	   /**
     * 导出库存批次快照
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> exportInventoryBatchSnapshotModelMap(InventoryBatchSnapshotModel model) throws Exception;
	
}
