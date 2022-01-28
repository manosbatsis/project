package com.topideal.api.dstp.d03;

import java.util.List;

public class SaleOrderToBDstp {

    private String customerCode;//客户编号
    private String entOrderNo;//企业订单编号
    private String orderTime;//订单时间格式(yyyy-MM-dd HH:mm:ss)"
    private String contractNo;//合同号
    private String poOrderNo;//PO单号
    private String wmsOrderNo;//出库单号
    private String outboundTime;//出库时间格式(yyyy-MM-dd HH:mm:ss
    private Integer salesQty;//销售总数量
    private String salesAmount;//销售总金额
    private String currency;//订单币种
    private String orderStatus;//订单状态
    private List<SaleOrderToBGoods> goodsList;//订单商品

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getEntOrderNo() {
        return entOrderNo;
    }

    public void setEntOrderNo(String entOrderNo) {
        this.entOrderNo = entOrderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getPoOrderNo() {
        return poOrderNo;
    }

    public void setPoOrderNo(String poOrderNo) {
        this.poOrderNo = poOrderNo;
    }

    public String getWmsOrderNo() {
        return wmsOrderNo;
    }

    public void setWmsOrderNo(String wmsOrderNo) {
        this.wmsOrderNo = wmsOrderNo;
    }

    public String getOutboundTime() {
        return outboundTime;
    }

    public void setOutboundTime(String outboundTime) {
        this.outboundTime = outboundTime;
    }

    public Integer getSalesQty() {
        return salesQty;
    }

    public void setSalesQty(Integer salesQty) {
        this.salesQty = salesQty;
    }

    public String getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(String salesAmount) {
        this.salesAmount = salesAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<SaleOrderToBGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<SaleOrderToBGoods> goodsList) {
        this.goodsList = goodsList;
    }
}
