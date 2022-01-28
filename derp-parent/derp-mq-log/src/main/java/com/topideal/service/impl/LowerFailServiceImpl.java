package com.topideal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.ConsumeMonitorInventoryDao;
import com.topideal.entity.vo.ConsumeMonitorInventoryModel;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.service.LowerFailService;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;

/**
 * 库存扣减失败重推
 */
@Service
public class LowerFailServiceImpl implements LowerFailService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LowerFailServiceImpl.class);
	// MQ消费监控
	@Autowired
	private ConsumeMonitorInventoryDao consumeMonitorInventoryDao;
	@Autowired
	private JSONMongoDao jsonMongoDao;

	@Autowired
	private RMQProducer rmqProducer;
		
	@Override
	public boolean resendLowerFailRecode() throws Exception {
		LOGGER.info("-----------------库存扣减失败重推开始----------------------");
		List<ConsumeMonitorInventoryModel> list = consumeMonitorInventoryDao.getLowerFailList();
		for (ConsumeMonitorInventoryModel mqModel : list) {
			if(mqModel.getLogId()!=null){
				//根据log_id查询报文
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", mqModel.getLogId());
				JSONObject jsonObject = jsonMongoDao.findOne(params, CollectionEnum.MQ_INVENTORY_LOG.getCollectionName());
				String topics = jsonObject.getString("topics");
				String tags = jsonObject.getString("tags");
				String requestMessage = jsonObject.getString("requestMessage");
				if (StringUtils.isBlank(topics) || StringUtils.isBlank(tags)) {
					throw new RuntimeException("topics或tags为空");
				}
				//重推库存MQ
				rmqProducer.send(requestMessage, topics, tags);
				//修改监控记录的重推状态和时间
				ConsumeMonitorInventoryModel model = new ConsumeMonitorInventoryModel();
				model.setId(mqModel.getId());
				model.setIsResend("1");
				model.setResendDate(TimeUtils.getNow());
				consumeMonitorInventoryDao.modify(model);
			}
		}
		LOGGER.info("-----------------库存扣减失败重推结束----------------------");
		return true;
	}

}
