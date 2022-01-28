package com.topideal.entity.vo.platformdata;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class AlipayDailySettlebatchModel extends PageModel implements Serializable{

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
    * 日结算单号
    */
    private String batchNo;
    /**
    * 结算日期
    */
    private Timestamp settleDate;
    /**
    * 结算币种
    */
    private String currency;
    /**
    * 结算货款
    */
    private BigDecimal amount;
    /**
    * 结算费用
    */
    private BigDecimal fee;
    /**
    * 结算金额
    */
    private BigDecimal settlement;
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
    
    /**
     * 文件key
     * @return
     */
    private String fileKey;

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
    /*batchNo get 方法 */
    public String getBatchNo(){
    return batchNo;
    }
    /*batchNo set 方法 */
    public void setBatchNo(String  batchNo){
    this.batchNo=batchNo;
    }
    /*settleDate get 方法 */
    public Timestamp getSettleDate(){
    return settleDate;
    }
    /*settleDate set 方法 */
    public void setSettleDate(Timestamp  settleDate){
    this.settleDate=settleDate;
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
    /*fee get 方法 */
    public BigDecimal getFee(){
    return fee;
    }
    /*fee set 方法 */
    public void setFee(BigDecimal  fee){
    this.fee=fee;
    }
    /*settlement get 方法 */
    public BigDecimal getSettlement(){
    return settlement;
    }
    /*settlement set 方法 */
    public void setSettlement(BigDecimal  settlement){
    this.settlement=settlement;
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
	public String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}






}
