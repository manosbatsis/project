package com.topideal.mq.syn;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.api.wx.WXUtils;
import com.topideal.common.enums.MQReportEnum;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.SynCheckService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据巡检->报表库 MQ
 * 主服务、业务库、仓储库、库存库>报表库 数据巡检->报表库
 */
@Component
public class SynCheckMQ extends ConsumerMQAbstract {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SynCheckMQ.class);

	@Autowired
	private SynCheckService synCheckService;

	public SynCheckMQ() {
		super.setTopics(MQReportEnum.SYN_CHECK.getTopic());
		super.setTags(MQReportEnum.SYN_CHECK.getTags());
	}

	@Override
	public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
		LOGGER.info("=============数据巡检->报表库 开始=================>");
		try {
			String errorTable = synCheckService.synCheck(json, keys, topics, tags);
			if(StringUtils.isNotBlank(errorTable)){
				String msg = "【"+ApolloUtil.jxRemark+"】数据巡检->报表库:不一致请核查";
				LOGGER.info(msg+errorTable);
				WXUtils.send(msg+errorTable);//发送机器人消息预警
				//WXUtils.sendPush(msg+errorTable);//发送机器人消息预警
			}else{
				int hour = TimeUtils.getHour();
				if(hour==10){
					WXUtils.send("【"+ApolloUtil.jxRemark+"】数据巡检->报表库: 正常");//发送机器人消息预警
					//WXUtils.sendPush("【"+ApolloUtil.jxRemark+"】数据巡检->报表库: 正常");//发送机器人消息预警
				}
				LOGGER.info("【"+ApolloUtil.jxRemark+"】数据巡检->报表库: 正常");
			}

		LOGGER.info("=============数据巡检->报表库 结束=================>");
		} catch (Exception e) {
			LOGGER.error("数据巡检->报表库:"+e.getMessage(), e);
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
}
