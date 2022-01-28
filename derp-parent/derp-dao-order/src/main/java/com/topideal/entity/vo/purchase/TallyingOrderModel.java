package com.topideal.entity.vo.purchase;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

/**
 * 理货单
 * 
 * @author lianchenxing
 */
public class TallyingOrderModel extends PageModel implements Serializable {

	// 仓库名称
	private String depotName;
	// 理货单号
	private String code;
	// 合同号
	private String contractNo;
	// 仓库id
	private Long depotId;
	// 确认理货时间
	private Timestamp confirmDate;
	// 订单编号
	private String orderCode;
	// 订单ID
	private Long orderId;
	// 创建人
	private Long creater;
	// 理货时间
	private Timestamp tallyingDate;
	// id
	private Long id;
	// 状态 009:待确认 010:已确认 004:已驳回
	private String state;
	// 创建时间
	private Timestamp createDate;
	// 商家ID
	private Long merchantId;
	// 商家名称
	private String merchantName;
	// 1 采购 2调拨 3销售退货
	private String type;
	// 确认人
	private Long confirmUser;
	// 确认人名称
	private String confirmUserName;

	/**
	 * 事业部名称
	 */
	private String buName;
	/**
	 *  事业部id
	 */
	private Long buId;

	// 理货单商品详情表
	private List<TallyingOrderItemModel> itemList;

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

}
