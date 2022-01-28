package com.topideal.json.api.v2_1;

import java.util.List;

/**
 * 销售退货  入库申报 推mq报文
 * @author lian_
 *
 */
public class SaleStorageDeclareRootIson {

	private String tallyingDate; //退货入库时间
	private String entInboundId;  //企业入仓编号
	private Long merchantId ;
	
	private List<SaleStorageDeclareGoodsListJson> goodsList;
	
	
	public List<SaleStorageDeclareGoodsListJson> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<SaleStorageDeclareGoodsListJson> goodsList) {
		this.goodsList = goodsList;
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
