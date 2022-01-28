package com.topideal.service.epass.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.base.ApiSecretConfigDao;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.epass.ReturnMessagePushService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 退运单信息推送
 * @author 杨创
 *2018/6/17
 */
@Service
public class ReturnMessagePushServiceImpl implements ReturnMessagePushService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(ReturnMessagePushServiceImpl.class);
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;//商品信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	@Autowired
	private ApiSecretConfigDao apiSecretConfigDao;// api密钥配置
	@Autowired
	private DepotInfoDao depotInfoDao;// 仓库信息
		
	//说明 目前用到退运信息的只有调拨出库
	@Override
	@SystemServiceLog(point="1210",model="退运单信息推送接口")
	public JSONObject returnMessagePushInfo(String json, Long merchantId,String isRookie) throws Exception {
		
		/**(例子 菜鸟的采购订单只会对应一个入库申报单)
		 * 说明 菜鸟仓的订单 无论是采购订单模块  销售订单模块 调拨订单模块  都是只对应 一个采购订单 (销售订单,调拨订单) 只会推一次入库申报接口
		 *无论是调拨 还是 销售 通过退运过来  一个调拨 (销售)订单 他们只会推一次 生成一个 对应的调拨出库单和销售出库单
		 */
		/**
		 * 对于退运信息 目前只对状态是70的操作
		 */
		LOGGER.info("退运单信息推送接口Service 请求开始json:"+json);
		JSONObject jsonData = JSONObject.fromObject(json);
		
		
		// 根据 商家id查询商家信息
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		if (merchantInfoModel==null) {
			LOGGER.error("根据秘钥表的商家id没有查询到商家信息,订单号order_id:" + (String) jsonData.get("order_id"));
			throw new RuntimeException("根据秘钥表的商家id没有查询到商家信息,订单号order_id:" + (String) jsonData.get("order_id"));	
		}
		
		String status = jsonData.getString("status");
		if (!"70".equals(status)) {
			LOGGER.error("单据状态status非70不接收,订单号order_id:" + (String) jsonData.get("order_id"));
			throw new RuntimeException("单据状态status非70不接收,订单号order_id:" + (String) jsonData.get("order_id"));	
	
		}
		
		
		//如果 是销毁服务20  仓库编码必填
		DepotInfoModel depotInfoModel = new DepotInfoModel();
		if ("20".equals(jsonData.getString("serve_types"))) {
			String depotCode = jsonData.getString("ware_houseId");			
			depotInfoModel.setCode(depotCode);
			//depotInfoModel.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
			depotInfoModel = depotInfoDao.searchByModel(depotInfoModel);
			if (depotInfoModel==null) {
				LOGGER.error("销毁服务20 根据仓库编码没有查询到对应的仓库信息,订单号order_id:" + (String) jsonData.get("order_id"));
				throw new RuntimeException("销毁服务20 根据仓库编码没有查询到对应的仓库信息,订单号order_id:" + (String) jsonData.get("order_id"));			
			}
			
			// 0商家 1 代理商 
			if (DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
				LOGGER.error("退运信息接口 (销毁服务20), 商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));
				throw new RuntimeException("退运信息接口 (销毁服务20), 商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));			
			}
		}
		// 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();
        jsonMQData.put("depotCode", (String)jsonData.get("ware_houseId"));// 仓库编码
        jsonMQData.put("merchantId", merchantInfoModel.getId());// 商家id
        jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
        jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码
        jsonMQData.put("isRookie", isRookie);// 是否为菜鸟仓（1-是，0-否)
        jsonMQData.put("orderCode", jsonData.getString("order_id"));// 订单号 必填
        jsonMQData.put("transferDate", jsonData.getString("odate")); // 调出日期(发货日期) 必填
        jsonMQData.put("status", jsonData.getString("status"));//10：制单30：提交70：成功90：作废 必填
        jsonMQData.put("serveTypes", jsonData.getString("serve_types"));//服务类型10：退运服务（默认）20：销毁服务 30：跨关区转出转关服务
       
        
        // 获取商品批次 
        /*JSONArray jsonGoodsList= (JSONArray) jsonData.get("good_list"); */ 
        JSONArray jsonWmsDetailList= (JSONArray) jsonData.get("wms_detail_list"); 
        
        // 承接推送MQ是商品json
        JSONArray jsonGoodsMQList=new JSONArray();// 商品
        
        for (Object wmsGoods : jsonWmsDetailList) {
        	//商品JSON
        	JSONObject wmsGoodsJSON = (JSONObject) wmsGoods;       	
        	// 把 跨境宝下面的生成日期 调到jsonGoodsList 循环中
        	String wmsGoodsNo = wmsGoodsJSON.getString("good_id");// 商品货号        
        	String wmsStockType=(String)wmsGoodsJSON.get("stock_type");//0:好品;1:坏品
        	// 
        	/*for (Object goods : jsonGoodsList) {
        		JSONObject goodsJSON = (JSONObject) goods;
        		String goodsNo=(String)goodsJSON.get("good_id");//  商品货号
        		String stockType= (String)goodsJSON.get("stock_type");//0:好品;1:坏品
        		// 如果wms_detail_list中商品货号和 good_list 中商品货号相同  把库存类型 good_list0:好品;1:坏品放到wms_detail_list中
        		if (wmsGoodsNo.equals(goodsNo)) {
        			if (null !=wmsStockType&&(!stockType.equals(wmsStockType))) {
        				LOGGER.error("同一商品货号不能同时又是好品,又是坏品,order_id订单号"+(String) jsonData.get("order_id"));
						throw new AbnormalException("同一商品货号不能同时又是好品,又是坏品,order_id订单号"+(String) jsonData.get("order_id"));
					}
        			wmsStockType=stockType;
				}
			}*/
        	
        	
        	
        	//承接推MQ的商品JSON
        	JSONObject goodsMQJSON = new JSONObject();
        	// 更加商品货号查询商品信息
        	MerchandiseInfoModel merchandiseInfoModel =new MerchandiseInfoModel();
        	merchandiseInfoModel.setGoodsNo(wmsGoodsJSON.getString("good_id"));
        	merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
        	merchandiseInfoModel.setMerchantId(merchantId);// 商家id
        	merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);
        	if (merchandiseInfoModel==null) {
        		LOGGER.error("该app_key商家找不到该货号商品,商品货号"+wmsGoodsJSON.getString("good_id"));
        		throw new RuntimeException("该app_key商家找不到该货号商品,商品货号"+wmsGoodsJSON.getString("good_id"));	
			}
        	// 判断标准条码是否为空
			if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}
        	goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
        	goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());//商品id
        	goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());//商品货号
        	goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());//商品名称
        	goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());//商品编码
        	goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());//条形码
        	goodsMQJSON.put("num", wmsGoodsJSON.get("wms_qtp"));//退运数量 必填
        	goodsMQJSON.put("stockType",wmsStockType);//0:好品;1:坏品; 必填
        	goodsMQJSON.put("batchNo", (String)wmsGoodsJSON.get("wms_lot_no"));//退运批次
        	goodsMQJSON.put("productionDate", (String)wmsGoodsJSON.get("wms_production_time"));//生产日期
        	goodsMQJSON.put("overdueDate", (String)wmsGoodsJSON.get("wms_exp_date"));//失效日期	
        	
        	
        	jsonGoodsMQList.add(goodsMQJSON);
		}
        
		jsonMQData.put("goodsList", jsonGoodsMQList);
		LOGGER.info("退运单信息推送Service 请求结束json:"+jsonMQData);
		return jsonMQData;
	}

}
