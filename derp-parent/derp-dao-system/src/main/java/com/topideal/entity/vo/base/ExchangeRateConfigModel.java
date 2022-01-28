package com.topideal.entity.vo.base;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class ExchangeRateConfigModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 原币种编码
    */
    private String origCurrencyCode;
    /**
    * 原币种名称
    */
    private String origCurrencyName;
    /**
    * 目标币种编码
    */
    private String tgtCurrencyCode;
    /**
    * 目标币种名称
    */
    private String tgtCurrencyName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 创建人id
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 数据来源： WGJ-外管局  DBS-星展银行  SGCJ-手工创建
    */
    private String dataSource;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*origCurrencyCode get 方法 */
    public String getOrigCurrencyCode(){
    return origCurrencyCode;
    }
    /*origCurrencyCode set 方法 */
    public void setOrigCurrencyCode(String  origCurrencyCode){
    this.origCurrencyCode=origCurrencyCode;
    }
    /*origCurrencyName get 方法 */
    public String getOrigCurrencyName(){
    return origCurrencyName;
    }
    /*origCurrencyName set 方法 */
    public void setOrigCurrencyName(String  origCurrencyName){
    this.origCurrencyName=origCurrencyName;
    }
    /*tgtCurrencyCode get 方法 */
    public String getTgtCurrencyCode(){
    return tgtCurrencyCode;
    }
    /*tgtCurrencyCode set 方法 */
    public void setTgtCurrencyCode(String  tgtCurrencyCode){
    this.tgtCurrencyCode=tgtCurrencyCode;
    }
    /*tgtCurrencyName get 方法 */
    public String getTgtCurrencyName(){
    return tgtCurrencyName;
    }
    /*tgtCurrencyName set 方法 */
    public void setTgtCurrencyName(String  tgtCurrencyName){
    this.tgtCurrencyName=tgtCurrencyName;
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
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*dataSource get 方法 */
    public String getDataSource(){
    return dataSource;
    }
    /*dataSource set 方法 */
    public void setDataSource(String  dataSource){
    this.dataSource=dataSource;
    }






}
