package com.topideal.entity.vo;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 库存加减接口来源单据表
 * @author lian_
 */
public class InventoryAddSubOrderModel extends PageModel implements Serializable{

     //业务单据号
     private String businessNo;
     //来源单据号
     private String orderNo;
     //id
     private Long id;
    //来源类型1-库存扣减 2-库存回滚
    private String sourceType;
     //创建日期
     private Timestamp createDate;

    /*businessNo get 方法 */
    public String getBusinessNo(){
        return businessNo;
    }
    /*businessNo set 方法 */
    public void setBusinessNo(String  businessNo){
        this.businessNo=businessNo;
    }
    /*orderNo get 方法 */
    public String getOrderNo(){
        return orderNo;
    }
    /*orderNo set 方法 */
    public void setOrderNo(String  orderNo){
        this.orderNo=orderNo;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}

