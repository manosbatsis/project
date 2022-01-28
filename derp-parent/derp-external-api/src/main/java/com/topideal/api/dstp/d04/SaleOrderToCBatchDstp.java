package com.topideal.api.dstp.d04;

import java.util.List;

public class SaleOrderToCBatchDstp {

    private String customerCode;//客户编号
    private List<SaleOrderToC> orderList;//订单

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public List<SaleOrderToC> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<SaleOrderToC> orderList) {
        this.orderList = orderList;
    }
}
