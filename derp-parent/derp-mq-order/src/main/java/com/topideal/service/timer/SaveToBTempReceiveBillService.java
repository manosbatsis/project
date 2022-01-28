package com.topideal.service.timer;

/**
 * @Description: 生成tob暂估核销数据
 * @Author: Chen Yiluan
 * @Date: 2021/04/23 18:03
 **/
public interface SaveToBTempReceiveBillService {

    /**
     * 生成ToB暂估核销账单
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    boolean saveToBTempReceiveBill(String json,String keys,String topics,String tags) throws Exception;


    /**
     * 应收账单核销ToB暂估
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    boolean updateVerifyToBTempBill(String json,String keys,String topics,String tags) throws Exception;



}
