package com.topideal.entity.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 经营管理-部门库存统计
 */
public class ManageDepartmentInventoryDTO {

    @ApiModelProperty(value = "序号", required = false)
    private Long No;

    @ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;

    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

    @ApiModelProperty(value = "公司id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "公司名称", required = false)
    private String merchantName;

    /**
     * 效期区间1:0＜X≤1/10 ; 2: 1/10＜X≤1/5 ; 3: 1/5＜X≤1/3 ; 4: 1/3＜X≤1/2 ; 5: 1/2＜X≤2/3 ; 6: 2/3以上
     */
    @ApiModelProperty(value = "效期区间", required = false)
    private String effectiveInterval;

    @ApiModelProperty(value = "效期区间Label", required = false)
    private String effectiveIntervalLabel;

    @ApiModelProperty(value = "标准品牌", required = false)
    private String brandParent;

    @ApiModelProperty(value = "标准品牌ID" , required = false)
    private Long brandParentId;

    @ApiModelProperty(value = "部门ID" , required = false)
    private Long departmentId;

    @ApiModelProperty(value = "部门名称 " , required = false)
    private String departmentName;

    @ApiModelProperty(value = "母品牌ID " , required = false)
    private Long parentBrandId;

    @ApiModelProperty(value = "母品牌名称" , required = false)
    private String parentBrandName;

    @ApiModelProperty(value = "仓库名称" , required = false)
    private String depotName;

    @ApiModelProperty(value = "仓库ID" , required = false)
    private String depotId;

    @ApiModelProperty(value = "库存数量" , required = false)
    private Long num;

    @ApiModelProperty(value = "库存金额" , required = false)
    private BigDecimal inventoryAmount;

    @ApiModelProperty(value = "金额占比" , required = false)
    private BigDecimal amountRate;

    @ApiModelProperty(value = "金额占比Label" , required = false)
    private String amountRateLabel;

    private Boolean departmentSumFlag;

    private String departmentSumTitle;

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

    public String getBrandParent() {
        return brandParent;
    }

    public void setBrandParent(String brandParent) {
        this.brandParent = brandParent;
    }

    public Long getBrandParentId() {
        return brandParentId;
    }

    public void setBrandParentId(Long brandParentId) {
        this.brandParentId = brandParentId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getDepotId() {
        return depotId;
    }

    public void setDepotId(String depotId) {
        this.depotId = depotId;
    }

    public Boolean getDepartmentSumFlag() {
        return departmentSumFlag;
    }

    public void setDepartmentSumFlag(Boolean departmentSumFlag) {
        this.departmentSumFlag = departmentSumFlag;
    }

    public String getDepartmentSumTitle() {
        return departmentSumTitle;
    }

    public void setDepartmentSumTitle(String departmentSumTitle) {
        this.departmentSumTitle = departmentSumTitle;
    }

    public Long getNo() {
        return No;
    }

    public void setNo(Long no) {
        No = no;
    }

    public String getEffectiveInterval() {
        return effectiveInterval;
    }

    public void setEffectiveInterval(String effectiveInterval) {
        this.effectiveInterval = effectiveInterval;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public BigDecimal getInventoryAmount() {
        return inventoryAmount;
    }

    public void setInventoryAmount(BigDecimal inventoryAmount) {
        this.inventoryAmount = inventoryAmount;
    }

    public BigDecimal getAmountRate() {
        return amountRate;
    }

    public void setAmountRate(BigDecimal amountRate) {
        this.amountRate = amountRate;
    }

    public String getAmountRateLabel() {
        return amountRateLabel;
    }

    public void setAmountRateLabel(String amountRateLabel) {
        this.amountRateLabel = amountRateLabel;
    }

    public String getEffectiveIntervalLabel() {
        return effectiveIntervalLabel;
    }

    public void setEffectiveIntervalLabel(String effectiveIntervalLabel) {
        this.effectiveIntervalLabel = effectiveIntervalLabel;
    }
}
