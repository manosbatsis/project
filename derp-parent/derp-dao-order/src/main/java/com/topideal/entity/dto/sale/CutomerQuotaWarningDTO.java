package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class CutomerQuotaWarningDTO extends PageModel implements Serializable{

	@ApiModelProperty(value = "主键ID")
    private Long id;

	@ApiModelProperty(value = "事业部ID")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "客户ID")
    private Long customerId;
 
	@ApiModelProperty(value = "客户名称")
    private String customerName;

	@ApiModelProperty(value = "客户额度")
    private BigDecimal customerQuota;

	@ApiModelProperty(value = "额度币种")
    private String currency;

	@ApiModelProperty(value = "额度类型")
    private String quotaType;

	@ApiModelProperty(value = "销售在途金额")
    private BigDecimal saleNoshelfAmount;

	@ApiModelProperty(value = "待出账单金额")
    private BigDecimal nobillAmount;

	@ApiModelProperty(value = "待确认账单金额")
    private BigDecimal noconfirmAmount;

	@ApiModelProperty(value = "待开票金额")
    private BigDecimal noinvoiceAmount;

	@ApiModelProperty(value = "待回款金额")
    private BigDecimal noreturnAmount;

	@ApiModelProperty(value = "可用额度金额")
    private BigDecimal availableAmount;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "事业部集合")
    private List<Long> buList;

	@ApiModelProperty(value = "销售在途列明细")
	private List<CutomerQuotaWarningItemDTO> noShelfList;
	
	@ApiModelProperty(value = "待确认账单明细")
	private List<CutomerQuotaWarningItemDTO> noConfirmList;
	
	@ApiModelProperty(value = "待开票明细")
	private List<CutomerQuotaWarningItemDTO> noInvoiceList;
	
	@ApiModelProperty(value = "待回款明细")
	private List<CutomerQuotaWarningItemDTO> noReturnList;
	
	@ApiModelProperty(value = "id集合，多个用逗号隔开")
    private String ids;
	
	@ApiModelProperty(value = "期初已使用额度")
    private BigDecimal periodQuota;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*customerQuota get 方法 */
    public BigDecimal getCustomerQuota(){
    return customerQuota;
    }
    /*customerQuota set 方法 */
    public void setCustomerQuota(BigDecimal  customerQuota){
    this.customerQuota=customerQuota;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
    }
    /*quotaType get 方法 */
    public String getQuotaType(){
    return quotaType;
    }
    /*quotaType set 方法 */
    public void setQuotaType(String  quotaType){
    this.quotaType=quotaType;
    }
    /*saleNoshelfAmount get 方法 */
    public BigDecimal getSaleNoshelfAmount(){
    return saleNoshelfAmount;
    }
    /*saleNoshelfAmount set 方法 */
    public void setSaleNoshelfAmount(BigDecimal  saleNoshelfAmount){
    this.saleNoshelfAmount=saleNoshelfAmount;
    }
    /*nobillAmount get 方法 */
    public BigDecimal getNobillAmount(){
    return nobillAmount;
    }
    /*nobillAmount set 方法 */
    public void setNobillAmount(BigDecimal  nobillAmount){
    this.nobillAmount=nobillAmount;
    }
    /*noconfirmAmount get 方法 */
    public BigDecimal getNoconfirmAmount(){
    return noconfirmAmount;
    }
    /*noconfirmAmount set 方法 */
    public void setNoconfirmAmount(BigDecimal  noconfirmAmount){
    this.noconfirmAmount=noconfirmAmount;
    }
    /*noinvoiceAmount get 方法 */
    public BigDecimal getNoinvoiceAmount(){
    return noinvoiceAmount;
    }
    /*noinvoiceAmount set 方法 */
    public void setNoinvoiceAmount(BigDecimal  noinvoiceAmount){
    this.noinvoiceAmount=noinvoiceAmount;
    }
    /*noreturnAmount get 方法 */
    public BigDecimal getNoreturnAmount(){
    return noreturnAmount;
    }
    /*noreturnAmount set 方法 */
    public void setNoreturnAmount(BigDecimal  noreturnAmount){
    this.noreturnAmount=noreturnAmount;
    }
    /*availableAmount get 方法 */
    public BigDecimal getAvailableAmount(){
    return availableAmount;
    }
    /*availableAmount set 方法 */
    public void setAvailableAmount(BigDecimal  availableAmount){
    this.availableAmount=availableAmount;
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
	public List<CutomerQuotaWarningItemDTO> getNoShelfList() {
		return noShelfList;
	}
	public void setNoShelfList(List<CutomerQuotaWarningItemDTO> noShelfList) {
		this.noShelfList = noShelfList;
	}
	public List<CutomerQuotaWarningItemDTO> getNoConfirmList() {
		return noConfirmList;
	}
	public void setNoConfirmList(List<CutomerQuotaWarningItemDTO> noConfirmList) {
		this.noConfirmList = noConfirmList;
	}
	public List<CutomerQuotaWarningItemDTO> getNoInvoiceList() {
		return noInvoiceList;
	}
	public void setNoInvoiceList(List<CutomerQuotaWarningItemDTO> noInvoiceList) {
		this.noInvoiceList = noInvoiceList;
	}
	public List<CutomerQuotaWarningItemDTO> getNoReturnList() {
		return noReturnList;
	}
	public void setNoReturnList(List<CutomerQuotaWarningItemDTO> noReturnList) {
		this.noReturnList = noReturnList;
	}

    public List<Long> getBuList() {
        return buList;
    }

    public void setBuList(List<Long> buList) {
        this.buList = buList;
    }
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public BigDecimal getPeriodQuota() {
		return periodQuota;
	}
	public void setPeriodQuota(BigDecimal periodQuota) {
		this.periodQuota = periodQuota;
	}
	
	
}
