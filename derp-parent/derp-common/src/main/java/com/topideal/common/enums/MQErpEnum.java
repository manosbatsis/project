package com.topideal.common.enums;

public enum MQErpEnum {

	SYNC_CL_GOODS("derp-erp-mq","sync-cl-goods"),
	GET_CURRENCY_RATE("derp-erp-mq","get-currency-rate-tags"),
	UPDATE_COMMBARCODE_BRAND("derp-erp-mq","update-commbarcode-brand-tags"),
	COPY_MERCHANDISE("derp-erp-mq","copy-merchandise-tags"),//宝信和卓普信商品拷贝
	SEND_SAPIENCE_GOODS("derp-erp-mq","send-sapience-goods-tags"),//向卓普信推送商品档案
	SEND_FINANCE_GOODS("derp-erp-mq","send-finance-goods-tags"),//向金融推送商品档案
	GET_OREAL_GOODS("derp-erp-mq","get-oreal_goods-tags"),// 获取欧莱雅商品数据
    USER_SYN_IDM("derp-erp-mq","user-syn-idm-tags"),//从idm同步用户
    SEND_REMINDER_MAIL("derp-erp-mq","send-reminder-mail-tags"),//发送邮件提醒
    GENERATE_COST_PRICE_DIFFERENCE("derp-erp-mq","generate-cost-price-difference-tags");//生成成本单价差异



	
    //topic
    private String topic;
    //tags
    private String tags;
    
	 /**
     * 构造器
     * @param topic
     * @param tags
     */
    private MQErpEnum(String topic, String tags){
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
