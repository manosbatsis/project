package com.topideal.json.api.v6_6;
/**
 * 大货理货 原批次商品
 * @author 杨创
 *2019.04.01 
 */
public class CCBigCargoTallyOriginGoodsListJson {
	
	private Long goodsId;// 商品id
	private String goodsNo;//商品货号
	private String goodsName;//商品名称
	private String goodsCode;//商品编码	
	private String barcode;// 条码
	private Integer num;//数量
	private String bathNo;//批次
	private String productionDate;//生产日期
	private String overdueDate;//失效日期
	private String unit;//出的单位   P：托盘，C：箱 , B：件
	private String isDamaged;////字符串 0：好品，1：坏品	
	private String stockBusinessType;//字符串：10.B2B,20.B2C,30.C2C	
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
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
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBathNo() {
		return bathNo;
	}
	public void setBathNo(String bathNo) {
		this.bathNo = bathNo;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getIsDamaged() {
		return isDamaged;
	}
	public void setIsDamaged(String isDamaged) {
		this.isDamaged = isDamaged;
	}
	public String getStockBusinessType() {
		return stockBusinessType;
	}
	public void setStockBusinessType(String stockBusinessType) {
		this.stockBusinessType = stockBusinessType;
	}
	
}
