package com.topideal.json.api.v2_8;

import java.util.List;

import com.topideal.json.api.v1_1.PurchaseFirstTallyGoodsListJson;

/**
 * 销售退货--初理货 推mq报文
 * @author 杨创
 * 2018.7.26
 */
public class SaleReturnFirstTallyRootJson {

	// 理货单编码
	private String tallyingOrderCode;
	// 理货时间
	private String tallyingDate;
	// 企业入仓编码
	private String entInboundId;
	// 仓库编码
	private String depotCode;
	// 托版数量
	private Integer palletNum;

	private List<SaleReturnFirstTallyGoodsListJson> goodsList;

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

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public Integer getPalletNum() {
		return palletNum;
	}

	public void setPalletNum(Integer palletNum) {
		this.palletNum = palletNum;
	}

	public List<SaleReturnFirstTallyGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<SaleReturnFirstTallyGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}

}
