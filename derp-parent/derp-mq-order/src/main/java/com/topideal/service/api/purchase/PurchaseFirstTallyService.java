package com.topideal.service.api.purchase;

/**
 * 初理货  业务
 * @author zhanghx
 * 2018/7/16
 */
public interface PurchaseFirstTallyService {

    //初存初理货信息
    boolean saveTallyInfo(String json,String keys,String topics,String tags) throws Exception;


}
