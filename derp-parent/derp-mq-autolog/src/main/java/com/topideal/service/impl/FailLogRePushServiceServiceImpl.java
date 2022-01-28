package com.topideal.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.dao.ConsumeMonitorInventoryDao;
import com.topideal.dao.ConsumeMonitorOrderDao;
import com.topideal.entity.vo.ConsumeMonitorInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorOrderModel;
import com.topideal.mongo.dao.ExceptionOrderHistoryPoolMongoDao;
import com.topideal.mongo.dao.ExceptionOrderPoolMongoDao;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.entity.ExceptionOrderHistoryPoolMongo;
import com.topideal.mongo.entity.ExceptionOrderPoolMongo;
import com.topideal.service.FailLogRePushService;
import com.topideal.tools.BaseUtils.Operator;
import com.topideal.tools.CollectionEnum;

/**
 * 智能运维日志重推service
 * @author gy
 *
 */
@Service
public class FailLogRePushServiceServiceImpl implements FailLogRePushService{

	/*打印日志*/
    private static final Logger LOGGER = LoggerFactory.getLogger(FailLogRePushServiceServiceImpl.class);
	
	@Autowired
	private ConsumeMonitorOrderDao consumeMonitorOrderDao ;
	
	@Autowired
	private ConsumeMonitorInventoryDao consumeMonitorInventoryDao ;
	
	@Autowired
	private ExceptionOrderHistoryPoolMongoDao exceptionOrderHistoryPoolMongoDao ;
	
	@Autowired
	private ExceptionOrderPoolMongoDao exceptionOrderPoolMongoDao ;
	
	@Autowired
	private JSONMongoDao jSONMongoDao ;
	
	@Autowired
	private RMQProducer rocketMQProducer;
	
	@Override
	public void rePushFailLog(String json, String tags) throws Exception {
		
		/**
		 * 1查询本异常类型、重推次数小于等于3次的异常单号，去日志监控mysql查询日志监控
		 */
		
		/*
		 * 1.1根据tags 判断当前重推消费端MQ类型
		 */
		String expType = null ;
		if(tags.endsWith("01")) {  			//B2C单号不存在
			expType = DERP.MQ_FAILTYPE_01 ;
		}else if(tags.endsWith("02")) { 	//冻结记录不存在
			expType = DERP.MQ_FAILTYPE_02 ;
		}else if(tags.endsWith("03")) {		//锁记录
			expType = DERP.MQ_FAILTYPE_03 ;
		}else if(tags.endsWith("04")) {		//单号不存在
			expType = DERP.MQ_FAILTYPE_04 ;
		}
		
		JSONObject jsonObject = JSONObject.parseObject(json) ;
		String keywords = jsonObject.getString("keywords") ;
		
		/**
		 * 2.1查询本异常类型、重推次数小于等于3次的异常单号，
		 */
		List<ExceptionOrderPoolMongo> notExceededOrders = exceptionOrderPoolMongoDao.getSpecifiedGroupList(Operator.le.name(), expType, keywords) ;
		
		/**
		 * 2.2遍历单号查询mysql日志监控 ，判断得到单号是否重推成功，如成功移至历史表。否则，重推报文。
		 */
		
		String[] orderModelCodes = {"1" ,"2" ,"3" ,"6" } ;
		String[] inventoryModelCodes = {"4"} ;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ; 
		
		for (ExceptionOrderPoolMongo exceptionOrderPoolMongo : notExceededOrders) {
			
			String point = exceptionOrderPoolMongo.getPoint();
			String keyword = exceptionOrderPoolMongo.getKeyword();
			String modelCode = exceptionOrderPoolMongo.getModelCode();
			String status = null ;
			Timestamp resendDate = null ;
			String mongoId = null ;
			String collectionName = null ;
			
			if(Arrays.asList(orderModelCodes).contains(modelCode)) {
				
				ConsumeMonitorOrderModel consumeMonitorOrderModel = new ConsumeMonitorOrderModel() ;
				consumeMonitorOrderModel.setKeyword(keyword);
				consumeMonitorOrderModel.setPoint(point);
				consumeMonitorOrderModel.setModelCode(modelCode);
				
				if(exceptionOrderPoolMongo.getType()!=null) {
					consumeMonitorOrderModel.setType(exceptionOrderPoolMongo.getType());
				}
				
				List<ConsumeMonitorOrderModel> consumeMonitorOrderList = consumeMonitorOrderDao.list(consumeMonitorOrderModel);
				
				if(consumeMonitorOrderList.isEmpty()) {
					LOGGER.error("日志记录不存在，接口编码：" + point + ",keyword:" + keyword);
					continue ;
				}
				
				consumeMonitorOrderModel = consumeMonitorOrderList.get(0) ;
				status = consumeMonitorOrderModel.getStatus();
				resendDate =consumeMonitorOrderModel.getResendDate() ;
				mongoId = consumeMonitorOrderModel.getLogId() ;
				collectionName = CollectionEnum.MQ_ORDER_LOG.getCollectionName() ;
				
			}else if(Arrays.asList(inventoryModelCodes).contains(modelCode)) {
				ConsumeMonitorInventoryModel consumeMonitorInventoryModel = new ConsumeMonitorInventoryModel() ;
				consumeMonitorInventoryModel.setKeyword(keyword);
				consumeMonitorInventoryModel.setPoint(point);
				consumeMonitorInventoryModel.setModelCode(modelCode);
				
				if(exceptionOrderPoolMongo.getType()!=null) {
					consumeMonitorInventoryModel.setType(exceptionOrderPoolMongo.getType());
				}
				
				List<ConsumeMonitorInventoryModel> consumeMonitorInventoryModelList = consumeMonitorInventoryDao.list(consumeMonitorInventoryModel) ;
				
				if(consumeMonitorInventoryModelList.isEmpty()) {
					LOGGER.error("日志记录不存在，接口编码：" + point + ",keyword:" + keyword);
					continue ;
				}
				
				consumeMonitorInventoryModel = consumeMonitorInventoryModelList.get(0) ;
				status = consumeMonitorInventoryModel.getStatus();
				resendDate =consumeMonitorInventoryModel.getResendDate() ;
				mongoId = consumeMonitorInventoryModel.getLogId() ;
				collectionName = CollectionEnum.MQ_INVENTORY_LOG.getCollectionName() ;
				
			}else {
				LOGGER.error("非业务、库存日志记录，接口编码：" + point + ",keyword:" + keyword);
				continue ;
			}
			
			/**
			 * 2.2.1若日志监控状态为重推成功或已关闭则从当前异常单号池把单号移到历史异常单号池
			 */
			if("2".equals(status) || "3".equals(status)) {
				
				ExceptionOrderHistoryPoolMongo historyPoolMongo = changePoolMongo2HistoryMongo(exceptionOrderPoolMongo);
				
				if( resendDate != null) {
					String rePushDate = sdf.format(resendDate) ;
					historyPoolMongo.setRePushDate(rePushDate);
				}
				
				exceptionOrderHistoryPoolMongoDao.insert(historyPoolMongo);
				
				Map<String , Object> removeMap = new HashMap<String , Object>() ;
				removeMap.put("point", exceptionOrderPoolMongo.getPoint()) ;
				removeMap.put("modelCode", exceptionOrderPoolMongo.getModelCode()) ;
				removeMap.put("keyword", exceptionOrderPoolMongo.getKeyword()) ;
				removeMap.put("type", exceptionOrderPoolMongo.getType()) ;
				removeMap.put("expType", exceptionOrderPoolMongo.getExpType()) ;
				exceptionOrderPoolMongoDao.remove(removeMap);
			}
			/**
			 * 2.2.2获取mongodb报文Id获取报文、推送报文、从当前异常单号池把单号移到历史异常单号池
			 */
			else if("0".equals(status)){
				
				Map<String , Object> logQueryMap = new HashMap<String , Object>() ;
				logQueryMap.put("id", mongoId) ;
				
				net.sf.json.JSONObject logMap = jSONMongoDao.findOne(logQueryMap, collectionName);
				
				if(logMap != null) {
					String rePushRequestMessage = logMap.getString("requestMessage");
					String rePushTags = logMap.getString("tags");
					String rePushTopics = logMap.getString("topics");
					
					ExceptionOrderHistoryPoolMongo historyPoolMongo = changePoolMongo2HistoryMongo(exceptionOrderPoolMongo);
					
					String rePushDate = sdf.format(new Date()) ;
					historyPoolMongo.setRePushDate(rePushDate);
					
					exceptionOrderHistoryPoolMongoDao.insert(historyPoolMongo);
					
					Map<String , Object> removeMap = new HashMap<String , Object>() ;
					removeMap.put("point", exceptionOrderPoolMongo.getPoint()) ;
					removeMap.put("modelCode", exceptionOrderPoolMongo.getModelCode()) ;
					removeMap.put("keyword", exceptionOrderPoolMongo.getKeyword()) ;
					removeMap.put("type", exceptionOrderPoolMongo.getType()) ;
					removeMap.put("expType", exceptionOrderPoolMongo.getExpType()) ;
					exceptionOrderPoolMongoDao.remove(removeMap);
					
					SendResult send = rocketMQProducer.send(rePushRequestMessage, rePushTopics, rePushTags);
					System.out.println(send.toString());
					
				}else {
					LOGGER.error("mongo日志记录不存在，接口编码：" + point + ",keyword:" + keyword);
					continue ;
				}
				
			}
		}
		
		/**
		 *  3. 查询重推次数大于3次的异常单号邮件发送邮件
		 */
		List<ExceptionOrderPoolMongo> exceededOrders = exceptionOrderPoolMongoDao.getSpecifiedGroupList(Operator.gt.name(), expType, keywords) ;
		for (ExceptionOrderPoolMongo orderPoolMongo : exceededOrders) {
			
			/**
			 * 3.1关联mysql查询是否重推成功或者已关闭
			 */
			String point = orderPoolMongo.getPoint();
			String keyword = orderPoolMongo.getKeyword();
			String modelCode = orderPoolMongo.getModelCode();
			String status = null ;
			Timestamp resendDate = null ;
			
			if(Arrays.asList(orderModelCodes).contains(modelCode)) {
				
				ConsumeMonitorOrderModel consumeMonitorOrderModel = new ConsumeMonitorOrderModel() ;
				consumeMonitorOrderModel.setKeyword(keyword);
				consumeMonitorOrderModel.setPoint(point);
				consumeMonitorOrderModel.setModelCode(modelCode);
				
				if(orderPoolMongo.getType()!=null) {
					consumeMonitorOrderModel.setType(orderPoolMongo.getType());
				}
				
				List<ConsumeMonitorOrderModel> listOrder = consumeMonitorOrderDao.list(consumeMonitorOrderModel);
				
				if(listOrder.isEmpty()) {
					LOGGER.error("日志记录不存在，接口编码：" + point + ",keyword:" + keyword);
					continue ;
				}
				
				consumeMonitorOrderModel = listOrder.get(0) ;
				status = consumeMonitorOrderModel.getStatus();
				resendDate =consumeMonitorOrderModel.getResendDate() ;
				
			}else if(Arrays.asList(inventoryModelCodes).contains(modelCode)) {
				ConsumeMonitorInventoryModel consumeMonitorInventoryModel = new ConsumeMonitorInventoryModel() ;
				consumeMonitorInventoryModel.setKeyword(keyword);
				consumeMonitorInventoryModel.setPoint(point);
				consumeMonitorInventoryModel.setModelCode(modelCode);
				
				if(orderPoolMongo.getType()!=null) {
					consumeMonitorInventoryModel.setType(orderPoolMongo.getType());
				}
				
				List<ConsumeMonitorInventoryModel> listInventory = consumeMonitorInventoryDao.list(consumeMonitorInventoryModel);
				
				if(listInventory.isEmpty()) {
					LOGGER.error("日志记录不存在，接口编码：" + point + ",keyword:" + keyword);
					continue ;
				}
				
				consumeMonitorInventoryModel = listInventory.get(0) ;
				status = consumeMonitorInventoryModel.getStatus();
				resendDate =consumeMonitorInventoryModel.getResendDate() ;
				
			}else {
				LOGGER.error("非业务、库存日志记录，接口编码：" + point + ",keyword:" + keyword);
				continue ;
			}
			
			/**
			 * 3.2.1若日志监控状态为重推成功或已关闭则从当前异常单号池把单号移到历史异常单号池
			 */
			if("2".equals(status) || "3".equals(status)) {
				
				ExceptionOrderHistoryPoolMongo historyPoolMongo = changePoolMongo2HistoryMongo(orderPoolMongo);
				
				if( resendDate != null) {
					String rePushDate = sdf.format(resendDate) ;
					historyPoolMongo.setRePushDate(rePushDate);
				}
				
				exceptionOrderHistoryPoolMongoDao.insert(historyPoolMongo);
				
				Map<String , Object> removeMap = new HashMap<String , Object>() ;
				removeMap.put("point", orderPoolMongo.getPoint()) ;
				removeMap.put("modelCode", orderPoolMongo.getModelCode()) ;
				removeMap.put("keyword", orderPoolMongo.getKeyword()) ;
				removeMap.put("type", orderPoolMongo.getType()) ;
				removeMap.put("expType", orderPoolMongo.getExpType()) ;
				exceptionOrderPoolMongoDao.remove(removeMap);
				
			}
		}
		
		/**
		 * 3.2.2 04状态未最后触发的MQ消费段，触发邮件，若日志监控状态为非重推成功则发送邮件
		 */
		
		/*List<ExceptionOrderPoolMongo> emailOrders = exceptionOrderPoolMongoDao.getSpecifiedGroupList(Operator.gt.name(), null, keywords) ;
		if(DERP.MQ_FAILTYPE_04.equals(expType) && !emailOrders.isEmpty()) {
			net.sf.json.JSONArray sendMailJSONArray = new JSONArray();
			
			//设置只发30条，防止蓝精灵大小出错
			int size = emailOrders.size() ;
			if(size > 30) {
				size = 30 ;
			}
			
			for (int i = 0 ; i < size ; i ++) {
				ExceptionOrderPoolMongo orderPoolMongo = emailOrders.get(i) ;
				JSONObject sendMailItem = new JSONObject();
				
				String modelCode = orderPoolMongo.getModelCode() ;
				String type = orderPoolMongo.getType() ;
				String tempExpType = orderPoolMongo.getExpType() ;
				
				if(StringUtils.isNotBlank(modelCode)) {
					switch (modelCode) {
					case "1":
						modelCode = "业务模块" ;
						break;
					case "2":
						modelCode = "推送外部API" ;
						break;
					case "3":
						modelCode = "仓储模块" ;
						break;
					case "4":
						modelCode = "库存模块" ;
						break;
	
					default:
						break;
					}
				}else {
					modelCode = "" ;
				}
				
				if(StringUtils.isNotBlank(tempExpType)) {
					switch (tempExpType) {
					case DERP.MQ_FAILTYPE_01:
						tempExpType = "B2C单号不存在" ;
						break;
					case DERP.MQ_FAILTYPE_02:
						tempExpType = "冻结记录不存在" ;
						break;
					case DERP.MQ_FAILTYPE_03:
						tempExpType = "锁记录" ;
						break;
					case DERP.MQ_FAILTYPE_04:
						tempExpType = "单号不存在" ;
						break;
	
					default:
						break;
					}
				}else {
					tempExpType = "" ;
				}
				
				if(StringUtils.isNotBlank(type)) {
					switch (type) {
					case "0":
						type = "冻结" ;
						break;
					case "1":
						type = "解冻" ;
						break;
					case "2":
						type = "国检" ;
						break;
					case "3":
						type = "海关" ;
						break;
	
					default:
						type = "" ;
						break;
					}
				}else {
					type = "" ;
				}
				
				sendMailItem.put("modelCode", modelCode) ;
				sendMailItem.put("point", orderPoolMongo.getPoint()) ;
				sendMailItem.put("keyword", orderPoolMongo.getKeyword()) ;
				sendMailItem.put("pushTimes", orderPoolMongo.getRePushTimes()) ;
				sendMailItem.put("type", type) ;
				sendMailItem.put("expType", tempExpType) ;
				sendMailItem.put("createDate", orderPoolMongo.getCreateDate()) ;
				
				sendMailJSONArray.add(sendMailItem) ;
			}
			
			if(!sendMailJSONArray.isEmpty()) {
				net.sf.json.JSONObject sendMailJson = new net.sf.json.JSONObject();
				JSONObject paramJson = new JSONObject();
				paramJson.put("list", sendMailJSONArray);
				sendMailJson.put("mailCode", SmurfsAPICodeEnum.EMAIL_M013.getCode());// 邮件编码
				sendMailJson.put("recipients",ApolloUtil.smurfsRecipients);
				sendMailJson.put("paramJson", paramJson);// 邮件模板参数 json
				// 调用外部接口发送邮件
				String resultMsg = SmurfsUtils.send(sendMailJson, SmurfsAPIEnum.SMURFS_EMAIL);
				LOGGER.info("经分销盘点结果邮件发送:" + resultMsg);
			}
		}*/
		
		
	}
	
	/**
	 * 异常池对象转成历史表记录
	 * @param poolMongo
	 * @return
	 */
	private ExceptionOrderHistoryPoolMongo changePoolMongo2HistoryMongo(ExceptionOrderPoolMongo poolMongo) {
		ExceptionOrderHistoryPoolMongo historyPoolMongo = new ExceptionOrderHistoryPoolMongo();
		
		historyPoolMongo.setCreateDate(poolMongo.getCreateDate());
		historyPoolMongo.setExpType(poolMongo.getExpType());
		historyPoolMongo.setKeyword(poolMongo.getKeyword());
		historyPoolMongo.setModelCode(poolMongo.getModelCode());
		historyPoolMongo.setPoint(poolMongo.getPoint());
		historyPoolMongo.setRePushTimes(poolMongo.getRePushTimes());
		historyPoolMongo.setType(poolMongo.getType());
		
		return historyPoolMongo ;
	}

}
