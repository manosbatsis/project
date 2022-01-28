package com.topideal.mongo.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.ExceptionOrderPoolMongoDao;
import com.topideal.mongo.entity.ExceptionOrderPoolMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.BaseUtils.Operator;
import com.topideal.tools.CollectionEnum;

@Repository
public class ExceptionOrderPoolMongoDaoImpl implements ExceptionOrderPoolMongoDao{

	//表名
    private  String  collectionName= CollectionEnum.MQ_EXCEPTION_ORDER_POOL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(ExceptionOrderPoolMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public ExceptionOrderPoolMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, ExceptionOrderPoolMongo.class,collectionName);
    }

    public List<ExceptionOrderPoolMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<ExceptionOrderPoolMongo> result = mongoTemplate.find(query, ExceptionOrderPoolMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, ExceptionOrderPoolMongo.class,collectionName);
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
        mongoTemplate.remove(query,ExceptionOrderPoolMongo.class,collectionName);
    }

    
	@Override
	public List<ExceptionOrderPoolMongo> getSpecifiedGroupList(String operatorType , String expType, String keywords) {
		
		List<AggregationOperation> operations = new ArrayList<>();
		Criteria criteria = new Criteria() ;
		
		if(StringUtils.isNotBlank(expType)) {
			criteria.and("expType").is(expType) ;
		}
		
		/**
		 * 1、如果指定单号，不论次数重推，不发邮件
		 * 2、若无单号，按次数判断是重推还是发邮件
		 */
		if(StringUtils.isNotBlank(keywords)) {
			if(Operator.le.name().equals(operatorType)) {
				String[] keywordArr= keywords.split(",") ;
				criteria.and("keyword").in(Arrays.asList(keywordArr)) ;
			}else {
				return new ArrayList<ExceptionOrderPoolMongo>() ;
			}
		}else {
			if(Operator.le.name().equals(operatorType)) {
				criteria.and("rePushTimes").lte(3) ;
			}else {
				criteria.and("rePushTimes").gt(3) ;
			}
		}
		
		operations.add(Aggregation.match(criteria));
		
		Aggregation aggregation = Aggregation.newAggregation(operations);
		AggregationResults<ExceptionOrderPoolMongo> resultList = mongoTemplate.aggregate(aggregation, collectionName , ExceptionOrderPoolMongo.class);
		
		return resultList.getMappedResults();
	}

}
