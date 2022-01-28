package com.topideal.webapi.form;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商家仓库信息表体
 * @author 
 */
public class MerchantInfoAddDepotForm implements Serializable{


	 @ApiModelProperty(value = "仓库id")
	 private Long depotId;
	 @ApiModelProperty(value = "进出接口指令 1-是 0 - 否")
     private String isInOutInstruction;
	 @ApiModelProperty(value = "统计存货跌价 1-是 0-否")
     private String isInvertoryFallingPrice;
	 @ApiModelProperty(value = "选品限制 1-仅备案商品 2-仅外仓统一码 3-无限制")
     private String productRestriction;
	 @ApiModelProperty(value = "已入定出 1-是 0-否")
     private String isInDependOut;
	 @ApiModelProperty(value = "已出定入 1-是 0-否")
     private String isOutDependIn;
	 @ApiModelProperty(value = "合同编码")
     private String contractCode;
	 @ApiModelProperty(value = "事业部id")
     private List<Long> buIdList;

	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public String getIsInOutInstruction() {
		return isInOutInstruction;
	}
	public void setIsInOutInstruction(String isInOutInstruction) {
		this.isInOutInstruction = isInOutInstruction;
	}
	public String getIsInvertoryFallingPrice() {
		return isInvertoryFallingPrice;
	}
	public void setIsInvertoryFallingPrice(String isInvertoryFallingPrice) {
		this.isInvertoryFallingPrice = isInvertoryFallingPrice;
	}
	public String getProductRestriction() {
		return productRestriction;
	}
	public void setProductRestriction(String productRestriction) {
		this.productRestriction = productRestriction;
	}
	public String getIsInDependOut() {
		return isInDependOut;
	}
	public void setIsInDependOut(String isInDependOut) {
		this.isInDependOut = isInDependOut;
	}
	public String getIsOutDependIn() {
		return isOutDependIn;
	}
	public void setIsOutDependIn(String isOutDependIn) {
		this.isOutDependIn = isOutDependIn;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public List<Long> getBuIdList() {
		return buIdList;
	}
	public void setBuIdList(List<Long> buIdList) {
		this.buIdList = buIdList;
	}
	 

}

