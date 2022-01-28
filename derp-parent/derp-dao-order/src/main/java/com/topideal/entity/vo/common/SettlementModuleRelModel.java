package com.topideal.entity.vo.common;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class SettlementModuleRelModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 费项配置表id
    */
    private Long settlementConfigId;
    /**
    * 适用模块:1.应付 2.应收 3.预付 4.预收
    */
    private String moduleType;
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
    /*settlementConfigId get 方法 */
    public Long getSettlementConfigId(){
    return settlementConfigId;
    }
    /*settlementConfigId set 方法 */
    public void setSettlementConfigId(Long  settlementConfigId){
    this.settlementConfigId=settlementConfigId;
    }
    /*moduleType get 方法 */
    public String getModuleType(){
    return moduleType;
    }
    /*moduleType set 方法 */
    public void setModuleType(String  moduleType){
    this.moduleType=moduleType;
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
