package com.topideal.mongo.dao.impl;


import com.topideal.mongo.dao.SupplierMerchandisePriceMongoDao;
import com.topideal.mongo.entity.ProductInfoMongo;
import com.topideal.mongo.entity.SupplierMerchandisePriceMongo;
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

@Repository
public class SupplierMerchandisePriceMongoDaoImpl implements SupplierMerchandisePriceMongoDao {
	
	//表名
    private  String  collectionName= CollectionEnum.SUPPLIER_MERCHANDISE_PRICE.getCollectionName();
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(SupplierMerchandisePriceMongo object) {
        mongoTemplate.insert(object, collectionName);
    }
    

	@Override
	public SupplierMerchandisePriceMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,SupplierMerchandisePriceMongo.class,collectionName);
	}

	@Override
	public List<SupplierMerchandisePriceMongo> findAll(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        List<SupplierMerchandisePriceMongo> result = mongoTemplate.find(query,SupplierMerchandisePriceMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update,ProductInfoMongo.class,collectionName);
		
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
	        mongoTemplate.remove(query,ProductInfoMongo.class,collectionName);
		
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
