package com.topideal.service;

/**
 * 冻结与释放库存
 * Created by baols on 2018/6/11
 */
public interface InventoryFreezeAddOrLowerService {

    /**
     *  冻结与释放库存
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsAddOrLowerInventoryfreeze(String json,String keys,String topics,String tags)throws Exception;





}
