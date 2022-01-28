package com.topideal.service;

public interface SaveCostPriceDifferenceService {

    /**
     * 生成成本单价差异表
     * @param json
     */
    void saveCostPriceDifference(String json,String keys, String topics, String tags) throws Exception;

}
