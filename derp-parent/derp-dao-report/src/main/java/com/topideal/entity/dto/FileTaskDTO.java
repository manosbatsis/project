package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 文件任务表
 *
 * @author lian_
 */
@ApiModel
public class FileTaskDTO extends PageModel implements Serializable {

    @ApiModelProperty(value = "id", required = false)
    private Long id;

    @ApiModelProperty(value = "任务类型", required = false)
    private String taskType;
    @ApiModelProperty(value = "任务类型,YWJXC-进销存汇总报表 ,CWJXC-财务进销存报表 ,SXCW-刷新财务,VIPHXMXB唯品核销报表,SYBCWJXC事业部财务经销存 ,SDSYBCWJXC-SD事业部财务经销存,SYBYWJXC-事业部进销存汇总报表,SXSYBCW-刷新事业部财务经销存 SXZHD-刷新自核对,ZKTS-在库天数 ,SYBCWZZ事业部财务总账, SYBCWZGYS 事业部财务暂估应收", required = false)
    private String taskTypeLable;

    @ApiModelProperty(value = "修改时间", required = false)
    private String modifyDate;

    @ApiModelProperty(value = "商家ID", required = false)
    private Long merchantId;
    //参数json
    private String param;

    @ApiModelProperty(value = "任务名称", required = false)
    private String taskName;

    @ApiModelProperty(value = "任务状态", required = false)
    private String state;
    @ApiModelProperty(value = "任务状态 1-待执行 2-执行中 3-已完成 4-失败", required = false)
    private String stateLabel;

    @ApiModelProperty(value = "用户名称", required = false)
    private String username;

    @ApiModelProperty(value = "用户id", required = false)
    private Long userId;

    @ApiModelProperty(value = "创建日期", required = false)
    private String createDate;

    @ApiModelProperty(value = "描述", required = false)
    private String remark;

    @ApiModelProperty(value = "存放目录", required = false)
    private String path;

    @ApiModelProperty(value = "仓库名称", required = false)
    private String depotName;

    @ApiModelProperty(value = "仓库id", required = false)
    private Long depotId;

    @ApiModelProperty(value = "归属月份", required = false)
    private String ownMonth;

    @ApiModelProperty(value = "模块 1-报表  2-order", required = false)
    private String module;
    private String moduleLabel;

    @ApiModelProperty(value = "任务单号", required = false)
    private String code;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getOwnMonth() {
        return ownMonth;
    }

    public void setOwnMonth(String ownMonth) {
        this.ownMonth = ownMonth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /*taskType get 方法 */
    public String getTaskType() {
        return taskType;
    }

    /*taskType set 方法 */
    public void setTaskType(String taskType) {
        this.taskType = taskType;
        this.taskTypeLable = DERP_REPORT.getLabelByKey(DERP_REPORT.fileTask_taskTypeList, taskType);
    }

    /*modifyDate get 方法 */
    public String getModifyDate() {
        return modifyDate;
    }

    /*modifyDate set 方法 */
    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    /*merchantId get 方法 */
    public Long getMerchantId() {
        return merchantId;
    }

    /*merchantId set 方法 */
    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    /*param get 方法 */
    public String getParam() {
        return param;
    }

    /*param set 方法 */
    public void setParam(String param) {
        this.param = param;
    }

    /*taskName get 方法 */
    public String getTaskName() {
        return taskName;
    }

    /*taskName set 方法 */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /*id get 方法 */
    public Long getId() {
        return id;
    }

    /*id set 方法 */
    public void setId(Long id) {
        this.id = id;
    }

    /*state get 方法 */
    public String getState() {
        return state;
    }

    /*username get 方法 */
    public String getUsername() {
        return username;
    }

    /*username set 方法 */
    public void setUsername(String username) {
        this.username = username;
    }

    /*createDate get 方法 */
    public String getCreateDate() {
        return createDate;
    }

    /*createDate set 方法 */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStateLabel() {
        return stateLabel;
    }

    public void setStateLabel(String stateLabel) {
        this.stateLabel = stateLabel;
    }

    /*state set 方法 */
    public void setState(String state) {
        this.state = state;
        this.setStateLabel(DERP_REPORT.getLabelByKey(DERP_REPORT.fileTask_stateList, state));
    }

    public String getTaskTypeLable() {
        return taskTypeLable;
    }

    public void setTaskTypeLable(String taskTypeLable) {
        this.taskTypeLable = taskTypeLable;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
        this.moduleLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.fileTask_moduleList, module);
    }

    public String getModuleLabel() {
        return moduleLabel;
    }

    public void setModuleLabel(String moduleLabel) {
        this.moduleLabel = moduleLabel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

