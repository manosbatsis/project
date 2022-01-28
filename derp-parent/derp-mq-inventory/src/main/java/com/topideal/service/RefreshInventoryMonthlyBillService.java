package com.topideal.service;

import com.topideal.entity.vo.MonthlyAccountModel;

/**
 *  刷新月结账单   业务
 * Created by baols on 2018/6/11
 */
public interface RefreshInventoryMonthlyBillService {

    /**
     *   刷新月结账单
     * @param json
     * @return
     * @throws Exception
     */
    public boolean synsRefreshMonthlyBill(String json,String keys,String topics,String tags,MonthlyAccountModel monthlyAccountModel)throws Exception;





}
