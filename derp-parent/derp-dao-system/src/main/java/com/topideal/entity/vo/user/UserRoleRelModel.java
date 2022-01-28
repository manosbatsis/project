package com.topideal.entity.vo.user;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;
/**
 *用户角色关联表
 */
public class UserRoleRelModel extends PageModel implements Serializable{


     //id
     private Long id;
     //角色id
     private Long roleId;
     //用户id
     private Long userId;
     //创建时间（CURRENT_TIMESTAMP）
     private Timestamp createDate;
     //创建人
     private Long creater;



     /*creater get 方法 */
    public Long getCreater() {
		return creater;
	}
    /*creater get 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

