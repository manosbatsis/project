package com.topideal.json.api.v6_1;

import java.util.List;

/**
 * 仓储-盘点结果实体类
 * @author 杨创
 *  2018.7.25
 */
public class CCInventoryResultsRootJson {
	private String inventoryCode;// 盘点单号
	private String orderCode;// 订单号
	private String updateDate;// 录入日期格式：yyyy-mm-dd
	private String status;// 单据状态 字符串10：制单30：提交70：成功90：作废
	private Long merchantId;// 商家id
	private String merchantName;// 商家名称
	private String topidealCode;// 卓志编码
	private String depoCode;// 仓库编码
	private String serverType;// 服务类型10：个性盘点 20：自主盘点
	private String model;// 业务场景10：客服盘点申请20：仓库自行盘点30：前端盘点申请
	private String dismissRemark;// 驳回原因	
	private String email;// 商家邮箱
	private String inventoryResultEmail;// 盘点结果邮箱
	
	private List<CCInventoryResultsGoodsListJson> goodsList;// 商品信息
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
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
	public String getDismissRemark() {
		return dismissRemark;
	}
	public void setDismissRemark(String dismissRemark) {
		this.dismissRemark = dismissRemark;
	}
	public List<CCInventoryResultsGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<CCInventoryResultsGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}
	public String getInventoryCode() {
		return inventoryCode;
	}
	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
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
	
	

}
