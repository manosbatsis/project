package com.topideal.mongo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * 标准品牌
 * @author lian_
 */
@Repository
public class BrandParentMongoDaoImpl implements BrandParentMongoDao{

	
	//表名
	private  String  collectionName= CollectionEnum.BRAND_PARENT.getCollectionName();
	@Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
    @Autowired
    private CommbarcodeMongoDao commbarcodeMongoDao ;
	
	@Override
	public void insert(BrandParentMongo object) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public BrandParentMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,BrandParentMongo.class,collectionName);
	}

	@Override
	public List<BrandParentMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<BrandParentMongo>  result = mongoTemplate.find(query, BrandParentMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update,BrandParentMongo.class,collectionName);
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
        mongoTemplate.remove(query,BrandParentMongo.class,collectionName);
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
     * 通过商品id获取母品牌
     * @param goodsId
     * @return
     * @throws Exception
     */
    @Override
    public BrandParentMongo getBrandParentMongo(Long goodsId) {
        Map<String, Object> goodsParams = new HashMap<String, Object>() ;
        goodsParams.put("merchandiseId", goodsId) ;
        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(goodsParams);
        if(merchandiseInfo == null) {
            throw new RuntimeException("商品不存在,商品id:"+goodsId) ;
        }
        //查询标准条码中的标准品牌
        Map<String, Object> commbarcodeParams = new HashMap<String, Object>() ;
        commbarcodeParams.put("commbarcode", merchandiseInfo.getCommbarcode()) ;
        // 查询标准条码
        CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(commbarcodeParams);
        if (commbarcode == null) {
            throw new RuntimeException("标准条码不存在,商品货号:"+merchandiseInfo.getGoodsNo()) ;
        }
        if (commbarcode.getCommBrandParentId() == null) {
            return null;
        }
        Map<String, Object> brandParentParams = new HashMap<String, Object>() ;
        brandParentParams.put("brandParentId", commbarcode.getCommBrandParentId());
        BrandParentMongo brandParent = findOne(brandParentParams);
        if (brandParent == null) {
            return null;
        }
        return brandParent;
    }

    @Override
    public BrandParentMongo getBrandParentMongoByCommbarcode(String commbarcode) {
        //查询标准条码中的标准品牌
        Map<String, Object> commbarcodeParams = new HashMap<String, Object>() ;
        commbarcodeParams.put("commbarcode", commbarcode) ;
        // 查询标准条码
        CommbarcodeMongo commbarcodeMongo = commbarcodeMongoDao.findOne(commbarcodeParams);
        if (commbarcodeMongo == null) {
            throw new RuntimeException("标准条码不存在,标准条码:"+commbarcode) ;
        }
        if (commbarcodeMongo.getCommBrandParentId() == null) {
            return null;
        }
        Map<String, Object> brandParentParams = new HashMap<String, Object>() ;
        brandParentParams.put("brandParentId", commbarcodeMongo.getCommBrandParentId());
        BrandParentMongo brandParent = findOne(brandParentParams);
        if (brandParent == null) {
            return null;
        }
        return brandParent;
    }


}
