package com.topideal.entity.dto.purchase;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class PurchaseReturnOrderDTO extends PageModel implements Serializable{

    /**
    * id
    */
	@ApiModelProperty(value="记录ID", required=false)
    private Long id;
    /**
    * 采购退货订单号
    */
	@ApiModelProperty(value="采购退货订单号", required=false)
    private String code;
    /**
    * 合同号
    */
	@ApiModelProperty(value="合同号", required=false)
    private String contractNo;
    /**
    * 退入仓库id
    */
	@ApiModelProperty(value="退入仓库id", required=false)
    private Long inDepotId;
    /**
    * 退入仓库名称
    */
	@ApiModelProperty(value="退入仓库名称", required=false)
    private String inDepotName;
    /**
    * 退出仓库id
    */
	@ApiModelProperty(value="退出仓库id", required=false)
    private Long outDepotId;
    /**
    * 退出仓库名称
    */
	@ApiModelProperty(value="退出仓库名称", required=false)
    private String outDepotName;
    /**
    * 备注
    */
	@ApiModelProperty(value="备注", required=false)
    private String remark;
    /**
    * 公司ID
    */
	@ApiModelProperty(value="公司ID", required=false)
    private Long merchantId;
    /**
    * 公司名称
    */
	@ApiModelProperty(value="公司名称", required=false)
    private String merchantName;
    /**
    * 创建时间
    */
	@ApiModelProperty(value="创建时间", required=false)
    private Timestamp createDate;
    /**
    * 创建人
    */
	@ApiModelProperty(value="创建人", required=false)
    private Long creater;
    /**
    * 状态：001-待审核 ,003-已审核 ,006-已删除 ,016已出仓 ,007已完结
    */
	@ApiModelProperty(value="状态：001-待审核 ,003-已审核 ,006-已删除 ,016已出仓 ,007已完结", required=false)
    private String status;
	@ApiModelProperty(value="状态中文：001-待审核 ,003-已审核 ,006-已删除 ,016已出仓 ,007已完结", required=false)
    private String statusLabel;
    /**
    * 审核人
    */
	@ApiModelProperty(value="审核人", required=false)
    private Long auditor;
    /**
    * 审核时间
    */
	@ApiModelProperty(value="审核时间", required=false)
    private Timestamp auditDate;
    /**
    * 客户id
    */
	@ApiModelProperty(value="供应商id", required=false)
    private Long supplierId;
    /**
    * 客户名称
    */
	@ApiModelProperty(value="供应商名称", required=false)
    private String supplierName;
    /**
    * 计划退货数量
    */
	@ApiModelProperty(value="计划退货数量", required=false)
    private Integer totalReturnNum;
    /**
    * 创建人名称
    */
	@ApiModelProperty(value="创建人名称", required=false)
    private String createName;
    /**
    * 审核人名称
    */
	@ApiModelProperty(value="审核人名称", required=false)
    private String auditName;
    /**
    * 修改时间
    */
	@ApiModelProperty(value="修改时间", required=false)
    private Timestamp modifyDate;
    /**
    * 是否同关区（0-否，1-是）
    */
	@ApiModelProperty(value="是否同关区（0-否，1-是）", required=false)
    private String isSameArea;
	@ApiModelProperty(value="是否同关区中文（0-否，1-是）", required=false)
    private String isSameAreaLabel;
    /**
    * 币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑
    */
	@ApiModelProperty(value="币种", required=false)
    private String currency;
	@ApiModelProperty(value="币种中文", required=false)
    private String currencyLabel;
    
    /**
    * 海外仓理货单位 00-托盘 01-箱 02-件
    */
	@ApiModelProperty(value="海外仓理货单位", required=false)
    private String tallyingUnit;
	@ApiModelProperty(value="海外仓理货单位中文", required=false)
    private String tallyingUnitLabel;
    /**
    * 关联销售单单号
    */
    @ApiModelProperty(value="关联采购单单号", required=false)
    private String purchaseOrderRelCode;
    /**
    * 事业部名称
    */
    @ApiModelProperty(value="事业部名称", required=false)
    private String buName;
    /**
    *  事业部id
    */
    @ApiModelProperty(value="事业部id", required=false)
    private Long buId;
    @ApiModelProperty(value="事业部id", required=false, hidden=false)
    private List<Long> buIds;
    /**
    * po号
    */
    @ApiModelProperty(value="po号", required=false)
    private String poNo;
    /**
    * 提货方式 1-邮寄 2-自提
    */
    @ApiModelProperty(value="提货方式 1-邮寄 2-自提", required=false)
    private String deliveryMethod;
    @ApiModelProperty(value="提货方式中文 1-邮寄 2-自提", required=false)
    private String deliveryMethodLabel;
    /**
    * 收货地址
    */
    @ApiModelProperty(value="收货地址", required=false)
    private String deliveryAddr;
    /**
    * 国家
    */
    @ApiModelProperty(value="国家", required=false)
    private String country;
    /**
    * 收货人名称
    */
    @ApiModelProperty(value="收货人名称", required=false)
    private String deliveryName;
    /**
    * 目的地
    */
    @ApiModelProperty(value="目的地", required=false)
    private String destinationName;
    @ApiModelProperty(value="目的地中文", required=false)
    private String destinationNameLabel;
    
    @ApiModelProperty(value="创建开始时间", required=false)
    private String createStartDate ;
    @ApiModelProperty(value="创建结束时间", required=false)
    private String createEndDate ;
    @ApiModelProperty(value="表体明细", required=false)
    private List<PurchaseReturnItemModel> itemList ;

    //本位币
    @ApiModelProperty(value="本位币", required=false)
    private String tgtCurrency;
    //汇率
    @ApiModelProperty(value="汇率", required=false)
    private BigDecimal rate;
    //目的地地址（内部使用）
    @ApiModelProperty(value="目的地地址（内部使用）", required=false)
    private String destinationAddress;
    
    //是否可以出库打托
    @ApiModelProperty(value="是否可以出库打托", required=false)
    private String isOutDepotAble ;
    
    //仓库类型
    @ApiModelProperty(value="仓库类型", required=false)
    private String depotType ;

    @ApiModelProperty(value = "订单ID,多个以‘,’隔开", required = false)
    private String ids;

    /**
     * 库位类型id
     */
    @ApiModelProperty(value = "库位类型id")
    private Long stockLocationTypeId;
    /**
     * 库位类型
     */
    @ApiModelProperty(value = "库位类型")
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
    this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_statusList, status) ;
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
    this.isSameAreaLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_isSameAreaList, isSameArea) ;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    this.currencyLabel = DERP.getLabelByKey(DERP.currencyCodeList, currency) ;
    }
    /*tallyingUnit get 方法 */
    public String getTallyingUnit(){
    return tallyingUnit;
    }
    /*tallyingUnit set 方法 */
    public void setTallyingUnit(String  tallyingUnit){
    this.tallyingUnit=tallyingUnit;
    this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit) ;
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
    this.deliveryMethodLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_mailModeList, deliveryMethod) ;
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
    this.destinationNameLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseReturnOrder_destinationList, destinationName);
    }
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getIsSameAreaLabel() {
		return isSameAreaLabel;
	}
	public void setIsSameAreaLabel(String isSameAreaLabel) {
		this.isSameAreaLabel = isSameAreaLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}
	public String getDeliveryMethodLabel() {
		return deliveryMethodLabel;
	}
	public void setDeliveryMethodLabel(String deliveryMethodLabel) {
		this.deliveryMethodLabel = deliveryMethodLabel;
	}
	public String getCreateStartDate() {
		return createStartDate;
	}
	public void setCreateStartDate(String createStartDate) {
		this.createStartDate = createStartDate;
	}
	public String getCreateEndDate() {
		return createEndDate;
	}
	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}
	public String getDestinationNameLabel() {
		return destinationNameLabel;
	}
	public void setDestinationNameLabel(String destinationNameLabel) {
		this.destinationNameLabel = destinationNameLabel;
	}
	public List<PurchaseReturnItemModel> getItemList() {
		return itemList;
	}
	public void setItemList(List<PurchaseReturnItemModel> itemList) {
		this.itemList = itemList;
	}
	public List<Long> getBuIds() {
		return buIds;
	}
	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
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
	public String getIsOutDepotAble() {
		return isOutDepotAble;
	}
	public void setIsOutDepotAble(String isOutDepotAble) {
		this.isOutDepotAble = isOutDepotAble;
	}
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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
