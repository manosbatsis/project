package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * toc 保存发票参数
 */
@ApiModel
public class TocReceiveBillInvoiceForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    private String codes;

    @ApiModelProperty(value = "ids",required = false)
    private String ids;

    @ApiModelProperty(value = "模板id",required = false)
    private Long fileTempId;

    @ApiModelProperty(value = "模板code",required = false)
    private String fileTempCode;

    @ApiModelProperty(value = "币种",required = false)
    private String currency;

    @ApiModelProperty(value = "客户id",required = false)
    private Long customerId;

    @ApiModelProperty(value = "客户名称",required = false)
    private String customerName;

    @ApiModelProperty(value = "月份",required = false)
    private String billMonth;

    @ApiModelProperty(value = "总金额",required = false)
    private BigDecimal totalAllAmount;

    @ApiModelProperty(value = "商家名称",required = false)
    private String merchantName;

    @ApiModelProperty(value = "开户银行",required = false)
    private String depositBank;

    @ApiModelProperty(value = "银行账号",required = false)
    private String bankAccount;

    @ApiModelProperty(value = "SWIFT CODE",required = false)
    private String swiftCode;

    @ApiModelProperty(value = "goodsList",required = false)
    private List<TocReceiveBillGoodsForm> goodsList;

    @ApiModelProperty("发票号")
    private String invoiceNo;

    @ApiModelProperty("发票来源 1-应收账单 2-平台结算单 ")
    private String invoiceStatus;

    /**
     * 公司发票头
     */
    @ApiModelProperty("公司发票头")
    private String merchantInvoiceName;

    @ApiModelProperty("公司英文名称")
    private String merchantEnglishName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCodes() {
        return codes;
    }

    public void setCodes(String codes) {
        this.codes = codes;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public Long getFileTempId() {
        return fileTempId;
    }

    public void setFileTempId(Long fileTempId) {
        this.fileTempId = fileTempId;
    }

    public String getFileTempCode() {
        return fileTempCode;
    }

    public void setFileTempCode(String fileTempCode) {
        this.fileTempCode = fileTempCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public BigDecimal getTotalAllAmount() {
        return totalAllAmount;
    }

    public void setTotalAllAmount(BigDecimal totalAllAmount) {
        this.totalAllAmount = totalAllAmount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public List<TocReceiveBillGoodsForm> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<TocReceiveBillGoodsForm> goodsList) {
        this.goodsList = goodsList;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getMerchantInvoiceName() {
        return merchantInvoiceName;
    }

    public void setMerchantInvoiceName(String merchantInvoiceName) {
        this.merchantInvoiceName = merchantInvoiceName;
    }

    public String getMerchantEnglishName() {
        return merchantEnglishName;
    }

    public void setMerchantEnglishName(String merchantEnglishName) {
        this.merchantEnglishName = merchantEnglishName;
    }
}
