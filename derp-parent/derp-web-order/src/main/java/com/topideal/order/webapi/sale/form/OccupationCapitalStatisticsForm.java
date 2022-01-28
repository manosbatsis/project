package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class OccupationCapitalStatisticsForm extends PageForm {

    @ApiModelProperty(value = "令牌",required = true)
    private String token;

    @ApiModelProperty(value = "赊销单号")
    private  String creditOrderCode;

    @ApiModelProperty(value = "事业部ID")
    private Long buId;

    @ApiModelProperty(value = "权责月份")
    private String ownMonth;

    @ApiModelProperty(value = "po单号")
    private String poNo;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreditOrderCode() {
        return creditOrderCode;
    }

    public void setCreditOrderCode(String creditOrderCode) {
        this.creditOrderCode = creditOrderCode;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getOwnMonth() {
        return ownMonth;
    }

    public void setOwnMonth(String ownMonth) {
        this.ownMonth = ownMonth;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
