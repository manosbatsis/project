package com.topideal.order.webapi.purchase.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
public class PurchaseReturnOdepotForm extends PageForm implements Serializable {

	@ApiModelProperty(value = "令牌", required = true)
    private String token;
	/**
	 * 采购退货出库订单号
	 */
	@ApiModelProperty(value = "采购退货出库订单号", required = false)
	private String code;
	/**
	 * 采购退订单号
	 */
	@ApiModelProperty(value = "采购退订单号", required = false)
	private String purchaseReturnCode;
	/**
	 * 退出仓库id
	 */
	@ApiModelProperty(value = "退出仓库id", required = false)
	private Long outDepotId;
	/**
	 * 出库时间
	 */
	@ApiModelProperty(value = "出库时间", required = false)
	private Timestamp returnOutDate;
	/**
	 * 状态：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结
	 */
	@ApiModelProperty(value = "状态：001-待审核 ,003-已审核 ,015:待出仓,016:已出仓,027:出库中 ,007已完结", required = false)
	private String status;
	/**
	 * 客户id
	 */
	@ApiModelProperty(value = "供应商id", required = false)
	private Long supplierId;
	/**
	 * 事业部id
	 */
	@ApiModelProperty(value = "事业部id", required = false)
	private Long buId;
	/**
	 * po号
	 */
	@ApiModelProperty(value = "po号", required = false)
	private String poNo;

	/**
	 * 出库时间
	 */
	@ApiModelProperty(value = "出库时间开始", required = false)
	private String returnOutStartDate;
	@ApiModelProperty(value = "出库时间结束", required = false)
	private String returnOutEndDate;

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* purchaseReturnCode get 方法 */
	public String getPurchaseReturnCode() {
		return purchaseReturnCode;
	}

	/* purchaseReturnCode set 方法 */
	public void setPurchaseReturnCode(String purchaseReturnCode) {
		this.purchaseReturnCode = purchaseReturnCode;
	}

	/* outDepotId get 方法 */
	public Long getOutDepotId() {
		return outDepotId;
	}

	/* outDepotId set 方法 */
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	/* returnOutDate get 方法 */
	public Timestamp getReturnOutDate() {
		return returnOutDate;
	}

	/* returnOutDate set 方法 */
	public void setReturnOutDate(Timestamp returnOutDate) {
		this.returnOutDate = returnOutDate;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* supplierId get 方法 */
	public Long getSupplierId() {
		return supplierId;
	}

	/* supplierId set 方法 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/* buId get 方法 */
	public Long getBuId() {
		return buId;
	}

	/* buId set 方法 */
	public void setBuId(Long buId) {
		this.buId = buId;
	}

	/* poNo get 方法 */
	public String getPoNo() {
		return poNo;
	}

	/* poNo set 方法 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getReturnOutStartDate() {
		return returnOutStartDate;
	}

	public void setReturnOutStartDate(String returnOutStartDate) {
		this.returnOutStartDate = returnOutStartDate;
	}

	public String getReturnOutEndDate() {
		return returnOutEndDate;
	}

	public void setReturnOutEndDate(String returnOutEndDate) {
		this.returnOutEndDate = returnOutEndDate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
