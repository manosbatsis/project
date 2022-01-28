package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

@Repository
public class DepotMerchantRelMongoDaoImpl implements DepotMerchantRelMongoDao{

	//表名
    private  String  collectionName= CollectionEnum.DEPOT_MERCHANT_REL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(DepotMerchantRelMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public DepotMerchantRelMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, DepotMerchantRelMongo.class,collectionName);
    }

    public List<DepotMerchantRelMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<DepotMerchantRelMongo> result = mongoTemplate.find(query, DepotMerchantRelMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, DepotMerchantRelMongo.class,collectionName);
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
        mongoTemplate.remove(query,DepotMerchantRelMongo.class,collectionName);
    }

    /**
     * 以json的模式插入数据库
     * @param json
     */
    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }

}
