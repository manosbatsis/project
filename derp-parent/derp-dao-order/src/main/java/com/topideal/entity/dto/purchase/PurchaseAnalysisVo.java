package com.topideal.entity.dto.purchase;

import java.io.Serializable;
import java.util.List;

/**
 * 采购入库勾稽明细
 * @author zhanghx
 */
public class PurchaseAnalysisVo implements Serializable {

	// 采购订单号
	private String purchaseCode;
	// 商品货号
	private String goodsNo;
	// 商品名称
	private String goodsName;
	// 采购数量
	private Integer purchaseNum;
	
	// 表体
	private List<PurchaseAnalysisItemVo> itemList;

	public List<PurchaseAnalysisItemVo> getItemList() {
		return itemList;
	}

	public void setItemList(List<PurchaseAnalysisItemVo> itemList) {
		this.itemList = itemList;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

}
