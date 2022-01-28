package com.topideal.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.dao.ConsumeMonitorApiDao;
import com.topideal.dao.ConsumeMonitorHistoryApiDao;
import com.topideal.dao.ConsumeMonitorHistoryInventoryDao;
import com.topideal.dao.ConsumeMonitorHistoryOrderDao;
import com.topideal.dao.ConsumeMonitorInventoryDao;
import com.topideal.dao.ConsumeMonitorOrderDao;
import com.topideal.entity.dto.ConsumeMonitorMqDTO;
import com.topideal.entity.vo.ConsumeMonitorApiModel;
import com.topideal.entity.vo.ConsumeMonitorHistoryApiModel;
import com.topideal.entity.vo.ConsumeMonitorHistoryInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorHistoryOrderModel;
import com.topideal.entity.vo.ConsumeMonitorInventoryModel;
import com.topideal.entity.vo.ConsumeMonitorMqModel;
import com.topideal.entity.vo.ConsumeMonitorOrderModel;
import com.topideal.entity.vo.ImportErrorMessage;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.service.ConsumeMonitorService;
import com.topideal.tools.CollectionEnum;
import com.topideal.tools.HttpClientUtil;
import com.topideal.tools.ParseDaterangepicker;

import net.sf.json.JSONObject;

/**
 * mq消费监控
 * @author zhanghx
 */
@Service
public class ConsumeMonitorServiceImpl implements ConsumeMonitorService {

	@Autowired
	private ConsumeMonitorApiDao consumeMonitorApiDao;
	// 历史监控日志
	@Autowired
	private ConsumeMonitorHistoryApiDao consumeMonitorHistoryApiDao;
	@Autowired
	private ConsumeMonitorOrderDao consumeMonitorOrderDao;
	// 历史监控日志
	@Autowired
	private ConsumeMonitorHistoryOrderDao consumeMonitorHistoryOrderDao;
	@Autowired
	private ConsumeMonitorInventoryDao consumeMonitorInventoryDao;
	// 历史监控日志
	@Autowired
	private ConsumeMonitorHistoryInventoryDao consumeMonitorHistoryInventoryDao;
	@Autowired
	private JSONMongoDao jsonMongoDao;
	//MQ
	@Autowired
	private RMQProducer rmqProducer;
	

	/**
	 * 分页
	 * @return
	 */
	@Override
	public ConsumeMonitorMqDTO listByPage(ConsumeMonitorMqDTO dto) throws SQLException {
		if (StringUtils.isNotBlank(dto.getEndDateStr())) {
			List<String> list = ParseDaterangepicker.parseToString(dto.getEndDateStr());
			if(list!=null && list.size() == 2){
				dto.setConsumeStartDate(list.get(0));//开始
				dto.setConsumeEndDate(list.get(1));//结束
			}
		}
		if (StringUtils.isNotBlank(dto.getCreateDateStr())) {
			List<String> list = ParseDaterangepicker.parseToString(dto.getCreateDateStr());
			if(list!=null && list.size() == 2){
				dto.setCreateStartDate(list.get(0));//开始
				dto.setCreateEndDate(list.get(1));//结束
			}
		}
		ConsumeMonitorMqDTO retrunDto = new ConsumeMonitorMqDTO();
		if(dto.getSelectScope() != null && dto.getSelectScope().equals(DERP_LOG.LOG_SELECT_SCOPE_2)){//历史表
			if(DERP_LOG.LOG_MODELCODE_1.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_6.equals(dto.getModelCode())){
				retrunDto = consumeMonitorHistoryOrderDao.getListByPage(dto);
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(dto.getModelCode())){
				retrunDto = consumeMonitorHistoryInventoryDao.getListByPage(dto);
			}else if("5".equals(dto.getModelCode())){
				retrunDto = consumeMonitorHistoryApiDao.getListByPage(dto);
			}
		}else{
			if(DERP_LOG.LOG_MODELCODE_1.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(dto.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_6.equals(dto.getModelCode())){
				retrunDto = consumeMonitorOrderDao.getListByPage(dto);
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(dto.getModelCode())){
				retrunDto = consumeMonitorInventoryDao.getListByPage(dto);
			}else if("5".equals(dto.getModelCode())){
				retrunDto = consumeMonitorApiDao.getListByPage(dto);
			}
		}
		
		return retrunDto;
	}

	/**
	 * 统计数量
	 * @param model
	 * @param endDateStr
	 * @return
	 */
	@Override
	public Long count(ConsumeMonitorMqModel model, String endDateStr, String createDateStr, String selectScope) throws SQLException{
		if (StringUtils.isNotBlank(endDateStr)) {
			List<String> list = ParseDaterangepicker.parseToString(endDateStr);
			if(list!=null && list.size() == 2){
				model.setConsumeStartDate(list.get(0));//开始
				model.setConsumeEndDate(list.get(1));//结束
			}
		}
		if (StringUtils.isNotBlank(createDateStr)) {
			List<String> list = ParseDaterangepicker.parseToString(createDateStr);
			if(list!=null && list.size() == 2){
				model.setCreateStartDate(list.get(0));//开始
				model.setCreateEndDate(list.get(1));//结束
			}
		}
		Long count = 0l;
		if(selectScope != null && selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){//历史表
			if(DERP_LOG.LOG_MODELCODE_1.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(model.getModelCode())
					|| DERP_LOG.LOG_MODELCODE_3.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_6.equals(model.getModelCode())){
				count = consumeMonitorHistoryOrderDao.getCount(model);
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(model.getModelCode())){
				count = consumeMonitorHistoryInventoryDao.getCount(model);
			}else if("5".equals(model.getModelCode())){
				count = consumeMonitorHistoryApiDao.getCount(model);
			}
		}else{
			if(DERP_LOG.LOG_MODELCODE_1.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_6.equals(model.getModelCode())){
				count = consumeMonitorOrderDao.getCount(model);
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(model.getModelCode())){
				count = consumeMonitorInventoryDao.getCount(model);
			}else if("5".equals(model.getModelCode())){
				count = consumeMonitorApiDao.getCount(model);
			}
		}
		return count;
	}

	/**
	 * 获取导出集合
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<ConsumeMonitorMqDTO> getExportList(ConsumeMonitorMqDTO model, String endDateStr, Integer count, String createDateStr, String selectScope, String modelCode) throws SQLException {
		List<ConsumeMonitorMqDTO> result = new ArrayList<ConsumeMonitorMqDTO>();//最后要返回的结果集
		if (StringUtils.isNotBlank(endDateStr)) {
			List<String> list = ParseDaterangepicker.parseToString(endDateStr);
			if(list!=null && list.size() == 2){
				model.setConsumeStartDate(list.get(0));//开始
				model.setConsumeEndDate(list.get(1));//结束
			}
		}
		if (StringUtils.isNotBlank(createDateStr)) {
			List<String> list = ParseDaterangepicker.parseToString(createDateStr);
			if(list!=null && list.size() == 2){
				model.setCreateStartDate(list.get(0));//开始
				model.setCreateEndDate(list.get(1));//结束
			}
		}
		model.setModelCode(modelCode);
		
		if(selectScope != null && selectScope.equals(DERP_LOG.LOG_SELECT_SCOPE_2)){//历史表
			if(DERP_LOG.LOG_MODELCODE_1.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_6.equals(model.getModelCode())){
				
				result = consumeMonitorHistoryOrderDao.getExportList(model);
				
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(model.getModelCode())){
				
				result = consumeMonitorHistoryInventoryDao.getExportList(model);
				
			}else if("5".equals(model.getModelCode())){
				
				result = consumeMonitorHistoryApiDao.getExportList(model);
				
			}
		}else{
			if(DERP_LOG.LOG_MODELCODE_1.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(model.getModelCode()) 
					|| DERP_LOG.LOG_MODELCODE_6.equals(model.getModelCode())){
				
				result = consumeMonitorOrderDao.getExportList(model);
				
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(model.getModelCode())){
				
				result = consumeMonitorInventoryDao.getExportList(model);
				
			}else if("5".equals(model.getModelCode())){
				
				result = consumeMonitorApiDao.getExportList(model);
				
			}
		}
		
		return result;
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
	 * 批量关闭
	 * @param list
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean closeBatch(List<Long> list,String modelCode) throws SQLException{
		for (Long id : list) {
			if(DERP_LOG.LOG_MODELCODE_1.equals(modelCode) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(modelCode) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(modelCode)){
				ConsumeMonitorOrderModel model = consumeMonitorOrderDao.searchById(id);
				if(model != null){
					if(model.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
						ConsumeMonitorOrderModel monitor = new ConsumeMonitorOrderModel();
						monitor.setId(model.getId());
						monitor.setStatus(DERP_LOG.LOG_STATUS_2);//已关闭
						consumeMonitorOrderDao.modify(monitor);
					}
				}else{
					ConsumeMonitorHistoryOrderModel model1 = consumeMonitorHistoryOrderDao.searchById(id);
					if(model1 != null){
						if(model1.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
							ConsumeMonitorHistoryOrderModel monitor = new ConsumeMonitorHistoryOrderModel();
							monitor.setId(model.getId());
							monitor.setStatus(DERP_LOG.LOG_STATUS_2);//已关闭
							consumeMonitorHistoryOrderDao.modify(monitor);
						}
					}
				}
				
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(modelCode)){
				ConsumeMonitorInventoryModel model = consumeMonitorInventoryDao.searchById(id);
				if(model != null){
					if(model.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
						ConsumeMonitorInventoryModel monitor = new ConsumeMonitorInventoryModel();
						monitor.setId(model.getId());
						monitor.setStatus(DERP_LOG.LOG_STATUS_2);//已关闭
						consumeMonitorInventoryDao.modify(monitor);
					}
				}else{
					ConsumeMonitorHistoryInventoryModel model1 = consumeMonitorHistoryInventoryDao.searchById(id);
					if(model1 != null){
						if(model1.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
							ConsumeMonitorHistoryInventoryModel monitor = new ConsumeMonitorHistoryInventoryModel();
							monitor.setId(model.getId());
							monitor.setStatus(DERP_LOG.LOG_STATUS_2);//已关闭
							consumeMonitorHistoryInventoryDao.modify(monitor);
						}
					}
				}
			}else if("5".equals(modelCode)){
				ConsumeMonitorApiModel model = consumeMonitorApiDao.searchById(id);
				if(model != null){
					if(model.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
						ConsumeMonitorApiModel monitor = new ConsumeMonitorApiModel();
						monitor.setId(model.getId());
						monitor.setStatus(DERP_LOG.LOG_STATUS_2);//已关闭
						consumeMonitorApiDao.modify(monitor);
					}
				}else{
					ConsumeMonitorHistoryApiModel model1 = consumeMonitorHistoryApiDao.searchById(id);
					if(model1 != null){
						if(model1.getStatus().equals(DERP_LOG.LOG_STATUS_0)){
							ConsumeMonitorHistoryApiModel monitor = new ConsumeMonitorHistoryApiModel();
							monitor.setId(model.getId());
							monitor.setStatus(DERP_LOG.LOG_STATUS_2);//已关闭
							consumeMonitorHistoryApiDao.modify(monitor);
						}
					}
				}
			}	
		}
		return true;
	}

	/**
	 * 查询错误的电商订单日志信息 分页
	 * @return
	 * @throws SQLException
	 */
	@Override
	public ConsumeMonitorMqDTO getErrorListByPage(ConsumeMonitorMqDTO dto) throws SQLException {
		if (StringUtils.isNotBlank(dto.getEndDateStr())) {
			List<String> list = ParseDaterangepicker.parseToString(dto.getEndDateStr());
			if(list!=null && list.size() == 2){
				dto.setConsumeStartDate(list.get(0));//开始
				dto.setConsumeEndDate(list.get(1));//结束
			}
		}
		if (StringUtils.isNotBlank(dto.getCreateDateStr())) {
			List<String> list = ParseDaterangepicker.parseToString(dto.getCreateDateStr());
			if(list!=null && list.size() == 2){
				dto.setCreateStartDate(list.get(0));//开始
				dto.setCreateEndDate(list.get(1));//结束
			}
		}
		if(dto.getSelectScope()!= null && dto.getSelectScope().equals(DERP_LOG.LOG_SELECT_SCOPE_2)){
			return consumeMonitorHistoryOrderDao.getListByPage(dto);
		}
		return consumeMonitorOrderDao.getErrorListByPage(dto);
	}

	/**
	 * 重推所有错误的电商订单日志信息
	 * @return
	 * @throws SQLException
	 */
	@Override
	public boolean allPush(ConsumeMonitorMqModel model, String endDateStr, String createDateStr) throws Exception {
		if (StringUtils.isNotBlank(endDateStr)) {
			List<String> list = ParseDaterangepicker.parseToString(endDateStr);
			if(list!=null && list.size() == 2){
				model.setConsumeStartDate(list.get(0));//开始
				model.setConsumeEndDate(list.get(1));//结束
			}
		}
		if (StringUtils.isNotBlank(createDateStr)) {
			List<String> list = ParseDaterangepicker.parseToString(createDateStr);
			if(list!=null && list.size() == 2){
				model.setCreateStartDate(list.get(0));//开始
				model.setCreateEndDate(list.get(1));//结束
			}
		}
		List<ConsumeMonitorMqModel> list = consumeMonitorOrderDao.getErrorListByPush(model);
		if(list==null || list.size()==0){
			list = consumeMonitorHistoryOrderDao.getErrorListByPush(model);
		}
		//循环推送mq
		for (ConsumeMonitorMqModel mqModel : list) {
			if(mqModel.getLogId()!=null){
				//根据log_id查询报文
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", mqModel.getLogId());
				JSONObject jsonData = jsonMongoDao.findOne(params, CollectionEnum.MQ_ORDER_LOG.getCollectionName());
				if(jsonData == null){
					jsonData = jsonMongoDao.findOne(params, CollectionEnum.MQ_ORDER_HISTORY_LOG.getCollectionName());
				}
				if(jsonData != null){
					String requestMessage = jsonData.getString("requestMessage");
					rmqProducer.send(requestMessage, MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTopic(),  MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTags());
				}
			}
		}
		return true;
	}

	/**
	 * 批量推送
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean resetSend(List<String> ids,String modelCode) throws Exception {
		for (String cmid : ids) {
			JSONObject jsonObject = new JSONObject();
			if(DERP_LOG.LOG_MODELCODE_1.equals(modelCode) 
					|| DERP_LOG.LOG_MODELCODE_2.equals(modelCode) 
					|| DERP_LOG.LOG_MODELCODE_3.equals(modelCode)){
				ConsumeMonitorOrderModel model = consumeMonitorOrderDao.searchById(Long.parseLong(cmid));
				
				if (!DERP_LOG.LOG_STATUS_0.equals(model.getStatus())) {
					continue;
				}
				String collectionName = "";
				if(DERP_LOG.LOG_MODELCODE_1.equals(model.getModelCode())){
					collectionName = CollectionEnum.MQ_ORDER_LOG.getCollectionName();
				}else if(DERP_LOG.LOG_MODELCODE_2.equals(model.getModelCode())){
					collectionName = CollectionEnum.MQ_PUSH_API_LOG.getCollectionName();
				}else if(DERP_LOG.LOG_MODELCODE_3.equals(model.getModelCode())){
					collectionName = CollectionEnum.MQ_STORAGE_LOG.getCollectionName();
				}
				String id = model.getLogId();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", id);
				jsonObject = jsonMongoDao.findOne(params, collectionName);
				if (jsonObject==null) {
					if(DERP_LOG.LOG_MODELCODE_1.equals(model.getModelCode())){
						collectionName = CollectionEnum.MQ_ORDER_HISTORY_LOG.getCollectionName();
					}else if(DERP_LOG.LOG_MODELCODE_2.equals(model.getModelCode())){
						collectionName = CollectionEnum.MQ_PUSH_API_HISTORY_LOG.getCollectionName();
					}else if(DERP_LOG.LOG_MODELCODE_3.equals(model.getModelCode())){
						collectionName = CollectionEnum.MQ_STORAGE_HISTORY_LOG.getCollectionName();
					}
					jsonObject = jsonMongoDao.findOne(params, collectionName);
				}
				String topics = jsonObject.getString("topics");
				String tags = jsonObject.getString("tags");
				String requestMessage = jsonObject.getString("requestMessage");
				if (StringUtils.isBlank(topics) || StringUtils.isBlank(tags)) {
					throw new Exception("topics或tags为空");
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
			}else if(DERP_LOG.LOG_MODELCODE_4.equals(modelCode)){
				ConsumeMonitorInventoryModel model = consumeMonitorInventoryDao.searchById(Long.parseLong(cmid));
				if (!DERP_LOG.LOG_STATUS_0.equals(model.getStatus())) {
					continue;
				}
				String collectionName = CollectionEnum.MQ_INVENTORY_LOG.getCollectionName();
				String id = model.getLogId();
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", id);
				jsonObject = jsonMongoDao.findOne(params, collectionName);
				if (jsonObject==null) {
					collectionName = CollectionEnum.MQ_INVENTORY_HISTORY_LOG.getCollectionName();
					jsonObject = jsonMongoDao.findOne(params, collectionName);
				}
				String topics = jsonObject.getString("topics");
				String tags = jsonObject.getString("tags");
				String requestMessage = jsonObject.getString("requestMessage");
				if (StringUtils.isBlank(topics) || StringUtils.isBlank(tags)) {
					throw new Exception("topics或tags为空");
				}
				rmqProducer.send(requestMessage, topics, tags);
			}else if("5".equals(modelCode)){
				ConsumeMonitorApiModel model1 = new ConsumeMonitorApiModel();
				model1.setId(Long.parseLong(cmid));
				ConsumeMonitorApiModel model = consumeMonitorApiDao.searchByModel(model1);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("id", model.getLogId());
				jsonObject = jsonMongoDao.findOne(params, CollectionEnum.API_MONITOR.getCollectionName());
				if (jsonObject==null) {
					jsonObject = jsonMongoDao.findOne(params, CollectionEnum.API_MONITOR_HISTORY.getCollectionName());
				}
				String str = jsonObject.getString("url");
				 Matcher slashMatcher = Pattern.compile("/").matcher(str);
				 int mIdx = 0;		 
				 while (slashMatcher.find()) {
			            mIdx++;
			            //当"/"符号第三次出现的位置
			            if (mIdx == 3) {
			                break;
			            } 
				 }
				 int indexOf = slashMatcher.start();
				String substring = str.substring(indexOf,str.length());
				String httpUrl=ApolloUtil.apiUrl+substring;
				System.out.println("api重推url："+httpUrl);
	            JSONObject requestJson = (JSONObject) jsonObject.get("requestMessage");
				//http发送到api
				String reString ="";
				if(substring.indexOf("qimen")>0){
					String xml = (String)requestJson.get("xml");
					reString = HttpClientUtil.doPostXml(httpUrl, xml, "utf-8");
				}else{
					reString = HttpClientUtil.doPost(httpUrl, requestJson.toString(), "utf-8");
				}
				System.out.println("api重推结果："+reString);
			}
			//推送完一个后睡眠1毫秒
			Thread.sleep(100);
		}
		return true;
	}

	/**
	 * 批量导入日志监控重推
	 */
	@Override
	public Map importConsumeMonitorData(Map<Integer, List<List<Object>>> data) throws Exception{
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int failure = 0;
		for (int i = 0; i < 1; i++) {// 表头
			List<List<Object>> objects = data.get(i);
			for (int j = 1; j < objects.size(); j++) {
				JSONObject jsonObject = new JSONObject();
			
				Map<String, String> map = this.toMap(data.get(i).get(0).toArray(), objects.get(j).toArray());
				String modelCode = map.get("模块枚举");
				if (checkIsNullOrNot(j, modelCode, "模块枚举不能为空", resultList)) {
					continue;
				}
				String orderNo = map.get("关键字");
				if (checkIsNullOrNot(j, orderNo, "关键字不能为空", resultList)) {
					continue;
				}
				
				String point = map.get("接口编码");
				if (checkIsNullOrNot(j, point, "接口编码不能为空", resultList)) {
					continue;
				}
				String type = map.get("类型枚举");
				// 冻结记录 type 不能为空
				if (DERP_LOG_POINT.POINT_13201301800.equals(point)) {
					if (checkIsNullOrNot(j, point, "库存冻结解冻,类型枚举不能为空", resultList)) {
						continue;
					}
				}
								
				//1业务模块,2 推送外部API,3 仓储模块,4 库存模块				
				String collectionName = "";
				String historyCollectionName="";
				if(DERP_LOG.LOG_MODELCODE_1.equals(modelCode)){
					collectionName = CollectionEnum.MQ_ORDER_LOG.getCollectionName();
					historyCollectionName=CollectionEnum.MQ_ORDER_HISTORY_LOG.getCollectionName();
							
				}else if(DERP_LOG.LOG_MODELCODE_2.equals(modelCode)){
					collectionName = CollectionEnum.MQ_PUSH_API_LOG.getCollectionName();
					historyCollectionName=CollectionEnum.MQ_PUSH_API_HISTORY_LOG.getCollectionName();
				}else if(DERP_LOG.LOG_MODELCODE_3.equals(modelCode)){
					collectionName = CollectionEnum.MQ_STORAGE_LOG.getCollectionName();
					historyCollectionName=CollectionEnum.MQ_STORAGE_HISTORY_LOG.getCollectionName();
				}else if(DERP_LOG.LOG_MODELCODE_4.equals(modelCode)){
					collectionName = CollectionEnum.MQ_INVENTORY_LOG.getCollectionName();
					historyCollectionName=CollectionEnum.MQ_INVENTORY_HISTORY_LOG.getCollectionName();
				}
				
				//1业务模块,2 推送外部API,3 仓储模块,4 库存模块
				if (DERP_LOG.LOG_MODELCODE_1.equals(modelCode)
						|| DERP_LOG.LOG_MODELCODE_2.equals(modelCode) 
						|| DERP_LOG.LOG_MODELCODE_3.equals(modelCode)) {
					ConsumeMonitorOrderModel orderModel=new ConsumeMonitorOrderModel();
					orderModel.setPoint(point);
					orderModel.setKeyword(orderNo);
					orderModel.setStatus(DERP_LOG.LOG_STATUS_0);
					orderModel = consumeMonitorOrderDao.searchByModel(orderModel);
					if (orderModel==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage(orderNo+"属于业务模块,没有找到对应的业务监控");
						resultList.add(message);
						continue;
					}
					// 获取推送报文json tag topic					
					String id = orderModel.getLogId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("id", id);
					jsonObject = jsonMongoDao.findOne(params, collectionName);
					// 查询历史表
					if (jsonObject==null) {					
						jsonObject = jsonMongoDao.findOne(params, historyCollectionName);
					}
					if (jsonObject==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage(orderNo+"属于业务模块,mongdb 报文和历史数据都没有找到报文");
						resultList.add(message);
						continue;
					}
					
					String topics = jsonObject.getString("topics");
					String tags = jsonObject.getString("tags");
					String requestMessage = jsonObject.getString("requestMessage");
					if (StringUtils.isBlank(topics) || StringUtils.isBlank(tags)) {						
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage(orderNo+"topics或tags为空");
						resultList.add(message);
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
					success++;
					//推送完一个后睡眠1毫秒
					Thread.sleep(100);
				}else if (DERP_LOG.LOG_MODELCODE_4.equals(modelCode)) {
					ConsumeMonitorInventoryModel inventoryModel=new ConsumeMonitorInventoryModel();
					inventoryModel.setPoint(point);
					inventoryModel.setKeyword(orderNo);
					inventoryModel.setStatus(DERP_LOG.LOG_STATUS_0);
					inventoryModel.setType(type);
					inventoryModel = consumeMonitorInventoryDao.searchByModel(inventoryModel);
					if (inventoryModel==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage(orderNo+"属于库存模块,没有找到对应的库存监控");
						resultList.add(message);
						continue;
					}
					// 获取推送报文json tag topic					
					String id = inventoryModel.getLogId();
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("id", id);
					jsonObject = jsonMongoDao.findOne(params, collectionName);
					// 查询历史表
					if (jsonObject==null) {					
						jsonObject = jsonMongoDao.findOne(params, historyCollectionName);
					}
					if (jsonObject==null) {
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage(orderNo+"属于库存模块,mongdb 报文和历史数据都没有找到报文");
						resultList.add(message);
						continue;
					}
					String topics = jsonObject.getString("topics");
					String tags = jsonObject.getString("tags");
					String requestMessage = jsonObject.getString("requestMessage");
					if (StringUtils.isBlank(topics) || StringUtils.isBlank(tags)) {						
						ImportErrorMessage message = new ImportErrorMessage();
						message.setRows(j + 1);
						message.setMessage(orderNo+"topics或tags为空");
						resultList.add(message);
						continue;
					}
					rmqProducer.send(requestMessage, topics, tags);
					success++;
					//推送完一个后睡眠1毫秒
					Thread.sleep(100);
					
				}
				
			}	
		}
		failure=resultList.size();
		Map map = new HashMap();
		map.put("success", success);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
	}
	
	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content , 
			String msg ,List<ImportErrorMessage> resultList ) {
		
		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);
			
			return true ;
			
		}else {
			return false ;
		}
		
	}

}
