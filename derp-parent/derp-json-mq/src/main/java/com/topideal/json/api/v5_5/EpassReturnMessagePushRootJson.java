package com.topideal.json.api.v5_5;

import java.util.List;

/**
 * 退运单信息推送 12103
 * 
 * @author 联想302
 *
 */
public class EpassReturnMessagePushRootJson {

	private String orderCode;// 订单号
	private String transferDate;// 理货时间时间格式：yyyy-mm-dd HH:mi:ss
	private String status;// 单据状态10：制单30：提交70：成功90：作废
	private Long merchantId;// 商家id
	private String merchantName;// 商家名称
	private String topidealCode;// 卓志编码
	private String isRookie;// 是否菜鸟字段(1-是，0-否)
	private List<EpassReturnMessagePushGoodsListJson> goodsList;// 商品集合

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getIsRookie() {
		return isRookie;
	}

	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}

	public List<EpassReturnMessagePushGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<EpassReturnMessagePushGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}

}
