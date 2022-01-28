package com.topideal.order.webapi.bill.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.bill.ReceiveBillCostItemDTO;
import com.topideal.entity.dto.bill.ReceiveBillItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 应收账单审核
 * @Author: Chen Yiluan
 * @Date: 2021/06/21 17:12
 **/
@ApiModel
public class ReceiveBillAuditForm extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "账单ID")
    private Long billId;

    @ApiModelProperty(value = "审批结果 00-审批通过 01-驳回")
    private String auditResult;

    @ApiModelProperty(value = "审批备注")
    private String auditRemark;

    @ApiModelProperty(value = "入账日期")
    private String creditDate;

    @ApiModelProperty(value = "应收明细")
    List<ReceiveBillItemDTO> itemDTOS;

    @ApiModelProperty(value = "费用明细")
    List<ReceiveBillCostItemDTO> costItemDTOS;

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

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public List<ReceiveBillItemDTO> getItemDTOS() {
        return itemDTOS;
    }

    public void setItemDTOS(List<ReceiveBillItemDTO> itemDTOS) {
        this.itemDTOS = itemDTOS;
    }

    public List<ReceiveBillCostItemDTO> getCostItemDTOS() {
        return costItemDTOS;
    }

    public void setCostItemDTOS(List<ReceiveBillCostItemDTO> costItemDTOS) {
        this.costItemDTOS = costItemDTOS;
    }

    public String getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(String creditDate) {
        this.creditDate = creditDate;
    }
}
