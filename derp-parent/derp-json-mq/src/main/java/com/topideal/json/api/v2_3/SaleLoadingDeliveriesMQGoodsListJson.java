package com.topideal.json.api.v2_3;

import java.math.BigDecimal;

/**
 * 装载交运回推MQ 商品推mq报文
 * @author lian_
 */
public class SaleLoadingDeliveriesMQGoodsListJson {

	private Long goodsId; // 商品id
	private String goodsNo; // 商品货号
	private String goodsName; // 商品名称
	private String goodsCode; // 商品编码
	private String barcode; // 货品条形码
	private Integer num; // 数量
	private BigDecimal price; // （单价）实际价格
	private String batchNo; // 批次号
	private Double cphTaxRate; // 税率
	private BigDecimal cphTaxFee; // 税费
	private String productionDate;//生产日期
	private String overdueDate;//失效日期
	private String tallyingUnit;//理货单位
	private String commbarcode;	// 标准条码
	//销售单表体Id(拆分报文用，非接口字段)
	private Long orderItemId;

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Double getCphTaxRate() {
		return cphTaxRate;
	}

	public void setCphTaxRate(Double cphTaxRate) {
		this.cphTaxRate = cphTaxRate;
	}

	public BigDecimal getCphTaxFee() {
		return cphTaxFee;
	}

	public void setCphTaxFee(BigDecimal cphTaxFee) {
		this.cphTaxFee = cphTaxFee;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
}
