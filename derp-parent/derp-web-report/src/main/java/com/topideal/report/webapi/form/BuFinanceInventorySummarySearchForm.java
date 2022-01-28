package com.topideal.report.webapi.form;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 事业部财务进销存汇总搜索列表
 */
@ApiModel
public class BuFinanceInventorySummarySearchForm  extends PageForm implements Serializable {
    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "归属月份 YYYY-MM", required = false)
    private String month;

    @ApiModelProperty(value = "状态", required = false)
    private String status;
    @ApiModelProperty(value = "状态 029-未关账 030-已关账", required = false)
    private String statusLabel;

    @ApiModelProperty(value = "事业部ID", required = false)
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.setStatusLabel(DERP_REPORT.getLabelByKey(DERP_REPORT.financeInventorySummary_statusList, status));
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }
}
