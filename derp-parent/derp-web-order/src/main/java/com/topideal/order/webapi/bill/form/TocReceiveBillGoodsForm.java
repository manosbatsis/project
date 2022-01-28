package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
public class TocReceiveBillGoodsForm implements Serializable {

    @ApiModelProperty(value = "月份",required = false)
    private String billDate;
    @ApiModelProperty(value = "商品名称",required = false)
    private String goodsName;
    @ApiModelProperty(value = "数量",required = false)
    private Integer totalNum;
    @ApiModelProperty(value = "总金额",required = false)
    private BigDecimal totalAmount;


    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
