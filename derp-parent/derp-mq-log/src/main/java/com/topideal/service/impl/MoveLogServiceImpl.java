package com.topideal.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.dao.ConsumeMonitorApiDao;
import com.topideal.dao.ConsumeMonitorHistoryApiDao;
import com.topideal.dao.ConsumeMonitorHistoryInventoryDao;
import com.topideal.dao.ConsumeMonitorHistoryOrderDao;
import com.topideal.dao.ConsumeMonitorInventoryDao;
import com.topideal.dao.ConsumeMonitorOrderDao;
import com.topideal.dao.LogStreamApiDao;
import com.topideal.dao.LogStreamHistoryApiDao;
import com.topideal.dao.LogStreamHistoryInventoryDao;
import com.topideal.dao.LogStreamHistoryOrderDao;
import com.topideal.dao.LogStreamInventoryDao;
import com.topideal.dao.LogStreamOrderDao;
import com.topideal.dao.LogWarningMqDao;
import com.topideal.entity.vo.ConsumeMonitorApiModel;
import com.topideal.entity.vo.ConsumeMonitorHistoryApiModel;
import com.topideal.entity.vo.ConsumeMonitorHistoryInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorHistoryOrderModel;
import com.topideal.entity.vo.ConsumeMonitorInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorOrderModel;
import com.topideal.entity.vo.LogStreamApiModel;
import com.topideal.entity.vo.LogStreamHistoryApiModel;
import com.topideal.entity.vo.LogStreamHistoryInventoryModel;
import com.topideal.entity.vo.LogStreamHistoryOrderModel;
import com.topideal.entity.vo.LogStreamInventoryModel;
import com.topideal.entity.vo.LogStreamOrderModel;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.MoveLogService;
import com.topideal.tools.CollectionEnum;
import com.topideal.tools.MonthUtils;

import net.sf.json.JSONObject;

/**
 * 日志搬迁
 * 
 * @author zhanghx
 */
@Service
public class MoveLogServiceImpl implements MoveLogService {

	private static final Logger LOGGER = Logger.getLogger(MoveLogServiceImpl.class);

	// 日志
	@Autowired
	private JSONMongoDao jsonMongoDao;
	// api监控日志
	@Autowired
	private ConsumeMonitorApiDao consumeMonitorApiDao;
	// api历史监控日志
	@Autowired
	private ConsumeMonitorHistoryApiDao consumeMonitorHistoryApiDao;
	// api日志流水
	@Autowired
	private LogStreamApiDao logStreamApiDao;
	// api历史日志流水
	@Autowired
	private LogStreamHistoryApiDao logStreamHistoryApiDao;
	// order监控日志
	@Autowired
	private ConsumeMonitorOrderDao consumeMonitorOrderDao;
	// order历史监控日志
	@Autowired
	private ConsumeMonitorHistoryOrderDao consumeMonitorHistoryOrderDao;
	// order日志流水
	@Autowired
	private LogStreamOrderDao logStreamOrderDao;
	// order历史日志流水
	@Autowired
	private LogStreamHistoryOrderDao logStreamHistoryOrderDao;
	// 库存监控日志
	@Autowired
	private ConsumeMonitorInventoryDao consumeMonitorInventoryDao;
	// 库存历史监控日志
	@Autowired
	private ConsumeMonitorHistoryInventoryDao consumeMonitorHistoryInventoryDao;
	// 库存日志流水
	@Autowired
	private LogStreamInventoryDao logStreamInventoryDao;
	// 库存历史日志流水
	@Autowired
	private LogStreamHistoryInventoryDao logStreamHistoryInventoryDao;
	// 日志预警
	@Autowired
	private LogWarningMqDao logWarningMqDao;

	/**
	 * 日志搬迁 每天3点 mongodb、mysql
	 * 60天前的日志搬到历史表
	 * 180天前的日志清空
	 * @throws Exception
	 */
	@Override
	public void synsMoveLog(String json, String topics, String tags) throws Exception {
		//获取60天前的日期
		String month2 = MonthUtils.getPreviousDay(60);
		//获取180天前的日期
		String month6 = MonthUtils.getPreviousDay(180);

		LOGGER.info("-----------------monogoDB日志搬迁开始");
		// mongoDB
		// api日志
		moveLogByMongoDB(CollectionEnum.API_MONITOR.getCollectionName(),CollectionEnum.API_MONITOR_HISTORY.getCollectionName(), month2, month6);
		// order日志
		moveLogByMongoDB(CollectionEnum.MQ_ORDER_LOG.getCollectionName(),CollectionEnum.MQ_ORDER_HISTORY_LOG.getCollectionName(), month2, month6);
		// 库存日志
		moveLogByMongoDB(CollectionEnum.MQ_INVENTORY_LOG.getCollectionName(),CollectionEnum.MQ_INVENTORY_HISTORY_LOG.getCollectionName(), month2, month6);
		// 仓储日志
		moveLogByMongoDB(CollectionEnum.MQ_STORAGE_LOG.getCollectionName(),CollectionEnum.MQ_STORAGE_HISTORY_LOG.getCollectionName(), month2, month6);
		// 推送外部api日志
		moveLogByMongoDB(CollectionEnum.MQ_PUSH_API_LOG.getCollectionName(),CollectionEnum.MQ_PUSH_API_HISTORY_LOG.getCollectionName(), month2, month6);
		// 爬虫日志
		moveCrawlerLogByMongoDB(CollectionEnum.CRAWLER_BILL_LOG.getCollectionName(),month6);

		LOGGER.info("-----------------monogoDB日志搬迁结束");

		LOGGER.info("-----------------mysql日志搬迁开始");
		/**日志监控API*/
		moveMonitorApiLog(month2, month6);
		/**日志监控业务*/
		moveMonitorOrderLog(month2, month6);
		/**日志监控库存*/
		moveMonitorInventoryLog(month2, month6);

		/** 日志流水API*/
		moveLogStreamApi(month2, month6);
		/** 日志流水业务*/
		moveLogStreamOrder(month2, month6);
		/** 日志流水库存*/
		moveLogStreamInventory(month2, month6);

		/**
		 * 日志预警
		 * 清除6个月前的历史数据
		 * */
		logWarningMqDao.delByCreateTime(month6);
		
		LOGGER.info("-----------------mysql日志搬迁结束");
	}

	/**
	 * 具体的日志搬迁 把前两个月的日志mongodb切到历史表 同时清除历史表6个月前的日志
	 * 
	 * @param tableName
	 * @param month
	 * @throws Exception
	 */
	public void moveLogByMongoDB(String oldTableName, String newTableName, String month2, String month6)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ltEndDate", sdf.parse(month2).getTime());
		Integer begin2 = 0;// 开始位置
		Integer pageSize2 = 1000;// 每页条数
		while (true) {
			PageMongo pageMongo = new PageMongo();
			pageMongo.setBegin(begin2);
			pageMongo.setPageSize(pageSize2);
			pageMongo = jsonMongoDao.findAll(params, null, pageMongo, oldTableName);
			List<JSONObject> result = pageMongo.getList();
			// 如果已经没有失败的单，则退出循环
			if (result == null || result.size() == 0) {
				break;
			}
			Map<String, Object> removeParams = new HashMap<String, Object>();
			for (JSONObject json : result) {
				// 移动到历史表
				json.remove("_id");
				jsonMongoDao.insertJson(json.toString(), newTableName);
				// 删除
				removeParams.put("id", json.getString("id"));
				jsonMongoDao.remove(removeParams, oldTableName);
			}
			// begin2 += pageSize2;
		}
		// 清除历史表6个月前的日志
		params.put("ltEndDate", sdf.parse(month6).getTime());
		Integer begin6 = 0;// 开始位置
		Integer pageSize6 = 1000;// 每页条数
		while (true) {
			PageMongo pageMongo = new PageMongo();
			pageMongo.setBegin(begin6);
			pageMongo.setPageSize(pageSize6);
			pageMongo = jsonMongoDao.findAll(params, null, pageMongo, newTableName);
			List<JSONObject> result = pageMongo.getList();
			// 如果已经没有失败的单，则退出循环
			if (result == null || result.size() == 0) {
				break;
			}
			Map<String, Object> removeParams = new HashMap<String, Object>();
			for (JSONObject json : result) {
				removeParams.put("id", json.getString("id"));
				jsonMongoDao.remove(removeParams, newTableName);
			}
			// begin6 += pageSize6;
		}
	}
	/**
	 *  删除mongodb爬虫日志6个月以前的日志
	 * 
	 * @param tableName
	 * @param month
	 * @throws Exception
	 */
	public void moveCrawlerLogByMongoDB( String newTableName,  String month6)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> params = new HashMap<String, Object>();
		// 清除历史表6个月前的日志
		params.put("ltcreateDate", sdf.parse(month6).getTime());
		Integer begin6 = 0;// 开始位置
		Integer pageSize6 = 1000;// 每页条数
		PageMongo pageMongo = new PageMongo();
		while (true) {
			pageMongo.setBegin(begin6);
			pageMongo.setPageSize(pageSize6);
			pageMongo = jsonMongoDao.findAll(params, null, pageMongo, newTableName);
			List<JSONObject> result = pageMongo.getList();
			// 如果已经没有失败的单，则退出循环
			if (result == null || result.size() == 0) {
				break;
			}
			Map<String, Object> removeParams = new HashMap<String, Object>();
			for (JSONObject json : result) {
				removeParams.put("createDate", json.getLong("createDate"));
				jsonMongoDao.remove(removeParams, newTableName);
			}
			// begin6 += pageSize6;
		}
	}
	/**
	 * 日志监控-API
	 * 60天前的日志搬到历史表
	 * 180天前的日志清空
	 * */
	public void moveMonitorApiLog(String month2,String month6)throws Exception{
		
		//查询60天以前的成功/重推成功/已关闭的日志搬到历史表
		while(true){
				ConsumeMonitorApiModel monitorApiModel = new ConsumeMonitorApiModel();
				monitorApiModel.setMonth(month2);
				monitorApiModel.setPageSize(1000);
				monitorApiModel = consumeMonitorApiDao.getListByMonth(monitorApiModel);
				if (monitorApiModel == null || monitorApiModel.getList() == null || monitorApiModel.getList().size() == 0) {
					break;
				}
				List<ConsumeMonitorApiModel> monitorList = monitorApiModel.getList();
				List<Long> apiIds = new ArrayList<Long>();
				// 移动到历史监控日志
				for (ConsumeMonitorApiModel mqModel : monitorList) {
					ConsumeMonitorHistoryApiModel model = new ConsumeMonitorHistoryApiModel();
					model.setId(mqModel.getId());
					model.setCreateTime(mqModel.getCreateTime());
					model.setModelCode(mqModel.getModelCode());
					model.setCloseTime(mqModel.getCloseTime());
					model.setModel(mqModel.getModel());
					model.setConsumeDate(mqModel.getConsumeDate());
					model.setKeyword(mqModel.getKeyword());
					model.setPoint(mqModel.getPoint());
					model.setStatus(mqModel.getStatus());
					model.setLogId(mqModel.getLogId());
					model.setType(mqModel.getType());
					model.setDifferenceTime(mqModel.getDifferenceTime());
					model.setExpMsg(mqModel.getExpMsg());
					model.setIsResend(mqModel.getIsResend());
					model.setResendDate(mqModel.getResendDate());
					consumeMonitorHistoryApiDao.save(model);
					apiIds.add(mqModel.getId());
				}
				// 删除
				consumeMonitorApiDao.delete(apiIds);
	    }
		
		//历史表-清除6个月前的api历史数据
		consumeMonitorHistoryApiDao.delByCreateTime(month6);
		
	}
	/**
	 * 日志监控-业务
	 * 60天前的日志搬到历史表
	 * 180天前的日志清空
	 * */
	public void moveMonitorOrderLog(String month2,String month6)throws Exception{
		 //查询60天以前的成功/重推成功/已关闭的日志搬到历史表
		 while(true){
				ConsumeMonitorOrderModel monitorOrderModel = new ConsumeMonitorOrderModel();
				monitorOrderModel.setMonth(month2);
				monitorOrderModel.setPageSize(1000);
				monitorOrderModel = consumeMonitorOrderDao.getListByMonth(monitorOrderModel);
				if(monitorOrderModel == null || monitorOrderModel.getList() == null || monitorOrderModel.getList().size() == 0) {
					break;
				}
				List<ConsumeMonitorOrderModel> monitorOrderList = monitorOrderModel.getList();
				List<Long> orderIds = new ArrayList<Long>();
				// 移动到历史监控日志
				for(ConsumeMonitorOrderModel mqModel : monitorOrderList) {
					ConsumeMonitorHistoryOrderModel model = new ConsumeMonitorHistoryOrderModel();
					model.setId(mqModel.getId());
					model.setCreateTime(mqModel.getCreateTime());
					model.setModelCode(mqModel.getModelCode());
					model.setCloseTime(mqModel.getCloseTime());
					model.setModel(mqModel.getModel());
					model.setConsumeDate(mqModel.getConsumeDate());
					model.setKeyword(mqModel.getKeyword());
					model.setPoint(mqModel.getPoint());
					model.setStatus(mqModel.getStatus());
					model.setLogId(mqModel.getLogId());
					model.setType(mqModel.getType());
					model.setDifferenceTime(mqModel.getDifferenceTime());
					model.setExpMsg(mqModel.getExpMsg());
					model.setIsResend(mqModel.getIsResend());
					model.setResendDate(mqModel.getResendDate());
					consumeMonitorHistoryOrderDao.save(model);
					orderIds.add(mqModel.getId());
				}
				// 删除
				consumeMonitorOrderDao.delete(orderIds);
	    }
		//历史表-清除6个月前的order历史数据
		consumeMonitorHistoryOrderDao.delByCreateTime(month6);
		
	}
	/**
	 * 日志监控-库存
	 * 60天前的日志搬到历史表
	 * 180天前的日志清空
	 * */
	public void moveMonitorInventoryLog(String month2,String month6)throws Exception{
		 //查询60天以前的成功/重推成功/已关闭的日志搬到历史表
		 while(true){
				
				ConsumeMonitorInventoryModel monitorInventoryModel = new ConsumeMonitorInventoryModel();
				monitorInventoryModel.setMonth(month2);
				monitorInventoryModel.setPageSize(1000);
				monitorInventoryModel = consumeMonitorInventoryDao.getListByMonth(monitorInventoryModel);
				if (monitorInventoryModel == null || monitorInventoryModel.getList() == null || monitorInventoryModel.getList().size() == 0) {
					break;
				}
				List<ConsumeMonitorInventoryModel> monitorInventoryList = monitorInventoryModel.getList();
				List<Long> inventoryIds = new ArrayList<Long>();
				// 移动到历史监控日志
				for (ConsumeMonitorInventoryModel mqModel : monitorInventoryList) {
					ConsumeMonitorHistoryInventoryModel model = new ConsumeMonitorHistoryInventoryModel();
					model.setId(mqModel.getId());
					model.setCreateTime(mqModel.getCreateTime());
					model.setModelCode(mqModel.getModelCode());
					model.setCloseTime(mqModel.getCloseTime());
					model.setModel(mqModel.getModel());
					model.setConsumeDate(mqModel.getConsumeDate());
					model.setKeyword(mqModel.getKeyword());
					model.setPoint(mqModel.getPoint());
					model.setStatus(mqModel.getStatus());
					model.setLogId(mqModel.getLogId());
					model.setType(mqModel.getType());
					model.setDifferenceTime(mqModel.getDifferenceTime());
					model.setExpMsg(mqModel.getExpMsg());
					model.setIsResend(mqModel.getIsResend());
					model.setResendDate(mqModel.getResendDate());
					consumeMonitorHistoryInventoryDao.save(model);
					inventoryIds.add(mqModel.getId());
				}
				// 删除
				consumeMonitorInventoryDao.delete(inventoryIds);
	    }
		//历史表-清除6个月前的inventory历史数据
		consumeMonitorHistoryInventoryDao.delByCreateTime(month6);
	}
	/**
	 * 日志流水-api
	 * 60天前的日志搬到历史表
	 * 180天前的日志清除
	 * */
	public void moveLogStreamApi(String month2,String month6)throws Exception{
	   //查询60天以前的日志搬到历史表
       while(true) {
        	 
			LogStreamApiModel streamApiModel = new LogStreamApiModel();
			streamApiModel.setMonth(month2);
			streamApiModel.setPageSize(1000);
			streamApiModel = logStreamApiDao.getListByMonthByPage(streamApiModel);
			if (streamApiModel == null || streamApiModel.getList() == null || streamApiModel.getList().size() == 0) {
				break;
			}
			List<LogStreamApiModel> streamApiList = streamApiModel.getList();
			List<Long> apiIds = new ArrayList<Long>();
			// 移动到历史监控日志
			for (LogStreamApiModel mqModel : streamApiList) {
				LogStreamHistoryApiModel model = new LogStreamHistoryApiModel();
				model.setId(mqModel.getId());
				model.setCreateTime(mqModel.getCreateTime());
				model.setModelCode(mqModel.getModelCode());
				model.setModel(mqModel.getModel());
				model.setConsumeDate(mqModel.getConsumeDate());
				model.setKeyword(mqModel.getKeyword());
				model.setPoint(mqModel.getPoint());
				model.setStatus(mqModel.getStatus());
				model.setLogId(mqModel.getLogId());
				model.setType(mqModel.getType());
				model.setDifferenceTime(mqModel.getDifferenceTime());
				model.setIsClose(mqModel.getIsClose());
				logStreamHistoryApiDao.save(model);
				apiIds.add(mqModel.getId());
			}
			// 删除
			logStreamApiDao.delete(apiIds);
		}
       //历史表-清除6个月前的api历史数据
       logStreamHistoryApiDao.delByCreateTime(month6);
	}
	
    public void moveLogStreamOrder(String month2,String month6)throws Exception{
    	//查询60天以前的日志搬到历史表
    	while(true) {
 			
 			LogStreamOrderModel streamOrderModel = new LogStreamOrderModel();
 			streamOrderModel.setMonth(month2);
 			streamOrderModel.setPageSize(1000);
 			streamOrderModel = logStreamOrderDao.getListByMonthByPage(streamOrderModel);
 			if (streamOrderModel == null || streamOrderModel.getList() == null || streamOrderModel.getList().size() == 0) {
 				break;
 			}
 			List<LogStreamOrderModel> streamOrderList = streamOrderModel.getList();
 			List<Long> orderIds = new ArrayList<Long>();
 			// 移动到历史监控日志
 			for (LogStreamOrderModel mqModel : streamOrderList) {
 				LogStreamHistoryOrderModel model = new LogStreamHistoryOrderModel();
 				model.setId(mqModel.getId());
 				model.setCreateTime(mqModel.getCreateTime());
 				model.setModelCode(mqModel.getModelCode());
 				model.setModel(mqModel.getModel());
 				model.setConsumeDate(mqModel.getConsumeDate());
 				model.setKeyword(mqModel.getKeyword());
 				model.setPoint(mqModel.getPoint());
 				model.setStatus(mqModel.getStatus());
 				model.setLogId(mqModel.getLogId());
 				model.setType(mqModel.getType());
 				model.setDifferenceTime(mqModel.getDifferenceTime());
 				model.setIsClose(mqModel.getIsClose());
 				logStreamHistoryOrderDao.save(model);
 				orderIds.add(mqModel.getId());
 			}
 			// 删除
 			logStreamOrderDao.delete(orderIds);
        }
    	//历史表-清除6个月前的order历史数据
        logStreamHistoryOrderDao.delByCreateTime(month6);
    	 
	}
    
    public void moveLogStreamInventory(String month2,String month6)throws Exception{
    	//查询60天以前的日志搬到历史表
        while(true) {
			
			LogStreamInventoryModel streamInventoryModel = new LogStreamInventoryModel();
			streamInventoryModel.setMonth(month2);
			streamInventoryModel.setPageSize(1000);
			streamInventoryModel = logStreamInventoryDao.getListByMonthByPage(streamInventoryModel);
			if (streamInventoryModel == null || streamInventoryModel.getList() == null || streamInventoryModel.getList().size() == 0) {
				break;
			}
			List<LogStreamInventoryModel> streamInventoryList = streamInventoryModel.getList();
			List<Long> inventoryIds = new ArrayList<Long>();
			// 移动到历史监控日志
			for (LogStreamInventoryModel mqModel : streamInventoryList) {
				LogStreamHistoryInventoryModel model = new LogStreamHistoryInventoryModel();
				model.setId(mqModel.getId());
				model.setCreateTime(mqModel.getCreateTime());
				model.setModelCode(mqModel.getModelCode());
				model.setModel(mqModel.getModel());
				model.setConsumeDate(mqModel.getConsumeDate());
				model.setKeyword(mqModel.getKeyword());
				model.setPoint(mqModel.getPoint());
				model.setStatus(mqModel.getStatus());
				model.setLogId(mqModel.getLogId());
				model.setType(mqModel.getType());
				model.setDifferenceTime(mqModel.getDifferenceTime());
				model.setIsClose(mqModel.getIsClose());
				logStreamHistoryInventoryDao.save(model);
				inventoryIds.add(mqModel.getId());
			}
			// 删除
			logStreamInventoryDao.delete(inventoryIds);
		}
        
        //历史表-清除6个月前的inventory历史数据
        logStreamHistoryInventoryDao.delByCreateTime(month6);
    }
}
