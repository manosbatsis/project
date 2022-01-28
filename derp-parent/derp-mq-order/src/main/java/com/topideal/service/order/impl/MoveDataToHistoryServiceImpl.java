package com.topideal.service.order.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.dao.sale.WayBillDao;
import com.topideal.dao.sale.WayBillItemDao;
import com.topideal.service.order.MoveDataToHistoryService;
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

	/**
	 * 迁移数据到历史表
	 * 
	 * @throws Exception
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201128900, model = DERP_LOG_POINT.POINT_13201128900_Label, keyword = "orderNo")
	public boolean synsMoveToHistory(String json, String keys,String topics,String tags) throws Exception {
        //计算12个月以前的
		int num = 12;
		String divergenceDate = TimeUtils.getAgoMonthByNow(num)+"-01 00:00:00";

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("divergenceDate",divergenceDate);
		/**运单表体****************************/
        LOGGER.info("迁移数据到历史表-------运单表体");
		wayBillItemDao.synsMoveToHistory(map);
		wayBillItemDao.delMoveToHistory(map);
		/**运单********************************/
        LOGGER.info("迁移数据到历史表-------运单");
		wayBillDao.synsMoveToHistory(map);
		wayBillDao.delMoveToHistory(map);
		/**电商订单表体*************************/
        LOGGER.info("迁移数据到历史表-------电商订单表体");
		orderItemDao.synsMoveToHistory(map);
		orderItemDao.delMoveToHistory(map);
		/**电商订单*****************************/
        LOGGER.info("迁移数据到历史表-------电商订单");
		orderDao.synsMoveToHistory(map);
		orderDao.delMoveToHistory(map);

		return true;
	}

}









