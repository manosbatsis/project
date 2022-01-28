package com.topideal.service;


public interface SendFinanceGoodsService {

	/**
	 * 向卓金融推送商品档案
	 * @param json
	 */
	void sendFinanceGoods(String json,String keys, String topics, String tags) throws Exception;

}
