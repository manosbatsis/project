package com.topideal.mongo.entity;
import java.sql.Timestamp;
/**
 * 批次效期强校验明细
 * @author lian_
 */
public class BatchValidationInfoMongo{

    /**
    * 仓库名称
    */
    private String depotName;
    /**
    * 修改时间
    */
    private Long modifyDate;
    /**
    * 操作员名称
    */
    private String createUsername;
    /**
    * 生效时间
    */
    private Long effectiveTime;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 批次效期强校验：1-是 0-否
    */
    private String batchValidation;
    /**
    * 操作员ID
    */
    private Long creater;
    /**
    * id
    */
    private Long batchValidationId;
    /**
    * 创建时间
    */
    private Long createDate;

    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*modifyDate get 方法 */
    public Long getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Long  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*createUsername get 方法 */
    public String getCreateUsername(){
        return createUsername;
    }
    /*createUsername set 方法 */
    public void setCreateUsername(String  createUsername){
        this.createUsername=createUsername;
    }
    /*effectiveTime get 方法 */
    public Long getEffectiveTime(){
        return effectiveTime;
    }
    /*effectiveTime set 方法 */
    public void setEffectiveTime(Long  effectiveTime){
        this.effectiveTime=effectiveTime;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*batchValidation get 方法 */
    public String getBatchValidation(){
        return batchValidation;
    }
    /*batchValidation set 方法 */
    public void setBatchValidation(String  batchValidation){
        this.batchValidation=batchValidation;
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
    public Long getBatchValidationIdId(){
        return batchValidationId;
    }
    /*id set 方法 */
    public void setBatchValidationIdId(Long  batchValidationId){
        this.batchValidationId=batchValidationId;
    }
    /*createDate get 方法 */
    public Long getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Long  createDate){
        this.createDate=createDate;
    }






}
