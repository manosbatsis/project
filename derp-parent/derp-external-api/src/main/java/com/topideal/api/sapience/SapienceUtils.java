package com.topideal.api.sapience;

import com.topideal.api.sapience.sapience008.FinancingQueryRequest;
import com.topideal.api.sapience.sapience010.FinancingAttachmentDownloadRequest;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.http.HttpClientUtil;
import com.topideal.http.SSLClient;
import com.topideal.tools.SapienceSignUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;

/**
 * @Description: 普洛斯交互工具类
 **/
public class SapienceUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SapienceUtils.class);

    /**
     * 请求接口
     * @param requestJson 请求 报文
     * @return
     */
    public static String sendSapience(JSONObject requestJson) {

        /**返回信息*/
        JSONObject returnJson = new JSONObject();
        LOGGER.info("卓普信接口请求报文：" + requestJson);
        try {
            requestJson.put("appkey", ApolloUtil.sapienceAppKey);
            requestJson.put("appSecret", ApolloUtil.sapienceAppSecret);
            String json = requestJson.toString();
            
            /**处理% 特殊字符*/
            //json = json.replaceAll("%(?![0-9a-fA-F]{2})", "%25") ;
            json = json.replaceAll("%(?![0-9]{2})", "%25") ;
            json = json.replaceAll("\\+", "%2B") ;
            
            json = URLDecoder.decode(json, "utf-8");
            String sign = SapienceSignUtils.getSign(json, ApolloUtil.sapienceAppSecret);
            requestJson.put("sign", sign);
            LOGGER.info("卓普信接口请求报文："+ requestJson);
            String responseStr = HttpClientUtil.doPost(ApolloUtil.sapienceUrl, requestJson.toString(), "UTF-8");
            LOGGER.info("卓普信接口响应报文：" + responseStr);
            return responseStr;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("卓普信接口请求失败:" + e.getMessage(), e);
            returnJson.put("status", "1");
            returnJson.put("notes", "卓普信接口请求失败," +  e.getMessage());
            return returnJson.toString();
        }
    }
    
    /**
     * 请求下载接口
     * @param requestJson 请求 报文
     * @return
     */
    public static File sendSapienceFileRequst(JSONObject requestJson, String fileSourcePath, String fileName) {

        /**返回信息*/
        JSONObject returnJson = new JSONObject();
        LOGGER.info("卓普信接口请求报文：" + requestJson);
        
        HttpClient httpClient=null;
        HttpPost httpPost=null;
        File returnFile = null ;
        
        try {
            requestJson.put("appkey", ApolloUtil.sapienceAppKey);
            requestJson.put("appSecret", ApolloUtil.sapienceAppSecret);
            String json = requestJson.toString();
            
            /**处理% 特殊字符*/
            json = json.replaceAll("%(?![0-9a-fA-F]{2})", "%25") ;
            json = json.replaceAll("\\+", "%2B") ;
            
            json = URLDecoder.decode(json, "utf-8");
            String sign = SapienceSignUtils.getSign(json, ApolloUtil.sapienceAppSecret);
            requestJson.put("sign", sign);
            LOGGER.info("卓普信接口请求报文："+ requestJson);
            
            httpClient = new SSLClient();
            httpPost = new HttpPost(ApolloUtil.sapienceUrl);
            if (StringUtils.isNotBlank(requestJson.toString())) {
            	StringEntity s = new StringEntity(requestJson.toString(), "UTF-8");  // 中文乱码在此解决
    			s.setContentType("application/json");
                httpPost.setEntity(s);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    
                	InputStream is = resEntity.getContent();
                	
                	returnFile = new File(fileSourcePath);
                    
                	if(!returnFile.exists()) {
                		returnFile.mkdirs() ;
                	}
                	
                	returnFile = new File(fileSourcePath + File.separator + fileName);
                	
                    FileOutputStream fileout = new FileOutputStream(returnFile);
                    
                    byte[] buffer = new byte[1024];
                    int ch = 0;
                    while ((ch = is.read(buffer)) != -1) {
                        fileout.write(buffer, 0, ch);
                    }
                    is.close();
                    fileout.flush();
                    fileout.close();
                }
            }
            
            return returnFile ;
            
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("卓普信接口请求失败:" + e.getMessage(), e);
            returnJson.put("status", "1");
            returnJson.put("notes", "卓普信接口请求失败," +  e.getMessage());
            
            return null ;
            
        } finally {
        	httpPost.releaseConnection();
        }
        
    }
    
    public static void main(String[] args) {
    	FinancingAttachmentDownloadRequest fileRequest = new FinancingAttachmentDownloadRequest() ;
		
		fileRequest.setFileKey("group1/M00/00/C2/rBAb-mBFxoGAMyj6AADjDylf4os059.pdf");
		fileRequest.setFileName("销售订单0308.pdf");
		fileRequest.setMethod(EpassAPIMethodEnum.SAPIENCE_E010_METHOD.getMethod());
		fileRequest.setSourceCode("10");
		
		JSONObject fileRequestJson = JSONObject.fromObject(fileRequest);
		
		SapienceUtils.sendSapienceFileRequst(fileRequestJson, "\\data\\derpfile\\tempFile\\1000030", "销售订单0308.pdf");
		
    }
}
