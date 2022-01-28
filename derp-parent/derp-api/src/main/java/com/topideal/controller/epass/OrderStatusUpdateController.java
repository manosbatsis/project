package com.topideal.controller.epass;

import java.sql.Timestamp;

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
import com.topideal.common.tools.TimeUtils;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.OutInventoryService;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 单据状态更新接口
 * @author 杨创
 *2021/04/08
 */
@Controller
@RequestMapping("/api/1216")
public class OrderStatusUpdateController {
	 /*
	* 打印日志
	 */
	private static final Logger LOGGER=LoggerFactory.getLogger(OrderStatusUpdateController.class);
	@Autowired
	private RMQProducer rocketRMQProducer;//MQ

	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	
	@RequestMapping(params="method=erp3370",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103110001",model="单据状态更新接口",keyword = "orderId")
	public JSONObject getEnterBorderStatus(@RequestBody String json){
		// 实例化json对象
		JSONObject jsonDate = new JSONObject();		
		try {
			//将字符串转成json对象
			jsonDate = JSONObject.fromObject(json);
			if (jsonDate.get("app_key")==null||StringUtils.isBlank(jsonDate.getString("app_key"))) {
				return ResponseFactory.error("app_key", "", "app_key is NULL");

			}
			
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonDate.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//订单编号
			if (jsonDate.get("orderId")==null||StringUtils.isBlank(jsonDate.getString("orderId"))) {
				return ResponseFactory.error("orderId", "", "order_id is NULL");

			}
			//业务节点编码
			if (jsonDate.get("statusCode")==null||StringUtils.isBlank(jsonDate.getString("statusCode"))) {
				return ResponseFactory.error("orderId", jsonDate.getString("orderId"), "statusCode is NULL");
			}
			String statusCode = jsonDate.getString("statusCode");
			// 非3300 不报异常
			if (!"3300".equals(statusCode)) {
				return ResponseFactory.success("orderId", jsonDate.getString("orderId"));
			}			
			//发货时间
			if (jsonDate.get("updateTime")==null||StringUtils.isBlank(jsonDate.getString("updateTime"))) {
				return ResponseFactory.error("orderId", jsonDate.getString("orderId"), "updateTime is NULL");
			}
			Timestamp deliverDate = TimeUtils.parse(jsonDate.getString("updateTime"), "yyyy-MM-dd HH:mm:ss");
			if (deliverDate==null) {
				return ResponseFactory.error("orderId", jsonDate.getString("orderId"), "updateTime 日期格式不正确");
			}
			
			String orderCode = jsonDate.getString("orderId");
			
			JSONObject jsonMQData=new JSONObject();
	        jsonMQData.put("orderId", jsonDate.getString("orderId"));//订单编号(外部单号)
	        jsonMQData.put("updateTime", jsonDate.getString("updateTime"));//申请时间--申报时间
	        jsonMQData.put("statusCode", jsonDate.getString("statusCode"));//放行状态
	        jsonMQData.put("merchantId", merchantId);//放行状态
	        
			
			
			//来自调拨
			if (orderCode.startsWith("DBO")) {
				rocketRMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_ORDER_STATUS_UPDATE_3.getTopic(),MQOrderEnum.EPASS_ORDER_STATUS_UPDATE_3.getTags());	
			}
			
			if (orderCode.startsWith("XSO")) {// 来自销售 XSO 和预申报 都是 XSOD 都是 以 XSO开头
				rocketRMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_OUT_INVENTORY_2_1.getTopic(),MQOrderEnum.EPASS_OUT_INVENTORY_2_1.getTags());	
			}
			if (orderCode.startsWith("LLD")) {// 来自领料单
				rocketRMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_OUT_INVENTORY_4_1.getTopic(),MQOrderEnum.EPASS_OUT_INVENTORY_4_1.getTags());	
			}

			if (orderCode.startsWith("CGT")) {// 来自采购退货
				rocketRMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_OUT_INVENTORY_1_UPDATE_STATUS.getTopic(),MQOrderEnum.EPASS_OUT_INVENTORY_1_UPDATE_STATUS.getTags());	
			}
			
			
			
			
			// 调MQ
			//rocketRMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTopic(),MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTags());
			//LOGGER.info("进境状态回推接口,推送MQ报文"+jsonMQData.toString());
		} catch (Exception e) {
			LOGGER.error("单据状态更新接口异常:{}", e.getMessage());
			return ResponseFactory.error("orderId", jsonDate.getString("orderId"), e.getMessage());
		}
		
		return ResponseFactory.success("orderId", jsonDate.getString("orderId"));
	}

}
