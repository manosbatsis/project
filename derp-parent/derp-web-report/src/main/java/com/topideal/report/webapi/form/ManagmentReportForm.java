package com.topideal.report.webapi.form;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author: Wilson Lau
 * @Date: 2021/9/8 14:59
 * @Description:
 */
public class ManagmentReportForm {

    /**
     * 月份
     */
    @ApiModelProperty("令牌")
    private String token;

    /**
     * 月份
     */
    @ApiModelProperty("月份")
    private String month;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private String year;

    /**
     * 部门集合
     */
    @ApiModelProperty("部门集合")
    private List<Long> departmentIds;


    @ApiModelProperty(value = "标准品牌ID集合")
    private List<Long> brandParentIds;
    /**
     * 母品牌ID
     */
    @ApiModelProperty("母品牌集合")
    private List<Long> parentBrandIds;

    @ApiModelProperty(value = "事业部ID集合", required = false)
    private List<Long> buIds;

    @ApiModelProperty(value = "公司id")
    private Long merchantId;

    @ApiModelProperty("公司id集合")
    private List<Long> merchantIds;

    @ApiModelProperty(value = "渠道类型集合")
    private List<String> channelTypes;

    @ApiModelProperty("客户id集合")
    private List<Long> customerIds;

    @ApiModelProperty(value = "出库仓库id集合", required = false)
    private List<Long> outDepotIds;

//    @ApiModelProperty(value = "出库仓库名称", required = false)
//    private String outDepotName;

    @ApiModelProperty(value = "平台编码集合", required = false)
    private List<String> storePlatformCodeList;

    private Boolean groupByParentBrandFlag;

    private Boolean groupByBrandParentFlag;

    private Boolean groupByBuIdFlag;

    private Boolean groupByMerchantFlag;

    private Boolean groupByChannelTypeFlag;

    private Boolean groupByCustomerFlag;

    private Boolean groupByDepotFlag;

    private Boolean groupByPlatformFlag;

    private Boolean groupByEffectiveInterval;

    @ApiModelProperty(value = "升序降序 DESC/ ASC")
    private String orderByAmount1;

    @ApiModelProperty(value = "升序降序 DESC/ ASC")
    private String orderByAmount2;

    @ApiModelProperty(value = "升序降序 DESC/ ASC")
    private String orderByAmount3;

    /**
     * 上上个月
     */
    private String beforeMonth;

    /**
     * 上个月
     */
    private String middleMonth;

    /**
     * 这个月
     */
    private String targetMonth;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<Long> getBrandParentIds() {
        return brandParentIds;
    }

    public void setBrandParentIds(List<Long> brandParentIds) {
        this.brandParentIds = brandParentIds;
    }

    public List<Long> getParentBrandIds() {
        return parentBrandIds;
    }

    public void setParentBrandIds(List<Long> parentBrandIds) {
        this.parentBrandIds = parentBrandIds;
    }

    public List<Long> getBuIds() {
        return buIds;
    }

    public void setBuIds(List<Long> buIds) {
        this.buIds = buIds;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public List<Long> getMerchantIds() {
        return merchantIds;
    }

    public void setMerchantIds(List<Long> merchantIds) {
        this.merchantIds = merchantIds;
    }

    public List<String> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<String> channelTypes) {
        this.channelTypes = channelTypes;
    }

    public Boolean getGroupByParentBrandFlag() {
        return groupByParentBrandFlag;
    }

    public void setGroupByParentBrandFlag(Boolean groupByParentBrandFlag) {
        this.groupByParentBrandFlag = groupByParentBrandFlag;
    }

    public Boolean getGroupByBrandParentFlag() {
        return groupByBrandParentFlag;
    }

    public void setGroupByBrandParentFlag(Boolean groupByBrandParentFlag) {
        this.groupByBrandParentFlag = groupByBrandParentFlag;
    }

    public Boolean getGroupByBuIdFlag() {
        return groupByBuIdFlag;
    }

    public void setGroupByBuIdFlag(Boolean groupByBuIdFlag) {
        this.groupByBuIdFlag = groupByBuIdFlag;
    }

    public Boolean getGroupByMerchantFlag() {
        return groupByMerchantFlag;
    }

    public void setGroupByMerchantFlag(Boolean groupByMerchantFlag) {
        this.groupByMerchantFlag = groupByMerchantFlag;
    }

    public Boolean getGroupByChannelTypeFlag() {
        return groupByChannelTypeFlag;
    }

    public void setGroupByChannelTypeFlag(Boolean groupByChannelTypeFlag) {
        this.groupByChannelTypeFlag = groupByChannelTypeFlag;
    }

    public Boolean getGroupByCustomerFlag() {
        return groupByCustomerFlag;
    }

    public void setGroupByCustomerFlag(Boolean groupByCustomerFlag) {
        this.groupByCustomerFlag = groupByCustomerFlag;
    }

    public Boolean getGroupByDepotFlag() {
        return groupByDepotFlag;
    }

    public void setGroupByDepotFlag(Boolean groupByDepotFlag) {
        this.groupByDepotFlag = groupByDepotFlag;
    }

    public Boolean getGroupByPlatformFlag() {
        return groupByPlatformFlag;
    }

    public void setGroupByPlatformFlag(Boolean groupByPlatformFlag) {
        this.groupByPlatformFlag = groupByPlatformFlag;
    }

    public String getBeforeMonth() {
        return beforeMonth;
    }

    public void setBeforeMonth(String beforeMonth) {
        this.beforeMonth = beforeMonth;
    }

    public String getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(String targetMonth) {
        this.targetMonth = targetMonth;
    }

    public List<String> getStorePlatformCodeList() {
        return storePlatformCodeList;
    }

    public void setStorePlatformCodeList(List<String> storePlatformCodeList) {
        this.storePlatformCodeList = storePlatformCodeList;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    public void setCustomerIds(List<Long> customerIds) {
        this.customerIds = customerIds;
    }

    public List<Long> getOutDepotIds() {
        return outDepotIds;
    }

    public void setOutDepotIds(List<Long> outDepotIds) {
        this.outDepotIds = outDepotIds;
    }

    public String getMiddleMonth() {
        return middleMonth;
    }

    public void setMiddleMonth(String middleMonth) {
        this.middleMonth = middleMonth;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOrderByAmount1() {
        return orderByAmount1;
    }

    public void setOrderByAmount1(String orderByAmount1) {
        this.orderByAmount1 = orderByAmount1;
    }

    public String getOrderByAmount2() {
        return orderByAmount2;
    }

    public void setOrderByAmount2(String orderByAmount2) {
        this.orderByAmount2 = orderByAmount2;
    }

    public String getOrderByAmount3() {
        return orderByAmount3;
    }

    public void setOrderByAmount3(String orderByAmount3) {
        this.orderByAmount3 = orderByAmount3;
    }

    public Boolean getGroupByEffectiveInterval() {
        return groupByEffectiveInterval;
    }

    public void setGroupByEffectiveInterval(Boolean groupByEffectiveInterval) {
        this.groupByEffectiveInterval = groupByEffectiveInterval;
    }
}
