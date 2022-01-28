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
import com.topideal.entity.vo.main.ProductInfoModel;
import com.topideal.service.epass.StorageDeclareService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 入库申报接口实现类
 * @author 杨创
 *2018/5/25
 */
@Service
public class StorageDeclareServiceImpl implements StorageDeclareService{
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageDeclareServiceImpl.class);
	

	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao;// 商品
	@Autowired
	private MerchantInfoDao merchantInfoDao;// 商家信息
	
	//保存入库申报信息
	@Override
	@SystemServiceLog(point="1202",model="入库申报接口")
	public JSONObject storageDeclareInfo(String json,Long merchantId,String isRookie,String contractNoTag) throws Exception {
		/**(例子 菜鸟的采购订单只会对应一个入库申报单)
		 * 说明 菜鸟仓的订单 无论是采购订单模块  销售订单模块 调拨订单模块  都是只对应 一个采购订单 (销售订单,调拨订单) 只会推一次入库申报接口
		 */
		LOGGER.info("入库申报Service 请求开始json:"+json);
    	// 实例化JSON对象
        JSONObject jsonData=JSONObject.fromObject(json);
        
        // 根据 商家id查询商家信息
        MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
 		if (merchantInfoModel==null) {
 			LOGGER.error("根据秘钥表的商家id没有查询到商家信息,理货单 asn_no" + (String) jsonData.get("asn_no"));
 			throw new RuntimeException("根据秘钥表的商家id没有查询到商家信息,理货单 asn_no:" + (String) jsonData.get("asn_no"));	
 		}	
        
        // 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();
        jsonMQData.put("merchantId",merchantInfoModel.getId());// 商家id
        jsonMQData.put("merchantName", merchantInfoModel.getName());// 商家名称
        jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());// 卓志编码
        jsonMQData.put("isRookie", isRookie);// 是否为菜鸟仓（1-是，0-否)
        jsonMQData.put("tallyingOrderCode", (String)jsonData.get("asn_no"));//理货单ID
        jsonMQData.put("tallyingDate", (String)jsonData.get("opera_date"));//理货日期
        jsonMQData.put("entInboundId", (String)jsonData.get("ent_inbound_id"));//企业入仓编号（采购编号，唯一）
        jsonMQData.put("depotCode", (String)jsonData.get("warehouse_id"));//仓库编码
        jsonMQData.put("palletNum", jsonData.get("panel_wallnumber"));//托版数量
        jsonMQData.put("contractNo", jsonData.get("contract_no"));// 合同号	
		jsonMQData.put("contractNoTag", contractNoTag);// 合同号查询标识 1.采购
        // 获取商品批次 
        JSONArray jsonGoodsList= (JSONArray) jsonData.get("goods_list");  
        // 承接推送MQ是商品json
        JSONArray jsonGoodsMQList=new JSONArray();// 商品
        for (Object goods : jsonGoodsList) {
        	//商品JSON
        	JSONObject goodsJSON = (JSONObject) goods;
        	//承接推MQ的商品JSON
        	JSONObject goodsMQJSON = new JSONObject();
        	MerchandiseInfoModel merchandiseInfoModel =new MerchandiseInfoModel();
        	merchandiseInfoModel.setGoodsNo((String)goodsJSON.get("goods_id"));// 商品货号
        	merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
        	merchandiseInfoModel.setMerchantId(merchantId);
        	merchandiseInfoModel = merchandiseInfoDao.searchByModel(merchandiseInfoModel);
        	// 商品不存在
        	if (merchandiseInfoModel==null) {
        		LOGGER.error("该app_key商家找不到该货号商品,商品货号goods_id"+(String)goodsJSON.get("goods_id"));
        		throw new RuntimeException("该app_key商家找不到该货号商品,商品货号goods_id"+(String)goodsJSON.get("goods_id"));		
			}
        	// 判断标准条码是否为空
			if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}			
        	
        	String expectedAmount = goodsJSON.getString("expected_amount");// 申报数量
        	String amount = goodsJSON.getString("amount");// 正常数量
        	String lackAmount = goodsJSON.getString("lack_amount");// 缺货数量
        	String manyAmount = goodsJSON.getString("many_amount");// 多货数量
        	int intAmount= Integer.valueOf(amount);// 正常数量
        	int compareAmount=Integer.valueOf(expectedAmount)-Integer.valueOf(lackAmount)+Integer.valueOf(manyAmount);
        	if (intAmount!=compareAmount) {
        		LOGGER.error("商品中(理货数量)不等于(申报数量-缺失数量+多货数量),商品货号"+(String)goodsJSON.get("goods_id"));
        		throw new RuntimeException("商品中(理货数量)不等于(申报数量-缺失数量+多货数量),商品货号"+(String)goodsJSON.get("goods_id"));			
		
			}
        	goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
        	goodsMQJSON.put("tallyingUnit", (String)goodsJSON.get("uom"));//理货单位
        	goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());//商品id
        	goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());//商品名称
        	goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());//商品编码
        	goodsMQJSON.put("goodsNo", (String)goodsJSON.get("goods_id"));//商品货号   	
        	goodsMQJSON.put("purchaseNum", goodsJSON.get("expected_amount"));//申报数量(应收数量)取采购单数       	
        	goodsMQJSON.put("tallyingNum", goodsJSON.get("amount"));//理货数量（规则：理货数量=申报数量-缺失数量+多货数量）实收数量
        	goodsMQJSON.put("lackNum", goodsJSON.get("lack_amount"));//缺失数量（默认0）
        	goodsMQJSON.put("multiNum", goodsJSON.get("many_amount"));//多货数量
        	goodsMQJSON.put("totoalNormalNum", goodsJSON.get("totoal_sales_amount"));//总数量（正常数量）
        	goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());//商品条形码
        	goodsMQJSON.put("grossWeight", merchandiseInfoModel.getGrossWeight());//毛重
        	goodsMQJSON.put("netWeight", merchandiseInfoModel.getNetWeight());//净重
        	goodsMQJSON.put("volume", merchandiseInfoModel.getVolume());//体积
        	goodsMQJSON.put("length", merchandiseInfoModel.getLength());//长
        	goodsMQJSON.put("width", merchandiseInfoModel.getWidth());//宽
        	goodsMQJSON.put("height", merchandiseInfoModel.getHeight());//高
        	
        	
        	//获取商品批次
        	JSONArray receiveList =(JSONArray) goodsJSON.get("receive_list");
        	// 承接推送商品批次的json
        	JSONArray receiveMQList =new JSONArray();
        	int salesAmountTotal=0;// 用于批次找那个正常品的统计
        	for (Object receive : receiveList) {
        		JSONObject receiveJSON=(JSONObject) receive;       		
        		JSONObject receiveMQJSON =new JSONObject();
        		receiveMQJSON.put("wornNum", receiveJSON.get("worn_amount"));//坏货数量
        		receiveMQJSON.put("expireNum", receiveJSON.get("expire_amount"));//过期数量
        		receiveMQJSON.put("normalNum", receiveJSON.get("sales_amount"));//正常数量（正常数量）
        		receiveMQJSON.put("productionDate", receiveJSON.get("produced_date"));//生产日期
        		receiveMQJSON.put("overdueDate", receiveJSON.get("expired_date"));//过期数量
        		receiveMQJSON.put("batchNo", receiveJSON.get("batch_id"));//批次号
        		receiveMQList.add(receiveMQJSON);       
        		String salesAmount = receiveJSON.getString("sales_amount");//正常数量
        		salesAmountTotal=salesAmountTotal+Integer.valueOf(salesAmount);
			}
        	// 商品中的总数量和批次中的正常品数量和比较
        	String totoalSalesAmount = goodsJSON.getString("totoal_sales_amount");
        	int intTotoalSalesAmount = Integer.valueOf(totoalSalesAmount);
        	if (salesAmountTotal!=intTotoalSalesAmount) {
        		LOGGER.error("商品中的订单商品总数量(正常品)和批次中正常品的数量和不等,商品货号goods_id"+(String)goodsJSON.get("goods_id"));
        		throw new RuntimeException("商品中的订单商品总数量(正常品)和批次中正常品的数量和不等,商品货号goods_id"+(String)goodsJSON.get("goods_id"));		
			}
        	
        	goodsMQJSON.put("receiveList", receiveMQList);        	
        	jsonGoodsMQList.add(goodsMQJSON);       	       	
		}
        jsonMQData.put("goodsList", jsonGoodsMQList);
        LOGGER.info("入库申报Service 请求结束json:"+jsonMQData.toString());
    
        return jsonMQData;
          
	}

}
