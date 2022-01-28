package com.topideal.inventory.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.entity.dto.InventoryRealTimeSnapshotDTO;
import com.topideal.entity.vo.InventoryRealTimeSnapshotModel;
import com.topideal.inventory.service.InventoryRealTimeSnapshotService;

/**
 * 实时库存快照
 * 杨创
 *
 */
@Service
public class InventoryRealTimeSnapshotServiceImpl implements InventoryRealTimeSnapshotService {

	/*@Autowired
	private InventoryBatchSnapshotDao  inventoryBatchSnapshotDao;*/
	
	@Autowired
	private InventoryRealTimeSnapshotDao realTimeSnapshotDao;
	
	/**
	 * 实时库存列表分页
	 */
	@Override
	public InventoryRealTimeSnapshotDTO listInventoryRealTimeSnapshotModel(InventoryRealTimeSnapshotDTO model)
			throws SQLException {
		InventoryRealTimeSnapshotDTO inventoryRealTimeSnapshotModel = realTimeSnapshotDao.getInventoryRealTimeSnapshotListByPage(model);
		return inventoryRealTimeSnapshotModel;
		
		
	}
	
	/**
	 * 实时库存页面导出
	 */
	@Override
	public List<Map<String, Object>> exportInventoryRealTimeSnapshotModelMap(InventoryRealTimeSnapshotModel model)
			throws Exception {
		return realTimeSnapshotDao.exportinventoryRealTimeSnapshotMap(model);
		
	}

}
