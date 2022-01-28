package com.topideal.entity.dto.bill;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformCostOrderDTO extends PageModel implements Serializable{

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "公司ID")
    private Long merchantId;

    @ApiModelProperty(value = "公司名称")
    private String merchantName;

    @ApiModelProperty(value = "事业部名称")
    private String buName;

    @ApiModelProperty(value = "事业部id")
    private Long buId;

    @ApiModelProperty(value = "客户id")
    private Long customerId;

    @ApiModelProperty(value = "客户名称")
    private String customerName;

    @ApiModelProperty(value = "费用单号")
    private String code;

    @ApiModelProperty(value = "平台单号")
    private String billCode;

    @ApiModelProperty(value = "费用项目Id")
    private Long itemProjectId;

    @ApiModelProperty(value = "费用项目名称")
    private String itemProjectName;

    @ApiModelProperty(value = "数量")
    private Integer num;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "币种")
    private String currency;

    @ApiModelProperty(value = "费用类型")
    private String costType;

    @ApiModelProperty(value = "单据状态")
    private String status;

    @ApiModelProperty(value = "确认人id")
    private Long confirmer;

    @ApiModelProperty(value = "确认人名称")
    private String confirmName;

    @ApiModelProperty(value = "提交时间")
    private Timestamp confirmDate;

    @ApiModelProperty(value = "转账单人id")
    private Long transferSliper;

    @ApiModelProperty(value = "转账单人名称")
    private String transferSlipName;

    @ApiModelProperty(value = "转账单时间")
    private Timestamp transferSlipDate;

    @ApiModelProperty(value = "单据来源")
    private String source;

    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;

    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "币种中文")
    private String currencyLabel;

    @ApiModelProperty(value = "费用状态中文")
    private String costTypeLabel;

    @ApiModelProperty(value = "单据状态中文")
    private String statusLabel;

    @ApiModelProperty(value = "单据来源中文")
    private String sourceLabel;

    @ApiModelProperty(value = "创建时间字符串")
    private String createDateStr;

    public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
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
    /*code get 方法 */
    public String getCode(){
    return code;
    }
    /*code set 方法 */
    public void setCode(String  code){
    this.code=code;
    }
    /*billCode get 方法 */
    public String getBillCode(){
    return billCode;
    }
    /*billCode set 方法 */
    public void setBillCode(String  billCode){
    this.billCode=billCode;
    }
    /*itemProjectId get 方法 */
    public Long getItemProjectId(){
    return itemProjectId;
    }
    /*itemProjectId set 方法 */
    public void setItemProjectId(Long  itemProjectId){
    this.itemProjectId=itemProjectId;
    }
    /*itemProjectName get 方法 */
    public String getItemProjectName(){
    return itemProjectName;
    }
    /*itemProjectName set 方法 */
    public void setItemProjectName(String  itemProjectName){
    this.itemProjectName=itemProjectName;
    }
    /*num get 方法 */
    public Integer getNum(){
    return num;
    }
    /*num set 方法 */
    public void setNum(Integer  num){
    this.num=num;
    }
    /*amount get 方法 */
    public BigDecimal getAmount(){
    return amount;
    }
    /*amount set 方法 */
    public void setAmount(BigDecimal  amount){
    this.amount=amount;
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
    /*costType get 方法 */
    public String getCostType(){
    return costType;
    }
    /*costType set 方法 */
    public void setCostType(String  costType){
    this.costType=costType;
    this.costTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformCostOrder_costTypeList, costType) ;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformCostOrder_platformCostStatusList, status) ;
    }
    /*confirmer get 方法 */
    public Long getConfirmer(){
    return confirmer;
    }
    /*confirmer set 方法 */
    public void setConfirmer(Long  confirmer){
    this.confirmer=confirmer;
    }
    /*confirmName get 方法 */
    public String getConfirmName(){
    return confirmName;
    }
    /*confirmName set 方法 */
    public void setConfirmName(String  confirmName){
    this.confirmName=confirmName;
    }
    /*confirmDate get 方法 */
    public Timestamp getConfirmDate(){
    return confirmDate;
    }
    /*confirmDate set 方法 */
    public void setConfirmDate(Timestamp  confirmDate){
    this.confirmDate=confirmDate;
    }
    /*transferSliper get 方法 */
    public Long getTransferSliper(){
    return transferSliper;
    }
    /*transferSliper set 方法 */
    public void setTransferSliper(Long  transferSliper){
    this.transferSliper=transferSliper;
    }
    /*transferSlipName get 方法 */
    public String getTransferSlipName(){
    return transferSlipName;
    }
    /*transferSlipName set 方法 */
    public void setTransferSlipName(String  transferSlipName){
    this.transferSlipName=transferSlipName;
    }
    /*transferSlipDate get 方法 */
    public Timestamp getTransferSlipDate(){
    return transferSlipDate;
    }
    /*transferSlipDate set 方法 */
    public void setTransferSlipDate(Timestamp  transferSlipDate){
    this.transferSlipDate=transferSlipDate;
    }
    /*source get 方法 */
    public String getSource(){
    return source;
    }
    /*source set 方法 */
    public void setSource(String  source){
    this.source=source;
    this.sourceLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.platformCostOrder_sourceList, source) ;
    
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
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getCostTypeLabel() {
		return costTypeLabel;
	}
	public void setCostTypeLabel(String costTypeLabel) {
		this.costTypeLabel = costTypeLabel;
	}
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getSourceLabel() {
		return sourceLabel;
	}
	public void setSourceLabel(String sourceLabel) {
		this.sourceLabel = sourceLabel;
	}






}
