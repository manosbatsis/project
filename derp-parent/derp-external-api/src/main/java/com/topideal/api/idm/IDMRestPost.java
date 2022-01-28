package com.topideal.api.idm;

import com.topideal.common.tools.ApolloUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IDMRestPost {

	private static final Logger logger = LoggerFactory.getLogger(IDMRestPost.class);

	//接入应用授权代码
	private static final String appuser = ApolloUtil.appUser;

	private static final String appkey = ApolloUtil.appKey;
    //随机字符串（字母和数字）
	private static final String randomStr = "1234567890qweasd";
	//记录API接口操作人，若不传该参数，则默认为鉴权用户(appuser)
	//private static final String operator = "moses";
	
	//private static final String ip = "10.0.8.4";
	
	private static final String url = ApolloUtil.simUrl;

	public static String restPost(String uri, String body, String method) {
		try {
			logger.info("appuser:"+ ApolloUtil.appUser);
			logger.info("appkey:"+ApolloUtil.appKey);
			logger.info("url："+ApolloUtil.simUrl);

			URL restServiceURL = new URL(url + uri);
			HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
			httpConnection.setRequestMethod(method);
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setRequestProperty("Content-Type", "application/json");

			httpConnection.setRequestProperty("appuser", appuser);
			httpConnection.setRequestProperty("randomcode", randomStr);//随机字符串（字母和数字）
			//注释httpConnection.setRequestProperty("operator", operator);
			//注释httpConnection.setRequestProperty("ip", ip);

			String dateNow = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "Z";
			httpConnection.setRequestProperty("timestamp", dateNow);//当前时间戳 (yyyyMMddHHmmss'Z')

			// 鉴权
			String encode = DigestUtils.sha256Hex(StringUtils.join(appuser, randomStr, dateNow, "{", appkey, "}"));
			httpConnection.setRequestProperty("encodekey", encode);

			// 签名
//			System.out.println("uri=" + url);
//			System.out.println("body=" + body);
//			System.out.println("appkey=" + appkey);
			System.out.println("uri=" + uri);
			String sign = DigestUtils.md5Hex(StringUtils.join(uri, "&", body, "&", appkey));
			httpConnection.setRequestProperty("sign", sign);
//			System.out.println("beforsign=" + StringUtils.join(url, "&", body, "&", appkey));
			System.out.println("sign=" + sign);
			if (StringUtils.isNotBlank(body)) {
				OutputStream outputStream = httpConnection.getOutputStream();
				outputStream.write(body.getBytes());
				outputStream.flush();
			}
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
			String output = responseBuffer.readLine();
			httpConnection.disconnect();
			return output;

		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return null;
	}
}
