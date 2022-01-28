package com.topideal.controller.dev;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.DPSign;
import com.topideal.dao.base.ApiExternalConfigDao;
import com.topideal.entity.vo.base.ApiExternalConfigModel;
import com.topideal.mongo.dao.ApiExternalConfigMongoDao;
import com.topideal.mongo.entity.ApiExternalConfigMongo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.tools.ApolloUtil;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.tools.QimenSignUtils;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONObject;

/**
 * 获取签名
 * 
 * @author 杨创 2018/8/1
 */
@Controller
@RequestMapping("/derpapi")
public class SignController {
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置

	@Autowired
	private ApiExternalConfigDao apiExternalConfigDao;

	/**
	 * 跨境宝签名
	 * */
	@RequestMapping(value="/1000" , params = "method=erp1000", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public JSONObject getEpassSign(@RequestBody String json) {
		JSONObject jsonData = new JSONObject();
		JSONObject signJsonData = new JSONObject();
		try {
			jsonData = JSONObject.fromObject(json);
			// 签名校验
			if (jsonData.get("app_key") == null || StringUtils.isBlank(jsonData.getString("app_key"))) {
				return ResponseFactory.error("order_id", (String) jsonData.get("order_id"), "app_key is NULL");
			}
			// 根据appkey 查询 配置 表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			if (model == null) {
				return ResponseFactory.error("order_id", (String) jsonData.get("order_id"), "根据app_key 没有对应的api配置信息");
			}
			String appSecret = model.getAppSecret();
			String sign = SignUtil.getVerify(json, appSecret);
			signJsonData.put("sign", sign);
		} catch (Exception e) {
			signJsonData.put("sign", "获取签名失败,请检查报文");
		}

		return signJsonData;
	}

	/**
	 * 众邦云仓签名
	 * */
	@RequestMapping(value="getYwmsSign.asyn", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public JSONObject getYwmsSign(@RequestBody String requestData, HttpServletRequest request) {
		JSONObject signJsonData = new JSONObject();
		try {

			Map<String, String[]> parameterMap = request.getParameterMap();
			Map<String, String> map = new HashMap<String, String>();
			
			for (String key : parameterMap.keySet()) {
				
				if("xml".equals(key) || "sign".equals(key)) {
					continue ;
				}
				String[] values = parameterMap.get(key);
				map.put(key, values[0]) ;
			}

			String data = URLDecoder.decode(requestData, "UTF-8");
			
			String xml = null;
	        
	        if (data.indexOf("<") >= 0) {
	            xml = data.substring(data.indexOf("<"), data.lastIndexOf(">") + 1);
	        }
			
			String sign = QimenSignUtils.sign(map, xml, ApolloUtil.ywmsSecret);

			signJsonData.put("sign", sign);

		} catch (Exception e) {
			signJsonData.put("sign", "获取签名失败,请检查报文");
		}

		return signJsonData;
	}

	/**
	 * 经销系统签名
	 * */
	@RequestMapping(value="/getDerpSign.asyn" ,  method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public JSONObject getDerpSign(@RequestBody String json) {
		JSONObject jsonData = new JSONObject();
		JSONObject responseJson = new JSONObject();
		try {
			jsonData = JSONObject.fromObject(json);
			// 签名校验
			if (jsonData.get("appKey") == null || StringUtils.isBlank(jsonData.getString("appKey"))) {
				responseJson.put("code","0001");
				responseJson.put("message","appKey不能为空");
				return responseJson;
			}

			//查询商家秘钥
			String appKey = jsonData.getString("appKey");// appKey
			ApiExternalConfigModel configModel = new ApiExternalConfigModel();
			configModel.setAppKey(appKey);
			configModel.setStatus(DERP_SYS.APIEXTERNALCONFIG_STATUS_1);
			configModel = apiExternalConfigDao.searchByModel(configModel);
			if(configModel==null){
				responseJson.put("code","0001");
				responseJson.put("message","秘钥不存在");
				return responseJson;
			}

			String sign = DPSign.sortMd5Encrypt(jsonData, configModel.getAppSecret(), "");

			responseJson.put("sign",sign);
			responseJson.put("code","0000");
			responseJson.put("message","操作成功");

		} catch (Exception e) {
			responseJson.put("code","9999");
			responseJson.put("message","未知异常"+e.getMessage());
		}
		return responseJson;
	}

}
