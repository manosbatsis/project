package com.topideal.tools;


import java.security.MessageDigest;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

public class SmurfSign {

	private SmurfSign() {
		super();
	}

    private static final Logger LOGGER = LoggerFactory.getLogger(SmurfSign.class);


    /**
     * 生成签名方法
     * @param json  请求参数
     * @param secret  密钥
     * @return
     */
    public static String buildRequestPara(String json,String secret){
    	if(json.trim().startsWith("[")){
    		JSONArray jsonArray = JSONArray.fromObject(json);
    		JSONObject jsonObject= JSONObject.fromObject(jsonArray.get(0));
    		return buildRequestPara(jsonObject, secret);
    	}
        JSONObject jsonObject= JSONObject.fromObject(json);
        return buildRequestPara(jsonObject, secret);
    }


    /**
     * 生成要请求给E速达的参数
     * @param jsonObject 请求前的SON对象
     * @return 要请求的参数
     */
    private static String buildRequestPara(JSONObject jsonObject,String secret) {
        //除去数组中的空值和签名参数
        Map<String, Object> sPara = paraFilter(jsonObject);
        //生成签名结果
        String mysign = buildRequestMysign(sPara,secret);
        return mysign;
    }


    /**
     * 除去数组中的空值和签名参数
     * @param jsonObject 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    private static Map<String, Object> paraFilter(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (jsonObject == null || jsonObject.size() <= 0) {
            return result;
        }
        for (String key : (Set<String>) jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            //空值不参与签名计算
            if(value!=null&& !"".equals(value) &&(value instanceof Double)&&Double.valueOf(value.toString())==0){
            	result.put(key, 0);
            }else if(value!=null&& !"".equals(value) &&!(value instanceof JSONNull)){
            	result.put(key, value);
            }
        }
        return result;
    }


    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
    private static String buildRequestMysign(Map<String, Object> sPara,String secret) {
        String prestr = createLinkString(sPara);
        String mysign=sign(prestr, secret, "utf-8");
        return mysign;
    }

    /**
     * 集合中的元素按一定的规则进行拼接
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    private static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys, Collator.getInstance(Locale.ENGLISH));
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            prestr = prestr + key + value;
        }
        return prestr;
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param secret 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    private static String sign(String text, String secret, String input_charset) {
        text = secret+text+secret;
        return Encode32MD5(text,input_charset);
    }


    private static String Encode32MD5(String myinfo,String input_charset) {
        byte[] digesta = null;
        try {
            byte[] btInput = myinfo.getBytes(input_charset);
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            digesta = mdInst.digest();
        }
        catch (Exception ex) {
            LOGGER.error("--------------------MD5加密失败-------------------");
        }
        return byte2hex(digesta);
    }

    private static String byte2hex(byte[] b) {
        StringBuilder md5Str = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF)).toUpperCase();
            if (stmp.length() == 1) {
                md5Str.append(0 + stmp);
            } else {
                md5Str.append(stmp);
            }
            if (n < b.length - 1)
            {md5Str.append("");}
        }
        return md5Str.toString();
    }





}
