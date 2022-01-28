package com.topideal.mq;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.enums.SmurfsAPICodeEnum;
import com.topideal.enums.SmurfsAPIEnum;

import net.sf.json.JSONObject;
import priv.smurfs.tools.gateway.client.GatewayHttpClient;
import priv.smurfs.tools.gateway.common.ApiResponse;

/**
 * 蓝精灵获取唯一ID
 * @author wenyan
 *
 */
public class SmurfsGetIDTest {	
	  private static final Logger logger= LoggerFactory.getLogger(SmurfsGetIDTest.class);

	    /**
	     * 调用外调接口
	     * @param json  业务报文
	     * @param apiEnum   接口枚举
	     * @return
	     */
	    public static String send(JSONObject json, SmurfsAPIEnum apiEnum){    	
			String smurfsHost="gateway.smurfs.topideal.mobi"; // 蓝精灵地址(生产环境)
			String appSecret = "D8478AA375024F218902E72B04D33F58";	// appsecret(生产环境)
			String appKey = "10002";	// appkey(生产环境)
	    	/*
	      	String smurfsHost="10.10.102.154:520";	// 蓝精灵地址(测试环境)
	    	String appSecret = "11C12A2B30F84A258CC1CFC95057A935";	// appsecret(测试环境)
	    	String appKey = "00000";	// appkey(测试环境)*/
	    	logger.info("蓝精灵地址：SMURFS_HOST:"+smurfsHost +",APP_KEY:"+appKey+"APP_SECRET:"+appSecret);   
	    	
	        try {
	        	GatewayHttpClient gatewayHttpClient =
		                GatewayHttpClient.getInstance(smurfsHost, appKey, appSecret);	      
		         //发送请求
		         ApiResponse response = gatewayHttpClient.send(apiEnum.getApiCode(),apiEnum.getV(),json.toString());
		        String resultJson = response.getResultJson();
		        logger.info("蓝精灵返回结果："+resultJson);
	            return resultJson;
	            
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("蓝精灵返回结果为空");
				return null;
			}
	        
	    }
		public static void main(String[] args) throws Exception {
			String prefix="QTOP20";	// 前缀
	    	Object id = null;		// 返回过来的唯一ID
	    	String code = SmurfsAPICodeEnum.getCodeByName(prefix);	// 根据前缀获取对应的请求编码
	    	JSONObject jsonObject = new JSONObject();
	    	jsonObject.put("code",code);
	    	String resultJson = null;
	    	try {
	    		resultJson = send(jsonObject,SmurfsAPIEnum.SMURFS_UNIQUE_ID);   	
		    	if(StringUtils.isBlank(resultJson)){	// 重试三次
		    		for (int i = 0; i < 3; i++) {
		    			resultJson = send(jsonObject,SmurfsAPIEnum.SMURFS_UNIQUE_ID);
						if (StringUtils.isNotBlank(resultJson)) {
							break;
						}
					}
		    	}
		    	if (StringUtils.isBlank(resultJson)) {
		    		System.out.println("三次请求都没有拿到编码");
	        		throw new Exception("三次请求都没有拿到编码");
				}
		        JSONObject jsonObj = JSONObject.fromObject(resultJson);
		        id = jsonObj.get("ID");
		        if(id==null || id.equals("")){
		        	System.out.println("异常:获取不到返回的ID");
	        		throw new Exception("异常:获取不到返回的ID");
	        	}
	    	} catch (Exception e) {
	    		System.out.println("蓝精灵获取ID编码失败"+e.getMessage());
				throw new Exception("蓝精灵获取ID编码失败"+e.getMessage());
			}
	        System.out.println("生成的唯一ID:"+id.toString());
		}
}