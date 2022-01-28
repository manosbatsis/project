package com.topideal.json.api.v5_4;

import java.util.List;

/**
 * 4.6 MQ装载交运回推12063
 * 
 * @author 联想302
 *
 */
public class EpassLoadinfDelivriesRootJson {

	private String orderCode;// 订单号或者LBX号
	private String wayBillNo;// 运单号必填
	private String deliverDate;// 发货时间 yyyy-MM-dd HH:mm:ss
	private String type;// 服务类型 业务类型10：B2B 20：B2B2C
	private String isRookie;// 是否菜鸟字段(1-是，0-否)
	private Long    merchantId;  //商家id
	private String  merchantName;  //商家名称
	private String  topidealCode;  //卓志编码
	private List<EpassLoadinfDelivriesGoodsListJson> goodsList;// 商品集合

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsRookie() {
		return isRookie;
	}

	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}

	public List<EpassLoadinfDelivriesGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<EpassLoadinfDelivriesGoodsListJson> goodsList) {
		this.goodsList = goodsList;
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

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	
}
