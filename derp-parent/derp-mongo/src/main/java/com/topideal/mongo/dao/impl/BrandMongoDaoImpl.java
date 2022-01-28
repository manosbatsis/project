package com.topideal.mongo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.ProductInfoMongoDao;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.ProductInfoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.BrandMongoDao;
import com.topideal.mongo.entity.BrandMongo;
import com.topideal.mongo.entity.ContractNoMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * 品牌
 * @author lian_
 */
@Repository
public class BrandMongoDaoImpl implements BrandMongoDao{

	
	//表名
	private  String  collectionName= CollectionEnum.BRAND.getCollectionName();
	@Autowired
    private MongoTemplate mongoTemplate;
	@Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
    @Autowired
    private ProductInfoMongoDao productInfoMongoDao;
	
	@Override
	public void insert(BrandMongo object) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public BrandMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,BrandMongo.class,collectionName);
	}

	@Override
	public List<BrandMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<BrandMongo>  result = mongoTemplate.find(query, BrandMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update,BrandMongo.class,collectionName);
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
        mongoTemplate.remove(query,BrandMongo.class,collectionName);
	}

    /**
     * 以json的模式插入数据库
     * @param json
     */
    @Override
    public void insertJson(String json) {
        mongoTemplate.insert(json, collectionName);
    }

    /**
     * 获取品牌
     */
    @Override
    public BrandMongo getBrandMongo(Long goodsId) {
        Map<String, Object> goodsParam = new HashMap<>();
        goodsParam.put("merchandiseId", goodsId);
        MerchandiseInfoMogo merchandiseInfoMogo = merchandiseInfoMogoDao.findOne(goodsParam);
        if (merchandiseInfoMogo != null) {
            Map<String, Object> productParam = new HashMap<>();
            productParam.put("productId", merchandiseInfoMogo.getProductId());
            ProductInfoMongo product = productInfoMongoDao.findOne(productParam);
            if (product != null) {
                Map<String, Object> brandParam = new HashMap<>();
                brandParam.put("brandId", product.getBrandId());
                BrandMongo brand = findOne(brandParam);
                if (brand != null){
                    return brand;
                }
            }
        }
        return null;
    }
}
