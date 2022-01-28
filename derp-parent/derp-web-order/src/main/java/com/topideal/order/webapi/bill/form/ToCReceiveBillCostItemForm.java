package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
public class ToCReceiveBillCostItemForm implements Serializable {

    @ApiModelProperty(value = "费项id", required = true)
    private Long projectId;

    @ApiModelProperty(value = "费项名称", required = true)
    private String projectName;

    @ApiModelProperty(value = "补扣款类型 0-补款 1-扣款", required = true)
    private String billType;

    @ApiModelProperty(value = "母品牌id", required = true)
    private Long brandParent;

    @ApiModelProperty(value = "母品牌名称", required = true)
    private String brandParentName;

    @ApiModelProperty(value = "数量", required = true)
    private Integer num;

    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "RMB金额", required = true)
    private BigDecimal rmbPrice;

    @ApiModelProperty(value = "备注", required = false)
    private String remark;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public Long getBrandParent() {
        return brandParent;
    }

    public void setBrandParent(Long brandParent) {
        this.brandParent = brandParent;
    }

    public String getBrandParentName() {
        return brandParentName;
    }

    public void setBrandParentName(String brandParentName) {
        this.brandParentName = brandParentName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getRmbPrice() {
        return rmbPrice;
    }

    public void setRmbPrice(BigDecimal rmbPrice) {
        this.rmbPrice = rmbPrice;
    }
}
