package com.topideal.service.pushback.purchase;

public interface DeclareDeliveryPushbackService {

	void modifyStatus(String json, String keys, String topics, String tags) throws Exception;

}
