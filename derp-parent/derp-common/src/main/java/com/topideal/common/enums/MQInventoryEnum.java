package com.topideal.common.enums;

/**
 * 库存管理MQ
 *
 * @author 联想302
 *
 */
public enum MQInventoryEnum {

	// 新增减少库存量
	INVENTORY_QUANTITY_ADD_LOWER("derp-inventory-mq", "inventory_quantity_add_lower"),
	// 冻结与释放库存
	INVENTORY_FREEZE_ADD_LOWER("derp-inventory-mq", "inventory_freeze_add_lower"),
	// 月结账单生成
	INVENTORY_MONTHLY_BILL("derp-inventory-mq", "inventory_monthly_bill"),
	// 库存解冻和扣减接口
	INVENTORY_UNFREEZE_OR_LOWER("derp-inventory-mq", "inventory_unfreeze_or_lower"),
	// 批次库存明细效期校验
	INVENTORY_BATCH_VALIDITY_CHECK("derp-inventory-mq", "inventory_batch_validity_check"),
	// 库存批次快照
	INVENTORY_GOODS_BATCH_SNAPSHOT("derp-inventory-mq", "inventory_goods_batch_snapshot"),
	// 实时库存快照
	INVENTORY_REAL_TIME_SNAPSHOT("derp-inventory-mq", "inventory_real_time_snapshot"),
	//Gss实时库存快照
	INVENTORY_GSS_REAL_TIME_SNAPSHOT("derp-inventory-mq", "inventory_gss_real_time_snapshot"),
	//阿里菜鸟实时库存快照
	INVENTORY_ALI_ROOKIE_REAL_TIME_SNAPSHOT("derp-inventory-mq", "inventory_ali_rookie_real_time_snapshot"),
	// 删除库存量为0的数据
	INVENTORY_DELETE_DATA_FOR_ZERO("derp-inventory-mq", "inventory_delete_data_for_zero"),
	// 移动已完结冻结解冻数据到历史表
	INVENTORY_FREEZE_MOVE("derp-inventory-mq", "inventory_freeze_move"),
	// 删除3个月之前数据且不包含每月1号
	INVENTORY_TIMING_DELETE_DATA("derp-inventory-mq", "inventory_timing_delete_data"),
	//生成已经月结的月结账单快照
	INVENTORY_MONTHLY_ACCOUNT_SNAPSHOT("derp-inventory-mq", "inventory_monthly_account_snapshot"),
	//事业部库存接口
	INVENTORY_BU_INVENTORY("derp-inventory-mq", "inventory_bu_inventory"),
	//刷新月结账单
	INVENTORY_REFRESH_MONTHLY_BILL("derp-inventory-mq", "inventory_refresh_monthly_bill"),
	//移库单审核生成商品收发明细
	INVENTORY_DETAIL_BY_MOVEORDER("derp-inventory-mq", "inventory_detail_by_moveorder"),
	//众邦云实时库存快照
	INVENTORY_YWMS_REAL_TIME_SNAPSHOT("derp-inventory-mq", "inventory_ywms_real_time_snapshot"),
	//众邦云实时库存信息回推
	INVENTORY_YWMS_REAL_TIME_SNAPSHOT_PUSH_BACK("derp-inventory-mq", "inventory_ywms_real_time_snapshot_push_back"),
	//迁移收发明细到历史表
	INVENTORY_INVENTORYDETAILS_MOVETOHISTORY("derp-inventory-mq", "inventorydata_movetohistory"),
	//库存同步数据到Mongdb
	INVENTORY_SYS_DATA("derp-inventory-mq", "inventory_sys_data"),
	//库存单据红冲
	INVENTORY_WRITE_OFF("derp-inventory-mq", "inventory_write_off"),

	// ===========内部异步回推接口===========================
	// 经分销库存异步回推接口
	INTERNAL_RETURN_PUSH("derp-inventory-mq", "internal-return-push-tags"),

	/*************************************更新商品信息*******************************************/
	UPDATE_INVENTORY_GOODS_INFO("derp-inventory-mq","update-inventory-goods-info-tags"),
	//库存数据回滚
	INVENTORY_DATA_ROLL_BACK("derp-inventory-mq","inventory-data-roll-back-tags"),

	//爬虫出库单扣减库存成功抓取出库单收发明细信息回推
	CRAWLER_OUT_DEPOT_INVENTORY_LOWER_PUSH_BACK("derp-inventory-mq", "crawler-out-depot-inventory-lower-push-back-tags"),
	//库存数据回滚
	INVENTORY_FREEZE_ROLL_BACK("derp-inventory-mq","inventory-freeze-roll-back-tags");

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
	private MQInventoryEnum(String topic, String tags) {
		this.topic = topic;
		this.tags = tags;
	}

	public static void main(String[] args) {
		System.out.println(MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.topic);
		System.out.println(MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.tags);
	}

	public String getTopic() {
		return topic;
	}

	public String getTags() {
		return tags;
	}

}
