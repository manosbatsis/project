package com.topideal.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/***
 * 经分销 表字段状态/类型常量文件
 * */
public class DERP{
	
	/**********************公共常量************************************************/
	/***/
	public static final String SESSION_TOKEN = "SESSION_TOKEN";
	/**最大导出记录数限制*/
	public static final int EXPORT_MAX = 50000;
	/**已删除*/
	public static final String DEL_CODE_006 = "006";
	
	/**智能重推MQ消费失败类型*/
	public static final String MQ_FAILTYPE_01 = "mq_failtype_01";
	public static final String MQ_FAILTYPE_02 = "mq_failtype_02";
	public static final String MQ_FAILTYPE_03 = "mq_failtype_03";
	public static final String MQ_FAILTYPE_04 = "mq_failtype_04";
	public static ArrayList<DerpBasic> mqFailtypeList = new ArrayList<DerpBasic>();
    
    /**数据来源1-跨境宝 2-导入 3-第e仓 4-订单100 5-爬虫*/
	public static final String DATASOURCE_1 = "1";
	public static final String DATASOURCE_2 = "2";
	public static final String DATASOURCE_3 = "3";
	public static final String DATASOURCE_4 = "4";
	public static final String DATASOURCE_5 = "5";
	public static ArrayList<DerpBasic> dataSourceList = new ArrayList<DerpBasic>();

	/**电商平台
	 * 100011111-第e仓 100044998-京东 1000000310-天猫 1000004790-拼多多 1000005237-有赞
	 * 1000004941-斑马 1000000390-考拉 1000002612-小红书 1000000687-澳新 1000004058-小小包  
	 * 1000000276-蜜芽 小米有品-1000007078  1000006657  全球仓（澳新严选）0000020，聚美优品 淘宝分销-9000000001
	 * 1000006474 -贝店 1000005287-达令家	1000000521-奥买家  1000005248-宝妈时光 9000000002-海拍客  1000005311-洋码头
	 * 1000053167 -多点     SK190814110900014-DSC   SK191119181600031-D2O   1000056278快手
	 **/ 
	public static final String STOREPLATFORM_100011111 = "100011111";
	public static final String STOREPLATFORM_100044998 = "100044998";
	public static final String STOREPLATFORM_1000000310 = "1000000310";
	public static final String STOREPLATFORM_1000004790 = "1000004790";
	public static final String STOREPLATFORM_1000005237 = "1000005237";
	public static final String STOREPLATFORM_1000004941 = "1000004941";
	public static final String STOREPLATFORM_1000000390 = "1000000390";
	public static final String STOREPLATFORM_1000002612 = "1000002612";
	public static final String STOREPLATFORM_1000000687 = "1000000687";
	public static final String STOREPLATFORM_1000004058 = "1000004058";
	public static final String STOREPLATFORM_1000000276 = "1000000276";
	public static final String STOREPLATFORM_1000007078 = "1000007078";
	public static final String STOREPLATFORM_1000006657 = "1000006657";
	public static final String STOREPLATFORM_0000020 = "0000020";
	public static final String STOREPLATFORM_9000000001 = "9000000001";
	public static final String STOREPLATFORM_1000006474 = "1000006474";
	public static final String STOREPLATFORM_1000005287 = "1000005287";
	public static final String STOREPLATFORM_1000000521 = "1000000521";
	public static final String STOREPLATFORM_1000005248 = "1000005248";
	public static final String STOREPLATFORM_9000000002 = "9000000002";
	public static final String STOREPLATFORM_1000005311 = "1000005311";
	public static final String STOREPLATFORM_1000053167 = "1000053167";
	public static final String STOREPLATFORM_SK190814110900014 = "SK190814110900014";
	public static final String STOREPLATFORM_SK191119181600031 = "SK191119181600031";
	public static final String STOREPLATFORM_1000053198 = "1000053198";
	public static final String STOREPLATFORM_1000056278 = "1000056278";
	public static ArrayList<DerpBasic> storePlatformList = new ArrayList<DerpBasic>();
	
	/**(通用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑*/
	public static final String CURRENCYCODE_CNY = "CNY";
	public static final String CURRENCYCODE_JPY = "JPY";
	public static final String CURRENCYCODE_AUD = "AUD";
	public static final String CURRENCYCODE_USD = "USD";
	public static final String CURRENCYCODE_HKD = "HKD";
	public static final String CURRENCYCODE_EUR = "EUR";
	public static final String CURRENCYCODE_GBP = "GBP";
	public static final String CURRENCYCODE_CAD = "CAD";
	public static final String CURRENCYCODE_NZD = "NZD";
	public static final String CURRENCYCODE_NOK = "NOK";
	public static final String CURRENCYCODE_CHF = "CHF";
	public static final String CURRENCYCODE_THB = "THB";
	public static final String CURRENCYCODE_DKK = "DKK";
	public static final String CURRENCYCODE_PHP = "PHP";
	public static final String CURRENCYCODE_SGD = "SGD";
	public static final String CURRENCYCODE_SEK = "SEK";
	public static final String CURRENCYCODE_MOP = "MOP";
	public static final String CURRENCYCODE_CNH = "CNH";
	public static final String CURRENCYCODE_KRW = "KRW";
	public static ArrayList<DerpBasic> currencyCodeList = new ArrayList<DerpBasic>();
	
	/**(即将废弃新功能勿使用)币种 01-人民币 02-日元 03-澳元 04-美元 05-港币 06-欧元 07-英镑*/
	/*public static final String ORDER_CURRENCY_01 = "01";
	public static final String ORDER_CURRENCY_02 = "02";
	public static final String ORDER_CURRENCY_03 = "03";
	public static final String ORDER_CURRENCY_04 = "04";
	public static final String ORDER_CURRENCY_05 = "05";
	public static final String ORDER_CURRENCY_06 = "06";
	public static final String ORDER_CURRENCY_07 = "07";
	public static ArrayList<DerpBasic> order_currencyList = new ArrayList<DerpBasic>();*/
	
	/**库存理货单位 0-托盘 1-箱  2-件*/
	public static final String INVENTORY_UNIT_0 = "0";
	public static final String INVENTORY_UNIT_1 = "1";
	public static final String INVENTORY_UNIT_2 = "2";
	public static ArrayList<DerpBasic> inventory_unitList = new ArrayList<DerpBasic>();
	
	/**订单理货单位: 00-托盘 01-箱 02-件*/
	public static final String ORDER_TALLYINGUNIT_00 = "00";
	public static final String ORDER_TALLYINGUNIT_01 = "01";
	public static final String ORDER_TALLYINGUNIT_02 = "02";
	public static ArrayList<DerpBasic> order_tallyingUnitList = new ArrayList<DerpBasic>();
	
	/**计量单位: 00-托盘 01-箱 02-件 03-KG*/
	public static final String UNIT_00 = "00";
	public static final String UNIT_01 = "01";
	public static final String UNIT_02 = "02";
	public static final String UNIT_03 = "03";
	public static ArrayList<DerpBasic> unitList = new ArrayList<DerpBasic>();

	/**是否过期 0-过期 1-未过期*/
	public static final String ISEXPIRE_0 = "0";
	public static final String ISEXPIRE_1 = "1";
	public static ArrayList<DerpBasic> isExpireList = new ArrayList<DerpBasic>();

	/**是否坏品 0-好品 1-坏品*/
	public static final String ISDAMAGE_0 = "0";
	public static final String ISDAMAGE_1 = "1";
	public static ArrayList<DerpBasic> isDamageList = new ArrayList<DerpBasic>();

	/**爬虫平台类型：1-云集  2-唯品 3-京东 4-天猫 5-考拉*/
	public static final String CRAWLER_TYPE_1 = "1";
	public static final String CRAWLER_TYPE_2 = "2";
	public static final String CRAWLER_TYPE_3 = "3";
	public static final String CRAWLER_TYPE_4 = "4";
	public static final String CRAWLER_TYPE_5 = "5";
	public static ArrayList<DerpBasic> crawler_typeList = new ArrayList<DerpBasic>();

	/**采购卸货港: 44011501-44011501：南沙新港口岸 44010318-44010318：黄埔广保通码头口岸  21021001-21021001：大连保税区口岸 50010001-50010001：重庆口岸 ; 其他:other*/
	public static final String PORTDEST_44011501 = "44011501";
	public static final String PORTDEST_44010318 = "44010318";
	public static final String PORTDEST_21021001 = "21021001";
	public static final String PORTDEST_50010001 = "50010001";
	public static final String PORTDEST_OTHER = "other";
	public static ArrayList<DerpBasic> portDestList = new ArrayList<DerpBasic>();
	
	/**是否同关区: 0-否 1-是*/
	public static final String ISSAMEAREA_0 = "0";
	public static final String ISSAMEAREA_1 = "1";
	public static ArrayList<DerpBasic> isSameAreaList = new ArrayList<DerpBasic>();

	/**包装方式*/
	public static final String PACKTYPE_190 = "190";
	public static final String PACKTYPE_1A1 = "1A1";
	public static final String PACKTYPE_1A2 = "1A2";
	public static final String PACKTYPE_1B1 = "1B1";
	public static final String PACKTYPE_1B2 = "1B2";
	public static final String PACKTYPE_1C = "1C";
	public static final String PACKTYPE_1D = "1D";
	public static final String PACKTYPE_1G = "1G";
	public static final String PACKTYPE_1H = "1H";
	public static final String PACKTYPE_1H1 = "1H1";
	public static final String PACKTYPE_1H2 = "1H2";
	public static final String PACKTYPE_2C1 = "2C1";
	public static final String PACKTYPE_2C2 = "2C2";
	public static final String PACKTYPE_390 = "390";
	public static final String PACKTYPE_3A1 = "3A1";
	public static final String PACKTYPE_3A2 = "3A2";
	public static final String PACKTYPE_3B1 = "3B1";
	public static final String PACKTYPE_3B2 = "3B2";
	public static final String PACKTYPE_3H1 = "3H1";
	public static final String PACKTYPE_3H2 = "3H2";
	public static final String PACKTYPE_490 = "490";
	public static final String PACKTYPE_4A = "4A";
	public static final String PACKTYPE_4B = "4B";
	public static final String PACKTYPE_4C11 = "4C11";
	public static final String PACKTYPE_4C12 = "4C12";
	public static final String PACKTYPE_4C13 = "4C13";
	public static final String PACKTYPE_4C2 = "4C2";
	public static final String PACKTYPE_4D = "4D";
	public static final String PACKTYPE_4F = "4F";
	public static final String PACKTYPE_4G = "4G";
	public static final String PACKTYPE_4H = "4H";
	public static final String PACKTYPE_4H1 = "4H1";
	public static final String PACKTYPE_4H2 = "4H2";
	public static final String PACKTYPE_4M = "4M";
	public static final String PACKTYPE_4M1 = "4M1";
	public static final String PACKTYPE_4M2 = "4M2";
	public static final String PACKTYPE_590 = "590";
	public static final String PACKTYPE_5991 = "5991";
	public static final String PACKTYPE_5992 = "5992";
	public static final String PACKTYPE_5H = "5H";
	public static final String PACKTYPE_5H1 = "5H1";
	public static final String PACKTYPE_5H2 = "5H2";
	public static final String PACKTYPE_5H3 = "5H3";
	public static final String PACKTYPE_5H4 = "5H4";
	public static final String PACKTYPE_5H5 = "5H5";
	public static final String PACKTYPE_5H6 = "5H6";
	public static final String PACKTYPE_5H7 = "5H7";
	public static final String PACKTYPE_5H8 = "5H8";
	public static final String PACKTYPE_5H9 = "5H9";
	public static final String PACKTYPE_5L1 = "5L1";
	public static final String PACKTYPE_5L2 = "5L2";
	public static final String PACKTYPE_5L3 = "5L3";
	public static final String PACKTYPE_5M1 = "5M1";
	public static final String PACKTYPE_5M2 = "5M2";
	public static final String PACKTYPE_5M91 = "5M91";
	public static final String PACKTYPE_6HA1 = "6HA1";
	public static final String PACKTYPE_6HA2 = "6HA2";
	public static final String PACKTYPE_6HB1 = "6HB1";
	public static final String PACKTYPE_6HB2 = "6HB2";
	public static final String PACKTYPE_6HC = "6HC";
	public static final String PACKTYPE_6HD1 = "6HD1";
	public static final String PACKTYPE_6HD2 = "6HD2";
	public static final String PACKTYPE_6HG1 = "6HG1";
	public static final String PACKTYPE_6HG2 = "6HG2";
	public static final String PACKTYPE_6HH1 = "6HH1";
	public static final String PACKTYPE_6HH2 = "6HH2";
	public static final String PACKTYPE_6PA1 = "6PA1";
	public static final String PACKTYPE_6PA2 = "6PA2";
	public static final String PACKTYPE_6PB1 = "6PB1";
	public static final String PACKTYPE_6PB2 = "6PB2";
	public static final String PACKTYPE_6PC = "6PC";
	public static final String PACKTYPE_6PD1 = "6PD1";
	public static final String PACKTYPE_6PD2 = "6PD2";
	public static final String PACKTYPE_6PG1 = "6PG1";
	public static final String PACKTYPE_6PG2 = "6PG2";
	public static final String PACKTYPE_6PH1 = "6PH1";
	public static final String PACKTYPE_6PH2 = "6PH2";
	public static final String PACKTYPE_9990 = "9990";
	public static final String PACKTYPE_9991 = "9991";
	public static final String PACKTYPE_9992 = "9992";
	public static final String PACKTYPE_9993 = "9993";
	public static final String PACKTYPE_9994 = "9994";
	public static final String PACKTYPE_9995 = "9995";
	public static final String PACKTYPE_9996 = "9996";
	public static final String PACKTYPE_9997 = "9997";
	public static final String PACKTYPE_9999 = "9999";
	public static final String PACKTYPE_9A91 = "9A91";
	public static final String PACKTYPE_9A92 = "9A92";
	public static final String PACKTYPE_9A93 = "9A93";
	public static final String PACKTYPE_9C91 = "9C91";
	public static final String PACKTYPE_9F91 = "9F91";
	public static ArrayList<DerpBasic> packTypeList = new ArrayList<DerpBasic>();
	
	/**唯一ID生成前缀 */
	public static final String UNIQUEID_PREFIX_BNO = "BNO";//交易链路跟踪批次号
	public static final String UNIQUEID_PREFIX_CGO = "CGO";//采购订单
	public static final String UNIQUEID_PREFIX_CGRK = "CGRK";//采购入库单
	public static final String UNIQUEID_PREFIX_CGOD = "CGOD";//预申报单
	public static final String UNIQUEID_PREFIX_CGTH= "CGTH";//采购退货单号
	public static final String UNIQUEID_PREFIX_CGTC= "CGTC";//采购退货出库单号
	public static final String UNIQUEID_PREFIX_DBO = "DBO";//调拨订单
	public static final String UNIQUEID_PREFIX_DBCK = "DBCK";//调拨出库单
	public static final String UNIQUEID_PREFIX_DBRK = "DBRK";//调拨入库单
	public static final String UNIQUEID_PREFIX_DSDD = "DSDD";//电商订单
	public static final String UNIQUEID_PREFIX_ZDY = "ZDY";//自定义运单
	public static final String UNIQUEID_PREFIX_DSDDTH = "DSDDTH";//电商订单退货
	public static final String UNIQUEID_PREFIX_SYBYK= "SYBYK";//事业部移库单
	public static final String UNIQUEID_PREFIX_XSO = "XSO";//销售订单
	public static final String UNIQUEID_PREFIX_XSCK = "XSCK";//销售出库
	public static final String UNIQUE_ID_SJD = "SJD";//上架单号
	public static final String UNIQUEID_PREFIX_SJRK= "SJRK";//上架入库单号
	public static final String UNIQUEID_PREFIX_YSD= "YSD";//预售单单号
	public static final String UNIQUEID_PREFIX_XSTO = "XSTO";//销售退货订单
	public static final String UNIQUEID_PREFIX_XSTOD = "XSTOD";//销售退预申报
	public static final String UNIQUEID_PREFIX_XSTR = "XSTR";//销售退货入库
	public static final String UNIQUEID_PREFIX_XSTC = "XSTC";//销售退货出库
	public static final String UNIQUEID_PREFIX_ZDCK = "ZDCK";//账单出库单号
	public static final String UNIQUEID_PREFIX_ZDRK = "ZDRK";//账单入库单号
	public static final String UNIQUEID_PREFIX_CGSD = "CGSD";//采购SD单
	public static final String UNIQUEID_PREFIX_DCRZ = "DCRZ";//融资单号
	public static final String UNIQUEID_PREFIX_LLD = "LLD";//领料单号
	public static final String UNIQUEID_PREFIX_LLCK = "LLCK";//领料出库单号
	public static final String UNIQUEID_PREFIX_XSSD = "XSSD";//销售SD编码
	public static final String UNIQUEID_PREFIX_XSOD = "XSOD";//销售预申报单编号
	public static final String UNIQUEID_PREFIX_XSRZ = "XSRZ";//赊销单号
	public static final String UNIQUEID_PREFIX_KWTZD = "KWTZD";    //库位调整单号
	public static final String UNIQUEID_PREFIX_ZDJS = "ZDJS";//账单结算单
	public static final String UNIQUEID_PREFIX_YSZD = "YSZD";//应收账单
	public static final String UNIQUEID_PREFIX_JSXM = "JSXM";//费项编码
	public static final String UNIQUEID_PREFIX_JCXMXL = "JCXMXL";//小类ID
	public static final String UNIQUEID_PREFIX_FPMB = "FPMB";//发票模板编码
	public static final String UNIQUEID_PREFIX_PTJS = "PTJS";//平台结算单
	public static final String UNIQUEID_PREFIX_YSKD = "YSKD";//预收账单单号
	public static final String UNIQUEID_PREFIX_ZGFY = "ZGFY";//暂估费用单号
	public static final String UNIQUEID_PREFIX_WJRW = "WJRW";//文件任务
	public static final String UNIQUE_ID_FYD = "FYD";//平台费用单号
	public static final String UNIQUE_ID_YFKD = "YFKD";//预付账单号
	public static final String UNIQUE_ID_YFZD = "YFZD";//应付账单号
	public static final String UNIQUE_ID_CGFP = "CGFP";//采购发票
	public static final String UNIQUE_ID_CGDH = "CGDH";//采购单号
	/**
	 * 应收账单发票编码商家前缀
	 */
	public static final String UNIQUEID_PREFIX_HNFH = "HNFH";//宝信-应收发票号
	public static final String UNIQUEID_PREFIX_QTOP = "QTOP";//健太-应收发票号
	public static final String UNIQUEID_PREFIX_TWKL = "TWKL";//卓烨-应收发票号
	public static final String UNIQUEID_PREFIX_YSTA = "YSTA";//元森泰-应收发票号
	public static final String UNIQUEID_PREFIX_ABHG = "ABHG";//广旺-应收发票号
	public static final String UNIQUEID_PREFIX_RYBZ = "RYBZ";//润佰-应收发票号
	public static final String UNIQUEID_PREFIX_WAMD = "WAMD";//万代-应收发票号
	public static final String UNIQUEID_PREFIX_HIGH = "HIGH";//轩盛有限公司-应收发票号
	public static final String UNIQUEID_PREFIX_PDJG = "PDJG";//盘点结果
	public static final String UNIQUEID_PREFIX_PDO = "PDO";//盘点指令
	public static final String UNIQUEID_PREFIX_LXTZO = "LXTZO" ;//类型调整单
	public static final String UNIQUEID_PREFIX_KCTZ = "KCTZ";//库存调整单
	public static final String UNIQUEID_PREFIX_WPHC = "WPHC";//唯品红冲
	public static final String UNIQUEID_PREFIX_ATT = "ATT";//附件管理
	public static final String UNIQUEID_PREFIX_SYB = "SYB";//事业部
	public static final String UNIQUEID_PREFIX_ERP = "ERP";//商品编码
	public static final String UNIQUEID_PREFIX_HDRW= "HDRW";//核对任务
	
	/**FinanceCloseAccountsMongo 来源 1.财务经销存关账 2.已经月结	*/
	public static final String CLOSE_ACCOUNTS_SOURCE_1 = "1";
	public static final String CLOSE_ACCOUNTS_SOURCE_2 = "2";
	public static ArrayList<DerpBasic> closeAccountsSourceList = new ArrayList<DerpBasic>();

	/**（采购）物流运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他*/
	public static final String TRANSPORT_1 = "1";
	public static final String TRANSPORT_2 = "2";
	public static final String TRANSPORT_3 = "3";
	public static final String TRANSPORT_4 = "4";
	public static final String TRANSPORT_5 = "5";
	public static final String TRANSPORT_6 = "6";
	public static ArrayList<DerpBasic> transportList = new ArrayList<DerpBasic>();

	/**陆运运输方式   1-港到仓拖车 2-中港车 3-转关车 4-短驳运输车 5-其他*/
	public static final String LAND_TRANSPORT_1 = "1";
	public static final String LAND_TRANSPORT_2 = "2";
	public static final String LAND_TRANSPORT_3 = "3";
	public static final String LAND_TRANSPORT_4 = "4";
	public static final String LAND_TRANSPORT_5 = "5";
	public static ArrayList<DerpBasic> landTransportList = new ArrayList<DerpBasic>();

	/**国际物流运输方式   1-空运 2-海运 5-中欧铁路  6-其他*/
	public static final String COUNTRY_TRANSPORT_1 = "1";
	public static final String COUNTRY_TRANSPORT_2 = "2";
	public static final String COUNTRY_TRANSPORT_5 = "5";
	public static final String COUNTRY_TRANSPORT_6 = "6";
	public static ArrayList<DerpBasic> countryTransportList= new ArrayList<DerpBasic>();

	/**物流企业名称(内贸仓才用得到)  */
	public static final String LOGISTICSNAME_SF = "SF";
	public static final String LOGISTICSNAME_EMS = "EMS";
	public static final String LOGISTICSNAME_EYB = "EYB";
	public static final String LOGISTICSNAME_ZJS = "ZJS";
	public static final String LOGISTICSNAME_YTO = "YTO";
	public static final String LOGISTICSNAME_ZTO = "ZTO";
	public static final String LOGISTICSNAME_HTKY = "HTKY";
	public static final String LOGISTICSNAME_UC = "UC";
	public static final String LOGISTICSNAME_STO = "STO";
	public static final String LOGISTICSNAME_TTKDEX = "TTKDEX";
	public static final String LOGISTICSNAME_QFKD = "QFKD";
	public static final String LOGISTICSNAME_FAST = "FAST";
	public static final String LOGISTICSNAME_POSTB = "POSTB";
	public static final String LOGISTICSNAME_GTO = "GTO";
	public static final String LOGISTICSNAME_YUNDA = "YUNDA";
	public static final String LOGISTICSNAME_JD = "JD";
	public static final String LOGISTICSNAME_DD = "DD";
	public static final String LOGISTICSNAME_OTHER = "OTHER";
	public static ArrayList<DerpBasic> logisticsNameList = new ArrayList<DerpBasic>();
	
	/**1.只校验关账  2.只校验月结 3.校验关账和月结  */
	public static final String CLOSEACCOUNTFLAG1 = "1";
	public static final String CLOSEACCOUNTFLAG2 = "2";
	public static final String CLOSEACCOUNTFLAG3 = "3";
	public static ArrayList<DerpBasic> closeAccountFlagList = new ArrayList<DerpBasic>();
	/**是否红冲单 0否，1是*/
	public static final String ISWRITEOFF0 = "0";
	public static final String ISWRITEOFF1 = "1";
	public static ArrayList<DerpBasic> isWriteOffList = new ArrayList<DerpBasic>();
   
static{
	   
	    /**智能重推MQ消费失败类型*/
   		mqFailtypeList.add(new DerpBasic(DERP.MQ_FAILTYPE_01,"B2C单号不存在"));
   		mqFailtypeList.add(new DerpBasic(DERP.MQ_FAILTYPE_02,"冻结记录不存在"));
   		mqFailtypeList.add(new DerpBasic(DERP.MQ_FAILTYPE_03,"锁记录"));
   		mqFailtypeList.add(new DerpBasic(DERP.MQ_FAILTYPE_04,"库存回调通知-单号不存在"));
	   	 
		 /**数据来源*/
   		dataSourceList.add(new DerpBasic(DERP.DATASOURCE_1,"跨境宝"));
   		dataSourceList.add(new DerpBasic(DERP.DATASOURCE_2,"导入"));
   		dataSourceList.add(new DerpBasic(DERP.DATASOURCE_3,"第e仓"));
   		dataSourceList.add(new DerpBasic(DERP.DATASOURCE_4,"订单100"));
   		dataSourceList.add(new DerpBasic(DERP.DATASOURCE_5,"爬虫"));

   		/**币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑*/
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_CNY,"人民币"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_AUD,"澳元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_JPY,"日元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_USD,"美元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_HKD,"港币"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_EUR,"欧元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_GBP,"英镑"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_CAD,"加拿大元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_NZD,"新西兰元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_NOK,"挪威克朗"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_CHF,"瑞士法郎"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_THB,"泰国铢"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_DKK,"丹麦克朗"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_PHP,"菲律宾比索"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_SGD,"新加坡元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_SEK,"瑞典克朗"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_MOP,"澳门元"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_CNH,"离岸人民币"));
   		currencyCodeList.add(new DerpBasic(DERP.CURRENCYCODE_KRW,"韩元"));

		/**币种 01-人民币 02-日元 03-澳元 04-美元 05-港币 06-欧元 07-英镑*/
		/*order_currencyList.add(new DerpBasic(DERP.ORDER_CURRENCY_01,"人民币"));
		order_currencyList.add(new DerpBasic(DERP.ORDER_CURRENCY_02,"日元"));
		order_currencyList.add(new DerpBasic(DERP.ORDER_CURRENCY_03,"澳元"));
		order_currencyList.add(new DerpBasic(DERP.ORDER_CURRENCY_04,"美元"));
		order_currencyList.add(new DerpBasic(DERP.ORDER_CURRENCY_05,"港币"));
		order_currencyList.add(new DerpBasic(DERP.ORDER_CURRENCY_06,"欧元"));
		order_currencyList.add(new DerpBasic(DERP.ORDER_CURRENCY_07,"英镑"));*/

		/**电商品台*/
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_100011111,"第e仓"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_100044998,"京东"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000000310,"天猫"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000004790,"拼多多"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000005237,"有赞"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000004941,"斑马"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000000390,"考拉"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000002612,"小红书"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000000687,"澳新"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000004058,"小小包"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000000276,"蜜芽"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000007078,"小米有品"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000006657,"全球仓（澳新严选）"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_9000000001,"淘宝分销"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_0000020,"聚美优品"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000006474,"贝店"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000005287,"达令家"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000000521,"奥买家"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000005248,"宝妈时光"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_9000000002,"海拍客"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000005311,"洋码头"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000053167,"多点"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_SK190814110900014,"DSC"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_SK191119181600031,"D2O"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000053198,"抖音"));
		storePlatformList.add(new DerpBasic(DERP.STOREPLATFORM_1000056278,"快手"));
		
		
		
		

		/**库存理货单位 0-托盘 1-箱  2-件*/
		inventory_unitList.add(new DerpBasic(DERP.INVENTORY_UNIT_0,"托盘"));
		inventory_unitList.add(new DerpBasic(DERP.INVENTORY_UNIT_1,"箱"));
		inventory_unitList.add(new DerpBasic(DERP.INVENTORY_UNIT_2,"件"));
		
		/**订单理货单位: 00-托盘 01-箱 02-件*/
		order_tallyingUnitList.add(new DerpBasic(DERP.ORDER_TALLYINGUNIT_00,"托盘"));
		order_tallyingUnitList.add(new DerpBasic(DERP.ORDER_TALLYINGUNIT_01,"箱"));
		order_tallyingUnitList.add(new DerpBasic(DERP.ORDER_TALLYINGUNIT_02,"件"));
		
		/**计量单位: 00-托盘 01-箱 02-件 03-KG*/
		unitList.add(new DerpBasic(DERP.UNIT_00,"托盘"));
		unitList.add(new DerpBasic(DERP.UNIT_01,"箱"));
		unitList.add(new DerpBasic(DERP.UNIT_02,"件"));
		unitList.add(new DerpBasic(DERP.UNIT_03,"KG"));
		
		/**是否过期 0-过期 1-未过期*/
		isExpireList.add(new DerpBasic(DERP.ISEXPIRE_0,"过期"));
		isExpireList.add(new DerpBasic(DERP.ISEXPIRE_1,"未过期"));

		/**是否坏品 0-好品 1-坏品*/
		isDamageList.add(new DerpBasic(DERP.ISDAMAGE_0,"好品"));
		isDamageList.add(new DerpBasic(DERP.ISDAMAGE_1,"坏品"));

		/**爬虫平台类型：1-云集  2-唯品*/
		crawler_typeList.add(new DerpBasic(DERP.CRAWLER_TYPE_1,"云集"));
		crawler_typeList.add(new DerpBasic(DERP.CRAWLER_TYPE_2,"唯品"));
		crawler_typeList.add(new DerpBasic(DERP.CRAWLER_TYPE_3,"京东"));
		crawler_typeList.add(new DerpBasic(DERP.CRAWLER_TYPE_4,"天猫"));
		crawler_typeList.add(new DerpBasic(DERP.CRAWLER_TYPE_5,"考拉"));
		
		/**卸货港 注意这里格式必须为：44011501：南沙新港口岸（：必须为中文的）*/
		portDestList.add(new DerpBasic(DERP.PORTDEST_44011501,"44011501：南沙新港口岸"));
		portDestList.add(new DerpBasic(DERP.PORTDEST_44010318,"44010318：黄埔广保通码头口岸"));
		portDestList.add(new DerpBasic(DERP.PORTDEST_21021001,"21021001：大连保税区口岸"));
		portDestList.add(new DerpBasic(DERP.PORTDEST_50010001,"50010001：重庆口岸"));
		portDestList.add(new DerpBasic(DERP.PORTDEST_OTHER,"其他"));
		
		/**是否同关区: 0-否 1-是*/
		isSameAreaList.add(new DerpBasic(DERP.ISSAMEAREA_0,"否"));
		isSameAreaList.add(new DerpBasic(DERP.ISSAMEAREA_1,"是"));
		
		/**包装方式*/
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_190,"其他桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1A1,"钢制不可拆装桶顶圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1A2,"钢制可拆装桶顶圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1B1,"铝制不可拆装桶顶圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1B2,"铝制可拆装桶顶圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1C,"木圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1D,"胶合板圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1G,"纤维圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1H,"塑料圆桶	"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1H1,"塑料不可拆装桶顶圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_1H2,"塑料可拆装桶顶圆桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_2C1,"塞式木琵琶桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_2C2,"非水密型木琵琶桶"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_390,"其他罐"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_3A1,"钢制不可拆装罐顶罐"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_3A2,"钢制可拆装罐顶罐"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_3B1,"铝制不可拆装罐顶罐"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_3B2,"铝制可拆装罐顶罐"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_3H1,"塑料制不可拆装罐顶罐"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_3H2,"塑料制可拆装罐顶罐"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_490,"其他箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4A,"钢箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4B,"铝箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4C11,"大木箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4C12,"中木箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4C13,"小木箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4C2,"箱壁防撤漏木箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4D,"胶合板箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4F,"再生木木箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4G,"纤维板箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4H,"塑料箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4H1,"膨胀的塑料箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4H2,"硬质的塑料箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4M,"纸箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4M1,"单瓦楞纸箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_4M2,"双瓦楞纸箱"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_590,"其他袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5991,"麻袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5992,"布袋/包"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H,"塑料编织袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H1,"塑料编织无内衬或涂层的袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H2,"塑料编织防撤漏的袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H3,"塑料编织防水的袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H4,"塑料薄膜袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H5,"无涂层或内衬的编织集装袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H6,"带涂层的编织塑料集装袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H7,"带内衬的编织塑料集装袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H8,"带涂层和内衬的编织集装袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5H9,"集装袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5L1,"纺织品无内衬或涂层的袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5L2,"纺织品防撤漏的袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5L3,"纺织品防水的袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5M1,"多层的纸袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5M2,"多层防水纸袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_5M91,"纸袋"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HA1,"塑料容器在钢桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HA2,"塑料容器在钢条或钢皮箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HB1,"塑料容器在铝桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HB2,"塑料容器在铝条或铝皮箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HC,"塑料容器在木箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HD1,"塑料容器在胶合板桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HD2,"塑料容器在胶合板箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HG1,"塑料容器在纤维桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HG2,"塑料容器在纤维板箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HH1,"塑料容器在塑料桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6HH2,"塑料容器在硬塑料箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PA1,"玻璃、陶瓷、粗陶器在钢桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PA2,"玻璃、陶瓷、粗陶器在钢条或钢皮箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PB1,"玻璃、陶瓷、粗陶器在铝桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PB2,"玻璃、陶瓷、粗陶器在铝条或铝皮箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PC,"玻璃、陶瓷、粗陶器在木箱内复合包装	"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PD1,"玻璃、陶瓷、粗陶器在胶合板内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PD2,"玻璃、陶瓷、粗陶器在柳条筐内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PG1,"玻璃、陶瓷、粗陶器在纤维桶内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PG2,"玻璃、陶瓷、粗陶器在纤维板箱内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PH1,"玻璃、陶瓷、粗陶器在膨胀塑料包装内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_6PH2,"玻璃、陶瓷、粗陶器在硬塑料包装内复合包装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9990,"竹箩"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9991,"竹笼"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9992,"植物性铺垫材料"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9993,"散装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9994,"裸装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9995,"挂装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9996,"铺席"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9997,"捆装"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9999,"其他"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9A91,"铁托"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9A92,"铁笼"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9A93,"铁皮"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9C91,"天然木托"));
		packTypeList.add(new DerpBasic(DERP.PACKTYPE_9F91,"再生木托"));

		/**FinanceCloseAccountsMongo 来源 1.财务经销存关账 2.已经月结	*/
		closeAccountsSourceList.add(new DerpBasic(DERP.CLOSE_ACCOUNTS_SOURCE_1,"财务经销存关账"));
		closeAccountsSourceList.add(new DerpBasic(DERP.CLOSE_ACCOUNTS_SOURCE_2,"已经月结"));

		/**运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他*/
		transportList.add(new DerpBasic(DERP.TRANSPORT_1,"空运"));
		transportList.add(new DerpBasic(DERP.TRANSPORT_2,"海运"));
		transportList.add(new DerpBasic(DERP.TRANSPORT_3,"陆运"));
		transportList.add(new DerpBasic(DERP.TRANSPORT_4,"港到仓拖车"));
		transportList.add(new DerpBasic(DERP.TRANSPORT_5,"中欧铁路"));
		transportList.add(new DerpBasic(DERP.TRANSPORT_6,"其他"));

		/**陆运运输方式 1-港到仓拖车 2-中港车 3-转关车 4-短驳运输车 5-其他*/
		landTransportList.add(new DerpBasic(DERP.LAND_TRANSPORT_1,"港到仓拖车"));
		landTransportList.add(new DerpBasic(DERP.LAND_TRANSPORT_2,"中港车"));
		landTransportList.add(new DerpBasic(DERP.LAND_TRANSPORT_3,"转关车"));
		landTransportList.add(new DerpBasic(DERP.LAND_TRANSPORT_4,"短驳运输车"));
		landTransportList.add(new DerpBasic(DERP.LAND_TRANSPORT_5,"其他"));

		/**国际物流运输方式   1-空运 2-海运 5-中欧铁路 6-其他*/
		countryTransportList.add(new DerpBasic(DERP.COUNTRY_TRANSPORT_1,"空运"));
		countryTransportList.add(new DerpBasic(DERP.COUNTRY_TRANSPORT_2,"海运"));
		countryTransportList.add(new DerpBasic(DERP.COUNTRY_TRANSPORT_5,"中欧铁路"));
		countryTransportList.add(new DerpBasic(DERP.COUNTRY_TRANSPORT_6,"其他"));

		/**物流企业名称(内贸仓才用得到)  */
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_SF,"顺丰"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_SF,"顺丰"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_EMS,"标准快递"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_EYB,"经济快件"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_ZJS,"宅急送"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_YTO,"圆通"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_ZTO,"中通 (ZTO)"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_HTKY,"百世汇通"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_UC,"优速"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_STO,"申通"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_TTKDEX,"天天快递"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_QFKD,"全峰"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_FAST,"快捷"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_POSTB,"邮政小包"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_GTO,"国通"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_YUNDA,"韵达"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_JD,"京东配送"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_DD,"当当宅配"));
		logisticsNameList.add(new DerpBasic(DERP.LOGISTICSNAME_OTHER,"其他"));
		
		/**1.只校验关账  2.只校验月结 3.校验关账和月结  */
		closeAccountFlagList.add(new DerpBasic(DERP.CLOSEACCOUNTFLAG1,"只校验关账"));
		closeAccountFlagList.add(new DerpBasic(DERP.CLOSEACCOUNTFLAG2,"只校验月结"));
		closeAccountFlagList.add(new DerpBasic(DERP.CLOSEACCOUNTFLAG3,"校验关账和月结"));
		
		/**是否红冲单 0否，1是*/
		isWriteOffList.add(new DerpBasic(DERP.ISWRITEOFF0,"否"));
		isWriteOffList.add(new DerpBasic(DERP.ISWRITEOFF1,"是"));
	

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
			   Field[] fields = DERP.class.getDeclaredFields();
		       for(Field field:fields){
		          if(field.getName().equals(listName)){
		        	 list = (ArrayList<DerpBasic>) field.get(new DERP());
		          }
		       }
		   }catch(Exception e){}
		   return list;
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
}

