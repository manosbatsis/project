package com.topideal.order.webapi.purchase.form;

import com.topideal.entity.dto.purchase.DeclareOrderItemDTO;
import com.topideal.entity.vo.common.PackingDetailsModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class DeclareOrderAddForm  {
	@ApiModelProperty(value="令牌", required=true)
    private String token;
	// 预申报单号
	@ApiModelProperty(value = "预申报单号", required = false)
	private String code;
	// 供应商ID
	@ApiModelProperty(value = "供应商ID", required = false)
	private Long supplierId;
	// 合同号
	@ApiModelProperty(value = "合同号", required = false)
	private String contractNo;
	// 业务模式 1 采购 2 代销
	@ApiModelProperty(value = "业务模式 1 采购 2 代销", required = false)
	private String businessModel;
	// 仓库ID
	@ApiModelProperty(value = "仓库ID", required = false)
	private Long depotId;
	// 销售渠道
	@ApiModelProperty(value = "销售渠道", required = false)
	private String channel;
	// 备注
	@ApiModelProperty(value = "备注", required = false)
	private String remark;
	// 商家名称
	@ApiModelProperty(value = "商家名称", required = false)
	private String merchantName;
	// po号
	@ApiModelProperty(value = "po号", required = false)
	private String poNo;
	// 属性说明
	@ApiModelProperty(value = "属性说明", required = false)
	private Long merchantId;
	// 头程提单号
	@ApiModelProperty(value = "头程提单号", required = false)
	private String ladingBill;
	// 目的地名称
	@ApiModelProperty(value = "目的地名称", required = false)
	private String destinationName;
	// 收货地点
	@ApiModelProperty(value = "收货地点", required = false)
	private String deliveryAddr;
	// 目的港名称
	@ApiModelProperty(value = "目的港名称", required = false)
	private String destinationPortName;
	// id
	@ApiModelProperty(value = "预申报单ID", required = false)
	private Long id;
	// 发票号
	@ApiModelProperty(value = "发票号", required = false)
	private String invoiceNo;
	// 供应商名称
	@ApiModelProperty(value = "供应商名称", required = false)
	private String supplierName;
	// 仓库名称
	@ApiModelProperty(value = "仓库名称", required = false)
	private String depotName;
	// 运输方式 1 空运 2 海运
	@ApiModelProperty(value = "国际物流运输方式 1 空运 2 海运", required = false)
	private String transport;
	// 陆运运输方式
	@ApiModelProperty(value = "陆运运输方式", required = false)
	private String landTransport;
	// 箱数
	@ApiModelProperty(value = "箱数", required = false)
	private Integer boxNum;
	// 订单状态 001待审核 002审核中 003已审核 008已取消
	@ApiModelProperty(value = "订单状态 001待审核 002审核中 003已审核 008已取消", required = false)
	private String status;
	// 订单ID
	@ApiModelProperty(value = "采购订单ID", required = false)
	private Long purchaseId;
	// 装货港
	@ApiModelProperty(value = "装货港", required = false)
	private String portLoading;
	// 包装方式
	@ApiModelProperty(value = "包装方式", required = false)
	private String packType;
	// 付款条约
	@ApiModelProperty(value = "付款条约", required = false)
	private String payRules;
	// 提单毛重
	@ApiModelProperty(value = "提单毛重", required = false)
	private Double billWeight;
	// 卸货港编码
	@ApiModelProperty(value = "卸货港编码", required = false)
	private String portDestNo;
	// 二程提单号
	@ApiModelProperty(value = "二程提单号", required = false)
	private String blNo;
	// 属性说明
	@ApiModelProperty(value = "邮件", required = false)
	private String email;
	// 进出口口岸
	@ApiModelProperty(value = "进出口口岸", required = false)
	private String imExpPort;
	// 单据状态(1-正常，0-异常) 用于海外仓推跨境宝
	@ApiModelProperty(value = "单据状态(1-正常，0-异常) 用于海外仓推跨境宝", required = false)
	private String state;
	// lbx单号
	@ApiModelProperty(value = "lbx单号", required = false)
	private String lbxNo;
	// 卓志编码
	@ApiModelProperty(value = "卓志编码", required = false)
	private String topidealCode;
	// 唛头
	@ApiModelProperty(value = "唛头", required = false)
	private String mark;
	// 境外发货人
	@ApiModelProperty(value = "境外发货人", required = false)
	private String shipper;
	// 提交人姓名
	@ApiModelProperty(value = "提交人姓名", required = false)
	private String submitter;
	// 理货单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件", required = false)
	private String tallyingUnit;
	// 卸货港名称
	@ApiModelProperty(value = "卸货港名称", required = false)
	private String portDest;

	// 拓展字段
	// 采购订单编码
	@ApiModelProperty(value = "采购订单编码", required = false)
	private String purchaseCode;
	// 预计到岗开始时间
	@ApiModelProperty(value = "预计到港开始时间", required = false)
	private String arriveStartDate;
	// 预计到岗结束时间
	@ApiModelProperty(value = "预计到港结束时间", required = false)
	private String arriveEndDate;

	/**
	 * 事业部名称
	 */
	@ApiModelProperty(value = "事业部名称", required = false)
	private String buName;
	/**
	 * 事业部id
	 */
	@ApiModelProperty(value = "事业部id", required = false)
	private Long buId;

	/**
	 * 仓库类型
	 */
	@ApiModelProperty(value = "仓库类型", required = false)
	private String depotType;

	@ApiModelProperty(value = "预计到港时间前端传入字符串，格式yyyy-MM-dd", required = false)
	private String arriveDate2;

	/**
	 * 装船时间
	 */
	@ApiModelProperty(value = "装船时间 格式yyyy-MM-dd", required = false)
	private String shipDateStr;
	/**
	 * 装货港
	 */
	@ApiModelProperty(value = "装货港", required = false)
	private String loadPort;
	/**
	 * 托数
	 */
	@ApiModelProperty(value = "托数", required = false)
	private Integer torrNum;
	/**
	 * 入库关区id
	 */
	@ApiModelProperty(value = "入库关区id", required = false)
	private Long inCustomsId;
	/**
	 * 入库关区编码
	 */
	@ApiModelProperty(value = "入库关区编码", required = false)
	private String inCustomsCode;
	/**
	 * 入库关区名称
	 */
	@ApiModelProperty(value = "入库关区名称", required = false)
	private String inPlatformCustoms;
	/**
	 * 托盘材质
	 */
	@ApiModelProperty(value = "托盘材质", required = false)
	private String palletMaterial;

	@ApiModelProperty(value="商品明细", required = false)
	private List<DeclareOrderItemDTO> items ;

	/**
	 * 车型 及 数量
	 */
	@ApiModelProperty(value = "车型 及 数量", required = false)
	private String motorcycleType;

	/**
    * 记录ID
    */
	@ApiModelProperty("物流联系人模版ID")
    private Long logisticsContactTemplateLink;
    /**
    * 名称
    */
	@ApiModelProperty("物流联系人模版名称")
    private String logisticsContactTemplateLinkName;
    /**
    * 发货人/提货信息模版ID
    */
	@ApiModelProperty("发货人/提货信息模版ID")
    private Long shipperTempId;
    /**
    * 发货人/提货信息模版名称
    */
	@ApiModelProperty("发货人/提货信息模版名称")
    private String shipperTempName;
	@ApiModelProperty("发货人/提货信息模版详情")
    private String shipperTempDetails;
    /**
    * 收货人/卸货信息模版ID
    */
	@ApiModelProperty("收货人/卸货信息模版ID")
    private Long consigneeTempId;
    /**
    * 收货人/卸货信息模版名称
    */
	@ApiModelProperty("收货人/卸货信息模版名称")
    private String consigneeTempName;
	@ApiModelProperty("收货人/卸货信息模版详情")
	private String consigneeTempDetails;
    /**
    * 通知人模版ID
    */
	@ApiModelProperty("通知人模版ID")
    private Long notifierTempId;
    /**
    * 通知人模版名
    */
	@ApiModelProperty("通知人模版名")
    private String notifierTempName;
	@ApiModelProperty("通知人模版详情")
    private String notifierTempDetails;
    /**
    * 对接人模版ID
    */
	@ApiModelProperty("对接人模版ID")
    private Long dockingTempId;
    /**
    * 对接人模版名
    */
	@ApiModelProperty("对接人模版名")
    private String dockingTempName;
	@ApiModelProperty("对接人模版详情")
    private String dockingTempDetails;
    /**
    * 承运商信息模版ID
    */
	@ApiModelProperty("承运商信息模版ID")
    private Long carrierInformationTempId;
    /**
    * 承运商信息模版名
    */
	@ApiModelProperty("承运商信息模版名")
    private String carrierInformationTempName;
	@ApiModelProperty("承运商信息模版详情")
	private String carrierInformationTempDetails;

	/**
	 * 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款
	 */
	@ApiModelProperty(value = "付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款", required = false)
	private String paymentProvision;
	/**
	 * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
	 */
	@ApiModelProperty(value = "贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW", required = false)
	private String tradeTerms;

	@ApiModelProperty("预计入仓时间字符串")
	private String arriveDepotDateStr;

	@ApiModelProperty("计划装船时间字符串")
	private String plannedShipDateStr;

	/**
	 * 标准箱TEU
	 */
	@ApiModelProperty("标准箱TEU")
	private String standardCaseTeu;

	/**
	 * 车次
	 */
	@ApiModelProperty("车次")
	private String trainNumber;

	/**
	 * 承运商
	 */
	@ApiModelProperty("承运商")
	private String carrier;

	/**
	 * 预计离港时间
	 */
	@ApiModelProperty("预计离港时间")
	private String estimatedDeliveryDate;

	@ApiModelProperty(value="装箱明细")
	private List<PackingDetailsModel> packingList ;

	public String getLandTransport() {
		return landTransport;
	}

	public void setLandTransport(String landTransport) {
		this.landTransport = landTransport;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getBusinessModel() {
		return businessModel;
	}

	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	public Long getDepotId() {
		return depotId;
	}

	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getLadingBill() {
		return ladingBill;
	}

	public void setLadingBill(String ladingBill) {
		this.ladingBill = ladingBill;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	public String getDestinationPortName() {
		return destinationPortName;
	}

	public void setDestinationPortName(String destinationPortName) {
		this.destinationPortName = destinationPortName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public Integer getBoxNum() {
		return boxNum;
	}

	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getPortLoading() {
		return portLoading;
	}

	public void setPortLoading(String portLoading) {
		this.portLoading = portLoading;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public String getPayRules() {
		return payRules;
	}

	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}

	public Double getBillWeight() {
		return billWeight;
	}

	public void setBillWeight(Double billWeight) {
		this.billWeight = billWeight;
	}

	public String getPortDestNo() {
		return portDestNo;
	}

	public void setPortDestNo(String portDestNo) {
		this.portDestNo = portDestNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImExpPort() {
		return imExpPort;
	}

	public void setImExpPort(String imExpPort) {
		this.imExpPort = imExpPort;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getPortDest() {
		return portDest;
	}

	public void setPortDest(String portDest) {
		this.portDest = portDest;
	}

	public String getInCustomsCode() {
		return inCustomsCode;
	}

	public void setInCustomsCode(String inCustomsCode) {
		this.inCustomsCode = inCustomsCode;
	}

	public String getInPlatformCustoms() {
		return inPlatformCustoms;
	}

	public void setInPlatformCustoms(String inPlatformCustoms) {
		this.inPlatformCustoms = inPlatformCustoms;
	}

	public String getPalletMaterial() {
		return palletMaterial;
	}

	public void setPalletMaterial(String palletMaterial) {
		this.palletMaterial = palletMaterial;
	}

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public String getArriveStartDate() {
		return arriveStartDate;
	}

	public void setArriveStartDate(String arriveStartDate) {
		this.arriveStartDate = arriveStartDate;
	}

	public String getArriveEndDate() {
		return arriveEndDate;
	}

	public void setArriveEndDate(String arriveEndDate) {
		this.arriveEndDate = arriveEndDate;
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

	public String getLoadPort() {
		return loadPort;
	}

	public void setLoadPort(String loadPort) {
		this.loadPort = loadPort;
	}

	public Integer getTorrNum() {
		return torrNum;
	}

	public void setTorrNum(Integer torrNum) {
		this.torrNum = torrNum;
	}

	public Long getInCustomsId() {
		return inCustomsId;
	}

	public void setInCustomsId(Long inCustomsId) {
		this.inCustomsId = inCustomsId;
	}

	public String getDepotType() {
		return depotType;
	}

	public void setDepotType(String depotType) {
		this.depotType = depotType;
	}

	public String getArriveDate2() {
		return arriveDate2;
	}

	public void setArriveDate2(String arriveDate2) {
		this.arriveDate2 = arriveDate2;
	}

	public String getShipDateStr() {
		return shipDateStr;
	}

	public void setShipDateStr(String shipDateStr) {
		this.shipDateStr = shipDateStr;
	}

	public List<DeclareOrderItemDTO> getItems() {
		return items;
	}

	public void setItems(List<DeclareOrderItemDTO> items) {
		this.items = items;
	}

	public String getMotorcycleType() {
		return motorcycleType;
	}

	public void setMotorcycleType(String motorcycleType) {
		this.motorcycleType = motorcycleType;
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

	public Long getLogisticsContactTemplateLink() {
		return logisticsContactTemplateLink;
	}

	public void setLogisticsContactTemplateLink(Long logisticsContactTemplateLink) {
		this.logisticsContactTemplateLink = logisticsContactTemplateLink;
	}

	public String getLogisticsContactTemplateLinkName() {
		return logisticsContactTemplateLinkName;
	}

	public void setLogisticsContactTemplateLinkName(String logisticsContactTemplateLinkName) {
		this.logisticsContactTemplateLinkName = logisticsContactTemplateLinkName;
	}

	public String getPaymentProvision() {
		return paymentProvision;
	}

	public void setPaymentProvision(String paymentProvision) {
		this.paymentProvision = paymentProvision;
	}

	public String getTradeTerms() {
		return tradeTerms;
	}

	public void setTradeTerms(String tradeTerms) {
		this.tradeTerms = tradeTerms;
	}

	public String getArriveDepotDateStr() {
		return arriveDepotDateStr;
	}

	public void setArriveDepotDateStr(String arriveDepotDateStr) {
		this.arriveDepotDateStr = arriveDepotDateStr;
	}

	public String getPlannedShipDateStr() {
		return plannedShipDateStr;
	}

	public void setPlannedShipDateStr(String plannedShipDateStr) {
		this.plannedShipDateStr = plannedShipDateStr;
	}

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getStandardCaseTeu() {
		return standardCaseTeu;
	}

	public void setStandardCaseTeu(String standardCaseTeu) {
		this.standardCaseTeu = standardCaseTeu;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public List<PackingDetailsModel> getPackingList() {
		return packingList;
	}

	public void setPackingList(List<PackingDetailsModel> packingList) {
		this.packingList = packingList;
	}
}
