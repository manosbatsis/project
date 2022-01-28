package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class MaterialOutDepotDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;
 
	@ApiModelProperty(value = "领料单id")
    private Long materialOrderId;
  
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;
  
	@ApiModelProperty(value = "PO号")
    private String poNo;
   
	@ApiModelProperty(value = "调出仓库id")
    private Long outDepotId;
   
	@ApiModelProperty(value = "调出仓库名称")
    private String outDepotName;
  
	@ApiModelProperty(value = "客户id(供应商)")
    private Long customerId;
   
	@ApiModelProperty(value = "客户名称 ")
    private String customerName;
   
	@ApiModelProperty(value = "发货时间")
    private Timestamp deliverDate;
   
	@ApiModelProperty(value = "状态 017-待出库,018-已出库,027-出库中 006-已删除 ")
    private String status;
	@ApiModelProperty(value = "状态(中文)")
    private String statusLabel;
   
	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
   
	@ApiModelProperty(value = "创建人")
    private Long creater;
    
	@ApiModelProperty(value = "出库清单编号")
    private String code;
  
	@ApiModelProperty(value = "商家名称")
    private String merchantName;
    
	@ApiModelProperty(value = "领料单编号")
    private String materialOrderCode;
  
	@ApiModelProperty(value = "物流企业名称")
    private String logisticsName;
  
	@ApiModelProperty(value = "提单号")
    private String blNo;
 
	@ApiModelProperty(value = "运单号")
    private String wayBillNo;
 
	@ApiModelProperty(value = "进口模式 10：BBC;20：BC 30：保留; 40：CC")
    private String importMode;
  
	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;
  
	@ApiModelProperty(value = "领料出库外部单号")
    private String outExternalCode;
 
	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
 
	@ApiModelProperty(value = "订单来源  1手工导入 2.接口回传")
    private String orderSource;
  
	@ApiModelProperty(value = "事业部名称")
    private String buName;
   
	@ApiModelProperty(value = "事业部id")
    private Long buId;
	
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
	
	@ApiModelProperty(value = "发货开始时间",hidden=true)
	private String deliverStartDate;
	
	@ApiModelProperty(value = "发货结束时间",hidden=true)
	private String deliverEndDate;
	
	@ApiModelProperty(value = "领料出库单表体")
	List<MaterialOutDepotItemDTO> itemList;
	@ApiModelProperty(value = "主键集合ids")
	private String ids;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "审核人ID")
    private Long auditor;

    @ApiModelProperty(value = "审核时间")
    private Timestamp auditDate;

    @ApiModelProperty(value = "审核人名称")
    private String auditName;
    /*id get 方法 */
    public Long getId(){
    	return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    	this.id=id;
    }
    /*materialOrderId get 方法 */
    public Long getMaterialOrderId(){
    	return materialOrderId;
    }
    /*materialOrderId set 方法 */
    public void setMaterialOrderId(Long  materialOrderId){
    	this.materialOrderId=materialOrderId;
    }
    /*merchantId get 方法 */
    public Long getMerchantId(){
    	return merchantId;
    }
    /*merchantId set 方法 */
    public void setMerchantId(Long  merchantId){
    	this.merchantId=merchantId;
    }
    /*poNo get 方法 */
    public String getPoNo(){
    	return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    	this.poNo=poNo;
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
    /*customerId get 方法 */
    public Long getCustomerId(){
    	return customerId;
    }
    /*customerId set 方法 */
    public void setCustomerId(Long  customerId){
    	this.customerId=customerId;
    }
    /*customerName get 方法 */
    public String getCustomerName(){
    	return customerName;
    }
    /*customerName set 方法 */
    public void setCustomerName(String  customerName){
    	this.customerName=customerName;
    }
    /*deliverDate get 方法 */
    public Timestamp getDeliverDate(){
    	return deliverDate;
    }
    /*deliverDate set 方法 */
    public void setDeliverDate(Timestamp  deliverDate){
    	this.deliverDate=deliverDate;
    }
    /*status get 方法 */
    public String getStatus(){
    	return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    	this.status=status;
    	this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.materialOutDepot_statusList, status);
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
    /*code get 方法 */
    public String getCode(){
    	return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    	this.code=code;
    }
    /*merchantName get 方法 */
    public String getMerchantName(){
    	return merchantName;
    }
    /*merchantName set 方法 */
    public void setMerchantName(String  merchantName){
    	this.merchantName=merchantName;
    }
    /*materialOrderCode get 方法 */
    public String getMaterialOrderCode(){
    	return materialOrderCode;
    }
    /*materialOrderCode set 方法 */
    public void setMaterialOrderCode(String  materialOrderCode){
    	this.materialOrderCode=materialOrderCode;
    }
    /*logisticsName get 方法 */
    public String getLogisticsName(){
    	return logisticsName;
    }
    /*logisticsName set 方法 */
    public void setLogisticsName(String  logisticsName){
    	this.logisticsName=logisticsName;
    }
    /*blNo get 方法 */
    public String getBlNo(){
    	return blNo;
    }
    /*blNo set 方法 */
    public void setBlNo(String  blNo){
    	this.blNo=blNo;
    }
    /*wayBillNo get 方法 */
    public String getWayBillNo(){
    	return wayBillNo;
    }
    /*wayBillNo set 方法 */
    public void setWayBillNo(String  wayBillNo){
    	this.wayBillNo=wayBillNo;
    }
    /*importMode get 方法 */
    public String getImportMode(){
    	return importMode;
    }
    /*importMode set 方法 */
    public void setImportMode(String  importMode){
    	this.importMode=importMode;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    	return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    	this.modifyDate=modifyDate;
    }
    /*outExternalCode get 方法 */
    public String getOutExternalCode(){
    	return outExternalCode;
    }
    /*outExternalCode set 方法 */
    public void setOutExternalCode(String  outExternalCode){
    	this.outExternalCode=outExternalCode;
    }
    /*currency get 方法 */
    public String getCurrency(){
    	return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    	this.currency=currency;
    }
    /*orderSource get 方法 */
    public String getOrderSource(){
    	return orderSource;
    }
    /*orderSource set 方法 */
    public void setOrderSource(String  orderSource){
    	this.orderSource=orderSource;
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
	public String getStatusLabel() {
		return statusLabel;
	}
	public List<Long> getBuList() {
		return buList;
	}
	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}
	public String getDeliverStartDate() {
		return deliverStartDate;
	}
	public void setDeliverStartDate(String deliverStartDate) {
		this.deliverStartDate = deliverStartDate;
	}
	public String getDeliverEndDate() {
		return deliverEndDate;
	}
	public void setDeliverEndDate(String deliverEndDate) {
		this.deliverEndDate = deliverEndDate;
	}
	public List<MaterialOutDepotItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<MaterialOutDepotItemDTO> itemList) {
		this.itemList = itemList;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Long getAuditor() {
        return auditor;
    }

    public void setAuditor(Long auditor) {
        this.auditor = auditor;
    }

    public Timestamp getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Timestamp auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }
}
