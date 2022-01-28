package com.topideal.order.webapi.bill.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 审批参数
 */
@ApiModel
public class AdvanceAuditForm implements Serializable {

    @ApiModelProperty(value = "token", required = false)
    private String token;

    @ApiModelProperty(value = "预收账单Id", required = false)
    private Long advanceId;

    @ApiModelProperty(value = "操作结果 0-通过 01-驳回 02-提交审核 03-提交作废", required = false)
    private String operateResult;
    private String operateResultLabel;

    @ApiModelProperty(value = "操作类型 0-提交 1-审核 2-提交作废 3-审核作废", required = false)
    private String operateType;
    private String operateTypeLabel;

    @ApiModelProperty(value = "备注", required = false)
    private String operateRemark;

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

    public String getOperateResult() {
        return operateResult;
    }

    public void setOperateResult(String operateResult) {
        this.operateResult = operateResult;
    }

    public String getOperateResultLabel() {
        return operateResultLabel;
    }

    public void setOperateResultLabel(String operateResultLabel) {
        this.operateResultLabel = operateResultLabel;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateTypeLabel() {
        return operateTypeLabel;
    }

    public void setOperateTypeLabel(String operateTypeLabel) {
        this.operateTypeLabel = operateTypeLabel;
    }

    public String getOperateRemark() {
        return operateRemark;
    }

    public void setOperateRemark(String operateRemark) {
        this.operateRemark = operateRemark;
    }
}
