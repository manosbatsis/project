/**  
 * @copyright:Copyright Corporation 2017 
 * @company:Guangdong Top Ideal SCM Service Group Co., Ltd. 
 * @date 2017年6月5日 上午10:30:16 
 * @author Qin Dachang 
 */
package com.topideal.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** 
 * 签名加解密工具类 
 * @date 2017年6月5日 上午10:30:16 
 * @author Qin Dachang 
 */
public class SignUtil {

	//日志打印
	private static final Logger logger = LoggerFactory.getLogger(SignUtil.class);
	

	/**
	 * 签名加密
	 * @date 2017年5月26日 下午6:42:59 
	 * @author Qin Dachang
	 * @param json vo对象转换的json字符串
	 * @return
	 */
	public static String sign(String json, String appSecret){
		String sign = "";
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//获取明文
		String plain = getPlain(map, appSecret);
		logger.info("签名明文：" + plain);
		//进行MD5加密、base64加密
		sign = base64Md5(plain);
		logger.info("签名密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("签名密文转换大写：" + sign);
		return sign;
	}
	
	/**
	 * 外部平台签名加密
	 * @date 2017年5月26日 下午6:42:59 
	 * @author Qin Dachang
	 * @param json vo对象转换的json字符串
	 * @return
	 */
	public static String signForOuter(String json, String appSecret){
		String sign = "";
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//获取明文
		String plain = getPlain(map, appSecret);
		logger.info("签名明文：" + plain);
		//进行MD5加密
		sign = md5(plain);
		logger.info("签名密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("签名密文转换大写：" + sign);
		return sign;
	}
	
	/**
	 * 订单100签名加密
	 * @date 2017年5月26日 下午6:42:59 
	 * @author Qin Dachang
	 * @param json vo对象转换的json字符串
	 * @return
	 */
	public static String signForOrder100(String json, String appSecret){
		String sign = "";
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//获取明文
		StringBuilder sb = new StringBuilder(appSecret);
		//遍历map，拼接报文明文
		Object obj = null;
		for (String key : map.keySet()) {
			obj = map.get(key);
			if(null != obj && !"null".equals(obj)){
				sb.append(key).append(obj.toString());	
			}
		}
		sb.append(appSecret);
		logger.info("签名明文：" + sb.toString());
		//进行MD5加密
		sign = md5(sb.toString());
		logger.info("签名密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("签名密文转换大写：" + sign);
		return sign;
	}
	
	/**
	 * 跨境宝签名验签
	 * @date 2017年6月5日 上午10:34:11 
	 * @author Qin Dachang
	 * @param json 接收到的报文
	 * @return
	 */
	public static boolean verify(String json, String appSecret){
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//去除密文的同时保存起来
		String verify = (String) map.remove(Constants.FinanceCommonParam.SIGN);
		//获取明文
		String plain = getPlain(map, appSecret);
		logger.info("验签明文：" + plain);
		//进行MD5加密
		String sign = base64Md5(plain);
		logger.info("验签密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("验签密文转换大写：" + sign);
		//与收到的密文比较，得出结果
		return verify.equals(sign);
	}
	/**
	 * 获取跨境宝签名
	 * @param json
	 * @param appSecret
	 * @return
	 * 杨创
	 * 2018/8/1
	 */
	public static String getVerify(String json, String appSecret){
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//去除密文的同时保存起来
		String verify = (String) map.remove(Constants.FinanceCommonParam.SIGN);
		//获取明文
		String plain = getPlain(map, appSecret);
		logger.info("验签明文：" + plain);
		//进行MD5加密
		String sign = md5(plain);
		logger.info("验签密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("验签密文转换大写：" + sign);
				//与收到的密文比较，得出结果
		return sign;
	}
	/**
	 * 外部平台签名验签
	 * @date 2017年6月5日 上午10:34:11 
	 * @author Qin Dachang
	 * @param json 接收到的报文
	 * @return
	 */
	public static boolean verifyForOuter(String json, String appSecret){
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//去除密文的同时保存起来
		String verify = (String) map.remove(Constants.FinanceCommonParam.SIGN);
		//获取明文
		String plain = getPlain(map, appSecret);
		logger.info("验签明文：" + plain);
		//进行MD5加密
		String sign = md5(plain);
		logger.info("验签密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("验签密文转换大写：" + sign);
		//与收到的密文比较，得出结果
		return verify.equals(sign);
	}
	
	/**
	 * 登录外部系统数字签名
	 * @date 2017年9月8日 下午2:39:43 
	 * @author Qin Dachang
	 * @param plain
	 * @return
	 */
	public static String signLogin(String plain){
		logger.info("登录外部系统数字签名加密前的字符串："+plain);
		//加密
		String sign = md5(plain);
		logger.info("登录外部系统数字签名加密后的字符串："+sign);
		//转大写
		sign = StringUtils.upperCase(sign);
		logger.info("登录外部系统数字签名转换成大写字符串："+sign);
		return sign;
	}
	
	/**
	 * 外部系统登录签名验签
	 * @date 2017年9月8日 下午2:40:13 
	 * @author Qin Dachang
	 * @param customerId
	 * @param sign
	 * @return
	 */
	public static boolean verifyLogin(String customerId, String sign, String signString){
		//生成明文
		String plain = signString + customerId + signString;
		//加密
		String verify = md5(plain);
		//转大写
		verify = StringUtils.upperCase(verify);
		//比对，返回结果
		return verify.equals(sign);
	}
	
	/**
	 * 请求奇门签名
	 * @date 2017年12月28日 下午3:03:11 
	 * @author Qin Dachang
	 * @param map
	 * @param xml
	 * @param appSecret
	 * @return
	 */
	public static String signForQiMen(Map<String, Object> map, String xml, String appSecret){
		TreeMap<String, Object> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		//获取明文
		StringBuilder sb = new StringBuilder(appSecret);
		//遍历map，拼接报文明文
		Object obj = null;
		for (String key : treeMap.keySet()) {
			obj = map.get(key);
			if(null != obj && !"null".equals(obj)){
				sb.append(key).append(obj.toString());	
			}
		}
		sb.append(xml).append(appSecret);
		System.out.println("签名明文：" + sb.toString());
		logger.info("签名明文：" + sb.toString());
		//进行MD5加密
		String sign = md5(sb.toString());
		logger.info("签名密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("签名密文转换大写：" + sign);
		return sign;
	}
	
	/**
	 * 奇门请求验签
	 * @date 2017年12月28日 下午3:03:31 
	 * @author Qin Dachang
	 * @param map
	 * @param xml
	 * @param appSecret
	 * @return
	 */
	public static boolean verifyForQiMen(Map<String, Object> map, String xml, String appSecret){
		TreeMap<String, Object> treeMap = new TreeMap<>();
		treeMap.putAll(map);
		//去除密文的同时保存起来
		String verify = (String) treeMap.remove(Constants.FinanceCommonParam.SIGN);
		//获取明文
		StringBuilder sb = new StringBuilder(appSecret);
		//遍历map，拼接报文明文
		Object obj = null;
		for (String key : treeMap.keySet()) {
			obj = map.get(key);
			if(null != obj && !"null".equals(obj)){
				sb.append(key).append(obj.toString());	
			}
		}
		sb.append(xml).append(appSecret);
		System.out.println("签名明文：" + sb.toString());
		logger.info("签名明文：" + sb.toString());
		//进行MD5加密
		String sign = md5(sb.toString());
		logger.info("签名密文：" + sign);
		//转换大写
		sign = StringUtils.upperCase(sign);
		logger.info("签名密文转换大写：" + sign);
		return verify.equals(sign);
	}
	
	
	/**
	 * 获取签名明文
	 * @date 2017年6月5日 上午10:36:45 
	 * @author Qin Dachang
	 * @param treeMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private static String getPlain(Map<String, Object> map, String appSecret){
		StringBuilder sb = new StringBuilder(appSecret);
		//遍历map，拼接报文明文
		Object obj = null;
		for (String key : map.keySet()) {
			if(Constants.FinanceCommonParam.SIGN.equals(key)){							
				continue;				
			}
			obj = map.get(key);
			if(null != obj){
				if(obj instanceof List){
					if(((List) obj).size() > 0){
						sb.append(key).append(obj.toString());
					}
				}else if(obj instanceof String){
					if(StringUtils.isNotBlank((CharSequence) obj)){	
						sb.append(key).append(obj.toString());				
					}
				}else{					
					sb.append(key).append(obj.toString());	
				}
			}
		}
		sb.append(appSecret);
		return sb.toString();
	}

	/**
	 * MD5加密
	 * @date 2017年3月14日 下午3:34:28
	 * @author Qin Dachang
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return encrypt(str, "MD5");
	}
	
	/**
	 * SHA加密
	 * @date 2017年9月8日 下午3:23:23 
	 * @author Qin Dachang
	 * @param inputText
	 * @return
	 */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }
    
    /**
     * base64加码
     * @date 2018年3月12日 下午5:18:57 
     * @author Qin Dachang
     * @param str
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static String base64Encode(String str, String appSecret) throws UnsupportedEncodingException{
    	BASE64Encoder encoder = new BASE64Encoder();
    	logger.info("base64第一次加密前：" + str);
    	str = encoder.encode(str.getBytes(Constants.System.CHARSET_UTF8));
    	logger.info("base64第一次加密后：" + str);
    	str = str + appSecret;
    	logger.info("base64第二次加密前：" + str);
    	str = encoder.encode(str.getBytes(Constants.System.CHARSET_UTF8));
    	logger.info("base64第二次加密后：" + str);
    	return str;
    }
    
    /**
     * base64解码
     * @date 2018年3月12日 下午5:18:57 
     * @author Qin Dachang
     * @param str
     * @return
     * @throws IOException 
     */
    public static String base64Decode(String str, String appsecret) throws IOException{
    	BASE64Decoder decoder = new BASE64Decoder();
    	logger.info("base64第一次解密前：" + str);
    	byte[] bytes = decoder.decodeBuffer(str);
    	str = new String(bytes, Constants.System.CHARSET_UTF8);
    	logger.info("base64第一次解密后：" + str);
    	str = str.replace(appsecret, "");
    	logger.info("base64第二次解密前：" + str);
    	bytes = decoder.decodeBuffer(str);
    	str = new String(bytes, Constants.System.CHARSET_UTF8);
    	logger.info("base64第二次解密后：" + str);
    	return str;
    }

	/**
	 * MD5加密
	 * @date 2017年3月14日 下午3:34:28
	 * @author Qin Dachang
	 * @param str
	 * @return
	 */
	public static String base64Md5(String str) {
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			byte s[] = m.digest(str.getBytes(Constants.System.CHARSET_UTF8));
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(s);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * md5或者sha-1加密
	 * @date 2017年9月8日 下午3:06:56 
	 * @author Qin Dachang
	 * @param inputText
	 * @param algorithmName
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            return null;
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = Constants.SignType.MD5;
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes(Constants.System.CHARSET_UTF8));
            byte s[] = m.digest();
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptText;
    }

	/**
	 * 返回十六进制字符串
	 * @date 2017年9月8日 下午3:06:42 
	 * @author Qin Dachang
	 * @param arr
	 * @return
	 */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }
	
	public static void main(String[] args) {
		String body="{\"app_key\":\"3397326205454853200\",\"cbepcom_code\":\"1000004790\",\"created\":\"2020-05-23 16:58:04\",\"declare_plan\":\"0011\",\"discount\":\"7.00\",\"electric_code\":\"1000000204\",\"exorder_id\":\"200523-406128887022698\",\"logistics_code\":\"1000000239\",\"method\":\"epass.push.orders.b2c.add\",\"modified\":\"2020-05-23 16:58:04\",\"order_goods\":[{\"dec_total\":\"114.5\",\"good_name\":\"elevit 爱乐维\\u00A0Women's\\u00A0Multi产后复合营养维生素片 100片/盒\",\"goods_id\":\"JTALW005\",\"index\":\"1\",\"num\":1,\"price\":\"114.50\"}],\"order_id\":\"XP0020052316200508566334005095\",\"payment\":\"124.92\",\"payment_goods\":\"114.50\",\"receiver_address\":\"广东省 中山市 东凤镇 万科金色家园五期（万科里）30-1单元\",\"receiver_city\":\"中山市\",\"receiver_country\":\"中国\",\"receiver_district\":\"东凤镇\",\"receiver_mobile\":\"18988588961\",\"receiver_name\":\"倪凤玉\",\"receiver_phone\":\"18988588961\",\"receiver_state\":\"广东省\",\"shop_id\":\"557881607\",\"sign\":\"3FB4B3FA6A69C676323CBD3E4D0B43C5\",\"timestamp\":\"2020-05-23 16:58:04\",\"v\":\"1.0\",\"warehouse_id\":\"WMS_360_04\",\"way_frt_fee\":\"0.00\",\"way_frt_fee_cy\":\"CNY\",\"way_ind_fee\":\"0.00\",\"way_tax_fee\":\"10.42\"}";
		String appSecret ="ix8hdzlocj1pfhzweughpq4tgi45hmb0";
		SignUtil.verifyForOuter(body, appSecret);
	}
}
