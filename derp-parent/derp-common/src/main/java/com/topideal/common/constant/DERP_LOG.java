package com.topideal.common.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DERP_LOG {

	/**爬虫商家 1000030:嘉宝    1000031:健太    1000032:润佰   1000033:广旺    1000034:云集   1000038:卓烨*/
	public static final String LOG_CRAWLER_MERCHANT_1000030 = "1000030" ;
	public static final String LOG_CRAWLER_MERCHANT_1000031 = "1000031" ;
	public static final String LOG_CRAWLER_MERCHANT_1000032 = "1000032" ;
	public static final String LOG_CRAWLER_MERCHANT_1000033 = "1000033" ;
	public static final String LOG_CRAWLER_MERCHANT_1000034 = "1000034" ;
	public static final String LOG_CRAWLER_MERCHANT_1000038 = "1000038" ;
	public static ArrayList<DerpBasic> log_crawlerMerchantList = new ArrayList<DerpBasic>();
	
	/**智能重推类型 1:B2C单号不存在  2:冻结记录不存在  3:锁记录 4:单号不存在*/
	public static final String LOG_AUTOLOG_EXPTYPE_1 = "1" ;
	public static final String LOG_AUTOLOG_EXPTYPE_2 = "2" ;
	public static final String LOG_AUTOLOG_EXPTYPE_3 = "3" ;
	public static final String LOG_AUTOLOG_EXPTYPE_4 = "4" ;
	public static ArrayList<DerpBasic> log_autologExpTypeList = new ArrayList<DerpBasic>();
	
	/**智能重推单据类型 0:冻结 1:解冻 2:国检 3:海关*/
	public static final String LOG_AUTOLOG_TYPE_0 = "0" ;
	public static final String LOG_AUTOLOG_TYPE_1 = "1" ;
	public static final String LOG_AUTOLOG_TYPE_2 = "2" ;
	public static final String LOG_AUTOLOG_TYPE_3 = "3" ;
	public static ArrayList<DerpBasic> log_autologTypeList = new ArrayList<DerpBasic>();
	
	/**日志状态 0:失败 1:成功 2:已关闭 3:重推成功*/
	public static final String LOG_STATUS_0 = "0" ;
	public static final String LOG_STATUS_1 = "1" ;
	public static final String LOG_STATUS_2 = "2" ;
	public static final String LOG_STATUS_3 = "3" ;
	public static ArrayList<DerpBasic> log_statusList = new ArrayList<DerpBasic>();
	
	/**查询范围 1: 0-60天 2: 61-180天*/
	public static final String LOG_SELECT_SCOPE_1 = "1" ;
	public static final String LOG_SELECT_SCOPE_2 = "2" ;
	public static ArrayList<DerpBasic> log_selectScopeList = new ArrayList<DerpBasic>();
	
	/**耗时 1: 小于0.5秒  2: 0.5秒-1秒内  3: 1秒以上*/
	public static final String LOG_DIFFERENCE_1 = "1" ;
	public static final String LOG_DIFFERENCE_2 = "2" ;
	public static final String LOG_DIFFERENCE_3 = "3" ;
	public static ArrayList<DerpBasic> log_differenceList = new ArrayList<DerpBasic>();
	
	/**模块 1：业务模块 2：推送外部API 3：仓储模块  4：库存模块  5：api模块 6：报表模块*/
	public static final String LOG_MODELCODE_1 = "1" ;
	public static final String LOG_MODELCODE_2 = "2" ;
	public static final String LOG_MODELCODE_3 = "3" ;
	public static final String LOG_MODELCODE_4 = "4" ;
	public static final String LOG_MODELCODE_5 = "5" ;
	public static final String LOG_MODELCODE_6 = "6" ;
	public static ArrayList<DerpBasic> log_modelCodeList = new ArrayList<DerpBasic>();
	
	/**推送平台 000：经分销  001:主数据  002:跨境宝*/
	public static final String LOG_DERPCODE_000 = "000" ;
	public static final String LOG_DERPCODE_001 = "001" ;
	public static final String LOG_DERPCODE_002 = "002" ;
	public static final String LOG_DERPCODE_003 = "003" ;
	public static ArrayList<DerpBasic> log_derpcodeList = new ArrayList<DerpBasic>();
	
	/**流水是否关闭状态 1-是，0-否*/
	public static final String LOG_STREAM_STATUS_1 = "1" ;
	public static final String LOG_STREAM_STATUS_0 = "0" ;
	public static ArrayList<DerpBasic> log_streamStatusList = new ArrayList<DerpBasic>();
	
	/**日志错误类型*/
	public static final String LOG_ORDER_ERROR_TYPE_00 = "00" ;
	public static final String LOG_ORDER_ERROR_TYPE_01 = "01" ;
	public static final String LOG_ORDER_ERROR_TYPE_02 = "02" ;
	public static final String LOG_ORDER_ERROR_TYPE_03 = "03" ;
	public static final String LOG_ORDER_ERROR_TYPE_04 = "04" ;
	public static final String LOG_ORDER_ERROR_TYPE_05 = "05" ;
	public static final String LOG_ORDER_ERROR_TYPE_06 = "06" ;
	public static final String LOG_ORDER_ERROR_TYPE_07 = "07" ;
	public static final String LOG_ORDER_ERROR_TYPE_08 = "08" ;
	public static final String LOG_ORDER_ERROR_TYPE_09 = "09" ;
	public static ArrayList<DerpBasic> logOrderErrorTypeList = new ArrayList<DerpBasic>();
	
	public static final String LOG_INVERTORY_ERROR_TYPE_00 = "00" ;
	public static final String LOG_INVERTORY_ERROR_TYPE_01 = "01" ;
	public static final String LOG_INVERTORY_ERROR_TYPE_02 = "02" ;
	public static ArrayList<DerpBasic> logInvertoryErrorTypeList = new ArrayList<DerpBasic>();
	
	public static final String LOG_API_ERROR_TYPE_00 = "00" ;
	public static final String LOG_API_ERROR_TYPE_01 = "01" ;
	public static final String LOG_API_ERROR_TYPE_02 = "02" ;
	public static final String LOG_API_ERROR_TYPE_03 = "03" ;
	public static final String LOG_API_ERROR_TYPE_04 = "04" ;
	public static final String LOG_API_ERROR_TYPE_05 = "05" ;
	public static ArrayList<DerpBasic> logApiErrorTypeList = new ArrayList<DerpBasic>();
	/**爬虫平台类型*/
	public static final String LOG_CRAWLER_PLATFORMTYPE_1 = "1" ;
	public static final String LOG_CRAWLER_PLATFORMTYPE_2 = "2" ;
	public static ArrayList<DerpBasic> logCrawlerPlatformtypeList = new ArrayList<DerpBasic>();
	
	static{
		/**爬虫商家 1000030:嘉宝    1000031:健太    1000032:润佰   1000033:广旺    1000034:云集   1000038:卓烨*/
		log_crawlerMerchantList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_MERCHANT_1000030,"嘉宝")) ;
		log_crawlerMerchantList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_MERCHANT_1000031,"健太")) ;
		log_crawlerMerchantList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_MERCHANT_1000032,"润佰")) ;
		log_crawlerMerchantList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_MERCHANT_1000033,"广旺")) ;
		log_crawlerMerchantList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_MERCHANT_1000034,"云集")) ;
		log_crawlerMerchantList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_MERCHANT_1000038,"卓烨")) ;
		
		/**爬虫平台类型 1：云集 2：唯品*/
		logCrawlerPlatformtypeList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_PLATFORMTYPE_1,"云集")) ;
		logCrawlerPlatformtypeList.add(new DerpBasic(DERP_LOG.LOG_CRAWLER_PLATFORMTYPE_2,"唯品")) ;
		
		/**智能重推类型 1:B2C单号不存在  2:冻结记录不存在  3:锁记录 4:单号不存在*/
		log_autologExpTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_EXPTYPE_1,"B2C单号不存在"));
		log_autologExpTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_EXPTYPE_2,"冻结记录不存在"));
		log_autologExpTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_EXPTYPE_3,"锁记录"));
		log_autologExpTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_EXPTYPE_4,"单号不存在"));
		
		/**智能重推单据类型 0:冻结 1:解冻 2:国检 3:海关*/
		log_autologTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_TYPE_0,"冻结"));
		log_autologTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_TYPE_1,"解冻"));
		log_autologTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_TYPE_2,"国检"));
		log_autologTypeList.add(new DerpBasic(DERP_LOG.LOG_AUTOLOG_TYPE_3,"海关"));
		
		/**流水是否关闭状态 1-是，0-否*/
		log_streamStatusList.add(new DerpBasic(DERP_LOG.LOG_STREAM_STATUS_1,"是"));
		log_streamStatusList.add(new DerpBasic(DERP_LOG.LOG_STREAM_STATUS_0,"否"));
		
		/**推送平台 000：经分销   001:主数据  002:跨境宝*/
		log_derpcodeList.add(new DerpBasic(DERP_LOG.LOG_DERPCODE_000,"经分销"));
		log_derpcodeList.add(new DerpBasic(DERP_LOG.LOG_DERPCODE_001,"主数据"));
		log_derpcodeList.add(new DerpBasic(DERP_LOG.LOG_DERPCODE_002,"跨境宝"));
		log_derpcodeList.add(new DerpBasic(DERP_LOG.LOG_DERPCODE_003,"奇门"));
		
		/**模块 1：业务模块 2：推送外部API 3：仓储模块  4：库存模块  6：报表模块*/
		log_modelCodeList.add(new DerpBasic(DERP_LOG.LOG_MODELCODE_1,"业务模块"));
		log_modelCodeList.add(new DerpBasic(DERP_LOG.LOG_MODELCODE_2,"推送外部API"));
		log_modelCodeList.add(new DerpBasic(DERP_LOG.LOG_MODELCODE_3,"仓储模块"));
		log_modelCodeList.add(new DerpBasic(DERP_LOG.LOG_MODELCODE_4,"库存模块"));
		log_modelCodeList.add(new DerpBasic(DERP_LOG.LOG_MODELCODE_5,"API模块"));
		log_modelCodeList.add(new DerpBasic(DERP_LOG.LOG_MODELCODE_6,"报表模块"));
		
		/**耗时 1: 小于0.5秒  2: 0.5秒-1秒内  3: 1秒以上*/
		log_differenceList.add(new DerpBasic(DERP_LOG.LOG_DIFFERENCE_1,"小于0.5秒"));
		log_differenceList.add(new DerpBasic(DERP_LOG.LOG_DIFFERENCE_2,"0.5秒-1秒内"));
		log_differenceList.add(new DerpBasic(DERP_LOG.LOG_DIFFERENCE_3,"1秒以上"));
		
		/**日志状态 0:失败 1:成功 2:已关闭 3:重推成功*/
		log_statusList.add(new DerpBasic(DERP_LOG.LOG_STATUS_0,"失败"));
		log_statusList.add(new DerpBasic(DERP_LOG.LOG_STATUS_1,"成功"));
		log_statusList.add(new DerpBasic(DERP_LOG.LOG_STATUS_2,"已关闭"));
		log_statusList.add(new DerpBasic(DERP_LOG.LOG_STATUS_3,"重推成功"));
		
		/**查询范围 1- 0-60天 2- 61-180天*/
		log_selectScopeList.add(new DerpBasic(DERP_LOG.LOG_SELECT_SCOPE_1,"0-60天"));
		log_selectScopeList.add(new DerpBasic(DERP_LOG.LOG_SELECT_SCOPE_2,"61-180天"));
		
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_00,"商品映射缺失维护"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_01,"订单不存在"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_02,"店铺编码不存在"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_03,"订单参数为空"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_04,"报文异常"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_05,"其他异常"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_06,"盘点结果状态异常"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_07,"类型调整单已存在"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_08,"好坏品互转异常"));
		logOrderErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_ORDER_ERROR_TYPE_09,"推送异常"));
		
		logInvertoryErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_INVERTORY_ERROR_TYPE_00,"库存不足"));
		logInvertoryErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_INVERTORY_ERROR_TYPE_01,"冻结异常"));
		logInvertoryErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_INVERTORY_ERROR_TYPE_02,"其他异常"));
		
		logApiErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_API_ERROR_TYPE_00,"非经分销单据")) ;
		logApiErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_API_ERROR_TYPE_01,"签名错误")) ;
		logApiErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_API_ERROR_TYPE_02,"订单无订单来源")) ;
		logApiErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_API_ERROR_TYPE_03,"标准条码为空")) ;
		logApiErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_API_ERROR_TYPE_04,"商品不存在")) ;
		logApiErrorTypeList.add(new DerpBasic(DERP_LOG.LOG_API_ERROR_TYPE_05,"其他异常")) ;
		
	}
	
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
	@SuppressWarnings("unchecked")
	public static ArrayList<DerpBasic> getConstantListByName(String listName){
		   ArrayList<DerpBasic> list = null;
		   try{
			   Field[] fields = DERP_LOG.class.getDeclaredFields();
		       for(Field field:fields){
		          if(field.getName().equals(listName)){
		        	 list = (ArrayList<DerpBasic>) field.get(new DERP_LOG());
		          }
		       }
		   }catch(Exception e){}
		   return list;
	}
}
