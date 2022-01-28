package com.topideal.controller.dev;

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
 * 获取业务端数据推送库存
 * @author 杨创
 * 
 *	2019/04/02
 *说明:此方法 针对生产 库存消费失败  通过过业务端生成报文从推库存触发接口
 */

@Controller
@RequestMapping("/derpapi/1005")
public class GetBusinessDataPushInventoryController {
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	
	@RequestMapping(params="method=erp1005",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public JSONObject getEwsManual (@RequestBody String json){

		JSONObject jsonMQData=new JSONObject();
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			jsonMQData.put("tag", jsonData.get("tag"));// 000:触发业务mq查询电商订单发货中单生成报文推送库存加减接口	
			jsonMQData.put("externalCode", jsonData.get("externalCode"));
			jsonMQData.put("desc", "通过过业务端生成报文从推库存触发接口成功");
			rocketMQProducer.send(jsonMQData.toString(),MQOrderEnum.GET_BUSINESS_DATA_PUSH_INVENTORY.getTopic(),MQOrderEnum.GET_BUSINESS_DATA_PUSH_INVENTORY.getTags());

		} catch (Exception e) {
			jsonMQData.put("desc", " 通过过业务端生成报文从推库存触发接口异常,请检查报文");
		}
		
		return jsonMQData;
	}
}
