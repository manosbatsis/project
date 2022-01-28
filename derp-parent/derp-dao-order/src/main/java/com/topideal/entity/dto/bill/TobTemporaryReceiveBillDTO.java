package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class TobTemporaryReceiveBillDTO extends PageModel implements Serializable{

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("事业部ID")
    private Long buId;

    @ApiModelProperty("事业部名称")
    private String buName;

    @ApiModelProperty("商家ID")
    private Long merchantId;

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty("客户id")
    private Long customerId;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("销售订单号")
    private String orderCode;

    @ApiModelProperty("上架单号")
    private String shelfCode;

    @ApiModelProperty("销售币种")
    private String currency;

    @ApiModelProperty("销售类型")
    private String saleType;
    @ApiModelProperty("销售类型中文")
    private String saleTypeLabel;

    @ApiModelProperty("po号")
    private String poNo;

    @ApiModelProperty("上架时间")
    private Timestamp shelfDate;

    @ApiModelProperty("应收账单号")
    private String receiveCode;

    @ApiModelProperty("应收结算状态")
    private String status;
    @ApiModelProperty("应收结算状态中文")
    private String statusLabel;

    @ApiModelProperty("应收金额")
    private BigDecimal receiveAmount;

    @ApiModelProperty("上架计提应收金额")
    private BigDecimal shelfAmount;

    @ApiModelProperty("上架计提返利金额")
    private BigDecimal shelfRebateAmount;

    @ApiModelProperty("已核销暂估应收金额")
    private BigDecimal verifyAmount;

    @ApiModelProperty("已核销暂估返利金额")
    private BigDecimal verifyRebateAmount;

    @ApiModelProperty("未核销暂估应收金额")
    private BigDecimal nonVerifyAmount;

    @ApiModelProperty("未核销暂估返利金额")
    private BigDecimal nonVerifyRebateAmount;

    @ApiModelProperty("出账单计划日期")
    private Timestamp outBillPlanDate;

    @ApiModelProperty("出账单实际日期")
    private Timestamp outBillRealDate;

    @ApiModelProperty("出账单日期预警")
    private String outBillWarn;

    @ApiModelProperty("账单确认计划日期")
    private Timestamp confirmPlanDate;

    @ApiModelProperty("账单确认实际日期")
    private Timestamp confirmRealDate;

    @ApiModelProperty("账单确认日期预警")
    private String confirmWarn;

    @ApiModelProperty("开票计划日期")
    private Timestamp invoicingPlanDate;

    @ApiModelProperty("开票实际日期")
    private Timestamp invoicingRealDate;

    @ApiModelProperty("开票日期预警")
    private String invoicingWarn;

    @ApiModelProperty("回款计划日期")
    private Timestamp paymentPlanDate;

    @ApiModelProperty("回款实际日期")
    private Timestamp paymentRealDate;

    @ApiModelProperty("回款日期预警")
    private String paymentWarn;

    @ApiModelProperty("回款计划周期")
    private Integer paymentPlanPeriod;

    @ApiModelProperty("回款实际周期")
    private Integer paymentRealPeriod;

    @ApiModelProperty("回款周期预警")
    private String paymentPeriodWarn;

    @ApiModelProperty("创建时间")
    private Timestamp createDate;

    @ApiModelProperty("修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "id集合,多个用英文逗号隔开")
    private String ids;

    @ApiModelProperty("关联应收单id")
    private Long receiveId;

    @ApiModelProperty("上架月份")
    private String shelfMonth;

    /**
     * 返利结算状态 1-已上架未结算 2-部分结算 5-已结算
     */
    @ApiModelProperty("返利结算状态")
    private String rebateStatus;
    private String rebateStatusLabel;

    /**
     * 单据类型 1-上架单 2-销售退货订单
     */
    @ApiModelProperty("单据类型 1-上架单 2-销售退货订单")
    private String orderType;

    @ApiModelProperty(value = "上架单号，多个以逗号隔开", hidden = true)
    private String shelfCodes;

    @ApiModelProperty(value = "0-购销退货 1-非购销退货", hidden = true)
    private String billType;
    @ApiModelProperty(value = "当前登录用户绑定的事业部集合")
    private List<Long> buList;

    @ApiModelProperty("是否红冲单：0-否，1-是")
    private String isWriteOff;

    @ApiModelProperty("原上架号")
    private String originalShelfCode;

    public String getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(String rebateStatus) {
        this.rebateStatus = rebateStatus;
        this.rebateStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tobTempReceiveBill_statusList, rebateStatus);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
        this.saleTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tobTempReceiveBill_saleTypeList, saleType);
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Timestamp getShelfDate() {
        return shelfDate;
    }

    public void setShelfDate(Timestamp shelfDate) {
        this.shelfDate = shelfDate;
    }

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tobTempReceiveBill_statusList, status);
    }

    public BigDecimal getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(BigDecimal receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public BigDecimal getShelfAmount() {
        return shelfAmount;
    }

    public void setShelfAmount(BigDecimal shelfAmount) {
        this.shelfAmount = shelfAmount;
    }

    public BigDecimal getShelfRebateAmount() {
        return shelfRebateAmount;
    }

    public void setShelfRebateAmount(BigDecimal shelfRebateAmount) {
        this.shelfRebateAmount = shelfRebateAmount;
    }

    public Timestamp getOutBillPlanDate() {
        return outBillPlanDate;
    }

    public void setOutBillPlanDate(Timestamp outBillPlanDate) {
        this.outBillPlanDate = outBillPlanDate;
    }

    public Timestamp getOutBillRealDate() {
        return outBillRealDate;
    }

    public void setOutBillRealDate(Timestamp outBillRealDate) {
        this.outBillRealDate = outBillRealDate;
    }

    public String getOutBillWarn() {
        return outBillWarn;
    }

    public void setOutBillWarn(String outBillWarn) {
        this.outBillWarn = outBillWarn;
    }

    public void setConfirmWarn(String confirmWarn) {
        this.confirmWarn = confirmWarn;
    }

    public Timestamp getInvoicingPlanDate() {
        return invoicingPlanDate;
    }

    public void setInvoicingPlanDate(Timestamp invoicingPlanDate) {
        this.invoicingPlanDate = invoicingPlanDate;
    }

    public Timestamp getInvoicingRealDate() {
        return invoicingRealDate;
    }

    public void setInvoicingRealDate(Timestamp invoicingRealDate) {
        this.invoicingRealDate = invoicingRealDate;
    }

    public String getInvoicingWarn() {
        return invoicingWarn;
    }

    public void setInvoicingWarn(String invoicingWarn) {
        this.invoicingWarn = invoicingWarn;
    }

    public Timestamp getPaymentPlanDate() {
        return paymentPlanDate;
    }

    public void setPaymentPlanDate(Timestamp paymentPlanDate) {
        this.paymentPlanDate = paymentPlanDate;
    }

    public Timestamp getPaymentRealDate() {
        return paymentRealDate;
    }

    public void setPaymentRealDate(Timestamp paymentRealDate) {
        this.paymentRealDate = paymentRealDate;
    }

    public String getPaymentWarn() {
        return paymentWarn;
    }

    public void setPaymentWarn(String paymentWarn) {
        this.paymentWarn = paymentWarn;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getPaymentPlanPeriod() {
        return paymentPlanPeriod;
    }

    public void setPaymentPlanPeriod(Integer paymentPlanPeriod) {
        this.paymentPlanPeriod = paymentPlanPeriod;
    }

    public Integer getPaymentRealPeriod() {
        return paymentRealPeriod;
    }

    public void setPaymentRealPeriod(Integer paymentRealPeriod) {
        this.paymentRealPeriod = paymentRealPeriod;
    }

    public String getPaymentPeriodWarn() {
        return paymentPeriodWarn;
    }

    public void setPaymentPeriodWarn(String paymentPeriodWarn) {
        this.paymentPeriodWarn = paymentPeriodWarn;
    }

    public Timestamp getConfirmPlanDate() {
        return confirmPlanDate;
    }

    public void setConfirmPlanDate(Timestamp confirmPlanDate) {
        this.confirmPlanDate = confirmPlanDate;
    }

    public Timestamp getConfirmRealDate() {
        return confirmRealDate;
    }

    public void setConfirmRealDate(Timestamp confirmRealDate) {
        this.confirmRealDate = confirmRealDate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getConfirmWarn() {
        return confirmWarn;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public String getShelfMonth() {
        return shelfMonth;
    }

    public void setShelfMonth(String shelfMonth) {
        this.shelfMonth = shelfMonth;
    }

    public BigDecimal getVerifyAmount() {
        return verifyAmount;
    }

    public void setVerifyAmount(BigDecimal verifyAmount) {
        this.verifyAmount = verifyAmount;
    }

    public BigDecimal getVerifyRebateAmount() {
        return verifyRebateAmount;
    }

    public void setVerifyRebateAmount(BigDecimal verifyRebateAmount) {
        this.verifyRebateAmount = verifyRebateAmount;
    }

    public BigDecimal getNonVerifyAmount() {
        return nonVerifyAmount;
    }

    public void setNonVerifyAmount(BigDecimal nonVerifyAmount) {
        this.nonVerifyAmount = nonVerifyAmount;
    }

    public BigDecimal getNonVerifyRebateAmount() {
        return nonVerifyRebateAmount;
    }

    public void setNonVerifyRebateAmount(BigDecimal nonVerifyRebateAmount) {
        this.nonVerifyRebateAmount = nonVerifyRebateAmount;
    }

    public String getSaleTypeLabel() {
        return saleTypeLabel;
    }

    public void setSaleTypeLabel(String saleTypeLabel) {
        this.saleTypeLabel = saleTypeLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getRebateStatusLabel() {
        return rebateStatusLabel;
    }

    public void setRebateStatusLabel(String rebateStatusLabel) {
        this.rebateStatusLabel = rebateStatusLabel;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getShelfCodes() {
        return shelfCodes;
    }

    public void setShelfCodes(String shelfCodes) {
        this.shelfCodes = shelfCodes;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    public String getOriginalShelfCode() {
        return originalShelfCode;
    }

    public void setOriginalShelfCode(String originalShelfCode) {
        this.originalShelfCode = originalShelfCode;
    }
}
