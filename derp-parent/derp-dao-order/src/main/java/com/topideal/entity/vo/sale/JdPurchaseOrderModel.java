package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class JdPurchaseOrderModel extends PageModel implements Serializable{

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
    * 采购单号
    */
    private String orderId;
    /**
    * 账号
    */
    private String userCode;
    /**
    * 采购金额
    */
    private BigDecimal totalPrice;
    /**
    * 订单属性
    */
    private String orderAttributeName;
    /**
    * 订购时间
    */
    private Timestamp jdCreatedDate;
    /**
    * 入库时间
    */
    private Timestamp storageTime;
    /**
    * 入库模式
    */
    private String poTypeName;
    /**
    * 订单来源
    */
    private String sourceName;
    /**
    * 订购城市
    */
    private String deliverCenterName;
    /**
    * 采购员
    */
    private String purchaserName;
    /**
    * 订单状态
    */
    private String stateName;
    /**
    * 回告状态
    */
    private String confirmStateName;
    /**
    * 品种数量
    */
    private Integer wareVariety;
    /**
    * TC转运
    */
    private String tcFlagName;
    /**
    * 预约时间
    */
    private Timestamp bookTime;
    /**
    * 贴码建议
    */
    private String markFlagDesc;
    /**
    * 京东贴码数量
    */
    private Integer markTotalNum;
    /**
    * 波次数量
    */
    private Integer stockNum;
    /**
    * 预计关单时间
    */
    private Timestamp closeOrderExpectTime;
    /**
    * 
    */
    private String currency;
    /**
    * 实际应收金额
    */
    private BigDecimal actualAmount;
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
    /*orderId get 方法 */
    public String getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(String  orderId){
    this.orderId=orderId;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*totalPrice get 方法 */
    public BigDecimal getTotalPrice(){
    return totalPrice;
    }
    /*totalPrice set 方法 */
    public void setTotalPrice(BigDecimal  totalPrice){
    this.totalPrice=totalPrice;
    }
    /*orderAttributeName get 方法 */
    public String getOrderAttributeName(){
    return orderAttributeName;
    }
    /*orderAttributeName set 方法 */
    public void setOrderAttributeName(String  orderAttributeName){
    this.orderAttributeName=orderAttributeName;
    }
    /*jdCreatedDate get 方法 */
    public Timestamp getJdCreatedDate(){
    return jdCreatedDate;
    }
    /*jdCreatedDate set 方法 */
    public void setJdCreatedDate(Timestamp  jdCreatedDate){
    this.jdCreatedDate=jdCreatedDate;
    }
    /*storageTime get 方法 */
    public Timestamp getStorageTime(){
    return storageTime;
    }
    /*storageTime set 方法 */
    public void setStorageTime(Timestamp  storageTime){
    this.storageTime=storageTime;
    }
    /*poTypeName get 方法 */
    public String getPoTypeName(){
    return poTypeName;
    }
    /*poTypeName set 方法 */
    public void setPoTypeName(String  poTypeName){
    this.poTypeName=poTypeName;
    }
    /*sourceName get 方法 */
    public String getSourceName(){
    return sourceName;
    }
    /*sourceName set 方法 */
    public void setSourceName(String  sourceName){
    this.sourceName=sourceName;
    }
    /*deliverCenterName get 方法 */
    public String getDeliverCenterName(){
    return deliverCenterName;
    }
    /*deliverCenterName set 方法 */
    public void setDeliverCenterName(String  deliverCenterName){
    this.deliverCenterName=deliverCenterName;
    }
    /*purchaserName get 方法 */
    public String getPurchaserName(){
    return purchaserName;
    }
    /*purchaserName set 方法 */
    public void setPurchaserName(String  purchaserName){
    this.purchaserName=purchaserName;
    }
    /*stateName get 方法 */
    public String getStateName(){
    return stateName;
    }
    /*stateName set 方法 */
    public void setStateName(String  stateName){
    this.stateName=stateName;
    }
    /*confirmStateName get 方法 */
    public String getConfirmStateName(){
    return confirmStateName;
    }
    /*confirmStateName set 方法 */
    public void setConfirmStateName(String  confirmStateName){
    this.confirmStateName=confirmStateName;
    }
    /*wareVariety get 方法 */
    public Integer getWareVariety(){
    return wareVariety;
    }
    /*wareVariety set 方法 */
    public void setWareVariety(Integer  wareVariety){
    this.wareVariety=wareVariety;
    }
    /*tcFlagName get 方法 */
    public String getTcFlagName(){
    return tcFlagName;
    }
    /*tcFlagName set 方法 */
    public void setTcFlagName(String  tcFlagName){
    this.tcFlagName=tcFlagName;
    }
    /*bookTime get 方法 */
    public Timestamp getBookTime(){
    return bookTime;
    }
    /*bookTime set 方法 */
    public void setBookTime(Timestamp  bookTime){
    this.bookTime=bookTime;
    }
    /*markFlagDesc get 方法 */
    public String getMarkFlagDesc(){
    return markFlagDesc;
    }
    /*markFlagDesc set 方法 */
    public void setMarkFlagDesc(String  markFlagDesc){
    this.markFlagDesc=markFlagDesc;
    }
    /*markTotalNum get 方法 */
    public Integer getMarkTotalNum(){
    return markTotalNum;
    }
    /*markTotalNum set 方法 */
    public void setMarkTotalNum(Integer  markTotalNum){
    this.markTotalNum=markTotalNum;
    }
    /*stockNum get 方法 */
    public Integer getStockNum(){
    return stockNum;
    }
    /*stockNum set 方法 */
    public void setStockNum(Integer  stockNum){
    this.stockNum=stockNum;
    }
    /*closeOrderExpectTime get 方法 */
    public Timestamp getCloseOrderExpectTime(){
    return closeOrderExpectTime;
    }
    /*closeOrderExpectTime set 方法 */
    public void setCloseOrderExpectTime(Timestamp  closeOrderExpectTime){
    this.closeOrderExpectTime=closeOrderExpectTime;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*actualAmount get 方法 */
    public BigDecimal getActualAmount(){
    return actualAmount;
    }
    /*actualAmount set 方法 */
    public void setActualAmount(BigDecimal  actualAmount){
    this.actualAmount=actualAmount;
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
