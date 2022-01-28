package com.topideal.json.api.v2_4;

import java.util.List;
/**
 * 退运信息接口 
 * @author lian_
 *
 */
public class SaleReturnMessagePushRootJson {
	
	private String orderCode;     //订单号
	private String transferDate;  //理货时间时间格式：yyyy-mm-dd HH:mi:ss
	private String status;        //单据状态10：制单30：提交70：成功90：作废
	private String topidealCode;  //卓志编码
	private String isRookie;      //是否菜鸟字段(1-是，0-否)
	
	private List<SaleReturnMessagePushGoodsListJson> goodsList;
	
	
	public List<SaleReturnMessagePushGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<SaleReturnMessagePushGoodsListJson> goodsList) {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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

	
}
