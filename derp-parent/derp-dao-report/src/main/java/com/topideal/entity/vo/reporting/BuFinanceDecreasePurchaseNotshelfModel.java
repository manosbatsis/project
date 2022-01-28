package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class BuFinanceDecreasePurchaseNotshelfModel extends PageModel implements Serializable{

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
    * 仓库名称
    */
    private String depotName;
    /**
    * 仓库ID
    */
    private Long depotId;
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
    * 订单日期 (订单创建日期)
    */
    private Timestamp orderCreateDate;
    /**
    * 入库单id,多个用,号隔开
    */
    private String warehouseIds;
    /**
    * 入库单编码,多个用逗号隔开
    */
    private String warehouseCodes;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 开发票时间(发票上面的时间)
    */
    private Timestamp drawInvoiceDate;
    /**
    * 商品id
    */
    private Long goodsId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品货号
    */
    private String goodsNo;
    /**
    * 商品条码
    */
    private String barcode;
    /**
    * 标准条码
    */
    private String commbarcode;
    /**
    * 理货单位(00-托盘，01-箱，02-件)
    */
    private String tallyingUnit;
    /**
    * 数量
    */
    private Integer num;
    /**
    * 采购币种CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String orderCurrency;
    /**
    * 采购金额
    */
    private BigDecimal orderAmount;
    /**
    * 入库单价(结算价格单价)
    */
    private BigDecimal warehousePrice;
    /**
    * 归属月份 YYYY-MM
    */
    private String month;
    /**
    * 入库币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String warehouseCurrency;
    /**
    * 入库金额
    */
    private BigDecimal warehouseAmount;
    /**
    * 采购单价
    */
    private BigDecimal orderPrice;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 入库时间
    */
    private String warehouseCreateDates;
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
     * sd 本币金额
     */
    private BigDecimal sdTgtAmount;

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
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
    }
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
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
    /*orderCreateDate get 方法 */
    public Timestamp getOrderCreateDate(){
    return orderCreateDate;
    }
    /*orderCreateDate set 方法 */
    public void setOrderCreateDate(Timestamp  orderCreateDate){
    this.orderCreateDate=orderCreateDate;
    }
    /*warehouseIds get 方法 */
    public String getWarehouseIds(){
    return warehouseIds;
    }
    /*warehouseIds set 方法 */
    public void setWarehouseIds(String  warehouseIds){
    this.warehouseIds=warehouseIds;
    }
    /*warehouseCodes get 方法 */
    public String getWarehouseCodes(){
    return warehouseCodes;
    }
    /*warehouseCodes set 方法 */
    public void setWarehouseCodes(String  warehouseCodes){
    this.warehouseCodes=warehouseCodes;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*drawInvoiceDate get 方法 */
    public Timestamp getDrawInvoiceDate(){
    return drawInvoiceDate;
    }
    /*drawInvoiceDate set 方法 */
    public void setDrawInvoiceDate(Timestamp  drawInvoiceDate){
    this.drawInvoiceDate=drawInvoiceDate;
    }
    /*goodsId get 方法 */
    public Long getGoodsId(){
    return goodsId;
    }
    /*goodsId set 方法 */
    public void setGoodsId(Long  goodsId){
    this.goodsId=goodsId;
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
    /*orderCurrency get 方法 */
    public String getOrderCurrency(){
    return orderCurrency;
    }
    /*orderCurrency set 方法 */
    public void setOrderCurrency(String  orderCurrency){
    this.orderCurrency=orderCurrency;
    }
    /*orderAmount get 方法 */
    public BigDecimal getOrderAmount(){
    return orderAmount;
    }
    /*orderAmount set 方法 */
    public void setOrderAmount(BigDecimal  orderAmount){
    this.orderAmount=orderAmount;
    }
    /*warehousePrice get 方法 */
    public BigDecimal getWarehousePrice(){
    return warehousePrice;
    }
    /*warehousePrice set 方法 */
    public void setWarehousePrice(BigDecimal  warehousePrice){
    this.warehousePrice=warehousePrice;
    }
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*warehouseCurrency get 方法 */
    public String getWarehouseCurrency(){
    return warehouseCurrency;
    }
    /*warehouseCurrency set 方法 */
    public void setWarehouseCurrency(String  warehouseCurrency){
    this.warehouseCurrency=warehouseCurrency;
    }
    /*warehouseAmount get 方法 */
    public BigDecimal getWarehouseAmount(){
    return warehouseAmount;
    }
    /*warehouseAmount set 方法 */
    public void setWarehouseAmount(BigDecimal  warehouseAmount){
    this.warehouseAmount=warehouseAmount;
    }
    /*orderPrice get 方法 */
    public BigDecimal getOrderPrice(){
    return orderPrice;
    }
    /*orderPrice set 方法 */
    public void setOrderPrice(BigDecimal  orderPrice){
    this.orderPrice=orderPrice;
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
    /*warehouseCreateDates get 方法 */
    public String getWarehouseCreateDates(){
    return warehouseCreateDates;
    }
    /*warehouseCreateDates set 方法 */
    public void setWarehouseCreateDates(String  warehouseCreateDates){
    this.warehouseCreateDates=warehouseCreateDates;
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
	public BigDecimal getSdTgtAmount() {
		return sdTgtAmount;
	}
	public void setSdTgtAmount(BigDecimal sdTgtAmount) {
		this.sdTgtAmount = sdTgtAmount;
	}

    
}
