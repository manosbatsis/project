package com.topideal.json.api.v6_4;

import java.util.List;

/**
 * 仓储-退运信息
 * 
 * @author 杨创
 *
 */
public class CCReturnMessageRootJson {
	private String orderCode;// 订单号
	private String transferDate;// 理货时间时间格式：yyyy-mm-dd HH:mi:ss
	private String status;// 单据状态10：制单30：提交70：成功90：作废
	private String serveTypes;// 服务类型 10：退运服务（默认）20：销毁服务
								// 30：跨关区转出转关服务(说明销毁服务20是对应仓储的mq,其他是对应订单mq)
	private Long merchantId;// 商家id
	private String merchantName;// 商家名称
	private String depotCode;// 仓库编码(服务类型20：销毁服务必填)
	private String topidealCode;// 卓志编码
	
	private List<CCReturnMessageGoodsListJson>goodsList;

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getServeTypes() {
		return serveTypes;
	}

	public void setServeTypes(String serveTypes) {
		this.serveTypes = serveTypes;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getDepotCode() {
		return depotCode;
	}

	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getTopidealCode() {
		return topidealCode;
	}

	public void setTopidealCode(String topidealCode) {
		this.topidealCode = topidealCode;
	}

	public List<CCReturnMessageGoodsListJson> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<CCReturnMessageGoodsListJson> goodsList) {
		this.goodsList = goodsList;
	}
	
	

}
