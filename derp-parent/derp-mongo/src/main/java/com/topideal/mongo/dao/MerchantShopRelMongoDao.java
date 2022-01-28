package com.topideal.mongo.dao;

import java.util.List;
import java.util.Map;

import com.topideal.mongo.entity.MerchantShopRelMongo;
import org.springframework.data.domain.Sort;

/**
 * 商家店铺关联表 mongoDao
 * @author lian_
 *
 */
public interface MerchantShopRelMongoDao extends MongoDao<MerchantShopRelMongo>{

	public void insertJson(String json);

	public List getRegexList(Map<String, String> likeMap);

	public List getListByRegexOrOther(Map<String, String> likeMap, Map<String, Object> params, Map<String, Sort.Direction> sortMap);

}
