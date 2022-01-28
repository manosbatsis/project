package com.topideal.controller.epass;

import java.util.HashMap;
import java.util.List;
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
import com.topideal.mongo.entity.ContractNoMongo;
import com.topideal.mongo.entity.LbxNoMongo;
import com.topideal.service.epass.ApiSecretConfigService;
import com.topideal.service.epass.ContractNoMongoService;
import com.topideal.service.epass.LbxNoMongoService;
import com.topideal.service.epass.StorageDeclareService;
import com.topideal.tools.ResponseFactory;
import com.topideal.tools.SignUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 入库申报接口(14)
 * @author 杨创
 *2018/5/10
 */
@Controller
@RequestMapping("/api/1202")
public class StorageDeclareController {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageDeclareController.class);
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private StorageDeclareService storageDeclareService;
	@Autowired
	private ApiSecretConfigService apiSecretConfigService;// api秘钥配置	
	@Autowired
	private LbxNoMongoService lbxNoMongoService;//LbxNoMongo 
	@Autowired
	private ContractNoMongoService contractNoMongoService;// 合同号
	
	@RequestMapping(params="method=erp9724",method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @SystemControllerLog(point="12103100100",model="入库申报接口",keyword = "ent_inbound_id")
	public JSONObject getStorageDeclare(@RequestBody String json){
		LOGGER.info("入库申报接口,请求开始json"+json);
		JSONObject jsonData=new JSONObject();
		try {
			//获取json对象
			jsonData = JSONObject.fromObject(json);
			 
			// 根据appkey 查询 配置  表获取商家信息
			String appKey = jsonData.getString("app_key");// appKey
			ApiSecretConfigModel model = apiSecretConfigService.getApiSecretConfig(appKey);
			Long merchantId = model.getMerchantId();

			//理货单ID
			if (jsonData.get("asn_no")==null||StringUtils.isBlank(jsonData.getString("asn_no"))) {
				return ResponseFactory.error("asn_no","", "asn_no is NULL");			
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
					//生产日期 由 必填变成非必填
					/*if (jsonReceive.get("produced_date")==null||StringUtils.isBlank(jsonReceive.getString("produced_date"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "produced_date is NULL");			
					}*/
					// 到期日期 必填变成非必填
					/*if (jsonReceive.get("expired_date")==null||StringUtils.isBlank(jsonReceive.getString("expired_date"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "expired_date is NULL");			
					}*/
					//批次号，WMS生成
					/*if (jsonReceive.get("batch_id")==null||StringUtils.isBlank(jsonReceive.getString("batch_id"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "batch_id is NULL");			
					}*/
					
				}
				
			}
			
			Map<String, Object> params = new HashMap<>();
			String entInboundId = jsonData.getString("ent_inbound_id");
			LbxNoMongo lbxNoMongInfo=new LbxNoMongo();
			String tag=null;// 判断来源
			String isRookie=null;//是否为菜鸟仓（1-是，0-否
			String contractNoTag=null;// 合同号查询标识 1.采购
			if (entInboundId.startsWith("CGO")) {// 采购
				tag="1";
				isRookie="0";
			}else if (entInboundId.startsWith("XSTO")) {// 销售退货
				tag="2";
				isRookie="0";
			}else if (entInboundId.startsWith("DBO")) {// 调拨
				tag="3";
				isRookie="0";
			}else {
				params.put("lbxNo", entInboundId);			
				lbxNoMongInfo = lbxNoMongoService.getLbxNoMongInfo(params);
				if (lbxNoMongInfo==null) {
					// 查询看是否是已合同号进行匹配
					if (jsonData.get("contract_no")==null||StringUtils.isBlank(jsonData.getString("contract_no"))) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no") ,"订单号没有匹配搭配订单来源并且 合同号(contract_no)为null,非经分的入库申报单");			
					}
					Map<String, Object> contractNoParams = new HashMap<>();
					contractNoParams.put("contractNo", jsonData.getString("contract_no"));	
					List<ContractNoMongo> contractNoMongoList = contractNoMongoService.getContractNoMongoInfoList(contractNoParams);
					if (contractNoMongoList.size()==0) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "非经分销的入库申报数据");			
					}
					/*if (contractNoMongoList.size()>1) {
						return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "(入库申报接口)查询到有多个相同的合同号");			
					}*/	
					tag="1";
					isRookie="0";
					contractNoTag="1";
				}
				if (lbxNoMongInfo!=null) {
					// 来自调拨菜鸟
					if ("CGO".equals(lbxNoMongInfo.getType())) {
						tag="1";
						isRookie="1";// 菜鸟
					}
					// 来自调拨菜鸟
					if ("DBO".equals(lbxNoMongInfo.getType())) {
						tag="3";
						isRookie="1";//菜鸟
					}
				}
				
			}
				
			JSONObject jsonMQData = storageDeclareService.storageDeclareInfo(jsonData.toString(),merchantId,isRookie,contractNoTag);
			// 采购
			if ("1".equals(tag)) {
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_STORAGE_DECLARE_1.getTopic(),MQOrderEnum.EPASS_STORAGE_DECLARE_1.getTags());
				LOGGER.info("入库申报接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("asn_no",jsonData.getString("asn_no"));

			}
			// 销售退货
			if ("2".equals(tag)) {
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_STORAGE_DECLARE_2.getTopic(), MQOrderEnum.EPASS_STORAGE_DECLARE_2.getTags());	
				LOGGER.info("入库申报接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("asn_no",jsonData.getString("asn_no"));

			}
			// 调拨
			if ("3".equals(tag)) {
				rocketMQProducer.send(jsonMQData.toString(), MQOrderEnum.EPASS_STORAGE_DECLARE_3.getTopic(), MQOrderEnum.EPASS_STORAGE_DECLARE_3.getTags());
				LOGGER.info("入库申报接口,推送MQ报文"+jsonMQData.toString());
				return ResponseFactory.success("asn_no",jsonData.getString("asn_no"));

			}
			return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), "非经分销的单");

			
		} catch (Exception e) {
			LOGGER.error("入库申报接口,错误信息:{}",e.getMessage());
			return ResponseFactory.error("asn_no",jsonData.getString("asn_no"), e.getMessage());
			
		}			

	
	}
	
	
	
	
}
