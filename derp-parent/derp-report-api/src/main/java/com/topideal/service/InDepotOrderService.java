package com.topideal.service;

import com.topideal.entity.dto.ResponseRoot;

import net.sf.json.JSONObject;

/**
 * 入库单查询
 * @author 杨创
 *2019.04.11
 */
public interface InDepotOrderService {
	/**
	 * 入库单查询
	 * @param jsonData
	 * @return
	 * @throws Exception
	 */
	ResponseRoot queryInDepotOrder(JSONObject jsonData, Long merchantId)throws Exception;

}
