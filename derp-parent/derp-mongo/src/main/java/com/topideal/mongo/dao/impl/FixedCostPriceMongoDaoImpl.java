package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.FixedCostPriceMongoDao;
import com.topideal.mongo.entity.FixedCostPriceMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FixedCostPriceMongoDaoImpl implements FixedCostPriceMongoDao {
    //表名
    private String collectionName = CollectionEnum.FIXED_COST_PRICE.getCollectionName();
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }

    @Override
    public List<FixedCostPriceMongo> findAllByIn(String keyName, List valueList) {
        Query query = new Query();
        if(valueList!=null){
            query.addCriteria(Criteria.where(keyName).in(valueList));
        }
        List<FixedCostPriceMongo> result = mongoTemplate.find(query, FixedCostPriceMongo.class,collectionName);
        return result;
    }

    @Override
    public void insert(FixedCostPriceMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    @Override
    public FixedCostPriceMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, FixedCostPriceMongo.class,collectionName);
    }

    @Override
    public List<FixedCostPriceMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<FixedCostPriceMongo> result = mongoTemplate.find(query, FixedCostPriceMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, FixedCostPriceMongo.class,collectionName);
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
        mongoTemplate.remove(query, FixedCostPriceMongo.class,collectionName);
    }
}
