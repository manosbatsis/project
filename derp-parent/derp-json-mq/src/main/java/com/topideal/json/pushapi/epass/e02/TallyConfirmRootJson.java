package com.topideal.json.pushapi.epass.e02;

/**
 * 理货确认 推送报文
 * 
 * @author zhanghx
 */
public class TallyConfirmRootJson {

	// 理货单号
	private String asn_no;
	// 指令值 1:确认 2:驳回
	private String directive;
	// 确认时间
	private String date;
	// 商家卓志编码
	private String topideal_code;

	public String getAsn_no() {
		return asn_no;
	}

	public void setAsn_no(String asn_no) {
		this.asn_no = asn_no;
	}

	public String getDirective() {
		return directive;
	}

	public void setDirective(String directive) {
		this.directive = directive;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTopideal_code() {
		return topideal_code;
	}

	public void setTopideal_code(String topideal_code) {
		this.topideal_code = topideal_code;
	}

}
