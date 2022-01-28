package com.topideal.entity.dto.sale;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.sale.SaleCreditBillOrderModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
@ApiModel
public class SaleCreditOrderDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "销售单id")
    private Long saleOrderId;

	@ApiModelProperty(value = "销售订单编号")
    private String saleOrderCode;

	@ApiModelProperty(value = "赊销单号")
    private String code;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;

	@ApiModelProperty(value = "客户名称")
    private String customerName;

	@ApiModelProperty(value = "商家ID")
    private Long merchantId;

	@ApiModelProperty(value = "商家名称")
    private String merchantName;

	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "赊销金额")
    private BigDecimal creditAmount;

	@ApiModelProperty(value = "赊销总数量")
    private Integer totalNum;

	@ApiModelProperty(value = "应收保证金")
    private BigDecimal payableMarginAmount;

	@ApiModelProperty(value = "实收保证金")
    private BigDecimal actualMarginAmount;

	@ApiModelProperty(value = "收保证金日期")
    private Timestamp receiveMarginDate;

	@ApiModelProperty(value = "放款日期")
    private Timestamp loanDate;

	@ApiModelProperty(value = "起息日期")
    private Timestamp valueDate;

	@ApiModelProperty(value = "到期日期")
    private Timestamp expireDate;

	@ApiModelProperty(value = "收款日期")
    private Timestamp receiveDate;

	@ApiModelProperty(value = "收款本金")
    private BigDecimal receivePrincipalAmount;

	@ApiModelProperty(value = "收款利息")
    private BigDecimal receiveInterestAmount;

	@ApiModelProperty(value = "订单状态:001:待提交,002:待收保证金,003:待放款,004:赊销中,005:待收款,006-已删除，007-已收款")
    private String status;
	@ApiModelProperty(value = "订单状态（中文）")
    private String statusLabel;

	@ApiModelProperty(value = "是否同步金融系统 0：未同步，1：已同步，默认值：0")
    private String isSyncFinance;

	@ApiModelProperty(value = "同步金融系统时间")
    private Timestamp syncDate;

	@ApiModelProperty(value = "创建人")
    private Long creater;

	@ApiModelProperty(value = "创建人用户名")
    private String createName;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改人")
    private Long modifier;

	@ApiModelProperty(value = "修改人用户名")
    private String modifierName;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;

	@ApiModelProperty(value = "商品表体集合")
	private List<SaleCreditOrderItemDTO> itemList;

	@ApiModelProperty(value = "赊销账单集合")
	private List<SaleCreditBillOrderModel> billList;

	@ApiModelProperty(value = "剩余天数")
    private Integer stayDays;

	/**
	 * 权责月份
	 */
	@ApiModelProperty(value = "权责月份")
	private String ownMonth;
	/**
	 * 卓普信放款时间
	 */
	@ApiModelProperty(value = "卓普信放款时间")
	private Timestamp sapienceLoanDate;

	@ApiModelProperty(value = "是否存在收款单，0-不存在，1-存在")
	private String isExistBillOrder;

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

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public BigDecimal getPayableMarginAmount() {
		return payableMarginAmount;
	}

	public void setPayableMarginAmount(BigDecimal payableMarginAmount) {
		this.payableMarginAmount = payableMarginAmount;
	}

	public BigDecimal getActualMarginAmount() {
		return actualMarginAmount;
	}

	public void setActualMarginAmount(BigDecimal actualMarginAmount) {
		this.actualMarginAmount = actualMarginAmount;
	}

	public Timestamp getReceiveMarginDate() {
		return receiveMarginDate;
	}

	public void setReceiveMarginDate(Timestamp receiveMarginDate) {
		this.receiveMarginDate = receiveMarginDate;
	}

	public Timestamp getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(Timestamp loanDate) {
		this.loanDate = loanDate;
	}

	public Timestamp getValueDate() {
		return valueDate;
	}

	public void setValueDate(Timestamp valueDate) {
		this.valueDate = valueDate;
	}

	public Timestamp getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Timestamp expireDate) {
		this.expireDate = expireDate;
	}

	public Timestamp getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}

	public BigDecimal getReceivePrincipalAmount() {
		return receivePrincipalAmount;
	}

	public void setReceivePrincipalAmount(BigDecimal receivePrincipalAmount) {
		this.receivePrincipalAmount = receivePrincipalAmount;
	}

	public BigDecimal getReceiveInterestAmount() {
		return receiveInterestAmount;
	}

	public void setReceiveInterestAmount(BigDecimal receiveInterestAmount) {
		this.receiveInterestAmount = receiveInterestAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		if(StringUtils.isNotBlank(status)) {
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleCredit_statusList, status);
		}
	}

	public String getStatusLabel() {
		return statusLabel;
	}
	public String getIsSyncFinance() {
		return isSyncFinance;
	}

	public void setIsSyncFinance(String isSyncFinance) {
		this.isSyncFinance = isSyncFinance;
	}

	public Timestamp getSyncDate() {
		return syncDate;
	}

	public void setSyncDate(Timestamp syncDate) {
		this.syncDate = syncDate;
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

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}

	public List<SaleCreditOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleCreditOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public List<SaleCreditBillOrderModel> getBillList() {
		return billList;
	}

	public void setBillList(List<SaleCreditBillOrderModel> billList) {
		this.billList = billList;
	}

	public Integer getStayDays() {
		return stayDays;
	}

	public void setStayDays(Integer stayDays) {
		this.stayDays = stayDays;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getOwnMonth() {
		return ownMonth;
	}

	public void setOwnMonth(String ownMonth) {
		this.ownMonth = ownMonth;
	}

	public Timestamp getSapienceLoanDate() {
		return sapienceLoanDate;
	}

	public void setSapienceLoanDate(Timestamp sapienceLoanDate) {
		this.sapienceLoanDate = sapienceLoanDate;
	}

	public String getIsExistBillOrder() {
		return isExistBillOrder;
	}

	public void setIsExistBillOrder(String isExistBillOrder) {
		this.isExistBillOrder = isExistBillOrder;
	}
}
