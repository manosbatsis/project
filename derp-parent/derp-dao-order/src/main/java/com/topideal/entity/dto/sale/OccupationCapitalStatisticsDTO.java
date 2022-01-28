package com.topideal.entity.dto.sale;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class OccupationCapitalStatisticsDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "赊销单号")
    private  String creditOrderCode;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "事业部ID")
    private Long buId;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "权责月份")
    private String ownMonth;

    @ApiModelProperty(value = "卓普信放款时间")
    private Timestamp sapienceLoanDate;

    @ApiModelProperty(value = "po单号")
    private String poNo;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;

    @ApiModelProperty(value = "赊销金额")
    private BigDecimal creditAmount;

    @ApiModelProperty(value = "起息日期")
    private Timestamp valueDate;

    @ApiModelProperty(value = "实收保证金")
    private BigDecimal actualMarginAmount;

    @ApiModelProperty(value = "收保证金日期")
    private Timestamp receiveMarginDate;

    @ApiModelProperty(value = "到期日期")
    private Timestamp expireDate;

    @ApiModelProperty(value = "垫付金额")
    private BigDecimal advancedAmount;

    @ApiModelProperty(value="回款本金")
    private BigDecimal principalAmount;

    @ApiModelProperty(value="回款日期")
    private Timestamp receiveDate;

    @ApiModelProperty(value = "垫付余额")
    private BigDecimal advancedRestAmount;

    @ApiModelProperty(value = "利息")
    private BigDecimal interest;

    @ApiModelProperty(value = "逾期天数")
    private Integer overdueDays;

    @ApiModelProperty(value = "逾期费用")
    private BigDecimal overdueAmount;

    @ApiModelProperty(value = "资金占用天数")
    private Integer occupationDays;

    @ApiModelProperty(value = "资金占用费率")
    private BigDecimal occupationRate;

    @ApiModelProperty(value = "资金占用费")
    private BigDecimal occupationAmount;

    @ApiModelProperty(value = "毛利")
    private BigDecimal grossProfit;

    @ApiModelProperty(value = "已回款本金")
    private BigDecimal receivePrincipalAmount;

    @ApiModelProperty(value = "滞纳金")
    private BigDecimal delayAmount;

    @ApiModelProperty(value = "滞纳减免金额")
    private BigDecimal discountDelayAmount;

    @ApiModelProperty(value = "事业部集合")
    private List<Long> buList;

    public String getCreditOrderCode() {
        return creditOrderCode;
    }

    public void setCreditOrderCode(String creditOrderCode) {
        this.creditOrderCode = creditOrderCode;
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

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
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

    public Timestamp getValueDate() {
        return valueDate;
    }

    public void setValueDate(Timestamp valueDate) {
        this.valueDate = valueDate;
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

    public Timestamp getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
    }

    public BigDecimal getAdvancedAmount() {
        return advancedAmount;
    }

    public void setAdvancedAmount(BigDecimal advancedAmount) {
        this.advancedAmount = advancedAmount;
    }

    public BigDecimal getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(BigDecimal principalAmount) {
        this.principalAmount = principalAmount;
    }

    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    public BigDecimal getAdvancedRestAmount() {
        return advancedRestAmount;
    }

    public void setAdvancedRestAmount(BigDecimal advancedRestAmount) {
        this.advancedRestAmount = advancedRestAmount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public BigDecimal getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(BigDecimal overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public Integer getOccupationDays() {
        return occupationDays;
    }

    public void setOccupationDays(Integer occupationDays) {
        this.occupationDays = occupationDays;
    }

    public BigDecimal getOccupationRate() {
        return occupationRate;
    }

    public void setOccupationRate(BigDecimal occupationRate) {
        this.occupationRate = occupationRate;
    }

    public BigDecimal getOccupationAmount() {
        return occupationAmount;
    }

    public void setOccupationAmount(BigDecimal occupationAmount) {
        this.occupationAmount = occupationAmount;
    }

    public BigDecimal getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit) {
        this.grossProfit = grossProfit;
    }

    public BigDecimal getReceivePrincipalAmount() {
        return receivePrincipalAmount;
    }

    public void setReceivePrincipalAmount(BigDecimal receivePrincipalAmount) {
        this.receivePrincipalAmount = receivePrincipalAmount;
    }

    public BigDecimal getDelayAmount() {
        return delayAmount;
    }

    public void setDelayAmount(BigDecimal delayAmount) {
        this.delayAmount = delayAmount;
    }

    public BigDecimal getDiscountDelayAmount() {
        return discountDelayAmount;
    }

    public void setDiscountDelayAmount(BigDecimal discountDelayAmount) {
        this.discountDelayAmount = discountDelayAmount;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}
