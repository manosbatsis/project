package com.topideal.entity.vo.purchase;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class PurchaseSdOrderSditemModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购SD单ID
    */
    private Long orderId;
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
    * 条形码
    */
    private String barcode;
    /**
     * 标准条码
     */
    private String commbarcode;
    /**
    * SD类型
    */
    private String sdTypeName;
    /**
    * SD简称
    */
    private String sdSimpleName;
    /**
    * SD采购单价
    */
    private BigDecimal sdPrice;
    /**
    * SD本位币单价
    */
    private BigDecimal sdTgtPrice;
    /**
    * SD采购总金额
    */
    private BigDecimal sdAmount;
    /**
    * SD本位币总价
    */
    private BigDecimal sdTgtAmount;
    /**
    * 采购单价
    */
    private BigDecimal price;

    /**
    * 本位币单价
    */
    private BigDecimal tgtPrice;
    //采购金额
    private BigDecimal amount;
    //采购本位币金额
    private BigDecimal tgtAmount;
    private String brandParent;

    /**
     * 数量
     */
    private Integer num;
    
    /**
     * 创建时间
     */
     private Timestamp createDate;
     /**
     * 修改时间
     */
     private Timestamp modifyDate;

    //采购订单表体Id
    private Long purchaseItemId;

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
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*sdTypeName get 方法 */
    public String getSdTypeName(){
    return sdTypeName;
    }
    /*sdTypeName set 方法 */
    public void setSdTypeName(String  sdTypeName){
    this.sdTypeName=sdTypeName;
    }
    /*sdSimpleName get 方法 */
    public String getSdSimpleName(){
    return sdSimpleName;
    }
    /*sdSimpleName set 方法 */
    public void setSdSimpleName(String  sdSimpleName){
    this.sdSimpleName=sdSimpleName;
    }
    /*sdPrice get 方法 */
    public BigDecimal getSdPrice(){
    return sdPrice;
    }
    /*sdPrice set 方法 */
    public void setSdPrice(BigDecimal  sdPrice){
    this.sdPrice=sdPrice;
    }
    /*sdTgtPrice get 方法 */
    public BigDecimal getSdTgtPrice(){
    return sdTgtPrice;
    }
    /*sdTgtPrice set 方法 */
    public void setSdTgtPrice(BigDecimal  sdTgtPrice){
    this.sdTgtPrice=sdTgtPrice;
    }
    /*sdAmount get 方法 */
    public BigDecimal getSdAmount(){
    return sdAmount;
    }
    /*sdAmount set 方法 */
    public void setSdAmount(BigDecimal  sdAmount){
    this.sdAmount=sdAmount;
    }
    /*sdTgtAmount get 方法 */
    public BigDecimal getSdTgtAmount(){
    return sdTgtAmount;
    }
    /*sdTgtAmount set 方法 */
    public void setSdTgtAmount(BigDecimal  sdTgtAmount){
    this.sdTgtAmount=sdTgtAmount;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*tgtPrice get 方法 */
    public BigDecimal getTgtPrice(){
    return tgtPrice;
    }
    /*tgtPrice set 方法 */
    public void setTgtPrice(BigDecimal  tgtPrice){
    this.tgtPrice=tgtPrice;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

    public String getCommbarcode() {
        return commbarcode;
    }

    public void setCommbarcode(String commbarcode) {
        this.commbarcode = commbarcode;
    }

    public String getBrandParent() {
        return brandParent;
    }

    public void setBrandParent(String brandParent) {
        this.brandParent = brandParent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTgtAmount() {
        return tgtAmount;
    }

    public void setTgtAmount(BigDecimal tgtAmount) {
        this.tgtAmount = tgtAmount;
    }

    public Long getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Long purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }
}
