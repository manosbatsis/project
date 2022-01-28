package com.topideal.entity.vo.reporting;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

public class BuFinanceSdAddPurchaseNotshelfModel extends PageModel implements Serializable{

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
    * 入库仓库id
    */
    private Long inDepotId;
    /**
    * 入库仓库名称
    */
    private String inDepotName;
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
    * 入库时间 (入库单创建时间)
    */
    private Timestamp warehouseCreateDate;
    /**
    * po单号
    */
    private String poNo;
    /**
    * 供应商id
    */
    private Long supplierId;
    /**
    * 供应商名称
    */
    private String supplierName;
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
    * 数量
    */
    private Integer warehouseNum;
    /**
    * 金额
    */
    private BigDecimal amount;
    /**
    * 上级母品牌
    */
    private String superiorParentBrand;
    /**
    * 品牌id
    */
    private Long brandId;
    /**
    * 品牌名称
    */
    private String brandName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
     * SD简称
     */
    private String sdSimpleName;
    /**
     * 归属月份
     */
    private String month;
    /**
     * SD类型
     */
    private String sdTypeName;
    /**
     * sd单id
     */
    private Long sdOrderId;
    /**
     * sd单号
     */
    private String sdOrderCode;

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
    /*inDepotId get 方法 */
    public Long getInDepotId(){
    return inDepotId;
    }
    /*inDepotId set 方法 */
    public void setInDepotId(Long  inDepotId){
    this.inDepotId=inDepotId;
    }
    /*inDepotName get 方法 */
    public String getInDepotName(){
    return inDepotName;
    }
    /*inDepotName set 方法 */
    public void setInDepotName(String  inDepotName){
    this.inDepotName=inDepotName;
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
    /*warehouseCreateDate get 方法 */
    public Timestamp getWarehouseCreateDate(){
    return warehouseCreateDate;
    }
    /*warehouseCreateDate set 方法 */
    public void setWarehouseCreateDate(Timestamp  warehouseCreateDate){
    this.warehouseCreateDate=warehouseCreateDate;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
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
    /*warehouseNum get 方法 */
    public Integer getWarehouseNum(){
    return warehouseNum;
    }
    /*warehouseNum set 方法 */
    public void setWarehouseNum(Integer  warehouseNum){
    this.warehouseNum=warehouseNum;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
    }
    /*superiorParentBrand get 方法 */
    public String getSuperiorParentBrand(){
    return superiorParentBrand;
    }
    /*superiorParentBrand set 方法 */
    public void setSuperiorParentBrand(String  superiorParentBrand){
    this.superiorParentBrand=superiorParentBrand;
    }
    /*brandId get 方法 */
    public Long getBrandId(){
    return brandId;
    }
    /*brandId set 方法 */
    public void setBrandId(Long  brandId){
    this.brandId=brandId;
    }
    /*brandName get 方法 */
    public String getBrandName(){
    return brandName;
    }
    /*brandName set 方法 */
    public void setBrandName(String  brandName){
    this.brandName=brandName;
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

    public String getSdSimpleName() {
        return sdSimpleName;
    }

    public void setSdSimpleName(String sdSimpleName) {
        this.sdSimpleName = sdSimpleName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSdTypeName() {
        return sdTypeName;
    }

    public void setSdTypeName(String sdTypeName) {
        this.sdTypeName = sdTypeName;
    }
	public Long getSdOrderId() {
		return sdOrderId;
	}
	public void setSdOrderId(Long sdOrderId) {
		this.sdOrderId = sdOrderId;
	}
	public String getSdOrderCode() {
		return sdOrderCode;
	}
	public void setSdOrderCode(String sdOrderCode) {
		this.sdOrderCode = sdOrderCode;
	}
    
    
}
