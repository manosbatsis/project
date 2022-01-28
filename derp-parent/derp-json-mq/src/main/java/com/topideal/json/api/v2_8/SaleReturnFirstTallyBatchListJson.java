package com.topideal.json.api.v2_8;

/**
 *销售退货-- 初理货 商品批次 mq报文
 * 
 * @author 杨创
 */
public class SaleReturnFirstTallyBatchListJson {

	// 坏货数量
	private Integer wornNum;
	// 过期数量
	private Integer expireNum;
	// 正常数量
	private Integer normalNum;
	// 生产日期
	private String productionDate;
	// 到期日期
	private String overdueDate;
	// 批次号，WMS生成
	private String batchNo;

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

}
