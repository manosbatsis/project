package com.topideal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.mongo.tools.PageMongo;
import com.topideal.service.AutoLogService;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;

@Service
public class AutoLogServiceImpl implements AutoLogService{

	@Autowired
	JSONMongoDao jsonMongoDao;
	//MQ
	@Autowired
	private RMQLogProducer rocketMQProducer;
	
	@SuppressWarnings("rawtypes")
	@Override
	public PageMongo searchLog(PageMongo pageMongo, String modelCode, String point, String keyword, String expType , String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(StringUtils.isNotBlank(modelCode)) {
			params.put("modelCode", modelCode) ;
		}
		
		if(StringUtils.isNotBlank(point)) {
			params.put("point", point) ;
		}
		
		if(StringUtils.isNotBlank(keyword)) {
			params.put("keyword", keyword) ;
		}
		
		if(StringUtils.isNotBlank(expType)) {
			params.put("expType", expType) ;
		}
		
		if(StringUtils.isNotBlank(type)) {
			params.put("type", type) ;
		}
		
		Map<String, String> sort = new HashMap<String, String>();
		sort.put("derection", "DESC");
		sort.put("property", "createDate");
		pageMongo = jsonMongoDao.findAll(params, sort, pageMongo, CollectionEnum.MQ_EXCEPTION_ORDER_POOL.getCollectionName());
		return pageMongo;
		
	}

	@Override
	public void batchResetSend(String ids) throws Exception {
		String[] idArr = ids.split(",");
		
		Map<String , Object > queryMap = new HashMap<String, Object>() ;
		
		Map<String , List<String>> rendMap = new HashMap<String, List<String>>() ;
		
		for (String id : idArr) {
			queryMap.put("id", id) ;
			
			JSONObject poolJson = jsonMongoDao.findOne(queryMap, CollectionEnum.MQ_EXCEPTION_ORDER_POOL.getCollectionName());
			String expType = poolJson.getString("expType") ;
			
			if(rendMap.get(expType) != null) {
				rendMap.get(expType).add(poolJson.getString("keyword")) ;
			}else {
				List<String> list = new ArrayList<String>() ;
				list.add(poolJson.getString("keyword")) ;
				
				rendMap.put(expType, list) ;
			}
		}
		
		for (String expType : rendMap.keySet()) {
			
			List<String> keyList = rendMap.get(expType);
			
			String keywords = listToString(keyList, ',');
			String topic = "" ;
			String tags = "" ;
			
			switch (expType) {
			case DERP.MQ_FAILTYPE_01:
				topic = MQLogEnum.MQ_FAILTYPE_01.getTopic() ;
				tags = MQLogEnum.MQ_FAILTYPE_01.getTags() ;
				
				break;
				
			case DERP.MQ_FAILTYPE_02:
				topic = MQLogEnum.MQ_FAILTYPE_02.getTopic() ;
				tags = MQLogEnum.MQ_FAILTYPE_02.getTags() ;
				
				break;
				
			case DERP.MQ_FAILTYPE_03:
				topic = MQLogEnum.MQ_FAILTYPE_03.getTopic() ;
				tags = MQLogEnum.MQ_FAILTYPE_03.getTags() ;
				
				break;
			case DERP.MQ_FAILTYPE_04:
				topic = MQLogEnum.MQ_FAILTYPE_04.getTopic() ;
				tags = MQLogEnum.MQ_FAILTYPE_04.getTags() ;

				break;
			default:
				break;
			}
			
			JSONObject jsonObject = new JSONObject() ;
			jsonObject.put("keywords", keywords) ;
			rocketMQProducer.send(jsonObject.toString(), topic, tags) ;
		}
	}

	@SuppressWarnings("rawtypes")
	private String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }
}
