package com.topideal.json.api.v2_1;

import java.util.List;

/**
 * 销售退货 入库申报   商品推mq报文
 * @author lian_
 *
 */
public class SaleStorageDeclareGoodsListJson {
	

	private String goodsCode; //商品编码
	private Long goodsId;//商品id
	private String goodsName;//商品名称
	private String goodsNo;//商品货号
	private String barcode;//条码
	private String commbarcode;//标准条码
	
	private List<SaleStorageDeclareBatchListIson> receiveList;
	
	
	public List<SaleStorageDeclareBatchListIson> getReceiveList() {
		return receiveList;
	}
	public void setReceiveList(List<SaleStorageDeclareBatchListIson> receiveList) {
		this.receiveList = receiveList;
	}
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
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCommbarcode() {
		return commbarcode;
	}
	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}


	
}
