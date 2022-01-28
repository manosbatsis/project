package com.topideal.report.webapi.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SettlementBillForm extends PageForm{

	@ApiModelProperty(value = "令牌")
    private String token;

	@ApiModelProperty(value = "结算单号")
	private String code;

	@ApiModelProperty(value = "公司ID")
	private Long merchantId;

	@ApiModelProperty(value = "仓库ID")
	private Long depotId;

	@ApiModelProperty(value = "结算对象id")
	private Long customerId;

	@ApiModelProperty(value = "账单月份")
	private String month;

	@ApiModelProperty(value = "状态：1-生成中 2-已生成 3-生成失败 4-已确认")
	private String status;

	@ApiModelProperty(value = "确认状态")
	private String confirmStatus ;

	@ApiModelProperty(value = "平台编码")
	private String platformCode;

	@ApiModelProperty(value = "计费周期开始时间")
	private String checkStartDate ;
	@ApiModelProperty(value = "计费周期结束时间")
	private String checkEndDate ;

	@ApiModelProperty(value = "币种")
	private String currency;

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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getCheckStartDate() {
		return checkStartDate;
	}

	public void setCheckStartDate(String checkStartDate) {
		this.checkStartDate = checkStartDate;
	}

	public String getCheckEndDate() {
		return checkEndDate;
	}

	public void setCheckEndDate(String checkEndDate) {
		this.checkEndDate = checkEndDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
