package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class OrderItemHistoryModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 电商订单ID
    */
    private Long orderId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品ID
    */
    private Long goodsId;
    /**
    * 结算单价
    */
    private BigDecimal price;
    /**
    * 销售/结算数量
    */
    private Integer num;
    /**
    * 结算总金额
    */
    private BigDecimal decTotal;
    /**
    * 商品编码
    */
    private String goodsCode;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 成本价（与商家结算价）
    */
    private BigDecimal deliveryPrice;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 商品优惠金额
    */
    private BigDecimal goodsDiscount;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 佣金
    */
    private BigDecimal saleCom;
    /**
    * 销售单价
    */
    private BigDecimal originalPrice;
    /**
    * 销售总金额
    */
    private BigDecimal originalDecTotal;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 商品税费
    */
    private BigDecimal tax;
    //商品skuid
    private String skuId;
    /**
     * 分摊运费，2位小数
     */
    private BigDecimal wayFrtFee;
    /**
	 * 内贸商品结算金额（不含税）
	 */
	private BigDecimal tradeDecTotal;
	/**
	 * 内贸商品结算税额
	 */
	private BigDecimal tradeDecTax;

    /**
     * 事业部库位类型ID
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    private String stockLocationTypeName;

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
    /*goodsName get 方法 */
    public String getGoodsName(){
    return goodsName;
    }
    /*goodsName set 方法 */
    public void setGoodsName(String  goodsName){
    this.goodsName=goodsName;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*decTotal get 方法 */
    public BigDecimal getDecTotal(){
    return decTotal;
    }
    /*decTotal set 方法 */
    public void setDecTotal(BigDecimal  decTotal){
    this.decTotal=decTotal;
    }
    /*goodsCode get 方法 */
    public String getGoodsCode(){
    return goodsCode;
    }
    /*goodsCode set 方法 */
    public void setGoodsCode(String  goodsCode){
    this.goodsCode=goodsCode;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*goodsNo get 方法 */
    public String getGoodsNo(){
    return goodsNo;
    }
    /*goodsNo set 方法 */
    public void setGoodsNo(String  goodsNo){
    this.goodsNo=goodsNo;
    }
    /*deliveryPrice get 方法 */
    public BigDecimal getDeliveryPrice(){
    return deliveryPrice;
    }
    /*deliveryPrice set 方法 */
    public void setDeliveryPrice(BigDecimal  deliveryPrice){
    this.deliveryPrice=deliveryPrice;
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
    /*goodsDiscount get 方法 */
    public BigDecimal getGoodsDiscount(){
    return goodsDiscount;
    }
    /*goodsDiscount set 方法 */
    public void setGoodsDiscount(BigDecimal  goodsDiscount){
    this.goodsDiscount=goodsDiscount;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
    }
    /*saleCom get 方法 */
    public BigDecimal getSaleCom(){
    return saleCom;
    }
    /*saleCom set 方法 */
    public void setSaleCom(BigDecimal  saleCom){
    this.saleCom=saleCom;
    }
    /*originalPrice get 方法 */
    public BigDecimal getOriginalPrice(){
    return originalPrice;
    }
    /*originalPrice set 方法 */
    public void setOriginalPrice(BigDecimal  originalPrice){
    this.originalPrice=originalPrice;
    }
    /*originalDecTotal get 方法 */
    public BigDecimal getOriginalDecTotal(){
    return originalDecTotal;
    }
    /*originalDecTotal set 方法 */
    public void setOriginalDecTotal(BigDecimal  originalDecTotal){
    this.originalDecTotal=originalDecTotal;
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
    /*tax get 方法 */
    public BigDecimal getTax(){
    return tax;
    }
    /*tax set 方法 */
    public void setTax(BigDecimal  tax){
    this.tax=tax;
    }
    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getWayFrtFee() {
        return wayFrtFee;
    }

    public void setWayFrtFee(BigDecimal wayFrtFee) {
        this.wayFrtFee = wayFrtFee;
    }
	public BigDecimal getTradeDecTotal() {
		return tradeDecTotal;
	}
	public void setTradeDecTotal(BigDecimal tradeDecTotal) {
		this.tradeDecTotal = tradeDecTotal;
	}
	public BigDecimal getTradeDecTax() {
		return tradeDecTax;
	}
	public void setTradeDecTax(BigDecimal tradeDecTax) {
		this.tradeDecTax = tradeDecTax;
	}

    public Long getStockLocationTypeId() {
        return stockLocationTypeId;
    }

    public void setStockLocationTypeId(Long stockLocationTypeId) {
        this.stockLocationTypeId = stockLocationTypeId;
    }

    public String getStockLocationTypeName() {
        return stockLocationTypeName;
    }

    public void setStockLocationTypeName(String stockLocationTypeName) {
        this.stockLocationTypeName = stockLocationTypeName;
    }
}
