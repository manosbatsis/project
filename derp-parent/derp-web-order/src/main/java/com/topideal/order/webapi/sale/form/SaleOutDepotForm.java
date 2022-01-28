package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel
public class SaleOutDepotForm extends PageForm{
	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "调出仓库id")
	private String outDepotId;
	@ApiModelProperty(value = "销售类型 1购销 2代销")
	private String saleType;
	@ApiModelProperty(value = "客户id")
	private String customerId;
	@ApiModelProperty(value = "PO号")
	private String poNo;
	@ApiModelProperty(value = "状态 017-待出库,018-已出库,025-已签收,026-已上架,027-出库中 006-已删除,028-部分上架")
	private String status;
	@ApiModelProperty(value = "销售订单编号")
	private String saleOrderCode;
	@ApiModelProperty(value = "出库清单编号")
	private String code;
	@ApiModelProperty(value = "事业部id")
	private String buId;
	@ApiModelProperty(value = "发货结束时间")
	private String deliverEndDate;
	@ApiModelProperty(value = "发货开始时间")
	private String deliverStartDate;
	@ApiModelProperty(value = "唯品账单号")
	private String vipsBillNo;
	@ApiModelProperty(value = "LBX单号")
	private String lbxNo;
	@ApiModelProperty(value = "商品名称/货号/条码（用于查询）")
	private String goodsStr;
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
	@ApiModelProperty(value = "销售订单id")
	private Long saleOrderId;
	@ApiModelProperty(value = "销售预申报单id")
	private Long saleDeclareOrderId;
	@ApiModelProperty(value = "是否红冲单：0-否，1-是")
	private String isWriteOff;
	@ApiModelProperty(value = "销售预申报单单号")
	private String saleDeclareOrderCode;

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(String outDepotId) {
		this.outDepotId = outDepotId;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSaleOrderCode() {
		return saleOrderCode;
	}
	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
	public String getDeliverEndDate() {
		return deliverEndDate;
	}
	public void setDeliverEndDate(String deliverEndDate) {
		this.deliverEndDate = deliverEndDate;
	}
	public String getDeliverStartDate() {
		return deliverStartDate;
	}
	public void setDeliverStartDate(String deliverStartDate) {
		this.deliverStartDate = deliverStartDate;
	}
	public String getVipsBillNo() {
		return vipsBillNo;
	}
	public void setVipsBillNo(String vipsBillNo) {
		this.vipsBillNo = vipsBillNo;
	}
	public String getLbxNo() {
		return lbxNo;
	}
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}
	public String getGoodsStr() {
		return goodsStr;
	}
	public void setGoodsStr(String goodsStr) {
		this.goodsStr = goodsStr;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Long getSaleOrderId() {
		return saleOrderId;
	}
	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}
	public Long getSaleDeclareOrderId() {
		return saleDeclareOrderId;
	}
	public void setSaleDeclareOrderId(Long saleDeclareOrderId) {
		this.saleDeclareOrderId = saleDeclareOrderId;
	}

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
	}

	public String getSaleDeclareOrderCode() {
		return saleDeclareOrderCode;
	}

	public void setSaleDeclareOrderCode(String saleDeclareOrderCode) {
		this.saleDeclareOrderCode = saleDeclareOrderCode;
	}
}
