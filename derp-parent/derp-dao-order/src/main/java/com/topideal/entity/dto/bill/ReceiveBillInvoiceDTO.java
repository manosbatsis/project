package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class ReceiveBillInvoiceDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司id")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "发票路径")
    private String invoicePath;

    @ApiModelProperty(value = "结算币种")
    private String currency;

    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty(value = "开单日期")
    private Timestamp invoiceDate;

    @ApiModelProperty(value = "多单开票关联单号")
    private String invoiceRelCodes;

    @ApiModelProperty(value = "多单开票关联id")
    private String invoiceRelIds;

    @ApiModelProperty(value = "开票人ID")
    private Long createrId;

    @ApiModelProperty(value = "开票人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "发票状态：0-待签章 1-已作废 2-已签章")
    private String status;
    @ApiModelProperty(value = "发票状态中文")
    private String statusLabel;

    @ApiModelProperty(value = "开票开始时间")
    private String invoiceStartDate;

    @ApiModelProperty(value = "开票结束时间")
    private String invoiceEndDate;

    @ApiModelProperty(value = "同步操作人id")
    private Long synchronizerId ;
    
    @ApiModelProperty(value = "同步操作人")
    private String synchronizer ;

    @ApiModelProperty(value = "关联平台结算单单号")
     private String relStatementCodes;

    @ApiModelProperty(value = "发票类型 0-ToB 1-ToC")
    private String invoiceType;
    @ApiModelProperty(value = "发票类型中文")
    private String invoiceTypeLabel;

    @ApiModelProperty(value = "id集合,多个用英文逗号隔开")
    private String ids;

    @ApiModelProperty(value = "当前登录用户绑定的事业部集合")
    private List<Long> buList;

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
    /*invoiceNo get 方法 */
    public String getInvoiceNo(){
    return invoiceNo;
    }
    /*invoiceNo set 方法 */
    public void setInvoiceNo(String  invoiceNo){
    this.invoiceNo=invoiceNo;
    }
    /*invoicePath get 方法 */
    public String getInvoicePath(){
    return invoicePath;
    }
    /*invoicePath set 方法 */
    public void setInvoicePath(String  invoicePath){
    this.invoicePath=invoicePath;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*invoiceAmount get 方法 */
    public BigDecimal getInvoiceAmount(){
    return invoiceAmount;
    }
    /*invoiceAmount set 方法 */
    public void setInvoiceAmount(BigDecimal  invoiceAmount){
    this.invoiceAmount=invoiceAmount;
    }
    /*invoiceDate get 方法 */
    public Timestamp getInvoiceDate(){
    return invoiceDate;
    }
    /*invoiceDate set 方法 */
    public void setInvoiceDate(Timestamp  invoiceDate){
    this.invoiceDate=invoiceDate;
    }
    /*invoiceRelCodes get 方法 */
    public String getInvoiceRelCodes(){
    return invoiceRelCodes;
    }
    /*invoiceRelCodes set 方法 */
    public void setInvoiceRelCodes(String  invoiceRelCodes){
    this.invoiceRelCodes=invoiceRelCodes;
    }
    /*invoiceRelIds get 方法 */
    public String getInvoiceRelIds(){
    return invoiceRelIds;
    }
    /*invoiceRelIds set 方法 */
    public void setInvoiceRelIds(String  invoiceRelIds){
    this.invoiceRelIds=invoiceRelIds;
    }
    /*createrId get 方法 */
    public Long getCreaterId(){
    return createrId;
    }
    /*createrId set 方法 */
    public void setCreaterId(Long  createrId){
    this.createrId=createrId;
    }
    /*creater get 方法 */
    public String getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(String  creater){
    this.creater=creater;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillInvoice_statusList, status);
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getInvoiceStartDate() {
        return invoiceStartDate;
    }

    public void setInvoiceStartDate(String invoiceStartDate) {
        this.invoiceStartDate = invoiceStartDate;
    }

    public String getInvoiceEndDate() {
        return invoiceEndDate;
    }

    public void setInvoiceEndDate(String invoiceEndDate) {
        this.invoiceEndDate = invoiceEndDate;
    }
	public Long getSynchronizerId() {
		return synchronizerId;
	}
	public void setSynchronizerId(Long synchronizerId) {
		this.synchronizerId = synchronizerId;
	}
	public String getSynchronizer() {
		return synchronizer;
	}
	public void setSynchronizer(String synchronizer) {
		this.synchronizer = synchronizer;
	}
	public String getRelStatementCodes() {
		return relStatementCodes;
	}
	public void setRelStatementCodes(String relStatementCodes) {
		this.relStatementCodes = relStatementCodes;
	}

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
        this.invoiceTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillInvoice_invoiceTypeList, invoiceType);
    }

    public String getInvoiceTypeLabel() {
        return invoiceTypeLabel;
    }

    public void setInvoiceTypeLabel(String invoiceTypeLabel) {
        this.invoiceTypeLabel = invoiceTypeLabel;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
}
