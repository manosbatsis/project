package com.topideal.entity.vo.sale;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PlatformPurchaseOrderModel extends PageModel implements Serializable{

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
    * 平台仓库名称
    */
    private String platformDepotName;
    /**
    * 客户id
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * PO号
    */
    private String poNo;
    /**
    * 下单时间
    */
    private Timestamp orderTime;
    /**
    * 入库时间
    */
    private Timestamp deliverDate;
    /**
    * 提交时间
    */
    private Timestamp submitDate;
    /**
    * 提交人
    */
    private Long submiter;
    /**
    * 提交人
    */
    private String submitName;
    /**
    * 提交时间
    */
    private Timestamp resaleDate;
    /**
    * 提交人
    */
    private Long resaler;
    /**
    * 提交人
    */
    private String resaleName;
    /**
    * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    /**
    * 金额
    */
    private BigDecimal amount;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 平台状态： 1.待发货确认、2.等待签收、3.等待入库、4.部分收货、5已完成
    */
    private String platformStatus;
    /**
    * 单据状态：1:待提交 2.未转销售,3:已转销售
    */
    private String status;
    /**
    * 销售订单号
    */
    private String saleCode;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //1. 京东 2.天猫
    private String orderSource;

    //账号
    private String userCode;

    //PR号
    private String prNo;

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
    /*platformDepotName get 方法 */
    public String getPlatformDepotName(){
    return platformDepotName;
    }
    /*platformDepotName set 方法 */
    public void setPlatformDepotName(String  platformDepotName){
    this.platformDepotName=platformDepotName;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*orderTime get 方法 */
    public Timestamp getOrderTime(){
    return orderTime;
    }
    /*orderTime set 方法 */
    public void setOrderTime(Timestamp  orderTime){
    this.orderTime=orderTime;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    this.deliverDate=deliverDate;
    }
    /*submitDate get 方法 */
    public Timestamp getSubmitDate(){
    return submitDate;
    }
    /*submitDate set 方法 */
    public void setSubmitDate(Timestamp  submitDate){
    this.submitDate=submitDate;
    }
    /*submiter get 方法 */
    public Long getSubmiter(){
    return submiter;
    }
    /*submiter set 方法 */
    public void setSubmiter(Long  submiter){
    this.submiter=submiter;
    }
    /*submitName get 方法 */
    public String getSubmitName(){
    return submitName;
    }
    /*submitName set 方法 */
    public void setSubmitName(String  submitName){
    this.submitName=submitName;
    }
    /*resaleDate get 方法 */
    public Timestamp getResaleDate(){
    return resaleDate;
    }
    /*resaleDate set 方法 */
    public void setResaleDate(Timestamp  resaleDate){
    this.resaleDate=resaleDate;
    }
    /*resaler get 方法 */
    public Long getResaler(){
    return resaler;
    }
    /*resaler set 方法 */
    public void setResaler(Long  resaler){
    this.resaler=resaler;
    }
    /*resaleName get 方法 */
    public String getResaleName(){
    return resaleName;
    }
    /*resaleName set 方法 */
    public void setResaleName(String  resaleName){
    this.resaleName=resaleName;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*platformStatus get 方法 */
    public String getPlatformStatus(){
    return platformStatus;
    }
    /*platformStatus set 方法 */
    public void setPlatformStatus(String  platformStatus){
    this.platformStatus=platformStatus;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*saleCode get 方法 */
    public String getSaleCode(){
    return saleCode;
    }
    /*saleCode set 方法 */
    public void setSaleCode(String  saleCode){
    this.saleCode=saleCode;
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

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPrNo() {
        return prNo;
    }

    public void setPrNo(String prNo) {
        this.prNo = prNo;
    }
}
