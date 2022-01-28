package com.topideal.json.api.v5_1;

import java.util.List;

/**
 *  MQ初理货接口 ROOT
 * @author 联想302
 *
 */
public class EpassFirstTallyRootJson {

	
	private String tallyingOrderCode ;//理货单编码
	private String tallyingDate      ;//理货时间
	private String entInboundId      ;//企业入仓编号（电商企业采购编号，唯一）
	private Long    merchantId;  //商家id
	private String  merchantName;  //商家名称
	private String  topidealCode;  //卓志编码
	private List<EpassFirstTallyGoodsListJson> goodsList;
	
	
	public String getTallyingOrderCode() {
		return tallyingOrderCode;
	}
	public void setTallyingOrderCode(String tallyingOrderCode) {
		this.tallyingOrderCode = tallyingOrderCode;
	}
	public String getTallyingDate() {
		return tallyingDate;
	}
	public void setTallyingDate(String tallyingDate) {
		this.tallyingDate = tallyingDate;
	}
	public String getEntInboundId() {
		return entInboundId;
	}
	public void setEntInboundId(String entInboundId) {
		this.entInboundId = entInboundId;
	}
	public List<EpassFirstTallyGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<EpassFirstTallyGoodsListJson> goodsList) {
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
