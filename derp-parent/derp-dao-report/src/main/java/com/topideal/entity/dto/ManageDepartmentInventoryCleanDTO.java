package com.topideal.entity.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/9 17:29
 * @Description: 经营管理-部门库存清空天数统计
 */
public class ManageDepartmentInventoryCleanDTO {

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
    private Integer inventoryNum;

    @ApiModelProperty(value = "库存金额" , required = false)
    private BigDecimal inventoryAmount;

    @ApiModelProperty(value = "销售总量" , required = false)
    private Integer saleNum;

    @ApiModelProperty(value = "销售统计天数" , required = false)
    private Integer saleDays;

    @ApiModelProperty(value = "90日内库存均销量" , required = false)
    private Double avgSaleNumOfNinety;

    @ApiModelProperty(value = "库存清空天数" , required = false)
    private Double inventoryCleanDay;

    @ApiModelProperty(value = "库存清空天数Label" , required = false)
    private String inventoryCleanDayLabel;

    private Boolean departmentSumFlag;

    private String departmentSumTitle;

    public Long getNo() {
        return No;
    }

    public void setNo(Long no) {
        No = no;
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

    public Integer getInventoryNum() {
        return inventoryNum;
    }

    public void setInventoryNum(Integer inventoryNum) {
        this.inventoryNum = inventoryNum;
    }

    public BigDecimal getInventoryAmount() {
        return inventoryAmount;
    }

    public void setInventoryAmount(BigDecimal inventoryAmount) {
        this.inventoryAmount = inventoryAmount;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getSaleDays() {
        return saleDays;
    }

    public void setSaleDays(Integer saleDays) {
        this.saleDays = saleDays;
    }

    public Double getAvgSaleNumOfNinety() {
        return avgSaleNumOfNinety;
    }

    public void setAvgSaleNumOfNinety(Double avgSaleNumOfNinety) {
        this.avgSaleNumOfNinety = avgSaleNumOfNinety;
    }

    public Double getInventoryCleanDay() {
        return inventoryCleanDay;
    }

    public void setInventoryCleanDay(Double inventoryCleanDay) {
        this.inventoryCleanDay = inventoryCleanDay;
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

    public String getInventoryCleanDayLabel() {
        return inventoryCleanDayLabel;
    }

    public void setInventoryCleanDayLabel(String inventoryCleanDayLabel) {
        this.inventoryCleanDayLabel = inventoryCleanDayLabel;
    }
}
