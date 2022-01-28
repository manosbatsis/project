package com.topideal.service;

/**
 * 事业部库存接口
 * 杨创  2020-04-13
 */
public interface BuInventoryService {

    /**
     * 事业部库存接口
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsBuInventory(String json,String keys,String topics,String tags)throws Exception;
}
