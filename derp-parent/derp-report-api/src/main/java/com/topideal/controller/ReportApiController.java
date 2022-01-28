package com.topideal.controller;

import com.topideal.entity.dto.api10001.Goods;
import com.topideal.entity.dto.api10001.OutDepotOrder;
import com.topideal.entity.dto.api10002.InDepotOrder;
import com.topideal.entity.dto.api10002.InGoods;
import com.topideal.entity.vo.reporting.ApiGoodsConfigModel;
import com.topideal.entity.vo.system.MerchandiseInfoModel;
import com.topideal.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.tools.DPParamVerify;
import com.topideal.dao.system.ApiExternalConfigDao;
import com.topideal.entity.dto.ResponseRoot;
import com.topideal.entity.vo.system.ApiExternalConfigModel;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/report/api")
public class ReportApiController {
	/**
	 * 打印日志
	 */
	public static final Logger logger = LoggerFactory.getLogger(ReportApiController.class);
	
	@Autowired
	private ApiExternalConfigDao apiExternalConfigDao;
	
	@Autowired
	private OutDepotOrderService outDepotOrderService;
	@Autowired
	private InDepotOrderService inDepotOrderService;
	@Autowired
	private OrderDocumentsService orderDocumentsService ;
	@Autowired
	private InventoryBatchService inventoryBatchService;
	@Autowired
	private ApiGoodsConfigService apiGoodsConfigService;
	
	/**出库单查询接口
	 * apicode：10001
	 */
	@RequestMapping(params={"apicode=10001","v=1.0"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseRoot outDepotOrder(@RequestBody String json){
		logger.debug("出库单查询开始======"+json);
		ResponseRoot responseRoot = new ResponseRoot();
		JSONObject jsonData=new JSONObject();
		try{
			if (true) {
				responseRoot.setmCode("9999");
				responseRoot.setMessage("接口已经禁用");
				return responseRoot;
			}
			jsonData = JSONObject.fromObject(json);
			//必填参数校验
			String[] paramNames = {"appKey","orderType","startTime","endTime","sign"};
			String nullKey = DPParamVerify.verifyNullForMap(jsonData, paramNames);
			if(StringUtils.isNotBlank(nullKey)){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("必填参数为空:"+nullKey);
				return responseRoot;
			}
			
			String appKey = (String)jsonData.get("appKey");//商家appkey
			
			//查询商家秘钥
			ApiExternalConfigModel configModel = new ApiExternalConfigModel();
			configModel.setAppKey(appKey);
			configModel.setStatus(DERP_SYS.APIEXTERNALCONFIG_STATUS_1);//状态状态(1使用中,0已禁用)
			configModel = apiExternalConfigDao.searchByModel(configModel);
			if(configModel==null){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("商家appkey不存在");
				return responseRoot;
			}
			responseRoot = outDepotOrderService.queryOutDepotOrder(jsonData, configModel.getMerchantId());

			//查询对外商品配置表该公司下启用的商品
			List<String> goodsNoList = apiGoodsConfigService.getConfigMerchandiseList(configModel.getMerchantId());

			List<OutDepotOrder> outOrderList = new ArrayList<OutDepotOrder>();

			for (Object object: responseRoot.getDataList()) {
				OutDepotOrder outDepotOrder = (OutDepotOrder) object;

				List<Goods> newGoodList = new ArrayList<>();
				List<Goods> goodList = outDepotOrder.getGoodList();

				for (Goods goods : goodList) {
					if (goodsNoList.contains(goods.getGoodsNo())) {
						newGoodList.add(goods);
					}
				}

				if (newGoodList.size() > 0) {
					outDepotOrder.setGoodList(newGoodList);
					outOrderList.add(outDepotOrder);
				}
			}

			responseRoot.setDataList(outOrderList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseRoot.setmCode("9999");
			responseRoot.setMessage("网络故障稍后查询");
			e.printStackTrace();
		}
		JSONObject jsonResult = JSONObject.fromObject(responseRoot);
		logger.debug("inDepotOrder==返回结果："+jsonResult.toString());
		return responseRoot;
	}
	
	/**入库单查询接口
	 * apicode：10002
	 */
	@RequestMapping(params={"apicode=10002","v=1.0"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseRoot inDepotOrder(@RequestBody String json)throws Exception{
		logger.debug("=======inDepotOrder======"+json);
		ResponseRoot responseRoot = new ResponseRoot();
		try {
			if (true) {
				responseRoot.setmCode("9999");
				responseRoot.setMessage("接口已经禁用");
				return responseRoot;
			}
			JSONObject jsonData = JSONObject.fromObject(json);
			
			//必填参数校验
			String[] paramNames = {"appKey","orderType","startTime","endTime","sign"};
			String nullKey = DPParamVerify.verifyNullForMap(jsonData, paramNames);
			if(StringUtils.isNotBlank(nullKey)){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("必填参数为空:"+nullKey);
				return responseRoot;
			}
			
			//验签
			String appKey = (String)jsonData.get("appKey");//商家appkey
			ApiExternalConfigModel configModel = new ApiExternalConfigModel();
			configModel.setAppKey(appKey);
			configModel.setStatus(DERP_SYS.APIEXTERNALCONFIG_STATUS_1);//状态状态(1使用中,0已禁用)
			configModel = apiExternalConfigDao.searchByModel(configModel);
			if(configModel==null){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("商家appkey不存在");
				return responseRoot;
			}
			responseRoot = inDepotOrderService.queryInDepotOrder(jsonData,configModel.getMerchantId());

			//查询对外商品配置表该公司下启用的商品
			List<String> goodsNoList = apiGoodsConfigService.getConfigMerchandiseList(configModel.getMerchantId());

			List<InDepotOrder> inDepotOrderList = new ArrayList<InDepotOrder>();

			for (Object object: responseRoot.getDataList()) {
				InDepotOrder inDepotOrder = (InDepotOrder) object;

				List<InGoods> newGoodList = new ArrayList<>();
				List<InGoods> goodList = inDepotOrder.getGoodList();

				for (InGoods goods : goodList) {
					if (goodsNoList.contains(goods.getGoodsNo())) {
						newGoodList.add(goods);
					}
				}

				if (newGoodList.size() > 0) {
					inDepotOrder.setGoodList(newGoodList);
					inDepotOrderList.add(inDepotOrder);
				}
			}

			responseRoot.setDataList(inDepotOrderList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseRoot.setmCode("9999");
			responseRoot.setMessage("网络故障稍后查询");
			e.printStackTrace();
		}
		JSONObject jsonResult = JSONObject.fromObject(responseRoot);
		logger.debug("inDepotOrder==返回结果："+jsonResult.toString());
		return responseRoot;
	}

	/**采购订单、销售、电商查询接口
	 * apicode：10003
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(params={"apicode=10003","v=1.0"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseRoot orderDocuments(@RequestBody String json)throws Exception{
		logger.debug("=======orderDocuments======"+json);
		ResponseRoot responseRoot = new ResponseRoot();
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			
			//必填参数校验
			String[] paramNames = {"appKey","orderType","startTime","endTime","sign"};
			String nullKey = DPParamVerify.verifyNullForMap(jsonData, paramNames);
			if(StringUtils.isNotBlank(nullKey)){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("必填参数为空:"+nullKey);
				return responseRoot;
			}
			
			//验签
			String appKey = (String)jsonData.get("appKey");//商家appkey
			ApiExternalConfigModel configModel = new ApiExternalConfigModel();
			configModel.setAppKey(appKey);
			configModel.setStatus(DERP_SYS.APIEXTERNALCONFIG_STATUS_1);//状态状态(1使用中,0已禁用)
			configModel = apiExternalConfigDao.searchByModel(configModel);
			if(configModel==null){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("商家appkey不存在");
				return responseRoot;
			}
			responseRoot = orderDocumentsService.queryOrder(jsonData,configModel.getMerchantId());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseRoot.setmCode("9999");
			responseRoot.setMessage("网络故障稍后查询");
		}
		JSONObject jsonResult = JSONObject.fromObject(responseRoot);
		logger.debug("orderDocuments==返回结果："+jsonResult.toString());
		return responseRoot;
	}
	/**
	 * 库存查询接口
	 * apicode：10004
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(params={"apicode=10004","v=1.0"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseRoot queryInventoryBatchList(@RequestBody String json){
		logger.debug("库存查询接口开始======"+json);
		ResponseRoot responseRoot = new ResponseRoot();
		try{			
			JSONObject jsonData=new JSONObject();
			jsonData = JSONObject.fromObject(json);	
			//必填参数校验
			String[] paramNames = {"appKey","sign"};
			String nullKey = DPParamVerify.verifyNullForMap(jsonData, paramNames);
			if(StringUtils.isNotBlank(nullKey)){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("必填参数为空:"+nullKey);
				return responseRoot;
			}			
			//验签
			String appKey = (String)jsonData.get("appKey");//商家appkey
			ApiExternalConfigModel configModel = new ApiExternalConfigModel();
			configModel.setAppKey(appKey);
			configModel.setStatus(DERP_SYS.APIEXTERNALCONFIG_STATUS_1);//状态状态(1使用中,0已禁用)
			configModel = apiExternalConfigDao.searchByModel(configModel);
			if(configModel==null){
				responseRoot.setmCode("9999");
				responseRoot.setMessage("商家appkey不存在");
				return responseRoot;
			}
			responseRoot = inventoryBatchService.queryInventoryBatch(jsonData); 
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseRoot.setmCode("9999");
			responseRoot.setMessage("网络故障稍后查询");
			e.printStackTrace();
		}
		JSONObject jsonResult = JSONObject.fromObject(responseRoot);
		logger.debug("queryInventoryBatchList==返回结果："+jsonResult.toString());
		return responseRoot;
	}
}
