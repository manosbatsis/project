package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class SaleFinancingOrderItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 融资订单ID
    */
    private Long orderId;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 销售数量
    */
    private Integer num;
    /**
    * 单价
    */
    private BigDecimal price;
    /**
    * 金额
    */
    private BigDecimal amount;
    /**
    * 理货单位(00-托盘，01-箱，02-件
    */
    private String tallyingUnit;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 合同号
    */
    private String contractNo;
    /**
    * 赎回单价
    */
    private BigDecimal ransomPrice;
    /**
    * 赎回金额
    */
    private BigDecimal ransomAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 应还本金
     */
    private BigDecimal principal;
    /**
     * 资金占用费
     */
    private BigDecimal occupationFee;
    /**
     * 代理费
     */
    private BigDecimal agencyFee;
    /**
     * 滞纳金
     */
    private BigDecimal delayFee;
    /**
     * 应还款金额
     */
    private BigDecimal payableFee;
    /**
     * 保证金
     */
    private BigDecimal margin;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
    }
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*contractno get 方法 */
    public String getContractNo(){
    return contractNo;
    }
    /*contractno set 方法 */
    public void setContractNo(String  contractNo){
    this.contractNo=contractNo;
    }
    /*ransomprice get 方法 */
    public BigDecimal getRansomPrice(){
    return ransomPrice;
    }
    /*ransomprice set 方法 */
    public void setRansomprice(BigDecimal  ransomPrice){
    this.ransomPrice=ransomPrice;
    }
    /*ransomamount get 方法 */
    public BigDecimal getRansomAmount(){
    return ransomAmount;
    }
    /*ransomamount set 方法 */
    public void setRansomamount(BigDecimal  ransomAmount){
    this.ransomAmount=ransomAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setRansomPrice(BigDecimal ransomPrice) {
        this.ransomPrice = ransomPrice;
    }

    public void setRansomAmount(BigDecimal ransomAmount) {
        this.ransomAmount = ransomAmount;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getOccupationFee() {
        return occupationFee;
    }

    public void setOccupationFee(BigDecimal occupationFee) {
        this.occupationFee = occupationFee;
    }

    public BigDecimal getAgencyFee() {
        return agencyFee;
    }

    public void setAgencyFee(BigDecimal agencyFee) {
        this.agencyFee = agencyFee;
    }

    public BigDecimal getDelayFee() {
        return delayFee;
    }

    public void setDelayFee(BigDecimal delayFee) {
        this.delayFee = delayFee;
    }

    public BigDecimal getPayableFee() {
        return payableFee;
    }

    public void setPayableFee(BigDecimal payableFee) {
        this.payableFee = payableFee;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }
}
