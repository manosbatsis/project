package com.topideal.service.epass.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.main.DepotInfoDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.dao.main.MerchantInfoDao;
import com.topideal.entity.vo.main.DepotInfoModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.entity.vo.main.MerchantInfoModel;
import com.topideal.service.epass.TransferOrderService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 调拨单回推
 */
@Service
public class TransferOrderServiceImpl implements TransferOrderService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferOrderServiceImpl.class);
	// 商品信息
	@Autowired 
	private MerchandiseInfoDao merchandiseInfoDao;
	@Autowired 
	private DepotInfoDao depotInfoDao;// 仓库
	// 商家信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	
	@Override
	@SystemServiceLog(point="1213",model="调拨单回推接口")
	public JSONObject transferOrderPush(String json,Long merchantId) throws Exception {
		LOGGER.info("调拨单回推接口Service 请求开始json:"+json);
    	// 实例化JSON对象
        JSONObject jsonData=JSONObject.fromObject(json);
        
        String appKey = jsonData.getString("app_key");// appKey
		
		// 根据 商家id查询商家信息
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		if (merchantInfoModel==null) {
			LOGGER.error("根据秘钥表的商家id没有查询到商家信息,调拨单回推custtransfer_code:" + (String) jsonData.get("custtransfer_code"));
			throw new RuntimeException("根据秘钥表的商家id没有查询到商家信息,调拨单回推custtransfer_code:" + (String) jsonData.get("custtransfer_code"));	
		}
		// 0商家 1 代理商 
		if (DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
			LOGGER.error("调拨单回推接口商家是代理商家(不接):custtransfer_code:" + (String) jsonData.get("custtransfer_code"));
			throw new RuntimeException("调拨单回推接口商家是代理商家(不接):custtransfer_code:" + (String) jsonData.get("custtransfer_code"));	
	
		}		
        // 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();
        jsonMQData.put("sourceCode", jsonData.getString("custtransfer_code"));//来源单据号
        jsonMQData.put("adjustmentTypeName", "货号变更");//类型调整名称
        String toStoreCode = jsonData.getString("to_store_code");//仓库编码 入的仓库编码
		String fromStoreCode = jsonData.getString("from_store_code");//仓库编码 出的仓库编码
		String odate = jsonData.getString("odate");//调拨时间   格式：yyyy-mm-dd HH:mi:ss
		jsonMQData.put("codeTime", odate);//单据日期
		// 如果出和入的仓库编码不同拦截
		if (!toStoreCode.equals(fromStoreCode)) {
			LOGGER.error("调拨单回推接口出和入的仓库编码不同:custtransfer_code:" + (String) jsonData.get("custtransfer_code"));
			throw new RuntimeException("调拨单回推接口出和入的仓库编码不同:custtransfer_code:" + (String) jsonData.get("custtransfer_code"));		
		}
		
		//获取调入仓库信息		
  		DepotInfoModel inDepotInfoModel= new  DepotInfoModel();
  		inDepotInfoModel.setCode(toStoreCode);
  		inDepotInfoModel.setIsValetManage(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);//  订单查询非代客管理的仓库
//  		inDepotInfoModel.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
  		inDepotInfoModel = depotInfoDao.searchByModel(inDepotInfoModel);
  		if (inDepotInfoModel==null) {
  			LOGGER.error("调入仓库不存在custtransfer_code:" + (String) jsonData.get("custtransfer_code"));
			throw new RuntimeException("调入仓库不存在custtransfer_code:" + (String) jsonData.get("custtransfer_code"));		
	
		}
  		// 调出仓库编码
  		DepotInfoModel outDepotInfoModel= new  DepotInfoModel();
  		outDepotInfoModel.setCode(fromStoreCode);
//  		outDepotInfoModel.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
  		outDepotInfoModel.setIsValetManage(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);//  订单查询非代客管理的仓库
  		outDepotInfoModel = depotInfoDao.searchByModel(outDepotInfoModel);
  		if (outDepotInfoModel==null) {
  			LOGGER.error("调出仓库不存在custTransferCode:" + (String) jsonData.get("custTransferCode"));
			throw new RuntimeException("调出仓库不存在custTransferCode:" + (String) jsonData.get("custTransferCode"));			
		}
        jsonMQData.put("inDepotId", inDepotInfoModel.getId());//仓库id
        jsonMQData.put("inDepotName", inDepotInfoModel.getName());//仓库名称
        jsonMQData.put("inDepotCode", inDepotInfoModel.getCode());//仓库编码
        jsonMQData.put("inDepotType", inDepotInfoModel.getType());//仓库类型
        jsonMQData.put("inDepotIsTopBooks", inDepotInfoModel.getIsTopBooks());//是否代销仓
        jsonMQData.put("outDepotId", outDepotInfoModel.getId());//仓库id
        jsonMQData.put("outDepotName", outDepotInfoModel.getName());//仓库名称
        jsonMQData.put("outDepotCode", outDepotInfoModel.getCode());//仓库编码
        jsonMQData.put("outDepotType", outDepotInfoModel.getType());//仓库类型
        jsonMQData.put("outDepotIsTopBooks", outDepotInfoModel.getIsTopBooks());//是否代销仓
		jsonMQData.put("merchantId", merchantInfoModel.getId());//商家id
		jsonMQData.put("merchantName", merchantInfoModel.getName());//商家名称
		jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());//卓志编码		
        // 获取商品信息
        JSONArray jsonGoodsList= (JSONArray) jsonData.get("detail_list");  
        JSONArray jsonWmsDetailList= (JSONArray) jsonData.get("wms_detail_list"); 
        // 承接推送MQ是商品json
        JSONArray jsonGoodsMQList=new JSONArray();// 商品
        for (Object wmsGoods : jsonWmsDetailList) {
        	//商品JSON
        	JSONObject wmsGoodsJSON = (JSONObject) wmsGoods;
        	// 根据 出库单商品货号匹配入的商品货号
        	String wmsFromGoodId = (String)wmsGoodsJSON.get("from_good_id");  
        	String wmsFromUom= (String)wmsGoodsJSON.get("uom");// 出的单位
        	
        	String fromGoodName=null;// 调出商品名称
    		String toGoodId=null;//调入商品货号
    		String toGoodName=null;// 调入商品名称     
    		String fromUom=null;// 入的理货单位
        	for (Object goods : jsonGoodsList) {
        		JSONObject goodsJSON = (JSONObject) goods;
        		String fromGoodId = (String)goodsJSON.get("from_good_id"); 
        		fromUom = (String)goodsJSON.get("uom"); //入的单位 
        		if (fromGoodId.equals(wmsFromGoodId)) {
        			fromGoodName = (String)goodsJSON.get("from_good_name");// 调出商品名称
        			toGoodId = (String)goodsJSON.get("to_good_id");// 调入商品货号
        			toGoodName = (String)goodsJSON.get("to_good_name"); // 调入商品名称       
        			// 如果是海外仓比较两个单位是否相同
        			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoModel.getType())) {
						if (!fromUom.equals(wmsFromUom)) {
							LOGGER.error("出的单位和入的单位不同"+wmsFromGoodId+"toGoodId(调入单号)"+toGoodId);
							throw new RuntimeException("出的单位和入的单位不同"+wmsFromGoodId+"toGoodId(调入单号)"+toGoodId);
					
						}
					}
        			
				}				
			}  
        	if (StringUtils.isBlank(toGoodId)) {
				LOGGER.error("根据调出商品货号没有匹配到调入商品货号wmsFromGoodId(调出货号)"+wmsFromGoodId+"toGoodId(调入单号)"+toGoodId);
				throw new RuntimeException("根据调出商品货号没有匹配到调入商品货号wmsFromGoodId(调出货号)"+wmsFromGoodId+"toGoodId(调入单号)"+toGoodId);
			}
        	//承接推MQ的商品JSON
        	JSONObject goodsMQJSON = new JSONObject();
        	// 根据商品货号查询商品
        	MerchandiseInfoModel fromGoods = new MerchandiseInfoModel();
        	fromGoods.setMerchantId(merchantId);// 商家id
        	fromGoods.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
        	fromGoods.setGoodsNo(wmsFromGoodId);// 商品货号
        	fromGoods= merchandiseInfoDao.searchByModel(fromGoods);
        	if (fromGoods==null) {
        		LOGGER.error("根据商品货号没有查询到对应的商品,商品货号"+wmsFromGoodId);
        		throw new RuntimeException("根据商品货号没有查询到对应的商品,商品货号"+wmsFromGoodId);	
			}
        	// 判断标准条码是否为空
        	if (StringUtils.isBlank(fromGoods.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  fromGoods.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + fromGoods.getGoodsNo());
			}
			goodsMQJSON.put("oldCommbarcode", fromGoods.getCommbarcode());// 商品标准条码			
        	goodsMQJSON.put("oldBarcode", fromGoods.getBarcode());// 商品条形码
        	goodsMQJSON.put("oldGoodsId", fromGoods.getId());//商品id
        	goodsMQJSON.put("oldGoodsNo", fromGoods.getGoodsNo());//商品货号
        	goodsMQJSON.put("oldGoodsName", fromGoods.getName());//商品条形码
        	goodsMQJSON.put("oldGoodsCode", fromGoods.getGoodsCode());// 商品编码			
        	MerchandiseInfoModel toGoods = new MerchandiseInfoModel();
        	toGoods.setMerchantId(merchantId);// 商家id
        	toGoods.setGoodsNo(toGoodId);// 商品货号
        	toGoods.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
        	toGoods= merchandiseInfoDao.searchByModel(toGoods);
        	if (toGoods==null) {
        		LOGGER.error("该app_key商家找不到该货号商品,商品货号"+toGoodId);
        		throw new RuntimeException("该app_key商家找不到该货号商品,商品货号"+toGoodId);	
			}
        	// 判断标准条码是否为空
        	if (StringUtils.isBlank(fromGoods.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  fromGoods.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + fromGoods.getGoodsNo());
			}
			goodsMQJSON.put("commbarcode", toGoods.getCommbarcode());// 商品标准条码	
        	goodsMQJSON.put("barcode", toGoods.getBarcode());// 商品条形码
        	goodsMQJSON.put("goodsId", toGoods.getId());//商品id
        	goodsMQJSON.put("goodsNo", toGoods.getGoodsNo());//商品货号
        	goodsMQJSON.put("goodsName", toGoods.getName());//商品条形码
        	goodsMQJSON.put("goodsCode", toGoods.getGoodsCode());// 商品编码
        	
        	goodsMQJSON.put("num", wmsGoodsJSON.get("qtp"));//数量
        	goodsMQJSON.put("bathNo", wmsGoodsJSON.get("lot_no"));//批次号
        	goodsMQJSON.put("productionDate", wmsGoodsJSON.get("produce_date"));//生产日期
        	goodsMQJSON.put("overdueDate", wmsGoodsJSON.get("due_date"));//失效日期    
        	goodsMQJSON.put("outTallyingUnit", wmsFromUom);//出的单位   P：托盘，C：箱 , B：件
        	goodsMQJSON.put("inTallyingUnit", fromUom);//入的单位  P：托盘，C：箱 , B：件
        	
        	jsonGoodsMQList.add(goodsMQJSON); 
        }
        jsonMQData.put("goodsList", jsonGoodsMQList);		
        LOGGER.info("调拨单回推消息接收Service,请求结束json"+jsonMQData);
		return jsonMQData;
		
	}

}
