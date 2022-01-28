package com.topideal.api.dstp.d04;

import java.util.List;

public class SaleOrderToC {

    private String entOrderNo;//企业订单编号
    private String platformOrderNo;//平台订单编号
    private String orderTime;//订单生成时间
    private String deliveryTime;//订单发货时间 格式yyyy-MM-dd HH:mm:ss
    private String declareTime;//申报时间 yyyy-MM-dd HH:mm:ss
    private String releaseTime;//放行时间格式有误(yyyy-MM-dd HH:mm:ss
    private String warehouse;//发货仓库
    private String logisticsName;//配送服务商
    private String orderType;//是否自运营订单 一件代发的则为1：非自营 为POP的则为2：自运营
    private String agentCode;//代售企业编码
    private String agentName;//代售企业名称
    private String cbepcomName;//电商平台名称
    private String shippingFee;//运费格
    private String taxFee;//税费
    private String totalAmount;//订单总金额
    private String currency;//订单币种
    private String orderStatus;//订单状态
    private List<SaleOrderToCGoods> goodList;
    private SaleOrderToCRecipient recipient;//收货信息

    public String getEntOrderNo() {
        return entOrderNo;
    }

    public void setEntOrderNo(String entOrderNo) {
        this.entOrderNo = entOrderNo;
    }

    public String getPlatformOrderNo() {
        return platformOrderNo;
    }

    public void setPlatformOrderNo(String platformOrderNo) {
        this.platformOrderNo = platformOrderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeclareTime() {
        return declareTime;
    }

    public void setDeclareTime(String declareTime) {
        this.declareTime = declareTime;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCbepcomName() {
        return cbepcomName;
    }

    public void setCbepcomName(String cbepcomName) {
        this.cbepcomName = cbepcomName;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public String getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(String taxFee) {
        this.taxFee = taxFee;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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

    public List<SaleOrderToCGoods> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<SaleOrderToCGoods> goodList) {
        this.goodList = goodList;
    }

    public SaleOrderToCRecipient getRecipient() {
        return recipient;
    }

    public void setRecipient(SaleOrderToCRecipient recipient) {
        this.recipient = recipient;
    }
}
