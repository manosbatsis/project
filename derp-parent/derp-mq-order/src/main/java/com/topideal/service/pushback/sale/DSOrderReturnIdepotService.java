package com.topideal.service.pushback.sale;


/**
 *  电商订单退货-成功回推 
 */
public interface DSOrderReturnIdepotService {


	void updateDSOrderReturnIdepotPushBackInfo(String json, String keys, String topics, String tags) throws Exception;

}
