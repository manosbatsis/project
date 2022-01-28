package com.topideal.order.webapi.sale.form;
import java.util.List;

import com.topideal.common.system.webapi.PageForm;
import com.topideal.entity.dto.sale.SaleDeclareOrderItemDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleDeclareOrderForm extends PageForm{
	
	@ApiModelProperty(value = "令牌",required = true)
	private String token; 

	@ApiModelProperty(value="id")
    private Long id;

	@ApiModelProperty(value="销售预申报单编号")
    private String code;

	@ApiModelProperty(value="客户ID")
    private Long customerId;

	@ApiModelProperty(value="客户名称")
    private String customerName;

	@ApiModelProperty(value="po单号")
    private String poNo;

	@ApiModelProperty(value="出库仓ID")
    private Long outDepotId;

	@ApiModelProperty(value="出库仓名称")
    private String outDepotName;

	@ApiModelProperty(value="事业部名称")
    private String buName;

	@ApiModelProperty(value="事业部id")
    private Long buId;

	@ApiModelProperty(value="LBX单号")
    private String lbxNo;

	@ApiModelProperty(value="入仓仓库id")
    private Long inDepotId;

	@ApiModelProperty(value="入仓仓库名称")
    private String inDepotName;

	@ApiModelProperty(value="贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW")
    private String tradeTerms;

	@ApiModelProperty(value="币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;

	@ApiModelProperty(value="理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;

	@ApiModelProperty(value="装货港")
    private String loadPort;

	@ApiModelProperty(value="目的地名称")
    private String destination;

	@ApiModelProperty(value="目的港名称")
    private String destinationPort;

	@ApiModelProperty(value="卸货港")
    private String portDes;

	@ApiModelProperty(value="出库地点")
    private String deliveryAddr;

	@ApiModelProperty(value="进出口口岸")
    private String imExpPort;

	@ApiModelProperty(value="出库关区id")
    private Long outCustomsId;

	@ApiModelProperty(value="出库关区名称")
    private String outCustomsName;

	@ApiModelProperty(value="入库关区id")
    private Long inCustomsId;

	@ApiModelProperty(value="入库关区名称")
    private String inCustomsName;

	@ApiModelProperty(value="启运港")
    private String departurePort;

	@ApiModelProperty(value="装船时间")
    private String shipDate;

	@ApiModelProperty(value="备注")
    private String remark;

	@ApiModelProperty(value="运输方式 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他")
    private String transport;

	@ApiModelProperty(value="境外发货人")
    private String shipper;

	@ApiModelProperty(value="承运商")
    private String carrier;

	@ApiModelProperty(value="发票单号")
    private String invoiceNo;

	@ApiModelProperty(value="报关合同号")
    private String contractNo;

	@ApiModelProperty(value="头程提单号")
    private String ladingBill;

	@ApiModelProperty(value="二程提单号")
    private String blNo;

	@ApiModelProperty(value="车次")
    private String trainNo;

	@ApiModelProperty(value="托数")
    private Integer torrNum;

	@ApiModelProperty(value="包装")
    private String pack;

	@ApiModelProperty(value="箱数")
    private Integer boxNum;

	@ApiModelProperty(value="托盘材质 01-塑料托盘 02-木质托盘 03-IPPC木托 04-纸箱")
    private String torrPackType;

	@ApiModelProperty(value="提单毛重")
    private Double billWeight;

	@ApiModelProperty(value="车型 及 数量")
    private String motorcycleType;

	@ApiModelProperty(value="唛头")
    private String mark;

	@ApiModelProperty(value="标准箱TEU")
    private String teu;

	@ApiModelProperty(value="订单状态 001-待打托 002待清关 003-待物流委托 004-已出库 005-部分上架 007-已上架 006已删除")
    private String status;

	@ApiModelProperty(value="接口状态 0-未推接口；1-已推接口；2-接口推送失败 用于海外仓推跨境宝")
    private String apiStatus;	

	@ApiModelProperty(value="出库时间")
    private String deliverDate;

	@ApiModelProperty(value="国家")
    private String country;

	@ApiModelProperty(value="收件人名称")
    private String receiverName;

	@ApiModelProperty(value="收件人地址")
    private String receiverAddress;

	@ApiModelProperty(value="邮寄方式 1.寄售 2.自提")
    private String mailMode;

	@ApiModelProperty(value="销售订单ID集合")
    private String saleOrderIds;

	@ApiModelProperty(value="销售订单编号集合")
    private String saleOrderCodes;
	
	@ApiModelProperty(value = "销售预申报id集合")
	private String ids;

	@ApiModelProperty("表体信息")
    private List<SaleDeclareOrderItemDTO> itemList;
	
	@ApiModelProperty("物流联系人模版ID")
    private Long logisticsContactTemplateId;

	@ApiModelProperty("物流联系人模版名称")
    private String logisticsContactTemplateLinkName;
 
	@ApiModelProperty("发货人/提货信息模版ID")
    private Long shipperTempId;

	@ApiModelProperty("发货人/提货信息模版名称")
    private String shipperTempName;
	
	@ApiModelProperty("发货人/提货信息模版详情")
    private String shipperTempDetails;

	@ApiModelProperty("收货人/卸货信息模版ID")
    private Long consigneeTempId;

	@ApiModelProperty("收货人/卸货信息模版名称")
    private String consigneeTempName;
	
	@ApiModelProperty("收货人/卸货信息模版详情")
	private String consigneeTempDetails;

	@ApiModelProperty("通知人模版ID")
    private Long notifierTempId;

	@ApiModelProperty("通知人模版名")
    private String notifierTempName;
	
	@ApiModelProperty("通知人模版详情")
    private String notifierTempDetails;

	@ApiModelProperty("对接人模版ID")
    private Long dockingTempId;

	@ApiModelProperty("对接人模版名")
    private String dockingTempName;
	
	@ApiModelProperty("对接人模版详情")
    private String dockingTempDetails;

	@ApiModelProperty("承运商信息模版ID")
    private Long carrierInformationTempId;

	@ApiModelProperty("承运商信息模版名")
    private String carrierInformationTempName;
	
	@ApiModelProperty("承运商信息模版详情")
	private String carrierInformationTempDetails;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public String getOutDepotName() {
		return outDepotName;
	}

	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public Long getInDepotId() {
		return inDepotId;
	}

	public void setInDepotId(Long inDepotId) {
		this.inDepotId = inDepotId;
	}

	public String getInDepotName() {
		return inDepotName;
	}

	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getLoadPort() {
		return loadPort;
	}

	public void setLoadPort(String loadPort) {
		this.loadPort = loadPort;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestinationPort() {
		return destinationPort;
	}

	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}

	public String getPortDes() {
		return portDes;
	}

	public void setPortDes(String portDes) {
		this.portDes = portDes;
	}

	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	public String getImExpPort() {
		return imExpPort;
	}

	public void setImExpPort(String imExpPort) {
		this.imExpPort = imExpPort;
	}

	public Long getOutCustomsId() {
		return outCustomsId;
	}

	public void setOutCustomsId(Long outCustomsId) {
		this.outCustomsId = outCustomsId;
	}

	public String getOutCustomsName() {
		return outCustomsName;
	}

	public void setOutCustomsName(String outCustomsName) {
		this.outCustomsName = outCustomsName;
	}

	public Long getInCustomsId() {
		return inCustomsId;
	}

	public void setInCustomsId(Long inCustomsId) {
		this.inCustomsId = inCustomsId;
	}

	public String getInCustomsName() {
		return inCustomsName;
	}

	public void setInCustomsName(String inCustomsName) {
		this.inCustomsName = inCustomsName;
	}

	public String getDeparturePort() {
		return departurePort;
	}

	public void setDeparturePort(String departurePort) {
		this.departurePort = departurePort;
	}

	public String getShipDate() {
		return shipDate;
	}

	public void setShipDate(String shipDate) {
		this.shipDate = shipDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getLadingBill() {
		return ladingBill;
	}

	public void setLadingBill(String ladingBill) {
		this.ladingBill = ladingBill;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getTrainNo() {
		return trainNo;
	}

	public void setTrainNo(String trainNo) {
		this.trainNo = trainNo;
	}

	public Integer getTorrNum() {
		return torrNum;
	}

	public void setTorrNum(Integer torrNum) {
		this.torrNum = torrNum;
	}

	public String getPack() {
		return pack;
	}

	public void setPack(String pack) {
		this.pack = pack;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	public String getTorrPackType() {
		return torrPackType;
	}

	public void setTorrPackType(String torrPackType) {
		this.torrPackType = torrPackType;
	}

	public Double getBillWeight() {
		return billWeight;
	}

	public void setBillWeight(Double billWeight) {
		this.billWeight = billWeight;
	}

	public String getMotorcycleType() {
		return motorcycleType;
	}

	public void setMotorcycleType(String motorcycleType) {
		this.motorcycleType = motorcycleType;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getTeu() {
		return teu;
	}

	public void setTeu(String teu) {
		this.teu = teu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(String apiStatus) {
		this.apiStatus = apiStatus;
	}

	public String getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(String deliverDate) {
		this.deliverDate = deliverDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getMailMode() {
		return mailMode;
	}

	public void setMailMode(String mailMode) {
		this.mailMode = mailMode;
	}

	public String getSaleOrderIds() {
		return saleOrderIds;
	}

	public void setSaleOrderIds(String saleOrderIds) {
		this.saleOrderIds = saleOrderIds;
	}

	public String getSaleOrderCodes() {
		return saleOrderCodes;
	}

	public void setSaleOrderCodes(String saleOrderCodes) {
		this.saleOrderCodes = saleOrderCodes;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public List<SaleDeclareOrderItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleDeclareOrderItemDTO> itemList) {
		this.itemList = itemList;
	}

	public Long getLogisticsContactTemplateId() {
		return logisticsContactTemplateId;
	}

	public void setLogisticsContactTemplateId(Long logisticsContactTemplateId) {
		this.logisticsContactTemplateId = logisticsContactTemplateId;
	}

	public String getLogisticsContactTemplateLinkName() {
		return logisticsContactTemplateLinkName;
	}

	public void setLogisticsContactTemplateLinkName(String logisticsContactTemplateLinkName) {
		this.logisticsContactTemplateLinkName = logisticsContactTemplateLinkName;
	}

	public Long getShipperTempId() {
		return shipperTempId;
	}

	public void setShipperTempId(Long shipperTempId) {
		this.shipperTempId = shipperTempId;
	}

	public String getShipperTempName() {
		return shipperTempName;
	}

	public void setShipperTempName(String shipperTempName) {
		this.shipperTempName = shipperTempName;
	}

	public String getShipperTempDetails() {
		return shipperTempDetails;
	}

	public void setShipperTempDetails(String shipperTempDetails) {
		this.shipperTempDetails = shipperTempDetails;
	}

	public Long getConsigneeTempId() {
		return consigneeTempId;
	}

	public void setConsigneeTempId(Long consigneeTempId) {
		this.consigneeTempId = consigneeTempId;
	}

	public String getConsigneeTempName() {
		return consigneeTempName;
	}

	public void setConsigneeTempName(String consigneeTempName) {
		this.consigneeTempName = consigneeTempName;
	}

	public String getConsigneeTempDetails() {
		return consigneeTempDetails;
	}

	public void setConsigneeTempDetails(String consigneeTempDetails) {
		this.consigneeTempDetails = consigneeTempDetails;
	}

	public Long getNotifierTempId() {
		return notifierTempId;
	}

	public void setNotifierTempId(Long notifierTempId) {
		this.notifierTempId = notifierTempId;
	}

	public String getNotifierTempName() {
		return notifierTempName;
	}

	public void setNotifierTempName(String notifierTempName) {
		this.notifierTempName = notifierTempName;
	}

	public String getNotifierTempDetails() {
		return notifierTempDetails;
	}

	public void setNotifierTempDetails(String notifierTempDetails) {
		this.notifierTempDetails = notifierTempDetails;
	}

	public Long getDockingTempId() {
		return dockingTempId;
	}

	public void setDockingTempId(Long dockingTempId) {
		this.dockingTempId = dockingTempId;
	}

	public String getDockingTempName() {
		return dockingTempName;
	}

	public void setDockingTempName(String dockingTempName) {
		this.dockingTempName = dockingTempName;
	}

	public String getDockingTempDetails() {
		return dockingTempDetails;
	}

	public void setDockingTempDetails(String dockingTempDetails) {
		this.dockingTempDetails = dockingTempDetails;
	}

	public Long getCarrierInformationTempId() {
		return carrierInformationTempId;
	}

	public void setCarrierInformationTempId(Long carrierInformationTempId) {
		this.carrierInformationTempId = carrierInformationTempId;
	}

	public String getCarrierInformationTempName() {
		return carrierInformationTempName;
	}

	public void setCarrierInformationTempName(String carrierInformationTempName) {
		this.carrierInformationTempName = carrierInformationTempName;
	}

	public String getCarrierInformationTempDetails() {
		return carrierInformationTempDetails;
	}

	public void setCarrierInformationTempDetails(String carrierInformationTempDetails) {
		this.carrierInformationTempDetails = carrierInformationTempDetails;
	}

}
