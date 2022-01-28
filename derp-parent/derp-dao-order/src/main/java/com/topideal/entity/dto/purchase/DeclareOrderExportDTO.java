package com.topideal.entity.dto.purchase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;

public class DeclareOrderExportDTO extends PageModel implements Serializable {
	// 预申报单号
	private String code;
	// 供应商ID
	private Long supplierId;
	// 合同号
	private String contractNo;
	// 业务模式 1 采购 2 代销
	private String businessModel;
	private String businessModelLabel;
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
	private String transportLabel;
	// 箱数
	private Integer boxNum;
	// 创建人
	private Long creater;
	// 订单状态 001待审核 002审核中 003已审核 008已取消
	private String status;
	private String statusLabel ;
	// 订单ID
	private Long purchaseId;
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
	private String portDestNoLabel ;
	// 二程提单号
	private String blNo;
	// 属性说明
	private String email;
	// 进出口口岸
	private String imExpPort;
	// 单据状态(1-正常，0-异常) 用于海外仓推跨境宝
	private String state;
	private String stateLabel;
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
	private String tallyingUnitLabel;
	// 卸货港名称
	private String portDest;

	// 拓展字段
	// 采购订单编码
	private String purchaseCode;
	// 预计到岗开始时间
	private String arriveStartDate;
	// 预计到岗结束时间
	private String arriveEndDate;

	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 *  事业部id
	 */
	private Long buId;
	
	private String motorcycleType;
	
	// 采购总金额
	private BigDecimal amount;
	// 批次号
	private String batchNo;
	// 子合同号
	private String subcontractNo;
	// 商品id
	private Long goodsId;
	// 采购数量
	private Integer num;
	// 成分占比
	private String constituentRatio;
	// 箱号
	private String boxNo;
	// 申报单价
	private BigDecimal price;
	// 采购订单号
	private String purchase;
	// 商品货号
	private String goodsNo;
	// 商品名称
	private String goodsName;
	// 商品编码
	private String goodsCode;
	// 净重
	private Double grossWeight;
	// 毛重
	private Double netWeight;
	// 品牌名称
	private String brandName;
	//采购单位(00-托盘，01-箱，02-件)
    private String purchaseUnit;
    private String purchaseUnitLabel;
    //总毛重
    private Double grossWeightSum;
	//总净重
    private Double netWeightSum;
    //托盘号
    private String palletNo;
    //箱数
    private Integer cartons;
    //商品规格型号
    private String goodsSpec;
    //采购单价
    private BigDecimal purchasePrice;
    
    //拓展字段
    //条形码
    private String barcode;

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
		if(tallyingUnit != null) {
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit) ;
		}
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

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	/* state get 方法 */
	public String getState() {
		return state;
	}

	/* state set 方法 */
	public void setState(String state) {
		this.state = state;
		if(state != null) {
			this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.declareOrder_stateList, state) ;
		}
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
		if(portDestNo != null) {
			portDestNoLabel = DERP.getLabelByKey(DERP.portDestList, portDestNo) ;
		}
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

	/* purchaseId get 方法 */
	public Long getPurchaseId() {
		return purchaseId;
	}

	/* purchaseId set 方法 */
	public void setPurchaseId(Long purchaseId) {
		this.purchaseId = purchaseId;
	}

	/* code set 方法 */
	public String getStatus() {
		return status;
	}

	/* code set 方法 */
	public void setStatus(String status) {
		this.status = status;
		if(status != null) {
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.declareOrder_statusList, status) ;
		}
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
		if(businessModel != null) {
			this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.declareOrder_businessModelList, businessModel);
		}
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
		if(transport != null) {
			this.transportLabel = DERP_ORDER.getLabelByKey(DERP.transportList, transport) ;
		}
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

	public String getBusinessModelLabel() {
		return businessModelLabel;
	}

	public void setBusinessModelLabel(String businessModelLabel) {
		this.businessModelLabel = businessModelLabel;
	}

	public String getTransportLabel() {
		return transportLabel;
	}

	public void setTransportLabel(String transportLabel) {
		this.transportLabel = transportLabel;
	}

	public String getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(String statusLabel) {
		this.statusLabel = statusLabel;
	}

	public String getPortDestNoLabel() {
		return portDestNoLabel;
	}

	public void setPortDestNoLabel(String portDestNoLabel) {
		this.portDestNoLabel = portDestNoLabel;
	}

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}

	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSubcontractNo() {
		return subcontractNo;
	}

	public void setSubcontractNo(String subcontractNo) {
		this.subcontractNo = subcontractNo;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getConstituentRatio() {
		return constituentRatio;
	}

	public void setConstituentRatio(String constituentRatio) {
		this.constituentRatio = constituentRatio;
	}

	public String getBoxNo() {
		return boxNo;
	}

	public void setBoxNo(String boxNo) {
		this.boxNo = boxNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPurchase() {
		return purchase;
	}

	public void setPurchase(String purchase) {
		this.purchase = purchase;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public Double getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(Double grossWeight) {
		this.grossWeight = grossWeight;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getPurchaseUnit() {
		return purchaseUnit;
	}

	public void setPurchaseUnit(String purchaseUnit) {
		this.purchaseUnit = purchaseUnit;
		this.purchaseUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit) ;
	}

	public String getPurchaseUnitLabel() {
		return purchaseUnitLabel;
	}

	public void setPurchaseUnitLabel(String purchaseUnitLabel) {
		this.purchaseUnitLabel = purchaseUnitLabel;
	}

	public Double getGrossWeightSum() {
		return grossWeightSum;
	}

	public void setGrossWeightSum(Double grossWeightSum) {
		this.grossWeightSum = grossWeightSum;
	}

	public Double getNetWeightSum() {
		return netWeightSum;
	}

	public void setNetWeightSum(Double netWeightSum) {
		this.netWeightSum = netWeightSum;
	}

	public String getPalletNo() {
		return palletNo;
	}

	public void setPalletNo(String palletNo) {
		this.palletNo = palletNo;
	}

	public Integer getCartons() {
		return cartons;
	}

	public void setCartons(Integer cartons) {
		this.cartons = cartons;
	}

	public String getGoodsSpec() {
		return goodsSpec;
	}

	public void setGoodsSpec(String goodsSpec) {
		this.goodsSpec = goodsSpec;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getMotorcycleType() {
		return motorcycleType;
	}

	public void setMotorcycleType(String motorcycleType) {
		this.motorcycleType = motorcycleType;
	}
	
	
}
