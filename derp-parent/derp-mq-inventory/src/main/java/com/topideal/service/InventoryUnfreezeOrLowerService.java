package com.topideal.service;

/**
 * 库存解冻和扣减接口（主要用于事物一致性）
 * @author 联想302
 * baols  2018-06-11
 */
public interface InventoryUnfreezeOrLowerService {

    /**
     * 库存解冻和扣减接口（主要用于事物一致性）
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsInventoryUnfreezeOrLower(String json,String keys,String topics,String tags)throws Exception;
}
