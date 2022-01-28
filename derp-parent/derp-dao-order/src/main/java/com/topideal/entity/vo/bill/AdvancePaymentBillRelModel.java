package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class AdvancePaymentBillRelModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 预售ID
    */
    private Long advancePaymentId;
    /**
    * 采购订单ID
    */
    private Long purchaseOrderId;
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
    /*advancePaymentId get 方法 */
    public Long getAdvancePaymentId(){
    return advancePaymentId;
    }
    /*advancePaymentId set 方法 */
    public void setAdvancePaymentId(Long  advancePaymentId){
    this.advancePaymentId=advancePaymentId;
    }
    /*purchaseOrderId get 方法 */
    public Long getPurchaseOrderId(){
    return purchaseOrderId;
    }
    /*purchaseOrderId set 方法 */
    public void setPurchaseOrderId(Long  purchaseOrderId){
    this.purchaseOrderId=purchaseOrderId;
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
