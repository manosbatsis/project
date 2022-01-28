package com.topideal.api.epass.e02;

/**
 * 理货确认 推送报文
 * @author zhanghx
 * */
public class TallyConfirmRequest  {

	// 理货单号
	private String asn_no;
	// 指令值 1:确认 2:驳回
	private String directive;
	// 确认时间
	private String date;

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

}
