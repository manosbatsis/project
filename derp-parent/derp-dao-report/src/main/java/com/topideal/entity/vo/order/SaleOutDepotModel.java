package com.topideal.entity.vo.order;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 销售出库
 * 
 * @author zhanghx
 */
public class SaleOutDepotModel extends PageModel implements Serializable {

	// 调出仓库id
	private Long outDepotId;
	// 销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结
	private String saleType;
	// 销售订单id
	private Long saleOrderId;
	// 客户名称
	private String customerName;
	// 调出仓库名称
	private String outDepotName;
	// PO号
	private String poNo;
	// 商家ID
	private Long merchantId;
	// 客户id(供应商)
	private Long customerId;
	// 创建人
	private Long creater;
	// 发货时间
	private Timestamp deliverDate;
	// id
	private Long id;
	//状态 017-待出库,018-已出库,025-已签收,026-已上架,027-出库中 006-已删除，028-部分上架
	private String status;
	// 创建时间
	private Timestamp createDate;
	// 销售订单编号
	private String saleOrderCode;
	// 出库清单编号
	private String code;
	// 商家名称
	private String merchantName;
	// 物流企业名称
	private String logisticsName;
	// 唯品账单号
	private String vipsBillNo;
	// LBX单号
	private String lbxNo;
	// 提单号
	private String blNo;
	// 运单号
	private String wayBillNo;
	// 进口模式 10：BBC;20：BC 30：保留; 40：CC
	private String importMode;
	// 退运状态 70：成功90：作废
	private String returnStatus;
	// 修改时间
	private Timestamp modifyDate;

	private Timestamp receiveDate;

	// po单时间
	private Timestamp poDate;

	//币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑
	private String currency;

	//订单来源  1手工导入 2.接口回传
	private String orderSource;

	/**
	 * 审核人ID
	 */
	private Long auditor;
	/**
	 * 审核时间
	 */
	private Timestamp auditDate;
	/**
	 * 审核人名称
	 */
	private String auditName;

	//事业部名称
	private String buName;

	/**
	 *  事业部id
	 */
	private Long buId;

	// 销售出库外部单号
	private String outExternalCode;
	//是否红冲单：0-否，1-是
	private String isWriteOff;
	/**
	 * 原销售出库单id
	 */
	private Long originalSaleOutOrderId;
	/**
	 * 原销售出库单号
	 */
	private String originalSaleOutOrderCode;
	private Timestamp shelfDate;//上架日期
	private Long receiver;//签收人
	private Long shelf;//上架人

	private String shelfName;//上架人名称

	private String receiveName;//签收人名称

	private String remark;//备注

	private Long saleDeclareOrderId;//销售预申报单ID

	private String saleDeclareOrderCode;//销售预申报单编号



	public Long getOutDepotId() {
		return outDepotId;
	}

	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOutDepotName() {
		return outDepotName;
	}

	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
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

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Timestamp getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getLogisticsName() {
		return logisticsName;
	}

	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	public String getVipsBillNo() {
		return vipsBillNo;
	}

	public void setVipsBillNo(String vipsBillNo) {
		this.vipsBillNo = vipsBillNo;
	}

	public String getLbxNo() {
		return lbxNo;
	}

	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	public String getBlNo() {
		return blNo;
	}

	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	public String getWayBillNo() {
		return wayBillNo;
	}

	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	public String getImportMode() {
		return importMode;
	}

	public void setImportMode(String importMode) {
		this.importMode = importMode;
	}

	public String getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Timestamp getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Timestamp getPoDate() {
		return poDate;
	}

	public void setPoDate(Timestamp poDate) {
		this.poDate = poDate;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public Long getAuditor() {
		return auditor;
	}

	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditName() {
		return auditName;
	}

	public void setAuditName(String auditName) {
		this.auditName = auditName;
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

	public String getOutExternalCode() {
		return outExternalCode;
	}

	public void setOutExternalCode(String outExternalCode) {
		this.outExternalCode = outExternalCode;
	}

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
	}

	public Timestamp getShelfDate() {
		return shelfDate;
	}

	public void setShelfDate(Timestamp shelfDate) {
		this.shelfDate = shelfDate;
	}

	public Long getReceiver() {
		return receiver;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
	}

	public Long getShelf() {
		return shelf;
	}

	public void setShelf(Long shelf) {
		this.shelf = shelf;
	}

	public String getShelfName() {
		return shelfName;
	}

	public void setShelfName(String shelfName) {
		this.shelfName = shelfName;
	}

	public String getReceiveName() {
		return receiveName;
	}

	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getSaleDeclareOrderId() {
		return saleDeclareOrderId;
	}

	public void setSaleDeclareOrderId(Long saleDeclareOrderId) {
		this.saleDeclareOrderId = saleDeclareOrderId;
	}

	public String getSaleDeclareOrderCode() {
		return saleDeclareOrderCode;
	}

	public void setSaleDeclareOrderCode(String saleDeclareOrderCode) {
		this.saleDeclareOrderCode = saleDeclareOrderCode;
	}

	public Long getOriginalSaleOutOrderId() {
		return originalSaleOutOrderId;
	}

	public void setOriginalSaleOutOrderId(Long originalSaleOutOrderId) {
		this.originalSaleOutOrderId = originalSaleOutOrderId;
	}

	public String getOriginalSaleOutOrderCode() {
		return originalSaleOutOrderCode;
	}

	public void setOriginalSaleOutOrderCode(String originalSaleOutOrderCode) {
		this.originalSaleOutOrderCode = originalSaleOutOrderCode;
	}
}
