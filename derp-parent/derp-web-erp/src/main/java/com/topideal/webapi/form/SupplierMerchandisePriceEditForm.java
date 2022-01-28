package com.topideal.webapi.form;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/12/22 15:17
 * @Description:
 */
public class SupplierMerchandisePriceEditForm implements Serializable {

    @ApiModelProperty(value="token",required = true)
    private String token;
    @ApiModelProperty(value="id",required = true)
    private Long id;

    @ApiModelProperty(value="价格生效时间")
    private String effectiveDateStr;

    @ApiModelProperty(value="价格生效时间")
    private String expiryDateStr;

    @ApiModelProperty(value="币种")
    private String currency;

    @ApiModelProperty(value="销售价格")
    private BigDecimal supplyPrice;

    @ApiModelProperty(value="备注")
    private String remark;

    @ApiModelProperty(value="编码")
    private String code;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getSupplyPrice() {
        return supplyPrice;
    }

    public void setSupplyPrice(BigDecimal supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
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
