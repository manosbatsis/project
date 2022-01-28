package com.topideal.mongo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.mongo.dao.BrandSuperiorMongoDao;
import com.topideal.mongo.entity.BrandSuperiorMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

/**
 * 标准品牌
 * @author lian_
 */
@Repository
public class BrandSuperiorMongoDaoImpl implements BrandSuperiorMongoDao{

    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
    @Autowired
    private CommbarcodeMongoDao commbarcodeMongoDao ;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao;
	
	//表名
	private  String  collectionName= CollectionEnum.BRAND_SUPERIOR.getCollectionName();
	@Autowired
    private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(BrandSuperiorMongo object) {
		mongoTemplate.insert(object, collectionName);
	}

	@Override
	public BrandSuperiorMongo findOne(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        return mongoTemplate.findOne(query,BrandSuperiorMongo.class,collectionName);
	}

	@Override
	public List<BrandSuperiorMongo> findAll(Map<String, Object> params) {
		Query query = null;
        Criteria criteria = BaseUtils.parseCriteria(params);
        //封闭查询条件
        if(criteria!=null){
            query=new Query(criteria);
        }else{
            query = new Query();
        }
        List<BrandSuperiorMongo>  result = mongoTemplate.find(query, BrandSuperiorMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update,BrandSuperiorMongo.class,collectionName);
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
        mongoTemplate.remove(query,BrandSuperiorMongo.class,collectionName);
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
    public BrandSuperiorMongo getBrandSuperiorByGoodsId(Long goodsId) {
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
        BrandParentMongo brandParent = brandParentMongoDao.findOne(brandParentParams);
        if (brandParent == null) {
            return null;
        }
        Map<String, Object> brandSuperiorParams = new HashMap<String, Object>() ;
        brandSuperiorParams.put("brandSuperiorId", brandParent.getSuperiorParentBrandId());
        BrandSuperiorMongo brandSuperior = findOne(brandSuperiorParams);
        return brandSuperior;
    }
}
