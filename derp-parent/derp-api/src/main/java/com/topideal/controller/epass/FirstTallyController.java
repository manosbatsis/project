package com.topideal.controller.epass;

import com.topideal.mongo.entity.LbxNoMongo;
import com.topideal.service.epass.LbxNoMongoService;
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
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.FirstTallyService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 初理货接口(18)
 * @author 杨创
 *
 */
@Controller
@RequestMapping("/api/1201")
public class FirstTallyController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(FirstTallyController.class);
	@Autowired
	private FirstTallyService firstTallyService;// 初理货	
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private LbxNoMongoService lbxNoMongoService;// LbxNoMongo
	
	/**
	 * 初理货接口
	 * @param json
	 * @return
	 */
	@RequestMapping(params="method=erp7165",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
	@SystemControllerLog(point="12103100000",model="初理货接口",keyword = "ent_inbound_id")
	public JSONObject getFirstTally(@RequestBody String json){
		JSONObject jsonData=new JSONObject();
		LOGGER.info("初理货接口,请求开始json"+json);
		try {
			//获取json对象
			jsonData = JSONObject.fromObject(json);
			
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();
			
			//理货单ID
			if (jsonData.get("asn_no")==null||StringUtils.isBlank(jsonData.getString("asn_no"))) {
				return ResponseFactory.error("asn_no","" ,"asn_no is NULL");			
			}
			//理货日期
			if (jsonData.get("opera_date")==null||StringUtils.isBlank(jsonData.getString("opera_date"))) {
				return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "opera_date is NULL");			
			}
			//企业入仓编号（采购编号，唯一）
			if (jsonData.get("ent_inbound_id")==null||StringUtils.isBlank(jsonData.getString("ent_inbound_id"))) {
				return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "ent_inbound_id is NULL");			
			}
			//仓库编码
			/*if (jsonData.get("warehouse_id")==null||StringUtils.isBlank(jsonData.getString("warehouse_id"))) {
				return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "warehouse_id is NULL");			
			}*/
			
								
			JSONArray jsonGoodsList= (JSONArray) jsonData.get("goods_list");
			// 商品
			if (null ==jsonGoodsList ||jsonGoodsList.size()==0) {
				return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "goods_list is NULL");			

			}
			for (Object object : jsonGoodsList) {
				JSONObject jsonGoods=(JSONObject) object;
				//商品编码
				if (jsonGoods.get("goods_id")==null||StringUtils.isBlank(jsonGoods.getString("goods_id"))) {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "goods_id is NULL");			
				}
				//商品条形码
				/*if (jsonGoods.get("barcode")==null||StringUtils.isBlank(jsonGoods.getString("barcode"))) {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "barcode is NULL");			
				}*/
				
				//申报数量(应收数量)取采购单数			
				if (jsonGoods.get("expected_amount")==null||StringUtils.isBlank(jsonGoods.getString("expected_amount"))) {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "expected_amount is NULL");			
				}
				//理货数量（规则：理货数量=申报数量-缺失数量+多货数量）实收数量
				if (jsonGoods.get("amount")==null||StringUtils.isBlank(jsonGoods.getString("amount"))) {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "amount is NULL");			
				}
				//缺失数量（默认0）
				if (jsonGoods.get("lack_amount")==null||StringUtils.isBlank(jsonGoods.getString("lack_amount"))) {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "lack_amount is NULL");			
				}		
				//多货数量
				if (jsonGoods.get("many_amount")==null||StringUtils.isBlank(jsonGoods.getString("many_amount"))) {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "many_amount is NULL");			
				}
				//总数量（正常数量）
				if (jsonGoods.get("totoal_sales_amount")==null||StringUtils.isBlank(jsonGoods.getString("totoal_sales_amount"))) {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "totoal_sales_amount is NULL");			
				}
				JSONArray receiveListArray=(JSONArray) jsonGoods.get("receive_list");
				for (Object object2 : receiveListArray) {
					JSONObject jsonReceive=(JSONObject) object2;
					//坏货数量
					if (jsonReceive.get("worn_amount")==null||StringUtils.isBlank(jsonReceive.getString("worn_amount"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "worn_amount is NULL");			
					}
					//expire_amount 过期数量
					if (jsonReceive.get("expire_amount")==null||StringUtils.isBlank(jsonReceive.getString("expire_amount"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "expire_amount is NULL");			
					}
					//正常数量（正常数量）
					if (jsonReceive.get("sales_amount")==null||StringUtils.isBlank(jsonReceive.getString("sales_amount"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "sales_amount is NULL");			
					}
					//生产日期
					/*if (jsonReceive.get("produced_date")==null||StringUtils.isBlank(jsonReceive.getString("produced_date"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "produced_date is NULL");			
					}*/
					// 到期日期
					/*if (jsonReceive.get("expired_date")==null||StringUtils.isBlank(jsonReceive.getString("expired_date"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "expired_date is NULL");			
					}*/
					//批次号，WMS生成
					/*if (jsonReceive.get("batch_id")==null||StringUtils.isBlank(jsonReceive.getString("batch_id"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "batch_id is NULL");			
					}*/
					
				}
				
			}
					
			String tag=null;
			String entInboundId = jsonData.getString("ent_inbound_id");
			if (entInboundId.startsWith("CGO")) {//来自采购				
				tag="1";
			}else if (entInboundId.startsWith("DBO")) {// 来自调拨
				tag="2";
			}else if (entInboundId.startsWith("XSTO")) {// 销售退货
				tag="3";
			}else {
				Map<String, Object> params = new HashMap<>();
				params.put("lbxNo", entInboundId);
				LbxNoMongo lbxNoMongInfo = lbxNoMongoService.getLbxNoMongInfo(params);
				if (lbxNoMongInfo != null && "DBO".equals(lbxNoMongInfo.getType())) {
					tag = "2";// 来自调拨菜鸟
				} else {
					return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "非经分销的初理货数据");
				}

			}
			// 调service
			JSONObject jsonMQData = firstTallyService.tallyBaseInfo(jsonData.toString(),merchantId);
			
			if ("1".equals(tag)) {//来自采购			
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_FIRST_TALLY_1.getTopic(),MQOrderEnum.EPASS_FIRST_TALLY_1.getTags());
			}
			if ("2".equals(tag)) {// 来自调拨
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_FIRST_TALLY_3.getTopic(),MQOrderEnum.EPASS_FIRST_TALLY_3.getTags());
			}
			if ("3".equals(tag)) {// 来自销售退货
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_FIRST_TALLY_2.getTopic(),MQOrderEnum.EPASS_FIRST_TALLY_2.getTags());
			}
			
			LOGGER.info("初理货接口,推送MQ报文"+jsonMQData.toString());	
		} catch (Exception e) {
			LOGGER.error("初理货接口异常,错误信息:{}",e.getMessage());
			return ResponseFactory.error("asn_no",(String)jsonData.get("asn_no"), e.getMessage());
			
		}			
		return ResponseFactory.success("asn_no",jsonData.getString("asn_no"));
	}
	


}
