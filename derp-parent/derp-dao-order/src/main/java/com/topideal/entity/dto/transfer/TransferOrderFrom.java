package com.topideal.entity.dto.transfer;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TransferOrderFrom implements Serializable{
	 //调拨订单id
    private String orderId;

    //调入仓库id
    private Long inDepotId;
    //调入仓库名称
    private String inDepotName;
    //调出仓库id
    private Long outDepotId;
    //调出仓库名称
    private String outDepotName;
    //调入客户id
    private Long inCustomerId;
    //调入客户名称
    private String inCustomerName;
    //合同号
    private String contractNo;
    //LBX单号
    private String lbxNo;
    private String invoiceNo;//发票号
    private String packType;//包装方式
    private String cartons;//箱数
    private String firstLadingBillNo;//头程提单号
    private String receivingAddress;//收货地址
    private String consigneeUsername;//收货人姓名
    private String destination;//收货地址
    private String country;//国家
    //备注
    private String remark;

    private String tallyingUnit;//理货单位 00-托盘 01-箱 02-件
    private String mark;//唛头
    private String shipper;//境外理货人
    private String  depotScheduleAddress;// 调入仓地址
    private Long  depotScheduleId;// 仓库附表id
    private String isSameArea;//是否同关区（0-否，1-是）
    private List<Map<String, Object>> goodsList;
    private String poNo;//po号
	//事业部id
	private Long buId;
	//事业部名称
	private String buName;
	//提单毛重
	private Double billWeight;

	/**
	 * 车次
	 */
	private String trainNumber;
	/**
	 * 标准箱TEU
	 */
	private String standardCaseTeu;
	/**
	 * 托数
	 */
	private Integer torrNum;
	/**
	 * 承运商
	 */
	private String carrier;
	/**
	 * 出库地点
	 */
	private String outdepotAddr;
	/**
	 * 运输方式   1-空运 2-海运 3-陆运 4-港到仓拖车 5-中欧铁路 6-其他
	 */
	private String transport;

	/**
	 * 托盘材质
	 */
	private String palletMaterial;
	/**
	 * 卸货港
	 */
	private String unloadPort;
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
	 * 出库关区id
	 */
	private Long outCustomsId;
	/**
	 * 出库关区编码
	 */
	private String outCustomsCode;
	/**
	 * 出库关区名称
	 */
	private String outPlatformCustoms;

	//装货港
	private String portLoading;

	//付款条约
	private String payRules;

	//事业部库位类型ID
	private String stockLocationTypeId;

	//事业部库位类型
	private String stockLocationTypeName;

	private List<Map<String, Object>> packingList ;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	public Long getInCustomerId() {
		return inCustomerId;
	}

	public void setInCustomerId(Long inCustomerId) {
		this.inCustomerId = inCustomerId;
	}

	public String getInCustomerName() {
		return inCustomerName;
	}

	public void setInCustomerName(String inCustomerName) {
		this.inCustomerName = inCustomerName;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Map<String, Object>> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<Map<String, Object>> goodsList) {
		this.goodsList = goodsList;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getPackType() {
		return packType;
	}

	public void setPackType(String packType) {
		this.packType = packType;
	}

	public String getCartons() {
		return cartons;
	}

	public void setCartons(String cartons) {
		this.cartons = cartons;
	}

	public String getFirstLadingBillNo() {
		return firstLadingBillNo;
	}

	public void setFirstLadingBillNo(String firstLadingBillNo) {
		this.firstLadingBillNo = firstLadingBillNo;
	}

	public String getReceivingAddress() {
		return receivingAddress;
	}

	public String getConsigneeUsername() {
		return consigneeUsername;
	}

	public void setConsigneeUsername(String consigneeUsername) {
		this.consigneeUsername = consigneeUsername;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
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

	public String getDepotScheduleAddress() {
		return depotScheduleAddress;
	}

	public void setDepotScheduleAddress(String depotScheduleAddress) {
		this.depotScheduleAddress = depotScheduleAddress;
	}

	public Long getDepotScheduleId() {
		return depotScheduleId;
	}

	public void setDepotScheduleId(Long depotScheduleId) {
		this.depotScheduleId = depotScheduleId;
	}

	public String getIsSameArea() {
		return isSameArea;
	}

	public void setIsSameArea(String isSameArea) {
		this.isSameArea = isSameArea;
	}
	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Long getBuId() {
		return buId;
	}

	public void setBuId(Long buId) {
		this.buId = buId;
	}

	public String getBuName() {
		return buName;
	}

	public void setBuName(String buName) {
		this.buName = buName;
	}

	public Double getBillWeight() {
		return billWeight;
	}

	public void setBillWeight(Double billWeight) {
		this.billWeight = billWeight;
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

	public Integer getTorrNum() {
		return torrNum;
	}

	public void setTorrNum(Integer torrNum) {
		this.torrNum = torrNum;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getOutdepotAddr() {
		return outdepotAddr;
	}

	public void setOutdepotAddr(String outdepotAddr) {
		this.outdepotAddr = outdepotAddr;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getPalletMaterial() {
		return palletMaterial;
	}

	public void setPalletMaterial(String palletMaterial) {
		this.palletMaterial = palletMaterial;
	}

	public String getUnloadPort() {
		return unloadPort;
	}

	public void setUnloadPort(String unloadPort) {
		this.unloadPort = unloadPort;
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

	public Long getOutCustomsId() {
		return outCustomsId;
	}

	public void setOutCustomsId(Long outCustomsId) {
		this.outCustomsId = outCustomsId;
	}

	public String getOutCustomsCode() {
		return outCustomsCode;
	}

	public void setOutCustomsCode(String outCustomsCode) {
		this.outCustomsCode = outCustomsCode;
	}

	public String getOutPlatformCustoms() {
		return outPlatformCustoms;
	}

	public void setOutPlatformCustoms(String outPlatformCustoms) {
		this.outPlatformCustoms = outPlatformCustoms;
	}

	public String getPortLoading() {
		return portLoading;
	}

	public void setPortLoading(String portLoading) {
		this.portLoading = portLoading;
	}

	public String getPayRules() {
		return payRules;
	}

	public void setPayRules(String payRules) {
		this.payRules = payRules;
	}

	public List<Map<String, Object>> getPackingList() {
		return packingList;
	}

	public void setPackingList(List<Map<String, Object>> packingList) {
		this.packingList = packingList;
	}

	public String getStockLocationTypeId() {
		return stockLocationTypeId;
	}

	public void setStockLocationTypeId(String stockLocationTypeId) {
		this.stockLocationTypeId = stockLocationTypeId;
	}

	public String getStockLocationTypeName() {
		return stockLocationTypeName;
	}

	public void setStockLocationTypeName(String stockLocationTypeName) {
		this.stockLocationTypeName = stockLocationTypeName;
	}
}
