package com.topideal.entity.vo.order;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class PurchaseReturnOrderModel extends PageModel implements Serializable{

    /**
    * id
    */
    private Long id;
    /**
    * 采购退货订单号
    */
    private String code;
    /**
    * 合同号
    */
    private String contractNo;
    /**
    * 退入仓库id
    */
    private Long inDepotId;
    /**
    * 退入仓库名称
    */
    private String inDepotName;
    /**
    * 退出仓库id
    */
    private Long outDepotId;
    /**
    * 退出仓库名称
    */
    private String outDepotName;
    /**
    * 备注
    */
    private String remark;
    /**
    * 公司ID
    */
    private Long merchantId;
    /**
    * 公司名称
    */
    private String merchantName;
    /**
    * 创建时间
    */
    private Timestamp createDate;
    /**
    * 创建人
    */
    private Long creater;
    /**
    * 状态：001-待审核 ,003-已审核 ,006-已删除 ,016已出仓 ,007已完结
    */
    private String status;
    /**
    * 审核人
    */
    private Long auditor;
    /**
    * 审核时间
    */
    private Timestamp auditDate;
    /**
    * 客户id
    */
    private Long supplierId;
    /**
    * 客户名称
    */
    private String supplierName;
    /**
    * 计划退货数量
    */
    private Integer totalReturnNum;
    /**
    * 创建人名称
    */
    private String createName;
    /**
    * 审核人名称
    */
    private String auditName;
    /**
    * 修改时间
    */
    private Timestamp modifyDate;
    /**
    * 是否同关区（0-否，1-是）
    */
    private String isSameArea;
    /**
    * 币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑
    */
    private String currency;
    /**
    * 海外仓理货单位 00-托盘 01-箱 02-件
    */
    private String tallyingUnit;
    /**
    * 关联销售单单号
    */
    private String purchaseOrderRelCode;
    /**
    * 事业部名称
    */
    private String buName;
    /**
    *  事业部id
    */
    private Long buId;
    /**
    * po号
    */
    private String poNo;
    /**
    * 提货方式 1-邮寄 2-自提
    */
    private String deliveryMethod;
    /**
    * 收货地址
    */
    private String deliveryAddr;
    /**
    * 国家
    */
    private String country;
    /**
    * 收货人名称
    */
    private String deliveryName;
    /**
    * 目的地
    */
    private String destinationName;

    //本位币
    private String tgtCurrency;
    //汇率
    private BigDecimal rate;

    //目的地地址（内部使用）
    private String destinationAddress;
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
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*contractNo get 方法 */
    public String getContractNo(){
    return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
    this.contractNo=contractNo;
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
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
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
    /*createDate get 方法 */
    public Timestamp getCreateDate(){
    return createDate;
    }
    /*createDate set 方法 */
    public void setCreateDate(Timestamp  createDate){
    this.createDate=createDate;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    }
    /*auditor get 方法 */
    public Long getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(Long  auditor){
    this.auditor=auditor;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
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
    /*totalReturnNum get 方法 */
    public Integer getTotalReturnNum(){
    return totalReturnNum;
    }
    /*totalReturnNum set 方法 */
    public void setTotalReturnNum(Integer  totalReturnNum){
    this.totalReturnNum=totalReturnNum;
    }
    /*createName get 方法 */
    public String getCreateName(){
    return createName;
    }
    /*createName set 方法 */
    public void setCreateName(String  createName){
    this.createName=createName;
    }
    /*auditName get 方法 */
    public String getAuditName(){
    return auditName;
    }
    /*auditName set 方法 */
    public void setAuditName(String  auditName){
    this.auditName=auditName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }
    /*isSameArea get 方法 */
    public String getIsSameArea(){
    return isSameArea;
    }
    /*isSameArea set 方法 */
    public void setIsSameArea(String  isSameArea){
    this.isSameArea=isSameArea;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    }
    /*purchaseOrderRelCode get 方法 */
    public String getPurchaseOrderRelCode(){
    return purchaseOrderRelCode;
    }
    /*purchaseOrderRelCode set 方法 */
    public void setPurchaseOrderRelCode(String  purchaseOrderRelCode){
    this.purchaseOrderRelCode=purchaseOrderRelCode;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*deliveryMethod get 方法 */
    public String getDeliveryMethod(){
    return deliveryMethod;
    }
    /*deliveryMethod set 方法 */
    public void setDeliveryMethod(String  deliveryMethod){
    this.deliveryMethod=deliveryMethod;
    }
    /*deliveryAddr get 方法 */
    public String getDeliveryAddr(){
    return deliveryAddr;
    }
    /*deliveryAddr set 方法 */
    public void setDeliveryAddr(String  deliveryAddr){
    this.deliveryAddr=deliveryAddr;
    }
    /*country get 方法 */
    public String getCountry(){
    return country;
    }
    /*country set 方法 */
    public void setCountry(String  country){
    this.country=country;
    }
    /*deliveryName get 方法 */
    public String getDeliveryName(){
    return deliveryName;
    }
    /*deliveryName set 方法 */
    public void setDeliveryName(String  deliveryName){
    this.deliveryName=deliveryName;
    }
    /*destinationName get 方法 */
    public String getDestinationName(){
    return destinationName;
    }
    /*destinationName set 方法 */
    public void setDestinationName(String  destinationName){
    this.destinationName=destinationName;
    }

    public String getTgtCurrency() {
        return tgtCurrency;
    }

    public void setTgtCurrency(String tgtCurrency) {
        this.tgtCurrency = tgtCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
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
