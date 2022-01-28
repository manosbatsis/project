package com.topideal.entity.vo.purchase;
import java.io.Serializable;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 预申报单关联采购订单表
 * @author lianchenxing
 *
 */
public class DeclarePurchaseRelModel extends PageModel implements Serializable{

     //预申报单ID
     private Long declareOrderId;
     //id
     private Long id;
     //采购订单ID
     private Long purchaseOrderId;

    /*declareOrderId get 方法 */
    public Long getDeclareOrderId(){
        return declareOrderId;
    }
    /*declareOrderId set 方法 */
    public void setDeclareOrderId(Long  declareOrderId){
        this.declareOrderId=declareOrderId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*purchaseOrderId get 方法 */
    public Long getPurchaseOrderId(){
        return purchaseOrderId;
    }
    /*purchaseOrderId set 方法 */
    public void setPurchaseOrderId(Long  purchaseOrderId){
        this.purchaseOrderId=purchaseOrderId;
    }






}

