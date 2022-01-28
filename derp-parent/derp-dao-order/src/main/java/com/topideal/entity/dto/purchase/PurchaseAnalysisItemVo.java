package com.topideal.entity.dto.purchase;

/**
 * 采购入库勾稽明细 表体
 * @author zhanghx
 */
public class PurchaseAnalysisItemVo {

	// 勾稽入库单号
	private String warehouseCode;
	// 入库商品货号
	private String goodsNo;
	// 入库商品名称
	private String goodsName;
	// 勾稽入库数量
	private String num;
	// 入库是否组合品
	private String isGroup;
	// 商品类型
	private String type;
	// 批次号
	private String batchNo;
	// 生产日期
	private String productionDate;
	// 失效日期
	private String overdueDate;
	// 勾稽关联时间
	private String relDate;
	// 是否完结入库
	private String isEnd;
	// 完结入库时间
	private String endDate;
	// 完结入库操作人
	private String endName;

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
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

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getRelDate() {
		return relDate;
	}

	public void setRelDate(String relDate) {
		this.relDate = relDate;
	}

	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndName() {
		return endName;
	}

	public void setEndName(String endName) {
		this.endName = endName;
	}

}
