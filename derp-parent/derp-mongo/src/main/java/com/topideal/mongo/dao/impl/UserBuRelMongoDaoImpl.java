package com.topideal.mongo.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.mongo.entity.UserBuRelMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * 用户事业部 daoImpl
 * @author lian_
 *
 */
@Repository
public class UserBuRelMongoDaoImpl implements UserBuRelMongoDao{

	//表名
    private String collectionName= CollectionEnum.USER_BU_REL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(UserBuRelMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public UserBuRelMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, UserBuRelMongo.class,collectionName);
	}

	@Override
	public List<UserBuRelMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<UserBuRelMongo> result = mongoTemplate.find(query, UserBuRelMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, UserBuRelMongo.class,collectionName);
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
        mongoTemplate.remove(query, UserBuRelMongo.class, collectionName);
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
	public List<Long> getBuListByUser(Long userId) {
		
		List<Long> buIds = new ArrayList<Long>() ;
		
		Map<String, Object> params = new HashMap<String, Object>() ;
		params.put("userId", userId) ;
		
		List<UserBuRelMongo> result = findAll(params) ;
		
        if(!result.isEmpty()) {
        	for (UserBuRelMongo userBuRelMongo : result) {
        		buIds.add(userBuRelMongo.getBuId()) ;
			}
        }
        
		return buIds;
	}

	@Override
	public boolean isUserRelateBu(Long userId, Long buId) {
		
		Map<String , Object> queryMap = new HashMap<String , Object>() ;
		queryMap.put("buId", buId) ;
		queryMap.put("userId", userId) ;
		
		UserBuRelMongo relMongo = findOne(queryMap);
		
		if(relMongo != null) {
			return true ;
		}
		
		return false;
	}
    
    
}
