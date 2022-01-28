package com.topideal.controller.dev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONObject;
/**
 * 第e仓手动触发抓取日期
 * @author 杨创
 *2018/10/8
 */
@Controller
@RequestMapping("/derpapi/1001")
public class EwsManualController {
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	
	@RequestMapping(params="method=erp1001",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public JSONObject getEwsManual (@RequestBody String json){
		JSONObject jsonData=new JSONObject();
		JSONObject jsonMQData=new JSONObject();
		try {
			jsonData = JSONObject.fromObject(json);
			String merchantCode = (String)jsonData.get("merchant_code");
			String startTime = (String)jsonData.get("start_time");
			String endTime = (String)jsonData.get("end_time");
			String orderId = (String)jsonData.get("order_id");
			
			if (merchantCode==null) {
				return ResponseFactory.error("order_id",(String)jsonData.get("order_id") ,"merchant_code卓志编码不能为空");
			}
			jsonMQData.put("tag", "2");// "1" 定时器动触发 ,"2"手动触发 
			jsonMQData.put("merchantCode", merchantCode);
			jsonMQData.put("startTime", startTime);
			jsonMQData.put("endTime", endTime);
			jsonMQData.put("orderId", orderId);		
			jsonMQData.put("desc", "手动执行-抓取寄售商e仓发货订单");
			rocketMQProducer.send(jsonMQData.toString(),MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTopic(),MQOrderEnum.TIMER_EWS_DELIVERY_ORDER.getTags());

		} catch (Exception e) {
			jsonMQData.put("desc", "手动执行-抓取寄售商e仓发货订单失败,请检查报文");
		}
		
		return jsonMQData;
	}
}
