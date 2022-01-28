package com.topideal.service;

import java.util.Map;

public interface MonthlyAccountAutomaticVeriService {

	void saveAutoVeriReport(String json, String keys, String topics, String tags) throws Exception;

	void delByTaskType(Map<String, Object> delTaskMap) ;

}
