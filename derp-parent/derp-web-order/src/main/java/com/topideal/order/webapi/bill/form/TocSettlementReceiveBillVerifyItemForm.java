package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
public class TocSettlementReceiveBillVerifyItemForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "账单Id")
    private Long billId;

    @ApiModelProperty(value = "核销金额")
    private BigDecimal price;

    @ApiModelProperty(value = "收款日期")
    private String receiveDateStr;

    @ApiModelProperty(value = "收款流水号")
    private String receiceNo;

    @ApiModelProperty(value = "核销月份")
    private String verifyMonth;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getReceiveDateStr() {
        return receiveDateStr;
    }

    public void setReceiveDateStr(String receiveDateStr) {
        this.receiveDateStr = receiveDateStr;
    }

    public String getReceiceNo() {
        return receiceNo;
    }

    public void setReceiceNo(String receiceNo) {
        this.receiceNo = receiceNo;
    }

    public String getVerifyMonth() {
        return verifyMonth;
    }

    public void setVerifyMonth(String verifyMonth) {
        this.verifyMonth = verifyMonth;
    }
}
