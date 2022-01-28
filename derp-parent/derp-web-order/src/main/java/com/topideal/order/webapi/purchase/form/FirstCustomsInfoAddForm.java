package com.topideal.order.webapi.purchase.form;

import java.sql.Timestamp;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 一线清关资料(预申报单)
 * 
 * @author gy
 */

@ApiModel
public class FirstCustomsInfoAddForm {

	@ApiModelProperty(value="令牌", required=true)
    private String token;
	// 卸货港
	@ApiModelProperty(value = "卸货港", required = false)
	private String portDest;
	// 原产国
	@ApiModelProperty(value = "原产国", required = false)
	private String country;
	// 境外发货人
	@ApiModelProperty(value = "境外发货人", required = false)
	private String shipper;
	// 自编码
	@ApiModelProperty(value = "自编码", required = false)
	private String code;
	// 乙方
	@ApiModelProperty(value = "乙方", required = false)
	private String secondParty;
	// 签订地点
	@ApiModelProperty(value = "签订地点", required = false)
	private String signingAddr;
	// 合同号
	@ApiModelProperty(value = "合同号", required = false)
	private String contractNo;
	// 签订日期
	@ApiModelProperty(value = "签订日期", required = false)
	private Timestamp signingDate;
	// 装货港
	@ApiModelProperty(value = "装货港", required = false)
	private String portLoading;
	// 收货人地址
	@ApiModelProperty(value = "收货人地址", required = false)
	private String addresseeAddr;
	// 特殊操作要求
	@ApiModelProperty(value = "特殊操作要求", required = false)
	private String requirement;
	// 装船时间
	@ApiModelProperty(value = "装船时间", required = false)
	private Timestamp shipDate;
	// 包装
	@ApiModelProperty(value = "包装", required = false)
	private String pack;
	// 乙方地址
	@ApiModelProperty(value = "乙方地址", required = false)
	private String secondPartyAddr;
	// 预申报单id
	@ApiModelProperty(value = "预申报单id", required = false)
	private Long declareOrderId;
	// 收货人
	@ApiModelProperty(value = "收货人", required = false)
	private String addressee;
	// 甲方地址
	@ApiModelProperty(value = "甲方地址", required = false)
	private String firstPartyAddr;
	// 甲方
	@ApiModelProperty(value = "甲方", required = false)
	private String firstParty;
	// 付款方式
	@ApiModelProperty(value = "付款方式", required = false)
	private String payment;
	// id
	@ApiModelProperty(value = "一线清关资料ID", required = false)
	private Long id;
	// 付款条约
	@ApiModelProperty(value = "付款条约", required = false)
	private String payRules;
	// 唛头
	@ApiModelProperty(value = "唛头", required = false)
	private String mark;
	// 原产地
	@ApiModelProperty(value = "原产地", required = false)
	private String countryOrigin;
	// 发票号
	@ApiModelProperty(value = "发票号", required = false)
	private String invoiceNo;
	// 英文收货地址
	@ApiModelProperty(value = "英文收货地址", required = false)
	private String eAddresseeAddr;
	// 英文收货人
	@ApiModelProperty(value = "英文收货人", required = false)
	private String eAddressee;
	// 发票date时间
	@ApiModelProperty(value = "发票date时间", required = false)
	private Timestamp invoiceDate;
	// 商品ID
	@ApiModelProperty(value = "商品ID，多个以','隔开", required = false)
	private String goodIds ;
	// 托盘号
	@ApiModelProperty(value = "托盘号，多个以','隔开", required = false)
	private String palletNos ;
	// 箱数
	@ApiModelProperty(value = "箱数，多个以','隔开", required = false)
	private String carton ; 
	// 签订日期字符串
	@ApiModelProperty(value = "签订日期字符串", required = false)
	private String signingDateStr ;
	// 发票时间字符串
	@ApiModelProperty(value = "发票时间字符串", required = false)
	private String invoiceDateStr ;
	// 装船时间字符串
	@ApiModelProperty(value = "装船时间字符串", required = false)
	private String shipDateStr ;

	public String geteAddresseeAddr() {
		return eAddresseeAddr;
	}

	public void seteAddresseeAddr(String eAddresseeAddr) {
		this.eAddresseeAddr = eAddresseeAddr;
	}

	public String geteAddressee() {
		return eAddressee;
	}

	public void seteAddressee(String eAddressee) {
		this.eAddressee = eAddressee;
	}

	public Timestamp getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Timestamp invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/* countryOrigin get 方法 */
	public String getCountryOrigin() {
		return countryOrigin;
	}

	/* countryOrigin set 方法 */
	public void setCountryOrigin(String countryOrigin) {
		this.countryOrigin = countryOrigin;
	}

	/* portDest get 方法 */
	public String getPortDest() {
		return portDest;
	}

	/* portDest set 方法 */
	public void setPortDest(String portDest) {
		this.portDest = portDest;
	}

	/* country get 方法 */
	public String getCountry() {
		return country;
	}

	/* country set 方法 */
	public void setCountry(String country) {
		this.country = country;
	}

	/* shipper get 方法 */
	public String getShipper() {
		return shipper;
	}

	/* shipper set 方法 */
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* secondParty get 方法 */
	public String getSecondParty() {
		return secondParty;
	}

	/* secondParty set 方法 */
	public void setSecondParty(String secondParty) {
		this.secondParty = secondParty;
	}

	/* signingAddr get 方法 */
	public String getSigningAddr() {
		return signingAddr;
	}

	/* signingAddr set 方法 */
	public void setSigningAddr(String signingAddr) {
		this.signingAddr = signingAddr;
	}

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* signingDate get 方法 */
	public Timestamp getSigningDate() {
		return signingDate;
	}

	/* signingDate set 方法 */
	public void setSigningDate(Timestamp signingDate) {
		this.signingDate = signingDate;
	}

	/* portLoading get 方法 */
	public String getPortLoading() {
		return portLoading;
	}

	/* portLoading set 方法 */
	public void setPortLoading(String portLoading) {
		this.portLoading = portLoading;
	}

	/* addresseeAddr get 方法 */
	public String getAddresseeAddr() {
		return addresseeAddr;
	}

	/* addresseeAddr set 方法 */
	public void setAddresseeAddr(String addresseeAddr) {
		this.addresseeAddr = addresseeAddr;
	}

	/* requirement get 方法 */
	public String getRequirement() {
		return requirement;
	}

	/* requirement set 方法 */
	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	/* shipDate get 方法 */
	public Timestamp getShipDate() {
		return shipDate;
	}

	/* shipDate set 方法 */
	public void setShipDate(Timestamp shipDate) {
		this.shipDate = shipDate;
	}

	/* pack get 方法 */
	public String getPack() {
		return pack;
	}

	/* pack set 方法 */
	public void setPack(String pack) {
		this.pack = pack;
	}

	/* secondPartyAddr get 方法 */
	public String getSecondPartyAddr() {
		return secondPartyAddr;
	}

	/* secondPartyAddr set 方法 */
	public void setSecondPartyAddr(String secondPartyAddr) {
		this.secondPartyAddr = secondPartyAddr;
	}

	/* declareOrderId get 方法 */
	public Long getDeclareOrderId() {
		return declareOrderId;
	}

	/* declareOrderId set 方法 */
	public void setDeclareOrderId(Long declareOrderId) {
		this.declareOrderId = declareOrderId;
	}

	/* addressee get 方法 */
	public String getAddressee() {
		return addressee;
	}

	/* addressee set 方法 */
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}

	/* firstPartyAddr get 方法 */
	public String getFirstPartyAddr() {
		return firstPartyAddr;
	}

	/* firstPartyAddr set 方法 */
	public void setFirstPartyAddr(String firstPartyAddr) {
		this.firstPartyAddr = firstPartyAddr;
	}

	/* firstParty get 方法 */
	public String getFirstParty() {
		return firstParty;
	}

	/* firstParty set 方法 */
	public void setFirstParty(String firstParty) {
		this.firstParty = firstParty;
	}

	/* payment get 方法 */
	public String getPayment() {
		return payment;
	}

	/* payment set 方法 */
	public void setPayment(String payment) {
		this.payment = payment;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* payRules get 方法 */
	public String getPayRules() {
		return payRules;
	}

	/* payRules set 方法 */
	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}

	/* mark get 方法 */
	public String getMark() {
		return mark;
	}

	/* mark set 方法 */
	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getGoodIds() {
		return goodIds;
	}

	public void setGoodIds(String goodIds) {
		this.goodIds = goodIds;
	}

	public String getPalletNos() {
		return palletNos;
	}

	public void setPalletNos(String palletNos) {
		this.palletNos = palletNos;
	}

	public String getCarton() {
		return carton;
	}

	public void setCarton(String carton) {
		this.carton = carton;
	}

	public String getSigningDateStr() {
		return signingDateStr;
	}

	public void setSigningDateStr(String signingDateStr) {
		this.signingDateStr = signingDateStr;
	}

	public String getInvoiceDateStr() {
		return invoiceDateStr;
	}

	public void setInvoiceDateStr(String invoiceDateStr) {
		this.invoiceDateStr = invoiceDateStr;
	}

	public String getShipDateStr() {
		return shipDateStr;
	}

	public void setShipDateStr(String shipDateStr) {
		this.shipDateStr = shipDateStr;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
