package com.topideal.report.webapi.form;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.webapi.PageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 单价预警配置列表搜索参数
 */
@ApiModel
public class SettlementPriceWarnconfigSearchForm extends PageForm implements Serializable {
    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "事业部id", required = false)
    private Long buId;

    @ApiModelProperty(value = " 状态(1启用,0禁用)", required = false)
    private String status;
    private String statusLabel;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_REPORT.settlementPriceWarnconfig_statusList, status);
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }
}
