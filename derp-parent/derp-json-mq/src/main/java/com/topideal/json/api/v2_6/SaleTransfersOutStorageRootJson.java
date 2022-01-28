package com.topideal.json.api.v2_6;

import java.util.List;
/**
 * 调拨出库 销售订单 回推接口
 * @author lian_
 *
 */
public class SaleTransfersOutStorageRootJson {

	private String orderCode;     //订单号
	private String transferDate;  //调拨时间格式：yyyy-mm-dd HH:mi:ss
	private String code;          //调拨单号
	private String isRookie;      //是否菜鸟字段(1-是，0-否)
	private String topidealCode;  //卓志编码
	
	private List<SaleTransfersOutStorageGoodsListJson> goodsList;
	
	public List<SaleTransfersOutStorageGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<SaleTransfersOutStorageGoodsListJson> goodsList) {
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
	public String getIsRookie() {
		return isRookie;
	}
	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}
	public String getTopidealCode() {
		return topidealCode;
	}
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	
}
