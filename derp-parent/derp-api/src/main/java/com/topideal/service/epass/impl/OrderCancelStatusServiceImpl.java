package com.topideal.service.epass.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.epass.OrderCancelStatusService;

import net.sf.json.JSONObject;

/**
 * 订单取消状态回推接口
 * 
 * @author 杨创 2018/6/28
 */
@Service
public class OrderCancelStatusServiceImpl implements OrderCancelStatusService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelStatusServiceImpl.class);
	@Autowired
	private ApiSecretConfigDao apiSecretConfigDao;// api密钥配置
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	/**
	 * 保存订单取消信息
	 */
	@Override
	@SystemServiceLog(point = "1207", model = "订单取消状态回推接口")
	public JSONObject orderCancelInfo(String json,Long merchantId) throws Exception {
		LOGGER.info(" 订单取消状态回推接口Service 请求开始json:" + json);
		// 实例化JSON对象
		JSONObject jsonData = JSONObject.fromObject(json);

		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
 		if (merchantInfoModel == null) {
			LOGGER.error("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
			throw new RuntimeException("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
		}
		

		// 实例化推送MQ的jJSON 对象
		JSONObject jsonMQData = new JSONObject();
		jsonMQData.put("merchantId", merchantId);// 订单号 必填
		jsonMQData.put("merchantName", merchantInfoModel.getName());// 订单号 必填
		jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 订单号 必填
		jsonMQData.put("orderCode", jsonData.getString("order_id"));// 订单号 必填
		jsonMQData.put("updateDate", (String) jsonData.get("update_date"));// 更新时间
																			// 必填
		jsonMQData.put("status", jsonData.getString("status"));// 单价状态 必填//90：作废
		LOGGER.info("订单取消接口 请求结束json:" + jsonMQData);
		return jsonMQData;
	}

}
