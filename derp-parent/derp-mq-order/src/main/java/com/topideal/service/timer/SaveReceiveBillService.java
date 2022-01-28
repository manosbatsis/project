package com.topideal.service.timer;

/**
 * @Description: 生成应收账单
 * @Author: Chen Yiluan
 * @Date: 2020-09-21 15:19
 **/
public interface SaveReceiveBillService {

    /**
     * 生成应收账单MQ Service
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    public boolean saveReceiveBill (String json,String keys,String topics,String tags) throws Exception;

}
