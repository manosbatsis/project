package com.topideal.api.op.v2_33;

import java.io.Serializable;
import java.util.List;

/**
 * 调拨指令推送接口实体类
 * @author 杨创
 *2018/4/3
 */
public class TransfersInstructionsRequest implements Serializable {

	private Integer fid;//
	// 企业调拨单号
	private String orderId;
	// 录入时间
	private String odate;
	// 申报海关
	private String customsCode;
	// 申报国检
	private String ciqbCode;
	// 业务场景
	private String busiScene;
	// 服务类型
	private String serveTypes;
	// 调出电商企业
	private String fromElectricCode;
	// 调入电商企业
	private String toElectricEode;
	// 委托单位
	private String trustCode;
	// 调出仓库
	private String fromStoreCode;
	// 调入仓库
	private String toStoreCode;
	// 合同号
	private String barGainNo;
	// 商品列表
	private List<TransfersInstructionsGoodRequest> goodList;
	public Integer getFid() {
		return fid;
	}
	public void setFid(Integer fid) {
		this.fid = fid;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOdate() {
		return odate;
	}
	public void setOdate(String odate) {
		this.odate = odate;
	}
	public String getCustomsCode() {
		return customsCode;
	}
	public void setCustomsCode(String customsCode) {
		this.customsCode = customsCode;
	}
	public String getCiqbCode() {
		return ciqbCode;
	}
	public void setCiqbCode(String ciqbCode) {
		this.ciqbCode = ciqbCode;
	}
	public String getBusiScene() {
		return busiScene;
	}
	public void setBusiScene(String busiScene) {
		this.busiScene = busiScene;
	}
	public String getServeTypes() {
		return serveTypes;
	}
	public void setServeTypes(String serveTypes) {
		this.serveTypes = serveTypes;
	}
	public String getFromElectricCode() {
		return fromElectricCode;
	}
	public void setFromElectricCode(String fromElectricCode) {
		this.fromElectricCode = fromElectricCode;
	}
	public String getToElectricEode() {
		return toElectricEode;
	}
	public void setToElectricEode(String toElectricEode) {
		this.toElectricEode = toElectricEode;
	}
	public String getTrustCode() {
		return trustCode;
	}
	public void setTrustCode(String trustCode) {
		this.trustCode = trustCode;
	}
	public String getFromStoreCode() {
		return fromStoreCode;
	}
	public void setFromStoreCode(String fromStoreCode) {
		this.fromStoreCode = fromStoreCode;
	}
	public String getToStoreCode() {
		return toStoreCode;
	}
	public void setToStoreCode(String toStoreCode) {
		this.toStoreCode = toStoreCode;
	}
	public String getBarGainNo() {
		return barGainNo;
	}
	public void setBarGainNo(String barGainNo) {
		this.barGainNo = barGainNo;
	}
	public List<TransfersInstructionsGoodRequest> getGoodList() {
		return goodList;
	}
	public void setGoodList(List<TransfersInstructionsGoodRequest> goodList) {
		this.goodList = goodList;
	}

	
}
