package com.topideal.entity.dto.api;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/2 19:04
 * @Description:
 */
public class TocSettleBillDataDTO {

    private String orderNo;

    private String expensesItem;

    private Double settleAmount;

    private Double settleAmountRMB;

    private String exchangeRate;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getExpensesItem() {
        return expensesItem;
    }

    public void setExpensesItem(String expensesItem) {
        this.expensesItem = expensesItem;
    }

    public Double getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(Double settleAmount) {
        this.settleAmount = settleAmount;
    }

    public Double getSettleAmountRMB() {
        return settleAmountRMB;
    }

    public void setSettleAmountRMB(Double settleAmountRMB) {
        this.settleAmountRMB = settleAmountRMB;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
