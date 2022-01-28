package com.topideal.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 成本单价新增表体参数
 */
@ApiModel
public class SettlementPriceItemDTO implements Serializable {

    @ApiModelProperty(value = "商品id", required = false)
    private Long goodsId;

    @ApiModelProperty(value = "商品货号", required = false)
    private String goodsNo;

    @ApiModelProperty(value = "条形码", required = false)
    private String barcode;

    @ApiModelProperty(value = "商品名称", required = false)
    private String goodsName;

    @ApiModelProperty(value = "货品id", required = false)
    private Long productId;

    @ApiModelProperty(value = "品牌id", required = false)
    private Long brandId;

    @ApiModelProperty(value = "品牌名称", required = false)
    private String brandName;

    @ApiModelProperty(value = "规格型号", required = false)
    private String goodsSpec;

    @ApiModelProperty(value = "结算币种  01人民币,02日元,03 澳元 ,04美元,05港币,06 欧元,07英镑", required = false)
    private String currency;

    @ApiModelProperty(value = "价格", required = false)
    private BigDecimal price;

    @ApiModelProperty(value = "生效日期", required = false)
    private String effectiveDate;

    @ApiModelProperty(value = "用于限制页面可选日期", required = false)
    private String startDate;//用于限制页面可选日期

    @ApiModelProperty(value = "是否组合品  1 是 0 否", required = false)
    private String isGroup;

    @ApiModelProperty(value = "计量单位id", required = false)
    private Long unitId;

    @ApiModelProperty(value = "计量单位名称", required = false)
    private String unitName;

    @ApiModelProperty(value = "调价原因",required = false)
    private String adjustPriceResult;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getAdjustPriceResult() {
        return adjustPriceResult;
    }

    public void setAdjustPriceResult(String adjustPriceResult) {
        this.adjustPriceResult = adjustPriceResult;
    }
}
