package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.sapience.sapience001.SapienceRequest;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.EpassAPIMethodEnum;
import com.topideal.common.enums.MQPushApiEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.base.PackTypeDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.entity.vo.base.PackTypeModel;
import com.topideal.service.SendSapienceGoodsService;

import net.sf.json.JSONObject;

/**
 * 向卓普信推送商品档案
 * @author 杨创
 *
 */
@Service
public class SendSapienceGoodsServiceImpl implements SendSapienceGoodsService {

	private static final Logger LOGGER= LoggerFactory.getLogger(SendSapienceGoodsServiceImpl.class);

	@Autowired
	private MerchandiseInfoDao  merchandiseInfoDao;
	@Autowired
	private PackTypeDao  packTypeDao;

	@Autowired
	private RMQProducer rocketMQProducer;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201700003,model=DERP_LOG_POINT.POINT_13201700003_Label,keyword="derpTime")
	public void sendSapienceGoods(String json,String keys, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		LOGGER.info("----------向卓普信推送商品档案--------开始"+json);
		//0000138宝信,1000000204健太1,0000134卓烨贸易,000000626润佰贸易
		List<PackTypeModel> packTypeList = packTypeDao.list(new PackTypeModel());
		Map<String, String>packTypeMap=new HashMap<>();
		for (PackTypeModel model : packTypeList) {
			packTypeMap.put(model.getCode(), model.getName());
		}
		String startTime = (String) jsonData.get("startTime");//开始时间 定时器/页面
		String endTime = (String) jsonData.get("endTime");// 结束时间  定时器的/页面
		String goodsNo = (String) jsonData.get("goodsNo");//货号  页面
		Long merchantId=null;
		Long goodsId =null;
		if (jsonData.get("merchantId")!=null) merchantId=jsonData.getLong("merchantId");//商家   页面
		if (jsonData.get("goodsId")!=null) goodsId=jsonData.getLong("goodsId");//商家   页面
		
		String tag = (String) jsonData.get("tag");//"2" 页面触发单个货号 "1" 定时器 
		if (StringUtils.isNotBlank(tag)&&"2".equals(tag)) {//来自页面刷新	
			if (merchantId==null)return;
			if (goodsId==null)return;
		}else {
			tag="1";
		}
		
			
		if (StringUtils.isBlank(startTime))startTime=TimeUtils.format(TimeUtils.addDay(TimeUtils.getNow(), -1), "yyyy-MM-dd");
		if (StringUtils.isBlank(endTime))endTime=TimeUtils.format(TimeUtils.getNow(), "yyyy-MM-dd");

		int startIndex = 0;
    	int pageSize = 1000;//每页1000		
		Map<String, Object>parmMap=new HashMap<>();
		parmMap.put("startTime", startTime);
		parmMap.put("endTime", endTime);
		parmMap.put("goodsNo", goodsNo);
		parmMap.put("merchantId", merchantId);
		parmMap.put("goodsId", goodsId);
		parmMap.put("tag", tag);
		parmMap.put("pageSize", pageSize);
		
		while(true){
			parmMap.put("startIndex",startIndex);			
			List<Map<String,Object>>goodsMapList=merchandiseInfoDao.getSendSapienceGoodsList(parmMap);
			if (goodsMapList.size()==0) break;
			startIndex=startIndex+pageSize;			
			List<String>requestJsonList=new ArrayList<String>();		
			for (Map<String, Object> goodsMap : goodsMapList) {			
				SapienceRequest request= getSapienceRequest(goodsMap,packTypeMap);
				JSONObject requestJson = JSONObject.fromObject(request);
				requestJson.put("order_id",goodsMap.get("goods_code"));
				requestJson.put("method", EpassAPIMethodEnum.SAPIENCE_E001_METHOD.getMethod());
				requestJsonList.add(requestJson.toString());
			}
			//推送mq
			for (String requestJson : requestJsonList) {
				rocketMQProducer.send(requestJson, MQPushApiEnum.PUSH_SAPIENCE.getTopic(), MQPushApiEnum.PUSH_SAPIENCE.getTags());
				Thread.sleep(10);
			}
			
		}
		
		
		LOGGER.info("----------向卓普信推送商品档案--------结束");

		
	}
	/**
	 * 获取请求实体
	 * @param goodsMap
	 * @return
	 */
	private SapienceRequest getSapienceRequest(Map<String, Object> goodsMap,Map<String, String>packTypeMap) {
		String goodsNo = (String) goodsMap.get("goods_no");
		String goodsName = (String) goodsMap.get("goods_name");
		BigDecimal filingPrice = (BigDecimal) goodsMap.get("filing_price");
		String barcode = (String) goodsMap.get("barcode");
		String goodsCode = (String) goodsMap.get("goods_code");
		String englishGoodsName = (String) goodsMap.get("english_goods_name");
		String describe = (String) goodsMap.get("describe");
		String isGroup = (String) goodsMap.get("is_group");
		String isRecord = (String) goodsMap.get("is_record");
		Timestamp modifyDate = (Timestamp) goodsMap.get("modify_date");
		String topidealCode = (String) goodsMap.get("topideal_code");
		String merchantName = (String) goodsMap.get("merchant_name");
		String spec = (String) goodsMap.get("spec");
		String hsCode = (String) goodsMap.get("hs_code");
		Integer shelfLifeDays = (Integer) goodsMap.get("shelf_life_days");
		String manufacturer = (String) goodsMap.get("manufacturer");
		String manufacturerAddr = (String) goodsMap.get("manufacturer_addr");
		Double netWeight = (Double) goodsMap.get("net_weight");
		Double grossWeight = (Double) goodsMap.get("gross_weight");
		String component = (String) goodsMap.get("component");		
		String countryName = (String) goodsMap.get("country_name");
		String packType = (String) goodsMap.get("pack_type");
		String unitName = (String) goodsMap.get("unit_name");
		//String catName = (String) goodsMap.get("cat_name");
		//String brandName = (String) goodsMap.get("brand_name");
		String packName=null;
		if (StringUtils.isNotBlank(packType)) {
			packName = packTypeMap.get(packType);
		}
		// 替换特殊字符
		//goodsName =replaceAllStr(goodsName);
		//spec=replaceAllStr(spec);
		if (StringUtils.isBlank(spec)) {
			spec="/";
		}
		//describe=replaceAllStr(describe);
		
		//润百推送卓普信 卓志编码要改为 2020120101
		if ("1000000626".equals(topidealCode)) {
			topidealCode="2020120101";
		}
		//component=replaceAllStr(component);
		//englishGoodsName=replaceAllStr(englishGoodsName);
		SapienceRequest request=new SapienceRequest();
		request.setGoodsNo(goodsNo);
		request.setGoodsName(goodsName);
		request.setStatus("10");// 全部推送10 全部推送新增
		//request.setGoodsCategory(catName);
		//request.setGoodsBrand(brandName);
		request.setGoodsSpec(spec);
		request.setGoodsPacktype(packName);// 
		//request.setGoodsType(goodsNo);
		request.setMerchantId(topidealCode);
		request.setMerchantName(merchantName);
		if (filingPrice!=null)request.setRecordPrice(filingPrice.toString());
		request.setContractsUnit(unitName);
		request.setGoodsBarcode(barcode);
		request.setGoodsHsCode(hsCode);
		request.setCompGoodsNo(goodsCode);// 企业商品字编码
		request.setGoodsEnName(englishGoodsName);
		request.setGoodsDesc(describe);
		if (shelfLifeDays!=null)request.setGoodsQualityDays(shelfLifeDays);		
		//request.setTaxCode(goodsNo);
		//request.setTaxRate(goodsNo);
		//request.setYTaxCode(goodsNo);
		//request.setYTaxRate(goodsNo);
		request.setProduceComp(manufacturer);
		request.setProduceCompAddr(manufacturerAddr);
		request.setQtpUnit(unitName);
		request.setQtyUnit2(unitName);
		if (grossWeight!=null) request.setKgs(grossWeight.toString());
		if (netWeight!=null) request.setNet(netWeight.toString());		
		request.setIngredient(component);//成分
		//request.setPoisonStr(goodsNo);
		//request.setAdditiveStr(goodsNo);
		request.setCustassemCountry(countryName);
		if (StringUtils.isNotBlank(isGroup))request.setIsCombined(Integer.valueOf(isGroup));
		
		//request.setNote(goodsNo);
		//request.setModifier(goodsNo);
		if (modifyDate!=null)request.setModTime(TimeUtils.format(modifyDate, "yyyy-MM-dd HH:mm:ss"));		
		//request.setDisableTime(goodsNo);
		///**是否备案 1-是，0-否*/
		if (DERP_SYS.MERCHANDISEINFO_ISRECORD_0.equals(isRecord)) {
			request.setIsRecord(2);//整数，1：是，2：否
		}else if (DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(isRecord)) {
			request.setIsRecord(1);//整数，1：是，2：否
		}
		request.setSourceCode("10");//要推送10
		return request;
	}
	/**
	 * 替换特殊字符  + 导致签名错误
	 * @param goodsName
	 * @return
	 */
	/*private String replaceAllStr(String str) {
		if (StringUtils.isBlank(str))return str;
		str=str.replace("+"," ");
		str=str.replace("%","% ");
		return str;
	}*/


}
