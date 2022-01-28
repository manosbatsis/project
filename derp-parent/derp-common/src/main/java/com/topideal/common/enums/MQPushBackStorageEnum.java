package com.topideal.common.enums;

/**
 * 库存仓储回推业务枚举
 * @author 杨创
 */
public enum MQPushBackStorageEnum {
	
	// 盘点结果推送库存加减成功回推 mq	 
	STORAGE_RESULTS_PUSH_BACK("derp-storage-mq","storage-results-push-back-tags"),
	// ofc盘点结果库存加减成功回推 mq
    OFCSTORAGE_RESULTS_PUSH_BACK("derp-storage-mq","storage-ofcresults-push-back-tags"),
    //自营库存更新库存加减成功回推 mq
  	STORAGE_SELF_INVENTORY_PUSH_BACK("derp-storage-mq","storage-self-inventory-push-back-tags"),
  	//仓储退运信息库存加减成功回推 mq
	STORAGE_RETURN_MESSAGE_PUSH_BACK("derp-storage-mq","storage-return-message-push-back-tags"),
	 //调拨单回推库存加减成功回推 mq
    STORAGE_TRANSFER_ORDER_PUSH_BACK("derp-storage-mq","storage-transfer-order-push-back-tags"),
    // 库存调整单页面审核 库存加减成功回推 mq
    STORAGE_ADJUSTMENT_INVENTORY_AUDIT_PUSH_BACK("derp-storage-mq","storage-adjustment-inventory-audit-push-back-tags"),
    //大货理货 库存加减成功回推
    STORAGE_BIG_CARGO_TALLY_PUSH_BACK("derp-storage-mq","storage-big-cargo-tally-push-back-tags"),

	// 手工导入盘点结果单确认推送库存加减成功回推 mq
	STORAGE_RESULTS_CONFIRM_PUSH_BACK("derp-storage-mq","storage-results-confirm-push-back-tags");
	
		
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
	private MQPushBackStorageEnum(String topic, String tags) {
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
