package com.topideal.order.webapi.purchase.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PurchaseContractAddForm {

	@ApiModelProperty(value = "令牌", required = true)
	private String token;
	/**
	 * 自增ID
	 */
	@ApiModelProperty(value = "采购合同记录ID")
	private Long id;
	/**
	 * 采购订单ID
	 */
	@ApiModelProperty(value = "采购订单ID")
	private Long orderId;
	/**
	 * 采购合同编号
	 */
	@ApiModelProperty(value = "采购合同编号")
	private String purchaseContractNo;
	/**
	 * 采购订单号(采购单-PO号字段)
	 */
	@ApiModelProperty(value = "采购订单号(采购单-PO号字段)")
	private String purchaseOrderNo;
	/**
	 * 公司信息-英文名称
	 */
	@ApiModelProperty(value = "公司信息-英文名称")
	private String buyerEnName;
	/**
	 * 公司信息-中文名称
	 */
	@ApiModelProperty(value = "公司信息-中文名称")
	private String buyerCnName;
	/**
	 * 供应商信息-英文名
	 */
	@ApiModelProperty(value = "供应商信息-英文名")
	private String sellerEnName;
	/**
	 * 供应商信息-全称
	 */
	@ApiModelProperty(value = "供应商信息-全称")
	private String sellerCnName;
	/**
	 * 运输方式
	 */
	@ApiModelProperty(value = "运输方式")
	private String meansOfTransportation;
	/**
	 * 始发地（港）
	 */
	@ApiModelProperty(value = "始发地（港）")
	private String loadingCnPort;
	/**
	 * 始发地（港）英文
	 */
	@ApiModelProperty(value = "始发地（港）英文")
	private String loadingEnPort;
	/**
	 * 目的地（港）
	 */
	@ApiModelProperty(value = "目的地（港）")
	private String destinationdCn;
	/**
	 * 目的地（港）英
	 */
	@ApiModelProperty(value = "目的地（港）英")
	private String destinationdEn;
	/**
	 * 交货日期
	 */
	@ApiModelProperty(value = "交货日期字符串")
	private String deliveryDateStr;
	/**
	 * 付款方式1-一次结清 2-分批付款 3- 其他
	 */
	@ApiModelProperty(value = "付款方式1-一次结清 2-分批付款 3- 其他")
	private String paymentMethod;
	/**
	 * 付款方式文案
	 */
	@ApiModelProperty(value = "付款方式文案")
	private String paymentMethodText;
	/**
	 * 特别约定
	 */
	@ApiModelProperty(value = "特别约定")
	private String specialAgreement;
	/**
	 * 账号
	 */
	@ApiModelProperty(value = "账号")
	private String accountNo;
	/**
	 * 采购币种
	 */
	@ApiModelProperty(value = "采购币种")
	private String currency;
	/**
	 * 账户
	 */
	@ApiModelProperty(value = "账户")
	private String beneficiaryName;
	/**
	 * 开户行
	 */
	@ApiModelProperty(value = "开户行")
	private String beneficiaryBankName;
	/**
	 * 地址
	 */
	@ApiModelProperty(value = "地址")
	private String bankAddress;
	/**
	 * Swift Code
	 */
	@ApiModelProperty(value = "Swift Code")
	private String swiftCode;
	/**
	 * 甲方授权代表签字（英）
	 */
	@ApiModelProperty(value = "甲方授权代表签字（英）")
	private String buyerAuthorizedSignatureEn;
	/**
	 * 甲方授权代表签字
	 */
	@ApiModelProperty(value = "甲方授权代表签字")
	private String buyerAuthorizedSignature;
	/**
	 * 乙方授权代表签字（英）
	 */
	@ApiModelProperty(value = "乙方授权代表签字（英）")
	private String sellerAuthorizedSignatureEn;
	/**
	 * 乙方授权代表签字
	 */
	@ApiModelProperty(value = "乙方授权代表签字")
	private String sellerAuthorizedSignature;
	// 特别约定英文
	@ApiModelProperty(value = "特别约定英文")
	private String specialAgreementEn;

	// 付款方式文案
	@ApiModelProperty(value = "付款方式文案")
	private String paymentMethodTextEn;

	/**
	 * 发货地址（英）
	 */
	@ApiModelProperty(value = "发货地址（英）")
	private String deliveryAddressEn;
	/**
	 * 发货地址
	 */
	@ApiModelProperty(value = "发货地址")
	private String deliveryAddress;
	/**
	 * 提货地址(英)
	 */
	@ApiModelProperty(value = "提货地址(英)")
	private String pickingUpAddressEn;
	/**
	 * 提货地址
	 */
	@ApiModelProperty(value = "提货地址")
	private String pickingUpAddress;

	/**
	 * 预计付款日期
	 */
	@ApiModelProperty(value = "预计付款日期")
	private String paymentDateStr;


	public String getPaymentDateStr() {
		return paymentDateStr;
	}

	public void setPaymentDateStr(String paymentDateStr) {
		this.paymentDateStr = paymentDateStr;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* orderId get 方法 */
	public Long getOrderId() {
		return orderId;
	}

	/* orderId set 方法 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/* purchaseContractNo get 方法 */
	public String getPurchaseContractNo() {
		return purchaseContractNo;
	}

	/* purchaseContractNo set 方法 */
	public void setPurchaseContractNo(String purchaseContractNo) {
		this.purchaseContractNo = purchaseContractNo;
	}

	/* purchaseOrderNo get 方法 */
	public String getPurchaseOrderNo() {
		return purchaseOrderNo;
	}

	/* purchaseOrderNo set 方法 */
	public void setPurchaseOrderNo(String purchaseOrderNo) {
		this.purchaseOrderNo = purchaseOrderNo;
	}

	/* buyerEnName get 方法 */
	public String getBuyerEnName() {
		return buyerEnName;
	}

	/* buyerEnName set 方法 */
	public void setBuyerEnName(String buyerEnName) {
		this.buyerEnName = buyerEnName;
	}

	/* buyerCnName get 方法 */
	public String getBuyerCnName() {
		return buyerCnName;
	}

	/* buyerCnName set 方法 */
	public void setBuyerCnName(String buyerCnName) {
		this.buyerCnName = buyerCnName;
	}

	/* sellerEnName get 方法 */
	public String getSellerEnName() {
		return sellerEnName;
	}

	/* sellerEnName set 方法 */
	public void setSellerEnName(String sellerEnName) {
		this.sellerEnName = sellerEnName;
	}

	/* sellerCnName get 方法 */
	public String getSellerCnName() {
		return sellerCnName;
	}

	/* sellerCnName set 方法 */
	public void setSellerCnName(String sellerCnName) {
		this.sellerCnName = sellerCnName;
	}

	/* meansOfTransportation get 方法 */
	public String getMeansOfTransportation() {
		return meansOfTransportation;
	}

	/* meansOfTransportation set 方法 */
	public void setMeansOfTransportation(String meansOfTransportation) {
		this.meansOfTransportation = meansOfTransportation;
	}

	/* loadingCnPort get 方法 */
	public String getLoadingCnPort() {
		return loadingCnPort;
	}

	/* loadingCnPort set 方法 */
	public void setLoadingCnPort(String loadingCnPort) {
		this.loadingCnPort = loadingCnPort;
	}

	/* loadingEnPort get 方法 */
	public String getLoadingEnPort() {
		return loadingEnPort;
	}

	/* loadingEnPort set 方法 */
	public void setLoadingEnPort(String loadingEnPort) {
		this.loadingEnPort = loadingEnPort;
	}

	/* destinationdCn get 方法 */
	public String getDestinationdCn() {
		return destinationdCn;
	}

	/* destinationdCn set 方法 */
	public void setDestinationdCn(String destinationdCn) {
		this.destinationdCn = destinationdCn;
	}

	/* destinationdEn get 方法 */
	public String getDestinationdEn() {
		return destinationdEn;
	}

	/* destinationdEn set 方法 */
	public void setDestinationdEn(String destinationdEn) {
		this.destinationdEn = destinationdEn;
	}

	/* deliveryDate get 方法 */
	public String getDeliveryDateStr() {
		return deliveryDateStr;
	}

	/* deliveryDate set 方法 */
	public void setDeliveryDateStr(String deliveryDateStr) {
		this.deliveryDateStr = deliveryDateStr;
	}

	/* paymentMethod get 方法 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/* paymentMethod set 方法 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/* paymentMethodText get 方法 */
	public String getPaymentMethodText() {
		return paymentMethodText;
	}

	/* paymentMethodText set 方法 */
	public void setPaymentMethodText(String paymentMethodText) {
		this.paymentMethodText = paymentMethodText;
	}

	/* specialAgreement get 方法 */
	public String getSpecialAgreement() {
		return specialAgreement;
	}

	/* specialAgreement set 方法 */
	public void setSpecialAgreement(String specialAgreement) {
		this.specialAgreement = specialAgreement;
	}

	/* accountNo get 方法 */
	public String getAccountNo() {
		return accountNo;
	}

	/* accountNo set 方法 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	/* currency get 方法 */
	public String getCurrency() {
		return currency;
	}

	/* currency set 方法 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/* beneficiaryName get 方法 */
	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	/* beneficiaryName set 方法 */
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	/* beneficiaryBankName get 方法 */
	public String getBeneficiaryBankName() {
		return beneficiaryBankName;
	}

	/* beneficiaryBankName set 方法 */
	public void setBeneficiaryBankName(String beneficiaryBankName) {
		this.beneficiaryBankName = beneficiaryBankName;
	}

	/* bankAddress get 方法 */
	public String getBankAddress() {
		return bankAddress;
	}

	/* bankAddress set 方法 */
	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	/* swiftCode get 方法 */
	public String getSwiftCode() {
		return swiftCode;
	}

	/* swiftCode set 方法 */
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	/* buyerAuthorizedSignatureEn get 方法 */
	public String getBuyerAuthorizedSignatureEn() {
		return buyerAuthorizedSignatureEn;
	}

	/* buyerAuthorizedSignatureEn set 方法 */
	public void setBuyerAuthorizedSignatureEn(String buyerAuthorizedSignatureEn) {
		this.buyerAuthorizedSignatureEn = buyerAuthorizedSignatureEn;
	}

	/* buyerAuthorizedSignature get 方法 */
	public String getBuyerAuthorizedSignature() {
		return buyerAuthorizedSignature;
	}

	/* buyerAuthorizedSignature set 方法 */
	public void setBuyerAuthorizedSignature(String buyerAuthorizedSignature) {
		this.buyerAuthorizedSignature = buyerAuthorizedSignature;
	}

	/* sellerAuthorizedSignatureEn get 方法 */
	public String getSellerAuthorizedSignatureEn() {
		return sellerAuthorizedSignatureEn;
	}

	/* sellerAuthorizedSignatureEn set 方法 */
	public void setSellerAuthorizedSignatureEn(String sellerAuthorizedSignatureEn) {
		this.sellerAuthorizedSignatureEn = sellerAuthorizedSignatureEn;
	}

	/* sellerAuthorizedSignature get 方法 */
	public String getSellerAuthorizedSignature() {
		return sellerAuthorizedSignature;
	}

	/* sellerAuthorizedSignature set 方法 */
	public void setSellerAuthorizedSignature(String sellerAuthorizedSignature) {
		this.sellerAuthorizedSignature = sellerAuthorizedSignature;
	}

	public String getSpecialAgreementEn() {
		return specialAgreementEn;
	}

	public void setSpecialAgreementEn(String specialAgreementEn) {
		this.specialAgreementEn = specialAgreementEn;
	}

	public String getPaymentMethodTextEn() {
		return paymentMethodTextEn;
	}

	public void setPaymentMethodTextEn(String paymentMethodTextEn) {
		this.paymentMethodTextEn = paymentMethodTextEn;
	}

	public String getDeliveryAddressEn() {
		return deliveryAddressEn;
	}

	public void setDeliveryAddressEn(String deliveryAddressEn) {
		this.deliveryAddressEn = deliveryAddressEn;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public String getPickingUpAddressEn() {
		return pickingUpAddressEn;
	}

	public void setPickingUpAddressEn(String pickingUpAddressEn) {
		this.pickingUpAddressEn = pickingUpAddressEn;
	}

	public String getPickingUpAddress() {
		return pickingUpAddress;
	}

	public void setPickingUpAddress(String pickingUpAddress) {
		this.pickingUpAddress = pickingUpAddress;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
