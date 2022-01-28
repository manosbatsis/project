package com.topideal.json.api.v5_1;



/**
 * MQ初理货接口 ReceiveList
 * 
 * @author 联想302
 *
 */
public class EpassFirstTallyReceiveListJson {
                                      
	private int wornNum;              //   坏货数量
	private int expireNum;            //   过期数量
	private int normalNum;            //   正常数量
	private String productionDate;    //   生产日期
	private String overdueDate;       //   到期日期
	private String batchNo;           //   批次号，WMS生成
	public int getWornNum() {
		return wornNum;
	}
	public void setWornNum(int wornNum) {
		this.wornNum = wornNum;
	}
	public int getExpireNum() {
		return expireNum;
	}
	public void setExpireNum(int expireNum) {
		this.expireNum = expireNum;
	}
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

	
	
}
