package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class FinancingOrderDTO1 extends PageModel implements Serializable{

	@ApiModelProperty(value = "id主键")
    private Long id;
   
	@ApiModelProperty(value = "销售单id")
    private Long orderId;
    
	@ApiModelProperty(value = "销售订单编号")
    private String orderCode;
    
	@ApiModelProperty(value = "外部申请单号")
    private String externalCode;
   
	@ApiModelProperty(value = "客户ID(供应商)")
    private Long customerId;
   
	@ApiModelProperty(value = "客户名称")
    private String customerName;
    
	@ApiModelProperty(value = "商家ID")
    private Long merchantid;
    
	@ApiModelProperty(value = "商家名称")
    private String merchantname;
   
	@ApiModelProperty(value = "po单号")
    private String poNo;
   
	@ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
   
	@ApiModelProperty(value = "事业部id")
    private Long buId;
   
	@ApiModelProperty(value = "事业部名称")
    private String buName;
   
	@ApiModelProperty(value = "订单金额")
    private BigDecimal saleAmount;
   
	@ApiModelProperty(value = "实收保证金")
    private BigDecimal actualMarginAmount;
   
	@ApiModelProperty(value = "起算时间")
    private Timestamp applyDate;
  
	@ApiModelProperty(value = "实际还款日期")
    private Timestamp deadlineDate;
   
	@ApiModelProperty(value = "保证金")
    private BigDecimal marginAmount;
  
	@ApiModelProperty(value = "还款本金")
    private BigDecimal principalAmount;
   
	@ApiModelProperty(value = "资金占用费")
    private BigDecimal occupationAmount;
    
	@ApiModelProperty(value = "代理费")
    private BigDecimal agencyAmount;
   
	@ApiModelProperty(value = "滞纳金")
    private BigDecimal delayAmount;
    
	@ApiModelProperty(value = "应还款金额")
    private BigDecimal payableAmount;
  
	@ApiModelProperty(value = "赎回人")
    private Long ransomer;
   
	@ApiModelProperty(value = "赎回人名称")
    private String ransomerName;
    
	@ApiModelProperty(value = "赎回时间")
    private Timestamp ransomDate;
 
	@ApiModelProperty(value = "融资申请编号")
    private String code;
   
	@ApiModelProperty(value = "创建人")
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
	
	@ApiModelProperty(value = "融资商品表体集合")
	private List<FinancingOrderItemDTO1> itemList;
	
	@ApiModelProperty(value = "总金额")
    private BigDecimal totalAmount;

    /*id get 方法 */
    public Long getId(){
    return id;
    }
    /*id set 方法 */
    public void setId(Long  id){
    this.id=id;
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
    /*externalCode get 方法 */
    public String getExternalCode(){
    return externalCode;
    }
    /*externalCode set 方法 */
    public void setExternalCode(String  externalCode){
    this.externalCode=externalCode;
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
    /*merchantid get 方法 */
    public Long getMerchantid(){
    return merchantid;
    }
    /*merchantid set 方法 */
    public void setMerchantid(Long  merchantid){
    this.merchantid=merchantid;
    }
    /*merchantname get 方法 */
    public String getMerchantname(){
    return merchantname;
    }
    /*merchantname set 方法 */
    public void setMerchantname(String  merchantname){
    this.merchantname=merchantname;
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
    /*saleAmount get 方法 */
    public BigDecimal getSaleAmount(){
    return saleAmount;
    }
    /*saleAmount set 方法 */
    public void setSaleAmount(BigDecimal  saleAmount){
    this.saleAmount=saleAmount;
    }
    /*actualMarginAmount get 方法 */
    public BigDecimal getActualMarginAmount(){
    return actualMarginAmount;
    }
    /*actualMarginAmount set 方法 */
    public void setActualMarginAmount(BigDecimal  actualMarginAmount){
    this.actualMarginAmount=actualMarginAmount;
    }
    /*applyDate get 方法 */
    public Timestamp getApplyDate(){
    return applyDate;
    }
    /*applyDate set 方法 */
    public void setApplyDate(Timestamp  applyDate){
    this.applyDate=applyDate;
    }
    /*deadlineDate get 方法 */
    public Timestamp getDeadlineDate(){
    return deadlineDate;
    }
    /*deadlineDate set 方法 */
    public void setDeadlineDate(Timestamp  deadlineDate){
    this.deadlineDate=deadlineDate;
    }
    /*marginAmount get 方法 */
    public BigDecimal getMarginAmount(){
    return marginAmount;
    }
    /*marginAmount set 方法 */
    public void setMarginAmount(BigDecimal  marginAmount){
    this.marginAmount=marginAmount;
    }
    /*principalAmount get 方法 */
    public BigDecimal getPrincipalAmount(){
    return principalAmount;
    }
    /*principalAmount set 方法 */
    public void setPrincipalAmount(BigDecimal  principalAmount){
    this.principalAmount=principalAmount;
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
    /*payableAmount get 方法 */
    public BigDecimal getPayableAmount(){
    return payableAmount;
    }
    /*payableAmount set 方法 */
    public void setPayableAmount(BigDecimal  payableAmount){
    this.payableAmount=payableAmount;
    }
    /*ransomer get 方法 */
    public Long getRansomer(){
    return ransomer;
    }
    /*ransomer set 方法 */
    public void setRansomer(Long  ransomer){
    this.ransomer=ransomer;
    }
    /*ransomerName get 方法 */
    public String getRansomerName(){
    return ransomerName;
    }
    /*ransomerName set 方法 */
    public void setRansomerName(String  ransomerName){
    this.ransomerName=ransomerName;
    }
    /*ransomDate get 方法 */
    public Timestamp getRansomDate(){
    return ransomDate;
    }
    /*ransomDate set 方法 */
    public void setRansomDate(Timestamp  ransomDate){
    this.ransomDate=ransomDate;
    }
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
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
	public List<FinancingOrderItemDTO1> getItemList() {
		return itemList;
	}
	public void setItemList(List<FinancingOrderItemDTO1> itemList) {
		this.itemList = itemList;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

}
