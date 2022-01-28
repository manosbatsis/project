package com.topideal.http;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * Created by acer on 2016/7/5.
 */
public class HttpClientUtil {

    //===============header 版本======================
    /**
     * 发送Post 请求
     * @param url  地址
     * @param json  json数据
     * @return  回执报文
     */
    public static String doPost(String url, String json,Map<String,String> header){
        HttpClient httpClient=null;
        HttpPost httpPost=null;
        String result=null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //header 请求头中注入参数
            for (String key : header.keySet()) {
                httpPost.setHeader(key,header.get(key));
            }
            if (StringUtils.isNotBlank(json)) {
                StringEntity s = new StringEntity(json, "utf-8");  // 中文乱码在此解决
                s.setContentType("application/json");
                httpPost.setEntity(s);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return result;

    }

    /**
     *发送Get 请求
     * @param url 地址
     * @return  回执报文
     */
    public static String doGetReq(String url,Map<String,Object> params,Map<String,String> header) {
        HttpClient httpClient=null;
        HttpGet httpGet=null;
        String result=null;
        try{
            httpClient = new SSLClient();
            //url 请求追加参数
            if(params!=null&&params.size()>0){
                String valUrl="";
                for (Entry<String, Object> stringObjectEntry : params.entrySet()) {
                    if("".equals(valUrl)){
                        valUrl+="?";
                    }else{
                        valUrl+="&";
                    }
                    valUrl+=stringObjectEntry.getKey()+"="+stringObjectEntry.getValue();
                }
                url+=valUrl;
            }
            httpGet = new HttpGet(url);
            //header 请求头中注入参数
            for (String key : header.keySet()) {
                httpGet.setHeader(key,header.get(key));
            }
            HttpResponse response = httpClient.execute(httpGet);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpGet.releaseConnection();
        }
        return result;

    }

    /**
     * 发送Post 请求
     * @param url  地址
     * @param json  json数据
     * @return  回执报文
     */
    public static String doPatch(String url, String json,Map<String,String> header){
        HttpClient httpClient=null;
        HttpPatch httpPatch=null;
        String result=null;
        try {
            httpClient = new SSLClient();
            httpPatch = new HttpPatch(url);
            //header 请求头中注入参数
            for (String key : header.keySet()) {
                httpPatch.setHeader(key,header.get(key));
            }
            if (StringUtils.isNotBlank(json)) {
                StringEntity s = new StringEntity(json, "utf-8");  // 中文乱码在此解决
                s.setContentType("application/json");
                httpPatch.setEntity(s);
            }
            HttpResponse response = httpClient.execute(httpPatch);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"utf-8");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpPatch.releaseConnection();
        }
        return result;

    }

    //===================

    /**
     *    发送Post请求
     * @param url  地址
     * @param map  map 类型的数据结构
     * @param charset  编码
     * @return 回执报文
     */
    public static String doPost(String url, Map<String,Object> map, String charset){
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try{
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while(iterator.hasNext()){
                Entry<String,String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
            }
            if(list.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return result;
    }

    /**
     * 发送Post 请求
     * @param url  地址
     * @param json  json数据
     * @param charset  编码
     * @return  回执报文
     */
    public static String doPost(String url, String json, String charset){
        HttpClient httpClient=null;
        HttpPost httpPost=null;
        String result=null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            if (StringUtils.isNotBlank(json)) {
            	StringEntity s = new StringEntity(json, charset);  // 中文乱码在此解决
    			s.setContentType("application/json");
                httpPost.setEntity(s);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return result;

    }


    /**
     * 发送Post 请求
     * @param url  地址
     * @param xml  xml格式的数据
     * @param charset  编码
     * @return  回执报文
     */
    public static String doPostXml(String url, String xml, String charset){
        HttpClient httpClient=null;
        HttpPost httpPost=null;
        String result=null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(xml, Charset.forName("UTF-8")));
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return result;
    }

    /**
     *发送Get 请求
     * @param url 地址
     * @param charset  编码
     * @return  回执报文
     */
    public static String doGetReq(String url, String charset) {
        HttpClient httpClient=null;
        HttpGet httpGet=null;
        String result=null;
        try{
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpGet.releaseConnection();
        }
        return result;

    }

    public static void main(String[] args){
        String url = "http://121.33.205.117:18080/customs/rest/custjson/addOrder.do";
        String json = "{  \"orderId\": \"TH2016050100088\",  \"orderDate\": \"2016-06-20 16:06:31\",  \"customerId\": \"1000000167\",  \"tpl\": \"1000823\",  \"electricCode\": \"1000000167\",  \"cbepcomCode\": \"1000000167\",  \"orderType\": 1,  \"customsType\": 2,  \"freightFcy\": 45.0000000000,  \"freightFcode\": \"CNY\",  \"goodList\": [    {      \"gnum\": 1,      \"goodId\": \"EGSCMATMD\",      \"amount\": 2.0,      \"price\": 12.0000000000    }  ],  \"recipient\": {    \"name\": \"关一日\",    \"mobilePhone\": \"1008610086\",    \"phone\": \"\",    \"country\": \"中国\",    \"city\": \"广州市\",    \"province\": \"广东省\",    \"address\": \"TH2016050100002\"  }}";
         HttpClientUtil util=new HttpClientUtil();
        System.out.println(util.doPost(url,json,"utf-8"));

    }



    /**
     * 发送Post 请求  无 json
     * @param url  地址
     * @param charset  编码
     * @return  回执报文
     */
    public static String doPost(String url,String charset){
        HttpClient httpClient=null;
        HttpPost httpPost=null;
        String result=null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,charset);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return result;

    }
    
    /**
     * 发送Post 请求 
     * 带认证Authorization
     * @param url  地址
     * @param json  json数据
     * @param charset  编码
     * @return  回执报文
     */
    public static String doPost(String url, String json,Map<String,Object>parmMap,String contentType, String authorUserName,String authorPassword){
        HttpClient httpClient=null;
        HttpPost httpPost=null;
        String result=null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            //设置认证
            if (StringUtils.isNotBlank(authorUserName)&&StringUtils.isNotBlank(authorPassword)) {
            	String authorNameAndpass=authorUserName+":"+authorPassword;
                String encoding = DatatypeConverter.printBase64Binary(authorNameAndpass.getBytes("UTF-8"));
                httpPost.setHeader("Authorization", "Basic " +encoding);
			}
            Iterator iterator = parmMap.entrySet().iterator();
            while(iterator.hasNext()){
                Entry<String,String> elem = (Entry<String, String>) iterator.next();
                httpPost.setHeader(elem.getKey(),elem.getValue());
            }            
            StringEntity s = new StringEntity(json, "UTF-8");  // 中文乱码在此解决
			s.setContentType(contentType);//"application/json" application/xml
            httpPost.setEntity(s);
            HttpResponse response = httpClient.execute(httpPost);
            if(response != null){
                HttpEntity resEntity = response.getEntity();
                if(resEntity != null){
                    result = EntityUtils.toString(resEntity,"UTF-8");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpPost.releaseConnection();
        }
        return result;

    }
   
}
