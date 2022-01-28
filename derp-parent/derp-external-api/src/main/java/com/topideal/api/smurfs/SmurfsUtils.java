package com.topideal.api.smurfs;

import com.topideal.common.tools.ApolloUtil;
import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import priv.smurfs.tools.gateway.client.GatewayHttpClient;
import priv.smurfs.tools.gateway.common.ApiResponse;

/**
 * Created by weixiaolei on 2018/9/19.
 */
public class SmurfsUtils {

    private static final Logger logger= LoggerFactory.getLogger(SmurfsUtils.class);
    //蓝精灵密钥
    private static final String APP_SECRET=ApolloUtil.smurfsAppsecret;
    //蓝精APP_KEY
    private static final String APP_KEY=ApolloUtil.smurfsAppkey;
    //蓝精灵日志端口+iP
    private static final String SMURFS_HOST=ApolloUtil.smurfsHost;
   
    //蓝精灵日志密钥
    private static final String LOG_APP_SECRET=ApolloUtil.smurfsLogAppsecret;
    //蓝精日志 APP_KEY
    private static final String LOG_APP_KEY=ApolloUtil.smurfsLogAppkey;
    //蓝精灵日志端口+iP
    private static final String SMURFS_LOG_HOST=ApolloUtil.smurfsLogHost;

    /**
     * 初始化
     * @param apiCode   接口编码
     * @param v         版本号
     * @param sign      签名
     * @return
     */
   /* private static  Map init(String apiCode,String v,String sign) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map header = new HashMap();
        header.put("appkey",APP_KEY);
        header.put("timestamp",dateFormat.format(System.currentTimeMillis()));
        header.put("sign",sign);
        header.put("apicode",apiCode);
        header.put("v",v);
        return header;
    }*/
    
    /**
     * 初始化
     * @param apiCode   接口编码
     * @param v         版本号
     * @param sign      签名
     * @return
     */
/*    private static  Map initLog(String apiCode,String v,String sign) {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map header = new HashMap();
        header.put("appkey",LOG_APP_KEY);
        header.put("timestamp",dateFormat.format(System.currentTimeMillis()));
        header.put("sign",sign);
        header.put("apicode",apiCode);
        header.put("v",v);
        return header;
    }*/
    
    
    /**
     * 调用外调接口
     * @param json  业务报文
     * @param apiEnum   接口枚举
     * @return
     */
    public static String send(JSONObject json, SmurfsAPIEnum apiEnum){    	
    	logger.info("蓝精灵地址：SMURFS_HOST"+SMURFS_HOST +",APP_KEY"+APP_KEY+"APP_SECRET"+APP_SECRET);   
    	
    	
        try {
        	GatewayHttpClient gatewayHttpClient =
	                GatewayHttpClient.getInstance(SMURFS_HOST, APP_KEY, APP_SECRET);
	         //发送请求
			System.out.println("请求报文："+json.toString());
			logger.info("请求报文："+json.toString());
	         ApiResponse response =gatewayHttpClient.send(apiEnum.getApiCode(),apiEnum.getV(),json.toString());
	        String resultJson = response.getResultJson();
	        //logger.info("蓝精灵返回结果："+resultJson);
            return resultJson;
            
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("蓝精灵返回异常:"+e.getMessage());
			logger.error("蓝精灵返回异常:"+e.getMessage());
			return null;
		}
        
    }
    /**
     * 调用蓝精灵日志接口
     * @param json
     * @param apiEnum
     * @return
     */
    public static String sendLog(JSONObject json, SmurfsAPIEnum apiEnum){
    	logger.info("蓝精灵地址：SMURFS_LOG_HOST"+SMURFS_LOG_HOST +",LOG_APP_KEY"+LOG_APP_KEY+"LOG_APP_SECRET"+LOG_APP_SECRET); 
        try {
        	GatewayHttpClient gatewayHttpClient =
	                GatewayHttpClient.getInstance(SMURFS_LOG_HOST, LOG_APP_KEY, LOG_APP_SECRET);	      
	         //发送请求
	         ApiResponse response =gatewayHttpClient.send(apiEnum.getApiCode(),apiEnum.getV(),json.toString());
	        String resultJson = response.getResultJson();
	        logger.info("蓝精灵返回结果："+resultJson);
            return resultJson;
            
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("蓝精灵返回异常:"+e.getMessage());
			logger.error("蓝精灵返回异常:"+e.getMessage());
			return null;
		}
        
    	

    }
    
    
    
    
    
    
    
    
    /**
     * 调用外调接口
     * @param json  业务报文
     * @param apiEnum   接口枚举
     * @return
     */
/*    public static String send(JSONObject json, SmurfsAPIEnum apiEnum){    	
    	logger.info("蓝精灵地址：SMURFS_HOST"+SMURFS_HOST +",APP_KEY"+APP_KEY+"APP_SECRET"+APP_SECRET);   
        try {
            APIRequestBuilder builder=APIRequestBuilder.create()
                    .setAppKey(APP_KEY)
                    .setHost(SMURFS_HOST)
                    .setApiCode(apiEnum.getApiCode())
                    .setV(apiEnum.getV())
                    .setBodys(json.toString().getBytes("utf-8"));
            APIResponse reslut=GatewayHttpClient
                    .getInstance(APP_SECRET)
                    .send(builder.builder());
            logger.info("蓝精灵返回结果："+reslut.getResultJson());
            String resultJson = reslut.getResultJson();
            return resultJson;
            
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("蓝精灵返回结果为空");
			return null;
		}
        
    }*/
    /**
     * 调用蓝精灵日志接口
     * @param json
     * @param apiEnum
     * @return
     */
/*    public static String sendLog(JSONObject json, SmurfsAPIEnum apiEnum){
    	logger.info("蓝精灵地址：SMURFS_LOG_HOST"+SMURFS_LOG_HOST +",LOG_APP_KEY"+LOG_APP_KEY+"LOG_APP_SECRET"+LOG_APP_SECRET); 
        try {
        	logger.info("蓝精灵地址：1");
            APIRequestBuilder builder=APIRequestBuilder.create()
                    .setAppKey(LOG_APP_KEY)
                    .setHost(SMURFS_LOG_HOST)
                    .setApiCode(apiEnum.getApiCode())
                    .setV(apiEnum.getV())
                    .setBodys(json.toString().getBytes("utf-8"));
            logger.info("蓝精灵地址：2");
            APIResponse reslut=GatewayHttpClient
                    .getInstance(LOG_APP_SECRET)
                    .send(builder.builder());
            logger.info("蓝精灵地址：21");
            logger.info("蓝精灵返回结果："+reslut.getResultJson());
            String resultJson = reslut.getResultJson();
            logger.info("蓝精灵地址：3");
            return resultJson;
            
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("蓝精灵返回结果为空");
			logger.info("蓝精灵地址：4");
			return null;
		}
        
    	

    }*/
    
    
    
    
    
    
    
    
    

    /**
     * 调用外调接口
     * @param json  业务报文
     * @param apiEnum   接口枚举
     * @return
     */
/*    public static String send(JSONObject json, SmurfsAPIEnum apiEnum){
    	logger.info("蓝精灵地址："+SMURFS_URL);
        String sign= SmurfSign.buildRequestPara(json.toString(),APP_SECRET);
        Map headerMap=init(apiEnum.getApiCode(),apiEnum.getV(),sign);
        String resultMsg= HttpClientUtil.doPost(SMURFS_URL,json.toString(),headerMap);
        logger.info("=========>"+json.toString());
    	
    	logger.info("蓝精灵地址："+SMURFS_HOST);
    	ApiMessage apiMessage=new ApiMessage();
        apiMessage.setApicode(apiEnum.getApiCode());
        apiMessage.setV(apiEnum.getV());
    	
        try {
            HttpClientFactory client= HttpClientFactory.getInstance(SMURFS_HOST, APP_KEY, APP_SECRET);
        	ApiResponse apiResponse=client.send(apiMessage,json.toString().getBytes("utf-8"));
        	com.alibaba.fastjson.JSONObject resultModel=(com.alibaba.fastjson.JSONObject) JSON.toJSON(apiResponse);
            resultModel.put("bodyStr",new String(apiResponse.getBody(),"utf-8"));
            String reslut = new String(apiResponse.getBody(),"utf-8");
            logger.info("蓝精灵返回结果："+reslut);
            return new String(apiResponse.getBody(),"utf-8");
            
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("蓝精灵返回结果为空");
			return null;
		}
        
    }*/
    /**
     * 调用蓝精灵日志接口
     * @param json
     * @param apiEnum
     * @return
     */
/*    public static String sendLog(JSONObject json, SmurfsAPIEnum apiEnum){
    	logger.info("蓝精灵地址："+SMURFS_LOG_URL);
        String sign= SmurfSign.buildRequestPara(json.toString(),LOG_APP_SECRET);
        Map headerMap=initLog(apiEnum.getApiCode(),apiEnum.getV(),sign);
        String resultMsg= HttpClientUtil.doPost(SMURFS_LOG_URL,json.toString(),headerMap);
        return resultMsg;
    	logger.info("蓝精灵地址："+SMURFS_LOG_HOST);
    	ApiMessage apiMessage=new ApiMessage();
        apiMessage.setApicode(apiEnum.getApiCode());
        apiMessage.setV(apiEnum.getV());
        try {
            HttpClientFactory client= HttpClientFactory.getInstance(SMURFS_LOG_HOST,LOG_APP_KEY,LOG_APP_SECRET);
        	ApiResponse apiResponse=client.send(apiMessage,json.toString().getBytes("utf-8"));
        	com.alibaba.fastjson.JSONObject resultModel=(com.alibaba.fastjson.JSONObject) JSON.toJSON(apiResponse);
            resultModel.put("bodyStr",new String(apiResponse.getBody(),"utf-8"));
            String reslut = new String(apiResponse.getBody(),"utf-8");
            logger.info("日志返回结果reslut："+reslut);
            return new String(apiResponse.getBody(),"utf-8");
            
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("日志返回结果为reslut为空");
			return null;
		}
        
        
    	

    }*/
    

    /**
     * 生成唯一ID
     * @param prefix    前缀
     * @return
     */
    public static String getID(String prefix) throws Exception{
//    	String id = CodeGeneratorUtil.getNo(prefix);//经分销工具类型生成随机ID
    	String id = smurfsGetID(prefix);//调蓝精灵获取自增ID
    	return id;
    }
    private static String smurfsGetID(String prefix) throws Exception{
    	Object id = null;		// 返回过来的唯一ID
    	String code = SmurfsAPICodeEnum.getCodeByName(prefix);	// 根据前缀获取对应的请求编码
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("code",code);
    	String resultJson = null;
    	try {
    		resultJson = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UNIQUE_ID);   	
	    	if(StringUtils.isBlank(resultJson)){	// 重试三次
	    		for (int i = 0; i < 3; i++) {
	    			resultJson = SmurfsUtils.send(jsonObject, SmurfsAPIEnum.SMURFS_UNIQUE_ID);
					if (StringUtils.isNotBlank(resultJson)) {
						break;
					}
				}
	    	}
	    	
	    	if (StringUtils.isBlank(resultJson)) {
	    		logger.error("三次请求都没有拿到编码");
        		throw new Exception("三次请求都没有拿到编码");
			}
	        JSONObject jsonObj = JSONObject.fromObject(resultJson);
	        id = jsonObj.get("ID");
	        if(id==null || id.equals("")){
    			logger.error("异常:获取不到返回的ID");
        		throw new Exception("异常:获取不到返回的ID");
        	}
    	} catch (Exception e) {
    		logger.error("蓝精灵获取ID编码失败"+e.getMessage());
			throw new Exception("蓝精灵获取ID编码失败"+e.getMessage());
		}
        return id.toString();
	}
}
