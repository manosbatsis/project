package com.topideal.entity.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: 开票模板唯品表体dto
 * @Author: Chen Yiluan
 * @Date: 2021/09/07 18:19
 **/
@ApiModel
public class InvoiceWeiPinTemplateItemDTO implements Serializable {

    @ApiModelProperty("客退补贴")
    private String customerReturn;

    @ApiModelProperty("客退折让")
    private String promotionDiscountsCust;

    @ApiModelProperty("活动折扣")
    private String promotionDiscounts;

    @ApiModelProperty("补偿折扣")
    private String extraDiscount;

    @ApiModelProperty("返利金额")
    private String extraAmount;

    @ApiModelProperty("总金额")
    private String totalPrice;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("品牌名称")
    private String brandName;

    @ApiModelProperty("单价")
    private String price;

    @ApiModelProperty("数量")
    private String totalNum;

    public String getCustomerReturn() {
        return customerReturn;
    }

    public void setCustomerReturn(String customerReturn) {
        this.customerReturn = customerReturn;
    }

    public String getPromotionDiscountsCust() {
        return promotionDiscountsCust;
    }

    public void setPromotionDiscountsCust(String promotionDiscountsCust) {
        this.promotionDiscountsCust = promotionDiscountsCust;
    }

    public String getPromotionDiscounts() {
        return promotionDiscounts;
    }

    public void setPromotionDiscounts(String promotionDiscounts) {
        this.promotionDiscounts = promotionDiscounts;
    }

    public String getExtraDiscount() {
        return extraDiscount;
    }

    public void setExtraDiscount(String extraDiscount) {
        this.extraDiscount = extraDiscount;
    }

    public String getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(String extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }
}
