package com.topideal.common.enums;

/**
 * 日志枚举
 * @author zhanghx
 */
public enum MQLogEnum {
	
	/**
	 * 订单通道
	 */
	LOG_CONSUME_MONITOR("derp-order-log-mq","log-monitor-tags"),
	/**
	 * 库存通道
	 */
	LOG_CONSUME_INVENTORY("derp-inventory-log-mq","log-monitor-tags"),
	/**
	 * api通道
	 */
	LOG_API("derp-api-log-mq","log-api-tags"),
	
	/**
	 * 其他通道
	 */
	LOG_CRAWLER_BILL("derp-log-mq","log-crawler-tags"),
	/**------------------发送邮件--------------------------------*/
	TIMER_DAILY_STATISTICS("derp-log-mq","log-statistics-tags"),
	LOG_REPORT("derp-log-mq","log-report-tags"),
	TIMER_CONSUME_FAIL("derp-log-mq","log-consumefail-tags"),
	LOG_MOVE("derp-log-mq","log-move-tags"),
	/**------------------发送邮件-end----------------------------*/
	//库存扣减失败重推
	TIMER_LOWER_FAIL("derp-log-mq","log-lowerfail-tags"),
	
	/**------------------智能运维----------------------------*/
	MQ_FAILTYPE_01("derp-autolog-mq","mq-failtype-01"),
	MQ_FAILTYPE_02("derp-autolog-mq","mq-failtype-02"),
	MQ_FAILTYPE_03("derp-autolog-mq","mq-failtype-03"),
	MQ_FAILTYPE_04("derp-autolog-mq","mq-failtype-04"),
	MQ_FAILTYPE_05("derp-autolog-mq","mq-failtype-05"),
	
	/**------------------关闭日志监控----------------------------*/
	LOG_CONSUME_COLSE("derp-log-mq","log-monitor_colse-tags");
	// topic
	private String topic;
	// tags
	private String tags;

	/**
	 * 构造器
	 * 
	 * @param topic
	 * @param tags
	 */
	private MQLogEnum(String topic, String tags) {
		this.topic = topic;
		this.tags = tags;
	}

	public static void main(String[] args) {

	}

	public String getTopic() {
		return topic;
	}

	public String getTags() {
		return tags;
	}

}
