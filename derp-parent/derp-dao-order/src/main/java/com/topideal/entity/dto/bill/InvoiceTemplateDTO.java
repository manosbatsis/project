package com.topideal.entity.dto.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 开票模板dto
 * @Author: Chen Yiluan
 * @Date: 2021/09/07 18:19
 **/
@ApiModel
public class InvoiceTemplateDTO implements Serializable {

    /**
     * 发票模板id
     */
    @ApiModelProperty("发票模板id")
    private Long fileTempId;
    /**
     * 发票模板编码
     */
    @ApiModelProperty("发票模板编码")
    private String fileTempCode;
    /**
     * 公司id
     */
    @ApiModelProperty("公司id")
    private Long merchantId;
    /**
     * 公司name
     */
    @ApiModelProperty("公司名")
    private String merchantName;
    /**
     * 公司发票头
     */
    @ApiModelProperty("公司发票头")
    private String merchantInvoiceName;

    @ApiModelProperty("公司英文名称")
    private String merchantEnglishName;
    /**
     * 发票注册地址
     */
    @ApiModelProperty("发票注册地址")
    private String englishRegisteredAddress;
    /**
     * 发票时间
     */
    @ApiModelProperty("发票时间")
    private String invoiceDate;

    @ApiModelProperty("账单月份")
    private String billMonth;
    /**
     * po号
     */
    @ApiModelProperty("po号")
    private String poNos;

    @ApiModelProperty("应收账单单号，多个以逗号隔开")
    private String codes;

    @ApiModelProperty("应收账单id，多个以逗号隔开")
    private String ids;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;
    /**
     * 客户id
     */
    @ApiModelProperty("客户id")
    private Long customerId;
    /**
     * 客户英文名称
     */
    @ApiModelProperty("客户英文名称")
    private String customerEnName;

    /**
     * 企业英文地址
     */
    @ApiModelProperty("客户英文名称")
    private String enBusinessAddress;
    /**
     * 客户名称
     */
    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("开户银行(客户)")
    private String cusDepositBank;

    @ApiModelProperty("银行帐号(客户)")
    private String cusBankAccount;
    /**
     * 开户银行
     */
    @ApiModelProperty("开户银行")
    private String depositBank;
    /**
     * 开户行地址
     */
    @ApiModelProperty("开户行地址")
    private String bankAddress;
    /**
     * 银行账户
     */
    @ApiModelProperty("银行账户")
    private String beneficiaryName;
    /**
     * 银行账号
     */
    @ApiModelProperty("银行账号")
    private String bankAccount;
    /**
     * swiftCode
     */
    @ApiModelProperty("swiftCode")
    private String swiftCode;
    /**
     * 币种
     */
    @ApiModelProperty("币种")
    private String currency;
    /**
     * 发票总金额
     */
    @ApiModelProperty("发票总金额")
    private String totalAllAmount;

    @ApiModelProperty("客退补贴总金额")
    private String customerReturnAll;

    @ApiModelProperty("客退折让总金额")
    private String promotionDiscountsCustAll;

    @ApiModelProperty("活动折扣总金额")
    private String promotionDiscountsAll;

    @ApiModelProperty("补偿折扣总金额")
    private String extraDiscountAll;

    @ApiModelProperty("返利金额总金额")
    private String extraAmountAll;

    @ApiModelProperty("发票总数量")
    private String totalAllNum;
    /**
     * 公司注册地址
     */
    @ApiModelProperty("公司注册地址")
    private String companyAddr;

    @ApiModelProperty("发票来源 1-应收账单 2-平台结算单 ")
    private String invoiceStatus;

    @ApiModelProperty("from联系人")
    private String fContactPerson;

    @ApiModelProperty("to联系人")
    private String tContactPerson;

    @ApiModelProperty("买方发票号")
    private String buyerInvoiceNo;

    @ApiModelProperty("付款条件英文")
    private String paymentEN;

    @ApiModelProperty("付款条件中文")
    private String paymentCN;

    @ApiModelProperty("贸易方式英文")
    private String incotermsEN;

    @ApiModelProperty("贸易方式中文")
    private String incotermsCN;

    @ApiModelProperty("原产地英文")
    private String placeEN;

    @ApiModelProperty("原产地中文")
    private String placeCN;

    @ApiModelProperty("税号")
    private String taxNo;

    @ApiModelProperty("联系方式")
    private String oTelNo;

    @ApiModelProperty("应收单的关联业务单据号")
    private String relCodes;

    @ApiModelProperty("账单日期")
    private String billDate;

    @ApiModelProperty("customerCode")
    private String customerCode;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("装货港")
    private String originCity;
    @ApiModelProperty("卸货港")
    private String destinationCity;

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("发票明细")
    private List<InvoiceTemplateItemDTO> itemList;

    @ApiModelProperty("唯品发票明细")
    private List<InvoiceWeiPinTemplateItemDTO> wpItemList;

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

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantInvoiceName() {
        return merchantInvoiceName;
    }

    public void setMerchantInvoiceName(String merchantInvoiceName) {
        this.merchantInvoiceName = merchantInvoiceName;
    }

    public String getEnglishRegisteredAddress() {
        return englishRegisteredAddress;
    }

    public void setEnglishRegisteredAddress(String englishRegisteredAddress) {
        this.englishRegisteredAddress = englishRegisteredAddress;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getPoNos() {
        return poNos;
    }

    public void setPoNos(String poNos) {
        this.poNos = poNos;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEnName() {
        return customerEnName;
    }

    public void setCustomerEnName(String customerEnName) {
        this.customerEnName = customerEnName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotalAllAmount() {
        return totalAllAmount;
    }

    public void setTotalAllAmount(String totalAllAmount) {
        this.totalAllAmount = totalAllAmount;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public List<InvoiceTemplateItemDTO> getItemList() {
        return itemList;
    }

    public void setItemList(List<InvoiceTemplateItemDTO> itemList) {
        this.itemList = itemList;
    }

    public String getTotalAllNum() {
        return totalAllNum;
    }

    public void setTotalAllNum(String totalAllNum) {
        this.totalAllNum = totalAllNum;
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

    public List<InvoiceWeiPinTemplateItemDTO> getWpItemList() {
        return wpItemList;
    }

    public void setWpItemList(List<InvoiceWeiPinTemplateItemDTO> wpItemList) {
        this.wpItemList = wpItemList;
    }

    public String getMerchantEnglishName() {
        return merchantEnglishName;
    }

    public void setMerchantEnglishName(String merchantEnglishName) {
        this.merchantEnglishName = merchantEnglishName;
    }

    public String getEnBusinessAddress() {
        return enBusinessAddress;
    }

    public void setEnBusinessAddress(String enBusinessAddress) {
        this.enBusinessAddress = enBusinessAddress;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getCustomerReturnAll() {
        return customerReturnAll;
    }

    public void setCustomerReturnAll(String customerReturnAll) {
        this.customerReturnAll = customerReturnAll;
    }

    public String getPromotionDiscountsCustAll() {
        return promotionDiscountsCustAll;
    }

    public void setPromotionDiscountsCustAll(String promotionDiscountsCustAll) {
        this.promotionDiscountsCustAll = promotionDiscountsCustAll;
    }

    public String getPromotionDiscountsAll() {
        return promotionDiscountsAll;
    }

    public void setPromotionDiscountsAll(String promotionDiscountsAll) {
        this.promotionDiscountsAll = promotionDiscountsAll;
    }

    public String getExtraDiscountAll() {
        return extraDiscountAll;
    }

    public void setExtraDiscountAll(String extraDiscountAll) {
        this.extraDiscountAll = extraDiscountAll;
    }

    public String getExtraAmountAll() {
        return extraAmountAll;
    }

    public void setExtraAmountAll(String extraAmountAll) {
        this.extraAmountAll = extraAmountAll;
    }

    public String getfContactPerson() {
        return fContactPerson;
    }

    public void setfContactPerson(String fContactPerson) {
        this.fContactPerson = fContactPerson;
    }

    public String gettContactPerson() {
        return tContactPerson;
    }

    public void settContactPerson(String tContactPerson) {
        this.tContactPerson = tContactPerson;
    }

    public String getBuyerInvoiceNo() {
        return buyerInvoiceNo;
    }

    public void setBuyerInvoiceNo(String buyerInvoiceNo) {
        this.buyerInvoiceNo = buyerInvoiceNo;
    }

    public String getPaymentEN() {
        return paymentEN;
    }

    public void setPaymentEN(String paymentEN) {
        this.paymentEN = paymentEN;
    }

    public String getPaymentCN() {
        return paymentCN;
    }

    public void setPaymentCN(String paymentCN) {
        this.paymentCN = paymentCN;
    }

    public String getIncotermsEN() {
        return incotermsEN;
    }

    public void setIncotermsEN(String incotermsEN) {
        this.incotermsEN = incotermsEN;
    }

    public String getIncotermsCN() {
        return incotermsCN;
    }

    public void setIncotermsCN(String incotermsCN) {
        this.incotermsCN = incotermsCN;
    }

    public String getPlaceEN() {
        return placeEN;
    }

    public void setPlaceEN(String placeEN) {
        this.placeEN = placeEN;
    }

    public String getPlaceCN() {
        return placeCN;
    }

    public void setPlaceCN(String placeCN) {
        this.placeCN = placeCN;
    }

    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getoTelNo() {
        return oTelNo;
    }

    public void setoTelNo(String oTelNo) {
        this.oTelNo = oTelNo;
    }

    public String getRelCodes() {
        return relCodes;
    }

    public void setRelCodes(String relCodes) {
        this.relCodes = relCodes;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCusDepositBank() {
        return cusDepositBank;
    }

    public void setCusDepositBank(String cusDepositBank) {
        this.cusDepositBank = cusDepositBank;
    }

    public String getCusBankAccount() {
        return cusBankAccount;
    }

    public void setCusBankAccount(String cusBankAccount) {
        this.cusBankAccount = cusBankAccount;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
