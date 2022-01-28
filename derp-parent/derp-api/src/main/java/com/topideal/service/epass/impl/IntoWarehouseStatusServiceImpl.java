package com.topideal.service.epass.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.epass.IntoWarehouseStatusService;

import net.sf.json.JSONObject;

/**
 * 进仓单状态回推
 * 
 * @author 杨创 2018/5/25
 */
@Service
public class IntoWarehouseStatusServiceImpl implements IntoWarehouseStatusService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IntoWarehouseStatusServiceImpl.class);
	@Autowired
	private ApiSecretConfigDao apiSecretConfigDao;// api密钥配置
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	// 进仓单状态回推
	@Override
	@SystemServiceLog(point = "1203", model = "进仓单状态回推接口")
	public JSONObject intoWarehouseStatusInfo(String json,Long merchantId,String isRookie,String contractNoTag) throws Exception {
		/**
		 * (例子 菜鸟的采购订单只会对应一个入库申报单) 说明 菜鸟仓的订单 无论是采购订单模块 销售订单模块 调拨订单模块 都是只对应
		 * 一个采购订单 (销售订单,调拨订单) 只会推一次进仓状态接口
		 */
		LOGGER.info("进仓单状态Service 请求开始json:" + json);
		// 实例化JSON对象
		JSONObject jsonData = JSONObject.fromObject(json);
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
 		if (merchantInfoModel == null) {
			LOGGER.error("商家不存在,企业入仓编号ent_inbound_id" + (String)jsonData.get("ent_inbound_id"));
			throw new RuntimeException("商家不存在,企业入仓编号ent_inbound_id" + (String)jsonData.get("ent_inbound_id"));
		}
						
		// 实例化推送MQ的jJSON 对象
		JSONObject jsonMQData = new JSONObject();
		jsonMQData.put("merchantId", merchantInfoModel.getId());// 商家id
		jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
		jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码
		jsonMQData.put("isRookie", isRookie);// 是否是经分销的单是否为菜鸟仓（1-是，0-否）
		jsonMQData.put("entInboundId", jsonData.getString("ent_inbound_id"));// 企业入仓编号
		jsonMQData.put("status", jsonData.getString("status"));// 进仓状态1：成功；2：失败
		jsonMQData.put("inboundDate", jsonData.getString("inbound_date"));// 进仓单生效日期,格式:yyyy-MM-dd
																			// HH:mm:ss。
		jsonMQData.put("tallyingOrderCode", jsonData.get("asn_no"));// 理货单ID
		jsonMQData.put("notes", jsonData.get("notes"));// 错误信息
		jsonMQData.put("contractNo", jsonData.get("contract_no"));// 合同号	
		jsonMQData.put("contractNoTag", contractNoTag);// 合同号查询标识 1.采购

		return jsonMQData;
	}

}
