package com.topideal.controller.epass;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.enums.MQStorageEnum;
import com.topideal.common.system.annotation.SystemControllerLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.entity.vo.base.ApiSecretConfigModel;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.TransferOrderService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 调拨单回推
 */
@RequestMapping("/api/1213")
@Controller
public class TransferOrderController {
	/**
	 * 打印日志
	 */
	public static final Logger LOGGER=LoggerFactory.getLogger(TransferOrderController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private TransferOrderService transferOrderService;// 调拨出库
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置	

	
	//调拨出库回推
	@RequestMapping(params="method=erp8761",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103201500",model="调拨单回推",keyword = "custtransfer_code")
	public JSONObject getTransfersOutStorage(@RequestBody String json){
		
		//实例化json
		JSONObject jsonData =new JSONObject();	
		LOGGER.info("调拨单回推,请求开始json"+json);
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
			//调拨时间
			if (jsonData.get("odate")==null||StringUtils.isBlank(jsonData.getString("odate"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "odate is NULL");
			}
			//业务场景
			if (jsonData.get("busi_scene")==null||StringUtils.isBlank(jsonData.getString("busi_scene"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "busi_scene is NULL");
			}
			if(!"20".equals(jsonData.getString("busi_scene"))){
				return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "busi_scene不为:20,不接收");
			}
			//调入仓库
			if (jsonData.get("to_store_code")==null||StringUtils.isBlank(jsonData.getString("to_store_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "to_store_code is NULL");
			}
			//调出仓库
			if (jsonData.get("from_store_code")==null||StringUtils.isBlank(jsonData.getString("from_store_code"))) {
				return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "from_store_code is NULL");
			}
			//商品信息
			JSONArray jsonDetailList = (JSONArray) jsonData.get("detail_list");
			if (jsonDetailList==null || jsonDetailList.size()==0) {
				return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "goods_list is NULL");
			}
			
			for (Object objectDetail : jsonDetailList) {
				JSONObject jsonDetail = (JSONObject) objectDetail;
				//调出商品货号
				if (jsonDetail.get("from_good_id")==null || StringUtils.isBlank(jsonDetail.getString("from_good_id"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "from_good_id is NULL");
				}
				
				//调出商品名称
				if (jsonDetail.get("from_good_name")==null || StringUtils.isBlank(jsonDetail.getString("from_good_name"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "from_good_name is NULL");
				}
				//调入商品货号
				if (jsonDetail.get("to_good_id")==null || StringUtils.isBlank(jsonDetail.getString("to_good_id"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "to_good_id is NULL");
				}
				
				//调入商品名称
				if (jsonDetail.get("to_good_name")==null || StringUtils.isBlank(jsonDetail.getString("to_good_name"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "to_good_name is NULL");
				}
				
				
			}
			//商品信息
			JSONArray wmsJsonDetailList = (JSONArray) jsonData.get("wms_detail_list");
			if (wmsJsonDetailList==null || wmsJsonDetailList.size()==0) {
				return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "wms_detail_list is NULL");
			}
			for (Object wmsObjectDetail : wmsJsonDetailList) {
				JSONObject wmsJsonDetail = (JSONObject) wmsObjectDetail;
				//调出商品货号
				if (wmsJsonDetail.get("from_good_id")==null || StringUtils.isBlank(wmsJsonDetail.getString("from_good_id"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "from_good_id is NULL");
				}
				//调出商品名称
				if (wmsJsonDetail.get("from_good_name")==null || StringUtils.isBlank(wmsJsonDetail.getString("from_good_name"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "from_good_name is NULL");
				}
				//调拨出库数量
				if (wmsJsonDetail.get("qtp")==null || StringUtils.isBlank(wmsJsonDetail.getString("qtp"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "qtp is NULL");
				}
				//调出批次
				/*if (wmsJsonDetail.get("lot_no")==null || StringUtils.isBlank(wmsJsonDetail.getString("lot_no"))) {
					return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), "lot_no is NULL");
				}*/
			}
			
			JSONObject jsonMQData = transferOrderService.transferOrderPush(jsonData.toString(),merchantId);
			rocketMQProducer.send(jsonMQData.toString(), MQStorageEnum.EPASS_STORAGE_TRANSFER_ORDER.getTopic(),MQStorageEnum.EPASS_STORAGE_TRANSFER_ORDER.getTags());
			LOGGER.info("调拨单回推接口,推送MQ报文"+jsonMQData.toString());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("调拨单回推", e.getMessage());
			return ResponseFactory.error("custtransfer_code", jsonData.get("custtransfer_code").toString(), e.getMessage());
		}
		return ResponseFactory.success("custtransfer_code", jsonData.get("custtransfer_code").toString());
	}
}
