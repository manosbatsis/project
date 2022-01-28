package com.topideal.json.api.v3_3;

import java.util.List;

/**
 * 电商 装载交运回推MQ
 * @author lian_
 */
public class ESaleLoadingDeliveriesMQRootJson {
	private String orderCode; // 订单号或者LBX号
	private String wayBillNo; // 运单号必填
	private String deliverDate; // 发货时间
	private String logisticsCode; // 物流公司代码
	private String logisticsName; // 物流公司名称
	private String blNo; // 提单号
	private String type; // 服务类型 业务类型10：B2B20：B2B2C
	private String remark; // 备注
	private String topidealCode; // 备注
	private Double loadWeight; // 包裹重量
	private Long merchantId ;


	private List<ESaleLoadingDeliveriesMQGoodsListJson> goodsList;

	public List<ESaleLoadingDeliveriesMQGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<ESaleLoadingDeliveriesMQGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getWayBillNo() {
		return wayBillNo;
	}

	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	public String getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public Double getLoadWeight() {
		return loadWeight;
	}

	public void setLoadWeight(Double loadWeight) {
		this.loadWeight = loadWeight;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
