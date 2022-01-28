package com.topideal.mq.timer;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.CrawlerRookieOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 获取菜鸟电商订单数据
 */
@Component
public class CrawlerRookieOrderMQ extends ConsumerMQAbstract{

	@Autowired
	private CrawlerRookieOrderService crawlerRookieOrderService;
	@Autowired
	private RMQProducer rocketMQProducer;
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER= LoggerFactory.getLogger(CrawlerRookieOrderMQ.class);

	public CrawlerRookieOrderMQ(){
		super.setTags(MQOrderEnum.TIMER_CRAWLER_ROOKIE_ORDER_DATA.getTags());
		super.setTopics(MQOrderEnum.TIMER_CRAWLER_ROOKIE_ORDER_DATA.getTopic());
	}


	@Override
	public ConsumeConcurrentlyStatus execute(String json,String keys,String topics,String tags) {
		LOGGER.info("===================获取菜鸟电商订单数据==================");
		synchronized(this){
			try {
				// 获取菜鸟电商订单数据
				crawlerRookieOrderService.getRookieOrderData(json,keys,topics,tags);

				//分摊税费运费
				//Thread.sleep(60000);// 睡眠60秒
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("remark", "电商订单金额更新(菜鸟电商订单)");
				rocketMQProducer.send(jsonObject.toString(),MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTopic(),MQOrderEnum.TIMER_ORDER_APPORTION_TAX_FREIGHT.getTags());
			} catch (Exception e) {
				LOGGER.error("获取菜鸟电商订单数据异常", e);
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
