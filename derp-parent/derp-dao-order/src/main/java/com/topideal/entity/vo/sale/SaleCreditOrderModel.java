package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class SaleCreditOrderModel extends PageModel implements Serializable{

    /**
    * id主键
    */
    private Long id;
    /**
    * 销售单id
    */
    private Long saleOrderId;
    /**
    * 销售订单编号
    */
    private String saleOrderCode;
    /**
    * 赊销单号
    */
    private String code;
    /**
    * 客户ID(供应商)
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String currency;
    /**
    *  事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 赊销金额
    */
    private BigDecimal creditAmount;
    /**
    * 赊销总数量
    */
    private Integer totalNum;
    /**
    * 应收保证金
    */
    private BigDecimal payableMarginAmount;
    /**
    * 实收保证金
    */
    private BigDecimal actualMarginAmount;
    /**
    * 收保证金日期
    */
    private Timestamp receiveMarginDate;
    /**
    * 放款日期
    */
    private Timestamp loanDate;
    /**
    * 起息日期
    */
    private Timestamp valueDate;
    /**
    * 到期日期
    */
    private Timestamp expireDate;
    /**
    * 还款日期
    */
    private Timestamp receiveDate;
    /**
    * 收款本金
    */
    private BigDecimal receivePrincipalAmount;
    /**
    * 收款利息
    */
    private BigDecimal receiveInterestAmount;
    /**
    * 订单状态:001:待提交,002:待收保证金,003:待放款,004:赊销中,005:待收款,006-已删除，007-已收款
    */
    private String status;
    /**
    * 是否同步金融系统 0：未同步，1：已同步，默认值：0
    */
    private String isSyncFinance;
    /**
    * 同步金融系统时间
    */
    private Timestamp syncDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建人用户名
    */
    private String createName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改人
    */
    private Long modifier;
    /**
    * 修改人用户名
    */
    private String modifierName;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /**
     * 权责月份
     */
    private String ownMonth;
    /**
     * 卓普信放款时间
     */
    private Timestamp sapienceLoanDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*saleOrderId get 方法 */
    public Long getSaleOrderId(){
    return saleOrderId;
    }
    /*saleOrderId set 方法 */
    public void setSaleOrderId(Long  saleOrderId){
    this.saleOrderId=saleOrderId;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
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
    /*creditAmount get 方法 */
    public BigDecimal getCreditAmount(){
    return creditAmount;
    }
    /*creditAmount set 方法 */
    public void setCreditAmount(BigDecimal  creditAmount){
    this.creditAmount=creditAmount;
    }
    /*totalNum get 方法 */
    public Integer getTotalNum(){
    return totalNum;
    }
    /*totalNum set 方法 */
    public void setTotalNum(Integer  totalNum){
    this.totalNum=totalNum;
    }
    /*payableMarginAmount get 方法 */
    public BigDecimal getPayableMarginAmount(){
    return payableMarginAmount;
    }
    /*payableMarginAmount set 方法 */
    public void setPayableMarginAmount(BigDecimal  payableMarginAmount){
    this.payableMarginAmount=payableMarginAmount;
    }
    /*actualMarginAmount get 方法 */
    public BigDecimal getActualMarginAmount(){
    return actualMarginAmount;
    }
    /*actualMarginAmount set 方法 */
    public void setActualMarginAmount(BigDecimal  actualMarginAmount){
    this.actualMarginAmount=actualMarginAmount;
    }
    /*receiveMarginDate get 方法 */
    public Timestamp getReceiveMarginDate(){
    return receiveMarginDate;
    }
    /*receiveMarginDate set 方法 */
    public void setReceiveMarginDate(Timestamp  receiveMarginDate){
    this.receiveMarginDate=receiveMarginDate;
    }
    /*loanDate get 方法 */
    public Timestamp getLoanDate(){
    return loanDate;
    }
    /*loanDate set 方法 */
    public void setLoanDate(Timestamp  loanDate){
    this.loanDate=loanDate;
    }
    /*valueDate get 方法 */
    public Timestamp getValueDate(){
    return valueDate;
    }
    /*valueDate set 方法 */
    public void setValueDate(Timestamp  valueDate){
    this.valueDate=valueDate;
    }
    /*expireDate get 方法 */
    public Timestamp getExpireDate(){
    return expireDate;
    }
    /*expireDate set 方法 */
    public void setExpireDate(Timestamp  expireDate){
    this.expireDate=expireDate;
    }
    /*receiveDate get 方法 */
    public Timestamp getReceiveDate(){
    return receiveDate;
    }
    /*receiveDate set 方法 */
    public void setReceiveDate(Timestamp  receiveDate){
    this.receiveDate=receiveDate;
    }
    /*receivePrincipalAmount get 方法 */
    public BigDecimal getReceivePrincipalAmount(){
    return receivePrincipalAmount;
    }
    /*receivePrincipalAmount set 方法 */
    public void setReceivePrincipalAmount(BigDecimal  receivePrincipalAmount){
    this.receivePrincipalAmount=receivePrincipalAmount;
    }
    /*receiveInterestAmount get 方法 */
    public BigDecimal getReceiveInterestAmount(){
    return receiveInterestAmount;
    }
    /*receiveInterestAmount set 方法 */
    public void setReceiveInterestAmount(BigDecimal  receiveInterestAmount){
    this.receiveInterestAmount=receiveInterestAmount;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*isSyncFinance get 方法 */
    public String getIsSyncFinance(){
    return isSyncFinance;
    }
    /*isSyncFinance set 方法 */
    public void setIsSyncFinance(String  isSyncFinance){
    this.isSyncFinance=isSyncFinance;
    }
    /*syncDate get 方法 */
    public Timestamp getSyncDate(){
    return syncDate;
    }
    /*syncDate set 方法 */
    public void setSyncDate(Timestamp  syncDate){
    this.syncDate=syncDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public String getOwnMonth() {
        return ownMonth;
    }

    public void setOwnMonth(String ownMonth) {
        this.ownMonth = ownMonth;
    }

    public Timestamp getSapienceLoanDate() {
        return sapienceLoanDate;
    }

    public void setSapienceLoanDate(Timestamp sapienceLoanDate) {
        this.sapienceLoanDate = sapienceLoanDate;
    }
}
