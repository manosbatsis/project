package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.ReptileConfigMongoDao;
import com.topideal.mongo.entity.ApiSecretConfigMongo;
import com.topideal.mongo.entity.ReptileConfigMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * 爬虫配置表 daoImpl
 * @author lian_
 *
 */
@Repository
public class ReptileConfigMongoDaoImpl implements ReptileConfigMongoDao{

	//表名
    private String collectionName= CollectionEnum.Reptile_Config.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(ReptileConfigMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public ReptileConfigMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, ReptileConfigMongo.class,collectionName);
	}

	@Override
	public List<ReptileConfigMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<ReptileConfigMongo> result = mongoTemplate.find(query, ReptileConfigMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, ReptileConfigMongo.class,collectionName);
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
        mongoTemplate.remove(query,ReptileConfigMongo.class,collectionName);
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
