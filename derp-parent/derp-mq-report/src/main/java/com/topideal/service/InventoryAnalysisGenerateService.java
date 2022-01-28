package com.topideal.service;

import java.sql.SQLException;

/**
 * @Description: (销售洞察)库存量分析数据生成
 * @Author: Chen Yiluan
 * @Date: 2020-12-18 14:33
 **/
public interface InventoryAnalysisGenerateService {

    public void saveInventoryAnalysis(String json, String keys, String topics, String tags) throws Exception;
}
