package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/1 15:15
 * @Description: 费用差异调整单
 */
@ApiModel
public class ToCTempCostDiffOrderForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "外部交易单号", required = false)
    private String externalCode;

    @ApiModelProperty(value = "结算日期", required = false)
    private String settlementDate;

    @ApiModelProperty(value = "平台", required = false)
    private String storePlatformCode;

    @ApiModelProperty(value = "电商订单号", required = false)
    private String orderCode;

    @ApiModelProperty(value = "平台结算单号", required = false)
    private String settlementCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getStorePlatformCode() {
        return storePlatformCode;
    }

    public void setStorePlatformCode(String storePlatformCode) {
        this.storePlatformCode = storePlatformCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSettlementCode() {
        return settlementCode;
    }

    public void setSettlementCode(String settlementCode) {
        this.settlementCode = settlementCode;
    }
}
