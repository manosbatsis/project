package com.topideal.order.webapi.sale.form;
import java.util.List;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.PlatformPurchaseOrderItemDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PlatformPurchaseOrderForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token; 	
  
	@ApiModelProperty(value = "客户id")
    private Long customerId;

	@ApiModelProperty(value = "PO号")
    private String poNo;
	
	@ApiModelProperty(value = "下单开始时间")
    private String orderTimeStartDate ;
	
	@ApiModelProperty(value = "下单结束时间")
    private String orderTimeEndDate ;
	
	@ApiModelProperty(value = "平台状态： 1.待发货确认、2.等待签收、3.等待入库、4.部分收货、5已完成")
    private String platformStatus;
	
	@ApiModelProperty(value = "单据状态：1:待提交 2.已转销售,3:未转销售")
    private String status;

	@ApiModelProperty(value = "PR号")
	private String prNo;
	 
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
	
	@ApiModelProperty(value = "出库仓id")
    private Long outDepotId;
	
	@ApiModelProperty(value = "事业部id")
    private Long buId;
	
	@ApiModelProperty(value = "表体数据")
    private List<PlatformPurchaseOrderItemDTO> itemList ;
	
	@ApiModelProperty(value = "入库开始时间")
    private String deliverStartDate;
    
    @ApiModelProperty(value = "入库结束时间")
    private String deliverEndDate;
    
    @ApiModelProperty(value = "币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
		 
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getOrderTimeStartDate() {
		return orderTimeStartDate;
	}

	public void setOrderTimeStartDate(String orderTimeStartDate) {
		this.orderTimeStartDate = orderTimeStartDate;
	}

	public String getOrderTimeEndDate() {
		return orderTimeEndDate;
	}

	public void setOrderTimeEndDate(String orderTimeEndDate) {
		this.orderTimeEndDate = orderTimeEndDate;
	}

	public String getPlatformStatus() {
		return platformStatus;
	}

	public void setPlatformStatus(String platformStatus) {
		this.platformStatus = platformStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPrNo() {
		return prNo;
	}

	public void setPrNo(String prNo) {
		this.prNo = prNo;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<PlatformPurchaseOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<PlatformPurchaseOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
