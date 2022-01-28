package com.topideal.service;


public interface SendSapienceGoodsService {

	/**
	 * 向卓普信推送商品档案
	 * @param json
	 */
	void sendSapienceGoods(String json,String keys, String topics, String tags) throws Exception;

}
