package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 开票模板form
 * @Author: Chen Yiluan
 * @Date: 2021/09/08 10:12
 **/
@ApiModel
public class InvoiceTempForm {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "发票模板id")
    private Long tempId;

    @ApiModelProperty(value = "开票关联单据id,多个以英文逗号隔开")
    private String ids;

    @ApiModelProperty("发票来源 1-应收账单 2-平台结算单 ")
    private String invoiceStatus;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
