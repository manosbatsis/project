package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel
public class ReceiveBillVerifyItemForm extends PageForm implements Serializable {

    /**
    * 核销金额
    */
    @ApiModelProperty(value = "核销金额", required = false)
    private BigDecimal price;
    /**
    * 收款日期
    */
    @ApiModelProperty(value = "收款日期", required = false)
    private String receiveDateStr;
    /**
    * 收款流水号
    */
    @ApiModelProperty(value = "收款流水号", required = false)
    private String receiceNo;

    /**
     * 单据类型 1-预收账单 2-收款单
     */
    @ApiModelProperty(value = "单据类型 1-预收账单 2-收款单", required = false)
    private String type;
    private String typeLabel;

    @ApiModelProperty(value = "预收账单id", required = false)
    private Long advanceId;

    @ApiModelProperty(value = "核销月份")
    private String verifyMonth;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public Long getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(Long advanceId) {
        this.advanceId = advanceId;
    }

    public String getVerifyMonth() {
        return verifyMonth;
    }

    public void setVerifyMonth(String verifyMonth) {
        this.verifyMonth = verifyMonth;
    }
}
