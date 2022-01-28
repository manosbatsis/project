package com.topideal.entity.dto.bill;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ReceiveBillCostItemDTO extends PageModel implements Serializable {

	/**
	 * ID
	 */
	private Long id;
	/**
	 * 应收账单Id
	 */
	private Long billId;
	/**
	 * 项目id
	 */
	private Long projectId;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * po单号
	 */
	private String poNo;
	/**
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品货号
	 */
	private String goodsNo;
	/**
	 * 费用金额
	 */
	private BigDecimal price;
	/**
	 * 数量
	 */
	private Integer num;
	// 补扣款类型 0-补款 1-扣款
	private String billType;
	private String billTypeLabel;
	/**
	 * 创建时间
	 */
	private Timestamp createDate;
	/**
	 * 修改时间
	 */
	private Timestamp modifyDate;

	// 标准条码
	private String commbarcode;

	/**
	 * 母品牌
	 */
	private String parentBrandName;
	/**
	 * 科目编码
	 */
	private String subjectCode;
	/**
	 * 科目名称
	 */
	private String subjectName;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * NC收支费项名称
	 */
	private String paymentSubjectName;
	/**
	 * NC收支费项id
	 */
	private Long paymentSubjectId;
	//核销平台
	private String verifyPlatformCode;
	private String verifyPlatformName;
	/**
    * 母品牌编码
    */
    private String parentBrandCode;

	/**
	 * 一级费项名称
	 */
    private String parentProjectName;
	/**
	 * 母品牌id
	 */
	private Long parentBrandId;
	/**
	 * 发票描述
	 */
	private String invoiceDescription;

	/**
	 * 已核销金额
	 */
	private BigDecimal verifiyAmount;
	/**
	* 税率
	*/
	private Double taxRate;
	/**
	* 税额
	*/
	private BigDecimal tax;
	/**
	* 费用金额（含税）
	*/
	private BigDecimal taxAmount;

	private String projectCode;

	//结算币种
	private String currency;

	/**
	 * 平台商品编码
	 */
	private String platformGoodsCode;

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
		this.billTypeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.receiveBillCost_billTypeList, billType);
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* billId get 方法 */
	public Long getBillId() {
		return billId;
	}

	/* billId set 方法 */
	public void setBillId(Long billId) {
		this.billId = billId;
	}

	/* projectId get 方法 */
	public Long getProjectId() {
		return projectId;
	}

	/* projectId set 方法 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	/* projectName get 方法 */
	public String getProjectName() {
		return projectName;
	}

	/* projectName set 方法 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/* poNo get 方法 */
	public String getPoNo() {
		return poNo;
	}

	/* poNo set 方法 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	/* goodsId get 方法 */
	public Long getGoodsId() {
		return goodsId;
	}

	/* goodsId set 方法 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/* goodsName get 方法 */
	public String getGoodsName() {
		return goodsName;
	}

	/* goodsName set 方法 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/* goodsNo get 方法 */
	public String getGoodsNo() {
		return goodsNo;
	}

	/* goodsNo set 方法 */
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	/* price get 方法 */
	public BigDecimal getPrice() {
		return price;
	}

	/* price set 方法 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/* num get 方法 */
	public Integer getNum() {
		return num;
	}

	/* num set 方法 */
	public void setNum(Integer num) {
		this.num = num;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getCommbarcode() {
		return commbarcode;
	}

	public void setCommbarcode(String commbarcode) {
		this.commbarcode = commbarcode;
	}

	public String getBillTypeLabel() {
		return billTypeLabel;
	}

	public void setBillTypeLabel(String billTypeLabel) {
		this.billTypeLabel = billTypeLabel;
	}

	public String getParentBrandName() {
		return parentBrandName;
	}

	public void setParentBrandName(String parentBrandName) {
		this.parentBrandName = parentBrandName;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPaymentSubjectName() {
		return paymentSubjectName;
	}

	public void setPaymentSubjectName(String paymentSubjectName) {
		this.paymentSubjectName = paymentSubjectName;
	}

	public Long getPaymentSubjectId() {
		return paymentSubjectId;
	}

	public void setPaymentSubjectId(Long paymentSubjectId) {
		this.paymentSubjectId = paymentSubjectId;
	}

	public String getParentBrandCode() {
		return parentBrandCode;
	}

	public void setParentBrandCode(String parentBrandCode) {
		this.parentBrandCode = parentBrandCode;
	}

	public String getParentProjectName() {
		return parentProjectName;
	}

	public void setParentProjectName(String parentProjectName) {
		this.parentProjectName = parentProjectName;
	}

	public Long getParentBrandId() {
		return parentBrandId;
	}

	public void setParentBrandId(Long parentBrandId) {
		this.parentBrandId = parentBrandId;
	}

	public String getInvoiceDescription() {
		return invoiceDescription;
	}

	public void setInvoiceDescription(String invoiceDescription) {
		this.invoiceDescription = invoiceDescription;
	}

	public String getVerifyPlatformCode() {
		return verifyPlatformCode;
	}

	public void setVerifyPlatformCode(String verifyPlatformCode) {
		this.verifyPlatformCode = verifyPlatformCode;
		this.verifyPlatformName = DERP.getLabelByKey(DERP.storePlatformList, verifyPlatformCode);
	}

	public String getVerifyPlatformName() {
		return verifyPlatformName;
	}

	public void setVerifyPlatformName(String verifyPlatformName) {
		this.verifyPlatformName = verifyPlatformName;
	}

	public BigDecimal getVerifiyAmount() {
		return verifiyAmount;
	}

	public void setVerifiyAmount(BigDecimal verifiyAmount) {
		this.verifiyAmount = verifiyAmount;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPlatformGoodsCode() {
		return platformGoodsCode;
	}

	public void setPlatformGoodsCode(String platformGoodsCode) {
		this.platformGoodsCode = platformGoodsCode;
	}
}
