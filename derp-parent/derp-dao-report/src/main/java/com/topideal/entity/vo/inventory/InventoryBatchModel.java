package com.topideal.entity.vo.inventory;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.core.serializer.Serializer;

import com.topideal.common.system.ibatis.PageModel;

public class InventoryBatchModel extends PageModel implements Serializable {
	// 商品货号
	private String goodsNo;
	// 失效日期
	private Date overdueDate;
	// 仓库名称
	private String depotName;
	// 批次号
	private String batchNo;
	// 在途库存
	private Integer onWayNum;
	// 商品id
	private Long goodsId;
	// 仓库id
	private Long depotId;
	// 库存余量
	private Integer surplusNum;
	// 可用库存量
	private Integer availableNum;
	// 库存类型 1 正常品 2 残次品
	private String type;
	// 商家名称
	private String merchantName;
	// 生产日期
	private Date productionDate;
	// 商家ID
	private Long merchantId;
	// 商品名称
	private String goodsName;
	// 创建人
	private Long creater;
	// id
	private Long id;
	// 创建时间
	private Timestamp createDate;
	// 冻结库存量
	private Integer freezeNum;
	// 托盘号
	private String lpn;
	// 理货单位
	private String unit;
	// 商家卓志编码
	private String topidealCode;
	// 仓库编码
	private String depotCode;
	// 仓库类型(a-卓志保税仓，b-非卓志保税仓，c-卓志海外仓，d-在途仓,e非卓志国内仓）
	private String depotType;
	// 是否代销仓(1-是,0-否)
	private String isTopBooks;
	// 结转月份
	private String ownMonth;
	// 是否过期（0 未过期 1已过期）
	private String isExpire;
	// 条形码
	private String barcode;
	/**
	 * 品牌id
	 */
	private Long brandId;
	/**
	 * 品牌名称
	 */
	private String brandName;

	// 修改时间
	private Timestamp modifyDate;
	

	//新增字段用于效期预警
     //剩余效期（天）
    private String surplusDays;
     //总效期（天）
    private String totalDays;
    //剩余效期
    private String surplusDate;
    //筛选字段效期标识
    private String validityType;
    ////筛选字段效期
    private Double validityTime;
    //坏品总数
    private Integer  badNum;
    //好品总数
    private Integer okayNum;

	//标准条码
	private String commbarcode;
    
    

	public String getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(String surplusDays) {
		this.surplusDays = surplusDays;
	}

	public String getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(String totalDays) {
		this.totalDays = totalDays;
	}

	public String getSurplusDate() {
		return surplusDate;
	}

	public void setSurplusDate(String surplusDate) {
		this.surplusDate = surplusDate;
	}

	public String getValidityType() {
		return validityType;
	}

	public void setValidityType(String validityType) {
		this.validityType = validityType;
	}

	public Double getValidityTime() {
		return validityTime;
	}

	public void setValidityTime(Double validityTime) {
		this.validityTime = validityTime;
	}

	public Integer getBadNum() {
		return badNum;
	}

	public void setBadNum(Integer badNum) {
		this.badNum = badNum;
	}

	public Integer getOkayNum() {
		return okayNum;
	}

	public void setOkayNum(Integer okayNum) {
		this.okayNum = okayNum;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public Date getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getOnWayNum() {
		return onWayNum;
	}

	public void setOnWayNum(Integer onWayNum) {
		this.onWayNum = onWayNum;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}

	public Integer getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(Integer availableNum) {
		this.availableNum = availableNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getProductionDate() {
		return productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getFreezeNum() {
		return freezeNum;
	}

	public void setFreezeNum(Integer freezeNum) {
		this.freezeNum = freezeNum;
	}

	public String getLpn() {
		return lpn;
	}

	public void setLpn(String lpn) {
		this.lpn = lpn;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDepotType() {
		return depotType;
	}

	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

	public String getIsTopBooks() {
		return isTopBooks;
	}

	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}

	public String getOwnMonth() {
		return ownMonth;
	}

	public void setOwnMonth(String ownMonth) {
		this.ownMonth = ownMonth;
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

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}
}
