package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class CrawlerVipExtraDataModel extends PageModel implements Serializable{

    /**
    * 自增ID
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
    * 商家编码
    */
    private String merchantCode;
    /**
    * 卓志编码
    */
    private String topidealCode;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 客户编码
    */
    private String customerCode;
    /**
    * 销售订单号
    */
    private String saleOrderCode;
    /**
    * 采购单号
    */
    private String poNo;
    /**
    * 供应商编号
    */
    private String vendorNumber;
    /**
    * 供应商名称
    */
    private String vendorName;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 货号 唯品/云集sku-对应经销货号/条码/对照表
    */
    private String goodsNo;
    /**
    * 商品名
    */
    private String goodsName;
    /**
    * 币种
    */
    private String currencyCode;
    /**
    * 数量
    */
    private Integer quantity;
    /**
    * 金额
    */
    private BigDecimal amount;
    /**
    * 活动折扣明细-活动折扣,客退补贴明细-客退补贴,补偿折扣明细-补偿折扣
    */
    private String processingType;
    /**
    * 账单编码
    */
    private String billCode;
    /**
    * 账号
    */
    private String userCode;
    /**
    * 原ID
    */
    private Integer oldId;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 0 ：未使用 ，1 ：已使用
    */
    private String isUsed;
    /**
    * 平台费用单号
    */
    private String platformCostCode;
    /**
     * 成功或失败原因
     */
    private String reason;
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
    /*merchantCode get 方法 */
    public String getMerchantCode(){
    return merchantCode;
    }
    /*merchantCode set 方法 */
    public void setMerchantCode(String  merchantCode){
    this.merchantCode=merchantCode;
    }
    /*topidealCode get 方法 */
    public String getTopidealCode(){
    return topidealCode;
    }
    /*topidealCode set 方法 */
    public void setTopidealCode(String  topidealCode){
    this.topidealCode=topidealCode;
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
    /*customerCode get 方法 */
    public String getCustomerCode(){
    return customerCode;
    }
    /*customerCode set 方法 */
    public void setCustomerCode(String  customerCode){
    this.customerCode=customerCode;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*vendorNumber get 方法 */
    public String getVendorNumber(){
    return vendorNumber;
    }
    /*vendorNumber set 方法 */
    public void setVendorNumber(String  vendorNumber){
    this.vendorNumber=vendorNumber;
    }
    /*vendorName get 方法 */
    public String getVendorName(){
    return vendorName;
    }
    /*vendorName set 方法 */
    public void setVendorName(String  vendorName){
    this.vendorName=vendorName;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*currencyCode get 方法 */
    public String getCurrencyCode(){
    return currencyCode;
    }
    /*currencyCode set 方法 */
    public void setCurrencyCode(String  currencyCode){
    this.currencyCode=currencyCode;
    }
    /*quantity get 方法 */
    public Integer getQuantity(){
    return quantity;
    }
    /*quantity set 方法 */
    public void setQuantity(Integer  quantity){
    this.quantity=quantity;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*processingType get 方法 */
    public String getProcessingType(){
    return processingType;
    }
    /*processingType set 方法 */
    public void setProcessingType(String  processingType){
    this.processingType=processingType;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*oldId get 方法 */
    public Integer getOldId(){
    return oldId;
    }
    /*oldId set 方法 */
    public void setOldId(Integer  oldId){
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
    /*isUsed get 方法 */
    public String getIsUsed(){
    return isUsed;
    }
    /*isUsed set 方法 */
    public void setIsUsed(String  isUsed){
    this.isUsed=isUsed;
    }
    /*platformCostCode get 方法 */
    public String getPlatformCostCode(){
    return platformCostCode;
    }
    /*platformCostCode set 方法 */
    public void setPlatformCostCode(String  platformCostCode){
    this.platformCostCode=platformCostCode;
    }
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}






}
