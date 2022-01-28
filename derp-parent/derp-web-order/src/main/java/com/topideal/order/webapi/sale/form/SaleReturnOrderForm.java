package com.topideal.order.webapi.sale.form;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.SaleReturnOrderItemDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class SaleReturnOrderForm extends PageForm{

	@ApiModelProperty(value = "令牌",required = true)
	private String token;
	@ApiModelProperty(value = "销售退货订单号")
	private String code;
	@ApiModelProperty(value = "退出仓库id")
	private Long outDepotId;
	@ApiModelProperty(value = "退入仓库id")
	private Long inDepotId;
	@ApiModelProperty(value = "客户id")
	private Long customerId;
	@ApiModelProperty(value = "状态：0-待审核 1-已审核 2-已删除")
	private String status;
	@ApiModelProperty(value = "销售退货类型 1-消费者退货 2-代销退货")
	private String returnType;
	@ApiModelProperty(value = "关联销售单id")
	private Long saleOrderId;
	@ApiModelProperty(value = "关联销售单单号")
	private String saleOrderRelCode;
	@ApiModelProperty(value = "创建开始日期")
	private String createStartDate;
	@ApiModelProperty(value = "创建结束日期")
	private String createEndDate;
	@ApiModelProperty(value = "事业部id")
	private Long buId;
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "商家名称")
	private String merchantName;
	@ApiModelProperty(value = "商家ID")
	private Long merchantId;
	@ApiModelProperty(value = "备注")
	private String remark;
	@ApiModelProperty(value = "LBX单号")
	private String lbxNo;
	@ApiModelProperty(value = "币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑")
	private String currency;
	@ApiModelProperty(value = "海外仓理货单位 00-托盘 01-箱 02-件")
	private String tallyingUnit;
	@ApiModelProperty(value = "PO号")
	private String poNo;
	@ApiModelProperty(value = "退货方式 1-退货退款，2-仅退货")
	private String returnMode;
	@ApiModelProperty(value = "业务模式 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结")
	private String businessModel;
	@ApiModelProperty(value = "主键集合ids")
	private String ids;
	@ApiModelProperty(value = "表体信息")
	private List<SaleReturnOrderItemDTO> itemList;
	@ApiModelProperty(value = "退入仓库id集合")
	private List<Long> inDepotIdList;
	@ApiModelProperty(value = "平台售后单号")
	private String platformReturnCode;
	@ApiModelProperty(value = "事业部库位类型ID")
	private Long stockLocationTypeId;

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
	public Long getOutDepotId() {
		return outDepotId;
	}
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}
	public Long getInDepotId() {
		return inDepotId;
	}
	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
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
	public String getCreateStartDate() {
		return createStartDate;
	}
	public void setCreateStartDate(String createStartDate) {
		this.createStartDate = createStartDate;
	}
	public String getCreateEndDate() {
		return createEndDate;
	}
	public void setCreateEndDate(String createEndDate) {
		this.createEndDate = createEndDate;
	}
	public Long getBuId() {
		return buId;
	}
	public void setBuId(Long buId) {
		this.buId = buId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Long getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLbxNo() {
		return lbxNo;
	}
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	public String getReturnMode() {
		return returnMode;
	}
	public void setReturnMode(String returnMode) {
		this.returnMode = returnMode;
	}
	public String getBusinessModel() {
		return businessModel;
	}
	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public List<SaleReturnOrderItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<SaleReturnOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public List<Long> getInDepotIdList() {
		return inDepotIdList;
	}

	public void setInDepotIdList(List<Long> inDepotIdList) {
		this.inDepotIdList = inDepotIdList;
	}

	public String getPlatformReturnCode() {
		return platformReturnCode;
	}

	public void setPlatformReturnCode(String platformReturnCode) {
		this.platformReturnCode = platformReturnCode;
	}

	public Long getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(Long stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}
}

