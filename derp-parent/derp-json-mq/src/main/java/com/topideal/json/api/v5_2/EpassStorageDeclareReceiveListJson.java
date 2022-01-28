package com.topideal.json.api.v5_2;

/**
 * MQ入库申报接口 12023-调拨
 * 
 * @author 联想302
 *
 */
public class EpassStorageDeclareReceiveListJson {

	private int normalNum;// 正常数量
	private String productionDate;// 生产日期
	private String overdueDate;// 到期日期
	private String batchNo;// 批次号，WMS生成
	private Integer wornNum;// 坏货数量	
	private Integer expireNum;// 过期数量	

	public int getNormalNum() {
		return normalNum;
	}

	public void setNormalNum(int normalNum) {
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

	public Integer getExpireNum() {
		return expireNum;
	}

	public void setExpireNum(Integer expireNum) {
		this.expireNum = expireNum;
	}

	public Integer getWornNum() {
		return wornNum;
	}

	public void setWornNum(Integer wornNum) {
		this.wornNum = wornNum;
	}

}
