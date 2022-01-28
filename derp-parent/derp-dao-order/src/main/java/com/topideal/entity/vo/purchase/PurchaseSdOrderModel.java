package com.topideal.entity.vo.purchase;
import java.io.Serializable;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import java.math.BigDecimal;

public class PurchaseSdOrderModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购订单编号
    */
    private String purchaseCode;
    /**
    * PO号
    */
    private String poNo;
    /**
    * 供应商ID
    */
    private Long supplierId;
    /**
    * 供应商名称
    */
    private String supplierName;
    /**
    * 入仓时间
    */
    private Timestamp inboundDate;
    /**
    * 商家ID
    */
    private Long merchantId;
    /**
    * 商家名称
    */
    private String merchantName;
    /**
    * 汇率
    */
    private Double rate;
    /**
    * 币种
    */
    private String currency;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    * 事业部id
    */
    private Long buId;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 创建人用户名
    */
    private String createName;
    /**
     * 创建时间
     */
     private Timestamp createDate;
     /**
     * 修改时间
     */
     private Timestamp modifyDate;
    /**
     * 采购SD配置ID
     */
    private Long sdPurchaseConfigId;
    /**
     * 本位币
     */
    private String tgtCurrency;
    //是否已同步宝信（0-否，1-是）
    private String isSyn;
    //采购SD编号
    private String code;
    //1-采购SD，2-采购退SD，3-调整SD
    private String type;
    //仓库ID
    private Long depotId;
    //仓库名称
    private String depotName;
    /**
     * SD金额
     */
    private BigDecimal sdAmount;
    /**
     * SD类型
     */
    private String sdTypeName;
    /**
     * SD简称
     */
    private String sdSimpleName;
    //备注
    private String remarks;
    //状态 006-已删除
    private String status;


    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*purchaseCode get 方法 */
    public String getPurchaseCode(){
    return purchaseCode;
    }
    /*purchaseCode set 方法 */
    public void setPurchaseCode(String  purchaseCode){
    this.purchaseCode=purchaseCode;
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
    /*inboundDate get 方法 */
    public Timestamp getInboundDate(){
    return inboundDate;
    }
    /*inboundDate set 方法 */
    public void setInboundDate(Timestamp  inboundDate){
    this.inboundDate=inboundDate;
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
    /*rate get 方法 */
    public Double getRate(){
    return rate;
    }
    /*rate set 方法 */
    public void setRate(Double  rate){
    this.rate=rate;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }

    public Long getSdPurchaseConfigId() {
        return sdPurchaseConfigId;
    }

    public void setSdPurchaseConfigId(Long sdPurchaseConfigId) {
        this.sdPurchaseConfigId = sdPurchaseConfigId;
    }
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

    public String getTgtCurrency() {
        return tgtCurrency;
    }

    public void setTgtCurrency(String tgtCurrency) {
        this.tgtCurrency = tgtCurrency;
    }

    public String getIsSyn() {
        return isSyn;
    }

    public void setIsSyn(String isSyn) {
        this.isSyn = isSyn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDepotId() {
        return depotId;
    }

    public void setDepotId(Long depotId) {
        this.depotId = depotId;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public BigDecimal getSdAmount() {
        return sdAmount;
    }

    public void setSdAmount(BigDecimal sdAmount) {
        this.sdAmount = sdAmount;
    }

    public String getSdTypeName() {
        return sdTypeName;
    }

    public void setSdTypeName(String sdTypeName) {
        this.sdTypeName = sdTypeName;
    }

    public String getSdSimpleName() {
        return sdSimpleName;
    }

    public void setSdSimpleName(String sdSimpleName) {
        this.sdSimpleName = sdSimpleName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
