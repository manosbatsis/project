package com.topideal.entity.vo.automatic;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class VipAutomaticVerificationModel extends PageModel implements Serializable{

    /**
    * 
    */
    private Long id;
    /**
    * 月份
    */
    private String month;
    /**
    * 平台
    */
    private String platform;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名
    */
    private String merchantName;
    /**
    * po号
    */
    private String poNo;
    /**
    * 爬虫账单号
    */
    private String crawlerNo;
    /**
    * 唯品SKU
    */
    private String crawlerGoodsNo;
    /**
    * 账单销售总量
    */
    private Integer billSalesAccount;
    /**
    * 系统销售出库总量
    */
    private Integer systemSalesOutAccount;
    /**
    * 销售出库差异
    */
    private Integer salesOutDifference;
    /**
    * 账单红冲总量
    */
    private Integer billHcAccount;
    /**
    * 系统红冲总量
    */
    private Integer systemHcAccount;
    /**
    * 红冲差异
    */
    private Integer hcDifference;
    /**
    * 账单其他总量（调增）
    */
    private Integer billAdjustmentIncreaseAccount;
    /**
    * 系统库存调整（调增）
    */
    private Integer systemAdjustmentIncreaseAccount;
    /**
    * 调增差异
    */
    private Integer adjustmentIncreaseDifferent;
    /**
    * 账单其他总量（调减）
    */
    private Integer billAdjustmentDecreaseAccount;
    /**
    * 系统库存调整（调减）
    */
    private Integer systemAdjustmentDecreaseAccount;
    /**
    * 调减差异
    */
    private Integer adjustmentDecreaseDifferent;
    /**
    * 1-已对平、0-未对平
    */
    private String verificationResult;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*platform get 方法 */
    public String getPlatform(){
    return platform;
    }
    /*platform set 方法 */
    public void setPlatform(String  platform){
    this.platform=platform;
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
    /*crawlerNo get 方法 */
    public String getCrawlerNo(){
    return crawlerNo;
    }
    /*crawlerNo set 方法 */
    public void setCrawlerNo(String  crawlerNo){
    this.crawlerNo=crawlerNo;
    }
    /*crawlerGoodsNo get 方法 */
    public String getCrawlerGoodsNo(){
    return crawlerGoodsNo;
    }
    /*crawlerGoodsNo set 方法 */
    public void setCrawlerGoodsNo(String  crawlerGoodsNo){
    this.crawlerGoodsNo=crawlerGoodsNo;
    }
    /*billSalesAccount get 方法 */
    public Integer getBillSalesAccount(){
    return billSalesAccount;
    }
    /*billSalesAccount set 方法 */
    public void setBillSalesAccount(Integer  billSalesAccount){
    this.billSalesAccount=billSalesAccount;
    }
    /*systemSalesOutAccount get 方法 */
    public Integer getSystemSalesOutAccount(){
    return systemSalesOutAccount;
    }
    /*systemSalesOutAccount set 方法 */
    public void setSystemSalesOutAccount(Integer  systemSalesOutAccount){
    this.systemSalesOutAccount=systemSalesOutAccount;
    }
    /*salesOutDifference get 方法 */
    public Integer getSalesOutDifference(){
    return salesOutDifference;
    }
    /*salesOutDifference set 方法 */
    public void setSalesOutDifference(Integer  salesOutDifference){
    this.salesOutDifference=salesOutDifference;
    }
    /*billHcAccount get 方法 */
    public Integer getBillHcAccount(){
    return billHcAccount;
    }
    /*billHcAccount set 方法 */
    public void setBillHcAccount(Integer  billHcAccount){
    this.billHcAccount=billHcAccount;
    }
    /*systemHcAccount get 方法 */
    public Integer getSystemHcAccount(){
    return systemHcAccount;
    }
    /*systemHcAccount set 方法 */
    public void setSystemHcAccount(Integer  systemHcAccount){
    this.systemHcAccount=systemHcAccount;
    }
    /*hcDifference get 方法 */
    public Integer getHcDifference(){
    return hcDifference;
    }
    /*hcDifference set 方法 */
    public void setHcDifference(Integer  hcDifference){
    this.hcDifference=hcDifference;
    }
    /*billAdjustmentIncreaseAccount get 方法 */
    public Integer getBillAdjustmentIncreaseAccount(){
    return billAdjustmentIncreaseAccount;
    }
    /*billAdjustmentIncreaseAccount set 方法 */
    public void setBillAdjustmentIncreaseAccount(Integer  billAdjustmentIncreaseAccount){
    this.billAdjustmentIncreaseAccount=billAdjustmentIncreaseAccount;
    }
    /*systemAdjustmentIncreaseAccount get 方法 */
    public Integer getSystemAdjustmentIncreaseAccount(){
    return systemAdjustmentIncreaseAccount;
    }
    /*systemAdjustmentIncreaseAccount set 方法 */
    public void setSystemAdjustmentIncreaseAccount(Integer  systemAdjustmentIncreaseAccount){
    this.systemAdjustmentIncreaseAccount=systemAdjustmentIncreaseAccount;
    }
    /*adjustmentIncreaseDifferent get 方法 */
    public Integer getAdjustmentIncreaseDifferent(){
    return adjustmentIncreaseDifferent;
    }
    /*adjustmentIncreaseDifferent set 方法 */
    public void setAdjustmentIncreaseDifferent(Integer  adjustmentIncreaseDifferent){
    this.adjustmentIncreaseDifferent=adjustmentIncreaseDifferent;
    }
    /*billAdjustmentDecreaseAccount get 方法 */
    public Integer getBillAdjustmentDecreaseAccount(){
    return billAdjustmentDecreaseAccount;
    }
    /*billAdjustmentDecreaseAccount set 方法 */
    public void setBillAdjustmentDecreaseAccount(Integer  billAdjustmentDecreaseAccount){
    this.billAdjustmentDecreaseAccount=billAdjustmentDecreaseAccount;
    }
    /*systemAdjustmentDecreaseAccount get 方法 */
    public Integer getSystemAdjustmentDecreaseAccount(){
    return systemAdjustmentDecreaseAccount;
    }
    /*systemAdjustmentDecreaseAccount set 方法 */
    public void setSystemAdjustmentDecreaseAccount(Integer  systemAdjustmentDecreaseAccount){
    this.systemAdjustmentDecreaseAccount=systemAdjustmentDecreaseAccount;
    }
    /*adjustmentDecreaseDifferent get 方法 */
    public Integer getAdjustmentDecreaseDifferent(){
    return adjustmentDecreaseDifferent;
    }
    /*adjustmentDecreaseDifferent set 方法 */
    public void setAdjustmentDecreaseDifferent(Integer  adjustmentDecreaseDifferent){
    this.adjustmentDecreaseDifferent=adjustmentDecreaseDifferent;
    }
    /*verificationResult get 方法 */
    public String getVerificationResult(){
    return verificationResult;
    }
    /*verificationResult set 方法 */
    public void setVerificationResult(String  verificationResult){
    this.verificationResult=verificationResult;
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
