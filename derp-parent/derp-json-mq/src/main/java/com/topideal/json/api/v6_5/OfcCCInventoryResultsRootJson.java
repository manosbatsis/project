package com.topideal.json.api.v6_5;

import java.util.List;

import com.topideal.json.api.v6_1.CCInventoryResultsGoodsListJson;

/**
 * 仓储-盘点结果实体类
 * @author 杨创
 *  2018.7.25
 */
public class OfcCCInventoryResultsRootJson {
	private String inventoryCode;// 盘点单号
	private String orderCode;// 订单号	
	private String status;// 00 初始化 20 已发布 30 审核中 40 审核通过 60 审核未通 99 完成  90 取消 (默认99)
	private Long merchantId;// 商家id	
	private String merchantName;// 商家名称
	private String topidealCode;// 卓志编码
	private String depoCode;// 仓库编码
	private Long depoId;// 仓库id
	private String depoName;// 仓库名称
	private String depoType;// 仓库类型
	private String isTopBooks;// 是否是代销仓
	private String serverType;// 服务类型字符串 10：客服盘点申请 20：仓库自行盘点  30：前端盘点申请	
	private String model;// 业务场景字符串 10：个性盘点  20：自主盘点
	private String email;// 商家邮箱
	private String galFinishTime;// 损溢单完成时间 既发货时间
	private String inventoryResultEmail;// 盘点结果邮箱
	
	private List<OfcCCInventoryResultsGoodsListJson> goodsList;// 商品信息

	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public String getDepoCode() {
		return depoCode;
	}

	public void setDepoCode(String depoCode) {
		this.depoCode = depoCode;
	}

	public Long getDepoId() {
		return depoId;
	}

	public void setDepoId(Long depoId) {
		this.depoId = depoId;
	}

	public String getDepoName() {
		return depoName;
	}

	public void setDepoName(String depoName) {
		this.depoName = depoName;
	}

	public String getDepoType() {
		return depoType;
	}

	public void setDepoType(String depoType) {
		this.depoType = depoType;
	}

	public String getIsTopBooks() {
		return isTopBooks;
	}

	public void setIsTopBooks(String isTopBooks) {
		this.isTopBooks = isTopBooks;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInventoryResultEmail() {
		return inventoryResultEmail;
	}

	public void setInventoryResultEmail(String inventoryResultEmail) {
		this.inventoryResultEmail = inventoryResultEmail;
	}

	public List<OfcCCInventoryResultsGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<OfcCCInventoryResultsGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}

	public String getGalFinishTime() {
		return galFinishTime;
	}

	public void setGalFinishTime(String galFinishTime) {
		this.galFinishTime = galFinishTime;
	}
	
}
