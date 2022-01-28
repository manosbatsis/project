package com.topideal.service.impl;

import com.topideal.api.wx.WXUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.*;
import com.topideal.entity.vo.*;
import com.topideal.mongo.dao.ExceptionOrderHistoryPoolMongoDao;
import com.topideal.mongo.dao.ExceptionOrderPoolMongoDao;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.entity.ExceptionOrderHistoryPoolMongo;
import com.topideal.mongo.entity.ExceptionOrderPoolMongo;
import com.topideal.tools.CollectionEnum;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * MQ消费监控 MQ
 * 
 * @author zhanghx 2018/8/17
 */
@Service
public class ConsumeMonitorServiceImpl implements com.topideal.service.ConsumeMonitorService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ConsumeMonitorServiceImpl.class);

	// MQ日志预警
	@Autowired
	private LogWarningMqDao logWarningMqDao;
	// 库存日志
	@Autowired
	private JSONMongoDao jsonMongoDao;
	//api
	@Autowired
	private ConsumeMonitorApiDao consumeMonitorApiDao;
	@Autowired
	private LogStreamApiDao logStreamApiDao;
	//库存
	@Autowired
	private ConsumeMonitorInventoryDao consumeMonitorInventoryDao;
	@Autowired
	private LogStreamInventoryDao logStreamInventoryDao;
	//业务
	@Autowired
	private ConsumeMonitorOrderDao consumeMonitorOrderDao;
	@Autowired
	private ExceptionOrderPoolMongoDao exceptionOrderPoolMongoDao;
	@Autowired
	private ExceptionOrderHistoryPoolMongoDao exceptionOrderHistoryPoolMongoDao;
	@Autowired
	private LogStreamOrderDao logStreamOrderDao;
	
	@Override
	public boolean save(String json, String topics, String tags) throws Exception {
		LOGGER.info("-----------------日志监控开始----------------------");

		Timestamp ts = new Timestamp(System.currentTimeMillis());

		// 实例化JSON对象
		JSONObject jsonObject = JSONObject.fromObject(json);
		
		LOGGER.info("-----------------初始报文："+jsonObject.toString()+"----------------------");
		JSONObject jsonObj = (JSONObject) jsonObject.get("logJson");
		// 实例化JSON对象
		JSONObject jsonData = new JSONObject();
		if(jsonObj!=null){
			jsonData = jsonObj;
		}else{
			jsonData = JSONObject.fromObject(json);
		}
		// 埋点
		String point = jsonData.getString("point");
		// 状态
		String state = jsonData.getString("state");
		// 模块编码
		String modelCode = jsonData.getString("moduleCode");
		// 消费时间
		Long endDate = jsonData.getLong("endDate");
		// 关键字
		String keyword = (String) jsonData.get("keyword");
		// 模块描述
		String model = (String) jsonData.get("model");

		Timestamp consume = new Timestamp(endDate);
		// 开始时间
		Long startDate = null;
		if (modelCode.equals(LogModuleType.MODULE_API.getType() + "")) {
			startDate = jsonData.getLong("receiveData");
		} else {
			startDate = jsonData.getLong("startDate");
		}
		// uuid
		String uuid = jsonData.getString("id");
		// 请求报文
		JSONObject requestMessage = (JSONObject) jsonData.get("requestMessage");
		// 异常信息
		String expMsg = "";
		
		Object expMsgObj = jsonData.get("expMsg");
		if(expMsgObj instanceof String){
			expMsg = (String) expMsgObj;
		}else{
			JSONObject expJson = (JSONObject) expMsgObj;
			expMsg = (String) expJson.get("notes");
		}
		String merchantName = (String) jsonData.get("merchantName");
		if(StringUtils.isBlank(merchantName)) merchantName = (String) requestMessage.get("merchantName");

		//判断异常类型,是否满足异常池类型
		String expType = null ;
		if(judgeRequirementsException(expMsg)) {
			if(expMsg.startsWith(DERP.MQ_FAILTYPE_01)) {
				expType = DERP.MQ_FAILTYPE_01 ;
			}else if(expMsg.startsWith(DERP.MQ_FAILTYPE_02)) {
				expType = DERP.MQ_FAILTYPE_02 ;
			}else if(expMsg.startsWith(DERP.MQ_FAILTYPE_03)) {
				expType = DERP.MQ_FAILTYPE_03 ;
			}else if(expMsg.startsWith(DERP.MQ_FAILTYPE_04)) {
				expType = DERP.MQ_FAILTYPE_04 ;
			}
			
			expMsg = expMsg.substring(expType.length()) ;
			jsonData.put("expMsg", expMsg);
		}

		// 存到mongodb
		String collectionName = "";
		if (modelCode.equals(LogModuleType.MODULE_ORDER.getType() + "")) {
			collectionName = CollectionEnum.MQ_ORDER_LOG.getCollectionName();
		} else if (modelCode.equals(LogModuleType.MODULE_PUSH_API.getType() + "")) {
			collectionName = CollectionEnum.MQ_PUSH_API_LOG.getCollectionName();
		} else if (modelCode.equals(LogModuleType.MODULE_STORAGE.getType() + "")) {
			collectionName = CollectionEnum.MQ_STORAGE_LOG.getCollectionName();
		} else if (modelCode.equals(LogModuleType.MODULE_INVENTORY.getType() + "")) {
			collectionName = CollectionEnum.MQ_INVENTORY_LOG.getCollectionName();
		} else if (modelCode.equals(LogModuleType.MODULE_API.getType() + "")) {
			collectionName = CollectionEnum.API_MONITOR.getCollectionName();
		}else if(modelCode.equals(LogModuleType.MODULE_REPORT.getType() + "")){
			collectionName = CollectionEnum.REPORT_LOG.getCollectionName();
		}
		Double differenceTime = (endDate - startDate) / 1000.0;
		jsonData.put("differenceTime", differenceTime);
		jsonMongoDao.insertJson(jsonData.toString(), collectionName);
		/*
		 * 根据模块存储到不同的表
		 * 对冻结解冻、进境状态的日志做特殊处理（用于日志页面显示）
		 */
		String type = null ;
		if(modelCode.equals(LogModuleType.MODULE_API.getType() + "")){//api
			//进境状态的特殊处理
			if("12103100400".equals(point) && requestMessage.containsKey("type")){
				String str = requestMessage.getString("type");
				if("1".equals(str)){//国检
					type = "2";
				}else if("2".equals(str)){//海关
					type = "3";
				}
			}
			//保存api日志
			saveApiLog(point,keyword,type,consume,uuid,state,expMsg,ts,differenceTime,model,modelCode,merchantName);
		}else if(modelCode.equals(LogModuleType.MODULE_INVENTORY.getType() + "")){//库存
			//冻结解冻的特殊处理
			if(DERP_LOG_POINT.POINT_13201301800.equals(point) && requestMessage.containsKey("operateType")){
				type = requestMessage.getString("operateType");
			}
			//保存库存日志
			saveInventoryLog(point,keyword,type,consume,uuid,state,expMsg,ts,differenceTime,model,modelCode,merchantName);
		}else{//业务、报表以及其他
			//进境状态的特殊处理
			if(DERP_LOG_POINT.POINT_12203120400.equals(point) && requestMessage.containsKey("type")){
				String str = requestMessage.getString("type");
				if("1".equals(str)){//国检
					type = "2";
				}else if("2".equals(str)){//海关
					type = "3";
				}
			}
			//冻结解冻的特殊处理
			if(DERP_LOG_POINT.POINT_13201301800.equals(point) && requestMessage.containsKey("operateType")){
				type = requestMessage.getString("operateType");
			}
			//保存业务、仓储、推送外部api、报表日志
			saveOrderLog(point,keyword,type,consume,uuid,state,expMsg,ts,differenceTime,model,modelCode,merchantName);
		}
		
		/**
		 * 日志mq消费端拦截指定消费失败类型消息写入异常单号池
		 */
		saveExceptionPool(point , uuid ,keyword , type , modelCode , endDate ,expType ) ;

		/**机器人*/
		AISendMsg(point,model,state,expMsg);
		
		LOGGER.info("-----------------日志监控结束----------------------");
		return true;
	}
	/**@param state 状态 0-失败 1-成功
	 * */
	private void AISendMsg(String point,String model,String state,String expMsg){
		if(!state.equals("0")) return ;
		//需要机器人发消息的接口编码
		List<String> pointList = new ArrayList<>();
		pointList.add(DERP_LOG_POINT.POINT_13201501050);//数据同步到报表库
		pointList.add(DERP_LOG_POINT.POINT_13201600010);//数据同步到备份库
		pointList.add(DERP_LOG_POINT.POINT_13201600020);//数据同步到美赞库
		pointList.add(DERP_LOG_POINT.POINT_13201302300);//Gss实时库存快照
		pointList.add(DERP_LOG_POINT.POINT_13201308300);//阿里菜鸟仓实时库存快照
		pointList.add(DERP_LOG_POINT.POINT_13201307700);//众邦云仓实时库存快照
		pointList.add(DERP_LOG_POINT.POINT_12207000100);//IDM用户同步

		if(ApolloUtil.jxEnable.equals("true")&&
				pointList.contains(point)){
			if(StringUtils.isNotBlank(expMsg)&&expMsg.length()>100) expMsg = expMsg.substring(0,100);
			String content = ApolloUtil.jxRemark+":"+model+"->失败"+ TimeUtils.formatFullTime() +":"+expMsg;
			//WXUtils.send(content);
			WXUtils.sendPush(content);//蓝精灵推送
		}
	}
	/**
	 * 日志mq消费端拦截指定消费失败类型消息写入异常单号池
	 * @param point
	 * @param keyword
	 * @param type
	 */
	private void saveExceptionPool(String point, String uuid ,String keyword, String type, 
			String modelCode , Long createTime , String expType) {
		
		/**
		 * 判断当前异常池是否已存在本接口编码、单号、类型(国检/海关、冻结/解冻,其他接口此字段为空),
      		已存在则跳过，否则保存(统计历史异常单号池本接口编码、单号记录数为重推次数)
		 */
		if(StringUtils.isNotBlank(expType)) {
			
			Map<String , Object> queryParam = new HashMap<String, Object>() ;
			queryParam.put("point", point) ;
			queryParam.put("keyword", keyword) ;
			queryParam.put("type", type) ;
			queryParam.put("expType", expType) ;
			
			List<ExceptionOrderPoolMongo> exceptionOrderPoolList = exceptionOrderPoolMongoDao.findAll(queryParam);
		
			if(exceptionOrderPoolList.isEmpty()) {
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(createTime);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
				String createDate = sdf.format(cal.getTime());
				
				JSONObject jsonObject = new JSONObject() ;
				jsonObject.put("point", point);
				jsonObject.put("keyword", keyword);
				jsonObject.put("modelCode", modelCode);
				jsonObject.put("type", type);
				jsonObject.put("expType", expType);
				jsonObject.put("id", uuid);
				jsonObject.put("createDate", createDate);
				
				List<ExceptionOrderHistoryPoolMongo> historyMongoList = exceptionOrderHistoryPoolMongoDao.getsSpecifiedGroupList(queryParam);
				
				if(!historyMongoList.isEmpty()) {
					
					Integer rePushTimes = historyMongoList.get(0).getRePushTimes();
					jsonObject.put("rePushTimes",rePushTimes);
				}else {
					jsonObject.put("rePushTimes" , 0);
				}
				
				jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.MQ_EXCEPTION_ORDER_POOL.getCollectionName());
				
			}
		}
	}
	
	/**
	 * 判断是否符合条件异常
	 * @param expMsg
	 * @return
	 */
	private boolean judgeRequirementsException(String expMsg) {
		if(StringUtils.isNotBlank(expMsg)) {
			if(expMsg.startsWith(DERP.MQ_FAILTYPE_01) 
					|| expMsg.startsWith(DERP.MQ_FAILTYPE_02) 
					|| expMsg.startsWith(DERP.MQ_FAILTYPE_03) 
					|| expMsg.startsWith(DERP.MQ_FAILTYPE_04) ) {
				return true ;
			}
		}
		
		return false ;
	}
	
	/**
	 * 保存api监控日志、api日志流水
	 * @return
	 * @throws SQLException 
	 */
	private boolean saveApiLog(String point,String keyword,String type,Timestamp consume,String uuid,String state,
			String expMsg,Timestamp ts,Double differenceTime,String model,String modelCode,String merchantName) throws SQLException{
		if(DERP_LOG_POINT.POINT_12102001000.equals(point)&&"商家卓志编码不存在".equals(expMsg)){//商品同步 标准条码为空的进监控,其他不进监控
			return true;
		}
		
		String errorType = judgeApiErrorType(expMsg) ;
		
		// 根据埋点、关键字查询监控是否存在
		ConsumeMonitorApiModel monitor = new ConsumeMonitorApiModel();
		monitor.setPoint(point);// 埋点
		monitor.setKeyword(keyword);// 关键字
		//进境状态的日志要加上类型
		if(StringUtils.isNotBlank(type)){
			monitor.setType(type);
		}
		
		//获取监控数据
		List<ConsumeMonitorApiModel> monitorList = consumeMonitorApiDao.list(monitor);
		//监控数据不为空
		if(monitorList != null && monitorList.size() > 0){
			monitor = monitorList.get(0);
			
			monitor.setErrorType(errorType);
			
			/*
			 * 日志的状态是异常，则修改日志信息
			 */
			if ("0".equals(monitor.getStatus())) {
				monitor.setConsumeDate(consume);// 消费时间
				monitor.setLogId(uuid);// 日志id
				if ("1".equals(state)) {
					monitor.setStatus("3");// 状态 3重推成功
					monitor.setCloseTime(ts);// 关闭时间
				} else {
					monitor.setStatus(state);
				}
				String newExpMsg = " ";
				if (expMsg != null && !"".equals(expMsg) && !"1".equals(state)) {//状态为“异常”
					newExpMsg = expMsg;//更新失败原因
					
					if (expMsg.length() > 400) {
						newExpMsg = expMsg.substring(0, 400);// 失败原因
					}
				}
				monitor.setExpMsg(newExpMsg);
				monitor.setDifferenceTime(differenceTime);// 耗时
				consumeMonitorApiDao.modify(monitor);
			}
		}else{
			//新增监控日志
			monitor = new ConsumeMonitorApiModel();
			monitor.setModel(model);// 模块描述
			monitor.setModelCode(modelCode);// 模块编码
			monitor.setConsumeDate(consume);// 消费时间
			monitor.setKeyword(keyword);// 关键字
			monitor.setPoint(point);// 埋点
			monitor.setStatus(state);// 状态
			monitor.setLogId(uuid);// 日志id
			monitor.setErrorType(errorType);
			monitor.setMerchantName(merchantName);
			String newExpMsg = expMsg;
			
			if (expMsg != null && expMsg.length() > 400) {
				newExpMsg = expMsg.substring(0, 400);// 失败原因超长，需要截取
			}
			monitor.setExpMsg(newExpMsg);// 失败原因
			monitor.setDifferenceTime(differenceTime);// 耗时
			if ("1".equals(state)) {
				monitor.setCloseTime(ts);// 关闭时间
			}
			if(StringUtils.isNotBlank(type)){
				monitor.setType(type);
			}
			Long monitorId = consumeMonitorApiDao.save(monitor);
			if (monitorId == null) {
				LOGGER.info("----------------api监控新增失败----------------------");
				return false;
			}
		}
		// 写入日志流水
		LogStreamApiModel stream = new LogStreamApiModel();
		stream.setModel(model);// 模块描述
		stream.setModelCode(modelCode);// 模块编码
		stream.setLogId(uuid);// 关联日志id（mongoDB）
		stream.setConsumeDate(consume);// 消费时间
		stream.setKeyword(keyword);// 关键字
		stream.setPoint(point);// 埋点
		stream.setStatus(state);// 状态
		stream.setDifferenceTime(differenceTime);// 耗时
		stream.setErrorType(errorType);
		if(StringUtils.isNotBlank(type)){
			stream.setType(type);
		}
		Long streamId = logStreamApiDao.save(stream);
		if (streamId == null) {
			LOGGER.info("-----------------api日志流水新增失败----------------------");
			return false;
		}
		return true;
	}
	/**
	 * 保存库存监控日志、库存日志流水
	 * @return
	 * @throws SQLException 
	 */
	private boolean saveInventoryLog(String point,String keyword,String type,Timestamp consume,String uuid,String state,
			String expMsg,Timestamp ts,Double differenceTime,String model,String modelCode,String merchantName) throws SQLException{
		
		String errorType = judgeInvertoryErrorType(expMsg) ;
		
		// 根据埋点、关键字查询MQ消费监控是否存在
		ConsumeMonitorInventoryModel monitor = new ConsumeMonitorInventoryModel();
		monitor.setPoint(point);// 埋点
		monitor.setKeyword(keyword);// 关键字
		// 对 库存冻结和释放冻结接口 的特殊处理
		if(StringUtils.isNotBlank(type) && ("0".equals(type) || "1".equals(type))){
			monitor.setType(type);
		}
		
		List<ConsumeMonitorInventoryModel> monitorList = consumeMonitorInventoryDao.list(monitor);
		//监控数据不为空
		if(monitorList != null && monitorList.size() > 0){
			monitor = monitorList.get(0);
			
			monitor.setErrorType(errorType);
			//状态为"异常"，修改日志信息
			if ("0".equals(monitor.getStatus())) {
				monitor.setConsumeDate(consume);// 消费时间
				monitor.setLogId(uuid);// 日志id
				if ("1".equals(state)) {
					monitor.setStatus("3");// 状态 3重推成功
					monitor.setCloseTime(ts);// 关闭时间
				} else {
					monitor.setStatus(state);
				}
				String newExpMsg = " ";
				if (expMsg != null && !"".equals(expMsg) && !"1".equals(state)) {//状态为“异常”
					newExpMsg = expMsg;//更新失败原因
					if (expMsg.length() > 400) {
						newExpMsg = expMsg.substring(0, 400);// 失败原因
					}
				}
				monitor.setExpMsg(newExpMsg);
				monitor.setDifferenceTime(differenceTime);// 耗时
				consumeMonitorInventoryDao.modify(monitor);
			} else {//状态为"成功"，记录日志预警
				// MQ日志预警 过滤掉 进境状态回推接口
				if (!DERP_LOG_POINT.POINT_12203120400.equals(point)) {
					LogWarningMqModel warning = new LogWarningMqModel();
					// 还继续推正常的状态过来，写入MQ日志预警，漏洞等级
					if ("1".equals(state)) {
						warning.setLevel("1");// 预警等级（1-漏洞，0-次要）
						warning.setRemark("在正常状态下，还接收到正常状态的日志");// 备注（描述原因）
					}
					// 推异常的状态过来，写入MQ日志预警，次要等级
					else {
						warning.setLevel("0");// 预警等级（1-漏洞，0-次要）
						warning.setRemark("在正常状态下，接收到异常状态的日志");// 备注（描述原因）
					}
					warning.setModel(model);// 模块描述
					warning.setModelCode(modelCode);// 模块编码
					warning.setConsumeDate(consume);// 消费时间
					warning.setKeyword(keyword);// 关键字
					warning.setPoint(point);// 埋点
					// 对 库存冻结和释放冻结接口 的特殊处理
					if (StringUtils.isNotBlank(type)) {
						warning.setType(type);
					}
					Long warningId = logWarningMqDao.save(warning);
					if (warningId == null) {
						LOGGER.info("-----------------库存MQ日志预警新增失败----------------------");
						return false;
					}
				}
			}
		}else{
			// 新增库存MQ消费监控
			monitor = new ConsumeMonitorInventoryModel();
			monitor.setModel(model);// 模块描述
			monitor.setModelCode(modelCode);// 模块编码
			monitor.setConsumeDate(consume);// 消费时间
			monitor.setKeyword(keyword);// 关键字
			monitor.setPoint(point);// 埋点
			monitor.setStatus(state);// 状态
			monitor.setLogId(uuid);// 日志id
			monitor.setErrorType(errorType);
			String newExpMsg = expMsg;
			if (expMsg != null && expMsg.length() > 400) {
				newExpMsg = expMsg.substring(0, 400);// 失败原因超长，需要截取
			}
			monitor.setExpMsg(newExpMsg);// 失败原因
			monitor.setDifferenceTime(differenceTime);// 耗时
			monitor.setMerchantName(merchantName);
			if ("1".equals(state)) {
				monitor.setCloseTime(ts);// 关闭时间
			}
			// 对 库存冻结和释放冻结接口 的特殊处理
			if(StringUtils.isNotBlank(type) && ("0".equals(type) || "1".equals(type))){
				monitor.setType(type);
			}
			Long monitorId = consumeMonitorInventoryDao.save(monitor);
			if (monitorId == null) {
				LOGGER.info("-----------------库存MQ消费监控新增失败----------------------");
				return false;
			}
		}
		// 写入日志流水
		LogStreamInventoryModel stream = new LogStreamInventoryModel();
		stream.setModel(model);// 模块描述
		stream.setModelCode(modelCode);// 模块编码
		stream.setLogId(uuid);// 关联日志id（mongoDB）
		stream.setConsumeDate(consume);// 消费时间
		stream.setKeyword(keyword);// 关键字
		stream.setPoint(point);// 埋点
		stream.setStatus(state);// 状态
		stream.setDifferenceTime(differenceTime);// 耗时
		stream.setErrorType(errorType);
		if(StringUtils.isNotBlank(type)){
			stream.setType(type);
		}
		Long streamId = logStreamInventoryDao.save(stream);
		if (streamId == null) {
			LOGGER.info("-----------------库存日志流水新增失败----------------------");
			return false;
		}
		return true;
	}
	/**
	 * 保存业务、仓储、推送外部、报表监控日志、日志流水
	 * @return
	 * @throws SQLException 
	 */
	private boolean saveOrderLog(String point,String keyword,String type,Timestamp consume,String uuid,String state,
			String expMsg,Timestamp ts,Double differenceTime,String model,String modelCode,String merchantName) throws SQLException{
		
		String errorType = judgeOrderErrorType(expMsg) ;
		
		// 根据埋点、关键字查询MQ消费监控是否存在
		ConsumeMonitorOrderModel monitor = new ConsumeMonitorOrderModel();
		monitor.setPoint(point);// 埋点
		monitor.setKeyword(keyword);// 关键字
		// 对 库存冻结和释放冻结接口 的特殊处理
		if(StringUtils.isNotBlank(type)){
			monitor.setType(type);
		}
		
		List<ConsumeMonitorOrderModel> monitorList = consumeMonitorOrderDao.list(monitor);
		//监控数据不为空
		if(monitorList != null && monitorList.size() > 0){
			monitor = monitorList.get(0);
			monitor.setErrorType(errorType);
			
			//状态为"异常"，修改日志信息
			if ("0".equals(monitor.getStatus())) {
				monitor.setConsumeDate(consume);// 消费时间
				monitor.setLogId(uuid);// 日志id
				if ("1".equals(state)) {
					monitor.setStatus("3");// 状态 3重推成功
					monitor.setCloseTime(ts);// 关闭时间
				} else {
					monitor.setStatus(state);
				}
				String newExpMsg = " ";
				if (expMsg != null && !"".equals(expMsg) && !"1".equals(state)) {//状态为“异常”
					newExpMsg = expMsg;//更新失败原因
					if (expMsg.length() > 400) {
						newExpMsg = expMsg.substring(0, 400);// 失败原因超长，需要截取
					}
				}
				monitor.setExpMsg(newExpMsg);
				monitor.setDifferenceTime(differenceTime);// 耗时
				consumeMonitorOrderDao.modify(monitor);
			} else {//状态为"成功"，记录日志预警
				// MQ日志预警 过滤掉 进境状态回推接口
				if (!DERP_LOG_POINT.POINT_12203120400.equals(point)) {
					LogWarningMqModel warning = new LogWarningMqModel();
					// 还继续推正常的状态过来，写入MQ日志预警，漏洞等级
					if ("1".equals(state)) {
						warning.setLevel("1");// 预警等级（1-漏洞，0-次要）
						warning.setRemark("在正常状态下，还接收到正常状态的日志");// 备注（描述原因）
					}
					// 推异常的状态过来，写入MQ日志预警，次要等级
					else {
						warning.setLevel("0");// 预警等级（1-漏洞，0-次要）
						warning.setRemark("在正常状态下，接收到异常状态的日志");// 备注（描述原因）
					}
					warning.setModel(model);// 模块描述
					warning.setModelCode(modelCode);// 模块编码
					warning.setConsumeDate(consume);// 消费时间
					warning.setKeyword(keyword);// 关键字
					warning.setPoint(point);// 埋点
					// 对 库存冻结和释放冻结接口 的特殊处理
					if (StringUtils.isNotBlank(type)) {
						warning.setType(type);
					}
					Long warningId = logWarningMqDao.save(warning);
					if (warningId == null) {
						LOGGER.info("-----------------业务MQ日志预警新增失败----------------------");
						return false;
					}
				}
			}
		}else{
			// 新增库存MQ消费监控
			monitor = new ConsumeMonitorOrderModel();
			monitor.setModel(model);// 模块描述
			monitor.setModelCode(modelCode);// 模块编码
			monitor.setConsumeDate(consume);// 消费时间
			monitor.setKeyword(keyword);// 关键字
			monitor.setPoint(point);// 埋点
			monitor.setStatus(state);// 状态
			monitor.setLogId(uuid);// 日志id
			monitor.setErrorType(errorType);
			
			String newExpMsg = expMsg;
			if (expMsg != null && expMsg.length() > 400) {
				newExpMsg = expMsg.substring(0, 400);// 失败原因超长，需要截取
			}
			monitor.setExpMsg(newExpMsg);// 失败原因
			monitor.setDifferenceTime(differenceTime);// 耗时
			monitor.setMerchantName(merchantName);
			if ("1".equals(state)) {
				monitor.setCloseTime(ts);// 关闭时间
			}
			// 对 库存冻结和释放冻结接口 的特殊处理
			if(StringUtils.isNotBlank(type)){
				monitor.setType(type);
			}
			Long monitorId = consumeMonitorOrderDao.save(monitor);
			if (monitorId == null) {
				LOGGER.info("-----------------业务MQ消费监控新增失败----------------------");
				return false;
			}
		}
		// 写入日志流水
		LogStreamOrderModel stream = new LogStreamOrderModel();
		stream.setModel(model);// 模块描述
		stream.setModelCode(modelCode);// 模块编码
		stream.setLogId(uuid);// 关联日志id（mongoDB）
		stream.setConsumeDate(consume);// 消费时间
		stream.setKeyword(keyword);// 关键字
		stream.setPoint(point);// 埋点
		stream.setStatus(state);// 状态
		stream.setDifferenceTime(differenceTime);// 耗时
		stream.setErrorType(errorType);
		if(StringUtils.isNotBlank(type)){
			stream.setType(type);
		}
		
		Long streamId = logStreamOrderDao.save(stream);
		if (streamId == null) {
			LOGGER.info("-----------------业务日志流水新增失败----------------------");
			return false;
		}
		return true;
	}
	
	/**
	 * API 判断错误类型
	 * @param newExpMsg
	 * @return
	 */
	private String judgeApiErrorType(String newExpMsg) {
		
		String errorType = null ;
		
		if(StringUtils.isBlank(newExpMsg)) {
			return null ;
		}
		
		if(newExpMsg.indexOf("非经分销的入库申报数据") > -1
				|| newExpMsg.indexOf("非经分销的单") > -1 
				|| newExpMsg.indexOf("非经分销的初理货数据") > -1 ) {
			errorType = DERP_LOG.LOG_API_ERROR_TYPE_00 ;
		}else if(newExpMsg.indexOf("签名错误") > -1) {
			errorType = DERP_LOG.LOG_API_ERROR_TYPE_01 ;
		}else if(newExpMsg.indexOf("订单号没有匹配搭配订单") > -1) {
			errorType = DERP_LOG.LOG_API_ERROR_TYPE_02 ;
		}else if(newExpMsg.indexOf("标准条码commbarcode为空") > -1) {
			errorType = DERP_LOG.LOG_API_ERROR_TYPE_03 ;
		}else if(newExpMsg.indexOf("该app_key商家找不到该货号商品") > -1
				|| newExpMsg.indexOf("商品不存在") > -1) {
			errorType = DERP_LOG.LOG_API_ERROR_TYPE_04 ;
		}else {
			errorType = DERP_LOG.LOG_API_ERROR_TYPE_05 ;
		}
		
		return errorType ;
		
	}
	
	/**
	 * 业务判断错误类型
	 */
	private String judgeOrderErrorType(String newExpMsg) {
		String errorType = null ;
		
		if(StringUtils.isBlank(newExpMsg)) {
			return null ;
		}
		
		if(newExpMsg.indexOf("SKU_ID不为空") > -1
				|| newExpMsg.indexOf("根据商家和货号没有查询到商品信息") > -1 
				|| newExpMsg.indexOf("调拨商品不存在") > -1 ) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_00 ;
		}else if(newExpMsg.indexOf("没有查到对应的订单") > -1 
				|| newExpMsg.indexOf("电商订单不存在") > -1
				|| newExpMsg.indexOf("根据电商订单号没有查到电商订单") > -1
				|| newExpMsg.indexOf("未找到采购入库单") > -1
				|| newExpMsg.indexOf("没有查询到对应的订单") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_01 ;
		}else if(newExpMsg.indexOf("抓取寄售商e仓的店铺编码在商家店铺关联表") > -1
				|| newExpMsg.indexOf("抓取蓝精灵已经发货和已完成订单没有查询到店铺编码") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_02 ;
		}else if(newExpMsg.indexOf("goods_no is null") > -1
				|| newExpMsg.indexOf("deliver_date Is Null") > -1
				|| newExpMsg.indexOf("批次效期强校验批次和效期不能为空") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_03 ;
		}else if(newExpMsg.indexOf("报文异常") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_04 ;
		}else if(newExpMsg.indexOf("op盘点结果状态是处理中或者是已确认") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_06 ;
		}else if(newExpMsg.indexOf("类型调整单已经存在") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_07 ;
		}else if(newExpMsg.indexOf("类型调整单好坏品互转加的数量和减的数量不相同") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_08 ;
		}else if(newExpMsg.indexOf("\"status\":2") > -1) {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_09 ;
		}else {
			errorType = DERP_LOG.LOG_ORDER_ERROR_TYPE_05 ;
		}
		
		return errorType ;
	}
	
	/**
	 * 库存 判断错误类型
	 * @param newExpMsg
	 * @return
	 */
	private String judgeInvertoryErrorType(String newExpMsg) {
		
		String errorType = null ;
		
		if(StringUtils.isBlank(newExpMsg)) {
			return null ;
		}
		
		if(newExpMsg.indexOf("扣减量大于实际库存量") > -1
				|| newExpMsg.indexOf("无实际库存量") > -1 
				|| newExpMsg.indexOf("当前数据库的可用量小于冻结量") > -1 
				|| newExpMsg.indexOf("当前数据库没有可用量") > -1 ) {
			errorType = DERP_LOG.LOG_INVERTORY_ERROR_TYPE_00 ;
		}else if(newExpMsg.indexOf("没有冻结记录") > -1) {
			errorType = DERP_LOG.LOG_INVERTORY_ERROR_TYPE_01 ;
		}else {
			errorType = DERP_LOG.LOG_INVERTORY_ERROR_TYPE_02 ;
		}
		
		return errorType ;
		
	}
}
