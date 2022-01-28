package com.topideal.controller.epass;

import com.topideal.common.constant.DERP;
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
import com.topideal.service.epass.BigCargoTallyService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 大货理货接口
 * @author 杨创
 *2019/04/01
 */
@RequestMapping("/api/1215")
@Controller
public class BigCargoTallyController {
	/**
	 * 
	 * 打印日志
	 */
	public static final Logger LOGGER=LoggerFactory.getLogger(BigCargoTallyController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private BigCargoTallyService bigCargoTallyService;// 大货理货接口
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置	

	
	//大货理货接口
	@RequestMapping(params="method=erp4391",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	@SystemControllerLog(point="12103202500",model="大货理货接口",keyword = "order_id")
	public JSONObject getTransfersOutStorage(@RequestBody String json){
		
		//实例化json
		JSONObject jsonData =new JSONObject();	
		LOGGER.info("大货理货接口,请求开始json"+json);
		try {
			jsonData = JSONObject.fromObject(json);
			
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
						
			//调拨单号
			if (jsonData.get("order_id")==null||StringUtils.isBlank(jsonData.getString("order_id"))) {
				return ResponseFactory.error("order_id", "", "order_id is NULL");
			}
			//仓库编码
			if (jsonData.get("warehouse_id")==null||StringUtils.isBlank(jsonData.getString("warehouse_id"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "warehouse_id is NULL");
			}
			//电商企业名称
			if (jsonData.get("electriccode")==null||StringUtils.isBlank(jsonData.getString("electriccode"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "electriccode is NULL");
			}
			// 加工完成时间
			if (jsonData.get("bom_finish_time")==null||StringUtils.isBlank(jsonData.getString("bom_finish_time"))) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "bom_finish_time is NULL");
			}
			
			//商品信息
			JSONArray detailList = (JSONArray) jsonData.get("detail_list");
			if (detailList==null || detailList.size()==0) {
				return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "detail_list is NULL");
			}
			
			for (Object detail : detailList) {
				JSONObject jsonDetail = (JSONObject) detail;
				
				//原商品信息
				JSONArray originJsonDetailList = (JSONArray) jsonDetail.get("origin_detail_list");
				if (originJsonDetailList==null || originJsonDetailList.size()==0) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "origin_detail_list is NULL");
				}
				//目标商品信息
				JSONArray targetJsonDetailList = (JSONArray) jsonDetail.get("target_detail_list");
				if (targetJsonDetailList==null || targetJsonDetailList.size()==0) {
					return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "target_detail_list is NULL");
				}
				
				for (Object originObjectDetail : originJsonDetailList) {
					JSONObject originJsonDetail = (JSONObject) originObjectDetail;
					//商品货号
					if (originJsonDetail.get("gcode")==null || StringUtils.isBlank(originJsonDetail.getString("gcode"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "gcode is NULL");
					}
					//分配数量
					if (originJsonDetail.get("alloc_qty")==null || StringUtils.isBlank(originJsonDetail.getString("alloc_qty"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "alloc_qty is NULL");
					}
					//批次
					/*if (originJsonDetail.get("lot_no")==null || StringUtils.isBlank(originJsonDetail.getString("lot_no"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "lot_no is NULL");
					}*/
					//单位
					if (originJsonDetail.get("uom")==null || StringUtils.isBlank(originJsonDetail.getString("uom"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "uom is NULL");
					}
					//字符串00：托盘01：箱02：件
					if (!(DERP.ORDER_TALLYINGUNIT_00.equals(originJsonDetail.getString("uom"))||DERP.ORDER_TALLYINGUNIT_01.equals(originJsonDetail.getString("uom"))||DERP.ORDER_TALLYINGUNIT_02.equals(originJsonDetail.getString("uom")))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "uom 值必须是 00：托盘01：箱02：件,其中一个");
					}
					//好坏品
					if (originJsonDetail.get("is_damaged")==null || StringUtils.isBlank(originJsonDetail.getString("is_damaged"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "is_damaged is NULL");
					}
					//库存业务类型字符串：10.B2B ,20.B2C ,30.C2C		
					if (originJsonDetail.get("stock_business_type")==null || StringUtils.isBlank(originJsonDetail.getString("stock_business_type"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "stock_business_type is NULL");
					}
					
					
				}
				
				for (Object targetObjectDetail : targetJsonDetailList) {
					JSONObject targetJsonDetail = (JSONObject) targetObjectDetail;
					//商品货号
					if (targetJsonDetail.get("gcode")==null || StringUtils.isBlank(targetJsonDetail.getString("gcode"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "gcode is NULL");
					}
					//目标库存数量
					if (targetJsonDetail.get("bom_qty")==null || StringUtils.isBlank(targetJsonDetail.getString("bom_qty"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "bom_qty is NULL");
					}
					//批次
					/*if (targetJsonDetail.get("lot_no")==null || StringUtils.isBlank(targetJsonDetail.getString("lot_no"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "lot_no is NULL");
					}*/
					//单位
					if (targetJsonDetail.get("uom")==null || StringUtils.isBlank(targetJsonDetail.getString("uom"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "uom is NULL");
					}
					//字符串00：托盘01：箱02：件
					if (!(DERP.ORDER_TALLYINGUNIT_00.equals(targetJsonDetail.getString("uom"))||DERP.ORDER_TALLYINGUNIT_01.equals(targetJsonDetail.getString("uom"))||DERP.ORDER_TALLYINGUNIT_02.equals(targetJsonDetail.getString("uom")))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "uom 值必须是 00：托盘01：箱02：件,其中一个");
					}
					//好坏品
					if (targetJsonDetail.get("is_damaged")==null || StringUtils.isBlank(targetJsonDetail.getString("is_damaged"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "is_damaged is NULL");
					}
					//库存业务类型字符串：10.B2B ,20.B2C ,30.C2C		
					if (targetJsonDetail.get("stock_business_type")==null || StringUtils.isBlank(targetJsonDetail.getString("stock_business_type"))) {
						return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), "stock_business_type is NULL");
					}

				}
			}

			JSONObject jsonMQData = bigCargoTallyService.bigCargoTally(jsonData.toString(), merchantId);
			rocketMQProducer.send(jsonMQData.toString(), MQStorageEnum.EPASS_BIG_CARGO_TALLY_PUSH.getTopic(),MQStorageEnum.EPASS_BIG_CARGO_TALLY_PUSH.getTags());
			LOGGER.info("大货理货接口,推送MQ报文"+jsonMQData.toString());
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("大货理货接口", e.getMessage());
			return ResponseFactory.error("order_id", jsonData.get("order_id").toString(), e.getMessage());
		}
		return ResponseFactory.success("order_id", jsonData.get("order_id").toString());
	}
}
