package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.AttachmentInfoMongoDao;
import com.topideal.mongo.entity.AttachmentInfoMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

@Repository
public class AttachmentInfoMongoDaoImpl implements AttachmentInfoMongoDao{

	//表名
    private  String  collectionName= CollectionEnum.ATTACHMENT_INFO.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;
    
	@Override
	public void insert(AttachmentInfoMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public AttachmentInfoMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, AttachmentInfoMongo.class,collectionName);
	}

	@Override
	public List<AttachmentInfoMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<AttachmentInfoMongo> result = mongoTemplate.find(query, AttachmentInfoMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, AttachmentInfoMongo.class,collectionName);
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
        mongoTemplate.remove(query,AttachmentInfoMongo.class,collectionName);
	}

	@Override
	public void insertJson(String json) {
		mongoTemplate.insert(json, collectionName);
	}

	
	@Override
	public List<AttachmentInfoMongo> findAllOrderByDate(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        query.with(sort);
        
        List<AttachmentInfoMongo> result = mongoTemplate.find(query, AttachmentInfoMongo.class,collectionName);
        return result;
	}

}
