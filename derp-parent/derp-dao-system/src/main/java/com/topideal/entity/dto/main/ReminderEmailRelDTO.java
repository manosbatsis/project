package com.topideal.entity.dto.main;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.main.ReminderEmailRelModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReminderEmailRelDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "邮件配置ID")
    private Long configId;

    @ApiModelProperty(value = "操作节点类型 1：提交  2：审核 3：上架 4：核销  5：开票  6：作废审核  7：盖章发票 8：审核完毕  9：作废完成  10：金额修改  11：金额确认")
    private String type;
    @ApiModelProperty(value = "操作节点类型中文 1：提交  2：审核 3：上架 4：核销  5：开票  6：作废审核  7：盖章发票 8：审核完毕  9：作废完成  10：金额修改  11：金额确认")
    private String typeLabel;

    @ApiModelProperty(value = "提醒类型 1:按角色 2:按用户")
    private String reminderType;
    @ApiModelProperty(value = "提醒类型中文 1:按角色 2:按用户")
    private String reminderTypeLabel;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "角色名")
    private String roleName;

    @ApiModelProperty(value = "单据对象：1：创建人 2：提交人 3：审核人 4：核销人  5：作废核销人 6：盖章发票人 7：审核完毕人 8：作废完成人 9：开票人 10：金额审核人 11：金额修改人 12：上架人")
    private String billType;
    @ApiModelProperty(value = "单据对象中文：1：创建人 2：提交人 3：审核人 4：核销人  5：作废核销人 6：盖章发票人 7：审核完毕人 8：作废完成人 9：开票人 10：金额审核人 11：金额修改人 12：上架人")
    private String billTypeLabel;

    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "固定对象,多个用,号隔开")
    private String userIdStr;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "提醒渠道 1：邮件 2 ：企业微信")
    private String reminderChannel;
    @ApiModelProperty(value = "提醒渠道中文 1：邮件 2 ：企业微信")
    private String reminderChannelLabel;

    @ApiModelProperty(value = "消息模板")
    private String templateCode;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeLabel= DERP_ORDER.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_AllTypeList, type) ;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public void setTypeLabel(String typeLabel) {
        this.typeLabel = typeLabel;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
        this.reminderTypeLabel= DERP_ORDER.getLabelByKey(DERP_SYS.receiveEmailConfigReminder_TypeList, reminderType) ;
    }

    public String getReminderTypeLabel() {
        return reminderTypeLabel;
    }

    public void setReminderTypeLabel(String reminderTypeLabel) {
        this.reminderTypeLabel = reminderTypeLabel;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
        this.billTypeLabel= DERP_ORDER.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_AllUserTypeList, billType) ;
    }

    public String getBillTypeLabel() {
        return billTypeLabel;
    }

    public void setBillTypeLabel(String billTypeLabel) {
        this.billTypeLabel = billTypeLabel;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReminderChannel() {
        return reminderChannel;
    }

    public void setReminderChannel(String reminderChannel) {
        this.reminderChannel = reminderChannel;
        this.reminderChannelLabel=DERP_ORDER.getLabelByKey(DERP_SYS.reminderEmailConfigReminder_ChannelList, reminderChannel) ;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
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

    public String getReminderChannelLabel() {
        return reminderChannelLabel;
    }

    public void setReminderChannelLabel(String reminderChannelLabel) {
        this.reminderChannelLabel = reminderChannelLabel;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }
}
