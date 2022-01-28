package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.topideal.mongo.dao.BuInventoryMongoDao;
import com.topideal.mongo.entity.BuInventoryMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * Created by weixiaolei on 2018/7/4.
 */
@Repository
public class BuInventoryMongoDaoImpl implements BuInventoryMongoDao {
    //表名
    private  String  collectionName= CollectionEnum.BU_INVENTORY.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(BuInventoryMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public BuInventoryMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, BuInventoryMongo.class,collectionName);
    }

    public List<BuInventoryMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<BuInventoryMongo> result = mongoTemplate.find(query, BuInventoryMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, BuInventoryMongo.class,collectionName);
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
        mongoTemplate.remove(query,BuInventoryMongo.class,collectionName);
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
