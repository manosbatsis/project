package com.topideal.entity.dto;

import com.topideal.mongo.entity.DepotInfoMongo;

/**
 * 库存调整导入vo
 * @author zhanghx
 */
public class AdjustmentInventoryVo {

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
	// 调整类型
	private String type;
	// 商品类型 0-好品 1-坏品
	private String stockType;
	// 调整数量
	private Integer num;
	// 批次号
	private String batchNo;
	// 生产日期
	private String productionDate;
	// 失效日期
	private String overdueDate;
	// 理货单位
	private String tallyingUnit;
	
	private String remark;

	//序号
	private String serialNum;

	//仓库自编码
	private String depotCode;

	//业务类型
	private String serviceType;

	//归属日期
	private String sourceTime;

	private DepotInfoMongo depotInfoMongo;

	//业务类别 1：销毁 3：其他出入 5：国检抽样
	private String model;

	//po单号
	private String poNo;

	//po单时间
	private String poDate;

	//事业部id
	private Long buId;
	//事业部名称
	private String buName;

	/**
	 * 事业部库位类型ID
	 */
	private Long stockLocationTypeId;
	/**
	 * 库位类型
	 */
	private String stockLocationTypeName;

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

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
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

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSourceTime() {
		return sourceTime;
	}

	public void setSourceTime(String sourceTime) {
		this.sourceTime = sourceTime;
	}

	public DepotInfoMongo getDepotInfoMongo() {
		return depotInfoMongo;
	}

	public void setDepotInfoMongo(DepotInfoMongo depotInfoMongo) {
		this.depotInfoMongo = depotInfoMongo;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getPoDate() {
		return poDate;
	}

	public void setPoDate(String poDate) {
		this.poDate = poDate;
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
