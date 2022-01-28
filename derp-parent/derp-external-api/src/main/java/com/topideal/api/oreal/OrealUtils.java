package com.topideal.api.oreal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.XmlConverUtil;
import com.topideal.http.HttpClientUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 推送欧莱雅工具类
 * @author gy
 *
 */
public class OrealUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrealUtils.class);
	
	/**
	 * 获取Token
	 * @param requestUrl
	 * @param jsonStr
	 * @return
	 */
	public static String getOREALToken() {
		LOGGER.info("--------获取欧莱雅Token开始--------：");
		//LOGGER.info("请求:json "+ json);       
		LOGGER.info("请求地址: oreal.url"+ ApolloUtil.orealUrl);
		LOGGER.info("请求: oreal.token.username"+ ApolloUtil.orealTokenUsername);
		LOGGER.info("请求: oreal.token.password"+ ApolloUtil.orealTokenPassword);
		LOGGER.info("请求: oreal.auth.username"+ ApolloUtil.orealAuthUsername);
		LOGGER.info("请求: oreal.auth.password"+ ApolloUtil.orealAuthPassword);
		LOGGER.info("请求: oreal.account.username"+ ApolloUtil.orealAccountUsername);
		LOGGER.info("请求: oreal.001.method"+ ApolloUtil.oreal001Method);
		LOGGER.info("请求: oreal.002.method"+ ApolloUtil.oreal002Method);
		
		String token="";	
		try {
			//设置Heard参数
			Map<String, Object>parmMap=new HashMap<String, Object>();
			parmMap.put("username", ApolloUtil.orealTokenUsername);
			parmMap.put("password", ApolloUtil.orealTokenPassword);			
			//设置Authorization（授权)
			String authorUserName=ApolloUtil.orealAuthUsername;
			String authorPassword=ApolloUtil.orealAuthPassword; 
			//请求地址
			String url=ApolloUtil.orealUrl+ApolloUtil.oreal001Method;			
			JSONObject requestJson= new JSONObject();;
			String responseStr = HttpClientUtil.doPost(url, requestJson.toString(), parmMap,"application/json",authorUserName,authorPassword);	
			if (StringUtils.isBlank(responseStr)) {
				throw new RuntimeException("获取欧莱雅Token结果为空");
			}
			JSONObject responseJson = JSONObject.fromObject(responseStr);
			String code = responseJson.getString("code");
			if (!"1000".equals(code)) {
				String msg = (String) responseJson.get("msg");	
				throw new RuntimeException(msg);
			}
			token = (String) responseJson.get("token");
			LOGGER.info("--------获取欧莱雅Token--------：" + responseStr);
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("获取欧莱雅Token密失败:" + e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		LOGGER.info("--------获取欧莱雅Token结束--------：");
		return token;
	}
	
	/**
	 * 获取欧莱雅商品
	 * @param xml
	 * @param token
	 * @return
	 */
	public static JSONArray getOREALGoods(String xml ,String token) {
		LOGGER.info("--------获取欧莱雅商品开始--------：");	
		LOGGER.info("请求:xml "+ xml);       
		LOGGER.info("请求地址: oreal.url"+ ApolloUtil.orealUrl);
		LOGGER.info("请求: oreal.token.username"+ ApolloUtil.orealTokenUsername);
		LOGGER.info("请求: oreal.token.password"+ ApolloUtil.orealTokenPassword);
		LOGGER.info("请求: oreal.auth.username"+ ApolloUtil.orealAuthUsername);
		LOGGER.info("请求: oreal.auth.password"+ ApolloUtil.orealAuthPassword);
		LOGGER.info("请求: oreal.account.username"+ ApolloUtil.orealAccountUsername);
		LOGGER.info("请求: oreal.001.method"+ ApolloUtil.oreal001Method);
		LOGGER.info("请求: oreal.002.method"+ ApolloUtil.oreal002Method);
		JSONArray jsonArray=null;
		try {
			//设置Heard参数
			Map<String, Object>parmMap=new HashMap<String, Object>();
			parmMap.put("username", ApolloUtil.orealTokenUsername);
			parmMap.put("password", ApolloUtil.orealTokenPassword);	
			parmMap.put("token", token);	
			//设置Authorization（授权)
			String authorUserName=ApolloUtil.orealAuthUsername;
			String authorPassword=ApolloUtil.orealAuthPassword; 
			//请求地址
			String url=ApolloUtil.orealUrl+ApolloUtil.oreal002Method;			
			String responseStr = HttpClientUtil.doPost(url, xml, parmMap,"application/json",authorUserName,authorPassword);	
			if (StringUtils.isBlank(responseStr)) {
				throw new RuntimeException("获取欧莱雅商品返回结果为空");
			}			
			/*org.json.JSONObject xmlJSONObj = XML.toJSONObject(responseStr);
			org.json.JSONObject ufinterfaceJson = (org.json.JSONObject) xmlJSONObj.get("ufinterface");
			org.json.JSONObject sendresultJson = (org.json.JSONObject) ufinterfaceJson.get("sendresult");
			String contentJson1 = (String) sendresultJson.get("content");*/
			String jsonStr = XmlConverUtil.XmlToJson(responseStr);			
			JSONObject fromObject = JSONObject.fromObject(jsonStr);
			JSONObject ufinterfaceJson = (JSONObject) fromObject.get("ufinterface");
			JSONObject sendresultJson =  (JSONObject) ufinterfaceJson.get("sendresult");
			String contentJson1 = (String) sendresultJson.get("content");	
			String getInfo = contentJson1.substring(contentJson1.indexOf("[") , contentJson1.indexOf("]")+1);
			getInfo="{"+"\"info\":"+getInfo+"}";
			JSONObject jsonObject = JSONObject.fromObject(getInfo);
			jsonArray = jsonObject.getJSONArray("info");
			
			
			//JSONObject fromObject2 = JSONObject.fromObject(contentJson1);
			
			
			LOGGER.info("--------获取欧莱雅商品getInfo--------：" + getInfo);
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("获取欧莱雅Token密失败:" + e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		LOGGER.info("--------获取欧莱雅商品结束--------：");
		return jsonArray;
	}

	/**
	 * 获取欧莱雅商品
	 * @param xml
	 * @param token
	 * @return
	 */
	public static JSONArray getOREALOrder(String xml ,String token) {
		LOGGER.info("--------获取欧莱雅商品开始--------：");	
		LOGGER.info("请求:xml "+ xml);       
		LOGGER.info("请求地址: oreal.url"+ ApolloUtil.orealUrl);
		LOGGER.info("请求: oreal.token.username"+ ApolloUtil.orealTokenUsername);
		LOGGER.info("请求: oreal.token.password"+ ApolloUtil.orealTokenPassword);
		LOGGER.info("请求: oreal.auth.username"+ ApolloUtil.orealAuthUsername);
		LOGGER.info("请求: oreal.auth.password"+ ApolloUtil.orealAuthPassword);
		LOGGER.info("请求: oreal.account.username"+ ApolloUtil.orealAccountUsername);
		LOGGER.info("请求: oreal.001.method"+ ApolloUtil.oreal001Method);
		LOGGER.info("请求: oreal.002.method"+ ApolloUtil.oreal002Method);
		JSONArray jsonArray=null;
		try {
			//设置Heard参数
			Map<String, Object>parmMap=new HashMap<String, Object>();
			parmMap.put("username", ApolloUtil.orealTokenUsername);
			parmMap.put("password", ApolloUtil.orealTokenPassword);	
			parmMap.put("token", token);	
			//设置Authorization（授权)
			String authorUserName=ApolloUtil.orealAuthUsername;
			String authorPassword=ApolloUtil.orealAuthPassword; 
			//请求地址
			String url=ApolloUtil.orealUrl+ApolloUtil.oreal002Method;			
			String responseStr = HttpClientUtil.doPost(url, xml, parmMap,"application/json",authorUserName,authorPassword);	
			if (StringUtils.isBlank(responseStr)) {
				throw new RuntimeException("获取欧莱雅返回结果为空");
			}			
			/*org.json.JSONObject xmlJSONObj = XML.toJSONObject(responseStr);
			org.json.JSONObject ufinterfaceJson = (org.json.JSONObject) xmlJSONObj.get("ufinterface");
			org.json.JSONObject sendresultJson = (org.json.JSONObject) ufinterfaceJson.get("sendresult");
			String contentJson1 = (String) sendresultJson.get("content");*/
			String jsonStr = XmlConverUtil.XmlToJson(responseStr);			
			JSONObject fromObject = JSONObject.fromObject(jsonStr);
			JSONObject ufinterfaceJson = (JSONObject) fromObject.get("ufinterface");
			JSONObject sendresultJson =  (JSONObject) ufinterfaceJson.get("sendresult");
			String contentJson1 = (String) sendresultJson.get("content");	
			String getInfo = contentJson1.substring(contentJson1.indexOf("[") , contentJson1.indexOf("]")+1);
			getInfo="{"+"\"info\":"+getInfo+"}";
			JSONObject jsonObject = JSONObject.fromObject(getInfo);
			jsonArray = jsonObject.getJSONArray("info");

			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("获取欧莱雅Token密失败:" + e.getMessage(), e);
			jsonArray=new JSONArray();
		}
		LOGGER.info("--------获取欧莱雅结束--------：");
		return jsonArray;
	}

	public static void main(String[] args) {
		try {	
			String orealToken = getOREALToken();
			System.out.println(orealToken);
			/*String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MjM2QSIsInN1YiI6IntcInVpZFwiOlwiZGlzcXVlcnlcIixcInV0eVwiOlwiaWZrZGlqMSMqUEJkZlxcdTAwMjZKa2xhXCJ9IiwiaWF0IjoxNjExMDQ1NTcwLCJleHAiOjE2MTExMzE5NzB9.cyF_Pf44xr9Lf0ZK2ceZ6OgPrOuKZKdZgKa7poi4kfw";
			long time = TimeUtils.getNow().getTime();
			String requesttime = TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd HH:mm:ss");
			String xml="<ufinterface account=\"05\" billtype=\"disQuery\" filename=\"\" isexchange=\"Y\" proc=\"add\" receiver=\"\" replace=\"Y\" roottag=\"\" sender=\"DIS\" subbilltype=\"\">";
			xml=xml+"<bill id=\""+time+"\">";
			xml=xml+"<billhead>"
					+ "<billid>"+time+"</billid>"
					+ "<userid>"+13942001+"</userid>"
					+ "<pk_corp>"+1735+"</pk_corp>"
					+ "<requesttime>"+requesttime+"</requesttime>"
					+ "<sqlcode>"+13942001+"</sqlcode>"
					+"</billhead>"
					+"<billbody>"
					+"<entry>"
					+"<code>"+1394200102+"</code>"  
					+"<field>"+"invcode"+"</field>" 
					+"<table>"+"bd_invbasdoc"+"</table>" 
					+"<condition>"+"in"+"</condition>" 
					+"<value>"+"K3523501"+"</value>"
					+"</entry>"
					+"</billbody>"
					+"</bill>"
					+"</ufinterface>";		
			
			JSONArray orealGoods = getOREALGoods(xml, token)*/;
			System.out.println(orealToken);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
