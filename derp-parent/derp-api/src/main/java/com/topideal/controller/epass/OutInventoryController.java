package com.topideal.controller.epass;

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
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.OutInventoryService;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 出库明细
 * @author 杨创 2020.06.18
 */
@Controller
@RequestMapping("/api/1214")
public class OutInventoryController {
	/**
	 * 打印日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OutInventoryController.class);
	@Autowired
	private RMQProducer rocketRMQProducer;// MQ
	@Autowired
	private OutInventoryService outInventoryService;// 出库明细
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置


	/**
	 * 出库明细
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "method=erp8465", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@SystemControllerLog(point = "12103127000", model = "出库明细", keyword = "order_id")
	public JSONObject getLoadingDeliveries(@RequestBody String json) {
		LOGGER.info("出库明细,请求开始json" + json);
		// 实例化json对象
		JSONObject jsonDate = new JSONObject();
		try {
			// 将字符串转成json
			jsonDate = JSONObject.fromObject(json);

			// 根据appkey 查询 配置 表获取商家信息
			String appKey = jsonDate.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();			
			// 出库单唯一编码
			if (jsonDate.get("order_id") == null || StringUtils.isBlank(jsonDate.getString("order_id"))) {
				return ResponseFactory.error("order_id", "", "order_id is null");
			}
			// 系统内部单号
			if (jsonDate.get("order_code") == null || StringUtils.isBlank(jsonDate.getString("order_code"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "order_code is null");
			}
			
			
			// 10：销售出库 20：调拨出库30：退运出库 02:区内跨账册调出
			/*if (jsonDate.get("busi_scene") == null || StringUtils.isBlank(jsonDate.getString("busi_scene"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "busi_scene is null");
			}*/
			
			// 订单创建时间 (发货时间)
			if (jsonDate.get("created_time") == null || StringUtils.isBlank(jsonDate.getString("created_time"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "created_time is null");
			}
			//电商企业编码
			/*if (jsonDate.get("ebc_code") == null || StringUtils.isBlank(jsonDate.getString("ebc_code"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "ebc_code is null");
			}*/
			JSONArray jsonGoodsList = (JSONArray) jsonDate.get("good_list");
			// 返回商品列表。good类型要求返回的对象具体参照实体附录
			if (jsonGoodsList == null || jsonGoodsList.size() == 0) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "good_list is null");
			}
			
			for (Object object : jsonGoodsList) {
				JSONObject jsonGoods = (JSONObject) object;
				// 商品货号
				if (jsonGoods.get("good_id") == null || StringUtils.isBlank(jsonGoods.getString("good_id"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "good_id is null");
				}
				// 数量
				if (jsonGoods.get("amount") == null || StringUtils.isBlank(jsonGoods.getString("amount"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "amount is null");
				}
				// （单价）实际价格
				/*if (jsonGoods.get("unit_price") == null || StringUtils.isBlank(jsonGoods.getString("unit_price"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "unit_price is null");
				}*/
				// 库存类型 0：好品  ,1：坏品
				if (jsonGoods.get("stock_type") == null || StringUtils.isBlank(jsonGoods.getString("stock_type"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "stock_type is null");
				}
			}
			

			String orderCode = jsonDate.getString("order_id");

			if (orderCode.startsWith("DBO")) {// 来自调拨
				// 获取推MQ的json
				JSONObject jsonMQDate = outInventoryService.outInventoryDetail(jsonDate.toString(), merchantId);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_OUT_INVENTORY_3.getTopic(),MQOrderEnum.EPASS_OUT_INVENTORY_3.getTags());
				LOGGER.info("出库明细,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));
			}
			
			if (orderCode.startsWith("XSO")) {// 来自销售 XSO 和预申报 都是 XSOD 都是 以 XSO开头
				JSONObject jsonMQDate = outInventoryService.outInventoryDetail(jsonDate.toString(), merchantId);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_OUT_INVENTORY_2.getTopic(),MQOrderEnum.EPASS_OUT_INVENTORY_2.getTags());
				LOGGER.info("出库明细,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));

			}
			if (orderCode.startsWith("CGT")) {// 来自采购退货
				JSONObject jsonMQDate = outInventoryService.outInventoryDetail(jsonDate.toString(), merchantId);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_OUT_INVENTORY_1.getTopic(),MQOrderEnum.EPASS_OUT_INVENTORY_1.getTags());
				LOGGER.info("出库明细,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));

			}
			if (orderCode.startsWith("LLD")) {// 来自领料单
				JSONObject jsonMQDate = outInventoryService.outInventoryDetail(jsonDate.toString(), merchantId);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_OUT_INVENTORY_4.getTopic(),MQOrderEnum.EPASS_OUT_INVENTORY_4.getTags());
				LOGGER.info("出库明细,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));

			}

									
			return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "非经分销的单");
			

		} catch (Exception e) {
			LOGGER.error("出库明细异常,{}", e.getMessage());
			return ResponseFactory.error("order_id", jsonDate.getString("order_id"), e.getMessage());
		}
		
	}

}
