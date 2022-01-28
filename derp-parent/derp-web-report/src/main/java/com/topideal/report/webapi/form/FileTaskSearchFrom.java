package com.topideal.report.webapi.form;

import java.io.Serializable;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报表任务搜索列表
 */
@ApiModel
public class FileTaskSearchFrom extends PageForm implements Serializable {

    @ApiModelProperty(value = "token", required = true)
    private String token;

    @ApiModelProperty(value = "任务类型", required = false)
    private String taskType;
    @ApiModelProperty(value = "任务类型,YWJXC-进销存汇总报表 ,CWJXC-财务进销存报表 ,SXCW-刷新财务,VIPHXMXB唯品核销报表,SYBCWJXC事业部财务经销存 ,SDSYBCWJXC-SD事业部财务经销存,SYBYWJXC-事业部进销存汇总报表,SXSYBCW-刷新事业部财务经销存 SXZHD-刷新自核对,ZKTS-在库天数 ,SYBCWZZ事业部财务总账, SYBCWZGYS 事业部财务暂估应收", required = false)
    private String taskTypeLable;

    @ApiModelProperty(value = "归属月份", required = false)
    private String ownMonth;
    
    @ApiModelProperty(value = "模块 1-报表  2-order", required = false)
    private String module;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
        this.taskTypeLable=DERP_REPORT.getLabelByKey(DERP_REPORT.fileTask_taskTypeList, taskType) ;
    }

    public String getTaskTypeLable() {
        return taskTypeLable;
    }

    public void setTaskTypeLable(String taskTypeLable) {
        this.taskTypeLable = taskTypeLable;
    }

    public String getOwnMonth() {
        return ownMonth;
    }

    public void setOwnMonth(String ownMonth) {
        this.ownMonth = ownMonth;
    }

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
}
