package com.topideal.service.pushback.purchase;

public interface YwmsPurchaseWarehousePushbackService {

	void modifyStatus(String json, String keys, String topics, String tags) throws Exception;

}
