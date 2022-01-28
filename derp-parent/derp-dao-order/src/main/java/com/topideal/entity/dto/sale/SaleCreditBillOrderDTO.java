package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SaleCreditBillOrderDTO extends PageModel implements Serializable{ 

	@ApiModelProperty(value="id")
    private Long id;

	@ApiModelProperty(value="赊销单id")
    private Long creditOrderId;

	@ApiModelProperty(value=" 赊销单编号")
    private String creditOrderCode;

	@ApiModelProperty(value="赊销收款单编号")
    private String code;

	@ApiModelProperty(value="客户ID(供应商)")
    private Long customerId;

	@ApiModelProperty(value="客户名称")
    private String customerName;

	@ApiModelProperty(value="商家ID")
    private Long merchantId;

	@ApiModelProperty(value="商家名称")
    private String merchantName;

	@ApiModelProperty(value="po单号")
    private String poNo;

	@ApiModelProperty(value="币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;

	@ApiModelProperty(value="事业部id")
    private Long buId;

	@ApiModelProperty(value="事业部名称")
    private String buName;

	@ApiModelProperty(value="收款本金")
    private BigDecimal principalAmount;

	@ApiModelProperty(value="保证金")
    private BigDecimal marginAmount;

	@ApiModelProperty(value="资金占用费")
    private BigDecimal occupationAmount;

	@ApiModelProperty(value="代理费")
    private BigDecimal agencyAmount;

	@ApiModelProperty(value="滞纳金")
    private BigDecimal delayAmount;

	@ApiModelProperty(value="应收款金额")
    private BigDecimal receivableAmount;

	@ApiModelProperty(value="收款日期")
    private Timestamp receiveDate;

	@ApiModelProperty(value="到账日期")
    private Timestamp accountDate;

	@ApiModelProperty(value="备注")
    private String remark;

	@ApiModelProperty(value="001:待收款，002-已收款，006-已删除")
    private String status;

	@ApiModelProperty(value="创建人")
    private Long creater;

	@ApiModelProperty(value="创建人用户名")
    private String createName;

	@ApiModelProperty(value="创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value="修改人")
    private Long modifier;

	@ApiModelProperty(value="修改人用户名")
    private String modifierName;

	@ApiModelProperty(value="修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value="商品表体信息")
    private List<SaleCreditBillOrderItemDTO> itemList;
	
	@ApiModelProperty(value = "计息天数")
    private Integer interestDay;

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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreditOrderId() {
		return creditOrderId;
	}

	public void setCreditOrderId(Long creditOrderId) {
		this.creditOrderId = creditOrderId;
	}

	public String getCreditOrderCode() {
		return creditOrderCode;
	}

	public void setCreditOrderCode(String creditOrderCode) {
		this.creditOrderCode = creditOrderCode;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}

	public void setPrincipalAmount(BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getMarginAmount() {
		return marginAmount;
	}

	public void setMarginAmount(BigDecimal marginAmount) {
		this.marginAmount = marginAmount;
	}

	public BigDecimal getOccupationAmount() {
		return occupationAmount;
	}

	public void setOccupationAmount(BigDecimal occupationAmount) {
		this.occupationAmount = occupationAmount;
	}

	public BigDecimal getAgencyAmount() {
		return agencyAmount;
	}

	public void setAgencyAmount(BigDecimal agencyAmount) {
		this.agencyAmount = agencyAmount;
	}

	public BigDecimal getDelayAmount() {
		return delayAmount;
	}

	public void setDelayAmount(BigDecimal delayAmount) {
		this.delayAmount = delayAmount;
	}

	public BigDecimal getReceivableAmount() {
		return receivableAmount;
	}

	public void setReceivableAmount(BigDecimal receivableAmount) {
		this.receivableAmount = receivableAmount;
	}

	public Timestamp getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Timestamp getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(Timestamp accountDate) {
		this.accountDate = accountDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public List<SaleCreditBillOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleCreditBillOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public Integer getInterestDay() {
		return interestDay;
	}

	public void setInterestDay(Integer interestDay) {
		this.interestDay = interestDay;
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
