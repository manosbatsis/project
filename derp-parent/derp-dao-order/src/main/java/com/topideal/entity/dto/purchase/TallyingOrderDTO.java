package com.topideal.entity.dto.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.ibatis.PageModel;
import com.topideal.entity.vo.purchase.TallyingOrderItemModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 理货单
 * 
 * @author lianchenxing
 */
@ApiModel
public class TallyingOrderDTO extends PageModel implements Serializable {

	// 仓库名称
	@ApiModelProperty(value="仓库名称", required=false)
	private String depotName;
	// 理货单号
	@ApiModelProperty(value="理货单号", required=false)
	private String code;
	// 合同号
	@ApiModelProperty(value="合同号", required=false)
	private String contractNo;
	// 仓库id
	@ApiModelProperty(value="仓库id", required=false)
	private Long depotId;
	// 确认理货时间
	@ApiModelProperty(value="确认理货时间", required=false)
	private Timestamp confirmDate;
	// 订单编号
	@ApiModelProperty(value="订单编号", required=false)
	private String orderCode;
	// 订单ID
	@ApiModelProperty(value="订单ID", required=false)
	private Long orderId;
	// 创建人
	@ApiModelProperty(value="创建人", required=false)
	private Long creater;
	// 理货时间
	@ApiModelProperty(value="理货时间", required=false)
	private Timestamp tallyingDate;
	// id
	@ApiModelProperty(value="订单ID", required=false)
	private Long id;
	// 状态 009:待确认 010:已确认 004:已驳回
	@ApiModelProperty(value="状态 009:待确认 010:已确认 004:已驳回", required=false)
	private String state;
	@ApiModelProperty(value="状态中文 009:待确认 010:已确认 004:已驳回", required=false)
	private String stateLabel;
	// 创建时间
	@ApiModelProperty(value="创建时间", required=false)
	private Timestamp createDate;
	// 商家ID
	@ApiModelProperty(value="商家ID", required=false)
	private Long merchantId;
	// 商家名称
	@ApiModelProperty(value="商家名称", required=false)
	private String merchantName;
	// 1 采购 2调拨 3销售退货
	@ApiModelProperty(value="类型：1 采购 2调拨 3销售退货", required=false)
	private String type;
	@ApiModelProperty(value="类型中文：1 采购 2调拨 3销售退货", required=false)
	private String typeLabel;
	// 确认人
	@ApiModelProperty(value="确认人", required=false)
	private Long confirmUser;
	// 确认人名称
	@ApiModelProperty(value="确认人名称", required=false)
	private String confirmUserName;

	// 理货单商品详情表
	@ApiModelProperty(value="商品明细", required=false)
	private List<TallyingOrderItemModel> itemList;
	@ApiModelProperty(value="商品批次明细", required=false)
	private List<TallyingItemBatchDTO> batchList ;

	// 拓展字段
	// 理货开始时间
	@ApiModelProperty(value="理货开始时间", required=false)
	private String tallyingStartDate;
	// 理货结束时间
	@ApiModelProperty(value="理货结束时间", required=false)
	private String tallyingEndDate;

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
	private List<Long> buIds;

	public String getTallyingStartDate() {
		return tallyingStartDate;
	}

	public void setTallyingStartDate(String tallyingStartDate) {
		this.tallyingStartDate = tallyingStartDate;
	}

	public String getTallyingEndDate() {
		return tallyingEndDate;
	}

	public void setTallyingEndDate(String tallyingEndDate) {
		this.tallyingEndDate = tallyingEndDate;
	}

	public String getConfirmUserName() {
		return confirmUserName;
	}

	public void setConfirmUserName(String confirmUserName) {
		this.confirmUserName = confirmUserName;
	}

	/* confirmUser get 方法 */
	public Long getConfirmUser() {
		return confirmUser;
	}

	/* confirmUser set 方法 */
	public void setConfirmUser(Long confirmUser) {
		this.confirmUser = confirmUser;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/* orderId get 方法 */
	public Long getOrderId() {
		return orderId;
	}

	/* orderId set 方法 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/* type get 方法 */
	public String getType() {
		return type;
	}

	/* type set 方法 */
	public void setType(String type) {
		this.type = type;
		if(type != null) {
			this.typeLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tallyingOrder_typeList, type) ;
		}
	}

	/* itemList get 方法 */
	public List<TallyingOrderItemModel> getItemList() {
		return itemList;
	}

	/* itemList set 方法 */
	public void setItemList(List<TallyingOrderItemModel> itemList) {
		this.itemList = itemList;
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

	/* contractNo get 方法 */
	public String getContractNo() {
		return contractNo;
	}

	/* contractNo set 方法 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/* depotId get 方法 */
	public Long getDepotId() {
		return depotId;
	}

	/* depotId set 方法 */
	public void setDepotId(Long depotId) {
		this.depotId = depotId;
	}

	/* confirmDate get 方法 */
	public Timestamp getConfirmDate() {
		return confirmDate;
	}

	/* confirmDate set 方法 */
	public void setConfirmDate(Timestamp confirmDate) {
		this.confirmDate = confirmDate;
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
			this.stateLabel = DERP_ORDER.getLabelByKey(DERP_ORDER.tallyingOrder_stateList, state) ;
		}
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

	public String getStateLabel() {
		return stateLabel;
	}

	public void setStateLabel(String stateLabel) {
		this.stateLabel = stateLabel;
	}

	public String getTypeLabel() {
		return typeLabel;
	}

	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
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

	public List<TallyingItemBatchDTO> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<TallyingItemBatchDTO> batchList) {
		this.batchList = batchList;
	}
}
