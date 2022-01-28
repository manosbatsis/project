package com.topideal.entity.vo.main;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品分类表
 * @author lchenxing
 *
 */
public class MerchandiseCatModel extends PageModel implements Serializable{


	@ApiModelProperty(value = "分类级别")
     private String level;
     //
	 @ApiModelProperty(value = "分类名称")
     private String name;
	 @ApiModelProperty(value = "备注")
     private String remark;
	 @ApiModelProperty(value = "自增ID")
     private Long id;
	 @ApiModelProperty(value = "上级分类")
     private Long parentId;
	 @ApiModelProperty(value = "创建人")
     private Long creater;
	 @ApiModelProperty(value = "创建日期")
     private Timestamp createDate;
	 @ApiModelProperty(value = "编码")
     private String code;
	 @ApiModelProperty(value = "修改时间")
     private Timestamp modifyDate;
	 @ApiModelProperty(value = "同步编码")
     private String sysCode;



     public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	/*code get 方法 */
     public String getCode(){
         return code;
     }
     /*code set 方法 */
     public void setCode(String  code){
         this.code=code;
     }
    /*level get 方法 */
    public String getLevel(){
        return level;
    }
    /*level set 方法 */
    public void setLevel(String  level){
        this.level=level;
    }
    /*name get 方法 */
    public String getName(){
        return name;
    }
    /*name set 方法 */
    public void setName(String  name){
        this.name=name;
    }
    /*remark get 方法 */
    public String getRemark(){
        return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
        this.remark=remark;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*parentId get 方法 */
    public Long getParentId(){
        return parentId;
    }
    /*parentId set 方法 */
    public void setParentId(Long  parentId){
        this.parentId=parentId;
    }
    /*createName get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*createName set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
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

