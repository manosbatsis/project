package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ReceiveInvoicenoModel extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 发票号前缀
    */
    private String invoiceNoPrefix;
    /**
    * 发票值
    */
    private Long value;
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
    /*invoiceNoPrefix get 方法 */
    public String getInvoiceNoPrefix(){
    return invoiceNoPrefix;
    }
    /*invoiceNoPrefix set 方法 */
    public void setInvoiceNoPrefix(String  invoiceNoPrefix){
    this.invoiceNoPrefix=invoiceNoPrefix;
    }
    /*value get 方法 */
    public Long getValue(){
    return value;
    }
    /*value set 方法 */
    public void setValue(Long  value){
    this.value=value;
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
