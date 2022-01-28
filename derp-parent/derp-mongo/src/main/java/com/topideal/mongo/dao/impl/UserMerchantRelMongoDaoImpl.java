package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.UserMerchantRelMongoDao;
import com.topideal.mongo.entity.UserMerchantRelMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 用户公司关联表
 */
@Repository
public class UserMerchantRelMongoDaoImpl implements UserMerchantRelMongoDao {

	//表名
    private String collectionName= CollectionEnum.USER_MERCHANT_REL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(UserMerchantRelMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public UserMerchantRelMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, UserMerchantRelMongo.class,collectionName);
	}

	@Override
	public List<UserMerchantRelMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<UserMerchantRelMongo> result = mongoTemplate.find(query, UserMerchantRelMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, UserMerchantRelMongo.class,collectionName);
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
        mongoTemplate.remove(query, UserMerchantRelMongo.class, collectionName);
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
	public List<Long> getMerchantListByUser(Long userId) {
		
		List<Long> merchantIds = new ArrayList<Long>() ;
		
		Map<String, Object> params = new HashMap<String, Object>() ;
		params.put("userId", userId) ;
		
		List<UserMerchantRelMongo> result = findAll(params) ;
		
        if(!result.isEmpty()) {
        	for (UserMerchantRelMongo userMerchantRelMongo : result) {
				merchantIds.add(userMerchantRelMongo.getMerchantId()) ;
			}
        }
        
		return merchantIds;
	}

	@Override
	public boolean isUserRelateMerchant(Long userId, Long merchantId) {
		
		Map<String , Object> queryMap = new HashMap<String , Object>() ;
		queryMap.put("merchantId", merchantId) ;
		queryMap.put("userId", userId) ;

		UserMerchantRelMongo relMongo = findOne(queryMap);
		
		if(relMongo != null) {
			return true ;
		}
		
		return false;
	}
    
    
}
