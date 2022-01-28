package com.topideal.inventory.webapi.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class QueryAvailabilityResponseDTO {
	
	@ApiModelProperty(value = "仓库名称")
    private String depotName;
	@ApiModelProperty(value = "商品货号")
	private String goodsNo;
	private String massage;
	@ApiModelProperty(value = "批次号")
    private String batchNo;
	@ApiModelProperty(value = "是否过期（0 未过期 1已过期）")
    private String isExpireLabel;
	@ApiModelProperty(value = "理货单位")
    private String unitLabel;
	@ApiModelProperty(value = "库存类型  1 正常品  2 残次品")
    private String typeLabel;	
	@ApiModelProperty(value = "出库量")
    private Integer num;
	@ApiModelProperty(value = "库存余量")
	
    private Integer surplusNum;
	@ApiModelProperty(value = "错误信息")
	
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getIsExpireLabel() {
		return isExpireLabel;
	}
	public void setIsExpireLabel(String isExpireLabel) {
		this.isExpireLabel = isExpireLabel;
	}
	public String getUnitLabel() {
		return unitLabel;
	}
	public void setUnitLabel(String unitLabel) {
		this.unitLabel = unitLabel;
	}
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getSurplusNum() {
		return surplusNum;
	}
	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}
	public String getMassage() {
		return massage;
	}
	public void setMassage(String massage) {
		this.massage = massage;
	}
	
	
}
