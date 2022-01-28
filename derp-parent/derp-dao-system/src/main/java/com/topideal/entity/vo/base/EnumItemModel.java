package com.topideal.entity.vo.base;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 枚举列表 
 * @author lian_
 */
public class EnumItemModel extends PageModel implements Serializable{

     //创建人
     private Long creater;
     //id
     private Long id;
     //名称
     private String value;
     //键（序号） 1 开始
     private Integer key;
     //创建时间
     private Timestamp createDate;
     //枚举类型id
     private Long enumId;

     /*enumId get 方法 */
     public Long getEnumId(){
         return enumId;
     }
     /*enumId set 方法 */
     public void setEnumId(Long  enumId){
         this.enumId=enumId;
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
    /*value get 方法 */
    public String getValue(){
        return value;
    }
    /*value set 方法 */
    public void setValue(String  value){
        this.value=value;
    }
    /*key get 方法 */
    public Integer getKey(){
        return key;
    }
    /*key set 方法 */
    public void setKey(Integer  key){
        this.key=key;
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

