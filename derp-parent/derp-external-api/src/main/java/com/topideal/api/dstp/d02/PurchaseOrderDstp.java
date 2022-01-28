package com.topideal.api.dstp.d02;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseOrderDstp {

    private String customerCode;//客户编码
    private String entOrderNo;//企业订单号
    private String orderTime;//订单时间 格式yyyy-MM-dd HH:mm:ss
    private String contractNo;//合同号
    private String poOrderNo;//po单号
    private String financingOrderNo;//融资单号
    private Integer purchaseQty;//采购总数量
    private String purchaseAmount;//采购总金额
    private String currency;//币种
    private String payTime;//付款时间格式 yyyy-MM-dd HH:mm:ss
    private String wmsOrderNo;//入库单号
    private String inboundTime;//入库完成时间格式yyyy-MM-dd HH:mm:ss
    /**采购订单状态
     *       10-执行中
     * 已入库:20-已完成
       已删除:30-已取消
       已红冲:30-已取消
     */
    private String orderStatus;//订单状态
    private String linKid;//链路id
    private List<PurchaseGoods> goodsList;//订单商品

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

    public String getFinancingOrderNo() {
        return financingOrderNo;
    }

    public void setFinancingOrderNo(String financingOrderNo) {
        this.financingOrderNo = financingOrderNo;
    }

    public Integer getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(Integer purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public String getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(String purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getWmsOrderNo() {
        return wmsOrderNo;
    }

    public void setWmsOrderNo(String wmsOrderNo) {
        this.wmsOrderNo = wmsOrderNo;
    }

    public String getInboundTime() {
        return inboundTime;
    }

    public void setInboundTime(String inboundTime) {
        this.inboundTime = inboundTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getLinKid() {
        return linKid;
    }

    public void setLinKid(String linKid) {
        this.linKid = linKid;
    }

    public List<PurchaseGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<PurchaseGoods> goodsList) {
        this.goodsList = goodsList;
    }
}
