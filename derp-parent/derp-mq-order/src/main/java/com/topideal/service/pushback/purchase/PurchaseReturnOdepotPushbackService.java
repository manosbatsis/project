package com.topideal.service.pushback.purchase;

public interface PurchaseReturnOdepotPushbackService {

	boolean modifyStatus(String json, String keys, String topics, String tags) throws Exception;

}
