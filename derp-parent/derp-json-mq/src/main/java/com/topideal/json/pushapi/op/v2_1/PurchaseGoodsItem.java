package com.topideal.json.pushapi.op.v2_1;

/**
 * 采购入库商品实体类
 * @author 杨创
 * 2018/4/3
 */
public class PurchaseGoodsItem {
    //企业商品SKU_ID（即商品备案时的货号）  商品：FITEMGOODSID
    private String goodsNo;
    //商品名称      商品名称：FNAME
    private String goodsName;
    //SKU_ID对应数量       FQTY
    private int amount;
    //单价（采购价）        商品:FPRICE
    private double unitPrice;
    //包装方式/规格（码表提供，卓志会检验有效性）  商品：FPACKTYPE
    private String packageType;
    //币种，参考（币种码表）   CNY
    private String currency="CNY";

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
