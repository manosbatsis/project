package com.topideal.service;

/**
 *同步商品到dstp
 *
 */
public interface DSTPSynGoodsService {

	void synGoosDSTP(String json, String keys, String topics, String tags) throws Exception;

}
