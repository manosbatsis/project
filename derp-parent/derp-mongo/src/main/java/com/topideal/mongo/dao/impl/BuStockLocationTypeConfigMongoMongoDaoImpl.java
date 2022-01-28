package com.topideal.mongo.dao.impl;

import com.topideal.mongo.dao.BuStockLocationTypeConfigMongoDao;
import com.topideal.mongo.entity.BuStockLocationTypeConfigMongo;
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

/**
 * 事业部库位类型
 */
@Repository
public class BuStockLocationTypeConfigMongoMongoDaoImpl implements BuStockLocationTypeConfigMongoDao {


	//表名
	private  String  collectionName= CollectionEnum.BU_STOCK_LOCATION_TYPE_CONFIG.getCollectionName();
	@Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(BuStockLocationTypeConfigMongo object) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public BuStockLocationTypeConfigMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,BuStockLocationTypeConfigMongo.class,collectionName);
	}

	@Override
	public List<BuStockLocationTypeConfigMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<BuStockLocationTypeConfigMongo>  result = mongoTemplate.find(query, BuStockLocationTypeConfigMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update,BuStockLocationTypeConfigMongo.class,collectionName);
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
        mongoTemplate.remove(query,BuStockLocationTypeConfigMongo.class,collectionName);
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
