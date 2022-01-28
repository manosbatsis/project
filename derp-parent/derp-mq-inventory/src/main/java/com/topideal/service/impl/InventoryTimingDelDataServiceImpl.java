package com.topideal.service.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.InventoryBatchSnapshotDao;
import com.topideal.dao.InventoryGoodsSnapshotDao;
import com.topideal.dao.InventoryRealTimeSnapshotDao;
import com.topideal.service.InventoryTimingDelDataService;

/**
 * 删除数据
 * @author liyuanjian
 */
@Service
public class InventoryTimingDelDataServiceImpl implements InventoryTimingDelDataService {

	private static final Logger logger = LoggerFactory.getLogger(InventoryTimingDelDataServiceImpl.class);
	
	@Autowired
	private InventoryBatchSnapshotDao inventoryBatchSnapshotDao;
	@Autowired
	private InventoryGoodsSnapshotDao inventoryGoodsSnapshotDao;
	@Autowired
	private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao;

	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201304700, model = DERP_LOG_POINT.POINT_13201304700_Label)
	public void delData(String json, String keys, String topics, String tags) throws Exception {
		logger.info("---------------删除快照数据---------------");
		
		inventoryBatchSnapshotDao.delData();
		inventoryGoodsSnapshotDao.delData();
		inventoryRealTimeSnapshotDao.delData();
		
		logger.info("----------------删除快照数据---------------");
	}

}
