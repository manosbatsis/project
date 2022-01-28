package com.topideal.mongo.entity;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;

public class FixedCostPriceMongo extends PageModel implements Serializable{

    /**
    * id
    */
    private Long fixedCostPriceId;
    /**
     * 状态(0待审核, 1已审核)
     */
    private String status;
    /**
     * 公司ID
     */
    private Long merchantId;
    /**
     * 公司名称
     */
    private String merchantName;
    /**
     * 事业部ID
     */
    private Long buId;
    /**
     * 事业部编码
     */
    private String buCode;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 标准品牌ID
     */
    private Long brandParentId;
    /**
     * 标准品牌名称
     */
    private String brandParentName;
    /**
     * 商品条形码
     */
    private String barcode;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 固定成本价
     */
    private BigDecimal fixedCost;

    /**
     * 币种
     */
    private String currency;
    /**
     * 生效日期
     */
    private String effectiveDate;

    /**
     * 审核时间
     */
    private String auditDate;
    /**
     * 审核人ID
     */
    private Long auditer;
    /**
     * 审核人名称
     */
    private String auditName;

    public Long getFixedCostPriceId() {
        return fixedCostPriceId;
    }

    public void setFixedCostPriceId(Long fixedCostPriceId) {
        this.fixedCostPriceId = fixedCostPriceId;
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

    public String getBuCode() {
        return buCode;
    }

    public void setBuCode(String buCode) {
        this.buCode = buCode;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public Long getAuditer() {
        return auditer;
    }

    public void setAuditer(Long auditer) {
        this.auditer = auditer;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }
}
