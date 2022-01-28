package com.topideal.service;

import com.topideal.entity.vo.reporting.SettlementBillModel;

/**
 * @Description: 结算账单分账service
 * @Author: Chen Yiluan
 * @Date: 2020-05-25 14:44
 **/
public interface SettlementBillSplitService {

    void saveSplitSettlementBill(String json, String keys, String topics, String tags) throws Exception;

    void updateBillStatus(String code, String expMsg);
}
