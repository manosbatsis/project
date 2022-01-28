package com.topideal.entity.dto.purchase;
import java.sql.Timestamp;

/**
 * 勾稽明细导出
 * 
 * @author zhanghx
 */
public class PurchaseWarehouseExportBean {

	//入库单id
	private Long id;
	// 入库单
	private String warehouseCode;
	// 商品货号
	private String goodsNo;
	// 是否勾稽
	private String isArticulation;
	// 勾稽数量
	private Integer num;
    // 未勾稽数量
	private Integer num2;
	// 采购订单号
	private String purchaseCode;
	// 失效时间
	private Timestamp overdueDate;
	// 批次号
	private String batchNo;
	// 生产日期
	private Timestamp productionDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getIsArticulation() {
		return isArticulation;
	}

	public void setIsArticulation(String isArticulation) {
		this.isArticulation = isArticulation;
	}

	public Integer getNum2() {
		return num2;
	}

	public void setNum2(Integer num2) {
		this.num2 = num2;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public Timestamp getOverdueDate() {
		return overdueDate;
	}

	public void setOverdueDate(Timestamp overdueDate) {
		this.overdueDate = overdueDate;
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

	
}
