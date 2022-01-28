package com.topideal.entity.vo.platformdata;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class OrealPurchaseOrderModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
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
    * 供应商
    */
    private String custname;
    /**
    * 订单编号
    */
    private String vordercode;
    /**
    * 品牌
    */
    private String vdef1;
    /**
    * 订单日期
    */
    private Timestamp dorderdate;
    /**
    * 业务类型
    */
    private String vdef13;
    /**
    * 收货地址
    */
    private String adress;
    /**
    * 来源:1.欧莱雅接口
    */
    private String source;
    /**
    * 采购订单类型
    */
    private String docname;
    /**
    * CSR确认单号
    */
    private String vdef7;
    /**
    * 创建日期
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
    /*custname get 方法 */
    public String getCustname(){
    return custname;
    }
    /*custname set 方法 */
    public void setCustname(String  custname){
    this.custname=custname;
    }
    /*vordercode get 方法 */
    public String getVordercode(){
    return vordercode;
    }
    /*vordercode set 方法 */
    public void setVordercode(String  vordercode){
    this.vordercode=vordercode;
    }
    /*vdef1 get 方法 */
    public String getVdef1(){
    return vdef1;
    }
    /*vdef1 set 方法 */
    public void setVdef1(String  vdef1){
    this.vdef1=vdef1;
    }
    /*dorderdate get 方法 */
    public Timestamp getDorderdate(){
    return dorderdate;
    }
    /*dorderdate set 方法 */
    public void setDorderdate(Timestamp  dorderdate){
    this.dorderdate=dorderdate;
    }
    /*vdef13 get 方法 */
    public String getVdef13(){
    return vdef13;
    }
    /*vdef13 set 方法 */
    public void setVdef13(String  vdef13){
    this.vdef13=vdef13;
    }
    /*adress get 方法 */
    public String getAdress(){
    return adress;
    }
    /*adress set 方法 */
    public void setAdress(String  adress){
    this.adress=adress;
    }
    /*source get 方法 */
    public String getSource(){
    return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
    this.source=source;
    }
    /*docname get 方法 */
    public String getDocname(){
    return docname;
    }
    /*docname set 方法 */
    public void setDocname(String  docname){
    this.docname=docname;
    }
    /*vdef7 get 方法 */
    public String getVdef7(){
    return vdef7;
    }
    /*vdef7 set 方法 */
    public void setVdef7(String  vdef7){
    this.vdef7=vdef7;
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
