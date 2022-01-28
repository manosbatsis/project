package com.topideal.order.webapi.bill.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 列表页搜索参数
 */
@ApiModel
public class ReceiveBillInvoiceForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "发票编码", required = false)
    private String invoiceNo;

    @ApiModelProperty(value = "开票人", required = false)
    private String creater;

    @ApiModelProperty(value = "发票状态", required = false)
    private String status;

    @ApiModelProperty(value = "客户ID", required = false)
    private Long customerId;

    @ApiModelProperty(value = "开票开始时间", required = false)
    private String invoiceStartDate;

    @ApiModelProperty(value = "开票结束时间", required = false)
    private String invoiceEndDate;

    @ApiModelProperty(value = "ID", required = false)
    private Long id;

    @ApiModelProperty(value = "id集合,多个用英文逗号隔开")
    private String ids;

    @ApiModelProperty(value = "应收单号")
    private String invoiceRelCodes;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(String invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public String getInvoiceEndDate() {
        return invoiceEndDate;
    }

    public void setInvoiceEndDate(String invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getInvoiceRelCodes() {
        return invoiceRelCodes;
    }

    public void setInvoiceRelCodes(String invoiceRelCodes) {
        this.invoiceRelCodes = invoiceRelCodes;
    }
}
