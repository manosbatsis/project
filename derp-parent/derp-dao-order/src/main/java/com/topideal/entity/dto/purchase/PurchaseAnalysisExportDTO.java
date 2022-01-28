package com.topideal.entity.dto.purchase;


import java.sql.Timestamp;

import com.topideal.common.constant.DERP;

/**
 * 采购入库勾稽明细 导出表头
 * @author zhanghx
 */
public class PurchaseAnalysisExportDTO {
	
	// 采购订单编码
	private String orderCode;
	// 采购商品货号
	private String goodsNo;
	// 采购数量
	private Integer purchaseNum;
	// 采购商品名称
	private String goodsName;
	// 是否完结
	private String isEnd;
	private String isEndLabel;
	// 完结时间
	private Timestamp endDate;
	// 事业部
	private String buName ;
	
	// 入库数量
	private Integer warehouseNum;
	// 入库单编码
	private String warehouseCode;
	// 差异
	private Integer differenceNum;
	// 是否组合
	private String isGroup;
	private String isGroupLabel;
	// 批次号
	private String batchNo;
	// 生产日期
	private Timestamp productionDate;
	// 失效日期
	private Timestamp overdueDate;
	// 勾稽时间
	private Timestamp relDate;
	// 海外仓理货单位
	private String tallyingUnit;
	private String tallyingUnitLabel ;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
		
		if("1".equals(isEnd)) {
			this.isEndLabel = "是" ;
		}else {
			this.isEndLabel = "否" ;
		}
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Integer getWarehouseNum() {
		return warehouseNum;
	}

	public void setWarehouseNum(Integer warehouseNum) {
		this.warehouseNum = warehouseNum;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Integer getDifferenceNum() {
		return differenceNum;
	}

	public void setDifferenceNum(Integer differenceNum) {
		this.differenceNum = differenceNum;
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
		
		if("1".equals(isGroup)) {
			this.isGroupLabel = "是" ;
		}else {
			this.isGroupLabel = "否" ;
		}
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Timestamp getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Timestamp productionDate) {
		this.productionDate = productionDate;
	}

	public Timestamp getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(Timestamp overdueDate) {
		this.overdueDate = overdueDate;
	}

	public Timestamp getRelDate() {
		return relDate;
	}

	public void setRelDate(Timestamp relDate) {
		this.relDate = relDate;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit) ;
	}

	public String getIsEndLabel() {
		return isEndLabel;
	}

	public void setIsEndLabel(String isEndLabel) {
		this.isEndLabel = isEndLabel;
	}

	public String getIsGroupLabel() {
		return isGroupLabel;
	}

	public void setIsGroupLabel(String isGroupLabel) {
		this.isGroupLabel = isGroupLabel;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	
}
