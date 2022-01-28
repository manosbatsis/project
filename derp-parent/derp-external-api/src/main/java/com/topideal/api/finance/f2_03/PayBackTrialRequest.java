package com.topideal.api.finance.f2_03;

import java.util.List;

/**
 * 3.16 还款试算 表头
 * @author 杨创
 * 2021-04-09
 */
public class PayBackTrialRequest {
	private String refundDate;//预计还款结息日期 必填 日期格式：yyyy-mm-dd
	private String borrower;//借款方   必填 
	private String interestCurrency;//还款币别  必填
	
	private List<PayBackTrialBillRequest> billList;
	private List<PayBackTrialGoodRequest> goodList;
	public String getRefundDate() {
		return refundDate;
	}
	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}
	public String getBorrower() {
		return borrower;
	}
	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}
	public String getInterestCurrency() {
		return interestCurrency;
	}
	public void setInterestCurrency(String interestCurrency) {
		this.interestCurrency = interestCurrency;
	}
	public List<PayBackTrialBillRequest> getBillList() {
		return billList;
	}
	public void setBillList(List<PayBackTrialBillRequest> billList) {
		this.billList = billList;
	}
	public List<PayBackTrialGoodRequest> getGoodList() {
		return goodList;
	}
	public void setGoodList(List<PayBackTrialGoodRequest> goodList) {
		this.goodList = goodList;
	}


	
	
	
	
}
