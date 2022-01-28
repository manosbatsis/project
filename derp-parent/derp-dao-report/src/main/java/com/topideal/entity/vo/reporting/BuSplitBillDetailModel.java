package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuSplitBillDetailModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 结算单id
    */
    private Long billId;
    /**
    * 结算单号
    */
    private String billCode;
    /**
    * 数据类型：1-仓内费用 2-快递费 3-取消订单服务费 4-综合税金
    */
    private String dataType;
    /**
    * 订单号
    */
    private String orderCode;
    /**
    * 是否红冲 1-是 0-否
    */
    private String hcStatus;
    /**
     * 入仓仓库ID
     */
    private Long inDepotId;
    /**
     * 入仓仓库名称
     */
    private String inDepotName;
    /**
     * 出仓仓库ID
     */
    private Long outDepotId;
    /**
     * 出仓仓库名称
     */
    private String outDepotName;
    /**
    * 平台名称
    */
    private String platformName;
    /**
    * 平台编码
    */
    private String platformCode;
    /**
    * 出/入库时间
    */
    private Timestamp deliverDate;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品毛重
    */
    private Double grossWeight;
    /**
    * 商品数量
    */
    private Integer goodsNum;
    /**
    * 商品总金额(快递理赔用)
    */
    private BigDecimal totalPrice;
    /**
    * 费用
    */
    private BigDecimal cost;
    /**
    * 超件附加费(仓内费用)
    */
    private BigDecimal attachCost;
    /**
    * 包材费(仓内费用)
    */
    private BigDecimal packagingCost;
    /**
    * 合计
    */
    private BigDecimal totalCost;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    /**
     * 超重附加费
     */
    private BigDecimal overweightCost;
    /**
     * 一线进口清关费（一线进口清关费）
     */
    private BigDecimal clearanceCost;
    /**
     * 入库验收费（一线进口清关费）
     */
    private BigDecimal acceptanceCost;
    /**
     * 库存数量
     */
    private String stockNum;
    /**
     * 计费日期
     */
    private String billingDate;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*billId get 方法 */
    public Long getBillId(){
    return billId;
    }
    /*billId set 方法 */
    public void setBillId(Long  billId){
    this.billId=billId;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
    }
    /*dataType get 方法 */
    public String getDataType(){
    return dataType;
    }
    /*dataType set 方法 */
    public void setDataType(String  dataType){
    this.dataType=dataType;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
    }
    /*hcStatus get 方法 */
    public String getHcStatus(){
    return hcStatus;
    }
    /*hcStatus set 方法 */
    public void setHcStatus(String  hcStatus){
    this.hcStatus=hcStatus;
    }

    public Long getInDepotId() {
        return inDepotId;
    }

    public void setInDepotId(Long inDepotId) {
        this.inDepotId = inDepotId;
    }

    public String getInDepotName() {
        return inDepotName;
    }

    public void setInDepotName(String inDepotName) {
        this.inDepotName = inDepotName;
    }

    public Long getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    public String getOutDepotName() {
        return outDepotName;
    }

    public void setOutDepotName(String outDepotName) {
        this.outDepotName = outDepotName;
    }

    /*platformName get 方法 */
    public String getPlatformName(){
    return platformName;
    }
    /*platformName set 方法 */
    public void setPlatformName(String  platformName){
    this.platformName=platformName;
    }
    /*platformCode get 方法 */
    public String getPlatformCode(){
    return platformCode;
    }
    /*platformCode set 方法 */
    public void setPlatformCode(String  platformCode){
    this.platformCode=platformCode;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    this.deliverDate=deliverDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*grossWeight get 方法 */
    public Double getGrossWeight(){
    return grossWeight;
    }
    /*grossWeight set 方法 */
    public void setGrossWeight(Double  grossWeight){
    this.grossWeight=grossWeight;
    }
    /*goodsNum get 方法 */
    public Integer getGoodsNum(){
    return goodsNum;
    }
    /*goodsNum set 方法 */
    public void setGoodsNum(Integer  goodsNum){
    this.goodsNum=goodsNum;
    }
    /*totalPrice get 方法 */
    public BigDecimal getTotalPrice(){
    return totalPrice;
    }
    /*totalPrice set 方法 */
    public void setTotalPrice(BigDecimal  totalPrice){
    this.totalPrice=totalPrice;
    }
    /*cost get 方法 */
    public BigDecimal getCost(){
    return cost;
    }
    /*cost set 方法 */
    public void setCost(BigDecimal  cost){
    this.cost=cost;
    }
    /*attachCost get 方法 */
    public BigDecimal getAttachCost(){
    return attachCost;
    }
    /*attachCost set 方法 */
    public void setAttachCost(BigDecimal  attachCost){
    this.attachCost=attachCost;
    }
    /*packagingCost get 方法 */
    public BigDecimal getPackagingCost(){
    return packagingCost;
    }
    /*packagingCost set 方法 */
    public void setPackagingCost(BigDecimal  packagingCost){
    this.packagingCost=packagingCost;
    }
    /*totalCost get 方法 */
    public BigDecimal getTotalCost(){
    return totalCost;
    }
    /*totalCost set 方法 */
    public void setTotalCost(BigDecimal  totalCost){
    this.totalCost=totalCost;
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

    public BigDecimal getOverweightCost() {
        return overweightCost;
    }

    public void setOverweightCost(BigDecimal overweightCost) {
        this.overweightCost = overweightCost;
    }

    public BigDecimal getClearanceCost() {
        return clearanceCost;
    }

    public void setClearanceCost(BigDecimal clearanceCost) {
        this.clearanceCost = clearanceCost;
    }

    public BigDecimal getAcceptanceCost() {
        return acceptanceCost;
    }

    public void setAcceptanceCost(BigDecimal acceptanceCost) {
        this.acceptanceCost = acceptanceCost;
    }

    public String getStockNum() {
        return stockNum;
    }

    public void setStockNum(String stockNum) {
        this.stockNum = stockNum;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }
}
