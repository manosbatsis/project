package com.topideal.service.order;

/**
 业务单据回滚
 */
public interface OrderRollbackDataService {

    /** 业务单据回滚
     */
    public boolean synsOrderRollbackData(String json, String keys, String topics, String tags)throws Exception;
}
