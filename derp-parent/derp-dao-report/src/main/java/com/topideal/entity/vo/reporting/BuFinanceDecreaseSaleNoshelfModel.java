package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class BuFinanceDecreaseSaleNoshelfModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 商家id
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 出库仓库id
    */
    private Long depotId;
    /**
    * 出库仓库名称
    */
    private String depotName;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 订单id
    */
    private Long orderId;
    /**
    * 订单编码
    */
    private String orderCode;
    /**
    * 1-代销订单 2-购销订单 3-购销整批结算 4-购销实销实结
    */
    private String orderType;
    /**
    * 订单日期 (订单创建日期)
    */
    private Timestamp orderCreateDate;
    /**
    * 出库单编码
    */
    private String outOrderCode;
    /**
    * 销售出库订单id
    */
    private Long outOrderId;
    /**
    * 出库时间
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
    * 商品条码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 00-托盘，01-箱，02-件
    */
    private String tallyingUnit;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 销售币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String saleCurrency;
    /**
    * 销售单价
    */
    private BigDecimal salePrice;
    /**
    * 销售金额
    */
    private BigDecimal saleAmount;
    /**
    * 出库币种CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String outDepotCurrency;
    /**
    * 出库单价
    */
    private BigDecimal outDepotPrice;
    /**
    * 出库金额
    */
    private BigDecimal outDepotAmount;
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
    /**
    * po单号
    */
    private String poNo;
    /**
    * 本次上架好品数量
    */
    private Integer shelfNum;
    /**
    * 本次残损数量
    */
    private Integer damagedNum;
    /**
    * 少货数量
    */
    private Integer lackNum;
    /**
    * 上架时间
    */
    private Timestamp shelfDate;
    /**
    * 上架人id
    */
    private Long shelfId;
    /**
    * 上架人名称
    */
    private String shelfName;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 上级母品牌
     */
    private String superiorParentBrand;   
    /**
     * 本期SD金额
     */
    private BigDecimal sdOrderAmount;
    /**
     * 本期sd单价
     */
    private BigDecimal sdOrderPrice;
    


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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*buName get 方法 */
    public String getBuName(){
    return buName;
    }
    /*buName set 方法 */
    public void setBuName(String  buName){
    this.buName=buName;
    }
    /*buId get 方法 */
    public Long getBuId(){
    return buId;
    }
    /*buId set 方法 */
    public void setBuId(Long  buId){
    this.buId=buId;
    }
    /*orderId get 方法 */
    public Long getOrderId(){
    return orderId;
    }
    /*orderId set 方法 */
    public void setOrderId(Long  orderId){
    this.orderId=orderId;
    }
    /*orderCode get 方法 */
    public String getOrderCode(){
    return orderCode;
    }
    /*orderCode set 方法 */
    public void setOrderCode(String  orderCode){
    this.orderCode=orderCode;
    }
    /*orderType get 方法 */
    public String getOrderType(){
    return orderType;
    }
    /*orderType set 方法 */
    public void setOrderType(String  orderType){
    this.orderType=orderType;
    }
    /*orderCreateDate get 方法 */
    public Timestamp getOrderCreateDate(){
    return orderCreateDate;
    }
    /*orderCreateDate set 方法 */
    public void setOrderCreateDate(Timestamp  orderCreateDate){
    this.orderCreateDate=orderCreateDate;
    }
    /*outOrderCode get 方法 */
    public String getOutOrderCode(){
    return outOrderCode;
    }
    /*outOrderCode set 方法 */
    public void setOutOrderCode(String  outOrderCode){
    this.outOrderCode=outOrderCode;
    }
    /*outOrderId get 方法 */
    public Long getOutOrderId(){
    return outOrderId;
    }
    /*outOrderId set 方法 */
    public void setOutOrderId(Long  outOrderId){
    this.outOrderId=outOrderId;
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
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*saleCurrency get 方法 */
    public String getSaleCurrency(){
    return saleCurrency;
    }
    /*saleCurrency set 方法 */
    public void setSaleCurrency(String  saleCurrency){
    this.saleCurrency=saleCurrency;
    }
    /*salePrice get 方法 */
    public BigDecimal getSalePrice(){
    return salePrice;
    }
    /*salePrice set 方法 */
    public void setSalePrice(BigDecimal  salePrice){
    this.salePrice=salePrice;
    }
    /*saleAmount get 方法 */
    public BigDecimal getSaleAmount(){
    return saleAmount;
    }
    /*saleAmount set 方法 */
    public void setSaleAmount(BigDecimal  saleAmount){
    this.saleAmount=saleAmount;
    }
    /*outDepotCurrency get 方法 */
    public String getOutDepotCurrency(){
    return outDepotCurrency;
    }
    /*outDepotCurrency set 方法 */
    public void setOutDepotCurrency(String  outDepotCurrency){
    this.outDepotCurrency=outDepotCurrency;
    }
    /*outDepotPrice get 方法 */
    public BigDecimal getOutDepotPrice(){
    return outDepotPrice;
    }
    /*outDepotPrice set 方法 */
    public void setOutDepotPrice(BigDecimal  outDepotPrice){
    this.outDepotPrice=outDepotPrice;
    }
    /*outDepotAmount get 方法 */
    public BigDecimal getOutDepotAmount(){
    return outDepotAmount;
    }
    /*outDepotAmount set 方法 */
    public void setOutDepotAmount(BigDecimal  outDepotAmount){
    this.outDepotAmount=outDepotAmount;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*shelfNum get 方法 */
    public Integer getShelfNum(){
    return shelfNum;
    }
    /*shelfNum set 方法 */
    public void setShelfNum(Integer  shelfNum){
    this.shelfNum=shelfNum;
    }
    /*damagedNum get 方法 */
    public Integer getDamagedNum(){
    return damagedNum;
    }
    /*damagedNum set 方法 */
    public void setDamagedNum(Integer  damagedNum){
    this.damagedNum=damagedNum;
    }
    /*lackNum get 方法 */
    public Integer getLackNum(){
    return lackNum;
    }
    /*lackNum set 方法 */
    public void setLackNum(Integer  lackNum){
    this.lackNum=lackNum;
    }
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
    }
    /*shelfId get 方法 */
    public Long getShelfId(){
    return shelfId;
    }
    /*shelfId set 方法 */
    public void setShelfId(Long  shelfId){
    this.shelfId=shelfId;
    }
    /*shelfName get 方法 */
    public String getShelfName(){
    return shelfName;
    }
    /*shelfName set 方法 */
    public void setShelfName(String  shelfName){
    this.shelfName=shelfName;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSuperiorParentBrand() {
        return superiorParentBrand;
    }

    public void setSuperiorParentBrand(String superiorParentBrand) {
        this.superiorParentBrand = superiorParentBrand;
    }
	public BigDecimal getSdOrderAmount() {
		return sdOrderAmount;
	}
	public void setSdOrderAmount(BigDecimal sdOrderAmount) {
		this.sdOrderAmount = sdOrderAmount;
	}
	public BigDecimal getSdOrderPrice() {
		return sdOrderPrice;
	}
	public void setSdOrderPrice(BigDecimal sdOrderPrice) {
		this.sdOrderPrice = sdOrderPrice;
	}
    
}
