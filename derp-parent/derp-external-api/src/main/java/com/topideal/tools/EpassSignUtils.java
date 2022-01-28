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
 * 跨境宝签名加解密工具类 
 * @author zhanghx
 */
public class EpassSignUtils {

	//日志打印
	private static final Logger logger = LoggerFactory.getLogger(EpassSignUtils.class);
	
	//常量池
	public static final String CHARSET_UTF8 = "utf-8";
	public static final String MD5 = "md5";
	public static final String METHOD = "method";
	public static final String APPKEY = "appKey";
	public static final String TIMESTAMP = "timesTamp";
	public static final String SIGN = "sign";
	public static final String V = "1.0";
	public static final String SOURCE_CODE = "10";

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
		TreeMap<String, Object> map = new TreeMap<String, Object>();
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
		TreeMap<String, Object> map = new TreeMap<String, Object>();
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
		TreeMap<String, Object> map = new TreeMap<String, Object>();
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
	 * 签名验签
	 * @date 2017年6月5日 上午10:34:11 
	 * @author Qin Dachang
	 * @param json 接收到的报文
	 * @return
	 */
	public static boolean verify(String json, String appSecret){
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//去除密文的同时保存起来
		String verify = (String) map.remove(SIGN);
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
	 * 外部平台签名验签
	 * @date 2017年6月5日 上午10:34:11 
	 * @author Qin Dachang
	 * @param json 接收到的报文
	 * @return
	 */
	public static boolean verifyForOuter(String json, String appSecret){
		//json转换成map
		TreeMap<String, Object> map = new TreeMap<String, Object>();
		map.putAll(JSON.parseObject(json, new TypeReference<LinkedHashMap<String, Object>>(){}, Feature.OrderedField));
		//去除密文的同时保存起来
		String verify = (String) map.remove(
				SIGN);
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
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
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
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		treeMap.putAll(map);
		//去除密文的同时保存起来
		String verify = (String) treeMap.remove(SIGN);
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
			if(SIGN.equals(key)){							
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
    	str = encoder.encode(str.getBytes(CHARSET_UTF8));
    	logger.info("base64第一次加密后：" + str);
    	str = str + appSecret;
    	logger.info("base64第二次加密前：" + str);
    	str = encoder.encode(str.getBytes(CHARSET_UTF8));
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
    	str = new String(bytes, CHARSET_UTF8);
    	logger.info("base64第一次解密后：" + str);
    	str = str.replace(appsecret, "");
    	logger.info("base64第二次解密前：" + str);
    	bytes = decoder.decodeBuffer(str);
    	str = new String(bytes, CHARSET_UTF8);
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
			byte s[] = m.digest(str.getBytes(CHARSET_UTF8));
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
            algorithmName = MD5;
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes(CHARSET_UTF8));
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
//		String s = "{\"applyNo\":\"SXZY000003\",\"applyVesion\":\"SXZY000003_2\",\"applyDate\":\"2017-06-15 00:00:00\",\"refundDate\":\"2017-06-29 00:00:00\",\"borrower\":\"跨境宝\",\"applyAmout\":1111.0,\"repaymentTime\":\"2017-06-21 00:00:00\",\"status\":\"20\",\"remarks\":111,\"bankBillsUrl\":\"\",\"warehouseId\":\"104301\",\"applyList\":[{\"procurementNo\":\"111\", \"amout\":1.0}],\"method\":\"epass.redemption.apply.cash.push\", \"appKey\":\"51028213972510161264\", \"v\":\"1.0\", \"timesTamp\":\"2017-06-15\", \"sign\":\"5ZTY1EMYKGQWNS1NMWKVIQ==\"}"; 
//		String s = "{\"updateDate\":\"2017-12-15 13:56:55\",\"isInStorage\":0,\"method\":\"epass.financing.apply.status.get\",\"v\":\"1.0\",\"applyNo\":\"FINANCE201712140001\",\"sign\":\"+7NLEHMTVE/ZVU6JVNYZEG==\",\"nodeDate\":\"2017-12-15 13:56:55\",\"urlList\":[],\"appKey\":\"51028213972510161264\",\"timesTamp\":\"2017-12-15 13:56:55\",\"status\":\"20\"}";
//		System.out.println(EpassSignUtils.sign(s, Constants.APP_SECRET));
//		System.out.println(EpassSignUtils.verify(s, Constants.APP_SECRET));
//		Map<String, Object> map=new HashMap<>();
//		map.put("method", "taobao.qimen.itemlack.report");
//		map.put("timestamp", "2015-04-26 00:00:07");
//		map.put("format", "xml");
//		map.put("app_key", "testerp_appkey");
//		map.put("v", "2.0");
//		map.put("sign_method", "md5");
//		map.put("customerId", "test");
//		map.put("sign", "232C57AD3B5FD73B6248991537EAE801");
//		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><response><flag>success|failure</flag><code>200</code><message>success</message><itemId></itemId></response>";
//		System.out.println(EpassSignUtils.verifyForQiMen(map, xml, Constants.APP_SECRET));
//		String mobile = "444422198610000022";
//		try {
//			String str = EpassSignUtils.base64Encode(mobile, "cc1yefociiv694mrlhk7wibie61ohnaq");
//			System.out.println(EpassSignUtils.base64Decode(str, "cc1yefociiv694mrlhk7wibie61ohnaq"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		String s = "{\"app_key\":\"9726229780589088799\",\"country\":\"110\",\"created\":\"2017-12-28 14:35:00.0\",\"declare_plan\":\"HK01\",\"discount\":\"0.00\",\"is_trace_source\":1,\"logistics_code\":\"0000026a\",\"method\":\"epass.orders.b2c.add\",\"modified\":\"2017-12-28 14:35:00.0\",\"order_id\":\"TEST1801260000224\",\"payment\":\"1085.12\",\"receiver_address\":\"广东省 深圳市 南山区. 沙河街道白石洲石洲中路未来时代公寓710室(518052).\",\"receiver_city\":\"深圳市\",\"receiver_country\":\"中国\",\"receiver_district\":\"南山区\",\"receiver_identity_card\":\"TkRRME5ESXlNVGs0TmpFd01EQXdNREl5Y2MxeWVmb2NpaXY2OTRtcmxoazd3aWJpZTYxb2huYXE=\",\"receiver_identity_type\":\"1\",\"receiver_mobile\":\"TVRVd09UWXpPVFV5TXpRPWNjMXllZm9jaWl2Njk0bXJsaGs3d2liaWU2MW9obmFx\",\"receiver_name\":\"徐莎\",\"receiver_state\":\"广东省\",\"sender_address\":\"葵涌\",\"sender_city\":\"香港\",\"sender_country\":\"中国\",\"sender_county\":\"香港\",\"sender_name\":\"张思\",\"sender_phone\":\"TVRNNE1EQXhNemd3TURBPWNjMXllZm9jaWl2Njk0bXJsaGs3d2liaWU2MW9obmFx\",\"sender_province\":\"香港\",\"shop_id\":\"SHOP000001\",\"sign\":\"9910DCE6B6F9984330076C3DB3FAC79D\",\"timestamp\":\"2018-03-22 17:46:08\",\"v\":\"1.0\",\"warehouse_id\":\"HK01\",\"way_bill_no\":\"1313131411\",\"way_frt_fee\":\"0.00\",\"way_ind_fee\":\"0.00\",\"way_tax_fee\":\"0.00\"}";
		JSONObject o = JSONObject.parseObject(s);
		try {
			System.err.println(EpassSignUtils.base64Decode(o.getString("receiver_identity_card"), "cc1yefociiv694mrlhk7wibie61ohnaq"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
