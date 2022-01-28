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
import com.topideal.service.epass.OrderCancelStatusService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONObject;

/**
 * 订单取消状态回推接口
 * @author 杨创
 *2018/6/15
 */
@Controller
@RequestMapping("/api/1207")
public class OrderCancelStatusController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderCancelStatusController.class);
	
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private OrderCancelStatusService orderCancelStatusService;//订单取消状态回推接口
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	
	
	/**
	 * 订单取消状态回推接口
	 * @param json
	 * @return
	 */
	@RequestMapping(params="method=erp6054",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
	@SystemControllerLog(point="12103120600",model="订单取消状态回推接口",keyword = "order_id")
	public JSONObject  getOrderCancelStatus (@RequestBody String json){
		
		JSONObject jsonData =new JSONObject();
		
		try {
			jsonData= JSONObject.fromObject(json);
		 
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//订单号
			if (jsonData.get("order_id")==null||StringUtils.isBlank(jsonData.getString("order_id"))) {
				return ResponseFactory.error("","", "order_id is NULL");
			}
			//更新日期 
			if (jsonData.get("update_date")==null||StringUtils.isBlank(jsonData.getString("update_date"))) {
				return ResponseFactory.error("","", "update_date is NULL");
			}
			// 单据状态  90：作废
			if (jsonData.get("status")==null||StringUtils.isBlank(jsonData.getString("status"))) {
				return ResponseFactory.error("","", "status is NULL");
			}
			//单据状态  90：作废
			if (!"90".equals(jsonData.getString("status"))) {
				return ResponseFactory.error("","", "status is not 90");		
			}
			JSONObject jsonMQData = orderCancelStatusService.orderCancelInfo(jsonData.toString(),merchantId);
			rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_OEDER_CANCE_STATUS_2.getTopic(),MQOrderEnum.EPASS_OEDER_CANCE_STATUS_2.getTags());
			LOGGER.info("订单取消状态回推接口,推送MQ报文"+jsonMQData.toString());	

		} catch (Exception e) {
			LOGGER.error("订单取消状态回推接口异常,错误信息{}", e.getMessage());
			return ResponseFactory.error("","", e.getMessage());
		}
				
		return ResponseFactory.success("", "");
		
	}

}
