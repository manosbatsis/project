package com.topideal.mq.api;


import com.topideal.api.sapience.SapienceUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;
import com.topideal.mq.tools.ConsumerMQAbstract;
import com.topideal.tools.SapienceSignUtils;

import net.sf.json.JSONObject;

/**
 * 向卓普信推送商品档案
 * 
 */
@Component
public class PushSapienceMQ extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger logger= LoggerFactory.getLogger(PushSapienceMQ.class);

    public PushSapienceMQ(){
        super.setTags(MQPushApiEnum.PUSH_SAPIENCE.getTags());
        super.setTopics(MQPushApiEnum.PUSH_SAPIENCE.getTopic());
    }


    @Override
    @SystemServiceLog(point="10001",model="经分销-->卓普信")
    public String execute(String json,String keys,String topics,String tags)throws Exception{
    	logger.info("===================异步推送卓普信接口开始==================");     

       // json="{\"additiveStr\":null,\"compGoodsNo\":\"表面活性剂（24%：烷基醚硫酸盐、纯肥皂成分（脂肪酸钠）、直链烷基苯磺酸钠、稳定化剂、PH调整剂、水软化剂（柠檬酸）、酶\",\"contractsUnit\":\"瓶\",\"custassemCountry\":null,\"disableTime\":null,\"goodsBarcode\":\"4902430189729\",\"goodsBrand\":\"宝洁/P＆G\",\"goodsCategory\":\"居家日用\",\"goodsDesc\":null,\"goodsEnName\":null,\"goodsHsCode\":\"3402209000\",\"goodsName\":\"Sarasa无添加洗衣液850G\",\"goodsNo\":\"82216768\",\"goodsPacktype\":\"其他箱\",\"goodsQualityDays\":\"720\",\"goodsSpec\":\"850g/瓶\",\"goodsType\":null,\"ingredient\":null,\"isCombined\":\"0\",\"isRecord\":1,\"kgs\":\"0.997\",\"merchantId\":\"0000138\",\"merchantName\":\"宝信\",\"modTime\":\"2021-01-19 17:33:33\",\"modifier\":null,\"net\":\"0.85\",\"note\":null,\"poisonStr\":null,\"produceComp\":\"P&G 宝洁\",\"produceCompAddr\":null,\"qtpUnit\":\"瓶\",\"qtyUnit2\":\"瓶\",\"recordPrice\":\"28.9000\",\"sourceCode\":\"10\",\"status\":\"20\",\"taxCode\":null,\"taxRate\":null,\"order_id\":\"1611208236777,ERP1873120134025533\",\"method\":\"wms.add.goods.push\"}";

        //https://demo-scf-scs.g2link.cn/wms/rest/api
        String returnJson=null;
        try {
        	//returnJson = HttpClientUtil.doPost(ApolloUtil.maindataSyncUrl+ApolloUtil.maindataSync001Method, json, "utf-8");
        	
        	JSONObject jsonObject = JSONObject.fromObject(json);
        	
        	logger.info("请求:jsonObject "+ jsonObject);
        	returnJson = SapienceUtils.sendSapience(jsonObject);
        	
            logger.info("返回结果:"+returnJson);
            if (returnJson==null)throw new RuntimeException("返回结果为空"+returnJson);
            JSONObject responseJson=JSONObject.fromObject(returnJson);
            Object status = responseJson.get("status");
            if (status==null||StringUtils.isBlank(status.toString())) {
            	 throw new RuntimeException("返回状态status为空:"+returnJson); 
    		}
            //0:成功
            if (!"0".equals(status.toString())) {
            	throw new RuntimeException(responseJson.getString("note"));
    		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("推送卓普信失败:"+returnJson);
			throw new RuntimeException(returnJson);
		}       	
        
        logger.info("===================异步推送卓普信接口结束==================");

        return returnJson;
    }



}
