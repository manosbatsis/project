package com.topideal.entity.dto.user;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;
/**
 * 用户信息表
 */
public class UserInfoDTO extends PageModel implements Serializable{


	 @ApiModelProperty(value = "用户编码（平台自编码）")
     private String code;
	 @ApiModelProperty(value = "修改人")
     private Long modifier;
	 @ApiModelProperty(value = "备注")
     private String remark;
	 @ApiModelProperty(value = "最后一次登陆时间")
     private Timestamp lastLoginDate;
	 @ApiModelProperty(value = "最后一次登陆IP")
     private String lastLoginIp;
	 @ApiModelProperty(value = "密码 ")
     private String password;
	 @ApiModelProperty(value = "禁用时间")
     private Timestamp disableDate;
	 @ApiModelProperty(value = "手机号码")
     private String tel;
	 @ApiModelProperty(value = "id")
     private Long id;
	 @ApiModelProperty(value = "邮箱")
     private String email;
	 @ApiModelProperty(value = "创建时间")
     private Timestamp createDate;
	 @ApiModelProperty(value = "修改时间")
     private Timestamp modifyDate;
	 @ApiModelProperty(value = "性别  m 男   f 女")
     private String sex;
	 @ApiModelProperty(value = "重置人")
     private Long resetPwdId;
	 @ApiModelProperty(value = "出生日期")
     private Timestamp birthDate;
	 @ApiModelProperty(value = "修改密码时间")
     private Timestamp modifyPwdDate;
	 @ApiModelProperty(value = "父级权用户ID")
     private Long parentId;
	 @ApiModelProperty(value = "头像")
     private String logoImg;
	 @ApiModelProperty(value = "是否禁用  0 启用(默认)  1 禁用")
     private String disable;
     private String disableLabel;
	 @ApiModelProperty(value = "用户姓名")
     private String name;
	 @ApiModelProperty(value = "创建人")
     private Long creater;
	 @ApiModelProperty(value = "用户类型  1 平台用户  2 商家（超管理）  3 商家用户")
     private String userType;
	 @ApiModelProperty(value = "重置密码时间")
     private Timestamp resetPwdDate;
	 @ApiModelProperty(value = "用户名称")
     private String username;
	 @ApiModelProperty(value = "删除时间")
     private Timestamp deteledDate;
	 @ApiModelProperty(value = "是否删除 0：不删除（默认）  1 删除")
     private String deteled;

    //扩展字段
    //账号类型 0-后台管理员 1-普通账号
	@ApiModelProperty(value = "账号类型 0-后台管理员 1-普通账号")
    private String accountType;
    private String accountTypeLabel;

    //拓展字段
    //绑定公司
    private String merchantIds;
    private String buIds;
    private List<String> ids;
    private String roles;
    @ApiModelProperty(value = "角色id集合")
    private String roleIds;

     /*deteled get 方法 */
	public String getDeteled() {
		return deteled;
	}
	/*deteled set 方法 */
	public void setDeteled(String deteled) {
		this.deteled = deteled;
	}
	/*deteledDate get 方法 */
	public Timestamp getDeteledDate() {
		return deteledDate;
	}
	/*deteledDate set 方法 */
	public void setDeteledDate(Timestamp deteledDate) {
		this.deteledDate = deteledDate;
	}
	/*code get 方法 */
    public String getCode(){
        return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
        this.code=code;
    }
    /*modifier get 方法 */
    public Long getModifier(){
        return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
        this.modifier=modifier;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*lastLoginDate get 方法 */
    public Timestamp getLastLoginDate(){
        return lastLoginDate;
    }
    /*lastLoginDate set 方法 */
    public void setLastLoginDate(Timestamp  lastLoginDate){
        this.lastLoginDate=lastLoginDate;
    }
    /*lastLoginIp get 方法 */
    public String getLastLoginIp(){
        return lastLoginIp;
    }
    /*lastLoginIp set 方法 */
    public void setLastLoginIp(String  lastLoginIp){
        this.lastLoginIp=lastLoginIp;
    }
    /*password get 方法 */
    public String getPassword(){
        return password;
    }
    /*password set 方法 */
    public void setPassword(String  password){
        this.password=password;
    }
    /*disableDate get 方法 */
    public Timestamp getDisableDate(){
        return disableDate;
    }
    /*disableDate set 方法 */
    public void setDisableDate(Timestamp  disableDate){
        this.disableDate=disableDate;
    }
    /*tel get 方法 */
    public String getTel(){
        return tel;
    }
    /*tel set 方法 */
    public void setTel(String  tel){
        this.tel=tel;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*email get 方法 */
    public String getEmail(){
        return email;
    }
    /*email set 方法 */
    public void setEmail(String  email){
        this.email=email;
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
    /*sex get 方法 */
    public String getSex(){
        return sex;
    }
    /*sex set 方法 */
    public void setSex(String  sex){
        this.sex=sex;
    }
    /*resetPwdId get 方法 */
    public Long getResetPwdId(){
        return resetPwdId;
    }
    /*resetPwdId set 方法 */
    public void setResetPwdId(Long  resetPwdId){
        this.resetPwdId=resetPwdId;
    }
    /*birthDate get 方法 */
    public Timestamp getBirthDate(){
        return birthDate;
    }
    /*birthDate set 方法 */
    public void setBirthDate(Timestamp  birthDate){
        this.birthDate=birthDate;
    }
    /*modifyPwdDate get 方法 */
    public Timestamp getModifyPwdDate(){
        return modifyPwdDate;
    }
    /*modifyPwdDate set 方法 */
    public void setModifyPwdDate(Timestamp  modifyPwdDate){
        this.modifyPwdDate=modifyPwdDate;
    }
    /*parentId get 方法 */
    public Long getParentId(){
        return parentId;
    }
    /*parentId set 方法 */
    public void setParentId(Long  parentId){
        this.parentId=parentId;
    }
    /*logoImg get 方法 */
    public String getLogoImg(){
        return logoImg;
    }
    /*logoImg set 方法 */
    public void setLogoImg(String  logoImg){
        this.logoImg=logoImg;
    }
    /*disable get 方法 */
    public String getDisable(){
        return disable;
    }
    /*disable set 方法 */
    public void setDisable(String  disable){
        this.disable=disable;
        this.disableLabel = DERP_SYS.getLabelByKey(DERP_SYS.userInfo_disableList, disable);
    }
    /*name get 方法 */
    public String getName(){
        return name;
    }
    /*name set 方法 */
    public void setName(String  name){
        this.name=name;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*userType get 方法 */
    public String getUserType(){
        return userType;
    }
    /*userType set 方法 */
    public void setUserType(String  userType){
        this.userType=userType;
    }
    /*resetPwdDate get 方法 */
    public Timestamp getResetPwdDate(){
        return resetPwdDate;
    }
    /*resetPwdDate set 方法 */
    public void setResetPwdDate(Timestamp  resetPwdDate){
        this.resetPwdDate=resetPwdDate;
    }
    /*username get 方法 */
    public String getUsername(){
        return username;
    }
    /*username set 方法 */
    public void setUsername(String  username){
        this.username=username;
    }
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
        this.accountTypeLabel = DERP_SYS.getLabelByKey(DERP_SYS.userInfo_accountTypeList, accountType);
    }

    public String getMerchantIds() {
        return merchantIds;
    }

    public void setMerchantIds(String merchantIds) {
        this.merchantIds = merchantIds;
    }

    public String getDisableLabel() {
        return disableLabel;
    }

    public void setDisableLabel(String disableLabel) {
        this.disableLabel = disableLabel;
    }

    public String getAccountTypeLabel() {
        return accountTypeLabel;
    }

    public void setAccountTypeLabel(String accountTypeLabel) {
        this.accountTypeLabel = accountTypeLabel;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
	public String getBuIds() {
		return buIds;
	}
	public void setBuIds(String buIds) {
		this.buIds = buIds;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
    
    
}

