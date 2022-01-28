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
import com.topideal.service.epass.SelfInventoryUpdateService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 自营库存更新
 */
@Service
public class SelfInventoryUpdateServiceImpl implements SelfInventoryUpdateService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SelfInventoryUpdateServiceImpl.class);
	// 商品信息
	@Autowired 
	private MerchandiseInfoDao merchandiseInfoDao;
	
	@Autowired 
	private DepotInfoDao depotInfoDao;// 仓库
	// 商家信息
	@Autowired
	private MerchantInfoDao merchantInfoDao;
	
	@Override
	@SystemServiceLog(point="1212",model="自营库存更新接口")
	public JSONObject selfInventoryUpdate(String json,Long merchantId) throws Exception {
		LOGGER.info("自营库存更新接口Service 请求开始json:"+json);
    	// 实例化JSON对象
        JSONObject jsonData=JSONObject.fromObject(json);               		
		// 根据 商家id查询商家信息
		MerchantInfoModel merchantInfoModel = merchantInfoDao.searchById(merchantId);
		if (merchantInfoModel==null) {
			LOGGER.error("根据秘钥表的商家id没有查询到商家信息,自营库存更新order_id:" + (String) jsonData.get("order_id"));
			throw new RuntimeException("根据秘钥表的商家id没有查询到商家信息,自营库存更新order_id:" + (String) jsonData.get("order_id"));	
		}
		
		// 0商家 1 代理商 
		if (DERP_SYS.MERCHANTINFO_ISPROXY_1.equals(merchantInfoModel.getIsProxy())) {
			LOGGER.error("自营库存更新接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));
			throw new RuntimeException("自营库存更新接口商家是代理商家(不接):order_id:" + (String) jsonData.get("order_id"));		
		}
		
        // 实例化推送MQ的jJSON 对象
        JSONObject jsonMQData=new JSONObject();
        jsonMQData.put("sourceCode", (String)jsonData.get("order_id"));//来源单据号      
        jsonMQData.put("orderType", (String)jsonData.get("order_type"));//调整类型 05：好坏品互转   06：效期调整      
        jsonMQData.put("adjustmentTypeName", (String)jsonData.get("order_typename"));//类型调整名称
        String warehouseId = (String)jsonData.get("warehouse_id");//仓库编码
        //获取仓库信息
        DepotInfoModel depotInfoModel = new DepotInfoModel();
        depotInfoModel.setCode(warehouseId);
        depotInfoModel.setStatus(DERP_SYS.DEPOTINFO_STATUS_1); //状态 0-禁用 1-启用
        depotInfoModel.setIsValetManage(DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);//  订单查询非代客管理的仓库
//        depotInfoModel.setIsTopBooks(DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);//是否是代销仓(1-是,0-否)
        depotInfoModel = depotInfoDao.searchByModel(depotInfoModel);   
        if (depotInfoModel==null) {
        	LOGGER.error("自营库存更新接口,根据仓库编码没有查到对应仓库信息:order_id:" + (String) jsonData.get("order_id"));
			throw new RuntimeException("自营库存更新接口,根据仓库编码没有查到对应仓库信息:order_id:" + (String) jsonData.get("order_id"));		
		}
        jsonMQData.put("depotId", depotInfoModel.getId());//仓库id
        jsonMQData.put("depotName", depotInfoModel.getName());//仓库名称
        jsonMQData.put("depotCode", depotInfoModel.getCode());//仓库编码
        jsonMQData.put("depotType", depotInfoModel.getType());//仓库类型
        jsonMQData.put("depotIsTopBooks", depotInfoModel.getIsTopBooks());//是否代销仓
		String orderDateStr = (String)jsonData.get("order_date");//单据日期   格式：yyyy-mm-dd HH:mi:ss
		jsonMQData.put("codeTime", orderDateStr);//单据时间
		String sendTimeStr = (String)jsonData.get("send_time");//推送时间   格式：yyyy-mm-dd HH:mi:ss
		jsonMQData.put("pushTime", sendTimeStr);//推送时间
		jsonMQData.put("merchantId", merchantInfoModel.getId());//商家id
		jsonMQData.put("merchantName", merchantInfoModel.getName());//商家名称
		jsonMQData.put("topidealCode", merchantInfoModel.getTopidealCode());//卓志编码
		
        // 获取商品信息
        JSONArray jsonGoodsList= (JSONArray) jsonData.get("goods_list");  
        // 承接推送MQ是商品json
        JSONArray jsonGoodsMQList=new JSONArray();// 商品
        for (Object goods : jsonGoodsList) {
        	//商品JSON
        	JSONObject goodsJSON = (JSONObject) goods;
        	
        	// 海外仓理货单位必填
        	if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoModel.getType())) {
        		//P：托盘，C：箱 , B：件
        		String uom = (String)goodsJSON.get("uom");	
        		if (StringUtils.isBlank(uom)) {
        			LOGGER.error("自营库存更新接口海外仓理货单位不能为空:order_id:" + (String) jsonData.get("order_id"));
        			throw new RuntimeException("自营库存更新接口海外仓理货单位不能为空:order_id:" + (String) jsonData.get("order_id"));		

				}
        		if (!("P".equals(uom)||"C".equals(uom)||"B".equals(uom))) {
        			LOGGER.error("自营库存更新接口海外仓理货单位不对:order_id:" + (String) jsonData.get("order_id"));
        			throw new RuntimeException("自营库存更新接口海外仓货单位不对:order_id:" + (String) jsonData.get("order_id"));		
				}
        		// 理货单位
        		String tallyingUnitStr0=(String)goodsJSON.get("goods_id")+goodsJSON.getString("goods_batch")+(String)goodsJSON.get("uom");        		
        		Integer storeQty0 = goodsJSON.getInt("store_qty");
        		int storeQtyAbs0=Math.abs(storeQty0);// 取绝对值
        		String isDamage0 = goodsJSON.getString("is_damage");
        		
        		
        		// 如果是好坏品互转 好品的单位和坏品的单位要一致
        		Boolean flag=true;
        		if ("05".equals((String)jsonData.get("order_type"))) {
        			for (Object goods1 : jsonGoodsList) {
        	        	//商品JSON
        	        	JSONObject goodsJSON1 = (JSONObject) goods1;
                		String tallyingUnitStr1=(String)goodsJSON1.get("goods_id")+goodsJSON1.getString("goods_batch")+(String)goodsJSON1.get("uom");        		
                		Integer storeQty1 = goodsJSON1.getInt("store_qty");
                		int storeQtyAbs1 = Math.abs(storeQty1);// 取绝对值
                		String isDamage1 = goodsJSON1.getString("is_damage");
                		// 如果根据批次匹配到好品
                		if (tallyingUnitStr1.equals(tallyingUnitStr0)&&!isDamage1.equals(isDamage0)) {
							if (storeQtyAbs0==storeQtyAbs1) {
								flag=false;
							}
						}
                		
                		
        			}
				}
        		
        		if ("05".equals((String)jsonData.get("order_type"))&&flag) {
        			LOGGER.error("自营库存更新接口存在好品理货单位和数量没有匹配到坏品理货单位和数量" + (String) jsonData.get("order_id"));
        			throw new RuntimeException("自营库存更新接口存在好品理货单位和数量没有匹配到坏品理货单位和数量" + (String) jsonData.get("order_id"));		
			
				}
        		
        		
        		
        		
			}
        	
        	//承接推MQ的商品JSON
        	JSONObject goodsMQJSON = new JSONObject();
        	// 根据商品货号查询商品
        	MerchandiseInfoModel merchandiseInfoModel = new MerchandiseInfoModel();
        	merchandiseInfoModel.setMerchantId(merchantId);// 商家id
        	merchandiseInfoModel.setGoodsNo((String)goodsJSON.get("goods_id"));// 商品货号
        	merchandiseInfoModel.setStatus(DERP_SYS.MERCHANDISEINFO_STATUS_1);//状态(1使用中,0已禁用)
        	merchandiseInfoModel= merchandiseInfoDao.searchByModel(merchandiseInfoModel);
        	if (merchandiseInfoModel==null) {
        		LOGGER.error("该app_key商家找不到该货号商品,商品货号"+(String)goodsJSON.get("goods_id"));
        		throw new RuntimeException("该app_key商家找不到该货号商品,商品货号"+(String)goodsJSON.get("goods_id"));	
			}
        	// 判断标准条码是否为空
			if (StringUtils.isBlank(merchandiseInfoModel.getCommbarcode())) {
				LOGGER.error("商品标准条码为空，商品货号" +  merchandiseInfoModel.getGoodsNo());
				throw new RuntimeException("商品标准条码为空，商品货号" + merchandiseInfoModel.getGoodsNo());
			}
			goodsMQJSON.put("commbarcode", merchandiseInfoModel.getCommbarcode());// 商品标准条码
        	goodsMQJSON.put("goodsId", merchandiseInfoModel.getId());//商品id
        	goodsMQJSON.put("goodsNo", merchandiseInfoModel.getGoodsNo());//商品货号
        	goodsMQJSON.put("goodsName", merchandiseInfoModel.getName());//商品条形码
        	goodsMQJSON.put("goodsCode", merchandiseInfoModel.getGoodsCode());// 商品编码
			goodsMQJSON.put("barcode", merchandiseInfoModel.getBarcode());// 商品条码
			goodsMQJSON.put("goodsBatch", goodsJSON.get("goods_batch"));// 商品批次号
			goodsMQJSON.put("isDamage", goodsJSON.get("is_damage"));//是否坏品  2：好品，1：坏品
			goodsMQJSON.put("productionDate", goodsJSON.get("production_time"));//生产日期
			goodsMQJSON.put("overdueDate", goodsJSON.get("exp_time"));//失效日期
			goodsMQJSON.put("oldProductionDate", goodsJSON.get("source_production_time"));//原生产日期
			goodsMQJSON.put("oldOverdueDate", goodsJSON.get("source_exp_date"));//原失效日期
			goodsMQJSON.put("storeQty", goodsJSON.get("store_qty"));//调整数量（正数为增加，负数为减少）
			String storeQty = goodsJSON.getString("store_qty");// 调整数量
			Integer num = Math.abs(Integer.parseInt(storeQty));// 调整数量的绝对值
			goodsMQJSON.put("num", num);//数量（绝对值）
			goodsMQJSON.put("tallyingUnit", (String)goodsJSON.get("uom"));// 海外仓理货单位	//P：托盘，C：箱 , B：件		
        	jsonGoodsMQList.add(goodsMQJSON); //// 调整数量的绝对值
        }
        jsonMQData.put("goodsList", jsonGoodsMQList);		
        LOGGER.info("自营库存更新消息接收Service,请求结束json"+jsonMQData);
		return jsonMQData;
		
	}

}
