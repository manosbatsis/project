package com.topideal.entity.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 经营管理-部门销售达成统计数据
 */
public class ManageDepartmentSaleAchieveDTO {

    @ApiModelProperty(value = "序号", required = false)
    private Long no;

    @ApiModelProperty(value = "事业部名称", required = false)
    private String buName;

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

    private List<SalesAchieveDTO> monthAchieveList;

    /**
     * 月目标金额
     */
    private BigDecimal monthTargetAmount;

    /**
     * 月达成金额
     */
    private BigDecimal monthAchieveAmount;

    /**
     * 月完成度
     */
    private BigDecimal monthCompletionPercentage;

    /**
     * 年目标金额
     */
    private BigDecimal yearTargetAmount;

    /**
     * 年达成金额
     */
    private BigDecimal yearAchieveAmount;

    /**
     * 年完成度
     */
    private BigDecimal yearCompletionPercentage;

    /**
     * 合并标识
     */
    private Boolean departmentSumFlag;

    /**
     * 合并表体
     */
    private String departmentSumTitle;

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
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

    public BigDecimal getYearTargetAmount() {
        return yearTargetAmount;
    }

    public void setYearTargetAmount(BigDecimal yearTargetAmount) {
        this.yearTargetAmount = yearTargetAmount;
    }

    public BigDecimal getYearAchieveAmount() {
        return yearAchieveAmount;
    }

    public void setYearAchieveAmount(BigDecimal yearAchieveAmount) {
        this.yearAchieveAmount = yearAchieveAmount;
    }


    public BigDecimal getYearCompletionPercentage() {
        return yearCompletionPercentage;
    }

    public void setYearCompletionPercentage(BigDecimal yearCompletionPercentage) {
        this.yearCompletionPercentage = yearCompletionPercentage;
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
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public List<SalesAchieveDTO> getMonthAchieveList() {
        return monthAchieveList;
    }

    public void setMonthAchieveList(List<SalesAchieveDTO> monthAchieveList) {
        this.monthAchieveList = monthAchieveList;
    }

    public BigDecimal getMonthTargetAmount() {
        return monthTargetAmount;
    }

    public void setMonthTargetAmount(BigDecimal monthTargetAmount) {
        this.monthTargetAmount = monthTargetAmount;
    }

    public BigDecimal getMonthAchieveAmount() {
        return monthAchieveAmount;
    }

    public void setMonthAchieveAmount(BigDecimal monthAchieveAmount) {
        this.monthAchieveAmount = monthAchieveAmount;
    }

    public BigDecimal getMonthCompletionPercentage() {
        return monthCompletionPercentage;
    }

    public void setMonthCompletionPercentage(BigDecimal monthCompletionPercentage) {
        this.monthCompletionPercentage = monthCompletionPercentage;
    }
}
