package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class PlatformStatementOrderModel extends PageModel implements Serializable{

    /**
    * 自增ID
    */
    private Long id;
    /**
    * 平台账单号
    */
    private String billCode;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名
    */
    private String merchantName;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 客户类型 1-云集 2-唯品 3-天猫
    */
    private String customerType;
    /**
    * 币种
    */
    private String currency;
    /**
    * 月份
    */
    private String month;
    /**
    * 账单金额
    */
    private BigDecimal billAmount;
    /**
    * 发票号
    */
    private String invoiceNo;
    /**
    * 开票人
    */
    private String invoiceDrawer;
    /**
    * 开票人ID
    */
    private Long invoiceDrawerId;
    /**
    * 开票时间
    */
    private Timestamp invoiceDate;
    /**
    * 是否开票
    */
    private String isInvoice;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 发票关联id
     */
    private Long invoiceId;
    //账单日期
    private Timestamp billDate;
    //是否创建应收
    private String isCreateReceive;
    //应收结算单号
    private String receiveCode;
    //事业部ID
    private Long buId;
    //事业部名称
    private String buName;
    //店铺编码
    private String shopCode;
    //店铺名称
    private String shopName;
    //账单类型1- to B 2-to C
    private String type;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
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
    /*customerType get 方法 */
    public String getCustomerType(){
    return customerType;
    }
    /*customerType set 方法 */
    public void setCustomerType(String  customerType){
    this.customerType=customerType;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*billAmount get 方法 */
    public BigDecimal getBillAmount(){
    return billAmount;
    }
    /*billAmount set 方法 */
    public void setBillAmount(BigDecimal  billAmount){
    this.billAmount=billAmount;
    }
    /*invoiceNo get 方法 */
    public String getInvoiceNo(){
    return invoiceNo;
    }
    /*invoiceNo set 方法 */
    public void setInvoiceNo(String  invoiceNo){
    this.invoiceNo=invoiceNo;
    }
    /*invoiceDrawer get 方法 */
    public String getInvoiceDrawer(){
    return invoiceDrawer;
    }
    /*invoiceDrawer set 方法 */
    public void setInvoiceDrawer(String  invoiceDrawer){
    this.invoiceDrawer=invoiceDrawer;
    }
    /*invoiceDrawerId get 方法 */
    public Long getInvoiceDrawerId(){
    return invoiceDrawerId;
    }
    /*invoiceDrawerId set 方法 */
    public void setInvoiceDrawerId(Long  invoiceDrawerId){
    this.invoiceDrawerId=invoiceDrawerId;
    }
    /*invoiceDate get 方法 */
    public Timestamp getInvoiceDate(){
    return invoiceDate;
    }
    /*invoiceDate set 方法 */
    public void setInvoiceDate(Timestamp  invoiceDate){
    this.invoiceDate=invoiceDate;
    }
    /*isInvoice get 方法 */
    public String getIsInvoice(){
    return isInvoice;
    }
    /*isInvoice set 方法 */
    public void setIsInvoice(String  isInvoice){
    this.isInvoice=isInvoice;
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

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }
	public Timestamp getBillDate() {
		return billDate;
	}
	public void setBillDate(Timestamp billDate) {
		this.billDate = billDate;
	}
	public String getIsCreateReceive() {
		return isCreateReceive;
	}
	public void setIsCreateReceive(String isCreateReceive) {
		this.isCreateReceive = isCreateReceive;
	}
	public String getReceiveCode() {
		return receiveCode;
	}
	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public String getBuName() {
		return buName;
	}
	public void setBuName(String buName) {
		this.buName = buName;
	}
	public String getShopCode() {
		return shopCode;
	}
	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
    
}
