package com.topideal.tools;

/**
 * Created by weixiaolei on 2018/1/9.
 */
public enum CollectionEnum {
    //=============日志监控=======================
    //API日志监控
    API_MONITOR("api_log"),
    //MQ日志
    MQ_LOG("mq_log"),
    //业务MQ日志
    MQ_ORDER_LOG("mq_order_log"),
    //库存MQ日志
    MQ_INVENTORY_LOG("mq_inventory_log"),
    //仓储MQ日志
    MQ_STORAGE_LOG("mq_storage_log"),
    //MQ推送外部API日志
    MQ_PUSH_API_LOG("mq_push_api_log"),
    //爬虫账单生成出库清单日志
  	CRAWLER_BILL_LOG("crawler_bill_log"),
  	// 丢失的日志
  	LOSE_LOG("lose_log"),

  	//==============历史日志监控=========================
    //API历史日志监控
    API_MONITOR_HISTORY("api_history_log"),
    //业务MQ历史日志
    MQ_ORDER_HISTORY_LOG("mq_order_history_log"),
    //库存MQ历史日志
    MQ_INVENTORY_HISTORY_LOG("mq_inventory_history_log"),
    //仓储MQ历史日志
    MQ_STORAGE_HISTORY_LOG("mq_storage_history_log"),
    //MQ推送外部API历史日志
    MQ_PUSH_API_HISTORY_LOG("mq_push_api_history_log"),

    //=============业务MONGO=======================
    //附件信息
    ATTACHMENT_INFO("attachment_info"),
    //采购执行配置表
    PURCHASE_COMMISSION("purchase_commission"),
    //商品表
    MERCHANDISE_INFO("merchandise_info"),
    //商品附表
    MERCHANDISE_SCHEDULE("merchandise_schedule"),
    //公司表
    MERCHANT_INFO("merchant_info"),
	//公司事业部表
    MERCHANT_BU_REL("merchant_bu_rel"),
    //公司仓库事业部表
    MERCHANT_DEPOT_BU_REL("merchant_depot_bu_rel"),
    //客户表
    CUSTOMER_INFO("customer_info"),
    //客=销售价格管理
    CUSTOMER_SALE_PRICE("customer_sale_price"),
  //客户公司关联表
    CUSTOMER_MERCHANT_INFO("customer_merchant_rel"),
    //仓库表
    DEPOT_INFO("depot_info"),
    //仓库商家关联表
    DEPOT_MERCHANT_REL("depot_merchant_rel"),
    //api密钥配置
    API_SECRET_CONFIG("api_secret_config"),
    //对外接口配置
    API_EXTERNAL_CONFIG("api_external_config"),
    //库位映射
    INVENTORY_LOCATION_MAPPING("inventory_location_mapping"),
	//包装方式
    PACK_TYPE("pack_type"),
	//
	LBX_NO("lbx_no"),
	//商品组合关系表
	MERCHANDIES_GROUP_REL("merchandise_group_rel"),
	//合同
	CONTRACT_NO("contract_no"),
	//
	PRODUCT_INFO("product_info"),
	//原产国
	COUNTRY_ORIGIN("country_origin"),
	//报表日志
	REPORT_LOG("mq_report_log"),
	//品牌
	BRAND("brand"),
	//标准条码
	COMMBARCODE("commbarcode"),
	//标准品牌
	BRAND_PARENT("brand_parent"),
	//爬虫配置表
	Reptile_Config("reptile_config"),
	//用户事业部
	USER_BU_REL("user_bu_rel"),
    //用户事业部
    USER_MERCHANT_REL("user_merchant_rel"),
	//店铺货主表
    MERCHANT_SHOP_SHIPPER("merchant_shop_shipper"),
    //商家店铺关联表
    Merchant_Shop_Rel("merchant_shop_rel"),
	//批次效期强校验明细
	BATCH_VALIDATION_INFO("batch_validation_info"),
	//仓库附表
	DEPOT_SCHEDULE("depot_schedule"),
	// 邮件发送配置表
	EMAIL_CONFIG("email_config"),
	// 异常单号池表
	MQ_EXCEPTION_ORDER_POOL("mq_exception_order_pool"),
	// 异常单号池历史表
	MQ_EXCEPTION_ORDER_HISTORY_POOL("mq_exception_order_history_pool"),
	// 财务经销存关账表
	FINANCE_CLOSE_ACCOUNTS("finance_close_accounts"),
	// 事业部库存mongo
	BU_INVENTORY("bu_inventory"),
    // 汇率mongo
    EXCHANGE_RATE("exchange_rate"),
    //文件模版
    FILE_TEMP_DATA("file_temp_data"),
    // 供应商商品价格表
    SUPPLIER_MERCHANDISE_PRICE("supplier_merchandise_price"),
    // 单位表
    UNIT_INFO("unit_info"),
	//母品牌
	BRAND_SUPERIOR("brand_superior"),
	//仓库关区关联表
    DEPOT_CUSTOMS_REL("depot_customs_rel"),
    //商品关区关联表
    MERCHANDISE_CUSTOMS_REL("merchandise_customs_rel"),
    //部门管理表
    DEPARTMENT_INFO("department_info"),
    //事业部表
    BUSINESS_UNIT("business_unit"),
    //外仓备案商品表
    MERCHANDISE_EXTERNAL_WAREHOUSE("merchandise_external_warehouse"),
    //税率配置
    TARIFFRATE_CONFIG("tariff_rate_config"),
    //文件任务
    FILE_TASK("file_task"),
    // 商品仓库关系表
    MERCHANDISE_DEPOT_REL("merchandise_depot_rel"),
    // 用户信息
    USER_INFO("user_info"),
    // 用户信息
    BU_STOCK_LOCATION_TYPE_CONFIG("bu_stock_location_type_config"),
    //固定成本价盘
    FIXED_COST_PRICE("fixed_cost_price");


    private String collectionName;

    private CollectionEnum(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCollectionName() {
        return collectionName;
    }
}
