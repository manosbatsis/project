package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class BuFinanceSaleShelfModel extends PageModel implements Serializable{

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
    private Long outDepotId;
    /**
    * 出库仓库名称
    */
    private String outDepotName;
    /**
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 客户名称
    */
    private String customerName;
    /**
    * 客户ID
    */
    private Long customerId;
    /**
    * 订单id
    */
    private Long orderId;
    /**
    * 订单编码
    */
    private String orderCode;
    /**
    * 1-销售代销订单 2-销售出库购销订单 3-电商订单 4-销售出库代销订单  5-电商订单退货 6-购销退货 7-账单出入库单调减 8-账单出入库单调增 9-购销整批结算 10-购销实销实结
    */
    private String orderType;
    /**
    * 订单日期 (订单创建日期)
    */
    private Timestamp orderCreateDate;
    /**
    * 上架时间
    */
    private Timestamp shelfDate;
    /**
    * 出库单编码
    */
    private String outOrderCode;
    /**
    * 销售出库订单id
    */
    private Long outOrderId;
    /**
    * 销售出库时间
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
    * 销售单价
    */
    private BigDecimal salePrice;
    /**
    * 销售金额
    */
    private BigDecimal saleAmount;
    /**
    * 出库币种 (通用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
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
    * 销售币种 (通用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String saleCurrency;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 电商平台名称
    */
    private String storePlatformName;
    /**
    * 外部单号
    */
    private String externalCode;
    /**
    * 税费
    */
    private BigDecimal taxes;
    /**
    * 运费
    */
    private BigDecimal wayFrtFee;
    /**
    * 优惠减免金额
    */
    private BigDecimal discount;
    /**
    * 订单实付金额
    */
    private BigDecimal payment;
    /**
    * 店铺编码
    */
    private String shopCode;
    /**
    * 店铺名称
    */
    private String shopName;
    /**
    * 电商平台编码
    */
    private String storePlatformCode;
    /**
    * 上架人id
    */
    private Long shelfId;
    /**
    * 上架人名称
    */
    private String shelfName;
    /**
    * 总佣金
    */
    private BigDecimal saleCom;
    /**
    * 平台订单号
    */
    private String exOrderId;
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
     * 本期sd金额
     */
    private BigDecimal sdOrderAmount;
    /**
     * 本期sd单价
     */
    private BigDecimal sdOrderPrice;
    /**
     * 店铺类型值编码 001:POP; 002:一件代发
     */
    private String shopTypeCode;
    /**
     * 渠道类型 To B,To C
     */
    private String channelType;
    /**
     * 母品牌NC编码
     */
    private String superiorParentBrandCode;
    /**
     * 上级母品牌id
     */
    private Long superiorParentBrandId;
    /**
     * SD利息单价
     */
    private BigDecimal sdInterestPrice;
     /**
     * SD利息金额
     */
    private BigDecimal sdInterestAmount;
    /**
     * 库位类型id
     */
    private Long stockLocationTypeId;
    /**
     * 库位类型名称
     */
    private String stockLocationTypeName;
    /**
     * 成本单价差异
     */
    private BigDecimal differencePrice;
    /**
     * 出库结转差异金额
     */
    private BigDecimal differenceAmount;
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
    /*outDepotId get 方法 */
    public Long getOutDepotId(){
    return outDepotId;
    }
    /*outDepotId set 方法 */
    public void setOutDepotId(Long  outDepotId){
    this.outDepotId=outDepotId;
    }
    /*outDepotName get 方法 */
    public String getOutDepotName(){
    return outDepotName;
    }
    /*outDepotName set 方法 */
    public void setOutDepotName(String  outDepotName){
    this.outDepotName=outDepotName;
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
    /*customerName get 方法 */
    public String getCustomerName(){
    return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    this.customerName=customerName;
    }
    /*customerId get 方法 */
    public Long getCustomerId(){
    return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    this.customerId=customerId;
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
    /*shelfDate get 方法 */
    public Timestamp getShelfDate(){
    return shelfDate;
    }
    /*shelfDate set 方法 */
    public void setShelfDate(Timestamp  shelfDate){
    this.shelfDate=shelfDate;
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
    /*saleCurrency get 方法 */
    public String getSaleCurrency(){
    return saleCurrency;
    }
    /*saleCurrency set 方法 */
    public void setSaleCurrency(String  saleCurrency){
    this.saleCurrency=saleCurrency;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*storePlatformName get 方法 */
    public String getStorePlatformName(){
    return storePlatformName;
    }
    /*storePlatformName set 方法 */
    public void setStorePlatformName(String  storePlatformName){
    this.storePlatformName=storePlatformName;
    }
    /*externalCode get 方法 */
    public String getExternalCode(){
    return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
    this.externalCode=externalCode;
    }
    /*taxes get 方法 */
    public BigDecimal getTaxes(){
    return taxes;
    }
    /*taxes set 方法 */
    public void setTaxes(BigDecimal  taxes){
    this.taxes=taxes;
    }
    /*wayFrtFee get 方法 */
    public BigDecimal getWayFrtFee(){
    return wayFrtFee;
    }
    /*wayFrtFee set 方法 */
    public void setWayFrtFee(BigDecimal  wayFrtFee){
    this.wayFrtFee=wayFrtFee;
    }
    /*discount get 方法 */
    public BigDecimal getDiscount(){
    return discount;
    }
    /*discount set 方法 */
    public void setDiscount(BigDecimal  discount){
    this.discount=discount;
    }
    /*payment get 方法 */
    public BigDecimal getPayment(){
    return payment;
    }
    /*payment set 方法 */
    public void setPayment(BigDecimal  payment){
    this.payment=payment;
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
    /*saleCom get 方法 */
    public BigDecimal getSaleCom(){
    return saleCom;
    }
    /*saleCom set 方法 */
    public void setSaleCom(BigDecimal  saleCom){
    this.saleCom=saleCom;
    }
    /*exOrderId get 方法 */
    public String getExOrderId(){
    return exOrderId;
    }
    /*exOrderId set 方法 */
    public void setExOrderId(String  exOrderId){
    this.exOrderId=exOrderId;
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

    public String getShopTypeCode() {
        return shopTypeCode;
    }

    public void setShopTypeCode(String shopTypeCode) {
        this.shopTypeCode = shopTypeCode;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getSuperiorParentBrandCode() {
        return superiorParentBrandCode;
    }

    public void setSuperiorParentBrandCode(String superiorParentBrandCode) {
        this.superiorParentBrandCode = superiorParentBrandCode;
    }

    public Long getSuperiorParentBrandId() {
        return superiorParentBrandId;
    }

    public void setSuperiorParentBrandId(Long superiorParentBrandId) {
        this.superiorParentBrandId = superiorParentBrandId;
    }
	public BigDecimal getSdInterestPrice() {
		return sdInterestPrice;
	}
	public void setSdInterestPrice(BigDecimal sdInterestPrice) {
		this.sdInterestPrice = sdInterestPrice;
	}
	public BigDecimal getSdInterestAmount() {
		return sdInterestAmount;
	}
	public void setSdInterestAmount(BigDecimal sdInterestAmount) {
		this.sdInterestAmount = sdInterestAmount;
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

    public BigDecimal getDifferencePrice() {
        return differencePrice;
    }

    public void setDifferencePrice(BigDecimal differencePrice) {
        this.differencePrice = differencePrice;
    }

    public BigDecimal getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(BigDecimal differenceAmount) {
        this.differenceAmount = differenceAmount;
    }
}
