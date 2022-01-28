package com.topideal.service.api.common;
/**
 * report修改商品信息
 */
public interface UpdateGoodsInfoService {
	/**
	 * 修改商品信息	
	 * @param json
	 * @param keys
	 * @param topics
	 * @param tags
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsInfo(String json, String keys, String topics, String tags)throws Exception;
}
