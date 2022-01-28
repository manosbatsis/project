package com.topideal.json.storage.j03;

import java.io.Serializable;

/**
 * 类型调整接口商品集合实体
 * 
 * @author 杨创
 *
 */
public class AdjustmentTypeGoodsListJson implements Serializable {

	private String goodsId;// 商品ID
	private String goodsName;// 商品名称
	private String goodsNo;// 商品货号
	private String batchNo;// 原始批次号
	private String barcode;// 商品条形码
	private String stockType;// 商品分类 0好品 1坏品
	private String productionDate;// 生产日期
	private String overdueDate;// 失效日期
	private int adjustTotal;// 数量
	private String unit;// 库存单位
	
	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
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


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public int getAdjustTotal() {
		return adjustTotal;
	}

	public void setAdjustTotal(int adjustTotal) {
		this.adjustTotal = adjustTotal;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	
}
