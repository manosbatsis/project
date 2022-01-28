package com.topideal.controller.epass;

import java.util.HashMap;
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
import com.topideal.common.enums.MQStorageEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.mongo.entity.LbxNoMongo;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.LbxNoMongoService;
import com.topideal.service.epass.ReturnMessagePushService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 退运单信息推送接口
 * 
 * @author 杨创 2018/6/15
 */
@Controller
@RequestMapping("/api/1210")
public class ReturnMessagePushController {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnMessagePushController.class);

	@Autowired
	private RMQProducer rocketMQProducer;// MQ
	@Autowired
	private ReturnMessagePushService returnMessagePushService;// 退运信息
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	@Autowired
	private LbxNoMongoService lbxNoMongoService;// LbxNoMongo

	/**
	 * 退运单信息推送接口
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "method=erp4991", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@SystemControllerLog(point = "12103100700", model = "退运单信息推送接口", keyword = "order_id")
	public JSONObject getReturnMessagePush(@RequestBody String json) {
		System.out.println("退运单信息推送接口" + json);
		// 创建json对象
		JSONObject jsonData = new JSONObject();
		try {
			jsonData = JSONObject.fromObject(json);

			// 根据appkey 查询 配置 表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();

			// 企业调拨单号order_id
			if (jsonData.get("order_id") == null || StringUtils.isBlank(jsonData.getString("order_id"))) {
				return ResponseFactory.error("order_id", "", "order_id is NULL");
			}

			// 录入日期odate
			if (jsonData.get("odate") == null || StringUtils.isBlank(jsonData.getString("odate"))) {
				return ResponseFactory.error("order_id", jsonData.getString("order_id"), "odate is NULL");
			}
			// 单据状态 status 10：制单30：提交70：成功90：作废
			if (jsonData.get("status") == null || StringUtils.isBlank(jsonData.getString("status"))) {
				return ResponseFactory.error("order_id", jsonData.getString("order_id"), "status is NULL");
			}
			// 申报地海关 BBC关区码表： 埔开发区：5208 南沙旅检：5165
			/*if (jsonData.get("customs_code") == null || StringUtils.isBlank(jsonData.getString("customs_code"))) {
				return ResponseFactory.error("order_id", jsonData.getString("order_id"), "customs_code is NULL");
			}*/
			// 申报地国检 BBC国检码表：黄埔局本部：000072 南沙局本部：000069
			/*if (jsonData.get("ciqb_code") == null || StringUtils.isBlank(jsonData.getString("ciqb_code"))) {
				return ResponseFactory.error("order_id", jsonData.getString("order_id"), "ciqb_code is NULL");
			}*/
			// 10：退运服务（默认） 20：销毁服务 30：跨关区转出转关服务
			if (jsonData.get("serve_types") == null || StringUtils.isBlank(jsonData.getString("serve_types"))) {
				return ResponseFactory.error("order_id", jsonData.getString("order_id"), "serve_types is NULL");
			}
			// 如果 是销毁服务20 只接收 单据状态是70 的单据
			if ("20".equals(jsonData.getString("serve_types"))) {
				if (!"70".equals(jsonData.getString("status"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "销毁服务20  只接收 单据状态是70 的单据");
				}
				String depotCode = (String) jsonData.get("ware_houseId");

				if (jsonData.get("ware_houseId") == null || StringUtils.isBlank(jsonData.getString("ware_houseId"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "服务类型是20销毁服务 仓库编码要必填");
				}
			}
			// 商品信息
			/*JSONArray goodList = (JSONArray) jsonData.get("good_list");
			if (goodList == null || goodList.size() == 0) {
				return ResponseFactory.error("order_id", jsonData.getString("order_id"), "good_list is NULL");
			}

			for (Object good : goodList) {
				JSONObject goodJSONObject = (JSONObject) good;
				// 商品货号
				if (goodJSONObject.get("good_id") == null || StringUtils.isBlank(goodJSONObject.getString("good_id"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "good_id is NULL");
				}
				// 库存类型 0:好品;1:坏品;
				if (goodJSONObject.get("stock_type") == null
						|| StringUtils.isBlank(goodJSONObject.getString("stock_type"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "stock_type is NULL");
				}
			}*/
			// 商品信息
			JSONArray wmsDetailList = (JSONArray) jsonData.get("wms_detail_list");
			// 判断商品批次是否为空
			if (wmsDetailList == null || wmsDetailList.size() == 0) {
				return ResponseFactory.error("order_id", jsonData.getString("order_id"), "wms_detail_list is NULL");
			}	
			for (Object detail : wmsDetailList) {
				JSONObject wmsGoodJSONObject = (JSONObject) detail;
				// 商品货号 
				if (wmsGoodJSONObject.get("good_id") == null || StringUtils.isBlank(wmsGoodJSONObject.getString("good_id"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "good_id is NULL");
				}
				//WMS退运数量
				if (wmsGoodJSONObject.get("wms_qtp") == null || StringUtils.isBlank(wmsGoodJSONObject.getString("wms_qtp"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "wms_qtp is NULL");
				}
				//WMS退运批次
				/*if (wmsGoodJSONObject.get("wms_lot_no") == null || StringUtils.isBlank(wmsGoodJSONObject.getString("wms_lot_no"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "wms_lot_no is NULL");
				}*/
				// 库存类型 0:好品;1:坏品;
				if (wmsGoodJSONObject.get("stock_type") == null
						|| StringUtils.isBlank(wmsGoodJSONObject.getString("stock_type"))) {
					return ResponseFactory.error("order_id", jsonData.getString("order_id"), "stock_type is NULL");
				}
			}
			Map<String, Object> params = new HashMap<>();
			String orderCode = jsonData.getString("order_id");
			LbxNoMongo lbxNoMongInfo = new LbxNoMongo();
			String isRookie = null;// 是否为菜鸟仓（1-是，0-否)

			/*if (orderCode.startsWith("DBO")) {// 来自调拨
				isRookie = "0";
				// 获取推MQ的json
				JSONObject jsonMQData = returnMessagePushService.returnMessagePushInfo(jsonData.toString(), merchantId,
						isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_3.getTopic(),MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_3.getTags());// 来自调拨的退运信息
				LOGGER.info("调拨退运信息接口,推送MQ报文" + jsonMQData.toString());
				return ResponseFactory.success("order_id", jsonData.getString("order_id"));
			}
*/
			/*if (orderCode.startsWith("XSO")) {// 来自销售的
				isRookie = "0";
				// 获取推MQ的json
				JSONObject jsonMQData = returnMessagePushService.returnMessagePushInfo(jsonData.toString(), merchantId,
						isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_2.getTopic(),MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_2.getTags());// 来自销售的退运信息
				LOGGER.info("销售退运信息接口,推送MQ报文" + jsonMQData.toString());
				return ResponseFactory.success("order_id", jsonData.getString("order_id"));
			}*/
			/*params.put("lbxNo", orderCode);
			lbxNoMongInfo = lbxNoMongoService.getLbxNoMongInfo(params);
			// 来自调拨菜鸟
			if (lbxNoMongInfo != null && "DBO".equals(lbxNoMongInfo.getType())) {
				isRookie = "1";
				// 获取推MQ的json
				JSONObject jsonMQData = returnMessagePushService.returnMessagePushInfo(jsonData.toString(), merchantId,
						isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_3.getTopic(),MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_3.getTags());// 来自调拨菜鸟的
				LOGGER.info("调拨菜鸟退运信息接口,推送MQ报文" + jsonMQData.toString());
				return ResponseFactory.success("order_id", jsonData.getString("order_id"));

			}*/
			// 来自销售菜鸟
			/*if (lbxNoMongInfo != null && "XSO".equals(lbxNoMongInfo.getType())) {
				isRookie = "1";
				// 获取推MQ的json
				JSONObject jsonMQData = returnMessagePushService.returnMessagePushInfo(jsonData.toString(), merchantId,
						isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_2.getTopic(),MQOrderEnum.EPASS_RETURN_MESSAGE_PUSH_2.getTags());// 来自销售菜鸟的退运信息
				LOGGER.info("销售菜鸟退运信息接口,推送MQ报文" + jsonMQData.toString());
				return ResponseFactory.success("order_id", jsonData.getString("order_id"));
			}*/

			// 服务类型 10：退运服务（默认）20：销毁服务 30：跨关区转出转关服务(说明销毁服务20是对应仓储的mq,其他是对应订单mq)
			// 仓储的退运信息MQ
			String serveTypes = jsonData.getString("serve_types");
			if ("20".equals(serveTypes)) {
				isRookie = "0";
				JSONObject jsonMQData = returnMessagePushService.returnMessagePushInfo(jsonData.toString(), merchantId,
						isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQStorageEnum.EPASS_STORAGE_RETURN_MESSAGE_PUSH.getTopic(),MQStorageEnum.EPASS_STORAGE_RETURN_MESSAGE_PUSH.getTags());// 仓储的退运信息MQ

				LOGGER.info("仓储退运信息接口,推送MQ报文" + jsonMQData.toString());
				return ResponseFactory.success("order_id", jsonData.getString("order_id"));
			}
			
			return ResponseFactory.error("order_id", jsonData.getString("order_id"), "非经分销的单");

		} catch (Exception e) {
			LOGGER.error("退运单信息推送接口异常,错误信息:{}", e.getMessage());
			return ResponseFactory.error("order_id", (String) jsonData.get("order_id"), e.getMessage());
		}
	}

}
