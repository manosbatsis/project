package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class YunjiAccountDataModel extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 结算单号
    */
    private String settleId;
    /**
    * 供应商编码
    */
    private String supplierCode;
    /**
    * 供应商名称
    */
    private String supplierName;
    /**
    * 1 ：国内贸易 ，2 ：一般贸易，3 ：跨境贸易
    */
    private String goodsType;
    /**
    * 结算周期开始日期
    */
    private Date businessStartDate;
    /**
    * 结算周期结束日期
    */
    private Date businessEndDate;
    /**
    * 结算货币
    */
    private String currencyType;
    /**
    * 0 ：其他， 1 ：特卖 ，2 ：商城，3 ：超市
    */
    private String departmentType;
    /**
    * 结算总金额
    */
    private BigDecimal settlementPriceTotal;
    /**
    * 费用合计金额
    */
    private BigDecimal totalCostPrice;
    /**
    * 应结算金额
    */
    private BigDecimal finalPriceTotal;
    /**
    * 结算主体
    */
    private String mechanismCode;
    /**
    * 结算日期
    */
    private Date settleDate;
    /**
    * 
    */
    private String userCode;
    /**
    * 0 ：已作废， 1 ：待确认 ，2 ：待开票，3 ：待收票，4 ：待结算，5 ：已结算
    */
    private String status;
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
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    //文件key
    private String fileKey;

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
    /*supplierCode get 方法 */
    public String getSupplierCode(){
    return supplierCode;
    }
    /*supplierCode set 方法 */
    public void setSupplierCode(String  supplierCode){
    this.supplierCode=supplierCode;
    }
    /*supplierName get 方法 */
    public String getSupplierName(){
    return supplierName;
    }
    /*supplierName set 方法 */
    public void setSupplierName(String  supplierName){
    this.supplierName=supplierName;
    }
    /*goodsType get 方法 */
    public String getGoodsType(){
    return goodsType;
    }
    /*goodsType set 方法 */
    public void setGoodsType(String  goodsType){
    this.goodsType=goodsType;
    }
    /*businessStartDate get 方法 */
    public Date getBusinessStartDate(){
    return businessStartDate;
    }
    /*businessStartDate set 方法 */
    public void setBusinessStartDate(Date  businessStartDate){
    this.businessStartDate=businessStartDate;
    }
    /*businessEndDate get 方法 */
    public Date getBusinessEndDate(){
    return businessEndDate;
    }
    /*businessEndDate set 方法 */
    public void setBusinessEndDate(Date  businessEndDate){
    this.businessEndDate=businessEndDate;
    }
    /*currencyType get 方法 */
    public String getCurrencyType(){
    return currencyType;
    }
    /*currencyType set 方法 */
    public void setCurrencyType(String  currencyType){
    this.currencyType=currencyType;
    }
    /*departmentType get 方法 */
    public String getDepartmentType(){
    return departmentType;
    }
    /*departmentType set 方法 */
    public void setDepartmentType(String  departmentType){
    this.departmentType=departmentType;
    }
    /*settlementPriceTotal get 方法 */
    public BigDecimal getSettlementPriceTotal(){
    return settlementPriceTotal;
    }
    /*settlementPriceTotal set 方法 */
    public void setSettlementPriceTotal(BigDecimal  settlementPriceTotal){
    this.settlementPriceTotal=settlementPriceTotal;
    }
    /*totalCostPrice get 方法 */
    public BigDecimal getTotalCostPrice(){
    return totalCostPrice;
    }
    /*totalCostPrice set 方法 */
    public void setTotalCostPrice(BigDecimal  totalCostPrice){
    this.totalCostPrice=totalCostPrice;
    }
    /*finalPriceTotal get 方法 */
    public BigDecimal getFinalPriceTotal(){
    return finalPriceTotal;
    }
    /*finalPriceTotal set 方法 */
    public void setFinalPriceTotal(BigDecimal  finalPriceTotal){
    this.finalPriceTotal=finalPriceTotal;
    }
    /*mechanismCode get 方法 */
    public String getMechanismCode(){
    return mechanismCode;
    }
    /*mechanismCode set 方法 */
    public void setMechanismCode(String  mechanismCode){
    this.mechanismCode=mechanismCode;
    }
    /*settleDate get 方法 */
    public Date getSettleDate(){
    return settleDate;
    }
    /*settleDate set 方法 */
    public void setSettleDate(Date  settleDate){
    this.settleDate=settleDate;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
