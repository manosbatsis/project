package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class OrderForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 
    @ApiModelProperty(value = "订单号(自生成)")
    private String code;
    @ApiModelProperty(value = "外部单号")
    private String externalCode;
    @ApiModelProperty(value = "仓库ID")
    private String depotId;
    @ApiModelProperty(value = "电商平台名称")
    private String storePlatformName;
    @ApiModelProperty(value = "订单来源  1:跨境宝推送, 2:导入,3:第e仓 4: 订单100") 
    private String orderSource;
    @ApiModelProperty(value = "单据状态：1:待申报 2.待放行,3:待发货,4:已发货,5:已取消")
    private String status;
    @ApiModelProperty(value = "店铺编码")
    private String shopCode;
	@ApiModelProperty(value = "交易开始时间") 
	private String tradingStartDate;
	@ApiModelProperty(value = "交易结束时间") 
	private String tradingEndDate; 	 	
	@ApiModelProperty(value = "发货时间开始") 
	private String deliverStartDate;
	@ApiModelProperty(value = "发货时间结束")
	private String deliverEndDate;
    @ApiModelProperty(value = "平台订单号")
    private String exOrderId;
    @ApiModelProperty(value = "时间段 1-近12个月数据 2-12个月以前数据") 
    private String orderTimeRange;
    @ApiModelProperty(value = "商品货号")
    private String goodsNo;
    @ApiModelProperty(value = "条形码")
    private String barcode;
    
   	@ApiModelProperty(value = "主键集合ids")
   	private String ids;
   	@ApiModelProperty(value = "表体ids集合")
   	private String itemIds;
   	 
    
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public String getStorePlatformName() {
		return storePlatformName;
	}
	public void setStorePlatformName(String storePlatformName) {
		this.storePlatformName = storePlatformName;
	}
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getTradingStartDate() {
		return tradingStartDate;
	}
	public void setTradingStartDate(String tradingStartDate) {
		this.tradingStartDate = tradingStartDate;
	}
	public String getTradingEndDate() {
		return tradingEndDate;
	}
	public void setTradingEndDate(String tradingEndDate) {
		this.tradingEndDate = tradingEndDate;
	}
	public String getDeliverStartDate() {
		return deliverStartDate;
	}
	public void setDeliverStartDate(String deliverStartDate) {
		this.deliverStartDate = deliverStartDate;
	}
	public String getDeliverEndDate() {
		return deliverEndDate;
	}
	public void setDeliverEndDate(String deliverEndDate) {
		this.deliverEndDate = deliverEndDate;
	}
	public String getExOrderId() {
		return exOrderId;
	}
	public void setExOrderId(String exOrderId) {
		this.exOrderId = exOrderId;
	}
	public String getOrderTimeRange() {
		return orderTimeRange;
	}
	public void setOrderTimeRange(String orderTimeRange) {
		this.orderTimeRange = orderTimeRange;
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
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getItemIds() {
		return itemIds;
	}
	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}
	
	
}
