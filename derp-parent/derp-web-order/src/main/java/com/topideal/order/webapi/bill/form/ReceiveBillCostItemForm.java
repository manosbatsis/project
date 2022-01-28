package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


@ApiModel
public class ReceiveBillCostItemForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "费项id", required = false)
    private String projectId;

    @ApiModelProperty(value = "费项名称")
    private String projectName;

    @ApiModelProperty(value = "商品id")
    private String goodsId;

    @ApiModelProperty(value = "商品货号")
    private String goodsNo;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "母品牌id")
    private String brandParent;

    @ApiModelProperty(value = "po号")
    private String poNo;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "单价")
    private String price;

    @ApiModelProperty(value = "补扣款类型")
    private String billType;

    @ApiModelProperty(value = "核销平台")
    private String storePlatformCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "发票描述")
    private String invoiceDescription;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "费用金额（含税）")
    private String taxAmount;

    /**
     * 平台商品编码
     */
    @ApiModelProperty(value = "平台商品编码")
    private String platformGoodsCode;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBrandParent() {
        return brandParent;
    }

    public void setBrandParent(String brandParent) {
        this.brandParent = brandParent;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getPlatformGoodsCode() {
        return platformGoodsCode;
    }

    public void setPlatformGoodsCode(String platformGoodsCode) {
        this.platformGoodsCode = platformGoodsCode;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }
}
