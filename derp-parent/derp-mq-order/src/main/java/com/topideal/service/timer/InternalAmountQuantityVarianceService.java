package com.topideal.service.timer;

/**
 * 内部交易金额数量差异提醒
 * @author huangrenya
 **/
public interface InternalAmountQuantityVarianceService {

    void getInternalAmountQuantityOrder(String json, String keys, String topics, String tags) throws Exception;

}
