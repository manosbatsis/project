package com.topideal.entity.vo.bill;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class TocTemporaryReceiveBillModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 店铺类型值编码 001:POP; 002:一件代发
    */
    private String shopTypeCode;
    /**
    * 归属月份 YYYY-MM
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
    * 结算完成月份 YYYY-MM
    */
    private String settlementEndMonth;
    /**
    * 结算状态：0-未结算 1-部分结算 2-已结算
    */
    private String settlementStatus;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //总暂估应收
    private BigDecimal totalReceiveAmount;
    //总订单数
    private Long totalReceiveNum;
    //已结算订单金额
    private BigDecimal alreadyReceiveAmount;
    //已结算订单数
    private Long alreadyReceiveNum;
    //剩余结算金额
    private BigDecimal lastReceiveAmount;
    /**
     * 店铺关联开店事业部id
     */
    private Long buId;
    /**
     * 店铺关联开店事业部名称
     */
    private String buName;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*shopTypeCode get 方法 */
    public String getShopTypeCode(){
    return shopTypeCode;
    }
    /*shopTypeCode set 方法 */
    public void setShopTypeCode(String  shopTypeCode){
    this.shopTypeCode=shopTypeCode;
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
    /*settlementEndMonth get 方法 */
    public String getSettlementEndMonth(){
    return settlementEndMonth;
    }
    /*settlementEndMonth set 方法 */
    public void setSettlementEndMonth(String  settlementEndMonth){
    this.settlementEndMonth=settlementEndMonth;
    }
    /*settlementStatus get 方法 */
    public String getSettlementStatus(){
    return settlementStatus;
    }
    /*settlementStatus set 方法 */
    public void setSettlementStatus(String  settlementStatus){
    this.settlementStatus=settlementStatus;
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

    public BigDecimal getTotalReceiveAmount() {
        return totalReceiveAmount;
    }

    public void setTotalReceiveAmount(BigDecimal totalReceiveAmount) {
        this.totalReceiveAmount = totalReceiveAmount;
    }

    public Long getTotalReceiveNum() {
        return totalReceiveNum;
    }

    public void setTotalReceiveNum(Long totalReceiveNum) {
        this.totalReceiveNum = totalReceiveNum;
    }

    public BigDecimal getAlreadyReceiveAmount() {
        return alreadyReceiveAmount;
    }

    public void setAlreadyReceiveAmount(BigDecimal alreadyReceiveAmount) {
        this.alreadyReceiveAmount = alreadyReceiveAmount;
    }

    public Long getAlreadyReceiveNum() {
        return alreadyReceiveNum;
    }

    public void setAlreadyReceiveNum(Long alreadyReceiveNum) {
        this.alreadyReceiveNum = alreadyReceiveNum;
    }

    public BigDecimal getLastReceiveAmount() {
        return lastReceiveAmount;
    }

    public void setLastReceiveAmount(BigDecimal lastReceiveAmount) {
        this.lastReceiveAmount = lastReceiveAmount;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }
}
