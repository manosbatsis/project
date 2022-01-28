package com.topideal.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/17 11:10
 * @Description: 固定成本价盘
 */
public class FixedCostPriceForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "状态(0待审核, 1已审核)")
    private String status;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
     * 事业部ID
     */
    @ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
     * 标准品牌ID
     */
    @ApiModelProperty(value = "标准品牌ID")
    private Long brandParentId;
    /**
     * 标准品牌名称
     */
    @ApiModelProperty(value = "标准品牌名称")
    private String brandParentName;

    @ApiModelProperty(value = "商品条形码")
    private String barcode;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "固定成本价")
    private BigDecimal fixedCost;

    @ApiModelProperty(value = "生效日期")
    private String effectiveDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(Long brandParentId) {
        this.brandParentId = brandParentId;
    }

    public String getBrandParentName() {
        return brandParentName;
    }

    public void setBrandParentName(String brandParentName) {
        this.brandParentName = brandParentName;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getFixedCost() {
        return fixedCost;
    }

    public void setFixedCost(BigDecimal fixedCost) {
        this.fixedCost = fixedCost;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
