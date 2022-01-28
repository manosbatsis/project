package com.topideal.controller.maindata;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.code.OPCodeEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.service.maindata.SyncCustomerMainService;
import com.topideal.tools.ResponseFactory;

import net.sf.json.JSONObject;


/**
 * （主数据新系统）客商信息下发接口2.0
 * 说明: 主数据接口 :4.13 企业信息推送接口
 * @author 杨创
 */
@Controller
@RequestMapping("/derpapi/1104")
public class SyncCustomerMainController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(SyncCustomerMainController.class);
	@Autowired
	private SyncCustomerMainService syncCustomerMainService;

	/**
	 * 客户信息下发接口2.0
	 * @param json (主数据传的是单个客户的json信息)
	 * @return
	 */
	@RequestMapping(params="method=erp5106",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
	@SystemControllerLog(point="12102001011",model="客商信息下发接口2.0",keyword = "code")
    public Map getMainCustomerInfo(@RequestBody String json){
		LOGGER.info("请求客商信息下发接口2.0json:"+json);
		try {
			//json对象
			JSONObject jsonData = JSONObject.fromObject(json);
			//企业编码（主数据编码）证件号+证件类型+租户进行清洗
			if (jsonData.get("code")==null||StringUtils.isBlank(jsonData.getString("code"))||"null".equals(jsonData.getString("code"))) {
				return ResponseFactory.syncError("code is NULL");
			}
			//企业全称(营业执照名称）
			if (jsonData.get("name")==null||StringUtils.isBlank(jsonData.getString("name"))||"null".equals(jsonData.getString("name"))) {
				return ResponseFactory.syncError("name is NULL");
			}
			//企业性质 枚举: Q,G,S,Z 枚举备注: Q:企业 G:个人 S:社会团体 Z:政府机构
			if (jsonData.get("merchantNature")==null||StringUtils.isBlank(jsonData.getString("merchantNature"))||"null".equals(jsonData.getString("merchantNature"))) {
				return ResponseFactory.syncError("merchantNature is NULL");
			}
			//企客商类型枚举: 01,10,11枚举备注: 01：客户；10：供应商；11供应商&客户
			if (jsonData.get("clientType")==null||StringUtils.isBlank(jsonData.getString("clientType"))||"null".equals(jsonData.getString("clientType"))) {
				return ResponseFactory.syncError("clientType is NULL");
			}
			//证件号码
			if (jsonData.get("idCard")==null||StringUtils.isBlank(jsonData.getString("idCard"))||"null".equals(jsonData.getString("idCard"))) {
				return ResponseFactory.syncError("idCard is NULL");
			}
			//枚举: Y,Z,G,H,J,S枚举备注: Y 营业执照 Z 注册登记证 G 个人身份证 H 护照 J 机构代码 S 社会团体法人登记证书
			if (jsonData.get("idType")==null||StringUtils.isBlank(jsonData.getString("idType"))||"null".equals(jsonData.getString("idType"))) {
				return ResponseFactory.syncError("idType is NULL");
			}
			//	客户状态 枚举备注: 1：有效/0：锁定，默认有效
			if (jsonData.get("status")==null||StringUtils.isBlank(jsonData.getString("status"))||"null".equals(jsonData.getString("status"))) {
				return ResponseFactory.syncError("status is NULL");
			}
			//数据来源
			if (jsonData.get("source")==null||StringUtils.isBlank(jsonData.getString("source"))||"null".equals(jsonData.getString("source"))) {
				return ResponseFactory.syncError("source is NULL");
			}
			//企业编码（主数据编码）证件号+证件类型+租户进行清洗
			if (jsonData.get("tenantCode")==null||StringUtils.isBlank(jsonData.getString("tenantCode"))||"null".equals(jsonData.getString("tenantCode"))) {
				return ResponseFactory.syncError("tenantCode is NULL");
			}
			//客户类型(客户角色)电商企业\仓储企业\物流公司\电商平台\支付企业\监管场所经营人\报关企业\委托单位\账册主体\资金方\客户\供应商;可多选考虑用置位方式：
			if (jsonData.get("type")==null||StringUtils.isBlank(jsonData.getString("type"))||"null".equals(jsonData.getString("type"))) {
				return ResponseFactory.syncError("type is NULL");
			}
			//版本号
			if (jsonData.get("version")==null||StringUtils.isBlank(jsonData.getString("version"))||"null".equals(jsonData.getString("version"))) {
				return ResponseFactory.syncError("version is NULL");
			}

			
			syncCustomerMainService.synsCustomerMain(jsonData);
		}catch(Exception e){
			LOGGER.error("主数据同步客商2.0信息异常,错误信息:{}",e.getMessage());
			return ResponseFactory.syncError("主数据同步客商2.0信息异常,错误信息:{}"+e.getMessage());
		}
		return ResponseFactory.syncSuccess();
	}

}
