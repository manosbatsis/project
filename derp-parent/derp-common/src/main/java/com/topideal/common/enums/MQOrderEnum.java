package com.topideal.common.enums;

public enum MQOrderEnum {
	
	/**------------------采购--------------------------------*/
	// 采购 
	// 初理货接口-采购
	EPASS_FIRST_TALLY_1("derp-order-mq", "epass-first-tally-tags-1"),
	// 入库申报接口-采购
	EPASS_STORAGE_DECLARE_1("derp-order-mq", "epass-storage-declare-tags-1"),
	// 进仓单状态回推-采购
	EPASS_WAREHOUSE_STATUS_1("derp-order-mq", "epass-wrehouse-status-tags-1"),
	//出库明细-采购退货
	EPASS_OUT_INVENTORY_1("derp-order-mq","epass-out-inventory-tags-1"),
	//出库明细-采购退货更新出库状态
	EPASS_OUT_INVENTORY_1_UPDATE_STATUS("derp-order-mq","epass-out-inventory-tags-1-update-status"),
	// 装载交运回-采购退货
	EPASS_LOADINF_DELIVRIES_1("derp-order-mq", "loading-deliveries-tags-1"),
	// 众邦云仓采购入库接口回推
	YWMS_PURCHASE_WAREHOUSE("derp-order-mq","ywms-purchase-warehouse-tags"),
	
	PurchaseSd("derp-order-mq","ywms-purchase-warehouse-tags"),
	/**------------------采购----end--------------------------*/
	
   /**------------------销售--------------------------------*/
	// 电商订单-B2C订单接口
	EPASS_B2C_ORDER_2("derp-order-mq", "epass-b2c-order-tags-2"),
	// 电商订单-进境状态回推接口
	EPASS_ENTER_BORDER_STATUS_2("derp-order-mq", "enter-border-status-tags-2"),
	// 电商订单-订单取消状态回推接口
	EPASS_OEDER_CANCE_STATUS_2("derp-order-mq", "order-cance-status-tags-2"),
	// 销售订单-装载交运回推接口
	EPASS_LOADINF_DELIVRIES_2_2("derp-order-mq", "loading-deliveries-tags-2-2"),
	// 销售订单-退运单信息推送
	//EPASS_RETURN_MESSAGE_PUSH_2("derp-order-mq", "return-message-push-tags-2"),
	// 销售订单-调拨出库
	//EPASS_TRANSFER_OUT_STIRAGE_2_1("derp-order-mq", "transfers-out-storage-tags-2-1"),
	// 销售退货订单-进仓单状态回推
	EPASS_WAREHOUSE_STATUS_2("derp-order-mq", "epass-wrehouse-status-tags-2"),
	// 销售退货订单-入库申报接口
	EPASS_STORAGE_DECLARE_2("derp-order-mq", "epass-storage-declare-tags-2"),
	// 销售退货订单-调拨入库
	EPASS_TRANSFER_IN_STIRAGE_2("derp-order-mq", "transfers-in-storage-tags-2"),
	// 销售订单-调拨出库
	EPASS_TRANSFER_OUT_STIRAGE_2_2("derp-order-mq", "transfers-out-storage-tags-2-2"),
	//销售退货-初理货接口
	EPASS_FIRST_TALLY_2("derp-order-mq","epass-first-tally-tags-2"),
	//出库明细-销售 生成出库单
	EPASS_OUT_INVENTORY_2("derp-order-mq","epass-out-inventory-tags-2"),
	//出库明细-销售 扣减库存
	EPASS_OUT_INVENTORY_2_1("derp-order-mq","epass-out-inventory-tags-2-1"),
	// 众邦云仓电商出库发货单接口回推
	YWMS_DSORDER_OUTDEPOT("derp-order-mq","ywms-dsorder-outdepot-tags"),
	//出库明细-领料单 生成出库单
	EPASS_OUT_INVENTORY_4("derp-order-mq","epass-out-inventory-tags-4"),
	//出库明细-领料单 扣减库存
	EPASS_OUT_INVENTORY_4_1("derp-order-mq","epass-out-inventory-tags-4-1"),
	// 领料单-装载交运回推接口
	EPASS_LOADINF_DELIVRIES_4("derp-order-mq", "loading-deliveries-tags-4"),
	// 定时器生成客户额度预警
	TIMER_CUSTOMER_QUATO_WARNING("derp-order-mq","customer-quato-warning-tags"),
	/**------------------销售---end--------------------------*/
	
	/**------------------调拨--------------------------------*/
	//初理货接口-调拨
    EPASS_FIRST_TALLY_3("derp-order-mq","epass-first-tally-tags-3"),
    //入库申报接口 -调拨
    EPASS_STORAGE_DECLARE_3("derp-order-mq","epass-storage-declare-tags-3"),
    //进仓单状态回  推 -调拨
    EPASS_WAREHOUSE_STATUS_3("derp-order-mq","epass-wrehouse-status-tags-3"),
    //装载交运-调拨
  	EPASS_LOADINF_DELIVRIES_3("derp-order-mq", "loading-deliveries-tags-3"),
    // 调拨入库 -调拨
  	EPASS_TRANSFER_IN_STIRAGE_3("derp-order-mq","transfers-in-storage-tags-3"),
  	 // 调拨出库 -调拨
  	EPASS_TRANSFER_OUT_STIRAGE_3("derp-order-mq","transfers-out-storage-tags-3"),
    // 退运单信息推送 -调拨
 	/*EPASS_RETURN_MESSAGE_PUSH_3("derp-order-mq","return-message-push-tags-3"),*/
 	//出库明细-调拨
 	EPASS_OUT_INVENTORY_3("derp-order-mq","epass-out-inventory-tags-3"),

 	EPASS_ORDER_STATUS_UPDATE_3("derp-order-mq","epass-order-status-update-tags-3"),
 	/**------------------调拨-end----------------------------*/
	/**------------------爬虫--------------------------------*/
 	//定时器同步爬虫唯品账单
	/*TIMER_CRAWLER_BILL("derp-order-mq","crawler-bill-tags"),*/
 	//定时器同步爬虫唯品账单
 	TIMER_CRAWLER_VIP_BILL("derp-order-mq","crawler-vip-bill-tags"),
 	//定时器同步爬虫唯品账单活动折扣明细
 	TIMER_CRAWLER_VIP_EXTRA_DATA("derp-order-mq","crawler-vip-extra-data-tags"),
	//定时器同步云集爬虫账单
	TIMER_CRAWLER_YUNJI_BILL("derp-order-mq","crawler-bill-yunji-tags"),
	//定时器同步京东平台采购订单
	TIMER_CRAWLER_JD_PURCHASE_ORDER("derp-order-mq","crawler-jd-purchase-order-tags"),
	//定时器同步京东商品-每日销量-库存表
	TIMER_CRAWLER_JD_GOODSANDSTOCKSALE("derp-order-mq","crawler-jd-goods-stocksale-tags"),
	//定时器同步天猫平台采购订单
	TIMER_CRAWLER_TMALL_PURCHASE_ORDER("derp-order-mq","crawler-tmall-purchase-order-tags"),
	//定时器同步考拉平台采购订单
	TIMER_CRAWLER_KL_PURCHASE_ORDER("derp-order-mq","crawler-kl-purchase-order-tags"),
	//定时器同步唯品平台采购订单
	TIMER_CRAWLER_VIP_PURCHASE_ORDER("derp-order-mq","crawler-vip-purchase-order-tags"),
	//定时器同步天猫支付数据
	TIMER_CRAWLER_TMALL_ALIPAY_DATA("derp-order-mq","crawler-tmall-alipay-data-tags"),
	//定时器获取菜鸟电商订单数据 RookieOrderData
	 TIMER_CRAWLER_ROOKIE_ORDER_DATA("derp-order-mq","crawler-rookie-order-data-tags"),
	//消费单个菜鸟电商订单数据 RookieOrderData
	TIMER_CRAWLER_ROOKIE_GET_ONE_ORDER("derp-order-mq","crawler-rookie-get-one-order-tags"),

	// 定时器生成采购额度预警
	TIMER_PROJUCET_QUATO_WARNING("derp-order-mq","projucet-quato-warning-tags"),
	// 定时器生成平台采购订单
	TIMER_PLATFORM_PURCHASE_ORDER("derp-order-mq","platform-purchase-order-tags"),
	// 定时器 平台采购单未上架邮件提醒
	TIMER_PLATFORM_PUR_NOTSHALF_MESSAGE("derp-order-mq","platform-pur-notshalf-message-tags"),

	// 定时器生成平台费用订单
	TIMER_PLATFORM_COST_ORDER("derp-order-mq","platform-cost-order-tags"),
	// 定时器生成平台结算订单
	TIMER_PLATFORM_STATEMENT_ORDER("derp-order-mq","platform-statement-order-tags"),
	//定时器同步云集结算爬虫账单
	TIMER_YUNJI_ACCOUNT_DATA("derp-order-mq","yunji-account-data-tags"),
	//定时器生成唯品销售出库单
	/*TIMER_SALE_OUT_DEPOT("derp-order-mq","create-sale-out-depot-tags"),*/
	//定时器生成唯品销售出库单
	TIMER_SALE_OUT_VIP_DEPOT("derp-order-mq","create-sale-out-vip-depot-tags"),
	//定时器抓取云集爬虫数据
	TIMER_YUNJI_INVENTORY_DATA("derp-order-mq","yunji-inventory-data-tags"),
	//定时生成云集爬虫退货入库单和 销售出库清单
	TIMER_YUNJI_DELIVERY_RETURN_DETAIL("derp-order-mq","yunji-delivery-return-detail-tags"),
	//定时器采购本位币金额回填
 	TIMER_PURCHASE_AMOUNT_BACKFILL("derp-order-mq","purchase-amount-backfill-tags"),
 	//定时器采购SD单生成
 	TIMER_PURCHASE_SD_ORDER("derp-order-mq","purchase-sd-order-tags"),
	//定时器邮件提醒采购发票预计付款
	TIMER_PURCHASE_INVOICE_PAYMENTDATE_ORDER("derp-order-mq","purchase-invoice-paymentdate-tags"),
	//定时器内部交易金额数量差异提醒定时任务
	TIME_ITERNAL_QUANTITY_VARIANCE_ORDER_ENUM("derp-order-mq","internal-quantity-variance-tags"),	
	//定时器获取电商售后退款订单数据
	TIMER_CRAWLER_ORDER_RETURN_IDEPOT("derp-order-mq","crawler-order-return-idepot-tags"),

	/**------------------爬虫-end----------------------------*/
	// 抓取寄售商e仓发货订单
	TIMER_EWS_DELIVERY_ORDER("derp-order-mq","grab-ews-delivery-order-tags"),
	//拆分成单个消费抓取寄售商e仓发货订单
	GETONE_EWS_DELIVERY_ORDER("derp-order-mq","getone-grab-ews-delivery-order-tags"),
	// 定时抓取经分销收到发票没有付款的采购订单
	TIMER_DERP_PURCHASE_ORDER("derp-order-mq","grab-derp-purchase-order-tags"),
	//抓取蓝精灵订单采集发货和完成数据
	TIMER_SMURFS_ORDER_COLLECTION_ORDER("derp-order-mq","grab-smurfs-order-collection-order-tags"),
	//拆分成单个消费蓝精灵订单采集订单
	GETONE_SMURFS_ORDER_COLLECTION_ORDER("derp-order-mq","getone-smurfs-order-collection-order-tags"),

	
	/*************************************菜鸟仓库*******************************************/
	// 菜鸟电商订单-装载交运回推接口
	EPASS_ROOKIE_LOADINF_DELIVRIES_2_1("derp-order-mq", "rookie-loading-deliveries-tags-2-1"),
	// 通过过业务端生成报文从推库存触发接口
	GET_BUSINESS_DATA_PUSH_INVENTORY("derp-order-mq", "get-business-data-push-inventory-tags"),
	
	/*************************************更新商品信息*******************************************/
	UPDATE_ORDER_GOODS_INFO("derp-order-mq","update-order-goods-info-tags"),

	/*************************************宝洁商品货期配置*******************************************/
	//宝洁商品货期配置
	//BJ_GOODS_TIME_CONFIG("derp-order-mq","bj-goods-time-config-tags"),
	/*************************************应收*******************************************/
	//收款核销跟踪 生成和刷新MQ
	RECEIVE_BILL_VERIFICATION ("derp-order-mq","receive-bill-verification-tags"),
	//生成应收账单
	RECEIVE_BILL_GENERATE ("derp-order-mq","receive-bill-generate-tags"),
	//定时器NC账单状态查询接口获取应收账单状态
	TIMER_RECEVICE_BILL_BACKFILL("derp-order-mq","recevice-bill-backfill-tags"),
	//定时器NC凭证号查询接口获取应收账单凭证
	TIMER_RECEVICE_BILL_VOUCHER_BACKFILL("derp-order-mq","recevice-bill-voucher-backfill-tags"),
	//定时器NC账单状态查询接口获取预收账单状态
	TIMER_ADVANCE_BILL_BACKFILL("derp-order-mq","advance-bill-backfill-tags"),
	//定时器NC凭证号查询接口获取预收账单凭证
	TIMER_ADVANCE_BILL_VOUCHER_BACKFILL("derp-order-mq","advance-bill-voucher-backfill-tags"),
	//定时器生成 To C暂估应收表
	TIMER_ToC_Temp_RECEVICE_BILL("derp-order-mq","toc-temp-recevice-bill-tags"),
	//To C暂估应收核销To C结算单
	TOC_TEMP_RECEIVE_BILL_VERIFY("derp-order-mq", "toc-temp-receive-bill-verify-tags"),
	//To 结算单审核核销toc暂估应收
	TOC_RECEIVE_BILL_VERIFY("derp-order-mq", "toc-receive-bill-verify-tags"),
	//定时器NC账单状态查询接口获取To C应收账单状态
	TIMER_TOC_RECEVICE_BILL_BACKFILL("derp-order-mq","toc-recevice-bill-backfill-tags"),
	//定时器NC凭证号查询接口获取ToC应收账单凭证
	TIMER_TOC_RECEVICE_BILL_VOUCHER_BACKFILL("derp-order-mq","toc-recevice-bill-voucher-backfill-tags"),
	//ToC 结算单生成
	TOC_RECEIVE_BILL_GENERATE ("derp-order-mq","toc_receive-bill-generate-tags"),
	//ToC 结算单生成(爬虫库)
	TOC_RECEIVE_BILL_BY_CRAWLER_GENERATE ("derp-order-mq","toc_receive-bill-by-crawler-generate-tags"),
	//ToC 结算单生成(蓝精灵)
	TOC_RECEIVE_BILL_BY_SMURFS_GENERATE ("derp-order-mq","toc_receive-bill-by-smurfs-generate-tags"),
	//根据平台结算单生成To结算单
	TOC_RECEIVE_BILL_GENERATE_BY_STATEMENT("derp-order-mq", "toc_receive-bill-generate-by-statement-tags"),
	//定时器生成 To B暂估应收表
	TOB_RECEIVE_BILL_GENERATE("derp-order-mq", "tob_receive-bill-generate-tags"),
	//定时器生成平台暂估费用单
	TIMER_PLATFORM_TEMPORARY_COST_ORDER("derp-order-mq", "platform-temporary-cost-order-tags"),
	//定时器生成应收账龄报告
	TIMER_RECEIVEAGING_REPORT_ORDER("derp-order-mq", "receive-aging-report-order-tags"),
	//定时器自动生成应收账单
	TIMER_GENERATE_RECEIVE_BILL("derp-order-mq", "generate-receive-bill-tags"),
	//定时器NC核销记录查询接口
	TIMER_RECEICE_BILL_NC_VERIFY_BACK("derp-order-mq", "receive-bill-nc-verify-back-tags"),
	//定时器生成应收月结快照
	TIMER_GENERATE_MONTHLY_SNAPSHOT("derp-order-mq", "receive-monthly-snapshot-tags"),
	//定时器生成tob暂估月结报表
	TIMER_GENERATE_TOB_TEMP_MONTHLY_REPORT("derp-order-mq", "generate-tob-temp-monthly-report-tags"),

	//定时器生成 To C暂估月结快照
	TIMER_GENERATE_TOC_TEMP_MONTHLY_SNAPSHOT("derp-order-mq","toc-temp-monthly-snapshot-tags"),
	//定时器生成 应收关账数据
	TIMER_GENERATE_RECEIVE_CLOSE_ACCOUNT("derp-order-mq","receive-close-account-tags"),
	//定时器收款核销回填赊销单
	TIMER_RECEIVE_BACKFILL_CREDIT("derp-order-mq","receive-receive-backfill-credit-tags"),
	/***************************************定时器检测数据邮件预警 start************************************/
	TIMER_ALERT_MEAIL_ORDER_DATA("derp-order-mq", "check-derp-order-data-alert-email-tags"),
	/***************************************定时器检测数据邮件预警 end************************************/

	/***************************************迁移数据到历史表***************************************/
	//迁移收发明细到历史表
	ORDERDATA_MOVETOHISTORY("derp-order-mq", "orderdata_movetohistory"),
	// 业务库回滚数据
	ORDERDATA_ROLLBACK("derp-order-mq", "orderdata_rollback"),
	/***************************************电商订单金额导入覆盖***************************************/
	// 电商订单金额导入覆盖
	AMOUNT_COVER("derp-order-mq", "amount_cover_tags"),
	//嘉宝的采购SD单同步到宝信定时器
	TIMER_SYN_PURCHASE_SD_JB_TO_BX("derp-order-mq","syn-purchase-sd-jb-to-bx-tags"),
	//电商订单分摊税费运费
	TIMER_ORDER_APPORTION_TAX_FREIGHT("derp-order-mq","order-apportion-tax-freight-tags"),
	/************************获取欧莱雅***********************************/
	GET_OREAL_PURCHASE_ORDERS("derp-order-mq","get-oreal-purchase-orders-tags"),// 获取欧莱雅采购订单数据;

	/***************************************OA审批推送日志-预付和应付***************************************/
	OAEXAMINE_ACOUNT_LOG("derp-order-mq","oa_examine_acount_log"),//获取OA审批日志
	OAEXAMINE_ACOUNT_RESULT("derp-order-mq","oa_examine_acount_result"),//获取OA审批结果
	
	//销售订单商品同步到金融系统
	TIMER_SYN_GOODSINFO_TO_FINANCE("derp-order-mq","syn-goodsinfo-to-finance"),
	//定时器生成内部公司应付账单
	TIMER_GENERATE_INNER_MERCHANT_PAYMENT_BILL("derp-order-mq","generate-inner-merchant-payment-bill"),

	/***************************************从OA获取采购列表***************************************/
	GET_PURCHASE_FRAME_CONTRACT("derp-order-mq", "get-purchase-frame-contract"),
	GET_PURCHASE_TRY_APPLY_ORDER("derp-order-mq", "get-purchase-try-apply-order");

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
	private MQOrderEnum(String topic, String tags) {
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
