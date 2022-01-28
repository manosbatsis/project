package com.topideal.entity.dto;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 采销日报明细DTO
 */
@ApiModel
public class SalePurchasedailyDayDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "商家名称", required = false)
    private String merchantName;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "客户名称", required = false)
    private String customerName;

    @ApiModelProperty(value = "品牌id", required = false)
    private Long brandId;

    @ApiModelProperty(value = "品牌名称", required = false)
    private String brandName;

    @ApiModelProperty(value = "分类id", required = false)
    private Long productTypeId;

    @ApiModelProperty(value = "分类名称", required = false)
    private String productTypeName;

    @ApiModelProperty(value = "当日销售额", required = false)
    private BigDecimal daySaleAmount;

    @ApiModelProperty(value = "报表归属日期 “yyyy-MM-dd”", required = false)
    private String reportDate;

    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

    @ApiModelProperty(value = "当日销售量", required = false)
    private Long daySaleCount;

    //===========扩展字段==========
    @ApiModelProperty(value = "月销售量", required = false)
    private Long monSaleCount;//月销售量（用于分页查询）

    @ApiModelProperty(value = "月销售额", required = false)
    private BigDecimal monSaleAmount;//月销售额（用于列表的分页查询）

    @ApiModelProperty(value = "年度销售量", required = false)
    private Long yearSaleCount;//年度销售量（用于分页查询）

    @ApiModelProperty(value = "年度销售额", required = false)
    private BigDecimal yearSaleAmount;//年度销售额（用于分页查询）

    @ApiModelProperty(value = "月日均销量", required = false)
    private Integer monAvgCount;//月日均销量（用于分页查询）


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public BigDecimal getDaySaleAmount() {
        return daySaleAmount;
    }

    public void setDaySaleAmount(BigDecimal daySaleAmount) {
        this.daySaleAmount = daySaleAmount;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
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

    public Long getDaySaleCount() {
        return daySaleCount;
    }

    public void setDaySaleCount(Long daySaleCount) {
        this.daySaleCount = daySaleCount;
    }

    public Long getMonSaleCount() {
        return monSaleCount;
    }

    public void setMonSaleCount(Long monSaleCount) {
        this.monSaleCount = monSaleCount;
    }

    public BigDecimal getMonSaleAmount() {
        return monSaleAmount;
    }

    public void setMonSaleAmount(BigDecimal monSaleAmount) {
        this.monSaleAmount = monSaleAmount;
    }

    public Long getYearSaleCount() {
        return yearSaleCount;
    }

    public void setYearSaleCount(Long yearSaleCount) {
        this.yearSaleCount = yearSaleCount;
    }

    public BigDecimal getYearSaleAmount() {
        return yearSaleAmount;
    }

    public void setYearSaleAmount(BigDecimal yearSaleAmount) {
        this.yearSaleAmount = yearSaleAmount;
    }

    public Integer getMonAvgCount() {
        return monAvgCount;
    }

    public void setMonAvgCount(Integer monAvgCount) {
        this.monAvgCount = monAvgCount;
    }
}
