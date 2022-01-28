package com.topideal.json.api.v1_2;

import java.util.List;

/**
 * 入库申报 mq报文
 * @author zhanghx
 */
public class PurchaseStorageDeclareRootJson {

	// 理货单编码
	private String tallyingOrderCode;
	// 理货时间
	private String tallyingDate;
	// 企业入仓编号（电商企业采购编号，唯一）
	private String entInboundId;
	// 仓库编码
	private String depotCode;
	// 托版数量
	private Integer palletNum;
	// 是否为菜鸟仓（1-是，0-否）
	private String isRookie;
	// 合同号
	private String contractNo;
	// 是否根据合同号去匹配（1-是）
	private String contractNoTag;
	
	private Long merchantId ;
	
	private List<PurchaseStorageDeclareGoodsListJson> goodsList;

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getContractNoTag() {
		return contractNoTag;
	}

	public void setContractNoTag(String contractNoTag) {
		this.contractNoTag = contractNoTag;
	}

	public List<PurchaseStorageDeclareGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<PurchaseStorageDeclareGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}

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

	public String getIsRookie() {
		return isRookie;
	}

	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

}
