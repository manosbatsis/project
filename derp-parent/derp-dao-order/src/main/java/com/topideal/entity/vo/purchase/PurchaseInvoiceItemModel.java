package com.topideal.entity.vo.purchase;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class PurchaseInvoiceItemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购发票ID
    */
    private Long purchaseInvoiceId;
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
    * 开发票数量
    */
    private Integer num;
    /**
    * 发票不含税单价
    */
    private BigDecimal price;
    /**
    * 发票不含税总金额
    */
    private BigDecimal amount;
    /**
    * 发票含税单价
    */
    private BigDecimal taxPrice;
    /**
    * 发票含税总金额
    */
    private BigDecimal taxAmount;
    /**
    * 税额
    */
    private BigDecimal tax;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * 采购单价
     */
     private BigDecimal purchasePrice;
     /**
     * 采购总金额
     */
     private BigDecimal purchaseAmount;
     /**
     * 采购数量
     */
     private Integer purchaseNum;
    /**
     * 采购订单表体Id
     */
     private Long purchaseItemId;
     /**
     * 条形码
     */
     private String barcode;
     /**
     * 工厂编码
     */
     private String factoryNo;


    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*purchaseInvoiceId get 方法 */
    public Long getPurchaseInvoiceId(){
    return purchaseInvoiceId;
    }
    /*purchaseInvoiceId set 方法 */
    public void setPurchaseInvoiceId(Long  purchaseInvoiceId){
    this.purchaseInvoiceId=purchaseInvoiceId;
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
    /*taxPrice get 方法 */
    public BigDecimal getTaxPrice(){
    return taxPrice;
    }
    /*taxPrice set 方法 */
    public void setTaxPrice(BigDecimal  taxPrice){
    this.taxPrice=taxPrice;
    }
    /*taxAmount get 方法 */
    public BigDecimal getTaxAmount(){
    return taxAmount;
    }
    /*taxAmount set 方法 */
    public void setTaxAmount(BigDecimal  taxAmount){
    this.taxAmount=taxAmount;
    }
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
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
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public BigDecimal getPurchaseAmount() {
		return purchaseAmount;
	}
	public void setPurchaseAmount(BigDecimal purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}
	public Integer getPurchaseNum() {
		return purchaseNum;
	}
	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

    public Long getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Long purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getFactoryNo() {
        return factoryNo;
    }

    public void setFactoryNo(String factoryNo) {
        this.factoryNo = factoryNo;
    }
}
