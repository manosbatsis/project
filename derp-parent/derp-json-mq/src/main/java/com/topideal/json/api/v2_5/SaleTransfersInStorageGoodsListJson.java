package com.topideal.json.api.v2_5;

/**
 * 调拨入库 商品推mq报文
 * 
 * @author lian_
 *
 */
public class SaleTransfersInStorageGoodsListJson {

	private Long goodsId; // 商品id
	private String goodsNo; // 商品货号
	private String goodsName; // 商品名称
	private String goodsCode; // 商品编码
	private Integer num; // 退运数量
	private String batchNo; // 商品批次
	private String productionDate; // 生产日期 时间格式：yyyy-mm-dd HH:mi:ss 非必填
	private String overdueDate; // 失效日期 时间格式：yyyy-mm-dd HH:mi:ss 非必填
	private String barcode; // 货品条形码
	private String commbarcode;//标准条码
	private Integer wornNum;// 损货数量
	private Integer salesNum;// 批次销售数量（正常数量）

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

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public Integer getWornNum() {
		return wornNum;
	}

	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}

	public Integer getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(Integer salesNum) {
		this.salesNum = salesNum;
	}
}
