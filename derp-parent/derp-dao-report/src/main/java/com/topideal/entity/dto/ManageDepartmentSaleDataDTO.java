package com.topideal.entity.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 经营管理-部门销售金额统计数据
 */
public class ManageDepartmentSaleDataDTO {

    @ApiModelProperty(value = "序号", required = false)
    private Long No;

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;

    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

    @ApiModelProperty(value = "公司id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "公司名称", required = false)
    private String merchantName;

    @ApiModelProperty(value = "客户名称", required = false)
    private String customerName;

    @ApiModelProperty(value = "客户ID", required = false)
    private Long customerId;

    @ApiModelProperty(value = "月份", required = false)
    private String month;

    @ApiModelProperty(value = "标准品牌", required = false)
    private String brandParent;

    @ApiModelProperty(value = "标准品牌ID" , required = false)
    private Long brandParentId;

    @ApiModelProperty(value = "渠道类型 To B,To C", required = false)
    private String channelType;

    @ApiModelProperty(value = "部门ID" , required = false)
    private Long departmentId;

    @ApiModelProperty(value = "部门名称 " , required = false)
    private String departmentName;

    @ApiModelProperty(value = "母品牌ID " , required = false)
    private Long parentBrandId;

    @ApiModelProperty(value = "母品牌编码 " , required = false)
    private String parentBrandCode;

    @ApiModelProperty(value = "母品牌名称" , required = false)
    private String parentBrandName;

    @ApiModelProperty(value = "出仓名称" , required = false)
    private String outDepotName;

    @ApiModelProperty(value = "出仓ID" , required = false)
    private String outDepotId;

    @ApiModelProperty(value = "港币汇率" , required = false)
    private Double hkRate;

    @ApiModelProperty(value = "平台编码" , required = false)
    private String storePlatformCode;

    @ApiModelProperty(value = "平台名称" , required = false)
    private String storePlatformName;

    @ApiModelProperty(value = "港币金额" , required = false)
    private BigDecimal hkAmount;

    private BigDecimal sumHKAmount;

    // 目标月金额
    private BigDecimal monthAmount1;

    // 上个月金额
    private BigDecimal monthAmount2;

    // 上上个月金额
    private BigDecimal monthAmount3;

    private Boolean departmentSumFlag;

    private String departmentSumTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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

    public String getParentBrandCode() {
        return parentBrandCode;
    }

    public void setParentBrandCode(String parentBrandCode) {
        this.parentBrandCode = parentBrandCode;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public Double getHkRate() {
        return hkRate;
    }

    public void setHkRate(Double hkRate) {
        this.hkRate = hkRate;
    }

    public BigDecimal getHkAmount() {
        return hkAmount;
    }

    public void setHkAmount(BigDecimal hkAmount) {
        this.hkAmount = hkAmount;
    }

    public BigDecimal getMonthAmount1() {
        return monthAmount1;
    }

    public void setMonthAmount1(BigDecimal monthAmount1) {
        this.monthAmount1 = monthAmount1;
    }

    public BigDecimal getMonthAmount2() {
        return monthAmount2;
    }

    public void setMonthAmount2(BigDecimal monthAmount2) {
        this.monthAmount2 = monthAmount2;
    }

    public BigDecimal getMonthAmount3() {
        return monthAmount3;
    }

    public void setMonthAmount3(BigDecimal monthAmount3) {
        this.monthAmount3 = monthAmount3;
    }

    public BigDecimal getSumHKAmount() {
        return sumHKAmount;
    }

    public void setSumHKAmount(BigDecimal sumHKAmount) {
        this.sumHKAmount = sumHKAmount;
    }

    public String getOutDepotName() {
        return outDepotName;
    }

    public void setOutDepotName(String outDepotName) {
        this.outDepotName = outDepotName;
    }

    public String getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(String outDepotId) {
        this.outDepotId = outDepotId;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
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
}
