package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class TocTemporaryReceiveBillItemMonthlyModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 归属月份 YYYY-MM
    */
    private String month;
    /**
    * 系统订单号
    */
    private String orderCode;
    /**
    * 外部订单号
    */
    private String externalCode;
    /**
    * 店铺类型值编码 001:POP; 002:一件代发
    */
    private String shopTypeCode;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
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
    * 客户id
    */
    private Long customerId;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 店铺编码
    */
    private String shopCode;
    /**
    * 店铺名称
    */
    private String shopName;
    /**
    * 电商平台编码：第e仓：100011111；京东自营：100044998；京东POP-拜耳：10002075；菜鸟：1000000310；拼多多：1000004790
    */
    private String storePlatformCode;
    /**
    * 销售数量
    */
    private Integer saleNum;
    /**
    * 订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String temporaryCurrency;
    /**
    * 暂估应收金额（RMB）
    */
    private BigDecimal temporaryRmbAmount;
    /**
    * 冲销金额
    */
    private BigDecimal writeOffAmount;
    /**
    * 平台结算货款（原币）
    */
    private BigDecimal settlementOriAmount;
    /**
    * 平台结算金额（RMB）
    */
    private BigDecimal settlementRmbAmount;
    /**
    * 订单原币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String originalCurrency;
    /**
    * 结算标识：0-未核销 1-已红冲 2-已核销
    */
    private String settlementMark;
    /**
    * 结算日期
    */
    private Timestamp settlementDate;
    /**
    * 结算单号
    */
    private String settlementCode;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 单据类型：0-发货订单 1-退款订单 
    */
    private String orderType;
    /**
    * 母品牌
    */
    private String parentBrandName;
    /**
    * 母品牌id
    */
    private Long parentBrandId;
    /**
    * 母品牌编码
    */
    private String parentBrandCode;
    /**
    * 暂估月份
    */
    private String tempMonth;

    //剩余结算金额
    private BigDecimal lastReceiveAmount;

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
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
    }
    /*externalCode get 方法 */
    public String getExternalCode(){
    return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
    this.externalCode=externalCode;
    }
    /*shopTypeCode get 方法 */
    public String getShopTypeCode(){
    return shopTypeCode;
    }
    /*shopTypeCode set 方法 */
    public void setShopTypeCode(String  shopTypeCode){
    this.shopTypeCode=shopTypeCode;
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
    /*shopCode get 方法 */
    public String getShopCode(){
    return shopCode;
    }
    /*shopCode set 方法 */
    public void setShopCode(String  shopCode){
    this.shopCode=shopCode;
    }
    /*shopName get 方法 */
    public String getShopName(){
    return shopName;
    }
    /*shopName set 方法 */
    public void setShopName(String  shopName){
    this.shopName=shopName;
    }
    /*storePlatformCode get 方法 */
    public String getStorePlatformCode(){
    return storePlatformCode;
    }
    /*storePlatformCode set 方法 */
    public void setStorePlatformCode(String  storePlatformCode){
    this.storePlatformCode=storePlatformCode;
    }
    /*saleNum get 方法 */
    public Integer getSaleNum(){
    return saleNum;
    }
    /*saleNum set 方法 */
    public void setSaleNum(Integer  saleNum){
    this.saleNum=saleNum;
    }
    /*temporaryCurrency get 方法 */
    public String getTemporaryCurrency(){
    return temporaryCurrency;
    }
    /*temporaryCurrency set 方法 */
    public void setTemporaryCurrency(String  temporaryCurrency){
    this.temporaryCurrency=temporaryCurrency;
    }
    /*temporaryRmbAmount get 方法 */
    public BigDecimal getTemporaryRmbAmount(){
    return temporaryRmbAmount;
    }
    /*temporaryRmbAmount set 方法 */
    public void setTemporaryRmbAmount(BigDecimal  temporaryRmbAmount){
    this.temporaryRmbAmount=temporaryRmbAmount;
    }
    /*writeOffAmount get 方法 */
    public BigDecimal getWriteOffAmount(){
    return writeOffAmount;
    }
    /*writeOffAmount set 方法 */
    public void setWriteOffAmount(BigDecimal  writeOffAmount){
    this.writeOffAmount=writeOffAmount;
    }
    /*settlementOriAmount get 方法 */
    public BigDecimal getSettlementOriAmount(){
    return settlementOriAmount;
    }
    /*settlementOriAmount set 方法 */
    public void setSettlementOriAmount(BigDecimal  settlementOriAmount){
    this.settlementOriAmount=settlementOriAmount;
    }
    /*settlementRmbAmount get 方法 */
    public BigDecimal getSettlementRmbAmount(){
    return settlementRmbAmount;
    }
    /*settlementRmbAmount set 方法 */
    public void setSettlementRmbAmount(BigDecimal  settlementRmbAmount){
    this.settlementRmbAmount=settlementRmbAmount;
    }
    /*originalCurrency get 方法 */
    public String getOriginalCurrency(){
    return originalCurrency;
    }
    /*originalCurrency set 方法 */
    public void setOriginalCurrency(String  originalCurrency){
    this.originalCurrency=originalCurrency;
    }
    /*settlementMark get 方法 */
    public String getSettlementMark(){
    return settlementMark;
    }
    /*settlementMark set 方法 */
    public void setSettlementMark(String  settlementMark){
    this.settlementMark=settlementMark;
    }
    /*settlementDate get 方法 */
    public Timestamp getSettlementDate(){
    return settlementDate;
    }
    /*settlementDate set 方法 */
    public void setSettlementDate(Timestamp  settlementDate){
    this.settlementDate=settlementDate;
    }
    /*settlementCode get 方法 */
    public String getSettlementCode(){
    return settlementCode;
    }
    /*settlementCode set 方法 */
    public void setSettlementCode(String  settlementCode){
    this.settlementCode=settlementCode;
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
    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    }
    /*parentBrandName get 方法 */
    public String getParentBrandName(){
    return parentBrandName;
    }
    /*parentBrandName set 方法 */
    public void setParentBrandName(String  parentBrandName){
    this.parentBrandName=parentBrandName;
    }
    /*parentBrandId get 方法 */
    public Long getParentBrandId(){
    return parentBrandId;
    }
    /*parentBrandId set 方法 */
    public void setParentBrandId(Long  parentBrandId){
    this.parentBrandId=parentBrandId;
    }
    /*parentBrandCode get 方法 */
    public String getParentBrandCode(){
    return parentBrandCode;
    }
    /*parentBrandCode set 方法 */
    public void setParentBrandCode(String  parentBrandCode){
    this.parentBrandCode=parentBrandCode;
    }

    public String getTempMonth() {
        return tempMonth;
    }

    public void setTempMonth(String tempMonth) {
        this.tempMonth = tempMonth;
    }

    public BigDecimal getLastReceiveAmount() {
        return lastReceiveAmount;
    }

    public void setLastReceiveAmount(BigDecimal lastReceiveAmount) {
        this.lastReceiveAmount = lastReceiveAmount;
    }
}
