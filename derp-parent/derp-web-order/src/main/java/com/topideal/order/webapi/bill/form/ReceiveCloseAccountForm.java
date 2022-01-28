package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @Author: Wilson Lau
 * @Date: 2021/11/17 10:40
 * @Description: 应收关账表单
 */
public class ReceiveCloseAccountForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 应收月份
     */
    @ApiModelProperty(value = "应收月份", required = false)
    private String month;

    /**
     * 核销应收时间
     */
    @ApiModelProperty(value = "核销应收时间", required = false)
    private String receiveDateStr;

    /**
     * 关账状态
     */
    @ApiModelProperty(value = "关账状态", required = false)
    private String status;

    /**
     * 事业部id
     */
    @ApiModelProperty(value = "事业部id", required = false)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
