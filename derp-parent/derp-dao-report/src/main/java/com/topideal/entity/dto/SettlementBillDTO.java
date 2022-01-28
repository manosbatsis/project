package com.topideal.entity.dto;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApiModel
public class SettlementBillDTO extends PageModel implements Serializable{

    /**
    * id
    */
    @ApiModelProperty(value = "id")
    private Long id;
    /**
    * 结算单号
    */
    @ApiModelProperty(value = "结算单号")
    private String code;
    /**
    * 公司ID
    */
    @ApiModelProperty(value = "公司ID")
    private Long merchantId;
    /**
    * 公司名称
    */
    @ApiModelProperty(value = "公司名称")
    private String merchantName;
    /**
    * 仓库ID
    */
    @ApiModelProperty(value = "仓库ID")
    private Long depotId;
    /**
    * 仓库名称
    */
    @ApiModelProperty(value = "仓库名称")
    private String depotName;
    /**
    * 客户id
    */
    @ApiModelProperty(value = "客户id")
    private Long customerId;
    /**
    * 客户名称
    */
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    /**
    * 平台编码
    */
    @ApiModelProperty(value = "平台编码")
    private String platformCode;
    @ApiModelProperty(value = "平台名称")
    private String platformLabel;
    /**
    * 结算金额
    */
    @ApiModelProperty(value = "结算金额")
    private BigDecimal settlementAccount;

    @ApiModelProperty(value = "币种")
    private String currency;
    @ApiModelProperty(value = "币种中文")
    private String currencyLabel ;

    @ApiModelProperty(value = "账单月份")
    private String month;

    @ApiModelProperty(value = "计费周期")
    private String billDate;

    @ApiModelProperty(value = "状态：1-生成中 2-已生成 3-生成失败 4-已确认")
    private String status;
    @ApiModelProperty(value = "状态中文")
    private String statusLabel;
    /**
    * 创建人ID
    */
    @ApiModelProperty(value = "创建人ID")
    private Long creater;
    /**
    * 创建人名称
    */
    @ApiModelProperty(value = "创建人名称")
    private String createrName;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间")
    private Timestamp createDate;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间")
    private Timestamp modifyDate;

    @ApiModelProperty(value = "确认状态")
    private String confirmStatus ;
    @ApiModelProperty(value = "确认状态中文")
    private String confirmStatusLabel ;

    @ApiModelProperty(value = "计费周期开始时间")
    private String checkStartDate ;
    @ApiModelProperty(value = "计费周期结束时间")
    private String checkEndDate ;

    //失败信息
    @ApiModelProperty(value = "失败信息")
    private String errorMsg;

    @ApiModelProperty(value = "事业部分账明细")
    private List<Map<String, Object>> listMap;

    @ApiModelProperty(value = "详情汇总")
    private Map<String, Object> totalMap;

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
    /*depotId get 方法 */
    public Long getDepotId(){
    return depotId;
    }
    /*depotId set 方法 */
    public void setDepotId(Long  depotId){
    this.depotId=depotId;
    }
    /*depotName get 方法 */
    public String getDepotName(){
    return depotName;
    }
    /*depotName set 方法 */
    public void setDepotName(String  depotName){
    this.depotName=depotName;
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
    /*platformCode get 方法 */
    public String getPlatformCode(){
    return platformCode;
    }
    /*platformCode set 方法 */
    public void setPlatformCode(String  platformCode){
    this.platformCode=platformCode;
    this.platformLabel = DERP_ORDER.getLabelByKey(DERP.storePlatformList, platformCode) ;
    }
    /*settlementAccount get 方法 */
    public BigDecimal getSettlementAccount(){
    return settlementAccount;
    }
    /*settlementAccount set 方法 */
    public void setSettlementAccount(BigDecimal  settlementAccount){
    this.settlementAccount=settlementAccount;
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
    /*month get 方法 */
    public String getMonth(){
    return month;
    }
    /*month set 方法 */
    public void setMonth(String  month){
    this.month=month;
    }
    /*billDate get 方法 */
    public String getBillDate(){
    return billDate;
    }
    /*billDate set 方法 */
    public void setBillDate(String  billDate){
    this.billDate=billDate;
    }
    /*status get 方法 */
    public String getStatus(){
    return status;
    }
    /*status set 方法 */
    public void setStatus(String  status){
    this.status=status;
    this.statusLabel = DERP_REPORT.getLabelByKey(DERP_REPORT.settlementBill_stateList, status) ;
    }
    /*creater get 方法 */
    public Long getCreater(){
    return creater;
    }
    /*creater set 方法 */
    public void setCreater(Long  creater){
    this.creater=creater;
    }
    /*createrName get 方法 */
    public String getCreaterName(){
    return createrName;
    }
    /*createrName set 方法 */
    public void setCreaterName(String  createrName){
    this.createrName=createrName;
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
	public String getStatusLabel() {
		return statusLabel;
	}
	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}
	public String getConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	public String getConfirmStatusLabel() {
		return confirmStatusLabel;
	}
	public void setConfirmStatusLabel(String confirmStatusLabel) {
		this.confirmStatusLabel = confirmStatusLabel;
	}
	public String getPlatformLabel() {
		return platformLabel;
	}
	public void setPlatformLabel(String platformLabel) {
		this.platformLabel = platformLabel;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	public String getCheckStartDate() {
		return checkStartDate;
	}
	public void setCheckStartDate(String checkStartDate) {
		this.checkStartDate = checkStartDate;
	}
	public String getCheckEndDate() {
		return checkEndDate;
	}
	public void setCheckEndDate(String checkEndDate) {
		this.checkEndDate = checkEndDate;
	}

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<Map<String, Object>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, Object>> listMap) {
        this.listMap = listMap;
    }

    public Map<String, Object> getTotalMap() {
        return totalMap;
    }

    public void setTotalMap(Map<String, Object> totalMap) {
        this.totalMap = totalMap;
    }
}
