package com.topideal.service;

/**
 迁移数据到历史表
 */
public interface InventoryDetailsMoveToHistoryService {

    /**迁移数据到历史表
     */
    public boolean synsMoveToHistory(String json, String keys, String topics, String tags)throws Exception;
}
