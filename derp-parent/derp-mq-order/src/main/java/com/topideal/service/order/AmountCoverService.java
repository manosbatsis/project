package com.topideal.service.order;

/**
 业务单据回滚
 */
public interface AmountCoverService {

    /**
     * 电商订单金额覆盖
     */
    public boolean synsAmountCover(String json, String keys, String topics, String tags)throws Exception;
}
