package com.topideal.entity.dto.main;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.List;

public class ReminderEmailConfigResponseDTO {

    @ApiModelProperty(value="邮件提醒信息")
    private ReminderEmailConfigDTO configDTO;

    @ApiModelProperty(value = "邮件提醒表体信息")
    private List<ReminderEmailRelDTO> items;

    @ApiModelProperty(value = "业务模块集合常量值")
    private List businessModuleValue;

    @ApiModelProperty(value = "操作节点集合常量值")
    private List reminderTypeValue;

    @ApiModelProperty(value = "提醒对象类型集合常量值")
    private List billReminderTypeValue;

    @ApiModelProperty(value = "提醒渠道集合常量值")
    private List reminderChannelValue;

    @ApiModelProperty(value = "单据对象集合常量值")
    private List reminderUserTypeValue;

    public ReminderEmailConfigDTO getConfigDTO() {
        return configDTO;
    }

    public void setConfigDTO(ReminderEmailConfigDTO configDTO) {
        this.configDTO = configDTO;
    }

    public List<ReminderEmailRelDTO> getItems() {
        return items;
    }

    public void setItems(List<ReminderEmailRelDTO> items) {
        this.items = items;
    }

    public List getBusinessModuleValue() {
        return businessModuleValue;
    }

    public void setBusinessModuleValue(List businessModuleValue) {
        this.businessModuleValue = businessModuleValue;
    }

    public List getReminderTypeValue() {
        return reminderTypeValue;
    }

    public void setReminderTypeValue(List reminderTypeValue) {
        this.reminderTypeValue = reminderTypeValue;
    }

    public List getBillReminderTypeValue() {
        return billReminderTypeValue;
    }

    public void setBillReminderTypeValue(List billReminderTypeValue) {
        this.billReminderTypeValue = billReminderTypeValue;
    }

    public List getReminderChannelValue() {
        return reminderChannelValue;
    }

    public void setReminderChannelValue(List reminderChannelValue) {
        this.reminderChannelValue = reminderChannelValue;
    }

    public List getReminderUserTypeValue() {
        return reminderUserTypeValue;
    }

    public void setReminderUserTypeValue(List reminderUserTypeValue) {
        this.reminderUserTypeValue = reminderUserTypeValue;
    }
}
