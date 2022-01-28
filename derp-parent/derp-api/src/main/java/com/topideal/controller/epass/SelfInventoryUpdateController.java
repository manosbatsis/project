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
import com.topideal.service.epass.SelfInventoryUpdateService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 自营库存更新
 */
@RequestMapping("/api/1212")
@Controller
public class SelfInventoryUpdateController {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER=LoggerFactory.getLogger(SelfInventoryUpdateController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private SelfInventoryUpdateService selfInventoryUpdateService;
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	
	//自营库存更新
	@RequestMapping(params="method=erp4716",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103201400",model="自营库存更新",keyword = "order_id")
	public JSONObject getTransfersOutStorage(@RequestBody String json){
		
		//实例化json
		JSONObject jsonData =new JSONObject();	
		LOGGER.info("自营库存更新回推,请求开始json"+json);
		try {
			jsonData = JSONObject.fromObject(json);

			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//单据号
			if (jsonData.get("order_id")==null||StringUtils.isBlank(jsonData.getString("order_id"))) {
				return ResponseFactory.error("order_id", "", "order_id is NULL");
			}
			//单据调整类型
			if (jsonData.get("order_type")==null||StringUtils.isBlank(jsonData.getString("order_type"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "order_type is NULL");
			}
			if(!"05".equals(jsonData.getString("order_type"))&& !"06".equals(jsonData.getString("order_type"))){
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "order_type不为:05、06,不接收");
			}
			//单据调整类型名称
			if (jsonData.get("order_typename")==null||StringUtils.isBlank(jsonData.getString("order_typename"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "order_typename is NULL");
			}
			//仓库编码
			if (jsonData.get("warehouse_id")==null||StringUtils.isBlank(jsonData.getString("warehouse_id"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "warehouse_id is NULL");
			}
			//仓库名称
			if (jsonData.get("warehouse_name")==null||StringUtils.isBlank(jsonData.getString("warehouse_name"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "warehouse_name is NULL");
			}
			//单据日期
			if (jsonData.get("order_date")==null||StringUtils.isBlank(jsonData.getString("order_date"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "order_date is NULL");
			}
			//推送时间 
			if (jsonData.get("send_time")==null||StringUtils.isBlank(jsonData.getString("send_time"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "send_time is NULL");
			}
			//商品信息
			JSONArray jsonDetailList = (JSONArray) jsonData.get("goods_list");
			if (jsonDetailList==null || jsonDetailList.size()==0) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "goods_list is NULL");
			}
			
			for (Object objectDetail : jsonDetailList) {
				JSONObject jsonDetail = (JSONObject) objectDetail;
				//商品货号
				if (jsonDetail.get("goods_id")==null || StringUtils.isBlank(jsonDetail.getString("goods_id"))) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "goods_id is NULL");
				}
				//商品名称
				if (jsonDetail.get("goods_name")==null || StringUtils.isBlank(jsonDetail.getString("goods_name"))) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "goods_name is NULL");
				}
				//数量
				if (jsonDetail.get("store_qty")==null || StringUtils.isBlank(jsonDetail.getString("store_qty"))) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "store_qty is NULL");
				}
				//批次
				/*if (jsonDetail.get("goods_batch")==null || StringUtils.isBlank(jsonDetail.getString("goods_batch"))) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "goods_batch is NULL");
				}*/	
				//商品条码
				if (jsonDetail.get("gbarcode")==null || StringUtils.isBlank(jsonDetail.getString("gbarcode"))) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "gbarcode is NULL");
				}	
				//好品、坏品
				if (jsonDetail.get("is_damage")==null || StringUtils.isBlank(jsonDetail.getString("is_damage"))) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "is_damage is NULL");
				}
				// 如果是效期调整   生产日期和失效日期  原生产日期和原失效日期必填
				if ("06".equals(jsonData.getString("order_type"))) {
					//生产日期
					if (jsonDetail.get("production_time")==null || StringUtils.isBlank(jsonDetail.getString("production_time"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "06效期调整,生产日期 必填 production_time is NULL");
					}
					//失效日期 
					if (jsonDetail.get("exp_time")==null || StringUtils.isBlank(jsonDetail.getString("exp_time"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "06效期调整,失效日期 必填exp_time is NULL");
					}
					//原生产日期 （效期调整前）					
					if (jsonDetail.get("source_production_time")==null || StringUtils.isBlank(jsonDetail.getString("source_production_time"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "06效期调整,原生产日期 必填source_production_time is NULL");
					}
					//原到期日期（效期调整前）
					if (jsonDetail.get("source_exp_date")==null || StringUtils.isBlank(jsonDetail.getString("source_exp_date"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "06效期调整,原生产日期 必填source_exp_date is NULL");
					}
				}
				
			}
			JSONObject jsonMQData = selfInventoryUpdateService.selfInventoryUpdate(jsonData.toString(),merchantId);
			rocketMQProducer.send(jsonMQData.toString(), MQStorageEnum.EPASS_STORAGE_SELF_INVENTORY_PUSH.getTopic(),MQStorageEnum.EPASS_STORAGE_SELF_INVENTORY_PUSH.getTags());
			LOGGER.info("自营库存更新接口,推送MQ报文"+jsonMQData.toString());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("自营库存更新回推", e.getMessage());
			return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), e.getMessage());
		}
		
		return ResponseFactory.success("order_id", jsonData.get("order_id").toString());
	}
}
