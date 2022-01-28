package com.topideal.json.api.v2_5;

import java.util.List;
/**
 * 调拨入库
 * @author lian_
 *
 */
public class SaleTransfersInStorageRootJson {
	
	private String orderCode;      //订单号
	private String transferDate;   //调拨时间格式：yyyy-mm-dd HH:mi:ss
	private String code;           //单号
	private String remark;         //备注
	private String isRookie;       //是否菜鸟字段(1-是，0-否)
	private String merchantId;     //商家id
	private String merchantName;   //商家名称
	private String topidealCode;   //商家卓志编码
	
	private List<SaleTransfersInStorageGoodsListJson> goodsList;
	
	public List<SaleTransfersInStorageGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<SaleTransfersInStorageGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}
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
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
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
