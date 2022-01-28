package com.topideal.service;

/**
 *同步订单到dstp
 *
 */
public interface DSTPSynOrderService {

	void synOrderDSTP(String json, String keys, String topics, String tags) throws Exception;

}
