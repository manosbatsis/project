package com.topideal.api.dstp.d01;

import java.util.List;

public class GoodsDstp {

    private String customerCode;//统一社会信用代码或香港注册登记证
    private String barCode;//条码
    private String goodsName;//商品名称
    private String brand;//标准品牌
    private String brandEn;//品牌英文名
    private String specification;//规格型号
    private String orgCountry;//原产地  取商品的原产国
    private String produceCompany;//生产厂家
    private String firstLegalUnit;//第一法定单位
    private String secondLegalUnit;//第二法定单位
    private String grossWeight;//毛重
    private String netWeight;//净重
    private List<String> picUrl;//图片 多个图片英文,拼接
    private List<GoodsItem> recordInfo;//备案商品

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandEn() {
        return brandEn;
    }

    public void setBrandEn(String brandEn) {
        this.brandEn = brandEn;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getOrgCountry() {
        return orgCountry;
    }

    public void setOrgCountry(String orgCountry) {
        this.orgCountry = orgCountry;
    }

    public String getProduceCompany() {
        return produceCompany;
    }

    public void setProduceCompany(String produceCompany) {
        this.produceCompany = produceCompany;
    }

    public String getFirstLegalUnit() {
        return firstLegalUnit;
    }

    public void setFirstLegalUnit(String firstLegalUnit) {
        this.firstLegalUnit = firstLegalUnit;
    }

    public String getSecondLegalUnit() {
        return secondLegalUnit;
    }

    public void setSecondLegalUnit(String secondLegalUnit) {
        this.secondLegalUnit = secondLegalUnit;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(String netWeight) {
        this.netWeight = netWeight;
    }

    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }

    public List<GoodsItem> getRecordInfo() {
        return recordInfo;
    }

    public void setRecordInfo(List<GoodsItem> recordInfo) {
        this.recordInfo = recordInfo;
    }

}
