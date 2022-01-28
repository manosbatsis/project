package com.topideal.entity.vo.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 预申报单
 * 
 * @author lianchenxing
 *
 */
public class DeclareOrderModel extends PageModel implements Serializable {

	// 预申报单号
	private String code;
	// 供应商ID
	private Long supplierId;
	// 合同号
	private String contractNo;
	// 业务模式 1 采购 2 代销
	private String businessModel;
	// 修改人
	private Long modifier;
	// 仓库ID
	private Long depotId;
	// 销售渠道
	private String channel;
	// 备注
	private String remark;
	// 商家名称
	private String merchantName;
	// po号
	private String poNo;
	// 属性说明
	private Long merchantId;
	// 头程提单号
	private String ladingBill;
	// 目的地名称
	private String destinationName;
	// 收货地点
	private String deliveryAddr;
	// 目的港名称
	private String destinationPortName;
	// id
	private Long id;
	// 发票号
	private String invoiceNo;
	// 创建时间
	private Timestamp createDate;
	// 供应商名称
	private String supplierName;
	// 仓库名称
	private String depotName;
	// 修改时间
	private Timestamp modifyDate;
	// 预计到港时间
	private Timestamp arriveDate;
	// 运输方式 1 空运 2 海运
	private String transport;
	// 箱数
	private Integer boxNum;
	// 创建人
	private Long creater;
	// 订单状态 001待审核 002审核中 003已审核 008已取消
	private String status;
	// 装货港
	private String portLoading;
	// 包装方式
	private String packType;
	// 付款条约
	private String payRules;
	// 提单毛重
	private Double billWeight;
	// 卸货港编码
	private String portDestNo;
	// 二程提单号
	private String blNo;
	// 属性说明
	private String email;
	// 进出口口岸
	private String imExpPort;
	// 单据状态(1-正常，0-异常) 用于海外仓推跨境宝
	private String state;
	// lbx单号
	private String lbxNo;
	// 修改人用户名
	private String updateName;
	// 创建人用户名
	private String createName;
	// 卓志编码
	private String topidealCode;
	// 唛头
	private String mark;
	// 境外发货人
	private String shipper;
	// 提交人姓名
	private String submitter;
	// 提交时间
	private Timestamp submitDate;
	// 理货单位 00-托盘 01-箱 02-件
	private String tallyingUnit;
	// 卸货港名称
	private String portDest;

	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 * 事业部id
	 */
	private Long buId;

	/**
	 * 装船时间
	 */
	private Timestamp shipDate;
	/**
	 * 装货港
	 */
	private String loadPort;
	/**
	 * 托数
	 */
	private Integer torrNum;
	/**
	 * 入库关区id
	 */
	private Long inCustomsId;
	/**
	 * 入库关区编码
	 */
	private String inCustomsCode;
	/**
	 * 入库关区名称
	 */
	private String inPlatformCustoms;
	/**
	 * 托盘材质
	 */
	private String palletMaterial;

	/**
	 * 付款条款 1-一次付款-付款发货 2-多次付款 3-一次付款-发货付款
	 */
	private String paymentProvision;
	/**
	 * 贸易条款 1- CIF 2-CRF 3-FOB 4-DPA 5-EXW
	 */
	private String tradeTerms;
	/**
	 * 车型 及 数量
	 */
	private String motorcycleType;

	private List<DeclareOrderItemModel> itemList;

	/**
	 * 清关确认时间
	 */
	private Timestamp customsConfirmDate;
	/**
	 * 确认申报时间
	 */
	private Timestamp confirmDeclarationDate;
	/**
	 * 确认入仓时间
	 */
	private Timestamp confirmDepotDate;
	/**
	 * 确认订舱/车时间
	 */
	private Timestamp confirmBookingDate;
	/**
	 * 物流委托时间
	 */
	private Timestamp logisticsCommissionDate;
	/**
	 * 预计离港时间
	 */
	private Timestamp estimatedDeliveryDate;
	/**
	 * 上架时间
	 */
	private Timestamp shelfDate;
	/**
    * 清关提交时间
    */
    private Timestamp customsSubmitDate;
    /**
    * 预计入仓时间
    */
    private Timestamp arriveDepotDate;
    /**
    * 计划装船时间
    */
    private Timestamp plannedShipDate;
	/**
	 * 车次
	 */
    private String trainNumber;
    /**
	 * 标准箱TEU
	 */
    private String standardCaseTeu;
    /**
	 * 承运商
	 */
    private String carrier;

	/**
	 * 陆运运输方式
	 */
	private String landTransport;

	/**
	 * 确认订车时间
	 */
	private Timestamp confirmCatDate;

	/**
	 * 陆运订车物流委托时间
	 */
	private Timestamp landCommissionDate;

	/**
	 * 推送入仓指令时间
	 */
	private Timestamp pushWarehouseDate;
	/**
    * 到港时间
    */
    private Timestamp arriveSeaDate;
	/**
	 * 提货时间
	 */
    private Timestamp pickUpDate;

	public Timestamp getPushWarehouseDate() {
		return pushWarehouseDate;
	}

	public void setPushWarehouseDate(Timestamp pushWarehouseDate) {
		this.pushWarehouseDate = pushWarehouseDate;
	}

	public Timestamp getLandCommissionDate() {
		return landCommissionDate;
	}

	public void setLandCommissionDate(Timestamp landCommissionDate) {
		this.landCommissionDate = landCommissionDate;
	}

	public String getLandTransport() {
		return landTransport;
	}

	public void setLandTransport(String landTransport) {
		this.landTransport = landTransport;
	}

	public Timestamp getConfirmCatDate() {
		return confirmCatDate;
	}

	public void setConfirmCatDate(Timestamp confirmCatDate) {
		this.confirmCatDate = confirmCatDate;
	}

	public String getPortDest() {
		return portDest;
	}

	public void setPortDest(String portDest) {
		this.portDest = portDest;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public Timestamp getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate = submitDate;
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

	/* topidealCode get 方法 */
	public String getTopidealCode() {
		return topidealCode;
	}

	/* topidealCode set 方法 */
	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	/* lbxNo get 方法 */
	public String getLbxNo() {
		return lbxNo;
	}

	/* lbxNo set 方法 */
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	/* createName get 方法 */
	public String getCreateName() {
		return createName;
	}

	/* createName set 方法 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/* updateName get 方法 */
	public String getUpdateName() {
		return updateName;
	}

	/* updateName set 方法 */
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	/* state get 方法 */
	public String getState() {
		return state;
	}

	/* state set 方法 */
	public void setState(String state) {
		this.state = state;
	}

	/* imExpPort get 方法 */
	public String getImExpPort() {
		return imExpPort;
	}

	/* imExpPort set 方法 */
	public void setImExpPort(String imExpPort) {
		this.imExpPort = imExpPort;
	}

	/* email get 方法 */
	public String getEmail() {
		return email;
	}

	/* email set 方法 */
	public void setEmail(String email) {
		this.email = email;
	}

	/* blNo get 方法 */
	public String getBlNo() {
		return blNo;
	}

	/* blNo set 方法 */
	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	/* portDestNo get 方法 */
	public String getPortDestNo() {
		return portDestNo;
	}

	/* portDestNo set 方法 */
	public void setPortDestNo(String portDestNo) {
		this.portDestNo = portDestNo;
	}

	/* billWeigh get 方法 */
	public Double getBillWeight() {
		return billWeight;
	}

	/* billWeigh set 方法 */
	public void setBillWeight(Double billWeight) {
		this.billWeight = billWeight;
	}

	/* payRules get 方法 */
	public String getPayRules() {
		return payRules;
	}

	/* payRules set 方法 */
	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}

	/* packType get 方法 */
	public String getPackType() {
		return packType;
	}

	/* packType set 方法 */
	public void setPackType(String packType) {
		this.packType = packType;
	}

	/* portLoading get 方法 */
	public String getPortLoading() {
		return portLoading;
	}

	/* portLoading set 方法 */
	public void setPortLoading(String portLoading) {
		this.portLoading = portLoading;
	}

	/* itemList set 方法 */
	public List<DeclareOrderItemModel> getItemList() {
		return itemList;
	}

	/* itemList set 方法 */
	public void setItemList(List<DeclareOrderItemModel> itemList) {
		this.itemList = itemList;
	}

	/* code set 方法 */
	public String getStatus() {
		return status;
	}

	/* code set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* supplierId get 方法 */
	public Long getSupplierId() {
		return supplierId;
	}

	/* supplierId set 方法 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* businessModel get 方法 */
	public String getBusinessModel() {
		return businessModel;
	}

	/* businessModel set 方法 */
	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}

	/* modifier get 方法 */
	public Long getModifier() {
		return modifier;
	}

	/* modifier set 方法 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* channel get 方法 */
	public String getChannel() {
		return channel;
	}

	/* channel set 方法 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/* remark get 方法 */
	public String getRemark() {
		return remark;
	}

	/* remark set 方法 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* poNo get 方法 */
	public String getPoNo() {
		return poNo;
	}

	/* poNo set 方法 */
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	/* merchantId get 方法 */
	public Long getMerchantId() {
		return merchantId;
	}

	/* merchantId set 方法 */
	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	/* ladingBill get 方法 */
	public String getLadingBill() {
		return ladingBill;
	}

	/* ladingBill set 方法 */
	public void setLadingBill(String ladingBill) {
		this.ladingBill = ladingBill;
	}

	/* destinationName get 方法 */
	public String getDestinationName() {
		return destinationName;
	}

	/* destinationName set 方法 */
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	/* deliveryAddr get 方法 */
	public String getDeliveryAddr() {
		return deliveryAddr;
	}

	/* deliveryAddr set 方法 */
	public void setDeliveryAddr(String deliveryAddr) {
		this.deliveryAddr = deliveryAddr;
	}

	/* destinationPortName get 方法 */
	public String getDestinationPortName() {
		return destinationPortName;
	}

	/* destinationPortName set 方法 */
	public void setDestinationPortName(String destinationPortName) {
		this.destinationPortName = destinationPortName;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* invoiceNo get 方法 */
	public String getInvoiceNo() {
		return invoiceNo;
	}

	/* invoiceNo set 方法 */
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	/* supplierName get 方法 */
	public String getSupplierName() {
		return supplierName;
	}

	/* supplierName set 方法 */
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	/* depotName get 方法 */
	public String getDepotName() {
		return depotName;
	}

	/* depotName set 方法 */
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	/* modifyDate get 方法 */
	public Timestamp getModifyDate() {
		return modifyDate;
	}

	/* modifyDate set 方法 */
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* arriveDate get 方法 */
	public Timestamp getArriveDate() {
		return arriveDate;
	}

	/* arriveDate set 方法 */
	public void setArriveDate(Timestamp arriveDate) {
		this.arriveDate = arriveDate;
	}

	/* transport get 方法 */
	public String getTransport() {
		return transport;
	}

	/* transport set 方法 */
	public void setTransport(String transport) {
		this.transport = transport;
	}

	/* boxNum get 方法 */
	public Integer getBoxNum() {
		return boxNum;
	}

	/* boxNum set 方法 */
	public void setBoxNum(Integer boxNum) {
		this.boxNum = boxNum;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
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

	public Timestamp getShipDate() {
		return shipDate;
	}

	public void setShipDate(Timestamp shipDate) {
		this.shipDate = shipDate;
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

	public String getMotorcycleType() {
		return motorcycleType;
	}

	public void setMotorcycleType(String motorcycleType) {
		this.motorcycleType = motorcycleType;
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

	public Timestamp getConfirmDepotDate() {
		return confirmDepotDate;
	}

	public void setConfirmDepotDate(Timestamp confirmDepotDate) {
		this.confirmDepotDate = confirmDepotDate;
	}

	public Timestamp getConfirmBookingDate() {
		return confirmBookingDate;
	}

	public void setConfirmBookingDate(Timestamp confirmBookingDate) {
		this.confirmBookingDate = confirmBookingDate;
	}

	public Timestamp getLogisticsCommissionDate() {
		return logisticsCommissionDate;
	}

	public void setLogisticsCommissionDate(Timestamp logisticsCommissionDate) {
		this.logisticsCommissionDate = logisticsCommissionDate;
	}

	public Timestamp getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(Timestamp estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public Timestamp getShelfDate() {
		return shelfDate;
	}

	public void setShelfDate(Timestamp shelfDate) {
		this.shelfDate = shelfDate;
	}

	public Timestamp getCustomsSubmitDate() {
		return customsSubmitDate;
	}

	public void setCustomsSubmitDate(Timestamp customsSubmitDate) {
		this.customsSubmitDate = customsSubmitDate;
	}

	public Timestamp getArriveDepotDate() {
		return arriveDepotDate;
	}

	public void setArriveDepotDate(Timestamp arriveDepotDate) {
		this.arriveDepotDate = arriveDepotDate;
	}

	public Timestamp getPlannedShipDate() {
		return plannedShipDate;
	}

	public void setPlannedShipDate(Timestamp plannedShipDate) {
		this.plannedShipDate = plannedShipDate;
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

	public Timestamp getArriveSeaDate() {
		return arriveSeaDate;
	}

	public void setArriveSeaDate(Timestamp arriveSeaDate) {
		this.arriveSeaDate = arriveSeaDate;
	}

	public Timestamp getPickUpDate() {
		return pickUpDate;
	}

	public void setPickUpDate(Timestamp pickUpDate) {
		this.pickUpDate = pickUpDate;
	}
}
