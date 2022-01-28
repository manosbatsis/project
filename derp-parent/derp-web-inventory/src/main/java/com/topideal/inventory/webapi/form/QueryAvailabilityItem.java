package com.topideal.inventory.webapi.form;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 校验库存量接口是否足够
 *
 * @author lian_
 */
@ApiModel
public class QueryAvailabilityItem   implements Serializable {

    @ApiModelProperty(value = "仓库id",required = true)
    private Long depotId;
    @ApiModelProperty(value = "商品id", required = true)
    private Long goodsId;
    @ApiModelProperty(value = "商品货号",required = true)
    private String goodsNo;
    @ApiModelProperty(value = "好品出库量")
    private Integer okNum;
    @ApiModelProperty(value = "坏品品出库量")
    private Integer badNum;
    @ApiModelProperty(value = "过期量")
    private Integer expireNum;
    @ApiModelProperty(value = "理货单位 00 托盘 01箱  02 件")
    private String tallyingUnit;
    @ApiModelProperty(value = "批次号")
    private String batchNo;
    @ApiModelProperty(value = "生产日期")
    private String productionDate;
    @ApiModelProperty(value = "失效日期")
    private String overdueDate;   
    @ApiModelProperty(value = "查询库存类型 1.查询余量,2.查询可用量", required = true)
    private String inventoryType;
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public Integer getOkNum() {
		return okNum;
	}
	public void setOkNum(Integer okNum) {
		this.okNum = okNum;
	}
	public Integer getBadNum() {
		return badNum;
	}
	public void setBadNum(Integer badNum) {
		this.badNum = badNum;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}
	public Long getDepotId() {
		return depotId;
	}
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}
	public Integer getExpireNum() {
		return expireNum;
	}
	public void setExpireNum(Integer expireNum) {
		this.expireNum = expireNum;
	}
	public String getInventoryType() {
		return inventoryType;
	}
	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}
	
	
  
}

