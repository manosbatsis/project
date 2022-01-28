package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ValueDictionaryManagedModel extends PageModel implements Serializable{

    /**
    * 主键
    */
    private Long id;
    /**
    * 值编码
    */
    private String valueCode;
    /**
    * 值名称
    */
    private String valueName;
    /**
    * 字典字段关联id
    */
    private Long fieldId;
    /**
    * 字段编码
    */
    private String fieldCode;
    /**
    * 字段名称
    */
    private String fieldName;
    /**
    * 状态(1启用,0禁用)
    */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*valueCode get 方法 */
    public String getValueCode(){
    return valueCode;
    }
    /*valueCode set 方法 */
    public void setValueCode(String  valueCode){
    this.valueCode=valueCode;
    }
    /*valueName get 方法 */
    public String getValueName(){
    return valueName;
    }
    /*valueName set 方法 */
    public void setValueName(String  valueName){
    this.valueName=valueName;
    }
    /*fieldId get 方法 */
    public Long getFieldId(){
    return fieldId;
    }
    /*fieldId set 方法 */
    public void setFieldId(Long  fieldId){
    this.fieldId=fieldId;
    }
    /*fieldCode get 方法 */
    public String getFieldCode(){
    return fieldCode;
    }
    /*fieldCode set 方法 */
    public void setFieldCode(String  fieldCode){
    this.fieldCode=fieldCode;
    }
    /*fieldName get 方法 */
    public String getFieldName(){
    return fieldName;
    }
    /*fieldName set 方法 */
    public void setFieldName(String  fieldName){
    this.fieldName=fieldName;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
