package com.topideal.json.inventory.j02;

import java.io.Serializable;
import java.util.List;

/**
 *  库存冻结和解冻ROOT实体
 * @author 联想302
 *
 */
public class InventoryFreezeRootJson implements Serializable{

	
	private String merchantId; //商家ID
	private String merchantName;//商家名称
	private String depotId;//仓库ID
	private String depotName;//仓库名称
	private String depotType;//仓库类型
	private String orderNo;//来源订单号
	private String businessNo;//业务订单号
	private String source;//单据1采购  2 调拨 3 销售 4库存调整单5类型调整
	private String sourceType;//单据类型
	private String sourceDate;//单据时间
	private String operateType;//操作类型  （0减，1加）
	private List<InventoryFreezeGoodsListJson> goodsList; //商品集合
	private String isReverse;//是否为反审  （0-否，1-是）
	
	
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getDepotType() {
		return depotType;
	}
	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourceDate() {
		return sourceDate;
	}
	public void setSourceDate(String sourceDate) {
		this.sourceDate = sourceDate;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public List<InventoryFreezeGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<InventoryFreezeGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}
	public String getBusinessNo() {
		return businessNo;
	}
	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	public String getIsReverse() {
		return isReverse;
	}
	public void setIsReverse(String isReverse) {
		this.isReverse = isReverse;
	}
	
}
