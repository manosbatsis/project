package com.topideal.entity.vo.main;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class GroupCommbarcodeModel extends PageModel implements Serializable{

    /**
    * 标准品牌Id
    */
    private Long id;
    /**
    * 组码
    */
    private String groupCommbarcode;
    /**
    * 组品名
    */
    private String groupName;
    /**
    * 创建时间   默认服务器时间
    */
    private Timestamp createDate;
    /**
    * 修改日期   默认服务器时间
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
    /*groupCommbarcode get 方法 */
    public String getGroupCommbarcode(){
    return groupCommbarcode;
    }
    /*groupCommbarcode set 方法 */
    public void setGroupCommbarcode(String  groupCommbarcode){
    this.groupCommbarcode=groupCommbarcode;
    }
    /*groupName get 方法 */
    public String getGroupName(){
    return groupName;
    }
    /*groupName set 方法 */
    public void setGroupName(String  groupName){
    this.groupName=groupName;
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
