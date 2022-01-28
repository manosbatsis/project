package com.topideal.json.pushapi.ofc.c01;

/**
 * OFC 海外系统实时库存查询
 * @author 杨创
 *2018/6/25
 */
public class OFCCurrentStoreRoot {
	
	private String customerId;// 卓志内部客户编号(电商平台编号)
	private String electricCode;//	电商企业编码（卓志提供）
	private String goodsId="*";// 商品货号（多个以，号隔开），输入* 则查该企业名下全商品
	private String authorizationCode;//授权码
	private String warehouseId;//仓库（只能逐个仓库查询）
	private String pageNo;//页数
	private String pageSize;//分页大小
	private String serveTypes;//业务模式字符串：10:B2B,  20:B2C,,30:C2C
	//private String foundpCode;//资金方(默认请填写电商企业编码)
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getElectricCode() {
		return electricCode;
	}
	public void setElectricCode(String electricCode) {
		this.electricCode = electricCode;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	public String getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getPageNo() {
		return pageNo;
	}
	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getServeTypes() {
		return serveTypes;
	}
	public void setServeTypes(String serveTypes) {
		this.serveTypes = serveTypes;
	}
	
}