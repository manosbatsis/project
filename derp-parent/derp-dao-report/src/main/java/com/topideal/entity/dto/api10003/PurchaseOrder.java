package com.topideal.entity.dto.api10003;

import java.sql.Timestamp;
import java.util.List;

/**
 * 采购表头
 * @author gy
 *
 */
public class PurchaseOrder {

	//订单编号
	private String code ;
	// po号
	private String poNo ;
	//客户、供应商主数据ID
	private String customerMainId ;
	//仓库名称
	private String depotName ;
	//状态
	private String status ;
	//是否完结
	private String isEnd ;
	//完结入库时间
	private Timestamp endDate ;
	//币种
	private String currency ;
	//审核时间
	private Timestamp auditDate ;
	//创建时间
	private Timestamp createDate ;
	//海外仓理货单位
	private String tallyingunit ;
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
	public String getDepotName() {
		return depotName;
	}
	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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
	public List<OrderGoods> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<OrderGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public String getCustomerMainId() {
		return customerMainId;
	}

	public void setCustomerMainId(String customerMainId) {
		this.customerMainId = customerMainId;
	}
}
