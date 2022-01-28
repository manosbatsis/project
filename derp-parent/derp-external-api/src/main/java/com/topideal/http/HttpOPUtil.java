package com.topideal.http;


import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * http 请求，未加密
 * Created by weixiaolei on 2017/3/7.
 */
public class HttpOPUtil {

    public static final Logger LOGGER = Logger.getLogger(HttpOPUtil.class);

    /**
     * 发送Post 请求
     * @param url  地址
     * @param json  json数据
     * @param charset  编码
     * @return  回执报文
     */
    public static String doPost(String url, String json, String charset){
        SSLClient httpClient=null;
        HttpPost httpPost=null;
        String result=null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            if (StringUtils.isNotBlank(json)) {
                StringEntity s = new StringEntity(json, charset);  // 中文乱码在此解决
                s.setContentType("application/x-www-form-urlencoded");
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
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String doPost(String url, String param,Map<String, String> header) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            if(header != null) {
                // 设置请求头参数
                for(Map.Entry<String, String> entry : header.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("usercms-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }




}
