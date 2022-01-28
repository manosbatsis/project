package com.topideal.webapi.form;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/22 18:18
 * @Description:
 */
public class AddCustomerSalePriceForm implements Serializable {

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
     * 销售价格（RMB）
     */
    @ApiModelProperty(value = "")
    private BigDecimal salePrice;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
}
