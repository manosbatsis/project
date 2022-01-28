package com.topideal.entity.vo.common;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class SdPurchaseConfigModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名
    */
    private String merchantName;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 供应商ID
    */
    private Long supplierId;
    /**
    * 供应商名
    */
    private String supplierName;
    /**
    * 生效时间
    */
    private Timestamp effectiveTime;
    /**
    * 失效时间
    */
    private Timestamp invalidTime;
    /**
    * 状态 0-未审核 1-已审核
    */
    private String status;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 创建人
    */
    private String creator;
    /**
    * 创建人ID
    */
    private Long creatorId;
    /**
    * 审核时间
    */
    private Timestamp examineDate;
    /**
    * 审核人
    */
    private String examiner;
    /**
    * 审核人ID
    */
    private Long examinerId;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    this.merchantId=merchantId;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    this.merchantName=merchantName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*supplierId get 方法 */
    public Long getSupplierId(){
    return supplierId;
    }
    /*supplierId set 方法 */
    public void setSupplierId(Long  supplierId){
    this.supplierId=supplierId;
    }
    /*supplierName get 方法 */
    public String getSupplierName(){
    return supplierName;
    }
    /*supplierName set 方法 */
    public void setSupplierName(String  supplierName){
    this.supplierName=supplierName;
    }
    /*effectiveTime get 方法 */
    public Timestamp getEffectiveTime(){
    return effectiveTime;
    }
    /*effectiveTime set 方法 */
    public void setEffectiveTime(Timestamp  effectiveTime){
    this.effectiveTime=effectiveTime;
    }
    /*invalidTime get 方法 */
    public Timestamp getInvalidTime(){
    return invalidTime;
    }
    /*invalidTime set 方法 */
    public void setInvalidTime(Timestamp  invalidTime){
    this.invalidTime=invalidTime;
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
    /*creator get 方法 */
    public String getCreator(){
    return creator;
    }
    /*creator set 方法 */
    public void setCreator(String  creator){
    this.creator=creator;
    }
    /*creatorId get 方法 */
    public Long getCreatorId(){
    return creatorId;
    }
    /*creatorId set 方法 */
    public void setCreatorId(Long  creatorId){
    this.creatorId=creatorId;
    }
    /*examineDate get 方法 */
    public Timestamp getExamineDate(){
    return examineDate;
    }
    /*examineDate set 方法 */
    public void setExamineDate(Timestamp  examineDate){
    this.examineDate=examineDate;
    }
    /*examiner get 方法 */
    public String getExaminer(){
    return examiner;
    }
    /*examiner set 方法 */
    public void setExaminer(String  examiner){
    this.examiner=examiner;
    }
    /*examinerId get 方法 */
    public Long getExaminerId(){
    return examinerId;
    }
    /*examinerId set 方法 */
    public void setExaminerId(Long  examinerId){
    this.examinerId=examinerId;
    }






}
