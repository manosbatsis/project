package com.topideal.entity.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: 开票模板表体dto
 * @Author: Chen Yiluan
 * @Date: 2021/09/07 18:19
 **/
@ApiModel
public class InvoiceTemplateItemDTO implements Serializable {

    @ApiModelProperty("序号")
    private String index;

    @ApiModelProperty("商品货号")
    private String goodsNo;

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("sku")
    private String platformSku;

    @ApiModelProperty("发票描述")
    private String invoiceDescription;

    @ApiModelProperty("费项id")
    private Long projectId;

    @ApiModelProperty("母品牌id")
    private Long parentBrandId;

    @ApiModelProperty("母品牌名称")
    private String parentBrandName;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("po号")
    private String poNo;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品条码")
    private String barcode;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("单价")
    private String price;

    @ApiModelProperty("数量")
    private String totalNum;

    @ApiModelProperty("总价")
    private String totalPrice;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getPlatformSku() {
        return platformSku;
    }

    public void setPlatformSku(String platformSku) {
        this.platformSku = platformSku;
    }

    public String getInvoiceDescription() {
        return invoiceDescription;
    }

    public void setInvoiceDescription(String invoiceDescription) {
        this.invoiceDescription = invoiceDescription;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
