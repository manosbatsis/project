package com.topideal.entity.vo.sale;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.topideal.common.system.ibatis.PageModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 销售出库
 * 
 * @author lian_
 *
 */
@ApiModel
public class SaleOutDepotModel extends PageModel implements Serializable {

	@ApiModelProperty(value = "调出仓库id") 
	private Long outDepotId;
	@ApiModelProperty(value = "销售类型 1 购销-整批结算 2 代销 3 采购执行 4.购销-实销实结") 
	private String saleType;
	@ApiModelProperty(value = "销售订单id") 
	private Long saleOrderId;
	@ApiModelProperty(value = "客户名称") 
	private String customerName;
	@ApiModelProperty(value = "调出仓库名称") 
	private String outDepotName;
	@ApiModelProperty(value = "PO号") 
	private String poNo;
	@ApiModelProperty(value = "商家ID") 
	private Long merchantId;
	@ApiModelProperty(value = "客户id(供应商)") 
	private Long customerId;
	@ApiModelProperty(value = "创建人")
	private Long creater;
	@ApiModelProperty(value = "发货时间") 
	private Timestamp deliverDate;
	@ApiModelProperty(value = "ID")
	private Long id;
	@ApiModelProperty(value = "状态 017-待出库,018-已出库,025-已签收,026-已上架,027-出库中 006-已删除,028-部分上架") 
	private String status;
	@ApiModelProperty(value = "创建时间") 
	private Timestamp createDate;
	@ApiModelProperty(value = "销售订单编号") 
	private String saleOrderCode;
	@ApiModelProperty(value = "出库清单编号") 
	private String code;
	@ApiModelProperty(value = "商家名称") 
	private String merchantName;
	@ApiModelProperty(value = "物流企业名称") 
	private String logisticsName;
	@ApiModelProperty(value = "唯品账单号") 
	private String vipsBillNo;
	@ApiModelProperty(value = "LBX单号") 
	private String lbxNo;
	@ApiModelProperty(value = "提单号") 
	private String blNo;
	@ApiModelProperty(value = "运单号") 
	private String wayBillNo;
	@ApiModelProperty(value = "进口模式 10：BBC;20：BC 30：保留; 40：CC") 
	private String importMode;
	@ApiModelProperty(value = "退运状态 70：成功90：作废") 
	private String returnStatus;
	@ApiModelProperty(value = "修改时间") 
	private Timestamp modifyDate;
	@ApiModelProperty(value = "签收时间") 
	private Timestamp receiveDate;
	@ApiModelProperty(value = "上架时间") 
	private Timestamp shelfDate;
	@ApiModelProperty(value = "签收人") 
	private Long receiver;
	@ApiModelProperty(value = "上架人") 
	private Long shelf;
	@ApiModelProperty(value = "上架人名称") 
	private String shelfName;
	@ApiModelProperty(value = "签收人名称") 
	private String receiveName;
	@ApiModelProperty(value = "备注") 
	private String remark;
	@ApiModelProperty(value = "销售出库外部单号") 
	private String outExternalCode;

	@ApiModelProperty(value = "po单时间") 
	private Timestamp poDate;

	@ApiModelProperty(value = "表体") 
	private List<SaleOutDepotItemModel> itemList;

	@ApiModelProperty(value = "币种 01 人民币 02 日元 03 澳元 04 美元 05 港币 06 欧元  07 英镑")
	private String currency;

	@ApiModelProperty(value = "订单来源  1手工导入 2.接口回传")
	private String orderSource;

	@ApiModelProperty(value = "审核人ID")
	private Long auditor;

	@ApiModelProperty(value = "审核时间")
	private Timestamp auditDate;

	@ApiModelProperty(value = "审核人名称")
	private String auditName;

	@ApiModelProperty(value = "事业部名称")
	private String buName;

	@ApiModelProperty(value = "事业部id")
	private Long buId;
	
	/**
    * 销售预申报单ID
    */
	@ApiModelProperty(value = "销售预申报单ID")
    private Long saleDeclareOrderId;
    /**
    * 销售预申报单编号
    */
	@ApiModelProperty(value = "销售预申报单编号")
    private String saleDeclareOrderCode;

	@ApiModelProperty(value = "是否红冲单：0-否，1-是")
	private String isWriteOff;

	/**
	 * 原销售出库单id
	 */
	@ApiModelProperty(value = "原销售出库单id")
	private Long originalSaleOutOrderId;
	/**
	 * 原销售出库单号
	 */
	@ApiModelProperty(value = "原销售出库单号")
	private String originalSaleOutOrderCode;

	public String getOutExternalCode() {
		return outExternalCode;
	}

	public void setOutExternalCode(String outExternalCode) {
		this.outExternalCode = outExternalCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Timestamp getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}

	/* shelfDate get 方法 */
	public Timestamp getShelfDate() {
		return shelfDate;
	}

	/* shelfDate set 方法 */
	public void setShelfDate(Timestamp shelfDate) {
		this.shelfDate = shelfDate;
	}

	public Timestamp getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	/* returnStatus get 方法 */
	public String getReturnStatus() {
		return returnStatus;
	}

	/* returnStatus set 方法 */
	public void setReturnStatus(String returnStatus) {
		this.returnStatus = returnStatus;
	}

	/* importMode get 方法 */
	public String getImportMode() {
		return importMode;
	}

	/* importMode set 方法 */
	public void setImportMode(String importMode) {
		this.importMode = importMode;
	}

	/* wayBillNo get 方法 */
	public String getWayBillNo() {
		return wayBillNo;
	}

	/* wayBillNo set 方法 */
	public void setWayBillNo(String wayBillNo) {
		this.wayBillNo = wayBillNo;
	}

	/* blNo get 方法 */
	public String getBlNo() {
		return blNo;
	}

	/* blNo set 方法 */
	public void setBlNo(String blNo) {
		this.blNo = blNo;
	}

	/* lbxNo get 方法 */
	public String getLbxNo() {
		return lbxNo;
	}

	/* lbxNo set 方法 */
	public void setLbxNo(String lbxNo) {
		this.lbxNo = lbxNo;
	}

	/* vipsBillNo get 方法 */
	public String getVipsBillNo() {
		return vipsBillNo;
	}

	/* vipsBillNo set 方法 */
	public void setVipsBillNo(String vipsBillNo) {
		this.vipsBillNo = vipsBillNo;
	}

	/* logisticsName get 方法 */
	public String getLogisticsName() {
		return logisticsName;
	}

	/* logisticsName set 方法 */
	public void setLogisticsName(String logisticsName) {
		this.logisticsName = logisticsName;
	}

	/* merchantName get 方法 */
	public String getMerchantName() {
		return merchantName;
	}

	/* merchantName set 方法 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	/* saleOrderCode get 方法 */
	public String getSaleOrderCode() {
		return saleOrderCode;
	}

	/* saleOrderCode set 方法 */
	public void setSaleOrderCode(String saleOrderCode) {
		this.saleOrderCode = saleOrderCode;
	}

	/* code get 方法 */
	public String getCode() {
		return code;
	}

	/* code set 方法 */
	public void setCode(String code) {
		this.code = code;
	}

	/* outDepotId get 方法 */
	public Long getOutDepotId() {
		return outDepotId;
	}

	/* outDepotId set 方法 */
	public void setOutDepotId(Long outDepotId) {
		this.outDepotId = outDepotId;
	}

	/* saleType get 方法 */
	public String getSaleType() {
		return saleType;
	}

	/* saleType set 方法 */
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	/* saleOrderId get 方法 */
	public Long getSaleOrderId() {
		return saleOrderId;
	}

	/* saleOrderId set 方法 */
	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

	/* customerName get 方法 */
	public String getCustomerName() {
		return customerName;
	}

	/* customerName set 方法 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/* outDepotName get 方法 */
	public String getOutDepotName() {
		return outDepotName;
	}

	/* outDepotName set 方法 */
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
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

	/* customerId get 方法 */
	public Long getCustomerId() {
		return customerId;
	}

	/* customerId set 方法 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	/* creater get 方法 */
	public Long getCreater() {
		return creater;
	}

	/* creater set 方法 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}

	/* deliverDate get 方法 */
	public Timestamp getDeliverDate() {
		return deliverDate;
	}

	/* deliverDate set 方法 */
	public void setDeliverDate(Timestamp deliverDate) {
		this.deliverDate = deliverDate;
	}

	/* id get 方法 */
	public Long getId() {
		return id;
	}

	/* id set 方法 */
	public void setId(Long id) {
		this.id = id;
	}

	/* status get 方法 */
	public String getStatus() {
		return status;
	}

	/* status set 方法 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* createDate get 方法 */
	public Timestamp getCreateDate() {
		return createDate;
	}

	/* createDate set 方法 */
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public List<SaleOutDepotItemModel> getItemList() {
		return itemList;
	}

	public void setItemList(List<SaleOutDepotItemModel> itemList) {
		this.itemList = itemList;
	}

/*	public String getGoodsNo() {
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

	public Integer getTransferNum() {
		return transferNum;
	}

	public void setTransferNum(Integer transferNum) {
		this.transferNum = transferNum;
	}

	public String getGoodsStr() {
		return goodsStr;
	}

	public void setGoodsStr(String goodsStr) {
		this.goodsStr = goodsStr;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public String getTallyingUnit() {
		return tallyingUnit;
	}

	public void setTallyingUnit(String tallyingUnit) {
		this.tallyingUnit = tallyingUnit;
	}*/

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

	public String getIsWriteOff() {
		return isWriteOff;
	}

	public void setIsWriteOff(String isWriteOff) {
		this.isWriteOff = isWriteOff;
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
