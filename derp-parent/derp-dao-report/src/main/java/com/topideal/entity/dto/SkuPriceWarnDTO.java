package com.topideal.entity.dto;

import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class SkuPriceWarnDTO extends PageModel implements Serializable{

    /**
    * id
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
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 商品条码
    */
    private String sku;
    /**
    * 采购币种
    */
    private String purchaseCurrency;
    /**
    * 采购单价
    */
    private BigDecimal purchasePrice;
    /**
    * 折算币种
    */
    private String afterCurrency;
    /**
    * 折算汇率价
    */
    private BigDecimal afterPrice;
    /**
    * 标准成本单价币种
    */
    private String settlementCurrency;
    /**
    * 标准成本单价
    */
    private BigDecimal settlementPrice;
    /**
    * 波动率
    */
    private BigDecimal waveRate;
    /**
    * 预警类型 1-新品预警维护  2-波动预警
    */
    private String warnType;
    /**
    * 归属月份
    */
    private String month;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    private List<Long> buList;

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
    /*sku get 方法 */
    public String getSku(){
    return sku;
    }
    /*sku set 方法 */
    public void setSku(String  sku){
    this.sku=sku;
    }
    /*purchaseCurrency get 方法 */
    public String getPurchaseCurrency(){
    return purchaseCurrency;
    }
    /*purchaseCurrency set 方法 */
    public void setPurchaseCurrency(String  purchaseCurrency){
    this.purchaseCurrency=purchaseCurrency;
    }
    /*purchasePrice get 方法 */
    public BigDecimal getPurchasePrice(){
    return purchasePrice;
    }
    /*purchasePrice set 方法 */
    public void setPurchasePrice(BigDecimal  purchasePrice){
    this.purchasePrice=purchasePrice;
    }
    /*afterCurrency get 方法 */
    public String getAfterCurrency(){
    return afterCurrency;
    }
    /*afterCurrency set 方法 */
    public void setAfterCurrency(String  afterCurrency){
    this.afterCurrency=afterCurrency;
    }
    /*afterPrice get 方法 */
    public BigDecimal getAfterPrice(){
    return afterPrice;
    }
    /*afterPrice set 方法 */
    public void setAfterPrice(BigDecimal  afterPrice){
    this.afterPrice=afterPrice;
    }
    /*settlementCurrency get 方法 */
    public String getSettlementCurrency(){
    return settlementCurrency;
    }
    /*settlementCurrency set 方法 */
    public void setSettlementCurrency(String  settlementCurrency){
    this.settlementCurrency=settlementCurrency;
    }
    /*settlementPrice get 方法 */
    public BigDecimal getSettlementPrice(){
    return settlementPrice;
    }
    /*settlementPrice set 方法 */
    public void setSettlementPrice(BigDecimal  settlementPrice){
    this.settlementPrice=settlementPrice;
    }
    /*waveRate get 方法 */
    public BigDecimal getWaveRate(){
    return waveRate;
    }
    /*waveRate set 方法 */
    public void setWaveRate(BigDecimal  waveRate){
    this.waveRate=waveRate;
    }
    /*warnType get 方法 */
    public String getWarnType(){
    return warnType;
    }
    /*warnType set 方法 */
    public void setWarnType(String  warnType){
    this.warnType=warnType;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
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

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}
