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
import com.topideal.service.epass.InventoryResultsPushService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 盘点结果推送
 * @author 杨创
 *2018/7/13
 *
 */
@Controller
@RequestMapping("/api/1211")
public class InventoryResultsPushController {
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResultsPushController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	@Autowired
	private InventoryResultsPushService inventoryResultsPushService;//  盘点结果推送
	
	
	/**
	 * 盘点结果推送
	 * @param json
	 * @return
	 */
	@RequestMapping(params="method=erp3417",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103201300",model="盘点结果推送接口",keyword = "custinventory_code")
	public JSONObject getInventoryResultsPushInfo(@RequestBody String json){
		JSONObject jsonData =new JSONObject();
		try {
			jsonData=jsonData.fromObject(json);
			
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//企业调拨单号 非必填 (盘点指令又可以不经过经分销造单直接推盘点结果)
			/*if (jsonData.get("order_id")==null||StringUtils.isBlank(jsonData.getString("order_id"))) {
				return ResponseFactory.error("", "", "order_id is NULL");
			}
			*/
			// 盘点单号不能为空
			if (jsonData.get("custinventory_code")==null||StringUtils.isBlank(jsonData.getString("custinventory_code"))) {
				return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "custinventory_code is NULL");
			}
			
			//录入日期 yyyy-mm-dd HH:mi:ss
			if (jsonData.get("order_date")==null||StringUtils.isBlank(jsonData.getString("order_date"))) {
				return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "order_date is NULL");
			}
			
			//单据状态 10：制单30：提交70：成功90：作废
			if (jsonData.get("status")==null||StringUtils.isBlank(jsonData.getString("status"))) {
				return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "status is NULL");
			}
			
			//  仓库编码
			if (jsonData.get("warehouse_id")==null||StringUtils.isBlank(jsonData.getString("warehouse_id"))) {
				return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "warehouse_id is NULL");
			}
			
			//服务类型 serve_types 10：个性盘点  20：自主盘点
			if (jsonData.get("serve_types")==null||StringUtils.isBlank(jsonData.getString("serve_types"))) {
				return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "serve_types is NULL");
			}
			
			// 商品信息
			JSONArray  goodList = (JSONArray) jsonData.get("good_list");
			if (goodList==null|| goodList.size()==0) {
				return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "good_list is NULL");
			}
			for (Object good : goodList) {
				JSONObject goodJSONObject = (JSONObject) good;
				// 商品货号
				if (goodJSONObject.get("good_id")==null||StringUtils.isBlank(goodJSONObject.getString("good_id"))) {
					return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "good_id is NULL");
				}
				// 商品名称
				if (goodJSONObject.get("good_name")==null||StringUtils.isBlank(goodJSONObject.getString("good_name"))) {
					return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "good_name is NULL");
				}
				// 系统数量
				if (goodJSONObject.get("amount")==null||StringUtils.isBlank(goodJSONObject.getString("amount"))) {
					return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "amount is NULL");
				}
				// 实盘数量
				if (goodJSONObject.get("realqty")==null||StringUtils.isBlank(goodJSONObject.getString("realqty"))) {
					return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "realqty is NULL");
				}
				// 源批次号
				/*if (goodJSONObject.get("batch_code")==null||StringUtils.isBlank(goodJSONObject.getString("batch_code"))) {
					return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"), "batch_code is NULL");
				}*/
						
			}
			JSONObject jsonMQData = inventoryResultsPushService.InventoryResultsPushInfo(jsonData.toString(),merchantId);
			rocketMQProducer.send(jsonMQData.toString(), MQStorageEnum.EPASS_STORAGE_RESULTS_PUSH.getTopic(),MQStorageEnum.EPASS_STORAGE_RESULTS_PUSH.getTags());
			
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("盘点结果推送异常{}", e.getMessage());
			return ResponseFactory.error("custinventory_code", (String)jsonData.get("custinventory_code"),e.getMessage());
		}
		
		return ResponseFactory.success("custinventory_code", (String)jsonData.get("custinventory_code"));
	}

}
