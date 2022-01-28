package com.topideal.entity.vo.platformdata;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class AlipayDailyFeeModel extends PageModel implements Serializable{

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
    * 结算金额(原币)
    */
    private BigDecimal amount;
    /**
    * 结算金额(RMB)
    */
    private BigDecimal rmbAmount;
    /**
    * 总金额
    */
    private BigDecimal grossAmount;
    /**
    * 汇率
    */
    private BigDecimal rate;
    /**
    * 结算费项
    */
    private String remark;
    /**
    * 结算币种
    */
    private String currency;
    /**
    * 天猫创建时间
    */
    private Timestamp alipayCreateDate;
    /**
    * 天猫修改时间
    */
    private Timestamp alipayModifyDate;
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
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

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
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*rmbAmount get 方法 */
    public BigDecimal getRmbAmount(){
    return rmbAmount;
    }
    /*rmbAmount set 方法 */
    public void setRmbAmount(BigDecimal  rmbAmount){
    this.rmbAmount=rmbAmount;
    }
    /*grossAmount get 方法 */
    public BigDecimal getGrossAmount(){
    return grossAmount;
    }
    /*grossAmount set 方法 */
    public void setGrossAmount(BigDecimal  grossAmount){
    this.grossAmount=grossAmount;
    }
    /*rate get 方法 */
    public BigDecimal getRate(){
    return rate;
    }
    /*rate set 方法 */
    public void setRate(BigDecimal  rate){
    this.rate=rate;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
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






}
