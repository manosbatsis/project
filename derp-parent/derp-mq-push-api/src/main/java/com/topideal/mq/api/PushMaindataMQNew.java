package com.topideal.mq.api;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;
import com.topideal.mq.tools.ConsumerMQAbstract;

import net.sf.json.JSONObject;

/**
 * 推送商品给主数据(菜鸟和商品同步)
 * 
 */
@Component
public class PushMaindataMQNew extends ConsumerMQAbstract {

    /**
     * 打印日志
     */
    public static final Logger logger= LoggerFactory.getLogger(PushMaindataMQNew.class);

    public PushMaindataMQNew(){
        super.setTags(MQPushApiEnum.PUSH_MAINDATA.getTags());
        super.setTopics(MQPushApiEnum.PUSH_MAINDATA.getTopic());
    }


    @Override
    @SystemServiceLog(point="10001",model="经分销-->主数据")
    public String execute(String json,String keys,String topics,String tags)throws Exception{
    	logger.info("===================异步推送主数据接口开始==================");
        logger.info("请求:json "+ json);       
        logger.info("请求地址: maindataSyncUrl"+ ApolloUtil.maindataSyncUrl);
        logger.info("请求地址: maindataSync001Method"+ ApolloUtil.maindataSyncUrl);
        logger.info("请求地址: url"+ ApolloUtil.maindataSyncUrl+ApolloUtil.maindataSync001Method);
        
        String returnJson=null;
        try {
        	returnJson = HttpClientUtil.doPost(ApolloUtil.maindataSyncUrl+ApolloUtil.maindataSync001Method, json, "utf-8");
            logger.info("返回结果:"+returnJson);
            if (returnJson==null)throw new RuntimeException("返回结果为空"+returnJson);
            JSONObject responseJson=JSONObject.fromObject(returnJson);
            Object status = responseJson.get("status");
            if (status==null||StringUtils.isBlank(status.toString())) {
            	 throw new RuntimeException("返回状态status为空:"+returnJson); 
    		}
            if (!"1".equals(status.toString())) {
            	throw new RuntimeException(responseJson.getString("notes"));
    		}
		} catch (Exception e) {
			throw new RuntimeException("推送主数据失败:"+returnJson);  
		}       	
        
        logger.info("===================异步推送主数据接口结束==================");

        return returnJson;
    }



}
