package com.topideal.api.sapience.sapience009;

import java.math.BigDecimal;

/**
 * @Description: 商品
 * @Author: Chen Yiluan
 * @Date: 2021/01/26 10:33
 **/
public class Goods {

    //商品编码
    private String goodsNo;
    //商品名称
    private String goodsName;
    //商品原产地
    private String originCountry;
    //商品保质天数
    private Integer qualityGuaranteeDates;
    //采购数量
    private Integer purchaseQuantity;
    //采购PO总价
    private BigDecimal purchaseAmount;

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

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public Integer getQualityGuaranteeDates() {
        return qualityGuaranteeDates;
    }

    public void setQualityGuaranteeDates(Integer qualityGuaranteeDates) {
        this.qualityGuaranteeDates = qualityGuaranteeDates;
    }

    public Integer getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(Integer purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public BigDecimal getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(BigDecimal purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }
}
