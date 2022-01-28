package com.topideal.entity.dto.sale;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP_ORDER;

public class InventoryLocationAdjustExportDTO {
    /**
     * 仓库名称
     */
    private String depotName;
    /**
     * 事业部名称
     */
    private String buName;
    /**
     * 归属月份
     */
    private Timestamp ownDate;
    /**
     * 备注
     */
    private String remark;

    /**
     * 关联外部单号
     */
    private String externalCode;
    /**
     * 调增商品货号
     */
    private String increaseGoodsNo;
    /**
     * 调减商品货号
     */
    private String reduceGoodsNo;
    /**
     * 库存类型 0：好品 1：坏品
     */
    private String inventoryType;
    private String inventoryTypeLabel;

    private String platformCode;
    private String shopCode;

    /**
     * 客户id
     */
    private Long customerId;
    /**
     * 客户名称
     */
    private String customerName;
    public String getPlatformCode(){
        return platformCode;
    }
    public void setPlatformCode(String  platformCode){
        this.platformCode=platformCode;
    }

    public String getShopCode(){
        return shopCode;
    }
    public void setShopCode(String  shopCode){
        this.shopCode=shopCode;
    }



    /**
     * 调整数量
     */
    private Integer adjustNum;
    /**
     * 单据类型 DSDD-电商订单 DBRK-调拨入库 DBCK-调拨出库 XSCK-销售出库
     */
    private String type;
    private String typeLabel;

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName;
    }

    public Timestamp getOwnDate() {
        return ownDate;
    }

    public void setOwnDate(Timestamp ownDate) {
        this.ownDate = ownDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExternalCode() {
        return externalCode;
    }

    public void setExternalCode(String externalCode) {
        this.externalCode = externalCode;
    }

    public String getIncreaseGoodsNo() {
        return increaseGoodsNo;
    }

    public void setIncreaseGoodsNo(String increaseGoodsNo) {
        this.increaseGoodsNo = increaseGoodsNo;
    }

    public String getReduceGoodsNo() {
        return reduceGoodsNo;
    }

    public void setReduceGoodsNo(String reduceGoodsNo) {
        this.reduceGoodsNo = reduceGoodsNo;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String  inventoryType){
        this.inventoryType=inventoryType;
        if(StringUtils.isNotBlank(inventoryType)){
            this.inventoryTypeLabel= DERP_ORDER.getLabelByKey(DERP_ORDER.inventoryLocationAdjustmentOrderItem_inventoryTypeList, inventoryType);
        }
    }
    public String getInventoryTypeLabel() {
        return inventoryTypeLabel;
    }

    public void setInventoryTypeLabel(String inventoryTypeLabel) {
        this.inventoryTypeLabel = inventoryTypeLabel;
    }

    public Integer getAdjustNum() {
        return adjustNum;
    }

    public void setAdjustNum(Integer adjustNum) {
        this.adjustNum = adjustNum;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.inventoryLocationAdjustmentOrder_typeList, type);
    }
    
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
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

	
	
	
	
}
