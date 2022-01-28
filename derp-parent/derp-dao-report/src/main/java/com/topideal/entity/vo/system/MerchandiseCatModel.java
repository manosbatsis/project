package com.topideal.entity.vo.system;

import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 商品分类表
 * @author lchenxing
 *
 */
public class MerchandiseCatModel extends PageModel implements Serializable{


     //分类级别
     private String level;
     //分类名称
     private String name;
     //备注
     private String remark;
     //Uuid
     private Long id;
     //上级分类
     private Long parentId;
     //创建人 
     private Long creater;
     //创建日期
     private Timestamp createDate;
     //编码
     private String code; 
     /**
      * 同步编码
      */
      private String sysCode;
      //修改时间
      private Timestamp modifyDate;



     public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getSysCode() {
		return sysCode;
	}
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
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

