package com.topideal.service.impl;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.inventory.InventoryDetailsDao;
import com.topideal.dao.order.OrderDao;
import com.topideal.dao.order.OrderItemDao;
import com.topideal.dao.order.WayBillDao;
import com.topideal.dao.order.WayBillItemDao;
import com.topideal.service.MoveDataToHistoryService;
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
public class MoveDataToHistoryServiceImpl implements MoveDataToHistoryService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MoveDataToHistoryServiceImpl.class);
	// 电商订单
	@Autowired
	private OrderDao orderDao;
	// 电商订单商品
	@Autowired
	private OrderItemDao orderItemDao;
	// 运单表
	@Autowired
	private WayBillDao wayBillDao;
	// 运单表体
	@Autowired
	private WayBillItemDao wayBillItemDao;
	@Autowired
	private InventoryDetailsDao inventoryDetailsDao;

	/**
	 * 迁移数据到历史表
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point = "13201502040", model = "迁移数据到历史表-报表", keyword = "orderNo")
	public boolean synsMoveToHistory(String json, String keys,String topics,String tags) throws Exception {
        //计算12个月以前的
        int num = 12;
		String divergenceDate = TimeUtils.getAgoMonthByNow(num)+"-01 00:00:00";

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("divergenceDate",divergenceDate);
		/**运单表体****************************/
		wayBillItemDao.synsMoveToHistory(map);
		wayBillItemDao.delMoveToHistory(map);
		/**运单********************************/
		wayBillDao.synsMoveToHistory(map);
		wayBillDao.delMoveToHistory(map);
		/**电商订单表体*************************/
		orderItemDao.synsMoveToHistory(map);
		orderItemDao.delMoveToHistory(map);
		/**电商订单*****************************/
		orderDao.synsMoveToHistory(map);
		orderDao.delMoveToHistory(map);
		/**收发明细*****************************/
		//迁移数据到历史表
		inventoryDetailsDao.synsMoveToHistory(map);
		//删除已经迁移到历史表的数据
		inventoryDetailsDao.delMoveToHistory(map);
		return true;
	}

}









