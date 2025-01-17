package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class YunjiDeliveryDetailModel extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 结算单号
    */
    private String settleId;
    /**
    * 订单编号
    */
    private String orderId;
    /**
    * 原始单号
    */
    private String srcOrderId;
    /**
    * 商品编码
    */
    private String skuNo;
    /**
    * 商品名称
    */
    private String itemName;
    /**
    * 商品规格
    */
    private String normName;
    /**
    * 支付时间
    */
    private Timestamp payTime;
    /**
    * 发货时间
    */
    private Timestamp wmsdelivertime;
    /**
    * 实发数
    */
    private Integer qty;
    /**
    * 结算单价
    */
    private BigDecimal settlementPrice;
    /**
    * 税率
    */
    private BigDecimal taxRate;
    /**
    * 结算总价
    */
    private BigDecimal settlementTotalPrice;
    /**
    * 结算税额
    */
    private BigDecimal settlementTaxPrice;
    /**
    * 筛选数据时间
    */
    private Timestamp businessData;
    /**
    * 
    */
    private String userCode;
    /**
    * 0 ：未使用 ，1 ：已试用
    */
    private String isUsed;
    /**
    * 厦门库创建时间
    */
    private Timestamp createTime;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 爬虫库原ID
    */
    private Long oldId;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 成功或失败原因
    */
    private String reason;
    /**
    * 账单出入库单号
    */
    private String billOutinCode;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*settleId get 方法 */
    public String getSettleId(){
    return settleId;
    }
    /*settleId set 方法 */
    public void setSettleId(String  settleId){
    this.settleId=settleId;
    }
    /*orderId get 方法 */
    public String getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(String  orderId){
    this.orderId=orderId;
    }
    /*srcOrderId get 方法 */
    public String getSrcOrderId(){
    return srcOrderId;
    }
    /*srcOrderId set 方法 */
    public void setSrcOrderId(String  srcOrderId){
    this.srcOrderId=srcOrderId;
    }
    /*skuNo get 方法 */
    public String getSkuNo(){
    return skuNo;
    }
    /*skuNo set 方法 */
    public void setSkuNo(String  skuNo){
    this.skuNo=skuNo;
    }
    /*itemName get 方法 */
    public String getItemName(){
    return itemName;
    }
    /*itemName set 方法 */
    public void setItemName(String  itemName){
    this.itemName=itemName;
    }
    /*normName get 方法 */
    public String getNormName(){
    return normName;
    }
    /*normName set 方法 */
    public void setNormName(String  normName){
    this.normName=normName;
    }
    /*payTime get 方法 */
    public Timestamp getPayTime(){
    return payTime;
    }
    /*payTime set 方法 */
    public void setPayTime(Timestamp  payTime){
    this.payTime=payTime;
    }
    /*wmsdelivertime get 方法 */
    public Timestamp getWmsdelivertime(){
    return wmsdelivertime;
    }
    /*wmsdelivertime set 方法 */
    public void setWmsdelivertime(Timestamp  wmsdelivertime){
    this.wmsdelivertime=wmsdelivertime;
    }
    /*qty get 方法 */
    public Integer getQty(){
    return qty;
    }
    /*qty set 方法 */
    public void setQty(Integer  qty){
    this.qty=qty;
    }
    /*settlementPrice get 方法 */
    public BigDecimal getSettlementPrice(){
    return settlementPrice;
    }
    /*settlementPrice set 方法 */
    public void setSettlementPrice(BigDecimal  settlementPrice){
    this.settlementPrice=settlementPrice;
    }
    /*taxRate get 方法 */
    public BigDecimal getTaxRate(){
    return taxRate;
    }
    /*taxRate set 方法 */
    public void setTaxRate(BigDecimal  taxRate){
    this.taxRate=taxRate;
    }
    /*settlementTotalPrice get 方法 */
    public BigDecimal getSettlementTotalPrice(){
    return settlementTotalPrice;
    }
    /*settlementTotalPrice set 方法 */
    public void setSettlementTotalPrice(BigDecimal  settlementTotalPrice){
    this.settlementTotalPrice=settlementTotalPrice;
    }
    /*settlementTaxPrice get 方法 */
    public BigDecimal getSettlementTaxPrice(){
    return settlementTaxPrice;
    }
    /*settlementTaxPrice set 方法 */
    public void setSettlementTaxPrice(BigDecimal  settlementTaxPrice){
    this.settlementTaxPrice=settlementTaxPrice;
    }
    /*businessData get 方法 */
    public Timestamp getBusinessData(){
    return businessData;
    }
    /*businessData set 方法 */
    public void setBusinessData(Timestamp  businessData){
    this.businessData=businessData;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*isUsed get 方法 */
    public String getIsUsed(){
    return isUsed;
    }
    /*isUsed set 方法 */
    public void setIsUsed(String  isUsed){
    this.isUsed=isUsed;
    }
    /*createTime get 方法 */
    public Timestamp getCreateTime(){
    return createTime;
    }
    /*createTime set 方法 */
    public void setCreateTime(Timestamp  createTime){
    this.createTime=createTime;
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
    /*oldId get 方法 */
    public Long getOldId(){
    return oldId;
    }
    /*oldId set 方法 */
    public void setOldId(Long  oldId){
    this.oldId=oldId;
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
    /*reason get 方法 */
    public String getReason(){
    return reason;
    }
    /*reason set 方法 */
    public void setReason(String  reason){
    this.reason=reason;
    }
    /*billOutinCode get 方法 */
    public String getBillOutinCode(){
    return billOutinCode;
    }
    /*billOutinCode set 方法 */
    public void setBillOutinCode(String  billOutinCode){
    this.billOutinCode=billOutinCode;
    }






}
