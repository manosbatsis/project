package com.topideal.entity.vo.purchase;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PurchaseReturnRelModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购订单id
    */
    private Long purchaseId;
    /**
    * 采购退订单id
    */
    private Long purchaseReturnId;
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
    /*purchaseId get 方法 */
    public Long getPurchaseId(){
    return purchaseId;
    }
    /*purchaseId set 方法 */
    public void setPurchaseId(Long  purchaseId){
    this.purchaseId=purchaseId;
    }
    /*purchaseReturnId get 方法 */
    public Long getPurchaseReturnId(){
    return purchaseReturnId;
    }
    /*purchaseReturnId set 方法 */
    public void setPurchaseReturnId(Long  purchaseReturnId){
    this.purchaseReturnId=purchaseReturnId;
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
