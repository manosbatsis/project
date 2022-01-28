package com.topideal.json.api.v2_1;
/**
 * 销售退货 入库申报 mq报文
 * @author lian_
 *
 */
public class SaleStorageDeclareBatchListIson {

	private Integer normalNum;//正常数量
	private String productionDate;//生产日期
	private String overdueDate;//到期日期
	private String batchNo;//批次号，WMS生成
	private Integer wornNum;// 坏货数量	
	private Integer expireNum;// 过期数量

	private String poNo;//po号（非接口字段销售退预申报拆单需要添加的）

	public Integer getNormalNum() {
		return normalNum;
	}
	public void setNormalNum(Integer normalNum) {
		this.normalNum = normalNum;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getOverdueDate() {
		return overdueDate;
	}
	public void setOverdueDate(String overdueDate) {
		this.overdueDate = overdueDate;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Integer getWornNum() {
		return wornNum;
	}
	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}
	public Integer getExpireNum() {
		return expireNum;
	}
	public void setExpireNum(Integer expireNum) {
		this.expireNum = expireNum;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
}
