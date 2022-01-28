package com.topideal.entity.vo.user;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;
/**
 * 角色权限关联表
 */
public class RolePermissionRelModel extends PageModel implements Serializable{


     //权限id
	 @ApiModelProperty(value = "权限id")
     private Long permissionId;
     //id
	 @ApiModelProperty(value = "id")
     private Long id;
     //角色id  （首位）
	 @ApiModelProperty(value = "角色id")
     private Long roleId;
     //创建时间（CURRENT_TIMESTAMP）
	 @ApiModelProperty(value = "创建时间")
     private Timestamp createDate;
     //创建人
	 @ApiModelProperty(value = "创建人")
     private Long creater;



     /*creater get 方法 */
    public Long getCreater() {
		return creater;
	}
    /*creater get 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	/*permissionId get 方法 */
    public Long getPermissionId(){
        return permissionId;
    }
    /*permissionId set 方法 */
    public void setPermissionId(Long  permissionId){
        this.permissionId=permissionId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*roleId get 方法 */
    public Long getRoleId(){
        return roleId;
    }
    /*roleId set 方法 */
    public void setRoleId(Long  roleId){
        this.roleId=roleId;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }






}

