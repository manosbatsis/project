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
public class AdvanceBillForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "预收账单导出ids", required = false)
    private String ids;
    @ApiModelProperty(value = "预收账单号", required = false)
    private String code;

    @ApiModelProperty(value = "客户id", required = false)
    private Long customerId;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = "账单月份", required = false)
    private String billMonth;

    @ApiModelProperty(value = "NC状态 1-待审核 2-待入erp 3-待入账 4-已入账 5-已关账 6-待作废 7-待红冲 8-已作废 9-已红冲 10-未同步 11-已同步", required = false)
    private String ncStatus;
    private String ncStatusLabel;

    @ApiModelProperty(value = "账单状态 00-待提交 01-待审核 02-待核销 03-部分核销 04-已核销 05-待作废 06-已作废", required = false)
    private String billStatus;
    private String billStatusLabel;
    @ApiModelProperty(value = "开票状态 00-待开票  01-待签章  02-已作废 03-已签章")
    private String invoiceStatus;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getNcStatus() {
        return ncStatus;
    }

    public void setNcStatus(String ncStatus) {
        this.ncStatus = ncStatus;
    }

    public String getNcStatusLabel() {
        return ncStatusLabel;
    }

    public void setNcStatusLabel(String ncStatusLabel) {
        this.ncStatusLabel = ncStatusLabel;
        this.ncStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBill_nvSynList, ncStatus) ;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBillStatusLabel() {
        return billStatusLabel;
    }

    public void setBillStatusLabel(String billStatusLabel) {
        this.billStatusLabel = billStatusLabel;
        this.billStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.advanceBill_billStatusList, billStatus);
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
	
    
}
