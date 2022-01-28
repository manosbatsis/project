package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.ApiExternalConfigMongoDao;
import com.topideal.mongo.entity.ApiExternalConfigMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * 对外接口配置 Dao实现
 * @author liuhanguang
 *
 */
@Repository
public class ApiExternalConfigMongoDaoImpl implements ApiExternalConfigMongoDao{
	
	//表名
    private String collectionName= CollectionEnum.API_EXTERNAL_CONFIG.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(ApiExternalConfigMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public ApiExternalConfigMongo findOne(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        return mongoTemplate.findOne(query, ApiExternalConfigMongo.class,collectionName);
	}

	@Override
	public List<ApiExternalConfigMongo> findAll(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        List<ApiExternalConfigMongo> result = mongoTemplate.find(query, ApiExternalConfigMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, ApiExternalConfigMongo.class,collectionName);
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
        mongoTemplate.remove(query,ApiExternalConfigMongo.class,collectionName);
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
