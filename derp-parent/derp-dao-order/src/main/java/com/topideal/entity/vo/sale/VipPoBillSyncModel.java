package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class VipPoBillSyncModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 仓库ID
    */
    private Long depotId;
    /**
    * 仓库名称
    */
    private String depotName;
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
    * PO号
    */
    private String poNo;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 未结算量(页面名称改为“库存量”）
    */
    private Integer unsettledAccount;
    /**
    * 初次上架时间
    */
    private Timestamp firstShelfDate;
    /**
    * 销售单价
    */
    private BigDecimal salePrice;
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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*unsettledAccount get 方法 */
    public Integer getUnsettledAccount(){
    return unsettledAccount;
    }
    /*unsettledAccount set 方法 */
    public void setUnsettledAccount(Integer  unsettledAccount){
    this.unsettledAccount=unsettledAccount;
    }
    /*firstShelfDate get 方法 */
    public Timestamp getFirstShelfDate(){
    return firstShelfDate;
    }
    /*firstShelfDate set 方法 */
    public void setFirstShelfDate(Timestamp  firstShelfDate){
    this.firstShelfDate=firstShelfDate;
    }
    /*salePrice get 方法 */
    public BigDecimal getSalePrice(){
    return salePrice;
    }
    /*salePrice set 方法 */
    public void setSalePrice(BigDecimal  salePrice){
    this.salePrice=salePrice;
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
