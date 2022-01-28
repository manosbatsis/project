package com.topideal.json.api.v3_2;
/**
 * 进境状态回推MQ
 * @author lian_
 *
 */
public class ESaleEnterBorderStatusJson {

	private Long merchantId ;
	private String externalCode; //外部单号
	private String declareDate;  //申报日期
	private String type;       //1:国检;2:海关
	private String status;      //放行状态：海关状态	21：已报海关22：海关单证放行23：报海关失败24：海关查验/转人工/挂起等
											//25：海关单证审核不通过26：海关已接受申报，待货物运抵后处理41：海关货物查扣42：海关货物放行
											//国检状态11：已报国检12：国检放行13：国检审核驳回14：国检抽检15：国检抽检未过
	
	public String getExternalCode() {
		return externalCode;
	}
	public void setExternalCode(String externalCode) {
		this.externalCode = externalCode;
	}
	public String getDeclareDate() {
		return declareDate;
	}
	public void setDeclareDate(String declareDate) {
		this.declareDate = declareDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}
}
