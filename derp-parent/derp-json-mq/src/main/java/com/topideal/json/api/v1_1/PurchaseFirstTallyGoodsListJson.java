package com.topideal.json.api.v1_1;

import java.util.List;

/**
 * 初理货 商品推mq报文
 * 
 * @author zhanghx
 */
public class PurchaseFirstTallyGoodsListJson {

	// 商品编码
	private String goodsNo;
	// 商品id
	private Long goodsId;
	// 商品名称
	private String goodsName;
	// 商品编码
	private String goodsCode;
	// 商品条形码
	private String barcode;
	// 申报数量(应收数量)取采购单数
	private Integer purchaseNum;
	// 理货数量（规则：理货数量=申报数量-缺失数量+多货数量）实收数量
	private Integer tallyingNum;
	// 缺失数量（默认0）
	private Integer lackNum;
	// 多货数量
	private Integer multiNum;
	// 总数量（正常数量）
	private Integer totoalNormalNum;
	// 产品毛重
	private Double grossWeight;
	// 产品净重
	private Double netWeight;
	// 产品体积
	private Double volume;
	// 产品长
	private Double length;
	// 产品宽
	private Double width;
	// 产品高
	private Double height;
	// 理货单位
	private String tallyingUnit;

	private List<PurchaseFirstTallyBatchListJson> receiveList;

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public Integer getTallyingNum() {
		return tallyingNum;
	}

	public void setTallyingNum(Integer tallyingNum) {
		this.tallyingNum = tallyingNum;
	}

	public Integer getLackNum() {
		return lackNum;
	}

	public void setLackNum(Integer lackNum) {
		this.lackNum = lackNum;
	}

	public Integer getMultiNum() {
		return multiNum;
	}

	public void setMultiNum(Integer multiNum) {
		this.multiNum = multiNum;
	}

	public Integer getTotoalNormalNum() {
		return totoalNormalNum;
	}

	public void setTotoalNormalNum(Integer totoalNormalNum) {
		this.totoalNormalNum = totoalNormalNum;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public List<PurchaseFirstTallyBatchListJson> getReceiveList() {
		return receiveList;
	}

	public void setReceiveList(List<PurchaseFirstTallyBatchListJson> receiveList) {
		this.receiveList = receiveList;
	}

}
