package com.topideal.entity.dto.common;

import io.swagger.annotations.ApiModelProperty;

public class ReminderEmailUserDataDTO {
    @ApiModelProperty("供应商")
    private String customer;
    @ApiModelProperty("订单号")
    private String orderCode;
    @ApiModelProperty("po号")
    private String poNum;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("发票号码")
    private String invoiceNo;
    @ApiModelProperty("事业部")
    private String buName;
    @ApiModelProperty("日期")
    private String date;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("金额")
    private String amount;

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

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
