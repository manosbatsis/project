package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 提交nc应收账单信息DTO
 **/
@ApiModel
public class ReceiveBillToNCDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "账单号",required = true)
    private String billCode;

    @ApiModelProperty(value = "事业部id",required = true)
    private Long buId;

    @ApiModelProperty(value = "事业部名称",required = true)
    private String buName;

    @ApiModelProperty(value = "结算币种",required = true)
    private String currency;
    private String currencyLabel;
    /**
     * NC渠道编码
     */
    @ApiModelProperty(value = "结算币种",required = true)
    private String ncChannelCode ;
    private String ncChannelName ;

    @ApiModelProperty(value = "结算类型 1-应收,2-预收",required = true)
    private String settlementType ;
    private String settlementTypeLabel ;

    @ApiModelProperty(value = "销售模式 1-代销 2-购销",required = true)
    private String saleModel ;
    private String saleModelLabel ;

    @ApiModelProperty(value = "应收账单Id",required = true)
    private Long billId;

    @ApiModelProperty(value = "系统业务单号",required = true)
    private String code;

    @ApiModelProperty(value = "po单号",required = true)
    private String poNo;

    @ApiModelProperty(value = "结算金额",required = true)
    private BigDecimal price;

    @ApiModelProperty(value = "数量",required = true)
    private Integer num;

    @ApiModelProperty(value = "母品牌",required = true)
    private String parentBrandName;

    @ApiModelProperty(value = "NC收支费项名称",required = true)
    private String paymentSubjectName;

    @ApiModelProperty(value = "NC收支费项id",required = true)
    private Long paymentSubjectId;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getParentBrandName() {
        return parentBrandName;
    }

    public void setParentBrandName(String parentBrandName) {
        this.parentBrandName = parentBrandName;
    }

    public String getPaymentSubjectName() {
        return paymentSubjectName;
    }

    public void setPaymentSubjectName(String paymentSubjectName) {
        this.paymentSubjectName = paymentSubjectName;
    }

    public Long getPaymentSubjectId() {
        return paymentSubjectId;
    }

    public void setPaymentSubjectId(Long paymentSubjectId) {
        this.paymentSubjectId = paymentSubjectId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency);
    }

    public String getCurrencyLabel() {
        return currencyLabel;
    }

    public void setCurrencyLabel(String currencyLabel) {
        this.currencyLabel = currencyLabel;
    }

    public String getNcChannelCode() {
        return ncChannelCode;
    }

    public void setNcChannelCode(String ncChannelCode) {
        this.ncChannelCode = ncChannelCode;
        this.ncChannelName = DERP_ORDER.getLabelByKey(DERP_ORDER.channel_codeList, ncChannelCode) ;
    }

    public String getNcChannelName() {
        return ncChannelName;
    }

    public void setNcChannelName(String ncChannelName) {
        this.ncChannelName = ncChannelName;
    }

    public String getSettlementType() {
        return settlementType;
    }

    public void setSettlementType(String settlementType) {
        this.settlementType = settlementType;
        this.settlementTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.settlement_typeList, settlementType) ;
    }

    public String getSettlementTypeLabel() {
        return settlementTypeLabel;
    }

    public void setSettlementTypeLabel(String settlementTypeLabel) {
        this.settlementTypeLabel = settlementTypeLabel;
    }

    public String getSaleModel() {
        return saleModel;
    }

    public void setSaleModel(String saleModel) {
        this.saleModel = saleModel;
        this.saleModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_saleModeList, saleModel) ;
    }

    public String getSaleModelLabel() {
        return saleModelLabel;
    }

    public void setSaleModelLabel(String saleModelLabel) {
        this.saleModelLabel = saleModelLabel;
    }
}
