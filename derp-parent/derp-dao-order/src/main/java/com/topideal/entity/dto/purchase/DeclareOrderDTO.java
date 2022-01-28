package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.common.PackingDetailsModel;
import com.topideal.entity.vo.purchase.DeclareOrderItemModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.sql.Timestamp;
import java.util.List;

@ApiModel
public class DeclareOrderDTO extends PageModel {
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
	@ApiModelProperty(value = "业务模式中文 1 采购 2 代销", required = false)
	private String businessModelLabel;
	// 修改人
	@ApiModelProperty(value = "修改人", required = false)
	private Long modifier;
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
	// 创建时间
	@ApiModelProperty(value = "创建时间", required = false)
	private Timestamp createDate;
	@ApiModelProperty(value = "创建时间字符串", required = false)
	private String createDateStr;
	// 供应商名称
	@ApiModelProperty(value = "供应商名称", required = false)
	private String supplierName;
	// 仓库名称
	@ApiModelProperty(value = "仓库名称", required = false)
	private String depotName;
	// 修改时间
	@ApiModelProperty(value = "修改时间", required = false)
	private Timestamp modifyDate;
	// 国际物流运输方式 1 空运 2 海运
	@ApiModelProperty(value = "运输方式 1 空运 2 海运", required = false)
	private String transport;
	@ApiModelProperty(value = "运输方式中文 1 空运 2 海运", required = false)
	private String transportLabel;
	// 箱数
	@ApiModelProperty(value = "箱数", required = false)
	private Integer boxNum;
	// 创建人
	@ApiModelProperty(value = "创建人", required = false)
	private Long creater;
	// 订单状态 001待审核 002审核中 003已审核 008已取消
	@ApiModelProperty(value = "订单状态 001待审核 002审核中 003已审核 008已取消", required = false)
	private String status;
	@ApiModelProperty(value = "订单状态中文 001待审核 002审核中 003已审核 008已取消", required = false)
	private String statusLabel;
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
	@ApiModelProperty(value = "提单净重", required = false)
	private Double netWeight;
	// 卸货港编码
	@ApiModelProperty(value = "卸货港编码", required = false)
	private String portDestNo;
	@ApiModelProperty(value = "卸货港编码中文", required = false)
	private String portDestNoLabel;
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
	@ApiModelProperty(value = "接口状态 0-未推接口；1-已推接口；2-接口推送失败", required = false)
	private String state;
	@ApiModelProperty(value = "接口状态 0-未推接口；1-已推接口；2-接口推送失败", required = false)
	private String stateLabel;
	// lbx单号
	@ApiModelProperty(value = "lbx单号", required = false)
	private String lbxNo;
	// 修改人用户名
	@ApiModelProperty(value = "修改人用户名", required = false)
	private String updateName;
	// 创建人用户名
	@ApiModelProperty(value = "创建人用户名", required = false)
	private String createName;
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
	// 提交时间
	@ApiModelProperty(value = "提交时间", required = false)
	private Timestamp submitDate;
	// 理货单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value = "理货单位 00-托盘 01-箱 02-件", required = false)
	private String tallyingUnit;
	@ApiModelProperty(value = "理货单位中文 00-托盘 01-箱 02-件", required = false)
	private String tallyingUnitLabel;
	// 卸货港名称
	@ApiModelProperty(value = "卸货港名称", required = false)
	private String portDest;

	@ApiParam(hidden = true)
	@ApiModelProperty(hidden = true)
	private List<DeclareOrderItemModel> itemList;
	@ApiModelProperty(value="装箱明细")
	private List<PackingDetailsModel> packingList ;

	// 拓展字段
	// 采购订单编码
	@ApiModelProperty(value = "采购订单编码", required = false)
	private String purchaseCode;
	@ApiModelProperty(value = "采购订单创建时间", required = false)
	private Timestamp purchaseCreateDate;
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
	@ApiModelProperty(hidden = true)
	private List<Long> buIds;

	/**
	 * 仓库类型
	 */
	@ApiModelProperty(value = "仓库类型", required = false)
	private String depotType;

	@ApiModelProperty(value = "预计到港时间前端传入字符串，格式yyyy-MM-dd", required = false)
	private String arriveDate2;

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
	@ApiModelProperty(value = "托盘材质中文", required = false)
	private String palletMaterialLabel;

	@ApiModelProperty(value = "预申报单ID,多个以‘,’隔开", required = false)
	private String ids;

	@ApiModelProperty(value = "预申报单需要展示商品ID", required = false)
	private String needsId;

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
	private String tradeTermsLabel;

	/**
	 * 车型 及 数量
	 */
	@ApiModelProperty(value = "车型 及 数量", required = false)
	private String motorcycleType;

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
	    * 预计到港时间
    */
	@ApiModelProperty("预计到港时间")
    private Timestamp arriveDate;
	@ApiModelProperty("预计到港时间字符串")
    private String arriveDateStr;
    /**
    * 工厂提货（装船） 工厂提货确认时间
    */
	@ApiModelProperty("工厂提货（装船） 工厂提货确认时间")
    private Timestamp shipDate;
    /**
    * 清关确认时间
    */
	@ApiModelProperty("清关确认时间")
    private Timestamp customsConfirmDate;
	/**
    * 清关提交时间
    */
	@ApiModelProperty("清关提交时间")
    private Timestamp customsSubmitDate;
    /**
    * 确认申报时间
    */
	@ApiModelProperty("确认申报时间")
    private Timestamp confirmDeclarationDate;
    /**
    * 确认入仓时间
    */
	@ApiModelProperty("确认入仓时间")
    private Timestamp confirmDepotDate;
    /**
    * 确认订舱时间
    */
	@ApiModelProperty("确认订舱时间")
    private Timestamp confirmBookingDate;
    /**
    * 物流委托时间
    */
	@ApiModelProperty("物流委托时间")
    private Timestamp logisticsCommissionDate;
    /**
    * 预计离港时间
    */
	@ApiModelProperty("预计离港时间")
    private Timestamp estimatedDeliveryDate;
	/**
    * 上架时间
    */
	@ApiModelProperty("上架时间")
    private Timestamp shelfDate;

	@ApiModelProperty("仓库进出口指令")
    private String depotIsInOutInstruction;

	@ApiModelProperty("预计入仓时间")
	private Timestamp arriveDepotDate;

	@ApiModelProperty("计划装船时间")
	private Timestamp plannedShipDate;

	@ApiModelProperty(hidden=true)
    private String firstGoodName;
	/**
	 * 车次
	 */
	@ApiModelProperty("车次")
	private String trainNumber;

	/**
	 * 标准箱TEU
	 */
	@ApiModelProperty("标准箱TEU")
	private String standardCaseTeu;

	/**
	 * 承运商
	 */
	@ApiModelProperty("承运商")
	private String carrier;

	// 陆运运输方式 1 空运 2 海运
	@ApiModelProperty(value = "陆运运输方式", required = false)
	private String landTransport;
	private String landTransportLabel;

	/**
	 * 确认订车时间
	 */
	@ApiModelProperty("确认订车时间")
	private Timestamp confirmCatDate;

	/**
	 * 陆运订车物流委托时间
	 */
	@ApiModelProperty("陆运订车物流委托时间")
	private Timestamp landCommissionDate;

	/**
	 * 推送入仓指令时间
	 */
	@ApiModelProperty("推送入仓指令时间")
	private Timestamp pushWarehouseDate;

   /**
    * 到港时间
    */
	@ApiModelProperty("到港时间")
    private Timestamp arriveSeaDate;

	@ApiModelProperty("提货时间")
    private Timestamp pickUpDate;

	public Timestamp getPurchaseCreateDate() {
		return purchaseCreateDate;
	}

	public void setPurchaseCreateDate(Timestamp purchaseCreateDate) {
		this.purchaseCreateDate = purchaseCreateDate;
	}

	public String getTradeTermsLabel() {
		return tradeTermsLabel;
	}

	public void setTradeTermsLabel(String tradeTermsLabel) {
		this.tradeTermsLabel = tradeTermsLabel;
	}

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

	public String getLandTransportLabel() {
		return landTransportLabel;
	}

	public void setLandTransportLabel(String landTransportLabel) {
		this.landTransportLabel = landTransportLabel;
	}

	public String getLandTransport() {
		return landTransport;
	}

	public void setLandTransport(String landTransport) {
		this.landTransport = landTransport;
		if (landTransport != null) {
			this.landTransportLabel = DERP_ORDER.getLabelByKey(DERP.landTransportList, landTransport);
		}
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
		if (tallyingUnit != null) {
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
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
		if (state != null) {
			this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.declareOrder_stateList, state);
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
		if (portDestNo != null) {
			portDestNoLabel = DERP.getLabelByKey(DERP.portDestList, portDestNo);
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
		if (status != null) {
			this.statusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.declareOrder_statusList, status);
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
		if (businessModel != null) {
			this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.declareOrder_businessModelList,
					businessModel);
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
		if (transport != null) {
			this.transportLabel = DERP_ORDER.getLabelByKey(DERP.countryTransportList, transport);
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

	public List<Long> getBuIds() {
		return buIds;
	}

	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
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
		this.palletMaterialLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.order_torrpacktypeList, palletMaterial);
	}

	public String getPalletMaterialLabel() {
		return palletMaterialLabel;
	}

	public void setPalletMaterialLabel(String palletMaterialLabel) {
		this.palletMaterialLabel = palletMaterialLabel;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getNeedsId() {
		return needsId;
	}

	public void setNeedsId(String needsId) {
		this.needsId = needsId;
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
        this.tradeTermsLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.declareorder_tradeTermsList, tradeTerms) ;
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

	public Timestamp getCustomsSubmitDate() {
		return customsSubmitDate;
	}

	public void setCustomsSubmitDate(Timestamp customsSubmitDate) {
		this.customsSubmitDate = customsSubmitDate;
	}

	public Timestamp getShelfDate() {
		return shelfDate;
	}

	public void setShelfDate(Timestamp shelfDate) {
		this.shelfDate = shelfDate;
	}

	public String getDepotIsInOutInstruction() {
		return depotIsInOutInstruction;
	}

	public void setDepotIsInOutInstruction(String depotIsInOutInstruction) {
		this.depotIsInOutInstruction = depotIsInOutInstruction;
	}

	public Double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(Double netWeight) {
		this.netWeight = netWeight;
	}

	public String getFirstGoodName() {
		return firstGoodName;
	}

	public void setFirstGoodName(String firstGoodName) {
		this.firstGoodName = firstGoodName;
	}

	public String getCreateDateStr() {
		return createDateStr;
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
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

	public String getArriveDateStr() {
		return arriveDateStr;
	}

	public void setArriveDateStr(String arriveDateStr) {
		this.arriveDateStr = arriveDateStr;
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

	public List<PackingDetailsModel> getPackingList() {
		return packingList;
	}

	public void setPackingList(List<PackingDetailsModel> packingList) {
		this.packingList = packingList;
	}
}
