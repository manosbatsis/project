package com.topideal.service.epass.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.epass.BigCargoTallyService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 大货理货接口
 * @author 杨创
 *2019.04.01
 */
@Service
public class BigCargoTallyServiceImpl implements BigCargoTallyService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BigCargoTallyServiceImpl.class);
	// 商品信息
	@Autowired 
	private MerchandiseInfoDao merchandiseInfoDao;
	@Autowired 
	private DepotInfoDao depotInfoDao;// 仓库
	// 商家信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	
	@Override
	public JSONObject bigCargoTally(String json,Long merchantId) throws Exception {
		LOGGER.info("大货理货接口Service 请求开始json:"+json);
    	// 实例化JSON对象
        JSONObject jsonData=JSONObject.fromObject(json);
        String warehouseId = jsonData.getString("warehouse_id");// 仓库编码	
		// 根据 商家id查询商家信息
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		if (merchantInfoModel==null) {
			LOGGER.error("根据秘钥表的商家id没有查询到商家信息,调拨单回推order_id:" + (String) jsonData.get("order_id"));
			throw new RuntimeException("根据秘钥表的商家id没有查询到商家信息,调拨单回推order_id:" + (String) jsonData.get("order_id"));	
		}
				
        // 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();
        jsonMQData.put("orderId", jsonData.getString("order_id"));//来源单据号
        jsonMQData.put("bomFinishTime", jsonData.getString("bom_finish_time"));//来源单据号
		
		//根据仓库编码查询仓库信息		
  		DepotInfoModel depotInfoModel= new  DepotInfoModel();
  		depotInfoModel.setCode(warehouseId);
  		depotInfoModel.setIsValetManage(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);//  订单查询非代客管理的仓库
//  		depotInfoModel.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
  		depotInfoModel = depotInfoDao.searchByModel(depotInfoModel);
  		if (depotInfoModel==null) {
  			LOGGER.error("根据仓库编码没有查询到对应的仓库信息warehouse_id:" + (String) jsonData.get("warehouse_id"));
			throw new RuntimeException("根据仓库编码没有查询到对应的仓库信息warehouse_id:" + (String) jsonData.get("warehouse_id"));		
	
		}

        jsonMQData.put("depotId", depotInfoModel.getId());//仓库id
        jsonMQData.put("depotName", depotInfoModel.getName());//仓库名称
        jsonMQData.put("depotCode", depotInfoModel.getCode());//仓库编码
        jsonMQData.put("depotType", depotInfoModel.getType());//仓库类型
        jsonMQData.put("isTopBooks", depotInfoModel.getIsTopBooks());//是否代销仓
        jsonMQData.put("batchValidation", depotInfoModel.getBatchValidation());// 是否强校验
		jsonMQData.put("merchantId", merchantInfoModel.getId());//商家id
		jsonMQData.put("merchantName", merchantInfoModel.getName());//商家名称
		jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());//卓志编码		
		//商品信息
		JSONArray detailList = (JSONArray) jsonData.get("detail_list");
		
		// 承接推送MQ是商品json
		 JSONArray originJsonGoodsMQList=new JSONArray();// 商品
		// 承接推送MQ是商品json
	     JSONArray targeJsonGoodsMQList=new JSONArray();// 商品
		for (Object detail : detailList) {
			JSONObject jsonDetail = (JSONObject) detail;
			
			// 获取商品信息批次
	        JSONArray originJsonDetailList= (JSONArray) jsonDetail.get("origin_detail_list");  // 原商品批次
	        JSONArray targetJsonDetailList= (JSONArray) jsonDetail.get("target_detail_list"); // 目标商品批次
	        
	       
	        for (Object originGoods : originJsonDetailList) {
	        	//商品JSON
	        	JSONObject originGoodsJSON = (JSONObject) originGoods;
	        	// 根据 出库单商品货号匹配入的商品货号
	        	String gcode = (String)originGoodsJSON.get("gcode");  

	        	//承接推MQ的商品JSON
	        	JSONObject goodsMQJSON = new JSONObject();
	        	// 根据商品货号查询商品
	        	MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
	        	merchandiseInfoModel.setMerchantId(merchantId);// 商家id
	        	merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1 );//状态(1使用中,0已禁用)
	        	merchandiseInfoModel.setGoodsNo(gcode);// 商品货号
	        	merchandiseInfoModel= merchandiseInfoDao.searchByModel(merchandiseInfoModel);
	        	if (merchandiseInfoModel==null) {
	        		LOGGER.error("根据商品货号没有查询到对应的商品,商品货号"+gcode);
	        		throw new RuntimeException("根据商品货号没有查询到对应的商品,商品货号"+gcode);	
				}
	        	// 判断标准条码是否为空
				if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
					LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
					throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
				}
				goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
	        	goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 商品条形码
	        	goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());//商品id
	        	goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());//商品货号
	        	goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());//商品条形码
	        	goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码			       	       	
	        	goodsMQJSON.put("num", originGoodsJSON.get("alloc_qty"));//数量
	        	goodsMQJSON.put("bathNo", originGoodsJSON.get("lot_no"));//批次号
	        	goodsMQJSON.put("productionDate", originGoodsJSON.get("production_time"));//生产日期
	        	goodsMQJSON.put("overdueDate", originGoodsJSON.get("expire_time"));//失效日期    
	        	goodsMQJSON.put("unit", originGoodsJSON.get("uom"));//出的单位   P：托盘，C：箱 , B：件
	        	goodsMQJSON.put("isDamaged", originGoodsJSON.get("is_damaged"));//字符串 0：好品，1：坏品	
	        	goodsMQJSON.put("stockBusinessType", originGoodsJSON.get("stock_business_type"));//字符串：10.B2B,20.B2C,30.C2C
	        	       	
	        	originJsonGoodsMQList.add(goodsMQJSON); 
	        }
	        
	        
	        for (Object targetGoods : targetJsonDetailList) {
	        	//商品JSON
	        	JSONObject targetGoodsJSON = (JSONObject) targetGoods;
	        	// 根据 出库单商品货号匹配入的商品货号
	        	String gcode = (String)targetGoodsJSON.get("gcode");  

	        	//承接推MQ的商品JSON
	        	JSONObject goodsMQJSON = new JSONObject();
	        	// 根据商品货号查询商品
	        	MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
	        	merchandiseInfoModel.setMerchantId(merchantId);// 商家id
	        	merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
	        	merchandiseInfoModel.setGoodsNo(gcode);// 商品货号
	        	merchandiseInfoModel= merchandiseInfoDao.searchByModel(merchandiseInfoModel);
	        	if (merchandiseInfoModel==null) {
	        		LOGGER.error("根据商品货号没有查询到对应的商品,商品货号"+gcode);
	        		throw new RuntimeException("根据商品货号没有查询到对应的商品,商品货号"+gcode);	
				}
	        	// 判断标准条码是否为空
				if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
					LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
					throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
				}
				goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
	        	goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 商品条形码
	        	goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());//商品id
	        	goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());//商品货号
	        	goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());//商品条形码
	        	goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码			       	       	
	        	goodsMQJSON.put("num", targetGoodsJSON.get("bom_qty"));//数量
	        	goodsMQJSON.put("bathNo", targetGoodsJSON.get("lot_no"));//批次号
	        	goodsMQJSON.put("productionDate", targetGoodsJSON.get("production_time"));//生产日期
	        	goodsMQJSON.put("overdueDate", targetGoodsJSON.get("expire_time"));//失效日期    
	        	goodsMQJSON.put("unit", targetGoodsJSON.get("uom"));//出的单位   P：托盘，C：箱 , B：件
	        	goodsMQJSON.put("isDamaged", targetGoodsJSON.get("is_damaged"));//字符串 0：好品，1：坏品	
	        	goodsMQJSON.put("stockBusinessType", targetGoodsJSON.get("stock_business_type"));//字符串：10.B2B,20.B2C,30.C2C   	       	
	        	targeJsonGoodsMQList.add(goodsMQJSON); 
	        }
	        
			
		}		  
        jsonMQData.put("originGoodsList", originJsonGoodsMQList);	
        jsonMQData.put("targeGoodsList", targeJsonGoodsMQList);	
        LOGGER.info("大货理货接收Service,请求结束json"+jsonMQData);
		return jsonMQData;
		
	}

}
