package com.topideal.entity.dto.bill;

import com.topideal.entity.vo.bill.ReceiveBillOperateModel;
import com.topideal.entity.vo.bill.ReceiveBillVerifyItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillAuditItemModel;
import com.topideal.entity.vo.bill.TocSettlementReceiveBillVerifyItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApiModel
public class ReceiveBillDetailDTO implements Serializable {

    @ApiModelProperty(value = "月份")
    private String month;

    @ApiModelProperty(value = "应收账单", required = false)
    private ReceiveBillDTO bill;

    @ApiModelProperty(value = "总数量", required = false)
    private BigDecimal totalNum;

    @ApiModelProperty(value = "总金额", required = false)
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "总金额（含税）", required = false)
    private BigDecimal totalTaxAmount;

    @ApiModelProperty(value = "总税额", required = false)
    private BigDecimal totalTax;

    @ApiModelProperty(value="币种",required = false)
    private String totalPriceLabel;

    @ApiModelProperty(value="应收费用汇总",required = false)
    private List<ReceiveBillCostItemDTO> deductionMap;

    @ApiModelProperty(value="应收明细汇总",required = false)
    private  List<Map<String, Object>> receiveMap;

    @ApiModelProperty(value="审核记录",required = false)
    private  List<ReceiveBillOperateModel> receiveBillOperateModels;

    @ApiModelProperty(value="核算记录",required = false)
    private  List<ReceiveBillVerifyItemModel> verifyItemModels;

    @ApiModelProperty(value="核销总金额",required = false)
    private BigDecimal alreadyPrice;

    @ApiModelProperty(value = "应收明细总数量", required = false)
    private BigDecimal itemTotalNum;

    @ApiModelProperty(value = "应收明细总金额", required = false)
    private BigDecimal itemTotalPrice;

    @ApiModelProperty(value = "费用明细总数量", required = false)
    private BigDecimal costTotalNum;

    @ApiModelProperty(value = "费用明细总金额", required = false)
    private BigDecimal costTotalPrice;

    @ApiModelProperty(value = "应收明细集合", required = false)
    private List<ReceiveBillItemDTO> itemDTOS;

    @ApiModelProperty(value = "费用明细集合", required = false)
    private List<ReceiveBillCostItemDTO> costItemDTOS;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public List<ReceiveBillCostItemDTO> getDeductionMap() {
        return deductionMap;
    }

    public void setDeductionMap(List<ReceiveBillCostItemDTO> deductionMap) {
        this.deductionMap = deductionMap;
    }

    public List<Map<String, Object>> getReceiveMap() {
        return receiveMap;
    }

    public void setReceiveMap(List<Map<String, Object>> receiveMap) {
        this.receiveMap = receiveMap;
    }

    public List<ReceiveBillOperateModel> getReceiveBillOperateModels() {
        return receiveBillOperateModels;
    }

    public void setReceiveBillOperateModels(List<ReceiveBillOperateModel> receiveBillOperateModels) {
        this.receiveBillOperateModels = receiveBillOperateModels;
    }

    public List<ReceiveBillVerifyItemModel> getVerifyItemModels() {
        return verifyItemModels;
    }

    public void setVerifyItemModels(List<ReceiveBillVerifyItemModel> verifyItemModels) {
        this.verifyItemModels = verifyItemModels;
    }

    public BigDecimal getAlreadyPrice() {
        return alreadyPrice;
    }

    public void setAlreadyPrice(BigDecimal alreadyPrice) {
        this.alreadyPrice = alreadyPrice;
    }

    public ReceiveBillDTO getBill() {
        return bill;
    }

    public void setBill(ReceiveBillDTO bill) {
        this.bill = bill;
    }

    public BigDecimal getItemTotalNum() {
        return itemTotalNum;
    }

    public void setItemTotalNum(BigDecimal itemTotalNum) {
        this.itemTotalNum = itemTotalNum;
    }

    public BigDecimal getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(BigDecimal itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public BigDecimal getCostTotalNum() {
        return costTotalNum;
    }

    public void setCostTotalNum(BigDecimal costTotalNum) {
        this.costTotalNum = costTotalNum;
    }

    public BigDecimal getCostTotalPrice() {
        return costTotalPrice;
    }

    public void setCostTotalPrice(BigDecimal costTotalPrice) {
        this.costTotalPrice = costTotalPrice;
    }

    public List<ReceiveBillItemDTO> getItemDTOS() {
        return itemDTOS;
    }

    public void setItemDTOS(List<ReceiveBillItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
    }

    public List<ReceiveBillCostItemDTO> getCostItemDTOS() {
        return costItemDTOS;
    }

    public void setCostItemDTOS(List<ReceiveBillCostItemDTO> costItemDTOS) {
        this.costItemDTOS = costItemDTOS;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }
}
