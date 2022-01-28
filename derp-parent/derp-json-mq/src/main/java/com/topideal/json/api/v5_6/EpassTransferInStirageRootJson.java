package com.topideal.json.api.v5_6;

import java.util.List;

/**
 * 调拨入库回推1208
 * 
 * @author 联想302
 *
 */
public class EpassTransferInStirageRootJson {

	private String orderCode;// 订单号
	private String transferDate;// 调拨时间格式：yyyy-mm-dd HH:mi:ss
	private String code;// 调拨单号
	private String remark;// 备注
	private String isRookie;// 是否菜鸟字段(1-是，0-否)
	private Long    merchantId;  //商家id
	private String  merchantName;  //商家名称
	private String  topidealCode;  //卓志编码
	private List<EpassTransferInStirageGoodsListJson> goodsList;// 商品集合

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsRookie() {
		return isRookie;
	}

	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}

	public List<EpassTransferInStirageGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<EpassTransferInStirageGoodsListJson> goodsList) {
		this.goodsList = goodsList;
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

	
}
