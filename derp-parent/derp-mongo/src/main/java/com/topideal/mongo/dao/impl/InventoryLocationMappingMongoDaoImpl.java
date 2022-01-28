package com.topideal.mongo.dao.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.topideal.common.exception.DerpException;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.dao.InventoryLocationMappingMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.InventoryLocationMappingMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.tools.BaseUtils;
import com.topideal.tools.CollectionEnum;

@Repository
public class InventoryLocationMappingMongoDaoImpl implements InventoryLocationMappingMongoDao{

	//表名
    private String collectionName= CollectionEnum.INVENTORY_LOCATION_MAPPING.getCollectionName();
    
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao ;
    @Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
    @Autowired
    private CommbarcodeMongoDao commbarcodeMongoDao ;
    @Autowired
    private BrandParentMongoDao brandParentMongoDao ;
    @Autowired
    private MongoTemplate mongoTemplate;

	@Override
	public void insert(InventoryLocationMappingMongo object) {
		mongoTemplate.insert(object,collectionName);
	}

	@Override
	public InventoryLocationMappingMongo findOne(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        return mongoTemplate.findOne(query, InventoryLocationMappingMongo.class,collectionName);
	}

	@Override
	public List<InventoryLocationMappingMongo> findAll(Map<String, Object> params) {
		 Query query = null;
	        Criteria criteria = BaseUtils.parseCriteria(params);
	        //封闭查询条件
	        if(criteria!=null){
	            query=new Query(criteria);
	        }else{
	            query = new Query();
	        }
	        List<InventoryLocationMappingMongo> result = mongoTemplate.find(query, InventoryLocationMappingMongo.class,collectionName);
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
        mongoTemplate.upsert(query, update, InventoryLocationMappingMongo.class,collectionName);
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
        mongoTemplate.remove(query,InventoryLocationMappingMongo.class,collectionName);
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
	public InventoryLocationMappingMongo getOriGoodsNoMappingModel(String topidealCode, String goodsNo) throws Exception {
		
		/*判断是否卓烨商家和标准品牌为美赞臣
		 * 	若满足返回保存库位映射表原货号，库位映射表查询无数据时抛异常
		 *      若不满足返回空 */
		if(!"0000134".equals(topidealCode)
				|| StringUtils.isBlank(goodsNo)) {
			return null ;
		}
		
		Map<String, Object> merchantParams = new HashMap<String, Object>() ;
		merchantParams.put("topidealCode", topidealCode) ;
		
		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
        
        
        Map<String, Object> goodsParams = new HashMap<String, Object>() ;
        goodsParams.put("goodsNo", goodsNo) ;
        goodsParams.put("merchantId", merchantInfo.getMerchantId()) ;
        
        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(goodsParams);
		
        if(merchandiseInfo == null) {
        	throw new DerpException("根据货号："+ goodsNo +"查询商品不存在") ;
        }
        
        //查询标准条码中的标准品牌
        Map<String, Object> commbarcodeParams = new HashMap<String, Object>() ;
        commbarcodeParams.put("commbarcode", merchandiseInfo.getCommbarcode()) ;		
        CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(commbarcodeParams);        
        if(commbarcode == null) {
        	throw new DerpException("根据货号："+ goodsNo +"查询标准条码不存在") ;
        }
        // 标准品牌id为空
        Long commBrandParentId = commbarcode.getCommBrandParentId();
        if (commBrandParentId==null||commBrandParentId.intValue()==0)return null;
        //根据标准品牌id查询标准品牌
        Map<String, Object> brandParenParams = new HashMap<String, Object>() ;
   	    brandParenParams.put("brandParentId",commBrandParentId) ;
   	    BrandParentMongo brandParent = brandParentMongoDao.findOne(brandParenParams);
        if (brandParent==null) {
        	throw new DerpException("根据货号："+ goodsNo +"查询标准品牌不存在") ;
		}        
        String superiorParentBrand = brandParent.getSuperiorParentBrand();
        if (StringUtils.isBlank(superiorParentBrand)) return null;
        superiorParentBrand = superiorParentBrand.trim();//去空格
        if(!superiorParentBrand.equals("美赞臣")) {
        	return null;
        }
        
        Map<String, Object> invenMappingParams = new HashMap<String, Object>() ;
        invenMappingParams.put("merchantId", merchantInfo.getMerchantId()) ;
        invenMappingParams.put("goodsNo", goodsNo) ;
        
        InventoryLocationMappingMongo invernMapping = findOne(invenMappingParams);
        
        if(invernMapping == null) {
        	throw new DerpException("根据库位货号查询库位映射表记录不存在, 库位货号：" + goodsNo) ;
        }
 		
		return invernMapping;
	}

	/**
	 * 通过原商品获取库位商品信息(e仓/跨境宝b2c)
	 * @param merchantId
	 * @param depotId
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	@Override
	public MerchandiseInfoMogo getLocationMerchandiseInfo(Long merchantId, Long depotId, String goodsNo)
			throws Exception {

		Map<String, Object> goodsParams = new HashMap<String, Object>() ; 
        goodsParams.put("goodsNo", goodsNo) ;
        goodsParams.put("merchantId", merchantId) ;
        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(goodsParams);
        if(merchandiseInfo == null) {
        	throw new DerpException("商品不存在,商家id:"+merchantId+"原货号:"+goodsNo) ;
        }
        
        //查询标准条码中的标准品牌
        Map<String, Object> commbarcodeParams = new HashMap<String, Object>() ;
        commbarcodeParams.put("commbarcode", merchandiseInfo.getCommbarcode()) ;
		// 查询标准条码
        CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(commbarcodeParams);       
        if(commbarcode == null) {
        	throw new DerpException("根据货号："+ goodsNo +"查询标准条码不存在") ;
        }  
        // 标准品牌id为空
        Long commBrandParentId = commbarcode.getCommBrandParentId();
        if (commBrandParentId==null||commBrandParentId.intValue()==0)return null;
        //根据标准品牌id查询标准品牌
        Map<String, Object> brandParenParams = new HashMap<String, Object>() ;
   	    brandParenParams.put("brandParentId",commBrandParentId) ;
   	    BrandParentMongo brandParent = brandParentMongoDao.findOne(brandParenParams);
        if (brandParent==null) {
        	throw new DerpException("根据货号："+ goodsNo +"查询标准品牌不存在") ;
		}        
        String superiorParentBrand = brandParent.getSuperiorParentBrand();
        if (StringUtils.isBlank(superiorParentBrand)) return null;
        superiorParentBrand = superiorParentBrand.trim();//去空格
        if(!superiorParentBrand.equals("美赞臣")) {
        	return null;
        }
        
        Map<String, Object> invenMappingParams = new HashMap<String, Object>() ;        
        invenMappingParams.put("merchantId", merchantId) ;
        invenMappingParams.put("originalGoodsNo", goodsNo) ;
        invenMappingParams.put("isorRappoint", "1");
        List<InventoryLocationMappingMongo> inventoryLocationList = findAll(invenMappingParams);  
        if (inventoryLocationList==null||inventoryLocationList.size()==0) {
        	throw new DerpException("根据原货号id查询库位映射表(指定商品)记录不存在,商家id:"+merchantId+"原货号:"+goodsNo) ;
		}
        if (inventoryLocationList.size()>1) {
        	throw new DerpException("根据原货号id查询库位映射表(指定商品)记录存在多条记录,商家id:"+merchantId+"原货号:"+goodsNo) ;
		}
        
        InventoryLocationMappingMongo mongo = inventoryLocationList.get(0);
        Map<String, Object> locationParams= new HashMap<>();
		locationParams.put("merchandiseId", mongo.getGoodsId()) ;        	        
		MerchandiseInfoMogo locationMerchandise = merchandiseInfoMogoDao.findOne(locationParams);
		
		if (locationMerchandise==null) {
			throw new DerpException("根据库位货号id查询商品记录不存在,商家id:"+merchantId+"原货号:"+goodsNo) ;
	 
		}		
		// 如果都没有事业部库存  拿最后一个库位货号
		
		
		return locationMerchandise;
	}

	@Override
	public List<InventoryLocationMappingMongo> getGoodsNoMappingList(String topidealCode, String originalGoodsNo)
			throws Exception {
		/*判断是否卓烨商家和标准品牌为美赞臣
		 * 	若满足返回保存库位映射表原货号，库位映射表查询无数据时抛异常
		 *      若不满足返回空 */
		if(!"0000134".equals(topidealCode)
				|| StringUtils.isBlank(originalGoodsNo)) {
			return null ;
		}
		
		Map<String, Object> merchantParams = new HashMap<String, Object>() ;
		merchantParams.put("topidealCode", topidealCode) ;
		
		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
        
        
        Map<String, Object> goodsParams = new HashMap<String, Object>() ;
        goodsParams.put("goodsNo", originalGoodsNo) ;
        goodsParams.put("merchantId", merchantInfo.getMerchantId()) ;
        
        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(goodsParams);
		
        if(merchandiseInfo == null) {
        	throw new DerpException("根据原货号："+ originalGoodsNo +"查询商品不存在") ;
        }
        
        //查询标准条码中的标准品牌
        Map<String, Object> commbarcodeParams = new HashMap<String, Object>() ;
        commbarcodeParams.put("commbarcode", merchandiseInfo.getCommbarcode()) ;
		
        CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(commbarcodeParams);
        
        if(commbarcode == null) {
        	throw new DerpException("根据原货号："+ originalGoodsNo +"标准条码不存在") ;
        }
        
        // 标准品牌id为空
        Long commBrandParentId = commbarcode.getCommBrandParentId();
        if (commBrandParentId==null||commBrandParentId.intValue()==0)return null;
        //根据标准品牌id查询标准品牌
        Map<String, Object> brandParenParams = new HashMap<String, Object>() ;
   	    brandParenParams.put("brandParentId",commBrandParentId) ;
   	    BrandParentMongo brandParent = brandParentMongoDao.findOne(brandParenParams);
        if (brandParent==null) {
        	throw new DerpException("根据原货号："+ originalGoodsNo +"标准品牌不存在") ;
		}        
        String superiorParentBrand = brandParent.getSuperiorParentBrand();
        if (StringUtils.isBlank(superiorParentBrand)) return null;
        superiorParentBrand = superiorParentBrand.trim();//去空格
        if(!superiorParentBrand.equals("美赞臣")) {
        	return null;
        }
        

        
        Map<String, Object> invenMappingParams = new HashMap<String, Object>() ;
        invenMappingParams.put("merchantId", merchantInfo.getMerchantId()) ;
        invenMappingParams.put("originalGoodsNo", originalGoodsNo) ;
        invenMappingParams.put("type", "1") ;
        
        List<InventoryLocationMappingMongo> invernMappingList = findAll(invenMappingParams);
        
        if(invernMappingList == null
        		|| invernMappingList.isEmpty()) {
        	throw new DerpException("根据原货号查询库位映射表记录不存在, 原货号：" + originalGoodsNo) ;
        }
        
        /**按N20， N19排序*/
        invernMappingList = invernMappingList.stream()
        		.sorted(Comparator.comparing(InventoryLocationMappingMongo :: getGoodsNo).reversed())
        		.collect(Collectors.toList()) ;
 		
		return invernMappingList;
	}

	@Override
	public List<InventoryLocationMappingMongo> getAllGoodsNoMappingList(String topidealCode, String originalGoodsNo)
			throws Exception {
		/*判断是否卓烨商家和标准品牌为美赞臣
		 * 	若满足返回保存库位映射表原货号，库位映射表查询无数据时抛异常
		 *      若不满足返回空 */
		if(!"0000134".equals(topidealCode)
				|| StringUtils.isBlank(originalGoodsNo)) {
			return null ;
		}
		
		Map<String, Object> merchantParams = new HashMap<String, Object>() ;
		merchantParams.put("topidealCode", topidealCode) ;
		
		MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(merchantParams);
        
        
        Map<String, Object> goodsParams = new HashMap<String, Object>() ;
        goodsParams.put("goodsNo", originalGoodsNo) ;
        goodsParams.put("merchantId", merchantInfo.getMerchantId()) ;
        
        MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(goodsParams);
		
        if(merchandiseInfo == null) {
        	throw new DerpException("根据原货号："+ originalGoodsNo +"商品不存在") ;
        }
        
        //查询标准条码中的标准品牌
        Map<String, Object> commbarcodeParams = new HashMap<String, Object>() ;
        commbarcodeParams.put("commbarcode", merchandiseInfo.getCommbarcode()) ;
		
        CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(commbarcodeParams);
        
        if(commbarcode == null) {
        	throw new DerpException("根据原货号："+ originalGoodsNo +"标准条码不存在") ;
        }
        
        // 标准品牌id为空
        Long commBrandParentId = commbarcode.getCommBrandParentId();
        if (commBrandParentId==null||commBrandParentId.intValue()==0)return null;
        //根据标准品牌id查询标准品牌
        Map<String, Object> brandParenParams = new HashMap<String, Object>() ;
   	    brandParenParams.put("brandParentId",commBrandParentId) ;
   	    BrandParentMongo brandParent = brandParentMongoDao.findOne(brandParenParams);
        if (brandParent==null) {
        	throw new DerpException("根据原货号："+ originalGoodsNo +"标准品牌不存在") ;
		}        
        String superiorParentBrand = brandParent.getSuperiorParentBrand();
        if (StringUtils.isBlank(superiorParentBrand)) return null;
        superiorParentBrand = superiorParentBrand.trim();//去空格
        if(!superiorParentBrand.equals("美赞臣")) {
        	return null;
        }
        
        Map<String, Object> invenMappingParams = new HashMap<String, Object>() ;
        invenMappingParams.put("merchantId", merchantInfo.getMerchantId()) ;
        invenMappingParams.put("originalGoodsNo", originalGoodsNo) ;
        
        List<InventoryLocationMappingMongo> invernMappingList = findAll(invenMappingParams);
        
        if(invernMappingList == null
        		|| invernMappingList.isEmpty()) {
        	throw new DerpException("根据原货号查询库位映射表记录不存在, 原货号：" + originalGoodsNo) ;
        }
        
        /**按N20， N19排序*/
        invernMappingList = invernMappingList.stream()
        		.sorted(Comparator.comparing(InventoryLocationMappingMongo :: getGoodsNo).reversed())
        		.collect(Collectors.toList()) ;
 		
		return invernMappingList;
	}


}
