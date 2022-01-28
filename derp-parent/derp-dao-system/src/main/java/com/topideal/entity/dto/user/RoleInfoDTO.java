package com.topideal.entity.dto.user;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;
/**
 * 角色信息
 * @author
 *
 */
public class RoleInfoDTO extends PageModel implements Serializable{


     //删除时间
	 @ApiModelProperty(value = "删除时间")
     private Timestamp deteledDate;
     //修改时间
	 @ApiModelProperty(value = "修改时间")
     private Timestamp modifyDate;
     //修改人
	 @ApiModelProperty(value = "修改人")
     private Long modifier;
     //备注
	 @ApiModelProperty(value = "备注")
     private String remark;
     //商家超管用户名称
	 @ApiModelProperty(value = "商家超管用户名称")
     private String userName;
     //商家超管用户id
	 @ApiModelProperty(value = "商家超管用户id")
     private Long userId;
     //父类id
	 @ApiModelProperty(value = "父类id")
     private Long parentId;
     //是否删除 0：不删除（默认）  1 删除
	 @ApiModelProperty(value = "是否删除 0：不删除（默认）  1 删除")
     private String deleted;
     //角色名称
	 @ApiModelProperty(value = "角色名称")
     private String name;
     //创建人
	 @ApiModelProperty(value = "创建人")
     private Long creater;
     //id
	 @ApiModelProperty(value = "id")
     private Long id;
     //创建时间
	 @ApiModelProperty(value = "创建时间")
     private Timestamp createDate;

     //账户类型
	 @ApiModelProperty(value = "账户类型")
     private String userType;

    /*deteledDate get 方法 */
    public Timestamp getDeteledDate(){
        return deteledDate;
    }
    /*deteledDate set 方法 */
    public void setDeteledDate(Timestamp  deteledDate){
        this.deteledDate=deteledDate;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
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
    /*userName get 方法 */
    public String getUserName(){
        return userName;
    }
    /*userName set 方法 */
    public void setUserName(String  userName){
        this.userName=userName;
    }
    /*userId get 方法 */
    public Long getUserId(){
        return userId;
    }
    /*userId set 方法 */
    public void setUserId(Long  userId){
        this.userId=userId;
    }
    /*parentId get 方法 */
    public Long getParentId(){
        return parentId;
    }
    /*parentId set 方法 */
    public void setParentId(Long  parentId){
        this.parentId=parentId;
    }
    /*deleted get 方法 */
    public String getDeleted(){
        return deleted;
    }
    /*deleted set 方法 */
    public void setDeleted(String  deleted){
        this.deleted=deleted;
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
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

