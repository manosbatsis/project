package com.topideal.entity.vo.base;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 枚举类型配置
 * @author lian_
 *
 */
public class EnumConfigModel extends PageModel implements Serializable{

     //创建人
     private Long creater;
     //备注
     private String remark;
     //id
     private Long id;
     //枚举名称
     private String enumName;
     //创建时间
     private Timestamp createDate;

    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
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
    /*enumName get 方法 */
    public String getEnumName(){
        return enumName;
    }
    /*enumName set 方法 */
    public void setEnumName(String  enumName){
        this.enumName=enumName;
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

