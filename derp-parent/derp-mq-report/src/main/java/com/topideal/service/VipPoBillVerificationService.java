package com.topideal.service;


public interface VipPoBillVerificationService {

	void saveSummaryReport(String json, String keys, String topics, String tags) throws Exception;

	void synsOrderData(String json, String keys, String topics, String tags) throws Exception;

}
