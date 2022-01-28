package com.topideal.service;

import java.sql.SQLException;

import com.topideal.entity.dto.ResponseRoot;

import net.sf.json.JSONObject;

public interface OrderDocumentsService {

	/**
	 * 查询采购、销售、电商订单
	 * @param jsonData
	 * @param merchantId
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	ResponseRoot queryOrder(JSONObject jsonData, Long merchantId) throws Exception;

}
