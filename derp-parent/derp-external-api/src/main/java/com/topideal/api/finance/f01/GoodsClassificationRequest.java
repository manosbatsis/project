package com.topideal.api.finance.f01;

import java.util.List;

/**
 * 商品分类查询接口请求头
 * @author 杨创
 * 2020-01-07
 */
public class GoodsClassificationRequest {
	private String requestTime;//请求时间  日期格式：yyyy-mm-dd HH:mi:ss
	private List<GoodsClassificationItemRequest>goodsList;//查询商品列表，可循环
	
	
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public List<GoodsClassificationItemRequest> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<GoodsClassificationItemRequest> goodsList) {
		this.goodsList = goodsList;
	}
	
}
