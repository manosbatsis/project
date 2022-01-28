package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Description: 提交nc toc应收账单信息DTO
 **/
public class ToCReceiveBillToNCDTO extends PageModel implements Serializable {

    /**
     * 账单号
     */
    private String billCode;
    /**
     * 事业部id
     */
    private Long buId;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 结算币种
     */
    private String currency;
    private String currencyLabel;

    /**
     * 结算类型 1-应收,2-预收
     */
    private String settlementType ;
    private String settlementTypeLabel ;

    /**
     * 销售模式 1-代销 2-购销
     */
    private String saleModel ;
    private String saleModelLabel ;
    /**
     * 应收账单Id
     */
    private Long billId;
    /**
     * 系统业务单号
     */
    private String code;
    /**
     * 结算金额
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 母品牌
     */
    private String parentBrandName;
    /**
     * NC收支费项名称
     */
    private String paymentSubjectName;
    /**
     * NC收支费项id
     */
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
