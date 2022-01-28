package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * @Author: Wilson Lau
 * @Date: 2022/1/17 11:11
 * @Description: 固定成本价盘
 */
public class FixedCostPriceDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "ids, 逗号分割")
    private String ids;

    @ApiModelProperty(value = "状态(0待审核, 1已审核)")
    private String status;
    private String statusLabel;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "事业部ID")
    private Long buId;

    @ApiModelProperty(value = "事业部编码")
    private String buCode;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "标准品牌ID")
    private Long brandParentId;

    @ApiModelProperty(value = "标准品牌名称")
    private String brandParentName;

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品条形码")
    private String barcode;

    @ApiModelProperty(value = "固定成本价")
    private BigDecimal fixedCost;
    private String fixedCostLabel;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "生效日期")
    private String effectiveDate;

    @ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;

    @ApiModelProperty(value = "审核人ID")
    private Long auditer;

    @ApiModelProperty(value = "审核人名称")
    private String auditName;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_SYS.getLabelByKey(DERP_SYS.fixedCostPrice_statusList, status);
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
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
        if(fixedCost != null) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00000000#");
            String strVal = decimalFormat.format(fixedCost);
            this.fixedCostLabel = strVal;
        }
    }

    public String getFixedCostLabel() {
        return fixedCostLabel;
    }

    public void setFixedCostLabel(String fixedCostLabel) {
        this.fixedCostLabel = fixedCostLabel;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
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

    public Timestamp getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Timestamp auditDate) {
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

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
