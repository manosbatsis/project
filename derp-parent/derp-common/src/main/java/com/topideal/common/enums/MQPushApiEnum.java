package com.topideal.common.enums;

public enum MQPushApiEnum {
	// ===========异步推送外部API===========================
	// 推跨境宝MQ
	PUSH_EPASS("derp-push-api-mq", "push-epass-tags"),
	// 推众邦云仓
	PUSH_YWMS("derp-push-api-mq", "push-ywms-tags"),
	// 推OPMQ
	PUSH_OP("derp-push-api-mq", "push-op-tags"),
	// 推OFC MQ
	PUSH_OFC("derp-push-api-mq", "push-ofc-tags"),
	// 推主数据MQ
	PUSH_MAINDATA("derp-push-api-mq", "push-maindata-tags"),
	// 推送卓普信商品
	PUSH_SAPIENCE("derp-push-api-mq", "push-sapience-tags"),

	// 推送api日志通用请求
	PUSH_API_LOG_COMMON("derp-push-api-mq", "push-api-log-common-tags");

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
	private MQPushApiEnum(String topic, String tags) {
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
