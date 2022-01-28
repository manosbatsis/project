package com.topideal.json.api.v5_5;

/**
 * 退运单信息推送 12103
 * 
 * @author 联想302
 *
 */
public class EpassReturnMessagePushGoodsListJson {

	private String goodsNo;// 商品编码
	private Long goodsId;// 商品id
	private String goodsName;// 商品名称
	private String goodsCode;// 商品编码
	private String barcode;// 商品条形码
	private Integer num;// 退运数量
	private String batchNo;// 退运批次
	private String productionDate;// 生产日期
	private String overdueDate;// 失效日期

	public String getGoodsNo() {
		return goodsNo;
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


}
