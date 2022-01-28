package com.topideal.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 业务库 表字段状态/类型常量文件
 */
public class DERP_ORDER {

	/** 平台结算单 是否开票 */
	public static final String PLATFORM_STATEMENT_IS_INVOICE_0 = "0";
	public static final String PLATFORM_STATEMENT_IS_INVOICE_1 = "1";
	public static ArrayList<DerpBasic> platformStatement_isInvoiceList = new ArrayList<>();

	/** 平台结算单 是否创建应收 */
	public static final String PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0 = "0";
	public static final String PLATFORM_STATEMENT_IS_CREATE_RECEIVE_1 = "1";
	public static final String PLATFORM_STATEMENT_IS_CREATE_RECEIVE_2 = "2";
	public static ArrayList<DerpBasic> platformStatement_isCreateReceiveList = new ArrayList<>();

	/** 平台结算单 客户类型 1-云集 2-唯品 3-天猫 */
	public static final String PLATFORM_STATEMENT_CUSTOMER_TYPE_1 = "1";
	public static final String PLATFORM_STATEMENT_CUSTOMER_TYPE_2 = "2";
	public static final String PLATFORM_STATEMENT_CUSTOMER_TYPE_3 = "3";
	public static ArrayList<DerpBasic> platformStatement_customerTypeList = new ArrayList<>();

	/** 平台结算单 账单类型 1- to B 2-to C */
	public static final String PLATFORM_STATEMENT_TYPE_1 = "1";
	public static final String PLATFORM_STATEMENT_TYPE_2 = "2";
	public static ArrayList<DerpBasic> platformStatement_typeList = new ArrayList<>();

	/** 平台结算单表体 类型1-销售、2-客退、3-国检、4-盘亏、5-报废、6-盘盈、7-客退补贴、8-活动折扣、9-补偿折扣 */
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_1 = "1";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_2 = "2";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_3 = "3";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_4 = "4";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_5 = "5";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_6 = "6";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_7 = "7";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_8 = "8";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_9 = "9";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_10 = "10";
	public static final String PLATFORM_STATEMENT_ITEM_TYPE_11 = "11";
	public static ArrayList<DerpBasic> platformStatement_itemTypeList = new ArrayList<>();

	/* NC收支费项类型 */
	/* 状态：0-已禁用 1-使用中 */
	public static final String RECEIVE_PAYMENT_SUBJECT_TYPE_0 = "0";
	public static final String RECEIVE_PAYMENT_SUBJECT_TYPE_1 = "1";
	public static ArrayList<DerpBasic> receive_payment_subject_typeList = new ArrayList<>();

	/* 结算类型 */
	/* 状态：1-应收 2-预收 */
	public static final String SETTLEMENT_TYPE_1 = "1";
	public static final String SETTLEMENT_TYPE_2 = "2";
	public static ArrayList<DerpBasic> settlement_typeList = new ArrayList<>();

	/* NC渠道编码枚举项 */
	public static final String CHANNEL_CODE_001 = "001";
	public static final String CHANNEL_CODE_002 = "002";
	public static final String CHANNEL_CODE_003 = "003";
	public static final String CHANNEL_CODE_004 = "004";
	public static final String CHANNEL_CODE_005 = "005";
	public static final String CHANNEL_CODE_006 = "006";
	public static final String CHANNEL_CODE_007 = "007";
	public static final String CHANNEL_CODE_008 = "008";
	public static final String CHANNEL_CODE_9999 = "9999";
	public static ArrayList<DerpBasic> channel_codeList = new ArrayList<>();

	/* NC平台编码枚举项 */
	public static final String PLATFORM_CODE_001 = "001";
	public static final String PLATFORM_CODE_002 = "002";
	public static final String PLATFORM_CODE_003 = "003";
	public static final String PLATFORM_CODE_004 = "004";
	public static final String PLATFORM_CODE_005 = "005";
	public static final String PLATFORM_CODE_006 = "006";
	public static final String PLATFORM_CODE_007 = "007";
	public static final String PLATFORM_CODE_008 = "008";
	public static final String PLATFORM_CODE_009 = "009";
	public static final String PLATFORM_CODE_010 = "010";
	public static final String PLATFORM_CODE_011 = "011";
	public static final String PLATFORM_CODE_012 = "012";
	public static final String PLATFORM_CODE_013 = "013";
	public static final String PLATFORM_CODE_014 = "014";
	public static final String PLATFORM_CODE_015 = "015";
	public static final String PLATFORM_CODE_016 = "016";
	public static final String PLATFORM_CODE_017 = "017";
	public static final String PLATFORM_CODE_018 = "018";
	public static final String PLATFORM_CODE_019 = "019";
	public static final String PLATFORM_CODE_020 = "020";
	public static final String PLATFORM_CODE_021 = "021";
	public static final String PLATFORM_CODE_022 = "022";
	public static final String PLATFORM_CODE_023 = "023";
	public static final String PLATFORM_CODE_024 = "024";
	public static final String PLATFORM_CODE_025 = "025";
	public static final String PLATFORM_CODE_026 = "026";
	public static final String PLATFORM_CODE_027 = "027";
	public static final String PLATFORM_CODE_998 = "998";
	public static final String PLATFORM_CODE_999 = "999";
	public static ArrayList<DerpBasic> platform_codeList = new ArrayList<>();

	/** 上架单t_shelf------------------------------------- */
	/** 状态：1-已上架 2-已入库 */
	public static final String SHELF_STATUS_1 = "1";
	public static final String SHELF_STATUS_2 = "2";
	public static ArrayList<DerpBasic> shelf_statusList = new ArrayList<>();
	/** 是否生成暂估 0-未生成 1-已生成 */
	public static final String SHELF_ISGENERATE_0 = "0";
	public static final String SHELF_ISGENERATE_1 = "1";
	public static ArrayList<DerpBasic> shelf_isGenerateList = new ArrayList<>();
	/** 是否红冲单：0-否，1-是 */
	public static final String SHELF_ISWRITEOFF_0 = "0";
	public static final String SHELF_ISWRITEOFF_1 = "1";
	public static ArrayList<DerpBasic> shelf_isWriteOffList = new ArrayList<DerpBasic>();

	/** 电商订单t_order------------------------------------- */
	/** 订单状态：1-待申报 2-待放行 8-待审核 3-待发货4-已发货 5-已取消 027-出库中 006-已删除 */
	public static final String ORDER_STATUS_1 = "1";
	public static final String ORDER_STATUS_2 = "2";
	public static final String ORDER_STATUS_8 = "8";
	public static final String ORDER_STATUS_3 = "3";
	public static final String ORDER_STATUS_4 = "4";
	public static final String ORDER_STATUS_5 = "5";
	public static final String ORDER_STATUS_027 = "027";
	public static final String ORDER_STATUS_006 = "006";
	public static ArrayList<DerpBasic> order_statusList = new ArrayList<DerpBasic>();

	/** 进口模式10-BBC 20-BC 30-保留 40-CC */
	public static final String ORDER_IMPORTMODE_10 = "10";
	public static final String ORDER_IMPORTMODE_20 = "20";
	public static final String ORDER_IMPORTMODE_30 = "30";
	public static final String ORDER_IMPORTMODE_40 = "40";
	public static ArrayList<DerpBasic> order_importModeList = new ArrayList<DerpBasic>();

	/** 国检状态 11-已报国检 12-国检放行 13-国检审核驳回 14-国检抽检 15-国检抽检未过 */
	public static final String ORDER_inspectstatus_11 = "11";
	public static final String ORDER_inspectstatus_12 = "12";
	public static final String ORDER_inspectstatus_13 = "13";
	public static final String ORDER_inspectstatus_14 = "14";
	public static final String ORDER_inspectstatus_15 = "15";
	public static ArrayList<DerpBasic> order_inspectStatusList = new ArrayList<DerpBasic>();

	/**
	 * 海关状态 21-已报海关22-海关单证放行23-报海关失败24-海关查验/转人工/挂起等25-海关单证审核不通过
	 * 26-海关已接受申报,待货物运抵后处理41-海关货物查扣42-海关货物放行
	 */
	public static final String ORDER_CUSTOMSSTATUS_21 = "21";
	public static final String ORDER_CUSTOMSSTATUS_22 = "22";
	public static final String ORDER_CUSTOMSSTATUS_23 = "23";
	public static final String ORDER_CUSTOMSSTATUS_24 = "24";
	public static final String ORDER_CUSTOMSSTATUS_25 = "25";
	public static final String ORDER_CUSTOMSSTATUS_26 = "26";
	public static final String ORDER_CUSTOMSSTATUS_41 = "41";
	public static final String ORDER_CUSTOMSSTATUS_42 = "42";
	public static ArrayList<DerpBasic> order_customsStatusList = new ArrayList<DerpBasic>();

	/**是否生成暂估费用 0-未生成 1-已生成*/
	public static final String ORDER_IS_GENERATE_0 = "0";
	public static final String ORDER_IS_GENERATE_1 = "1";
	public static ArrayList<DerpBasic> order_isGenerateList = new ArrayList<DerpBasic>();

	/** 电商订单唯一表t_order_external_code------------------------------------- */
	/** 来源：1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退' 9.应付账单 10预付款单 */
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE1 = "1";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE2 = "2";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE3 = "3";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE4 = "4";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE5 = "5";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE6 = "6";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE7 = "7";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE8 = "8";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE9 = "9";
	public static final String ORDER_EXTERNAl_CODE_ORDER_SOURCE10 = "10";


	/** 电商订单退货t_order_return_idepot------------------------------------- */
	/** 订单来源 1:导入 2.接口数据 */
	public static final String ORDER_RETURN_SOURCE_1 = "1";
	public static final String ORDER_RETURN_SOURCE_2 = "2";
	public static ArrayList<DerpBasic> order_returnSourceList = new ArrayList<DerpBasic>();

	/**单据状态：011:待审核 012:已入仓 028.入仓中 013:已退款*/
	public static final String ORDER_RETURN_STATUS_011 = "011";
	public static final String ORDER_RETURN_STATUS_012 = "012";
	public static final String ORDER_RETURN_STATUS_028 = "028";
	public static final String ORDER_RETURN_STATUS_013 = "013";
	public static ArrayList<DerpBasic> order_returnStatusList = new ArrayList<DerpBasic>();

	/**售后类型：00-仅退款 01-退货退款 */
	public static final String ORDER_RETURN_AFTER_SALE_TYPE_00 = "00";
	public static final String ORDER_RETURN_AFTER_SALE_TYPE_01 = "01";
	public static ArrayList<DerpBasic> order_returnAfterSaleTypeList = new ArrayList<DerpBasic>();

	/**售后单据类型 0-退款单 1-退货单*/
	public static final String ORDERRETURN_RETURN_ORDER_TYPE_0 = "0";
	public static final String ORDERRETURN_RETURN_ORDER_TYPE_1 = "1";
	public static ArrayList<DerpBasic> orderReturn_returnOderTypeList = new ArrayList<DerpBasic>();

	/** 宝洁商品货期配置t_goods_time_config------------------------------------- */
	/** 货期（月）: 0-未维护 2-2 4-4 */
	public static final String GOODSTIMECONFIG_DELIVERYPERIOD_0 = "0";
	public static final String GOODSTIMECONFIG_DELIVERYPERIOD_2 = "2";
	public static final String GOODSTIMECONFIG_DELIVERYPERIOD_4 = "4";
	public static ArrayList<DerpBasic> goodsTimeConfig_deliveryPeriodList = new ArrayList<DerpBasic>();

	/** 模型配置t_model_setting------------------------------------- */
	/** 日销统计类型: 0-平常日 1-平常日+促销日 */
	public static final String MODELSETTING_DAILYSTATISTICALTYPE_0 = "0";
	public static final String MODELSETTING_DAILYSTATISTICALTYPE_1 = "1";
	public static ArrayList<DerpBasic> modelSetting_dailyStatisticalTypeList = new ArrayList<DerpBasic>();

	/** 日销统计范围: 0-1个月 1-3个月 2-6个月 3-年初至今 */
	public static final String MODELSETTING_DAILYSTATISTICALRANGE_0 = "0";
	public static final String MODELSETTING_DAILYSTATISTICALRANGE_1 = "1";
	public static final String MODELSETTING_DAILYSTATISTICALRANGE_2 = "2";
	public static final String MODELSETTING_DAILYSTATISTICALRANGE_3 = "3";
	public static ArrayList<DerpBasic> modelSetting_dailyStatisticalRangeList = new ArrayList<DerpBasic>();

	/** 采购计划表t_purchase_planning------------------------------------- */
	/** 下单状态：01-已下单 00-未下单 */
	public static final String PURCHASEPLANNING_ORDERSTATUS_01 = "01";
	public static final String PURCHASEPLANNING_ORDERSTATUS_00 = "00";
	public static ArrayList<DerpBasic> purchasePlanning_orderStatusList = new ArrayList<DerpBasic>();

	/** 同步状态：01-已同步 00-未同步 */
	public static final String PURCHASEPLANNING_SYNCSTATUS_01 = "01";
	public static final String PURCHASEPLANNING_SYNCSTATUS_00 = "00";
	public static ArrayList<DerpBasic> purchasePlanning_syncStatusList = new ArrayList<DerpBasic>();

	/** 虚拟在途表t_virtual_in_transit------------------------------------- */
	/** 处理状态: 01-已处理 00-未处理 */
	public static final String VIRTUALINTRANSIT_STATUS_01 = "01";
	public static final String VIRTUALINTRANSIT_STATUS_00 = "00";
	public static ArrayList<DerpBasic> virtualInTransit_statusList = new ArrayList<DerpBasic>();

	/** 爬虫账单t_crawler_bill------------------------------------- */
	/** 爬虫账单类型: 00-销售明细、01-库存买断、02库存盘亏、03报废、04库存盘盈、05国检抽样 */
	public static final String CRAWLER_BILLTYPE_00 = "00";
	public static final String CRAWLER_BILLTYPE_01 = "01";
	public static final String CRAWLER_BILLTYPE_02 = "02";
	public static final String CRAWLER_BILLTYPE_03 = "03";
	public static final String CRAWLER_BILLTYPE_04 = "04";
	public static final String CRAWLER_BILLTYPE_05 = "05";
	public static ArrayList<DerpBasic> crawler_billTypeList = new ArrayList<DerpBasic>();

	/** 是否使用: 0-未使用 1-已使用 */
	public static final String CRAWLERBILL_ISUSED_0 = "0";
	public static final String CRAWLERBILL_ISUSED_1 = "1";
	public static ArrayList<DerpBasic> crawlerBill_isUsedList = new ArrayList<DerpBasic>();

	/** 生成爬虫出库单临时分配表t_crawler_out_temp------------------------------------- */
	/** 数据类型 1-库存量 2-已有销售单账单数量分配 3-新增销售单账单数量分配 4-唯品红冲 5-库存盘亏 6-库存盘盈 7-唯品报废 8-国检抽样 */
	public static final String CRAWLEROUTTEMP_DATATYPE_1 = "1";
	public static final String CRAWLEROUTTEMP_DATATYPE_2 = "2";
	public static final String CRAWLEROUTTEMP_DATATYPE_3 = "3";
	public static final String CRAWLEROUTTEMP_DATATYPE_4 = "4";
	public static final String CRAWLEROUTTEMP_DATATYPE_5 = "5";
	public static final String CRAWLEROUTTEMP_DATATYPE_6 = "6";
	public static final String CRAWLEROUTTEMP_DATATYPE_7 = "7";
	public static final String CRAWLEROUTTEMP_DATATYPE_8 = "8";
	public static ArrayList<DerpBasic> crawlerOutTemp_dataTypeList = new ArrayList<DerpBasic>();

	/** 库存调整明细表t_inventory_adjustment_detail------------------------------------- */
	/** 业务类别: 4-唯品红冲 5-国检抽样 6-唯品报废 7-库存盘亏 8-库存盘盈 */
	public static final String INVENTORYADJUSTMENTDETAIL_MODEL_4 = "4";
	public static final String INVENTORYADJUSTMENTDETAIL_MODEL_5 = "5";
	public static final String INVENTORYADJUSTMENTDETAIL_MODEL_6 = "6";
	public static final String INVENTORYADJUSTMENTDETAIL_MODEL_7 = "7";
	public static final String INVENTORYADJUSTMENTDETAIL_MODEL_8 = "8";
	public static ArrayList<DerpBasic> inventoryAdjustmentDetail_modelList = new ArrayList<DerpBasic>();

	/** 库存/减库存类型：0-减库存 1-加库存 */
	public static final String INVENTORYADJUSTMENTDETAIL_TYPE_0 = "0";
	public static final String INVENTORYADJUSTMENTDETAIL_TYPE_1 = "1";
	public static ArrayList<DerpBasic> inventoryAdjustmentDetail_typeList = new ArrayList<DerpBasic>();

	/** 爬虫商品对照表t_merchandise_contrast------------------------------------- */
	/** 状态: 0-启用 1-禁用 */
	public static final String MERCHANDISECONTRAST_STATUS_0 = "0";
	public static final String MERCHANDISECONTRAST_STATUS_1 = "1";
	public static ArrayList<DerpBasic> merchandiseContrast_statusList = new ArrayList<DerpBasic>();

	/** 类型 1-云集 2-唯品 */
	public static final String MERCHANDISECONTRAST_TYPE_1 = "1";
	public static final String MERCHANDISECONTRAST_TYPE_2 = "2";
	public static ArrayList<DerpBasic> merchandiseContrast_typeList = new ArrayList<>();

	/** 外部单号来源表t_order_external_code------------------------------------- */
	/** 订单来源: 1-电商订单, 2-装载交运 3-销售出库 4-调拨入库 5-调拨出库 6-采购退货 7-众邦云仓采购入库 8-领料出库 */
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_1 = "1";
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_2 = "2";
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_3 = "3";
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_4 = "4";
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_5 = "5";
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_6 = "6";
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_7 = "7";
	public static final String ORDEREXTERNALCODE_ORDERSOURCE_8 = "8";
	public static ArrayList<DerpBasic> orderExternalCode_orderSourceList = new ArrayList<DerpBasic>();

	/** 采购入库分析表t_purchase_analysis------------------------------------- */
	/** 是否完结入库: 1-是 0-否 */
	public static final String PURCHASEANALYSIS_ISEND_1 = "1";
	public static final String PURCHASEANALYSIS_ISEND_0 = "0";
	public static ArrayList<DerpBasic> purchaseAnalysis_isEndList = new ArrayList<DerpBasic>();

	/** 入库是否组合品: 1-是 0-否 */
	public static final String PURCHASEANALYSIS_ISGROUP_0 = "0";
	public static final String PURCHASEANALYSIS_ISGROUP_1 = "1";
	public static ArrayList<DerpBasic> purchaseAnalysis_isGroupList = new ArrayList<DerpBasic>();

	/** 采购订单t_purchase_order start------------------------------------- */
	/**OA 交货方式换码表 ----------------------------------*/
	public final static Map<String, String> OA_DELIVERYTYPE_MAP = new HashMap<String, String>();
	static {
		OA_DELIVERYTYPE_MAP.put("1", "1");// CIF
		OA_DELIVERYTYPE_MAP.put("2", "7");// CFR
		OA_DELIVERYTYPE_MAP.put("3", "0");// FOB
		OA_DELIVERYTYPE_MAP.put("4", "6");// DAP
		OA_DELIVERYTYPE_MAP.put("5", "4");// EXW
		OA_DELIVERYTYPE_MAP.put("6", "2");// CIP
		OA_DELIVERYTYPE_MAP.put("7", "3");// FCA
		OA_DELIVERYTYPE_MAP.put("8", "5");// 其它
	}

	/** 合作模式 0：购销买断，1：入仓代销，2：囤货销售，3：一件代发，4：购销买断+囤货销售，5：其他 */
	public static final String PURCHASEORDER_COOPERATIONMODE_0 = "0";
	public static final String PURCHASEORDER_COOPERATIONMODE_1 = "1";
	public static final String PURCHASEORDER_COOPERATIONMODE_2 = "2";
	public static final String PURCHASEORDER_COOPERATIONMODE_3 = "3";
	public static final String PURCHASEORDER_COOPERATIONMODE_4 = "4";
	public static final String PURCHASEORDER_COOPERATIONMODE_5 = "5";
	public static ArrayList<DerpBasic> purchaseOrder_cooperationModeList = new ArrayList<DerpBasic>();
	
	/** 审批方式 1-OA审批 2-经分销审批*/
	public static final String PURCHASEORDER_AUDITMETHOD_1 = "1";
	public static final String PURCHASEORDER_AUDITMETHOD_2 = "2";
	public static ArrayList<DerpBasic> purchaseOrder_auditMethodList = new ArrayList<DerpBasic>();
	
	
	/** 采购方式 0-以销定采 1-备货(已立项) 2-备货（集团自主） 3-试单 */
	public static final String PURCHASEORDER_PURCHASEMETHOD_0 = "0";
	public static final String PURCHASEORDER_PURCHASEMETHOD_1 = "1";
	public static final String PURCHASEORDER_PURCHASEMETHOD_2 = "2";
	public static final String PURCHASEORDER_PURCHASEMETHOD_3 = "3";
	public static ArrayList<DerpBasic> purchaseOrder_purchaseMethodList = new ArrayList<DerpBasic>();

	/** 付款主体 1-卓普信 2-商家 3-卓烨 */
	public static final String PURCHASEORDER_PAYSUBJECT_1 = "1";
	public static final String PURCHASEORDER_PAYSUBJECT_2 = "2";
	public static final String PURCHASEORDER_PAYSUBJECT_3 = "3";
	public static ArrayList<DerpBasic> purchaseOrder_paySubjectList = new ArrayList<DerpBasic>();

	/** 付款条款 1-先款后货—全款预付 2-先款后货—分期付款 3-先货后款—账期结算（收货后N天结算） 4-实销实结 */
	public static final String PURCHASEORDER_PAYMENTPROVISION_1 = "1";
	public static final String PURCHASEORDER_PAYMENTPROVISION_2 = "2";
	public static final String PURCHASEORDER_PAYMENTPROVISION_3 = "3";
	public static final String PURCHASEORDER_PAYMENTPROVISION_4 = "4";
	public static ArrayList<DerpBasic> purchaseOrder_paymentProvisionList = new ArrayList<DerpBasic>();

	/** 结算方式 0：先货后款，1：先款后货，2：实销实结 */
	public static final String PURCHASEORDER_SETTLEMENTMETHOD_0 = "0";
	public static final String PURCHASEORDER_SETTLEMENTMETHOD_1 = "1";
	public static final String PURCHASEORDER_SETTLEMENTMETHOD_2 = "2";
	public static ArrayList<DerpBasic> purchaseOrder_settlementMethodList = new ArrayList<DerpBasic>();

	/** 结算条款 0：全款预付；1：分期付款（预付30%-50%，理货后付余款）；2：账期结算（收货后N天结算） 3：实销实结 */
	public static final String PURCHASEORDER_SETTLEMENTPROVISION_0 = "0";
	public static final String PURCHASEORDER_SETTLEMENTPROVISION_1 = "1";
	public static final String PURCHASEORDER_SETTLEMENTPROVISION_2 = "2";
	public static final String PURCHASEORDER_SETTLEMENTPROVISION_3 = "3";
	public static ArrayList<DerpBasic> purchaseOrder_settlementProvisionList = new ArrayList<DerpBasic>();

	/** 收货方式 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW 6-CIP 7-FCA 8-其他*/
	public static final String PURCHASEORDER_TRADETERMS_1 = "1";
	public static final String PURCHASEORDER_TRADETERMS_2 = "2";
	public static final String PURCHASEORDER_TRADETERMS_3 = "3";
	public static final String PURCHASEORDER_TRADETERMS_4 = "4";
	public static final String PURCHASEORDER_TRADETERMS_5 = "5";
	public static final String PURCHASEORDER_TRADETERMS_6 = "6";
	public static final String PURCHASEORDER_TRADETERMS_7 = "7";
	public static final String PURCHASEORDER_TRADETERMS_8 = "8";
	public static ArrayList<DerpBasic> purchaseOrder_tradeTermsList = new ArrayList<DerpBasic>();

	/** 业务模式 1-购销 2-代销 3-采购执行 */
	public static final String PURCHASEORDER_BUSINESSMODEL_1 = "1";
	public static final String PURCHASEORDER_BUSINESSMODEL_2 = "2";
	public static final String PURCHASEORDER_BUSINESSMODEL_3 = "3";
	public static ArrayList<DerpBasic> purchaseOrder_businessModelList = new ArrayList<DerpBasic>();

	/** 状态 001-待提交 002-已提交 003-已审核 005-部分入库 007-已入库  028-入库中 006-已删除*/
	public static final String PURCHASEORDER_STATUS_001 = "001";
	public static final String PURCHASEORDER_STATUS_002 = "002";
	public static final String PURCHASEORDER_STATUS_003 = "003";
	public static final String PURCHASEORDER_STATUS_005 = "005";
	public static final String PURCHASEORDER_STATUS_006 = "006";
	public static final String PURCHASEORDER_STATUS_007 = "007";
	public static ArrayList<DerpBasic> purchaseOrder_statusList = new ArrayList<DerpBasic>();

	/** 是否已生成预申报单 1-是 0-否 */
	public static final String PURCHASEORDER_ISGENERATE_1 = "1";
	public static final String PURCHASEORDER_ISGENERATE_0 = "0";
	public static ArrayList<DerpBasic> purchaseOrder_isGenerateList = new ArrayList<DerpBasic>();

	/** 金额调整 1-是 0-否 */
	public static final String PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_1 = "1";
	public static final String PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0 = "0";
	public static ArrayList<DerpBasic> purchaseOrder_amountAdjustmentStatusList = new ArrayList<DerpBasic>();

	/** 金额确认 2-已确认 1-确认不通过 0-待确认 */
	public static final String PURCHASEORDER_AMOUNT_CONFIRM_STATUS_2 = "2";
	public static final String PURCHASEORDER_AMOUNT_CONFIRM_STATUS_1 = "1";
	public static final String PURCHASEORDER_AMOUNT_CONFIRM_STATUS_0 = "0";
	public static ArrayList<DerpBasic> purchaseOrder_amountConfirmStatusList = new ArrayList<DerpBasic>();

	/** 是否完结 1-是 0-否 */
	public static final String PURCHASEORDER_ISEND_0 = "0";
	public static final String PURCHASEORDER_ISEND_1 = "1";
	public static ArrayList<DerpBasic> purchaseOrder_isEndList = new ArrayList<DerpBasic>();

	/** 发送邮件状态 0-未发送邮件 1-发送邮件1次 2-发送邮件2次 */
	public static final String PURCHASEORDER_MAILSTATUS_0 = "0";
	public static final String PURCHASEORDER_MAILSTATUS_1 = "1";
	public static final String PURCHASEORDER_MAILSTATUS_2 = "2";
	public static ArrayList<DerpBasic> purchaseOrder_mailStatusList = new ArrayList<DerpBasic>();

	/**效期区间 1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品*/
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_1 = "1";
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_2 = "2";
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_3 = "3";
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_4 = "4";
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_5 = "5";
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_6 = "6";
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_7 = "7";
	public static final String PURCHASEORDER_EFFECTIVEINTERVAL_8 = "8";
	public static ArrayList<DerpBasic> purchaseOrder_effectiveIntervalList = new ArrayList<DerpBasic>();

	/** 单据状态 1-收到发票 2-已付款 3-收到发票已付款 */
	public static final String PURCHASEORDER_BILLSTATUS_1 = "1";
	public static final String PURCHASEORDER_BILLSTATUS_2 = "2";
	public static final String PURCHASEORDER_BILLSTATUS_3 = "3";
	public static ArrayList<DerpBasic> purchaseOrder_billStatusList = new ArrayList<DerpBasic>();

	/**红冲状态: 001-待红冲 002-红冲中 003-已红冲 */
	public static final String PURCHASEORDER_WRITEOFFSTATUS_001 = "001";
	public static final String PURCHASEORDER_WRITEOFFSTATUS_002 = "002";
	public static final String PURCHASEORDER_WRITEOFFSTATUS_003 = "003";
	public static ArrayList<DerpBasic> purchaseOrder_writeOffStatusList = new ArrayList<DerpBasic>();

	/**用印顺序 0：我方先盖章；1：对方先盖章；2：无需盖章 */
	public static final String PURCHASEORDER_SEALORDER_0 = "0";
	public static final String PURCHASEORDER_SEALORDER_1 = "1";
	public static final String PURCHASEORDER_SEALORDER_2 = "2";
	public static ArrayList<DerpBasic> purchaseOrder_sealOrderList = new ArrayList<DerpBasic>();

	/**用印类型 0：传统物理盖章；1：线上电子签章 */
	public static final String PURCHASEORDER_SEALTYPE_0 = "0";
	public static final String PURCHASEORDER_SEALTYPE_1 = "1";
	public static ArrayList<DerpBasic> purchaseOrder_sealTypeList = new ArrayList<DerpBasic>();

	/** 采购订单t_purchase_order end------------------------------------- */

	/** 采购预申报单t_declare_order------------------------------------- */
	/** 运输方式 1 空运 2 海运 */
	public static final String DECLAREORDER_TRANSPORT_1 = "1";
	public static final String DECLAREORDER_TRANSPORT_2 = "2";
	public static ArrayList<DerpBasic> declareOrder_transportList = new ArrayList<DerpBasic>();

	/** 业务模式 1-采购 2-代销 */
	public static final String DECLAREORDER_BUSINESSMODEL_1 = "1";
	public static final String DECLAREORDER_BUSINESSMODEL_2 = "2";
	public static ArrayList<DerpBasic> declareOrder_businessModelList = new ArrayList<DerpBasic>();

	/** 订单状态订单状态 001待物流委托 002待清关 003已上架 004 待入仓 006已删除*/
	public static final String DECLAREORDER_STATUS_001 = "001";
	public static final String DECLAREORDER_STATUS_002 = "002";
	public static final String DECLAREORDER_STATUS_003 = "003";
	public static final String DECLAREORDER_STATUS_004 = "004";
	public static ArrayList<DerpBasic> declareOrder_statusList = new ArrayList<DerpBasic>();

	/** 接口状态 0-未推接口；1-已推接口；2-接口推送失败 */
	public static final String DECLAREORDER_STATE_1 = "1";
	public static final String DECLAREORDER_STATE_0 = "0";
	public static final String DECLAREORDER_STATE_2 = "2";
	public static ArrayList<DerpBasic> declareOrder_stateList = new ArrayList<DerpBasic>();

	/** 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款 */
	public static final String DECLAREORDER_PAYMENTPROVISION_1 = "1";
	public static final String DECLAREORDER_PAYMENTPROVISION_2 = "2";
	public static final String DECLAREORDER_PAYMENTPROVISION_3 = "3";
	public static ArrayList<DerpBasic> declareorder_paymentProvisionList = new ArrayList<DerpBasic>();

	/** 贸易条款 1- CIF 2-CRF 3-FOB 4-DAP 5-EXW */
	public static final String DECLAREORDER_TRADETERMS_1 = "1";
	public static final String DECLAREORDER_TRADETERMS_2 = "2";
	public static final String DECLAREORDER_TRADETERMS_3 = "3";
	public static final String DECLAREORDER_TRADETERMS_4 = "4";
	public static final String DECLAREORDER_TRADETERMS_5 = "5";
	public static ArrayList<DerpBasic> declareorder_tradeTermsList = new ArrayList<DerpBasic>();

	/** 采购入库单t_purchase_warehouse------------------------------------- */
	/** 单据状态: 011-待入仓 012-已入仓 028-入库中 */
	public static final String PURCHASEWAREHOUSE_STATE_011 = "011";
	public static final String PURCHASEWAREHOUSE_STATE_012 = "012";
	public static final String PURCHASEWAREHOUSE_STATE_028 = "028";
	public static ArrayList<DerpBasic> purchaseWarehouse_stateList = new ArrayList<DerpBasic>();

	/** 采购勾稽状态: 1-是 0-否 */
	public static final String PURCHASEWAREHOUSE_CROSSSTATUS_1 = "1";
	public static final String PURCHASEWAREHOUSE_CROSSSTATUS_0 = "0";
	public static ArrayList<DerpBasic> purchaseWarehouse_crossStatusList = new ArrayList<DerpBasic>();

	/** 采购入库勾稽状态1-未勾稽 2-已勾稽 3-勾稽失败*/
	public static final String PURCHASEWAREHOUSE_CORRELATION_STATUS_3 = "3";
	public static final String PURCHASEWAREHOUSE_CORRELATION_STATUS_2 = "2";
	public static final String PURCHASEWAREHOUSE_CORRELATION_STATUS_1 = "1";
	public static ArrayList<DerpBasic> purchaseWarehouse_correlationStatusList = new ArrayList<DerpBasic>();

	/** 业务模式: 1-采购 2-代销 3-采购执行 */
	public static final String PURCHASEWAREHOUSE_BUSINESSMODEL_1 = "1";
	public static final String PURCHASEWAREHOUSE_BUSINESSMODEL_2 = "2";
	public static final String PURCHASEWAREHOUSE_BUSINESSMODEL_3 = "3";
	public static ArrayList<DerpBasic> purchaseWarehouse_businessModelList = new ArrayList<DerpBasic>();

	/** 是否红冲单：0-否，1-是 */
	public static final String PURCHASEWAREHOUSE_ISWRITEOFF_0 = "0";
	public static final String PURCHASEWAREHOUSE_ISWRITEOFF_1 = "1";
	public static ArrayList<DerpBasic> purchaseWarehouse_isWriteOffList = new ArrayList<DerpBasic>();

	/** 采购框架合同表t_purchase_frame_contract start------------------------------------- */
	/** 合同模板 0：我司标板；1：非我司标板；2：我司修改版；3：我司参考版；4：我司草拟版； */
	public static final String PURCHASEFRAMECONTRACT_CONTRACTVERSION_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTVERSION_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTVERSION_2 = "2";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTVERSION_3 = "3";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTVERSION_4 = "4";
	public static ArrayList<DerpBasic> purchaseFrameContract_contractVersionList = new ArrayList<DerpBasic>();

	/** 合同类型 0：新签，1：续签 2：补充 3：终止 */
	public static final String PURCHASEFRAMECONTRACT_CONTRACTTYPE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTTYPE_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTTYPE_2 = "2";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTTYPE_3 = "3";
	public static ArrayList<DerpBasic> purchaseFrameContract_contractTypeList = new ArrayList<DerpBasic>();

	/** 供应商类型 0：品牌商，1：一级授权商，2：其他 */
	public static final String PURCHASEFRAMECONTRACT_SUPPLIERTYPE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_SUPPLIERTYPE_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_SUPPLIERTYPE_2 = "2";
	public static ArrayList<DerpBasic> purchaseFrameContract_supplierTypeList = new ArrayList<DerpBasic>();

	/** 采购类型 0：进口，1：出口，2：内贸 */
	public static final String PURCHASEFRAMECONTRACT_PURCHASETYPE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_PURCHASETYPE_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_PURCHASETYPE_2 = "2";
	public static ArrayList<DerpBasic> purchaseFrameContract_purchaseTypeList = new ArrayList<DerpBasic>();

	/** 资金来源 0：自有资金，1：卓普信资金 */
	public static final String PURCHASEFRAMECONTRACT_CAPITALTYPE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_CAPITALTYPE_1 = "1";
	public static ArrayList<DerpBasic> purchaseFrameContract_capitalTypeList = new ArrayList<DerpBasic>();

	/** 商品类型 0：母婴类，1：美妆个护，2：保健品，3：日化家清，4：普通食品，5：数码家电，6：宠物食品，7：其他 */
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_2 = "2";
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_3 = "3";
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_4 = "4";
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_5 = "5";
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_6 = "6";
	public static final String PURCHASEFRAMECONTRACT_COMTYTYPE_7 = "7";
	public static ArrayList<DerpBasic> purchaseFrameContract_comtyTypeList = new ArrayList<DerpBasic>();

	/** 商品来源 0：品牌方，1：工厂采购，2：一级经销商，3：海外超市，4：其他 */
	public static final String PURCHASEFRAMECONTRACT_COMTYSOURCE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_COMTYSOURCE_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_COMTYSOURCE_2 = "2";
	public static final String PURCHASEFRAMECONTRACT_COMTYSOURCE_3 = "3";
	public static final String PURCHASEFRAMECONTRACT_COMTYSOURCE_4 = "4";
	public static ArrayList<DerpBasic> purchaseFrameContract_comtySourceList = new ArrayList<DerpBasic>();

	/** 合同状态 0：生效，1：补充生效，2：期满终止，3：提前终止 */
	public static final String PURCHASEFRAMECONTRACT_CONTRACTSTATE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTSTATE_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTSTATE_2 = "2";
	public static final String PURCHASEFRAMECONTRACT_CONTRACTSTATE_3 = "3";
	public static ArrayList<DerpBasic> purchaseFrameContract_contractStateList = new ArrayList<DerpBasic>();

	/** 是否已取得授权 0：是，1：否 */
	public static final String PURCHASEFRAMECONTRACT_EMPJUDGE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_EMPJUDGE_1 = "1";
	public static ArrayList<DerpBasic> purchaseFrameContract_empJudgeList = new ArrayList<DerpBasic>();

	/** 交货方式 0：FOB，1：CIF，2：CIP，3：FCA，4：EXW，5：其他 6：DAP，7：CFR */
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_2 = "2";
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_3 = "3";
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_4 = "4";
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_5 = "5";
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_6 = "6";
	public static final String PURCHASEFRAMECONTRACT_DELIVERTYPE_7 = "7";
	public static ArrayList<DerpBasic> purchaseFrameContract_deliveryTypeList = new ArrayList<DerpBasic>();

	/** 结算方式 0：先货后款，1：先款后货，2：实销实结 */
	public static final String PURCHASEFRAMECONTRACT_SETTLEMENT_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_SETTLEMENT_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_SETTLEMENT_2 = "2";
	public static ArrayList<DerpBasic> purchaseFrameContract_settleMentList = new ArrayList<DerpBasic>();

	/** 预付款 0：有，1：无 */
	public static final String PURCHASEFRAMECONTRACT_ACJUDGE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_ACJUDGE_1 = "1";
	public static ArrayList<DerpBasic> purchaseFrameContract_advanceChargeJudgeList = new ArrayList<DerpBasic>();

	/** 业务对应的财务经理 0：龚小香；1：董欢 */
	public static final String PURCHASEFRAMECONTRACT_FIN_MANAGER_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_FIN_MANAGER_1 = "1";
	public static ArrayList<DerpBasic> purchaseFrameContract_financeManagerList = new ArrayList<DerpBasic>();

	/** 用印顺序 0：我方先盖章；1：对方先盖章；2：无需盖章 */
	public static final String PURCHASEFRAMECONTRACT_SEALORDER_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_SEALORDER_1 = "1";
	public static final String PURCHASEFRAMECONTRACT_SEALORDER_2 = "2";
	public static ArrayList<DerpBasic> purchaseFrameContract_sealOrderList = new ArrayList<DerpBasic>();

	/** 用印类型 0：传统物理盖章；1：线上电子签章 */
	public static final String PURCHASEFRAMECONTRACT_SEALTYPE_0 = "0";
	public static final String PURCHASEFRAMECONTRACT_SEALTYPE_1 = "1";
	public static ArrayList<DerpBasic> purchaseFrameContract_sealTypeList = new ArrayList<DerpBasic>();
	/** 采购框架合同表t_purchase_frame_contract end------------------------------------- */

	/** 采购试单申请表t_purchase_try_apply_order start------------------------------------- */
	/** 审批状态 0：审批驳回；1：审批中；2：审批通过 */
	public static final String PURCHASETRYORDER_APPSTATE_0 = "0";
	public static final String PURCHASETRYORDER_APPSTATE_1 = "1";
	public static final String PURCHASETRYORDER_APPSTATE_2 = "2";
	public static ArrayList<DerpBasic> purchaseTryOrder_appStateList = new ArrayList<DerpBasic>();

	/** 业务类型 0：跨境进口；1：跨境出口；2：一般贸易；3：内贸 */
	public static final String PURCHASETRYORDER_BUSSINESSMODE_0 = "0";
	public static final String PURCHASETRYORDER_BUSSINESSMODE_1 = "1";
	public static final String PURCHASETRYORDER_BUSSINESSMODE_2 = "2";
	public static final String PURCHASETRYORDER_BUSSINESSMODE_3 = "3";
	public static ArrayList<DerpBasic> purchaseTryOrder_bussinessModeList = new ArrayList<DerpBasic>();
	/** 采购试单申请表tt_purchase_try_apply_order end------------------------------------- */

	/** 销售出库分析表t_sale_analysis------------------------------------- */
	/** 是否完结入库 1-已完结 0-未完结 */
	public static final String SALEANALYSIS_ISEND_1 = "1";
	public static final String SALEANALYSIS_ISEND_0 = "0";
	public static ArrayList<DerpBasic> saleAnalysis_isEndList = new ArrayList<DerpBasic>();

	/** 销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结------------------------------------- */
	public static final String SALEANALYSIS_SALETYPE_1 = "1";
	public static final String SALEANALYSIS_SALETYPE_4 = "4";
	public static final String SALEANALYSIS_SALETYPE_3 = "3";
	public static final String SALEANALYSIS_SALETYPE_2 = "2";
	public static ArrayList<DerpBasic> saleAnalysis_saleTypeList = new ArrayList<DerpBasic>();

	/** 销售订单t_sale_order------------------------------------- */
	/** 销售订单服务类型 E0-区内调拨调出服务 F0-区内调拨调入服务 G0-库内调拨服务 */
	public static final String SALEORDER_SERVETYPES_E0 = "E0";
	public static final String SALEORDER_SERVETYPES_F0 = "F0";
	public static final String SALEORDER_SERVETYPES_G0 = "G0";
	public static ArrayList<DerpBasic> saleOrder_serveTypesList = new ArrayList<DerpBasic>();
	/**
	 * 订单状态: 001-待审核 002-审核中 003-已审核 006-已删除 007-已完结 018-已出库 027-出库中 025-已签收 026-已上架
	 * 031-部分上架 008-待提交 017-待出库 019-部分出库
	 */
	public static final String SALEORDER_STATE_008 = "008";
	public static final String SALEORDER_STATE_001 = "001";
	public static final String SALEORDER_STATE_003 = "003";
	public static final String SALEORDER_STATE_017 = "017";
	public static final String SALEORDER_STATE_027 = "027";
	public static final String SALEORDER_STATE_018 = "018";
//	public static final String SALEORDER_STATE_025 = "025";
	public static final String SALEORDER_STATE_031 = "031";
	public static final String SALEORDER_STATE_026 = "026";
//	public static final String SALEORDER_STATE_007 = "007";
	public static final String SALEORDER_STATE_006 = "006";
	public static final String SALEORDER_STATE_002 = "002";
	public static final String SALEORDER_STATE_019 = "019";
	public static ArrayList<DerpBasic> saleOrder_stateList = new ArrayList<DerpBasic>();

	/** 订单来源 1-人工录入 2-系统自动生成 */
	public static final String SALEORDER_ORDERSOURCE_1 = "1";
	public static final String SALEORDER_ORDERSOURCE_2 = "2";
	public static ArrayList<DerpBasic> saleOrder_orderSourceList = new ArrayList<DerpBasic>();

	/** 业务模式 1-购销-整批结算 2-代销 3-采销执行 4.购销-实销实结 */
	public static final String SALEORDER_BUSINESSMODEL_1 = "1";
	public static final String SALEORDER_BUSINESSMODEL_2 = "2";
	public static final String SALEORDER_BUSINESSMODEL_3 = "3";
	public static final String SALEORDER_BUSINESSMODEL_4 = "4";
	public static ArrayList<DerpBasic> saleOrder_businessModelList = new ArrayList<DerpBasic>();

	/** 邮寄方式 1-寄售 2-自提 */
	public static final String SALEORDER_MAILMODE_1 = "1";
	public static final String SALEORDER_MAILMODE_2 = "2";
	public static ArrayList<DerpBasic> saleOrder_mailModeList = new ArrayList<DerpBasic>();

	/** 付款主体: 1-卓普信 2-商家 3-卓烨 */
	public static final String SALEORDER_PAYSUBJECT_1 = "1";
	public static final String SALEORDER_PAYSUBJECT_2 = "2";
	public static final String SALEORDER_PAYSUBJECT_3 = "3";
	public static ArrayList<DerpBasic> saleOrder_paySubjectModeList = new ArrayList<DerpBasic>();

	/** 目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
	public static final String SALEORDER_DESTINATION_HP01 = "HP01";
	public static final String SALEORDER_DESTINATION_HK01 = "HK01";
	public static final String SALEORDER_DESTINATION_GZJC01 = "GZJC01";
	public static ArrayList<DerpBasic> saleOrder_destinationList = new ArrayList<DerpBasic>();

	/** 单据标识 1 预售转销 2 非预售转销 */
	public static final String SALEORDER_ORDERTYPE_1 = "1";
	public static final String SALEORDER_ORDERTYPE_2 = "2";
	public static ArrayList<DerpBasic> saleOrder_orderTypeList = new ArrayList<DerpBasic>();

	/** 金额调整状态 0-未调整 1-已调整 */
	public static final String SALEORDER_AMOUMTSTATUS_0 = "0";
	public static final String SALEORDER_AMOUMTSTATUS_1 = "1";
	public static ArrayList<DerpBasic> saleOrder_amountStatusList = new ArrayList<DerpBasic>();

	/** 金额确认状态 0-未确认 1-确认通过 2-确认不通过 */
	public static final String SALEORDER_AMOUMT_CONFIRM_STATUS_0 = "0";
	public static final String SALEORDER_AMOUMT_CONFIRM_STATUS_1 = "1";
	public static final String SALEORDER_AMOUMT_CONFIRM_STATUS_2 = "2";
	public static ArrayList<DerpBasic> saleOrder_amountConfirmStatusList = new ArrayList<DerpBasic>();

	/** 融资申请状态 0-未申请 1-已申请 2-已赎回 */
	public static final String SALEORDER_FINANCE_STATUS_0 = "0";
	public static final String SALEORDER_FINANCE_STATUS_1 = "1";
	public static final String SALEORDER_FINANCE_STATUS_2 = "2";
	public static ArrayList<DerpBasic> saleOrder_financeStatusList = new ArrayList<DerpBasic>();

	/** 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款 */
	public static final String SALEORDER_PAYMENTTERMS_1 = "1";
	public static final String SALEORDER_PAYMENTTERMS_2 = "2";
	public static final String SALEORDER_PAYMENTTERMS_3 = "3";
	public static ArrayList<DerpBasic> saleOrder_paymentTermsList = new ArrayList<>();

	/** 贸易条款 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW */
	public static final String SALEORDER_TRADETERMS_1 = "1";
	public static final String SALEORDER_TRADETERMS_2 = "2";
	public static final String SALEORDER_TRADETERMS_3 = "3";
	public static final String SALEORDER_TRADETERMS_4 = "4";
	public static final String SALEORDER_TRADETERMS_5 = "5";
	public static ArrayList<DerpBasic> saleOrder_tradeTermsList = new ArrayList<>();

	/** 红冲状态 001-待红冲 002-红冲中 003-已红冲 */
	public static final String SALEORDER_WRITEOFF_STATUS_001 = "001";
	public static final String SALEORDER_WRITEOFF_STATUS_002 = "002";
	public static final String SALEORDER_WRITEOFF_STATUS_003 = "003";
	public static ArrayList<DerpBasic> saleOrder_writeOffStatusList = new ArrayList<>();

	/** 预售单t_pre_sale_order------------------------------------- */
	/** 订单状态: 001-待审核 003-已审核 007-已完结 */
	public static final String PRESALEORDER_STATE_001 = "001";
	public static final String PRESALEORDER_STATE_003 = "003";
	public static final String PRESALEORDER_STATE_007 = "007";
	public static ArrayList<DerpBasic> preSaleOrder_stateList = new ArrayList<DerpBasic>();

	/** 业务模式 1-购销-整批结算 4.购销-实销实结 */
	public static final String PRESALEORDER_BUSINESSMODEL_1 = "1";
	public static final String PRESALEORDER_BUSINESSMODEL_4 = "4";
	public static ArrayList<DerpBasic> preSaleOrder_businessModelList = new ArrayList<DerpBasic>();

	/** 销售出库t_sale_out_depot------------------------------------- */
	/** 销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结------------------------------------- */
	public static final String SALEOUTDEPOT_SALETYPE_1 = "1";
	public static final String SALEOUTDEPOT_SALETYPE_4 = "4";
	public static final String SALEOUTDEPOT_SALETYPE_3 = "3";
	public static final String SALEOUTDEPOT_SALETYPE_2 = "2";
	public static ArrayList<DerpBasic> saleOutDepot_saleTypeList = new ArrayList<DerpBasic>();

	/** 状态: 017-待出库 018-已出库 027-出库中 006-已删除 026-已上架 031-部分上架*/
	public static final String SALEOUTDEPOT_STATUS_017 = "017";
	public static final String SALEOUTDEPOT_STATUS_027 = "027";
	public static final String SALEOUTDEPOT_STATUS_018 = "018";
	public static final String SALEOUTDEPOT_STATUS_026 = "026";
	public static final String SALEOUTDEPOT_STATUS_031 = "031";
	public static ArrayList<DerpBasic> saleOutDepot_statusList = new ArrayList<DerpBasic>();

	/** 订单来源 1:导入 2.接口回传 */
	public static final String SALEOUTDEPOT_SOURCE_1 = "1";
	public static final String SALEOUTDEPOT_SOURCE_2 = "2";
	public static ArrayList<DerpBasic> saleOutDepot_sourceList = new ArrayList<DerpBasic>();

	/** 进口模式: 10-BBC 20-BC 30-保留 40-CC */
	public static final String SALEOUTDEPOT_IMPORTMODE_10 = "10";
	public static final String SALEOUTDEPOT_IMPORTMODE_20 = "20";
	public static final String SALEOUTDEPOT_IMPORTMODE_30 = "30";
	public static final String SALEOUTDEPOT_IMPORTMODE_40 = "40";
	public static ArrayList<DerpBasic> saleOutDepot_importModeList = new ArrayList<DerpBasic>();

	/** 退运状态: 70-成功 90-作废 */
	public static final String SALEOUTDEPOT_RETURNSTATUS_70 = "70";
	public static final String SALEOUTDEPOT_RETURNSTATUS_90 = "90";
	public static ArrayList<DerpBasic> saleOutDepot_returnStatusList = new ArrayList<DerpBasic>();

	/** 是否红冲单：0-否，1-是 */
	public static final String SALEOUTDEPOT_ISWRITEOFF_0 = "0";
	public static final String SALEOUTDEPOT_ISWRITEOFF_1 = "1";
	public static ArrayList<DerpBasic> saleOutDepot_isWriteOffList = new ArrayList<DerpBasic>();

	/** 销售出库表体t_sale_out_depot_item------------------------------------- */
	/** 账单类型: 00-销售明细 01-库存买断 */
	public static final String SALEOUTDEPOTITEM_BILLTYPE_00 = "00";
	public static final String SALEOUTDEPOTITEM_BILLTYPE_01 = "01";
	public static ArrayList<DerpBasic> saleOutDepotItem_billTypeList = new ArrayList<DerpBasic>();

	/** 销售单_po关联表t_sale_po_rel------------------------------------- */
	/** 类型: 0-销售订单 1-销售退订单 */
	public static final String SALEPOREL_STATE_0 = "0";
	public static final String SALEPOREL_STATE_1 = "1";
	public static ArrayList<DerpBasic> salePoRel_stateList = new ArrayList<DerpBasic>();

	/** 销售退货入库t_sale_return_idepot------------------------------------- */
	/** 状态: 011-待入仓 012-已入仓 006-已删除 028-入库中 */
	public static final String SALERETURNIDEPOT_STATUS_011 = "011";
	public static final String SALERETURNIDEPOT_STATUS_012 = "012";
	public static final String SALERETURNIDEPOT_STATUS_006 = "006";
	public static final String SALERETURNIDEPOT_STATUS_028 = "028";
	public static ArrayList<DerpBasic> saleReturnIdepot_statusList = new ArrayList<DerpBasic>();

	/** 销售退货出库t_sale_return_odepot------------------------------------- */
	/** 单据状态: 015-待出仓 016-已出仓 027-出库中 006-已删除 */
	public static final String SALERETURNODEPOT_STATUS_015 = "015";
	public static final String SALERETURNODEPOT_STATUS_016 = "016";
	public static final String SALERETURNODEPOT_STATUS_027 = "027";
	public static final String SALERETURNODEPOT_STATUS_006 = "006";
	public static ArrayList<DerpBasic> saleReturnOdepot_statusList = new ArrayList<DerpBasic>();

	/** 销售退货订单t_sale_return_order------------------------------------- */
	/** 销售退服务类型 E0-区内调拨调出服务 F0-区内调拨调入服务 G0-库内调拨服务 */
	public static final String SALERETURN_SERVETYPES_E0 = "E0";
	public static final String SALERETURN_SERVETYPES_F0 = "F0";
	public static final String SALERETURN_SERVETYPES_G0 = "G0";
	public static ArrayList<DerpBasic> saleReturn_serveTypesList = new ArrayList<DerpBasic>();

	/** 销售退业务场景 40-账册内货权转移 50-账册内货权转移调仓 */
	public static final String SALERETURN_MODEL_40 = "40";
	public static final String SALERETURN_MODEL_50 = "50";
	public static ArrayList<DerpBasic> saleReturn_modelList = new ArrayList<DerpBasic>();

	/**
	 * 状态：001-待审核 003-已审核 006-已删除 011-待入仓 012-已入仓 015-待出仓 016-已出仓 007-已完结 028-入库中
	 * 027-出库中
	 */
	public static final String SALERETURNORDER_STATUS_001 = "001";
	public static final String SALERETURNORDER_STATUS_003 = "003";
	public static final String SALERETURNORDER_STATUS_006 = "006";
	public static final String SALERETURNORDER_STATUS_011 = "011";
	public static final String SALERETURNORDER_STATUS_012 = "012";
	public static final String SALERETURNORDER_STATUS_015 = "015";
	public static final String SALERETURNORDER_STATUS_016 = "016";
	public static final String SALERETURNORDER_STATUS_007 = "007";
	public static final String SALERETURNORDER_STATUS_028 = "028";
	public static final String SALERETURNORDER_STATUS_027 = "027";
	public static ArrayList<DerpBasic> saleReturnOrder_statusList = new ArrayList<DerpBasic>();

	/** 销售退货类型: 1-消费者退货 2-代销退货 3-购销退货 */
	public static final String SALERETURNORDER_RETURNTYPE_1 = "1";
	public static final String SALERETURNORDER_RETURNTYPE_2 = "2";
	public static final String SALERETURNORDER_RETURNTYPE_3 = "3";
	public static ArrayList<DerpBasic> saleReturnOrder_returnTypeList = new ArrayList<DerpBasic>();

	/** 退货方式 1-退货退款，2-仅退货 */
	public static final String SALERETURNORDER_RETURNMODE_1 = "1";
	public static final String SALERETURNORDER_RETURNMODE_2 = "2";
	public static ArrayList<DerpBasic> saleReturnOrder_returnModeList = new ArrayList<DerpBasic>();

	/** 销售上架信息表t_sale_shelf------------------------------------- */
	/** 订单类型：1-销售 2-销售出库 */
	public static final String SALESHELF_ORDERTYPE_1 = "1";
	public static final String SALESHELF_ORDERTYPE_2 = "2";
	public static ArrayList<DerpBasic> saleShelf_orderTypeList = new ArrayList<DerpBasic>();

	/** 核销表获取状态: 0/空-未获取 1-已获取 */
	public static final String SALESHELF_VERIFYRELSTATE_0 = "0";
	public static final String SALESHELF_VERIFYRELSTATE_1 = "1";
	public static ArrayList<DerpBasic> saleShelf_verifyRelStateList = new ArrayList<DerpBasic>();

	/** 上架入库单t_sale_shelf_idepot------------------------------------- */
	/** 状态 011-待入仓 012-已入仓 028-入库中 */
	public static final String SALESHELFIDEPOT_STATUS_011 = "011";
	public static final String SALESHELFIDEPOT_STATUS_012 = "012";
	public static final String SALESHELFIDEPOT_STATUS_028 = "028";
	public static ArrayList<DerpBasic> saleShelfIdepot_statusList = new ArrayList<DerpBasic>();

	/** 是否红冲单：0-否，1-是 */
	public static final String SALESHELFIDEPOT_ISWRITEOFF_0 = "0";
	public static final String SALESHELFIDEPOT_ISWRITEOFF_1 = "1";
	public static ArrayList<DerpBasic> saleShelfIdepot_isWriteOffList = new ArrayList<DerpBasic>();

	/** 理货单t_tallying_order------------------------------------- */
	/** 理货单状态 009-待确认 010-已确认 004-已驳回 */
	public static final String TALLYINGORDER_STATE_009 = "009";
	public static final String TALLYINGORDER_STATE_010 = "010";
	public static final String TALLYINGORDER_STATE_004 = "004";
	public static ArrayList<DerpBasic> tallyingOrder_stateList = new ArrayList<DerpBasic>();

	/** 订单类型 1-采购 2-调拨 3-销售退理货 */
	public static final String TALLYINGORDER_TYPE_1 = "1";
	public static final String TALLYINGORDER_TYPE_2 = "2";
	public static final String TALLYINGORDER_TYPE_3 = "3";
	public static ArrayList<DerpBasic> tallyingOrder_typeList = new ArrayList<DerpBasic>();

	/** 调拨入库t_transfer_in_depot------------------------------------- */
	/** 状态 011-待入仓 012-已入仓 028-入库中 006-已删除 */
	public static final String TRANSFERINDEPOT_STATUS_011 = "011";
	public static final String TRANSFERINDEPOT_STATUS_012 = "012";
	public static final String TRANSFERINDEPOT_STATUS_028 = "028";
	public static final String TRANSFERINDEPOT_STATUS_006 = "006";
	public static ArrayList<DerpBasic> transferInDepot_statusList = new ArrayList<DerpBasic>();

	/** 单据来源: 1-调拨入自动生成 2-接口回推生成 3-手工导入 */
	public static final String TRANSFERINDEPOT_SOURCE_1 = "1";
	public static final String TRANSFERINDEPOT_SOURCE_2 = "2";
	public static final String TRANSFERINDEPOT_SOURCE_3 = "3";
	public static ArrayList<DerpBasic> transferInDepot_sourceList = new ArrayList<DerpBasic>();

	/** 调拨订单t_transfer_order------------------------------------- */
	/** 业务场景: 10-账册内调仓 40-账册内货权转移 50-账册内货权转移调仓 */
	public static final String TRANSFERORDER_MODEL_10 = "10";
	public static final String TRANSFERORDER_MODEL_40 = "40";
	public static final String TRANSFERORDER_MODEL_50 = "50";
	public static ArrayList<DerpBasic> transferOrder_modelList = new ArrayList<DerpBasic>();

	/** 服务类型: E0-区内调拨调出服务 F0-区内调拨调入服务 G0-库内调拨服务 */
	public static final String TRANSFERORDER_SERVETYPES_E0 = "E0";
	public static final String TRANSFERORDER_SERVETYPES_F0 = "F0";
	public static final String TRANSFERORDER_SERVETYPES_G0 = "G0";
	public static ArrayList<DerpBasic> transferOrder_serveTypesList = new ArrayList<DerpBasic>();

	/** 状态: 013-待提交 014-已提交 023-调拨已出库 024-调拨已入库 007-已完结 006-已删除 */
	public static final String TRANSFERORDER_STATUS_013 = "013";
	public static final String TRANSFERORDER_STATUS_014 = "014";
	public static final String TRANSFERORDER_STATUS_023 = "023";
	public static final String TRANSFERORDER_STATUS_024 = "024";
	public static final String TRANSFERORDER_STATUS_007 = "007";
	public static final String TRANSFERORDER_STATUS_006 = "006";
	public static ArrayList<DerpBasic> transferOrder_statusList = new ArrayList<DerpBasic>();

	/** 目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
	public static final String TRANSFERORDER_DESTINATION_HP01 = "HP01";
	public static final String TRANSFERORDER_DESTINATION_HK01 = "HK01";
	public static final String TRANSFERORDER_DESTINATION_GZJC01 = "GZJC01";
	public static ArrayList<DerpBasic> transferOrder_destinationList = new ArrayList<DerpBasic>();

	/** 调拨出库表t_transfer_out_depot------------------------------------- */
	/** 状态 015-待出仓 016-已出仓 006-已删除 027-出库中 */
	public static final String TRANSFEROUTDEPOT_STATUS_015 = "015";
	public static final String TRANSFEROUTDEPOT_STATUS_016 = "016";
	public static final String TRANSFEROUTDEPOT_STATUS_006 = "006";
	public static final String TRANSFEROUTDEPOT_STATUS_027 = "027";
	public static ArrayList<DerpBasic> transferOutDepot_statusList = new ArrayList<DerpBasic>();

	/** 退运状态: 70-成功 90-作废 */
	public static final String TRANSFEROUTDEPOT_RETURNSTATUS_70 = "70";
	public static final String TRANSFEROUTDEPOT_RETURNSTATUS_90 = "90";
	public static ArrayList<DerpBasic> transferOutDepot_returnStatusList = new ArrayList<DerpBasic>();

	/** 单据来源：1-手工导入 2-接口生成 */
	public static final String TRANSFEROUTDEPOT_DATASOURCE_1 = "1";
	public static final String TRANSFEROUTDEPOT_DATASOURCE_2 = "2";
	public static ArrayList<DerpBasic> transferOutDepot_dataSourceList = new ArrayList<DerpBasic>();

	/** 附件状态 001-已启用 006-已删除 */
	public static final String ATTACHMENT_STATUS_001 = "001";
	public static final String ATTACHMENT_STATUS_006 = "006";
	public static ArrayList<DerpBasic> attachment_statusList = new ArrayList<DerpBasic>();

	/** 云集结算账单是否使用: 0-未使用 1-已使用 */
	public static final String YJ_ACCOUNT_DATA_ISUSED_0 = "0";
	public static final String YJ_ACCOUNT_DATA_ISUSED_1 = "1";
	public static ArrayList<DerpBasic> yjAccountData_isUsedList = new ArrayList<DerpBasic>();

	/** 组合商品对照表t_group_merchandise_contrast */
	/** 状态：0-已停用 1-已启用 */
	public static final String GROUPMERCHANDISECONTRAST_STATUS_0 = "0";
	public static final String GROUPMERCHANDISECONTRAST_STATUS_1 = "1";
	public static ArrayList<DerpBasic> groupMerchandiseContrast_statusList = new ArrayList<DerpBasic>();

	/** 账单出入库表t_bill_outin_depot------------------------------------- */
	/** 处理类型:XSC-销售出库 GJC-国检出库 PKC-盘亏出库 BFC-报废 PYR-盘盈入库 KTR-客退入库 */
	public static final String PROCESSINGTYPE_XSC = "XSC";
	public static final String PROCESSINGTYPE_GJC = "GJC";
	public static final String PROCESSINGTYPE_PKC = "PKC";
	public static final String PROCESSINGTYPE_BFC = "BFC";
	public static final String PROCESSINGTYPE_PYR = "PYR";
	public static final String PROCESSINGTYPE_KTR = "KTR";
	public static ArrayList<DerpBasic> processingTypeList = new ArrayList<DerpBasic>();

	/** 单据状态 00-待审核 01-处理中 02-已出库 03-已入库 */
	public static final String BILLOUTINDEPOT_STATE_00 = "00";
	public static final String BILLOUTINDEPOT_STATE_01 = "01";
	public static final String BILLOUTINDEPOT_STATE_02 = "02";
	public static final String BILLOUTINDEPOT_STATE_03 = "03";
	public static ArrayList<DerpBasic> billOutinDepot_stateList = new ArrayList<DerpBasic>();

	/** 库存调整类型 0 调减 1调增 */
	public static final String BILLOUTINDEPOT_OPERATE_TYPE_0 = "0";
	public static final String BILLOUTINDEPOT_OPERATE_TYPE_1 = "1";
	public static ArrayList<DerpBasic> billOutinDepot_operateTypeList = new ArrayList<DerpBasic>();

	/** 账单来源 1-云集爬虫账单 2-唯品爬虫账单 3-手动导入 */
	public static final String BILLOUTINDEPOT_BILL_SOURCE_1 = "1";
	public static final String BILLOUTINDEPOT_BILL_SOURCE_2 = "2";
	public static final String BILLOUTINDEPOT_BILL_SOURCE_3 = "3";
	public static ArrayList<DerpBasic> billOutinDepot_billSourceList = new ArrayList<DerpBasic>();

	/** 采购退货状态：001-待审核 003-已审核 006-已删除 015-待出仓 016-已出仓 007-已完结 027-出库中 */
	public static final String PURCHASERETURNORDER_STATUS_001 = "001";
	public static final String PURCHASERETURNORDER_STATUS_003 = "003";
	public static final String PURCHASERETURNORDER_STATUS_006 = "006";
	public static final String PURCHASERETURNORDER_STATUS_015 = "015";
	public static final String PURCHASERETURNORDER_STATUS_016 = "016";
	public static final String PURCHASERETURNORDER_STATUS_007 = "007";
	public static final String PURCHASERETURNORDER_STATUS_027 = "027";
	public static ArrayList<DerpBasic> purchaseReturnOrder_statusList = new ArrayList<DerpBasic>();

	/** 采购退货邮寄方式 1-寄售 2-自提 */
	public static final String PURCHASERETURNORDER_MAILMODE_1 = "1";
	public static final String PURCHASERETURNORDER_MAILMODE_2 = "2";
	public static ArrayList<DerpBasic> purchaseReturnOrder_mailModeList = new ArrayList<DerpBasic>();

	/** 采购退货目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
	public static final String PURCHASERETURNORDER_DESTINATION_HP01 = "HP01";
	public static final String PURCHASERETURNORDER_DESTINATION_HK01 = "HK01";
	public static final String PURCHASERETURNORDER_DESTINATION_GZJC01 = "GZJC01";
	public static ArrayList<DerpBasic> purchaseReturnOrder_destinationList = new ArrayList<DerpBasic>();

	/** 采购退货是否同关区 0-否，1-是 */
	public static final String PURCHASERETURNORDER_ISSAMEAREA_0 = "0";
	public static final String PURCHASERETURNORDER_ISSAMEAREA_1 = "1";
	public static ArrayList<DerpBasic> purchaseReturnOrder_isSameAreaList = new ArrayList<DerpBasic>();

	/** 采购退货出库状态：001-待审核 003-已审核 006-已删除 015-待出仓 016-已出仓 007-已完结 027-出库中 */
	public static final String PURCHASERETURNORDERODEPOT_STATUS_001 = "001";
	public static final String PURCHASERETURNORDERODEPOT_STATUS_003 = "003";
	public static final String PURCHASERETURNORDERODEPOT_STATUS_006 = "006";
	public static final String PURCHASERETURNORDERODEPOT_STATUS_015 = "015";
	public static final String PURCHASERETURNORDERODEPOT_STATUS_016 = "016";
	public static final String PURCHASERETURNORDERODEPOT_STATUS_007 = "007";
	public static final String PURCHASERETURNORDERODEPOT_STATUS_027 = "027";
	public static ArrayList<DerpBasic> purchaseReturnOrderOdepot_statusList = new ArrayList<DerpBasic>();

	/** 采购合同运输方式：CIF ；CFR； FOB； DAP； EXW； 空运； 海运； 陆运 */
	public static final String PURCHASERECONTRACT_TRAN_CIF = "CIF";
	public static final String PURCHASERECONTRACT_TRAN_CFR = "CFR";
	public static final String PURCHASERECONTRACT_TRAN_FOB = "FOB";
	public static final String PURCHASERECONTRACT_TRAN_DAP = "DAP";
	public static final String PURCHASERECONTRACT_TRAN_EXW = "EXW";
	public static final String PURCHASERECONTRACT_TRAN_BYAIR = "BY AIR";
	public static final String PURCHASERECONTRACT_TRAN_BYSEA = "BY SEA";
	public static final String PURCHASERECONTRACT_TRAN_BYLAND = "BY LAND";
	public static final String PURCHASERECONTRACT_TRAN_CIP = "CIP";
	public static ArrayList<DerpBasic> purchasereContract_tranList = new ArrayList<DerpBasic>();

	/** 事业部移库单表t_bu_move_inventory--------------------------------- */
	/** 单据来源 1：手工导入；2：系统自动生成 */
	public static final String BUMOVEINVENTORY_ORDERSOURCE_1 = "1";
	public static final String BUMOVEINVENTORY_ORDERSOURCE_2 = "2";
	public static ArrayList<DerpBasic> buMoveInventory_orderSourceList = new ArrayList<DerpBasic>();

	/** 移库状态 017-待移库,018-已移库,027-移库中 */
	public static final String BUMOVEINVENTORY_STATUS_017 = "017";
	public static final String BUMOVEINVENTORY_STATUS_027 = "027";
	public static final String BUMOVEINVENTORY_STATUS_018 = "018";
	public static ArrayList<DerpBasic> buMoveInventory_statusList = new ArrayList<DerpBasic>();

	/** 单据类别 DSDD:电商订单、XSDD：销售订单 KCYKD:库存移库单 */
	public static final String BUMOVEINVENTORY_ORDERTYPE_DSDD = "DSDD";
	public static final String BUMOVEINVENTORY_ORDERTYPE_XSDD = "XSDD";
	public static final String BUMOVEINVENTORY_ORDERTYPE_KCYKD = "KCYKD";
	public static ArrayList<DerpBasic> buMoveInventory_orderTypeList = new ArrayList<DerpBasic>();

	/**事业部移库表体t_bu_move_inventory_item---------------------------------*/
	/**库存类型 0 好品  1 坏品*/
	public static final String BUMOVEINVENTORYITEM_TYPE_0 = "0";
	public static final String BUMOVEINVENTORYITEM_TYPE_1 = "1";
	public static ArrayList<DerpBasic> buMoveInventoryItem_typeList = new ArrayList<DerpBasic>();

	/** 事业部移库单表t_agreement_currency_config--------------------------------- */
	/** 状态 032-已生效,006-已删除 */
	public static final String AGREEMENTCURRENCYCONFIG_STATUS_032 = "032";
	public static final String AGREEMENTCURRENCYCONFIG_STATUS_006 = "006";
	public static ArrayList<DerpBasic> agreementCurrencyConfig_statusList = new ArrayList<DerpBasic>();

	/** 应收账单跟踪表t_receive_bill_verification */
	/** 是否逾期 0否,1是 */
	public static final String RECEIVEBILLVERIFICATION_OVERDUESTATUS_0 = "0";
	public static final String RECEIVEBILLVERIFICATION_OVERDUESTATUS_1 = "1";
	public static ArrayList<DerpBasic> receiveBillVerification_overdueStatusList = new ArrayList<DerpBasic>();
	/** 开票状态 0未开票  1-已开票*/
	public static final String RECEIVEBILLVERIFICATION_INVOICESTATUS_0 = "0";
	public static final String RECEIVEBILLVERIFICATION_INVOICESTATUS_1 = "1";
	public static ArrayList<DerpBasic> receiveBillVerification_invoiceStatusList = new ArrayList<DerpBasic>();

	/** 应收账单表t_receive_bill */
	/** 账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-作废待审 06-已作废 */
	public static final String RECEIVEBILL_BILLSTATUS_00 = "00";
	public static final String RECEIVEBILL_BILLSTATUS_01 = "01";
	public static final String RECEIVEBILL_BILLSTATUS_02 = "02";
	public static final String RECEIVEBILL_BILLSTATUS_03 = "03";
	public static final String RECEIVEBILL_BILLSTATUS_04 = "04";
	public static final String RECEIVEBILL_BILLSTATUS_05 = "05";
	public static final String RECEIVEBILL_BILLSTATUS_06 = "06";
	public static ArrayList<DerpBasic> receiveBill_billStatusList = new ArrayList<DerpBasic>();
	/** 开票状态 00-待开票 01-待签章 02-已作废 03-已签章 */
	public static final String RECEIVEBILL_INVOICESTATUS_00 = "00";
	public static final String RECEIVEBILL_INVOICESTATUS_01 = "01";
	public static final String RECEIVEBILL_INVOICESTATUS_02 = "02";
	public static final String RECEIVEBILL_INVOICESTATUS_03 = "03";
	public static ArrayList<DerpBasic> receiveBill_invoiceStatusList = new ArrayList<DerpBasic>();
	/** 单据类型 1-上架单 2-账单出库单 3-预售单 4-销售订单 5-采购SD单 6-融资申请单 7-销售退货单 8-赊销单 9-赊销账单*/
	public static final String RECEIVEBILL_ORDERTYPE_1 = "1";
	public static final String RECEIVEBILL_ORDERTYPE_2 = "2";
	public static final String RECEIVEBILL_ORDERTYPE_3 = "3";
	public static final String RECEIVEBILL_ORDERTYPE_4 = "4";
	public static final String RECEIVEBILL_ORDERTYPE_5 = "5";
//	public static final String RECEIVEBILL_ORDERTYPE_6 = "6";
	public static final String RECEIVEBILL_ORDERTYPE_7 = "7";
	public static final String RECEIVEBILL_ORDERTYPE_8 = "8";
	public static final String RECEIVEBILL_ORDERTYPE_9 = "9";
	public static ArrayList<DerpBasic> receiveBill_orderTypeList = new ArrayList<DerpBasic>();
	/** 销售模式 009-经销模式 */
	public static final String RECEIVEBILL_SALEMODEL_009 = "009";
	public static ArrayList<DerpBasic> receiveBill_saleModeList = new ArrayList<DerpBasic>();
	/**
	 * NC应收状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步
	 */
	public static final String RECEIVEBILL_NCSYNSTATUS_1 = "1";
	public static final String RECEIVEBILL_NCSYNSTATUS_2 = "2";
	public static final String RECEIVEBILL_NCSYNSTATUS_3 = "3";
	public static final String RECEIVEBILL_NCSYNSTATUS_4 = "4";
	public static final String RECEIVEBILL_NCSYNSTATUS_5 = "5";
	public static final String RECEIVEBILL_NCSYNSTATUS_6 = "6";
	public static final String RECEIVEBILL_NCSYNSTATUS_7 = "7";
	public static final String RECEIVEBILL_NCSYNSTATUS_8 = "8";
	public static final String RECEIVEBILL_NCSYNSTATUS_9 = "9";
	public static final String RECEIVEBILL_NCSYNSTATUS_10 = "10";
	public static final String RECEIVEBILL_NCSYNSTATUS_11 = "11";
	public static ArrayList<DerpBasic> receiveBill_nvSynList = new ArrayList<DerpBasic>();
	/** 凭证状态：1-正常，0-作废 */
	public static final String RECEIVEBILL_VOUCHERSYNSTATUS_0 = "0";
	public static final String RECEIVEBILL_VOUCHERSYNSTATUS_1 = "1";
	public static ArrayList<DerpBasic> receiveBill_voucherStatusList = new ArrayList<DerpBasic>();
	/** 是否增加残损金额 0-是 1-否 */
	public static final String RECEIVEBILL_ISADDWORN_0 = "0";
	public static final String RECEIVEBILL_ISADDWORN_1 = "1";
	public static ArrayList<DerpBasic> receiveBill_isAddWornList = new ArrayList<DerpBasic>();
	/** 分类：1-商销收入 2-GTN核销 3-采购rebate */
	public static final String RECEIVEBILL_SORTTYPE_1 = "1";
	public static final String RECEIVEBILL_SORTTYPE_2 = "2";
	public static final String RECEIVEBILL_SORTTYPE_3 = "3";
	public static ArrayList<DerpBasic> receiveBill_sortTypeList = new ArrayList<DerpBasic>();

	/* 预收账单 */
	/* 预收账单操作日志表 t_advance_bill_operate_item 0-通过 01-驳回 02-提交审核 03-提交作废 */
	public static final String ADVANCEBILL_RESULT_0 = "0";
	public static final String ADVANCEBILL_RESULT_1 = "1";
	public static final String ADVANCEBILL_RESULT_2 = "2";
	public static final String ADVANCEBILL_RESULT_3 = "3";
	public static ArrayList<DerpBasic> advanceBill_resultList = new ArrayList<DerpBasic>();
	/* 预收账单操作日志表 操作类型 0-提交 1-审核 2-提交作废 3-审核作废 4.开票 */
	public static final String ADVANCEBILL_TYPE_0 = "0";
	public static final String ADVANCEBILL_TYPE_1 = "1";
	public static final String ADVANCEBILL_TYPE_2 = "2";
	public static final String ADVANCEBILL_TYPE_3 = "3";
	public static final String ADVANCEBILL_TYPE_4 = "4";
	public static ArrayList<DerpBasic> advanceBill_typeList = new ArrayList<DerpBasic>();
	/** 账单状态 00-待提交 01-待审核 02-待核销 03-待收款 04-已核销 05-作废待审 06-已作废 */
	public static final String ADVANCEBILL_BILLSTATUS_00 = "00";
	public static final String ADVANCEBILL_BILLSTATUS_01 = "01";
	public static final String ADVANCEBILL_BILLSTATUS_02 = "02";
	public static final String ADVANCEBILL_BILLSTATUS_03 = "03";
	public static final String ADVANCEBILL_BILLSTATUS_04 = "04";
	public static final String ADVANCEBILL_BILLSTATUS_05 = "05";
	public static final String ADVANCEBILL_BILLSTATUS_06 = "06";
	public static ArrayList<DerpBasic> advanceBill_billStatusList = new ArrayList<DerpBasic>();
	/* 预收账单 单据类型 1-销售 */
	public static final String ADVANCEBILL_ORDERTYPE_1 = "1";
	public static ArrayList<DerpBasic> advanceBill_orderTypeList = new ArrayList<DerpBasic>();
	/** 开票状态 00-待开票 01-待签章 02-已作废 03-已签章 */
	public static final String ADVANCEBILL_INVOICESTATUS_00 = "00";
	public static final String ADVANCEBILL_INVOICESTATUS_01 = "01";
	public static final String ADVANCEBILL_INVOICESTATUS_02 = "02";
	public static final String ADVANCEBILL_INVOICESTATUS_03 = "03";
	public static ArrayList<DerpBasic> advanceBill_invoiceStatusList = new ArrayList<DerpBasic>();

	/** 模板管理 模版类型 1-采购合同 2-应收账单 3-清关单证  4-预收账单*/
	public static final String TEMP_TYPE_1 = "1";
	public static final String TEMP_TYPE_2 = "2";
	public static final String TEMP_TYPE_3 = "3";
	public static final String TEMP_TYPE_4 = "4";
	public static ArrayList<DerpBasic> temp_typelist = new ArrayList<DerpBasic>();

	/** 模板管理 状态 1-启用 0-禁用 */
	public static final String TEMP_STATUS_1 = "1";
	public static final String TEMP_STATUS_0 = "0";
	public static ArrayList<DerpBasic> temp_statusList = new ArrayList<DerpBasic>();

	/**
	 * 清关单证模板对应类型配置 NANSHA-南沙综合仓模板 ZONGHEYICANG-综合1仓模板 VIPTIANJIN-唯品天津模板
	 * VIPCHONGQING-唯品重庆模板 VIPZHENGZHOU-唯品郑州模板 VIPNANSHA-唯品南沙仓模板 VIPQINGDAO-唯品青岛模板
	 * VIPONLINE-唯品线上模板 JDHUANGPU-京东黄埔仓进仓单模板 JDSHANGHAI-京东上海仓进仓单模板
	 * HPZIYING-黄埔自营仓-调拨转关报入资料模板
	 */
	public static final String TEMP_CUSTOMSFILECONFIG_NANSHA = "NANSHA";
	public static final String TEMP_CUSTOMSFILECONFIG_ZONGHEYICANG = "ZONGHEYICANG";
	public static final String TEMP_CUSTOMSFILECONFIG_VIPTIANJIN = "VIPTIANJIN";
	public static final String TEMP_CUSTOMSFILECONFIG_VIPCHONGQING = "VIPCHONGQING";
	public static final String TEMP_CUSTOMSFILECONFIG_VIPZHENGZHOU = "VIPZHENGZHOU";
	public static final String TEMP_CUSTOMSFILECONFIG_VIPNANSHA = "VIPNANSHA";
	public static final String TEMP_CUSTOMSFILECONFIG_VIPQINGDAO = "VIPQINGDAO";
	public static final String TEMP_CUSTOMSFILECONFIG_VIPONLINE = "VIPONLINE";
	public static final String TEMP_CUSTOMSFILECONFIG_JDHUANGPU = "JDHUANGPU";
	public static final String TEMP_CUSTOMSFILECONFIG_JDSHANGHAI = "JDSHANGHAI";
	public static final String TEMP_CUSTOMSFILECONFIG_JDNANSHA = "JDNANSHA";
	public static final String TEMP_CUSTOMSFILECONFIG_JDQINGDAO = "JDQINGDAO";
	public static final String TEMP_CUSTOMSFILECONFIG_JDZHENGZHOU = "JDZHENGZHOU";
	public static final String TEMP_CUSTOMSFILECONFIG_CNHUAZENG = "CNHUAZENG";
	public static final String TEMP_CUSTOMSFILECONFIG_CNWTXYICANG = "CNWTXYICANG";
	public static final String TEMP_CUSTOMSFILECONFIG_CNWTXDB = "CNWTXDB";
	public static final String TEMP_CUSTOMSFILECONFIG_JDNINGBO = "JDNINGBO";
	public static final String TEMP_CUSTOMSFILECONFIG_JDXIAMEN = "JDXIAMEN";
	public static final String TEMP_CUSTOMSFILECONFIG_JDZIYINGHK = "JDZIYINGHK";
	public static final String TEMP_CUSTOMSFILECONFIG_JDTIANJIN = "JDTIANJIN";
	public static final String TEMP_CUSTOMSFILECONFIG_JDJINYI = "JDJINYI";
	public static final String TEMP_CUSTOMSFILECONFIG_JDHEBEI = "JDHEBEI";
	public static final String TEMP_CUSTOMSFILECONFIG_JDHEBEIDETAIL = "JDHEBEIDETAIL";
	public static final String TEMP_CUSTOMSFILECONFIG_TDINANSHA = "TDINANSHA";
	public static final String TEMP_CUSTOMSFILECONFIG_TDININGBO = "TDININGBO";
	public static final String TEMP_CUSTOMSFILECONFIG_TDITIANJIN = "TDITIANJIN";
	public static final String TEMP_CUSTOMSFILECONFIG_TDIZHENGZHOU = "TDIZHENGZHOU";
	public static final String TEMP_CUSTOMSFILECONFIG_HPZIYING = "HPZIYING";
	public static final String TEMP_CUSTOMSFILECONFIG_JDCHONGQING = "JDCHONGQING";
	public static final String TEMP_CUSTOMSFILECONFIG_NBCIXI = "NBCIXI";

	/** 应收账单明细表t_receive_bill_item */
	/**
	 * 数据来源：0-单据 1-手动添加
	 */
	public static final String RECEIVEBILLITEM_DATASOURCE_0 = "0";
	public static final String RECEIVEBILLITEM_DATASOURCE_1 = "1";
	public static ArrayList<DerpBasic> receiveBillItem_dataSourceList = new ArrayList<DerpBasic>();

	/** 应收账单费用明细表t_receive_bill_cost_item */
	/**
	 * 补扣款类型 0-补款 1-扣款
	 */
	public static final String RECEIVEBILLCOST_BILLTYPE_0 = "0";
	public static final String RECEIVEBILLCOST_BILLTYPE_1 = "1";
	public static ArrayList<DerpBasic> receiveBillCost_billTypeList = new ArrayList<DerpBasic>();

	/** 应收账单操作记录表 t_receive_bill_operate */
	/** 操作节点 0-提交 1-审核通过 2-审核驳回 3-作废 4-作废审核通过 5—作废审核驳回 6-开票 7-发票签章 8-核销 */
	public static final String RECEIVEBILLOPERATE_OPERATENODE_0 = "0";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_1 = "1";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_2 = "2";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_3 = "3";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_4 = "4";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_5 = "5";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_6 = "6";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_7 = "7";
	public static final String RECEIVEBILLOPERATE_OPERATENODE_8 = "8";
	public static ArrayList<DerpBasic> receiveBillOperate_operateNodeList = new ArrayList<DerpBasic>();

	/** 应收账单审批记录表t_receive_bill_audit_item */
	/** 审批结果 00-审批通过 01-驳回 */
	public static final String RECEIVEBILLAUDIT_AUDITRESULT_00 = "00";
	public static final String RECEIVEBILLAUDIT_AUDITRESULT_01 = "01";
	public static ArrayList<DerpBasic> receiveBillAudit_auditResultList = new ArrayList<DerpBasic>();

	/** 审批类型 0-应收审核 1-作废审核 */
	public static final String RECEIVEBILLAUDIT_AUDITTYPE_0 = "0";
	public static final String RECEIVEBILLAUDIT_AUDITTYPE_1 = "1";
	public static ArrayList<DerpBasic> receiveBillAudit_auditTypeList = new ArrayList<DerpBasic>();

	/** 平台采购订单 单据状态：1:待提交 2.未转销售,3:已转销售 */
	public static final String PLATFORM_PURCHASE_STATUS_1 = "1";
	public static final String PLATFORM_PURCHASE_STATUS_2 = "2";
	public static final String PLATFORM_PURCHASE_STATUS_3 = "3";
	public static ArrayList<DerpBasic> platformPurchase_statusList = new ArrayList<DerpBasic>();

	/**
	 * 平台采购订单 平台状态： 1.待发货确认、2.等待签收、3.等待入库、4.部分收货、5已完成 、 6.待供应商发货、7.等待收货 、8.收货完成
	 * 、9.待审核 、10.待确认,11.审核不通过 12-已审核
	 */
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_1 = "待发货确认";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_2 = "等待签收";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_3 = "等待入库";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_4 = "部分收货";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_5 = "已完成";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_6 = "待供应商发货";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_7 = "等待收货";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_8 = "收货完成";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_9 = "待审核";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_10 = "待确认";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_11 = "审核不通过";
	public static final String PLATFORM_PURCHASE_PLATFORM_STATUS_12 = "已审核";

	public static ArrayList<DerpBasic> platformPurchase_platformStatusList = new ArrayList<DerpBasic>();

	/** 库位调整单表头 t_inventory_location_adjustment_order */
	/** 订单状态:001:待审核,027:审核中,003:已审核,006:已删除 */
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_001 = "001";
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_027 = "027";
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_003 = "003";
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_006 = "006";
	public static ArrayList<DerpBasic> inventoryLocationAdjustmentOrder_statusList = new ArrayList<DerpBasic>();

	/** 单据类型:DSDD:电商订单，DBRK:调拨入库，DBCK:调拨出库，XSCK:销售出库 */
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DSDD = "DSDD";
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DBRK = "DBRK";
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DBCK = "DBCK";
	public static final String INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_XSCK = "XSCK";
	public static ArrayList<DerpBasic> inventoryLocationAdjustmentOrder_typeList = new ArrayList<DerpBasic>();

	/** 库位调整单表体 t_inventory_location_adjustment_order_item */
	/** 库存类型 0：好品 1：坏品 */
	public static final String INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_0 = "0";
	public static final String INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_1 = "1";
	public static ArrayList<DerpBasic> inventoryLocationAdjustmentOrderItem_inventoryTypeList = new ArrayList<DerpBasic>();

	/** 平台费用订单 t_platform_cost_order */
	/** 单据状态： 1-待确认 2-已确认 3-已转账单 */
	public static final String PLATFORM_COST_ORDER_STATUS_1 = "1";
	public static final String PLATFORM_COST_ORDER_STATUS_2 = "2";
	public static final String PLATFORM_COST_ORDER_STATUS_3 = "3";
	public static ArrayList<DerpBasic> platformCostOrder_platformCostStatusList = new ArrayList<DerpBasic>();
	/** 付款主体 1 补款 2 扣款 */
	public static final String PLATFORM_COST_ORDER_COST_TYPE_1 = "1";
	public static final String PLATFORM_COST_ORDER_COST_TYPE_2 = "2";
	public static ArrayList<DerpBasic> platformCostOrder_costTypeList = new ArrayList<DerpBasic>();

	/** 单据来源 1:爬虫账单 */
	public static final String PLATFORM_COST_ORDER_source_1 = "1";
	public static ArrayList<DerpBasic> platformCostOrder_sourceList = new ArrayList<DerpBasic>();

	/** 应收账单发票关联表 t_receive_bill_invoice */
	/** 发票状态： 01-待签章 02-已作废 03-已签章 */
	public static final String RECEIVEBILLINVOICE_STATUS_01 = "01";
	public static final String RECEIVEBILLINVOICE_STATUS_02 = "02";
	public static final String RECEIVEBILLINVOICE_STATUS_03 = "03";
	public static ArrayList<DerpBasic> receiveBillInvoice_statusList = new ArrayList<DerpBasic>();

	/** NC同步状态： 0-未同步 1-已同步 2-作废同步 */
	public static final String RECEIVEBILLINVOICE_NCSYNSTATUS_0 = "0";
	public static final String RECEIVEBILLINVOICE_NCSYNSTATUS_1 = "1";
	public static final String RECEIVEBILLINVOICE_NCSYNSTATUS_2 = "2";
	public static ArrayList<DerpBasic> receiveBillInvoice_ncSynStatusList = new ArrayList<DerpBasic>();

	/** 发票类型 0-ToB 1-ToC */
	public static final String RECEIVEBILLINVOICE_INVOICETYPE_0 = "0";
	public static final String RECEIVEBILLINVOICE_INVOICETYPE_1 = "1";
	public static final String RECEIVEBILLINVOICE_INVOICETYPE_2 = "2";
	public static ArrayList<DerpBasic> receiveBillInvoice_invoiceTypeList = new ArrayList<DerpBasic>();

	/** 费项配置表 t_settlement_config */
	/** 层级 1:一级,2:二级 */
	public static final String SETTLEMENTCONFIG_LEVRL_1 = "1";
	public static final String SETTLEMENTCONFIG_LEVRL_2 = "2";
	public static ArrayList<DerpBasic> settlementConfig_levelList = new ArrayList<DerpBasic>();

	/** 状态(1使用中,0已禁用) */
	public static final String SETTLEMENTCONFIG_STATUS_0 = "0";
	public static final String SETTLEMENTCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> settlementConfig_statusList = new ArrayList<DerpBasic>();

	/** 适用客户:1通用,2指定客户 */
	public static final String SETTLEMENTCONFIG_SUITABLECUSTOMER1 = "1";
	public static final String SETTLEMENTCONFIG_SUITABLECUSTOMER2 = "2";
	public static ArrayList<DerpBasic> settlementConfig_suitableCustomerList = new ArrayList<DerpBasic>();

	/** 适用类型:1.To B 2.To C 3.ToB和ToC */
	public static final String SETTLEMENTCONFIG_TYPE1 = "1";
	public static final String SETTLEMENTCONFIG_TYPE2 = "2";
	public static final String SETTLEMENTCONFIG_TYPE3 = "3";
	public static ArrayList<DerpBasic> settlementConfig_typeList = new ArrayList<DerpBasic>();

	/** 适用模块:1.应付 2.应收 3.预付 4.预收 */
	public static final String SETTLEMENTCONFIG_MODULETYPE1 = "1";
	public static final String SETTLEMENTCONFIG_MODULETYPE2 = "2";
	public static final String SETTLEMENTCONFIG_MODULETYPE3 = "3";
	public static final String SETTLEMENTCONFIG_MODULETYPE4 = "4";
	public static ArrayList<DerpBasic> settlementConfig_moduleTypeList = new ArrayList<DerpBasic>();

	/** 平台费项配映射表 t_platform_settlement_config */
	/** 状态(1使用中,0已禁用) */
	public static final String PLATFORMSETTLEMENTCONFIG_STATUS_0 = "0";
	public static final String PLATFORMSETTLEMENTCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> platform_settlementConfig_statusList = new ArrayList<DerpBasic>();

	/** 发票来源 1-应收账单 2-平台结算单 */
	public static final String RECEIVEINVOICE_SOURCE_1 = "1";
	public static final String RECEIVEINVOICE_SOURCE_2 = "2";

	/** SD类型表 t_sd_type_config */
	/** SD类型 1-单比例 2-多比例 */
	public static final String SDTYPECONFIG_TYPE_1 = "1";
	public static final String SDTYPECONFIG_TYPE_2 = "2";
	public static ArrayList<DerpBasic> sdtypeconfig_typeList = new ArrayList<DerpBasic>();

	/** SD状态 1-启用 0-禁用 */
	public static final String SDTYPECONFIG_STATUS_1 = "1";
	public static final String SDTYPECONFIG_STATUS_0 = "0";
	public static ArrayList<DerpBasic> sdtypeconfig_statusList = new ArrayList<DerpBasic>();

	/** SD采购表 t_sd_purchase_config */
	/** 审核状态 1-已审核 0-待审核 */
	public static final String SDPURCHASE_STATUS_1 = "1";
	public static final String SDPURCHASE_STATUS_0 = "0";
	public static ArrayList<DerpBasic> sdPurchase_statusList = new ArrayList<DerpBasic>();

	/** 采购SD表 t_purchase_sd_order */
	/** SD单类型 1-采购SD，2-采购退SD，3-调整SD */
	public static final String PURCHASE_SD_ORDER_TYPE_1 = "1";
	public static final String PURCHASE_SD_ORDER_TYPE_2 = "2";
	public static final String PURCHASE_SD_ORDER_TYPE_3 = "3";
	public static ArrayList<DerpBasic> purchaseSdOrder_typeList = new ArrayList<DerpBasic>();

	/** To C暂估应收表 t_toc_temporary_receive_bill */
	/** 结算状态：0-未结算 1-部分结算 2-已结算 */
	public static final String TOCTEMPBILL_SETTLEMENTSTATUS_0 = "0";
	public static final String TOCTEMPBILL_SETTLEMENTSTATUS_1 = "1";
	public static final String TOCTEMPBILL_SETTLEMENTSTATUS_2 = "2";
	public static ArrayList<DerpBasic> toctempbill_settlementStatusList = new ArrayList<DerpBasic>();
	/** To C暂估应收明细表 t_toc_temporary_receive_bill_item */
	/** 结算标识：0-未核销 1-已红冲 2-已核销 */
	public static final String TOCTEMPITEMBILL_SETTLEMENTMARK_0 = "0";
	public static final String TOCTEMPITEMBILL_SETTLEMENTMARK_1 = "1";
	public static final String TOCTEMPITEMBILL_SETTLEMENTMARK_2 = "2";
	public static ArrayList<DerpBasic> tocTempItemBill_settlementMarkList = new ArrayList<DerpBasic>();
	/** 单据类型：0-发货订单 1-退款订单 2-差异调整发货订单 3-差异调整退货订单*/
	public static final String TOCTEMPITEMBILL_ORDERTYPE_0 = "0";
	public static final String TOCTEMPITEMBILL_ORDERTYPE_1 = "1";
	public static final String TOCTEMPITEMBILL_ORDERTYPE_2 = "2";
	public static final String TOCTEMPITEMBILL_ORDERTYPE_3 = "3";
	public static ArrayList<DerpBasic> tocTempItemBill_orderTypeList = new ArrayList<DerpBasic>();

	/** To C暂估费用明细表 t_toc_temporary_receive_bill_cost_item */
	/** 结算标识：0-未核销 1-已红冲 2-已核销 */
	public static final String TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_0 = "0";
	public static final String TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_1 = "1";
	public static final String TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_2 = "2";
	public static ArrayList<DerpBasic> tocTempItemCostBill_settlementMarkList = new ArrayList<DerpBasic>();
	/** 单据类型：1-发货订单 0-退款订单 2-差异调整退货单 差异调整发货单*/
	public static final String TOCTEMPITEMCOSTBILL_ORDERTYPE_0 = "0";
	public static final String TOCTEMPITEMCOSTBILL_ORDERTYPE_1 = "1";
	public static final String TOCTEMPITEMCOSTBILL_ORDERTYPE_2 = "2";
	public static final String TOCTEMPITEMCOSTBILL_ORDERTYPE_3 = "3";
	public static ArrayList<DerpBasic> tocTempItemCostBill_orderTypeList = new ArrayList<DerpBasic>();

	/** To C结算单表 t_toc_settlement_receive_bill */
	/**
	 * 账单状态 账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-作废待审 06-已作废 07-生成中 08-生成失败
	 */
	public static final String TOCRECEIVEBILL_BILLSTATUS_00 = "00";
	public static final String TOCRECEIVEBILL_BILLSTATUS_01 = "01";
	public static final String TOCRECEIVEBILL_BILLSTATUS_02 = "02";
	public static final String TOCRECEIVEBILL_BILLSTATUS_03 = "03";
	public static final String TOCRECEIVEBILL_BILLSTATUS_04 = "04";
	public static final String TOCRECEIVEBILL_BILLSTATUS_05 = "05";
	public static final String TOCRECEIVEBILL_BILLSTATUS_06 = "06";
	public static final String TOCRECEIVEBILL_BILLSTATUS_07 = "07";
	public static final String TOCRECEIVEBILL_BILLSTATUS_08 = "08";
	public static ArrayList<DerpBasic> tocReceiveBill_billStatusList = new ArrayList<DerpBasic>();

	/**
	 * NC应收状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步
	 */
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_1 = "1";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_2 = "2";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_3 = "3";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_4 = "4";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_5 = "5";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_6 = "6";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_7 = "7";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_8 = "8";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_9 = "9";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_10 = "10";
	public static final String TOCRECEIVEBILL_NCSYNSTATUS_11 = "11";
	public static ArrayList<DerpBasic> tocReceiveBill_nvSynList = new ArrayList<DerpBasic>();

	/** 开票状态 00-待开票 01-待签章 02-已作废 03-已签章 */
	public static final String TOCRECEIVEBILL_INVOICESTATUS_00 = "00";
	public static final String TOCRECEIVEBILL_INVOICESTATUS_01 = "01";
	public static final String TOCRECEIVEBILL_INVOICESTATUS_02 = "02";
	public static final String TOCRECEIVEBILL_INVOICESTATUS_03 = "03";
	public static ArrayList<DerpBasic> receiveBill_tocInvoiceStatusList = new ArrayList<DerpBasic>();

	/** To C结算单费用明细表 t_toc_settlement_receive_bill_cost_item */
	/** 补扣款类型 0-补款 1-扣款 */
	public static final String TOCRECEIVEBILLCOST_BILLTYPE_0 = "0";
	public static final String TOCRECEIVEBILLCOST_BILLTYPE_1 = "1";
	public static ArrayList<DerpBasic> tocReceiveBillCost_billTypeList = new ArrayList<DerpBasic>();
	/** 数据来源 0-导入 1-手动添加 */
	public static final String TOCRECEIVEBILLCOST_DATASOURCE_0 = "0";
	public static final String TOCRECEIVEBILLCOST_DATASOURCE_1 = "1";
	public static ArrayList<DerpBasic> tocReceiveBillCost_datasourceList = new ArrayList<DerpBasic>();

	/** toc应收账单审批记录表t_toc_settlement_receive_bill_audit_item */
	/** 审批结果 00-审批通过 01-驳回 */
	public static final String TOCRECEIVEBILLAUDIT_AUDITRESULT_00 = "00";
	public static final String TOCRECEIVEBILLAUDIT_AUDITRESULT_01 = "01";
	public static ArrayList<DerpBasic> tocReceiveBillAudit_auditResultList = new ArrayList<DerpBasic>();

	/** 审批类型 0-应收审核 1-作废审核 */
	public static final String TOCRECEIVEBILLAUDIT_AUDITTYPE_0 = "0";
	public static final String TOCRECEIVEBILLAUDIT_AUDITTYPE_1 = "1";
	public static ArrayList<DerpBasic> tocReceiveBillAudit_auditTypeList = new ArrayList<DerpBasic>();

	/** 清关单证配置 进出仓配置 0-进仓，1-出仓 */
	public static final String CUSTOMSFILECONFIG_DEPOTCONFIG_0 = "0";
	public static final String CUSTOMSFILECONFIG_DEPOTCONFIG_1 = "1";
	public static ArrayList<DerpBasic> customsFileConfig_DepotConfigList = new ArrayList<DerpBasic>();

	/** 状态：0-已停用 1-已启用 */
	public static final String CUSTOMSFILECONFIG_STATUS_0 = "0";
	public static final String CUSTOMSFILECONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> customsfileconfig_statusList = new ArrayList<DerpBasic>();

	/** 欧莱雅采购订单t_oreal_purchase_order */
	/** 来源:1.欧莱雅接口 */
	public static final String OREAL_PURCHASE_ORDER_SOURCE_1 = "1";
	public static ArrayList<DerpBasic> orealPurchaseOrder_sourceList = new ArrayList<DerpBasic>();

	/** 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱 05-纸质托盘*/
	public static final String ORDER_TORRPACKTYPE_01 = "01";
	public static final String ORDER_TORRPACKTYPE_02 = "02";
	public static final String ORDER_TORRPACKTYPE_03 = "03";
	public static final String ORDER_TORRPACKTYPE_04 = "04";
	public static final String ORDER_TORRPACKTYPE_05 = "05";
	public static ArrayList<DerpBasic> order_torrpacktypeList = new ArrayList<DerpBasic>();

	/** 装箱明细表 t_packing_details */
	/** 单据类型 1-预申报单 2-调拨订单 */
	public static final String PACKINGDETAILS_ORDERTYPE_1 = "1";
	public static final String PACKINGDETAILS_ORDERTYPE_2 = "2";
	public static ArrayList<DerpBasic> packingDetails_orderTypeList = new ArrayList<DerpBasic>();

	/** 领料单 t_material_order */
	/** 领料单状态:001:待审核,002:审核中,003:已审核,027:出库中,018:已出库,006:已删除 017-待出库 */
	public static final String MATERIALORDER_STATE_001 = "001";
	public static final String MATERIALORDER_STATE_002 = "002";
	public static final String MATERIALORDER_STATE_003 = "003";
	public static final String MATERIALORDER_STATE_017 = "017";
	public static final String MATERIALORDER_STATE_027 = "027";
	public static final String MATERIALORDER_STATE_018 = "018";
	public static ArrayList<DerpBasic> materialOrder_stateList = new ArrayList<DerpBasic>();

	/** 领料单 是否同关区0-否，1-是 */
	public static final String MATERIALORDER_ISSAMEAREA_0 = "0";
	public static final String MATERIALORDER_ISSAMEAREA_1 = "1";
	public static ArrayList<DerpBasic> materialOrder_IsSameAreaList = new ArrayList<DerpBasic>();

	/** 领料出库单 t_material_out_depot */
	/** 状态: 017-待出库 018-已出库 027-出库中 006-已删除 */
	public static final String MATERIALOUTDEPOT_STATUS_017 = "017";
	public static final String MATERIALOUTDEPOT_STATUS_027 = "027";
	public static final String MATERIALOUTDEPOT_STATUS_018 = "018";
	public static ArrayList<DerpBasic> materialOutDepot_statusList = new ArrayList<DerpBasic>();

	/** 应收账单核销记录表 t_receive_bill_verify_item */
	/** 单据类型 1-预收账单 2-收款单 */
	public static final String RECEIVEBILLVERIFY_TYPE_1 = "1";
	public static final String RECEIVEBILLVERIFY_TYPE_2 = "2";
	public static ArrayList<DerpBasic> receiveBillVerify_typeList = new ArrayList<DerpBasic>();

	/** 账期提醒配置 t_accounting_reminder_config */
	/** 提醒类型 1-收款 2-付款 */
	public static final String ACCOUNTINGREMINDERCONFIG_REMINDER_TYPE_1 = "1";
	public static final String ACCOUNTINGREMINDERCONFIG_REMINDER_TYPE_2 = "2";
	public static ArrayList<DerpBasic> accountingReminderConfig_reminderTypeList = new ArrayList<DerpBasic>();

	/** 账期提醒配置 t_accounting_reminder_config */
	/** 提醒类型 1-发票日期 2-上架日期 */
	public static final String ACCOUNTINGREMINDERCONFIG_BASE_DATE_1 = "1";
	public static final String ACCOUNTINGREMINDERCONFIG_BASE_DATE_2 = "2";
	public static ArrayList<DerpBasic> accountingReminderConfig_baseDateList = new ArrayList<DerpBasic>();

	/** 账期提醒配置 t_accounting_reminder_config */
	/** 状态 0-待审核 1-已审核 */
	public static final String ACCOUNTINGREMINDERCONFIG_STATUS_0 = "0";
	public static final String ACCOUNTINGREMINDERCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> accountingReminderConfig_statusList = new ArrayList<DerpBasic>();

	/** 账期提醒配置节点 t_accounting_reminder_item */
	/** 节点 1-付款 2-出账单 3-账单确认 4-开票 5-回款 */
	public static final String ACCOUNTINGREMINDERITEM_NOTE_1 = "1";
	public static final String ACCOUNTINGREMINDERITEM_NOTE_2 = "2";
	public static final String ACCOUNTINGREMINDERITEM_NOTE_3 = "3";
	public static final String ACCOUNTINGREMINDERITEM_NOTE_4 = "4";
	public static final String ACCOUNTINGREMINDERITEM_NOTE_5 = "5";
	public static ArrayList<DerpBasic> accountingReminderConfig_noteList = new ArrayList<DerpBasic>();

	/** 账期提醒配置节点 t_accounting_reminder_item */
	/** 基准单位 1-工作日 2-自然日 */
	public static final String ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_1 = "1";
	public static final String ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_2 = "2";
	public static ArrayList<DerpBasic> accountingReminderConfig_benchmarkUnitList = new ArrayList<DerpBasic>();

	/** 项目额度配置 t_project_quota_config */
	/** 状态 0-待审核 1-已审核 */
	public static final String PROJECTQUOTACONFIG_STATUS_0 = "0";
	public static final String PROJECTQUOTACONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> projectquotaconfig_statusList = new ArrayList<DerpBasic>();

	/** 项目额度配置 t_project_quota_config */
	/** 额度类型 额度类型 1-品牌额度  2-客户额度*/
	public static final String PROJECTQUOTACONFIG_quotaType_1 = "1";
	public static ArrayList<DerpBasic> projectquotaconfig_quotaTypeList = new ArrayList<DerpBasic>();

	/** 项目额度配置 t_quota_period_config */
	/** 额度类型 1-品牌额度  2-客户额度 */
	public static final String QUOTACONFIG_quotaType_1 = "1";
	public static final String QUOTACONFIG_quotaType_2 = "2";
	public static ArrayList<DerpBasic> quotaconfig_quotaTypeList = new ArrayList<DerpBasic>();

	/** 客户额度配置 */
	/** 状态 0-待审核 1-已审核 */
	public static final String CUSTOMERQUOTACONFIG_STATUS_0 = "0";
	public static final String CUSTOMERQUOTACONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> customerQuotaConfig_statusList = new ArrayList<DerpBasic>();

	/** 客户额度预警 */
	/** 应收类型 1-应收 2-费用 */
	public static final String CUSTOMERQUOTAWARNINGITEM_RECEIVETYPE_1 = "1";
	public static final String CUSTOMERQUOTAWARNINGITEM_RECEIVETYPE_2 = "2";
	public static ArrayList<DerpBasic> customerQuotaWarningItem_receiveTypeList = new ArrayList<DerpBasic>();

	/** 项目额度配置 t_project_quota_warning_item */
	/**  明细类型 1-累计采购冻结金额 2-累计销售已回款金额 3.累计采购已付金额 */
	public static final String PROJECTQUOTAWARNINGITEM_type_1 = "1";
	public static final String PROJECTQUOTAWARNINGITEM_type_2 = "2";
	public static final String PROJECTQUOTAWARNINGITEM_type_3= "3";
	public static ArrayList<DerpBasic> projectquotaWarningItem_TypeList = new ArrayList<DerpBasic>();

	/** 项目额度配置 t_project_quota_warning_item */
	/** 应收类型 1-应收 2-费用 */
	public static final String PROJECTQUOTAWARNINGITEM_receiveType_1 = "1";
	public static final String PROJECTQUOTAWARNINGITEM_receiveType_2 = "2";
	public static ArrayList<DerpBasic> projectquotaWarningItem_receiveTypeList = new ArrayList<DerpBasic>();

	/** ToB暂估核销表 t_tob_temporary_receive_bill */
	/** 销售类型 1:购销-整批结算 2:购销-实销实结 3-购销退货*/
	public static final String TOBTEMPRECEIVEBILL_SALETYPE_1 = "1";
	public static final String TOBTEMPRECEIVEBILL_SALETYPE_2 = "2";
	public static final String TOBTEMPRECEIVEBILL_SALETYPE_3 = "3";
	public static ArrayList<DerpBasic> tobTempReceiveBill_saleTypeList = new ArrayList<DerpBasic>();

	/** 应收结算状态 1-已上架未结算 2-部分结算 5-已结算 */
	public static final String TOBTEMPRECEIVEBILL_STATUS_1 = "1";
	public static final String TOBTEMPRECEIVEBILL_STATUS_2 = "2";
	public static final String TOBTEMPRECEIVEBILL_STATUS_5 = "5";
	public static ArrayList<DerpBasic> tobTempReceiveBill_statusList = new ArrayList<DerpBasic>();

	/** 单据类型 1-上架单 2-销售退货订单 */
	public static final String TOBTEMPRECEIVEBILL_ORDERTYPE_1 = "1";
	public static final String TOBTEMPRECEIVEBILL_ORDERTYPE_2 = "2";

	/** 是否红冲单：0-否，1-是 */
	public static final String TOBTEMPRECEIVEBILL_ISWRITEOFF_0 = "0";
	public static final String TOBTEMPRECEIVEBILL_ISWRITEOFF_1 = "1";

	/** ToB暂估核销表 t_tob_temporary_receive_bill_rebate_item */
	/** 是否红冲单：0-否，1-是 */
	public static final String TOBTEMPRECEIVEBILLRABATEITEM_ISWRITEOFF_0 = "0";
	public static final String TOBTEMPRECEIVEBILLRABATEITEM_ISWRITEOFF_1 = "1";


	/** 销售sd */
	/** 销售sd单 数据来源 1-系统生成 2-手工导入*/
	public static final String SALE_SD_ORDER_ORDERSOURCE_1 = "1";
	public static final String SALE_SD_ORDER_ORDERSOURCE_2 = "2";
	public static ArrayList<DerpBasic> saleSdOrder_orderSourceList = new ArrayList<DerpBasic>();

	/** 销售sd单 数据类型 1-上架单 2-销售退入库单*/
	public static final String SALE_SD_ORDER_ORDERTYPE_1 = "1";
	public static final String SALE_SD_ORDER_ORDERTYPE_2 = "2";
	public static ArrayList<DerpBasic> saleSdOrder_orderTypeList = new ArrayList<DerpBasic>();
	/**销售sd**/


	/** 预付款单 */
	/** 预付款单 账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-待作废、05-已作废、06-待核销、07已核销 */
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_00 = "00";
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_01 = "01";
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_02 = "02";
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_03 = "03";
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_04 = "04";
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_05 = "05";
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_06 = "06";
	public static final String ADVANCE_PAYMENT_BILL_BILLSTATUS_07 = "07";
	public static ArrayList<DerpBasic> advancePaymentBill_billStatusList = new ArrayList<DerpBasic>();

	/** 预付款单 */
	/** 预付款单 审批方式 1-OA审批 2-经分销审批 */
	public static final String ADVANCE_PAYMENT_BILL_AUDIT_METHOD_1 = "1";
	public static final String ADVANCE_PAYMENT_BILL_AUDIT_METHOD_2 = "2";
	public static ArrayList<DerpBasic> advancePaymentBill_auditMethodList = new ArrayList<DerpBasic>();

	/** 操作日志 */
	/** 操作日志 模块 1-采购 2-预付 3-应付 4-采购预申报 5-销售 6-销售预申报 7-部门额度配置 8-客户额度 9-项目额度 10-赊销单 11-应收 12-sd核销配置 13-爬虫商品对照表
	 * 14-平台费用配置 15-销售SD配置 16-销售退 17-销售退预申报 18.事业移库单 19-领料出库单 20-应收关账 21-采购入库单 22-电商退货单 23-库位调整单
	 */
	public static final String OPERATE_LOG_MODULE_1 = "1";
	public static final String OPERATE_LOG_MODULE_2 = "2";
	public static final String OPERATE_LOG_MODULE_3 = "3";
	public static final String OPERATE_LOG_MODULE_4 = "4";
	public static final String OPERATE_LOG_MODULE_5 = "5";
	public static final String OPERATE_LOG_MODULE_6 = "6";
	public static final String OPERATE_LOG_MODULE_7 = "7";
	public static final String OPERATE_LOG_MODULE_8 = "8";
	public static final String OPERATE_LOG_MODULE_9 = "9";
	public static final String OPERATE_LOG_MODULE_10 = "10";
	public static final String OPERATE_LOG_MODULE_11 = "11";
	public static final String OPERATE_LOG_MODULE_12 = "12";
	public static final String OPERATE_LOG_MODULE_13 = "13";
	public static final String OPERATE_LOG_MODULE_14 = "14";
	public static final String OPERATE_LOG_MODULE_15 = "15";
	public static final String OPERATE_LOG_MODULE_16 = "16";
	public static final String OPERATE_LOG_MODULE_17 = "17";
	public static final String OPERATE_LOG_MODULE_18 = "18";
	public static final String OPERATE_LOG_MODULE_19 = "19";
	public static final String OPERATE_LOG_MODULE_20 = "20";
	public static final String OPERATE_LOG_MODULE_21 = "21";
	public static final String OPERATE_LOG_MODULE_22 = "22";
	public static final String OPERATE_LOG_MODULE_23 = "23";
	public static ArrayList<DerpBasic> operateLog_ModuleList = new ArrayList<DerpBasic>();

	/** 应付款单 */
	/** 应付款单 账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-已付款、05-待作废、06-已作废 07-部分付款*/
	public static final String PAYMENT_BILL_BILLSTATUS_00 = "00";
	public static final String PAYMENT_BILL_BILLSTATUS_01 = "01";
	public static final String PAYMENT_BILL_BILLSTATUS_02 = "02";
	public static final String PAYMENT_BILL_BILLSTATUS_03 = "03";
	public static final String PAYMENT_BILL_BILLSTATUS_04 = "04";
	public static final String PAYMENT_BILL_BILLSTATUS_05 = "05";
	public static final String PAYMENT_BILL_BILLSTATUS_06 = "06";
	public static final String PAYMENT_BILL_BILLSTATUS_07 = "07";
	public static ArrayList<DerpBasic> paymentBill_billStatusList = new ArrayList<DerpBasic>();
	/*平台费用配置*/
	/*状态 0-未审核 1-已审核*/
	public static final String PLATFORMCOSTCONFIG_STATUS_0 = "0";
	public static final String PLATFORMCOSTCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> platFormConfigConfig_statusList = new ArrayList<DerpBasic>();

	/*是否全子品牌 0-否 1-是*/
	public static final String PLATFORMCOSTCONFIG_ISAllBRAND_0 = "0";
	public static final String PLATFORMCOSTCONFIG_ISAllBRAND_1 = "1";
	public static ArrayList<DerpBasic> platFormConfigConfig_IsAllBrandList = new ArrayList<DerpBasic>();

	/** 应付款单 */
	/** 应付款单 审批方式 1-OA审批 2-经分销审批 */
	public static final String PAYMENT_BILL_AUDIT_METHOD_1 = "1";
	public static final String PAYMENT_BILL_AUDIT_METHOD_2 = "2";
	public static ArrayList<DerpBasic> paymentBill_auditMethodList = new ArrayList<DerpBasic>();

	/** 应付款单 */
	/** 应付款单 NC状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步*/
	public static final String PAYMENT_BILL_NCSTATUS_1 = "1";
	public static final String PAYMENT_BILL_NCSTATUS_2 = "2";
	public static final String PAYMENT_BILL_NCSTATUS_3 = "3";
	public static final String PAYMENT_BILL_NCSTATUS_4 = "4";
	public static final String PAYMENT_BILL_NCSTATUS_5 = "5";
	public static final String PAYMENT_BILL_NCSTATUS_6 = "6";
	public static final String PAYMENT_BILL_NCSTATUS_7 = "7";
	public static final String PAYMENT_BILL_NCSTATUS_8 = "8";
	public static final String PAYMENT_BILL_NCSTATUS_9 = "9";
	public static final String PAYMENT_BILL_NCSTATUS_10 = "10";
	public static final String PAYMENT_BILL_NCSTATUS_11 = "11";
	public static ArrayList<DerpBasic> paymentBill_ncStatusList = new ArrayList<DerpBasic>();

	/** 应付核销记录明细 */
	/** 应付核销记录明细 单据类型1-预付 2-NC*/
	public static final String PAYMENT_BILL_VERIFICATE_BILL_STATUS_1 = "1";
	public static final String PAYMENT_BILL_VERIFICATE_BILL_STATUS_2 = "2";
	public static ArrayList<DerpBasic> paymentBillVerificate_billStatusList = new ArrayList<DerpBasic>();

	/** 应付打印标识 */
	/** 应付打印标识 1：已打印 0：未打印*/
	public static final String PAYMENT_BILL_PRINTING_STATE_1 = "1";
	public static final String PAYMENT_BILL_PRINTING_STATE_0 = "0";
	public static ArrayList<DerpBasic> paymentBill_printingStateList = new ArrayList<DerpBasic>();


	/** 应付否垫资标识 */
	/** 应付否垫资标识 是否垫资 1：否 0：是*/
	public static final String PAYMENT_BILL_ENDOWMENT_STATE_1 = "1";
	public static final String PAYMENT_BILL_ENDOWMENT_STATE_0 = "0";
	public static ArrayList<DerpBasic> paymentBill_endowmentStateList = new ArrayList<DerpBasic>();

	/** 应付单据类型 */
	/** 应付单据类型 单据类型 1：采购订单 2：采购发票*/
	public static final String PAYMENT_BILL_TYPE_STATE_1 = "1";
	public static final String PAYMENT_BILL_TYPE_STATE_2 = "2";
	public static ArrayList<DerpBasic> paymentBill_typeStateList = new ArrayList<DerpBasic>();

	/** 应付费用明细 */
	/** 应付费用明细 单据类型 1-补款 0-扣款 */
	public static final String PAYMENT_COST_ITEM_TYPE_0 = "0";
	public static final String PAYMENT_COST_ITEM_TYPE_1 = "1";
	public static ArrayList<DerpBasic> paymentCost_item_typeList = new ArrayList<DerpBasic>();

	/** 物流联系人模版 */
	/** 类型 1-发货人/提货信息 2-收货人/卸货信息 3-通知人 4-对接人 5-承运商信*/
	public static final String LOGISTICS_CONTACT_TEMPLATE_TYPE_1 = "1";
	public static final String LOGISTICS_CONTACT_TEMPLATE_TYPE_2 = "2";
	public static final String LOGISTICS_CONTACT_TEMPLATE_TYPE_3 = "3";
	public static final String LOGISTICS_CONTACT_TEMPLATE_TYPE_4 = "4";
	public static final String LOGISTICS_CONTACT_TEMPLATE_TYPE_5 = "5";
	public static ArrayList<DerpBasic> logisticsContactTemplate_TypeList = new ArrayList<DerpBasic>();

	/** 物流委托书 类型 1-采购预申报 2-销售预申报*/
	public static final String LOGISTICS_ATTORNEY_TYPE_1 = "1";
	public static final String LOGISTICS_ATTORNEY_TYPE_2 = "2";
	public static ArrayList<DerpBasic> logisticsAttorney_typeList = new ArrayList<>();

	/** 销售预申报 start*/
	/** 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款 */
	public static final String SALEDECLARE_PAYMENTTERMS_1 = "1";
	public static final String SALEDECLARE_PAYMENTTERMS_2 = "2";
	public static final String SALEDECLARE_PAYMENTTERMS_3 = "3";
	public static ArrayList<DerpBasic> saleDeclare_paymentTermsList = new ArrayList<>();

	/** 贸易条款 1- CIF 2-CRF 3-FOB 4-DAP 5-EXW */
	public static final String SALEDECLARE_TRADETERMS_1 = "1";
	public static final String SALEDECLARE_TRADETERMS_2 = "2";
	public static final String SALEDECLARE_TRADETERMS_3 = "3";
	public static final String SALEDECLARE_TRADETERMS_4 = "4";
	public static final String SALEDECLARE_TRADETERMS_5 = "5";
	public static ArrayList<DerpBasic> saleDeclare_tradeTermsList = new ArrayList<>();

	/** 业务模式  1 购销-整批结算 3 采购执行 4.购销-实销实结 */
	public static final String SALEDECLARE_BUSINESSMODEL_1 = "1";
	public static final String SALEDECLARE_BUSINESSMODEL_3 = "3";
	public static final String SALEDECLARE_BUSINESSMODEL_4 = "4";
	public static ArrayList<DerpBasic> saleDeclare_businessModelList = new ArrayList<>();

	/** 订单状态 001-待打托 002待清关 003-待物流委托 004-已出库 005-部分上架 007-已上架 006已删除 008-出库中 009-待出库*/
	public static final String SALEDECLARE_STATUS_001 = "001";
	public static final String SALEDECLARE_STATUS_002 = "002";
	public static final String SALEDECLARE_STATUS_003 = "003";
	public static final String SALEDECLARE_STATUS_004 = "004";
	public static final String SALEDECLARE_STATUS_005 = "005";
	public static final String SALEDECLARE_STATUS_007 = "007";
	public static final String SALEDECLARE_STATUS_008 = "008";
	public static final String SALEDECLARE_STATUS_009 = "009";
	public static ArrayList<DerpBasic> saleDeclare_statusList = new ArrayList<>();

	/** 接口状态 0-未推接口；1-已推接口；2-接口推送失败 用于海外仓推跨境宝 */
	public static final String SALEDECLARE_APISTATUS_0 = "0";
	public static final String SALEDECLARE_APISTATUS_1 = "1";
	public static final String SALEDECLARE_APISTATUS_2 = "2";
	public static ArrayList<DerpBasic> saleDeclare_apiStatusList = new ArrayList<>();

	/** 是否已生成预申报单 1-是 0-否 */
	public static final String SALEDECLARE_ISGENERATE_1 = "1";
	public static final String SALEDECLARE_ISGENERATE_0 = "0";
	public static ArrayList<DerpBasic> saleDeclare_isGenerateList = new ArrayList<DerpBasic>();

	/** 目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
	public static final String SALEDECLARE_DESTINATION_HP01 = "HP01";
	public static final String SALEDECLARE_DESTINATION_HK01 = "HK01";
	public static final String SALEDECLARE_DESTINATION_GZJC01 = "GZJC01";
	public static ArrayList<DerpBasic> saleDeclare_destinationList = new ArrayList<DerpBasic>();
	/** 销售预申报 end*/

	/** tob应收核销暂估关联表 t_tob_temp_verify_rel*/
	/** 类型 0-明细 1-返利 */
	public static final String TOBTEMPVERIFYREL_TYPE_0 = "0";
	public static final String TOBTEMPVERIFYREL_TYPE_1 = "1";
	public static ArrayList<DerpBasic> tobTempVerifyRel_typeList = new ArrayList<>();

	/** 销售SD核销配置 t_sd_sale_verify_config*/
	/** 核销类型 0-按PO核销 1-按上架核销 */
	public static final String SDSALEVERIFYCONFIG_VERIFYTYPE_0 = "0";
	public static final String SDSALEVERIFYCONFIG_VERIFYTYPE_1 = "1";
	public static ArrayList<DerpBasic> sdSaleVerifyConfig_verifyTypeList = new ArrayList<>();

	/** 状态 0-未审核 1-已启用 2-已停用 */
	public static final String SDSALEVERIFYCONFIG_STAUTS_0 = "0";
	public static final String SDSALEVERIFYCONFIG_STAUTS_1 = "1";
	public static final String SDSALEVERIFYCONFIG_STAUTS_2 = "2";
	public static ArrayList<DerpBasic> sdSaleVerifyConfig_statusList = new ArrayList<>();

	/** 是否按商品核销 0-否 1-是 */
	public static final String SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_0 = "0";
	public static final String SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_1 = "1";
	public static ArrayList<DerpBasic> sdSaleVerifyConfig_isMerchandiseVerifyList = new ArrayList<>();

	/** 赊销单 t_sale_credit_order start*/
	/** 订单状态 001:待提交,002:待收保证金,003:待放款,004:赊销中,005:待收款,006-已删除，007-已收款*/
	public static final String SALECREDIT_STATUS_001 = "001";
	public static final String SALECREDIT_STATUS_002 = "002";
	public static final String SALECREDIT_STATUS_003 = "003";
	public static final String SALECREDIT_STATUS_004 = "004";
	public static final String SALECREDIT_STATUS_005 = "005";
	public static final String SALECREDIT_STATUS_007 = "007";
	public static ArrayList<DerpBasic> saleCredit_statusList = new ArrayList<>();

	/** 是否同步金融系统 0：未同步，1：已同步 */
	public static final String SALECREDIT_IS_SYNC_FINANCE_0 = "0";
	public static final String SALECREDIT_IS_SYNC_FINANCE_1 = "1";
	public static ArrayList<DerpBasic> saleCredit_isSyncFinanceList = new ArrayList<>();

	/** 赊销账单状态 001:待收款，002-已收款，006-已删除 */
	public static final String SALECREDITBILL_STATUS_001 = "001";
	public static final String SALECREDITBILL_STATUS_002 = "002";
	public static ArrayList<DerpBasic> saleCreditBill_statusList = new ArrayList<DerpBasic>();
	/** 赊销单 t_sale_credit_order end*/


	/** 应收账龄报告 t_receive_aging_report 1.ToB 2.ToC */
	public static final String RECEIVEAGING_CHANNEL_TYPE_1 = "1";
	public static final String RECEIVEAGING_CHANNEL_TYPE_2 = "2";
	public static ArrayList<DerpBasic> receiveAging_channelTypeList = new ArrayList<DerpBasic>();

	/** 应收账龄报告 t_receive_aging_report 单据类型 1.To B 暂估 2.2B应收账单  3.To C 暂估 4.2C应收账单*/
	public static final String RECEIVEAGING_ORDERTYPE_TYPE_1 = "1";
	public static final String RECEIVEAGING_ORDERTYPE_TYPE_2 = "2";
	public static final String RECEIVEAGING_ORDERTYPE_TYPE_3 = "3";
	public static final String RECEIVEAGING_ORDERTYPE_TYPE_4 = "4";
	public static ArrayList<DerpBasic> receiveAging_orderTypeList = new ArrayList<DerpBasic>();

	/** 销售退预申报 t_sale_return_declare_order  start **/
	/** 贸易条款 1- CIF 2-CRF 3-FOB 4-DAP 5-EXW */
	public static final String SALERETURNDECLARE_TRADETERMS_1 = "1";
	public static final String SALERETURNDECLARE_TRADETERMS_2 = "2";
	public static final String SALERETURNDECLARE_TRADETERMS_3 = "3";
	public static final String SALERETURNDECLARE_TRADETERMS_4 = "4";
	public static final String SALERETURNDECLARE_TRADETERMS_5 = "5";
	public static ArrayList<DerpBasic> saleReturnDeclare_tradeTermsList = new ArrayList<>();

	/** 订单状态 001-待审核、002-已审核、003-待入仓、004-已入仓、005-已完结、006-已删除 */
	public static final String SALERETURNDECLARE_STATUS_001 = "001";
	public static final String SALERETURNDECLARE_STATUS_002 = "002";
	public static final String SALERETURNDECLARE_STATUS_003 = "003";
	public static final String SALERETURNDECLARE_STATUS_004 = "004";
	public static final String SALERETURNDECLARE_STATUS_005 = "005";
	public static ArrayList<DerpBasic> saleReturnDeclare_statusList = new ArrayList<>();

	/**
	 * 接口状态 0-未推接口；1-已推接口；2-接口推送失败 用于海外仓推跨境宝
	 */
	public static final String SALERETURNDECLARE_APISTATUS_0 = "0";
	public static final String SALERETURNDECLARE_APISTATUS_1 = "1";
	public static ArrayList<DerpBasic> saleReturnDeclare_apiStatusList = new ArrayList<>();

	/** 销售退预申报 t_sale_return_declare_order  end **/

	/** 平台暂估费用单表 t_platform_temporary_cost_order  start **/
	/**
	 * 订单类型 0-发货单 1-退款单
	 */
	public static final String PLATFORMTEMPCOSTORDER_ORDERTYPE_0 = "0";
	public static final String PLATFORMTEMPCOSTORDER_ORDERTYPE_1 = "1";
	public static ArrayList<DerpBasic> platformTempCostOrder_orderTypeList = new ArrayList<>();

	/** 平台暂估费用单表 t_platform_temporary_cost_order  end **/

	/** 应收关账表 t_receive_close_accounts  start **/
	/**
	 * 关账状态 029-未关账 030-已关账
	 */
	public static final String RECEIVE_CLOSE_ACCOUNTS_STATUS_029 = "029";
	public static final String RECEIVE_CLOSE_ACCOUNTS_STATUS_030 = "030";
	public static ArrayList<DerpBasic> receiveCloseAccounts_statusList = new ArrayList<>();

	/** 应收关账 t_receive_close_accounts  end **/


	/** 应收账单跟踪表 t_receive_bill_verification  start **/
	/**
	 * 账单状态 02-待核销 03-部分核销 04-已核销
	 */
	public static final String RECEIVEBILLVERIFICATION_BILLSTATUS_02 = "02";
	public static final String RECEIVEBILLVERIFICATION_BILLSTATUS_03 = "03";
	public static final String RECEIVEBILLVERIFICATION_BILLSTATUS_04 = "04";
	public static final String RECEIVEBILLVERIFICATION_BILLSTATUS_05 = "05";
	public static ArrayList<DerpBasic> receiveBillVerification_billStatusList = new ArrayList<>();

	/**
	 * 账单类型 0-tob 1-toc
	 */
	public static final String RECEIVEBILLVERIFICATION_BILLTYPE_0 = "0";
	public static final String RECEIVEBILLVERIFICATION_BILLTYPE_1 = "1";
	public static ArrayList<DerpBasic> receiveBillVerification_billTypeList = new ArrayList<>();

	/** 应收账单跟踪表 t_receive_bill_verification  end **/

	/** 应收月结快照 t_bill_monthly_snapshot  start **/
	/**
	 * 账单类型 0-tob 1-toc
	 */
	public static final String BILLMONTHLYSNAPSHOT_BILLTYPE_0 = "0";
	public static final String BILLMONTHLYSNAPSHOT_BILLTYPE_1 = "1";
	public static ArrayList<DerpBasic> billMonthlySnapshot_billTypeList = new ArrayList<>();

	/** 应收月结快照 t_bill_monthly_snapshot  end **/

	/** 库位调整单 t_location_adjustment_order  start **/

	/**
	 * 导入单据类型 DSDD-电商订单 DBDD-调拨订单 XSCK-销售出库单 SY-损益 XSTH-销售退货单
	 */
	public static final String LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_DSDD = "DSDD";
	public static final String LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_DBDD = "DBDD";
	public static final String LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_XSCK = "XSCK";
	public static final String LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_SY = "SY";
	public static final String LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_XSTH = "XSTH";
	public static ArrayList<DerpBasic> locationAdjustmentOrder_importOrderTypeList = new ArrayList<>();

	/**
	 * 单据类型 DSDD-电商订单 DBRK-调拨入库单 DBCK-调拨出库单 XSCK-销售出库单 SY-损益 XSTH-销售退货单
	 */
	public static final String LOCATIONADJUSTMENTORDER_ORDERTYPE_DSDD = "DSDD";
	public static final String LOCATIONADJUSTMENTORDER_ORDERTYPE_DBRK = "DBRK";
	public static final String LOCATIONADJUSTMENTORDER_ORDERTYPE_DBCK = "DBCK";
	public static final String LOCATIONADJUSTMENTORDER_ORDERTYPE_XSCK = "XSCK";
	public static final String LOCATIONADJUSTMENTORDER_ORDERTYPE_SY = "SY";
	public static final String LOCATIONADJUSTMENTORDER_ORDERTYPE_XSTH = "XSTH";
	public static ArrayList<DerpBasic> locationAdjustmentOrder_orderTypeList = new ArrayList<>();

	/**
	 * 状态 001-待审核 002-已审核
	 */
	public static final String LOCATIONADJUSTMENTORDER_STATUS_001 = "001";
	public static final String LOCATIONADJUSTMENTORDER_STATUS_002 = "002";
	public static ArrayList<DerpBasic> locationAdjustmentOrder_statusList = new ArrayList<>();

	/** 库位调整单 t_location_adjustment_order  end **/

	static {

		/** 物流委托书 */
		/** 类型 1-采购预申报*/
		logisticsAttorney_typeList.add(new DerpBasic(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_1, "采购预申报"));
		logisticsAttorney_typeList.add(new DerpBasic(DERP_ORDER.LOGISTICS_ATTORNEY_TYPE_2, "销售预申报"));

		/** 物流联系人模版 */
		/** 类型 1-发货人/提货信息 2-收货人/卸货信息 3-通知人 4-对接人 5-承运商信*/
		logisticsContactTemplate_TypeList.add(new DerpBasic(DERP_ORDER.LOGISTICS_CONTACT_TEMPLATE_TYPE_1, "发货人/提货信息") ) ;
		logisticsContactTemplate_TypeList.add(new DerpBasic(DERP_ORDER.LOGISTICS_CONTACT_TEMPLATE_TYPE_2, "收货人/卸货信息") ) ;
		logisticsContactTemplate_TypeList.add(new DerpBasic(DERP_ORDER.LOGISTICS_CONTACT_TEMPLATE_TYPE_3, "通知人") ) ;
		logisticsContactTemplate_TypeList.add(new DerpBasic(DERP_ORDER.LOGISTICS_CONTACT_TEMPLATE_TYPE_4, "对接人") ) ;
		logisticsContactTemplate_TypeList.add(new DerpBasic(DERP_ORDER.LOGISTICS_CONTACT_TEMPLATE_TYPE_5, "承运商") ) ;

		/** 应付核销记录明细 单据类型1-预付 2-NC */
		paymentBillVerificate_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_VERIFICATE_BILL_STATUS_1, "预付款单"));
		paymentBillVerificate_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_VERIFICATE_BILL_STATUS_2, "NC账单"));
		/*平台费用配置*/
		platFormConfigConfig_statusList.add(new DerpBasic(DERP_ORDER.PLATFORMCOSTCONFIG_STATUS_0,"未审核"));
		platFormConfigConfig_statusList.add(new DerpBasic(DERP_ORDER.PLATFORMCOSTCONFIG_STATUS_1,"已审核"));

		/*是否全子品牌 0-否 1-是*/
		platFormConfigConfig_IsAllBrandList.add(new DerpBasic(DERP_ORDER.PLATFORMCOSTCONFIG_ISAllBRAND_0,"否"));
		platFormConfigConfig_IsAllBrandList.add(new DerpBasic(DERP_ORDER.PLATFORMCOSTCONFIG_ISAllBRAND_1,"是"));

		/** 应付打印 */
		paymentBill_printingStateList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_PRINTING_STATE_0, "未打印"));
		paymentBill_printingStateList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_PRINTING_STATE_1, "已打印"));

		/*是否垫资*/
		paymentBill_endowmentStateList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_ENDOWMENT_STATE_0, "是"));
		paymentBill_endowmentStateList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_ENDOWMENT_STATE_1, "否"));

		/** 单据类型*/
		paymentBill_typeStateList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_TYPE_STATE_1, "采购订单"));
		paymentBill_typeStateList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_TYPE_STATE_2, "采购发票"));

		/** 应付费用明细 单据类型*/
		paymentCost_item_typeList.add(new DerpBasic(DERP_ORDER.PAYMENT_COST_ITEM_TYPE_0, "扣款"));
		paymentCost_item_typeList.add(new DerpBasic(DERP_ORDER.PAYMENT_COST_ITEM_TYPE_1, "补款"));


		/** 操作日志 模块1-采购 2-预付 3-应付 4-采购预申报 5-销售 6-销售预申报 7-部门额度配置 8-客户额度 9-项目额度  10-赊销单 11-应收 12-sd核销配置  13-爬虫商品对照表
		 *  14-平台费用配置  15-销售SD配置  16-销售退 17-销售退预申报 18.事业移库单 19-领料出库单 22-电商退货单 23-库位调整单
		 */
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_1,"采购"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_2,"预付"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_3,"应付"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_4,"采购预申报"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_5,"销售"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_6,"销售预申报"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_7,"部门额度配置"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_8,"客户额度"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_9,"项目额度"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_10,"赊销单"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_11,"应收"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_12,"sd核销配置"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_13,"爬虫商品对照表"))	 ;
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_14,"平台费用配置"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_15,"销售SD配置"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_16,"销售退"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_17,"销售退预申报"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_18,"事业移库单"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_19,"领料出库单"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_20,"应收关账"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_21,"采购入库单"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_22,"电商退货单"));
		operateLog_ModuleList.add(new DerpBasic(DERP_ORDER.OPERATE_LOG_MODULE_23,"库位调整单"));


		/** 应付款单 NC状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步*/
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_1, "待审核")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_2, "待入erp")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_3, "待入账")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_4, "已入账")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_5, "已关账")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_6, "待作废")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_7, "待红冲")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_8, "已作废")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_9, "已红冲")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_10, "未同步")) ;
		paymentBill_ncStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_NCSTATUS_11, "已同步")) ;

		/** 应付款单 审批方式 1-OA审批 2-经分销审批 */
		paymentBill_auditMethodList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_AUDIT_METHOD_1, "OA审批"));
		paymentBill_auditMethodList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_AUDIT_METHOD_2, "经分销审批"));

		/** 应付款单 账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-已付款、05-待作废、06-已作废 */
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_00, "待提交"));
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_01, "审核中"));
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_02, "已驳回"));
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_03, "待付款"));
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_07, "部分付款"));
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_04, "已付款"));
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_05, "待作废"));
		paymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.PAYMENT_BILL_BILLSTATUS_06, "已作废"));

		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_1, "待审核"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_2, "待入erp"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_3, "待入账"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_4, "已入账"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_5, "已关账"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_6, "待作废"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_7, "待红冲"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_8, "已作废"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_9, "已红冲"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_10, "未同步"));
		receiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_NCSYNSTATUS_11, "已同步"));


		/** 预付款单 审批方式 1-OA审批 2-经分销审批 */
		advancePaymentBill_auditMethodList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_AUDIT_METHOD_1, "OA审批"));
		advancePaymentBill_auditMethodList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_AUDIT_METHOD_2, "经分销审批"));

		/** 预付款单 账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-待作废、05-已作废、06-待核销、07已核销 */
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_00, "待提交"));
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_01, "审核中"));
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_02, "已驳回"));
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_03, "待付款"));
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_04, "待作废"));
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_05, "已作废"));
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_06, "待核销"));
		advancePaymentBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCE_PAYMENT_BILL_BILLSTATUS_07, "已核销"));

		/** 明细类型 1-采购 2-应收 */
		projectquotaWarningItem_receiveTypeList
				.add(new DerpBasic(DERP_ORDER.PROJECTQUOTAWARNINGITEM_receiveType_1, "应收"));
		projectquotaWarningItem_receiveTypeList
				.add(new DerpBasic(DERP_ORDER.PROJECTQUOTAWARNINGITEM_receiveType_2, "费用"));

		/** 明细类型 1-累计采购冻结金额 2-累计销售已回款金额 3.累计采购已付金额  */
		projectquotaWarningItem_TypeList.add(new DerpBasic(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_1, "累计采购冻结金额 "));
		projectquotaWarningItem_TypeList.add(new DerpBasic(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_2, "累计销售已回款金额"));
		projectquotaWarningItem_TypeList.add(new DerpBasic(DERP_ORDER.PROJECTQUOTAWARNINGITEM_type_3, "累计采购已付金额"));

		projectquotaconfig_quotaTypeList.add(new DerpBasic(DERP_ORDER.PROJECTQUOTACONFIG_quotaType_1, "品牌额度"));

		/**额度类型 1-品牌额度  2-客户额度*/
		quotaconfig_quotaTypeList.add(new DerpBasic(DERP_ORDER.QUOTACONFIG_quotaType_1, "品牌额度"));
		quotaconfig_quotaTypeList.add(new DerpBasic(DERP_ORDER.QUOTACONFIG_quotaType_2, "客户额度"));

		/** 状态 0-待审核 1-已审核 */
		projectquotaconfig_statusList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_0, "待审核"));
		projectquotaconfig_statusList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_1, "已审核"));

		/** 状态 0-待审核 1-已审核 */
		accountingReminderConfig_statusList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_0, "待审核"));
		accountingReminderConfig_statusList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_STATUS_1, "已审核"));

		/** 账期提醒配置 基准单位 1-工作日 2-自然日 */
		accountingReminderConfig_benchmarkUnitList
				.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_1, "工作日"));
		accountingReminderConfig_benchmarkUnitList
				.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERITEM_BENCHMARK_UNIT_2, "自然日"));

		/** 账期提醒配置 节点 1-付款 2-出账单 3-账单确认 4-开票 5-回款 */
		accountingReminderConfig_noteList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_1, "付款"));
		accountingReminderConfig_noteList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_2, "出账单"));
		accountingReminderConfig_noteList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_3, "账单确认"));
		accountingReminderConfig_noteList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_4, "开票 "));
		accountingReminderConfig_noteList.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERITEM_NOTE_5, "回款"));

		/** 账期提醒配置 提醒类型 1-发票日期 2-上架日期 */
		accountingReminderConfig_baseDateList
				.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_BASE_DATE_1, "发票日期"));
		accountingReminderConfig_baseDateList
				.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_BASE_DATE_2, "上架日期"));

		/** 账期提醒配置 提醒类型 1-收款 2-付款 */
		accountingReminderConfig_reminderTypeList
				.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_REMINDER_TYPE_1, "收款"));
		accountingReminderConfig_reminderTypeList
				.add(new DerpBasic(DERP_ORDER.ACCOUNTINGREMINDERCONFIG_REMINDER_TYPE_2, "付款"));

		/** 平台结算单 是否开票 */
		platformStatement_isInvoiceList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_0, "否"));
		platformStatement_isInvoiceList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_IS_INVOICE_1, "是"));

		platformStatement_isCreateReceiveList
				.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_0, "否"));
		platformStatement_isCreateReceiveList
				.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_1, "是"));
		platformStatement_isCreateReceiveList
				.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_IS_CREATE_RECEIVE_2, "生成中"));

		/** 平台结算单 客户类型 1-云集 2-唯品 3-天猫 */
		platformStatement_customerTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_1, "云集"));
		platformStatement_customerTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_2, "唯品"));
		platformStatement_customerTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_CUSTOMER_TYPE_3, "天猫"));

		platformStatement_typeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_TYPE_1, "to B"));
		platformStatement_typeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_TYPE_2, "to C"));

		/** 平台结算单表体 类型1-销售、2-客退、3-国检、4-盘亏、5-报废、6-盘盈、7-客退补贴、8-活动折扣、9-补偿折扣 */
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_1, "销售"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_2, "客退"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_3, "国检"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_4, "盘亏"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_5, "报废"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_6, "盘盈"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_7, "客退补贴"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_8, "活动折扣"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_9, "补偿折扣"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_10, "订单实付"));
		platformStatement_itemTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_STATEMENT_ITEM_TYPE_11, "费项配置"));

		/* 状态 */
		receive_payment_subject_typeList.add(new DerpBasic(DERP_ORDER.RECEIVE_PAYMENT_SUBJECT_TYPE_0, "已停用"));
		receive_payment_subject_typeList.add(new DerpBasic(DERP_ORDER.RECEIVE_PAYMENT_SUBJECT_TYPE_1, "已启用"));

		/* 结算类型 */
		settlement_typeList.add(new DerpBasic(DERP_ORDER.SETTLEMENT_TYPE_1, "应收"));
		settlement_typeList.add(new DerpBasic(DERP_ORDER.SETTLEMENT_TYPE_2, "预收"));
		/**订单来源  1:导入 2.接口数据*/
//		order_returnSourceList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_SOURCE_1,"导入"));
//		order_returnSourceList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_SOURCE_2,"接口数据"));

		/**单据状态： 011:待入仓 012:已入仓 028.入仓中 013-已退款*/
		order_returnStatusList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_STATUS_011,"待审核"));
		order_returnStatusList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_STATUS_028,"入仓中"));
		order_returnStatusList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_STATUS_012,"已入仓"));
		order_returnStatusList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_STATUS_013,"已退款"));

		/**售后类型：00-仅退款 01-退货退款 */
		order_returnAfterSaleTypeList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_AFTER_SALE_TYPE_00,"仅退款"));
		order_returnAfterSaleTypeList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_AFTER_SALE_TYPE_01,"退货退款"));

		/**售后单据类型 0-退款单 1-退货单 */
		orderReturn_returnOderTypeList.add(new DerpBasic(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_0,"退款单"));
		orderReturn_returnOderTypeList.add(new DerpBasic(DERP_ORDER.ORDERRETURN_RETURN_ORDER_TYPE_1,"退货单"));

		/* NC渠道编码枚举项 */
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_001, "天猫店"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_002, "POP店"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_003, "DSC微商城"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_004, "淘分销"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_005, "D2O"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_006, "线上"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_007, "线下"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_008, "第E仓"));
		channel_codeList.add(new DerpBasic(DERP_ORDER.CHANNEL_CODE_9999, "其他"));

		/* NC平台编码枚举项 */
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_001, "蜜芽平台"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_002, "天猫西选"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_003, "唯品会"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_004, "天猫海外直营"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_005, "京东"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_006, "小红书"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_007, "麦德龙"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_008, "第e仓"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_009, "云集平台"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_010, "大V店"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_011, "小小包"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_012, "大姨吗"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_013, "聚美优品"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_014, "网易考拉"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_015, "福利社"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_016, "亚马逊"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_017, "京东POP"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_018, "DSC微商城"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_019, "全球仓"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_020, "采销平台"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_021, "D2O"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_022, "贝店"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_023, "斑马"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_024, "拼多多"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_025, "淘分销"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_026, "有赞"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_027, "洋码头"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_998, "线下平台"));
		platform_codeList.add(new DerpBasic(DERP_ORDER.PLATFORM_CODE_999, "经销平台"));

		/** 上架单t_shelf------------------------------------- */
		/** 上架单状态 1-已上架 2-已入库 */
		shelf_statusList.add(new DerpBasic(DERP_ORDER.SHELF_STATUS_1, "已上架"));
		shelf_statusList.add(new DerpBasic(DERP_ORDER.SHELF_STATUS_2, "已入库"));

		/** 是否生成暂估 0-未生成 1-已生成 */
		shelf_isGenerateList.add(new DerpBasic(DERP_ORDER.SHELF_ISGENERATE_0, "未生成"));
		shelf_isGenerateList.add(new DerpBasic(DERP_ORDER.SHELF_ISGENERATE_1, "已生成"));

		/** 是否红冲单：0-否，1-是 */
		shelf_isWriteOffList.add(new DerpBasic(DERP_ORDER.SHELF_ISWRITEOFF_0, "否"));
		shelf_isWriteOffList.add(new DerpBasic(DERP_ORDER.SHELF_ISWRITEOFF_1, "是"));

		/** 爬虫账单类型: 00-销售明细、01-库存买断、02-库存盘亏、03-报废、04-库存盘盈、05-国检抽样 */
		crawler_billTypeList.add(new DerpBasic(DERP_ORDER.CRAWLER_BILLTYPE_00, "销售明细"));
		crawler_billTypeList.add(new DerpBasic(DERP_ORDER.CRAWLER_BILLTYPE_01, "库存买断"));
		crawler_billTypeList.add(new DerpBasic(DERP_ORDER.CRAWLER_BILLTYPE_02, "库存盘亏"));
		crawler_billTypeList.add(new DerpBasic(DERP_ORDER.CRAWLER_BILLTYPE_03, "报废"));
		crawler_billTypeList.add(new DerpBasic(DERP_ORDER.CRAWLER_BILLTYPE_04, "库存盘盈"));
		crawler_billTypeList.add(new DerpBasic(DERP_ORDER.CRAWLER_BILLTYPE_05, "国检抽样"));

		/** 电商订单t_order------------------------------------- */
		/** 订单状态 */
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_1, "待申报"));
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_2, "待放行"));
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_8, "待审核"));
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_3, "待发货"));
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_4, "已发货"));
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_5, "已取消"));
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_027, "出库中"));
		order_statusList.add(new DerpBasic(DERP_ORDER.ORDER_STATUS_006, "已删除"));

		/** 进口模式 */
		order_importModeList.add(new DerpBasic(DERP_ORDER.ORDER_IMPORTMODE_10, "BBC"));
		order_importModeList.add(new DerpBasic(DERP_ORDER.ORDER_IMPORTMODE_20, "BC"));
		order_importModeList.add(new DerpBasic(DERP_ORDER.ORDER_IMPORTMODE_30, "保留"));
		order_importModeList.add(new DerpBasic(DERP_ORDER.ORDER_IMPORTMODE_40, "CC"));

		/** 国检状态 */
		order_inspectStatusList.add(new DerpBasic(DERP_ORDER.ORDER_inspectstatus_11, "已报国检"));
		order_inspectStatusList.add(new DerpBasic(DERP_ORDER.ORDER_inspectstatus_12, "国检放行"));
		order_inspectStatusList.add(new DerpBasic(DERP_ORDER.ORDER_inspectstatus_13, "国检审核驳回"));
		order_inspectStatusList.add(new DerpBasic(DERP_ORDER.ORDER_inspectstatus_14, "国检抽检"));
		order_inspectStatusList.add(new DerpBasic(DERP_ORDER.ORDER_inspectstatus_15, "国检抽检未过"));

		/** 海关状态 */
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_21, "已报海关"));
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_22, "海关单证放行"));
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_23, "报海关失败"));
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_24, "海关查验/转人工/挂起等"));
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_25, "海关单证审核不通过"));
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_26, "海关已接受申报,待货物运抵后处理"));
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_41, "海关货物查扣"));
		order_customsStatusList.add(new DerpBasic(DERP_ORDER.ORDER_CUSTOMSSTATUS_42, "海关货物放行"));

		/**是否生成暂估费用 0-未生成 1-已生成*/
		order_isGenerateList.add(new DerpBasic(DERP_ORDER.ORDER_IS_GENERATE_0, "未生成"));
		order_isGenerateList.add(new DerpBasic(DERP_ORDER.ORDER_IS_GENERATE_1, "已生成"));

		/** 订单来源 1:导入 2.接口数据 */
		order_returnSourceList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_SOURCE_1, "导入"));
		order_returnSourceList.add(new DerpBasic(DERP_ORDER.ORDER_RETURN_SOURCE_2, "接口数据"));

		/** 宝洁商品货期配置t_goods_time_config------------------------------------- */
		/** 货期（月）: 0-未维护 2-2 4-4 */
		goodsTimeConfig_deliveryPeriodList.add(new DerpBasic(DERP_ORDER.GOODSTIMECONFIG_DELIVERYPERIOD_0, "未维护"));
		goodsTimeConfig_deliveryPeriodList.add(new DerpBasic(DERP_ORDER.GOODSTIMECONFIG_DELIVERYPERIOD_2, "2"));
		goodsTimeConfig_deliveryPeriodList.add(new DerpBasic(DERP_ORDER.GOODSTIMECONFIG_DELIVERYPERIOD_4, "4"));

		/** 模型配置t_model_setting------------------------------------- */
		/** 日销统计类型: 0-平常日 1-平常日+促销日 */
		modelSetting_dailyStatisticalTypeList.add(new DerpBasic(DERP_ORDER.MODELSETTING_DAILYSTATISTICALTYPE_0, "平常日"));
		modelSetting_dailyStatisticalTypeList
				.add(new DerpBasic(DERP_ORDER.MODELSETTING_DAILYSTATISTICALTYPE_1, "平常日+促销日"));

		/** 日销统计范围: 0-1个月 1-3个月 2-6个月 3-年初至今 */
		modelSetting_dailyStatisticalRangeList
				.add(new DerpBasic(DERP_ORDER.MODELSETTING_DAILYSTATISTICALRANGE_0, "1个月"));
		modelSetting_dailyStatisticalRangeList
				.add(new DerpBasic(DERP_ORDER.MODELSETTING_DAILYSTATISTICALRANGE_1, "3个月"));
		modelSetting_dailyStatisticalRangeList
				.add(new DerpBasic(DERP_ORDER.MODELSETTING_DAILYSTATISTICALRANGE_2, "6个月"));
		modelSetting_dailyStatisticalRangeList
				.add(new DerpBasic(DERP_ORDER.MODELSETTING_DAILYSTATISTICALRANGE_3, "年初至今"));

		/** 采购计划表t_purchase_planning------------------------------------- */
		/** 下单状态：01-已下单 00-未下单 */
		purchasePlanning_orderStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEPLANNING_ORDERSTATUS_01, "已下单"));
		purchasePlanning_orderStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEPLANNING_ORDERSTATUS_00, "未下单"));

		/** 同步状态：01-已同步 00-未同步 */
		purchasePlanning_syncStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEPLANNING_SYNCSTATUS_01, "已同步"));
		purchasePlanning_syncStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEPLANNING_SYNCSTATUS_00, "未同步"));

		/** 虚拟在途表t_virtual_in_transit------------------------------------- */
		/** 处理状态: 01-已处理 00-未处理 */
		virtualInTransit_statusList.add(new DerpBasic(DERP_ORDER.VIRTUALINTRANSIT_STATUS_01, "已处理"));
		virtualInTransit_statusList.add(new DerpBasic(DERP_ORDER.VIRTUALINTRANSIT_STATUS_00, "未处理"));

		/** 爬虫账单t_crawler_bill------------------------------------- */
		/** 是否使用: 0-未使用 1-已使用 */
		crawlerBill_isUsedList.add(new DerpBasic(DERP_ORDER.CRAWLERBILL_ISUSED_0, "未使用"));
		crawlerBill_isUsedList.add(new DerpBasic(DERP_ORDER.CRAWLERBILL_ISUSED_1, "已使用"));

		/** 生成爬虫出库单临时分配表t_crawler_out_temp------------------------------------- */
		/** 数据类型 1-库存量 2-已有销售单账单数量分配 3-新增销售单账单数量分配 4-唯品红冲 5-库存盘亏 6-库存盘盈 7-唯品报废 8-国检抽样 */
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_1, "库存量"));
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_2, "已有销售单账单数量分配"));
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_3, "新增销售单账单数量分配"));
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_4, "唯品红冲"));
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_5, "库存盘亏"));
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_6, "库存盘盈"));
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_7, "唯品报废"));
		crawlerOutTemp_dataTypeList.add(new DerpBasic(DERP_ORDER.CRAWLEROUTTEMP_DATATYPE_8, "国检抽样"));

		/** 库存调整明细表t_inventory_adjustment_detail------------------------------------- */
		/** 业务类别: 4-唯品红冲 5-国检抽样 6-唯品报废 7-库存盘亏 8-库存盘盈 */
		inventoryAdjustmentDetail_modelList.add(new DerpBasic(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_MODEL_4, "唯品红冲"));
		inventoryAdjustmentDetail_modelList.add(new DerpBasic(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_MODEL_5, "国检抽样"));
		inventoryAdjustmentDetail_modelList.add(new DerpBasic(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_MODEL_6, "唯品报废"));
		inventoryAdjustmentDetail_modelList.add(new DerpBasic(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_MODEL_7, "库存盘亏"));
		inventoryAdjustmentDetail_modelList.add(new DerpBasic(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_MODEL_8, "库存盘盈"));

		/** 库存/减库存类型：0-减库存 1-加库存 */
		inventoryAdjustmentDetail_typeList.add(new DerpBasic(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_0, "减库存"));
		inventoryAdjustmentDetail_typeList.add(new DerpBasic(DERP_ORDER.INVENTORYADJUSTMENTDETAIL_TYPE_1, "加库存"));

		/** 爬虫商品对照表t_merchandise_contrast------------------------------------- */
		/** 状态: 0-启用 1-禁用 */
		merchandiseContrast_statusList.add(new DerpBasic(DERP_ORDER.MERCHANDISECONTRAST_STATUS_0, "启用"));
		merchandiseContrast_statusList.add(new DerpBasic(DERP_ORDER.MERCHANDISECONTRAST_STATUS_1, "禁用"));

		merchandiseContrast_typeList.add(new DerpBasic(DERP_ORDER.MERCHANDISECONTRAST_TYPE_1, "云集"));
		merchandiseContrast_typeList.add(new DerpBasic(DERP_ORDER.MERCHANDISECONTRAST_TYPE_2, "唯品"));

		/** 外部单号来源表t_order_external_code------------------------------------- */
		/** 订单来源: 1-电商订单, 2-装载交运 3-销售出库 */
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_1, "电商订单"));
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_2, "装载交运"));
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_3, "销售出库"));
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_4, "调拨入库"));
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5, "调拨出库"));
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_6, "采购退货"));
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_7, "众邦云仓采购入库"));
		orderExternalCode_orderSourceList.add(new DerpBasic(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_8, "领料出库"));

		/** 采购入库分析表t_purchase_analysis------------------------------------- */
		/** 是否完结入库: 1-是 0-否 */
		purchaseAnalysis_isEndList.add(new DerpBasic(DERP_ORDER.PURCHASEANALYSIS_ISEND_1, "是"));
		purchaseAnalysis_isEndList.add(new DerpBasic(DERP_ORDER.PURCHASEANALYSIS_ISEND_0, "否"));

		/** 入库是否组合品: 1-是 0-否 */
		purchaseAnalysis_isGroupList.add(new DerpBasic(DERP_ORDER.PURCHASEANALYSIS_ISGROUP_1, "是"));
		purchaseAnalysis_isGroupList.add(new DerpBasic(DERP_ORDER.PURCHASEANALYSIS_ISGROUP_0, "否"));

		/** 采购订单t_purchase_order start------------------------------------- */
		/** 合作模式 0：购销买断，1：入仓代销，2：囤货销售，3：一件代发，4：购销买断+囤货销售，5：其他 */
		purchaseOrder_cooperationModeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_COOPERATIONMODE_0, "购销买断"));
		purchaseOrder_cooperationModeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_COOPERATIONMODE_1, "入仓代销"));
		purchaseOrder_cooperationModeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_COOPERATIONMODE_2, "囤货销售"));
		purchaseOrder_cooperationModeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_COOPERATIONMODE_3, "一件代发"));
		purchaseOrder_cooperationModeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_COOPERATIONMODE_4, "购销买断+囤货销售"));
		purchaseOrder_cooperationModeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_COOPERATIONMODE_5, "其他"));

		/** 审批方式 1-OA审批 2-经分销审批*/
		purchaseOrder_auditMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_AUDITMETHOD_1, "OA审批"));
		purchaseOrder_auditMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_AUDITMETHOD_2, "经分销审批"));

		/** 采购方式 0：以销定采；1：备货（已立项）；2：备货（集团自主）；3：试单 */
		purchaseOrder_purchaseMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PURCHASEMETHOD_0, "以销定采"));
		purchaseOrder_purchaseMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PURCHASEMETHOD_1, "备货（已立项）"));
		purchaseOrder_purchaseMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PURCHASEMETHOD_2, "备货（集团自主）"));
		purchaseOrder_purchaseMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PURCHASEMETHOD_3, "试单"));

		/** 付款主体 1-卓普信 2-商家 3-卓烨 */
		purchaseOrder_paySubjectList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PAYSUBJECT_1, "卓普信"));
		purchaseOrder_paySubjectList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PAYSUBJECT_2, "商家"));
		purchaseOrder_paySubjectList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PAYSUBJECT_3, "卓烨"));

		/** 结算方式 0：先货后款，1：先款后货，2：实销实结 */
		purchaseOrder_settlementMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SETTLEMENTMETHOD_0, "先货后款"));
		purchaseOrder_settlementMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SETTLEMENTMETHOD_1, "先款后货"));
		purchaseOrder_settlementMethodList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SETTLEMENTMETHOD_2, "实销实结"));

		/** 结算方式 0：全款预付，1：分期付款（预付30%-50%，理货后付余款），2：账期结算（收货后N天结算） 3：实销实结 */
		purchaseOrder_settlementProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_0, "全款预付"));
		purchaseOrder_settlementProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_1, "分期付款（预付30%-50%，理货后付余款）"));
		purchaseOrder_settlementProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_2, "账期结算（收货后N天结算）"));
		purchaseOrder_settlementProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SETTLEMENTPROVISION_3, "实销实结"));

		/** 付款条款 1-先款后货—全款预付 2-先款后货—分期付款（预付30%-50%，理货后付余款） 3-先货后款—账期结算（收货后N天结算） 实销实结 */
		purchaseOrder_paymentProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_1, "先款后货—全款预付"));
		purchaseOrder_paymentProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_2, "先款后货—分期付款（预付30%-50%，理货后付余款）"));
		purchaseOrder_paymentProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_3, "先货后款—账期结算（收货后N天结算）"));
		purchaseOrder_paymentProvisionList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_PAYMENTPROVISION_4, "实销实结"));

		/** 收货方式 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW 6-CIP 7-FCA 8-其他*/
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_1, "CIF"));
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_2, "CFR"));
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_3, "FOB"));
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_4, "DAP"));
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_5, "EXW"));
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_6, "CIP"));
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_7, "FCA"));
		purchaseOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_TRADETERMS_8, "其他"));

		/** 业务模式 1-购销 2-代销 3-采销执行 */
		purchaseOrder_businessModelList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_1, "购销"));
		purchaseOrder_businessModelList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_2, "代销"));
		purchaseOrder_businessModelList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_BUSINESSMODEL_3, "采销执行"));

		/** 状态 001-待提交 002-已提交 003-已审核 005-部分入库 007-已入库  028-入库中 006-已删除 */
		purchaseOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_STATUS_001, "待提交"));
		purchaseOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_STATUS_002, "已提交"));
		purchaseOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_STATUS_003, "已审核"));
		purchaseOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_STATUS_005, "部分入库"));
		purchaseOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_STATUS_007, "已入库"));
//		purchaseOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_STATUS_006, "已删除"));

		/** 是否已生成预申报单 1-是 0-否 */
		purchaseOrder_isGenerateList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_ISGENERATE_1, "是"));
		purchaseOrder_isGenerateList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_ISGENERATE_0, "否"));

		/** 金额调整 1-已调整 0-未调整 */
		purchaseOrder_amountAdjustmentStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_1, "已调整"));
		purchaseOrder_amountAdjustmentStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_AMOUNT_ADJUSTMENT_STATUS_0, "未调整"));

		/** 金额确认 2-已确认 1-确认不通过 0-待确认 */
		purchaseOrder_amountConfirmStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_0, "待确认"));
		purchaseOrder_amountConfirmStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_1, "确认不通过"));
		purchaseOrder_amountConfirmStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_AMOUNT_CONFIRM_STATUS_2, "已确认"));

		/** 是否完结 1- 0-否 */
		purchaseOrder_isEndList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_ISEND_0, "否"));
		purchaseOrder_isEndList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_ISEND_1, "是"));

		/** 发送邮件状态 0-未发送邮件 1-发送邮件1次 2-发送邮件2次 */
		purchaseOrder_mailStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_MAILSTATUS_0, "未发送邮件"));
		purchaseOrder_mailStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_MAILSTATUS_1, "发送邮件1次"));
		purchaseOrder_mailStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_MAILSTATUS_2, "发送邮件2次"));

		/** 单据状态 1-收到发票 2-已付款 3-收到发票已付款 */
		purchaseOrder_billStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_BILLSTATUS_1, "收到发票"));
		purchaseOrder_billStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_BILLSTATUS_2, "已付款"));
		purchaseOrder_billStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_BILLSTATUS_3, "收到发票已付款"));

		/**红冲状态: 001-待红冲 002-红冲中 003-已红冲 */
		purchaseOrder_writeOffStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_001, "待红冲"));
		purchaseOrder_writeOffStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_002, "红冲中"));
		purchaseOrder_writeOffStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_WRITEOFFSTATUS_003, "已红冲"));

		/**用印顺序 0：我方先盖章；1：对方先盖章；2：无需盖章 */
		purchaseOrder_sealOrderList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SEALORDER_0, "我方先盖章"));
		purchaseOrder_sealOrderList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SEALORDER_1, "对方先盖章"));
		purchaseOrder_sealOrderList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SEALORDER_2, "无需盖章"));

		/**用印类型 0：传统物理盖章；1：线上电子签章 */
		purchaseOrder_sealTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SEALTYPE_0, "传统物理盖章"));
		purchaseOrder_sealTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEORDER_SEALTYPE_1, "线上电子签章"));

		/**效期区间 1:0≤X<1/10 ; 2: 1/10≤X<1/5 ; 3: 1/5≤X<1/3 ; 4: 1/3≤X<1/2 ; 5: 1/2≤X<2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品*/
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1,"0<X≤1/10"));
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2,"1/10<X≤1/5"));
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3,"1/5<X≤1/3"));
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4,"1/3<X≤1/2"));
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_5,"1/2<X≤2/3"));
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_6,"2/3以上"));
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7,"过期品"));
		purchaseOrder_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_8,"残次品"));

		/** 采购订单t_purchase_order end------------------------------------- */

		/** 采购预申报单t_declare_order------------------------------------- */
		/** 运输方式 1 空运 2 海运 */
		declareOrder_transportList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_TRANSPORT_1, "空运"));
		declareOrder_transportList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_TRANSPORT_2, "海运"));

		/** 业务模式 1-采购 2-代销 */
		declareOrder_businessModelList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_BUSINESSMODEL_1, "购销"));
		declareOrder_businessModelList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_BUSINESSMODEL_2, "代销"));

		/** 订单状态订单状态 001待物流委托 002待清关 003已上架 004 待入仓 006已删除 */
		declareOrder_statusList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_STATUS_001, "待物流委托"));
		declareOrder_statusList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_STATUS_002, "待清关"));
		declareOrder_statusList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_STATUS_004, "待入仓"));
		declareOrder_statusList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_STATUS_003, "已上架"));
		//declareOrder_statusList.add(new DerpBasic(DERP.DEL_CODE_006, "已删除"));

		/** 接口状态 0-未推接口；1-已推接口；2-接口推送失败 */
		declareOrder_stateList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_STATE_1, "已推接口"));
		declareOrder_stateList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_STATE_0, "未推接口"));
		declareOrder_stateList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_STATE_2, "接口推送失败"));

		/** 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款*/
		declareorder_paymentProvisionList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_PAYMENTPROVISION_1, "一次付款-付款发货"));
		declareorder_paymentProvisionList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_PAYMENTPROVISION_2, "多次付款"));
		declareorder_paymentProvisionList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_PAYMENTPROVISION_3, "一次付款-发货付款"));

		/** 贸易条款 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW*/
		declareorder_tradeTermsList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_TRADETERMS_1, "CIF"));
		declareorder_tradeTermsList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_TRADETERMS_2, "CFR"));
		declareorder_tradeTermsList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_TRADETERMS_3, "FOB"));
		declareorder_tradeTermsList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_TRADETERMS_4, "DAP"));
		declareorder_tradeTermsList.add(new DerpBasic(DERP_ORDER.DECLAREORDER_TRADETERMS_5, "EXW"));

		/** 采购入库单t_purchase_warehouse------------------------------------- */
		/** 单据状态: 011-待入仓 012-已入仓 028-入库中 */
		purchaseWarehouse_stateList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_STATE_011, "待入仓"));
		purchaseWarehouse_stateList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028, "入库中"));
		purchaseWarehouse_stateList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012, "已入仓"));

		/** 采购勾稽状态: 1-是 0-否 */
		purchaseWarehouse_crossStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_CROSSSTATUS_1, "是"));
		purchaseWarehouse_crossStatusList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_CROSSSTATUS_0, "否"));

		/** 采购入库勾稽状态: 1-未勾稽 2-已勾稽 */
		purchaseWarehouse_correlationStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_1, "未勾稽"));
		purchaseWarehouse_correlationStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_2, "已勾稽"));
		purchaseWarehouse_correlationStatusList
				.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_3, "勾稽失败"));

		/** 业务模式: 1-采购 2-代销 3-采购执行 */
		purchaseWarehouse_businessModelList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_BUSINESSMODEL_1, "采购"));
		purchaseWarehouse_businessModelList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_BUSINESSMODEL_2, "代销"));
		purchaseWarehouse_businessModelList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_BUSINESSMODEL_3, "采销执行"));

		/** 是否红冲单：0-否，1-是 */
		purchaseWarehouse_isWriteOffList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_ISWRITEOFF_0, "否"));
		purchaseWarehouse_isWriteOffList.add(new DerpBasic(DERP_ORDER.PURCHASEWAREHOUSE_ISWRITEOFF_1, "是"));

		/** 采购框架合同表t_purchase_frame_contract start------------------------------------- */
		/** 合同模板 0：我司标板；1：非我司标板；2：我司修改版；3：我司参考版；4：我司草拟版； */
		purchaseFrameContract_contractVersionList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTVERSION_0, "我司标板"));
		purchaseFrameContract_contractVersionList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTVERSION_1, "非我司标板"));
		purchaseFrameContract_contractVersionList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTVERSION_2, "我司修改版"));
		purchaseFrameContract_contractVersionList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTVERSION_3, "我司参考版"));
		purchaseFrameContract_contractVersionList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTVERSION_4, "我司草拟版"));

		/** 合同类型 0：新签，1：续签 2：补充 3：终止 */
		purchaseFrameContract_contractTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTTYPE_0, "新签"));
		purchaseFrameContract_contractTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTTYPE_1, "续签"));
		purchaseFrameContract_contractTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTTYPE_2, "补充"));
		purchaseFrameContract_contractTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTTYPE_3, "终止"));

		/** 供应商类型 0：品牌商，1：一级授权商，2：其他 */
		purchaseFrameContract_supplierTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SUPPLIERTYPE_0, "品牌商"));
		purchaseFrameContract_supplierTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SUPPLIERTYPE_1, "一级授权商"));
		purchaseFrameContract_supplierTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SUPPLIERTYPE_2, "其他"));

		/** 采购类型 0：进口，1：出口，2：内贸 */
		purchaseFrameContract_purchaseTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_PURCHASETYPE_0, "进口"));
		purchaseFrameContract_purchaseTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_PURCHASETYPE_1, "出口"));
		purchaseFrameContract_purchaseTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_PURCHASETYPE_2, "内贸"));

		/** 资金来源 0：自有资金，1：卓普信资金 */
		purchaseFrameContract_capitalTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CAPITALTYPE_0, "自有资金"));
		purchaseFrameContract_capitalTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CAPITALTYPE_1, "卓普信资金"));

		/** 商品类型 0：母婴类，1：美妆个护，2：保健品，3：日化家清，4：普通食品，5：数码家电，6：宠物食品，7：其他 */
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_0, "母婴类"));
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_1, "美妆个护"));
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_2, "保健品"));
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_3, "日化家清"));
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_4, "普通食品"));
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_5, "数码家电"));
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_6, "宠物食品"));
		purchaseFrameContract_comtyTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYTYPE_7, "其他"));

		/** 商品来源 0：品牌方，1：工厂采购，2：一级经销商，3：海外超市，4：其他 */
		purchaseFrameContract_comtySourceList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYSOURCE_0, "品牌方"));
		purchaseFrameContract_comtySourceList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYSOURCE_1, "工厂采购"));
		purchaseFrameContract_comtySourceList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYSOURCE_2, "一级经销商"));
		purchaseFrameContract_comtySourceList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYSOURCE_3, "海外超市"));
		purchaseFrameContract_comtySourceList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_COMTYSOURCE_4, "其他"));

		/** 合同状态 0：生效，1：补充生效，2：期满终止，3：提前终止 */
		purchaseFrameContract_contractStateList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTSTATE_0, "生效"));
		purchaseFrameContract_contractStateList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTSTATE_1, "补充生效"));
		purchaseFrameContract_contractStateList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTSTATE_2, "期满终止"));
		purchaseFrameContract_contractStateList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_CONTRACTSTATE_3, "提前终止"));

		/** 是否已取得授权 0：是，1：否 */
		purchaseFrameContract_empJudgeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_EMPJUDGE_0, "是"));
		purchaseFrameContract_empJudgeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_EMPJUDGE_1, "否"));

		/** 交货方式 0：FOB，1：CIF，2：CIP，3：FCA，4：EXW，5：其他  6：DAP，7：CFR*/
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_0, "FOB"));
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_1, "CIF"));
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_2, "CIP"));
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_3, "FCA"));
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_4, "EXW"));
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_5, "其他"));
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_6, "DAP"));
		purchaseFrameContract_deliveryTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_DELIVERTYPE_7, "CFR"));

		/** 结算方式 0：先货后款，1：先款后货，2：实销实结 */
		purchaseFrameContract_settleMentList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SETTLEMENT_0, "先货后款"));
		purchaseFrameContract_settleMentList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SETTLEMENT_1, "先款后货"));
		purchaseFrameContract_settleMentList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SETTLEMENT_2, "实销实结"));

		/** 预付款 0：有，1：无 */
		purchaseFrameContract_advanceChargeJudgeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_ACJUDGE_0, "有"));
		purchaseFrameContract_advanceChargeJudgeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_ACJUDGE_1, "无"));

		/** 业务对应的财务经理 0：龚小香；1：董欢 */
		purchaseFrameContract_financeManagerList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_FIN_MANAGER_0, "龚小香"));
		purchaseFrameContract_financeManagerList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_FIN_MANAGER_1, "董欢"));

		/** 用印顺序 0：我方先盖章；1：对方先盖章；2：无需盖章 */
		purchaseFrameContract_sealOrderList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SEALORDER_0, "我方先盖章"));
		purchaseFrameContract_sealOrderList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SEALORDER_1, "对方先盖章"));
		purchaseFrameContract_sealOrderList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SEALORDER_2, "无需盖章"));


		/** 用印类型 0：传统物理盖章；1：线上电子签章 */
		purchaseFrameContract_sealTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SEALTYPE_0, "传统物理盖章"));
		purchaseFrameContract_sealTypeList.add(new DerpBasic(DERP_ORDER.PURCHASEFRAMECONTRACT_SEALTYPE_1, "线上电子签章"));
		/** 采购框架合同表t_purchase_frame_contract end------------------------------------- */


		/** 采购试单申请表t_purchase_try_apply_order start------------------------------------- */
		/** 审批状态 0：审批驳回；1：审批中；2：审批通过 */
		purchaseTryOrder_appStateList.add(new DerpBasic(DERP_ORDER.PURCHASETRYORDER_APPSTATE_0, "审批驳回"));
		purchaseTryOrder_appStateList.add(new DerpBasic(DERP_ORDER.PURCHASETRYORDER_APPSTATE_1, "审批中"));
		purchaseTryOrder_appStateList.add(new DerpBasic(DERP_ORDER.PURCHASETRYORDER_APPSTATE_2, "审批通过"));

		/** 业务类型 0：跨境进口；1：跨境出口；2：一般贸易；3：内贸 */
		purchaseTryOrder_bussinessModeList.add(new DerpBasic(DERP_ORDER.PURCHASETRYORDER_BUSSINESSMODE_0, "跨境进口"));
		purchaseTryOrder_bussinessModeList.add(new DerpBasic(DERP_ORDER.PURCHASETRYORDER_BUSSINESSMODE_1, "跨境出口"));
		purchaseTryOrder_bussinessModeList.add(new DerpBasic(DERP_ORDER.PURCHASETRYORDER_BUSSINESSMODE_2, "一般贸易"));
		purchaseTryOrder_bussinessModeList.add(new DerpBasic(DERP_ORDER.PURCHASETRYORDER_BUSSINESSMODE_3, "内贸"));
		/** 采购试单申请表tt_purchase_try_apply_order end------------------------------------- */



		/** 销售出库分析表t_sale_analysis------------------------------------- */
		/** 是否完结入库 1-已完结 0-未完结 */
		saleAnalysis_isEndList.add(new DerpBasic(DERP_ORDER.SALEANALYSIS_ISEND_1, "已完结"));
		saleAnalysis_isEndList.add(new DerpBasic(DERP_ORDER.SALEANALYSIS_ISEND_0, "未完结"));

		/** 销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结------------------------------------- */
		saleAnalysis_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEANALYSIS_SALETYPE_1, "购销-整批结算"));
		saleAnalysis_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEANALYSIS_SALETYPE_4, "购销-实销实结"));
		saleAnalysis_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEANALYSIS_SALETYPE_3, "采销执行"));
		saleAnalysis_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEANALYSIS_SALETYPE_2, "代销"));

		/** 销售订单t_sale_order------------------------------------- */
		/** 服务类型: E0-区内调拨调出服务 F0-区内调拨调入服务 G0-库内调拨服务 */
		saleOrder_serveTypesList.add(new DerpBasic(DERP_ORDER.SALEORDER_SERVETYPES_E0, "区内调拨调出服务"));
		saleOrder_serveTypesList.add(new DerpBasic(DERP_ORDER.SALEORDER_SERVETYPES_F0, "区内调拨调入服务"));
		saleOrder_serveTypesList.add(new DerpBasic(DERP_ORDER.SALEORDER_SERVETYPES_G0, "库内调拨服务"));

		/**
		 * 订单状态: 001-待审核 002-审核中 003-已审核 006-已删除 007-已完结 018-已出库 027-出库中 025-已签收 026-已上架
		 * 031-部分上架 008-待提交 017-待出库 019-部分出库
		 */
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_008, "待提交"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_001, "待审核"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_003, "已审核"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_017, "待出库"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_027, "出库中"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_019, "部分出库"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_018, "已出库"));
//		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_025, "已签收"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_031, "部分上架"));
		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_026, "已上架"));
//		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_007, "已完结"));
//		saleOrder_stateList.add(new DerpBasic(DERP_ORDER.SALEORDER_STATE_002,"审核中"));
		saleOrder_stateList.add(new DerpBasic(DERP.DEL_CODE_006, "已删除"));

		/** 订单来源 1-人工录入 2-系统自动生成 */
		saleOrder_orderSourceList.add(new DerpBasic(DERP_ORDER.SALEORDER_ORDERSOURCE_1, "人工录入"));
		saleOrder_orderSourceList.add(new DerpBasic(DERP_ORDER.SALEORDER_ORDERSOURCE_2, "系统自动生成"));

		/** 业务模式 1-购销-整批结算 2-代销 3-采购执行 4.购销-实销实结 */
		saleOrder_businessModelList.add(new DerpBasic(DERP_ORDER.SALEORDER_BUSINESSMODEL_1, "购销-整批结算"));
		saleOrder_businessModelList.add(new DerpBasic(DERP_ORDER.SALEORDER_BUSINESSMODEL_4, "购销-实销实结"));
		saleOrder_businessModelList.add(new DerpBasic(DERP_ORDER.SALEORDER_BUSINESSMODEL_3, "采销执行"));
		saleOrder_businessModelList.add(new DerpBasic(DERP_ORDER.SALEORDER_BUSINESSMODEL_2, "代销"));

		/** 邮寄方式 1-寄售 2-自提 */
		saleOrder_mailModeList.add(new DerpBasic(DERP_ORDER.SALEORDER_MAILMODE_1, "寄售"));
		saleOrder_mailModeList.add(new DerpBasic(DERP_ORDER.SALEORDER_MAILMODE_2, "自提"));

		/** 付款主体: 1-卓普信 2-商家 3-卓烨 */
		saleOrder_paySubjectModeList.add(new DerpBasic(DERP_ORDER.SALEORDER_PAYSUBJECT_1, "卓普信"));
		saleOrder_paySubjectModeList.add(new DerpBasic(DERP_ORDER.SALEORDER_PAYSUBJECT_2, "商家"));
		saleOrder_paySubjectModeList.add(new DerpBasic(DERP_ORDER.SALEORDER_PAYSUBJECT_3, "卓烨"));

		/** 目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
		saleOrder_destinationList.add(new DerpBasic(DERP_ORDER.SALEORDER_DESTINATION_HP01, "黄埔状元谷"));
		saleOrder_destinationList.add(new DerpBasic(DERP_ORDER.SALEORDER_DESTINATION_HK01, "香港本地"));
		saleOrder_destinationList.add(new DerpBasic(DERP_ORDER.SALEORDER_DESTINATION_GZJC01, "广州机场"));

		/** 单据标识 1 预售转销 2 非预售转销 */
		saleOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.SALEORDER_ORDERTYPE_1, "预售转销"));
		saleOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.SALEORDER_ORDERTYPE_2, "非预售转销"));

		/** 融资申请状态 0-未申请 1-已申请 2-已赎回 */
		saleOrder_financeStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_FINANCE_STATUS_0, "未申请"));
		saleOrder_financeStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_FINANCE_STATUS_1, "已申请"));
		saleOrder_financeStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_FINANCE_STATUS_2, "已赎回"));

		/** 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款 */
		saleOrder_paymentTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_PAYMENTTERMS_1, "一次付款-付款发货"));
		saleOrder_paymentTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_PAYMENTTERMS_2, "多次付款 "));
		saleOrder_paymentTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_PAYMENTTERMS_3, "一次付款-发货付款"));

		/** 贸易条款 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW */
		saleOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_TRADETERMS_1, "CIF"));
		saleOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_TRADETERMS_2, "CFR"));
		saleOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_TRADETERMS_3, "FOB"));
		saleOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_TRADETERMS_4, "DAP"));
		saleOrder_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEORDER_TRADETERMS_5, "EXW"));

		/** 红冲状态 001-待红冲 002-红冲中 003-已红冲 */
		saleOrder_writeOffStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_001, "待红冲"));
		saleOrder_writeOffStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_002, "红冲中"));
		saleOrder_writeOffStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_WRITEOFF_STATUS_003, "已红冲"));

		/** 预售单t_pre_sale_order------------------------------------- */
		/** 订单状态: 001-待审核 003-已审核 007-已完结 */
		preSaleOrder_stateList.add(new DerpBasic(DERP_ORDER.PRESALEORDER_STATE_001, "待审核"));
		preSaleOrder_stateList.add(new DerpBasic(DERP_ORDER.PRESALEORDER_STATE_003, "已审核"));
		preSaleOrder_stateList.add(new DerpBasic(DERP_ORDER.PRESALEORDER_STATE_007, "已完结"));

		/** 业务模式 1-购销-整批结算 4.购销-实销实结 */
		preSaleOrder_businessModelList.add(new DerpBasic(DERP_ORDER.PRESALEORDER_BUSINESSMODEL_1, "购销-整批结算"));
		preSaleOrder_businessModelList.add(new DerpBasic(DERP_ORDER.PRESALEORDER_BUSINESSMODEL_4, "购销-实销实结"));

		/** 销售出库t_sale_out_depot------------------------------------- */
		/** 销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结------------------------------------- */
		saleOutDepot_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_SALETYPE_1, "购销-整批结算"));
		saleOutDepot_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_SALETYPE_4, "购销-实销实结"));
		saleOutDepot_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_SALETYPE_3, "采销执行"));
		saleOutDepot_saleTypeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_SALETYPE_2, "代销"));

		/** 状态: 017-待出库 018-已出库 027-出库中 019-待出库 026-已上架 031-部分上架*/
		saleOutDepot_statusList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_STATUS_017, "待出库"));
		saleOutDepot_statusList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_STATUS_027, "出库中"));
		saleOutDepot_statusList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_STATUS_018, "已出库"));
		saleOutDepot_statusList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_STATUS_031, "部分上架"));
		saleOutDepot_statusList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_STATUS_026, "已上架"));

		/** 订单来源 1:导入 2.接口回传 */
		saleOutDepot_sourceList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_SOURCE_1, "导入"));
		saleOutDepot_sourceList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_SOURCE_2, "接口回传"));

		/** 进口模式: 10-BBC 20-BC 30-保留 40-CC */
		saleOutDepot_importModeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_IMPORTMODE_10, "BBC"));
		saleOutDepot_importModeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_IMPORTMODE_20, "BC"));
		saleOutDepot_importModeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_IMPORTMODE_30, "保留"));
		saleOutDepot_importModeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_IMPORTMODE_40, "CC"));

		/** 退运状态: 70-成功 90-作废 */
		saleOutDepot_returnStatusList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_RETURNSTATUS_70, "成功"));
		saleOutDepot_returnStatusList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_RETURNSTATUS_90, "作废"));

		/** 是否红冲单：0-否，1-是 */
		saleOutDepot_isWriteOffList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_0, "否"));
		saleOutDepot_isWriteOffList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOT_ISWRITEOFF_1, "是"));

		/** 销售出库表体t_sale_out_depot_item------------------------------------- */
		/** 账单类型: 00-销售明细 01-库存买断 */
		saleOutDepotItem_billTypeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOTITEM_BILLTYPE_00, "销售明细"));
		saleOutDepotItem_billTypeList.add(new DerpBasic(DERP_ORDER.SALEOUTDEPOTITEM_BILLTYPE_01, "库存买断"));

		/** 销售单_po关联表t_sale_po_rel------------------------------------- */
		/** 类型: 0-销售订单 1-销售退订单 */
		salePoRel_stateList.add(new DerpBasic(DERP_ORDER.SALEPOREL_STATE_0, "销售订单"));
		salePoRel_stateList.add(new DerpBasic(DERP_ORDER.SALEPOREL_STATE_1, "销售退订单"));

		/** 销售退货入库t_sale_return_idepot------------------------------------- */
		/** 状态: 011-待入仓 012-已入仓 028-入库中 */
		saleReturnIdepot_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNIDEPOT_STATUS_011, "待入仓"));
		saleReturnIdepot_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNIDEPOT_STATUS_028, "入库中"));
		saleReturnIdepot_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNIDEPOT_STATUS_012, "已入仓"));

		/** 销售退货出库t_sale_return_odepot------------------------------------- */
		/** 单据状态: 015-待出仓 016-已出仓 027-出库中 */
		saleReturnOdepot_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNODEPOT_STATUS_015, "待出仓"));
		saleReturnOdepot_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNODEPOT_STATUS_027, "出库中"));
		saleReturnOdepot_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNODEPOT_STATUS_016, "已出仓"));

		/** 销售退货订单t_sale_return_order------------------------------------- */
		/** 销售退服务类型 E0-区内调拨调出服务 F0-区内调拨调入服务 G0-库内调拨服务 */
		saleReturn_serveTypesList.add(new DerpBasic(DERP_ORDER.SALERETURN_SERVETYPES_E0, "区内调拨调出服务"));
		saleReturn_serveTypesList.add(new DerpBasic(DERP_ORDER.SALERETURN_SERVETYPES_F0, "区内调拨调入服务"));
		saleReturn_serveTypesList.add(new DerpBasic(DERP_ORDER.SALERETURN_SERVETYPES_G0, "库内调拨服务"));

		/** 销售退业务场景 40-账册内货权转移 50-账册内货权转移调仓 */
		saleReturn_modelList.add(new DerpBasic(DERP_ORDER.SALERETURN_MODEL_40, "账册内货权转移"));
		saleReturn_modelList.add(new DerpBasic(DERP_ORDER.SALERETURN_MODEL_50, "账册内货权转移调仓"));

		/**
		 * 状态：001-待审核 003-已审核 011-待入仓 012-已入仓 015-待出仓 016-已出仓 007-已完结 028-入库中 027-出库中
		 */
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_001, "待审核"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_003, "已审核"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_011, "待入仓"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_012, "已入仓"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_015, "待出仓"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_016, "已出仓"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_007, "已完结"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_028, "入库中"));
		saleReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_STATUS_027, "出库中"));

		/** 销售退货类型: 1-消费者退货 2-代销退货 3-购销退货 */
		saleReturnOrder_returnTypeList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_RETURNTYPE_1, "消费者退货"));
		saleReturnOrder_returnTypeList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_RETURNTYPE_2, "代销退货"));
		saleReturnOrder_returnTypeList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_RETURNTYPE_3, "购销退货"));

		/** 退货方式 1-退货退款，2-仅退货 */
		saleReturnOrder_returnModeList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_RETURNMODE_1, "退货退款"));
		saleReturnOrder_returnModeList.add(new DerpBasic(DERP_ORDER.SALERETURNORDER_RETURNMODE_2, "仅退货"));

		/** 销售上架信息表t_sale_shelf------------------------------------- */
		/** 订单类型：1-销售 2-销售出库 */
		saleShelf_orderTypeList.add(new DerpBasic(DERP_ORDER.SALESHELF_ORDERTYPE_1, "销售"));
		saleShelf_orderTypeList.add(new DerpBasic(DERP_ORDER.SALESHELF_ORDERTYPE_2, "销售出库"));

		/** 核销表获取状态: 0/空-未获取 1-已获取 */
		saleShelf_verifyRelStateList.add(new DerpBasic(DERP_ORDER.SALESHELF_VERIFYRELSTATE_0, "未获取"));
		saleShelf_verifyRelStateList.add(new DerpBasic(DERP_ORDER.SALESHELF_VERIFYRELSTATE_1, "已获取"));

		/** 上架入库单t_sale_shelf_idepot------------------------------------- */
		/** 状态 011-待入仓 012-已入仓 028-入库中 */
		saleShelfIdepot_statusList.add(new DerpBasic(DERP_ORDER.SALESHELFIDEPOT_STATUS_011, "待入库"));
		saleShelfIdepot_statusList.add(new DerpBasic(DERP_ORDER.SALESHELFIDEPOT_STATUS_028, "入库中"));
		saleShelfIdepot_statusList.add(new DerpBasic(DERP_ORDER.SALESHELFIDEPOT_STATUS_012, "已入库"));

		/** 是否红冲单：0-否，1-是 */
		saleShelfIdepot_isWriteOffList.add(new DerpBasic(DERP_ORDER.SALESHELFIDEPOT_ISWRITEOFF_0, "否"));
		saleShelfIdepot_isWriteOffList.add(new DerpBasic(DERP_ORDER.SALESHELFIDEPOT_ISWRITEOFF_1, "是"));

		/** 理货单t_tallying_order------------------------------------- */
		/** 理货单状态 009-待确认 010-已确认 004-已驳回 */
		tallyingOrder_stateList.add(new DerpBasic(DERP_ORDER.TALLYINGORDER_STATE_009, "待确认"));
		tallyingOrder_stateList.add(new DerpBasic(DERP_ORDER.TALLYINGORDER_STATE_010, "已确认"));
		tallyingOrder_stateList.add(new DerpBasic(DERP_ORDER.TALLYINGORDER_STATE_004, "已驳回"));

		/** 订单类型 1-采购 2-调拨 3-销售退理货 */
		tallyingOrder_typeList.add(new DerpBasic(DERP_ORDER.TALLYINGORDER_TYPE_1, "采购"));
		tallyingOrder_typeList.add(new DerpBasic(DERP_ORDER.TALLYINGORDER_TYPE_2, "调拨"));
		tallyingOrder_typeList.add(new DerpBasic(DERP_ORDER.TALLYINGORDER_TYPE_3, "销售退理货"));

		/** 调拨入库t_transfer_in_depot------------------------------------- */
		/** 状态 011-待入仓 012-已入仓 028-入库中 */
		transferInDepot_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERINDEPOT_STATUS_011, "待入仓"));
		transferInDepot_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERINDEPOT_STATUS_028, "入库中"));
		transferInDepot_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERINDEPOT_STATUS_012, "已入仓"));

		/** 单据来源: 1-调拨入自动生成 2-接口回推生成 3-手工导入 */
		transferInDepot_sourceList.add(new DerpBasic(DERP_ORDER.TRANSFERINDEPOT_SOURCE_1, "调拨入自动生成"));
		transferInDepot_sourceList.add(new DerpBasic(DERP_ORDER.TRANSFERINDEPOT_SOURCE_2, "接口回推生成"));
		transferInDepot_sourceList.add(new DerpBasic(DERP_ORDER.TRANSFERINDEPOT_SOURCE_3, "手工导入"));

		/** 调拨订单t_transfer_order------------------------------------- */
		/** 业务场景: 10-账册内调仓 40-账册内货权转移 50-账册内货权转移调仓 */
		transferOrder_modelList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_MODEL_10, "账册内调仓"));
		transferOrder_modelList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_MODEL_40, "账册内货权转移"));
		transferOrder_modelList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_MODEL_50, "账册内货权转移调仓"));

		/** 服务类型: E0-区内调拨调出服务 F0-区内调拨调入服务 G0-库内调拨服务 */
		transferOrder_serveTypesList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_SERVETYPES_E0, "区内调拨调出服务"));
		transferOrder_serveTypesList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_SERVETYPES_F0, "区内调拨调入服务"));
		transferOrder_serveTypesList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_SERVETYPES_G0, "库内调拨服务"));

		/** 状态: 013-待提交 014-已提交 023-调拨已出库 024-调拨已入库 007-已完结 */
		transferOrder_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_STATUS_013, "待提交"));
		transferOrder_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_STATUS_014, "已提交"));
		transferOrder_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_STATUS_023, "调拨已出库"));
		transferOrder_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_STATUS_024, "调拨已入库"));
		transferOrder_statusList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_STATUS_007, "已完结"));

		/** 目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
		transferOrder_destinationList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_DESTINATION_HP01, "黄埔状元谷"));
		transferOrder_destinationList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_DESTINATION_HK01, "香港本地"));
		transferOrder_destinationList.add(new DerpBasic(DERP_ORDER.TRANSFERORDER_DESTINATION_GZJC01, "广州机场"));

		/** 调拨出库表t_transfer_out_depot------------------------------------- */
		/** 状态 015-待出仓 016-已出仓 027-出库中 */
		transferOutDepot_statusList.add(new DerpBasic(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_015, "待出仓"));
		transferOutDepot_statusList.add(new DerpBasic(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027, "出库中"));
		transferOutDepot_statusList.add(new DerpBasic(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_016, "已出仓"));

		/** 退运状态: 70-成功 90-作废 */
		transferOutDepot_returnStatusList.add(new DerpBasic(DERP_ORDER.TRANSFEROUTDEPOT_RETURNSTATUS_70, "待出仓"));
		transferOutDepot_returnStatusList.add(new DerpBasic(DERP_ORDER.TRANSFEROUTDEPOT_RETURNSTATUS_90, "已出仓"));

		/** 单据来源：1-手工导入 2-接口生成 */
		transferOutDepot_dataSourceList.add(new DerpBasic(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_1, "手工导入"));
		transferOutDepot_dataSourceList.add(new DerpBasic(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_2, "接口生成"));

		/** 附件状态: 001-已启用 006-已删除 */
		attachment_statusList.add(new DerpBasic(DERP_ORDER.ATTACHMENT_STATUS_001, "已启用"));
		attachment_statusList.add(new DerpBasic(DERP_ORDER.ATTACHMENT_STATUS_006, "已删除"));

		/** 云集结算账单是否使用: 0-未使用 1-已使用 */
		yjAccountData_isUsedList.add(new DerpBasic(DERP_ORDER.YJ_ACCOUNT_DATA_ISUSED_0, "未使用"));
		yjAccountData_isUsedList.add(new DerpBasic(DERP_ORDER.YJ_ACCOUNT_DATA_ISUSED_1, "已使用"));

		/** 组合商品对照表t_group_merchandise_contrast */
		/** 状态：0-已停用 1-已启用 */
		groupMerchandiseContrast_statusList.add(new DerpBasic(DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_0, "已停用"));
		groupMerchandiseContrast_statusList.add(new DerpBasic(DERP_ORDER.GROUPMERCHANDISECONTRAST_STATUS_1, "已启用"));

		/** 账单出入库表t_bill_outin_depot */
		/** 处理类型:XSC-销售出库 GJC-国检出库 PKC-盘亏出库 BFC-报废 PYR-盘盈入库 KTR-客退入库 */
		processingTypeList.add(new DerpBasic(DERP_ORDER.PROCESSINGTYPE_XSC, "销售出库"));
		processingTypeList.add(new DerpBasic(DERP_ORDER.PROCESSINGTYPE_GJC, "国检出库"));
		processingTypeList.add(new DerpBasic(DERP_ORDER.PROCESSINGTYPE_PKC, "盘亏出库"));
		processingTypeList.add(new DerpBasic(DERP_ORDER.PROCESSINGTYPE_BFC, "报废"));
		processingTypeList.add(new DerpBasic(DERP_ORDER.PROCESSINGTYPE_PYR, "盘盈入库"));
		processingTypeList.add(new DerpBasic(DERP_ORDER.PROCESSINGTYPE_KTR, "客退入库"));

		/** 状态 单据状态 00-待审核 01-处理中 02-已出库 03-已入库 */
		billOutinDepot_stateList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_STATE_00, "待审核"));
		billOutinDepot_stateList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_STATE_01, "处理中"));
		billOutinDepot_stateList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_STATE_02, "已出库"));
		billOutinDepot_stateList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_STATE_03, "已入库"));

		/** 库存调整类型 0 调减 1调增 */
		billOutinDepot_operateTypeList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_0, "调减"));
		billOutinDepot_operateTypeList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_OPERATE_TYPE_1, "调增"));

		/** 账单来源 1-云集爬虫账单 2-唯品爬虫账单 3-手动导入 */
		billOutinDepot_billSourceList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_1, "云集爬虫账单"));
		billOutinDepot_billSourceList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_2, "唯品爬虫账单"));
		billOutinDepot_billSourceList.add(new DerpBasic(DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_3, "手动导入"));

		/** 采购退货状态：001-待审核 003-已审核 015-待出仓 016-已出仓 007-已完结 027-出库中 */
		purchaseReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_STATUS_001, "待审核"));
		purchaseReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_STATUS_003, "已审核"));
		purchaseReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_STATUS_015, "待出仓"));
		purchaseReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_STATUS_027, "出库中"));
		purchaseReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_STATUS_016, "已出仓"));
		purchaseReturnOrder_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_STATUS_007, "已完结"));

		/** 采购退货邮寄方式 1-寄售 2-自提 */
		purchaseReturnOrder_mailModeList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_MAILMODE_1, "寄售"));
		purchaseReturnOrder_mailModeList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_MAILMODE_2, "自提"));

		/** 采购退货目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
		purchaseReturnOrder_destinationList
				.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_DESTINATION_HP01, "黄埔状元谷"));
		purchaseReturnOrder_destinationList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_DESTINATION_HK01, "香港本地"));
		purchaseReturnOrder_destinationList
				.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_DESTINATION_GZJC01, "广州机场"));

		/** 采购退货是否同关区0-否，1-是 */
		purchaseReturnOrder_isSameAreaList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_ISSAMEAREA_0, "否"));
		purchaseReturnOrder_isSameAreaList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDER_ISSAMEAREA_1, "是"));

		/** 采购退货出库状态：001-待审核 003-已审核 015-待出仓 016-已出仓 007-已完结 027-出库中 */
		purchaseReturnOrderOdepot_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_001, "待审核"));
		purchaseReturnOrderOdepot_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_003, "已审核"));
		purchaseReturnOrderOdepot_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_015, "待出仓"));
		purchaseReturnOrderOdepot_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_027, "出库中"));
		purchaseReturnOrderOdepot_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_016, "已出仓"));
		purchaseReturnOrderOdepot_statusList.add(new DerpBasic(DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_007, "已完结"));

		/** 采购合同运输方式：英文枚举值：CIF；CFR ；FOB； DAP； EXW； BY AIR； BY SEA； BY LAND */
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_CIF, "CIF"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_CFR, "CFR"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_FOB, "FOB"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_DAP, "DAP"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_EXW, "EXW"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_CIP, "CIP"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_BYAIR, "空运"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_BYSEA, "海运"));
		purchasereContract_tranList.add(new DerpBasic(DERP_ORDER.PURCHASERECONTRACT_TRAN_BYLAND, "陆运"));

		/** 事业部移库单表t_bu_move_inventory--------------------------------- */
		/** 单据来源 1：手工导入；2：系统自动生成 */
		buMoveInventory_orderSourceList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_ORDERSOURCE_1, "手工导入"));
		buMoveInventory_orderSourceList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_ORDERSOURCE_2, "系统自动生成"));

		/** 移库状态 017-待移库,018-已移库,027-移库中 */
		buMoveInventory_statusList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_STATUS_017, "待移库"));
		buMoveInventory_statusList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_STATUS_027, "移库中"));
		buMoveInventory_statusList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_STATUS_018, "已移库"));

		/** 单据类别 DSDD:电商订单、XSDD：销售订单 KCYKD:库存移库单 */
		buMoveInventory_orderTypeList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_DSDD, "电商订单"));
		buMoveInventory_orderTypeList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_XSDD, "销售订单"));
		buMoveInventory_orderTypeList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORY_ORDERTYPE_KCYKD, "库存移库单"));

		/**事业部移库表体t_bu_move_inventory_item---------------------------------*/
		/**库存类型 0 好品  1 坏品*/
		buMoveInventoryItem_typeList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORYITEM_TYPE_0, "好品"));
		buMoveInventoryItem_typeList.add(new DerpBasic(DERP_ORDER.BUMOVEINVENTORYITEM_TYPE_1, "坏品"));

		/** 事业部移库单表t_agreement_currency_config--------------------------------- */
		/** 状态 032-已生效,006-已删除 */
		agreementCurrencyConfig_statusList.add(new DerpBasic(DERP_ORDER.AGREEMENTCURRENCYCONFIG_STATUS_032, "已生效"));

		/** 应收账单跟踪表t_receive_bill_verification */
		/** 是否逾期 0否,1是 */
		receiveBillVerification_overdueStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_OVERDUESTATUS_0, "否"));
		receiveBillVerification_overdueStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_OVERDUESTATUS_1, "是"));
		/** 开票状态 0未开票  1-已开票*/
		receiveBillVerification_invoiceStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_INVOICESTATUS_0, "未开票"));
		receiveBillVerification_invoiceStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_INVOICESTATUS_1, "已开票"));

		/** 应收账单表t_receive_bill */
		/** 账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-作废待审 06-已作废 */
		receiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_BILLSTATUS_00, "待提交"));
		receiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_BILLSTATUS_01, "待审核"));
		receiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_BILLSTATUS_02, "待核销"));
		receiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_BILLSTATUS_03, "部分核销"));
		receiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_BILLSTATUS_04, "已核销"));
		receiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_BILLSTATUS_05, "作废待审"));
		receiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_BILLSTATUS_06, "已作废"));
		/** 开票状态 00-待开票 01-待签章 02-已作废 03-已签章 */
		receiveBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_00, "待开票"));
		receiveBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_01, "待签章"));
		receiveBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_02, "已作废"));
		receiveBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_INVOICESTATUS_03, "已签章"));

		/** 单据类型 1-上架单 2-账单出库单 3-预售单 4-销售订单 5-采购SD单 6-融资申请单 7-销售退货单 8-赊销单 9-赊销账单 */
		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_1, "上架单"));
		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_2, "账单出库单"));
		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_3, "预售单"));
//		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_4, "销售订单"));
		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_5, "采购SD单"));
//		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_6, "融资申请单"));
		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_7, "销售退货单"));
		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_8, "赊销单"));
		receiveBill_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ORDERTYPE_9, "赊销账单"));

		/** 销售模式 009-经销模式 */
		receiveBill_saleModeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_SALEMODEL_009, "经销模式"));

		/** 凭证状态：1-正常，0-作废 */
		receiveBill_voucherStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_VOUCHERSYNSTATUS_0, "作废"));
		receiveBill_voucherStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_VOUCHERSYNSTATUS_1, "正常"));

		/** 是否增加残损金额 0-是 1-否 */
		receiveBill_isAddWornList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ISADDWORN_0, "是"));
		receiveBill_isAddWornList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_ISADDWORN_1, "否"));

		/** 分类：1-商销收入 2-GTN核销 3-采购rebate */
		receiveBill_sortTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_SORTTYPE_1, "商销收入"));
		receiveBill_sortTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_SORTTYPE_2, "GTN核销"));
		receiveBill_sortTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILL_SORTTYPE_3, "采购rebate"));

		/* 预收账单操作日志 操作结果00-审批通过 01-驳回 02-提交审核 03-提交作废 */
		advanceBill_resultList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_RESULT_0, "通过"));
		advanceBill_resultList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_RESULT_1, "驳回"));
		advanceBill_resultList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_RESULT_2, "提交审核"));
		advanceBill_resultList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_RESULT_3, "提交作废"));
		/* 预收账单操作日志表 操作类型 0-提交 1-审核 2-提交作废 3-审核作废 */
		advanceBill_typeList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_TYPE_0, "提交"));
		advanceBill_typeList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_TYPE_1, "审核"));
		advanceBill_typeList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_TYPE_2, "提交作废"));
		advanceBill_typeList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_TYPE_3, "审核作废"));
		advanceBill_typeList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_TYPE_4, "开票"));
		/** 账单状态 00-待提交 01-待审核 02-待核销 03-待收款 04-已核销 05-作废待审 06-已作废 */
		advanceBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_BILLSTATUS_00, "待提交"));
		advanceBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_BILLSTATUS_01, "待审核"));
		advanceBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_BILLSTATUS_02, "待核销"));
		advanceBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_BILLSTATUS_03, "待收款"));
		advanceBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_BILLSTATUS_04, "已核销"));
		advanceBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_BILLSTATUS_05, "作废待审"));
		advanceBill_billStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_BILLSTATUS_06, "已作废"));
		/* 预收账单 单据类型 1-销售 */
		advanceBill_orderTypeList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_ORDERTYPE_1, "销售订单"));
		/** 开票状态 00-待开票 01-待签章 02-已作废 03-已签章 */
		advanceBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_00, "待开票"));
		advanceBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_01, "待签章"));
		advanceBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_02, "已作废"));
		advanceBill_invoiceStatusList.add(new DerpBasic(DERP_ORDER.ADVANCEBILL_INVOICESTATUS_03, "已签章"));

		/** 模版类型 */
		temp_typelist.add(new DerpBasic(DERP_ORDER.TEMP_TYPE_1, "采购合同"));
		temp_typelist.add(new DerpBasic(DERP_ORDER.TEMP_TYPE_2, "应收账单"));
		temp_typelist.add(new DerpBasic(DERP_ORDER.TEMP_TYPE_3, "清关单证"));
		temp_typelist.add(new DerpBasic(DERP_ORDER.TEMP_TYPE_4, "预收账单"));
		/** 模版管理 状态 1-启用 0-禁用 */
		temp_statusList.add(new DerpBasic(DERP_ORDER.TEMP_STATUS_1, "启用"));
		temp_statusList.add(new DerpBasic(DERP_ORDER.TEMP_STATUS_0, "禁用"));

		/** 应收账单明细表t_receive_bill_item */
		/** 数据来源：0-单据 1-手动添加 */
		receiveBillItem_dataSourceList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_0, "单据"));
		receiveBillItem_dataSourceList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLITEM_DATASOURCE_1, "手动添加"));

		/** 应收账单费用明细表t_receive_bill_cost_item */
		/** 补扣款类型 0-补款 1-扣款 */
		receiveBillCost_billTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_0, "补款"));
		receiveBillCost_billTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLCOST_BILLTYPE_1, "扣款"));

		/** 应收账单操作记录表 t_receive_bill_operate */
		/** 操作节点 0-提交 1-审核通过 2-审核驳回 3-作废 4-作废审核通过 5—作废审核驳回 6-开票 7-发票签章 8-核销 */
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_0, "提交"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_1, "审核通过"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_2, "审核驳回"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_3, "作废"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_4, "作废审核通过"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_5, "作废审核驳回"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_6, "开票"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_7, "发票签章"));
		receiveBillOperate_operateNodeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLOPERATE_OPERATENODE_8, "核销"));

		/** 应收账单审批记录表t_receive_bill_audit_item */
		/** 审批结果 00-审批通过 01-驳回 */
		receiveBillAudit_auditResultList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_00, "审批通过"));
		receiveBillAudit_auditResultList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLAUDIT_AUDITRESULT_01, "驳回"));

		/** 审批类型 0-应收审核 1-作废审核 */
		receiveBillAudit_auditTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLAUDIT_AUDITTYPE_0, "应收审核"));
		receiveBillAudit_auditTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLAUDIT_AUDITTYPE_1, "作废审核"));

		/** 平台采购订单 单据状态：1:待提交 2.未转销售,3:已转销售 */
		platformPurchase_statusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_STATUS_1, "待提交"));
		platformPurchase_statusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_STATUS_2, "未转销售"));
		platformPurchase_statusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_STATUS_3, "已转销售"));

		/**
		 * 平台采购订单 平台状态： 1.待发货确认、2.等待签收、3.等待入库、4.部分收货、5已完成 6.待供应商发货、7.等待收货 、8.收货完成、9.待审核
		 * 、10.待确认,11.审核不通过
		 */
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_1, "待发货确认"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_2, "等待签收"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_3, "等待入库"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_4, "部分收货"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_5, "已完成"));
		platformPurchase_platformStatusList
				.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_6, "待供应商发货"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_7, "等待收货"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_8, "收货完成"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_9, "待审核"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_10, "待确认"));
		platformPurchase_platformStatusList
				.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_11, "审核不通过"));
		platformPurchase_platformStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_PURCHASE_PLATFORM_STATUS_12, "已审核"));

		/** 库位调整单表头 t_inventory_location_adjustment_order */
		/** 订单状态:001:待审核,027:审核中,003:已审核,006:已删除 */
		inventoryLocationAdjustmentOrder_statusList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_001, "待审核"));
		inventoryLocationAdjustmentOrder_statusList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_027, "审核中"));
		inventoryLocationAdjustmentOrder_statusList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_003, "已审核"));
		inventoryLocationAdjustmentOrder_statusList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_STATUS_006, "已删除"));

		/** 单据类型:DSDD:电商订单，DBRK:调拨入库，DBCK:调拨出库，XSCK:销售出库 */
		inventoryLocationAdjustmentOrder_typeList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DSDD, "电商订单"));
		inventoryLocationAdjustmentOrder_typeList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DBRK, "调拨入库"));
		inventoryLocationAdjustmentOrder_typeList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_DBCK, "调拨出库"));
		inventoryLocationAdjustmentOrder_typeList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_ORDER_TYPE_XSCK, "销售出库"));

		/** 库位调整单表体 t_inventory_location_adjustment_order_item */
		/** 库存类型 0：好品 1：坏品 */
		inventoryLocationAdjustmentOrderItem_inventoryTypeList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_0, "好品"));
		inventoryLocationAdjustmentOrderItem_inventoryTypeList
				.add(new DerpBasic(DERP_ORDER.INVENTORY_LOCATION_ADJUSTMENT_INVENTORY_TYPE_1, "坏品"));

		/** 平台费用订单 t_platform_cost_order */
		/** 单据状态： 1-待确认 2-已确认 3-已转账单 */
		platformCostOrder_platformCostStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_1, "待确认"));
		platformCostOrder_platformCostStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_2, "已确认"));
		platformCostOrder_platformCostStatusList.add(new DerpBasic(DERP_ORDER.PLATFORM_COST_ORDER_STATUS_3, "已转账单"));

		/** 付款主体 1 补款 2 扣款 */
		platformCostOrder_costTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_COST_ORDER_COST_TYPE_1, "补款"));
		platformCostOrder_costTypeList.add(new DerpBasic(DERP_ORDER.PLATFORM_COST_ORDER_COST_TYPE_2, "扣款"));

		/** 单据来源 1:爬虫账单 */
		platformCostOrder_sourceList.add(new DerpBasic(DERP_ORDER.PLATFORM_COST_ORDER_source_1, "爬虫账单"));

		/** 应收账单发票关联表 t_receive_bill_invoice */
		/** 发票状态：01-待签章 02-已作废 03-已签章 */
		receiveBillInvoice_statusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_01, "待签章"));
		receiveBillInvoice_statusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_02, "已作废"));
		receiveBillInvoice_statusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_STATUS_03, "已签章"));

		/** NC同步状态： 0-未同步 1-已同步 2-作废同步 */
		receiveBillInvoice_ncSynStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_NCSYNSTATUS_0, "未同步"));
		receiveBillInvoice_ncSynStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_NCSYNSTATUS_1, "已同步"));
		receiveBillInvoice_ncSynStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_NCSYNSTATUS_2, "作废同步"));

		/** 发票类型 0-ToB发票 1-ToC发票 3-收据*/
		receiveBillInvoice_invoiceTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_0, "To B发票"));
		receiveBillInvoice_invoiceTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_1, "To C发票"));
		receiveBillInvoice_invoiceTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLINVOICE_INVOICETYPE_2, "收据"));

		/** 费项配置表 t_settlement_config */
		/** 层级 1:一级,2:二级 */
		settlementConfig_levelList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_1, "一级"));
		settlementConfig_levelList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_LEVRL_2, "二级"));

		/** 状态(1使用中,0已禁用) */
		settlementConfig_statusList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_STATUS_0, "已禁用"));
		settlementConfig_statusList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_STATUS_1, "使用中"));

		/** 适用客户:1通用,2指定客户 */
		settlementConfig_suitableCustomerList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_SUITABLECUSTOMER1, "通用"));
		settlementConfig_suitableCustomerList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_SUITABLECUSTOMER2, "指定客户"));

		/** 适用类型:1.To B 2.To C 3ToB和ToC */
		settlementConfig_typeList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_TYPE1, "To B"));
		settlementConfig_typeList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_TYPE2, "To C"));
		settlementConfig_typeList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_TYPE3, "To B和To C"));

		/** 适用模块:1.应付 2.应收 3.预付 4.预收 */
		settlementConfig_moduleTypeList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_MODULETYPE1, "应付"));
		settlementConfig_moduleTypeList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_MODULETYPE2, "应收"));
		settlementConfig_moduleTypeList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_MODULETYPE3, "预付"));
		settlementConfig_moduleTypeList.add(new DerpBasic(DERP_ORDER.SETTLEMENTCONFIG_MODULETYPE4, "预收"));

		/** 平台费项配映射表 t_platform_settlement_config */
		/** 状态(1使用中,0已禁用) */
		platform_settlementConfig_statusList.add(new DerpBasic(DERP_ORDER.PLATFORMSETTLEMENTCONFIG_STATUS_0, "已禁用"));
		platform_settlementConfig_statusList.add(new DerpBasic(DERP_ORDER.PLATFORMSETTLEMENTCONFIG_STATUS_1, "使用中"));

		/** SD类型配置表 t_sd_type_config */
		/** 类型 1-单比例 2-多比例 */
		sdtypeconfig_typeList.add(new DerpBasic(DERP_ORDER.SDTYPECONFIG_TYPE_1, "单比例"));
		sdtypeconfig_typeList.add(new DerpBasic(DERP_ORDER.SDTYPECONFIG_TYPE_2, "多比例"));

		/** 状态 1-启用 0-禁用 */
		sdtypeconfig_statusList.add(new DerpBasic(DERP_ORDER.SDTYPECONFIG_STATUS_1, "启用"));
		sdtypeconfig_statusList.add(new DerpBasic(DERP_ORDER.SDTYPECONFIG_STATUS_0, "禁用"));

		/** 状态 1-已审核 0-待审核 */
		sdPurchase_statusList.add(new DerpBasic(DERP_ORDER.SDPURCHASE_STATUS_1, "已审核"));
		sdPurchase_statusList.add(new DerpBasic(DERP_ORDER.SDPURCHASE_STATUS_0, "待审核"));

		/** 1-采购SD，2-采购退SD，3-调整SD */
		purchaseSdOrder_typeList.add(new DerpBasic(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1, "采购SD"));
		purchaseSdOrder_typeList.add(new DerpBasic(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_2, "采购退SD"));
		purchaseSdOrder_typeList.add(new DerpBasic(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_3, "调整SD"));

		/** 金额调整状态 0-未调整 1-已调整 */
		saleOrder_amountStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_AMOUMTSTATUS_0, "未调整"));
		saleOrder_amountStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_AMOUMTSTATUS_1, "已调整"));

		/** 金额确认状态 0-未确认 1-确认通过 2-确认不通过 */
		saleOrder_amountConfirmStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_AMOUMT_CONFIRM_STATUS_0, "未确认"));
		saleOrder_amountConfirmStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_AMOUMT_CONFIRM_STATUS_1, "确认通过"));
		saleOrder_amountConfirmStatusList.add(new DerpBasic(DERP_ORDER.SALEORDER_AMOUMT_CONFIRM_STATUS_2, "确认不通过"));

		/** To C暂估应收表 t_toc_temporary_receive_bill */
		/** 结算状态：0-未结算 1-部分结算 2-已结算 */
		toctempbill_settlementStatusList.add(new DerpBasic(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_0, "未结算"));
		toctempbill_settlementStatusList.add(new DerpBasic(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_1, "部分结算"));
		toctempbill_settlementStatusList.add(new DerpBasic(DERP_ORDER.TOCTEMPBILL_SETTLEMENTSTATUS_2, "已结算"));
		/** To C暂估应收明细表 t_toc_temporary_receive_bill_item */
		/** 结算标识：0-未核销 1-已红冲 2-已核销 */
		tocTempItemBill_settlementMarkList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_0, "未核销"));
		tocTempItemBill_settlementMarkList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_1, "已红冲"));
		tocTempItemBill_settlementMarkList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMBILL_SETTLEMENTMARK_2, "已核销"));
		/** 单据类型：0-发货订单 1-退款订单 2-差异调整发货单  3-差异调整退货单*/
		tocTempItemBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_0, "发货订单"));
		tocTempItemBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_1, "退款订单"));
		tocTempItemBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_2, "差异调整发货单"));
		tocTempItemBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMBILL_ORDERTYPE_3, "差异调整退货单"));


		/** To C暂估费用明细表 t_toc_temporary_receive_bill_cost_item */
		/** 结算标识：0-未核销 1-已红冲 2-已核销  */
		tocTempItemCostBill_settlementMarkList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_0, "未核销"));
		tocTempItemCostBill_settlementMarkList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_1, "已红冲"));
		tocTempItemCostBill_settlementMarkList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMCOSTBILL_SETTLEMENTMARK_2, "已核销"));
		/** 单据类型：1-发货订单 0-退款订单 */
		tocTempItemCostBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_1, "发货订单"));
		tocTempItemCostBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_0, "退款订单"));
		tocTempItemCostBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_2, "差异调整退款单"));
		tocTempItemCostBill_orderTypeList.add(new DerpBasic(DERP_ORDER.TOCTEMPITEMCOSTBILL_ORDERTYPE_3, "差异调整发货单"));

		/** To C结算单表 t_toc_settlement_receive_bill */
		/**
		 * 账单状态 账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-作废待审 06-已作废 07-生成中 08-生成失败
		 */
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_00, "待提交"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_01, "待审核"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_02, "待核销"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_03, "部分核销"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_04, "已核销"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_05, "作废待审"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_06, "已作废"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_07, "生成中"));
		tocReceiveBill_billStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_BILLSTATUS_08, "生成失败"));

		/**
		 * NC应收状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步
		 */
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_1, "待审核"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_2, "待入erp"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_3, "待入账"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_4, "已入账"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_5, "已关账"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_6, "待作废"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_7, "待红冲"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_8, "已作废"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_9, "已红冲"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_10, "未同步"));
		tocReceiveBill_nvSynList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_NCSYNSTATUS_11, "已同步"));

		/** 开票状态 00-待开票 01-待签章 02-已作废 03-已签章 */
		receiveBill_tocInvoiceStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_00, "待开票"));
		receiveBill_tocInvoiceStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_01, "待签章"));
		receiveBill_tocInvoiceStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_02, "已作废"));
		receiveBill_tocInvoiceStatusList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILL_INVOICESTATUS_03, "已签章"));

		/** To C结算单费用明细表 t_toc_settlement_receive_bill_cost_item */
		/** 补扣款类型 0-补款 1-扣款 */
		tocReceiveBillCost_billTypeList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_0, "补款"));
		tocReceiveBillCost_billTypeList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILLCOST_BILLTYPE_1, "扣款"));

		/** toc应收账单审批记录表t_toc_settlement_receive_bill_audit_item */
		/** 审批结果 00-审批通过 01-驳回 */
		tocReceiveBillAudit_auditResultList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITRESULT_00, "审批通过"));
		tocReceiveBillAudit_auditResultList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITRESULT_01, "驳回"));

		/** 审批类型 0-应收审核 1-作废审核 */
		tocReceiveBillAudit_auditTypeList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITTYPE_0, "应收审核"));
		tocReceiveBillAudit_auditTypeList.add(new DerpBasic(DERP_ORDER.TOCRECEIVEBILLAUDIT_AUDITTYPE_1, "作废审核"));

		/** 清关单证配置 进出仓配置 0-进仓，1-出仓 */
		customsFileConfig_DepotConfigList.add(new DerpBasic(DERP_ORDER.CUSTOMSFILECONFIG_DEPOTCONFIG_0, "进仓"));
		customsFileConfig_DepotConfigList.add(new DerpBasic(DERP_ORDER.CUSTOMSFILECONFIG_DEPOTCONFIG_1, "出仓"));

		/** 状态：0-已停用 1-已启用 */
		customsfileconfig_statusList.add(new DerpBasic(DERP_ORDER.CUSTOMSFILECONFIG_STATUS_0, "禁用"));
		customsfileconfig_statusList.add(new DerpBasic(DERP_ORDER.CUSTOMSFILECONFIG_STATUS_1, "启用"));

		/** 欧莱雅采购订单t_oreal_purchase_order */
		/** 来源:1.欧莱雅接口 */
		orealPurchaseOrder_sourceList.add(new DerpBasic(DERP_ORDER.OREAL_PURCHASE_ORDER_SOURCE_1, "欧莱雅接口"));

		/** 托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱 05-纸质托盘*/
		order_torrpacktypeList.add(new DerpBasic(DERP_ORDER.ORDER_TORRPACKTYPE_01, "塑料托盘"));
		order_torrpacktypeList.add(new DerpBasic(DERP_ORDER.ORDER_TORRPACKTYPE_02, "木质托盘"));
		order_torrpacktypeList.add(new DerpBasic(DERP_ORDER.ORDER_TORRPACKTYPE_03, "IPPC木托"));
		order_torrpacktypeList.add(new DerpBasic(DERP_ORDER.ORDER_TORRPACKTYPE_04, "纸箱"));
		order_torrpacktypeList.add(new DerpBasic(DERP_ORDER.ORDER_TORRPACKTYPE_05, "纸质托盘"));

		/** 领料单 t_material_order */
		/** 订单状态:001:待审核,002:审核中,003:已审核,027:出库中,018:已出库,006:已删除 017-待出库 */
		materialOrder_stateList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_STATE_001, "待审核"));
		materialOrder_stateList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_STATE_002, "审核中"));
		materialOrder_stateList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_STATE_003, "已审核"));
		materialOrder_stateList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_STATE_017, "待出库"));
		materialOrder_stateList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_STATE_027, "出库中"));
		materialOrder_stateList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_STATE_018, "已出库"));

		/** 领料单 是否同关区0-否，1-是 */
		materialOrder_IsSameAreaList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_ISSAMEAREA_0, "否"));
		materialOrder_IsSameAreaList.add(new DerpBasic(DERP_ORDER.MATERIALORDER_ISSAMEAREA_1, "是"));

		/** 领料出库单 t_material_out_depot */
		/** 状态: 017-待出库 018-已出库 027-出库中 006-已删除 */
		materialOutDepot_statusList.add(new DerpBasic(DERP_ORDER.MATERIALOUTDEPOT_STATUS_017, "待出库"));
		materialOutDepot_statusList.add(new DerpBasic(DERP_ORDER.MATERIALOUTDEPOT_STATUS_027, "出库中"));
		materialOutDepot_statusList.add(new DerpBasic(DERP_ORDER.MATERIALOUTDEPOT_STATUS_018, "已出库"));

		/** 应收账单核销记录表 t_receive_bill_verify_item */
		/** 单据类型 1-预收账单 2-收款单 */
		receiveBillVerify_typeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_1, "预收账单"));
		receiveBillVerify_typeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFY_TYPE_2, "收款单"));

		/** 客户额度配置 */
		/** 状态 0-待审核 1-已审核 */
		customerQuotaConfig_statusList.add(new DerpBasic(DERP_ORDER.CUSTOMERQUOTACONFIG_STATUS_0, "待审核"));
		customerQuotaConfig_statusList.add(new DerpBasic(DERP_ORDER.CUSTOMERQUOTACONFIG_STATUS_1, "已审核"));

		/** 客户额度预警 */
		/** 应收类型 1-应收 2-费用 */
		customerQuotaWarningItem_receiveTypeList
				.add(new DerpBasic(DERP_ORDER.CUSTOMERQUOTAWARNINGITEM_RECEIVETYPE_1, "应收"));
		customerQuotaWarningItem_receiveTypeList
				.add(new DerpBasic(DERP_ORDER.CUSTOMERQUOTAWARNINGITEM_RECEIVETYPE_2, "费用"));

		/** ToB暂估核销表 t_tob_temporary_receive_bill */
		/** 销售类型 1:购销-整批结算 2:购销-实销实结 3-购销退货*/
		tobTempReceiveBill_saleTypeList.add(new DerpBasic(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_1, "购销-整批结算"));
		tobTempReceiveBill_saleTypeList.add(new DerpBasic(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_2, "购销-实销实结"));
		tobTempReceiveBill_saleTypeList.add(new DerpBasic(DERP_ORDER.TOBTEMPRECEIVEBILL_SALETYPE_3, "购销退货"));
		/**应收结算状态 1-已上架未结算 2-部分结算 5-已结算 */
		tobTempReceiveBill_statusList.add(new DerpBasic(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_1, "已上架未结算"));
		tobTempReceiveBill_statusList.add(new DerpBasic(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_2, "部分结算"));
		tobTempReceiveBill_statusList.add(new DerpBasic(DERP_ORDER.TOBTEMPRECEIVEBILL_STATUS_5, "已结算"));

		/** 销售sd单 数据来源 1-上架生成 2-手工导入 3-销售退生成*/
		saleSdOrder_orderSourceList.add(new DerpBasic(DERP_ORDER.SALE_SD_ORDER_ORDERSOURCE_1, "上架生成"));
		saleSdOrder_orderSourceList.add(new DerpBasic(DERP_ORDER.SALE_SD_ORDER_ORDERSOURCE_2, "手工导入"));

		/** 销售sd单 数据类型 1-上架单 2-销售退入库单*/
		saleSdOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_1, "上架单"));
		saleSdOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.SALE_SD_ORDER_ORDERTYPE_2, "销售退入库单"));

		/** 销售预申报 start*/
		/** 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款 */
		saleDeclare_paymentTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_PAYMENTTERMS_1, "一次付款-付款发货"));
		saleDeclare_paymentTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_PAYMENTTERMS_2, "多次付款 "));
		saleDeclare_paymentTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_PAYMENTTERMS_3, "一次付款-发货付款"));

		/** 贸易条款 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW */
		saleDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_1, "CIF"));
		saleDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_2, "CFR"));
		saleDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_3, "FOB"));
		saleDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_4, "DAP"));
		saleDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_5, "EXW"));

		/** 业务模式  1 购销-整批结算 3 采购执行 4.购销-实销实结 */
		saleDeclare_businessModelList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_BUSINESSMODEL_1, "购销-整批结算"));
		saleDeclare_businessModelList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_BUSINESSMODEL_3, "采购执行"));
		saleDeclare_businessModelList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_BUSINESSMODEL_4, "购销-实销实结"));

		/** 订单状态 001-待打托 002待清关 003-待物流委托 004-已出库 005-部分上架 007-已上架 006已删除 008-出库中 009-待出库*/
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_001, "待打托"));
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_002, "待清关"));
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_003, "待物流委托"));
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_008, "出库中"));
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_009, "待出库"));
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_004, "已出库"));
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_005, "部分上架"));
		saleDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_STATUS_007, "已上架"));

		/** 接口状态 0-未推接口；1-已推接口；2-接口推送失败 用于海外仓推跨境宝 */
		saleDeclare_apiStatusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_APISTATUS_0, "未推接口"));
		saleDeclare_apiStatusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_APISTATUS_1, "已推接口"));
		saleDeclare_apiStatusList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_APISTATUS_2, "接口推送失败"));

		/** 是否已生成预申报单 1-是 0-否 */
		saleDeclare_isGenerateList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_ISGENERATE_0, "否"));
		saleDeclare_isGenerateList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_ISGENERATE_1, "是"));

		/** 采购退货目的地: HP01-黄埔状元谷 HK01-香港本地 GZJC01-广州机场 */
		saleDeclare_destinationList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_DESTINATION_HP01, "黄埔状元谷"));
		saleDeclare_destinationList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_DESTINATION_HK01, "香港本地"));
		saleDeclare_destinationList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_DESTINATION_GZJC01, "广州机场"));
		/** 销售预申报 end*/

		/** 销售SD核销配置 t_sd_sale_verify_config*/
		/** 核销类型 0-按PO核销 1-按上架核销 */
		sdSaleVerifyConfig_verifyTypeList.add(new DerpBasic(DERP_ORDER.SDSALEVERIFYCONFIG_VERIFYTYPE_0, "按PO核销"));
		sdSaleVerifyConfig_verifyTypeList.add(new DerpBasic(DERP_ORDER.SDSALEVERIFYCONFIG_VERIFYTYPE_1, "按上架核销"));

		/** 状态 0-未审核 1-已启用 2-已停用 */
		sdSaleVerifyConfig_statusList.add(new DerpBasic(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_0, "未审核"));
		sdSaleVerifyConfig_statusList.add(new DerpBasic(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_1, "已启用"));
		sdSaleVerifyConfig_statusList.add(new DerpBasic(DERP_ORDER.SDSALEVERIFYCONFIG_STAUTS_2, "已停用"));

		/** 是否按商品核销 0-否 1-是 */
		sdSaleVerifyConfig_isMerchandiseVerifyList.add(new DerpBasic(DERP_ORDER.SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_0, "否"));
		sdSaleVerifyConfig_isMerchandiseVerifyList.add(new DerpBasic(DERP_ORDER.SDSALEVERIFYCONFIG_ISMERCHANDISEVERIFY_1, "是"));

		/** 赊销单 t_sale_credit_order start*/
		/** 订单状态 001:待提交,002:待收保证金,003:待放款,004:赊销中,005:待收款,006-已删除，007-已收款*/
		saleCredit_statusList.add(new DerpBasic(DERP_ORDER.SALECREDIT_STATUS_001, "待提交"));
		saleCredit_statusList.add(new DerpBasic(DERP_ORDER.SALECREDIT_STATUS_002, "待收保证金"));
		saleCredit_statusList.add(new DerpBasic(DERP_ORDER.SALECREDIT_STATUS_003, "待放款"));
		saleCredit_statusList.add(new DerpBasic(DERP_ORDER.SALECREDIT_STATUS_004, "赊销中"));
		saleCredit_statusList.add(new DerpBasic(DERP_ORDER.SALECREDIT_STATUS_005, "待收款"));
		saleCredit_statusList.add(new DerpBasic(DERP_ORDER.SALECREDIT_STATUS_007, "已收款"));

		/** 是否同步金融系统 0：未同步，1：已同步 */
		saleCredit_isSyncFinanceList.add(new DerpBasic(DERP_ORDER.SALECREDIT_IS_SYNC_FINANCE_0, "未同步"));
		saleCredit_isSyncFinanceList.add(new DerpBasic(DERP_ORDER.SALECREDIT_IS_SYNC_FINANCE_1, "已同步"));

		/** 赊销账单状态 001:待收款，002-已收款，006-已删除 */
		saleCreditBill_statusList.add(new DerpBasic(DERP_ORDER.SALECREDITBILL_STATUS_001, "待收款"));
		saleCreditBill_statusList.add(new DerpBasic(DERP_ORDER.SALECREDITBILL_STATUS_002, "已收款"));
		/** 赊销单 t_sale_credit_order end*/

		/** 应收账龄报告 t_receive_aging_report 1.ToB 2.ToC */
		receiveAging_channelTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_1, "To B"));
		receiveAging_channelTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEAGING_CHANNEL_TYPE_2, "To C"));

		/** 应收账龄报告 t_receive_aging_report 单据类型 1.To B 暂估 2.To B 收款核销  3.To C 暂估 4.To C 应收账单*/
		receiveAging_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_1, "To B 暂估"));
		receiveAging_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_2, "2B应收账单"));
		receiveAging_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_3, "To C 暂估"));
		receiveAging_orderTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEAGING_ORDERTYPE_TYPE_4, "2C应收账单"));
		/** 应收账龄报告 end*/

		/** 销售退预申报 t_sale_return_declare_order  start **/
		/** 贸易条款 1- CIF 2-CFR 3-FOB 4-DAP 5-EXW */
		saleReturnDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_1, "CIF"));
		saleReturnDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_2, "CFR"));
		saleReturnDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_3, "FOB"));
		saleReturnDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_4, "DAP"));
		saleReturnDeclare_tradeTermsList.add(new DerpBasic(DERP_ORDER.SALEDECLARE_TRADETERMS_5, "EXW"));

		/** 订单状态 001-待审核、002-已审核、003-待入仓、004-已入仓、005-已完结、006-已删除 */
		saleReturnDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_STATUS_001, "待审核"));
		saleReturnDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_STATUS_002, "已审核"));
		saleReturnDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_STATUS_003, "待入仓"));
		saleReturnDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_STATUS_004, "已入仓"));
		saleReturnDeclare_statusList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_STATUS_005, "已完结"));

		/** 接口状态 0-未推接口；1-已推接口 */
		saleReturnDeclare_apiStatusList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_APISTATUS_0, "未推接口"));
		saleReturnDeclare_apiStatusList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_APISTATUS_1, "已推接口"));
		/** 销售退预申报 t_sale_return_declare_order  end **/

		/** 平台暂估费用单表 t_platform_temporary_cost_order  start **/
		/** 订单类型 0-发货单 1-退款单 */
		platformTempCostOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_APISTATUS_0, "发货单"));
		platformTempCostOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.SALERETURNDECLARE_APISTATUS_1, "退款单"));

		/** 平台暂估费用单表 t_platform_temporary_cost_order  end **/

		/** 应收关账 t_receive_close_accounts  start **/
		/** 关账状态 029-未关账 030-已关账 */
		receiveCloseAccounts_statusList.add(new DerpBasic(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_029, "未关账"));
		receiveCloseAccounts_statusList.add(new DerpBasic(DERP_ORDER.RECEIVE_CLOSE_ACCOUNTS_STATUS_030, "已关账"));

		/** 应收关账 t_receive_close_accounts end **/

		/** 应收账单跟踪表 t_receive_bill_verification  start **/
		/**
		 * 账单状态 02-待核销 03-部分核销 04-已核销 05-作废待审
		 */
		receiveBillVerification_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLSTATUS_02, "待核销"));
		receiveBillVerification_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLSTATUS_03, "部分核销"));
		receiveBillVerification_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLSTATUS_04, "已核销"));
		receiveBillVerification_billStatusList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLSTATUS_05, "作废待审"));

		/**
		 * 账单类型 0-tob 1-toc
		 */
		receiveBillVerification_billTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_0, "To B"));
		receiveBillVerification_billTypeList.add(new DerpBasic(DERP_ORDER.RECEIVEBILLVERIFICATION_BILLTYPE_1, "To C"));

		/** 应收账单跟踪表 t_receive_bill_verification  end **/

		/** 应收月结快照 t_bill_monthly_snapshot  start **/
		/**
		 * 账单类型 0-tob 1-toc
		 */
		billMonthlySnapshot_billTypeList.add(new DerpBasic(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_0, "To B"));
		billMonthlySnapshot_billTypeList.add(new DerpBasic(DERP_ORDER.BILLMONTHLYSNAPSHOT_BILLTYPE_1, "To C"));

		/** 应收月结快照 t_bill_monthly_snapshot  end **/

		/** 库位调整单 t_location_adjustment_order  start **/

		/**
		 * 导入单据类型 DSDD-电商订单 DBDD-调拨订单 XSCK-销售出库单 SY-损益 XSTH-销售退货单
		 */
		locationAdjustmentOrder_importOrderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_DSDD, "电商订单"));
		locationAdjustmentOrder_importOrderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_DBDD, "调拨订单"));
		locationAdjustmentOrder_importOrderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_XSCK, "销售出库单"));
		locationAdjustmentOrder_importOrderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_SY, "损益"));
		locationAdjustmentOrder_importOrderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_IMPORTORDERTYPE_XSTH, "销售退货单"));

		/**
		 * 单据类型 DSDD-电商订单 DBRK-调拨入库单 DBCK-调拨出库单 XSDD-销售出库单 SY-损益 XSTH-销售退货单
		 */
		locationAdjustmentOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DSDD, "电商订单"));
		locationAdjustmentOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DBRK, "调拨入库单"));
		locationAdjustmentOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_DBCK, "调拨出库单"));
		locationAdjustmentOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_XSCK, "销售出库单"));
		locationAdjustmentOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_SY, "损益"));
		locationAdjustmentOrder_orderTypeList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_ORDERTYPE_XSTH, "销售退货单"));

		/**
		 * 状态 001-待审核 002-已审核
		 */
		locationAdjustmentOrder_statusList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_STATUS_001, "待审核"));
		locationAdjustmentOrder_statusList.add(new DerpBasic(DERP_ORDER.LOCATIONADJUSTMENTORDER_STATUS_002, "已审核"));

		/** 库位调整单 t_location_adjustment_order  end **/
	}

	/**
	 * 根据key获取中文
	 *
	 * @param list 集合
	 * @param key  常量值
	 */
	public static String getLabelByKey(List<DerpBasic> list, Object key) {
		for (DerpBasic item : list) {
			if (item.getKey().equals(key)) {
				return item.getValue();
			}
		}
		return "";
	}

	/**
	 * 根据key获取编码key
	 *
	 * @param list  集合
	 * @param Label 常量值
	 */
	public static Object getKeyByLabel(List<DerpBasic> list, String Label) {
		for (DerpBasic item : list) {
			if (item.getValue().equals(Label)) {
				return item.getKey();
			}
		}
		return "";
	}

	/**
	 * 获取常量集合
	 *
	 * @param listName 集合名称
	 */
	public static ArrayList<DerpBasic> getConstantListByName(String listName) {
		ArrayList<DerpBasic> list = null;
		try {
			Field[] fields = DERP_ORDER.class.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals(listName)) {
					list = (ArrayList<DerpBasic>) field.get(new DERP_ORDER());
				}
			}
		} catch (Exception e) {
		}
		return list;
	}
}
