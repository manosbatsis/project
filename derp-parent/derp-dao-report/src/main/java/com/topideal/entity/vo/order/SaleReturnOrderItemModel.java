package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 销售退货订单表体
 * @author lian_
 */
public class SaleReturnOrderItemModel extends PageModel implements Serializable{

    /**
    * 销售退货订单ID
    */
    private Long orderId;
    /**
    * 退货批次
    */
    private String returnBatchNo;
    /**
    * 退货商品合同号
    */
    private String contractNo;
    /**
    * 销售订单ID
    */
    private String saleOrderId;
    /**
    * 退入商品id
    */
    private Long inGoodsId;
    /**
    * 退出商品id
    */
    private Long outGoodsId;
    /**
    * 退货商品单价
    */
    private BigDecimal price;
    /**
    * 退入商品编码
    */
    private String inGoodsCode;
    /**
    * id
    */
    private Long id;
    /**
    * 商品条形码
    */
    private String barcode;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 销售订单号
    */
    private String saleOrderCode;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 退出商品编码
    */
    private String outGoodsCode;
    /**
    * 退入商品货号
    */
    private String inGoodsNo;
    /**
    * 退出商品名称
    */
    private String outGoodsName;
    /**
    * 退货商品毛重
    */
    private Double grossWeight;
    /**
    * 退货商品净重
    */
    private Double netWeight;
    /**
    * 好品数量
    */
    private Integer returnNum;
    /**
    * 退货商品箱号
    */
    private String boxNo;
    /**
    * 退入商品名称
    */
    private String inGoodsName;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 退出商品货号
    */
    private String outGoodsNo;

    //坏品数量
    private Integer badGoodsNum;

    //po单号
    private String poNo;

    // po单时间
    private Timestamp poDate;
    //序号
    private Integer seq;
    /**
     * 销售总金额（不含税）
     */
    private BigDecimal amount;
    /**
     * 销售单价（含税）
     */
    private BigDecimal taxPrice;
    /**
     * 销售总金额（含税）
     */
    private BigDecimal taxAmount;
    /**
     * 税额
     */
    private BigDecimal tax;
    /**
     * 税率
     */
    private Double taxRate;

    /*orderId get 方法 */
    public Long getOrderId(){
        return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
        this.orderId=orderId;
    }
    /*returnBatchNo get 方法 */
    public String getReturnBatchNo(){
        return returnBatchNo;
    }
    /*returnBatchNo set 方法 */
    public void setReturnBatchNo(String  returnBatchNo){
        this.returnBatchNo=returnBatchNo;
    }
    /*contractNo get 方法 */
    public String getContractNo(){
        return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
        this.contractNo=contractNo;
    }
    /*saleOrderId get 方法 */
    public String getSaleOrderId(){
        return saleOrderId;
    }
    /*saleOrderId set 方法 */
    public void setSaleOrderId(String  saleOrderId){
        this.saleOrderId=saleOrderId;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
        return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
        this.inGoodsId=inGoodsId;
    }
    /*outGoodsId get 方法 */
    public Long getOutGoodsId(){
        return outGoodsId;
    }
    /*outGoodsId set 方法 */
    public void setOutGoodsId(Long  outGoodsId){
        this.outGoodsId=outGoodsId;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
        return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
        this.price=price;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
        return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
        this.inGoodsCode=inGoodsCode;
    }
    /*id get 方法 */
    public Long getId(){
        return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
        this.id=id;
    }
    /*barcode get 方法 */
    public String getBarcode(){
        return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
        this.barcode=barcode;
    }
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
        return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
        this.createDate=createDate;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
        return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
        this.saleOrderCode=saleOrderCode;
    }
    /*brandName get 方法 */
    public String getBrandName(){
        return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
        this.brandName=brandName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
        return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
        this.modifyDate=modifyDate;
    }
    /*outGoodsCode get 方法 */
    public String getOutGoodsCode(){
        return outGoodsCode;
    }
    /*outGoodsCode set 方法 */
    public void setOutGoodsCode(String  outGoodsCode){
        this.outGoodsCode=outGoodsCode;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
        return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
        this.inGoodsNo=inGoodsNo;
    }
    /*outGoodsName get 方法 */
    public String getOutGoodsName(){
        return outGoodsName;
    }
    /*outGoodsName set 方法 */
    public void setOutGoodsName(String  outGoodsName){
        this.outGoodsName=outGoodsName;
    }
    /*grossWeight get 方法 */
    public Double getGrossWeight(){
        return grossWeight;
    }
    /*grossWeight set 方法 */
    public void setGrossWeight(Double  grossWeight){
        this.grossWeight=grossWeight;
    }
    /*netWeight get 方法 */
    public Double getNetWeight(){
        return netWeight;
    }
    /*netWeight set 方法 */
    public void setNetWeight(Double  netWeight){
        this.netWeight=netWeight;
    }
    /*returnNum get 方法 */
    public Integer getReturnNum(){
        return returnNum;
    }
    /*returnNum set 方法 */
    public void setReturnNum(Integer  returnNum){
        this.returnNum=returnNum;
    }
    /*boxNo get 方法 */
    public String getBoxNo(){
        return boxNo;
    }
    /*boxNo set 方法 */
    public void setBoxNo(String  boxNo){
        this.boxNo=boxNo;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
        return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
        this.inGoodsName=inGoodsName;
    }
    /*creater get 方法 */
    public Long getCreater(){
        return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
        this.creater=creater;
    }
    /*outGoodsNo get 方法 */
    public String getOutGoodsNo(){
        return outGoodsNo;
    }
    /*outGoodsNo set 方法 */
    public void setOutGoodsNo(String  outGoodsNo){
        this.outGoodsNo=outGoodsNo;
    }

    public Integer getBadGoodsNum() {
        return badGoodsNum;
    }

    public void setBadGoodsNum(Integer badGoodsNum) {
        this.badGoodsNum = badGoodsNum;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public Timestamp getPoDate() {
        return poDate;
    }

    public void setPoDate(Timestamp poDate) {
        this.poDate = poDate;
    }
    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public BigDecimal getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(BigDecimal taxPrice) {
        this.taxPrice = taxPrice;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public Double getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
