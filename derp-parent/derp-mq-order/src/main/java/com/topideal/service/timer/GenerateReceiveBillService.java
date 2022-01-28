package com.topideal.service.timer;

import com.topideal.mongo.entity.MerchantInfoMongo;

import java.util.List;

/**
 * @Description: 自动生成应收账单
 * @Author: Chen Yiluan
 * @Date: 2021-06-21 10:09
 **/
public interface GenerateReceiveBillService {

    /**
     * 自动生成应收账单
     * @param json
     */
    List<String> saveReceiveBill(String json,String keys,String topics,String tags) throws Exception;

    /**
     * 自动开票
     * @param json
     */
    void saveReceiveInvoice(String json,String keys,String topics,String tags) throws Exception;

    List<MerchantInfoMongo> getMerchantList(Integer merchantId);
}
