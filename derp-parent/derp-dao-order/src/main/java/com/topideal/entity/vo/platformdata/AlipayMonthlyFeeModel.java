package com.topideal.entity.vo.platformdata;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class AlipayMonthlyFeeModel extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 账号
    */
    private String userCode;
    /**
    * 结算账单号
    */
    private String settleNo;
    /**
    * 电商订单号
    */
    private String partnerTransactionId;
    /**
    * 结算年月
    */
    private String settleYearMonth;
    /**
    * 结算金额(原币)
    */
    private BigDecimal foreignFeeAmount;
    /**
    * 结算金额(RMB)
    */
    private BigDecimal feeAmount;
    /**
    * 汇率
    */
    private BigDecimal exchangeRate;
    /**
    * 结算费项
    */
    private String feeDesc;
    /**
    * 结算币种
    */
    private String currency;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 同步表ID
    */
    private Long oldId;
    /**
    * 天猫创建时间
    */
    private Timestamp alipayCreateDate;
    /**
    * 天猫修改时间
    */
    private Timestamp alipayModifyDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 文件key
     * @return
     */
    private String fileKey;
    /**
     * 原始电商订单号
     */
    private String originalPartnerTransactionId;

    public String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	public String getOriginalPartnerTransactionId() {
		return originalPartnerTransactionId;
	}
	public void setOriginalPartnerTransactionId(String originalPartnerTransactionId) {
		this.originalPartnerTransactionId = originalPartnerTransactionId;
	}
	/*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*userCode get 方法 */
    public String getUserCode(){
    return userCode;
    }
    /*userCode set 方法 */
    public void setUserCode(String  userCode){
    this.userCode=userCode;
    }
    /*settleNo get 方法 */
    public String getSettleNo(){
    return settleNo;
    }
    /*settleNo set 方法 */
    public void setSettleNo(String  settleNo){
    this.settleNo=settleNo;
    }
    /*partnerTransactionId get 方法 */
    public String getPartnerTransactionId(){
    return partnerTransactionId;
    }
    /*partnerTransactionId set 方法 */
    public void setPartnerTransactionId(String  partnerTransactionId){
    this.partnerTransactionId=partnerTransactionId;
    }
    /*settleYearMonth get 方法 */
    public String getSettleYearMonth(){
    return settleYearMonth;
    }
    /*settleYearMonth set 方法 */
    public void setSettleYearMonth(String  settleYearMonth){
    this.settleYearMonth=settleYearMonth;
    }
    /*foreignFeeAmount get 方法 */
    public BigDecimal getForeignFeeAmount(){
    return foreignFeeAmount;
    }
    /*foreignFeeAmount set 方法 */
    public void setForeignFeeAmount(BigDecimal  foreignFeeAmount){
    this.foreignFeeAmount=foreignFeeAmount;
    }
    /*feeAmount get 方法 */
    public BigDecimal getFeeAmount(){
    return feeAmount;
    }
    /*feeAmount set 方法 */
    public void setFeeAmount(BigDecimal  feeAmount){
    this.feeAmount=feeAmount;
    }
    /*exchangeRate get 方法 */
    public BigDecimal getExchangeRate(){
    return exchangeRate;
    }
    /*exchangeRate set 方法 */
    public void setExchangeRate(BigDecimal  exchangeRate){
    this.exchangeRate=exchangeRate;
    }
    /*feeDesc get 方法 */
    public String getFeeDesc(){
    return feeDesc;
    }
    /*feeDesc set 方法 */
    public void setFeeDesc(String  feeDesc){
    this.feeDesc=feeDesc;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
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
    /*alipayCreateDate get 方法 */
    public Timestamp getAlipayCreateDate(){
    return alipayCreateDate;
    }
    /*alipayCreateDate set 方法 */
    public void setAlipayCreateDate(Timestamp  alipayCreateDate){
    this.alipayCreateDate=alipayCreateDate;
    }
    /*alipayModifyDate get 方法 */
    public Timestamp getAlipayModifyDate(){
    return alipayModifyDate;
    }
    /*alipayModifyDate set 方法 */
    public void setAlipayModifyDate(Timestamp  alipayModifyDate){
    this.alipayModifyDate=alipayModifyDate;
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
