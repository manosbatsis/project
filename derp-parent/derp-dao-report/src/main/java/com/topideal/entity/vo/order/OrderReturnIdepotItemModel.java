package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class OrderReturnIdepotItemModel extends PageModel implements Serializable{

    /**
    * 主键
    */
    private Long id;
    /**
    * 电商订单退货入库ID
    */
    private Long oreturnIdepotId;
    /**
    * 退入商品id
    */
    private Long inGoodsId;
    /**
    * 退入商品编码
    */
    private String inGoodsCode;
    /**
    * 退入商品货号
    */
    private String inGoodsNo;
    /**
    * 退入商品名称
    */
    private String inGoodsName;
    /**
    * 销售单价
    */
    private BigDecimal price;

    //销售金额
    private BigDecimal decTotal;

    /**
    * 退货正常品数量
    */
    private Integer returnNum;
    /**
    * 退货残品数量
    */
    private Integer badGoodsNum;
    /**
    * 条形码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;

    //事业部名称
    private String buName;

    //事业部id
    private Long buId;
   /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
	 * 内贸商品退款金额（不含税）
	 */
	private BigDecimal tradeRefundAmount;
	/**
	 * 内贸商品退款税额
	 */
	private BigDecimal tradeRefundTax;
    /**
     * 库位类型id
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
    /*oreturnIdepotId get 方法 */
    public Long getOreturnIdepotId(){
    return oreturnIdepotId;
    }
    /*oreturnIdepotId set 方法 */
    public void setOreturnIdepotId(Long  oreturnIdepotId){
    this.oreturnIdepotId=oreturnIdepotId;
    }
    /*inGoodsId get 方法 */
    public Long getInGoodsId(){
    return inGoodsId;
    }
    /*inGoodsId set 方法 */
    public void setInGoodsId(Long  inGoodsId){
    this.inGoodsId=inGoodsId;
    }
    /*inGoodsCode get 方法 */
    public String getInGoodsCode(){
    return inGoodsCode;
    }
    /*inGoodsCode set 方法 */
    public void setInGoodsCode(String  inGoodsCode){
    this.inGoodsCode=inGoodsCode;
    }
    /*inGoodsNo get 方法 */
    public String getInGoodsNo(){
    return inGoodsNo;
    }
    /*inGoodsNo set 方法 */
    public void setInGoodsNo(String  inGoodsNo){
    this.inGoodsNo=inGoodsNo;
    }
    /*inGoodsName get 方法 */
    public String getInGoodsName(){
    return inGoodsName;
    }
    /*inGoodsName set 方法 */
    public void setInGoodsName(String  inGoodsName){
    this.inGoodsName=inGoodsName;
    }
    /*price get 方法 */
    public BigDecimal getPrice(){
    return price;
    }
    /*price set 方法 */
    public void setPrice(BigDecimal  price){
    this.price=price;
    }
    /*returnNum get 方法 */
    public Integer getReturnNum(){
    return returnNum;
    }
    /*returnNum set 方法 */
    public void setReturnNum(Integer  returnNum){
    this.returnNum=returnNum;
    }
    /*badGoodsNum get 方法 */
    public Integer getBadGoodsNum(){
    return badGoodsNum;
    }
    /*badGoodsNum set 方法 */
    public void setBadGoodsNum(Integer  badGoodsNum){
    this.badGoodsNum=badGoodsNum;
    }
    /*barcode get 方法 */
    public String getBarcode(){
    return barcode;
    }
    /*barcode set 方法 */
    public void setBarcode(String  barcode){
    this.barcode=barcode;
    }
    /*commbarcode get 方法 */
    public String getCommbarcode(){
    return commbarcode;
    }
    /*commbarcode set 方法 */
    public void setCommbarcode(String  commbarcode){
    this.commbarcode=commbarcode;
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

    public BigDecimal getDecTotal() {
        return decTotal;
    }

    public void setDecTotal(BigDecimal decTotal) {
        this.decTotal = decTotal;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
	public BigDecimal getTradeRefundAmount() {
		return tradeRefundAmount;
	}
	public void setTradeRefundAmount(BigDecimal tradeRefundAmount) {
		this.tradeRefundAmount = tradeRefundAmount;
	}
	public BigDecimal getTradeRefundTax() {
		return tradeRefundTax;
	}
	public void setTradeRefundTax(BigDecimal tradeRefundTax) {
		this.tradeRefundTax = tradeRefundTax;
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
