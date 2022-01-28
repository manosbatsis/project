package com.topideal.entity.dto.sale;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PreSaleOrderCorrelationDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;
   
	@ApiModelProperty(value = "公司ID")
    private Long merchantId;
    
	@ApiModelProperty(value = "公司名称")
    private String merchantName;
  
	@ApiModelProperty(value = "预售单ID")
    private Long preSaleOrderId;
    
	@ApiModelProperty(value = "预售单号")
    private String preSaleOrderCode;
    
	@ApiModelProperty(value = "销售单号")
    private String saleOrderCode;
    
	@ApiModelProperty(value = "销售出库单号")
    private String saleOutDepotCode;
    
	@ApiModelProperty(value = "商品ID")
    private Long goodsId;
    
	@ApiModelProperty(value = "商品货号")
    private String goodsNo;
    
	@ApiModelProperty(value = "商品名称")
    private String goodsName;
    
	@ApiModelProperty(value = "条形码")
    private String barcode;
    
	@ApiModelProperty(value = "标准条码")
    private String commbarcode;
    
	@ApiModelProperty(value = "预售数量")
    private Integer preNum;
    
	@ApiModelProperty(value = "销售数量")
    private Integer saleNum;
    
	@ApiModelProperty(value = "出库数量")
    private Integer outNum;
    
	@ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;
    
	@ApiModelProperty(value = "审核人")
    private Long auditor;
    
	@ApiModelProperty(value = "审核人用户名")
    private String auditName;
    
	@ApiModelProperty(value = "出库时间")
    private Timestamp outDepotDate;
    
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

	@ApiModelProperty(value = "审核开始时间" ,hidden=true)
    private String auditStartDate;

	@ApiModelProperty(value = "审核结束时间",hidden=true)
    private String auditEndDate;

	@ApiModelProperty(value = "出库开始时间",hidden=true)
    private String outDepotStartDate;

	@ApiModelProperty(value = "出库结束时间",hidden=true)
    private String outDepotEndDate;
  
	@ApiModelProperty(value = "客户ID")
    private Long customerId;
  
	@ApiModelProperty(value = "客户名称")
    private String customerName;
  
	@ApiModelProperty(value = "出仓仓库ID")
    private Long outDepotId;
  
	@ApiModelProperty(value = "出仓仓库名称")
    private String outDepotName;
  
	@ApiModelProperty(value = "事业部id")
    private Long buId;
  
	@ApiModelProperty(value = "事业部名称")
    private String buName;
  
	@ApiModelProperty(value = "po单号")
    private String poNo;
	
	@ApiModelProperty(value = "单价")
    private BigDecimal price;
  
	@ApiModelProperty(value = "销售总金额")
    private BigDecimal amount;
  
	@ApiModelProperty(value = "品牌类型")
    private String brandName;
	
	@ApiModelProperty(value = "商品编码")
    private String goodsCode;

	@ApiModelProperty(value = "业务模式  1 购销-整批结算  4.购销-实销实结")
    private String businessModel;

	@ApiModelProperty(value = "待销量")
    private Integer staySaleNum;

	@ApiModelProperty(value = "事业部集合")
    private List<Long> buList;

	@ApiModelProperty(value = "毛重")
    private Double grossWeight;
   
	@ApiModelProperty(value = "净重")
    private Double netWeight;

	@ApiModelProperty(value = "主键id集合")
	private String ids;
	
    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    public Integer getStaySaleNum() {
        return staySaleNum;
    }

    public void setStaySaleNum(Integer staySaleNum) {
        this.staySaleNum = staySaleNum;
    }

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
    /*preSaleOrderId get 方法 */
    public Long getPreSaleOrderId(){
    return preSaleOrderId;
    }
    /*preSaleOrderId set 方法 */
    public void setPreSaleOrderId(Long  preSaleOrderId){
    this.preSaleOrderId=preSaleOrderId;
    }
    /*preSaleOrderCode get 方法 */
    public String getPreSaleOrderCode(){
    return preSaleOrderCode;
    }
    /*preSaleOrderCode set 方法 */
    public void setPreSaleOrderCode(String  preSaleOrderCode){
    this.preSaleOrderCode=preSaleOrderCode;
    }
    /*saleOrderCode get 方法 */
    public String getSaleOrderCode(){
    return saleOrderCode;
    }
    /*saleOrderCode set 方法 */
    public void setSaleOrderCode(String  saleOrderCode){
    this.saleOrderCode=saleOrderCode;
    }
    /*saleOutDepotCode get 方法 */
    public String getSaleOutDepotCode(){
    return saleOutDepotCode;
    }
    /*saleOutDepotCode set 方法 */
    public void setSaleOutDepotCode(String  saleOutDepotCode){
    this.saleOutDepotCode=saleOutDepotCode;
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
    /*preNum get 方法 */
    public Integer getPreNum(){
    return preNum;
    }
    /*preNum set 方法 */
    public void setPreNum(Integer  preNum){
    this.preNum=preNum;
    }
    /*saleNum get 方法 */
    public Integer getSaleNum(){
    return saleNum;
    }
    /*saleNum set 方法 */
    public void setSaleNum(Integer  saleNum){
    this.saleNum=saleNum;
    }
    /*outNum get 方法 */
    public Integer getOutNum(){
    return outNum;
    }
    /*outNum set 方法 */
    public void setOutNum(Integer  outNum){
    this.outNum=outNum;
    }
    /*auditDate get 方法 */
    public Timestamp getAuditDate(){
    return auditDate;
    }
    /*auditDate set 方法 */
    public void setAuditDate(Timestamp  auditDate){
    this.auditDate=auditDate;
    }
    /*auditor get 方法 */
    public Long getAuditor(){
    return auditor;
    }
    /*auditor set 方法 */
    public void setAuditor(Long  auditor){
    this.auditor=auditor;
    }
    /*auditName get 方法 */
    public String getAuditName(){
    return auditName;
    }
    /*auditName set 方法 */
    public void setAuditName(String  auditName){
    this.auditName=auditName;
    }
    /*outDepotDate get 方法 */
    public Timestamp getOutDepotDate(){
    return outDepotDate;
    }
    /*outDepotDate set 方法 */
    public void setOutDepotDate(Timestamp  outDepotDate){
    this.outDepotDate=outDepotDate;
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

    public String getAuditStartDate() {
        return auditStartDate;
    }

    public void setAuditStartDate(String auditStartDate) {
        this.auditStartDate = auditStartDate;
    }

    public String getAuditEndDate() {
        return auditEndDate;
    }

    public void setAuditEndDate(String auditEndDate) {
        this.auditEndDate = auditEndDate;
    }

    public String getOutDepotStartDate() {
        return outDepotStartDate;
    }

    public void setOutDepotStartDate(String outDepotStartDate) {
        this.outDepotStartDate = outDepotStartDate;
    }

    public String getOutDepotEndDate() {
        return outDepotEndDate;
    }

    public void setOutDepotEndDate(String outDepotEndDate) {
        this.outDepotEndDate = outDepotEndDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getOutDepotId() {
        return outDepotId;
    }

    public void setOutDepotId(Long outDepotId) {
        this.outDepotId = outDepotId;
    }

    public String getOutDepotName() {
        return outDepotName;
    }

    public void setOutDepotName(String outDepotName) {
        this.outDepotName = outDepotName;
    }

    public Long getBuId() {
        return buId;
    }

    public void setBuId(Long buId) {
        this.buId = buId;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }

    public Double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public Double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(Double netWeight) {
        this.netWeight = netWeight;
    }

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
}
