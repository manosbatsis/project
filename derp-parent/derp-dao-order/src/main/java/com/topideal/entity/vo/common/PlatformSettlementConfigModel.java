package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class PlatformSettlementConfigModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 电商平台名称
    */
    private String storePlatformName;
    /**
    * 电商平台编码
    */
    private String storePlatformCode;
    /**
    * 平台费项名称
    */
    private String name;
    /**
    * 费项配置表id
    */
    private Long settlementConfigId;
    /**
    * 费项配置表名称
    */
    private String settlementConfigName;
    /**
    * NC收支费项id
    */
    private Long ncPaymentId;
    /**
    * NC收支费项名称
    */
    private String ncPaymentName;
    /**
    * 状态(1使用中,0已禁用)
    */
    private String status;
    /**
    * 修改人ID
    */
    private Long modifier;
    /**
    * 修改人名
    */
    private String modifierName;
    /**
    * 创建人ID
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createrName;
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
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    }
    /*name get 方法 */
    public String getName(){
    return name;
    }
    /*name set 方法 */
    public void setName(String  name){
    this.name=name;
    }
    /*settlementConfigId get 方法 */
    public Long getSettlementConfigId(){
    return settlementConfigId;
    }
    /*settlementConfigId set 方法 */
    public void setSettlementConfigId(Long  settlementConfigId){
    this.settlementConfigId=settlementConfigId;
    }
    /*settlementConfigName get 方法 */
    public String getSettlementConfigName(){
    return settlementConfigName;
    }
    /*settlementConfigName set 方法 */
    public void setSettlementConfigName(String  settlementConfigName){
    this.settlementConfigName=settlementConfigName;
    }
    /*ncPaymentId get 方法 */
    public Long getNcPaymentId(){
    return ncPaymentId;
    }
    /*ncPaymentId set 方法 */
    public void setNcPaymentId(Long  ncPaymentId){
    this.ncPaymentId=ncPaymentId;
    }
    /*ncPaymentName get 方法 */
    public String getNcPaymentName(){
    return ncPaymentName;
    }
    /*ncPaymentName set 方法 */
    public void setNcPaymentName(String  ncPaymentName){
    this.ncPaymentName=ncPaymentName;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createrName get 方法 */
    public String getCreaterName(){
    return createrName;
    }
    /*createrName set 方法 */
    public void setCreaterName(String  createrName){
    this.createrName=createrName;
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
