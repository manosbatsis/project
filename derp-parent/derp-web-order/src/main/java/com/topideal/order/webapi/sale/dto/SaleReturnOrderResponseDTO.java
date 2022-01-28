package com.topideal.order.webapi.sale.dto;

import com.topideal.common.system.bean.SelectBean;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class SaleReturnOrderResponseDTO {

	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "销售退货订单号")
	private String code;
	@ApiModelProperty(value = "商家ID")
	private Long merchantId;
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
	@ApiModelProperty(value = "销售退货类型 1-消费者退货 2-代销退货")
	private String returnType;
	@ApiModelProperty(value = "客户id")
	private Long customerId;
	@ApiModelProperty(value = "客户名称")
	private String customerName;
	@ApiModelProperty(value = "退出仓库id")
	private Long outDepotId;
	@ApiModelProperty(value = "退出仓库名称")
	private String outDepotName;
	@ApiModelProperty(value = "退入仓库id")
	private Long inDepotId;
	@ApiModelProperty(value = "退入仓库名称")
	private String inDepotName;
	@ApiModelProperty(value = "事业部id")
	private Long buId;
	@ApiModelProperty(value = "事业部名称")
	private String buName;
	@ApiModelProperty(value = "退货方式 1-退货退款，2-仅退货")
	private String returnMode;
	@ApiModelProperty(value = "关联销售单id")
	private Long saleOrderId;
	@ApiModelProperty(value = "关联销售单单号")
	private String saleOrderRelCode;
	@ApiModelProperty(value = "业务模式")
	private String businessModel;
	@ApiModelProperty(value = "PO号")
	private String poNo;
	@ApiModelProperty(value = "币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑")
	private String currency;
	@ApiModelProperty(value = "LBX单号")
	private String lbxNo;
	@ApiModelProperty(value = "海外仓理货单位 00-托盘 01-箱 02-件")
	private String tallyingUnit;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "销售退订单商品信息数量")
	private String itemCount;
	@ApiModelProperty(value = "退货类型、客户是否必填 yes,no")
	private String isForbid;
	@ApiModelProperty(value = "包装方式集合")
	private List<SelectBean> packTypeBean;
	@ApiModelProperty(value = "是否必填 0：否 1：是")
	String isRequired;
	@ApiModelProperty(value = "退入仓库类型")
	String inDepotType;
	@ApiModelProperty(value = "是否进出接口指令 1-是 0 - 否")
	String inIsInOutInstruction;
	@ApiModelProperty(value = "商品信息")
	private List<SaleReturnOrderItemDTO> itemList;

	@ApiModelProperty(value = "平台售后单号")
	private String platformReturnCode;

	@ApiModelProperty(value = "退入仓库id集合")
	private List<Long> inDepotIdList;

	@ApiModelProperty(value = "事业部库位类型ID")
	private Long stockLocationTypeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
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

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public String getOutDepotName() {
		return outDepotName;
	}

	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	public Long getInDepotId() {
		return inDepotId;
	}

	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	public String getInDepotName() {
		return inDepotName;
	}

	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public String getReturnMode() {
		return returnMode;
	}

	public void setReturnMode(String returnMode) {
		this.returnMode = returnMode;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

	public String getSaleOrderRelCode() {
		return saleOrderRelCode;
	}

	public void setSaleOrderRelCode(String saleOrderRelCode) {
		this.saleOrderRelCode = saleOrderRelCode;
	}

	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getIsForbid() {
		return isForbid;
	}

	public void setIsForbid(String isForbid) {
		this.isForbid = isForbid;
	}

	public List<SelectBean> getPackTypeBean() {
		return packTypeBean;
	}

	public void setPackTypeBean(List<SelectBean> packTypeBean) {
		this.packTypeBean = packTypeBean;
	}

	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}

	public String getInDepotType() {
		return inDepotType;
	}

	public void setInDepotType(String inDepotType) {
		this.inDepotType = inDepotType;
	}

	public String getInIsInOutInstruction() {
		return inIsInOutInstruction;
	}

	public void setInIsInOutInstruction(String inIsInOutInstruction) {
		this.inIsInOutInstruction = inIsInOutInstruction;
	}

	public List<SaleReturnOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleReturnOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public String getPlatformReturnCode() {
		return platformReturnCode;
	}

	public void setPlatformReturnCode(String platformReturnCode) {
		this.platformReturnCode = platformReturnCode;
	}

	public List<Long> getInDepotIdList() {
		return inDepotIdList;
	}

	public void setInDepotIdList(List<Long> inDepotIdList) {
		this.inDepotIdList = inDepotIdList;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}
}
