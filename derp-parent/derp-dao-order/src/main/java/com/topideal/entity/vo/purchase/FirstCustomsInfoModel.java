package com.topideal.entity.vo.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 一线清关资料(预申报单)
 * 
 * @author lian_
 */
public class FirstCustomsInfoModel extends PageModel implements Serializable {

	// 卸货港
	private String portDest;
	// 原产国
	private String country;
	// 境外发货人
	private String shipper;
	// 自编码
	private String code;
	// 乙方
	private String secondParty;
	// 签订地点
	private String signingAddr;
	// 合同号
	private String contractNo;
	// 签订日期
	private Timestamp signingDate;
	// 装货港
	private String portLoading;
	// 收货人地址
	private String addresseeAddr;
	// 特殊操作要求
	private String requirement;
	// 装船时间
	private Timestamp shipDate;
	// 包装
	private String pack;
	// 乙方地址
	private String secondPartyAddr;
	// 预申报单id
	private Long declareOrderId;
	// 收货人
	private String addressee;
	// 甲方地址
	private String firstPartyAddr;
	// 甲方
	private String firstParty;
	// 付款方式
	private String payment;
	// id
	private Long id;
	// 付款条约
	private String payRules;
	// 唛头
	private String mark;
	// 原产地
	private String countryOrigin;
	// 发票号
	private String invoiceNo;
	// 创建人
	private Long creater;
	// 修改日期
	private Timestamp modifyDate;
	// 创建时间
	private Timestamp createDate;
	// 修改人
	private Long modifier;
	// 英文收货地址
	private String eAddresseeAddr;
	// 英文收货人
	private String eAddressee;
	// 发票date时间
	private Timestamp invoiceDate;

	// 商品
	private List<DeclareOrderItemModel> itemList;

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

	public List<DeclareOrderItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<DeclareOrderItemModel> itemList) {
		this.itemList = itemList;
	}

	/* modifier get 方法 */
	public Long getModifier() {
		return modifier;
	}

	/* modifier set 方法 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
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

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
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

}
