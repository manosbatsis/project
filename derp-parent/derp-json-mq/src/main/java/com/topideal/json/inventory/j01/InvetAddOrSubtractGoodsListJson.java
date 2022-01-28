package com.topideal.json.inventory.j01;

import java.io.Serializable;

/**
 * 库存加减接口商品集合实体
 * @author 联想302
 *
 */
public class InvetAddOrSubtractGoodsListJson implements Serializable{
                                  
	private String goodsId;//商品ID
	private String goodsName;//商品名称
	private String goodsNo;//商品货号
	private String barcode;//商品条码
	private String batchNo;//批次号
	private String productionDate;//生产日期
	private String overdueDate;//失效日期
	private String type;//商品分类 （0 好品 1坏品 ）
	private int num;//数量
	private String unit;//单位
	private String operateType;//(调增、调减)
	private String isExpire;//是否过期  （0 是 1 否）
	private String commbarcode;// 标准条码
	private String buId;// 事业部ID 必填
	private String buName;// 事业部名称 必填
    private String stockLocationTypeId;// 事业部库位类型ID
    private String stockLocationTypeName;// 事业部库位类型名称
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getIsExpire() {
		return isExpire;
	}
	public void setIsExpire(String isExpire) {
		this.isExpire = isExpire;
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
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
	}
	public String getStockLocationTypeId() {
		return stockLocationTypeId;
	}
	public void setStockLocationTypeId(String stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}
	public String getStockLocationTypeName() {
		return stockLocationTypeName;
	}
	public void setStockLocationTypeName(String stockLocationTypeName) {
		this.stockLocationTypeName = stockLocationTypeName;
	}

	
}
