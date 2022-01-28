package com.topideal.json.api.v6_5;

/**
 * 仓储-盘点结果商品
 * @author 杨创
 *  2018.7.25
 */
public class OfcCCInventoryResultsGoodsListJson {
	private String goodsNo;// 商品货号
	private Long goodsId;// 商品id
	private String goodsName;// 商品名称
	private String goodsCode;// 商品编码
	private String barcode;// 货品条码
	private Integer amount;// 系统数量
	private Integer realqty;// 实盘数量
	private String diffqty;// 差异数
	private String productionDate;// 生产日期
	private String overdueDate;// 失效日期
	private String isDamage;// YN坏品 0：好品 1：坏品
	private String batchNo;// 退运批次	
	private String unit;// 单位P：托盘，C：箱 , B：件
	private String reasonCode;//01：盘点差异；02：可保存报废；03：不可保存报废；04：二手售卖；05：调拨差异；06：收货错误；07：仓库错发；08：赔付平台已审批
	private String reasonDes;// 原因描述
	private String stockBusiType;//库存业务类型 B2B、B2C，默认B2C
	
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Integer getRealqty() {
		return realqty;
	}
	public void setRealqty(Integer realqty) {
		this.realqty = realqty;
	}
	public String getDiffqty() {
		return diffqty;
	}
	public void setDiffqty(String diffqty) {
		this.diffqty = diffqty;
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
	public String getIsDamage() {
		return isDamage;
	}
	public void setIsDamage(String isDamage) {
		this.isDamage = isDamage;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getReasonCode() {
		return reasonCode;
	}
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	public String getReasonDes() {
		return reasonDes;
	}
	public void setReasonDes(String reasonDes) {
		this.reasonDes = reasonDes;
	}
	public String getStockBusiType() {
		return stockBusiType;
	}
	public void setStockBusiType(String stockBusiType) {
		this.stockBusiType = stockBusiType;
	}
	
	
	
	
	

}
