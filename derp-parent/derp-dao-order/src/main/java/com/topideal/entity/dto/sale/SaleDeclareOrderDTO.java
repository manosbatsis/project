package com.topideal.entity.dto.sale;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class SaleDeclareOrderDTO extends PageModel implements Serializable{

	@ApiModelProperty(value="id")
    private Long id;

	@ApiModelProperty(value="销售预申报单编号")
    private String code;

	@ApiModelProperty(value="客户ID")
    private Long customerId;

	@ApiModelProperty(value="客户名称")
    private String customerName;

	@ApiModelProperty(value="商家ID")
    private Long merchantId;

	@ApiModelProperty(value="商家名称")
    private String merchantName;

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

	@ApiModelProperty(value="付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款")
    private String paymentTerms;
	@ApiModelProperty(value="付款条款（中文）")
    private String paymentTermsLabel;

	@ApiModelProperty(value="贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW")
    private String tradeTerms;
	@ApiModelProperty(value="贸易条款（中文）")
    private String tradeTermsLabel;

	@ApiModelProperty(value="业务模式  1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结")
    private String businessModel;
	@ApiModelProperty(value="业务模式（中文）")
    private String businessModelLabel;

	@ApiModelProperty(value="币种 CNY-人民币 AUD-澳元 JPY-日元 USD-美元 HKD-港币 EUR-欧元 GBP-英镑")
    private String currency;
	@ApiModelProperty(value="币种（中文）")
    private String currencyLabel;

	@ApiModelProperty(value="理货单位 00-托盘 01-箱 02-件")
    private String tallyingUnit;
	@ApiModelProperty(value="理货单位（中文）")
    private String tallyingUnitLabel;

	@ApiModelProperty(value="装货港")
    private String loadPort;

	@ApiModelProperty(value="目的地名称")
    private String destination;

	@ApiModelProperty(value="目的港名称")
    private String destinationPort;

	@ApiModelProperty(value="卸货港")
    private String portDes;
	@ApiModelProperty(value="卸货港（中文）")
    private String portDesLabel;

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
    private Timestamp shipDate;

	@ApiModelProperty(value="备注")
    private String remark;

	@ApiModelProperty(value="运输方式 1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他")
    private String transport;
	@ApiModelProperty(value="运输方式（中文）")
    private String transportLabel;

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
	@ApiModelProperty(value="托盘材质（中文）")
    private String torrPackTypeLabel;

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
	@ApiModelProperty(value="订单状态（中文）")
    private String statusLabel;

	@ApiModelProperty(value="接口状态 0-未推接口；1-已推接口；2-接口推送失败 用于海外仓推跨境宝")
    private String apiStatus;	
	@ApiModelProperty(value="接口状态（中文）")
    private String apiStatusLabel;

	@ApiModelProperty(value="推送出仓指令时间")
    private Timestamp pushOutDate;

	@ApiModelProperty(value="打托时间")
    private Timestamp pallteizeDate;

	@ApiModelProperty(value="清关提交时间")
    private Timestamp customsSubmitDate;

	@ApiModelProperty(value="清关确认时间")
    private Timestamp customsConfirmDate;

	@ApiModelProperty(value="确认申报时间")
    private Timestamp confirmDeclarationDate;

	@ApiModelProperty(value="物流委托时间")
    private Timestamp logisticsCommissionDate;

	@ApiModelProperty(value="出库时间")
    private Timestamp deliverDate;

	@ApiModelProperty(value="上架时间")
    private Timestamp shelfDate;

	@ApiModelProperty(value="国家")
    private String country;

	@ApiModelProperty(value="收件人名称")
    private String receiverName;

	@ApiModelProperty(value="收件人地址")
    private String receiverAddress;

	@ApiModelProperty(value="邮寄方式 1.寄售 2.自提")
    private String mailMode;
	@ApiModelProperty(value="邮寄方式（中文）")
    private String mailModeLabel;

	@ApiModelProperty(value="销售订单ID集合")
    private String saleOrderIds;

	@ApiModelProperty(value="销售订单编号集合")
    private String saleOrderCodes;
	
	@ApiModelProperty(value="创建人")
    private Long creater;

	@ApiModelProperty(value="创建人名称")
    private String createName;

	@ApiModelProperty(value="创建时间")
    private Timestamp createDate;
	
	@ApiModelProperty(value = "事业部集合")
	private List<Long> buList;
	
	@ApiModelProperty(value = "销售预申报id集合")
	private String ids;
	
	@ApiModelProperty("仓库进出口指令  1-是 0-否")
    private String depotIsInOutInstruction;
	
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
	
	@ApiModelProperty("首次上架时间")
    private Timestamp firstShelfDate;
    
	@ApiModelProperty("完结上架时间")
    private Timestamp endShelfDate;

	@ApiModelProperty("导出打托资料时间")
	private Timestamp exportPallteizeDate;

	@ApiModelProperty("仓库类别 a-保税仓，b-备查库，c-海外仓，d-中转仓,e-暂存区，f-销毁区 g-内贸仓")
	private String outDepotType;

	public Timestamp getExportPallteizeDate() {
		return exportPallteizeDate;
	}

	public void setExportPallteizeDate(Timestamp exportPallteizeDate) {
		this.exportPallteizeDate = exportPallteizeDate;
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
		if(StringUtils.isNotBlank(paymentTerms)){
			this.paymentTermsLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleDeclare_paymentTermsList, paymentTerms);
		}
	}
	public String getPaymentTermsLabel() {
		return paymentTermsLabel;
	}
	
	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
		if(StringUtils.isNotBlank(tradeTerms)){
			this.tradeTermsLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleDeclare_tradeTermsList, tradeTerms);
		}
	}
	
	public String getTradeTermsLabel() {
		return tradeTermsLabel;
	}
	
	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
		if(StringUtils.isNotBlank(businessModel)){
			this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleDeclare_businessModelList, businessModel);
		}
	}
	public String getBusinessModelLabel() {
		return businessModelLabel;
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
		if(StringUtils.isNotBlank(tallyingUnit)){
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
		}
	}
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
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
		if(StringUtils.isNotBlank(portDes)){
			this.portDesLabel = DERP.getLabelByKey(DERP.portDestList, portDes);
		}
	}

	public String getPortDesLabel() {
		return portDesLabel;
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

	public Timestamp getShipDate() {
		return shipDate;
	}

	public void setShipDate(Timestamp shipDate) {
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
		if(StringUtils.isNotBlank(transport)){
			this.transportLabel = DERP.getLabelByKey(DERP.transportList, transport);
		}
	}
	public String getTransportLabel() {
		return transportLabel;
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
		if(StringUtils.isNotBlank(torrPackType)){
			this.torrPackTypeLabel = DERP.getLabelByKey(DERP_ORDER.order_torrpacktypeList, torrPackType);
		}
	}

	public String getTorrPackTypeLabel() {
		return torrPackTypeLabel;
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
		if(StringUtils.isNotBlank(status)){
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleDeclare_statusList, status);
		}
	}
	
	public String getStatusLabel() {
		return statusLabel;
	}
	
	public String getApiStatus() {
		return apiStatus;
	}

	public void setApiStatus(String apiStatus) {
		this.apiStatus = apiStatus;
		if(StringUtils.isNotBlank(apiStatus)){
			this.apiStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleDeclare_apiStatusList, apiStatus);
		}
	}

	public String getApiStatusLabel() {
		return apiStatusLabel;
	}

	public Timestamp getPushOutDate() {
		return pushOutDate;
	}

	public void setPushOutDate(Timestamp pushOutDate) {
		this.pushOutDate = pushOutDate;
	}

	public Timestamp getPallteizeDate() {
		return pallteizeDate;
	}

	public void setPallteizeDate(Timestamp pallteizeDate) {
		this.pallteizeDate = pallteizeDate;
	}

	public Timestamp getCustomsSubmitDate() {
		return customsSubmitDate;
	}

	public void setCustomsSubmitDate(Timestamp customsSubmitDate) {
		this.customsSubmitDate = customsSubmitDate;
	}

	public Timestamp getCustomsConfirmDate() {
		return customsConfirmDate;
	}

	public void setCustomsConfirmDate(Timestamp customsConfirmDate) {
		this.customsConfirmDate = customsConfirmDate;
	}

	public Timestamp getConfirmDeclarationDate() {
		return confirmDeclarationDate;
	}

	public void setConfirmDeclarationDate(Timestamp confirmDeclarationDate) {
		this.confirmDeclarationDate = confirmDeclarationDate;
	}

	public Timestamp getLogisticsCommissionDate() {
		return logisticsCommissionDate;
	}

	public void setLogisticsCommissionDate(Timestamp logisticsCommissionDate) {
		this.logisticsCommissionDate = logisticsCommissionDate;
	}

	public Timestamp getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}

	public Timestamp getShelfDate() {
		return shelfDate;
	}

	public void setShelfDate(Timestamp shelfDate) {
		this.shelfDate = shelfDate;
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
		if(StringUtils.isNotBlank(mailMode)){
			this.mailModeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.saleOrder_mailModeList, mailMode);
		}
	}

	public String getMailModeLabel() {
		return mailModeLabel;
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

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public List<Long> getBuList() {
		return buList;
	}

	public void setBuList(List<Long> buList) {
		this.buList = buList;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getDepotIsInOutInstruction() {
		return depotIsInOutInstruction;
	}

	public void setDepotIsInOutInstruction(String depotIsInOutInstruction) {
		this.depotIsInOutInstruction = depotIsInOutInstruction;
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

	public String getCurrencyLabel() {
		return currencyLabel;
	}

	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}

	public Timestamp getFirstShelfDate() {
		return firstShelfDate;
	}

	public void setFirstShelfDate(Timestamp firstShelfDate) {
		this.firstShelfDate = firstShelfDate;
	}

	public Timestamp getEndShelfDate() {
		return endShelfDate;
	}

	public void setEndShelfDate(Timestamp endShelfDate) {
		this.endShelfDate = endShelfDate;
	}

	public void setPaymentTermsLabel(String paymentTermsLabel) {
		this.paymentTermsLabel = paymentTermsLabel;
	}

	public void setTradeTermsLabel(String tradeTermsLabel) {
		this.tradeTermsLabel = tradeTermsLabel;
	}

	public void setBusinessModelLabel(String businessModelLabel) {
		this.businessModelLabel = businessModelLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}

	public void setPortDesLabel(String portDesLabel) {
		this.portDesLabel = portDesLabel;
	}

	public void setTransportLabel(String transportLabel) {
		this.transportLabel = transportLabel;
	}

	public void setTorrPackTypeLabel(String torrPackTypeLabel) {
		this.torrPackTypeLabel = torrPackTypeLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public void setApiStatusLabel(String apiStatusLabel) {
		this.apiStatusLabel = apiStatusLabel;
	}

	public void setMailModeLabel(String mailModeLabel) {
		this.mailModeLabel = mailModeLabel;
	}

	public String getOutDepotType() {
		return outDepotType;
	}

	public void setOutDepotType(String outDepotType) {
		this.outDepotType = outDepotType;
	}
}
