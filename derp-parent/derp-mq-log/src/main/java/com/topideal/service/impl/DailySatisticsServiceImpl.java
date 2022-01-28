package com.topideal.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.dao.ConsumeMonitorApiDao;
import com.topideal.dao.ConsumeMonitorOrderDao;
import com.topideal.dao.LogStreamApiDao;
import com.topideal.dao.LogStreamOrderDao;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.service.DailyStatisticsService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 统计MQ消费情况-发送邮件
 */
@Service
public class DailySatisticsServiceImpl implements DailyStatisticsService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DailySatisticsServiceImpl.class);
	// MQ消费监控
	@Autowired
	private ConsumeMonitorOrderDao consumeMonitorOrderDao;
	// MQ消费流水
	@Autowired
	LogStreamOrderDao logStreamOrderDao;
	// api消费流水
	@Autowired
	LogStreamApiDao logStreamApiDao;
	//api监控
	@Autowired
	ConsumeMonitorApiDao consumeMonitorApiDao;
		
	@Override
	public boolean sendMail(String json, String topics, String tags) throws Exception {
		LOGGER.info("-----------------统计MQ消费情况-发送邮件开始----------------------");
		//业务参数=========================
        JSONObject jsonObject = new JSONObject();//推送内容
        jsonObject.put("mailCode",SmurfsAPICodeEnum.EMAIL_M014.getCode());
        JSONObject paramJson=new JSONObject();//存储所有数据的结果
        JSONObject data=new JSONObject();//存储数据结果集
        
        
        //当前月份接口统计表
        //本月第一天
      	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
      	paramJson.put("reportDate", sdf.format(new Date())) ;
      	
        Calendar calendar=Calendar.getInstance();
        Integer month = calendar.get(Calendar.MONTH);
        int dateOfMonth = calendar.get(Calendar.DATE); 
        if(dateOfMonth == 1){//本月的第一天，统计上个月的数据
        	month = month-1;
        }
        calendar.set(calendar.get(Calendar.YEAR), month, 1, 0, 0, 0);
        String sDate = sdf.format(calendar.getTime());
        
        //本月最后一天
        Calendar c=Calendar.getInstance();
		int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(c.get(Calendar.YEAR), month, lastMonthMaxDay+1, 0, 0, 0);
		String eDate = sdf.format(c.getTime());
		
		//获取前一天的时间
        sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date);
		calendar1.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar1.getTime();
        String startDate = sdf.format(date);
        Date date1=new Date();
        String endDate = sdf.format(date1);
		
		/**
		 * API统计模块
		 */
        //获取本月API成功数
        Long requestSuccNum = consumeMonitorApiDao.getSuccNum(sDate,eDate);
        //获取本月API已处理的失败数
        Long treatedNum = consumeMonitorApiDao.getTreatedNum(sDate,eDate) ;
        //获取本月API未处理的失败数
        Long untreatedNum = consumeMonitorApiDao.getUntreatedNum(sDate,eDate);
        //获取本月API接收请求数
        //Long requestNum = logStreamApiDao.getRequestNum(sDate,eDate);
        Long requestNum = requestSuccNum + treatedNum + untreatedNum ;
        
        //昨日新增
        //获取昨日API未处理的失败数
        Long lastUntreatedNum = consumeMonitorApiDao.getUntreatedNum(startDate,endDate);
        
        data.put("apiLastTreateNum", lastUntreatedNum);
        
        /*//获取本月已处理的失败数
        Long treatedNum = consumeMonitorOrderDao.getTreatedNum(sDate,eDate);
        //获取本月未处理的失败数
        Long untreatedNum = consumeMonitorOrderDao.getUntreatedNum(sDate,eDate);*/
        
        String apiRateStr = "" ;
        //请求成功率
        if(requestSuccNum == 0){
        	apiRateStr = "0%";
    	}else if(requestSuccNum ==requestNum){
    		apiRateStr = "100%";
    	}else{
    		BigDecimal rate = new BigDecimal(requestSuccNum).divide(new BigDecimal(requestNum),3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
    		apiRateStr = rate.doubleValue()+"%";
    	}
        
        String apiTreatedRateStr = "" ;
        //处理成功率
        if(treatedNum == 0){
        	apiTreatedRateStr = "0%";
    	}else if(untreatedNum == 0){
    		apiTreatedRateStr = "100%";
    	}else{
    		BigDecimal rate = new BigDecimal(treatedNum).divide(new BigDecimal(treatedNum).add(new BigDecimal(untreatedNum)),3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
    		apiTreatedRateStr = rate.doubleValue()+"%";
    	}
        
        JSONArray apiJsonArray = new JSONArray();
        JSONObject apiJsonModel=new JSONObject();
        apiJsonModel.put("requestNum",requestNum);
        apiJsonModel.put("requestSuccNum",requestSuccNum);
        apiJsonModel.put("requestSuccRate",apiRateStr);
        apiJsonModel.put("treatedNum",treatedNum);
        apiJsonModel.put("untreatedNum",untreatedNum);
        apiJsonModel.put("apiTreatedRate",apiTreatedRateStr);
        apiJsonArray.add(apiJsonModel);
        data.put("monthApiList", apiJsonArray);
        
        /**
         * API错误类型统计
         */
        JSONArray apiErrorTypeJsonArray = new JSONArray();
        JSONObject apiErrorTypeJsonModel=new JSONObject();
        apiErrorTypeJsonModel.put("errorType", "总计") ;
        apiErrorTypeJsonModel.put("errorNum", new BigDecimal(treatedNum).add(new BigDecimal(untreatedNum)).toString()) ;
        apiErrorTypeJsonModel.put("unTreatErrorNum", untreatedNum) ;
        apiErrorTypeJsonModel.put("rate", apiTreatedRateStr) ;
        apiErrorTypeJsonArray.add(apiErrorTypeJsonModel) ;
        
        List<Map<String, Object>> errorTypeList = consumeMonitorApiDao.getErrorNumGroupByType(sDate, eDate) ;
        
        for (Map<String, Object> map : errorTypeList) {
        	apiErrorTypeJsonModel=new JSONObject();
        	
        	String errorType = String.valueOf(map.get("error_type")) ;
        	Long num = (Long)map.get("num") ;
        	
        	Map<String, Object> queryMap = new HashMap<String, Object>() ;
        	queryMap.put("startDate", sDate) ;
        	queryMap.put("endDate", eDate) ;
        	queryMap.put("errorType", errorType) ;
        	Integer errorTypeUnTreatNum = consumeMonitorApiDao.getUnTreatNumByMap(queryMap) ;
        	
        	apiTreatedRateStr = "" ;
            //处理成功率
            if(errorTypeUnTreatNum == 0){
            	apiTreatedRateStr = "100%";
        	}else{
        		BigDecimal rate = new BigDecimal(num).subtract(new BigDecimal(errorTypeUnTreatNum)).divide(new BigDecimal(num),3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
        		apiTreatedRateStr = rate.doubleValue()+"%";
        	}
        	
        	apiErrorTypeJsonModel.put("errorType", DERP_LOG.getLabelByKey(DERP_LOG.logApiErrorTypeList, errorType)) ;
            apiErrorTypeJsonModel.put("errorNum", num.toString()) ;
            apiErrorTypeJsonModel.put("unTreatErrorNum", errorTypeUnTreatNum) ;
            apiErrorTypeJsonModel.put("rate", apiTreatedRateStr) ;
            apiErrorTypeJsonArray.add(apiErrorTypeJsonModel) ;
		}
        data.put("apiErrorTypeJsonArray", apiErrorTypeJsonArray);
        
        
        /**
         * MQ消费统计
         */
        JSONArray mqJsonArray=new JSONArray();
        //MQ昨日新增数量
        Long lastMqIncreaseNum = 0L ;
        List<Map<String,Object>> failYesterdayList = consumeMonitorOrderDao.getGroupCountByStatus("\"0\"",startDate,endDate);
        for(Map<String,Object> map:failYesterdayList){
        	lastMqIncreaseNum += Long.valueOf(map.get("num").toString()) ;
        }
        data.put("lastMqIncreaseNum", lastMqIncreaseNum);
        
        //当月失败
        Map<String,Object> failResultMap = new HashMap<String,Object>();
        List<Map<String,Object>> failList = consumeMonitorOrderDao.getGroupCountByStatus("\"0\"",sDate,eDate);
        for(Map<String,Object> map:failList){
        	String code = (String) map.get("model_code");
        	failResultMap.put(code, map.get("num"));
        }
        
        //当月成功
        List<Map<String,Object>> successList = consumeMonitorOrderDao.getGroupCountByStatus("\"1\"",sDate,eDate);
        Map<String,Object> successResultMap = new HashMap<String,Object>();
        for(Map<String,Object> map:successList){
        	String code = (String) map.get("model_code");
        	successResultMap.put(code, map.get("num"));
        }
        
        //当月处理
        List<Map<String,Object>> treatList = consumeMonitorOrderDao.getGroupCountByStatus("\"2\",\"3\"",sDate,eDate);
        Map<String,Object> treatMap = new HashMap<String,Object>();
        for(Map<String,Object> map:treatList){
        	String code = (String) map.get("model_code");
        	treatMap.put(code, map.get("num"));
        }
        
        for(int i = 1;i<5;i++){
        	//名称
        	String name = "";
        	if("1".equals(String.valueOf(i))){
        		name="业务模块";
        	}else if("2".equals(String.valueOf(i))){
        		name="推送API";
        	}else if("3".equals(String.valueOf(i))){
        		name="仓储模块";
        	}else if("4".equals(String.valueOf(i))){
        		name="库存模块";
        	}
        	
        	//成功数量
        	Long success = 0L;
        	if(successResultMap.containsKey(String.valueOf(i)) 
        			&& StringUtils.isNotBlank(successResultMap.get(String.valueOf(i)).toString())){
        		success = (Long) successResultMap.get(String.valueOf(i));
        	}
        	//失败数量
        	Long fail = 0L;
        	if(failResultMap.containsKey(String.valueOf(i)) 
        			&& StringUtils.isNotBlank(failResultMap.get(String.valueOf(i)).toString())){
        		fail = (Long) failResultMap.get(String.valueOf(i));
        	}
        	//处理失败数
        	Long treat = 0L;
        	if(treatMap.containsKey(String.valueOf(i)) 
        			&& StringUtils.isNotBlank(treatMap.get(String.valueOf(i)).toString())){
        		treat = (Long) treatMap.get(String.valueOf(i));
        	}
        	
        	//成功率
        	String rateStr = "";
        	if(success == 0){
        		rateStr = "0%";
        	}else if(fail == 0){
        		rateStr = "100%";
        	}else{
        		BigDecimal rate = new BigDecimal(success).divide(new BigDecimal(success).add(new BigDecimal(fail).add(new BigDecimal(treat))),3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
        		rateStr = rate.doubleValue()+"%";
        	}
        	
        	//处理率
        	String treatRateStr = "";
        	if((success + treat) == 0){
        		treatRateStr = "0%";
        	}else if(fail == 0){
        		treatRateStr = "100%";
        	}else{
        		BigDecimal rate = new BigDecimal(treat)
        				.divide(new BigDecimal(fail).add(new BigDecimal(treat)),3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
        		treatRateStr = rate.doubleValue()+"%";
        	}
        	
        	JSONObject jsonModel=new JSONObject();
	        jsonModel.put("name",name);
	        jsonModel.put("success",success);
	        jsonModel.put("fail",fail);
	        jsonModel.put("treat",treat);
	        jsonModel.put("successRate",rateStr);
	        jsonModel.put("treatRate",treatRateStr);
	        mqJsonArray.add(jsonModel);
        }
        data.put("monthMQList", mqJsonArray);
        
        
        JSONArray orderJsonArray = getJsonArrayByModelCode(DERP_LOG.LOG_MODELCODE_1, sDate, eDate);
        JSONArray invertoryJsonArray = getJsonArrayByModelCode(DERP_LOG.LOG_MODELCODE_4, sDate, eDate);
        JSONArray storageJsonArray = getJsonArrayByModelCode(DERP_LOG.LOG_MODELCODE_3, sDate, eDate);
        JSONArray pushApiJsonArray = getJsonArrayByModelCode(DERP_LOG.LOG_MODELCODE_2, sDate, eDate);
        JSONArray totalJsonArray = new JSONArray() ;
        		
        //汇总
        JSONObject orderTotal = (JSONObject)orderJsonArray.get(0);
        JSONObject invertoryTotal = (JSONObject)invertoryJsonArray.get(0);
        JSONObject storageTotal = (JSONObject)storageJsonArray.get(0);
        JSONObject pushApiTotal = (JSONObject)pushApiJsonArray.get(0);
        
        Long totalAccount = orderTotal.getLong("account") + invertoryTotal.getLong("account") +
        		storageTotal.getLong("account") + pushApiTotal.getLong("account");
        
        Long failAccount = orderTotal.getLong("failCount") + invertoryTotal.getLong("failCount") +
        		storageTotal.getLong("failCount") + pushApiTotal.getLong("failCount");
        
        Long succ = orderTotal.getLong("succ") + invertoryTotal.getLong("succ") +
        		storageTotal.getLong("succ") + pushApiTotal.getLong("succ");
        
      //总处理率
    	String treatRateStr = "";
    	if(succ == 0){
    		treatRateStr = "0%";
    	}else if(failAccount == 0){
    		treatRateStr = "100%";
    	}else{
    		
    		BigDecimal tempBD = new BigDecimal(totalAccount).subtract(new BigDecimal(failAccount));
    		BigDecimal rate = tempBD.divide(new BigDecimal(totalAccount), 3, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
    		treatRateStr = rate.doubleValue()+"%";
    	}
        
        JSONObject lastObj = new JSONObject() ;
        lastObj.put("modelCode", "") ;
        lastObj.put("errorType", "总计") ;
        lastObj.put("account", totalAccount) ;
        lastObj.put("failCount", failAccount) ;
        lastObj.put("rate", treatRateStr) ;
        totalJsonArray.add(lastObj) ;
        
        data.put("orderJsonArray", orderJsonArray) ;
        data.put("invertoryJsonArray", invertoryJsonArray) ;
        data.put("storageJsonArray", storageJsonArray) ;
        data.put("pushApiJsonArray", pushApiJsonArray) ;
        data.put("totalJsonArray", totalJsonArray) ;
        
        sdf=new SimpleDateFormat("M");
        
        paramJson.put("dataMap",data);
        paramJson.put("month", sdf.format(calendar.getTime()));
        jsonObject.put("paramJson",paramJson);
        System.out.println("------paramJson:"+paramJson);
        jsonObject.put("recipients",ApolloUtil.smurfsRecipients);
        
        //调用外部接口发送邮件
        String resultMsg=SmurfsUtils.send(jsonObject,SmurfsAPIEnum.SMURFS_EMAIL);
        System.out.println(resultMsg);
		LOGGER.info("-----------------统计MQ消费情况-发送邮件结束----------------------");
		return true;
	}
	
	/**
	 * 根据类型获取数量
	 * @return
	 */
	private JSONArray getJsonArrayByModelCode(String modelCode, String sdate, String edate) {
		
		Long account = 0L;
		Long failAccount = 0L;
		
		Map<String, Object> querymap = new HashMap<String, Object>() ;
		querymap.put("modelCode", modelCode) ;
		querymap.put("startDate", sdate) ;
		querymap.put("endDate", edate) ;
		
		//成功
		Long succ = 0L;
		querymap.put("status", "1") ;
        List<Map<String, Object>> succListMap = consumeMonitorOrderDao.getErrorTypeAccountByMap(querymap) ;
        if(!succListMap.isEmpty()) {
        	succ = (Long)succListMap.get(0).get("num") ;
        }
        
        Map<String, Map<String, Object>> countMap = new LinkedHashMap<String, Map<String, Object>>() ;
        
        //失败
        querymap.put("status", "0") ;
        List<Map<String, Object>> failListMap = consumeMonitorOrderDao.getErrorTypeAccountByMap(querymap) ;
        
        for (Map<String, Object> map : failListMap) {
			String errorType = String.valueOf(map.get("error_type")) ;
			
			Map<String, Object> tempMap = countMap.get(errorType);
			if(tempMap == null) {
				tempMap = new HashMap<String, Object>() ;
			}
			
			tempMap.put("fail", (Long)map.get("num")) ;
			
			account += (Long)map.get("num") ;
			failAccount += (Long)map.get("num") ;
			
			countMap.put(errorType, tempMap) ;
		}
        
        //处理
        querymap.put("status", "\"2\",\"3\"") ;
        List<Map<String, Object>> treatListMap = consumeMonitorOrderDao.getErrorTypeAccountByMap(querymap) ;
        
        for (Map<String, Object> map : treatListMap) {
			String errorType = String.valueOf(map.get("error_type")) ;
			Map<String, Object> tempMap = countMap.get(errorType);
			if(tempMap == null) {
				tempMap = new HashMap<String, Object>() ;
			}
			
			tempMap.put("treat", (Long)map.get("num")) ;
			
			account += (Long)map.get("num") ;
			
			countMap.put(errorType, tempMap) ;
		}
        
        //总处理率
    	String treatRateStr = "";
    	if(succ == 0){
    		treatRateStr = "0%";
    	}else if(failAccount == 0){
    		treatRateStr = "100%";
    	}else{
    		BigDecimal rate = new BigDecimal(account).subtract(new BigDecimal(failAccount))
    				.divide(new BigDecimal(account),3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
    		treatRateStr = rate.doubleValue()+"%";
    	}
        
        JSONArray jsonArray = new JSONArray();
        JSONObject firstObj = new JSONObject() ;
        firstObj.put("modelCode", DERP_LOG.getLabelByKey(DERP_LOG.log_modelCodeList, modelCode)) ;
        firstObj.put("errorType", "总计") ;
        firstObj.put("account", account) ;
        firstObj.put("failCount", failAccount) ;
        firstObj.put("rate", treatRateStr) ;
        firstObj.put("succ", succ) ;
        jsonArray.add(firstObj) ;
        
        for(Entry<String, Map<String, Object>> set: countMap.entrySet()){
        	
        	String errorType = set.getKey();
        	
        	if(DERP_LOG.LOG_MODELCODE_4.equals(modelCode)) {
        		errorType = DERP_LOG.getLabelByKey(DERP_LOG.logInvertoryErrorTypeList, errorType) ;
        	}else {
        		errorType = DERP_LOG.getLabelByKey(DERP_LOG.logOrderErrorTypeList, errorType) ;
        	}
        	
        	Map<String, Object> tempMap = set.getValue();
        	Long fail = (Long)tempMap.get("fail") == null ? 0L : (Long)tempMap.get("fail") ;
        	Long treat = (Long)tempMap.get("treat") == null ? 0L : (Long)tempMap.get("treat");
        	
        	//总处理率
        	treatRateStr = "";
        	if(succ == 0){
        		treatRateStr = "0%";
        	}else if(fail == 0){
        		treatRateStr = "100%";
        	}else{
        		BigDecimal rate = new BigDecimal(treat)
        				.divide(new BigDecimal(fail).add(new BigDecimal(treat)),3,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100));
        		treatRateStr = rate.doubleValue()+"%";
        	}
        	
        	JSONObject obj = new JSONObject() ;
        	obj.put("modelCode", DERP_LOG.getLabelByKey(DERP_LOG.log_modelCodeList, modelCode)) ;
        	obj.put("errorType", errorType) ;
        	obj.put("account", fail + treat) ;
        	obj.put("failCount", fail) ;
        	obj.put("rate", treatRateStr) ;
        	jsonArray.add(obj) ;
            
        }
        
        
        
        return jsonArray ;
	}
	
}
