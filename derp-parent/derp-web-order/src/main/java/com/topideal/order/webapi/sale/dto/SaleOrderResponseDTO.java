package com.topideal.order.webapi.sale.dto;

import java.util.List;

import com.topideal.entity.dto.sale.SaleFinancingOrderDTO;
import com.topideal.entity.dto.sale.SaleOrderDTO;
import com.topideal.entity.dto.sale.SaleOrderItemDTO;
import com.topideal.entity.dto.sale.SaleShelfDTO;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.entity.vo.sale.SaleShelfModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleOrderResponseDTO {
	@ApiModelProperty(value = "销售订单信息")
	private SaleOrderDTO saleOrderDTO ;
	@ApiModelProperty(value = "销售订单表体信息")
	private List<SaleOrderItemDTO> saleOrderItemList;
	@ApiModelProperty(value = "平台采购单id")
	private String platformPurchaseIds;
	@ApiModelProperty(value = "车次是否必填 0:否，1：是")
	private String isTrainnoRequired;	
	@ApiModelProperty(value = "标准箱TEU是否必填  0:否，1：是")
	private String isTeuRequired; 
	@ApiModelProperty(value = "是否同关区是否禁用 true，false")
	private String isSameAreaDisabled ;	 
	@ApiModelProperty(value = "是否同关区是否必填 0:否，1：是")
	private String isSameAreaRequired ;	 
	@ApiModelProperty(value = "入库仓库是否禁用 true，false")
	private String inDepotDisabled ; 
	@ApiModelProperty(value = "入库仓库是否必填 0:否，1：是")
	private String inDepotRequired ; 
	@ApiModelProperty(value = "PO单号是否必填 0:否，1：是")
	private String isPoRequired; 
	@ApiModelProperty(value = "合同号是否必填 0:否，1：是")
	private String isContractNoRequired ;	 
	@ApiModelProperty(value = "出库仓类型")
	private String outDepotType;
	@ApiModelProperty(value = "公司的申报系数")
	private String priceDeclareRatio;
	@ApiModelProperty(value = "商品上架信息")
	private SaleShelfDTO saleShelfDTO;
	@ApiModelProperty(value = "商品出库信息")
	private SaleOutDepotModel saleOutModel;
	@ApiModelProperty(value = "销售订单是否有商品已上架 yes：该销售订单有商品上架，no:该销售订单还没有商品上架")
	private String isNotShelf;
	@ApiModelProperty(value = "pageSource")
	private String pageSource;
	@ApiModelProperty(value = "商品表体数量")
	private String itemCount;
	@ApiModelProperty(value = "申报地海关编码")
	private String customsNo;
	@ApiModelProperty(value = "销售订单出库的百分比")
	private String percent;
	@ApiModelProperty(value = "销售订单编码")
	private String orderCode;
	@ApiModelProperty(value = "上架信息集合")
	private List<SaleShelfModel> shelfList;
	@ApiModelProperty(value = "po号集合")
	private List<String> poList;
	@ApiModelProperty(value = "操作 1-编辑 2-审核")
	private String operate;
	@ApiModelProperty(value = "出仓仓库是否为香港仓")
	private String isHKOutDepot;
	@ApiModelProperty(value = "是否启用销售价格管理")
	private Boolean salePriceManage;
	@ApiModelProperty(value = "融资信息")
	private SaleFinancingOrderDTO saleFinancingOrderDTO;
	@ApiModelProperty(value = "保存平台采购单转销售，存在的错误信息")
	private String errorMsg;
	
	public SaleOrderDTO getSaleOrderDTO() {
		return saleOrderDTO;
	}
	public void setSaleOrderDTO(SaleOrderDTO saleOrderDTO) {
		this.saleOrderDTO = saleOrderDTO;
	}
	public List<SaleOrderItemDTO> getSaleOrderItemList() {
		return saleOrderItemList;
	}
	public void setSaleOrderItemList(List<SaleOrderItemDTO> saleOrderItemList) {
		this.saleOrderItemList = saleOrderItemList;
	}
	public String getPlatformPurchaseIds() {
		return platformPurchaseIds;
	}
	public void setPlatformPurchaseIds(String platformPurchaseIds) {
		this.platformPurchaseIds = platformPurchaseIds;
	}
	public String getIsTrainnoRequired() {
		return isTrainnoRequired;
	}
	public void setIsTrainnoRequired(String isTrainnoRequired) {
		this.isTrainnoRequired = isTrainnoRequired;
	}
	public String getIsTeuRequired() {
		return isTeuRequired;
	}
	public void setIsTeuRequired(String isTeuRequired) {
		this.isTeuRequired = isTeuRequired;
	}
	public String getIsSameAreaDisabled() {
		return isSameAreaDisabled;
	}
	public void setIsSameAreaDisabled(String isSameAreaDisabled) {
		this.isSameAreaDisabled = isSameAreaDisabled;
	}
	public String getIsSameAreaRequired() {
		return isSameAreaRequired;
	}
	public void setIsSameAreaRequired(String isSameAreaRequired) {
		this.isSameAreaRequired = isSameAreaRequired;
	}
	public String getInDepotDisabled() {
		return inDepotDisabled;
	}
	public void setInDepotDisabled(String inDepotDisabled) {
		this.inDepotDisabled = inDepotDisabled;
	}
	public String getInDepotRequired() {
		return inDepotRequired;
	}
	public void setInDepotRequired(String inDepotRequired) {
		this.inDepotRequired = inDepotRequired;
	}
	public String getIsPoRequired() {
		return isPoRequired;
	}
	public void setIsPoRequired(String isPoRequired) {
		this.isPoRequired = isPoRequired;
	}
	public String getIsContractNoRequired() {
		return isContractNoRequired;
	}
	public void setIsContractNoRequired(String isContractNoRequired) {
		this.isContractNoRequired = isContractNoRequired;
	}
	public String getOutDepotType() {
		return outDepotType;
	}
	public void setOutDepotType(String outDepotType) {
		this.outDepotType = outDepotType;
	}
	public String getPriceDeclareRatio() {
		return priceDeclareRatio;
	}
	public void setPriceDeclareRatio(String priceDeclareRatio) {
		this.priceDeclareRatio = priceDeclareRatio;
	}
	public SaleShelfDTO getSaleShelfDTO() {
		return saleShelfDTO;
	}
	public void setSaleShelfDTO(SaleShelfDTO saleShelfDTO) {
		this.saleShelfDTO = saleShelfDTO;
	}
	public SaleOutDepotModel getSaleOutModel() {
		return saleOutModel;
	}
	public void setSaleOutModel(SaleOutDepotModel saleOutModel) {
		this.saleOutModel = saleOutModel;
	}
	public String getIsNotShelf() {
		return isNotShelf;
	}
	public void setIsNotShelf(String isNotShelf) {
		this.isNotShelf = isNotShelf;
	}
	public String getPageSource() {
		return pageSource;
	}
	public void setPageSource(String pageSource) {
		this.pageSource = pageSource;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}
	public String getCustomsNo() {
		return customsNo;
	}
	public void setCustomsNo(String customsNo) {
		this.customsNo = customsNo;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public List<SaleShelfModel> getShelfList() {
		return shelfList;
	}
	public void setShelfList(List<SaleShelfModel> shelfList) {
		this.shelfList = shelfList;
	}
	public List<String> getPoList() {
		return poList;
	}
	public void setPoList(List<String> poList) {
		this.poList = poList;
	}
	public String getIsHKOutDepot() {
		return isHKOutDepot;
	}
	public void setIsHKOutDepot(String isHKOutDepot) {
		this.isHKOutDepot = isHKOutDepot;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	public Boolean getSalePriceManage() {
		return salePriceManage;
	}
	public void setSalePriceManage(Boolean salePriceManage) {
		this.salePriceManage = salePriceManage;
	}

	public SaleFinancingOrderDTO getSaleFinancingOrderDTO() {
		return saleFinancingOrderDTO;
	}

	public void setSaleFinancingOrderDTO(SaleFinancingOrderDTO saleFinancingOrderDTO) {
		this.saleFinancingOrderDTO = saleFinancingOrderDTO;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
