package com.topideal.json.api.v6_2;

/**
 * 仓储-自营库存更新
 * 
 * @author 杨创 2018.7.25
 */
public class CCSelfInventoryUpdateGoodsListJson {

	private String goodsNo;// 商品货号
	private Long goodsId;// 商品id
	private String goodsName;// 商品名称
	private String goodsCode;// 商品编码
	private String barcode;// 货品条码
	private String goodsBatch;// 商品批次号
	private String isDamage;// 是否坏品 1：好品，2：坏品
	private String productionDate;// 生产日期
	private String overdueDate;// 失效日期
	private String oldProductionDate;// 原生产日期
	private String oldOverdueDate;// 原失效日期
	private Integer num;// 数量（绝对值）
	private String storeQty;// 数量（正数为增加，负数为减少）
	private String tallyingUnit;//P：托盘，C：箱 , B：件
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getGoodsBatch() {
		return goodsBatch;
	}
	public void setGoodsBatch(String goodsBatch) {
		this.goodsBatch = goodsBatch;
	}
	public String getIsDamage() {
		return isDamage;
	}
	public void setIsDamage(String isDamage) {
		this.isDamage = isDamage;
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
	public String getOldProductionDate() {
		return oldProductionDate;
	}
	public void setOldProductionDate(String oldProductionDate) {
		this.oldProductionDate = oldProductionDate;
	}
	public String getOldOverdueDate() {
		return oldOverdueDate;
	}
	public void setOldOverdueDate(String oldOverdueDate) {
		this.oldOverdueDate = oldOverdueDate;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getStoreQty() {
		return storeQty;
	}
	public void setStoreQty(String storeQty) {
		this.storeQty = storeQty;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	
	

}
