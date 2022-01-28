package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class TobTemporaryReceiveBillModel extends PageModel implements Serializable{

    /**
    * 记录ID
    */
    private Long id;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
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
    * 销售订单号
    */
    private String orderCode;
    /**
    * 上架单号
    */
    private String shelfCode;
    /**
    * 销售币种
    */
    private String currency;
    /**
    * 销售类型 1:购销-整批结算 2:购销-实销实结
    */
    private String saleType;
    /**
    * po号
    */
    private String poNo;
    /**
    * 上架时间
    */
    private Timestamp shelfDate;
    /**
    * 应收账单号
    */
    private String receiveCode;
    /**
    * 应收结算状态 1-已上架未结算 2-待确认账单 3-已确认待开票 4-已开票待核销 5-已核销
    */
    private String status;
    /**
    * 上架计提应收金额
    */
    private BigDecimal shelfAmount;
    /**
     * 上架计提返利金额
     */
    private BigDecimal shelfRebateAmount;
    /**
    * 出账单计划日期
    */
    private Timestamp outBillPlanDate;
    /**
    * 出账单实际日期
    */
    private Timestamp outBillRealDate;
    /**
    * 账单确认计划日期
    */
    private Timestamp confirmPlanDate;
    /**
    * 账单确认实际日期
    */
    private Timestamp confirmRealDate;
    /**
    * 开票计划日期
    */
    private Timestamp invoicingPlanDate;
    /**
    * 开票实际日期
    */
    private Timestamp invoicingRealDate;
    /**
    * 回款计划日期
    */
    private Timestamp paymentPlanDate;
    /**
    * 回款实际日期
    */
    private Timestamp paymentRealDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 关联应收单id
    */
    private Long receiveId;

    /**
     * 返利结算状态 1-已上架未结算 2-部分结算 5-已结算
     */
    private String rebateStatus;

    /**
     * 单据类型 1-上架单 2-销售退货订单
     */
    private String orderType;

    /**
     * 是否红冲单：0-否，1-是
     */
    private String isWriteOff;
    /**
     * 原上架号
     */
    private String originalShelfCode;


    public String getRebateStatus() {
        return rebateStatus;
    }

    public void setRebateStatus(String rebateStatus) {
        this.rebateStatus = rebateStatus;
    }

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
    }
    /*shelfCode get 方法 */
    public String getShelfCode(){
    return shelfCode;
    }
    /*shelfCode set 方法 */
    public void setShelfCode(String  shelfCode){
    this.shelfCode=shelfCode;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*saleType get 方法 */
    public String getSaleType(){
    return saleType;
    }
    /*saleType set 方法 */
    public void setSaleType(String  saleType){
    this.saleType=saleType;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
    }
    /*receiveCode get 方法 */
    public String getReceiveCode(){
    return receiveCode;
    }
    /*receiveCode set 方法 */
    public void setReceiveCode(String  receiveCode){
    this.receiveCode=receiveCode;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*outBillPlanDate get 方法 */
    public Timestamp getOutBillPlanDate(){
    return outBillPlanDate;
    }
    /*outBillPlanDate set 方法 */
    public void setOutBillPlanDate(Timestamp  outBillPlanDate){
    this.outBillPlanDate=outBillPlanDate;
    }
    /*outBillRealDate get 方法 */
    public Timestamp getOutBillRealDate(){
    return outBillRealDate;
    }
    /*outBillRealDate set 方法 */
    public void setOutBillRealDate(Timestamp  outBillRealDate){
    this.outBillRealDate=outBillRealDate;
    }
    /*invoicingPlanDate get 方法 */
    public Timestamp getInvoicingPlanDate(){
    return invoicingPlanDate;
    }
    /*invoicingPlanDate set 方法 */
    public void setInvoicingPlanDate(Timestamp  invoicingPlanDate){
    this.invoicingPlanDate=invoicingPlanDate;
    }
    /*invoicingRealDate get 方法 */
    public Timestamp getInvoicingRealDate(){
    return invoicingRealDate;
    }
    /*invoicingRealDate set 方法 */
    public void setInvoicingRealDate(Timestamp  invoicingRealDate){
    this.invoicingRealDate=invoicingRealDate;
    }
    /*paymentPlanDate get 方法 */
    public Timestamp getPaymentPlanDate(){
    return paymentPlanDate;
    }
    /*paymentPlanDate set 方法 */
    public void setPaymentPlanDate(Timestamp  paymentPlanDate){
    this.paymentPlanDate=paymentPlanDate;
    }
    /*paymentRealDate get 方法 */
    public Timestamp getPaymentRealDate(){
    return paymentRealDate;
    }
    /*paymentRealDate set 方法 */
    public void setPaymentRealDate(Timestamp  paymentRealDate){
    this.paymentRealDate=paymentRealDate;
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

    public Timestamp getConfirmPlanDate() {
        return confirmPlanDate;
    }

    public void setConfirmPlanDate(Timestamp confirmPlanDate) {
        this.confirmPlanDate = confirmPlanDate;
    }

    public Timestamp getConfirmRealDate() {
        return confirmRealDate;
    }

    public void setConfirmRealDate(Timestamp confirmRealDate) {
        this.confirmRealDate = confirmRealDate;
    }

    public Long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(Long receiveId) {
        this.receiveId = receiveId;
    }

    public BigDecimal getShelfAmount() {
        return shelfAmount;
    }

    public void setShelfAmount(BigDecimal shelfAmount) {
        this.shelfAmount = shelfAmount;
    }

    public BigDecimal getShelfRebateAmount() {
        return shelfRebateAmount;
    }

    public void setShelfRebateAmount(BigDecimal shelfRebateAmount) {
        this.shelfRebateAmount = shelfRebateAmount;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getIsWriteOff() {
        return isWriteOff;
    }

    public void setIsWriteOff(String isWriteOff) {
        this.isWriteOff = isWriteOff;
    }

    public String getOriginalShelfCode() {
        return originalShelfCode;
    }

    public void setOriginalShelfCode(String originalShelfCode) {
        this.originalShelfCode = originalShelfCode;
    }
}
