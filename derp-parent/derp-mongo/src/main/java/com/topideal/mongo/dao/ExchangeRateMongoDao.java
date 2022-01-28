package com.topideal.mongo.dao;

import java.util.Map;

import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.tools.BaseUtils;

/**
 * 汇率管理Dao
 *
 */
public interface ExchangeRateMongoDao extends MongoDao<ExchangeRateMongo>{

	public void insertJson(String json);

	//根据条件删除
	public void removeByOperator(Map<BaseUtils.Operator, Map<String, Object>> params);
	
	// 根据条件获取生效时间小于等于传输时间  的最后一条数据 按时间排序
	public ExchangeRateMongo findLastRateByParams(Map<String, Object> params);
	
}
