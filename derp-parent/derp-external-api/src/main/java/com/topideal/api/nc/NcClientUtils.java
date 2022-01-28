package com.topideal.api.nc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mnc.common.rsa.RSAUtils;
import com.topideal.api.nc.nc07.Details;
import com.topideal.api.nc.nc07.ReceiveBillJsonRoot;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.http.HttpClientUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 财务NC交互工具类
 * @author gy
 *
 */
public class NcClientUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NcClientUtils.class);
	
	public static final String KEY_ALGORTHM = "RSA";
	//公钥
	//private static final String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC+fZIlHixgL0xrGFuGyBYG9xoforb8hzt3AIPQsOiI3dsfkhKpr1zPPGw4wCWo1bY2q9z0r21v1yNyp3I/mikC+B/AulqxjM58J3tPooAHwcTJUbXyU0d8GCu90hPL5u7yMZIPob9kTmwrAyhdn2Di6+D0ObhLNnchcBhElx03DwIDAQAB";

	//private static final String privateKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL59kiUeLGAvTGsYW4bIFgb3Gh+itvyHO3cAg9Cw6Ijd2x+SEqmvXM88bDjAJajVtjar3PSvbW/XI3Kncj+aKQL4H8C6WrGMznwne0+igAfBxMlRtfJTR3wYK73SE8vm7vIxkg+hv2RObCsDKF2fYOLr4PQ5uEs2dyFwGESXHTcPAgMBAAECgYAKdT5mx0dMaIxbfNTZi3aaH0rR3c8KAiqjl+WkuXGqwfNbwls/8Z64yepvGy12HXWJftn8nPz3HOqXStYc48vIg7LGyeKnccwrkLFE8SGwu4X0yV/j9wUjKUUNgEsaRwWhvqXBvi7GY4KxqbA5mMO5qlywwisYTgszfrZO/D5LcQJBAOAHEzisL9lgyT3Z2aIfYDRE5/5iLXDj6klRSkrOO/k/WwepldnR6+lPBYsh9WU4a/laZkyelbcXNOYpb+HQpcUCQQDZrTSlS/CFfhvenTfJi+x7hUP85vSh/YbSZUhQujVqfZaTGq4m8rHdSAmxYWN1+Qw3FukUhz9OdU1PUZwIdErDAkEAkeCpiIa+5arbf/YCfEo4B/Eyq/fcJR2UKsfPO6TNcsDpkrTACkVie9rj/jRPqeiyjn1qDcxaKw3meVVUMFQADQJAGu1FJOl28FyAQBJ8sy7e6wy6M0+ylVNSWTZ0MqBGOU0d0karol/FRtVBdbLRZeeP7kSkQojUePUjwhsbTSRtlwJBANakLP1b/LF/MgqCfnolLdkXfaYfqaYSj+mf1zLz8SnZbq+Z8UXF983nZ9ttME1XY2tO/4CBaYyQKzmWPMPSGW0=" ;
	
	/**
	 * 发送NC
	 * @param requestUrl
	 * @param jsonStr
	 * @return
	 */
	public static String sendNc(String requestUrl, String jsonStr) {
		
		/**请求 报文*/
		JSONObject requestJson = new JSONObject() ;
		
		/**返回信息*/
		JSONObject returnJson = new JSONObject() ;
		
		/**加密json报文*/
		String rsadata = null ;
		
		try {
			
			LOGGER.debug("--------NC明文报文--------：" + jsonStr);
			
			/**通过nc 工具类rsa 公钥加密报文*/
			rsadata = RSAUtils.encode(jsonStr, ApolloUtil.ncPublicKey) ;
			
			LOGGER.debug("--------NC加密报文--------：" + rsadata);
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("NC报文加密失败:" + e.getMessage(), e);
			
			returnJson.put("code", "1002") ;
			returnJson.put("msg", "报文加密失败") ;
			returnJson.put("rsadata", "") ;
			
			return returnJson.toString() ;
		}
		
		requestJson.put("appkey", ApolloUtil.ncAppKey) ;
		requestJson.put("requestdate", TimeUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")) ;
		requestJson.put("rsadata", rsadata) ;
		
		String responseStr = HttpClientUtil.doPost(requestUrl, requestJson.toString(), "UTF-8");

		/**解密NC 返回报文*/
		JSONObject responseJson = JSONObject.fromObject(responseStr) ;

		if (responseJson.containsKey("rsadata")) {
			String responseRsadata = responseJson.getString("rsadata") ;
			String clearText = RSAUtils.decode(responseRsadata, ApolloUtil.ncPrivateKey) ;

			Object json = new JSONTokener(clearText).nextValue();

			if (json instanceof JSONObject) {
				Map<String, String> jsonMap = new Gson().fromJson(clearText, new TypeToken<Map<String, String>>() {}.getType());
				JSONObject jsonObject = JSONObject.fromObject(jsonMap);
				responseJson.put("rsadata", jsonObject) ;
			} else if (json instanceof JSONArray) {
				List<Map<String, String>> jsonMapList = new Gson().fromJson(clearText, new TypeToken<List<Map<String, String>>>() {}.getType());

				JSONArray jsonArray = JSONArray.fromObject(jsonMapList);
				responseJson.put("rsadata", jsonArray) ;
			} else {
				responseJson.put("rsadata", new JSONArray()) ;
			}

		} else {
			responseJson.put("rsadata", new JSONArray()) ;
		}

		
		LOGGER.debug("--------NC返回解密信息--------：" + responseJson.toString());

		return responseJson.toString() ;
	}
	
	/**
	 * NC 报文加密
	 * @param clearText 明文字符串
	 * @return
	 */
	public static String ncEncode(String clearText) {
		try {
			return RSAUtils.encode(clearText, ApolloUtil.ncPublicKey) ;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			
			return null ;
		}
	}
	
	/**
	 * NC 报文解密
	 * @param cripText 密文字符串
	 * @return
	 */
	public static String ncDecode(String cripText) {
		try {
			return RSAUtils.decode(cripText, ApolloUtil.ncPrivateKey) ;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			
			return null ;
		}
	}

	public static void main(String[] args) {
		try {			
			
			ReceiveBillJsonRoot root = new ReceiveBillJsonRoot() ;
			root.setConfirmBillId("201801110001");
			root.setSourceCode("001");
			root.setType("1");
			root.setCorCcode("100001");
			root.setCusCcode("100002");
			root.setYearMonth("201809");
			root.setCreated("2018-01-01 00:00:00");
			root.setTotalAmount(new BigDecimal(100.00)) ;
			root.setTaxAmount(new BigDecimal(10.00)) ;
			root.setCurrency("CNY");
			root.setChecked("1");
			root.setInvoiced("1");
			root.setRemark("");
			
			Details details = new Details() ;
			details.setSindex("1");
			details.setInExpCode("test");
			details.setCurrency("CNY");
			details.setAmount(new BigDecimal(100.00));
			details.setRate("11%");
			details.setTax(new BigDecimal(11.00));
			List<Details> detailsList = new ArrayList<Details>() ;
			detailsList.add(details) ;
			
			root.setDetails(detailsList);
			
			JSONObject json = JSONObject.fromObject(root);
			
			String clearText = json.toString() ;
			System.out.println(clearText);
			
			String ste = sendNc("http://10.10.102.188:7276/external/settlement/confirmbill", clearText);
			System.out.println(ste);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
