package com.topideal.service.epass.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.epass.TransfersOutStorageService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 调拨出库接口
 * @author 杨创
 *2018/6/11
 */
@Service
public class TransfersOutStorageServiceImpl implements TransfersOutStorageService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransfersOutStorageServiceImpl.class);
	@Autowired 
	private MerchandiseInfoDao merchandiseInfoDao;// 商品信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息

	
	@Override
	@SystemServiceLog(point="1209",model="调拨出库回推接口")
	public JSONObject transfersOutStorageInfo(String json,Long merchantId,String isRookie) throws Exception {
		LOGGER.info("调拨出库回推接口Service 请求开始json:"+json);
    	// 实例化JSON对象
        JSONObject jsonData=JSONObject.fromObject(json);
        
        MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
 		if (merchantInfoModel == null) {
			LOGGER.error("商家不存在,调拨单号custtransfer_code" + (String)jsonData.get("custtransfer_code"));
			throw new RuntimeException("商家不存在,调拨单号custtransfer_code" + (String)jsonData.get("custtransfer_code"));
		}
        
        // 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();
        jsonMQData.put("merchantId", merchantInfoModel.getId());// 商家id
		jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
		jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码
		jsonMQData.put("isRookie", isRookie);// 是否是经分销的单是否为菜鸟仓（1-是，0-否）
        jsonMQData.put("orderCode", jsonData.getString("order_id"));//订单号 必填
        jsonMQData.put("code", jsonData.getString("custtransfer_code"));//调拨单号(例如调拨入库单单号) 必填
        jsonMQData.put("transferDate", jsonData.getString("odate"));//调拨时间 非必填
        jsonMQData.put("remark", (String)jsonData.get("note"));// 备注 非必填
        
        // jsonMQData.put("orderCode", jsonData.getString("customs_code"));//申报第海关必填
        // jsonMQData.put("orderCode", jsonData.getString("ciqb_code"));//申报地国检 必填
         jsonMQData.put("model", jsonData.getString("busi_scene"));//业务场景busi_scene 必填
         jsonMQData.put("serveTypes", jsonData.getString("serve_types"));// 服务类型必填
        // jsonMQData.put("orderCode", jsonData.getString("busi_type"));//业务类型 必填
        // jsonMQData.put("orderCode", (String)jsonData.get("from_electric_code"));//调出电商企业编码 必填
        // jsonMQData.put("orderCode", (String)jsonData.get("from_electric_name"));// 调出电商企业名称 非必填
        //  jsonMQData.put("orderCode", (String)jsonData.get("from_foundp_code"));//调出资金方 必填
        //  jsonMQData.put("orderCode", (String)jsonData.get("to_electric_code"));// 调入电商企业编码 非必填
        // jsonMQData.put("orderCode", (String)jsonData.get("to_foundp_code"));// 调入资金方 必填
        //jsonMQData.put("orderCode", (String)jsonData.get("trust_code"));// 委托单位 必填
        //  jsonMQData.put("orderCode", (String)jsonData.get("from_store_code"));// 调出仓库 必填
        //  jsonMQData.put("orderCode", (String)jsonData.get("to_store_code"));// 调入仓库 必填
        //  jsonMQData.put("orderCode", (String)jsonData.get("contract_no"));// 合同号 非必填
		
		
        // 获取商品批
        JSONArray jsonGoodsList= (JSONArray) jsonData.get("detail_list");  
        // 承接推送MQ是商品json
        JSONArray jsonGoodsMQList=new JSONArray();// 商品
        for (Object goods : jsonGoodsList) {
        	//商品JSON
        	JSONObject goodsJSON = (JSONObject) goods;
        	//承接推MQ的商品JSON
        	JSONObject goodsMQJSON = new JSONObject();
        	// 根据商品货号查询商品
        	MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
        	merchandiseInfoModel.setMerchantId(merchantId);// 商家id
        	merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
        	merchandiseInfoModel.setGoodsNo((String)goodsJSON.get("from_good_id"));// 调出商品货号
        	merchandiseInfoModel= merchandiseInfoDao.searchByModel(merchandiseInfoModel);
        	if (merchandiseInfoModel==null) {
        		LOGGER.error("该app_key商家找不到该货号商品,商品货号"+(String)goodsJSON.get("from_good_id"));
        		throw new RuntimeException("该app_key商家找不到该货号商品,商品货号"+(String)goodsJSON.get("from_good_id"));	
			}
        	if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}
			goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
        	goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());//商品id
        	goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());//商品货号
        	goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());//商品名称
        	goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());//(货品)条形码       	
        	goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码
        	goodsMQJSON.put("num", goodsJSON.get("amount"));// 数量 必填
        	goodsMQJSON.put("batchNo", (String)goodsJSON.get("batch_id"));// 调入批次 非必填
        	goodsMQJSON.put("productionDate", (String)goodsJSON.get("produce_d"));// 时间格式：yyyy-mm-dd HH:mi:ss 非必填
        	goodsMQJSON.put("overdueDate", (String)goodsJSON.get("expire_date"));//  时间格式：yyyy-mm-dd HH:mi:ss 非必填
        	goodsMQJSON.put("expiration", goodsJSON.get("expiration"));//调入有效期天数 非必填

			String wornAmount = "0";
			if (goodsJSON.containsKey("worn_amount")) {
				wornAmount = goodsJSON.getString("worn_amount"); //损货数量
			}
			if (StringUtils.isEmpty(wornAmount)) {
				wornAmount = "0";
			}
			goodsMQJSON.put("wornNum", wornAmount); //损货数量


			String salesAmount = "0";
			if (goodsJSON.containsKey("sales_amount")) {
				salesAmount = goodsJSON.getString("sales_amount"); //批次销售数量（正常数量）
				//非必填
			}

			if (StringUtils.isEmpty(salesAmount)) {
				salesAmount = "0";
			}
			goodsMQJSON.put("salesNum", salesAmount);

			//检查每个批次的调拨数量（amount）=损货数量（wornAmount）+批次销售数量（salesAmount）是否成立
			if (Integer.valueOf(goodsJSON.getString("amount")) != Integer.valueOf(salesAmount) +
					Integer.valueOf(wornAmount)) {
				LOGGER.error("商品货号:" + (String) goodsJSON.get("from_good_id") + "中批次号:" + goodsJSON.get("batch_id") + "的好品数量与坏品数量总和不等于批次总数量");
				throw new RuntimeException("商品货号:" + (String) goodsJSON.get("from_good_id") + "中批次号:" + goodsJSON.get("batch_id") + "的好品数量与坏品数量总和不等于批次总数量");
			}
        	jsonGoodsMQList.add(goodsMQJSON); 
        	       	
        }
		
        jsonMQData.put("goodsList", jsonGoodsMQList);		
        LOGGER.info("调拨入库消息接收Service,请求结束json"+jsonMQData);
		return jsonMQData;
		
	}

}
