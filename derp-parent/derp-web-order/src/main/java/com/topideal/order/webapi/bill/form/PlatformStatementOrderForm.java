package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class PlatformStatementOrderForm extends PageForm implements Serializable {

	@ApiModelProperty(value = "token", required = true)
	private String token;

	@ApiModelProperty(value = "平台账单号")
	private String billCode;

	@ApiModelProperty(value = "客户类型")
	private String customerType;

	@ApiModelProperty(value = "账单月份")
	private String month;

	@ApiModelProperty(value = "应收结算单号")
    private String receiveCode;

	@ApiModelProperty(value = "id集合,多个用英文逗号隔开")
	private String ids;

	@ApiModelProperty(value = "平台结算单id")
	private Long platformStatementOrderId;

	@ApiModelProperty(value = "是否开票 0-否 1-是")
	private String isInvoice;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public Long getPlatformStatementOrderId() {
		return platformStatementOrderId;
	}

	public void setPlatformStatementOrderId(Long platformStatementOrderId) {
		this.platformStatementOrderId = platformStatementOrderId;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(String isInvoice) {
		this.isInvoice = isInvoice;
	}
}
