package com.topideal.json.inventory.j07;

import java.io.Serializable;
import java.util.Map;

/**
 *  库存红冲接口ROOT实体
 * @author 联想302
 *
 */
public class InventoryWriteOffRootJson implements Serializable{	
	private String merchantId; //商家ID
	private String merchantName;//商家名称
	private String sourceOrderNo;//原出入库订单号（红冲单号对应的出入库单号）
	private String orderNo;// 红冲单号
	private String divergenceDate;//出入时间  yyyy-MM-dd HH:mm:ss
	/*回调业务端参数 start*/
	private String backTopic;//回调主题
	private String backTags;//回调标签
	private Map<String, Object> customParam;//自定义回调参数
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDivergenceDate() {
		return divergenceDate;
	}
	public void setDivergenceDate(String divergenceDate) {
		this.divergenceDate = divergenceDate;
	}
	public String getBackTopic() {
		return backTopic;
	}
	public void setBackTopic(String backTopic) {
		this.backTopic = backTopic;
	}
	public String getBackTags() {
		return backTags;
	}
	public void setBackTags(String backTags) {
		this.backTags = backTags;
	}
	public Map<String, Object> getCustomParam() {
		return customParam;
	}
	public void setCustomParam(Map<String, Object> customParam) {
		this.customParam = customParam;
	}
	public String getSourceOrderNo() {
		return sourceOrderNo;
	}
	public void setSourceOrderNo(String sourceOrderNo) {
		this.sourceOrderNo = sourceOrderNo;
	}
	
	

	
	
}
