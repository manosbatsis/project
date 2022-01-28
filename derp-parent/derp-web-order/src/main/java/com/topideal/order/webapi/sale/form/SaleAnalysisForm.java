package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 销售出库分析表
 *
 */
@ApiModel
public class SaleAnalysisForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    @ApiModelProperty(value = "销售出库编码")
    private String saleOutDepotCode;
    @ApiModelProperty(value = "是否完结入库(1-是,0-否)")
    private String isEnd;
    @ApiModelProperty(value = "完结出库时间")
    private String endDateStr;
    @ApiModelProperty(value = "销售订单编号")
    private String orderCode;
    @ApiModelProperty(value = "销售类型 1购销 2代销")
    private String saleType;
    @ApiModelProperty(value = "事业部id")
    private String buId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getSaleOutDepotCode() {
		return saleOutDepotCode;
	}
	public void setSaleOutDepotCode(String saleOutDepotCode) {
		this.saleOutDepotCode = saleOutDepotCode;
	}
	public String getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}	
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
	
}
