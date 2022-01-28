package com.topideal.entity.vo.purchase;
import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

import com.topideal.common.system.ibatis.PageModel;
/**
 * 电商订单外部单号来源表
 * @author lian_
 *
 */
public class OrderExternalCodeModel extends PageModel implements Serializable{

     //id
     private Long id;
     //外部单号
     private String externalCode;
     //创建时间
     private Timestamp createDate;

     //订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库 6.采购退货 7.采购入库 8.销售退
     private Integer orderSource;

    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*externalCode get 方法 */
    public String getExternalCode(){
        return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
        this.externalCode=externalCode;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }
}

