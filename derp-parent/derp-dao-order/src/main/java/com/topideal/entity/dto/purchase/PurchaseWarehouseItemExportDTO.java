package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 入库明细表体
 * @author zhanghx
 */
public class PurchaseWarehouseItemExportDTO implements Serializable {

	// id
	private Long id;
	// 采购订单号
	private String declareCode;
	// 采购订单号
	private String purchaseCode;
	// 商品货号
	private Long goodsId ;
	private String goodsNo;
	private String goodsName;
	// 商品条形码
	private String barcode;
	// 申报数量
	private String purchaseNum;
	// 理货数量
	private String tallyingNum;
	// 缺失数量
	private String lackNum;
	// 多货数量
	private String multiNum;
	// 可售数量/入库数量
	private String normalNum;
	// 批次号
	private String batchNo;
	// 生产日期
	private Timestamp productionDate;
	//	过期日期
	private Timestamp overdueDate;
	// 长度
	private String length;
	// 体积
	private String volume;
	// 长
	private String width;
	// 高
	private String height;
	// 正常数量
	private String normalNum2;
	// 过期数量
	private String expireNum;
	// 坏货数量
	private String wornNum;
	// 理货单位
	private String tallyingUnit;
	private String tallyingUnitLabel ;

	private String commbarcode;
	//采购单价
	private BigDecimal purchasePrice;

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit) ;
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

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getNormalNum2() {
		return normalNum2;
	}

	public void setNormalNum2(String normalNum2) {
		this.normalNum2 = normalNum2;
	}

	public String getExpireNum() {
		return expireNum;
	}

	public void setExpireNum(String expireNum) {
		this.expireNum = expireNum;
	}

	public String getWornNum() {
		return wornNum;
	}

	public void setWornNum(String wornNum) {
		this.wornNum = wornNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeclareCode() {
		return declareCode;
	}

	public void setDeclareCode(String declareCode) {
		this.declareCode = declareCode;
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

	public String getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(String purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public String getTallyingNum() {
		return tallyingNum;
	}

	public void setTallyingNum(String tallyingNum) {
		this.tallyingNum = tallyingNum;
	}

	public String getLackNum() {
		return lackNum;
	}

	public void setLackNum(String lackNum) {
		this.lackNum = lackNum;
	}

	public String getMultiNum() {
		return multiNum;
	}

	public void setMultiNum(String multiNum) {
		this.multiNum = multiNum;
	}

	public String getNormalNum() {
		return normalNum;
	}

	public void setNormalNum(String normalNum) {
		this.normalNum = normalNum;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
}
