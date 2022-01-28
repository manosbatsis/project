package com.topideal.entity.dto.bill;

import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApiModel
public class TocSettlementReceiveBillDetailDTO implements Serializable {

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "toc账单对象", required = false)
    private TocSettlementReceiveBillDTO bill;

    @ApiModelProperty(value = "总数量", required = false)
    private BigDecimal totalNum;

    @ApiModelProperty(value = "总金额", required = false)
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "总金额Rmb", required = false)
    private BigDecimal totalRmbPrice;

    @ApiModelProperty(value="币种",required = false)
    private String totalPriceLabel;

    @ApiModelProperty(value="抵扣费用",required = false)
    private List<Map<String, Object>> deductionMap;

    @ApiModelProperty(value="应收结算",required = false)
    private  List<Map<String, Object>> receiveMap;

    @ApiModelProperty(value="审核记录",required = false)
    private  List<TocSettlementReceiveBillAuditItemModel> receiveBillAuditItemModels;

    @ApiModelProperty(value="核算记录",required = false)
    private  List<TocSettlementReceiveBillVerifyItemModel> verifyItems;

    @ApiModelProperty(value="核销总金额",required = false)
    private BigDecimal alreadyPrice;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public TocSettlementReceiveBillDTO getTocSettlementReceiveBillDTO() {
        return bill;
    }

    public void setBill(TocSettlementReceiveBillDTO tocSettlementReceiveBillDTO) {
        this.bill = tocSettlementReceiveBillDTO;
    }

    public BigDecimal getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(BigDecimal totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPriceLabel() {
        return totalPriceLabel;
    }

    public void setTotalPriceLabel(String totalPriceLabel) {
        this.totalPriceLabel = totalPriceLabel;
    }

    public List<Map<String, Object>> getDeductionMap() {
        return deductionMap;
    }

    public void setDeductionMap(List<Map<String, Object>> deductionMap) {
        this.deductionMap = deductionMap;
    }

    public List<Map<String, Object>> getReceiveMap() {
        return receiveMap;
    }

    public void setReceiveMap(List<Map<String, Object>> receiveMap) {
        this.receiveMap = receiveMap;
    }

    public List<TocSettlementReceiveBillAuditItemModel> getReceiveBillAuditItemModels() {
        return receiveBillAuditItemModels;
    }

    public void setReceiveBillAuditItemModels(List<TocSettlementReceiveBillAuditItemModel> receiveBillAuditItemModels) {
        this.receiveBillAuditItemModels = receiveBillAuditItemModels;
    }

    public List<TocSettlementReceiveBillVerifyItemModel> getVerifyItemModels() {
        return verifyItems;
    }

    public void setVerifyItems(List<TocSettlementReceiveBillVerifyItemModel> verifyItemModels) {
        this.verifyItems = verifyItemModels;
    }

    public BigDecimal getAlreadyPrice() {
        return alreadyPrice;
    }

    public void setAlreadyPrice(BigDecimal alreadyPrice) {
        this.alreadyPrice = alreadyPrice;
    }

    public BigDecimal getTotalRmbPrice() {
        return totalRmbPrice;
    }

    public void setTotalRmbPrice(BigDecimal totalRmbPrice) {
        this.totalRmbPrice = totalRmbPrice;
    }
}
