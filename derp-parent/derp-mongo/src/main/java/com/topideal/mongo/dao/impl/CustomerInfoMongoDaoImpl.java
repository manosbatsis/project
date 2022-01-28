package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.CustomerInfoMongoDao;
import com.topideal.mongo.entity.BatchValidationInfoMongo;
import com.topideal.mongo.entity.CustomerInfoMogo;
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
public class CustomerInfoMongoDaoImpl implements CustomerInfoMongoDao {
    //表名
    private  String  collectionName= CollectionEnum.CUSTOMER_INFO.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(CustomerInfoMogo object) {
        mongoTemplate.insert(object, collectionName);
    }

    public CustomerInfoMogo findOne(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, CustomerInfoMogo.class,collectionName);
    }

    public List<CustomerInfoMogo> findAll(Map<String, Object> params) {
        Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<CustomerInfoMogo> result = mongoTemplate.find(query, CustomerInfoMogo.class,collectionName);
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
        mongoTemplate.upsert(query, update, CustomerInfoMogo.class,collectionName);
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
        mongoTemplate.remove(query,CustomerInfoMogo.class,collectionName);
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
    public List<CustomerInfoMogo> findAllCustomers(Map<BaseUtils.Operator, Map<String, Object>> params) {
        Query query = null;
        Criteria criteria = BaseUtils.customParseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }

        List<CustomerInfoMogo> result = mongoTemplate.find(query, CustomerInfoMogo.class,collectionName);
        return result;
    }


}
