package com.topideal.json.api.v5_2;

import java.util.List;

/**
 * MQ入库申报接口 12023-调拨
 * 
 * @author 联想302
 *
 */
public class EpassStorageDeclareRootJson {

	private String entInboundId;// 企业入仓编号（电商企业采购编号，唯一）
	private String isRookie;// 是否菜鸟字段(1-是，0-否)
	private Long    merchantId;  //商家id
	private String  merchantName;  //商家名称
	private String  topidealCode;  //卓志编码
	private String tallyingOrderCode;// 理货单编码
	private List<EpassStorageDeclareGoodsListJson> goodsList;

	public String getEntInboundId() {
		return entInboundId;
	}

	public void setEntInboundId(String entInboundId) {
		this.entInboundId = entInboundId;
	}

	public String getIsRookie() {
		return isRookie;
	}

	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}

	public List<EpassStorageDeclareGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<EpassStorageDeclareGoodsListJson> goodsList) {
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

	public String getTallyingOrderCode() {
		return tallyingOrderCode;
	}

	public void setTallyingOrderCode(String tallyingOrderCode) {
		this.tallyingOrderCode = tallyingOrderCode;
	}
}
