package com.topideal.entity.vo.user;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

public class UserBuRelModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "自增id")
    private Long id;
    /**
    * 用户Id
    */
	@ApiModelProperty(value = "用户Id")
    private Long userId;
    /**
    * 事业部ID
    */
	@ApiModelProperty(value = "事业部ID")
    private Long buId;
    /**
    * 事业部编码
    */
	@ApiModelProperty(value = "事业部编码")
    private String buCode;
    /**
    * 事业部名称
    */
	@ApiModelProperty(value = "事业部名称")
    private String buName;
    /**
    * 创建时间
    */
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*userId get 方法 */
    public Long getUserId(){
    return userId;
    }
    /*userId set 方法 */
    public void setUserId(Long  userId){
    this.userId=userId;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buCode get 方法 */
    public String getBuCode(){
    return buCode;
    }
    /*buCode set 方法 */
    public void setBuCode(String  buCode){
    this.buCode=buCode;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
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






}
