package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.CustomerMerchantRelMongoDao;
import com.topideal.mongo.entity.CustomerInfoMogo;
import com.topideal.mongo.entity.CustomerMerchantRelMongo;
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

/**
 * Created by weixiaolei on 2018/7/4.
 */
@Repository
public class CustomerMerchantRelMongoDaoImpl implements CustomerMerchantRelMongoDao {
    //表名
    private  String  collectionName= CollectionEnum.CUSTOMER_MERCHANT_INFO.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(CustomerMerchantRelMongo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public CustomerMerchantRelMongo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, CustomerMerchantRelMongo.class,collectionName);
    }

    public List<CustomerMerchantRelMongo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<CustomerMerchantRelMongo> result = mongoTemplate.find(query, CustomerMerchantRelMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, CustomerMerchantRelMongo.class,collectionName);
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
        mongoTemplate.remove(query,CustomerMerchantRelMongo.class,collectionName);
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
