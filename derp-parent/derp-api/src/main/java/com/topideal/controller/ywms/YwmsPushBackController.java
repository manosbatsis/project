package com.topideal.controller.ywms;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.topideal.json.pushapi.ywms.sale.pushback.*;
import com.topideal.json.pushapi.ywms.sale.pushback.Package;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.tools.CollectionEnum;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP_LOG;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.enums.PushYwmsTypeEnum;
import com.topideal.common.system.log.APILog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.XmlConverUtil;
import com.topideal.json.pushapi.ywms.purchase.pushback.EntryOrder;
import com.topideal.json.pushapi.ywms.purchase.pushback.OrderLine;
import com.topideal.json.pushapi.ywms.purchase.pushback.OrderLines;
import com.topideal.json.pushapi.ywms.purchase.pushback.PurchasePushBackRoot;

@Controller
@RequestMapping("/qimen")
public class YwmsPushBackController {
	
	private static final Long POINT = 13201158100L ; 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YwmsPushBackController.class);
	
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	
	@Autowired
    private RMQLogProducer rocketLogMQProducer;
	// 库存日志
	@Autowired
	private JSONMongoDao jsonMongoDao;

	@RequestMapping(value="/yWmsPushBack.asyn" ,method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/xml")
	@ResponseBody
	public String yWmsPushBack(@RequestBody String requestData) {
		
		JSONObject bodyJson = new JSONObject() ;
		bodyJson.put("flag", "success") ;
		bodyJson.put("code", 200) ;
		bodyJson.put("message", "成功") ;
		
		JSONObject responseJson = new JSONObject() ;

		String keyword = String.valueOf(new Date().getTime()) ;
		
		try {
			String data = URLDecoder.decode(requestData, "UTF-8");
			
			String xml = null;
	        
	        if (data.indexOf("<") >= 0) {
	            xml = data.substring(data.indexOf("<"), data.lastIndexOf(">") + 1);
	        }
	        
			String jsonStr = XmlConverUtil.XmlToJson(xml);
			
			//根据报文内容判断接口回推类型
			if(jsonStr.indexOf(PushYwmsTypeEnum.CGRK.getType()) > -1) {
				
				PurchasePushBackRoot root = JSONObject.parseObject(jsonStr, PurchasePushBackRoot.class) ;
				EntryOrder entryOrder = root.getRequest().getEntryOrder();
				OrderLines orderLines = root.getRequest().getOrderLines();
				
				keyword = entryOrder.getEntryOrderCode();
				
				/**
				 * ---------------------必填校验开始-----------------------------
				 */
				checkPurchare(entryOrder, orderLines);
				/**
				 * ---------------------必填校验结束-----------------------------
				 */
				
				//转换成符合mq日志监控格式报文
				JSONObject purchaseJSON = new JSONObject() ;
				purchaseJSON.put("jsonStr", jsonStr) ;
				purchaseJSON.put("orderCode", keyword) ;

				rocketMQProducer.send(purchaseJSON.toJSONString(), MQOrderEnum.YWMS_PURCHASE_WAREHOUSE.getTopic(),MQOrderEnum.YWMS_PURCHASE_WAREHOUSE.getTags());
				LOGGER.info("众邦云仓采购入库回推接口,推送MQ报文"+jsonStr);
				
			}else if(jsonStr.indexOf(PushYwmsTypeEnum.JYCK.getType()) > -1){

				OrderPushBackRoot root = JSONObject.parseObject(jsonStr, OrderPushBackRoot.class) ;
				DeliveryOrder deliveryOrder = root.getRequest().getDeliveryOrder();
				
				keyword = deliveryOrder.getDeliveryOrderCode() ;

				/**
				 * ---------------------必填校验开始-----------------------------
				 */
				checkOrder(root, deliveryOrder, keyword);
				/**
				 * ---------------------必填校验结束-----------------------------
				 */

				//转换成符合mq日志监控格式报文
				JSONObject orderJSON = new JSONObject() ;
				orderJSON.put("jsonStr", jsonStr) ;
				orderJSON.put("orderCode", keyword) ;
				
				rocketMQProducer.send(orderJSON.toJSONString(), MQOrderEnum.YWMS_DSORDER_OUTDEPOT.getTopic(),MQOrderEnum.YWMS_DSORDER_OUTDEPOT.getTags());
				LOGGER.info("众邦云仓电商出库接口回推,推送MQ报文"+jsonStr);
				
			}else{
				bodyJson.put("flag", "failure") ;
				bodyJson.put("code", 400) ;
				bodyJson.put("message", "非接口接收类型") ;
				LOGGER.error("非接口接收类型");
			}
			
			sendSuccLog(requestData, keyword);
			
		} catch (Exception e) {
			bodyJson.put("flag", "failure") ;
			bodyJson.put("code", 500) ;
			bodyJson.put("message", e.getMessage()) ;
			LOGGER.error("众邦云仓采购入库回推接口异常:" + e.getMessage(), e);
			
			sendErrorLog(requestData, keyword, e);
		}
		
		responseJson.put("response", bodyJson) ;
		
		String returnXML = XmlConverUtil.JsonToXml(responseJson.toJSONString());
		
		return returnXML ;
	}
	
	/**
	 * 电商订单必填项校验
	 * @param root
	 * @param deliveryOrder
	 * @param keyword
	 */
	private void checkOrder(OrderPushBackRoot root, DeliveryOrder deliveryOrder, String keyword) {
		if(StringUtils.isBlank(keyword)) {
			throw new RuntimeException("订单出库单单号deliveryOrderCode为空") ;
		}

		if(StringUtils.isBlank(deliveryOrder.getWarehouseCode())) {
			throw new RuntimeException("订单:"+keyword+"仓库编码warehouseCode为空") ;
		}

		if(StringUtils.isBlank(deliveryOrder.getOrderType())) {
			throw new RuntimeException("订单:"+keyword+"出库单类型orderType为空") ;
		}

		if(StringUtils.isBlank(deliveryOrder.getStatus())) {
			throw new RuntimeException("订单:"+keyword+"出库单状态status为空") ;
		}
		if(StringUtils.isBlank(deliveryOrder.getOrderConfirmTime())) {
			throw new RuntimeException("订单:"+keyword+"订单完成时间orderConfirmTime为空") ;
		}
		Packages packages = root.getRequest().getPackages();
		if(packages==null){
			throw new RuntimeException("订单:"+keyword+"packages为空") ;
		}
		List<Package> packageList = packages.getPackage();
		if (packageList==null || packageList.size()==0) {
			throw new RuntimeException("订单:"+keyword+"package为空") ;
		}
		for (int i = 0; i < packageList.size(); i++) {
			Package aPackage = packageList.get(i);
			if(StringUtils.isBlank(aPackage.getLogisticsCode())){
				throw new RuntimeException("订单:"+keyword+"物流公司编码logisticsCode为空") ;
			}
			if(StringUtils.isBlank(aPackage.getExpressCode())){
				throw new RuntimeException("订单:"+keyword+"运单号expressCode为空") ;
			}
			if(StringUtils.isBlank(aPackage.getWeight())){
				throw new RuntimeException("订单:"+keyword+"包裹重量weight为空") ;
			}
			Items items = aPackage.getItems();
			if(items==null){
				throw new RuntimeException("订单:"+keyword+"items为空") ;
			}
			List<Item> itemList = items.getItem();
			if(itemList==null || itemList.size()==0){
				throw new RuntimeException("订单:"+keyword+"item为空") ;
			}
			for (int j = 0; j < itemList.size(); j++) {
				Item item = itemList.get(j);
				if(StringUtils.isBlank(item.getItemCode())){
					throw new RuntimeException("订单:"+keyword+"商品编码itemCode为空") ;
				}
				if(StringUtils.isBlank(item.getQuantity())){
					throw new RuntimeException("订单:"+keyword+"商品数量quantity为空") ;
				}
			}
		}
	}
	
	/**
	 * 采购报文必填项校验
	 * @param entryOrder
	 * @param orderLines
	 */
	private void checkPurchare(EntryOrder entryOrder, OrderLines orderLines) {
		if(StringUtils.isBlank(entryOrder.getEntryOrderCode())) {
			throw new RuntimeException("订单实体入库单编码entryOrder为空") ;
		}
		
		if(StringUtils.isBlank(entryOrder.getOwnerCode())) {
			throw new RuntimeException("订单实体货主编码ownerCode为空") ;
		}
		
		if(StringUtils.isBlank(entryOrder.getWarehouseCode())) {
			throw new RuntimeException("订单实体仓库编码warehouseCode为空") ;
		}
		
		if(StringUtils.isBlank(entryOrder.getEntryOrderType())) {
			throw new RuntimeException("订单实体入库单类型entryOrderType为空") ;
		}
		
		if(StringUtils.isBlank(entryOrder.getStatus())) {
			throw new RuntimeException("订单实体入库单状态status为空") ;
		}
		
		if(orderLines == null || orderLines.getOrderLine() == null
				|| orderLines.getOrderLine().size() == 0) {
			throw new RuntimeException("商品实体为空") ;
		}
		
		List<OrderLine> goodsList = orderLines.getOrderLine() ;
		
		for (OrderLine orderLine : goodsList) {
			if(StringUtils.isBlank(orderLine.getItemCode())) {
				throw new RuntimeException("商品实体商品编码itemCode为空") ;
			}
			
			if(StringUtils.isBlank(orderLine.getActualQty())) {
				throw new RuntimeException("商品实体实收数量actualQty为空") ;
			}
		}
	}
	
	private void sendSuccLog(String requestData, String keyword) {
		LOGGER.info("================众邦云仓日志发送开始  正常流程===============");
        //API日志实体
        APILog apiLog=new APILog();
        //请求报文
        JSONObject requestMessage = new JSONObject();
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            //--------------日志基本信息-------------------------------
            //请求地址
            String url="http://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?"+request.getQueryString();
            //请求方法
            String requestMethod= "com.topideal.controller.ywms.YwmsPushBackController.yWmsPushBack()";
            //设置主键字值
            apiLog.setKeyword(keyword);
            //设置主键字段名
            apiLog.setKeywordName("order_id");
            //实体类注入值
            apiLog.setUrl(url);
            //接口名称
            apiLog.setModel("众邦云仓回推接口");
            //埋点
            apiLog.setPoint(POINT);
            //请求方法
            apiLog.setRequestMethod(requestMethod);
            //设置接收时间
            apiLog.setReceiveData(System.currentTimeMillis());
            apiLog.setState(1);
            apiLog.setExpMsg("");
            apiLog.setDerpCode(DERP_LOG.LOG_DERPCODE_003);
            //--------------- 获取用户信息
            JSONObject jsonObject= (JSONObject)JSONObject.toJSON(apiLog);
            
            requestMessage.put("xml", requestData) ;
            
            //设置请求报文
            jsonObject.put("requestMessage",requestMessage);
            //响应报文
            jsonObject.put("responseMessage","成功");
            jsonObject.put("endDate", System.currentTimeMillis());
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("moduleCode", LogModuleType.MODULE_API.getType());
            SendResult sendResult = rocketLogMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_API.getTopic(),MQLogEnum.LOG_API.getTags());
            jsonObject.put("modulCode", "1005");
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+sendResult+"==");
            if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            	 LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
			}
            //推送到日志MQ平台
            LOGGER.info("================众邦云仓日志发送结束   正常流程===============");
        }  catch (Exception e) {
            //记录本地异常日志
            LOGGER.error("================众邦云仓日志发送  保存日志异常！！！===============");
            LOGGER.error("异常信息:{}", e.getMessage());
        }
	}
	
	private void sendErrorLog(String requestData, String keyword, Exception ex) {
		LOGGER.info("================众邦云仓日志发送开始  正常流程===============");
        //API日志实体
        APILog apiLog=new APILog();
        //请求报文
        JSONObject requestMessage = new JSONObject();
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            //--------------日志基本信息-------------------------------
            //请求地址
            String url="http://"+request.getServerName()+":"+request.getServerPort()+request.getRequestURI()+"?"+request.getQueryString();
            //请求方法
            String requestMethod= "com.topideal.controller.ywms.YwmsPushBackController.yWmsPushBack()";
            //设置主键字值
            apiLog.setKeyword(keyword);
            //设置主键字段名
            apiLog.setKeywordName("order_id");
            //实体类注入值
            apiLog.setUrl(url);
            //接口名称
            apiLog.setModel("众邦云仓回推接口");
            //埋点
            apiLog.setPoint(POINT);
            //请求方法
            apiLog.setRequestMethod(requestMethod);
            //设置接收时间
            apiLog.setReceiveData(System.currentTimeMillis());
            apiLog.setState(0);
            apiLog.setExpMsg(ex.getMessage());
            apiLog.setDerpCode(DERP_LOG.LOG_DERPCODE_003);
            
            //--------------- 获取用户信息
            JSONObject jsonObject= (JSONObject)JSONObject.toJSON(apiLog);
            
            requestMessage.put("xml", requestData) ;
            //设置请求报文
            jsonObject.put("requestMessage",requestMessage);
            //响应报文
            jsonObject.put("responseMessage",ex.getMessage());
            jsonObject.put("endDate", System.currentTimeMillis());
            jsonObject.put("id", UUID.randomUUID().toString());
            jsonObject.put("moduleCode", LogModuleType.MODULE_API.getType());
            SendResult sendResult = rocketLogMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_API.getTopic(),MQLogEnum.LOG_API.getTags());
            jsonObject.put("modulCode", "1005");
            LOGGER.info("==报文："+jsonObject.toString()+"==");
            LOGGER.info("==响应报文："+sendResult+"==");
            if (sendResult==null||!sendResult.getSendStatus().name().equals("SEND_OK")) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            	 LOGGER.info("==报文丢失："+jsonObject.toString()+"==");
			}
            //推送到日志MQ平台
            LOGGER.info("================众邦云仓日志发送结束   正常流程===============");
        }  catch (Exception e) {
            //记录本地异常日志
            LOGGER.error("================众邦云仓日志发送  保存日志异常！！！===============");
            LOGGER.error("异常信息:{}", e.getMessage());
        }
	}
}
