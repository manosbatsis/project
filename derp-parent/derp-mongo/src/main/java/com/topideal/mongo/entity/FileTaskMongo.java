package com.topideal.mongo.entity;

/**
 * 文件任务表
 */
public class FileTaskMongo {

    //任务类型 1-进销存汇总报表 2-财务进销存报表
    private String taskType;
    //修改时间
    private String modifyDate;
    //商家ID
    private Long merchantId;
    //参数json
    private String param;
    //任务名称
    private String taskName;
    //任务状态 1-待执行 2-执行中 3-已完成 4-失败
    private String state;
    //用户名称
    private String username;
    //用户id
    private Long userId;
    //创建日期
    private String createDate;
    //描述
    private String remark;
    //存放目录
    private String path;
    //仓库名称
    private String depotName;
    //仓库id
    private Long depotId;
    //归属月份
    private String ownMonth;
    //模块 1-报表  2-order
    private String module;
    //任务单号
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

    /*state get 方法 */
    public String getState() {
        return state;
    }

    /*state set 方法 */
    public void setState(String state) {
        this.state = state;
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

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

