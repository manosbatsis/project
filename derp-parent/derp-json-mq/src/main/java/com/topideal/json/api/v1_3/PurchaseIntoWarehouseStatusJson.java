package com.topideal.json.api.v1_3;

/**
 * 进仓状态回推 mq报文
 * @author zhanghx
 */
public class PurchaseIntoWarehouseStatusJson {

	// 企业入仓编号（电商企业采购编号，唯一）
	private String entInboundId;
	// 理货单ID（多个以，分隔）
	private String tallyingOrderCode;
	// 1: 成功 ，2： 失败
	private String status;
	// 是否为菜鸟仓（1-是，0-否）
	private String isRookie;
	// 商家id
	private String merchantId;
	// 商家名称
	private String merchantName;
	// 商家卓志编码
	private String topidealCode;
	// 进仓单生效日期,格式:yyyy-MM-dd HH:mm:ss
	private String inboundDate;
	// 错误信息
	private String notes;
	// 合同号
	private String contractNo;
	// 是否根据合同号去匹配（1-是）
	private String contractNoTag;

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

	public String getEntInboundId() {
		return entInboundId;
	}

	public void setEntInboundId(String entInboundId) {
		this.entInboundId = entInboundId;
	}

	public String getTallyingOrderCode() {
		return tallyingOrderCode;
	}

	public void setTallyingOrderCode(String tallyingOrderCode) {
		this.tallyingOrderCode = tallyingOrderCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsRookie() {
		return isRookie;
	}

	public void setIsRookie(String isRookie) {
		this.isRookie = isRookie;
	}

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

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	public String getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(String inboundDate) {
		this.inboundDate = inboundDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
