package com.topideal.entity.dto.api10001;

public class Detail {
	private Integer num;//正常数量
	private Integer wornNum;//坏品数量
	private Integer expireNum;//过期数量
	private String productionDate;//生产日期
	private String overdueDate;//到期日期
	private String batchNo;//批次号
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
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
   
}
