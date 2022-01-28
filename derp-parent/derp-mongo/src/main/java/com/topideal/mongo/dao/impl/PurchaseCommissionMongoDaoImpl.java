package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.PurchaseCommissionMongoDao;
import com.topideal.mongo.entity.PurchaseCommissionMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * 采购执行回购设置MongoService实现类
 * @author gy
 *
 */
@Repository
public class PurchaseCommissionMongoDaoImpl implements PurchaseCommissionMongoDao{
	
	//表名
    private  String  collectionName= CollectionEnum.PURCHASE_COMMISSION.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;
    
	@Override
	public void insert(PurchaseCommissionMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public PurchaseCommissionMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, PurchaseCommissionMongo.class,collectionName);
	}

	@Override
	public List<PurchaseCommissionMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<PurchaseCommissionMongo> result = mongoTemplate.find(query, PurchaseCommissionMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, PurchaseCommissionMongo.class,collectionName);
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
        mongoTemplate.remove(query,PurchaseCommissionMongo.class,collectionName);
	}

	@Override
	public void insertJson(String json) {
		mongoTemplate.insert(json, collectionName);
	}

}
