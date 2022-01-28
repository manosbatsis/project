package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

public class TmallscmPurchaseOrderModel extends PageModel implements Serializable{

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
    * 客户id
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 账号
    */
    private String userCode;
    /**
    * 采购单编号
    */
    private String bizId;
    /**
    * 状态
    */
    private Integer bizStatus;
    /**
    * 状态名称
    */
    private String bizStatusDesc;
    /**
    * 币别
    */
    private String currency;
    /**
    * 币别名称
    */
    private String currencyDesc;
    /**
    * 经营模式
    */
    private String marketingType;
    /**
    * 经营模式名称
    */
    private String marketingTypeDesc;
    /**
    * 仓库
    */
    private String warehouse;
    /**
    * 仓库名称
    */
    private String warehouseName;
    /**
    * 期望货权交接时间
    */
    private Date expectOwnershipTransferTime;
    /**
    * 创建时间
    */
    private Timestamp gmtCreate;
    /**
    * 实际入库时间
    */
    private Timestamp gmtReceiveFinish;
    /**
    * 金额
    */
    private BigDecimal totalAmount;
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*bizId get 方法 */
    public String getBizId(){
    return bizId;
    }
    /*bizId set 方法 */
    public void setBizId(String  bizId){
    this.bizId=bizId;
    }
    /*bizStatus get 方法 */
    public Integer getBizStatus(){
    return bizStatus;
    }
    /*bizStatus set 方法 */
    public void setBizStatus(Integer  bizStatus){
    this.bizStatus=bizStatus;
    }
    /*bizStatusDesc get 方法 */
    public String getBizStatusDesc(){
    return bizStatusDesc;
    }
    /*bizStatusDesc set 方法 */
    public void setBizStatusDesc(String  bizStatusDesc){
    this.bizStatusDesc=bizStatusDesc;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*currencyDesc get 方法 */
    public String getCurrencyDesc(){
    return currencyDesc;
    }
    /*currencyDesc set 方法 */
    public void setCurrencyDesc(String  currencyDesc){
    this.currencyDesc=currencyDesc;
    }
    /*marketingType get 方法 */
    public String getMarketingType(){
    return marketingType;
    }
    /*marketingType set 方法 */
    public void setMarketingType(String  marketingType){
    this.marketingType=marketingType;
    }
    /*marketingTypeDesc get 方法 */
    public String getMarketingTypeDesc(){
    return marketingTypeDesc;
    }
    /*marketingTypeDesc set 方法 */
    public void setMarketingTypeDesc(String  marketingTypeDesc){
    this.marketingTypeDesc=marketingTypeDesc;
    }
    /*warehouse get 方法 */
    public String getWarehouse(){
    return warehouse;
    }
    /*warehouse set 方法 */
    public void setWarehouse(String  warehouse){
    this.warehouse=warehouse;
    }
    /*warehouseName get 方法 */
    public String getWarehouseName(){
    return warehouseName;
    }
    /*warehouseName set 方法 */
    public void setWarehouseName(String  warehouseName){
    this.warehouseName=warehouseName;
    }
    /*expectOwnershipTransferTime get 方法 */
    public Date getExpectOwnershipTransferTime(){
    return expectOwnershipTransferTime;
    }
    /*expectOwnershipTransferTime set 方法 */
    public void setExpectOwnershipTransferTime(Date  expectOwnershipTransferTime){
    this.expectOwnershipTransferTime=expectOwnershipTransferTime;
    }
    /*gmtCreate get 方法 */
    public Timestamp getGmtCreate(){
    return gmtCreate;
    }
    /*gmtCreate set 方法 */
    public void setGmtCreate(Timestamp  gmtCreate){
    this.gmtCreate=gmtCreate;
    }
    /*gmtReceiveFinish get 方法 */
    public Timestamp getGmtReceiveFinish(){
    return gmtReceiveFinish;
    }
    /*gmtReceiveFinish set 方法 */
    public void setGmtReceiveFinish(Timestamp  gmtReceiveFinish){
    this.gmtReceiveFinish=gmtReceiveFinish;
    }
    /*totalAmount get 方法 */
    public BigDecimal getTotalAmount(){
    return totalAmount;
    }
    /*totalAmount set 方法 */
    public void setTotalAmount(BigDecimal  totalAmount){
    this.totalAmount=totalAmount;
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
