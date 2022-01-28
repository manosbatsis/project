package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class TocSettlementReceiveBillSaveForm  implements Serializable {

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "结算日期", required = false)
    private String settlementDateStr;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "运营类型 001:POP; 002:一件代发", required = false)
    private String shopTypeCode;

    @ApiModelProperty(value = "电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790", required = false)
    private String storePlatformCode;

    @ApiModelProperty(value = "店铺名称", required = false)
    private String shopName;

    @ApiModelProperty(value = "店铺编码", required = false)
    private String shopCode;

    @ApiModelProperty(value = "结算币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String oriCurrency;

    /**
     * 结算币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
     */
    @ApiModelProperty(value = "结算币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String settlementCurrency;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "外部结算单号", required = false)
    private String externalCode;

    @ApiModelProperty(value = "结算编码", required = false)
    private String code;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getShopTypeCode() {
        return shopTypeCode;
    }

    public void setShopTypeCode(String shopTypeCode) {
        this.shopTypeCode = shopTypeCode;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getSettlementDateStr() {
        return settlementDateStr;
    }

    public void setSettlementDateStr(String settlementDateStr) {
        this.settlementDateStr = settlementDateStr;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getOriCurrency() {
        return oriCurrency;
    }

    public void setOriCurrency(String oriCurrency) {
        this.oriCurrency = oriCurrency;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
