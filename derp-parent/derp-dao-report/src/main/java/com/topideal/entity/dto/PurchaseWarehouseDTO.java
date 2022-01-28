package com.topideal.entity.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.order.PurchaseWarehouseItemModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 采购入库单
 * @author lianchenxing
 */
public class PurchaseWarehouseDTO extends PageModel implements Serializable {

	// 仓库名称
	private String depotName;
	// 采购入库编号
	private String code;
	// 进仓生效日期
	private Timestamp inboundDate;
	// 仓库id
	private Long depotId;
	// 理货单号
	private String tallyingOrderCode;
	// 确认理货时间
	private Timestamp confirmDate;
	// 预申报单编号
	private String declareOrderCode;
	// 预申报单id
	private Long declareOrderId;
	// 创建人
	private Long creater;
	// 理货时间
	private Timestamp tallyingDate;
	// id
	private Long id;
	// 状态 1：待入仓 2：已入仓
	private String state;
	private String stateLabel;
	// 理货单号
	private Long tallyingOrderId;
	// 创建时间
	private Timestamp createDate;
	// 商家ID
	private Long merchantId;
	// 商家名称
	private String merchantName;
	// LBX单号
	private String lbxNo;
	// 托板数量
	private Integer palletNum;
	// 采购勾稽状态(1-是,0-否)
	private String crossStatus;
	private String crossStatusLabel;
	// 入库人
	private Long warehouser;
	// 入库时间
	private Timestamp warehouseDate;
	// 入库单商品详情表
	private List<PurchaseWarehouseItemModel> itemList;
    //合同号
    private String contractNo;
    //理货单位 00-托盘 01-箱 02-件
    private String tallyingUnit;
    private String tallyingUnitLabel;
    //修改时间
    private Timestamp modifyDate;

	//拓展字段
	private String purchaseCode;
	//拓展字段 入库开始时间
	private String warehouseStartDate;
	//拓展字段 入库结束时间
	private String warehouseEndDate;

    //业务模式  1 采购 2 代销
    private String businessModel;
    private String businessModelLabel;
    
	// 创建开始时间
	private Timestamp createStartDate;
	// 创建结束时间
	private Timestamp createEndDate;

	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 *  事业部id
	 */
	private Long buId;

	//勾稽状态1-部分勾稽 2-已勾稽 3-勾稽失败
	private String correlationStatus;
	private String correlationStatusLabel ;
	
	private String purchaseOrderCode ;

	//外部单号（对应PMS回传tallyOrderCode）
	private String extraCode;

	//采购币种
	private String currency;

	//是否红冲单：0-否，1-是
	private String isWriteOff;
	/**
	 * 原入库单号
	 */
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

	public List<PurchaseWarehouseItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<PurchaseWarehouseItemModel> itemList) {
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
	public Timestamp getCreateStartDate() {
		return createStartDate;
	}
	public void setCreateStartDate(Timestamp createStartDate) {
		this.createStartDate = createStartDate;
	}
	public Timestamp getCreateEndDate() {
		return createEndDate;
	}
	public void setCreateEndDate(Timestamp createEndDate) {
		this.createEndDate = createEndDate;
	}

	public String getCorrelationStatus() {
		return correlationStatus;
	}

	public void setCorrelationStatus(String correlationStatus) {
		this.correlationStatus = correlationStatus;
		this.correlationStatusLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseWarehouse_correlationStatusList, correlationStatus) ;
	}

	public String getExtraCode() {
		return extraCode;
	}

	public void setExtraCode(String extraCode) {
		this.extraCode = extraCode;
	}
	public String getCorrelationStatusLabel() {
		return correlationStatusLabel;
	}
	public void setCorrelationStatusLabel(String correlationStatusLabel) {
		this.correlationStatusLabel = correlationStatusLabel;
	}
	public String getPurchaseOrderCode() {
		return purchaseOrderCode;
	}
	public void setPurchaseOrderCode(String purchaseOrderCode) {
		this.purchaseOrderCode = purchaseOrderCode;
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
	}

	public String getOriWarehouseCode() {
		return oriWarehouseCode;
	}

	public void setOriWarehouseCode(String oriWarehouseCode) {
		this.oriWarehouseCode = oriWarehouseCode;
	}
}
