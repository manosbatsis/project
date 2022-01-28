package com.topideal.json.api.v6_4;

/**
 * 仓储- 退运信息商品
 * 
 * @author 杨创 2018.07.25
 */
public class CCReturnMessageGoodsListJson {

	private String goodsNo;//商品货号
	private Long goodsId;// 商品id
	private String goodsName;// 商品名称
	private String goodsCode;// 商品编码
	private String barcode;// 货品条码
	private Double pric;//申报单价
	private Integer num;//退运数量
	private String stockType;//0:好品;1:坏品;
	private String batchNo;//退运批次
	private String productionDate;//生产日期
	private String overdueDate;//失效日期
	private String commbarcode;//标准条码
	public String getGoodsNo() {
		return goodsNo;
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
	public Double getPric() {
		return pric;
	}
	public void setPric(Double pric) {
		this.pric = pric;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getStockType() {
		return stockType;
	}
	public void setStockType(String stockType) {
		this.stockType = stockType;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

}
