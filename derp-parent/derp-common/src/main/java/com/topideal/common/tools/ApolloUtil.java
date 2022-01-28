package com.topideal.common.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 从apollo注入配置管理类-配置
 * 
 */
@Component
public class ApolloUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ApolloUtil.class);

	public static String env;
	public static String appId;

	/**#orer web子服务ip*/
	public static String orderWebhost;
	/**#仓储web子服务ip*/
	public static String storageWebhost;
	/**#库存web子服务ip*/
	public static String inventoryWebhost;
	/**#报表web子服务ip*/
	public static String reportWebhost;
	/**#前端web子服务ip*/
	public static String frontWebhost;
	
	/**经分销api地址*/
	public static String apiUrl;
	
	/**==================update-tabale-goodsinfo==================*/
	/**1刷新商品条码-业务库*/
	public static String updateOrderAttribute;
	/**2刷新商品条码-库存库*/
	public static String updateInventoryAttribute;
	/**3刷新商品条码-仓储库*/
	public static String updateStorageAttribute;
	/**4刷新商品条码-报表库*/
	public static String updateReportAttribute;
		
	
	/**=================external-api-config======================*/
	/**OP------------------------------*/
	/**OP实时库存查询接口*/
	public static String opV214;
	
	/**跨境宝--------------------------*/
	/**跨境宝接口版本*/
	public static String epassV;
	/**跨境宝接口秘钥*/
	public static String epassAppSecret;
	/**跨境宝接口appKey*/
	public static String epassAppKey;
	/**跨境宝接口地址*/
	public static String epassApi;
	/**采购订单*/
	public static String epass001Method;
	/**确认理货*/
	public static String epass002Method;
	/**销售出仓订单*/
	public static String epass003Method;
	/**调拨指令推送*/
	public static String epass004Method;
	
	/**OFC-----------------------------*/
	/**实时库存查询接口地址(卓志海外仓)*/
	public static String ofcC29;
	
	/**蓝精灵--------------------------*/
	/**api地址*/
	//public static String smurfsApi;
	/**appkey*/
	public static String smurfsAppkey;
	/**秘钥*/
	public static String smurfsAppsecret;
	/**邮箱*/
	public static String smurfsRecipients;
	/**ip+端口*/
	public static String smurfsHost;
	/**上传图片版本号*/
	public static String smurfsUploadVersion;
	
	/**蓝精灵Log日志--------------------------*/
	/**api地址*/
	//public static String smurfsLogApi;
	/**appkey*/
	public static String smurfsLogAppkey;
	/**秘钥*/
	public static String smurfsLogAppsecret;
	/**邮箱*/
	public static String smurfsLogRecipients;
	/**ip+端口*/
	public static String smurfsLogHost;
	/**链接*/
	//public static String smurfsLogPath;

	/**e仓-----------------------------*/
    /**定时抓取e仓发货订单地址*/
	public static String ewsW410;
	/**抓取的商家卓志编码*/
	public static String ewsTopidealcode;
	
	/**GSS----------------------------*/
    /**Gss实时快照地址*/
	public static String gssG2Url;
	 /**Gss批次库存查询地址*/
	public static String gssG2batchUrl;

	
	/**金融----------------------------*/
	/**api地址*/
	public static String financeApi;
	/**appkey*/
	public static String financeAppkey;
	/**秘钥*/
	public static String financeAppsecret;
	/**版本*/
	public static String financeV;
	/**商品分类查询接口*/
	public static String finance001Method;
	/**商品同步接口（通用库）*/
	public static String finance002Method;
	/**金融融资相关接口**/
	public static String financeApi2;
	/**appkey*/
	public static String financeAppkey2;
	/**秘钥*/
	public static String financeAppsecret2;
	/**版本*/
	public static String financeV2;
	/**融资申请接口*/
	public static String finance001Method2;
	/**企业还款赎回接收接口*/
	public static String finance002Method2;
	/**还款试算*/
	public static String finance003Method2;
	/**金融同步商品接口**/
	public static String financeApi3;
	/**同步商品*/
	public static String finance001Method3;

	/**汇率-------------------------*/
	/**api地址*/
	public static String currencyApi;
	/**appkey*/
	public static String currencyAppkey;
	/**秘钥*/
	public static String currencyAppsecret;
	/**版本*/
	public static String currencyV;
	/**获取汇率接口*/
	public static String currency001Method;
	/**获取平均汇率接口*/
	public static String currency002Method;

	/**淘宝API*/
	public static String taobaoApi ;
	
	/**下推主数据商品同步*/
	public static String maindataSyncGoodsUrl ;
	/**主数据来源系统*/
	public static String maindataSource ;
	
	/**主数据地址 */
	public static String maindataSyncUrl;
	/**主数据方法 产品商品信息同步接口(推送主数据)*/
	public static String maindataSync001Method;

	/**-----------众邦云仓库查询接口-------------------*/
	/**api地址*/
	public static String ywmsApi;
	/**appkey*/
	public static String ywmsAppkey;
	/**secret*/
	public static String ywmsSecret;
	/**content格式*/
	public static String ywmsFormat;
	/**customerId*/
	public static String ywmsCustomerId;
	/**版本号*/
	public static String ywmsV;
	/**货主*/
	public static String ywmsOwnerCode;
	/**货主对应公司主体*/
	public static String ywmsTopidealCode;
	/**商家在众邦云仓有库存的商品货号*/
	public static String ywmsGoodsNos;
	
	/**财务 NC*/
	/**nc api 地址*/
	public static String ncApi ;
	/**nc appkey */
	public static String ncAppKey ;
	/**nc 公钥*/
	public static String ncPublicKey ;
	/**nc 秘钥*/
	public static String ncPrivateKey ;
	/**nc 来源系统*/
	public static String ncSourceType ;

	/**卓普信*/
	/**卓普信 地址*/
	public static String sapienceUrl ;
	/**卓普信 appkey*/
	public static String sapienceAppKey ;
	/**卓普信 秘钥*/
	public static String sapienceAppSecret ;

	/**欧莱雅*/
	/**欧莱雅 地址*/
	public static String orealUrl ;
	/**欧莱雅 token用户名*/
	public static String orealTokenUsername ;
	/**欧莱雅 token密码*/
	public static String orealTokenPassword ;
	/**欧莱雅 认证用户名*/
	public static String orealAuthUsername ;
	/**欧莱雅 认证密码*/
	public static String orealAuthPassword ;
	/**欧莱雅 account 账号*/
	public static String orealAccountUsername ;
	/**欧莱雅 获取token方法*/
	public static String oreal001Method ;
	/**欧莱雅 获取商品方法*/
	public static String oreal002Method ;
	/**DSTP---------------------------------------------*/
	/**url*/
	public static String dstpUrl;
	/**appkey*/
	public static String dstpAppkey_0000138;
	public static String dstpAppkey_1000000204;
	public static String dstpAppkey_1000000626;
	public static String dstpAppkey_0000134;
	/**appsecret*/
	public static String dstpAppsecret_0000138;
	public static String dstpAppsecret_1000000204;
	public static String dstpAppsecret_1000000626;
	public static String dstpAppsecret_0000134;





	/**单点登录===========================*/
	/**服务端地址*/
	public static String serverUrlPrefix;
    //客户端地址
	public static String clientHostUrl;

    //授权代码
	public static  String appUser;
    //appkey
	public static  String appKey;
	//idm数据接口地址
	public static  String simUrl;
	//授权符
	public static  String idmAuthStr;

	//系统名称
	public static String systemName;

	/**机器人===========================*/
	//是否启用 true false
	public static  String jxEnable;
	//环境备注
	public static  String jxRemark;
	//经销精灵地址
	public static  String jxWebhookUrl;
	
	/**OA接口地址===========================*/
	//获取员工信息接口地址
	public static  String oaUrl1;
	//流程创建接口地址
	public static  String oaUrl2;
	//流程ID
	public static  String oaWorkflowId;
	//来源系统
	public static  String oaLyxt;

	//获取采购框架合同
	public static  String oaPurchaseFrameContractUrl;
	public static  String oaPurchaseFrameContractFormId;

	//获取试单申请数据
	public static  String oaPurchaseTryApplyContractUrl;
	public static  String oaPurchaseTryApplyContractFormId;

	// 采购OA审批流程URL
	public static  String oaPurchaseWorkflowIdUrl;

	// 采购OA审批流程workflowId
	public static  String oaPurchaseWorkflowId;
	
	/**PMS接口地址===========================*/
	public static  String pmsUrl;

	/**内部交易金额数量差异接口===========================*/
	//宝信事业部门邮件
	public static  String baoxinEmail;
	//保健品事业部门邮件
	public static  String baojianEmail;
	//母婴事业部门邮件
	public static  String muyingEmail;

	/**实施以及开发接收人 一般可用于数据预警提示========================================*/
	public static String internalAlertEmail;

	/**===================xxxxxx================================*/
	@Value("${env:}")
	public void setEnv(String env) {
		ApolloUtil.env = env;
	}

	@Value("${app.id:}")
	public void setAppId(String appId) {
		ApolloUtil.appId = appId;
	}

	/**只需配置set方法*/
	@Value("${orderwebhost:}")
	public void setOrderWebhost(String orderWebhost) {
		ApolloUtil.orderWebhost = orderWebhost;
	}
	@Value("${storagewebhost:}")
	public void setStorageWebhost(String storageWebhost) {
		ApolloUtil.storageWebhost = storageWebhost;
	}
	@Value("${inventorywebhost:}")
	public void setInventoryWebhost(String inventoryWebhost) {
		ApolloUtil.inventoryWebhost = inventoryWebhost;
	}
	@Value("${reportwebhost:}")
	public void setReportWebhost(String reportWebhost) {
		ApolloUtil.reportWebhost = reportWebhost;
	}
	@Value("${frontwebhost:}")
	public void setFrontWebhost(String frontWebhost) {
		ApolloUtil.frontWebhost = frontWebhost;
	}

	@Value("${apiurl:}")
	public void setApiUrl(String apiUrl) {
		ApolloUtil.apiUrl = apiUrl;
	}
	/**=================update-tabale-goodsinfo======================*/
	
	@Value("${updateOrderAttribute:}")
	public void setUpdateOrderAttribute(String updateOrderAttribute) {
		ApolloUtil.updateOrderAttribute = updateOrderAttribute;
	}
	@Value("${updateInventoryAttribute:}")
	public void setUpdateInventoryAttribute(String updateInventoryAttribute) {
		ApolloUtil.updateInventoryAttribute = updateInventoryAttribute;
	}
	@Value("${updateStorageAttribute:}")
	public void setUpdateStorageAttribute(String updateStorageAttribute) {
		ApolloUtil.updateStorageAttribute = updateStorageAttribute;
	}
	@Value("${updateReportAttribute:}")
	public void setUpdateReportAttribute(String updateReportAttribute) {
		ApolloUtil.updateReportAttribute = updateReportAttribute;
	}
	/**=================external-api======================*/
	@Value("${op_v2_14:}")
	public void setOpV214(String opV214) {
		ApolloUtil.opV214 = opV214;
	}
	@Value("${epass_v:}")
	public void setEpassV(String epassV) {
		ApolloUtil.epassV = epassV;
	}
	@Value("${epass_appSecret:}")
	public void setEpassAppSecret(String epassAppSecret) {
		ApolloUtil.epassAppSecret = epassAppSecret;
	}
	@Value("${epass_appKey:}")
	public void setEpassAppKey(String epassAppKey) {
		ApolloUtil.epassAppKey = epassAppKey;
	}
	@Value("${epass_api:}")
	public void setEpassApi(String epassApi) {
		ApolloUtil.epassApi = epassApi;
	}
	@Value("${epass_001_method:}")
	public void setEpass001Method(String epass001Method) {
		ApolloUtil.epass001Method = epass001Method;
	}
	@Value("${epass_002_method:}")
	public void setEpass002Method(String epass002Method) {
		ApolloUtil.epass002Method = epass002Method;
	}
	@Value("${epass_003_method:}")
	public void setEpass003Method(String epass003Method) {
		ApolloUtil.epass003Method = epass003Method;
	}
	@Value("${epass_004_method:}")
	public void setEpass004Method(String epass004Method) {
		ApolloUtil.epass004Method = epass004Method;
	}
	@Value("${ofc_c2_9:}")
	public void setOfcC29(String ofcC29) {
		ApolloUtil.ofcC29 = ofcC29;
	}
	/*@Value("${smurfs_api:}")
	public void setSmurfsApi(String smurfsApi) {
		ApolloUtil.smurfsApi = smurfsApi;
	}*/
	@Value("${smurfs_appkey:}")
	public void setSmurfsAppkey(String smurfsAppkey) {
		ApolloUtil.smurfsAppkey = smurfsAppkey;
	}
	@Value("${smurfs_appsecret:}")
	public void setSmurfsAppsecret(String smurfsAppsecret) {
		ApolloUtil.smurfsAppsecret = smurfsAppsecret;
	}
	@Value("${smurfs_recipients:}")
	public void setSmurfsRecipients(String smurfsRecipients) {
		ApolloUtil.smurfsRecipients = smurfsRecipients;
	}
	@Value("${smurfs_host:}")
	public void setSmurfsHost(String smurfsHost) {
		ApolloUtil.smurfsHost = smurfsHost;
	}
	@Value("${smurfs_upload_version:}")
	public void setSmurfsUploadVersion(String smurfsUploadVersion) {
		ApolloUtil.smurfsUploadVersion = smurfsUploadVersion;
	}
	/*@Value("${smurfs_log_api:}")
	public void setSmurfsLogApi(String smurfsLogApi) {
		ApolloUtil.smurfsLogApi = smurfsLogApi;
	}*/
	@Value("${smurfs_log_appkey:}")
	public void setSmurfsLogAppkey(String smurfsLogAppkey) {
		ApolloUtil.smurfsLogAppkey = smurfsLogAppkey;
	}
	@Value("${smurfs_log_appsecret:}")
	public void setSmurfsLogAppsecret(String smurfsLogAppsecret) {
		ApolloUtil.smurfsLogAppsecret = smurfsLogAppsecret;
	}
	@Value("${smurfs_log_recipients:}")
	public void setSmurfsLogRecipients(String smurfsLogRecipients) {
		ApolloUtil.smurfsLogRecipients = smurfsLogRecipients;
	}
	@Value("${smurfs_log_host:}")
	public void setSmurfsLogHost(String smurfsLogHost) {
		ApolloUtil.smurfsLogHost = smurfsLogHost;
	}
	/*@Value("${smurfs_log_path:}")
	public void setSmurfsLogPath(String smurfsLogPath) {
		ApolloUtil.smurfsLogPath = smurfsLogPath;
	}*/
	@Value("${ews_w4_10:}")
	public void setEwsW410(String ewsW410) {
		ApolloUtil.ewsW410 = ewsW410;
	}
	@Value("${ews_topidealcode:}")
	public void setEwsTopidealcode(String ewsTopidealcode) {
		ApolloUtil.ewsTopidealcode = ewsTopidealcode;
	}
	@Value("${gss_g2_url:}")
	public void setGssG2Url(String gssG2Url) {
		ApolloUtil.gssG2Url = gssG2Url;
	}
	@Value("${gss_g2_batch_url:}")
	public void setGssG2batchUrl(String gssG2batchUrl) {
		ApolloUtil.gssG2batchUrl = gssG2batchUrl;
	}
	@Value("${finance_api:}")
	public void setFinanceApi(String financeApi) {
		ApolloUtil.financeApi = financeApi;
	}
	@Value("${finance_appkey:}")
	public void setFinanceAppkey(String financeAppkey) {
		ApolloUtil.financeAppkey = financeAppkey;
	}
	@Value("${finance_appsecret:}")
	public void setFinanceAppsecret(String financeAppsecret) {
		ApolloUtil.financeAppsecret = financeAppsecret;
	}
	@Value("${finance_v:}")
	public void setFinanceV(String financeV) {
		ApolloUtil.financeV = financeV;
	}
	@Value("${finance_001_method:}")
	public void setFinance001Method(String finance001Method) {
		ApolloUtil.finance001Method = finance001Method;
	}
	@Value("${finance_002_method:}")
	public void setFinance002Method(String finance002Method) {
		ApolloUtil.finance002Method = finance002Method;
	}


	@Value("${finance_api2:}")
	public void setFinanceApi2(String financeApi2) {
		ApolloUtil.financeApi2 = financeApi2;
	}
	@Value("${finance_appkey2:}")
	public void setFinanceAppkey2(String financeAppkey2) {
		ApolloUtil.financeAppkey2 = financeAppkey2;
	}
	@Value("${finance_appsecret2:}")
	public void setFinanceAppsecret2(String financeAppsecret2) {
		ApolloUtil.financeAppsecret2 = financeAppsecret2;
	}
	@Value("${finance_v2:}")
	public void setFinanceV2(String financeV2) {
		ApolloUtil.financeV2 = financeV2;
	}
	@Value("${finance_001_method2:}")
	public void setFinance001Method2(String finance001Method2) {
		ApolloUtil.finance001Method2 = finance001Method2;
	}
	@Value("${finance_002_method2:}")
	public void setFinance002Method2(String finance002Method2) {
		ApolloUtil.finance002Method2 = finance002Method2;
	}
	@Value("${finance_003_method2:}")
	public void setFinance003Method2(String finance003Method2) {
		ApolloUtil.finance003Method2 = finance003Method2;
	}
	
	
	@Value("${finance_api3:}")
	public void setFinanceApi3(String financeApi3) {
		ApolloUtil.financeApi3 = financeApi3;
	}
	@Value("${finance_001_method3:}")
	public void setFinance001Method3(String finance001Method3) {
		ApolloUtil.finance001Method3 = finance001Method3;
	}
	@Value("${currency_api:}")
	public void setCurrencyApi(String currencyApi) {
		ApolloUtil.currencyApi = currencyApi;
	}
	@Value("${currency_appkey:}")
	public void setCurrencyAppkey(String currencyAppkey) {
		ApolloUtil.currencyAppkey = currencyAppkey;
	}
	@Value("${currency_appsecret:}")
	public void setCurrencyAppsecret(String currencyAppsecret) {
		ApolloUtil.currencyAppsecret = currencyAppsecret;
	}
	@Value("${currency_v:}")
	public void setCurrencyV(String currencyV) {
		ApolloUtil.currencyV = currencyV;
	}
	@Value("${currency_001_method:}")
	public void setCurrency001Method(String currency001Method) {
		ApolloUtil.currency001Method = currency001Method;
	}
	@Value("${currency_002_method:}")
	public void setCurrency002Method(String currency002Method) {
		ApolloUtil.currency002Method = currency002Method;
	}
	@Value("${taobao_api:}")
	public void setTaobaoApi(String taobaoApi) {
		ApolloUtil.taobaoApi = taobaoApi;
	}
	@Value("${maindata_syncGoods_url:}")
	public void setMaindataSyncGoodsUrl(String maindataSyncGoodsUrl) {
		ApolloUtil.maindataSyncGoodsUrl = maindataSyncGoodsUrl;
	}
	@Value("${maindata_source:}")
	public void setMaindataSource(String maindataSource) {
		ApolloUtil.maindataSource = maindataSource;
	}
	@Value("${maindata_sync_url:}")
	public void setMaindataSyncUrl(String maindataSyncUrl) {
		ApolloUtil.maindataSyncUrl = maindataSyncUrl;
	}
	@Value("${maindata_sync_001_Method:}")
	public void setMaindataSync001Method(String maindataSync001Method) {
		ApolloUtil.maindataSync001Method = maindataSync001Method;
	}
	
	@Value("${ywms_api:}")
	public void setYwmsApi(String ywmsApi) {
		ApolloUtil.ywmsApi = ywmsApi;
	}
	@Value("${ywms_appkey:}")
	public void setYwmsAppkey(String ywmsAppkey) {
		ApolloUtil.ywmsAppkey = ywmsAppkey;
	}
	@Value("${ywms_secret:}")
	public void setYwmsSecret(String ywmsSecret) {
		ApolloUtil.ywmsSecret = ywmsSecret;
	}

	@Value("${ywms_format:}")
	public void setYwmsFormat(String ywmsFormat) {
		ApolloUtil.ywmsFormat = ywmsFormat;
	}
	@Value("${ywms_customerId:}")
	public void setYwmsCustomerId(String ywmsCustomerId) {
		ApolloUtil.ywmsCustomerId = ywmsCustomerId;
	}
	@Value("${ywms_v:}")
	public void setYwmsV(String ywmsV) {
		ApolloUtil.ywmsV = ywmsV;
	}
	@Value("${ywms_ownerCode:}")
	public void setYwmsOwnerCode(String ywmsOwnerCode) {
		ApolloUtil.ywmsOwnerCode = ywmsOwnerCode;
	}
	@Value("${ywms_topidealCode:}")
	public void setYwmsTopidealCode(String ywmsTopidealCode) {
		ApolloUtil.ywmsTopidealCode = ywmsTopidealCode;
	}
	@Value("${ywms_goodsNos:}")
	public void setYwmsGoodsNos(String ywmsGoodsNos) {
		ApolloUtil.ywmsGoodsNos = ywmsGoodsNos;
	}
	@Value("${nc_api:}")
	public void setNcApi(String ncApi) {
		ApolloUtil.ncApi = ncApi;
	}
	@Value("${nc_appKey:}")
	public void setNcAppKey(String ncAppKey) {
		ApolloUtil.ncAppKey = ncAppKey;
	}
	@Value("${nc_sourceType:}")
	public void setNcSourceType(String sourceType) {
		ApolloUtil.ncSourceType = sourceType;
	}
	@Value("${nc_publicKey:}")
	public void setNcPublicKey(String ncPublicKey) {
		ApolloUtil.ncPublicKey = ncPublicKey;
	}
	@Value("${nc_privateKey:}")
	public void setNcPrivateKey(String ncPrivateKey) {
		ApolloUtil.ncPrivateKey = ncPrivateKey;
	}
	@Value("${sapience_url:}")
	public  void setSapienceUrl(String sapienceUrl) {
		ApolloUtil.sapienceUrl = sapienceUrl;
	}
	@Value("${sapience_appKey:}")
	public void setSapienceAppKey(String sapienceAppKey) {
		ApolloUtil.sapienceAppKey = sapienceAppKey;
	}
	@Value("${sapience_appSecret:}")
	public void setSapienceAppSecret(String sapienceAppSecret) {
		ApolloUtil.sapienceAppSecret = sapienceAppSecret;
	}
	@Value("${oreal_url:}")
	public void setOrealUrl(String orealUrl) {
		ApolloUtil.orealUrl = orealUrl;
	}
	@Value("${oreal_token_username:}")
	public void setOrealTokenUsername(String orealTokenUsername) {
		ApolloUtil.orealTokenUsername = orealTokenUsername;
	}
	@Value("${oreal_token_password:}")
	public void setOrealTokenPassword(String orealTokenPassword) {
		ApolloUtil.orealTokenPassword = orealTokenPassword;
	}
	@Value("${oreal_auth_username:}")
	public void setOrealAuthUsername(String orealAuthUsername) {
		ApolloUtil.orealAuthUsername = orealAuthUsername;
	}
	@Value("${oreal_auth_password:}")
	public void setOrealAuthPassword(String orealAuthPassword) {
		ApolloUtil.orealAuthPassword = orealAuthPassword;
	}
	@Value("${oreal_account_username:}")
	public void setOrealAccountUsername(String orealAccountUsername) {
		ApolloUtil.orealAccountUsername = orealAccountUsername;
	}
	@Value("${oreal_001_method:}")
	public void setOreal001Method(String oreal001Method) {
		ApolloUtil.oreal001Method = oreal001Method;
	}
	@Value("${oreal_002_method:}")
	public void setOreal002Method(String oreal002Method) {
		ApolloUtil.oreal002Method = oreal002Method;
	}
	@Value("${dstp_url:}")
	public void setDstpUrl(String dstpUrl) {
		ApolloUtil.dstpUrl = dstpUrl;
	}
	@Value("${dstp_appkey_0000138:}")
	public void setDstpAppkey_0000138(String dstpAppkey_0000138) {
		ApolloUtil.dstpAppkey_0000138 = dstpAppkey_0000138;
	}
	@Value("${dstp_appkey_1000000204:}")
	public void setDstpAppkey_1000000204(String dstpAppkey_1000000204) {
		ApolloUtil.dstpAppkey_1000000204 = dstpAppkey_1000000204;
	}
	@Value("${dstp_appkey_1000000626:}")
	public void setDstpAppkey_1000000626(String dstpAppkey_1000000626) {
		ApolloUtil.dstpAppkey_1000000626 = dstpAppkey_1000000626;
	}
	@Value("${dstp_appkey_0000134:}")
	public void setDstpAppkey_0000134(String dstpAppkey_0000134) {
		ApolloUtil.dstpAppkey_0000134 = dstpAppkey_0000134;
	}
	@Value("${dstp_appsecret_0000138:}")
	public void setDstpAppsecret_0000138(String dstpAppsecret_0000138) {
		ApolloUtil.dstpAppsecret_0000138 = dstpAppsecret_0000138;
	}
	@Value("${dstp_appsecret_1000000204:}")
	public void setDstpAppsecret_1000000204(String dstpAppsecret_1000000204) {
		ApolloUtil.dstpAppsecret_1000000204 = dstpAppsecret_1000000204;
	}
	@Value("${dstp_appsecret_1000000626:}")
	public void setDstpAppsecret_1000000626(String dstpAppsecret_1000000626) {
		ApolloUtil.dstpAppsecret_1000000626 = dstpAppsecret_1000000626;
	}
	@Value("${dstp_appsecret_0000134:}")
	public void setDstpAppsecret_0000134(String dstpAppsecret_0000134) {
		ApolloUtil.dstpAppsecret_0000134 = dstpAppsecret_0000134;
	}

	/**单点登录===================================*/
	@Value("${cas.server-url-prefix:}")
	public void setServerUrlPrefix(String serverUrlPrefix) {
		ApolloUtil.serverUrlPrefix = serverUrlPrefix;
	}
	@Value("${cas.client-host-url:}")
	public void setClientHostUrl(String clientHostUrl) {
		ApolloUtil.clientHostUrl = clientHostUrl;
	}

	@Value("${idmappuser:}")
	public  void setAppUser(String appUser) {
		ApolloUtil.appUser = appUser;
	}
	@Value("${idmappkey:}")
	public  void setAppKey(String appKey) {
		ApolloUtil.appKey = appKey;
	}
	@Value("${idmsimurl:}")
	public  void setSimUrl(String simUrl) {
		ApolloUtil.simUrl = simUrl;
	}
	@Value("${idmauthstr:}")
	public void setIdmAuthStr(String idmAuthStr) {
		ApolloUtil.idmAuthStr = idmAuthStr;
	}

	@Value("${system.name:}")
	public  void setSystemName(String systemName) {
		ApolloUtil.systemName = systemName;
	}

	/**单点登录===================================*/
	/**机器人===================================*/
	@Value("${jx_webhook_url:}")
	public void setJxWebhookUrl(String jxWebhookUrl) {
		ApolloUtil.jxWebhookUrl = jxWebhookUrl;
	}
	@Value("${jx_remark:}")
	public void setJxRemark(String jxRemark) {
		ApolloUtil.jxRemark = jxRemark;
	}
	@Value("${jx_enable:}")
	public void setJxEnable(String jxEnable) {
		ApolloUtil.jxEnable = jxEnable;
	}
	/**机器人===================================*/
	
	/**OA接口地址===========================*/
	@Value("${oa_url1:}")
	public void setOaUrl1(String oaUrl1) {
		ApolloUtil.oaUrl1 = oaUrl1;
	}
	@Value("${oa_url2:}")
	public void setOaUrl2(String oaUrl2) {
		ApolloUtil.oaUrl2 = oaUrl2;
	}
	
	@Value("${oa_workflow_id:}")
	public void setOaWorkflowId(String oaWorkflowId) {
		ApolloUtil.oaWorkflowId = oaWorkflowId;
	}
	@Value("${oa_lyxt:}")
	public void setOaLyxt(String oaLyxt) {
		ApolloUtil.oaLyxt = oaLyxt;
	}

	@Value("${oa_purchase_frame_contract_url:}")
	public void setOaPurchaseFrameContractUrl(String oaPurchaseFrameContractUrl) {
		ApolloUtil.oaPurchaseFrameContractUrl = oaPurchaseFrameContractUrl;
	}

	@Value("${oa_purchase_try_apply_url:}")
	public void setOaPurchaseTryApplyContractUrl(String oaPurchaseTryApplyContractUrl) {
		ApolloUtil.oaPurchaseTryApplyContractUrl = oaPurchaseTryApplyContractUrl;
	}

	@Value("${oa_purchase_frame_contract_formId:}")
	public void setOaPurchaseFrameContractFormId(String oaPurchaseFrameContractFormId) {
		ApolloUtil.oaPurchaseFrameContractFormId = oaPurchaseFrameContractFormId;
	}

	@Value("${oa_purchase_try_apply_formId:}")
	public void setOaPurchaseTryApplyContractFormId(String oaPurchaseTryApplyContractFormId) {
		ApolloUtil.oaPurchaseTryApplyContractFormId = oaPurchaseTryApplyContractFormId;
	}

	@Value("${oa_purchase_workflow_id_url:}")
	public void setOaPurchaseWorkflowIdUrl(String oaPurchaseWorkflowIdUrl) {
		ApolloUtil.oaPurchaseWorkflowIdUrl = oaPurchaseWorkflowIdUrl;
	}

	@Value("${oa_purchase_workflow_id:}")
	public void setOaPurchaseWorkflowId(String oaPurchaseWorkflowId) {
		ApolloUtil.oaPurchaseWorkflowId = oaPurchaseWorkflowId;
	}

	/**PMS接口地址===========================*/
	@Value("${pms_url:}")
	public void setPmsUrl(String pmsUrl) {
		ApolloUtil.pmsUrl = pmsUrl;
	}
	
	
	/**OA接口===========================*/


	/**内部交易金额数量差异接口===========================*/
	@Value("${baoxin_email:}")
	public void setBaoxinEmail(String baoxinEmail) {
		ApolloUtil.baoxinEmail = baoxinEmail;
	}
	@Value("${baojian_email:}")
	public void setBaojianEmail(String baojianEmail) { ApolloUtil.baojianEmail = baojianEmail; }
	@Value("${muying_email:}")
	public void setMuyingEmail(String muyingEmail) {
		ApolloUtil.muyingEmail = muyingEmail;
	}

	/**内部交易金额数量差异接口===========================*/

	/**实施以及开发接收人 一般可用于数据预警提示 start========================================*/
	@Value("${internal_alert_email:}")
	public void setInternalAlertEmail(String internalAlertEmail) {
		ApolloUtil.internalAlertEmail = internalAlertEmail;
	}
/**实施以及开发接收人 一般可用于数据预警提示 end========================================*/
}
