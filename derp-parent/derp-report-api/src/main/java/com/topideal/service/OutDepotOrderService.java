package com.topideal.service;

import com.topideal.entity.dto.ResponseRoot;

import net.sf.json.JSONObject;

public interface OutDepotOrderService {
	/**
	 * 出库单查询
	 * */
	public ResponseRoot queryOutDepotOrder(JSONObject jsonData, Long merchantId)throws Exception;
}
