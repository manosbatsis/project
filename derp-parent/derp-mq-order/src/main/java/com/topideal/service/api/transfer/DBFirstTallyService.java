package com.topideal.service.api.transfer;

/**
 * 调拨-初理货  业务
 * Created by yuciafu on 2018/5/24.
 */
public interface DBFirstTallyService {

    //初存初理货信息
    boolean saveTallyInfo(String json,String keys,String topics,String tags)throws Exception;


}
