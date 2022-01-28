package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.w3c.dom.stylesheets.LinkStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@ApiModel
public class ReceiveBillVerifyForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    /**
    * 应收账单Id
    */
    @ApiModelProperty(value = "应收账单Id", required = false)
    private Long billId;


    @ApiModelProperty(value = "核销明细")
    private List<ReceiveBillVerifyItemForm> verifyItems;

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

    public List<ReceiveBillVerifyItemForm> getVerifyItems() {
        return verifyItems;
    }

    public void setVerifyItems(List<ReceiveBillVerifyItemForm> verifyItems) {
        this.verifyItems = verifyItems;
    }
}
