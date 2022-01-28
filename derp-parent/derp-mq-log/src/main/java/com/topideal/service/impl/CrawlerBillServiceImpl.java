package com.topideal.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.mongo.dao.CrawlerBillMongoDao;
import com.topideal.mongo.entity.CrawlerBillMongo;
import com.topideal.service.CrawlerBillService;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * 爬虫日志 MQ
 */
@Service
public class CrawlerBillServiceImpl implements CrawlerBillService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerBillServiceImpl.class);

	//爬虫账单生成出库清单日志Dao
	@Autowired
	CrawlerBillMongoDao CrawlerBillMongoDao;

	@Override
	public boolean save(String json, String topics, String tags) throws Exception {
		LOGGER.info("-----------------爬虫日志 MQ开始----------------------");
		JSONObject object = JSONObject.fromObject(json);
		LOGGER.info("-----------------初始报文："+object.toString()+"----------------------");
		JSONObject jsonObj = (JSONObject) object.get("logJson");
		JSONObject jsonObject = new JSONObject();
		if(jsonObj != null){
			jsonObject = jsonObj;
		}else{
			jsonObject = object;
		}
		CrawlerBillMongo crawlerBillMongo = (CrawlerBillMongo)JSONObject.toBean(jsonObject, CrawlerBillMongo.class);
		CrawlerBillMongoDao.insert(crawlerBillMongo);
		LOGGER.info("-----------------爬虫日志 MQ结束----------------------");
		return true;
	}

}
