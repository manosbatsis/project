package com.topideal.mongo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;

/**
 * 汇率管理 Dao实现
 *
 */
@Repository
public class ExchangeRateMongoDaoImpl implements ExchangeRateMongoDao {
	
	//表名
    private String collectionName= CollectionEnum.EXCHANGE_RATE.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(ExchangeRateMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public ExchangeRateMongo findOne(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        return mongoTemplate.findOne(query, ExchangeRateMongo.class,collectionName);
	}

	@Override
	public List<ExchangeRateMongo> findAll(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        List<ExchangeRateMongo> result = mongoTemplate.find(query, ExchangeRateMongo.class,collectionName);
	        return result;
	}

	@Override
	public void update(Map<String, Object> queryParams, Map<String, Object> data) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(queryParams);
        Update update = BaseUtils.parseUpdate(data);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.upsert(query, update, ExchangeRateMongo.class,collectionName);
	}

	@Override
	public void remove(Map<String, Object> params) {
	    Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.remove(query,ExchangeRateMongo.class,collectionName);
	}

    /**
     * 以json的模式插入数据库
     * @param json
     */
    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }

	@Override
	public void removeByOperator(Map<BaseUtils.Operator, Map<String, Object>> params) {
		Query query = null;
		Criteria criteria = BaseUtils.customParseCriteria(params);
		//封闭查询条件
		if(criteria!=null){
			query=new Query(criteria);
		}else{
			query = new Query();
		}
		mongoTemplate.remove(query,ExchangeRateMongo.class,collectionName);
	}

	/**
	 * 根据条件查询距离生效日期最近的一条汇率
	 */
	@Override
	public ExchangeRateMongo findLastRateByParams(Map<String, Object> params) {
		//获取生效时间 
		String effectiveDate = (String) params.get("effectiveDate");
		//移除生效时间 
		params.remove("effectiveDate");
	
		Query query = null;
		//传输参数转化
        Criteria criteria = BaseUtils.parseCriteriaAndDate(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        // 生效时间小于等于传输时间
        query.addCriteria(Criteria.where("effectiveDate").lte(effectiveDate));
        
        //按生效时间降序
        query.with(new Sort(new Sort.Order(Sort.Direction.DESC,"effectiveDate")));
        //分页
        query.skip(0);
        query.limit(1);
        List<JSONObject> jsonList = mongoTemplate.find(query, JSONObject.class,collectionName);
        if (jsonList==null||jsonList.size()==0) {
			return null;
		}
        JSONObject jsonData = jsonList.get(0);
        jsonData.remove("_id");
        jsonData.remove("list");
        jsonData.remove("createDate");
        jsonData.remove("modifyDate");
        
        //json转实体
		Map classMap = new HashMap();
		ExchangeRateMongo exchangeRateMongo =(ExchangeRateMongo) JSONObject.toBean(jsonData, ExchangeRateMongo.class, classMap);      
		return exchangeRateMongo;
	}
}
