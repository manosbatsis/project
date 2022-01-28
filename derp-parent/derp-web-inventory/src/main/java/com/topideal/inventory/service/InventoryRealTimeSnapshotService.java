package com.topideal.inventory.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.topideal.entity.dto.InventoryRealTimeSnapshotDTO;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
/**
 * 库存管理 -实时库存快照表-service实现类
 */
public interface InventoryRealTimeSnapshotService {
	
	/**
	 * 实时库存快照表列表（分页）
	 * @param model 
	 * @return
	 */
	InventoryRealTimeSnapshotDTO listInventoryRealTimeSnapshotModel(InventoryRealTimeSnapshotDTO model) throws SQLException;
	
	
	   /**
     * 导出库实时库存快照表
     * @param id
     * @return
     * @throws Exception
     */
	List<Map<String,Object>> exportInventoryRealTimeSnapshotModelMap(InventoryRealTimeSnapshotModel model) throws Exception;
	
}
