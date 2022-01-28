package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.TariffRateConfigMongoDao;
import com.topideal.mongo.entity.TariffRateConfigMongo;
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
public class TariffRateConfigMongoDaoImpl implements TariffRateConfigMongoDao {

    //表名
    private String collectionName = CollectionEnum.TARIFFRATE_CONFIG.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }

    @Override
    public void insert(TariffRateConfigMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    @Override
    public TariffRateConfigMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,TariffRateConfigMongo.class,collectionName);
    }

    @Override
    public List<TariffRateConfigMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<TariffRateConfigMongo> result = mongoTemplate.find(query,TariffRateConfigMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, TariffRateConfigMongo.class,collectionName);
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
        mongoTemplate.remove(query,TariffRateConfigMongo.class,collectionName);

    }
}
