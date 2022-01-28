package com.topideal.entity.vo.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 采购入库分析表
 * 
 * @author lian_
 */
public class PurchaseAnalysisModel extends PageModel implements Serializable {

	// 商品货号
	private String goodsNo;
	// 供应商名称
	private String supplierName;
	// 供应商ID
	private Long supplierId;
	// 采购订单ID
	private Long orderId;
	// 商品ID
	private Long goodsId;
	// 入库数量
	private Integer warehouseNum;
	// 采购入库编码
	private String warehouseCode;
	// 采购入库单ID
	private Long warehouseId;
	// 创建人
	private Long creater;
	// 采购订单编号
	private String orderCode;
	// id
	private Long id;
	// 采购数量
	private Integer purchaseNum;
	// 商品名称
	private String goodsName;
	// 创建时间
	private Timestamp createDate;
	// 完结入库时间
	private Timestamp endDate;
	// 是否完结入库(1-是,0-否)
	private String isEnd;
	// 失效日期
	private Date overdueDate;
	// 批次号
	private String batchNo;
	// 入库是否组合品(1-是，0-否)
	private String isGroup;
	// 勾稽关联时间
	private Timestamp relDate;
	// 生产日期
	private Date productionDate;
	// 商家ID
	private Long merchantId;
	// 商家名称
	private String merchantName;
    //修改时间
    private Timestamp modifyDate;
    //条形码
    private String barcode;
    //理货单位 00-托盘 01-箱 02-件
    private String tallyingUnit;


	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 *  事业部id
	 */
	private Long buId;

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* productionDate get 方法 */
	public Date getProductionDate() {
		return productionDate;
	}

	/* productionDate set 方法 */
	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	/* relDate get 方法 */
	public Timestamp getRelDate() {
		return relDate;
	}

	/* relDate set 方法 */
	public void setRelDate(Timestamp relDate) {
		this.relDate = relDate;
	}

	/* isGroup get 方法 */
	public String getIsGroup() {
		return isGroup;
	}

	/* isGroup set 方法 */
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}

	/* overdueDate get 方法 */
	public Date getOverdueDate() {
		return overdueDate;
	}

	/* overdueDate set 方法 */
	public void setOverdueDate(Date overdueDate) {
		this.overdueDate = overdueDate;
	}

	/* batchNo get 方法 */
	public String getBatchNo() {
		return batchNo;
	}

	/* batchNo set 方法 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/* isEnd get 方法 */
	public String getIsEnd() {
		return isEnd;
	}

	/* isEnd set 方法 */
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	/* endDate get 方法 */
	public Timestamp getEndDate() {
		return endDate;
	}

	/* endDate set 方法 */
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* supplierName get 方法 */
	public String getSupplierName() {
		return supplierName;
	}

	/* supplierName set 方法 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/* supplierId get 方法 */
	public Long getSupplierId() {
		return supplierId;
	}

	/* supplierId set 方法 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/* orderId get 方法 */
	public Long getOrderId() {
		return orderId;
	}

	/* orderId set 方法 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* warehouseNum get 方法 */
	public Integer getWarehouseNum() {
		return warehouseNum;
	}

	/* warehouseNum set 方法 */
	public void setWarehouseNum(Integer warehouseNum) {
		this.warehouseNum = warehouseNum;
	}

	/* warehouseCode get 方法 */
	public String getWarehouseCode() {
		return warehouseCode;
	}

	/* warehouseCode set 方法 */
	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/* warehouseId get 方法 */
	public Long getWarehouseId() {
		return warehouseId;
	}

	/* warehouseId set 方法 */
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* orderCode get 方法 */
	public String getOrderCode() {
		return orderCode;
	}

	/* orderCode set 方法 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* purchaseNum get 方法 */
	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	/* purchaseNum set 方法 */
	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	/* goodsName get 方法 */
	public String getGoodsName() {
		return goodsName;
	}

	/* goodsName set 方法 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}
}
