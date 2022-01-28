package com.topideal.mongo.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.topideal.mongo.dao.ExceptionOrderHistoryPoolMongoDao;
import com.topideal.mongo.entity.ExceptionOrderHistoryPoolMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

@Repository
public class ExceptionOrderHistoryPoolMongoDaoImpl implements ExceptionOrderHistoryPoolMongoDao{

	//表名
    private  String  collectionName= CollectionEnum.MQ_EXCEPTION_ORDER_HISTORY_POOL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(ExceptionOrderHistoryPoolMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public ExceptionOrderHistoryPoolMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, ExceptionOrderHistoryPoolMongo.class,collectionName);
    }

    public List<ExceptionOrderHistoryPoolMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<ExceptionOrderHistoryPoolMongo> result = mongoTemplate.find(query, ExceptionOrderHistoryPoolMongo.class,collectionName);
        return result;
    }

    public void update(Map<String, Object> queryParams,Map<String,Object> data) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(queryParams);
        Update update = BaseUtils.parseUpdate(data);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.upsert(query, update, ExceptionOrderHistoryPoolMongo.class,collectionName);
    }

    public void remove(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        mongoTemplate.remove(query,ExceptionOrderHistoryPoolMongo.class,collectionName);
    }

	@Override
	public List<ExceptionOrderHistoryPoolMongo> getsSpecifiedGroupList(Map<String , Object> param) {
		
		String expType = String.valueOf(param.get("expType")) ;
		
		
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteria = new Criteria("expType").is(expType) ;
		
		if(param.get("point") != null) {
			criteria.and("point").is(param.get("point")) ;
		}
		
		if(param.get("type") != null) {
			criteria.and("type").is(param.get("type")) ;
		}
		
		if(param.get("keyword") != null) {
			
			String keyword = (String)param.get("keyword") ;
			String[] keywords = keyword.split(",") ;
			
			criteria.and("keyword").in(Arrays.asList(keywords)) ;
		}
		
		operations.add(Aggregation.match(criteria));
		
		String[] groupArr = new String[] {"point" , "modelCode" , "keyword" , "type" , "expType"} ;
		operations.add(Aggregation.group(groupArr).count().as("rePushTimes")) ;
		
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<ExceptionOrderHistoryPoolMongo> resultList = mongoTemplate.aggregate(aggregation, collectionName , ExceptionOrderHistoryPoolMongo.class);
		
		
		return resultList.getMappedResults();
	}

}
