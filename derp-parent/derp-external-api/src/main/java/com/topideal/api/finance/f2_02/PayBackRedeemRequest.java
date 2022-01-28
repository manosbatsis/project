package com.topideal.api.finance.f2_02;

import java.math.BigDecimal;
import java.util.List;

/**
 * 3.7企业还款赎回接收接口 表头
 * @author 杨创
 * 2021-04-09
 */
public class PayBackRedeemRequest {
	private String applyNo;//融资申请单号 必填
	private String applyDate;//申请时间 日期 格式：yyyy-mm-dd 必填  取系统当前时间  必填 
	private String refundDate;//预计还款结息日期：yyyy-mm-dd 必填 取融资赎回弹窗-实际还款日期字段  必填
	private BigDecimal applyAmount;//申请还款本金，decimal，2位小数  默认：空  非 必填
	private String borrower;// 借款方  根据销售订单中客户编码，查询客户档案的营业执照号 必填
	private String interestCurrency;//还款币别 取销售订单币种字段的编码 必填
	private String repaymentType; // 字符串 online：线上   offline：线下 必填	
	private String actualDate; //实际还款日（线上还款必填） 非必填
	private String onCredit; // 是否赊销 字符串0：否  1：是
	private String status;// 字符串 10：新增 20：取消 30：还款
	private List<PayBackRedeemApplyItemRequest> applyList;
	private List<PayBackRedeemItemRequest> goodList;
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getRefundDate() {
		return refundDate;
	}
	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}
	public BigDecimal getApplyAmount() {
		return applyAmount;
	}
	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
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
	public String getRepaymentType() {
		return repaymentType;
	}
	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}
	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	public String getOnCredit() {
		return onCredit;
	}
	public void setOnCredit(String onCredit) {
		this.onCredit = onCredit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<PayBackRedeemApplyItemRequest> getApplyList() {
		return applyList;
	}
	public void setApplyList(List<PayBackRedeemApplyItemRequest> applyList) {
		this.applyList = applyList;
	}
	public List<PayBackRedeemItemRequest> getGoodList() {
		return goodList;
	}
	public void setGoodList(List<PayBackRedeemItemRequest> goodList) {
		this.goodList = goodList;
	}

	
	
	
}
