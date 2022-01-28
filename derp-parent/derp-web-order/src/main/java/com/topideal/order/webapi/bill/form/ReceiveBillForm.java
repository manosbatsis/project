package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class ReceiveBillForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "客户ID", required = false)
    private Long customerId;

    @ApiModelProperty(value = "账单月份", required = false)
    private String billMonth;

    @ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;

    @ApiModelProperty(value = "开票状态 00-待开票  01-待签章  02-已作废 03-已签章")
    private String invoiceStatus;

    @ApiModelProperty(value = "应收账单号")
    private String code;

    @ApiModelProperty(value = "关联业务单号")
    private String relCode;

    @ApiModelProperty(value = "发票编码", required = false)
    private String invoiceNo;

    @ApiModelProperty(value = "账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销")
    private String billStatus;
    @ApiModelProperty(value = "账单状态，多个以英文逗号隔开")
    private String billStates;

    @ApiModelProperty(value = "NC状态1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-未同步 7-已同步")
    private String ncStatus;

    @ApiModelProperty(value = "单据类型 1-上架单 2-账单出库单 3-预售单 4-销售订单 5-采购SD单 6-融资申请单")
    private String orderType;

    @ApiModelProperty(value = "id集合,多个用英文逗号隔开")
    private String ids;

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "分类：1-商销收入 2-GTN核销 3-采购rebate\n")
    private String sortType;

    @ApiModelProperty(value = "结算币种")
    private String currency;

    @ApiModelProperty(value = "会计期间")
    private String period;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "发票月份")
    private String invoiceMonth;

    @ApiModelProperty(value = "po号")
    private String poNo;

    @ApiModelProperty(value = "是否增加残损金额 0-是 1-否")
    private String isAddWorn;

    @ApiModelProperty(value = "业务单据List")
    private List<ReceiveBillAddForm> orderLists;

    @ApiModelProperty(value = "补扣款明细List")
    private List<ReceiveBillCostItemForm> costItemList;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRelCode() {
        return relCode;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getNcStatus() {
        return ncStatus;
    }

    public void setNcStatus(String ncStatus) {
        this.ncStatus = ncStatus;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<ReceiveBillAddForm> getOrderLists() {
        return orderLists;
    }

    public void setOrderLists(List<ReceiveBillAddForm> orderLists) {
        this.orderLists = orderLists;
    }

    public List<ReceiveBillCostItemForm> getCostItemList() {
        return costItemList;
    }

    public void setCostItemList(List<ReceiveBillCostItemForm> costItemList) {
        this.costItemList = costItemList;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getInvoiceMonth() {
        return invoiceMonth;
    }

    public void setInvoiceMonth(String invoiceMonth) {
        this.invoiceMonth = invoiceMonth;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public String getIsAddWorn() {
        return isAddWorn;
    }

    public void setIsAddWorn(String isAddWorn) {
        this.isAddWorn = isAddWorn;
    }

    public String getBillStates() {
        return billStates;
    }

    public void setBillStates(String billStates) {
        this.billStates = billStates;
    }
}
