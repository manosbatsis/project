package com.topideal.webapi.form;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ReminderEmailConfigEnumDTO {

    @ApiModelProperty(value="操作节点集合常量值")
    private List reminderTypeValue;

    @ApiModelProperty(value="单据对象集合常量值")
    private List reminderUserTypeValue;

    public List getReminderTypeValue() {
        return reminderTypeValue;
    }

    public void setReminderTypeValue(List reminderTypeValue) {
        this.reminderTypeValue = reminderTypeValue;
    }

    public List getReminderUserTypeValue() {
        return reminderUserTypeValue;
    }

    public void setReminderUserTypeValue(List reminderUserTypeValue) {
        this.reminderUserTypeValue = reminderUserTypeValue;
    }
}
