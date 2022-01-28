package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 10:40
 * @Description: 获取应收关账 核销月份表单
 */
public class ReceiveCloseAccountVerifyMonthForm implements Serializable {

    @ApiModelProperty(value = "token")
    private String token;

    /**
     * 应收月份
     */
    @ApiModelProperty(value = "month", required = false)
    private String month;

    /**
     * 核销应收时间
     */
    @ApiModelProperty(value = "receiveDateStr", required = false)
    private String receiveDateStr;

    /**
     * 关账状态
     */
    @ApiModelProperty(value = "status", required = false)
    private String status;

    /**
     * 事业部id
     */
    @ApiModelProperty(value = "buId", required = false)
    private Long buId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getReceiveDateStr() {
        return receiveDateStr;
    }

    public void setReceiveDateStr(String receiveDateStr) {
        this.receiveDateStr = receiveDateStr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }
}
