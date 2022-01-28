package com.topideal.mongo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.UserBuRelMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;
/**
 * 商家信息 mongoDaoImpl
 * @author lian_
 */
@Repository
public class MerchantBuRelMongoDaoImpl implements MerchantBuRelMongoDao{
	
	//表名
    private  String  collectionName= CollectionEnum.MERCHANT_BU_REL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;
    
	@Override
	public void insert(MerchantBuRelMongo object) {
		mongoTemplate.insert(object,collectionName);
		
	}

	@Override
	public MerchantBuRelMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, MerchantBuRelMongo.class,collectionName);
	}

	@Override
	public List<MerchantBuRelMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<MerchantBuRelMongo> result = mongoTemplate.find(query, MerchantBuRelMongo.class,collectionName);
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
	        mongoTemplate.upsert(query, update, MerchantBuRelMongo.class,collectionName);
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
        mongoTemplate.remove(query,MerchantBuRelMongo.class,collectionName);
	}

    /**
     * 以json的模式插入数据库
     * @param json
     */
	@Override
	public void insertJson(String json) {
		mongoTemplate.insert(json, collectionName);
	}

/*	@Override
	public boolean isMerchantIdRelateBuCode(Long merchantId, String buCode) {
		Map<String , Object> queryMap = new HashMap<String , Object>() ;
		queryMap.put("buCode", buCode) ;
		queryMap.put("merchantId", merchantId) ;

		MerchantBuRelMongo relMongo = findOne(queryMap);

		if(relMongo != null) {
			return true ;
		}

		return false;
	}*/

	/*@Override
	public MerchantBuRelMongo findBuName(Long merchantId,String buCode) {
		Map<String , Object> queryMap = new HashMap<String , Object>() ;
		queryMap.put("buCode", buCode) ;
		queryMap.put("merchantId", merchantId) ;

		MerchantBuRelMongo relMongo = findOne(queryMap);
		return relMongo;
	}*/

}
