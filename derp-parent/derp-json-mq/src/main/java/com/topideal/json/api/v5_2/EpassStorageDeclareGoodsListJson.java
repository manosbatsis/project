package com.topideal.json.api.v5_2;

import java.util.List;

/**
 * MQ入库申报接口 12023-调拨
 * 
 * @author 联想302
 *
 */
public class EpassStorageDeclareGoodsListJson {

	private String goodsCode;// 商品编码
	private Long goodsId;// 商品id
	private String goodsName;// 商品名称
	private String goodsNo;// 商品货号
	private String tallyingUnit;//理货单位 00：托盘，01：箱 , 02：件
	private String barcode;//条形码
	private List<EpassStorageDeclareReceiveListJson> receiveList;// 上级goodsList

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
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

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public List<EpassStorageDeclareReceiveListJson> getReceiveList() {
		return receiveList;
	}

	public void setReceiveList(List<EpassStorageDeclareReceiveListJson> receiveList) {
		this.receiveList = receiveList;
	}

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
   
}
