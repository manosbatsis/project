package com.topideal.service;

import com.topideal.mongo.tools.PageMongo;

public interface AutoLogService {

	PageMongo searchLog(PageMongo pageMongo, String modelCode, String point, String keyword, String expType, String type);

	void batchResetSend(String ids) throws Exception;

}
