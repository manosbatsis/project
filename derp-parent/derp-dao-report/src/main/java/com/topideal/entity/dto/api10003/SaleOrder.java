package com.topideal.entity.dto.api10003;

import java.sql.Timestamp;
import java.util.List;

/**
 * 销售表头
 * @author gy
 *
 */
public class SaleOrder {

	//订单编号
	private String code ;
	// po号
	private String poNo ;
	//客户、供应商主数据id
	private String customerMainId ;
	//入库仓库名称
	private String inDepotName ;
	//出库仓库名称
	private String outDepotName ;
	//状态
	private String status ;
	//类型
	private String businessModel ;
	//归属月份
	private String month ;
	//币种
	private String currency ;
	//审核时间
	private Timestamp auditDate ;
	//创建时间
	private Timestamp createDate ;
	//海外仓理货单位
	private String tallyingunit ;
	//合同号
	private String contractNo ;
	//商品
	private List<OrderGoods> goodsList  ;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getCustomerMainId() {
		return customerMainId;
	}
	public void setCustomerMainId(String customerMainId) {
		this.customerMainId = customerMainId;
	}


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Timestamp getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	public String getTallyingunit() {
		return tallyingunit;
	}
	public void setTallyingunit(String tallyingunit) {
		this.tallyingunit = tallyingunit;
	}
	public String getInDepotName() {
		return inDepotName;
	}
	public void setInDepotName(String inDepotName) {
		this.inDepotName = inDepotName;
	}
	public String getOutDepotName() {
		return outDepotName;
	}
	public void setOutDepotName(String outDepotName) {
		this.outDepotName = outDepotName;
	}
	public String getBusinessModel() {
		return businessModel;
	}
	public void setBusinessModel(String businessModel) {
		this.businessModel = businessModel;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public List<OrderGoods> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<OrderGoods> goodsList) {
		this.goodsList = goodsList;
	}
	
	
}
