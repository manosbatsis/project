package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel
public class ToBTemporaryMonthSnapshotForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "事业部ID", required = false)
    private Long buId;

    @ApiModelProperty(value = "客户ID", required = false)
    private Long customerId;

    @ApiModelProperty(value = "月结月份", required = false)
    private String month;

    @ApiModelProperty(value = "母品牌id")
    private Long parentBrandId;

    @ApiModelProperty(value = "po号")
    private String poNo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
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

    public Long getParentBrandId() {
        return parentBrandId;
    }

    public void setParentBrandId(Long parentBrandId) {
        this.parentBrandId = parentBrandId;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }
}
