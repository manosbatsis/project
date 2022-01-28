package com.topideal.order.webapi.purchase.form;

import java.io.Serializable;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class DeclareOrderForm extends PageForm implements Serializable {

	@ApiModelProperty(value = "令牌", required = true)
	private String token;
	/**
	 * 预申报单号
	 */
	@ApiModelProperty(value = "预申报单号", required = false)
	private String code;
	/**
	 * 供应商ID
	 */
	@ApiModelProperty(value = "供应商ID", required = false)
	private Long supplierId;
	/**
	 * 仓库ID
	 */
	@ApiModelProperty(value = "仓库ID", required = false)
	private Long depotId;
	/**
	 * 订单状态 001待审核 002审核中 003已审核 006已取消
	 */
	@ApiModelProperty(value = "订单状态", required = false)
	private String status;
	/**
	 * 采购订单编码
	 */
	@ApiModelProperty(value = "采购订单号", required = false)
	private String purchaseCode;
	/**
	 * 预计到岗开始时间
	 */
	@ApiModelProperty(value = "预计到港开始时间", required = false)
	private String arriveStartDate;
	/**
	 * 预计到岗结束时间
	 */
	@ApiModelProperty(value = "预计到港结束时间", required = false)
	private String arriveEndDate;
	/**
	 * 事业部id
	 */
	@ApiModelProperty(value = "事业部ID", required = false)
	private Long buId;

	@ApiModelProperty(value = "预申报单ID,多个以‘,’隔开", required = false)
	private String ids;
	
	@ApiModelProperty(value = "接口状态 0-未推接口；1-已推接口；2-接口推送失败", required = false)
	private String state;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public String getArriveStartDate() {
		return arriveStartDate;
	}

	public void setArriveStartDate(String arriveStartDate) {
		this.arriveStartDate = arriveStartDate;
	}

	public String getArriveEndDate() {
		return arriveEndDate;
	}

	public void setArriveEndDate(String arriveEndDate) {
		this.arriveEndDate = arriveEndDate;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}