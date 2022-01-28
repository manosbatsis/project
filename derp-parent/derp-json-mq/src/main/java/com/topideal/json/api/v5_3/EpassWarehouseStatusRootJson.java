package com.topideal.json.api.v5_3;

/**
 * MQ进仓单状态回推接口 12033
 * 
 * @author 联想302
 *
 */
public class EpassWarehouseStatusRootJson {

	private String entInboundId;// 企业入仓编号（电商企业采购编号，唯一）
	private String status;// 1: 成功 ，2： 失败
	private String isRookie;// 是否菜鸟字段(1-是，0-否)
	private Long    merchantId;  //商家id
	private String  merchantName;  //商家名称
	private String  topidealCode;  //卓志编码
	private String  inboundDate;  //进仓单生效日期 yyyy-MM-dd HH:mm:ss

	public String getEntInboundId() {
		return entInboundId;
	}

	public void setEntInboundId(String entInboundId) {
		this.entInboundId = entInboundId;
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

	public String getInboundDate() {
		return inboundDate;
	}

	public void setInboundDate(String inboundDate) {
		this.inboundDate = inboundDate;
	}
	

}
