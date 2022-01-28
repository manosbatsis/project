package com.topideal.controller.epass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.mongo.entity.ContractNoMongo;
import com.topideal.mongo.entity.LbxNoMongo;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.ContractNoMongoService;
import com.topideal.service.epass.IntoWarehouseStatusService;
import com.topideal.service.epass.LbxNoMongoService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONObject;

/**
 * 进仓单状态回推 (接口)(15)
 * 
 * @author 杨创 2018/5/10
 */
@Controller
@RequestMapping("/api/1203")
public class IntoWarehouseStatusContoller {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(IntoWarehouseStatusContoller.class);
	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	@Autowired
	private IntoWarehouseStatusService intoWarehouseStatusService;// 进仓单状态
	@Autowired
	private LbxNoMongoService lbxNoMongoService;// LbxNoMongo
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	@Autowired
	private ContractNoMongoService contractNoMongoService;// 合同号

	@RequestMapping(params = "method=erp4282", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@SystemControllerLog(point = "12103100200", model = "进仓单状态回推", keyword = "ent_inbound_id")
	public JSONObject getIntoWarehouseStatus(@RequestBody String json) {
		LOGGER.info("进仓单状态回推,请求开始json" + json);
		JSONObject jsonData = new JSONObject();
		try {
			// 获取json对象
			jsonData = JSONObject.fromObject(json);

			// 根据appkey 查询 配置 表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();

			// 企业入仓编号（电商企业采购编号，唯一）
			if (jsonData.get("ent_inbound_id") == null || StringUtils.isBlank(jsonData.getString("ent_inbound_id"))) {
				return ResponseFactory.error("ent_inbound_id", "", "ent_inbound_id is NULL");
			}
			// 进仓状态1：成功；2：失败
			if (jsonData.get("status") == null || StringUtils.isBlank(jsonData.getString("status"))) {
				return ResponseFactory.error("ent_inbound_id", jsonData.getString("ent_inbound_id"), "status is NULL");
			}
			// 进仓单生效日期,格式:yyyy-MM-dd HH:mm:ss。
			if (jsonData.get("inbound_date") == null || StringUtils.isBlank(jsonData.getString("inbound_date"))) {
				return ResponseFactory.error("ent_inbound_id", jsonData.getString("ent_inbound_id"),
						"inbound_date is NULL");
			}

			Map<String, Object> params = new HashMap<>();
			String entInboundId = jsonData.getString("ent_inbound_id");
			LbxNoMongo lbxNoMongInfo = new LbxNoMongo();
			String tag = null;// 判断来源
			String isRookie = null;// 是否为菜鸟仓（1-是，0-否
			String contractNoTag=null;// 合同号查询标识    1.采购
			if (entInboundId.startsWith("CGO")) {// 采购
				tag = "1";
				isRookie = "0";
			} else if (entInboundId.startsWith("XSTO")) {// 销售退货
				tag = "2";
				isRookie = "0";
			} else if (entInboundId.startsWith("DBO")) {// 调拨
				tag = "3";
				isRookie = "0";
			} else {
				params.put("lbxNo", entInboundId);
				lbxNoMongInfo = lbxNoMongoService.getLbxNoMongInfo(params);
				if (lbxNoMongInfo == null) {
					// 查询看是否是已合同号进行匹配
					if (jsonData.get("contract_no")==null||StringUtils.isBlank(jsonData.getString("contract_no"))) {
						return ResponseFactory.error("ent_inbound_id", jsonData.getString("ent_inbound_id"),"订单号没有匹配搭配订单来源并且 合同号(contract_no)为null,非经分的进仓状态");			
					}
					Map<String, Object> contractNoParams = new HashMap<>();
					contractNoParams.put("contractNo", jsonData.getString("contract_no"));	
					List<ContractNoMongo> contractNoMongoList = contractNoMongoService.getContractNoMongoInfoList(contractNoParams);
					if (contractNoMongoList.size()==0) {
						return ResponseFactory.error("ent_inbound_id", jsonData.getString("ent_inbound_id"), "非经分销的进仓状态数据");			
					}
					/*if (contractNoMongoList.size()>1) {
						return ResponseFactory.error("ent_inbound_id", jsonData.getString("ent_inbound_id"), "(进仓状态)查询到有多个相同的合同号");			
					}*/	
					tag="1";
					isRookie="0";
					contractNoTag="1";
				}
				
				if (lbxNoMongInfo != null) {
					// 来自调拨菜鸟
					if ("CGO".equals(lbxNoMongInfo.getType())) {
						tag = "1";
						isRookie = "1";// 菜鸟
					}
					// 来自调拨菜鸟
					if ("DBO".equals(lbxNoMongInfo.getType())) {
						tag = "3";
						isRookie = "1";// 菜鸟
					}
				}
				
			}

			// 获取经分销的JSON
			JSONObject jsonMQData = intoWarehouseStatusService.intoWarehouseStatusInfo(jsonData.toString(), merchantId,
					isRookie,contractNoTag);

			// 采购
			if ("1".equals(tag)) {
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_WAREHOUSE_STATUS_1.getTopic(),MQOrderEnum.EPASS_WAREHOUSE_STATUS_1.getTags());
				LOGGER.info("进仓单状态回推,请求结束json" + jsonMQData.toString());
				return ResponseFactory.success("ent_inbound_id", jsonData.getString("ent_inbound_id"));
			}
			// 销售退货
			if ("2".equals(tag)) {
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_WAREHOUSE_STATUS_2.getTopic(),MQOrderEnum.EPASS_WAREHOUSE_STATUS_2.getTags());
				LOGGER.info("进仓单状态回推,请求结束json" + jsonMQData.toString());
				return ResponseFactory.success("ent_inbound_id", jsonData.getString("ent_inbound_id"));
			}
			// 调拨
			if ("3".equals(tag)) {
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_WAREHOUSE_STATUS_3.getTopic(),MQOrderEnum.EPASS_WAREHOUSE_STATUS_3.getTags());
				LOGGER.info("进仓单状态回推,请求结束json" + jsonMQData.toString());
				return ResponseFactory.success("ent_inbound_id", jsonData.getString("ent_inbound_id"));
			}

			return ResponseFactory.error("ent_inbound_id", jsonData.getString("ent_inbound_id"), "非经分销的单");


		} catch (Exception e) {
			LOGGER.error("进仓单状态回推 异常,错误信息:{}", e.getMessage());
			return ResponseFactory.error("ent_inbound_id", jsonData.getString("ent_inbound_id"), e.getMessage());
		}
		
	}

}
