package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ShelfForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value="销售订单编号")
    private String saleOrderCode;
	@ApiModelProperty(value="状态")
    private String state;
	@ApiModelProperty(value="po单号")
    private String poNo;
	@ApiModelProperty(value="客户id")
    private Long customerId;
	@ApiModelProperty(value="客户名称")
    private String customerName;
	@ApiModelProperty(value="事业部id")
    private Long buId;
	@ApiModelProperty(value="上架开始时间")
    private String shelfStartDate;
	@ApiModelProperty(value="上架结束时间")
    private String shelfEndDate;
	@ApiModelProperty(value="出仓仓库id")
    private Long outDepotId;
	@ApiModelProperty(value="上架单编号")
    private String code;
	@ApiModelProperty(value="上架单id,多个用逗号隔开")
	private String ids;

	@ApiModelProperty(value="上架日期")
	private String shelfDate;
	@ApiModelProperty(value="出库单编码")
	private String saleOutDepotCode;
	@ApiModelProperty(value="出库单id")
	private Long saleOutDepotId;
	@ApiModelProperty(value = "上架单明细信息")
	private List<SaleShelfDTO> itemList;
	@ApiModelProperty(value = "是否红冲单：0-否，1-是")
	private String isWriteOff;


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
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

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getShelfStartDate() {
		return shelfStartDate;
	}

	public void setShelfStartDate(String shelfStartDate) {
		this.shelfStartDate = shelfStartDate;
	}

	public String getShelfEndDate() {
		return shelfEndDate;
	}

	public void setShelfEndDate(String shelfEndDate) {
		this.shelfEndDate = shelfEndDate;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getShelfDate() {
		return shelfDate;
	}

	public void setShelfDate(String shelfDate) {
		this.shelfDate = shelfDate;
	}

	public String getSaleOutDepotCode() {
		return saleOutDepotCode;
	}

	public void setSaleOutDepotCode(String saleOutDepotCode) {
		this.saleOutDepotCode = saleOutDepotCode;
	}

	public Long getSaleOutDepotId() {
		return saleOutDepotId;
	}

	public void setSaleOutDepotId(Long saleOutDepotId) {
		this.saleOutDepotId = saleOutDepotId;
	}

	public List<SaleShelfDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleShelfDTO> itemList) {
		this.itemList = itemList;
	}

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
	}
}
