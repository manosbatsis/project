package com.topideal.service.pushback.sale;

public interface YWmsDSOrderOutDepotPushbackService {

	void modifyStatus(String json, String keys, String topics, String tags) throws Exception;

}
