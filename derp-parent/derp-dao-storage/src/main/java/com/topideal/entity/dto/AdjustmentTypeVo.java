package com.topideal.entity.dto;


import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;

/**
 * 库存类型导入vo
 */
public class AdjustmentTypeVo {

	// 仓库id
	private Long depotId;
	// 仓库名称
	private String depotName;
	// 商品id
	private Long goodsId;
	// 商品编码
	private String goodsCode;
	// 商品名称
	private String goodsName;
	// 商品货号
	private String goodsNo;
	// 商品条码
	private String barcode;
	// 总调整数量
	private Integer adjustTotal;
	// 批次号
	private String batchNo;
	// 生产日期
	private String productionDate;
	// 失效日期
	private String overdueDate;
	// 理货单位
	private String tallyingUnit;
	// 调整原因
	private String adjustmentRemark;
	//序号
	private String serialNum;
	// 来源单据号
	private String sourceCode;
	//仓库自编码
	private String depotCode;
	private DepotInfoMongo depotInfoMongo;
	private DepotMerchantRelMongo depotMerchantRelMongo;
	// 类型调整名称
	private String adjustmentTypeName;
	//事业部id
	private Long buId;
	//事业部名称
	private String buName;
    //调整前商品类型
    private String oldGoodType;
    //调整后商品类型
    private String newGoodType;
    // 调整时间
    private String adjustmentTime;

	/**
	 * 事业部库位类型ID
	 */
	private Long stockLocationTypeId;
	/**
	 * 库位类型
	 */
	private String stockLocationTypeName;


	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
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
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	public String getAdjustmentRemark() {
		return adjustmentRemark;
	}
	public void setAdjustmentRemark(String adjustmentRemark) {
		this.adjustmentRemark = adjustmentRemark;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}
	public DepotInfoMongo getDepotInfoMongo() {
		return depotInfoMongo;
	}
	public void setDepotInfoMongo(DepotInfoMongo depotInfoMongo) {
		this.depotInfoMongo = depotInfoMongo;
	}
	public DepotMerchantRelMongo getDepotMerchantRelMongo() {
		return depotMerchantRelMongo;
	}
	public void setDepotMerchantRelMongo(DepotMerchantRelMongo depotMerchantRelMongo) {
		this.depotMerchantRelMongo = depotMerchantRelMongo;
	}

	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
	}
	public String getOldGoodType() {
		return oldGoodType;
	}
	public void setOldGoodType(String oldGoodType) {
		this.oldGoodType = oldGoodType;
	}
	public String getNewGoodType() {
		return newGoodType;
	}
	public void setNewGoodType(String newGoodType) {
		this.newGoodType = newGoodType;
	}
	public String getAdjustmentTime() {
		return adjustmentTime;
	}
	public void setAdjustmentTime(String adjustmentTime) {
		this.adjustmentTime = adjustmentTime;
	}
	public Integer getAdjustTotal() {
		return adjustTotal;
	}
	public void setAdjustTotal(Integer adjustTotal) {
		this.adjustTotal = adjustTotal;
	}
	public String getAdjustmentTypeName() {
		return adjustmentTypeName;
	}
	public void setAdjustmentTypeName(String adjustmentTypeName) {
		this.adjustmentTypeName = adjustmentTypeName;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}

	public String getStockLocationTypeName() {
		return stockLocationTypeName;
	}

	public void setStockLocationTypeName(String stockLocationTypeName) {
		this.stockLocationTypeName = stockLocationTypeName;
	}
}
