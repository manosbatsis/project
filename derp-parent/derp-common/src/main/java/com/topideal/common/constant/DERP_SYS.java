package com.topideal.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***主服务
 *  表字段状态/类型常量文件
 * */
public class DERP_SYS{

	/**OA 币种转换码表 ----------------------------------*/
	public final static Map<String, String> OA_CURRENCY_MAP = new HashMap<String, String>();
	static {
		OA_CURRENCY_MAP.put("HKD", "110");// 港币
		OA_CURRENCY_MAP.put("JPY", "116");// 日本元
		OA_CURRENCY_MAP.put("MOP", "121");// 澳门元
		OA_CURRENCY_MAP.put("PHP", "129");// 菲律宾比索
		OA_CURRENCY_MAP.put("SGD", "132");// 新加坡元
		OA_CURRENCY_MAP.put("KRW", "133");// 韩国圆
		OA_CURRENCY_MAP.put("THB", "136");// 泰国铢
		OA_CURRENCY_MAP.put("CNY", "142");// 人民币
		OA_CURRENCY_MAP.put("EUR", "300");// 欧元
		OA_CURRENCY_MAP.put("DKK", "302");// 丹麦克朗
		OA_CURRENCY_MAP.put("GBP", "303");// 英镑
		OA_CURRENCY_MAP.put("DEM", "304");// 德国马克
		OA_CURRENCY_MAP.put("FRF", "305");// 法国法郎
		OA_CURRENCY_MAP.put("ITL", "307");// 意大利里拉
		OA_CURRENCY_MAP.put("ESP", "312");// 西班牙比赛塔
		OA_CURRENCY_MAP.put("ATS", "315");// 奥地利先令
		OA_CURRENCY_MAP.put("FIM", "318");// 芬兰马克
		OA_CURRENCY_MAP.put("NOK", "326");// 挪威克朗
		OA_CURRENCY_MAP.put("SEK", "330");// 瑞典克朗
		OA_CURRENCY_MAP.put("CHF", "331");// 瑞士法郎
		OA_CURRENCY_MAP.put("CAD", "501");// 加拿大元
		OA_CURRENCY_MAP.put("USD", "502");// 美元
		OA_CURRENCY_MAP.put("AUD", "601");// 澳大利亚元
		OA_CURRENCY_MAP.put("NZD", "609");// 新西兰元
	}

	/**采购管理中的模板类目 ----------------------------------*/
	public static final String TEMPLATE_DEPOT_CATEGORY_TYPE = "DEPOT";
	public static final String TEMPLATE_BUSINESS_UNIT_CATEGORY_TYPE = "BUSINESS_UNIT";
	public static final String TEMPLATE_COUNTRY_ORIGIN_CATEGORY_TYPE = "COUNTRY_ORIGIN";
	public static final String TEMPLATE_MERCHANDISE_CAT_CATEGORY_TYPE = "MERCHANDISE_CAT";
	public static final String TEMPLATE_UNIT_CATEGORY_TYPE = "UNIT";
	public static final String TEMPLATE_CUSTOMSAREA_CATEGORY_TYPE = "CUSTOMSAREA";
	public static final String TEMPLATE_BRAND_TYPE = "BRAND";
	public static final String TEMPLATE_PACKTYPE_CATEGORY_TYPE = "PACKTYPE";
	public static final ArrayList<DerpBasic> templateExplainCategoryList = new ArrayList<>();

	/**固定成本价盘维护st_fixed_cost_price---------------------------------*/
	/**状态 0-待审核, 1-已审核*/
	public static final String FIXED_COST_PRICE_STATUS_0 = "0";
	public static final String FIXED_COST_PRICE_STATUS_1 = "1";
	public static ArrayList<DerpBasic> fixedCostPrice_statusList = new ArrayList<DerpBasic>();

	/**商家st_merchant_info---------------------------------*/
	/**是否代理 0-否 1-是*/
	public static final String MERCHANTINFO_ISPROXY_0 = "0";
	public static final String MERCHANTINFO_ISPROXY_1 = "1";
	public static ArrayList<DerpBasic> merchantInfo_isProxyList = new ArrayList<DerpBasic>();

	public static final String MERCHANTINFO_ISBINDUSER_0 = "0";
	public static final String MERCHANTINFO_ISBINDUSER_1 = "1";
	public static ArrayList<DerpBasic> merchantInfo_isBindUserList = new ArrayList<DerpBasic>();

	/**核算单价 1-标准成本单价 2-月末加权单价*/
	public static final String MERCHANTINFO_ACCOUNTPRICE_1 = "1";
	public static final String MERCHANTINFO_ACCOUNTPRICE_2 = "2";
	public static ArrayList<DerpBasic> merchantInfo_accountPriceList = new ArrayList<DerpBasic>();

	/*注册地类型 1、境内；2、境外*/
	public static final String MERCHANTINFO_REGISTEREDTYPE_1 = "1";
	public static final String MERCHANTINFO_REGISTEREDTYPE_2 = "2";
	public static ArrayList<DerpBasic> merchantInfo_registeredTypeList = new ArrayList<DerpBasic>();
	/**状态(1启用,0禁用)*/
	public static final String MERCHANTINFO_STATUS_0 = "0";
	public static final String MERCHANTINFO_STATUS_1 = "1";
	public static ArrayList<DerpBasic> merchantInfo_statusList = new ArrayList<DerpBasic>();
	
	


	/**仓库类别st_depot_info------------------------------------*/
	/**仓库类别 a-保税仓，b-备查库，c-海外仓，d-中转仓,e-暂存区，f-销毁区 g-内贸仓*/
	public static final String DEPOTINFO_TYPE_A = "a";
	public static final String DEPOTINFO_TYPE_B = "b";
	public static final String DEPOTINFO_TYPE_C = "c";
	public static final String DEPOTINFO_TYPE_D = "d";
	public static final String DEPOTINFO_TYPE_E = "e";
	public static final String DEPOTINFO_TYPE_F = "f";
	public static final String DEPOTINFO_TYPE_G = "g";
	public static ArrayList<DerpBasic> depotInfo_typeList = new ArrayList<DerpBasic>();

	/**仓库类型  1-仓库(BBC),2-场站(BC),3-其他*/
	public static final String DEPOTINFO_DEPOTTYPE_1 = "1";
	public static final String DEPOTINFO_DEPOTTYPE_2 = "2";
	public static final String DEPOTINFO_DEPOTTYPE_3 = "3";
	public static ArrayList<DerpBasic> depotInfo_depotTypeList = new ArrayList<DerpBasic>();

	/**是否是代销仓 0-否,1-是*/
	public static final String DEPOTINFO_ISTOPBOOKS_0 = "0";
	public static final String DEPOTINFO_ISTOPBOOKS_1 = "1";
	public static ArrayList<DerpBasic> depotInfo_isTopBooksList = new ArrayList<DerpBasic>();

	/**状态 0-禁用 1-启用*/
	public static final String DEPOTINFO_STATUS_0 = "0";
	public static final String DEPOTINFO_STATUS_1 = "1";
	public static final String DEPOTINFO_STATUS_006 = "006";
	public static ArrayList<DerpBasic> depotInfo_statusList = new ArrayList<DerpBasic>();

	/**入仓下推指令appkey 1-商家key 2-关联商家key*/
	public static final String DEPOTINFO_ISMERCHANT_1 = "1";
	public static final String DEPOTINFO_ISMERCHANT_2 = "2";
	public static ArrayList<DerpBasic> depotInfo_isMerchantList = new ArrayList<DerpBasic>();

	/**批次效期强校验：0-否 1-是 2-入库强校验/出库弱校验 */
	public static final String DEPOTINFO_BATCHVALIDATION_0 = "0";
	public static final String DEPOTINFO_BATCHVALIDATION_1 = "1";
	public static final String DEPOTINFO_BATCHVALIDATION_2 = "2";
	public static ArrayList<DerpBasic> depotInfo_batchValidationList = new ArrayList<DerpBasic>();

	/**是否代客管理仓库： 1-是 0-否*/
	public static final String DEPOTINFO_IS_VALET_MANAGE_0 = "0";
	public static final String DEPOTINFO_IS_VALET_MANAGE_1 = "1";
	public static ArrayList<DerpBasic> depotInfo_isValetManageList = new ArrayList<DerpBasic>();


	/**ISV仓库类型 01：卓志-合作外仓；02：卓志-合作仓；03：卓志-卓志仓；04：卓志-海外仓； 05：卓志-外部仓*/
	public static final String DEPOTINFO_ISVDEPOTTYPE_01 = "01";
	public static final String DEPOTINFO_ISVDEPOTTYPE_02 = "02";
	public static final String DEPOTINFO_ISVDEPOTTYPE_03 = "03";
	public static final String DEPOTINFO_ISVDEPOTTYPE_04 = "04";
	public static final String DEPOTINFO_ISVDEPOTTYPE_05 = "05";
	public static ArrayList<DerpBasic> depotInfo_ISVDepotTypeList = new ArrayList<DerpBasic>();

	/**进出接口指令：0-否 1-是 */
	public static final String DEPOTMERCHANTREL_ISINOUTINSTRUCTION_0 = "0";
	public static final String DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1 = "1";
	public static ArrayList<DerpBasic> depotMerchantRel_isInOutInstructionList = new ArrayList<DerpBasic>();

	/**统计存货跌价：0-否 1-是 */
	public static final String DEPOTMERCHANTREL_ISINVERTORYFALLINGPRICE_0 = "0";
	public static final String DEPOTMERCHANTREL_ISINVERTORYFALLINGPRICE_1 = "1";
	public static ArrayList<DerpBasic> depotMerchantRel_isInvertoryFallingPriceList = new ArrayList<DerpBasic>();

	/**选品限制 1-仅备案商品 2-仅外仓统一码 3-无限制 */
	public static final String DEPOTMERCHANTREL_PRODUCTRESTRICTION_1 = "1";
	public static final String DEPOTMERCHANTREL_PRODUCTRESTRICTION_2 = "2";
	public static final String DEPOTMERCHANTREL_PRODUCTRESTRICTION_3 = "3";
	public static ArrayList<DerpBasic> depotMerchantRel_productRestrictionList = new ArrayList<DerpBasic>();

	/**已入定出 ：0-否 1-是 */
	public static final String DEPOTMERCHANTREL_ISINDEPENDOUT_0 = "0";
	public static final String DEPOTMERCHANTREL_ISINDEPENDOUT_1 = "1";
	public static ArrayList<DerpBasic> depotMerchantRel_isInDependOutList = new ArrayList<DerpBasic>();

	/**已出定入 ：0-否 1-是 */
	public static final String DEPOTMERCHANTREL_ISOUTDEPENDIN_0 = "0";
	public static final String DEPOTMERCHANTREL_ISOUTDEPENDIN_1 = "1";
	public static ArrayList<DerpBasic> depotMerchantRel_isOutDependInList = new ArrayList<DerpBasic>();

	/**对外接口配置st_api_external_config---------------------------------*/
	/**启用状态 0-已禁用 1-已启用*/
	public static final String APIEXTERNALCONFIG_STATUS_0 = "0";
	public static final String APIEXTERNALCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> apiExternalConfig_statusList = new ArrayList<DerpBasic>();

	/**api密钥配置st_api_secret_config---------------------------------*/
	/**启用状态 0-已禁用 1-已启用*/
	public static final String APISECRETCONFIG_STATUS_0 = "0";
	public static final String APISECRETCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> apiSecretConfig_statusList = new ArrayList<DerpBasic>();

	/**汇率管理st_exchange_rate---------------------------------*/
	/**(即将废弃新功能勿使用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑*/
	public static final String EXCHANGERATE_CURRENCYCODE_CNY = "CNY";
	public static final String EXCHANGERATE_CURRENCYCODE_AUD = "AUD";
	public static final String EXCHANGERATE_CURRENCYCODE_JPY = "JPY";
	public static final String EXCHANGERATE_CURRENCYCODE_USD = "USD";
	public static final String EXCHANGERATE_CURRENCYCODE_HKD = "HKD";
	public static final String EXCHANGERATE_CURRENCYCODE_EUR = "EUR";
	public static final String EXCHANGERATE_CURRENCYCODE_GBP = "GBP";
	public static ArrayList<DerpBasic> exchangeRate_currencyCodeList = new ArrayList<DerpBasic>();
	/**数据来源 WGJ-外管局  DBS-星展银行 BOC-中国银行 SGCJ-手工创建 NAV-NAV*/
	public static final String EXCHANGERATE_DATASOURCE_WGJ = "WGJ";
	public static final String EXCHANGERATE_DATASOURCE_DBS = "DBS";
	public static final String EXCHANGERATE_DATASOURCE_BOC = "BOC";
	public static final String EXCHANGERATE_DATASOURCE_SGCJ = "SGCJ";
	public static final String EXCHANGERATE_DATASOURCE_NAV = "NAV";
	public static final String EXCHANGERATE_DATASOURCE_SYS = "SYS";
	public static ArrayList<DerpBasic> exchangeRate_dataSourceList = new ArrayList<DerpBasic>();

	/**汇率配置st_exchange_rate_config---------------------------------*/
	/**数据来源 WGJ-外管局  DBS-星展银行  BOC-中国银行 NAV-NAV*/
	public static final String EXCHANGERATECONFIG_DATASOURCE_WGJ = "WGJ";
	public static final String EXCHANGERATECONFIG_DATASOURCE_DBS = "DBS";
	public static final String EXCHANGERATECONFIG_DATASOURCE_BOC = "BOC";
	public static final String EXCHANGERATECONFIG_DATASOURCE_NAV = "NAV";
	public static ArrayList<DerpBasic> exchangeRateConfig_dataSourceList = new ArrayList<DerpBasic>();

	/**商品分类st_merchandise_cat---------------------------------*/
	/**分类级别 0-一级类目 1-二级类目*/
	public static final String MERCHANDISECAT_LEVEL_0 = "0";
	public static final String MERCHANDISECAT_LEVEL_1 = "1";
	public static ArrayList<DerpBasic> merchandiseCat_levelList = new ArrayList<DerpBasic>();

	/**权限信息st_permission_info---------------------------------*/
	/**是否启用 0-禁用 1-启用*/
	public static final String PERMISSIONINFO_ISENABLED_0 = "0";
	public static final String PERMISSIONINFO_ISENABLED_1 = "1";
	public static ArrayList<DerpBasic> permissionInfo_isEnabledList = new ArrayList<DerpBasic>();

	/**权限类型 1-菜单 2-按钮*/
	public static final String PERMISSIONINFO_TYPE_1 = "1";
	public static final String PERMISSIONINFO_TYPE_2 = "2";
	public static ArrayList<DerpBasic> permissionInfo_typeList = new ArrayList<DerpBasic>();

	/**模块 1-主服务-main 2-业务-order 3-仓储-storage 4-库存-inventory 5-报表-report*/
	public static final String PERMISSIONINFO_MODULE_1 = "1";
	public static final String PERMISSIONINFO_MODULE_2 = "2";
	public static final String PERMISSIONINFO_MODULE_3 = "3";
	public static final String PERMISSIONINFO_MODULE_4 = "4";
	public static final String PERMISSIONINFO_MODULE_5 = "5";
	public static ArrayList<DerpBasic> permissionInfo_moduleList = new ArrayList<DerpBasic>();


	/**爬虫配置st_reptile_config---------------------------------*/
	/**是否代理 0-商家 1-代理商家*/
	public static final String REPTILECONFIG_ISPROXY_0 = "0";
	public static final String REPTILECONFIG_ISPROXY_1 = "1";
	public static ArrayList<DerpBasic> reptileConfig_isProxyList = new ArrayList<DerpBasic>();

	/**角色信息st_role_info---------------------------------*/
	/**是否删除 0-不删除（默认）  1-删除*/
	public static final String ROLEINFO_DELETED_0 = "0";
	public static final String ROLEINFO_DELETED_1 = "1";
	public static ArrayList<DerpBasic> roleInfo_deletedList = new ArrayList<DerpBasic>();

	/**用户信息st_user_info---------------------------------*/
	/**性别  m-男 f-女*/
	public static final String USERINFO_SEX_M = "m";
	public static final String USERINFO_SEX_F = "f";
	public static ArrayList<DerpBasic> userInfo_sexList = new ArrayList<DerpBasic>();

	/**用户类型  1 平台用户  2 商家（超管理）  3 商家用户*/
	public static final String USERINFO_USERTYPE_1 = "1";
	public static final String USERINFO_USERTYPE_2 = "2";
	public static final String USERINFO_USERTYPE_3 = "3";
	public static ArrayList<DerpBasic> userInfo_userTypeList = new ArrayList<DerpBasic>();

	/**是否禁用  0-启用(默认)  1-禁用*/
	public static final String USERINFO_DISABLE_0 = "0";
	public static final String USERINFO_DISABLE_1 = "1";
	public static ArrayList<DerpBasic> userInfo_disableList = new ArrayList<DerpBasic>();

	/**是否删除 0-不删除（默认）  1-删除*/
	public static final String USERINFO_DETELED_0 = "0";
	public static final String USERINFO_DETELED_1 = "1";
	public static ArrayList<DerpBasic> userInfo_deteledList = new ArrayList<DerpBasic>();

	/**账号类型 0-后台管理员 1-普通账号*/
	public static final String USERINFO_ACCOUNTTYPE_0 = "0";
	public static final String USERINFO_ACCOUNTTYPE_1 = "1";
	public static ArrayList<DerpBasic> userInfo_accountTypeList = new ArrayList<DerpBasic>();

	/**批次效期强校验明细st_batch_validation_info---------------------------------*/
	/**批次效期强校验：1-是 0-否 2-入库强校验/出库弱校验*/
	public static final String BATCHVALIDATION_0 = "0";
	public static final String BATCHVALIDATION_1 = "1";
	public static final String BATCHVALIDATION_2 = "2";
	public static ArrayList<DerpBasic> batchValidationList = new ArrayList<DerpBasic>();

	/**标准条码管理st_commbarcode---------------------------------*/
	/**维护状态 0-未维护 1-已维护*/
	public static final String COMMBARCODE_MAINTAINSTATUS_0 = "0";
	public static final String COMMBARCODE_MAINTAINSTATUS_1 = "1";
	public static ArrayList<DerpBasic> commbarcode_maintainStatusList = new ArrayList<DerpBasic>();

	/**客户信息st_customer_info---------------------------------*/
	/**是否内部客户1-是 2-否*/
	public static final String CUSTOMERINFO_TYPE_1 = "1";
	public static final String CUSTOMERINFO_TYPE_2 = "2";
	public static ArrayList<DerpBasic> customerInfo_typeList = new ArrayList<DerpBasic>();

	/**渠道分类  1-2C平台 2-线下2B 3-线上2B*/
	public static final String CUSTOMERINFO_CHANNELCLASSIFY_1 = "1";
	public static final String CUSTOMERINFO_CHANNELCLASSIFY_2 = "2";
	public static final String CUSTOMERINFO_CHANNELCLASSIFY_3 = "3";
	public static ArrayList<DerpBasic> customerInfo_channelClassifyList = new ArrayList<DerpBasic>();

	/**状态 1-使用中 0-已禁用 */
	public static final String CUSTOMERINFO_STATUS_0 = "0";
	public static final String CUSTOMERINFO_STATUS_1 = "1";
	public static ArrayList<DerpBasic> customerInfo_statusList = new ArrayList<DerpBasic>();
	/**是否启用采购价格管理 1-启用 0-不启用*/
	public static final String CUSTOMERINFO_PURCHASE_PRICEMANADE_0 = "0";
	public static final String CUSTOMERINFO_PURCHASE_PRICEMANADE_1 = "1";
	public static ArrayList<DerpBasic> customerInfo_purchasePriceManageList = new ArrayList<DerpBasic>();

	/**账期0:预售货款,1:信用账期-7天,2:信用账期-15天,3:信用账期-30天,4:信用账期-40天,5:信用账期-45天,6:信用账期-50天,7:信用账期-60天,8:信用账期-90天,9信用账期-90天以 */
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_0 = "0";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_1 = "1";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_2 = "2";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_3 = "3";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_4 = "4";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_5 = "5";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_6 = "6";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_7 = "7";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_8 = "8";
	public static final String CUSTOMERINFO_ACCOUNTPERIOD_9 = "9";
	public static ArrayList<DerpBasic> customerInfo_accountPeriodList = new ArrayList<DerpBasic>();

	/**结算方式 1-预付 2-到付 3-月结*/
	public static final String CUSTOMERINFO_SETTLEMENTMODE_1 = "1";
	public static final String CUSTOMERINFO_SETTLEMENTMODE_2 = "2";
	public static final String CUSTOMERINFO_SETTLEMENTMODE_3 = "3";
	public static ArrayList<DerpBasic> customerInfo_settlementModeList = new ArrayList<DerpBasic>();

	/**类型  1-客户  2-供应商*/
	public static final String CUSTOMERINFO_CUSTYPE_1 = "1";
	public static final String CUSTOMERINFO_CUSTYPE_2 = "2";
	public static ArrayList<DerpBasic> customerInfo_cusTypeList = new ArrayList<DerpBasic>();

	/**来源  1-主数据  2-录入/导入*/
	public static final String CUSTOMERINFO_SOURCE_1 = "1";
	public static final String CUSTOMERINFO_SOURCE_2 = "2";
	public static ArrayList<DerpBasic> customerInfo_sourceList = new ArrayList<DerpBasic>();

	/**客户销售价格st_customer_sale_price---------------------------------*/
	/**是否立即生效 1-立即生效 0-不立即生效*/
	public static final String CUSTOMERINFO_IMMEDIATEEFFECT_0 = "0";
	public static final String CUSTOMERINFO_IMMEDIATEEFFECT_1 = "1";
	public static ArrayList<DerpBasic> customerInfo_immediateEffectList = new ArrayList<DerpBasic>();
	/**客户销售价格st_customer_sale_price---------------------------------*/
	/**状态 006删除,001待审核,002待提交,003已审核,004已驳回 ,005-待作废 ,007-已作废*/
	public static final String CUSTOMER_SALE_PRICE_STATUS_006 = "006";
	public static final String CUSTOMER_SALE_PRICE_STATUS_001 = "001";
	public static final String CUSTOMER_SALE_PRICE_STATUS_002 = "002";
	public static final String CUSTOMER_SALE_PRICE_STATUS_003 = "003";
	public static final String CUSTOMER_SALE_PRICE_STATUS_004 = "004";
	public static final String CUSTOMER_SALE_PRICE_STATUS_005 = "005";
	public static final String CUSTOMER_SALE_PRICE_STATUS_007 = "007";
	public static ArrayList<DerpBasic> customer_sale_price_statusList = new ArrayList<DerpBasic>();

	/**是否组合 1-是 0-否*/
	public static final String CUSTOMERINFO_ISGROUP_0 = "0";
	public static final String CUSTOMERINFO_ISGROUP_1 = "1";
	public static ArrayList<DerpBasic> customerInfo_isGroupList = new ArrayList<DerpBasic>();

	/**邮件发送配置表st_email_config---------------------------------*/
	/**发送邮件选择的账单时间单位 1-自然日, 2-工作日*/
	public static final String EMAILCONFIG_UNITTYPE_1 = "1";
	public static final String EMAILCONFIG_UNITTYPE_2 = "2";
	public static ArrayList<DerpBasic> emailConfig_unitTypeList = new ArrayList<DerpBasic>();

	/**应收邮件提醒列表 提醒类型 1:按角色 2:按用户*/
	public static final String RECEIVE_EMAILCONFIG_REMINDER_TYPE_1 = "1";
	public static final String RECEIVE_EMAILCONFIG_REMINDER_TYPE_2 = "2";
	public static ArrayList<DerpBasic> receiveEmailConfigReminder_TypeList = new ArrayList<DerpBasic>();

	/**提醒类型 1-付款提醒*/
	public static final String EMAILCONFIG_REMINDERTYPE_1 = "1";
	public static ArrayList<DerpBasic> emailConfig_reminderTypeList = new ArrayList<DerpBasic>();

	/**基准时间类型 1-发票时间*/
	public static final String EMAILCONFIG_BASETIMETYPE_1 = "1";
	public static ArrayList<DerpBasic> emailConfig_baseTimeTypeList = new ArrayList<DerpBasic>();

	/**状态 1-启用,0-禁用*/
	public static final String EMAILCONFIG_STATUS_0 = "0";
	public static final String EMAILCONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> emailConfig_statusList = new ArrayList<DerpBasic>();

	/**邮件提醒列表 业务模块 1:To B应收 2:采购 3:销售 4:采购价格管理 5:销售价格管理 6:销售赊销模块 7:：平台采购单 8:应付 9:To C应收 10: 预收管理*/
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_1 = "1";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_2 = "2";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_3 = "3";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_4 = "4";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_5 = "5";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_6 = "6";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_7 = "7";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_8 = "8";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_9 = "9";
	public static final String REMINDER_EMAILCONFIG_REMINDER_TYPE_10 = "10";
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_BuisList = new ArrayList<DerpBasic>();

	/**邮件提醒列表 操作节点类型：1：提交  2：审核 3：上架 4：核销  5：开票  6：作废审核  7：盖章发票 8：审核完毕   10：金额修改  11：金额确认 
	 * 12：收到保证金 13：放款 14：提交还款 15：收到还款 16:作废提交 17:保存 18：未创建应付
	 * 19:审核通过 20：审核驳回 21：开票待签章 22:作废审核通过 23:作废审核驳回*/
	public static final String REMINDER_EMAILCONFIG_TYPE_1 = "1";
	public static final String REMINDER_EMAILCONFIG_TYPE_2 = "2";
	public static final String REMINDER_EMAILCONFIG_TYPE_3 = "3";
	public static final String REMINDER_EMAILCONFIG_TYPE_4 = "4";
	public static final String REMINDER_EMAILCONFIG_TYPE_5 = "5";
	public static final String REMINDER_EMAILCONFIG_TYPE_6 = "6";
	public static final String REMINDER_EMAILCONFIG_TYPE_7 = "7";
	public static final String REMINDER_EMAILCONFIG_TYPE_8 = "8";
	//public static final String REMINDER_EMAILCONFIG_TYPE_9 = "9";//作废完成
	public static final String REMINDER_EMAILCONFIG_TYPE_10 = "10";
	public static final String REMINDER_EMAILCONFIG_TYPE_11 = "11";
	public static final String REMINDER_EMAILCONFIG_TYPE_12 = "12";
	public static final String REMINDER_EMAILCONFIG_TYPE_13 = "13";
	public static final String REMINDER_EMAILCONFIG_TYPE_14 = "14";
	public static final String REMINDER_EMAILCONFIG_TYPE_15 = "15";
	public static final String REMINDER_EMAILCONFIG_TYPE_16 = "16";
	public static final String REMINDER_EMAILCONFIG_TYPE_17 = "17";
	public static final String REMINDER_EMAILCONFIG_TYPE_18 = "18";
	public static final String REMINDER_EMAILCONFIG_TYPE_19 = "19";
	public static final String REMINDER_EMAILCONFIG_TYPE_20 = "20";
	public static final String REMINDER_EMAILCONFIG_TYPE_21 = "21";
	public static final String REMINDER_EMAILCONFIG_TYPE_22 = "22";
	public static final String REMINDER_EMAILCONFIG_TYPE_23 = "23";
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_AllTypeList = new ArrayList<DerpBasic>();
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_ReceviceTypeList = new ArrayList<DerpBasic>();//应收
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_SaleTypeList = new ArrayList<DerpBasic>();//销售
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_PurchaseTypeList = new ArrayList<DerpBasic>();//采购
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_PurchasePriceTypeList = new ArrayList<DerpBasic>();//采购价格管理
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_SalePriceTypeList = new ArrayList<DerpBasic>();//销售价格管理
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_SaleCreditTypeList = new ArrayList<DerpBasic>();//销售赊销管理
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_PurchaseOrderTypeList = new ArrayList<DerpBasic>();//平台采购单上架管理
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_PaymentOrderTypeList = new ArrayList<DerpBasic>();//应付
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_AdvanceBillTypeList = new ArrayList<DerpBasic>();//预收账单

	


	/**邮件提醒列表 提醒对象(按用户) 1：保存人 2：提交人 3：审核人 4：核销人  5：作废审核人 6：盖章发票人 （7：审核完毕人）
	 （8：作废完成人 ）9：开票人 10：金额审核人 11：金额修改人 12：上架人 13：作废提交人*/
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_1 = "1";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2 = "2";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3 = "3";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_4 = "4";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_5 = "5";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_6 = "6";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_7 = "7";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_8 = "8";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_9 = "9";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_10 = "10";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_11 = "11";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_12 = "12";
	public static final String REMINDER_EMAILCONFIG_REMINDER_BillTYPE_13 = "13";
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_AllUserTypeList = new ArrayList<DerpBasic>();
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_BillTypeList = new ArrayList<DerpBasic>();//应收
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_UserSaleTypeList = new ArrayList<DerpBasic>();//销售
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_UserPurchaseTypeList = new ArrayList<DerpBasic>();//采购
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_UserPurchasePriceTypeList = new ArrayList<DerpBasic>();//采购价格管理
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_UserSalePriceTypeList = new ArrayList<DerpBasic>();//销售价格管理
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_UserSaleCreditTypeList = new ArrayList<DerpBasic>();//平台采购单上架管理
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_UserPaymentOrderTypeList = new ArrayList<DerpBasic>();//应付
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_UserAdvanceBillTypeList = new ArrayList<DerpBasic>();//预收

	/**邮件提醒列表 提醒渠道  1：邮件 2 ：企业微信*/
	public static final String REMINDER_EMAILCONFIG_CHANNEL_TYPE_1 = "1";
	public static final String REMINDER_EMAILCONFIG_CHANNEL_TYPE_2 = "2";
	public static ArrayList<DerpBasic> reminderEmailConfigReminder_ChannelList = new ArrayList<DerpBasic>();

	/**商品信息st_merchandise_info---------------------------------*/
	/**数据来源 2-菜鸟 1-主数据 0-录入/导入*/
	public static final String MERCHANDISEINFO_SOURCE_0 = "0";
	public static final String MERCHANDISEINFO_SOURCE_1 = "1";
	public static final String MERCHANDISEINFO_SOURCE_2 = "2";
	public static ArrayList<DerpBasic> merchandiseInfo_sourceList = new ArrayList<DerpBasic>();

	/**预警类型 1-分仓预警 2-全仓预警*/
	public static final String MERCHANDISEINFO_WARNINGTYPE_1 = "1";
	public static final String MERCHANDISEINFO_WARNINGTYPE_2 = "2";
	public static ArrayList<DerpBasic> merchandiseInfo_warningTypeList = new ArrayList<DerpBasic>();

	/**是否组合 1-是，0-否*/
	public static final String MERCHANDISEINFO_ISGROUP_0 = "0";
	public static final String MERCHANDISEINFO_ISGROUP_1 = "1";
	public static ArrayList<DerpBasic> merchandiseInfo_isGroupList = new ArrayList<DerpBasic>();

	/**是否备案 1-是，0-否*/
	public static final String MERCHANDISEINFO_ISRECORD_0 = "0";
	public static final String MERCHANDISEINFO_ISRECORD_1 = "1";
	public static ArrayList<DerpBasic> merchandiseInfo_isRecordList = new ArrayList<DerpBasic>();

	/**是否自有 1-是，0-否*/
	public static final String MERCHANDISEINFO_ISSELF_0 = "0";
	public static final String MERCHANDISEINFO_ISSELF_1 = "1";
	public static ArrayList<DerpBasic> merchandiseInfo_isSelfList = new ArrayList<DerpBasic>();

	/**状态 1-启用,0-禁用*/
	public static final String MERCHANDISEINFO_STATUS_0 = "0";
	public static final String MERCHANDISEINFO_STATUS_1 = "1";
	public static ArrayList<DerpBasic> merchandiseInfo_statusList = new ArrayList<DerpBasic>();

	/**外仓统一码 0-是,1-否*/
	public static final String MERCHANDISEINFO_OUTDEPOTFLAG_0 = "0";
	public static final String MERCHANDISEINFO_OUTDEPOTFLAG_1 = "1";
	public static ArrayList<DerpBasic> merchandiseInfo_outDepotFlagList = new ArrayList<DerpBasic>();

	/**商家店铺关联表st_merchant_shop_rel---------------------------------*/
	/**状态 1-使用中 0-已禁用*/
	public static final String MERCHANTSHOPREL_STATUS_0 = "0";
	public static final String MERCHANTSHOPREL_STATUS_1 = "1";
	public static ArrayList<DerpBasic> merchantShopRel_statusList = new ArrayList<DerpBasic>();

	/**订单是否拆解 0-是 1-否*/
	public static final String MERCHANTSHOPREL_ISDISMANTLE_0 = "0";
	public static final String MERCHANTSHOPREL_ISDISMANTLE_1 = "1";
	public static ArrayList<DerpBasic> merchantShopRel_isDismantleList = new ArrayList<DerpBasic>();
	/**运营类型 001-POP 002-一件代发*/
	public static final String MERCHANTSHOPREL_SHOPTYPE_001= "001";
	public static final String MERCHANTSHOPREL_SHOPTYPE_002= "002";
	public static ArrayList<DerpBasic> merchantShopRel_shopTypeList = new ArrayList<DerpBasic>();

	/**店铺类型 DZD：单主店、WBD：外部店*/
	public static final String MERCHANTSHOPREL_STORETYPE_DZD= "DZD";
	public static final String MERCHANTSHOPREL_STORETYPE_WBD= "WBD";
	public static ArrayList<DerpBasic> merchantShopRel_storeTypeList = new ArrayList<DerpBasic>();

	/**是否同步菜鸟商品 1-是 0-否*/
	public static final String MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_0 = "0";
	public static final String MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_1 = "1";
	public static ArrayList<DerpBasic> merchantShopRel_isSycnMerchandiseList = new ArrayList<DerpBasic>();

	/**供应商商品价格表st_supplier_merchandise_price---------------------------------*/
	/**订单状态:001:待审核,003:已审核,002:待提交,004：已驳回,005:待作废，007：已作废*/
	public static final String SUPPLIER_MERCHANDISE_PRICE_STATUS_001 = "001";
	public static final String SUPPLIER_MERCHANDISE_PRICE_STATUS_002 = "002";
	public static final String SUPPLIER_MERCHANDISE_PRICE_STATUS_003 = "003";
	public static final String SUPPLIER_MERCHANDISE_PRICE_STATUS_004 = "004";
	public static final String SUPPLIER_MERCHANDISE_PRICE_STATUS_005 = "005";
	public static final String SUPPLIER_MERCHANDISE_PRICE_STATUS_007 = "007";
	public static ArrayList<DerpBasic> supplierMerchandisePrice_statusList = new ArrayList<DerpBasic>();

	/**数据来源 1-新增 2-导入 3-单据*/
	public static final String SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_1 = "1";
	public static final String SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_2 = "2";
	public static final String SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_3 = "3";
	public static ArrayList<DerpBasic> supplierMerchandisePrice_dataSourceList = new ArrayList<DerpBasic>();

	/**报价来源 1-第e仓，2-外部，3-品牌经销*/
	public static final String SUPPLIER_SOURCE_1 = "1";
	public static final String SUPPLIER_SOURCE_2 = "2";
	public static final String SUPPLIER_SOURCE_3 = "3";
	public static ArrayList<DerpBasic> supplier_sourceList = new ArrayList<DerpBasic>();

	/**报价币种 1-人民币，2-日元，3-澳元，4-美元，5-港币,  6-欧元,  7-英镑*/
	public static final String SUPPLIER_CURRENCY_1 = "1";
	public static final String SUPPLIER_CURRENCY_2 = "2";
	public static final String SUPPLIER_CURRENCY_3 = "3";
	public static final String SUPPLIER_CURRENCY_4 = "4";
	public static final String SUPPLIER_CURRENCY_5 = "5";
	public static final String SUPPLIER_CURRENCY_6 = "6";
	public static final String SUPPLIER_CURRENCY_7 = "7";
	public static ArrayList<DerpBasic> supplier_currencyList = new ArrayList<DerpBasic>();

	/**结算方式 1-预付 2-到付 3-月结*/
	public static final String SUPPLIER_SETTLEMENTMODE_1 = "1";
	public static final String SUPPLIER_SETTLEMENTMODE_2 = "2";
	public static final String SUPPLIER_SETTLEMENTMODE_3 = "3";
	public static ArrayList<DerpBasic> supplier_settlementModeList = new ArrayList<DerpBasic>();

	/**报价模式 1-阶梯报价，2-固定报价*/
	public static final String SUPPLIERMERCHANDISEPRICE_PRICEMODEL_1 = "1";
	public static final String SUPPLIERMERCHANDISEPRICE_PRICEMODEL_2 = "2";
	public static ArrayList<DerpBasic> supplierMerchandisePrice_priceModeList = new ArrayList<DerpBasic>();

	/**供应商商品价格 剩余效期*//*
	public static final String SM_PRICE_ITEM_RESIDUAL_MATURITY_E01 = "E01";
	public static final String SM_PRICE_ITEM_RESIDUAL_MATURITY_E02 = "E02";
	public static final String SM_PRICE_ITEM_RESIDUAL_MATURITY_E03 = "E03";
	public static final String SM_PRICE_ITEM_RESIDUAL_MATURITY_E04 = "E04";
	public static final String SM_PRICE_ITEM_RESIDUAL_MATURITY_E05 = "E05";
	public static ArrayList<DerpBasic> smPriceItem_ResidualMaturitylist = new ArrayList<DerpBasic>();*/


	/**公告管理 公告类型 1-上线公告 2-运维公告*/
	public static final String NOTICE_TYPE_1 = "1" ;
	public static final String NOTICE_TYPE_2 = "2" ;
	public static ArrayList<DerpBasic> notice_typelist = new ArrayList<DerpBasic>();

	/**公告管理 公告状态 001-待发布 002-已发布 006-已删除*/
	public static final String NOTICE_STATUS_001 = "001" ;
	public static final String NOTICE_STATUS_002 = "002" ;
	public static final String NOTICE_STATUS_006 = "006" ;
	public static ArrayList<DerpBasic> notice_statuslist = new ArrayList<DerpBasic>();

	/**公告用户关联 状态 0-未读  1-已读*/
	public static final String NOTICE_USER_REL_STATUS_0 = "0" ;
	public static final String NOTICE_USER_REL_STATUS_1 = "1" ;
	public static ArrayList<DerpBasic> notice_user_rel_statuslist = new ArrayList<DerpBasic>();

	/**公司事业部关联表st_merchant_bu_rel---------------------------------*/
	/**公司事业部关联表库位状态管理 0-启用 1-禁用*/
	public static final String MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0 = "0";
	public static final String MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1 = "1";
	public static ArrayList<DerpBasic> merchant_bu_rel_stock_location_managelist = new ArrayList<DerpBasic>();

	/**公司事业部关联表状态 0-禁用 1-启用*/
	public static final String MERCHANT_BU_REL_STATUS_0 = "0";
	public static final String MERCHANT_BU_REL_STATUS_1 = "1";
	public static ArrayList<DerpBasic> merchant_bu_rel_statuslist = new ArrayList<DerpBasic>();

	/**是否启用采购价格管理 1-启用 0-不启用*/
	public static final String PURCHASE_PRICE_MANAGE_1 = "1" ;
	public static final String PURCHASE_PRICE_MANAGE_0 = "0" ;
	public static ArrayList<DerpBasic> merchant_bu_rel_purchase_price_manageList = new ArrayList<DerpBasic>();

	/**是否启用销售价格管理 1-启用 0-不启用*/
	public static final String SALE_PRICE_MANAGE_1 = "1" ;
	public static final String SALE_PRICE_MANAGE_0 = "0" ;
	public static ArrayList<DerpBasic> merchant_bu_rel_sale_price_manageList = new ArrayList<DerpBasic>();

	/**采购审批方式 1-OA审批 2-经分销审批*/
	public static final String MERCHANT_BU_REL_PURCHASE_AUDIT_METHOD_1 = "1" ;
	public static final String MERCHANT_BU_REL_PURCHASE_AUDIT_METHOD_2 = "2" ;
	public static ArrayList<DerpBasic> merchant_bu_rel_purchase_audit_methodList = new ArrayList<DerpBasic>();

	/**公司店铺st_merchant_shop_shipper---------------------------------*/
	/**销售类型 01-自营 02-代发*/
	public static final String SHOP_SHIPPER_SALETYPE_01 = "01" ;
	public static final String SHOP_SHIPPER_SALETYPE_02 = "02" ;
	public static ArrayList<DerpBasic> shopShipperSaleTypeList = new ArrayList<DerpBasic>();

	/**主数据客商详情 是否供应商、客户状态 0-否 1-是*/
	public static final String CUSTOMER_MAIN_IS_0 = "0" ;
	public static final String CUSTOMER_MAIN_IS_1 = "1" ;
	public static ArrayList<DerpBasic> customerMainIsList = new ArrayList<DerpBasic>();
	

	/**主数据客商 主数据客户状态 1：有效   0：锁定*/
	public static final String CUSTOMER_MAIN_STATUS_0 = "0" ;
	public static final String CUSTOMER_MAIN_STATUS_1 = "1" ;
	public static ArrayList<DerpBasic> customerMainStatusList = new ArrayList<DerpBasic>();

	/**客商类型 01、客户，10、供应商，11、供应商&客户, 00、非客商企业 */
	public static final String CUSTOMER_MAIN_TYPE_1 = "01" ;
	public static final String CUSTOMER_MAIN_TYPE_2 = "10" ;
	public static final String CUSTOMER_MAIN_TYPE_3 = "11" ;
	public static ArrayList<DerpBasic> customerMainTypeList = new ArrayList<DerpBasic>();

	/**采购执行佣金配置st_purchase_commission---------------------------------*/
	/**配置类型  1-采购执行比例 2-公司加价比例*/
	public static final String PURCHASE_COMMISSION_CONFIGTYPE_1 = "1";
	public static final String PURCHASE_COMMISSION_CONFIGTYPE_2 = "2";
	public static ArrayList<DerpBasic> purchaseCommission_configTypeList = new ArrayList<DerpBasic>();

	/**库位类型：1、常规品 2、赠送品 3、sample---------------------------------*/
	public static final String INVEN_LOCAITON_MAPPING_TYPE_1 = "1";
	public static final String INVEN_LOCAITON_MAPPING_TYPE_2 = "2";
	public static final String INVEN_LOCAITON_MAPPING_TYPE_3 = "3";
	public static ArrayList<DerpBasic> invenlocaitonMapping_TypeList = new ArrayList<DerpBasic>();

	/**库位类型：  0未指定,1已指定  sample---------------------------------*/
	public static final String INVEN_LOCAITON_MAPPING_ISORRAPPOINT_0 = "0";
	public static final String INVEN_LOCAITON_MAPPING_ISORRAPPOINT_1 = "1";
	public static ArrayList<DerpBasic> invenlocaitonMapping_isorRappoint = new ArrayList<DerpBasic>();

	/**标准品牌 st_brand_parent--------------------------------- */
	/**上级母品牌编码 ---------------------------------*/
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE001="001";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE002="002";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE003="003";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE004="004";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE005="005";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE006="006";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE007="007";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE008="008";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE009="009";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE010="010";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE011="011";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE012="012";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE013="013";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE014="014";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE015="015";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE016="016";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE017="017";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE018="018";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE019="019";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE020="020";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE021="021";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE022="022";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE023="023";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE024="024";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE025="025";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE026="026";
	public static final String BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE9999="9999";

	public static ArrayList<DerpBasic> brandParent_superiorParentBrandCodeList = new ArrayList<DerpBasic>();
	/** 母品牌授权方 1-品牌方 2-经销商 3-无授权 **/
	public static final String BRAND_PARENT_AUTHORIZER_1 = "1";
	public static final String BRAND_PARENT_AUTHORIZER_2 = "2";
	public static final String BRAND_PARENT_AUTHORIZER_3 = "3";
	public static ArrayList<DerpBasic> brandParent_authorizerList = new ArrayList<DerpBasic>();



	/** 母品牌分类 1-跨境电商 2-内贸 **/
	public static final String BRAND_PARENT_TYPE_1 = "1";
	public static final String BRAND_PARENT_TYPE_2 = "2";
	public static ArrayList<DerpBasic> brandParent_typeList = new ArrayList<DerpBasic>();

	/**st_bu_stock_location_type_config 事业部库位类型配置表*/
	/**状态: 1启用,0禁用 ---------------------------------*/
	public static final String BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_0 = "0";
	public static final String BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1 = "1";
	public static ArrayList<DerpBasic> bu_stock_location_type_config_statusList = new ArrayList<DerpBasic>();


	/**供应商商品 st_supplier_merchandise --------------------------------- */
	/**来源:1.欧莱雅接口 ---------------------------------*/
	public static final String SUPPLIER_MERCHANDISE_SOURCE1="1";
	public static ArrayList<DerpBasic> supplierMerchandise_sourceList = new ArrayList<DerpBasic>();

	/**客户供应商 商家关系表 st_customer_merchant_rel --------------------------------- */
	/**结算类型 1:应收,2:预收 settlement_type---------------------------------*/
	public static final String SETTLEMENT_TYPE1="1";
	public static final String SETTLEMENT_TYPE2="2";
	public static ArrayList<DerpBasic> customerMerchantRel_settlementTypeList = new ArrayList<DerpBasic>();

	/**销售模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结  business_model---------------------------------*/
	public static final String BUSINESS_MODEL1="1";
	public static final String BUSINESS_MODEL2="2";
	public static final String BUSINESS_MODEL3="3";
	public static final String BUSINESS_MODEL4="4";
	public static ArrayList<DerpBasic> customerMerchantRel_businessModelList = new ArrayList<DerpBasic>();

	/**平台备案关区状态(1使用中,删除-006)---------------------------------*/
	public static final String CUSTOMS_AREA_STATUS = "1";

	/*操作日志 1:平台备案商品管理 2：采购价格管理 3：销售价格管理 4.商品列表 5.品牌管理 6.仓库列表 7.事业部库位类型 8.固定成本价盘*/
	public static final String OREARTE_SYS_LOG_1="1";
	public static final String OREARTE_SYS_LOG_2="2";
	public static final String OREARTE_SYS_LOG_3="3";
	public static final String OREARTE_SYS_LOG_4="4";
	public static final String OREARTE_SYS_LOG_5="5";
	public static final String OREARTE_SYS_LOG_6="6";
	public static final String OREARTE_SYS_LOG_7="7";
	public static final String OREARTE_SYS_LOG_8="8";
	public static ArrayList<DerpBasic> opeateSys_logList = new ArrayList<DerpBasic>();

	/**状态(1使用中,0已禁用)---------------------------------*/
	public static final String CUSTOMER_MERCHANT_REL_status0="0";
	public static final String CUSTOMER_MERCHANT_REL_status1="1";
	public static ArrayList<DerpBasic> customerMerchantRel_statusList = new ArrayList<DerpBasic>();

	/**是否启用采购价格管理 1-启用 0-不启用---------------------------------*/
	public static final String CUSTOMER_MERCHANT_REL_PURCHASE_PRICE_MANGAGE0="0";
	public static final String CUSTOMER_MERCHANT_REL_PURCHASE_PRICE_MANGAGE1="1";
	public static ArrayList<DerpBasic> customerMerchantRel_purchasePriceManageList = new ArrayList<DerpBasic>();

	/**是否启用销售价格管理 1-启用 0-不启用---------------------------------*/
	public static final String CUSTOMER_MERCHANT_REL_SALE_PRICE_MANGAGE0="0";
	public static final String CUSTOMER_MERCHANT_REL_SALE_PRICE_MANGAGE1="1";
	public static ArrayList<DerpBasic> customerMerchantRel_salePriceManageList = new ArrayList<DerpBasic>();

	/**平台备案商品st_merchandise_external_warehouse---------------------------------*/
	/**数据来源 0-录入/导入*/
	public static final String MERCHANDISEWAREHOUSE_SOURCE_0 = "0";
	public static ArrayList<DerpBasic> merchandiseWarehouse_sourceList = new ArrayList<DerpBasic>();

	/**公司/商家 卓志编码 -----------------------------------**/
	public static final String MERCHANT_TOPIDEAL_CODE_BAOXIN = "0000138";
	public static final String MERCHANT_TOPIDEAL_CODE_JIANTAI = "1000000204";
	public static final String MERCHANT_TOPIDEAL_CODE_ZHUOYE = "0000134";
	public static final String MERCHANT_TOPIDEAL_CODE_YUANSUNTAI = "1000004669";
	public static final String MERCHANT_TOPIDEAL_CODE_GUANGWANG = "1000000645";
	public static final String MERCHANT_TOPIDEAL_CODE_RUNBAI = "1000000626";
	public static final String MERCHANT_TOPIDEAL_CODE_WANDAI = "1000005377";
	public static final String MERCHANT_TOPIDEAL_CODE_XUANSHENG = "1000052958";

	/**首页-------------------------*/
	/**销售记录接口*/
	public static final String SALE_ORDER_TYPE_1 = "1";
	public static final String SALE_ORDER_TYPE_2 = "2";
	public static final String SALE_ORDER_TYPE_3 = "3";
	public static final String SALE_ORDER_TYPE_4 = "4";
	public static ArrayList<DerpBasic> saleOrderTypeList = new ArrayList<DerpBasic>();

	/**理货接口*/
	public static final String TALLY_ORDER_TYPE_1 = "1";
	public static final String TALLY_ORDER_TYPE_2 = "2";
	public static final String TALLY_ORDER_TYPE_3 = "3";
	public static ArrayList<DerpBasic> tallyOrderType = new ArrayList<DerpBasic>();

	/**上架接口*/
	public static final String SHELF_SALE_TYPE_1 = "1";
	public static final String SHELF_SALE_TYPE_2 = "2";
	public static ArrayList<DerpBasic> shelfSaleType = new ArrayList<DerpBasic>();

	static{
		/**模板说明类名 templateExplainCategoryList ----------------------------------*/
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_DEPOT_CATEGORY_TYPE, "仓库名称"));
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_BUSINESS_UNIT_CATEGORY_TYPE, "事业部"));
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_COUNTRY_ORIGIN_CATEGORY_TYPE, "原产国"));
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_MERCHANDISE_CAT_CATEGORY_TYPE, "二级类目"));
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_UNIT_CATEGORY_TYPE, "计量单位"));
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_CUSTOMSAREA_CATEGORY_TYPE, "关区名称"));
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_BRAND_TYPE, "商品品牌"));
		templateExplainCategoryList.add(new DerpBasic(DERP_SYS.TEMPLATE_PACKTYPE_CATEGORY_TYPE, "包装方式"));

		/**平台备案商品st_merchandise_external_warehouse---------------------------------*/
		/**数据来源 0-录入/导入*/
		merchandiseWarehouse_sourceList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_ISPROXY_0,"导入"));

		/**固定成本价盘维护st_fixed_cost_price---------------------------------*/
		/**状态 0-待审核, 1-已审核*/
		fixedCostPrice_statusList.add(new DerpBasic(DERP_SYS.FIXED_COST_PRICE_STATUS_0,"待审核"));
		fixedCostPrice_statusList.add(new DerpBasic(DERP_SYS.FIXED_COST_PRICE_STATUS_1,"已审核"));

		/**商家st_merchant_info---------------------------------*/
		/**是否代理*/
		merchantInfo_isProxyList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_ISPROXY_0,"否"));
		merchantInfo_isProxyList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_ISPROXY_1,"是"));
		/**是否绑定*/
		merchantInfo_isBindUserList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_ISBINDUSER_0,"否"));
		merchantInfo_isBindUserList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_ISBINDUSER_1,"是"));
		/**核算单价 1-标准成本单价 2-月末加权单价*/
		merchantInfo_accountPriceList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_1,"标准成本单价"));
		merchantInfo_accountPriceList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_ACCOUNTPRICE_2,"月末加权单价"));
		/*注册地类型 1、境内；2、境外*/
		merchantInfo_registeredTypeList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_REGISTEREDTYPE_1,"境内"));
		merchantInfo_registeredTypeList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_REGISTEREDTYPE_2,"境外"));
		/*状态(1启用,0禁用*/
		merchantInfo_statusList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_STATUS_0,"禁用"));
		merchantInfo_statusList.add(new DerpBasic(DERP_SYS.MERCHANTINFO_STATUS_1,"启用"));


		/**仓库st_depot_info------------------------------------*/
		/**仓库类别*/
		depotInfo_typeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_TYPE_A,"保税仓"));
		depotInfo_typeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_TYPE_B,"备查库"));
		depotInfo_typeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_TYPE_C,"海外仓"));
		depotInfo_typeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_TYPE_D,"中转仓"));
		depotInfo_typeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_TYPE_E,"暂存区"));
		depotInfo_typeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_TYPE_F,"销毁区"));
		depotInfo_typeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_TYPE_G,"内贸仓"));

		/**仓库类型 */
		depotInfo_depotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_DEPOTTYPE_1,"仓库(BBC)"));
		depotInfo_depotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_DEPOTTYPE_2,"场站(BC)"));
		depotInfo_depotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_DEPOTTYPE_3,"其他"));

		/**是否是代销仓*/
		depotInfo_isTopBooksList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0,"否"));
		depotInfo_isTopBooksList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISTOPBOOKS_1,"是"));

		/**状态*/
		depotInfo_statusList.add(new DerpBasic(DERP_SYS.DEPOTINFO_STATUS_0,"禁用"));
		depotInfo_statusList.add(new DerpBasic(DERP_SYS.DEPOTINFO_STATUS_1,"启用"));
		depotInfo_statusList.add(new DerpBasic(DERP_SYS.DEPOTINFO_STATUS_006,"已删除"));

		/**入仓下推指令appkey*/
		depotInfo_isMerchantList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISMERCHANT_1,"商家key"));
		depotInfo_isMerchantList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISMERCHANT_2,"关联商家key"));

		/**批次效期强校验 */
		depotInfo_batchValidationList.add(new DerpBasic(DERP_SYS.DEPOTINFO_BATCHVALIDATION_0,"否"));
		depotInfo_batchValidationList.add(new DerpBasic(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1,"是"));
		depotInfo_batchValidationList.add(new DerpBasic(DERP_SYS.DEPOTINFO_BATCHVALIDATION_2,"入库强校验/出库弱校验"));

		/**是否代客管理仓库： 1-是 0-否*/
		depotInfo_isValetManageList.add(new DerpBasic(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0,"否"));
		depotInfo_isValetManageList.add(new DerpBasic(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_1,"是"));


		/**ISV仓库类型 01：卓志-合作外仓；02：卓志-合作仓；03：卓志-卓志仓；04：卓志-海外仓； 05：卓志-外部仓*/
		depotInfo_ISVDepotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_01,"卓志-合作外仓"));
		depotInfo_ISVDepotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_02,"卓志-合作仓"));
		depotInfo_ISVDepotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_03,"卓志-卓志仓"));
		depotInfo_ISVDepotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_04,"卓志-海外仓"));
		depotInfo_ISVDepotTypeList.add(new DerpBasic(DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_05,"卓志-外部仓"));

		/**进出接口指令 */
		depotMerchantRel_isInOutInstructionList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_0,"否"));
		depotMerchantRel_isInOutInstructionList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISINOUTINSTRUCTION_1,"是"));

		/**统计存货跌价 */
		depotMerchantRel_isInvertoryFallingPriceList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISINVERTORYFALLINGPRICE_0,"否"));
		depotMerchantRel_isInvertoryFallingPriceList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISINVERTORYFALLINGPRICE_1,"是"));

		/**已入定出 */
		depotMerchantRel_isInDependOutList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_0,"否"));
		depotMerchantRel_isInDependOutList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1,"是"));

		/**已出定入 */
		depotMerchantRel_isOutDependInList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISOUTDEPENDIN_0,"否"));
		depotMerchantRel_isOutDependInList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_ISOUTDEPENDIN_1,"是"));

		/**选品限制 1-仅备案商品 2-仅外仓统一码 3-无限制 */
		depotMerchantRel_productRestrictionList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1,"仅备案商品"));
		depotMerchantRel_productRestrictionList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2,"仅外仓统一码"));
		depotMerchantRel_productRestrictionList.add(new DerpBasic(DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_3,"无限制"));

		/**对外接口配置st_api_external_config---------------------------------*/
		/**启用状态 0-已禁用 1-已启用*/
		apiExternalConfig_statusList.add(new DerpBasic(DERP_SYS.APIEXTERNALCONFIG_STATUS_0,"已禁用"));
		apiExternalConfig_statusList.add(new DerpBasic(DERP_SYS.APIEXTERNALCONFIG_STATUS_1,"已启用"));

		/**api密钥配置st_api_secret_config---------------------------------*/
		/**启用状态 0-已禁用 1-已启用*/
		apiSecretConfig_statusList.add(new DerpBasic(DERP_SYS.APISECRETCONFIG_STATUS_0,"已禁用"));
		apiSecretConfig_statusList.add(new DerpBasic(DERP_SYS.APISECRETCONFIG_STATUS_1,"已启用"));

		/**汇率管理st_exchange_rate---------------------------------*/
		/**币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑*/
		exchangeRate_currencyCodeList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY,"人民币"));
		exchangeRate_currencyCodeList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_CURRENCYCODE_AUD,"澳元"));
		exchangeRate_currencyCodeList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_CURRENCYCODE_JPY,"日元"));
		exchangeRate_currencyCodeList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_CURRENCYCODE_USD,"美元"));
		exchangeRate_currencyCodeList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_CURRENCYCODE_HKD,"港币"));
		exchangeRate_currencyCodeList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_CURRENCYCODE_EUR,"欧元"));
		exchangeRate_currencyCodeList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_CURRENCYCODE_GBP,"英镑"));

		/**数据来源 WGJ-外管局  DBS-星展银行 BOC-中国银行  SGCJ-手工创建 NAV-NAV*/
		exchangeRate_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_DATASOURCE_WGJ,"外管局"));
		exchangeRate_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_DATASOURCE_DBS,"星展银行"));
		exchangeRate_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_DATASOURCE_BOC,"中国银行"));
		exchangeRate_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_DATASOURCE_SGCJ,"手工创建"));
		exchangeRate_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_DATASOURCE_NAV,"NAV"));
		exchangeRate_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATE_DATASOURCE_SYS,"系统创建"));

		/**汇率配置表st_exchange_rate_config---------------------------------*/
		exchangeRateConfig_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATECONFIG_DATASOURCE_WGJ,"外管局"));
		exchangeRateConfig_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATECONFIG_DATASOURCE_DBS,"星展银行"));
		exchangeRateConfig_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATECONFIG_DATASOURCE_BOC,"中国银行"));
		exchangeRateConfig_dataSourceList.add(new DerpBasic(DERP_SYS.EXCHANGERATECONFIG_DATASOURCE_NAV,"NAV"));


		/**商品分类st_merchandise_cat---------------------------------*/
		/**分类级别 0-一级类目 1-二级类目*/
		merchandiseCat_levelList.add(new DerpBasic(DERP_SYS.MERCHANDISECAT_LEVEL_0,"一级类目"));
		merchandiseCat_levelList.add(new DerpBasic(DERP_SYS.MERCHANDISECAT_LEVEL_1,"二级类目"));

		/**权限信息st_permission_info---------------------------------*/
		/**是否启用 0-禁用 1-启用*/
		permissionInfo_isEnabledList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_ISENABLED_0,"禁用"));
		permissionInfo_isEnabledList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_ISENABLED_1,"启用"));

		/**权限类型 1-菜单 2-按钮*/
		permissionInfo_typeList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_TYPE_1,"菜单"));
		permissionInfo_typeList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_TYPE_2,"按钮"));

		/**模块 1-主服务-main 2-业务-order 3-仓储-storage 4-库存-inventory 5-报表-report*/
		permissionInfo_moduleList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_MODULE_1,"主服务-main"));
		permissionInfo_moduleList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_MODULE_2,"业务-order"));
		permissionInfo_moduleList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_MODULE_3,"仓储-storage"));
		permissionInfo_moduleList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_MODULE_4,"库存-inventory"));
		permissionInfo_moduleList.add(new DerpBasic(DERP_SYS.PERMISSIONINFO_MODULE_5,"报表-report"));

		/**爬虫配置st_reptile_config---------------------------------*/
		/**是否代理 0-商家 1-代理商家*/
		reptileConfig_isProxyList.add(new DerpBasic(DERP_SYS.REPTILECONFIG_ISPROXY_0,"商家"));
		reptileConfig_isProxyList.add(new DerpBasic(DERP_SYS.REPTILECONFIG_ISPROXY_1,"代理商家"));

		/**角色信息st_role_info---------------------------------*/
		/**是否删除 0-不删除（默认）  1-删除*/
		roleInfo_deletedList.add(new DerpBasic(DERP_SYS.ROLEINFO_DELETED_0,"不删除"));
		roleInfo_deletedList.add(new DerpBasic(DERP_SYS.ROLEINFO_DELETED_1,"删除"));

		/**用户信息st_user_info---------------------------------*/
		/**性别  m-男 f-女*/
		userInfo_sexList.add(new DerpBasic(DERP_SYS.USERINFO_SEX_M,"男"));
		userInfo_sexList.add(new DerpBasic(DERP_SYS.USERINFO_SEX_F,"女"));

		/**用户类型  1 平台用户  2 商家（超管理）  3 商家用户*/
		userInfo_userTypeList.add(new DerpBasic(DERP_SYS.USERINFO_USERTYPE_1,"平台用户"));
		userInfo_userTypeList.add(new DerpBasic(DERP_SYS.USERINFO_USERTYPE_2,"商家"));
		userInfo_userTypeList.add(new DerpBasic(DERP_SYS.USERINFO_USERTYPE_3,"商家用户"));

		/**是否禁用  0-启用(默认)  1-禁用*/
		userInfo_disableList.add(new DerpBasic(DERP_SYS.USERINFO_DISABLE_0,"启用"));
		userInfo_disableList.add(new DerpBasic(DERP_SYS.USERINFO_DISABLE_1,"禁用"));

		/**是否删除 0-不删除（默认）  1-删除*/
		userInfo_deteledList.add(new DerpBasic(DERP_SYS.USERINFO_DETELED_0,"不删除"));
		userInfo_deteledList.add(new DerpBasic(DERP_SYS.USERINFO_DETELED_1,"删除"));

		/**账号类型 0-后台管理员 1-普通账号*/
		userInfo_accountTypeList.add(new DerpBasic(DERP_SYS.USERINFO_ACCOUNTTYPE_0,"后台管理员"));
		userInfo_accountTypeList.add(new DerpBasic(DERP_SYS.USERINFO_ACCOUNTTYPE_1,"普通账号"));

		/**批次效期强校验明细st_batch_validation_info---------------------------------*/
		/**批次效期强校验：1-是 0-否 2-入库强校验/出库弱校验*/
		batchValidationList.add(new DerpBasic(DERP_SYS.BATCHVALIDATION_0,"否"));
		batchValidationList.add(new DerpBasic(DERP_SYS.BATCHVALIDATION_1,"是"));
		batchValidationList.add(new DerpBasic(DERP_SYS.BATCHVALIDATION_2,"入库强校验/出库弱校验"));

		/**标准条码管理st_commbarcode---------------------------------*/
		/**维护状态 0-未维护 1-已维护*/
		commbarcode_maintainStatusList.add(new DerpBasic(DERP_SYS.COMMBARCODE_MAINTAINSTATUS_0,"未维护"));
		commbarcode_maintainStatusList.add(new DerpBasic(DERP_SYS.COMMBARCODE_MAINTAINSTATUS_1,"已维护"));

		/**客户信息st_customer_info---------------------------------*/
		/**客户类型 1-内部 2-外部*/
		customerInfo_typeList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_TYPE_1,"是"));
		customerInfo_typeList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_TYPE_2,"否"));

		/**渠道分类  1-2C平台 2-线下2B 3-线上2B*/
		customerInfo_channelClassifyList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_CHANNELCLASSIFY_1,"2C平台"));
		customerInfo_channelClassifyList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_CHANNELCLASSIFY_2,"线下2B"));
		customerInfo_channelClassifyList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_CHANNELCLASSIFY_3,"线上2B"));


		/**状态 1-使用中 0-已禁用 */
		customerInfo_statusList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_STATUS_0,"已禁用"));
		customerInfo_statusList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_STATUS_1,"使用中"));
		/**是否启用采购价格管理 1-启用 0-不启用 */
		customerInfo_purchasePriceManageList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_PURCHASE_PRICEMANADE_0,"已禁用"));
		customerInfo_purchasePriceManageList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_PURCHASE_PRICEMANADE_1,"使用中"));


		/**账期0:预售货款,1:信用账期-7天,2:信用账期-15天,3:信用账期-30天,4:信用账期-40天,5:信用账期-45天,6:信用账期-50天,7:信用账期-60天,8:信用账期-90天,9信用账期-90天以 */
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_0,"预售货款"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_1,"信用账期-7天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_2,"信用账期-15天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_3,"信用账期-30天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_4,"信用账期-40天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_5,"信用账期-45天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_6,"信用账期-50天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_7,"信用账期-60天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_8,"信用账期-90天"));
		customerInfo_accountPeriodList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ACCOUNTPERIOD_9,"信用账期-90天以上"));

		/**结算方式 1-预付 2-到付 3-月结*/
		customerInfo_settlementModeList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_SETTLEMENTMODE_1,"预付"));
		customerInfo_settlementModeList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_SETTLEMENTMODE_2,"到付"));
		customerInfo_settlementModeList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_SETTLEMENTMODE_3,"月结"));

		/**类型  1-客户  2-供应商*/
		customerInfo_cusTypeList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_CUSTYPE_1,"客户"));
		customerInfo_cusTypeList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_CUSTYPE_2,"供应商"));

		/**来源  1-主数据  2-录入/导入*/
		customerInfo_sourceList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_SOURCE_1,"主数据"));
		customerInfo_sourceList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_SOURCE_2,"录入/导入"));

		/**客户销售价格st_customer_sale_price---------------------------------*/
		/**是否立即生效 1-立即生效 0-不立即生效*/
		customerInfo_immediateEffectList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_IMMEDIATEEFFECT_0,"不立即生效"));
		customerInfo_immediateEffectList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_IMMEDIATEEFFECT_1,"立即生效"));
		/**客户销售价格st_customer_sale_price---------------------------------*/
		/**状态 006删除,001待审核,002待提交,003已审核,004已驳回 ,005-待作废 ,007-已作废*/
		customer_sale_price_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_001,"待审核"));
		customer_sale_price_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_002,"待提交"));
		customer_sale_price_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_003,"已审核"));
		customer_sale_price_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_004,"已驳回"));
		customer_sale_price_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_005,"待作废"));
		customer_sale_price_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_007,"已作废"));


		//customer_sale_price_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_SALE_PRICE_STATUS_006,"删除"));
		/**是否组合 1-是 0-否*/
		customerInfo_isGroupList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ISGROUP_0,"否"));
		customerInfo_isGroupList.add(new DerpBasic(DERP_SYS.CUSTOMERINFO_ISGROUP_1,"是"));

		/**邮件发送配置表st_email_config---------------------------------*/
		/**发送邮件选择的账单时间单位 1-自然日, 2-工作日*/
		emailConfig_unitTypeList.add(new DerpBasic(DERP_SYS.EMAILCONFIG_UNITTYPE_1,"自然日"));
		emailConfig_unitTypeList.add(new DerpBasic(DERP_SYS.EMAILCONFIG_UNITTYPE_2,"工作日"));

		/**提醒类型 1-付款提醒*/
		emailConfig_reminderTypeList.add(new DerpBasic(DERP_SYS.EMAILCONFIG_REMINDERTYPE_1,"付款提醒"));

		/**基准时间类型 1-发票时间*/
		emailConfig_baseTimeTypeList.add(new DerpBasic(DERP_SYS.EMAILCONFIG_BASETIMETYPE_1,"发票时间"));

		/**状态 1-启用,0-禁用*/
		emailConfig_statusList.add(new DerpBasic(DERP_SYS.EMAILCONFIG_STATUS_0,"禁用"));
		emailConfig_statusList.add(new DerpBasic(DERP_SYS.EMAILCONFIG_STATUS_1,"启用"));

		/**应收邮件提醒列表 提醒类型 1:按角色 2:按用户*/
		receiveEmailConfigReminder_TypeList.add(new DerpBasic(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_1,"角色")) ;
		receiveEmailConfigReminder_TypeList.add(new DerpBasic(DERP_SYS.RECEIVE_EMAILCONFIG_REMINDER_TYPE_2,"用户")) ;

		/*邮件提醒列表 业务模块 1:To B应收 2:采购 3:销售 4:采购价格管理 5:销售价格管理 6:销售赊销模块 7:：平台采购单 8:应付 9:To C应收 10:预收管理*/
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_2,"采购"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_3,"销售"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_8,"应付"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_4,"采购价格管理"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_5,"销售价格管理"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_6,"销售赊销模块"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_7,"平台采购单"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_1,"To B应收"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_9,"To C应收"));
		reminderEmailConfigReminder_BuisList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_TYPE_10,"预收管理"));

		/**邮件提醒列表 操作节点（应收）：提交，审核，开票，核销，盖章发票，作废审核，作废提交、保存*/
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_17,"保存"));
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2,"审核"));
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_5,"开票"));
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4,"核销"));
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_7,"盖章发票"));
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16,"作废提交"));
		reminderEmailConfigReminder_ReceviceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6,"作废审核"));



		/**邮件提醒列表 操作节点（采购）：提交，审核，金额修改，金额确认，未创建应付*/
		reminderEmailConfigReminder_PurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_PurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2,"审核"));
		reminderEmailConfigReminder_PurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_3,"上架"));
		reminderEmailConfigReminder_PurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_10,"金额修改"));
		reminderEmailConfigReminder_PurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_11,"金额确认"));
		reminderEmailConfigReminder_PurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_18,"未创建应付"));

		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2,"审核"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_3,"上架"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4,"核销"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_5,"开票"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6,"作废审核"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_7,"盖章发票"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_8,"审核完成"));
		//reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_9,"作废完成"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_10,"金额修改"));
		reminderEmailConfigReminder_AllTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_11,"金额确认"));

		/**邮件提醒列表 操作节点（销售）：提交，审核，上架，金额修改，金额确认*/
		reminderEmailConfigReminder_SaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_SaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2,"审核"));
		reminderEmailConfigReminder_SaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_3,"上架"));
		reminderEmailConfigReminder_SaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_10,"金额修改"));
		reminderEmailConfigReminder_SaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_11,"金额确认"));

		/**邮件提醒列表 操作节点（采购价格管理）：提交，审核*/
		reminderEmailConfigReminder_PurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_PurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2,"审核"));
        reminderEmailConfigReminder_PurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6,"作废审核"));
        reminderEmailConfigReminder_PurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16,"申请作废"));

		/**邮件提醒列表 操作节点（销售价格管理）：提交，审核*/
		reminderEmailConfigReminder_SalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_SalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2,"审核"));
        reminderEmailConfigReminder_SalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6,"作废审核"));
        reminderEmailConfigReminder_SalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16,"申请作废"));

		/**邮件提醒列表 操作节点（销售赊销管理）：提交，收到保证金，放款，提交还款，收到还款*/
		reminderEmailConfigReminder_SaleCreditTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_SaleCreditTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_12,"收到保证金"));
		reminderEmailConfigReminder_SaleCreditTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_13,"放款"));
		reminderEmailConfigReminder_SaleCreditTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_14,"提交还款"));
		reminderEmailConfigReminder_SaleCreditTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_15,"收到还款"));

		/*邮件提醒列表 操作节点（平台采购管理）：*/
		reminderEmailConfigReminder_PurchaseOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_3,"上架"));

		/*邮件提醒列表 操作节点（应付）*/
		reminderEmailConfigReminder_PaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_PaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_2,"审核"));
		reminderEmailConfigReminder_PaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4,"核销"));
		reminderEmailConfigReminder_PaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16,"作废提交"));
		reminderEmailConfigReminder_PaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_6,"作废审核"));

		/*邮件提醒列表 操作节点（预收）*/
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_1,"提交"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_19,"审核通过"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_20,"审核驳回"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_21,"开票待签章"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_7,"盖章发票"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_4,"核销"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_16,"作废提交"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_22,"作废审核通过"));
		reminderEmailConfigReminder_AdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_TYPE_23,"作废审核驳回"));


		/*邮件提醒列表 提醒对象(按用户，应收) 提交人，审核人，核销人，开票人，盖章发票人，作废审核人，作废提交人*/
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_1,"保存人"));
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_9,"开票人"));
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_4,"核销人"));
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_6,"盖章发票人"));
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_5,"作废审核人"));
		reminderEmailConfigReminder_BillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_13,"作废提交人"));

		/*、邮件提醒列表 提醒对象(按用户，采购):提交人，审核人，金额审核人，金额修改人*/
		reminderEmailConfigReminder_UserPurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_UserPurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));
		reminderEmailConfigReminder_UserPurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_10,"金额审核人"));
		reminderEmailConfigReminder_UserPurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_11,"金额修改人"));
		reminderEmailConfigReminder_UserPurchaseTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_12,"上架人"));

		/*、邮件提醒列表 提醒对象(按用户，销售）提交人，审核人，上架人,金额审核人，金额修改人*/
		reminderEmailConfigReminder_UserSaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_UserSaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));
		reminderEmailConfigReminder_UserSaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_12,"上架人"));
		reminderEmailConfigReminder_UserSaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_10,"金额审核人"));
		reminderEmailConfigReminder_UserSaleTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_11,"金额修改人"));

		/*、邮件提醒列表 采购价格管理，提交人，审核人，提交作废，作废审核*/
		reminderEmailConfigReminder_UserPurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_UserPurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));
        reminderEmailConfigReminder_UserPurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_5,"作废审核人"));
        reminderEmailConfigReminder_UserPurchasePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_13,"作废提交人"));
		/*、邮件提醒列表 销售价格管理 提交人，审核人，提交作废，作废审核*/
		reminderEmailConfigReminder_UserSalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_UserSalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));
        reminderEmailConfigReminder_UserSalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_5,"作废审核人"));
        reminderEmailConfigReminder_UserSalePriceTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_13,"作废提交人"));

		/*、邮件提醒列表 销售赊销管理 提交人，审核人*/
		reminderEmailConfigReminder_UserSaleCreditTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_1,"创建人"));
		reminderEmailConfigReminder_UserSaleCreditTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		/* 邮件提醒列表  应付*/
		reminderEmailConfigReminder_UserPaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_1,"创建人"));
		reminderEmailConfigReminder_UserPaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_UserPaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));
		reminderEmailConfigReminder_UserPaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_4,"核销人"));
		reminderEmailConfigReminder_UserPaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_13,"作废提交人"));
		reminderEmailConfigReminder_UserPaymentOrderTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_5,"作废审核人"));
				
		/* 邮件提醒列表  预收*/
		reminderEmailConfigReminder_UserAdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_UserAdvanceBillTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));




		/**邮件提醒列表 提醒渠道  1：邮件 2 ：企业微信*/
		reminderEmailConfigReminder_ChannelList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_1,"邮件"));
		reminderEmailConfigReminder_ChannelList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_CHANNEL_TYPE_2,"企业微信"));

		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_1,"创建人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_2,"提交人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_3,"审核人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_4,"核销人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_5,"作废核销人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_8,"作废完成人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_6,"盖章发票人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_7,"审核完成人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_9,"开票人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_10,"金额审核人"));
		reminderEmailConfigReminder_AllUserTypeList.add(new DerpBasic(DERP_SYS.REMINDER_EMAILCONFIG_REMINDER_BillTYPE_11,"金额修改人"));


		/**商品信息st_merchandise_info---------------------------------*/
		/**数据来源 2-菜鸟 1-主数据 0-录入/导入*/
		merchandiseInfo_sourceList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_SOURCE_0,"录入/导入"));
		merchandiseInfo_sourceList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_SOURCE_1,"主数据"));
		merchandiseInfo_sourceList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_SOURCE_2,"菜鸟"));

		/**预警类型 1-分仓预警 2-全仓预警*/
		merchandiseInfo_warningTypeList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_WARNINGTYPE_1,"分仓预警"));
		merchandiseInfo_warningTypeList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_WARNINGTYPE_2,"全仓预警"));

		/**是否组合 1-是，0-否*/
		merchandiseInfo_isGroupList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_ISGROUP_0,"否"));
		merchandiseInfo_isGroupList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_ISGROUP_1,"是"));

		/**是否备案 1-是，0-否*/
		merchandiseInfo_isRecordList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_ISRECORD_0,"否"));
		merchandiseInfo_isRecordList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_ISRECORD_1,"是"));

		/**是否自有 1-是，0-否*/
		merchandiseInfo_isSelfList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_ISSELF_0,"否"));
		merchandiseInfo_isSelfList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_ISSELF_1,"是"));

		/**状态 1-启用,0-禁用*/
		merchandiseInfo_statusList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_STATUS_0,"禁用"));
		merchandiseInfo_statusList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_STATUS_1,"启用"));

		/**外仓唯一码 0-是 1-否*/
		merchandiseInfo_outDepotFlagList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0, "是"));
		merchandiseInfo_outDepotFlagList.add(new DerpBasic(DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_1, "否"));


		/**商家店铺关联表st_merchant_shop_rel---------------------------------*/
		/**状态 1-使用中 0-已禁用*/
		merchantShopRel_statusList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_STATUS_0,"已禁用"));
		merchantShopRel_statusList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_STATUS_1,"使用中"));

		/**订单是否拆解 0-是 1-否*/
		merchantShopRel_isDismantleList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_ISDISMANTLE_0,"是"));
		merchantShopRel_isDismantleList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_ISDISMANTLE_1,"否"));

		/**运营类型 001-POP 002-一件代发*/
		merchantShopRel_shopTypeList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001,"POP"));
		merchantShopRel_shopTypeList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_002,"一件代发"));

		/**店铺类型 DZD：单主店、WBD：外部店*/
		merchantShopRel_storeTypeList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD,"单主店"));
		merchantShopRel_storeTypeList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD,"外部店"));

		/**是否同步菜鸟商品 0-否 1-是*/
		merchantShopRel_isSycnMerchandiseList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_1,"是"));
		merchantShopRel_isSycnMerchandiseList.add(new DerpBasic(DERP_SYS.MERCHANTSHOPREL_IS_SYCN_MERCHANDISE_0,"否"));

		/**供应商商品价格表st_supplier_merchandise_price---------------------------------*/
		/**订单状态:001:待审核,003:已审核,002:待提交,004：已驳回，005：待作废，007：已作废*/
		supplierMerchandisePrice_statusList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_001,"待审核"));
		supplierMerchandisePrice_statusList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_003,"已审核"));
		supplierMerchandisePrice_statusList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_002,"待提交"));
		supplierMerchandisePrice_statusList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_004,"已驳回"));
		supplierMerchandisePrice_statusList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_005,"待作废"));
		supplierMerchandisePrice_statusList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_STATUS_007,"已作废"));

		/**数据来源 1-新增 2-导入 3-单据*/
		supplierMerchandisePrice_dataSourceList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_1,"新增"));
		supplierMerchandisePrice_dataSourceList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_2,"导入"));
		supplierMerchandisePrice_dataSourceList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_PRICE_DATASOURCE_3,"单据"));

		/**报价来源 1-第e仓，2-外部，3-品牌经销*/
		supplier_sourceList.add(new DerpBasic(DERP_SYS.SUPPLIER_SOURCE_1,"第e仓"));
		supplier_sourceList.add(new DerpBasic(DERP_SYS.SUPPLIER_SOURCE_2,"外部"));
		supplier_sourceList.add(new DerpBasic(DERP_SYS.SUPPLIER_SOURCE_3,"品牌经销"));

		/**报价币种 1-人民币，2-日元，3-澳元，4-美元，5-港币,  6-欧元,  7-英镑*/
		supplier_currencyList.add(new DerpBasic(DERP_SYS.SUPPLIER_CURRENCY_1,"人民币"));
		supplier_currencyList.add(new DerpBasic(DERP_SYS.SUPPLIER_CURRENCY_2,"日元"));
		supplier_currencyList.add(new DerpBasic(DERP_SYS.SUPPLIER_CURRENCY_3,"澳元"));
		supplier_currencyList.add(new DerpBasic(DERP_SYS.SUPPLIER_CURRENCY_4,"美元"));
		supplier_currencyList.add(new DerpBasic(DERP_SYS.SUPPLIER_CURRENCY_5,"港币"));
		supplier_currencyList.add(new DerpBasic(DERP_SYS.SUPPLIER_CURRENCY_6,"欧元"));
		supplier_currencyList.add(new DerpBasic(DERP_SYS.SUPPLIER_CURRENCY_7,"英镑"));

		/**结算方式 1-预付 2-到付 3-月结*/
		supplier_settlementModeList.add(new DerpBasic(DERP_SYS.SUPPLIER_SETTLEMENTMODE_1,"预付"));
		supplier_settlementModeList.add(new DerpBasic(DERP_SYS.SUPPLIER_SETTLEMENTMODE_2,"到付"));
		supplier_settlementModeList.add(new DerpBasic(DERP_SYS.SUPPLIER_SETTLEMENTMODE_3,"月结"));

		/**报价模式 1-阶梯报价，2-固定报价*/
		supplierMerchandisePrice_priceModeList.add(new DerpBasic(DERP_SYS.SUPPLIERMERCHANDISEPRICE_PRICEMODEL_1,"阶梯报价"));
		supplierMerchandisePrice_priceModeList.add(new DerpBasic(DERP_SYS.SUPPLIERMERCHANDISEPRICE_PRICEMODEL_2,"固定报价"));

		/**供应商商品价格 剩余效期E01-最新效期  E02-低于1/3 E03-低于1/2 E04-高于1/2 E05-高于2/3*/
/*	smPriceItem_ResidualMaturitylist.add(new DerpBasic(DERP_SYS.SM_PRICE_ITEM_RESIDUAL_MATURITY_E01,"最新效期"));
	smPriceItem_ResidualMaturitylist.add(new DerpBasic(DERP_SYS.SM_PRICE_ITEM_RESIDUAL_MATURITY_E02,"低于1/3"));
	smPriceItem_ResidualMaturitylist.add(new DerpBasic(DERP_SYS.SM_PRICE_ITEM_RESIDUAL_MATURITY_E03,"低于1/2"));
	smPriceItem_ResidualMaturitylist.add(new DerpBasic(DERP_SYS.SM_PRICE_ITEM_RESIDUAL_MATURITY_E04,"高于1/2"));
	smPriceItem_ResidualMaturitylist.add(new DerpBasic(DERP_SYS.SM_PRICE_ITEM_RESIDUAL_MATURITY_E05,"高于2/3"));*/

		/**公告类型*/
		notice_typelist.add(new DerpBasic(DERP_SYS.NOTICE_TYPE_1,"上线公告"));
		notice_typelist.add(new DerpBasic(DERP_SYS.NOTICE_TYPE_2,"运维公告"));

		/**公告状态*/
		notice_statuslist.add(new DerpBasic(DERP_SYS.NOTICE_STATUS_001,"待发布"));
		notice_statuslist.add(new DerpBasic(DERP_SYS.NOTICE_STATUS_002,"已发布"));
		notice_statuslist.add(new DerpBasic(DERP_SYS.NOTICE_STATUS_006,"已删除"));

		/**公告用户关联*/
		notice_user_rel_statuslist.add(new DerpBasic(DERP_SYS.NOTICE_USER_REL_STATUS_0,"未读"));
		notice_user_rel_statuslist.add(new DerpBasic(DERP_SYS.NOTICE_USER_REL_STATUS_1,"已读"));

		/**公司事业部st_merchant_bu_ref---------------------------------*/
		/**是否启用库位管理 0-启用 1-不启用*/
		merchant_bu_rel_stock_location_managelist.add(new DerpBasic(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_0,"启用"));
		merchant_bu_rel_stock_location_managelist.add(new DerpBasic(DERP_SYS.MERCHANT_BU_REL_STOCK_LOCATION_MANAGE_1,"禁用"));

		/**status*/
		merchant_bu_rel_statuslist.add(new DerpBasic(DERP_SYS.MERCHANT_BU_REL_STATUS_0,"禁用"));
		merchant_bu_rel_statuslist.add(new DerpBasic(DERP_SYS.MERCHANT_BU_REL_STATUS_1,"启用"));

		/**是否启用采购价格管理 1-启用 0-不启用*/
		merchant_bu_rel_purchase_price_manageList.add(new DerpBasic(DERP_SYS.PURCHASE_PRICE_MANAGE_1,"启用"));
		merchant_bu_rel_purchase_price_manageList.add(new DerpBasic(DERP_SYS.PURCHASE_PRICE_MANAGE_0,"禁用"));

		/**是否启用销售价格管理 1-启用 0-不启用*/
		merchant_bu_rel_sale_price_manageList.add(new DerpBasic(DERP_SYS.SALE_PRICE_MANAGE_1,"启用"));
		merchant_bu_rel_sale_price_manageList.add(new DerpBasic(DERP_SYS.SALE_PRICE_MANAGE_0,"禁用"));

		/**采购审批方式 1-OA审批 2-经分销审批*/
		merchant_bu_rel_purchase_audit_methodList.add(new DerpBasic(DERP_SYS.MERCHANT_BU_REL_PURCHASE_AUDIT_METHOD_1,"OA审批"));
		merchant_bu_rel_purchase_audit_methodList.add(new DerpBasic(DERP_SYS.MERCHANT_BU_REL_PURCHASE_AUDIT_METHOD_2,"经分销审批"));


		/**公司店铺st_merchant_shop_shipper---------------------------------*/
		/**销售类型 01-自营 02-代发*/
		shopShipperSaleTypeList.add(new DerpBasic(DERP_SYS.SHOP_SHIPPER_SALETYPE_01,"自营"));
		shopShipperSaleTypeList.add(new DerpBasic(DERP_SYS.SHOP_SHIPPER_SALETYPE_02,"代发"));

		/**主数据客商详情 1-是，0-否*/
		customerMainIsList.add(new DerpBasic(DERP_SYS.CUSTOMER_MAIN_IS_0,"否"));
		customerMainIsList.add(new DerpBasic(DERP_SYS.CUSTOMER_MAIN_IS_1,"是"));
		
		/**主数据客商 主数据客户状态 1：有效   0：锁定*/
		customerMainStatusList.add(new DerpBasic(DERP_SYS.CUSTOMER_MAIN_STATUS_0,"锁定"));
		customerMainStatusList.add(new DerpBasic(DERP_SYS.CUSTOMER_MAIN_STATUS_1,"有效"));

		/**客商类型 1、客户，2、供应商，3、供应商&客户 */
		customerMainTypeList.add(new DerpBasic(DERP_SYS.CUSTOMER_MAIN_TYPE_1,"客户"));
		customerMainTypeList.add(new DerpBasic(DERP_SYS.CUSTOMER_MAIN_TYPE_2,"供应商"));
		customerMainTypeList.add(new DerpBasic(DERP_SYS.CUSTOMER_MAIN_TYPE_3,"供应商&客户"));


		/**采购执行佣金配置st_purchase_commission---------------------------------*/
		/**配置类型  1-采购执行比例 2-公司加价比例*/
		purchaseCommission_configTypeList.add(new DerpBasic(DERP_SYS.PURCHASE_COMMISSION_CONFIGTYPE_1,"采购执行比例"));
		purchaseCommission_configTypeList.add(new DerpBasic(DERP_SYS.PURCHASE_COMMISSION_CONFIGTYPE_2,"公司加价比例"));

		/**库位类型：1、常规品 2、赠送品 3、sample---------------------------------*/
		invenlocaitonMapping_TypeList.add(new DerpBasic(DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_1, "常规品"));
		invenlocaitonMapping_TypeList.add(new DerpBasic(DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_2, "赠送品"));
		invenlocaitonMapping_TypeList.add(new DerpBasic(DERP_SYS.INVEN_LOCAITON_MAPPING_TYPE_3, "sample"));

		/**库位类型：  0未指定,1已指定  sample---------------------------------*/
		invenlocaitonMapping_isorRappoint.add(new DerpBasic(DERP_SYS.INVEN_LOCAITON_MAPPING_ISORRAPPOINT_0, "未指定"));
		invenlocaitonMapping_isorRappoint.add(new DerpBasic(DERP_SYS.INVEN_LOCAITON_MAPPING_ISORRAPPOINT_1, "已指定"));

		/**标准品牌 st_brand_parent--------------------------------- */
		/**上级母品牌编码 ---------------------------------*/
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE001, "P&G"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE002, "好奇"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE003, "箭牌"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE004, "白金盐"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE005, "枫之宝"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE006, "雀巢食品"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE007, "雀巢宠物食品"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE008, "拉杜蓝乔"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE009, "玛氏食品"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE010, "玛氏宠物食品"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE011, "拜耳"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE012, "Huxley"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE013, "BEKIND"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE014, "GRACO"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE015, "亿滋"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE016, "美赞臣"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE017, "BY-HEALTH"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE018, "博朗"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE019, "Brauer"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE020, "STC"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE021, "美赞臣（外）"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE022, "Prinker"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE023, "Mokibo"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE024, "任天堂"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE025, "OLLY"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE026, "salefino"));
		brandParent_superiorParentBrandCodeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_SUPERIOR_PARENT_BRAND_CODE9999, "其他"));



		/** 母品牌授权方 1-品牌方 2-经销商 3-无授权 **/
		brandParent_authorizerList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_AUTHORIZER_1, "品牌方"));
		brandParent_authorizerList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_AUTHORIZER_2, "经销商"));
		brandParent_authorizerList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_AUTHORIZER_3, "无授权"));


		/** 母品牌分类 1-跨境电商 2-内贸 **/
		brandParent_typeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_TYPE_1, "跨境电商"));
		brandParent_typeList.add(new DerpBasic(DERP_SYS.BRAND_PARENT_TYPE_2, "内贸"));

		/**st_bu_stock_location_type_config 事业部库位类型配置表*/
		/**状态: 1启用,0禁用 ---------------------------------*/
		bu_stock_location_type_config_statusList.add(new DerpBasic(DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_0, "禁用"));
		bu_stock_location_type_config_statusList.add(new DerpBasic(DERP_SYS.BU_STOCK_LOCATION_TYPE_CONFIG_STATUS_1, "启用"));

		/**供应商商品 st_supplier_merchandise --------------------------------- */
		/**来源:1.欧莱雅接口 ---------------------------------*/
		supplierMerchandise_sourceList.add(new DerpBasic(DERP_SYS.SUPPLIER_MERCHANDISE_SOURCE1, "欧莱雅接口"));

		/**客户供应商 商家关系表  st_customer_merchant_rel --------------------------------- */
		/**结算类型 1:应收,2:预收 settlement_type ---------------------------------*/
		customerMerchantRel_settlementTypeList.add(new DerpBasic(DERP_SYS.SETTLEMENT_TYPE1, "应收"));
		customerMerchantRel_settlementTypeList.add(new DerpBasic(DERP_SYS.SETTLEMENT_TYPE2, "预收"));

		/**销售模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结  business_model ---------------------------------*/
		customerMerchantRel_businessModelList.add(new DerpBasic(DERP_SYS.BUSINESS_MODEL1, "购销-整批结算"));
		customerMerchantRel_businessModelList.add(new DerpBasic(DERP_SYS.BUSINESS_MODEL2, "代销"));
		customerMerchantRel_businessModelList.add(new DerpBasic(DERP_SYS.BUSINESS_MODEL3, "采购执行"));
		customerMerchantRel_businessModelList.add(new DerpBasic(DERP_SYS.BUSINESS_MODEL4, "购销-实销实结"));


		/**状态(1使用中,0已禁用)---------------------------------*/
		customerMerchantRel_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_MERCHANT_REL_status0, "禁用"));
		customerMerchantRel_statusList.add(new DerpBasic(DERP_SYS.CUSTOMER_MERCHANT_REL_status1, "启用"));

		/**是否启用采购价格管理 1-启用 0-不启用---------------------------------*/
		customerMerchantRel_purchasePriceManageList.add(new DerpBasic(DERP_SYS.CUSTOMER_MERCHANT_REL_PURCHASE_PRICE_MANGAGE0, "禁用"));
		customerMerchantRel_purchasePriceManageList.add(new DerpBasic(DERP_SYS.CUSTOMER_MERCHANT_REL_PURCHASE_PRICE_MANGAGE1, "启用"));
		/**是否启用销售价格管理 1-启用 0-不启用---------------------------------*/
		customerMerchantRel_salePriceManageList.add(new DerpBasic(DERP_SYS.CUSTOMER_MERCHANT_REL_SALE_PRICE_MANGAGE0, "禁用"));
		customerMerchantRel_salePriceManageList.add(new DerpBasic(DERP_SYS.CUSTOMER_MERCHANT_REL_SALE_PRICE_MANGAGE1, "启用"));

		/*主数据操作日志*/
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_1, "外仓备案商品管理"));
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_2, "采购价格管理"));
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_3, "销售价格管理"));
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_4, "商品列表"));
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_5, "品牌管理"));
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_6, "仓库管理"));
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_7, "事业部库位类型"));
		opeateSys_logList.add(new DerpBasic(DERP_SYS.OREARTE_SYS_LOG_8, "固定成本价盘"));

		/**销售记录接口*/
		saleOrderTypeList.add(new DerpBasic(DERP_SYS.SALE_ORDER_TYPE_1, "采购订单"));
		saleOrderTypeList.add(new DerpBasic(DERP_SYS.SALE_ORDER_TYPE_2, "销售订单"));
		saleOrderTypeList.add(new DerpBasic(DERP_SYS.SALE_ORDER_TYPE_3, "调拨订单"));
		saleOrderTypeList.add(new DerpBasic(DERP_SYS.SALE_ORDER_TYPE_4, "销售退订单"));

		/**理货接口*/
		tallyOrderType.add(new DerpBasic(DERP_SYS.TALLY_ORDER_TYPE_1, "采购理货"));
		tallyOrderType.add(new DerpBasic(DERP_SYS.TALLY_ORDER_TYPE_2, "调拨理货"));
		tallyOrderType.add(new DerpBasic(DERP_SYS.TALLY_ORDER_TYPE_3, "销售退理货"));

		/**上架接口*/
		shelfSaleType.add(new DerpBasic(DERP_SYS.SHELF_SALE_TYPE_1, "购销—整批结算"));
		shelfSaleType.add(new DerpBasic(DERP_SYS.SHELF_SALE_TYPE_2, "购销—实销实结"));
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
			Field[] fields = DERP_SYS.class.getDeclaredFields();
			for(Field field:fields){
				if(field.getName().equals(listName)){
					list = (ArrayList<DerpBasic>) field.get(new DERP_SYS());
				}
			}
		}catch(Exception e){}
		return list;
	}
}
