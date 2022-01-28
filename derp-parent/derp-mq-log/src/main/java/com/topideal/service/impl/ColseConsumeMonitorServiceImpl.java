package com.topideal.service.impl;

import java.util.List;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.ConsumeMonitorApiDao;
import com.topideal.dao.ConsumeMonitorInventoryDao;
import com.topideal.dao.ConsumeMonitorOrderDao;
import com.topideal.dao.LogStreamApiDao;
import com.topideal.dao.LogStreamInventoryDao;
import com.topideal.dao.LogStreamOrderDao;
import com.topideal.dao.LogWarningMqDao;
import com.topideal.entity.vo.ConsumeMonitorInventoryModel;
import com.topideal.mongo.dao.ExceptionOrderHistoryPoolMongoDao;
import com.topideal.mongo.dao.ExceptionOrderPoolMongoDao;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.service.ColseConsumeMonitorService;

import net.sf.json.JSONObject;

/**
 * MQ消费监控 MQ
 * 
 * @author zhanghx 2018/8/17
 */
@Service
public class ColseConsumeMonitorServiceImpl implements ColseConsumeMonitorService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ColseConsumeMonitorServiceImpl.class);

	// MQ日志预警
	@Autowired
	private LogWarningMqDao logWarningMqDao;
	// 库存日志
	@Autowired
	private JSONMongoDao jsonMongoDao;
	//api
	@Autowired
	private ConsumeMonitorApiDao consumeMonitorApiDao;
	@Autowired
	private LogStreamApiDao logStreamApiDao;
	//库存
	@Autowired
	private ConsumeMonitorInventoryDao consumeMonitorInventoryDao;
	@Autowired
	private LogStreamInventoryDao logStreamInventoryDao;
	//业务
	@Autowired
	private ConsumeMonitorOrderDao consumeMonitorOrderDao;
	@Autowired
	private ExceptionOrderPoolMongoDao exceptionOrderPoolMongoDao;
	@Autowired
	private ExceptionOrderHistoryPoolMongoDao exceptionOrderHistoryPoolMongoDao;
	@Autowired
	private LogStreamOrderDao logStreamOrderDao;
	
	@Override
	public boolean saveClose(String json, String topics, String tags) throws Exception {
		LOGGER.info("-----------------关闭日志监控开始jsonData:"+json+"----------------------");		
		// 实例化JSON对象
		JSONObject jsonData = JSONObject.fromObject(json);		
		String orderNo = (String) jsonData.get("orderNo");
		if (StringUtils.isBlank(orderNo)) {
			LOGGER.error("关闭日志监控orderNo:"+"不能为空");
			return false;
		}
		// 获取失败库存监控
		ConsumeMonitorInventoryModel consumeInventory=new ConsumeMonitorInventoryModel();
		consumeInventory.setKeyword(orderNo);
		consumeInventory.setStatus(DERP_LOG.LOG_STATUS_0);
		List<ConsumeMonitorInventoryModel> list = consumeMonitorInventoryDao.list(consumeInventory);
		for (ConsumeMonitorInventoryModel model : list) {
			// 关闭1.库存加减明细失败监控  2.关闭库存冻结解冻失败监控
			if (DERP_LOG_POINT.POINT_13201301700.equals(model.getPoint())||DERP_LOG_POINT.POINT_13201301800.equals(model.getPoint())) {
				ConsumeMonitorInventoryModel colseInventoryConsume=new ConsumeMonitorInventoryModel();
				colseInventoryConsume.setStatus(DERP_LOG.LOG_STATUS_2);
				colseInventoryConsume.setId(model.getId());
				colseInventoryConsume.setCloseTime(TimeUtils.getNow());
				consumeMonitorInventoryDao.modify(colseInventoryConsume);	
			}
		}

		LOGGER.info("-----------------日志监控结束----------------------");
		return true;
	}
	
	
}
