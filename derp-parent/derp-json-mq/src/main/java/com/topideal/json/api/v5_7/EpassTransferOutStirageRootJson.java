package com.topideal.json.api.v5_7;

import java.util.List;

/**
 * 调拨出库回推12093
 * 
 * @author 联想302
 *
 */
public class EpassTransferOutStirageRootJson {

	private String orderCode;// 订单号
	private String transferDate;// 调拨时间格式：yyyy-mm-dd HH:mi:ss
	private String code;// 调拨单号
	private String model;// 业务场景10：账册内调仓20：账册内货号变更30：账册内货号变更调仓40：账册内货权转移50：账册内货权转移调仓60：区内跨海关账册调入70：区内跨海关账册调出80：非实物调拨
	private String serveTypes;// 服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
	private String isRookie;// 是否菜鸟字段(1-是，0-否)
	private Long    merchantId;  //商家id
	private String  merchantName;  //商家名称
	private String  topidealCode;  //卓志编码
	private List<EpassTransferOutStirageGoodsListJson> goodsList;// 商品集合

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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getServeTypes() {
		return serveTypes;
	}

	public void setServeTypes(String serveTypes) {
		this.serveTypes = serveTypes;
	}

	public String getIsRookie() {
		return isRookie;
	}

	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}

	public List<EpassTransferOutStirageGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<EpassTransferOutStirageGoodsListJson> goodsList) {
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
