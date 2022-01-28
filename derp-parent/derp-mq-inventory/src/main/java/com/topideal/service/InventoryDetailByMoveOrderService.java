package com.topideal.service;

/**
 * 移库单审核生成商品收发明细
 **/
public interface InventoryDetailByMoveOrderService {

    /**
     * 移库单审核生成商品收发明细
     */
    public boolean saveInventoryDetail(String json,String keys,String topics,String tags)throws Exception;
}
