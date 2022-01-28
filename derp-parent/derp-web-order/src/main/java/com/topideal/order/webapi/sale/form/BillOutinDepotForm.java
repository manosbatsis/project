package com.topideal.order.webapi.sale.form;
import com.topideal.common.system.webapi.PageForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BillOutinDepotForm  extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "账单出库单号")
    private String code;
	@ApiModelProperty(value = "仓库ID")
    private String depotId;
    @ApiModelProperty(value = "平台账单号")
    private String billCode;   
	@ApiModelProperty(value = "处理类型 XSC-销售出库 GJC-国检出库 PKC-盘亏出库 BFC-报废 PYR-盘盈入库 KTR-客退入库")
    private String processingType;
	@ApiModelProperty(value = "出库开始时间 yyyy-MM-dd HH:mm:ss")
    private String deliverStartDate;
	@ApiModelProperty(value = "出库结束时间 yyyy-MM-dd HH:mm:ss")
    private String deliverEndDate;
	@ApiModelProperty(value = "客户名称")
    private String customerName;
	@ApiModelProperty(value = "库存调整类型 0 调减 1调增")
    private String operateType;
    @ApiModelProperty(value = "单据状态 00-待审核 01-处理中 02-已出库 03-已入库")
    private String state;
	@ApiModelProperty(value = "账单来源")
    private String billSource;
	@ApiModelProperty(value = "事业部id")
    private String buId;
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
	@ApiModelProperty(value = "客户Id")
    private Long customerId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public String getProcessingType() {
		return processingType;
	}
	public void setProcessingType(String processingType) {
		this.processingType = processingType;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBillSource() {
		return billSource;
	}
	public void setBillSource(String billSource) {
		this.billSource = billSource;
	}
	public String getBuId() {
		return buId;
	}
	public void setBuId(String buId) {
		this.buId = buId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
}
