package com.topideal.common.enums;

/**
 * 库存回推业务枚举
 * @author 杨创
 */
public enum MQPushBackOrderEnum {

	// 采购 在途仓入库
	ON_THE_WAY_PUSH_BACK("derp-order-mq","on-the-way-push-back-tags"),
	// 采购 确认入仓
//	CONFIRM_DEPOT_PUSH_BACK("derp-order-mq","confirm-depot-push-back-tags"),
	// 采购 手动入库勾稽入仓
	//MANUAL_ANALYSIS_PUSH_BACK("derp-order-mq","manual-analysis-push-back-tags"),
	// 采购 进仓状态回推
	WAREHOUSE_STATUS_PUSH_BACK("derp-order-mq","warehouse-status-push-back-tags"),
	// 众邦云仓采购入库回推
	YWMS_PURCHASE_WAREHOUSE_PUSH_BACK("derp-order-mq","ywms-purchase-warehouse-push-back-tags"),
	// 采购 退货出库回推
	PURCHASE_RETURN_ODEPOT_PUSH_BACK("derp-order-mq","purchase-return-odepot-push-back-tags"),
	// 采购 预申报打托入库库存回推
	DECLARE_DELIVERY_PUSH_BACK("derp-order-mq","declare-delivery-push-back-tags"),
	// 销售 在途仓出库
	SALE_ORDER_STOCK_OUT_PUSH_BACK("derp-order-mq","sale-order-stock-out-push-back-tags"),
	// 销售出库
	SALE_OUT_DEPOT_PUSH_BACK("derp-order-mq","sale-out-depot-push-back-tags"),
	// 采购红冲回推
	PURCHASE_WRITE_OFF_PUSH_BACK("derp-order-mq","purchase-write-off-push-back-tags"),

	/**-销售--库存回调通知start--------------------------------*/
	// 电商订单-装载交运库存扣减成功回推 mq
	LOADINF_DELIVRIES_PUSH_BACK_2_1("derp-order-mq", "loading-deliveries-push-back-tags-2-1"),
	// 销售订单-装载交运库存扣减成功回推 mq 已废弃
	//LOADINF_DELIVRIES_PUSH_BACK_2_2("derp-order-mq", "loading-deliveries-push-back-tags-2-2"),
	// 销售订单-退运单信息推送库存扣减成功回推 mq
	/*RETURN_MESSAGE_PUSH_BACK_2("derp-order-mq", "return-message-push-back-tags-2"),*/
	// 销售退货订单-进仓单状态库存扣减成功回推 mq
	WAREHOUSE_STATUS_PUSH_BACK_2("derp-order-mq", "wrehouse-status-push-back-tags-2"),
	// 销售订单-调拨出库库存扣减成功回推 mq
	//TRANSFER_OUT_STIRAGE_PUSH_BACK_2_1("derp-order-mq", "transfers-out-storage-push-back-tags-2-1"),
	// 销售退货订单-调拨入库库存扣减成功回推 mq
	TRANSFER_IN_STIRAGE_PUSH_BACK_2("derp-order-mq", "transfers-in-storage-push-back-tags-2"),
	// 销售订单退货-调拨出库库存扣减成功回推 mq
	TRANSFER_OUT_STIRAGE_PUSH_BACK_2_2("derp-order-mq", "transfers-out-storage-push-back-tags-2-2"),
	//爬虫生成出库单库存扣减成功回推 mqstock-out-
	CRAWLER_STOCK_OUT_PUSH_BACK("derp-order-mq", "crawler-push-back-tags"),
	// 电商订单退货-回推 mq
	ORDER_RETURN_IDEPOT_PUSH_BACK("derp-order-mq", "order-return-idepot-push-back-tags"),

	// 销售订单退货-(出库打托)出库库存扣减成功回推 mq
	SALERETURN_OUTDEPOT_REPORT__PUSH_BACK("derp-order-mq", "salereturn-outdepot-report-push-back-tags"),

	// 消费者销售退入库库存扣减成功回推 mq
	SALE_ORDER_STOCK_IN_PUSH_BACK("derp-order-mq", "sale-order-stock-in-push-back"),
	//销售退上架入库库存扣减成功回推 mq
	SALER_ETURN_IDEPOT_IN_PUSH_BACK("derp-order-mq", "sale-return-idepot-in-push-back"),
	// 销售上架入库库存扣减成功回推 mq
	SALE_SHELF_IN_DEPOT_PUSH_BACK("derp-order-mq", "sale-shelf-in-depot-push-back"),
	//定时生成云集爬虫退货入库单库存扣减成功回推mq
	CRAWLER_YUNJI_RETURN_PUSH_BACK("derp-order-mq","crawler-yunji-return-push-back-tags"),
	//爬虫出库单库存回调通知接口保存批次信息 mq
	CRAWLER_INVENTORY_DETAIL_PUSH_BACK("derp-order-mq","crawler-inventory-detail-push-back-tags"),
	//移库单审核生成商品收发明细成功回推 mq
	MOVEORDER_INVENTORY_DETAIL_PUSH_BACK("derp-order-mq","moveorder-inventory-detail-push-back-tags"),
	//库位调整单审核生成商品收发明细成功回推 mq
	INVENTORY_LOCATION_ADJUSTMENT_DETAIL_PUSH_BACK("derp-order-mq","inventory-location-adjustment-detail-push-back-tags"),
	// 众邦云仓电商出库接口回推
	YWMS_DSORDER_OUTDEPOT_PUSH_BACK("derp-order-mq","ywms_dsorder_outdepot_push_back-tags"),
	// 领料单出库
	MATERIAL_OUT_DEPOT_PUSH_BACK("derp-order-mq","material-out-depot-push-back-tags"),
	//销售订单红冲回调
	SALE_WRITE_OFF_PUSH_BACK("derp-order-mq","sale-write-off-push-back-tags"),

	/**-销售--库存回调通知end--------------------------------*/

	/**调拨start--------------------------------*/
	//调拨库存回调通知--出库
    DB_OUTDEPOT_BACK("derp-order-mq","db_outdepot_back"),
    //调拨库存回调通知--入库
    DB_INDEPOT_BACK("derp-order-mq", "db_indepot_back");
  	/**调拨end--------------------------------*/

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
	private MQPushBackOrderEnum(String topic, String tags) {
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
