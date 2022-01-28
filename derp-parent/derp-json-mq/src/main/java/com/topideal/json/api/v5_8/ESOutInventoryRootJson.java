package com.topideal.json.api.v5_8;

import java.util.List;

/**
 *出库明细
 */
public class ESOutInventoryRootJson {
	
	private String orderCode;// 订单号
	private String externalCode;// 外部单号
	private String deliverDate;// 发货时间
	private Long   merchantId;  //商家id
	private String merchantName;  //商家名称
	private String topidealCode;  //卓志编码
	private List<ESOutInventoryGoodsListJson> goodsList;// 商品集合
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	public String getDeliverDate() {
		return deliverDate;
	}
	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}
	public List<ESOutInventoryGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<ESOutInventoryGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}

}
