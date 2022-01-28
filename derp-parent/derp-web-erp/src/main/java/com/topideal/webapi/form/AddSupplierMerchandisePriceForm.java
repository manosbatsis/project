package com.topideal.webapi.form;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/22 15:17
 * @Description:
 */
public class AddSupplierMerchandisePriceForm implements Serializable {

    /**
     * token
     */
    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 编码(暂时用于关联附件)
     */
    @ApiModelProperty(value = "编码(暂时用于关联附件)")
    private String code;

    /**
     * 公司ID
     */
    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    /**
     * 事业部id
     */
    @ApiModelProperty(value = "事业部id")
    private Long buId;

    /**
     * 供应商id
     */
    @ApiModelProperty(value = "供应商id")
    private Long customerId;

    /**
     * 条形码
     */
    @ApiModelProperty(value = "条形码")
    private String barcode;

    /**
     * 币种
     */
    @ApiModelProperty(value = "币种")
    private String currency;

    /**
     * 供货价
     */
    @ApiModelProperty(value = "供货价")
    private BigDecimal supplyPrice;

    /**
     * 报价生效时间
     */
    @ApiModelProperty(value = "报价生效时间")
    private String effectiveDateStr;

    /**
     * 报价失效时间
     */
    @ApiModelProperty(value = "报价失效时间")
    private String expiryDateStr;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 库位类型ID
     */
    @ApiModelProperty(value = "库位类型ID")
    private Long stockLocationTypeId;
    /**
     * 库位类型名称
     */
    @ApiModelProperty(value = "库位类型名称")
    private String stockLocationTypeName;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }


    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public String getEffectiveDateStr() {
        return effectiveDateStr;
    }

    public void setEffectiveDateStr(String effectiveDateStr) {
        this.effectiveDateStr = effectiveDateStr;
    }

    public String getExpiryDateStr() {
        return expiryDateStr;
    }

    public void setExpiryDateStr(String expiryDateStr) {
        this.expiryDateStr = expiryDateStr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}
