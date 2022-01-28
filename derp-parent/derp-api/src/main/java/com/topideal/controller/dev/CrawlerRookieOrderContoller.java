package com.topideal.controller.dev;

import com.topideal.common.constant.DERP_LOG_POINT;
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
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 压测 菜鸟电商订单
 * @author 杨创
 *2021/07/27
 */
@Controller
@RequestMapping("/derpapi/1007")
public class CrawlerRookieOrderContoller {
	
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerRookieOrderContoller.class);
	@Autowired
	private RMQProducer rocketMQProducer;// MQ

	/**压力测试用*/
	@RequestMapping(params="method=erp1007",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point= DERP_LOG_POINT.POINT_13201140003,model=DERP_LOG_POINT.POINT_13201140003_Label,keyword = "tradeId")
	public JSONObject getSysData (@RequestBody String json){
		
		LOGGER.info("mongdb数据同步开始"+json);
		JSONObject jsonData=new JSONObject();

		try {
			jsonData = JSONObject.fromObject(json);		
			//订单号
			if (jsonData.get("tradeId")==null||StringUtils.isBlank(jsonData.getString("tradeId"))) {
				return ResponseFactory.error("tradeId","" , "tradeId is NULL");
			}
			//店铺id
			if (jsonData.get("shopId")==null||StringUtils.isBlank(jsonData.getString("shopId"))) {
				return ResponseFactory.error("shopId",jsonData.getString("tradeId"), "shopId is NULL");
			}
			//店铺名称
			if (jsonData.get("shopName")==null||StringUtils.isBlank(jsonData.getString("shopName"))) {
				return ResponseFactory.error("shopName",jsonData.getString("tradeId") , "shopName is NULL");
			}
			//店铺编码
			if (jsonData.get("shopCode")==null||StringUtils.isBlank(jsonData.getString("shopCode"))) {
				return ResponseFactory.error("shopCode",jsonData.getString("tradeId") , "shopCode is NULL");
			}
			
			if (jsonData.get("itemList")==null) {
				return ResponseFactory.error("itemList",jsonData.getString("tradeId") , "itemList is NULL");
			}
			
			JSONArray itemList = jsonData.getJSONArray("itemList");
			if (itemList.size()==0) {
				return ResponseFactory.error("itemList",jsonData.getString("tradeId") , "itemList is NULL");
			}
			
			// 订单表头随机取一条
			JSONObject jSONObject = (JSONObject) itemList.get(0);
			if (jSONObject.get("tradeTime")==null||StringUtils.isBlank(jSONObject.getString("tradeTime"))) {
				return ResponseFactory.error("tradeTime",jsonData.getString("tradeId") , "tradeTime is NULL");
			}
			if (jSONObject.get("consignTime")==null||StringUtils.isBlank(jSONObject.getString("consignTime"))) {
				return ResponseFactory.error("consignTime",jsonData.getString("tradeId") , "consignTime is NULL");
			}
			
			//校验表体
			for (Object object : itemList) {
				JSONObject itemJson=(JSONObject) object;
				if (itemJson.get("quantity")==null||StringUtils.isBlank(itemJson.getString("quantity"))) {
					return ResponseFactory.error("quantity",jsonData.getString("tradeId") , "quantity is NULL");
				}
				if (Integer.valueOf(itemJson.getString("quantity")).intValue()<=0) {
					return ResponseFactory.error("quantity",jsonData.getString("tradeId") , "数量必须大于0");
				}
				if (itemJson.get("goodsId")==null||StringUtils.isBlank(itemJson.getString("goodsId"))) {
					return ResponseFactory.error("goodsId",jsonData.getString("tradeId") , "goodsId is null");

				}
				if (itemJson.get("goodsCode")==null||StringUtils.isBlank(itemJson.getString("goodsCode"))) {
					return ResponseFactory.error("goodsCode",jsonData.getString("tradeId") , "goodsCode is null");
				}
			}
			rocketMQProducer.send(jsonData.toString(), MQOrderEnum.TIMER_CRAWLER_ROOKIE_GET_ONE_ORDER.getTopic(),MQOrderEnum.TIMER_CRAWLER_ROOKIE_GET_ONE_ORDER.getTags());				
			return ResponseFactory.success("tradeId", jsonData.getString("tradeId"));
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("菜鸟电商订单接口异常{}", e.getMessage());
			return ResponseFactory.error("tradeId", jsonData.getString("tradeId"), e.getMessage());
		}
		

	}

	

	
}
