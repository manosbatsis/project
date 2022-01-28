package com.topideal.service.order;

/**
 迁移数据到历史表
 */
public interface MoveDataToHistoryService {

    /**迁移数据到历史表
     */
    public boolean synsMoveToHistory(String json, String keys, String topics, String tags)throws Exception;
}
