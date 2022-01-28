package com.topideal.entity.vo.automatic;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.Date;

public class YunjiAutomaticVerificationModel extends PageModel implements Serializable{

    /**
    * ID
    */
    private Long id;
    /**
    * 账单月份
    */
    private String month;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 结算单号
    */
    private String settleId;
    /**
    * 结算日期
    */
    private Date settleDate;
    /**
    * 云集商品编码
    */
    private String skuNo;
    /**
    * 平台发货量
    */
    private Integer platformDeliveryAccount;
    /**
    * 平台退货量
    */
    private Integer platformReturnAccount;
    /**
    * 系统商品货号
    */
    private String goodsNo;
    /**
    * 系统发货量
    */
    private Integer systemDeliveryAccount;
    /**
    * 系统退货量
    */
    private Integer systemReturnAccount;
    /**
    * 出货差异
    */
    private Integer deliveryDifferent;
    /**
    * 退货差异
    */
    private Integer returnDifferent;
    /**
    * 原因
    */
    private String result;
    /**
    * 创建日期
    */
    private Timestamp createDate;
    /**
    * 修改日期
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
    /*settleId get 方法 */
    public String getSettleId(){
    return settleId;
    }
    /*settleId set 方法 */
    public void setSettleId(String  settleId){
    this.settleId=settleId;
    }
    /*settleDate get 方法 */
    public Date getSettleDate(){
    return settleDate;
    }
    /*settleDate set 方法 */
    public void setSettleDate(Date  settleDate){
    this.settleDate=settleDate;
    }
    /*skuno get 方法 */
    public String getSkuNo(){
    return skuNo;
    }
    /*skuno set 方法 */
    public void setSkuNo(String  skuNo){
    this.skuNo=skuNo;
    }
    /*platformDeliveryAccount get 方法 */
    public Integer getPlatformDeliveryAccount(){
    return platformDeliveryAccount;
    }
    /*platformDeliveryAccount set 方法 */
    public void setPlatformDeliveryAccount(Integer  platformDeliveryAccount){
    this.platformDeliveryAccount=platformDeliveryAccount;
    }
    /*platformReturnAccount get 方法 */
    public Integer getPlatformReturnAccount(){
    return platformReturnAccount;
    }
    /*platformReturnAccount set 方法 */
    public void setPlatformReturnAccount(Integer  platformReturnAccount){
    this.platformReturnAccount=platformReturnAccount;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*systemDeliveryAccount get 方法 */
    public Integer getSystemDeliveryAccount(){
    return systemDeliveryAccount;
    }
    /*systemDeliveryAccount set 方法 */
    public void setSystemDeliveryAccount(Integer  systemDeliveryAccount){
    this.systemDeliveryAccount=systemDeliveryAccount;
    }
    /*systemReturnAccount get 方法 */
    public Integer getSystemReturnAccount(){
    return systemReturnAccount;
    }
    /*systemReturnAccount set 方法 */
    public void setSystemReturnAccount(Integer  systemReturnAccount){
    this.systemReturnAccount=systemReturnAccount;
    }
    /*deliveryDifferent get 方法 */
    public Integer getDeliveryDifferent(){
    return deliveryDifferent;
    }
    /*deliveryDifferent set 方法 */
    public void setDeliveryDifferent(Integer  deliveryDifferent){
    this.deliveryDifferent=deliveryDifferent;
    }
    /*returnDifferent get 方法 */
    public Integer getReturnDifferent(){
    return returnDifferent;
    }
    /*returnDifferent set 方法 */
    public void setReturnDifferent(Integer  returnDifferent){
    this.returnDifferent=returnDifferent;
    }
    /*result get 方法 */
    public String getResult(){
    return result;
    }
    /*result set 方法 */
    public void setResult(String  result){
    this.result=result;
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
