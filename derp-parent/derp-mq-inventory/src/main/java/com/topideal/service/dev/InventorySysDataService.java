package com.topideal.service.dev;

/**
 * 
 * 杨创  2020-08-25
 */
public interface InventorySysDataService {

    /**
     * 库存数据同步到mongdb
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsDataToMongo(String json,String keys,String topics,String tags)throws Exception;
}
