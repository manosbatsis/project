package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ReminderEmailRelModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 邮件配置ID
    */
    private Long configId;
    /**
    *操作节点类型：1：提交  2：审核 3：上架 4：核销  5：开票  6：作废审核  7：盖章发票 8：审核完毕  9：作废完成  10：金额修改  11：金额确认
    */
    private String type;
    /**
    * 提醒类型 1:按角色 2:按用户
    */
    private String reminderType;
    /**
    * 角色ID
    */
    private Long roleId;
    /**
    * 角色名
    */
    private String roleName;
    /**
    * 单据对象：1：创建人 2：提交人 3：审核人 4：上架人
    */
    private String billType;
    /**
    * 用户ID
    */
    private Long userId;
    /**
    * 用户名
    */
    private String userName;
    /**
    * 提醒渠道 1：邮件 2 ：企业微信
    */
    private String reminderChannel;
    /**
    * 消息模板
    */
    private String templateCode;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    //多个固定用户ids字符串
    private String userIdStr;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*configId get 方法 */
    public Long getConfigId(){
    return configId;
    }
    /*configId set 方法 */
    public void setConfigId(Long  configId){
    this.configId=configId;
    }
    /*type get 方法 */
    public String getType(){
    return type;
    }
    /*type set 方法 */
    public void setType(String  type){
    this.type=type;
    }
    /*reminderType get 方法 */
    public String getReminderType(){
    return reminderType;
    }
    /*reminderType set 方法 */
    public void setReminderType(String  reminderType){
    this.reminderType=reminderType;
    }
    /*roleId get 方法 */
    public Long getRoleId(){
    return roleId;
    }
    /*roleId set 方法 */
    public void setRoleId(Long  roleId){
    this.roleId=roleId;
    }
    /*roleName get 方法 */
    public String getRoleName(){
    return roleName;
    }
    /*roleName set 方法 */
    public void setRoleName(String  roleName){
    this.roleName=roleName;
    }
    /*billType get 方法 */
    public String getBillType(){
    return billType;
    }
    /*billType set 方法 */
    public void setBillType(String  billType){
    this.billType=billType;
    }
    /*userId get 方法 */
    public Long getUserId(){
    return userId;
    }
    /*userId set 方法 */
    public void setUserId(Long  userId){
    this.userId=userId;
    }
    /*userName get 方法 */
    public String getUserName(){
    return userName;
    }
    /*userName set 方法 */
    public void setUserName(String  userName){
    this.userName=userName;
    }
    /*reminderChannel get 方法 */
    public String getReminderChannel(){
    return reminderChannel;
    }
    /*reminderChannel set 方法 */
    public void setReminderChannel(String  reminderChannel){
    this.reminderChannel=reminderChannel;
    }
    /*templateCode get 方法 */
    public String getTemplateCode(){
    return templateCode;
    }
    /*templateCode set 方法 */
    public void setTemplateCode(String  templateCode){
    this.templateCode=templateCode;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }
}
