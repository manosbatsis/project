package com.topideal.entity.vo.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class SaleCreditBillOrderModel extends PageModel implements Serializable{

	@ApiModelProperty(value = "id")
    private Long id;

	@ApiModelProperty(value = "赊销单id")
    private Long creditOrderId;
 
	@ApiModelProperty(value = "赊销单编号")
    private String creditOrderCode;

	@ApiModelProperty(value = "赊销收款单编号")
    private String code;

	@ApiModelProperty(value = "客户ID(供应商)")
    private Long customerId;

	@ApiModelProperty(value = "客户名称")
    private String customerName;
 
	@ApiModelProperty(value = "商家ID")
    private Long merchantId;

	@ApiModelProperty(value = "商家名称")
    private String merchantName;
 
	@ApiModelProperty(value = "po单号")
    private String poNo;

	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;

	@ApiModelProperty(value = "事业部id")
    private Long buId;

	@ApiModelProperty(value = "事业部名称")
    private String buName;

	@ApiModelProperty(value = "收款本金")
    private BigDecimal principalAmount;

	@ApiModelProperty(value = "保证金")
    private BigDecimal marginAmount;

	@ApiModelProperty(value = "资金占用费")
    private BigDecimal occupationAmount;

	@ApiModelProperty(value = "代理费")
    private BigDecimal agencyAmount;

	@ApiModelProperty(value = "滞纳金")
    private BigDecimal delayAmount;

	@ApiModelProperty(value = "应收款金额")
    private BigDecimal receivableAmount;

	@ApiModelProperty(value = "收款日期")
    private Timestamp receiveDate;

	@ApiModelProperty(value = "到账日期")
    private Timestamp accountDate;

	@ApiModelProperty(value = "备注")
    private String remark;

	@ApiModelProperty(value = "001:待收款，002-已收款，006-已删除")
    private String status;

	@ApiModelProperty(value = " 创建人")
    private Long creater;

	@ApiModelProperty(value = "创建人用户名")
    private String createName;

	@ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

	@ApiModelProperty(value = "修改人")
    private Long modifier;

	@ApiModelProperty(value = "修改人用户名")
    private String modifierName;

	@ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    /**
     * 滞纳金减免金额
     */
    @ApiModelProperty(value = "滞纳金减免金额")
    private BigDecimal discountDelayAmount;
    /**
     * 减免原因
     */
    @ApiModelProperty(value = "减免原因")
    private String discountReason;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
    }
    /*creditOrderId get 方法 */
    public Long getCreditOrderId(){
    return creditOrderId;
    }
    /*creditOrderId set 方法 */
    public void setCreditOrderId(Long  creditOrderId){
    this.creditOrderId=creditOrderId;
    }
    /*creditOrderCode get 方法 */
    public String getCreditOrderCode(){
    return creditOrderCode;
    }
    /*creditOrderCode set 方法 */
    public void setCreditOrderCode(String  creditOrderCode){
    this.creditOrderCode=creditOrderCode;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
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
    /*poNo get 方法 */
    public String getPoNo(){
    return poNo;
    }
    /*poNo set 方法 */
    public void setPoNo(String  poNo){
    this.poNo=poNo;
    }
    /*currency get 方法 */
    public String getCurrency(){
    return currency;
    }
    /*currency set 方法 */
    public void setCurrency(String  currency){
    this.currency=currency;
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
    /*principalAmount get 方法 */
    public BigDecimal getPrincipalAmount(){
    return principalAmount;
    }
    /*principalAmount set 方法 */
    public void setPrincipalAmount(BigDecimal  principalAmount){
    this.principalAmount=principalAmount;
    }
    /*marginAmount get 方法 */
    public BigDecimal getMarginAmount(){
    return marginAmount;
    }
    /*marginAmount set 方法 */
    public void setMarginAmount(BigDecimal  marginAmount){
    this.marginAmount=marginAmount;
    }
    /*occupationAmount get 方法 */
    public BigDecimal getOccupationAmount(){
    return occupationAmount;
    }
    /*occupationAmount set 方法 */
    public void setOccupationAmount(BigDecimal  occupationAmount){
    this.occupationAmount=occupationAmount;
    }
    /*agencyAmount get 方法 */
    public BigDecimal getAgencyAmount(){
    return agencyAmount;
    }
    /*agencyAmount set 方法 */
    public void setAgencyAmount(BigDecimal  agencyAmount){
    this.agencyAmount=agencyAmount;
    }
    /*delayAmount get 方法 */
    public BigDecimal getDelayAmount(){
    return delayAmount;
    }
    /*delayAmount set 方法 */
    public void setDelayAmount(BigDecimal  delayAmount){
    this.delayAmount=delayAmount;
    }
    /*receivableAmount get 方法 */
    public BigDecimal getReceivableAmount(){
    return receivableAmount;
    }
    /*receivableAmount set 方法 */
    public void setReceivableAmount(BigDecimal  receivableAmount){
    this.receivableAmount=receivableAmount;
    }
    /*receiveDate get 方法 */
    public Timestamp getReceiveDate(){
    return receiveDate;
    }
    /*receiveDate set 方法 */
    public void setReceiveDate(Timestamp  receiveDate){
    this.receiveDate=receiveDate;
    }
    /*accountDate get 方法 */
    public Timestamp getAccountDate(){
    return accountDate;
    }
    /*accountDate set 方法 */
    public void setAccountDate(Timestamp  accountDate){
    this.accountDate=accountDate;
    }
    /*remark get 方法 */
    public String getRemark(){
    return remark;
    }
    /*remark set 方法 */
    public void setRemark(String  remark){
    this.remark=remark;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
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
    /*modifier get 方法 */
    public Long getModifier(){
    return modifier;
    }
    /*modifier set 方法 */
    public void setModifier(Long  modifier){
    this.modifier=modifier;
    }
    /*modifierName get 方法 */
    public String getModifierName(){
    return modifierName;
    }
    /*modifierName set 方法 */
    public void setModifierName(String  modifierName){
    this.modifierName=modifierName;
    }
    /*modifyDate get 方法 */
    public Timestamp getModifyDate(){
    return modifyDate;
    }
    /*modifyDate set 方法 */
    public void setModifyDate(Timestamp  modifyDate){
    this.modifyDate=modifyDate;
    }

    public BigDecimal getDiscountDelayAmount() {
        return discountDelayAmount;
    }

    public void setDiscountDelayAmount(BigDecimal discountDelayAmount) {
        this.discountDelayAmount = discountDelayAmount;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }
}
