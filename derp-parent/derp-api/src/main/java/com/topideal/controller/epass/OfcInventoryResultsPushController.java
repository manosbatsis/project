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

import com.topideal.common.enums.MQStorageEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.OfcInventoryResultsPushService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ofc盘点结果推送
 * @author 杨创
 *2018/11/30
 *
 */
@Controller
@RequestMapping("/api/1214")
public class OfcInventoryResultsPushController {
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(OfcInventoryResultsPushController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	@Autowired
	private OfcInventoryResultsPushService ofcInventoryResultsPushService;//  盘点结果推送
	
	
	/**
	 * ofc盘点结果推送
	 * @param json
	 * @return
	 */
	@RequestMapping(params="method=erp6705",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103201301",model="Ofc盘点结果推送接口",keyword = "inventory_order")
	public JSONObject getOfcInventoryResultsPushInfo(@RequestBody String json){
		JSONObject jsonData =new JSONObject();
		try {
			jsonData=jsonData.fromObject(json);
			
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();

			// 盘点单号不能为空
			if (jsonData.get("inventory_order")==null||StringUtils.isBlank(jsonData.getString("inventory_order"))) {
				return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "inventory_order is NULL");
			}
			
			//仓库编码
			if (jsonData.get("stock_code")==null||StringUtils.isBlank(jsonData.getString("stock_code"))) {
				return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "stock_code is NULL");
			}
			//电商企业编码（商家ID）
			if (jsonData.get("electric_code")==null||StringUtils.isBlank(jsonData.getString("electric_code"))) {
				return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "electric_code is NULL");

			}
			//损益单状态
			if (jsonData.get("profit_loss_status")==null||StringUtils.isBlank(jsonData.getString("profit_loss_status"))) {
				return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "profit_loss_status is NULL");
			}
			if (!"99".equals(jsonData.getString("profit_loss_status"))) {
				return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "ofc盘点结果只接收状态是状态是99(完成)的单");
			}
			
			
			//损溢单完成时间  既发货时间要必填
			if (jsonData.get("galFinishTime")==null||StringUtils.isBlank(jsonData.getString("galFinishTime"))) {
				return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "galFinishTime is NULL");
			}
				
			// 商品信息
			JSONArray  goodList = (JSONArray) jsonData.get("goods_list");
			if (goodList==null|| goodList.size()==0) {
				return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "goods_list is NULL");
			}
			for (Object good : goodList) {
				JSONObject goodJSONObject = (JSONObject) good;
				// 盘点单明细ID
				if (goodJSONObject.get("order_detail_id")==null||StringUtils.isBlank(goodJSONObject.getString("order_detail_id"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "order_detail_id is NULL");
				}
				
				// 商品货号
				if (goodJSONObject.get("good_id")==null||StringUtils.isBlank(goodJSONObject.getString("good_id"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "good_id is NULL");
				}
				
				// 商品名称
				if (goodJSONObject.get("goods_name")==null||StringUtils.isBlank(goodJSONObject.getString("goods_name"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "goods_name is NULL");
				}
				
				// 系统数量
				if (goodJSONObject.get("amount")==null||StringUtils.isBlank(goodJSONObject.getString("amount"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "amount is NULL");
				}
				
				// 实盘数量
				if (goodJSONObject.get("realqty")==null||StringUtils.isBlank(goodJSONObject.getString("realqty"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "realqty is NULL");
				}
				//差异数量
				if (goodJSONObject.get("diversity_number")==null||StringUtils.isBlank(goodJSONObject.getString("diversity_number"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "diversity_number is NULL");
				}
				//生产日期
				/*if (goodJSONObject.get("produced_date")==null||StringUtils.isBlank(goodJSONObject.getString("produced_date"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "produced_date is NULL");
				}*/
				//有效期止
				/*if (goodJSONObject.get("due_date")==null||StringUtils.isBlank(goodJSONObject.getString("due_date"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "due_date is NULL");
				}*/
				// 原因代码
				if (goodJSONObject.get("reason_code")==null||StringUtils.isBlank(goodJSONObject.getString("reason_code"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "reason_code is NULL");
				}
				
				// 是否坏品
				if (goodJSONObject.get("is_damage")==null||StringUtils.isBlank(goodJSONObject.getString("is_damage"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "is_damage is NULL");
				}
				//单位
				if (goodJSONObject.get("unit")==null||StringUtils.isBlank(goodJSONObject.getString("unit"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "unit is NULL");
				}
				//库存业务类型
				if (goodJSONObject.get("stock_busi_type")==null||StringUtils.isBlank(goodJSONObject.getString("stock_busi_type"))) {
					return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"), "stock_busi_type is NULL");
				}
						
			}
			JSONObject jsonMQData = ofcInventoryResultsPushService.getOfcInventoryResultsPushInfo(jsonData.toString());
			rocketMQProducer.send(jsonMQData.toString(), MQStorageEnum.EPASS_OFCSTORAGE_RESULTS_PUSH.getTopic(),MQStorageEnum.EPASS_OFCSTORAGE_RESULTS_PUSH.getTags());
			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("ofc盘点结果推送异常{}", e.getMessage());
			return ResponseFactory.error("inventory_order", (String)jsonData.get("inventory_order"),e.getMessage());
		}
		
		return ResponseFactory.success("inventory_order", (String)jsonData.get("inventory_order"));
	}

}
