package com.topideal.entity.vo.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.topideal.common.system.ibatis.PageModel;

public class ReceiveAgingReportModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 销售币种
    */
    private String currency;
    /**
    * 折算汇率
    */
    private Double rate;
    /**
    * 汇率日期  格式: yyyy-MM-dd
    */
    private Date effectiveDate;
    /**
    * 应收收入（原币）
    */
    private BigDecimal originalAmount;
    /**
    * 应收收入（本币）
    */
    private BigDecimal rmbAmount;
    /**
    * 0~30天暂估应收金额
    */
    private BigDecimal thirtyAmount;
    /**
    * 30~60天暂估应收金额
    */
    private BigDecimal sixtyAmount;
    /**
    * 60~90天暂估应收金额
    */
    private BigDecimal ninetyAmount;
    /**
    * 90~120天暂估应收金额
    */
    private BigDecimal onetwentyAmount;
    /**
    * 120天以上暂估应收金额
    */
    private BigDecimal twentyAboveAmount;
    /**
    *  正常账期天数
    */
    private Integer accountDay;
    /**
    * 账期内金额
    */
    private BigDecimal accountAmount;
    /**
    * 逾期金额
    */
    private BigDecimal overdueAmount;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 币种（RMB）
    */
    private String rmbCurrency;
    /**
     * 渠道类型 1.To B 2.To C
     */
     private String channelType;
     /**
     * 客户简称
     */
     private String simpleName;

	  /**
	  * 应收费用（原币）
	  */
     private BigDecimal costOriginalAmount;
     
      /**
      * 应收费用（本币）
      */
     private BigDecimal costRmbAmount;
      /**
      * 应收账面余额（本币）
      */
     private BigDecimal receiveCostOriginalAmount;
      /**
      * 应收账面余额（原币）
      */
     private BigDecimal receiveIncomeOriginalAmount;
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
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*rate get 方法 */
    public Double getRate(){
    return rate;
    }
    /*rate set 方法 */
    public void setRate(Double  rate){
    this.rate=rate;
    }
    /*effectiveDate get 方法 */
    public Date getEffectiveDate(){
    return effectiveDate;
    }
    /*effectiveDate set 方法 */
    public void setEffectiveDate(Date  effectiveDate){
    this.effectiveDate=effectiveDate;
    }
    /*originalAmount get 方法 */
    public BigDecimal getOriginalAmount(){
    return originalAmount;
    }
    /*originalAmount set 方法 */
    public void setOriginalAmount(BigDecimal  originalAmount){
    this.originalAmount=originalAmount;
    }
    /*rmbAmount get 方法 */
    public BigDecimal getRmbAmount(){
    return rmbAmount;
    }
    /*rmbAmount set 方法 */
    public void setRmbAmount(BigDecimal  rmbAmount){
    this.rmbAmount=rmbAmount;
    }
    /*thirtyAmount get 方法 */
    public BigDecimal getThirtyAmount(){
    return thirtyAmount;
    }
    /*thirtyAmount set 方法 */
    public void setThirtyAmount(BigDecimal  thirtyAmount){
    this.thirtyAmount=thirtyAmount;
    }
    /*sixtyAmount get 方法 */
    public BigDecimal getSixtyAmount(){
    return sixtyAmount;
    }
    /*sixtyAmount set 方法 */
    public void setSixtyAmount(BigDecimal  sixtyAmount){
    this.sixtyAmount=sixtyAmount;
    }
    /*ninetyAmount get 方法 */
    public BigDecimal getNinetyAmount(){
    return ninetyAmount;
    }
    /*ninetyAmount set 方法 */
    public void setNinetyAmount(BigDecimal  ninetyAmount){
    this.ninetyAmount=ninetyAmount;
    }
    /*onetwentyAmount get 方法 */
    public BigDecimal getOnetwentyAmount(){
    return onetwentyAmount;
    }
    /*onetwentyAmount set 方法 */
    public void setOnetwentyAmount(BigDecimal  onetwentyAmount){
    this.onetwentyAmount=onetwentyAmount;
    }
    /*twentyAboveAmount get 方法 */
    public BigDecimal getTwentyAboveAmount(){
    return twentyAboveAmount;
    }
    /*twentyAboveAmount set 方法 */
    public void setTwentyAboveAmount(BigDecimal  twentyAboveAmount){
    this.twentyAboveAmount=twentyAboveAmount;
    }
    /*accountDay get 方法 */
    public Integer getAccountDay(){
    return accountDay;
    }
    /*accountDay set 方法 */
    public void setAccountDay(Integer  accountDay){
    this.accountDay=accountDay;
    }
    /*accountAmount get 方法 */
    public BigDecimal getAccountAmount(){
    return accountAmount;
    }
    /*accountAmount set 方法 */
    public void setAccountAmount(BigDecimal  accountAmount){
    this.accountAmount=accountAmount;
    }
    /*overdueAmount get 方法 */
    public BigDecimal getOverdueAmount(){
    return overdueAmount;
    }
    /*overdueAmount set 方法 */
    public void setOverdueAmount(BigDecimal  overdueAmount){
    this.overdueAmount=overdueAmount;
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
    /*rmbCurrency get 方法 */
    public String getRmbCurrency(){
    return rmbCurrency;
    }
    /*rmbCurrency set 方法 */
    public void setRmbCurrency(String  rmbCurrency){
    this.rmbCurrency=rmbCurrency;
    }
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getSimpleName() {
		return simpleName;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	public BigDecimal getCostOriginalAmount() {
		return costOriginalAmount;
	}
	public void setCostOriginalAmount(BigDecimal costOriginalAmount) {
		this.costOriginalAmount = costOriginalAmount;
	}
	public BigDecimal getCostRmbAmount() {
		return costRmbAmount;
	}
	public void setCostRmbAmount(BigDecimal costRmbAmount) {
		this.costRmbAmount = costRmbAmount;
	}
	public BigDecimal getReceiveCostOriginalAmount() {
		return receiveCostOriginalAmount;
	}
	public void setReceiveCostOriginalAmount(BigDecimal receiveCostOriginalAmount) {
		this.receiveCostOriginalAmount = receiveCostOriginalAmount;
	}
	public BigDecimal getReceiveIncomeOriginalAmount() {
		return receiveIncomeOriginalAmount;
	}
	public void setReceiveIncomeOriginalAmount(BigDecimal receiveIncomeOriginalAmount) {
		this.receiveIncomeOriginalAmount = receiveIncomeOriginalAmount;
	}






}
