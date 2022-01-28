package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SaleCreditOrderExportDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "赊销单号")
    private String code;

	@ApiModelProperty(value = "商家名称")
    private String merchantName;

	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
	
	@ApiModelProperty(value = "po单号")
    private String poNo;
	
	@ApiModelProperty(value = "赊销金额")
    private BigDecimal creditAmount;
	
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

	@ApiModelProperty(value = "还款日期")
    private Timestamp receiveDate;

	@ApiModelProperty(value = "收款本金")
    private BigDecimal receivePrincipalAmount;

	@ApiModelProperty(value = "收款利息")
    private BigDecimal receiveInterestAmount;
	
	@ApiModelProperty(value = "银行账户")
    private String beneficiaryName;
	
	@ApiModelProperty(value = "银行账号")
    private String bankAccount;
	
	@ApiModelProperty(value = "开户银行")
    private String depositBank;
	
	@ApiModelProperty(value = "开户行地址")
    private String bankAddress;
	
	@ApiModelProperty(value = "swiftCode")
    private String swiftCode;

	@ApiModelProperty(value = "赊销账单集合")
	private List<SaleCreditBillOrderDTO> itemList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public List<SaleCreditBillOrderDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleCreditBillOrderDTO> itemList) {
		this.itemList = itemList;
	}

}
