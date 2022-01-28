package com.topideal.entity.dto.purchase;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 采购入库单
 * @author lianchenxing
 */
@ApiModel
public class PurchaseWarehouseDTO extends PageModel implements Serializable {

	// 仓库名称
	@ApiModelProperty(value="仓库名称", required=false)
	private String depotName;
	// 采购入库编号
	@ApiModelProperty(value="采购入库单号", required=false)
	private String code;
	// 进仓生效日期
	@ApiModelProperty(value="进仓生效日期", required=false)
	private Timestamp inboundDate;
	// 仓库id
	@ApiModelProperty(value="仓库id", required=false)
	private Long depotId;
	// 理货单号
	@ApiModelProperty(value="理货单号", required=false)
	private String tallyingOrderCode;
	// 确认理货时间
	@ApiModelProperty(value="确认理货时间", required=false)
	private Timestamp confirmDate;
	// 预申报单编号
	@ApiModelProperty(value="预申报单编号", required=false)
	private String declareOrderCode;
	// 预申报单id
	@ApiModelProperty(value="预申报单id", required=false)
	private Long declareOrderId;
	// 创建人
	@ApiModelProperty(value="创建人", required=false)
	private Long creater;
	// 理货时间
	@ApiModelProperty(value="理货时间", required=false)
	private Timestamp tallyingDate;
	// id
	@ApiModelProperty(value="采购入库ID", required=false)
	private Long id;
	// 状态 1：待入仓 2：已入仓
	@ApiModelProperty(value="状态 1：待入仓 2：已入仓", required=false)
	private String state;
	@ApiModelProperty(value="状态 中文1：待入仓 2：已入仓", required=false)
	private String stateLabel;
	// 理货单号
	@ApiModelProperty(value="理货单号ID", required=false)
	private Long tallyingOrderId;
	// 创建时间
	@ApiModelProperty(value="创建时间", required=false)
	private Timestamp createDate;
	// 商家ID
	@ApiModelProperty(value="商家ID", required=false)
	private Long merchantId;
	// 商家名称
	@ApiModelProperty(value="商家名称", required=false)
	private String merchantName;
	// LBX单号
	@ApiModelProperty(value="LBX单号", required=false)
	private String lbxNo;
	// 托板数量
	@ApiModelProperty(value="托板数量", required=false)
	private Integer palletNum;
	// 采购勾稽状态(1-是,0-否)
	@ApiModelProperty(value="采购勾稽状态(1-是,0-否)", required=false)
	private String crossStatus;
	@ApiModelProperty(value="采购勾稽状态中文(1-是,0-否)", required=false)
	private String crossStatusLabel;
	// 入库人
	@ApiModelProperty(value="入库人ID", required=false)
	private Long warehouser;
	// 入库时间
	@ApiModelProperty(value="入库时间", required=false)
	private Timestamp warehouseDate;
	// 入库单商品详情表
    //合同号
	@ApiModelProperty(value="合同号", required=false)
    private String contractNo;
    //理货单位 00-托盘 01-箱 02-件
	@ApiModelProperty(value="理货单位 00-托盘 01-箱 02-件", required=false)
    private String tallyingUnit;
	@ApiModelProperty(value="理货单位中文 00-托盘 01-箱 02-件", required=false)
    private String tallyingUnitLabel;
    //修改时间
	@ApiModelProperty(value="修改时间", required=false)
    private Timestamp modifyDate;

	//拓展字段
	@ApiModelProperty(value="采购订单号", required=false)
	private String purchaseCode;
	//拓展字段 入库开始时间
	@ApiModelProperty(value="入库开始时间", required=false)
	private String warehouseStartDate;
	//拓展字段 入库结束时间
	@ApiModelProperty(value="入库结束时间", required=false)
	private String warehouseEndDate;

    //业务模式  1 采购 2 代销
	@ApiModelProperty(value="业务模式  1 采购 2 代销", required=false)
    private String businessModel;
	@ApiModelProperty(value="业务模式中文  1 采购 2 代销", required=false)
    private String businessModelLabel;

	/**
	 * 事业部名称
	 */
	@ApiModelProperty(value="事业部名称", required=false)
	private String buName;
	/**
	 *  事业部id
	 */
	@ApiModelProperty(value="事业部id", required=false)
	private Long buId;
	@ApiModelProperty(hidden=true)
	private List<Long> buIds ;
	
	//勾稽状态1-部分勾稽 2-已勾稽 3-勾稽失败
	@ApiModelProperty(value="采购入库勾稽状态1-未勾稽 2-已勾稽3-勾稽失败", required=false)
	private String correlationStatus;
	@ApiModelProperty(value="采购入库勾稽状态中文1-未勾稽 2-已勾稽3-勾稽失败", required=false)
	private String correlationStatusLabel;

	//外部单号（对应PMS回传tallyOrderCode）
	@ApiModelProperty(value="外部单号", required=false)
	private String extraCode;
	
	@ApiModelProperty(value="表体信息", required=false)
	private List<PurchaseWarehouseItemDTO> itemList;
	@ApiModelProperty(value="批次信息", required=false)
	private List<PurchaseWarehouseBatchModel> batchList;

	@ApiModelProperty(value="采购币种", required=false)
	private String currency;

	@ApiModelProperty(value="是否红冲单：0-否，1-是")
	private String isWriteOff;
	@ApiModelProperty(value="是否红冲单中文")
	private String isWriteOffLabel;

	@ApiModelProperty(value="原入库单号")
	private String oriWarehouseCode;

    public String getWarehouseStartDate() {
		return warehouseStartDate;
	}
	public void setWarehouseStartDate(String warehouseStartDate) {
		this.warehouseStartDate = warehouseStartDate;
	}
	public String getWarehouseEndDate() {
		return warehouseEndDate;
	}
	public void setWarehouseEndDate(String warehouseEndDate) {
		this.warehouseEndDate = warehouseEndDate;
	}
	public Timestamp getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getTallyingUnit() {
		return tallyingUnit;
	}
	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
		if(tallyingUnit != null) {
			this.tallyingUnitLabel = DERP.getLabelByKey(DERP.order_tallyingUnitList, tallyingUnit);
		}
	}
	/*contractNo get 方法 */
    public String getContractNo(){
        return contractNo;
    }
    /*contractNo set 方法 */
    public void setContractNo(String  contractNo){
        this.contractNo=contractNo;
    }
	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public List<PurchaseWarehouseItemDTO> getItemList() {
		return itemList;
	}

	public void setItemList(List<PurchaseWarehouseItemDTO> itemList) {
		this.itemList = itemList;
	}

	/* lbxNo get 方法 */
	public String getLbxNo() {
		return lbxNo;
	}

	/* lbxNo set 方法 */
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	/* palletNum get 方法 */
	public Integer getPalletNum() {
		return palletNum;
	}

	/* palletNum set 方法 */
	public void setPalletNum(Integer palletNum) {
		this.palletNum = palletNum;
	}

	/* crossStatus get 方法 */
	public String getCrossStatus() {
		return crossStatus;
	}

	/* crossStatus set 方法 */
	public void setCrossStatus(String crossStatus) {
		this.crossStatus = crossStatus;
		if(crossStatus != null) {
			this.crossStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_crossStatusList, crossStatus);
		}
	}

	/* warehouser get 方法 */
	public Long getWarehouser() {
		return warehouser;
	}

	/* warehouser set 方法 */
	public void setWarehouser(Long warehouser) {
		this.warehouser = warehouser;
	}

	/* warehouseDate get 方法 */
	public Timestamp getWarehouseDate() {
		return warehouseDate;
	}

	/* warehouseDate set 方法 */
	public void setWarehouseDate(Timestamp warehouseDate) {
		this.warehouseDate = warehouseDate;
	}

	/* depotName get 方法 */
	public String getDepotName() {
		return depotName;
	}

	/* depotName set 方法 */
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* inboundDate get 方法 */
	public Timestamp getInboundDate() {
		return inboundDate;
	}

	/* inboundDate set 方法 */
	public void setInboundDate(Timestamp inboundDate) {
		this.inboundDate = inboundDate;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* tallyingOrderCode get 方法 */
	public String getTallyingOrderCode() {
		return tallyingOrderCode;
	}

	/* tallyingOrderCode set 方法 */
	public void setTallyingOrderCode(String tallyingOrderCode) {
		this.tallyingOrderCode = tallyingOrderCode;
	}

	/* confirmDate get 方法 */
	public Timestamp getConfirmDate() {
		return confirmDate;
	}

	/* confirmDate set 方法 */
	public void setConfirmDate(Timestamp confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getDeclareOrderCode() {
		return declareOrderCode;
	}

	public void setDeclareOrderCode(String declareOrderCode) {
		this.declareOrderCode = declareOrderCode;
	}

	public Long getDeclareOrderId() {
		return declareOrderId;
	}

	public void setDeclareOrderId(Long declareOrderId) {
		this.declareOrderId = declareOrderId;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* tallyingDate get 方法 */
	public Timestamp getTallyingDate() {
		return tallyingDate;
	}

	/* tallyingDate set 方法 */
	public void setTallyingDate(Timestamp tallyingDate) {
		this.tallyingDate = tallyingDate;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* state get 方法 */
	public String getState() {
		return state;
	}

	/* state set 方法 */
	public void setState(String state) {
		this.state = state;
		if(state != null) {
			this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_stateList, state);
		}
	}

	/* tallyingOrderId get 方法 */
	public Long getTallyingOrderId() {
		return tallyingOrderId;
	}

	/* tallyingOrderId set 方法 */
	public void setTallyingOrderId(Long tallyingOrderId) {
		this.tallyingOrderId = tallyingOrderId;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
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

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
        if(businessModel != null) {
        	this.businessModelLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_businessModelList, businessModel);
        }
    }
	public String getStateLabel() {
		return stateLabel;
	}
	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}
	public String getCrossStatusLabel() {
		return crossStatusLabel;
	}
	public void setCrossStatusLabel(String crossStatusLabel) {
		this.crossStatusLabel = crossStatusLabel;
	}
	public String getTallyingUnitLabel() {
		return tallyingUnitLabel;
	}
	public void setTallyingUnitLabel(String tallyingUnitLabel) {
		this.tallyingUnitLabel = tallyingUnitLabel;
	}
	public String getBusinessModelLabel() {
		return businessModelLabel;
	}
	public void setBusinessModelLabel(String businessModelLabel) {
		this.businessModelLabel = businessModelLabel;
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

	public String getCorrelationStatus() {
		return correlationStatus;
	}

	public void setCorrelationStatus(String correlationStatus) {
		this.correlationStatus = correlationStatus;
		this.correlationStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_correlationStatusList, correlationStatus) ;
	}
	public String getCorrelationStatusLabel() {
		return correlationStatusLabel;
	}
	public void setCorrelationStatusLabel(String correlationStatusLabel) {
		this.correlationStatusLabel = correlationStatusLabel;
	}

	public String getExtraCode() {
		return extraCode;
	}

	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}
	public List<Long> getBuIds() {
		return buIds;
	}
	public void setBuIds(List<Long> buIds) {
		this.buIds = buIds;
	}
	public List<PurchaseWarehouseBatchModel> getBatchList() {
		return batchList;
	}
	public void setBatchList(List<PurchaseWarehouseBatchModel> batchList) {
		this.batchList = batchList;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
		this.isWriteOffLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_isWriteOffList, isWriteOff);
	}

	public String getOriWarehouseCode() {
		return oriWarehouseCode;
	}

	public void setOriWarehouseCode(String oriWarehouseCode) {
		this.oriWarehouseCode = oriWarehouseCode;
	}

	public String getIsWriteOffLabel() {
		return isWriteOffLabel;
	}

	public void setIsWriteOffLabel(String isWriteOffLabel) {
		this.isWriteOffLabel = isWriteOffLabel;
	}
}
