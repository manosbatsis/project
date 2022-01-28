package com.topideal.mq.api;/*package com.topideal.mq.api;


import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.http.HttpClientUtil;
import com.topideal.mq.tools.ConsumerMQAbstract;

import net.sf.json.JSONObject;

*//**
 * 推送菜鸟商品给主数据
 * 
 *//*
@Component
public class PushMaindataMQ extends ConsumerMQAbstract {

    *//**
     * 打印日志
     *//*
    public static final Logger logger= LoggerFactory.getLogger(PushMaindataMQ.class);

    public PushMaindataMQ(){
        super.setTags(MQPushApiEnum.PUSH_MAINDATA.getTags());
        super.setTopics(MQPushApiEnum.PUSH_MAINDATA.getTopic());
    }


    @Override
    @SystemServiceLog(point="10001",model="经分销-->主数据")
    public String execute(String json,String keys,String topics,String tags)throws Exception{
    	logger.info("===================异步推送主数据接口开始==================");
        //返回接口报文
        String returnJson=null;
        JSONObject jsonObject= JSONObject.fromObject(json);
        jsonObject.remove("order_id") ;
        jsonObject.remove("method") ;
        jsonObject.put("source", ApolloUtil.maindataSource) ;
		jsonObject.put("version", "1.0") ;
		jsonObject.put("syncType", "1") ;
		jsonObject.put("permissions", "ACCESS,U_ADD,U_UPDATE") ;
		
		JSONObject headJson = new JSONObject() ;
		headJson.put("messageID", "MDM107OP201801051629142292") ;
		headJson.put("messageType", "MDM107") ;
		headJson.put("senderID", "JFX") ;
		headJson.put("version", "1.0") ;
		headJson.put("receiverID", "MDM") ;
		headJson.put("sendTime", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")) ;
		jsonObject.put("messageHead", headJson) ;
		
        logger.info("请求: "+ jsonObject.toString());
        String jsonStr = jsonObject.toString();
        // 防止中文乱码转码和特殊字符
        logger.info("请求地址: "+ ApolloUtil.maindataSyncGoodsUrl);
        logger.info("请求转码: "+ jsonStr);
        returnJson = HttpClientUtil.doPost(ApolloUtil.maindataSyncGoodsUrl, jsonStr, "utf-8");
        logger.info("返回结果:"+returnJson);
        //返回结果解析
        JSONObject responseJson=JSONObject.fromObject(returnJson);
        if(responseJson==null) throw new RuntimeException(returnJson);  
        Object flag = responseJson.get("flag");
        if (flag==null||StringUtils.isBlank(flag.toString())) {
        	 throw new RuntimeException(returnJson); 
		}
        if (!"SUCCESS".equals(flag.toString())) {
        	throw new RuntimeException(responseJson.getString("errInfo"));
		}
        logger.info("===================异步推送主数据接口结束==================");

        return returnJson;
    }



}
*/