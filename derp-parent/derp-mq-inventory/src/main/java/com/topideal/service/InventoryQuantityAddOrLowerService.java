package com.topideal.service;

/**
 * 库存数量增减
 * @author 联想302
 * baols  2018-06-11
 */
public interface InventoryQuantityAddOrLowerService {

    /**
     * 库存数量增减
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsAddInventoryQuantity(String json,String keys,String topics,String tags)throws Exception;
}
