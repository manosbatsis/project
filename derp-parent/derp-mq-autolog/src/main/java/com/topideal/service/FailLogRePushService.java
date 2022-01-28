package com.topideal.service;


public interface FailLogRePushService {

	void rePushFailLog(String json, String tags) throws Exception;

}
