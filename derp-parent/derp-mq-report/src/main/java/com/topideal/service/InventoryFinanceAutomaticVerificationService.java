package com.topideal.service;

import java.util.Map;

/**
 * @Description: 业财自核表service
 * @Author: Chen Yiluan
 * @Date: 2020-05-18 15:49
 **/
public interface InventoryFinanceAutomaticVerificationService {

    /**
     * @Description 生成业财自核表数据
     * @Date 2020/5/18
     */
    void saveAutoVeriReport(String json, String keys, String topics, String tags) throws Exception;

    void delByTaskType(Map<String, Object> params) ;

}
