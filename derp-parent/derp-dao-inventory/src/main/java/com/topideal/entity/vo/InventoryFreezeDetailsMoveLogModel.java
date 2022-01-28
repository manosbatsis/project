package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

/**
 * 库存冻结明细移动日志
 * @author lian_
 *
 */
public class InventoryFreezeDetailsMoveLogModel extends PageModel implements Serializable{

     //库存冻结明细id
     private Long freezeDetailsId;
     //仓库名称
     private String depotName;
     //来源单据号
     private String orderNo;
     //修改时间
     private Timestamp modifyDate;
     //商家ID
     private Long merchantId;
     //仓库id
     private Long depotId;
     //id
     private Long id;
     //类型(1需要移动，2异常)
     private String type;
     //异常原因
     private String expMsg;
     //商家名称
     private String merchantName;
     //创建时间
     private Timestamp createDate;
     /**
      * 业务单号
      */
      private String businessNo;

    public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	/*freezeDetailsId get 方法 */
    public Long getFreezeDetailsId(){
        return freezeDetailsId;
    }
    /*freezeDetailsId set 方法 */
    public void setFreezeDetailsId(Long  freezeDetailsId){
        this.freezeDetailsId=freezeDetailsId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
        return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
        this.depotName=depotName;
    }
    /*orderNo get 方法 */
    public String getOrderNo(){
        return orderNo;
    }
    /*orderNo set 方法 */
    public void setOrderNo(String  orderNo){
        this.orderNo=orderNo;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
        return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
        this.merchantId=merchantId;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
        return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
        this.depotId=depotId;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*type get 方法 */
    public String getType(){
        return type;
    }
    /*type set 方法 */
    public void setType(String  type){
        this.type=type;
    }
    /*expMsg get 方法 */
    public String getExpMsg(){
        return expMsg;
    }
    /*expMsg set 方法 */
    public void setExpMsg(String  expMsg){
        this.expMsg=expMsg;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
        return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
        this.merchantName=merchantName;
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
