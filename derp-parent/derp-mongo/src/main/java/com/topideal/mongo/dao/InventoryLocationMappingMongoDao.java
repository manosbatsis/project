package com.topideal.mongo.dao;

import java.util.List;

import com.topideal.mongo.entity.InventoryLocationMappingMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;

/**
 * 库位映射表Dao
 *
 */
public interface InventoryLocationMappingMongoDao extends MongoDao<InventoryLocationMappingMongo>{

	public void insertJson(String json);

	/**
	 * 	判断是否卓烨商家和标准品牌为美赞臣
	 * 	若满足返回保存库位映射表对象，库位映射表查询无数据时抛异常
	 *      若不满足，返回null
	 * @param topidealCode 商家卓志编码
	 * @param goodsNo      查询货号，对应库位映射表--库位货号
	 * @return inventoryLocationMappingMongo 
	 */
	public InventoryLocationMappingMongo getOriGoodsNoMappingModel(String topidealCode, String goodsNo) throws Exception;
	
	/**
	 * 	根据原货号查询库位货号列表按常规库位货号规则排序
	 * 	判断是否卓烨商家和标准品牌为美赞臣
	 * 	若满足返回库位货号列表，库位映射表查询无数据时抛异常
	 * @param topidealCode 商家卓志编码
	 * @param originalGoodsNo      查询货号，对应库位映射表--原货号
	 * @return inventoryLocationMappingMongo 
	 */
	public List<InventoryLocationMappingMongo> getGoodsNoMappingList(String topidealCode, String originalGoodsNo) throws Exception;
	
	/**
	 * 	根据原货号查询库位货号列表按所有库位货号规则排序
	 * 	判断是否卓烨商家和标准品牌为美赞臣
	 * 	若满足返回库位货号列表，库位映射表查询无数据时抛异常
	 * @param topidealCode 商家卓志编码
	 * @param originalGoodsNo      查询货号，对应库位映射表--原货号
	 * @return inventoryLocationMappingMongo 
	 */
	public List<InventoryLocationMappingMongo> getAllGoodsNoMappingList(String topidealCode, String originalGoodsNo) throws Exception;
	
	/**
	 * 通过原商品获取库存货商品(e仓/跨境宝b2c)
	 * @param topidealCode
	 * @param goodsNo
	 * @return
	 * @throws Exception
	 */
	public MerchandiseInfoMogo getLocationMerchandiseInfo(Long merchantId, Long depotId, String goodsNo) throws Exception;

}
