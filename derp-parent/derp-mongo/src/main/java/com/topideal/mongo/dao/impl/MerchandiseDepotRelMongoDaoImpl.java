package com.topideal.mongo.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.MerchandiseDepotRelMongoDao;
import com.topideal.mongo.entity.MerchandiseDepotRelMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;
/**
 * 商品仓库关系表 mongoDaoImpl
 * @author lian_
 */
@Repository
public class MerchandiseDepotRelMongoDaoImpl implements MerchandiseDepotRelMongoDao{
	
	//表名
    private  String  collectionName= CollectionEnum.MERCHANDISE_DEPOT_REL.getCollectionName();

    @Autowired
    private MongoTemplate mongoTemplate;
    
	@Override
	public void insert(MerchandiseDepotRelMongo object) {
		mongoTemplate.insert(object,collectionName);
		
	}

	@Override
	public MerchandiseDepotRelMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query, MerchandiseDepotRelMongo.class,collectionName);
	}

	@Override
	public List<MerchandiseDepotRelMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<MerchandiseDepotRelMongo> result = mongoTemplate.find(query, MerchandiseDepotRelMongo.class,collectionName);
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
	        mongoTemplate.upsert(query, update, MerchandiseDepotRelMongo.class,collectionName);
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
        mongoTemplate.remove(query,MerchandiseDepotRelMongo.class,collectionName);
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
