package com.topideal.json.api.v5_1;

import java.util.List;

/**
 * MQ初理货接口 goodsList
 * 
 * @author 联想302
 *
 */
public class EpassFirstTallyGoodsListJson {

	private Long goodsId;          // 商品id
	private String goodsNo;		   //商品货号
	private String goodsName;      // 商品名称
	private String barcode;        // 商品条形码
	private int purchaseNum;       // 申报数量(应收数量)取采购单数
	private int tallyingNum;       // 理货数量（规则：理货数量=申报数量-缺失数量+多货数量）实收数量
	private int lackNum;           // 缺失数量（默认0）
	private int multiNum;          // 多货数量
	private int totoalNormalNum;   // 总数量（正常数量）
	private Double grossWeight;    // 产品毛重
	private Double netWeight;      // 产品净重
	private Double volume;         // 产品体积
	private Double length;         // 产品长
	private Double width;          // 产品宽
	private Double height;         // 产品高
	private List<EpassFirstTallyReceiveListJson> receiveList;//上级goodsList
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public int getPurchaseNum() {
		return purchaseNum;
	}
	public void setPurchaseNum(int purchaseNum) {
		this.purchaseNum = purchaseNum;
	}
	public int getTallyingNum() {
		return tallyingNum;
	}
	public void setTallyingNum(int tallyingNum) {
		this.tallyingNum = tallyingNum;
	}
	public int getLackNum() {
		return lackNum;
	}
	public void setLackNum(int lackNum) {
		this.lackNum = lackNum;
	}
	public int getMultiNum() {
		return multiNum;
	}
	public void setMultiNum(int multiNum) {
		this.multiNum = multiNum;
	}
	public int getTotoalNormalNum() {
		return totoalNormalNum;
	}
	public void setTotoalNormalNum(int totoalNormalNum) {
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
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public List<EpassFirstTallyReceiveListJson> getReceiveList() {
		return receiveList;
	}
	public void setReceiveList(List<EpassFirstTallyReceiveListJson> receiveList) {
		this.receiveList = receiveList;
	}

	
	

}
