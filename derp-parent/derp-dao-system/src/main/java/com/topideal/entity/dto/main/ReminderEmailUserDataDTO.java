package com.topideal.entity.dto.main;

import io.swagger.annotations.ApiModelProperty;

public class ReminderEmailUserDataDTO {
    @ApiModelProperty("供应商")
    private String customer;
    @ApiModelProperty("订单号")
    private String orderCode;
    @ApiModelProperty("po号")
    private String poNum;
    @ApiModelProperty("po号")
    private String status;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPoNum() {
        return poNum;
    }

    public void setPoNum(String poNum) {
        this.poNum = poNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
