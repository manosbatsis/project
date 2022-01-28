package com.topideal.controller.epass;

import java.util.HashMap;
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

import com.topideal.common.enums.MQOrderEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.mongo.entity.LbxNoMongo;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.LbxNoMongoService;
import com.topideal.service.epass.TransfersOutStorageService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 调拨出库
 * @author 杨创
 *2018/06/08
 */
@RequestMapping("/api/1209")
@Controller
public class TransfersOutStorageController {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER=LoggerFactory.getLogger(TransfersOutStorageController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private TransfersOutStorageService transfersOutStorageService;// 调拨出库
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置		
	@Autowired
	private LbxNoMongoService lbxNoMongoService;//LbxNoMongo 

	
	//调拨出库回推
	@RequestMapping(params="method=erp2283",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103100900",model="调拨出库回推",keyword = "order_id")
	public JSONObject getTransfersOutStorage(@RequestBody String json){
		
		//实例化json
		JSONObject jsonData =new JSONObject();	
		LOGGER.info("调拨出库回推,请求开始json"+json);
		try {
			jsonData = JSONObject.fromObject(json);

			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//调拨单号
			if (jsonData.get("custtransfer_code")==null||StringUtils.isBlank(jsonData.getString("custtransfer_code"))) {
				return ResponseFactory.error("custtransfer_code", "", "custtransfer_code is NULL");
			}
			//订单号非空校验
			if (jsonData.get("order_id")==null||StringUtils.isBlank(jsonData.getString("order_id"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "order_id is NULL");
			}
			//调拨时间
			if (jsonData.get("odate")==null||StringUtils.isBlank(jsonData.getString("odate"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "odate is NULL");
			}
			
			//申报地海关  BBC关区码表：埔开发区：5208 南沙旅检：5165
			/*if (jsonData.get("customs_code")==null||StringUtils.isBlank(jsonData.getString("customs_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "customs_code is NULL");
			}*/
			
			//申报地国检   BBC国检码表：黄埔局本部：000072,南沙局本部：000069
			/*if (jsonData.get("ciqb_code")==null||StringUtils.isBlank(jsonData.getString("ciqb_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "ciqb_code is NULL");
			}*/
			//10：账册内调仓20：账册内货号变更  30：账册内货号变更调仓 40：账册内货权转移  50：账册内货权转移调仓  60：区内跨海关账册调入  70：区内跨海关账册调出  80：非实物调拨
			if (jsonData.get("busi_scene")==null||StringUtils.isBlank(jsonData.getString("busi_scene"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "busi_scene is NULL");
			}
			//E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务
			if (jsonData.get("serve_types")==null||StringUtils.isBlank(jsonData.getString("serve_types"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "serve_types is NULL");
			}
			//业务类型  10：账册变更调拨 20：企业变更调拨  30：货号变更调拨  40：备案号变更调拨
			/*if (jsonData.get("busi_type")==null||StringUtils.isBlank(jsonData.getString("busi_type"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "busi_type is NULL");
			}*/
			
			//调出电商企业编码
			/*if (jsonData.get("from_electric_code")==null||StringUtils.isBlank(jsonData.getString("from_electric_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "from_electric_code is NULL");
			}*/
			//调出资金方
			/*if (jsonData.get("from_foundp_code")==null||StringUtils.isBlank(jsonData.getString("from_foundp_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "from_foundp_code is NULL");
			}*/
			//调入电商企业名称
			/*if (jsonData.get("to_electric_name")==null||StringUtils.isBlank(jsonData.getString("to_electric_name"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "to_electric_name is NULL");
			}*/
			//  调入资金方
			/*if (jsonData.get("to_foundp_code")==null||StringUtils.isBlank(jsonData.getString("to_foundp_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "to_foundp_code is NULL");
			}*/
			//委托单位 默认值：调出电商企业
			/*if (jsonData.get("trust_code")==null||StringUtils.isBlank(jsonData.getString("trust_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "trust_code is NULL");
			}*/
			//调出仓库
			if (jsonData.get("from_store_code")==null||StringUtils.isBlank(jsonData.getString("from_store_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "from_store_code is NULL");
			}
			//调入仓库
			if (jsonData.get("to_store_code")==null||StringUtils.isBlank(jsonData.getString("to_store_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "to_store_code is NULL");
			}
			JSONArray jsonDetailList = (JSONArray) jsonData.get("detail_list");
			if (jsonDetailList==null || jsonDetailList.size()==0) {
				return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "detail_list is NULL");
			}
			
			for (Object objectDetail : jsonDetailList) {
				JSONObject jsonDetail = (JSONObject) objectDetail;
				//调出商品货号
				if (jsonDetail.get("from_good_id")==null || StringUtils.isBlank(jsonDetail.getString("from_good_id"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "from_good_id is NULL");
				}
				
				//调出商品名称
				if (jsonDetail.get("from_good_name")==null || StringUtils.isBlank(jsonDetail.getString("from_good_name"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "from_good_name is NULL");
				}
				
				//调拨数量
				if (jsonDetail.get("amount")==null || StringUtils.isBlank(jsonDetail.getString("amount"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "amount is NULL");
				}
				
				//调出批次
				/*if (jsonDetail.get("batch_id")==null || StringUtils.isBlank(jsonDetail.getString("batch_id"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "batch_id is NULL");
				}*/				
				
			}
			
			
			
			Map<String, Object> params = new HashMap<>();
			String orderCode = jsonData.getString("order_id");
			LbxNoMongo lbxNoMongInfo=new LbxNoMongo();
			String tag=null;// 判断来源
			String isRookie=null;//是否为菜鸟仓（1-是，0-否
			if (orderCode.startsWith("DBO")) {// 来自调拨
				isRookie="0";
				JSONObject jsonMQData = transfersOutStorageService.transfersOutStorageInfo(jsonData.toString(),merchantId,isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_3.getTopic(),MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_3.getTags());
				LOGGER.info("调拨出库接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("custtransfer_code", jsonData.getString("custtransfer_code"));
			}
			
			
			/*if (orderCode.startsWith("XSO")) {// 来自销售
				isRookie="0";
				JSONObject jsonMQData = transfersOutStorageService.transfersOutStorageInfo(jsonData.toString(),merchantId,isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_1.getTopic(),MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_1.getTags());
				LOGGER.info("调拨出库接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("custtransfer_code", jsonData.getString("custtransfer_code"));
			}*/
			
			if (orderCode.startsWith("XSTO")) {// 来自销售退货
				isRookie="0";
				JSONObject jsonMQData = transfersOutStorageService.transfersOutStorageInfo(jsonData.toString(),merchantId,isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_2.getTopic(),MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_2.getTags());
				LOGGER.info("调拨出库接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("custtransfer_code", jsonData.getString("custtransfer_code"));
			}
			
			// 查询mongdb查询 
			params.put("lbxNo", orderCode);
			lbxNoMongInfo = lbxNoMongoService.getLbxNoMongInfo(params);
			
			// 来自调拨菜鸟
			if (lbxNoMongInfo != null && "DBO".equals(lbxNoMongInfo.getType())) {
				isRookie = "1";// 菜鸟
				JSONObject jsonMQData = transfersOutStorageService.transfersOutStorageInfo(jsonData.toString(),merchantId,isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_3.getTopic(),MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_3.getTags());
				LOGGER.info("调拨出库接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("custtransfer_code", jsonData.getString("custtransfer_code"));
			}
			
			// 来自销售菜鸟
			/*if (lbxNoMongInfo != null && "XSO".equals(lbxNoMongInfo.getType())) {
				isRookie = "1";// 菜鸟
				JSONObject jsonMQData = transfersOutStorageService.transfersOutStorageInfo(jsonData.toString(),merchantId,isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_1.getTopic(),MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_1.getTags());
				LOGGER.info("调拨出库接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("custtransfer_code", jsonData.getString("custtransfer_code"));
			}*/
			
			if (lbxNoMongInfo != null && "XSTO".equals(lbxNoMongInfo.getType())) {
				isRookie = "1";// 菜鸟
				JSONObject jsonMQData = transfersOutStorageService.transfersOutStorageInfo(jsonData.toString(),merchantId,isRookie);
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_2.getTopic(), MQOrderEnum.EPASS_TRANSFER_OUT_STIRAGE_2_2.getTags());
				LOGGER.info("调拨出库接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("custtransfer_code", jsonData.getString("custtransfer_code"));
			}
			
			
			return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), "非经分销的单");

			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("调拨出库回推", e.getMessage());
			return ResponseFactory.error("custtransfer_code", jsonData.getString("custtransfer_code"), e.getMessage());
		}
		
		
	}
}