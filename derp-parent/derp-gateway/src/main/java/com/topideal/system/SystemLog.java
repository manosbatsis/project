package com.topideal.system;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.enums.LogModuleType;
import com.topideal.common.enums.MQLogEnum;
import com.topideal.common.system.log.APILog;
import com.topideal.common.system.mq.RMQLogProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.enums.SmurfsAPIEnum;
import com.topideal.mongo.dao.JSONMongoDao;
import com.topideal.tools.CollectionEnum;

import net.sf.json.JSONObject;

@Component
public class SystemLog {
	
	private  static  final Logger logger = LoggerFactory.getLogger(SystemLog.class);
	
	@Autowired
    private RMQLogProducer rocketMQProducer;
    /**
     * ApolloUtil的注入有时比切面注入晚导致取不到属性问题在此注入一下，确保在切面的前面注入
     * */
    @Autowired
    private  ApolloUtil apolloUtil;
	// 库存日志
	@Autowired
	private JSONMongoDao jsonMongoDao;
   
	public  void sendLog(ServerRequest request,String json,String expMsg,String responseMessage) {
		try {
	    	String url = request.uri().toString();
	    	String requestMethod = request.methodName();
	    	
	    	APILog apiLog=new APILog();
	        apiLog.setKeyword("G"+TimeUtils.getNow().getTime()); //设置主键字值
	        apiLog.setKeywordName("code"); //设置主键字段名
		    apiLog.setUrl(url);//请求地址
		    apiLog.setModel("网关-经销");//接口名称
		    apiLog.setPoint(10000000001L);//接口编码
		    apiLog.setRequestMethod(requestMethod); //请求方法
		    apiLog.setReceiveData(System.currentTimeMillis());//设置接收时间
		    apiLog.setExpMsg(expMsg);
		    JSONObject jsonObject=JSONObject.fromObject(apiLog);
		    jsonObject.put("requestMessage",json);  //设置请求报文
		    jsonObject.put("responseMessage",responseMessage);//响应报文
		    jsonObject.put("endDate", System.currentTimeMillis());
		    jsonObject.put("id", UUID.randomUUID().toString());
		    jsonObject.put("moduleCode", LogModuleType.MODULE_API.getType());
		    SendResult resultMsg = rocketMQProducer.send(jsonObject.toString(), MQLogEnum.LOG_API.getTopic(),MQLogEnum.LOG_API.getTags());
		    jsonObject.put("modulCode", "1005");
            if (resultMsg==null||!resultMsg.getSendStatus().name().equals("SEND_OK")) {
            	jsonMongoDao.insertJson(jsonObject.toString(), CollectionEnum.LOSE_LOG.getCollectionName());
            	logger.info("==报文丢失："+jsonObject.toString()+"==");
			}
		    //推送到日志MQ平台
		    //String resultMsg=SmurfsUtils.sendLog(jsonObject,SmurfsAPIEnum.SMURFS_DERPLOG_REPORT);
		    logger.info("==报文："+jsonObject.toString()+"==");
		    logger.info("==响应报文："+resultMsg+"==");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
