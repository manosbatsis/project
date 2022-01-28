package com.topideal.mq.api.purchase;

import java.sql.SQLException;

import com.topideal.common.enums.MQInventoryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.json.api.v1_3.PurchaseIntoWarehouseStatusJson;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.api.purchase.PurchaseIntoWarehouseStatusService;
import com.topideal.service.common.CommonBusinessService;

import net.sf.json.JSONObject;

/**
 * 进仓单状态回推
 * @author zhanghx
 * 2018/7/16
 */
@Component
public class PurchaseIntoWarehouseStatusMQ extends ConsumerMQAbstract {
	
	/**
	 * 打印日志
	 */
	private Logger LOGGER=LoggerFactory.getLogger(PurchaseIntoWarehouseStatusMQ.class);
	
	@Autowired
	private PurchaseIntoWarehouseStatusService purchaseIntoWarehouseStatusService;//进仓单状态回推service
	@Autowired
	private CommonBusinessService commonBusinessService ;
	
	public PurchaseIntoWarehouseStatusMQ(){
		super.setTopics(MQOrderEnum.EPASS_WAREHOUSE_STATUS_1.getTopic());
		super.setTags(MQOrderEnum.EPASS_WAREHOUSE_STATUS_1.getTags());
	}
	
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("==========进仓单状态回推========");
		try {
			purchaseIntoWarehouseStatusService.saveIntoWarehouseStatusInfo(json,keys,topics,tags);

		}catch (Exception e) {
			LOGGER.error("异常,{}", e.getMessage(), e);
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		LOGGER.info("==========进仓单状态回推结束========");
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
