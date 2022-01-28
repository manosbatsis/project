package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.MerchandiseCustomsRelMongoDao;
import com.topideal.mongo.entity.MerchandiseCustomsRelMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * Created by weixiaolei on 2018/7/4.
 */
@Repository
public class MerchandiseCustomsRelMongoDaoImpl implements MerchandiseCustomsRelMongoDao{
    //表名
    private  String  collectionName= CollectionEnum.MERCHANDISE_CUSTOMS_REL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(MerchandiseCustomsRelMongo object) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public MerchandiseCustomsRelMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,MerchandiseCustomsRelMongo.class,collectionName);
	}

	@Override
	public List<MerchandiseCustomsRelMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<MerchandiseCustomsRelMongo>  result = mongoTemplate.find(query, MerchandiseCustomsRelMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update,MerchandiseCustomsRelMongo.class,collectionName);
		
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
        mongoTemplate.remove(query,MerchandiseCustomsRelMongo.class,collectionName);
	}

	@Override
	public void insertJson(String json) {
		 mongoTemplate.insert(json, collectionName);
		
	}

}
