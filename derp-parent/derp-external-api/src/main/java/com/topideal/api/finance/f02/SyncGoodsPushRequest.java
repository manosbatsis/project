package com.topideal.api.finance.f02;

public class SyncGoodsPushRequest {
	private String goodsName;//经分销系统商品名称
	private String goodsSpec;//经分销系统商品规格型号
	private String goodsPacktype;//goodsPacktype
	private Double recordPrice;//经分销系统商品单价
	private String goodsBarcode;//经分销系统商品条形码
	private String goodsEnName;//商品英文名称
	private String goodsDese;//商品描述
	private Integer goodsQualityDays;//保质天数
	private String productComp;//生产厂家
	private String productCompAddr;//生产厂家地址
	private String qtpUnit;//标准单位
	private Double kgs;//毛重
	private Double net;//净重
	private String custassemCountry;//海关原产国
	private String messageType;//同步类型
	private String contractsUnit;//合同单位不能为空
	
	public String getContractsUnit() {
		return contractsUnit;
	}
	public void setContractsUnit(String contractsUnit) {
		this.contractsUnit = contractsUnit;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsSpec() {
		return goodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}
	public String getGoodsPacktype() {
		return goodsPacktype;
	}
	public void setGoodsPacktype(String goodsPacktype) {
		this.goodsPacktype = goodsPacktype;
	}
	public Double getRecordPrice() {
		return recordPrice;
	}
	public void setRecordPrice(Double recordPrice) {
		this.recordPrice = recordPrice;
	}
	public String getGoodsBarcode() {
		return goodsBarcode;
	}
	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}
	public String getGoodsEnName() {
		return goodsEnName;
	}
	public void setGoodsEnName(String goodsEnName) {
		this.goodsEnName = goodsEnName;
	}
	public String getGoodsDese() {
		return goodsDese;
	}
	public void setGoodsDese(String goodsDese) {
		this.goodsDese = goodsDese;
	}
	public Integer getGoodsQualityDays() {
		return goodsQualityDays;
	}
	public void setGoodsQualityDays(Integer goodsQualityDays) {
		this.goodsQualityDays = goodsQualityDays;
	}
	public String getProductComp() {
		return productComp;
	}
	public void setProductComp(String productComp) {
		this.productComp = productComp;
	}
	public String getProductCompAddr() {
		return productCompAddr;
	}
	public void setProductCompAddr(String productCompAddr) {
		this.productCompAddr = productCompAddr;
	}
	public String getQtpUnit() {
		return qtpUnit;
	}
	public void setQtpUnit(String qtpUnit) {
		this.qtpUnit = qtpUnit;
	}

	public Double getKgs() {
		return kgs;
	}
	public void setKgs(Double kgs) {
		this.kgs = kgs;
	}
	public Double getNet() {
		return net;
	}
	public void setNet(Double net) {
		this.net = net;
	}
	public String getCustassemCountry() {
		return custassemCountry;
	}
	public void setCustassemCountry(String custassemCountry) {
		this.custassemCountry = custassemCountry;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	
	
	

			
			
	
	
	
}
