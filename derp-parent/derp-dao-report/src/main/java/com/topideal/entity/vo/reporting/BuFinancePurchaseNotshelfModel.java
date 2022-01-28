package com.topideal.entity.vo.reporting;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class BuFinancePurchaseNotshelfModel extends PageModel implements Serializable{

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
    * 事业部ID
    */
    private Long buId;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 订单id
    */
    private Long orderId;
    /**
    * 订单编码
    */
    private String orderCode;
    /**
    * 采购完结入库时间
    */
    private Timestamp orderEndDate;
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
    * 供应商id
    */
    private Long supplierId;
    /**
    * 供应商名称
    */
    private String supplierName;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 开发票时间(发票上面的时间)
    */
    private Timestamp drawInvoiceDate;
    /**
    * 发票号
    */
    private String invoiceNo;
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
    * 采购币种 (通用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String orderCurrency;
    /**
    * 采购金额
    */
    private BigDecimal orderAmount;
    /**
    * 数量
    */
    private Integer warehouseNum;
    /**
    * 入库币种 (通用)币种代码 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑
    */
    private String warehouseCurrency;
    /**
    * 入库单价(结算价格单价)
    */
    private BigDecimal warehousePrice;
    /**
    * 入库金额
    */
    private BigDecimal warehouseAmount;
    /**
    * 归属月份 YYYY-MM
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
    * 采购单价
    */
    private BigDecimal orderPrice;
    /**
    * 创建人id
    */
    private Long creater;
    /**
    * 创建人名称
    */
    private String createName;


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
    /*orderEndDate get 方法 */
    public Timestamp getOrderEndDate(){
    return orderEndDate;
    }
    /*orderEndDate set 方法 */
    public void setOrderEndDate(Timestamp  orderEndDate){
    this.orderEndDate=orderEndDate;
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
    /*supplierId get 方法 */
    public Long getSupplierId(){
    return supplierId;
    }
    /*supplierId set 方法 */
    public void setSupplierId(Long  supplierId){
    this.supplierId=supplierId;
    }
    /*supplierName get 方法 */
    public String getSupplierName(){
    return supplierName;
    }
    /*supplierName set 方法 */
    public void setSupplierName(String  supplierName){
    this.supplierName=supplierName;
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
    /*invoiceNo get 方法 */
    public String getInvoiceNo(){
    return invoiceNo;
    }
    /*invoiceNo set 方法 */
    public void setInvoiceNo(String  invoiceNo){
    this.invoiceNo=invoiceNo;
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
    /*warehouseNum get 方法 */
    public Integer getWarehouseNum(){
    return warehouseNum;
    }
    /*warehouseNum set 方法 */
    public void setWarehouseNum(Integer  warehouseNum){
    this.warehouseNum=warehouseNum;
    }
    /*warehouseCurrency get 方法 */
    public String getWarehouseCurrency(){
    return warehouseCurrency;
    }
    /*warehouseCurrency set 方法 */
    public void setWarehouseCurrency(String  warehouseCurrency){
    this.warehouseCurrency=warehouseCurrency;
    }
    /*warehousePrice get 方法 */
    public BigDecimal getWarehousePrice(){
    return warehousePrice;
    }
    /*warehousePrice set 方法 */
    public void setWarehousePrice(BigDecimal  warehousePrice){
    this.warehousePrice=warehousePrice;
    }
    /*warehouseAmount get 方法 */
    public BigDecimal getWarehouseAmount(){
    return warehouseAmount;
    }
    /*warehouseAmount set 方法 */
    public void setWarehouseAmount(BigDecimal  warehouseAmount){
    this.warehouseAmount=warehouseAmount;
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
    /*orderPrice get 方法 */
    public BigDecimal getOrderPrice(){
    return orderPrice;
    }
    /*orderPrice set 方法 */
    public void setOrderPrice(BigDecimal  orderPrice){
    this.orderPrice=orderPrice;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }

}
