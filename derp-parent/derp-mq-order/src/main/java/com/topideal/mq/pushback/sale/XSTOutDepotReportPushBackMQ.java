package com.topideal.mq.pushback.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.pushback.sale.XSShelfInDepotPushBackService;
import com.topideal.service.pushback.sale.XSTOutDepotReportPushBackService;
/**
 * 销售退出库打托减库存成功回推
 * @author wenyan
 *
 */
@Component
public class XSTOutDepotReportPushBackMQ extends ConsumerMQAbstract {
	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(XSTOutDepotReportPushBackMQ.class);
    @Autowired
    private XSTOutDepotReportPushBackService xSTOutDepotReportPushBackService;// 销售退出库打托service
    
    public  XSTOutDepotReportPushBackMQ() {
		super.setTags(MQPushBackOrderEnum.SALERETURN_OUTDEPOT_REPORT__PUSH_BACK.getTags());
		super.setTopics(MQPushBackOrderEnum.SALERETURN_OUTDEPOT_REPORT__PUSH_BACK.getTopic());
	}
	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("====================销售退出库打托减库存成功回推===================");
		try {
			xSTOutDepotReportPushBackService.updateXSTOutDepotReportPushBack(json, keys, topics, tags);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("销售退出库打托减库存成功回推异常", e.getMessage());
			return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
