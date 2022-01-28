package com.topideal.entity.dto.common;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 发送邮件提醒json对象
 */
public class ReminderEmailUserDTO {

    @ApiModelProperty("商家id")
    private Long merchantId;
    @ApiModelProperty("商家名称")
    private String merchantName;
    @ApiModelProperty("事业部id")
    private Long buId;
    @ApiModelProperty("事业部名称")
    private String buName;
    @ApiModelProperty("业务模块 1:应收 2:采购 3:销售 4:采购价格管理 5:销售价格管理 6:销售赊销模块 7:平台采购单 8:应付 9:toc应收")
    private String businessModuleType;
    @ApiModelProperty("类型：应收 采购 销售")
    private String typeName;
    @ApiModelProperty("操作节点 1：提交  2：审核 3：上架 4：核销  5：开票  6：作废审核  7：盖章发票 8：审核完毕  9：作废完成  10：金额修改  11：金额确认 12：收到保证金 13：放款 14：提交还款 15：收到还款 16:作废提交 17:保存 18:未创建应付")
    private String type;
    @ApiModelProperty("供应商")
    private String supplier;
    @ApiModelProperty("订单号")
    private String orderCode;
    @ApiModelProperty("po号")
    private String poNum;
    @ApiModelProperty("操作人名称")
    private String userName;
    @ApiModelProperty("状态名称 （例如：已驳回、未确认）")
    private String status;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("金额")
    private String amount;
    @ApiModelProperty("收款金额")
    private String veriAmount;
    @ApiModelProperty("待核销金额")
    private String unVeriAmount;
    @ApiModelProperty("审核驳回原因")
    private String invalidRemark;
    @ApiModelProperty("电商平台")
    private String storePlatformName;
    @ApiModelProperty("审批方式 OA审批 经分销审批")
    private String auditMethod;


    /**
     * 1：创建人 2：提交人 3：审核人 4：核销人  5：作废核销人
     * 6：盖章发票人 7：审核完毕人 8:作废完成人 9：开票人 10：金额审核人 11：金额修改人 12：上架人,
     */
    @ApiModelProperty("创建人")
    private Long createId;
    @ApiModelProperty("提交人")
    private List<String> submitId;
    @ApiModelProperty("审核人")
    private Long auditorId;
    @ApiModelProperty("核销人")
    private Long verificationId;
    @ApiModelProperty("作废核销人")
    private Long cancelId;
    @ApiModelProperty("盖章发票人")
    private Long reminderOrgId;
    @ApiModelProperty("审核完毕人")
    private Long completeId;
    @ApiModelProperty("作废完成人")
    private Long cancelCompleteId;
    @ApiModelProperty("开票人")
    private Long drawerId;
    @ApiModelProperty("金额审核人")
    private Long reviewerId;
    @ApiModelProperty("金额审核人")
    private Long modifyId;
    @ApiModelProperty("上架人")
    private Long shelvesId;
    @ApiModelProperty(value = "集合",required = false)
    private List<ReminderEmailUserDataDTO> dataList;
    @ApiModelProperty(value = "附件集合",required = false)
    private JSONArray attArray;
    @ApiModelProperty(value = "数据集合",required = false)
    private JSONObject dataJson;
    @ApiModelProperty("当前操作人以及上个节点提醒人")
    private List<String> userId;

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public String getStorePlatformName() {
        return storePlatformName;
    }

    public void setStorePlatformName(String storePlatformName) {
        this.storePlatformName = storePlatformName;
    }

    public String getInvalidRemark() {
        return invalidRemark;
    }

    public void setInvalidRemark(String invalidRemark) {
        this.invalidRemark = invalidRemark;
    }

    public List<ReminderEmailUserDataDTO> getDataList() {
        return dataList;
    }

    public void setDataList(List<ReminderEmailUserDataDTO> dataList) {
        this.dataList = dataList;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getBusinessModuleType() {
        return businessModuleType;
    }

    public void setBusinessModuleType(String businessModuleType) {
        this.businessModuleType = businessModuleType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPoNum() {
        return poNum;
    }

    public void setPoNum(String poNum) {
        this.poNum = poNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public Long getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(Long auditorId) {
        this.auditorId = auditorId;
    }

    public Long getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(Long verificationId) {
        this.verificationId = verificationId;
    }

    public Long getCancelId() {
        return cancelId;
    }

    public void setCancelId(Long cancelId) {
        this.cancelId = cancelId;
    }

    public Long getReminderOrgId() {
        return reminderOrgId;
    }

    public void setReminderOrgId(Long reminderOrgId) {
        this.reminderOrgId = reminderOrgId;
    }

    public Long getCompleteId() {
        return completeId;
    }

    public void setCompleteId(Long completeId) {
        this.completeId = completeId;
    }

    public Long getCancelCompleteId() {
        return cancelCompleteId;
    }

    public void setCancelCompleteId(Long cancelCompleteId) {
        this.cancelCompleteId = cancelCompleteId;
    }

    public Long getDrawerId() {
        return drawerId;
    }

    public void setDrawerId(Long drawerId) {
        this.drawerId = drawerId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Long getModifyId() {
        return modifyId;
    }

    public void setModifyId(Long modifyId) {
        this.modifyId = modifyId;
    }

    public Long getShelvesId() {
        return shelvesId;
    }

    public void setShelvesId(Long shelvesId) {
        this.shelvesId = shelvesId;
    }

    public List<String> getSubmitId() {
        return submitId;
    }

    public void setSubmitId(List<String> submitId) {
        this.submitId = submitId;
    }

    public ReminderEmailUserDTO() {
    }

    public JSONArray getAttArray() {
        return attArray;
    }

    public void setAttArray(JSONArray attArray) {
        this.attArray = attArray;
    }

    public String getVeriAmount() {
        return veriAmount;
    }

    public void setVeriAmount(String veriAmount) {
        this.veriAmount = veriAmount;
    }

    public String getUnVeriAmount() {
        return unVeriAmount;
    }

    public void setUnVeriAmount(String unVeriAmount) {
        this.unVeriAmount = unVeriAmount;
    }

	public JSONObject getDataJson() {
		return dataJson;
	}

	public void setDataJson(JSONObject dataJson) {
		this.dataJson = dataJson;
	}

	public String getAuditMethod() {
		return auditMethod;
	}

	public void setAuditMethod(String auditMethod) {
		this.auditMethod = auditMethod;
	}

}
