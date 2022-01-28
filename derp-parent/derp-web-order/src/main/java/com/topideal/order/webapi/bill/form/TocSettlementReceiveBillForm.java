package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Toc应收账单列表页搜索参数
 */
@ApiModel
public class TocSettlementReceiveBillForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "电商平台编码", required = false)
    private String storePlatformCode;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "平台结算单号", required = false)
    private String code;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "账单月份", required = false)
    private String month;

    @ApiModelProperty(value = "账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-作废待审 06-已作废 07-生成中 08-生成失败", required = false)
    private String billStatus;
    private List<String> billStatusList;

    @ApiModelProperty(value = "发票号码", required = false)
    private String invoiceNo;

    @ApiModelProperty(value = "发票状态 00-待开票  01-待签章  02-已作废 03-已签章", required = false)
    private String invoiceStatus;

    @ApiModelProperty(value = "toc应收账单id，多个以英文逗号隔开", required = false)
    private String ids;

    /**
     * 外部结算单号
     */
    @ApiModelProperty(value = "外部单号", required = false)
    private String externalCode;

    @ApiModelProperty(value = "店铺编码", required = false)
    private String shopCode;

    @ApiModelProperty(value = "NC应收状态", required = false)
    private String ncStatus;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public String getNcStatus() {
        return ncStatus;
    }

    public void setNcStatus(String ncStatus) {
        this.ncStatus = ncStatus;
    }

    public List<String> getBillStatusList() {
        return billStatusList;
    }

    public void setBillStatusList(List<String> billStatusList) {
        this.billStatusList = billStatusList;
    }
}
