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
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.mongo.entity.LbxNoMongo;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.LbxNoMongoService;
import com.topideal.service.epass.LoadingDeliveriesService;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 装载交运回推接口(5)
 * 
 * @author 杨创 2018/5/17
 */
@Controller
@RequestMapping("/api/1206")
public class LoadingDeliveriesController {
	/**
	 * 打印日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadingDeliveriesController.class);
	@Autowired
	private RMQProducer rocketRMQProducer;// MQ
	@Autowired
	private LoadingDeliveriesService loadingDeliveriesService;// 装载交运
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	@Autowired
	private LbxNoMongoService lbxNoMongoService;// LbxNoMongo

	/**
	 * 装载交运接口
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(params = "method=erp1852", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	@SystemControllerLog(point = "12103100500", model = "装载交运接口", keyword = "order_id")
	public JSONObject getLoadingDeliveries(@RequestBody String json) {
		LOGGER.info("装载交运接口,请求开始json" + json);
		// 实例化json对象
		JSONObject jsonDate = new JSONObject();
		try {
			// 将字符串转成json
			jsonDate = JSONObject.fromObject(json);

			// 根据appkey 查询 配置 表获取商家信息
			String appKey = jsonDate.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			// 订单号
			if (jsonDate.get("order_id") == null || StringUtils.isBlank(jsonDate.getString("order_id"))) {
				return ResponseFactory.error("order_id", "", "order_id is null");
			}
			// 运单号 运单号在香港仓的时候是非必填的
			/*if (jsonDate.get("way_bill_no") == null || StringUtils.isBlank(jsonDate.getString("way_bill_no"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "way_bill_no is null");
			}*/
			// 业务类型10：B2B ,20：B2B2C
			if (jsonDate.get("app_type") == null || StringUtils.isBlank(jsonDate.getString("app_type"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "app_type is null");
			}
			JSONArray jsonGoodsList = (JSONArray) jsonDate.get("goods");
			// 返回商品列表。good类型要求返回的对象具体参照实体附录
			if (jsonGoodsList == null || jsonGoodsList.size() == 0) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "goods is null");
			}
			/****************************************电商订单装载交运开始*************************************************************/
			String appType = jsonDate.getString("app_type");
			String isRookie = null;// 是否为菜鸟仓（1-是，0-否)
			if ("20".equals(appType)) {
				isRookie = "1";// 菜鸟				
				for (Object object : jsonGoodsList) {
					JSONObject jsonGoods = (JSONObject) object;
					// 商品编码
					if (jsonGoods.get("good_no") == null || StringUtils.isBlank(jsonGoods.getString("good_no"))) {
						return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "good_no is null");
					}
					// 数量
					if (jsonGoods.get("amount") == null || StringUtils.isBlank(jsonGoods.getString("amount"))) {
						return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "amount is null");
					}
					// （单价）实际价格
					if (jsonGoods.get("unit_price") == null || StringUtils.isBlank(jsonGoods.getString("unit_price"))) {
						return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "unit_price is null");
					}
					// 批次号
					/*if (jsonGoods.get("batch_id") == null || StringUtils.isBlank(jsonGoods.getString("batch_id"))) {
						return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "batch_id is null");
					}*/
				}
				JSONObject jsonMQDate=loadingDeliveriesService.loadingDeliveriesInfo(jsonDate.toString(), merchantId,
						isRookie);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_ROOKIE_LOADINF_DELIVRIES_2_1.getTopic(),MQOrderEnum.EPASS_ROOKIE_LOADINF_DELIVRIES_2_1.getTags());
				LOGGER.info("菜鸟仓电商订单装载交运接口,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));
			}
			

			/****************************************电商订单装载交运结束*************************************************************/	
			
			
			
			
			for (Object object : jsonGoodsList) {
				JSONObject jsonGoods = (JSONObject) object;
				// 商品编码
				if (jsonGoods.get("good_no") == null || StringUtils.isBlank(jsonGoods.getString("good_no"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "good_no is null");
				}
				// 数量
				if (jsonGoods.get("amount") == null || StringUtils.isBlank(jsonGoods.getString("amount"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "amount is null");
				}
				// （单价）实际价格
				if (jsonGoods.get("unit_price") == null || StringUtils.isBlank(jsonGoods.getString("unit_price"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "unit_price is null");
				}
				// 批次号
				/*if (jsonGoods.get("batch_id") == null || StringUtils.isBlank(jsonGoods.getString("batch_id"))) {
					return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "batch_id is null");
				}*/
			}

			Map<String, Object> params = new HashMap<>();
			String orderCode = jsonDate.getString("order_id");
			LbxNoMongo lbxNoMongInfo = new LbxNoMongo();
			String tag = null;
			//String isRookie = null;// 是否为菜鸟仓（1-是，0-否)	
		
			if (orderCode.startsWith("DBO")) {// 来自调拨
				//tag = "1";
				isRookie = "0";
				// 获取推MQ的json
				JSONObject jsonMQDate = loadingDeliveriesService.loadingDeliveriesInfo(jsonDate.toString(), merchantId,
						isRookie);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_LOADINF_DELIVRIES_3.getTopic(),MQOrderEnum.EPASS_LOADINF_DELIVRIES_3.getTags());
				LOGGER.info("装载交运接口,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));
			}
			
			if (orderCode.startsWith("XSO")) {// 来自销售 XSO 和预申报 都是 XSOD 都是 以 XSO开头
				//tag = "2";
				isRookie = "0";
				JSONObject jsonMQDate = loadingDeliveriesService.loadingDeliveriesInfo(jsonDate.toString(), merchantId,
						isRookie);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_LOADINF_DELIVRIES_2_2.getTopic(),MQOrderEnum.EPASS_LOADINF_DELIVRIES_2_2.getTags());
				LOGGER.info("装载交运接口,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));

			}
			if (orderCode.startsWith("CGT")) {// 来自采购退货
				JSONObject jsonMQDate = loadingDeliveriesService.loadingDeliveriesInfo(jsonDate.toString(), merchantId,null);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_LOADINF_DELIVRIES_1.getTopic(),MQOrderEnum.EPASS_LOADINF_DELIVRIES_1.getTags());
				LOGGER.info("出库明细,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));

			}
			params.put("lbxNo", orderCode);
			lbxNoMongInfo = lbxNoMongoService.getLbxNoMongInfo(params);

			// 来自调拨菜鸟
			if (lbxNoMongInfo != null && "DBO".equals(lbxNoMongInfo.getType())) {
				//tag = "1";
				isRookie = "1";// 菜鸟
				JSONObject jsonMQDate = loadingDeliveriesService.loadingDeliveriesInfo(jsonDate.toString(), merchantId,
						isRookie);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_LOADINF_DELIVRIES_3.getTopic(),MQOrderEnum.EPASS_LOADINF_DELIVRIES_3.getTags());
				LOGGER.info("装载交运接口,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));
			}
			
			// 来自电商订单
			//String appType = jsonDate.getString("app_type");
			// 说明是经分销的单
			/*if ("20".equals(appType)) {
				isRookie = "1";// 菜鸟
				JSONObject jsonMQDate = loadingDeliveriesService.loadingDeliveriesInfo(jsonDate.toString(), merchantId,
						isRookie);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_LOADINF_DELIVRIES_2_1.getTopic(),MQOrderEnum.EPASS_LOADINF_DELIVRIES_2_1.getTags());
				LOGGER.info("装载交运接口,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));	
			}*/
			
			if (orderCode.startsWith("LLD")) {// 来自领料单
				JSONObject jsonMQDate = loadingDeliveriesService.loadingDeliveriesInfo(jsonDate.toString(), merchantId,null);
				rocketRMQProducer.send(jsonMQDate.toString(), MQOrderEnum.EPASS_LOADINF_DELIVRIES_4.getTopic(),MQOrderEnum.EPASS_LOADINF_DELIVRIES_4.getTags());
				LOGGER.info("出库明细,推送MQ报文" + jsonMQDate.toString());
				return ResponseFactory.success("order_id", jsonDate.getString("order_id"));

			}	
			return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "非经分销的单");
		

		} catch (Exception e) {
			LOGGER.error("装载交运回推接口异常,{}", e.getMessage());
			return ResponseFactory.error("order_id", jsonDate.getString("order_id"), e.getMessage());
		}
		
	}

}
