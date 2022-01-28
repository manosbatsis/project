package com.topideal.json.api.v6_3;

/**
 * 仓储- 调拨单回推
 * 
 * @author 杨创 2018.07.25
 */
public class CCTransferOrderGoodListJson {
	private String goodsNo;// 商品货号
	private Long goodsId;// 商品id
	private String goodsName;// 商品名字
	private String goodsCode;// 商品编码
	private String oldGoodsNo;// 原商品货号
	private Long oldGoodsId;// 原商品id
	private String oldGoodsName;// 原商品id
	private String oldGoodsCode;// 原商品编码
	private Integer num;// 数量	
	private String productionDate;// 生产日期	
	private String overdueDate;	// 到期日期
	private String bathNo;// 批次
	private String oldBarcode;// 原商品条形码	
	private String barcode;// 商品条形码
	private String outTallyingUnit;//P：托盘，C：箱 , B：件
	private String inTallyingUnit;//P：托盘，C：箱 , B：件
	
	
	public String getOutTallyingUnit() {
		return outTallyingUnit;
	}
	public void setOutTallyingUnit(String outTallyingUnit) {
		this.outTallyingUnit = outTallyingUnit;
	}
	public String getInTallyingUnit() {
		return inTallyingUnit;
	}
	public void setInTallyingUnit(String inTallyingUnit) {
		this.inTallyingUnit = inTallyingUnit;
	}
	public String getOldBarcode() {
		return oldBarcode;
	}
	public void setOldBarcode(String oldBarcode) {
		this.oldBarcode = oldBarcode;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	public String getOldGoodsNo() {
		return oldGoodsNo;
	}
	public void setOldGoodsNo(String oldGoodsNo) {
		this.oldGoodsNo = oldGoodsNo;
	}
	public Long getOldGoodsId() {
		return oldGoodsId;
	}
	public void setOldGoodsId(Long oldGoodsId) {
		this.oldGoodsId = oldGoodsId;
	}
	public String getOldGoodsName() {
		return oldGoodsName;
	}
	public void setOldGoodsName(String oldGoodsName) {
		this.oldGoodsName = oldGoodsName;
	}
	public String getOldGoodsCode() {
		return oldGoodsCode;
	}
	public void setOldGoodsCode(String oldGoodsCode) {
		this.oldGoodsCode = oldGoodsCode;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getBathNo() {
		return bathNo;
	}
	public void setBathNo(String bathNo) {
		this.bathNo = bathNo;
	}

}
