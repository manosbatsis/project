package com.topideal.service;

/**
 *  月结账单   业务
 * Created by baols on 2018/6/11
 */
public interface InventoryMonthlyBillService {

    /**
     *   月结账单
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsInventoryMonthlyBill(String json,String keys,String topics,String tags)throws Exception;





}
