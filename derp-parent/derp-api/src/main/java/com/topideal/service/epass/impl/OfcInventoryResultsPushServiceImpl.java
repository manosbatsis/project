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
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.epass.OfcInventoryResultsPushService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * ofc盘点结果推送
 * 
 * @author 杨创 2017/12/3
 */
@Service
public class OfcInventoryResultsPushServiceImpl implements OfcInventoryResultsPushService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(B2COrderServiceImpl.class);

	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;// 商品信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	@Autowired
	private DepotInfoDao depotInfoDao;// 仓库信息

	// 盘点结果推送
	@Override
	public JSONObject getOfcInventoryResultsPushInfo(String json) throws Exception {
		LOGGER.info("ofc盘点结果推送接口,请求开始json" + json);
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);

		String orderId = (String) jsonData.get("order_id");// 订单id
		String inventoryOrder =(String) jsonData.get("inventory_order");// 盘点单号
		String electricCode=(String) jsonData.get("electric_code");// 卓志编码
		
		
		// 根据 商家id查询商家信息
		MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
		merchantInfoModel.setTopidealCode(electricCode);
		merchantInfoModel = merchantInfoDao.searchByModel(merchantInfoModel);
		if (merchantInfoModel==null) {
			LOGGER.error("根据卓志编码没有查询到商家信息,盘点单号inventory_order:" + (String) jsonData.get("inventory_order"));
			throw new RuntimeException("根据卓志编码没有查询到商家信息,盘点单号inventory_order:" + (String) jsonData.get("inventory_order"));	
		}		
		// 0商家 1 代理商 
		if (DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
			LOGGER.error("ofc盘点结果推送 接口, 商家是代理商家(不接):inventory_order:" + (String) jsonData.get("inventory_order"));
			throw new RuntimeException("ofc盘点结果推送 接口, 商家是代理商家(不接):inventory_order:" + (String) jsonData.get("inventory_order"));			
		}	
		
		// 仓库
		DepotInfoModel depotInfoModel = new DepotInfoModel();
		String depotCode = jsonData.getString("stock_code");			
		depotInfoModel.setCode(depotCode);
		depotInfoModel.setIsValetManage(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);//  订单查询非代客管理的仓库
//		depotInfoModel.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
		depotInfoModel = depotInfoDao.searchByModel(depotInfoModel);
		if (depotInfoModel==null) {
			LOGGER.error("根据仓库编码没有查询非代销仓仓库,盘点单号inventory_order:" + (String) jsonData.get("inventory_order"));
			throw new RuntimeException("根据仓库编码没有查询非代销仓仓库,盘点单号inventory_order:" + (String) jsonData.get("inventory_order"));			
		}
		if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoModel.getType())) {
			LOGGER.error("ofc盘点结果回推接口目前只接收海外仓业务,盘点单号inventory_order:" + (String) jsonData.get("inventory_order"));
			throw new RuntimeException("ofc盘点结果回推接口目前只接收海外仓业务,盘点单号inventory_order:" + (String) jsonData.get("inventory_order"));				
		}
										
		// 实例化推送MQ的jJSON 对象
		JSONObject jsonMQData = new JSONObject();
		
		
		jsonMQData.put("merchantId", merchantInfoModel.getId());// 商家id
        jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
        jsonMQData.put("email", merchantInfoModel.getEmail());// 商家邮箱
        jsonMQData.put("inventoryResultEmail", merchantInfoModel.getInventoryResultEmail());// 盘点结果邮箱
        jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码       
        jsonMQData.put("inventoryCode", inventoryOrder);// 企业调拨单号
		jsonMQData.put("orderCode", orderId);// 企业调拨单号																		// ----录入日期
		jsonMQData.put("status", (String) jsonData.get("profit_loss_status"));// 00 初始化 20 已发布 30 审核中 40 审核通过 60 审核未通 99 完成  90 取消 (默认99)
		jsonMQData.put("depoId", depotInfoModel.getId());// 仓库id
		jsonMQData.put("depoName", depotInfoModel.getName());// 仓库名称
		jsonMQData.put("depoCode", depotInfoModel.getCode());// 仓库编码
		jsonMQData.put("depoType", depotInfoModel.getType());// 类别
		jsonMQData.put("isTopBooks", depotInfoModel.getIsTopBooks());//是否是代销仓(1-是,0-否)',		
		jsonMQData.put("serverType", (String) jsonData.get("serve_types"));//服务类型  10：客服盘点申请 20：仓库自行盘点  30：前端盘点申请
		jsonMQData.put("model", jsonData.get("busi_scene"));// 业务场景 10：个性盘点 20自主盘点
		jsonMQData.put("galFinishTime", jsonData.get("galFinishTime"));// 业务场景 10：个性盘点 20自主盘点
		
		
		
		
		// 商品信息
		JSONArray goodList = (JSONArray) jsonData.get("goods_list");
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
			merchandiseInfoModel.setMerchantId(merchantInfoModel.getId());// 商家id
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
			goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());// 商品货号
			goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 条形码
			goodsMQJSON.put("batchNo",(String) goodsJSON.get("batch_code"));// 批次号			
			goodsMQJSON.put("amount", goodsJSON.get("amount"));// 系统数量
			goodsMQJSON.put("realqty", goodsJSON.get("realqty"));// 实盘数量
			goodsMQJSON.put("diffqty", goodsJSON.get("diversity_number"));// 差异数
			goodsMQJSON.put("productionDate", (String) goodsJSON.get("produced_date"));// 生产日期
			goodsMQJSON.put("overdueDate", (String) goodsJSON.get("due_date"));// 失效日期
			goodsMQJSON.put("isDamage", (String) goodsJSON.get("is_damage"));// 好坏品
			goodsMQJSON.put("unit", (String) goodsJSON.get("unit"));// P：托盘，C：箱 , B：件		
			goodsMQJSON.put("reasonCode", (String) goodsJSON.get("reason_code"));//原因代码01：盘点差异；02：可保存报废；03：不可保存报废；04：二手售卖；05：调拨差异；06：收货错误；07：仓库错发；08：赔付平台已审批			
			goodsMQJSON.put("reasonDes", (String) goodsJSON.get("reason_des"));//原因描述
			goodsMQJSON.put("stockBusiType", (String) goodsJSON.get("stock_busi_type"));//B2B、B2C，默认B2C		
			orderGoodsMQList.add(goodsMQJSON);
			
		}
		
		jsonMQData.put("goodsList", orderGoodsMQList);
		LOGGER.info("ofc盘点结果推送接口,请求开始json" + jsonMQData.toString());
		return jsonMQData;
	}

}
