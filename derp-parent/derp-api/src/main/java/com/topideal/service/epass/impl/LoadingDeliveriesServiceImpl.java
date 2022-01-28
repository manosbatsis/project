package com.topideal.service.epass.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.service.epass.LoadingDeliveriesService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 装载交运回推接口实现类
 * @author 杨创
 *2018/6/5
 */
@Service
public class LoadingDeliveriesServiceImpl implements LoadingDeliveriesService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(LoadingDeliveriesServiceImpl.class);
	
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;	// 商品		
	@Autowired 
	private DepotInfoDao depotInfoDao;//仓库信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息

	/**
	 * 保存装载交运信息 
	 * 说明: 装载交运信息推进来(电商订单变成已发货并且生成交运单和交运单商品 和减库存)
	 */
	@Override
	public JSONObject loadingDeliveriesInfo(String json,Long merchantId,String isRookie) throws Exception {
		LOGGER.info("装载交运信息Service 请求开始json:"+json);
		//  获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
 		if (merchantInfoModel == null) {
			LOGGER.error("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
			throw new RuntimeException("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
		}
 		String appType = jsonData.getString("app_type");
 		if ("20".equals(appType)&&DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
 			// 0商家 1 代理商 			
 			LOGGER.error("装载交运接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));
 			throw new RuntimeException("装载交运接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));	
		}
		
		
		// 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();     
        jsonMQData.put("merchantId",merchantInfoModel.getId());// 商家id
        jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
        jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码
        jsonMQData.put("isRookie", isRookie);// 是否为菜鸟仓（1-是，0-否)
        jsonMQData.put("orderCode", jsonData.getString("order_id"));// 订单号(LBX号)必填
        jsonMQData.put("wayBillNo", (String)jsonData.get("way_bill_no"));//运单号必填 香港仓的时候非必填
        jsonMQData.put("deliverDate", (String)jsonData.get("delivery_time"));//发货时间   非必填
        jsonMQData.put("logisticsCode", (String)jsonData.get("logistics_code"));//物流公司代码 非必填
        jsonMQData.put("logisticsName", (String)jsonData.get("logistics_name"));//物流公司名称  非必填
        jsonMQData.put("blNo", (String)jsonData.get("bl_no"));// 提单号 非必填
        jsonMQData.put("type", jsonData.getString("app_type"));//服务类型 必填
        jsonMQData.put("remark", (String)jsonData.get("notes"));//服务类型 非必填
        jsonMQData.put("loadWeight", jsonData.get("load_weight"));//包裹重量 非必填

        
        //获取商品json
        JSONArray goodsList = (JSONArray) jsonData.get("goods");
        // 承接推送商品的json
        JSONArray goodsMQList = new JSONArray();
        for (Object goods : goodsList) {
        	JSONObject goodsJSON =(JSONObject) goods;
        	//承接推MQ的商品JSON
        	JSONObject goodsMQJSON = new JSONObject();
        	//根据商品货号查询商品信息
			MerchandiseInfoModel merchandiseInfoModel =new MerchandiseInfoModel();
			merchandiseInfoModel.setMerchantId(merchantId);
			merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
			merchandiseInfoModel.setGoodsNo((String)goodsJSON.get("good_no"));//商品货号good_no
			merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);
			// 判断商品是否存在
			if (merchandiseInfoModel==null) {
				throw new RuntimeException("该app_key商家找不到该货号商品,商品货号good_no"+(String)goodsJSON.get("good_no"));		
			}
			// 判断标准条码是否为空
			if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}
			goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
			goodsMQJSON.put("tallyingUnit", goodsJSON.get("uom"));// 商品id
			goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());// 商品id
			goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());// 商品货号
			goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());// 商品名称
			goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码
			goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 货品条形码
			goodsMQJSON.put("num", goodsJSON.get("amount"));//数量
			goodsMQJSON.put("price", goodsJSON.get("unit_price"));//（单价）实际价格
			goodsMQJSON.put("batchNo", goodsJSON.get("batch_id"));// 批次号 
			goodsMQJSON.put("cphTaxRate", goodsJSON.get("cph_tax_rate"));//税率 非必填
			goodsMQJSON.put("cphTaxFee", goodsJSON.get("cph_tax_fee"));//税费 非必填
			goodsMQJSON.put("productionDate", goodsJSON.get("production_time"));//生产日期
			goodsMQJSON.put("overdueDate", goodsJSON.get("exp_date"));//失效日期
			
			
			// 装载交运 没有涉及 生产日期和失效日期
			goodsMQList.add(goodsMQJSON);
		}
        jsonMQData.put("goodsList", goodsMQList);// 放置商品批次			
		LOGGER.info("装载交运信息Service 请求结束json:"+jsonMQData);
		return jsonMQData;
	}
	/**
	 * 菜鸟仓装载交运
	 */
/*	@Override
	public JSONObject rookieLoadingDeliveriesInfo(String json, Long merchantId, String isRookie) throws Exception {
		LOGGER.info("菜鸟仓库装载交运信息Service 请求开始");
		//  获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
 		if (merchantInfoModel == null) {
			LOGGER.error("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
			throw new RuntimeException("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
		}
 		String appType = jsonData.getString("app_type");
 		if ("20".equals(appType)&&DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
 			// 0商家 1 代理商 			
 			LOGGER.error("类型是20装载交运接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));
 			throw new RuntimeException("类型是20装载交运接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));	
		}
 		
 		// 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();     
        jsonMQData.put("merchantId",merchantInfoModel.getId());// 商家id
        jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
        jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码
        jsonMQData.put("isRookie", isRookie);// 是否为菜鸟仓（1-是，0-否)
        jsonMQData.put("orderCode", jsonData.getString("order_id"));// 订单号(LBX号)必填
        jsonMQData.put("wayBillNo", (String)jsonData.get("way_bill_no"));//运单号必填 香港仓的时候非必填
        jsonMQData.put("deliverDate", (String)jsonData.get("delivery_time"));//发货时间   非必填
        jsonMQData.put("logisticsCode", (String)jsonData.get("logistics_code"));//物流公司代码 非必填
        jsonMQData.put("logisticsName", (String)jsonData.get("logistics_name"));//物流公司名称  非必填
        jsonMQData.put("blNo", (String)jsonData.get("bl_no"));// 提单号 非必填
        jsonMQData.put("type", jsonData.getString("app_type"));//服务类型 必填
        jsonMQData.put("remark", (String)jsonData.get("notes"));//服务类型 非必填
		jsonMQData.put("loadWeight", jsonData.get("load_weight"));//包裹重量 非必填
        LOGGER.info("装载交运信息Service 请求结束");
		return jsonMQData;
	}*/

}
