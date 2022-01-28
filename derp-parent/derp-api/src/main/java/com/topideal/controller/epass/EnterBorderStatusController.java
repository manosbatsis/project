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
import com.topideal.service.epass.EnterBorderStatusService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONObject;

/**
 * 进境状态回推接口(4)
 * @author 杨创
 *2018/5/17
 */
@Controller
@RequestMapping("/api/1205")
public class EnterBorderStatusController {
	/**
	 * 打印日志
	 */
	private static final Logger LOGGER=LoggerFactory.getLogger(EnterBorderStatusController.class);
	@Autowired
	private RMQProducer rocketRMQProducer;//MQ
	@Autowired
	private EnterBorderStatusService enterBorderStatusService;//进境状态回推
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	
	@RequestMapping(params="method=erp3369",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103100400",model="进境状态回推接口",keyword = "order_id")
	public JSONObject getEnterBorderStatus(@RequestBody String json){
		// 实例化json对象
		JSONObject jsonDate = new JSONObject();		
		try {
			//将字符串转成json对象
			jsonDate = JSONObject.fromObject(json);
			
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonDate.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//订单编号
			if (jsonDate.get("order_id")==null||StringUtils.isBlank(jsonDate.getString("order_id"))) {
				return ResponseFactory.error("order_id", "", "order_id is NULL");
			}
			//申请时间
			if (jsonDate.get("apply_time")==null||StringUtils.isBlank(jsonDate.getString("apply_time"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "apply_time is NULL");
			}
			//放行状态
			if (jsonDate.get("status")==null||StringUtils.isBlank(jsonDate.getString("status"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "status is NULL");
			}
			// 1:国检;2:海关。
			if (jsonDate.get("type")==null||StringUtils.isBlank(jsonDate.getString("type"))) {
				return ResponseFactory.error("order_id", jsonDate.getString("order_id"), "type is NULL");
			}
			// 调MQ
			JSONObject jsonMQData = enterBorderStatusService.enterBorderStatusInfo(jsonDate.toString(),merchantId);
			rocketRMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTopic(),MQOrderEnum.EPASS_ENTER_BORDER_STATUS_2.getTags());
			LOGGER.info("进境状态回推接口,推送MQ报文"+jsonMQData.toString());
		} catch (Exception e) {
			LOGGER.error("进境状态回推接口异常:{}", e.getMessage());
			ResponseFactory.error("order_id", jsonDate.getString("order_id"), e.getMessage());
		}
		
		return ResponseFactory.success("order_id", jsonDate.getString("order_id"));
	}
	
	
}
