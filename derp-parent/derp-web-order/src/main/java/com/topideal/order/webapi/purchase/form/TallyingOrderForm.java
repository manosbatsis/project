package com.topideal.order.webapi.purchase.form;

import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 理货单form
 * 
 * @author gy
 */
@ApiModel
public class TallyingOrderForm extends PageForm {

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	// 理货单号
	@ApiModelProperty(value = "理货单号",required = false)
	private String code;
	// 合同号
	@ApiModelProperty(value = "合同号",required = false)
	private String contractNo;
	// 仓库id
	@ApiModelProperty(value = "仓库id",required = false)
	private Long depotId;
	// 订单编号
	@ApiModelProperty(value = "预申报单号",required = false)
	private String orderCode;
	
	// 状态 009:待确认 010:已确认 004:已驳回
	@ApiModelProperty(value = "状态",required = false)
	private String state;
	// 拓展字段
	// 理货开始时间
	@ApiModelProperty(value = "理货开始时间",required = false)
	private String tallyingStartDate;
	// 理货结束时间
	@ApiModelProperty(value = "理货结束时间",required = false)
	private String tallyingEndDate;
	/**
	 *  事业部id
	 */
	private Long buId;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getTallyingStartDate() {
		return tallyingStartDate;
	}
	public void setTallyingStartDate(String tallyingStartDate) {
		this.tallyingStartDate = tallyingStartDate;
	}
	public String getTallyingEndDate() {
		return tallyingEndDate;
	}
	public void setTallyingEndDate(String tallyingEndDate) {
		this.tallyingEndDate = tallyingEndDate;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}