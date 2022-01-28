package com.topideal.common.enums;

public enum MQReportEnum {
	
	//同步数据
	SYS_MONGO_DATA("derp-report-mq", "report-sys-mongo-data-tags"),
	//同步数据
	SYS_DATA("derp-report-mq", "report-sys-data-tags"),
	//数据同步检查
	SYN_CHECK("derp-report-mq", "report-syn-check-tags"),
    //定期删除三个月前数据
    TIMING_SYS_DEL_DATA("derp-report-mq", "report-timing-sys-del-data-tags"),
	// 标准成本单价波动预警MQ
	TIMER_DERP_PURCHASE_IDEPOT_ORDER("derp-report-mq","grab-derp-purchase-idepot-order-tags"),
	//实时库存快照-每日
	INVENTORY_REAL_TIME_SNAPSHOT("derp-report-mq", "report-inventory-real"),
	//生成采销日报天数据
    GENERATE_YUNJI_PURCHASE("derp-report-mq","generate-yunji-purchase-tags"),
    // 生成云集采销日报(新)
    YUNJI_DAILY_SALES_REPORT("derp-report-mq","yunji-daily-sales-report-tags"),
	//生成采销日报天数据 根据起始和结束时间
    GENERATE_YUNJI_PURCHASE_BY_DATE("derp-report-mq","generate-yunji-purchase-by-date"),
    //生成采销日报天数据
    GENERATE_SALE_PURCHASE("derp-report-mq","generate-sale-purchase-tags"),
    //删除月结明细数据
    DEL_MONTHLY_ACCOUNT_ITEM("derp-report-mq","del-monthly-account-item-tags"),
    //生成事业部财务经销存汇总报表
  	BU_FINANCE_SUMMARY("derp-report-mq", "report-bu-finance-summary-tags"),
  	//生成事业部业务经销存汇总报表
  	BU_INVENTORY_SUMMARY("derp-report-mq", "report-bu-inventory_summary-tags"),
  	// 报表数据处理
  	BU_DARA_MANAGE("derp-report-mq", "report-data-manage-tags"),
    
    /*************************************更新商品信息*******************************************/
	UPDATE_REPORT_GOODS_INFO("derp-report-mq","update-report-goods-info-tags"),
	//删除报表对应的数据
	DEL_REPORT_DATAS("derp-report-mq","del-report-data-tags"),
	//在库天数报表
	IN_WAREHOUSE_DAYS("derp-report-mq","in-warehouse-date-tags"),
	//唯品PO账单核销报表
	VIP_PO_BILL_VERI("derp-report-mq","vip_po_bill_veri"),
	//唯品自动校验报表
	VIP_AUTO_VERI("derp-report-mq","vip-auto-veri"),
	//云集自动校验报表
	YUNJI_AUTO_VERI("derp-report-mq","yunji-auto-veri"),
	//业财自核报表
	INVENTORY_FINANCE_AUTO_VERI("derp-report-mq","inventory-finance-auto-veri"),
	//月结自动校验报表
	MONTHLY_ACCOUNT_AUTO_VERI("derp-report-mq","monthly-account-auto-veri"),
	//存货跌价准备
	INVENTORY_FALLING_PRICE_RESERVES("derp-report-mq" , "inventory-falling-price-reserves") ,
	//删除备份库数据
	DEL_BXM_DATAS("derp-bxm-mq","del-bxm-data-tags"),
	//首页POP-一件代发统计数据每天生成数据
	ORDER_STATISTICS_DAILY("derp-report-mq" , "order_statistics_daily"),
	//销售目标达成率
	SALES_TARGET_ACHIEVEMENT("derp-report-mq","sales-target-achievement-tags"),
	//迁移收发明细到历史表
	REPORTDATA_MOVETOHISTORY("derp-report-mq", "reportdata_movetohistory"),
	//异常监控预警
	ABNORMAL_MONITORING("derp-report-mq", "abnormal_monitoring"),
	//(销售洞察)库存量分析数据生成
	INVENTORY_ANALYSIS_GENERATE("derp-report-mq", "inventory-analysis-generate-tags"),
	//同步商品到dstp
	DSTP_SYNGOODS("derp-report-mq", "dstp-syngoods-tags"),
	//同步订单到dstp
	DSTP_SYNORDER("derp-report-mq", "dstp-synorder-tags");

	// topic
	private String topic;
	// tags
	private String tags;

	/**
	 * 构造器
	 * @param topic
	 * @param tags
	 */
	private MQReportEnum(String topic, String tags) {
		this.topic = topic;
		this.tags = tags;
	}

	public String getTopic() {
		return topic;
	}

	public String getTags() {
		return tags;
	}
}
