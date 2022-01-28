package com.topideal.api.finance.f2_01;

import java.math.BigDecimal;
import java.util.List;

/**
 * 融资申请请求头
 * @author 杨创
 * 2021-04-09
 */
public class FinanceApplyRequest {
	private String applyNo;//融资申请单号 必填
	private String borrower;// 借款方 必填
	private String sourceCode;// 所属物流企业 字符串，枚举值：卓志：10 非必填
	private String tenant;// 字符串DSTP  JFX 必填
	private String capital;//资金方字符串  广州银行  健太  卓烨  必填
	private String pledgeType;//质押类型，金融产品为存货质押时不可为空  字符串，枚举值：1：全仓质押 2：部分质押
	private BigDecimal amount;//融资金额 浮点数，decimal，2位小数  必填
	private String billCurrency;// 订单币别 必填
	private String applyTime;//申请时间 日期 格式：yyyy-mm-dd HH:mi:ss 必填
	private Integer totalQty;//总数量 非必填
	private String supplier;//供应商 非必填
	private String warehouseId;//仓库ID （多个以，号隔开） 非必填
	private String remarks;//备注 非必填 
	private String addType;//类型 10：新增 20：附件上传  必填
	private String interestCurrency;//计息币别，存货质押、浮动质押必须与订单币别一致  必填
	private String platform;//渠道，融资方式为以销定采必填    字符串TMALL：天猫   JD：京东   VIP：唯品  KAOLA：考拉    YUNJI：云集 
	private String financingMode;//融资方式，组合金融产品必填  字符串01：组合资产  02：以销定采   03：采购融资
	private String product;//金融产品  字符串  CHZY：存货质押   FDZY：浮动质押    XJDC：现金代采  YSZK：应收账款   ZHJR：组合金融
	private String paymentType;//  放款方式 字符串 Online：线上 Offline：线下 必填
	private String paymentStatus;// 放款状态 字符串 0：未放款  1：已放款 非必填
	private String paymentDate;//实际放款日期 非必填  格式：yyyy-mm-dd 非必填
	private BigDecimal pickupAddr;// 提货地址  非必填
	private BigDecimal actualMargin;//实收保证金 浮点数，decimal，2位小数  非必填
	
	private List<FinanceApplyItemRequest> custentrystoreList;

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getPledgeType() {
		return pledgeType;
	}

	public void setPledgeType(String pledgeType) {
		this.pledgeType = pledgeType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBillCurrency() {
		return billCurrency;
	}

	public void setBillCurrency(String billCurrency) {
		this.billCurrency = billCurrency;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public Integer getTotalQty() {
		return totalQty;
	}

	public void setTotalQty(Integer totalQty) {
		this.totalQty = totalQty;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getAddType() {
		return addType;
	}

	public void setAddType(String addType) {
		this.addType = addType;
	}

	public String getInterestCurrency() {
		return interestCurrency;
	}

	public void setInterestCurrency(String interestCurrency) {
		this.interestCurrency = interestCurrency;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getFinancingMode() {
		return financingMode;
	}

	public void setFinancingMode(String financingMode) {
		this.financingMode = financingMode;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getPickupAddr() {
		return pickupAddr;
	}

	public void setPickupAddr(BigDecimal pickupAddr) {
		this.pickupAddr = pickupAddr;
	}

	public BigDecimal getActualMargin() {
		return actualMargin;
	}

	public void setActualMargin(BigDecimal actualMargin) {
		this.actualMargin = actualMargin;
	}

	public List<FinanceApplyItemRequest> getCustentrystoreList() {
		return custentrystoreList;
	}

	public void setCustentrystoreList(List<FinanceApplyItemRequest> custentrystoreList) {
		this.custentrystoreList = custentrystoreList;
	}
	
	
	
}
