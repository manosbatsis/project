package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Description: 开票模板表体form
 * @Author: Chen Yiluan
 * @Date: 2021/09/07 18:19
 **/
@ApiModel
public class InvoiceTemplateItemDetailsYSForm implements Serializable {

	  @ApiModelProperty("序号")
	    private String index;
	    @ApiModelProperty("商品货号")
	    private String goodsNo;
	    @ApiModelProperty("商品条码")
	    private String barcode;
	    @ApiModelProperty("商品名称")
	    private String goodsName;
	    @ApiModelProperty("数量")
	    private String totalNum;
	    @ApiModelProperty("单价")
	    private String price;    
	    @ApiModelProperty("单位")
	    private String unit;
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
		public String getBarcode() {
			return barcode;
		}
		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}
		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
		public String getTotalNum() {
			return totalNum;
		}
		public void setTotalNum(String totalNum) {
			this.totalNum = totalNum;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getUnit() {
			return unit;
		}
		public void setUnit(String unit) {
			this.unit = unit;
		}
		public String getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(String totalPrice) {
			this.totalPrice = totalPrice;
		}
	    
}
