package com.topideal.service;

/**
 * 库存单据红冲
 * @author 联想302
 * baols  2018-06-11
 */
public interface InventoryWriteOffService {

    /**
     * 库存单据红冲
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsInventoryWriteOff(String json,String keys,String topics,String tags)throws Exception;
}
