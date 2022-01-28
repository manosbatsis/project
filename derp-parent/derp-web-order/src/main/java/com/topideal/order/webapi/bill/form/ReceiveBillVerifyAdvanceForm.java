package com.topideal.order.webapi.bill.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 应收账单核销预收单Dto
 */
@ApiModel
public class ReceiveBillVerifyAdvanceForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = false)
    private String token;

    @ApiModelProperty(value = "预收账单id", required = false)
    private Long advanceId;

    @ApiModelProperty(value = "预收账单明细id", required = false)
    private Long advanceItemId;

    @ApiModelProperty(value = "预收账单号", required = false)
    private String code;

    @ApiModelProperty(value = "商家id", required = false)
    private Long merchantId;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "结算币种", required = false)
    private String currency;

    @ApiModelProperty(value = "账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-待作废 06-已作废", required = false)
    private String billStatus;
    private String billStatusLabel;

    @ApiModelProperty(value = "po单号", required = false)
    private String poNo;

    @ApiModelProperty(value = "销售单号", required = false)
    private String orderCode;

    @ApiModelProperty(value = "关联应收id", hidden = false)
    private Long receiveBillId;

    @ApiModelProperty(value = "预收金额", hidden = false)
    private BigDecimal amount;

    @ApiModelProperty(value = "核销时间", hidden = false)
    private Timestamp verifyDate;

    @ApiModelProperty(value = "收款日期", hidden = false)
    private Timestamp receiveDate;

    @ApiModelProperty(value = "核销人ID", hidden = false)
    private Long verifyId;

    @ApiModelProperty(value = "核销人名称", hidden = false)
    private String verifier;

    @ApiModelProperty(value = "已选择的核销预收id集合，多个以英文逗号隔开", hidden = false)
    private String ids;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getAdvanceItemId() {
        return advanceItemId;
    }

    public void setAdvanceItemId(Long advanceItemId) {
        this.advanceItemId = advanceItemId;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
        this.billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_billStatusList, billStatus);
    }

    public String getBillStatusLabel() {
        return billStatusLabel;
    }

    public void setBillStatusLabel(String billStatusLabel) {
        this.billStatusLabel = billStatusLabel;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Long getReceiveBillId() {
        return receiveBillId;
    }

    public void setReceiveBillId(Long receiveBillId) {
        this.receiveBillId = receiveBillId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(Long advanceId) {
        this.advanceId = advanceId;
    }

    public Timestamp getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Timestamp verifyDate) {
        this.verifyDate = verifyDate;
    }

    public Long getVerifyId() {
        return verifyId;
    }

    public void setVerifyId(Long verifyId) {
        this.verifyId = verifyId;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public Timestamp getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Timestamp receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
