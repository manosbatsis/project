package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.SaleCreditOrderItemDTO;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

public class SaleCreditOrderForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售订单id")
    private Long saleOrderId;

	@ApiModelProperty(value = "销售订单编号")
    private String saleOrderCode;

	@ApiModelProperty(value = "赊销单号")
    private String code;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "赊销金额")
    private BigDecimal creditAmount;

	@ApiModelProperty(value = "应收保证金")
    private BigDecimal payableMarginAmount;

	@ApiModelProperty(value = "收保证金日期")
    private String receiveMarginDate;

	@ApiModelProperty(value = "放款日期")
    private String loanDate;

	@ApiModelProperty(value = "起息日期")
    private String valueDate;

	@ApiModelProperty(value = "还款日期")
    private String receiveDate;

	@ApiModelProperty(value = "订单状态:001:待提交,002:待收保证金,003:待放款,004:赊销中,005:待收款,006-已删除，007-已收款")
    private String status;

	@ApiModelProperty(value = "权责月份 yyyy-MM")
	private String ownMonth;
	@ApiModelProperty(value = "商品表体集合")
	private List<SaleCreditOrderItemDTO> itemList;

	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 滞纳金减免金额
	 */
	@ApiModelProperty(value = "滞纳金减免金额")
	private BigDecimal discountDelayAmount;
	/**
	 * 减免原因
	 */
	@ApiModelProperty(value = "减免原因")
	private String discountReason;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public BigDecimal getPayableMarginAmount() {
		return payableMarginAmount;
	}

	public void setPayableMarginAmount(BigDecimal payableMarginAmount) {
		this.payableMarginAmount = payableMarginAmount;
	}

	public String getReceiveMarginDate() {
		return receiveMarginDate;
	}

	public void setReceiveMarginDate(String receiveMarginDate) {
		this.receiveMarginDate = receiveMarginDate;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<SaleCreditOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleCreditOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOwnMonth() {
		return ownMonth;
	}

	public void setOwnMonth(String ownMonth) {
		this.ownMonth = ownMonth;
	}

	public BigDecimal getDiscountDelayAmount() {
		return discountDelayAmount;
	}

	public void setDiscountDelayAmount(BigDecimal discountDelayAmount) {
		this.discountDelayAmount = discountDelayAmount;
	}

	public String getDiscountReason() {
		return discountReason;
	}

	public void setDiscountReason(String discountReason) {
		this.discountReason = discountReason;
	}
}
