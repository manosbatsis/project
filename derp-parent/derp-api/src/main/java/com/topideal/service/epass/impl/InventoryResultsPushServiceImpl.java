package com.topideal.service.epass.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.service.epass.InventoryResultsPushService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 盘点结果推送
 * 
 * @author 杨创 2017/7
 */
@Service
public class InventoryResultsPushServiceImpl implements InventoryResultsPushService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResultsPushServiceImpl.class);

	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;// 商品信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息

	// 盘点结果推送
	@Override
	public JSONObject InventoryResultsPushInfo(String json,Long merchantId) throws Exception {
		LOGGER.info("盘点结果推送接口,请求开始json" + json);
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);

		String orderId = (String) jsonData.get("order_id");// 订单id
		String inventoryCode =(String) jsonData.get("custinventory_code");// 盘点单号
		String appKey = jsonData.getString("app_key");// appKey
		
		

		// 根据 商家id查询商家信息
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		if (merchantInfoModel==null) {
			LOGGER.error("根据秘钥表的商家id没有查询到商家信息,订单号custinventory_code:" + (String) jsonData.get("custinventory_code"));
			throw new RuntimeException("根据秘钥表的商家id没有查询到商家信息,订单号custinventory_code:" + (String) jsonData.get("custinventory_code"));	
		}		
		// 0商家 1 代理商 
		if (DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
			LOGGER.error("盘点结果推送 接口, 商家是代理商家(不接):custinventory_code:" + (String) jsonData.get("custinventory_code"));
			throw new RuntimeException("盘点结果推送 接口, 商家是代理商家(不接):custinventory_code:" + (String) jsonData.get("custinventory_code"));			
		}					
		// 实例化推送MQ的jJSON 对象
		JSONObject jsonMQData = new JSONObject();
		
		
		jsonMQData.put("merchantId", merchantInfoModel.getId());// 商家id
        jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
        jsonMQData.put("email", merchantInfoModel.getEmail());// 商家邮箱
        jsonMQData.put("inventoryResultEmail", merchantInfoModel.getInventoryResultEmail());// 盘点结果邮箱
        jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码       
        jsonMQData.put("inventoryCode", inventoryCode);// 企业调拨单号
		jsonMQData.put("orderCode", orderId);// 企业调拨单号
		jsonMQData.put("updateDate", (String) jsonData.get("order_date"));// 接收时间																			// ----录入日期
		jsonMQData.put("status", (String) jsonData.get("status"));// 10：制单30：提交70：成功90：作废
		jsonMQData.put("depoCode", (String) jsonData.get("warehouse_id"));// 仓库编码
		jsonMQData.put("serverType", (String) jsonData.get("serve_types"));// 服务类型 10：个性盘点  20：自主盘点
		jsonMQData.put("model", jsonData.get("busi_scene"));// 业务场景 非必填 10：客服盘点申请 20：仓库自行盘点30：前端盘点申请
		jsonMQData.put("dismissRemark", (String) jsonData.get("deal_result"));// 驳回原因必填
		
		
		
		

		// 商品信息
		JSONArray goodList = (JSONArray) jsonData.get("good_list");
		// 承接商品MQ
		JSONArray orderGoodsMQList = new JSONArray();

		for (Object good : goodList) {
			// 商品JSON
			JSONObject goodsJSON = (JSONObject) good;
			// 承接推MQ的商品JSON
			JSONObject goodsMQJSON = new JSONObject();

			// 商品货号 (根据商品货号查询商品)
			MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
			merchandiseInfoModel.setGoodsNo((String) goodsJSON.get("good_id"));// 商品货号
			merchandiseInfoModel.setMerchantId(merchantId);// 商家id
			merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
			merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);
			if (merchandiseInfoModel == null) {
				LOGGER.error("该app_key商家找不到该货号商品，商品货号" + (String) goodsJSON.get("good_id"));
				throw new RuntimeException("该app_key商家找不到该货号商品，商品货号" + (String) goodsJSON.get("good_id"));
			}
			// 判断标准条码是否为空
			if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}
			
			goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
			goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());// 商品id
			goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());// 商品名称
			goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码
			goodsMQJSON.put("goodsNo", (String) goodsJSON.get("good_id"));// 商品货号
			goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 条形码
			goodsMQJSON.put("batchNo",(String) goodsJSON.get("batch_code"));// 批次号			
			goodsMQJSON.put("amount", goodsJSON.get("amount"));// 系统数量
			goodsMQJSON.put("realqty", goodsJSON.get("realqty"));// 实盘数量
			goodsMQJSON.put("diffqty", goodsJSON.get("diffqty"));// 差异数
			//goodsMQJSON.put("", (String) goodsJSON.get("note"));// 备注
			goodsMQJSON.put("productionDate", (String) goodsJSON.get("produce_date"));// 生产日期
			goodsMQJSON.put("overdueDate", (String) goodsJSON.get("expire_date"));// 失效日期
			goodsMQJSON.put("isDamage", (String) goodsJSON.get("is_damage"));// YN坏品 0：好品 1：坏品
			orderGoodsMQList.add(goodsMQJSON);
			
		}
		
		jsonMQData.put("goodsList", orderGoodsMQList);
		LOGGER.info("盘点结果推送接口,请求开始json" + jsonMQData.toString());
		return jsonMQData;
	}

}
