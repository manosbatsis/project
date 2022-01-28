package com.topideal.controller.dev;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.mq.RMQProducer;

import net.sf.json.JSONObject;
/**
 * 蓝精灵订单采集手动触发抓取
 * @author 杨创
 *2019/03/13
 */

@Controller
@RequestMapping("/derpapi/1004")
public class SmurfsManualController {
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	
	@RequestMapping(params="method=erp1004",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public JSONObject getEwsManual (@RequestBody String json){
		JSONObject jsonData=new JSONObject();
		JSONObject jsonMQData=new JSONObject();
		try {
			jsonData = JSONObject.fromObject(json);
			String orderNo = (String)jsonData.get("orderNo");
			String startDate = (String)jsonData.get("startDate");
			String endDate = (String)jsonData.get("endDate");
			String status = (String)jsonData.get("status");
			String shopCode = (String)jsonData.get("shopCode");

			
			// 开始时间
			if (StringUtils.isBlank(startDate)) {
				jsonMQData.put("desc", "startDate IS NULL");
				return jsonMQData;
			}
			// 结束时间
			if (StringUtils.isBlank(endDate)) {
				jsonMQData.put("desc", "endDate IS NULL");
				return jsonMQData;
			}
			// 订单状态
			if (StringUtils.isBlank(status)) {
				jsonMQData.put("desc", "status IS NULL");
				return jsonMQData;
			}
			// 店铺编码
			if (StringUtils.isBlank(shopCode)) {
				jsonMQData.put("desc", "shopCode IS NULL");
				return jsonMQData;
			}
			
			jsonMQData.put("tag", "2");// "1" 定时器动触发 ,"2"手动触发 
			jsonMQData.put("orderNo", orderNo);
			jsonMQData.put("startDate", startDate);
			jsonMQData.put("endDate", endDate);
			jsonMQData.put("status", status);	
			jsonMQData.put("shopCode", shopCode);
			jsonMQData.put("desc", "手动执行-抓取蓝精灵订单采集数据");
			rocketMQProducer.send(jsonMQData.toString(),MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTopic(),MQOrderEnum.TIMER_SMURFS_ORDER_COLLECTION_ORDER.getTags());

			
		} catch (Exception e) {
			jsonMQData.put("desc", "手动执行-抓取蓝精灵订单采集数据错误,请检查报文");
		}
		
		return jsonMQData;
	}
}
