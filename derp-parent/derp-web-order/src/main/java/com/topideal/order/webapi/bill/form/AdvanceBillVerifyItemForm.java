package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 保存核销参数
 */
@ApiModel
public class AdvanceBillVerifyItemForm  implements Serializable {

    @ApiModelProperty(value = "token", required = false)
    private String token;

    @ApiModelProperty(value = "预收账单Id", required = false)
    private Long advanceId;

    @ApiModelProperty(value = "核销金额", required = false)
    private BigDecimal price;

    @ApiModelProperty(value = "收款日期字符串", required = false)
    private String verifyDateStr;

    @ApiModelProperty(value = "收款流水号", required = false)
    private String verifyNo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(Long advanceId) {
        this.advanceId = advanceId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getVerifyDateStr() {
        return verifyDateStr;
    }

    public void setVerifyDateStr(String verifyDateStr) {
        this.verifyDateStr = verifyDateStr;
    }

    public String getVerifyNo() {
        return verifyNo;
    }

    public void setVerifyNo(String verifyNo) {
        this.verifyNo = verifyNo;
    }
}
