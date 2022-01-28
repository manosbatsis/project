package com.topideal.service.impl;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.InventoryDetailsDao;
import com.topideal.service.InventoryDetailsMoveToHistoryService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 迁移数据到历史表
 */
@Service
public class InventoryDetailsMoveToHistoryServiceImpl implements InventoryDetailsMoveToHistoryService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryDetailsMoveToHistoryServiceImpl.class);

	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;

	/**
	 * 迁移数据到历史表
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point = "13201309000", model = "迁移数据到历史表-库存", keyword = "orderNo")
	public boolean synsMoveToHistory(String json, String keys,String topics,String tags) throws Exception {
        //计算12个月以前的
        int num = 12;
		String divergenceDate = TimeUtils.getAgoMonthByNow(num)+"-01 00:00:00";

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("divergenceDate",divergenceDate);
		//迁移数据到历史表
		inventoryDetailsDao.synsMoveToHistory(map);
		//删除已经迁移到历史表的数据
		inventoryDetailsDao.delMoveToHistory(map);

		return true;
	}

}









