package com.topideal.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.reporting.InventoryRealTimeSnapshotDao;
import com.topideal.service.SysDelDataService;

/**
 * 删除数据
 * @author liyuanjian
 */
@Service
public class SysDelDataServiceImpl implements SysDelDataService {

	private static final Logger logger = LoggerFactory.getLogger(SysDelDataServiceImpl.class);
	
	@Autowired
	private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao;

	@SystemServiceLog(point = "13201501060", model = "删除历史快照数据-报表-已废弃")
	public void delData(String json, String keys, String topics, String tags) throws Exception {
		logger.info("----------------删除快照数据---------------");
		
		//inventoryRealTimeSnapshotDao.delData();
		
		logger.info("----------------删除快照数据---------------");
	}

}
