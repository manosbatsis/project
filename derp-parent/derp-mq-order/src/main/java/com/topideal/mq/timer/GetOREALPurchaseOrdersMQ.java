package com.topideal.mq.timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.topideal.api.oreal.OrealUtils;
import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.platformdata.OrealPurchaseOrderDao;
import com.topideal.entity.vo.platformdata.OrealPurchaseOrderModel;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.service.timer.GetOREALPurchaseOrdersService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 获取欧莱雅采购订单
 **/
@Component
public class GetOREALPurchaseOrdersMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(GetOREALPurchaseOrdersMQ.class);

    @Autowired
    private GetOREALPurchaseOrdersService getOREALPurchaseOrdersService;
	@Autowired
	private OrealPurchaseOrderDao orealPurchaseOrderDao;

    public GetOREALPurchaseOrdersMQ() {
        super.setTags(MQOrderEnum.GET_OREAL_PURCHASE_ORDERS.getTags());
        super.setTopics(MQOrderEnum.GET_OREAL_PURCHASE_ORDERS.getTopic());
    }

    @Override
    public ConsumeConcurrentlyStatus execute(String json, String keys, String topics, String tags) {
        LOGGER.info("----获取欧莱雅供应商商品开始----");
        try {
        	synchronized (this) {
        		JSONObject jsonData = JSONObject.fromObject(json);
        		String startDate = (String) jsonData.get("startDate");
        		String endDate = (String) jsonData.get("endDate");       		
        		// 如果开始时间为空/结束时间为空  开始时间取昨天的数据/结束时间取昨天的数据
        		if (StringUtils.isBlank(startDate))startDate=TimeUtils.format(TimeUtils.addDay(TimeUtils.getNow(), -1), "yyyy-MM-dd");
        		if (StringUtils.isBlank(endDate))endDate=TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd");
        		
        		// 获取欧莱雅接口Token
        		String token = OrealUtils.getOREALToken();
        		// 获取请求xml字符串
        		String xml=getXmlStr(startDate,endDate);
        		JSONArray orealOrders= OrealUtils.getOREALOrder(xml, token);		
        		if (orealOrders==null) {
        			throw new RuntimeException("获取欧莱雅订单返回结果为空");
        		}
        		Map<String, List<Object>> orderMap=new HashMap<>();
        		for (Object object : orealOrders) {
        			JSONObject jSONObject=(JSONObject) object;
        			if (jSONObject.get("vordercode")==null||StringUtils.isBlank(jSONObject.getString("vordercode"))||"null".equals(jSONObject.getString("vordercode"))) {
        				continue;
        			}
        			String vordercode = jSONObject.getString("vordercode");
        			if (orderMap.containsKey(vordercode)) {
        				List<Object> list = orderMap.get(vordercode);
        				list.add(object);
        				orderMap.put(vordercode, list);
    				}else {
    					List<Object> list=new ArrayList<>();
    					list.add(object);
    					orderMap.put(vordercode, list);
    				}
        		}
        		Set<String> keySet = orderMap.keySet();
        		for (String vordercode : keySet) {
        			List<Object> itemList = orderMap.get(vordercode);
        			JSONObject jSONObject=new JSONObject();
        			jSONObject.put("vordercode", vordercode);
        			jSONObject.put("item", itemList);
        			OrealPurchaseOrderModel orderModel=new OrealPurchaseOrderModel();
        			orderModel.setVordercode(vordercode);
        			orderModel = orealPurchaseOrderDao.searchByModel(orderModel);
        			if (orderModel!=null) {
        				LOGGER.info("欧莱雅采购订单号:vordercode:"+vordercode+ "已经存在");
        				continue;
        			}
        			getOREALPurchaseOrdersService.saveOREALPurchaseOrders(jSONObject.toString(),keys,topics,tags);
        			
    			}
			}


        } catch(Exception e) {
            LOGGER.error("获取欧莱雅订单异常:{}", e);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
    
	/**
	 * 获取请求xml字符串
	 * @return
	 */
	private String getXmlStr(String startDate,String endDate) {
		long time = TimeUtils.getNow().getTime();
		String requesttime = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
		String xml="<ufinterface account=\"05\" billtype=\"disQuery\" filename=\"\" isexchange=\"Y\" proc=\"add\" receiver=\"\" replace=\"Y\" roottag=\"\" sender=\"DIS\" subbilltype=\"\">";
		xml=xml+"<bill id=\""+time+"\">";
		xml=xml+"<billhead>"
				+ "<billid>"+time+"</billid>"
				+ "<userid>"+13942001+"</userid>"
				+ "<pk_corp>"+1735+"</pk_corp>"
				+ "<requesttime>"+requesttime+"</requesttime>"
				+ "<sqlcode>"+13942002+"</sqlcode>"
				+"</billhead>"
				+"<billbody>"
				+"<entry>"
				+"<code>"+1394200201+"</code>"  
				+"<field>"+"dorderdate"+"</field>" 
				+"<table>"+"po_order"+"</table>" 
				+"<condition>"+">="+"</condition>" 
				+"<value>"+startDate+"</value>"
				+"</entry>"
				+"<entry>"
				+"<code>"+1394200202+"</code>"  
				+"<field>"+"dorderdate"+"</field>" 
				+"<table>"+"po_order"+"</table>" 
				+"<condition>"+"<![CDATA[ <= ]]>"+"</condition>" 
				+"<value>"+endDate+"</value>"
				+"</entry>"
				+"</billbody>"
				+"</bill>"
				+"</ufinterface>";		
		return xml;
	}

}
