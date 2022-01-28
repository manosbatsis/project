package com.topideal.common.tools;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 签名校验类
 * @author 
 *
 */
public class DPSign {
	
	/*打印日志*/
	private static final Logger logger = LoggerFactory.getLogger(DPSign.class);
	
	/** 参数排序md5 16进制加密
	 * map 报文
	 * encryptKey 签名秘钥
	 * reKey 不参与签名字段名称
	 *  */
	public static String sortMd5Encrypt(Map<String, Object> map,String encryptKey,String ... reKey){
		String sign = "";
		String encryptData = "";
		try {
			Set<String> keySet = map.keySet();
			List<String> keyList = new ArrayList<String>(keySet);
			logger.info("排序前："+keyList.toString());
			Collections.sort(keyList);//排序或者自己写冒泡排序
			logger.info("排序后："+keyList.toString());
			int i = 0;
			out:
			for(String key : keyList){
				Object value = map.get(key);
				if(value != null && !"".equals(value)){
					for(String re : reKey){
						if(re.equals(key)){
							continue out;
						}
					}
					i++;
					encryptData += (i!=1?"&":"")+key + "=" + map.get(key);
				}
			}
			encryptData += "&"+"key" +"="+encryptKey;
			logger.info("签名加密数据:"+encryptData);
			sign = md5UTF8Hex(encryptData);
			logger.info("签名:"+sign);
		} catch (Exception e) {
			logger.error("参数排序md5加密异常.", e);
		}
		return sign;
	}
	
	/**验签
	 * sign 签名
	 * valueMap 报文
	 * encryptKey 秘钥
	 * reKey 不参与签名字段名称
	 * */
	public static boolean signVerify(String sign,Map<String, Object> valueMap,String encryptKey,String ... reKey){
		String signresult = sortMd5Encrypt(valueMap, encryptKey, reKey);
		if(StringUtils.isBlank(sign)||StringUtils.isBlank(signresult)||!sign.equals(signresult)){
			logger.info("验签失败");
			return false;//验签失败
		}
		logger.info("验签通过");
		return true;
	}

	/** 通常用的MD5加密：MD5加密字符串的UTF-8字节 */
	private static String md5UTF8Hex(String str) throws Exception{
		byte[] bs = str.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");	
		return byteToHex(md.digest(bs));
	}
	/** 将byte[]转为16进制的字符串数组 */
	private static String byteToHex(byte[] bs){
		StringBuilder sb = new StringBuilder();			
		for (int i = 0; i < bs.length; i++) {
			int c = bs[i];
			if(c<0){
				c += 256;
			}
			// 转16进制字符串
			String hex = Integer.toHexString(c);
			if(hex.length()==1){
				sb.append("0");
			}
			sb.append(hex);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("appKey", "5203572001356236606");
			map.put("orderType", "005");
			map.put("startTime", "2020-08-01 00:00:00");
			map.put("endTime", "2020-11-10 00:00:00");
			map.put("pageNo", 1);
			map.put("pageSize", 100);
			
			String encryptKey="ugk5b8k64ave2c6kq246hsp2kd4vz0t7";
			String sign = sortMd5Encrypt(map, encryptKey, "");
			
			System.out.println("签名结果"+sign);
			
			/**String str="appKey=8814133443895333395&endTime=2019-04-15 23:59:59&orderType=001&pageNo=1&pageSize=100&startTime=2019-04-15 00:00:00&key=HmKfEl1ULrqfzKJjAIGDi4AxfNwJAF5e";
			String sign = md5UTF8Hex(str);
			System.out.println(sign);*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	
	
}
