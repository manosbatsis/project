package com.topideal.entity.dto.purchase;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.entity.dto.common.CustomsDeclareItemDTO;
import com.topideal.entity.dto.common.CustomsPackingDetailsDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 一线清关资料(预申报单)
 * 
 * @author lian_
 */
@ApiModel
public class FirstCustomsInfoDTO {

	// 卸货港
	@ApiModelProperty("卸货港")
	private String portDest;
	// 原产国
	@ApiModelProperty("原产国")
	private String country;
	// 境外发货人
	@ApiModelProperty("境外发货人")
	private String shipper;
	// 自编码
	@ApiModelProperty("自编码")
	private String code;
	// 乙方（卖方）
	@ApiModelProperty("乙方")
	private String secondParty;
	// 签订地点
	@ApiModelProperty("签订地点")
	private String signingAddr;
	// 合同号
	@ApiModelProperty("合同号")
	private String contractNo;
	// 签订日期
	@ApiModelProperty("签订日期")
	private Timestamp signingDate;
	// 装货港
	@ApiModelProperty("装货港")
	private String portLoading;
	// 收货人地址
	@ApiModelProperty("收货人地址")
	private String addresseeAddr;
	// 特殊操作要求
	@ApiModelProperty("特殊操作要求")
	private String requirement;
	// 装船时间
	@ApiModelProperty("装船时间")
	private Timestamp shipDate;
	// 包装
	@ApiModelProperty("包装")
	private String pack;
	// 乙方地址
	@ApiModelProperty("乙方地址")
	private String secondPartyAddr;
	// 预申报单id
	@ApiModelProperty("预申报单id")
	private Long declareOrderId;
	// 收货人
	@ApiModelProperty("收货人")
	private String addressee;
	// 甲方地址
	@ApiModelProperty("甲方地址")
	private String firstPartyAddr;
	// 甲方英文地址
	@ApiModelProperty("甲方英文地址")
	private String firstPartyEnAddr;
	// 甲方（买方）
	@ApiModelProperty("甲方")
	private String firstParty;
	// 付款方式
	@ApiModelProperty("付款方式")
	private String payment;
	// id
	@ApiModelProperty("id")
	private Long id;
	// 付款条约
	@ApiModelProperty("付款条约")
	private String payRules;
	// 唛头
	@ApiModelProperty("唛头")
	private String mark;
	// 原产地
	@ApiModelProperty("原产地")
	private String countryOrigin;
	// 发票号
	@ApiModelProperty("发票号")
	private String invoiceNo;
	// 创建人
	@ApiModelProperty("创建人")
	private Long creater;
	// 修改日期
	@ApiModelProperty("修改日期")
	private Timestamp modifyDate;
	// 创建时间
	@ApiModelProperty("创建时间")
	private Timestamp createDate;
	// 修改人
	@ApiModelProperty("修改人")
	private Long modifier;
	// 英文收货地址
	@ApiModelProperty("英文收货地址")
	private String eAddresseeAddr;
	// 英文收货人
	@ApiModelProperty("英文收货人")
	private String eAddressee;
	// 发票date时间
	@ApiModelProperty("发票date时间")
	private Timestamp invoiceDate;	
	// 境外收货人
	@ApiModelProperty("境外收货人")
	private String consignee;
	@ApiModelProperty("运输方式")
	private String transportation;
	@ApiModelProperty("托盘材质")
	private String palletMaterial;
	@ApiModelProperty("提单号")
	private String ladingBillNo;
	@ApiModelProperty("签订编号")
	private String signNo;
	@ApiModelProperty("托数")
	private Integer torrNum;
	@ApiModelProperty("目的地")
	private String destination;
	@ApiModelProperty("目的地字母拼音")
	private String destinationPY;

	@ApiModelProperty("报关合同号")
	private String customsContractNo;
	@ApiModelProperty("PO号")
	private String poNo;
	@ApiModelProperty("订单日期")
	private Timestamp orderDate;
	// 乙方联系方式（卖方）
	@ApiModelProperty("乙方联系方式")
	private String secondPartyTelephone;

	// 客户
	@ApiModelProperty("客户")
	private String customerName;

	// 当前公司主体英文名称
	@ApiModelProperty("当前公司主体英文名称")
	private String merchantEnName;
	
	@ApiModelProperty("箱数")
	private Integer boxNum;
	
	@ApiModelProperty("总数量")
	private Integer totalNum;
	
	 // 毛重
    @ApiModelProperty("总毛重")
    private Double totalGrossWeight;
    // 净重
    @ApiModelProperty("总净重")
    private Double totalNetWeight;
    
    @ApiModelProperty("总价")
    private BigDecimal totalAmount;
    
    @ApiModelProperty("美元总价")
    private BigDecimal totalUsAmount;
 
 	@ApiModelProperty("发货日期")
 	private Timestamp deliverDate;
 	
 	@ApiModelProperty("装运日期")
 	private Timestamp loadingDate;
 	
 	@ApiModelProperty("LBX单号")
 	private String lbxNo;

	@ApiModelProperty("总箱数")
	private Integer totalBoxNum;
	
	@ApiModelProperty("邮箱")
	private String email;
	
	@ApiModelProperty("店铺名称")
	private String shopName;

	@ApiModelProperty("品牌名称")
	private String brands;
	
	@ApiModelProperty("目的港")
	private String destinationPort;
	
	@ApiModelProperty("标准箱TEU")
	private String teu;
	// 商品
	@ApiModelProperty("表体明细")
	private List<CustomsDeclareItemDTO> itemList;
	
	@ApiModelProperty("电商账册号")
	private String onlineRegisterNo;
	
	@ApiModelProperty("启运港")
	private String departurePort;

	@ApiModelProperty("公司主体的名称全称")
	private String merchantName;

	@ApiModelProperty(value = "关区代码")
	private String customsAreaCode;

	@ApiModelProperty(value="出库地点")
	private String deliveryAddr;

	//箱装明细
	@ApiModelProperty("箱装明细")
	private List<CustomsPackingDetailsDTO> detailsDTOList;

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

	public List<CustomsDeclareItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<CustomsDeclareItemDTO> itemList) {
		this.itemList = itemList;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getTransportation() {
		return transportation;
	}

	public void setTransportation(String transportation) {
		this.transportation = transportation;
	}

	public String getPalletMaterial() {
		return palletMaterial;
	}

	public void setPalletMaterial(String palletMaterial) {
		this.palletMaterial = palletMaterial;
	}

	public String getLadingBillNo() {
		return ladingBillNo;
	}

	public void setLadingBillNo(String ladingBillNo) {
		this.ladingBillNo = ladingBillNo;
	}

	public String getSignNo() {
		return signNo;
	}

	public void setSignNo(String signNo) {
		this.signNo = signNo;
	}

	public Integer getTorrNum() {
		return torrNum;
	}

	public void setTorrNum(Integer torrNum) {
		this.torrNum = torrNum;
	}

	public List<CustomsPackingDetailsDTO> getDetailsDTOList() {
		return detailsDTOList;
	}

	public void setDetailsDTOList(List<CustomsPackingDetailsDTO> detailsDTOList) {
		this.detailsDTOList = detailsDTOList;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCustomsContractNo() {
		return customsContractNo;
	}

	public void setCustomsContractNo(String customsContractNo) {
		this.customsContractNo = customsContractNo;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	public String getSecondPartyTelephone() {
		return secondPartyTelephone;
	}

	public void setSecondPartyTelephone(String secondPartyTelephone) {
		this.secondPartyTelephone = secondPartyTelephone;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getMerchantEnName() {
		return merchantEnName;
	}

	public void setMerchantEnName(String merchantEnName) {
		this.merchantEnName = merchantEnName;
	}

	public String getDestinationPY() {
		return destinationPY;
	}

	public void setDestinationPY(String destinationPY) {
		this.destinationPY = destinationPY;
	}

	public String getFirstPartyEnAddr() {
		return firstPartyEnAddr;
	}

	public void setFirstPartyEnAddr(String firstPartyEnAddr) {
		this.firstPartyEnAddr = firstPartyEnAddr;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	public Integer getTotalNum() {
		return totalNum;
	}

	public Double getTotalGrossWeight() {
		return totalGrossWeight;
	}

	public void setTotalGrossWeight(Double totalGrossWeight) {
		this.totalGrossWeight = totalGrossWeight;
	}

	public Double getTotalNetWeight() {
		return totalNetWeight;
	}

	public void setTotalNetWeight(Double totalNetWeight) {
		this.totalNetWeight = totalNetWeight;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalUsAmount() {
		return totalUsAmount;
	}

	public void setTotalUsAmount(BigDecimal totalUsAmount) {
		this.totalUsAmount = totalUsAmount;
	}

	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	public Timestamp getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public Timestamp getLoadingDate() {
		return loadingDate;
	}

	public void setLoadingDate(Timestamp loadingDate) {
		this.loadingDate = loadingDate;
	}

	public Integer getTotalBoxNum() {
		return totalBoxNum;
	}

	public void setTotalBoxNum(Integer totalBoxNum) {
		this.totalBoxNum = totalBoxNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getBrands() {
		return brands;
	}

	public void setBrands(String brands) {
		this.brands = brands;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getTeu() {
		return teu;
	}

	public void setTeu(String teu) {
		this.teu = teu;
	}

	public String getOnlineRegisterNo() {
		return onlineRegisterNo;
	}

	public void setOnlineRegisterNo(String onlineRegisterNo) {
		this.onlineRegisterNo = onlineRegisterNo;
	}

	public String getDeparturePort() {
		return departurePort;
	}

	public void setDeparturePort(String departurePort) {
		this.departurePort = departurePort;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCustomsAreaCode() {
		return customsAreaCode;
	}

	public void setCustomsAreaCode(String customsAreaCode) {
		this.customsAreaCode = customsAreaCode;
	}

	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}
}
