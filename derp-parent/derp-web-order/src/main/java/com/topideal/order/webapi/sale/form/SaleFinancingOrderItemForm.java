package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel
public class SaleFinancingOrderItemForm  extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

	@ApiModelProperty(value = "融资订单ID")
    private Long orderId;
   
	@ApiModelProperty(value = "商品ID")
    private String goodsId;
   
	@ApiModelProperty(value = "商品编码")
    private String goodsCode;
   
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
  
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;

	@ApiModelProperty(value = "销售数量")
    private Integer num;
    
	@ApiModelProperty(value = "单价")
    private BigDecimal price;
   
	@ApiModelProperty(value = "金额")
    private BigDecimal amount;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
