package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class ReceiveCloseAccountsModel extends PageModel implements Serializable{

    /**
    * id主键
    */
    private Long id;
    /**
    * 公司id
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 应收月份
    */
    private String month;
    /**
    * 期初时间
    */
    private Timestamp firstDate;
    /**
    * 期末时间
    */
    private Timestamp endDate;
    /**
    * 关账时间
    */
    private Timestamp closeDate;
    /**
    * 关账状态 029-未关账 030-已关账
    */
    private String status;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*firstDate get 方法 */
    public Timestamp getFirstDate(){
    return firstDate;
    }
    /*firstDate set 方法 */
    public void setFirstDate(Timestamp  firstDate){
    this.firstDate=firstDate;
    }
    /*endDate get 方法 */
    public Timestamp getEndDate(){
    return endDate;
    }
    /*endDate set 方法 */
    public void setEndDate(Timestamp  endDate){
    this.endDate=endDate;
    }
    /*closeDate get 方法 */
    public Timestamp getCloseDate(){
    return closeDate;
    }
    /*closeDate set 方法 */
    public void setCloseDate(Timestamp  closeDate){
    this.closeDate=closeDate;
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
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }






}
