package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class ReceiveBillInvoiceModel extends PageModel implements Serializable{

    /**
    * id主键
    */
    private Long id;
    /**
    * 商家id
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
    * 发票号码
    */
    private String invoiceNo;
    /**
    * 发票文件地址
    */
    private String invoicePath;
    /**
    * 结算币种
    */
    private String currency;
    /**
    * 开票金额
    */
    private BigDecimal invoiceAmount;
    /**
    * 开单日期
    */
    private Timestamp invoiceDate;
    /**
    * 多单开票关联单号
    */
    private String invoiceRelCodes;
    /**
    * 多单开票关联id
    */
    private String invoiceRelIds;
    /**
    * 开票人ID
    */
    private Long createrId;
    /**
    * 开票人
    */
    private String creater;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 发票状态：0-待签章 1-已作废 2-已签章
    */
    private String status;
    /**
    * 同步操作人id
    */
    private Long synchronizerId;
    /**
    * 同步操作人
    */
    private String synchronizer;
    /**
    * 关联平台结算单单号
    */
    private String relStatementCodes;
    /**
     * 发票类型 0-ToB 1-ToC
     */
    private String invoiceType;

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
    /*invoiceNo get 方法 */
    public String getInvoiceNo(){
    return invoiceNo;
    }
    /*invoiceNo set 方法 */
    public void setInvoiceNo(String  invoiceNo){
    this.invoiceNo=invoiceNo;
    }
    /*invoicePath get 方法 */
    public String getInvoicePath(){
    return invoicePath;
    }
    /*invoicePath set 方法 */
    public void setInvoicePath(String  invoicePath){
    this.invoicePath=invoicePath;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*invoiceAmount get 方法 */
    public BigDecimal getInvoiceAmount(){
    return invoiceAmount;
    }
    /*invoiceAmount set 方法 */
    public void setInvoiceAmount(BigDecimal  invoiceAmount){
    this.invoiceAmount=invoiceAmount;
    }
    /*invoiceDate get 方法 */
    public Timestamp getInvoiceDate(){
    return invoiceDate;
    }
    /*invoiceDate set 方法 */
    public void setInvoiceDate(Timestamp  invoiceDate){
    this.invoiceDate=invoiceDate;
    }
    /*invoiceRelCodes get 方法 */
    public String getInvoiceRelCodes(){
    return invoiceRelCodes;
    }
    /*invoiceRelCodes set 方法 */
    public void setInvoiceRelCodes(String  invoiceRelCodes){
    this.invoiceRelCodes=invoiceRelCodes;
    }
    /*invoiceRelIds get 方法 */
    public String getInvoiceRelIds(){
    return invoiceRelIds;
    }
    /*invoiceRelIds set 方法 */
    public void setInvoiceRelIds(String  invoiceRelIds){
    this.invoiceRelIds=invoiceRelIds;
    }
    /*createrId get 方法 */
    public Long getCreaterId(){
    return createrId;
    }
    /*createrId set 方法 */
    public void setCreaterId(Long  createrId){
    this.createrId=createrId;
    }
    /*creater get 方法 */
    public String getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(String  creater){
    this.creater=creater;
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
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*synchronizerId get 方法 */
    public Long getSynchronizerId(){
    return synchronizerId;
    }
    /*synchronizerId set 方法 */
    public void setSynchronizerId(Long  synchronizerId){
    this.synchronizerId=synchronizerId;
    }
    /*synchronizer get 方法 */
    public String getSynchronizer(){
    return synchronizer;
    }
    /*synchronizer set 方法 */
    public void setSynchronizer(String  synchronizer){
    this.synchronizer=synchronizer;
    }
    /*relStatementCodes get 方法 */
    public String getRelStatementCodes(){
    return relStatementCodes;
    }
    /*relStatementCodes set 方法 */
    public void setRelStatementCodes(String  relStatementCodes){
    this.relStatementCodes=relStatementCodes;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }
}
