package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.ImportErrorMessage;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.LogService;
import com.topideal.tools.CollectionEnum;
import com.topideal.tools.ParseDaterangepicker;

import net.sf.json.JSONObject;

/**
 * Created by weixiaolei on 2018/7/23.
 */
@Service
public class LogServiceImpl implements LogService {

	@Autowired
	private JSONMongoDao jsonMongoDao;

	@Autowired
	private RMQProducer rmqProducer;

	@Override
	public PageMongo searchPushAPILog(PageMongo pageMongo, String keyword, String state, String point,
			String endDateStr, String id, String differenceTime, String selectScope) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("endDate", ParseDaterangepicker.parse(endDateStr));
		}
		if (StringUtils.isNotBlank(id)) {
			params.put("id", id);
		}
		if (StringUtils.isNotBlank(differenceTime)) {
			params.put("differenceTime", differenceTime);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "endDate");
		if(selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_PUSH_API_HISTORY_LOG.getCollectionName());
		}
		return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_PUSH_API_LOG.getCollectionName());
	}

	@Override
	public PageMongo searchOrderLog(PageMongo pageMongo, String keyword, String state, String point, String endDateStr,
			String id, String differenceTime, String selectScope) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("endDate", ParseDaterangepicker.parse(endDateStr));
		}
		if (StringUtils.isNotBlank(id)) {
			params.put("id", id);
		}
		if (StringUtils.isNotBlank(differenceTime)) {
			params.put("differenceTime", differenceTime);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "endDate");
		if(selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_ORDER_HISTORY_LOG.getCollectionName());
		}
		return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_ORDER_LOG.getCollectionName());
	}

	@Override
	public PageMongo searchInventoryLog(PageMongo pageMongo, String keyword, String state, String point,
			String endDateStr, String id, String differenceTime, String selectScope) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("endDate", ParseDaterangepicker.parse(endDateStr));
		}
		if (StringUtils.isNotBlank(id)) {
			params.put("id", id);
		}
		if (StringUtils.isNotBlank(differenceTime)) {
			params.put("differenceTime", differenceTime);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "endDate");
		if(selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_INVENTORY_HISTORY_LOG.getCollectionName());
		}
		return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_INVENTORY_LOG.getCollectionName());
	}

	@Override
	public PageMongo searchStorageLog(PageMongo pageMongo, String keyword, String state, String point,
			String endDateStr, String id, String differenceTime, String selectScope) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("endDate", ParseDaterangepicker.parse(endDateStr));
		}
		if (StringUtils.isNotBlank(id)) {
			params.put("id", id);
		}
		if (StringUtils.isNotBlank(differenceTime)) {
			params.put("differenceTime", differenceTime);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "endDate");
		if(selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			return jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_STORAGE_HISTORY_LOG.getCollectionName());
		}
		return  jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_STORAGE_LOG.getCollectionName());
	}

	/**
	 * 单个推送
	 * @param id
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean resetSend(String id, String collectionName) throws Exception {
		if (StringUtils.isNotBlank(id)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			JSONObject jsonObject = jsonMongoDao.findOne(params, collectionName);
			if(jsonObject==null) {
				collectionName = getHisTableName(collectionName);
				jsonObject = jsonMongoDao.findOne(params, collectionName);
			}
			String topics = jsonObject.getString("topics");
			String tags = jsonObject.getString("tags");
			String point = String.valueOf(jsonObject.get("point"));
			//页面从推过滤订单100定时器抓取功能
			if (MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTopic().equals(topics)&&
				MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTags().equals(tags)	) {
				return false;
			}
			// e仓从推过滤e仓的单
			if (MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTopic().equals(topics)&&
					MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTags().equals(tags)	) {
				return false;
			}
			String requestMessage = jsonObject.getString("requestMessage");
			if (StringUtils.isBlank(topics) || StringUtils.isBlank(tags)) {
//				throw new ParameterException("topics或tags为空");
			}
			rmqProducer.send(requestMessage, topics, tags);
			return true;
		}
		return false;
	}
    private String getHisTableName(String collectionName){
    	if (CollectionEnum.API_MONITOR.getCollectionName().equals(collectionName)) {
			collectionName = CollectionEnum.API_MONITOR_HISTORY.getCollectionName();
		}
		if (CollectionEnum.MQ_ORDER_LOG.getCollectionName().equals(collectionName)) {
			collectionName = CollectionEnum.MQ_ORDER_HISTORY_LOG.getCollectionName();
		}
		if (CollectionEnum.MQ_INVENTORY_LOG.getCollectionName().equals(collectionName)) {
			collectionName = CollectionEnum.MQ_INVENTORY_HISTORY_LOG.getCollectionName();
		}
		if (CollectionEnum.MQ_STORAGE_LOG.getCollectionName().equals(collectionName)) {
			collectionName = CollectionEnum.MQ_STORAGE_HISTORY_LOG.getCollectionName();
		}
		if (CollectionEnum.MQ_PUSH_API_LOG.getCollectionName().equals(collectionName)) {
			collectionName = CollectionEnum.MQ_PUSH_API_HISTORY_LOG.getCollectionName();
		}
		return collectionName;
    }
	/**
	 * 批量推送
	 * @param ids
	 * @param collectionName
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean resetSend(List<String> ids, String collectionName) throws Exception {
		for (String id : ids) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", id);
			JSONObject jsonObject = jsonMongoDao.findOne(params, collectionName);
			if (jsonObject==null) {
				collectionName = getHisTableName(collectionName);
				jsonObject = jsonMongoDao.findOne(params, collectionName);
			}
			String topics = jsonObject.getString("topics");
			String tags = jsonObject.getString("tags");
			String requestMessage = jsonObject.getString("requestMessage");
			if (StringUtils.isBlank(topics) || StringUtils.isBlank(tags)) {
				continue;
			}
			//页面从推过滤订单100定时器抓取功能
			if (MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTopic().equals(topics)&&
				MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTags().equals(tags)	) {
				continue;
			}
			// e仓从推过滤e仓的单
			if (MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTopic().equals(topics)&&
					MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTags().equals(tags)	) {
				continue;
			}
			rmqProducer.send(requestMessage, topics, tags);
			Thread.currentThread().sleep(100);
		}
		return true;
	}

	/**
	 * 导出
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @return
	 * @throws Exception
	 */
	@Override
	public List searchListLog(String keyword, String state, String point, String endDateStr,String differenceTime, String selectScope ,String collectionName,
			String historyCollectionName)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("endDate", ParseDaterangepicker.parse(endDateStr));
		}
		if (StringUtils.isNotBlank(differenceTime)) {
			params.put("differenceTime", differenceTime);
		}
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "endDate");
		
		if(selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			
			List<JSONObject> historyList = jsonMongoDao.findAll(params,sort,historyCollectionName);
			for (JSONObject jsonObject : historyList) {
				String stateLabel = jsonObject.getString("state");
				stateLabel = DERP_LOG.getLabelByKey(DERP_LOG.log_statusList, stateLabel) ;
				
				jsonObject.put("state", stateLabel) ;
			}
			
			return historyList;
		}
		
		List<JSONObject> list = jsonMongoDao.findAll(params, sort, collectionName);
		for (JSONObject jsonObject : list) {
			String stateLabel = jsonObject.getString("state");
			stateLabel = DERP_LOG.getLabelByKey(DERP_LOG.log_statusList, stateLabel) ;
			
			jsonObject.put("state", stateLabel) ;
		}
		
		return list ;
	}

	/**
	 * 导入
	 * @param data
	 * @return
	 */
	@Override
	public Map importByKeyword(Map<Integer, List<List<Object>>> data, String collectionName) {
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;
		List<List<Object>> objects = data.get(0);
		for (int j = 1; j < objects.size(); j++) {
			Map<String, String> map = this.toMap(data.get(0).get(0).toArray(), objects.get(j).toArray());
			try {
				String keyword = map.get("关键字");
				if (keyword == null || "".equals(keyword)) {
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(j + 1);
					message.setMessage("关键字不能为空");
					resultList.add(message);
					failure += 1;
					continue;
				}
				//根据关键字查询出库存mq日志信息
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("keyword", keyword);
				Map<String, String> sort = new HashMap<String, String>();
				sort.put("derection", "DESC");
				sort.put("property", "endDate");
				List<JSONObject> list = jsonMongoDao.findAll(params, sort, collectionName);
				if(list!=null && list.size()>0){
					for (JSONObject jsonObject : list) {
						String point = jsonObject.getString("point");
						String topics = (String)jsonObject.get("topics");
						String tags = (String) jsonObject.get("tags");
						
						//页面从推过滤订单100定时器抓取功能
						if (MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTopic().equals(topics)&&
							MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTags().equals(tags)	) {
							continue;
						}
						// e仓从推过滤e仓的单
						if (MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTopic().equals(topics)&&
								MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTags().equals(tags)	) {
							continue;
						}
						if("12003".equals(point)){
							if(topics==null){
								jsonObject.put("topics", "derp-inventory-mq");
							}
							if(tags==null){
								jsonObject.put("tags", "inventory_quantity_add_lower");
							}
							String requestMessage = jsonObject.getString("requestMessage");
							rmqProducer.send(requestMessage, topics.toString(), tags.toString());
							Thread.currentThread().sleep(200);
						}
					}
					success++;
				}else{
					ImportErrorMessage message = new ImportErrorMessage();
					message.setRows(j + 1);
					message.setMessage("根据关键字找不到日志记录");
					resultList.add(message);
					failure += 1;
					continue;
				}
			}catch(Exception e){
				ImportErrorMessage message = new ImportErrorMessage();
				message.setRows(j + 1);
				message.setMessage("异常信息："+e.getMessage());
				resultList.add(message);
				failure += 1;
				continue;
			}
		}
		Map map = new HashMap();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}
	
	/**
	 * 把两个数组组成一个map
	 * 
	 * @param keys
	 *            键数组
	 * @param values
	 *            值数组
	 * @return 键值对应的map
	 */
	private Map<String, String> toMap(Object[] keys, Object[] values) {
		if (keys != null && values != null && keys.length == values.length) {
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			for (int i = 0; i < keys.length; i++) {
				map.put((String) keys[i], values[i].toString());
			}
			return map;
		}
		return null;
	}

	/**
	 * 统计数量
	 * @param keyword
	 * @param state
	 * @param point
	 * @param endDateStr
	 * @param collectionName
	 * @return
	 */
	@Override
	public Long count(String keyword, String state, String point, String endDateStr,String differenceTime, String selectScope ,String collectionName,
			String historyCollectionName) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword);
		}
		if (StringUtils.isNotBlank(point)) {
			params.put("point", Long.valueOf(point));
		}
		if (StringUtils.isNotBlank(state)) {
			params.put("state", Integer.valueOf(state));
		}
		if (StringUtils.isNotBlank(endDateStr)) {
			params.put("endDate", ParseDaterangepicker.parse(endDateStr));
		}
		if (StringUtils.isNotBlank(differenceTime)) {
			params.put("differenceTime", differenceTime);
		}
		if(selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			return jsonMongoDao.count(params,historyCollectionName);
		}
		return  jsonMongoDao.count(params, collectionName);
	}
}
