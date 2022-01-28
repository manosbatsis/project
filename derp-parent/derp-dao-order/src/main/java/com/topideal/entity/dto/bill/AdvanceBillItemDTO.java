package com.topideal.entity.dto.bill;

import com.topideal.entity.vo.bill.AdvanceBillItemModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 预收账单标表体DTO
 */
public class AdvanceBillItemDTO {
    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "预收账单Id", required = false)
    private Long advanceId;

    @ApiModelProperty(value = "系统业务单号", required = false)
    private String relCode;

    @ApiModelProperty(value = "费项id", required = false)
    private Long projectId;

    @ApiModelProperty(value = "费项名称", required = false)
    private String projectName;

    @ApiModelProperty(value = "po单号", required = false)
    private String poNo;

    @ApiModelProperty(value = "结算金额", required = false)
    private BigDecimal amount;

    @ApiModelProperty(value = "备注", required = false)
    private String remark;

    @ApiModelProperty(value = "创建时间", required = false)
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间", required = false)
    private Timestamp modifyDate;

    @ApiModelProperty(value = "一级费项名称", required = false)
    private String oneLevelProjectName;

    @ApiModelProperty(value = "预收总金额", required = false)
    private BigDecimal sumAmount;

    @ApiModelProperty(value = "预收账单号", required = false)
    private String advanceCode;

    public String getRelCode() {
        return relCode;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOneLevelProjectName() {
        return oneLevelProjectName;
    }

    public void setOneLevelProjectName(String oneLevelProjectName) {
        this.oneLevelProjectName = oneLevelProjectName;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(Long advanceId) {
        this.advanceId = advanceId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    public String getAdvanceCode() {
        return advanceCode;
    }

    public void setAdvanceCode(String advanceCode) {
        this.advanceCode = advanceCode;
    }
}
