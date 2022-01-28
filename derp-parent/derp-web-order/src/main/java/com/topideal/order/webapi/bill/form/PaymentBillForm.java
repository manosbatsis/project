package com.topideal.order.webapi.bill.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PaymentBillForm extends PageForm {

	@ApiModelProperty("票据")
	private String token ;
    /**
    * id主键
    */
	@ApiModelProperty("id主键")
    private Long id;
	@ApiModelProperty("paymentIds")
	private String paymentIds;
    /**
    * 应付账单号
    */
	@ApiModelProperty("应付账单号")
    private String code;
    /**
    * 商家id
    */
	@ApiModelProperty("商家id")
    private Long merchantId;
    /**
    * 供应商id
    */
	@ApiModelProperty("供应商id")
    private Long supplierId;
    /**
    * 事业部ID
    */
	@ApiModelProperty("事业部ID")
    private Long buId;
    /**
    * 账单状态 00-待提交、01-审核中、02-已驳回、03-待付款、04-已付款、05-待作废、06-已作废
    */
	@ApiModelProperty("账单状态00-待提交、01-审核中、02-已驳回、03-待付款、04-已付款、05-待作废、06-已作废")
    private String billStatus;
    /**
    * NC状态 1-未同步、2-已同步、3-待审核、4-待入erp、5-待入账、6-已入账、7-已关账
    */
    @ApiModelProperty("NC状态 1-未同步、2-已同步、3-待审核、4-待入erp、5-待入账、6-已入账、7-已关账")
    private String ncStatus;
    /**
     * 预计付款日期
     */
 	@ApiModelProperty("预计付款日期开始")
     private String expectedPaymentDateStart;

 	@ApiModelProperty("预计付款日期结束")
    private String expectedPaymentDateEnd;

	@ApiModelProperty("打印状态 1：已打印 0：未打印")
	private String printingState;

	@ApiModelProperty("po单号")
	private String poNo;

	public String getPrintingState() {
		return printingState;
	}

	public void setPrintingState(String printingState) {
		this.printingState = printingState;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getNcStatus() {
		return ncStatus;
	}

	public void setNcStatus(String ncStatus) {
		this.ncStatus = ncStatus;
	}

	public String getExpectedPaymentDateStart() {
		return expectedPaymentDateStart;
	}

	public void setExpectedPaymentDateStart(String expectedPaymentDateStart) {
		this.expectedPaymentDateStart = expectedPaymentDateStart;
	}

	public String getExpectedPaymentDateEnd() {
		return expectedPaymentDateEnd;
	}

	public void setExpectedPaymentDateEnd(String expectedPaymentDateEnd) {
		this.expectedPaymentDateEnd = expectedPaymentDateEnd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPaymentIds() {
		return paymentIds;
	}

	public void setPaymentIds(String paymentIds) {
		this.paymentIds = paymentIds;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
}
