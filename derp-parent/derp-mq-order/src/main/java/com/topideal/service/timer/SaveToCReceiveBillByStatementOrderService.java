package com.topideal.service.timer;

/**
 * @Description: 根据平台结算单生成Toc应收账单service
 * @Author: Chen Yiluan
 * @Date: 2021-03-09 14:08
 **/
public interface SaveToCReceiveBillByStatementOrderService {

    /**
     * 生成ToC应收账单MQ Service
     * @param json
     * @param keys
     * @param topics
     * @param tags
     * @return
     * @throws Exception
     */
    public boolean saveToCReceiveBill (String json,String keys,String topics,String tags) throws Exception;

}
