package com.topideal.json.api.v2_7;

import java.util.List;
/**
 * 调拨出库 销售退货订单 回推接口
 * @author lian_
 *
 */
public class SaleReturnTransfersOutStorageRootJson {

	private String orderCode;     //订单号
	private String transferDate;  //调拨时间格式：yyyy-mm-dd HH:mi:ss
	private String code;          //调拨单号
	private String isRookie;      //是否菜鸟字段(1-是，0-否)
	private String topidealCode;  //卓志编码
	private String model;//业务场景 账册内货权转移/账册内货权转移调仓 必填	
	private String serveTypes;//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务'必填
	private String remark;//备注
	private Long merchantId ;
	
	private List<SaleReturnTransfersOutStorageGoodsListJson> goodsList;
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public List<SaleReturnTransfersOutStorageGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<SaleReturnTransfersOutStorageGoodsListJson> goodsList) {
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
