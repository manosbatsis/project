package com.topideal.service.epass.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.service.epass.OutInventoryService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 出库明细
 * @author 杨创
 *2020/6/8
 */
@Service
public class OutInventoryServiceImpl implements OutInventoryService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(OutInventoryServiceImpl.class);
	
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;	// 商品		
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息

	/**
	 * 出库明细 
	 */
	@Override
	public JSONObject outInventoryDetail(String json,Long merchantId) throws Exception {
		//  获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
 		if (merchantInfoModel == null) {
			LOGGER.error("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
			throw new RuntimeException("商家不存在,订单号order_id" + (String)jsonData.get("order_id"));
		}
		
		// 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();     
        jsonMQData.put("merchantId",merchantInfoModel.getId());// 商家id
        jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
        jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码
        jsonMQData.put("orderCode", jsonData.getString("order_id"));// 订单号(LBX号)必填
        jsonMQData.put("externalCode", (String)jsonData.get("order_code"));//外部单号
        jsonMQData.put("deliverDate", (String)jsonData.get("created_time"));//发货时间   非必填
       
        //获取商品json
        JSONArray goodsList = (JSONArray) jsonData.get("good_list");
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
			merchandiseInfoModel.setGoodsNo((String)goodsJSON.get("good_id"));//商品货号good_no
			merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);
			// 判断商品是否存在
			if (merchandiseInfoModel==null) {
				throw new RuntimeException("该app_key商家找不到该货号商品,商品货号good_id"+(String)goodsJSON.get("good_id"));		
			}
			// 判断标准条码是否为空
			if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}			
			goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
			goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());// 商品id
			goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());// 商品货号
			goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());// 商品名称
			goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码
			goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 货品条形码
			goodsMQJSON.put("num", goodsJSON.get("amount"));//数量
			goodsMQJSON.put("batchNo", goodsJSON.get("batch_no"));// 批次号 
			goodsMQJSON.put("productionDate", goodsJSON.get("production_date"));//生产日期
			goodsMQJSON.put("overdueDate", goodsJSON.get("expired_date"));//失效日期
			goodsMQJSON.put("stockType", goodsJSON.get("stock_type"));//0：好品  ,1：坏品
			goodsMQJSON.put("remark", goodsJSON.get("notes"));//备注
			
			
			// 出库明细 
			goodsMQList.add(goodsMQJSON);
		}
        jsonMQData.put("goodsList", goodsMQList);// 放置商品批次			
		return jsonMQData;
	}

}
