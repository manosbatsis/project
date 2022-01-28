package com.topideal.common.enums;

public enum MQStorageEnum {
	
	//盘点结果推送
	EPASS_STORAGE_RESULTS_PUSH("derp-storage-mq","storage-results-push-tags"),
	//仓储模块 退运信息推送
	EPASS_STORAGE_RETURN_MESSAGE_PUSH("derp-storage-mq","storage-return-message-push-tags"),
	//类型调整推送-自营库存更新
	EPASS_STORAGE_SELF_INVENTORY_PUSH("derp-storage-mq","storage-self-inventory-push-tags"),
    //类型调整推送-调拨单回推
    EPASS_STORAGE_TRANSFER_ORDER("derp-storage-mq","storage-transfer-order-tags"), 
    //类型调整推送-自然过期
    DERP_STORAGE_NATURAL_EXPIRE("derp-storage-mq","storage-natural-expire-tags"),   
    //库存调整内部推送-月结库存转接
    DERP_MONTH_INVENTORY_CARRY("derp-storage-mq","storage-month-inventory-carry-tags"),
    // ofc盘点结果推送
    EPASS_OFCSTORAGE_RESULTS_PUSH("derp-storage-mq","storage-ofcresults-push-tags"),
    //大货理货接口
    EPASS_BIG_CARGO_TALLY_PUSH("derp-storage-mq","storage-big-cargo-push-tally-tags"),
    //库存调整内部—唯品库存调整单据
    DERP_VIP_INVENTORY_ADJUST("derp-storage-mq", "storage-vip-red-inventory-adjust-tags"),
  //库存调整内部—唯品盘点结果单据
    DERP_VIP_STOCK_RESULTS("derp-storage-mq", "storage-vip-stock-results-tags"),

    /*************************************更新商品信息*******************************************/
	UPDATE_STORAGE_GOODS_INFO("derp-storage-mq","update-storage-goods-info-tags");





	
	  //topic
    private String topic;
    //tags
    private String tags;
    
	 /**
     * 构造器
     * @param topic
     * @param tags
     */
    private MQStorageEnum(String topic, String tags){
         this.topic=topic;
         this.tags=tags;
    }


    public static void main(String[] args){
       
    }


    public String getTopic() {
        return topic;
    }

    public String getTags() {
        return tags;
    }

}
