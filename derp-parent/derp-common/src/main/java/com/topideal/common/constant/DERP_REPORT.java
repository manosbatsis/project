package com.topideal.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/***报表库
 *  表字段状态/类型常量文件
 * */
public class DERP_REPORT {
  
	/**(业务经销存)累计销售在途明细表r_business_add_sale_noshelf_details---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	public static final String BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_1 = "1";
	public static final String BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_2 = "2";
	public static final String BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_3 = "3";
	public static final String BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_4 = "4";
	public static ArrayList<DerpBasic> businessAddSaleNoshelfDetails_orderTypeList = new ArrayList<DerpBasic>();

	/**(业务经分销)本期减少销售在途明细表r_business_decrease_sale_noshelf---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	public static final String BUSINESSDECREASESALENOSHELF_ORDERTYPE_1 = "1";
	public static final String BUSINESSDECREASESALENOSHELF_ORDERTYPE_2 = "2";
	public static final String BUSINESSDECREASESALENOSHELF_ORDERTYPE_3 = "3";
	public static final String BUSINESSDECREASESALENOSHELF_ORDERTYPE_4 = "4";
	public static ArrayList<DerpBasic> businessDecreaseSaleNoshelf_orderTypeList = new ArrayList<DerpBasic>();

	/**文件任务表r_file_task---------------------------------*/
	/**
	 * 任务类型 YWJXC-进销存汇总报表 ,CWJXC-财务进销存报表 ,SXCW-刷新财务,VIPHXMXB唯品核销报表,SYBCWJXC事业部财务经销存 ,SDSYBCWJXC-SD事业部财务经销存,
	 * SYBYWJXC-事业部进销存汇总报表
	 * SXSYBCW-刷新事业部财务经销存 SXZHD-刷新自核对 ZKTS-在库天数 ,SYBCWZZ事业部财务总账  SYBCWZGYS 事业部财务暂估应收   SYBCWLX-利息经销存导出任务
	 * TOCTEMPITEM-toc暂估收入明细导出 TOCTEMPCOST-toc暂估费用明细导出 DSDD-电商订单导出 TOCTEMPITEMTOTAL-toc暂估收入累计暂估导出
	 * TOCTEMPCOSTTOTAL-toc暂估费用累计暂估导出, TOCSETTLEMENT-toc应收账单导出, TOCMONTHLY_RECEIVE-toc月结收入快照, TOCMONTHLY_COST-月结费用快照
	 * AGINGREPORTMONTHLY-应收账龄月结导出  TOCTEMPITEMSUMMARY-toc暂估收入汇总导出  TOCTEMPCOSTSUMMARY-toc暂估费用汇总导出
	 * SYBCW_MZCYCBDC 美赞成本差异导出
	 */
	public static final String FILETASK_TASKTYPE_YWJXC = "YWJXC";
	public static final String FILETASK_TASKTYPE_CWJXC = "CWJXC";
	public static final String FILETASK_TASKTYPE_SXCW = "SXCW";
	public static final String FILETASK_TASKTYPE_VIPHX = "VIPHX";
	public static final String FILETASK_TASKTYPE_SYBCWJXC = "SYBCWJXC";
	public static final String FILETASK_TASKTYPE_SDSYBCWJXC = "SDSYBCWJXC";
	public static final String FILETASK_TASKTYPE_SYBYWJXC = "SYBYWJXC";
	public static final String FILETASK_TASKTYPE_SXSYBCW = "SXSYBCW";
	public static final String FILETASK_TASKTYPE_SXZHD = "SXZHD";
	public static final String FILETASK_TASKTYPE_ZKTS = "ZKTS";
	public static final String FILETASK_TASKTYPE_SYBCWZZ = "SYBCWZZ";
	public static final String FILETASK_TASKTYPE_SYBCWZGYS = "SYBCWZGYS";
	public static final String FILETASK_TASKTYPE_SYBCWLX = "SYBCWLX";
	public static final String FILETASK_TASKTYPE_TOCTEMPITEM = "TOCTEMPITEM";
	public static final String FILETASK_TASKTYPE_TOCTEMPITEMTOTAL = "TOCTEMPITEMTOTAL";
	public static final String FILETASK_TASKTYPE_TOCTEMPITEMSUMMARY = "TOCTEMPITEMSUMMARY";//暂估收入汇总导出
	public static final String FILETASK_TASKTYPE_TOCTEMPCOST = "TOCTEMPCOST";
	public static final String FILETASK_TASKTYPE_TOCTEMPCOSTTOTAL = "TOCTEMPCOSTTOTAL";
	public static final String FILETASK_TASKTYPE_TOCTEMPCOSTSUMMARY = "TOCTEMPCOSTSUMMARY";//暂估费用汇总导出
	public static final String FILETASK_TASKTYPE_TOCPLATFORMTEMPCOSTTOTAL = "TOCPLATFORMTEMPCOSTTOTAL";
	public static final String FILETASK_TASKTYPE_TOCSETTLEMENT = "TOCSETTLEMENT";//2C应收导出
	public static final String FILETASK_TASKTYPE_TOCSETTLEMENT_SUMMARY = "TOCSETTLEMENT_SUMMARY";//2C应收汇总导出
	public static final String FILETASK_TASKTYPE_TOC_MONTHLY_RECEIVE = "TOCMONTHLY_RECEIVE";//2C应收月结收入快照
	public static final String FILETASK_TASKTYPE_TOC_MONTHLY_COST = "TOCMONTHLY_COST";//2C应收月结费用快照
	public static final String FILETASK_TASKTYPE_DSDD = "DSDD";
	public static final String FILETASK_TASKTYPE_AGINGREPORTMONTHLY = "AGINGREPORTMONTHLY";
	public static final String FILETASK_TASKTYPE_PAYMENT_BILL = "PAYMENT_BILL";//应付账单导出
	public static final String FILETASK_TASKTYPE_PLATFORMSTATEMENT = "PLATFORMSTATEMENT";//平台结算单
	public static final String FILETASK_TASKTYPE_SYBCW_MZCYCBDC = "SYBCW_MZCYCBDC";//美赞成本差异导出

	public static ArrayList<DerpBasic> fileTask_taskTypeList = new ArrayList<DerpBasic>();

	/**
	 * 任务状态 1-待执行 2-执行中 3-已完成 4-失败
	 */
	public static final String FILETASK_STATE_1 = "1";
	public static final String FILETASK_STATE_2 = "2";
	public static final String FILETASK_STATE_3 = "3";
	public static final String FILETASK_STATE_4 = "4";
	public static ArrayList<DerpBasic> fileTask_stateList = new ArrayList<DerpBasic>();

	/**
	 * 任务模块 1-报表 2-order
	 */
	public static final String FILETASK_MODULE_1 = "1";
	public static final String FILETASK_MODULE_2 = "2";
	public static ArrayList<DerpBasic> fileTask_moduleList = new ArrayList<DerpBasic>();

	/**(财务经分销)累计销售在途明细表r_finance_add_sale_noshelf_details---------------------------------*/
	/**
	 * 订单类型: 1-代销订单 2-购销订单  3-购销整批结算 4-购销实销实结
	 */
	public static final String FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_1 = "1";
	public static final String FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_2 = "2";
	public static final String FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_3 = "3";
	public static final String FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_4 = "4";
	public static ArrayList<DerpBasic> financeAddSaleNoshelfDetails_orderTypeList = new ArrayList<DerpBasic>();

	/**(财务经分销)本期减少销售在途r_finance_decrease_sale_noshelf---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单  3-购销整批结算 4-购销实销实结*/
	public static final String FINANCEDECREASESALENOSHELF_ORDERTYPE_1 = "1";
	public static final String FINANCEDECREASESALENOSHELF_ORDERTYPE_2 = "2";
	public static final String FINANCEDECREASESALENOSHELF_ORDERTYPE_3 = "3";
	public static final String FINANCEDECREASESALENOSHELF_ORDERTYPE_4 = "4";
	public static ArrayList<DerpBasic> financeDecreaseSaleNoshelf_orderTypeList = new ArrayList<DerpBasic>();

	/**财务进销存报表r_finance_inventory_summary---------------------------------*/
	/**状态 029-未关账 030-已关账*/
	public static final String FINANCEINVENTORYSUMMARY_STATUS_029 = "029";
	public static final String FINANCEINVENTORYSUMMARY_STATUS_030 = "030";
	public static ArrayList<DerpBasic> financeInventorySummary_statusList = new ArrayList<DerpBasic>();

	/**(财务经销存)销售残损明细表r_finance_sale_damaged---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	public static final String FINANCESALEDAMAGED_ORDERTYPE_1 = "1";
	public static final String FINANCESALEDAMAGED_ORDERTYPE_2 = "2";
	public static final String FINANCESALEDAMAGED_ORDERTYPE_3 = "3";
	public static final String FINANCESALEDAMAGED_ORDERTYPE_4 = "4";
	public static ArrayList<DerpBasic> financeSaleDamaged_orderTypeList = new ArrayList<DerpBasic>();

	/**(财务经分销)销售未上架r_finance_sale_notshelf---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	public static final String FINANCESALENOTSHELF_ORDERTYPE_1 = "1";
	public static final String FINANCESALENOTSHELF_ORDERTYPE_2 = "2";
	public static final String FINANCESALENOTSHELF_ORDERTYPE_3 = "3";
	public static final String FINANCESALENOTSHELF_ORDERTYPE_4 = "4";
	public static ArrayList<DerpBasic> financeSaleNotshelf_orderTypeList = new ArrayList<DerpBasic>();
	/**(财务经销存)采购残损明细表r_finance_purchase_damaged-------------------------*/
	/**残损类型：1来货残次、2来货短缺*/
	public static final String FINANCEPURCHASEDAMAGED_ORDERTYPE_1 = "1";
	public static final String FINANCEPURCHASEDAMAGED_ORDERTYPE_2 = "2";
	public static ArrayList<DerpBasic> financePurchaseDamaged_orderTypeList = new ArrayList<DerpBasic>();

	/**(财务经分销)销售已经上架r_finance_sale_shelf---------------------------------*/
	/**订单类型: 1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单  5-电商订单退货 6-购销退货 7-账单出入库单调减 8-账单出入库单调增 9-购销整批结算 10-购销实销实结  11-库位调整单调增 12-库位调整单调减 13-采销执行,14 电商订单退款 */
	public static final String FINANCESALESHELF_ORDERTYPE_1 = "1";
	public static final String FINANCESALESHELF_ORDERTYPE_2 = "2";
	public static final String FINANCESALESHELF_ORDERTYPE_3 = "3";
	public static final String FINANCESALESHELF_ORDERTYPE_4 = "4";
	public static final String FINANCESALESHELF_ORDERTYPE_5 = "5";
	public static final String FINANCESALESHELF_ORDERTYPE_6 = "6";
	public static final String FINANCESALESHELF_ORDERTYPE_7 = "7";
	public static final String FINANCESALESHELF_ORDERTYPE_8 = "8";
	public static final String FINANCESALESHELF_ORDERTYPE_9 = "9";
	public static final String FINANCESALESHELF_ORDERTYPE_10 = "10";
	public static final String FINANCESALESHELF_ORDERTYPE_11 = "11";
	public static final String FINANCESALESHELF_ORDERTYPE_12 = "12";
	public static final String FINANCESALESHELF_ORDERTYPE_13 = "13";
	public static final String FINANCESALESHELF_ORDERTYPE_14 = "14";
	public static ArrayList<DerpBasic> financeSaleShelf_orderTypeList = new ArrayList<DerpBasic>();

	/**(财务经分销)盘盈盘亏明细表r_finance_takes_stock_results---------------------------------*/
	/**调整类型: 1-盘盈 2-盘亏*/
	public static final String FINANCETAKESSTOCKRESULTS_TYPE_1 = "1";
	public static final String FINANCETAKESSTOCKRESULTS_TYPE_2 = "2";
	public static ArrayList<DerpBasic> financeTakesStockResults_typeList = new ArrayList<DerpBasic>();

	/**商品在库天数明细表r_in_warehouse_details---------------------------------*/
	/**状态: 01-已匹配 00-未匹配*/
	public static final String INWAREHOUSEDETAILS_STATUS_01 = "01";
	public static final String INWAREHOUSEDETAILS_STATUS_00 = "00";
	public static ArrayList<DerpBasic> inWarehouseDetails_statusList = new ArrayList<DerpBasic>();

	/**存货跌价准备报表r_inventory_falling_price_reserves---------------------------------*/
	/**效期区间 1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品*/
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_1 = "1";
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_2 = "2";
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_3 = "3";
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_4 = "4";
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_5 = "5";
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_6 = "6";
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_7 = "7";
	public static final String FALLINGPRICE_EFFECTIVEINTERVAL_8 = "8";
	public static ArrayList<DerpBasic> fallingPrice_effectiveIntervalList = new ArrayList<DerpBasic>();
	public static ArrayList<DerpBasic> fallingPrice_effectiveIntervalLabelList = new ArrayList<DerpBasic>();
	
	/**剩余效期占比(财务逻辑)1:1/10 ; 2: 1/5 ; 3: 1/3; 4: 1/2 ; 5: 1/2及以上 ; 7:过期品(为负) ; 8: 残次品*/
	public static final String FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_1 = "1";
	public static final String FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_2 = "2";
	public static final String FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_3 = "3";
	public static final String FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_4 = "4";
	public static final String FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_5 = "5";
	public static final String FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_7 = "7";
	public static final String FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_8 = "8";
	public static ArrayList<DerpBasic> fallingPrice_financialSurplusproportionList = new ArrayList<DerpBasic>();

	/**存货跌价库存类型*/
	/**0-好品，1-坏品*/
	public static final String FALLINGPRICE_INITINVENTORY_TYPE_0 = "0";
	public static final String FALLINGPRICE_INITINVENTORY_TYPE_1 = "1";
	public static ArrayList<DerpBasic> fallingPrice_initinventoryTypeList = new ArrayList<DerpBasic>();
	//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
	public static final String INVERNTORYSTATUS_1="1";
	public static final String INVERNTORYSTATUS_2="2";
	public static final String INVERNTORYSTATUS_3="3";
	public static final String INVERNTORYSTATUS_4="4";
	public static ArrayList<DerpBasic> inverntory_statusList = new ArrayList<DerpBasic>();

	/**结算价格r_settlement_price---------------------------------*/
	/**是否组合品  1-是 0-否*/
	public static final String SETTLEMENTPRICE_ISGROUP_1 = "1";
	public static final String SETTLEMENTPRICE_ISGROUP_0 = "0";
	public static ArrayList<DerpBasic> settlementPrice_isGroupList = new ArrayList<DerpBasic>();

	/**成本单价审核状态 001:待审核 , 013:待提交，021:已作废，032:已生效，033审核不通过*/
	public static final String SETTLEMENTPRICE_STATUS_001 = "001";
	public static final String SETTLEMENTPRICE_STATUS_013 = "013";
	public static final String SETTLEMENTPRICE_STATUS_021 = "021";
	public static final String SETTLEMENTPRICE_STATUS_032 = "032";
	public static final String SETTLEMENTPRICE_STATUS_033 = "033";
	public static ArrayList<DerpBasic> settlementPrice_statusList = new ArrayList<DerpBasic>();
	
	/**唯品库存调整明细r_vip_adjustment_inventory_details---------------------------------*/
	/**业务模式  4-唯品红冲、5-国检抽样、6-唯品报废*/
	public static final String VIPADJUSTMENTIDETAILS_MODEL_4 = "4";
	public static final String VIPADJUSTMENTIDETAILS_MODEL_5 = "5";
	public static final String VIPADJUSTMENTIDETAILS_MODEL_6 = "6";
	public static ArrayList<DerpBasic> vipAdjustmentIDetails_modelList = new ArrayList<DerpBasic>();

	/**唯品PO账单核销表r_vip_po_bill_verification---------------------------------*/
	/**完结状态 0-未完结，1-已完结*/
	public static final String VIPPOBILLVERIFICATION_STATUS_0 = "0";
	public static final String VIPPOBILLVERIFICATION_STATUS_1 = "1";
	public static ArrayList<DerpBasic> vipPoBillVerification_statusList = new ArrayList<DerpBasic>();

	/**唯品账单明细r_vip_bill_details---------------------------------*/
	/**账单类型: 00-销售明细、01-库存买断、02库存盘亏、03报废、04库存盘盈、05国检抽样*/
	public static final String VIPBILLDETAILS_BILLTYPE_00 = "00";
	public static final String VIPBILLDETAILS_BILLTYPE_01 = "01";
	public static final String VIPBILLDETAILS_BILLTYPE_02 = "02";
	public static final String VIPBILLDETAILS_BILLTYPE_03 = "03";
	public static final String VIPBILLDETAILS_BILLTYPE_04 = "04";
	public static final String VIPBILLDETAILS_BILLTYPE_05 = "05";
	public static ArrayList<DerpBasic> vipBillDetails_billTypeList = new ArrayList<DerpBasic>();

	/**唯品盘点结果明细r_vip_takes_stock_results_details---------------------------------*/
	/**调整类型  1-盘盈 2-盘亏*/
	public static final String VIPTRDETAILS_TAKESSTOCKRESULTTYPE_1 = "1";
	public static final String VIPTRDETAILS_TAKESSTOCKRESULTTYPE_2 = "2";
	public static ArrayList<DerpBasic> vipTRDetails_takesStockResultTypeList = new ArrayList<DerpBasic>();
	
	/**唯品调拨出库明细r_vip_takes_stock_results_details---------------------------------*/
	/**调拨类型  1-入库 2-出库*/
	public static final String VIPTRDETAILS_TRANSFER_TYPE_1 = "1";
	public static final String VIPTRDETAILS_TRANSFER_TYPE_2 = "2";
	public static ArrayList<DerpBasic> vipTransDetails_transferTypeList = new ArrayList<DerpBasic>();

	/**报表删除表体明细分类明细1.来源库存加减明细 2.批次库存,3,采购订单表体 4,销售订单表体,5调拨订单表体,6销售退货订单表体/表头,7公司仓库关联关系
	 * ，8公司仓库事业部关联关系 ，9根据商家 仓库 事业部 月份 删除事业部库存 10采购退货表体 13 预售单表体 14 事业部移库单表头表体
	 * 15 库位调整单表头表体 ,16 删除爬虫商品对照表表体 17.删除商家客户关系表数据*/
	public static final String DEL_REPORT_DATAS_ITEM_1 = "1" ;
	public static final String DEL_REPORT_DATAS_ITEM_2 = "2" ;
	public static final String DEL_REPORT_DATAS_ITEM_3 = "3" ;
	public static final String DEL_REPORT_DATAS_ITEM_4 = "4" ;
	public static final String DEL_REPORT_DATAS_ITEM_5 = "5" ;
	public static final String DEL_REPORT_DATAS_ITEM_6 = "6" ;
	public static final String DEL_REPORT_DATAS_ITEM_7 = "7" ;
	public static final String DEL_REPORT_DATAS_ITEM_8 = "8" ;
	public static final String DEL_REPORT_DATAS_ITEM_9 = "9" ;
	public static final String DEL_REPORT_DATAS_ITEM_10 = "10" ;
	public static final String DEL_REPORT_DATAS_ITEM_11 = "11" ;
	public static final String DEL_REPORT_DATAS_ITEM_12 = "12" ;
	public static final String DEL_REPORT_DATAS_ITEM_13 = "13" ;
	public static final String DEL_REPORT_DATAS_ITEM_14 = "14" ;
	public static final String DEL_REPORT_DATAS_ITEM_15 = "15" ;
	public static final String DEL_REPORT_DATAS_ITEM_16 = "16" ;
	public static final String DEL_REPORT_DATAS_ITEM_17 = "17" ;
	public static final String DEL_REPORT_DATAS_ITEM_18 = "18" ;
	public static ArrayList<DerpBasic> delReportDatasItemList = new ArrayList<DerpBasic>();

	/**首页—电商订单统计表r_index_order_statistics---------------------------------*/
	/**统计维度 1-店铺销售总量 2-品牌销售总量*/
	public static final String INDEXORDERSTATISTICS_STATISTICALDIMENSION_1 = "1" ;
	public static final String INDEXORDERSTATISTICS_STATISTICALDIMENSION_2 = "2" ;
	public static ArrayList<DerpBasic> indexOrderStatistics_statisticalDimensionList = new ArrayList<DerpBasic>();

	/**唯品自动核验  账单类型 00-销售明细、01-库存买断、02库存盘亏、03报废、04库存盘盈、05国检抽样、06唯品红冲*/
	public static final String VIP_AUTO_VERI_BILL_TYPE_00 = "00" ; 
	public static final String VIP_AUTO_VERI_BILL_TYPE_01 = "01" ; 
	public static final String VIP_AUTO_VERI_BILL_TYPE_02 = "02" ; 
	public static final String VIP_AUTO_VERI_BILL_TYPE_03 = "03" ; 
	public static final String VIP_AUTO_VERI_BILL_TYPE_04 = "04" ; 
	public static final String VIP_AUTO_VERI_BILL_TYPE_05 = "05" ; 
	public static final String VIP_AUTO_VERI_BILL_TYPE_06 = "06" ; 
	public static ArrayList<DerpBasic> vipAutoVeri_BillTypeList = new ArrayList<DerpBasic>();
	
	/**唯品自动核验 校验结果1-已对平、0-未对平*/
	public static final String VIP_AUTO_VERI_VERIFICATION_RESULT_0 = "0" ; 
	public static final String VIP_AUTO_VERI_VERIFICATION_RESULT_1 = "1" ; 
	public static ArrayList<DerpBasic> vipAutoVeri_verificationResultList = new ArrayList<DerpBasic>();
	
	/**唯品自动校验 系统单据类型 00销售出库单、02盘亏、03报废、04盘盈、05国检抽样、06唯品红冲*/
	public static final String VIP_AUTO_VERI_ORDER_TYPE_00 = "00" ;
	public static final String VIP_AUTO_VERI_ORDER_TYPE_02 = "02" ; 
	public static final String VIP_AUTO_VERI_ORDER_TYPE_03 = "03" ; 
	public static final String VIP_AUTO_VERI_ORDER_TYPE_04 = "04" ; 
	public static final String VIP_AUTO_VERI_ORDER_TYPE_05 = "05" ; 
	public static final String VIP_AUTO_VERI_ORDER_TYPE_06 = "06" ; 
	public static ArrayList<DerpBasic> vipAutoVeri_orderTypeList = new ArrayList<DerpBasic>();
	
	/**云集结算账单是否使用: 0-未使用 1-已使用*/
	public static final String YJ_ACCOUNT_DATA_ISUSED_0 = "0";
	public static final String YJ_ACCOUNT_DATA_ISUSED_1 = "1";
	public static ArrayList<DerpBasic> yjAccountData_isUsedList = new ArrayList<DerpBasic>();
	
	/**自动检验任务表r_automatic_check_task---------------------------------*/
	/**任务类型  1:POP流水核对 2:仓库流水核对*/
	public static final String AUTOMATICCHECKTASK_TASKTYPE_1= "1";
	public static final String AUTOMATICCHECKTASK_TASKTYPE_2= "2";
	public static final String AUTOMATICCHECKTASK_TASKTYPE_3= "3";
	public static ArrayList<DerpBasic> automaticCheckTask_taskTypeList = new ArrayList<DerpBasic>();
	
	/**核对结果 0:未对平 1:已对平 2:未标记*/
	public static final String AUTOMATICCHECKTASK_CHECKRESULT_0= "0";
	public static final String AUTOMATICCHECKTASK_CHECKRESULT_1= "1";
	public static final String AUTOMATICCHECKTASK_CHECKRESULT_2= "2";
	public static ArrayList<DerpBasic> automaticCheckTask_checkResultList = new ArrayList<DerpBasic>();
	
	/**处理状态 1:待处理 2:处理中 3:已完成 4：处理失败*/
	public static final String AUTOMATICCHECKTASK_STATE_1= "1";
	public static final String AUTOMATICCHECKTASK_STATE_2= "2";
	public static final String AUTOMATICCHECKTASK_STATE_3= "3";
	public static final String AUTOMATICCHECKTASK_STATE_4= "4";
	public static ArrayList<DerpBasic> automaticCheckTask_stateList = new ArrayList<DerpBasic>();
	
	/**数据源 1：GSS报表 2：菜鸟后台*/
	public static final String AUTOMATICCHECKTASK_DATASOURCE_1= "1";
	public static final String AUTOMATICCHECKTASK_DATASOURCE_2= "2";
	public static ArrayList<DerpBasic> automaticCheckTask_dataSourceList = new ArrayList<DerpBasic>();
	
	/**是否标记过 0-否，1-是*/
	public static final String AUTOMATICCHECKTASK_ISMARK_0= "0";
	public static final String AUTOMATICCHECKTASK_ISMARK_1= "1";
	public static ArrayList<DerpBasic> automaticCheckTask_isMarkList = new ArrayList<DerpBasic>();

	/**差异金额是否调整（0-否，1-是）*/
	public static final String AUTOMATICCHECKTASK_ISADJUST_0= "0";
	public static final String AUTOMATICCHECKTASK_ISADJUST_1= "1";
	public static ArrayList<DerpBasic> automaticCheckTask_isAdjustList = new ArrayList<DerpBasic>();


	/**销售未上架明细 r_sale_notshelf_info---------------------------------*/
	/**订单类型: 1-购销 2-代销 3-购销整批结算 4-购销实销实结*/
	public static final String SALENOTSHELFINFO_BUSINESSMODEL_1 = "1";
	public static final String SALENOTSHELFINFO_BUSINESSMODEL_2 = "2";
	public static final String SALENOTSHELFINFO_BUSINESSMODEL_3 = "3";
	public static final String SALENOTSHELFINFO_BUSINESSMODEL_4 = "4";	
	public static ArrayList<DerpBasic> saleNotshelfInfo_businessModelList = new ArrayList<DerpBasic>();
	/**(财务经销存)采购入库明细r_finance_warehouse_details---------------------------------*/
	/**订单类型: 1-采购入库 2-采购退货出库 3-调整SD*/
	public static final String FINANCEWAREHOUSEDETAILS_ORDERTYPE_1 = "1";
	public static final String FINANCEWAREHOUSEDETAILS_ORDERTYPE_2 = "2";
	public static final String FINANCEWAREHOUSEDETAILS_ORDERTYPE_3 = "3";
	public static ArrayList<DerpBasic> financeWarehouseDetails_orderTypeList = new ArrayList<DerpBasic>();
	
	/**(财务经销存)采购入库明细r_finance_sd_warehouse_details---------------------------------*/
	/**订单类型: 1-采购SD，2-采购退SD，3-调整SD*/
	public static final String FINANCESDWAREHOUSEDETAILS_ORDERTYPE_1 = "1";
	public static final String FINANCESDWAREHOUSEDETAILS_ORDERTYPE_2 = "2";
	public static final String FINANCESDWAREHOUSEDETAILS_ORDERTYPE_3 = "3";
	public static ArrayList<DerpBasic> financeSdWarehouseDetails_orderTypeList = new ArrayList<DerpBasic>();
	
	/**标准成本单价预警配置表r_settlement_price_warnconfig-------------------------*/
	/**状态:1启用  0禁用*/
	public static final String SETTLEMENTPRICEWARNCONFIG_STATUS_0= "0";
	public static final String SETTLEMENTPRICEWARNCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> settlementPriceWarnconfig_statusList = new ArrayList<DerpBasic>();
		
	/**(业务经销存)来货残次明细 r_bu_business_in_bad_details-------------------------*/
	/**单据类型：1-采购入库 2-调拨入库 3-退货入库 4-上架入库单*/
	public static final String BUBUSINESSINBADDETAILS_ORDERTYPE_1 = "1";
	public static final String BUBUSINESSINBADDETAILS_ORDERTYPE_2 = "2";
	public static final String BUBUSINESSINBADDETAILS_ORDERTYPE_3 = "3";
	public static final String BUBUSINESSINBADDETAILS_ORDERTYPE_4 = "4";
	public static ArrayList<DerpBasic> buBusinessInBadDetails_orderTypeList = new ArrayList<DerpBasic>();
	
	/**(业务经销存)出库残次明细r_bu_business_out_bad_details-------------------------*/
	/**单据类型：1-销售出库 2-调拨出库 3-销售退货出库 4-采购退货出库 5.采购执行,6 领料单*/
	public static final String BUBUSINESSOUTBADDETAILS_ORDERTYPE_1 = "1";
	public static final String BUBUSINESSOUTBADDETAILS_ORDERTYPE_2 = "2";
	public static final String BUBUSINESSOUTBADDETAILS_ORDERTYPE_3 = "3";
	public static final String BUBUSINESSOUTBADDETAILS_ORDERTYPE_4 = "4";
	public static final String BUBUSINESSOUTBADDETAILS_ORDERTYPE_5 = "5";
	public static final String BUBUSINESSOUTBADDETAILS_ORDERTYPE_6 = "6";
	public static ArrayList<DerpBasic> buBusinessOutBadDetails_orderTypeList = new ArrayList<DerpBasic>();
	
	/**SKU单价预警表r_sku_price_warn-------------------------*/
	/**预警类型 1-新品预警维护  2-波动预警*/
	public static final String SKUPRICEWARN_WARNTYPE_1= "1";
	public static final String SKUPRICEWARN_WARNTYPE_2= "2";
	public static ArrayList<DerpBasic> skuPriceWarn_warnTypeList = new ArrayList<DerpBasic>();

	/**业财自核表r_business_finance_automatic_verification-------------------------*/
	/**校验结果: 1-已对平 0-未对平*/
	public static final String BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_1= "1";
	public static final String BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0= "0";
	public static ArrayList<DerpBasic> businessFinanceAutomaticVerification_statusList = new ArrayList<DerpBasic>();

	/**任务状态 1-生成中 2-已生成 3-生成失败 4-已确认*/
	public static final String SETTLEMENTBILL_STATE_1 = "1";
	public static final String SETTLEMENTBILL_STATE_2 = "2";
	public static final String SETTLEMENTBILL_STATE_3 = "3";
	public static final String SETTLEMENTBILL_STATE_4 = "4";
	public static ArrayList<DerpBasic> settlementBill_stateList = new ArrayList<DerpBasic>();
	
	/**结算账单明细表r_settlement_bill_item-------------------------*/
	/**数据类型：1-仓内费用 2-快递费 3-取消订单服务费 4-综合税金
	 * 5-一线进口清关费 6-转关费 7-调拨费 8-退运费 9-堆存费
	 **/
	public static final String SETTLEMENTBILLITEM_DATETYPE_1 = "1";
	public static final String SETTLEMENTBILLITEM_DATETYPE_2 = "2";
	public static final String SETTLEMENTBILLITEM_DATETYPE_3 = "3";
	public static final String SETTLEMENTBILLITEM_DATETYPE_4 = "4";
	public static final String SETTLEMENTBILLITEM_DATETYPE_5 = "5";
	public static final String SETTLEMENTBILLITEM_DATETYPE_6 = "6";
	public static final String SETTLEMENTBILLITEM_DATETYPE_7 = "7";
	public static final String SETTLEMENTBILLITEM_DATETYPE_8 = "8";
	public static final String SETTLEMENTBILLITEM_DATETYPE_9 = "9";
	public static ArrayList<DerpBasic> settlementBillItem_dateTypeList = new ArrayList<DerpBasic>();

	/**事业部结算账单分账异常明细  r_bu_split_bill_abnormal_detail-------------------------*/
	/**异常原因：1-找不到订单号 2-找不到商品毛重 3-找不到订单事业部信息 4-找不到对应商品货号
	 * 5-找不到对应仓库 6-找不到事业部库存信息 7-存在多个事业部库存信息  8-找到多条订单号对应订单*/
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_1 = "1";
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_2 = "2";
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_3 = "3";
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_4 = "4";
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_5 = "5";
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_6 = "6";
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_7 = "7";
	public static final String BUSPLITBILLABNORMALDETAIL_REASON_8 = "8";
	public static ArrayList<DerpBasic> buSplitBillAbnormalDetail_reasonList = new ArrayList<DerpBasic>();

	/**结算配置表r_settlement_config-------------------------*/
	/**项目名称 JSXM0001-快递服务费、JSXM0002-其他费用、JSXM0003-税费
	 * JSXM0004-入仓费用、JSXM0005-出仓费用、JSXM0006-堆存费*/
	public static final String SETTLEMENTCONFIG_PROJECT_JSXM0001 = "JSXM0001";
	public static final String SETTLEMENTCONFIG_PROJECT_JSXM0002 = "JSXM0002";
	public static final String SETTLEMENTCONFIG_PROJECT_JSXM0003 = "JSXM0003";
	public static final String SETTLEMENTCONFIG_PROJECT_JSXM0004 = "JSXM0004";
	public static final String SETTLEMENTCONFIG_PROJECT_JSXM0005 = "JSXM0005";
	public static final String SETTLEMENTCONFIG_PROJECT_JSXM0006 = "JSXM0006";
	public static ArrayList<DerpBasic> settlementConfig_projectList = new ArrayList<DerpBasic>();
	
	/**销售计划表r_sale_target 1-按销售类型计划 2-按平台计划 3-按店铺计划*/
	public static final String SALE_TARGET_TYPE_1 = "1" ;
	public static final String SALE_TARGET_TYPE_2 = "2" ;
	public static final String SALE_TARGET_TYPE_3 = "3" ;
	public static ArrayList<DerpBasic> sale_target_typeList = new ArrayList<DerpBasic>();

	/**销售数据表 r_sale_data*/
	/**销售类型：1-购销A 2-购销B 3-一件代发 4-POP 5-代销 6-采销执行*/
	public static final String SALE_DATA_ORDERTYPE_1 = "1" ;
	public static final String SALE_DATA_ORDERTYPE_2 = "2" ;
	public static final String SALE_DATA_ORDERTYPE_3 = "3" ;
	public static final String SALE_DATA_ORDERTYPE_4 = "4" ;
	public static final String SALE_DATA_ORDERTYPE_5 = "5" ;
	public static final String SALE_DATA_ORDERTYPE_6 = "6" ;
	public static ArrayList<DerpBasic> sale_data_orderTypeList = new ArrayList<DerpBasic>();
	/**客户是否为内部公司 1-是 0-否*/
	public static final String SALE_DATA_INNERMERCHANTTYPE_1 = "1" ;
	public static final String SALE_DATA_INNERMERCHANTTYPE_0 = "0" ;
	public static ArrayList<DerpBasic> sale_data_innerMerchantTypeList = new ArrayList<DerpBasic>();

	/**渠道类型  1-To C   2-To B*/
	public static final String SALE_DATA_CHANNEL_TYPE_1 = "To C";
	public static final String SALE_DATA_CHANNEL_TYPE_2 = "To B";
	public static ArrayList<DerpBasic> sale_data_channelTypeList = new ArrayList<DerpBasic>();

	static{
	
	/**(业务经销存)累计销售在途明细表r_business_add_sale_noshelf_details---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	businessAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_1,"代销订单"));
	businessAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_2,"购销订单"));
	businessAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_3,"购销整批结算"));
	businessAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSADDSALENOSHELFDETAILS_ORDERTYPE_4,"购销实销实结"));

	/**(业务经分销)本期减少销售在途明细表r_business_decrease_sale_noshelf---------------------------------*/
		/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
		businessDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSDECREASESALENOSHELF_ORDERTYPE_1, "代销订单"));
		businessDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSDECREASESALENOSHELF_ORDERTYPE_2, "购销订单"));
		businessDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSDECREASESALENOSHELF_ORDERTYPE_3, "购销整批结算"));
		businessDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.BUSINESSDECREASESALENOSHELF_ORDERTYPE_4, "购销实销实结"));

		/**文件任务表r_file_task---------------------------------*/
		/**任务类型 YWJXC-进销存汇总报表 CWJXC-财务进销存报表 SXCW-刷新财务VIPHXMXB-唯品PO核销报表明细表 DSDD-电商订单导出 */
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_YWJXC, "进销存汇总报表"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_CWJXC, "财务进销存报表"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SXCW, "刷新财务"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_VIPHX, "唯品PO核销报表明细表"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SYBCWJXC, "事业部财务经销存"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SDSYBCWJXC, "SD事业部财务经销存"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SYBYWJXC, "事业部进销存汇总报表"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SXSYBCW, "刷新事业部财务经销存"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SXZHD, "刷新自核对"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_ZKTS, "在库天数报表"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZZ, "事业部财务总账"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SYBCWZGYS, "事业部财务暂估应收"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SYBCWLX, "事业部财务利息经销存"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEM, "toc暂估收入明细导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOST, "toc暂估费用明细导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_DSDD, "电商订单导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPITEMTOTAL, "toc暂估收入累计暂估导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOCTEMPCOSTTOTAL, "toc暂估费用累计暂估导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOCPLATFORMTEMPCOSTTOTAL, "toc平台暂估费用单导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOCSETTLEMENT, "toc应收账单导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOC_MONTHLY_RECEIVE, "toc暂估应收月结导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_TOC_MONTHLY_COST, "toc暂估费用导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_PAYMENT_BILL, "应付账单导出"));
		fileTask_taskTypeList.add(new DerpBasic(DERP_REPORT.FILETASK_TASKTYPE_SYBCW_MZCYCBDC, "美赞成本差异导出"));

		

		/**任务状态 1-待执行 2-执行中 3-已完成 4-失败*/
		fileTask_stateList.add(new DerpBasic(DERP_REPORT.FILETASK_STATE_1, "待执行"));
		fileTask_stateList.add(new DerpBasic(DERP_REPORT.FILETASK_STATE_2, "执行中"));
		fileTask_stateList.add(new DerpBasic(DERP_REPORT.FILETASK_STATE_3, "已完成"));
		fileTask_stateList.add(new DerpBasic(DERP_REPORT.FILETASK_STATE_4, "失败"));

		/**任务模块 1-报表 2-order */
		fileTask_moduleList.add(new DerpBasic(DERP_REPORT.FILETASK_MODULE_1, "报表"));
		fileTask_moduleList.add(new DerpBasic(DERP_REPORT.FILETASK_MODULE_2, "业务"));

		/**(财务经分销)累计销售在途明细表r_finance_add_sale_noshelf_details---------------------------------*/
		/**订单类型: 1-代销订单 2-购销订单  3-购销整批结算 4-购销实销实结*/
		financeAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_1, "代销订单"));
		financeAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_2, "购销订单"));
		financeAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_3, "购销整批结算"));
		financeAddSaleNoshelfDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEADDSALENOSHELFDETAILS_ORDERTYPE_4, "购销实销实结"));

		/**(财务经分销)本期减少销售在途r_finance_decrease_sale_noshelf---------------------------------*/
		/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	financeDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEDECREASESALENOSHELF_ORDERTYPE_1,"代销订单"));
	financeDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEDECREASESALENOSHELF_ORDERTYPE_2,"购销订单"));
	financeDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEDECREASESALENOSHELF_ORDERTYPE_3,"购销整批结算"));
	financeDecreaseSaleNoshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEDECREASESALENOSHELF_ORDERTYPE_4,"购销实销实结"));

	/**财务进销存报表r_finance_inventory_summary---------------------------------*/
	/**状态 029-未关账 030-已关账*/
	financeInventorySummary_statusList.add(new DerpBasic(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_029,"未关账"));
	financeInventorySummary_statusList.add(new DerpBasic(DERP_REPORT.FINANCEINVENTORYSUMMARY_STATUS_030,"已关账"));

	/**(财务经销存)销售残损明细表r_finance_sale_damaged---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	financeSaleDamaged_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALEDAMAGED_ORDERTYPE_1,"代销订单"));
	financeSaleDamaged_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALEDAMAGED_ORDERTYPE_2,"购销订单"));
	financeSaleDamaged_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALEDAMAGED_ORDERTYPE_3,"购销整批结算"));
	financeSaleDamaged_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALEDAMAGED_ORDERTYPE_4,"购销实销实结"));

	/**(财务经分销)销售未上架r_finance_sale_notshelf---------------------------------*/
	/**订单类型: 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结*/
	financeSaleNotshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALENOTSHELF_ORDERTYPE_1,"代销订单"));
	financeSaleNotshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALENOTSHELF_ORDERTYPE_2,"购销订单"));
	financeSaleNotshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALENOTSHELF_ORDERTYPE_3,"购销整批结算"));
	financeSaleNotshelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALENOTSHELF_ORDERTYPE_4,"购销实销实结"));
	
	/**(财务经销存)采购残损明细表r_finance_purchase_damaged-------------------------*/
	/**残损类型：1来货残次、2来货短缺*/
	financePurchaseDamaged_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEPURCHASEDAMAGED_ORDERTYPE_1,"来货残次"));
	financePurchaseDamaged_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEPURCHASEDAMAGED_ORDERTYPE_2,"来货短缺"));

	/**(财务经分销)销售已经上架r_finance_sale_shelf---------------------------------*/
	/**订单类型: 1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单 5-电商订单退货 6-购销退货 7-账单出入库单(调减) 8-账单出入库单(调增) 9-购销整批结算 10-购销实销实结 11-库位调整单调增 12-库位调整单调减 13-采销执行,14 电商订单退款*/
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_1,"代销订单"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_2,"购销订单"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_3,"电商订单"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_4,"销售出库代销订单"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_5,"电商订单退货"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_6,"购销退货"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_7,"账单出入库单(调减)"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_8,"账单出入库单(调增)"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_9,"购销整批结算"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_10,"购销实销实结"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_11,"库位调整单调增 "));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_12,"库位调整单调减"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_13,"采销执行"));
	financeSaleShelf_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_14,"电商订单退款"));
	
		

	/**(财务经分销)盘盈盘亏明细表r_finance_takes_stock_results---------------------------------*/
	/**调整类型: 1-盘盈 2-盘亏*/
	financeTakesStockResults_typeList.add(new DerpBasic(DERP_REPORT.FINANCETAKESSTOCKRESULTS_TYPE_1,"盘盈"));
	financeTakesStockResults_typeList.add(new DerpBasic(DERP_REPORT.FINANCETAKESSTOCKRESULTS_TYPE_2,"盘亏"));

	/**商品在库天数明细表r_in_warehouse_details---------------------------------*/
	/**状态: 01-已匹配 00-未匹配*/
	inWarehouseDetails_statusList.add(new DerpBasic(DERP_REPORT.INWAREHOUSEDETAILS_STATUS_01,"已匹配"));
	inWarehouseDetails_statusList.add(new DerpBasic(DERP_REPORT.INWAREHOUSEDETAILS_STATUS_00,"未匹配"));

	/**存货跌价准备报表r_inventory_falling_price_reserves---------------------------------*/
	/**效期区间 1:0≤X<1/10 ; 2: 1/10≤X<1/5 ; 3: 1/5≤X<1/3 ; 4: 1/3≤X<1/2 ; 5: 1/2≤X<2/3 ; 6: 2/3以上 ; 7:过期品(为负) ; 8: 残次品*/
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1,"0 ＜X≤1/10"));
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2,"1/10＜X≤1/5"));
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3,"1/5＜X≤1/3"));
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4,"1/3＜X≤1/2"));
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_5,"1/2＜X≤2/3"));
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_6,"2/3以上"));
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7,"过期品"));
	fallingPrice_effectiveIntervalList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_8,"残次品"));

	/**首页效期预警图标：效期区间 1:1/10效期 ; 2: 1/5效期 ; 3: 1/3效期 ; 4: 1/2效期 ; 5: 2/3效期 ; 6: 2/3以上效期 ; 7:过期品 ; 8: 残次品*/
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_1,"1/10效期"));
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_2,"1/5效期"));
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_3,"1/3效期"));
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_4,"1/2效期"));
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_5,"2/3效期"));
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_6,"2/3以上效期"));
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_7,"过期品"));
	fallingPrice_effectiveIntervalLabelList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_EFFECTIVEINTERVAL_8,"残次品"));
	
	/**剩余效期占比(财务逻辑)1:1/10 ; 2: 1/5 ; 3: 1/3; 4: 1/2 ; 5: 1/2及以上 ; 7:过期品(为负) ; 8: 残次品*/
	fallingPrice_financialSurplusproportionList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_1,"1/10效期"));
	fallingPrice_financialSurplusproportionList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_2,"1/5效期"));
	fallingPrice_financialSurplusproportionList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_3,"1/3效期"));
	fallingPrice_financialSurplusproportionList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_4,"1/2效期"));
	fallingPrice_financialSurplusproportionList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_5,"1/2及以上效期"));
	fallingPrice_financialSurplusproportionList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_7,"过期品"));
	fallingPrice_financialSurplusproportionList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_FINANCIAL_SURPLUSPROPORTION_8,"残次品"));

	/**库存类型1-好品，0-坏品*/
	fallingPrice_initinventoryTypeList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_INITINVENTORY_TYPE_0,"好品")) ;
	fallingPrice_initinventoryTypeList.add(new DerpBasic(DERP_REPORT.FALLINGPRICE_INITINVENTORY_TYPE_1,"坏品")) ;

	//状态-1-在库库存 2-采购在途 3-销售在途 4-调拨在途
	inverntory_statusList.add(new DerpBasic(DERP_REPORT.INVERNTORYSTATUS_1,"在库库存"));
	inverntory_statusList.add(new DerpBasic(DERP_REPORT.INVERNTORYSTATUS_2,"采购在途"));
	inverntory_statusList.add(new DerpBasic(DERP_REPORT.INVERNTORYSTATUS_3,"销售在途"));
	inverntory_statusList.add(new DerpBasic(DERP_REPORT.INVERNTORYSTATUS_4,"调拨在途"));

	/**结算价格r_settlement_price---------------------------------*/
	/**是否组合品  1-是 0-否*/
	settlementPrice_isGroupList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICE_ISGROUP_1,"是"));
	settlementPrice_isGroupList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICE_ISGROUP_0,"否"));

	/**审核状态*/
	settlementPrice_statusList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICE_STATUS_001,"待审核")) ;
	settlementPrice_statusList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICE_STATUS_013,"待提交")) ;
	settlementPrice_statusList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICE_STATUS_021,"已作废")) ;
	settlementPrice_statusList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICE_STATUS_032,"已生效")) ;
	settlementPrice_statusList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICE_STATUS_033,"审核不通过")) ;
	
	/**唯品库存调整明细r_vip_adjustment_inventory_details---------------------------------*/
	/**业务模式  4-唯品红冲、5-国检抽样、6-唯品报废*/
	vipAdjustmentIDetails_modelList.add(new DerpBasic(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_4,"唯品红冲"));
	vipAdjustmentIDetails_modelList.add(new DerpBasic(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_5,"国检抽样"));
	vipAdjustmentIDetails_modelList.add(new DerpBasic(DERP_REPORT.VIPADJUSTMENTIDETAILS_MODEL_6,"唯品报废"));

	/**唯品PO账单核销表r_vip_po_bill_verification---------------------------------*/
	/**完结状态 0-未完结，1-已完结*/
	vipPoBillVerification_statusList.add(new DerpBasic(DERP_REPORT.VIPPOBILLVERIFICATION_STATUS_0,"未完结"));
	vipPoBillVerification_statusList.add(new DerpBasic(DERP_REPORT.VIPPOBILLVERIFICATION_STATUS_1,"已完结"));

	/**唯品账单明细r_vip_bill_details---------------------------------*/
	/**账单类型: 00-销售明细、01-库存买断、02-库存盘亏、03-报废、04-库存盘盈、05-国检抽样*/
	vipBillDetails_billTypeList.add(new DerpBasic(DERP_REPORT.VIPBILLDETAILS_BILLTYPE_00,"销售明细"));
	vipBillDetails_billTypeList.add(new DerpBasic(DERP_REPORT.VIPBILLDETAILS_BILLTYPE_01,"库存买断"));
	vipBillDetails_billTypeList.add(new DerpBasic(DERP_REPORT.VIPBILLDETAILS_BILLTYPE_02,"库存盘亏"));
	vipBillDetails_billTypeList.add(new DerpBasic(DERP_REPORT.VIPBILLDETAILS_BILLTYPE_03,"报废"));
	vipBillDetails_billTypeList.add(new DerpBasic(DERP_REPORT.VIPBILLDETAILS_BILLTYPE_04,"库存盘盈"));
	vipBillDetails_billTypeList.add(new DerpBasic(DERP_REPORT.VIPBILLDETAILS_BILLTYPE_05,"国检抽样"));

	/**唯品盘点结果明细r_vip_takes_stock_results_details---------------------------------*/
	/**调整类型  1-盘盈 2-盘亏*/
	vipTRDetails_takesStockResultTypeList.add(new DerpBasic(DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_1,"盘盈"));
	vipTRDetails_takesStockResultTypeList.add(new DerpBasic(DERP_REPORT.VIPTRDETAILS_TAKESSTOCKRESULTTYPE_2,"盘亏"));
	
	/**唯品调拨明细r_vip_takes_stock_results_details---------------------------------*/
	/**调拨类型  1-入库 2-出库*/
	vipTransDetails_transferTypeList.add(new DerpBasic(DERP_REPORT.VIPTRDETAILS_TRANSFER_TYPE_1,"调拨入库"));
	vipTransDetails_transferTypeList.add(new DerpBasic(DERP_REPORT.VIPTRDETAILS_TRANSFER_TYPE_2,"调拨出库"));

	/**报表删除表体明细分类明细1.来源库存加减明细 2.批次库存,3,采购订单表体 4,销售订单表体,5调拨订单表体,6销售退货订单表体/表头*/
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_1,"库存加减明细表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_2,"批次库存表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_3,"采购订单表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_4,"销售订单表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_5,"调拨订单表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_6,"销售退货订单表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_7,"商家仓库关联关系"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_13,"预售单表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_14,"事业部移库单表头表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_15,"库位调整单表头表体"));
	delReportDatasItemList.add(new DerpBasic(DERP_REPORT.DEL_REPORT_DATAS_ITEM_18,"领料单表体"));

	/**首页—电商订单统计表r_index_order_statistics---------------------------------*/
	/**统计维度 1-店铺销售总量 2-品牌销售总量*/
	indexOrderStatistics_statisticalDimensionList.add(new DerpBasic(DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_1, "店铺销售总量"));
	indexOrderStatistics_statisticalDimensionList.add(new DerpBasic(DERP_REPORT.INDEXORDERSTATISTICS_STATISTICALDIMENSION_2, "品牌销售总量"));
    
	/**唯品自动核验  账单类型 00-销售明细、01-库存买断、02库存盘亏、03报废、04库存盘盈、05国检抽样、06唯品红冲*/
	vipAutoVeri_BillTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_00, "销售明细"));
	vipAutoVeri_BillTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_01, "库存买断"));
	vipAutoVeri_BillTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_02, "库存盘亏"));
	vipAutoVeri_BillTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_03, "报废"));
	vipAutoVeri_BillTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_04, "库存盘盈"));
	vipAutoVeri_BillTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_05, "国检抽样"));
	vipAutoVeri_BillTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_06, "唯品红冲"));
	
	/**唯品自动核验  系统单据类型 00-销售出库单、02库存盘亏、03报废、04库存盘盈、05国检抽样、06唯品红冲*/
	vipAutoVeri_orderTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_00, "销售出库单"));
	vipAutoVeri_orderTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_02, "盘亏"));
	vipAutoVeri_orderTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_03, "报废"));
	vipAutoVeri_orderTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_04, "盘盈"));
	vipAutoVeri_orderTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_05, "国检抽样"));
	vipAutoVeri_orderTypeList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_BILL_TYPE_06, "唯品红冲"));
	
	/**唯品自动核验 校验结果1-已对平、0-未对平*/
	vipAutoVeri_verificationResultList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_0, "未对平"));
	vipAutoVeri_verificationResultList.add(new DerpBasic(DERP_REPORT.VIP_AUTO_VERI_VERIFICATION_RESULT_1, "已对平"));
	
	/**云集结算账单是否使用: 0-未使用 1-已使用*/
	yjAccountData_isUsedList.add(new DerpBasic(DERP_ORDER.YJ_ACCOUNT_DATA_ISUSED_0,"未使用"));
	yjAccountData_isUsedList.add(new DerpBasic(DERP_ORDER.YJ_ACCOUNT_DATA_ISUSED_1,"已使用"));
	
	/**自动检验任务表r_automatic_check_task---------------------------------*/
	/**任务类型  1:POP流水核对 2:仓库流水核对*/
	automaticCheckTask_taskTypeList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_1,"POP流水核对"));
	automaticCheckTask_taskTypeList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_2,"仓库流水核对"));
	automaticCheckTask_taskTypeList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_TASKTYPE_3,"POP金额核对"));
	
	/**核对结果 0:未对平 1:已对平 2:未标记*/
	automaticCheckTask_checkResultList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_CHECKRESULT_0,"未对平"));
	automaticCheckTask_checkResultList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_CHECKRESULT_1,"已对平"));
	automaticCheckTask_checkResultList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_CHECKRESULT_2,"未标记"));
	
	/**处理状态 1:处理中 2:已完成*/
	automaticCheckTask_stateList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_STATE_1,"待处理"));
	automaticCheckTask_stateList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_STATE_2,"处理中"));
	automaticCheckTask_stateList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_STATE_3,"已完成"));
	automaticCheckTask_stateList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_STATE_4,"处理失败"));

	/**数据源 1：GSS报表 2：菜鸟后台*/
	automaticCheckTask_dataSourceList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_DATASOURCE_1,"GSS报表"));
	automaticCheckTask_dataSourceList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_DATASOURCE_2,"菜鸟后台"));
	
	/**是否标记过 0-否，1-是*/
	automaticCheckTask_isMarkList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_ISMARK_0,"否"));
	automaticCheckTask_isMarkList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_ISMARK_1,"是"));

	/**差异金额是否调整（0-否，1-是）*/
	automaticCheckTask_isAdjustList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_ISADJUST_0,"否"));
	automaticCheckTask_isAdjustList.add(new DerpBasic(DERP_REPORT.AUTOMATICCHECKTASK_ISADJUST_1,"是"));
	
	/**销售未上架明细 r_sale_notshelf_info---------------------------------*/
	/**订单类型: 1-购销 2-代销 3-购销整批结算 4-购销实销实结*/
	saleNotshelfInfo_businessModelList.add(new DerpBasic(DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_1,"购销"));
	saleNotshelfInfo_businessModelList.add(new DerpBasic(DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_2,"代销"));
	saleNotshelfInfo_businessModelList.add(new DerpBasic(DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_3,"购销整批结算"));
	saleNotshelfInfo_businessModelList.add(new DerpBasic(DERP_REPORT.SALENOTSHELFINFO_BUSINESSMODEL_4,"购销实销实结"));

	/**(财务经销存)采购入库明细r_finance_warehouse_details---------------------------------*/
	/**订单类型: 1-采购入库 2-采购退货出库*/
	financeWarehouseDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_1,"采购入库"));
	financeWarehouseDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_2,"采购退货出库"));
	financeWarehouseDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCEWAREHOUSEDETAILS_ORDERTYPE_3,"调整SD"));
	/**(财务经销存)采购入库明细r_finance_sd_warehouse_details---------------------------------*/
	/**订单类型: 1-采购SD，2-采购退SD，3-调整SD*/
	financeSdWarehouseDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESDWAREHOUSEDETAILS_ORDERTYPE_1,"采购SD"));
	financeSdWarehouseDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESDWAREHOUSEDETAILS_ORDERTYPE_2,"采购退SD"));
	financeSdWarehouseDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.FINANCESDWAREHOUSEDETAILS_ORDERTYPE_3,"调整SD"));

	/**标准成本单价预警配置表r_settlement_price_warnconfig---------------------------------*/
	/**状态:1启用  0禁用*/
	settlementPriceWarnconfig_statusList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICEWARNCONFIG_STATUS_0,"禁用"));
	settlementPriceWarnconfig_statusList.add(new DerpBasic(DERP_REPORT.SETTLEMENTPRICEWARNCONFIG_STATUS_1,"启用"));
	
	/**(业务经销存)来货残次明细 r_bu_business_in_bad_details-------------------------*/
	/**(业务经销存)来货残次明细 单据类型：1-采购入库 2-调拨入库 3-退货入库 4-上架入库单*/
	buBusinessInBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_1,"采购入库"));
	buBusinessInBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_2,"调拨入库"));
	buBusinessInBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_3,"退货入库"));
	buBusinessInBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSINBADDETAILS_ORDERTYPE_4,"上架入库单"));
	
	/**(业务经销存)出库残次明细r_bu_business_out_bad_details-------------------------*/
	/**单据类型：1-销售出库 2-调拨出库 3-销售退货出库 4-采购退货出库*/
	buBusinessOutBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_1,"销售出库"));
	buBusinessOutBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_2,"调拨出库"));
	buBusinessOutBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_3,"销售退货出库"));
	buBusinessOutBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_4,"采购退货出库"));
	buBusinessOutBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_5,"采购执行"));
	buBusinessOutBadDetails_orderTypeList.add(new DerpBasic(DERP_REPORT.BUBUSINESSOUTBADDETAILS_ORDERTYPE_6,"领料单"));

	/**SKU单价预警表r_sku_price_warn-------------------------*/
	/**预警类型 1-新品预警维护  2-波动预警*/
	skuPriceWarn_warnTypeList.add(new DerpBasic(DERP_REPORT.SKUPRICEWARN_WARNTYPE_1,"新品预警维护"));
	skuPriceWarn_warnTypeList.add(new DerpBasic(DERP_REPORT.SKUPRICEWARN_WARNTYPE_2,"波动预警"));

	/**业财自核表r_business_finance_automatic_verification-------------------------*/
	/**校验结果: 1-已对平 0-未对平*/
	businessFinanceAutomaticVerification_statusList.add(new DerpBasic(BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0, "未对平"));
	businessFinanceAutomaticVerification_statusList.add(new DerpBasic(BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_1, "已对平"));
	
	/**账单状态 1-生成中 2-已生成 3-生成失败 4-已确认*/
	settlementBill_stateList.add(new DerpBasic(DERP_REPORT.SETTLEMENTBILL_STATE_1,"生成中"));
	settlementBill_stateList.add(new DerpBasic(DERP_REPORT.SETTLEMENTBILL_STATE_2,"已生成 "));
	settlementBill_stateList.add(new DerpBasic(DERP_REPORT.SETTLEMENTBILL_STATE_3,"生成失败"));
	settlementBill_stateList.add(new DerpBasic(DERP_REPORT.SETTLEMENTBILL_STATE_4,"已确认"));

	/**结算账单明细表r_settlement_bill_item-------------------------*/
	/**数据类型：1-仓内费用 2-快递费 3-取消订单服务费 4-综合税金
	 * 5-一线进口清关费 6-转关费 7-调拨费 8-退运费 9-堆存费
	 **/
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_1, "仓内费用"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_2, "快递费"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_3, "取消订单服务费"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_4, "综合税金"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_5, "一线进口清关费"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_6, "转关费"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_7, "调拨费"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_8, "退运费"));
	settlementBillItem_dateTypeList.add(new DerpBasic(SETTLEMENTBILLITEM_DATETYPE_9, "堆存费"));

	/**事业部结算账单分账异常明细 r_bu_split_bill_abnormal_detail-------------------------*/
	/**异常原因：1-找不到订单号 2-找不到商品毛重 3-找不到订单事业部信息 4-找不到对应商品货号
	 * 5-找不到对应仓库  6-找不到事业部库存信息 7-存在多个事业部库存信息 8-找到多条订单号对应订单*/
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_1, "找不到订单号"));
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_2, "找不到商品毛重"));
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_3, "找不到订单事业部信息"));
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_4, "找不到对应商品货号"));
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_5, "找不到对应仓库"));
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_6, "找不到事业部库存信息"));
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_7, "存在多个事业部库存信息"));
	buSplitBillAbnormalDetail_reasonList.add(new DerpBasic(BUSPLITBILLABNORMALDETAIL_REASON_8, "找到多条订单号对应订单"));

	/**结算配置表r_settlement_config-------------------------*/
	/**项目名称 JSXM0001-快递服务费、JSXM0002-其他费用、JSXM0003-税费
	 * JSXM0004-入仓费用、JSXM0005-出仓费用、JSXM0006-堆存费*/
	settlementConfig_projectList.add(new DerpBasic(SETTLEMENTCONFIG_PROJECT_JSXM0001, "快递服务费"));
	settlementConfig_projectList.add(new DerpBasic(SETTLEMENTCONFIG_PROJECT_JSXM0002, "其他费用"));
	settlementConfig_projectList.add(new DerpBasic(SETTLEMENTCONFIG_PROJECT_JSXM0003, "税费"));
	settlementConfig_projectList.add(new DerpBasic(SETTLEMENTCONFIG_PROJECT_JSXM0004, "入仓费用"));
	settlementConfig_projectList.add(new DerpBasic(SETTLEMENTCONFIG_PROJECT_JSXM0005, "出仓费用"));
	settlementConfig_projectList.add(new DerpBasic(SETTLEMENTCONFIG_PROJECT_JSXM0006, "堆存费"));

	/**销售目标r_sale_target---------------------------------*/
	/**1-按销售类型计划 2-按平台计划 3-按店铺计划*/
	sale_target_typeList.add(new DerpBasic(DERP_REPORT.SALE_TARGET_TYPE_1,"销售类型"));
	sale_target_typeList.add(new DerpBasic(DERP_REPORT.SALE_TARGET_TYPE_2,"平台计划"));
	sale_target_typeList.add(new DerpBasic(DERP_REPORT.SALE_TARGET_TYPE_3,"店铺计划"));

	/**销售数据表 r_sale_data*/
	/**销售类型：1-购销A 2-购销B 3-一件代发 4-POP 5-代销 6-采销执行*/
	sale_data_orderTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_ORDERTYPE_1,"购销A"));
	sale_data_orderTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_ORDERTYPE_2,"购销B"));
	sale_data_orderTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_ORDERTYPE_3,"一件代发"));
	sale_data_orderTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_ORDERTYPE_4,"POP"));
	sale_data_orderTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_ORDERTYPE_5,"代销"));
	sale_data_orderTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_ORDERTYPE_6,"采销执行"));
	/**客户是否为内部公司 1-是 0-否*/
	sale_data_innerMerchantTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_INNERMERCHANTTYPE_1,"是"));
	sale_data_innerMerchantTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_INNERMERCHANTTYPE_0,"否"));

	sale_data_channelTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_CHANNEL_TYPE_1, "To C"));
	sale_data_channelTypeList.add(new DerpBasic(DERP_REPORT.SALE_DATA_CHANNEL_TYPE_2, "To B"));
	}
    
	/**根据key获取中文
	 * @param list 集合
	 * @param key 常量值
	 * */
	public static String getLabelByKey(List<DerpBasic> list,Object key){
		   for (DerpBasic item : list) {
				if(item.getKey().equals(key)){
					return item.getValue();
				}
			}
			return ""; 
	}
	/**获取常量集合
	 * @param listName 集合名称
	 */
	public static ArrayList<DerpBasic> getConstantListByName(String listName){
		   ArrayList<DerpBasic> list = null;
		   try{
			   Field[] fields = DERP_REPORT.class.getDeclaredFields();
		       for(Field field:fields){
		          if(field.getName().equals(listName)){
		        	 list = (ArrayList<DerpBasic>) field.get(new DERP_REPORT());
		          }
		       }
		   }catch(Exception e){}
		   return list;
	}
}
