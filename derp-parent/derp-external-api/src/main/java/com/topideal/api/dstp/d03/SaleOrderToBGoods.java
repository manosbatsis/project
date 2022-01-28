package com.topideal.api.dstp.d03;

public class SaleOrderToBGoods {

    private String goodsName;//商品名称
    private String barCode;//条码
    private Integer qty;//销售数量

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
