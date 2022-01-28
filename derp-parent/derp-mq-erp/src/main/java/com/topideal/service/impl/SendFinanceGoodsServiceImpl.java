package com.topideal.service.impl;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.finance.FinanceUtils;
import com.topideal.api.finance.f3_01.FianceSysGoodsRequest;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.base.CountryOriginDao;
import com.topideal.dao.base.UnitInfoDao;
import com.topideal.dao.main.CustomerInfoDao;
import com.topideal.dao.main.MerchandiseInfoDao;
import com.topideal.entity.vo.base.CountryOriginModel;
import com.topideal.entity.vo.base.UnitInfoModel;
import com.topideal.entity.vo.main.CustomerInfoModel;
import com.topideal.entity.vo.main.MerchandiseInfoModel;
import com.topideal.service.SendFinanceGoodsService;

import net.sf.json.JSONObject;

/**
 * 向卓普信推送商品档案
 * @author 杨创
 *
 */
@Service
public class SendFinanceGoodsServiceImpl implements SendFinanceGoodsService {

	private static final Logger LOGGER= LoggerFactory.getLogger(SendFinanceGoodsServiceImpl.class);

	@Autowired
	private MerchandiseInfoDao  merchandiseInfoDao;
	@Autowired
	private CustomerInfoDao customerInfoDao;
	@Autowired
	private UnitInfoDao unitInfoDao;
	@Autowired
	private CountryOriginDao countryOriginDao;


	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201700006,model=DERP_LOG_POINT.POINT_13201700006_Label,keyword="derpTime")
	public void sendFinanceGoods(String json,String keys, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		LOGGER.info("----------向金融推送商品档案--------开始"+json);

		
		Object goodsIdObj = jsonData.get("goodsId");
		if (goodsIdObj==null) throw new RuntimeException("goodsId不能为空");
		Object customerIdObj = jsonData.get("customerId");
		if (customerIdObj==null) throw new RuntimeException("customerId不能为空");
	
		Long goodsId = jsonData.getLong("goodsId");//商品id
		Long customerId = jsonData.getLong("customerId");//客户id
		//查询客户
		CustomerInfoModel customerInfoModel = customerInfoDao.searchById(customerId);
		if (customerInfoModel==null)throw new RuntimeException("根据客户id没有查询到客户");
		//查询商品
		MerchandiseInfoModel merchandiseInfo = merchandiseInfoDao.searchById(goodsId);
		if (merchandiseInfo==null)throw new RuntimeException("根据商品id没有找到商品");
		//查询单位
		UnitInfoModel unitInfo =null;
		if (merchandiseInfo.getUnit()!=null) {
			unitInfo = unitInfoDao.searchById(merchandiseInfo.getUnit());
		}
		if (unitInfo==null) unitInfo=new UnitInfoModel();
		//查询原产国
		CountryOriginModel country =null;
		if (merchandiseInfo.getCountyId()!=null) {
			country = countryOriginDao.searchById(merchandiseInfo.getCountyId());
		}
		if (country==null) country=new CountryOriginModel();
		
		FianceSysGoodsRequest goodsRequest=new FianceSysGoodsRequest();
		goodsRequest.setGoodsNo(merchandiseInfo.getGoodsNo());
		goodsRequest.setGoodsName(merchandiseInfo.getName());
		goodsRequest.setStatus("10");
		goodsRequest.setGoodsSpec(merchandiseInfo.getSpec());
		goodsRequest.setMerchantId(customerInfoModel.getMainId());
		goodsRequest.setMerchantName(customerInfoModel.getName());
		goodsRequest.setRecordPrice(merchandiseInfo.getFilingPrice());
		goodsRequest.setContractsUnit(unitInfo.getName());
		goodsRequest.setGoodsBarcode(merchandiseInfo.getBarcode());
		goodsRequest.setGoodsQualityDays(merchandiseInfo.getShelfLifeDays());
		goodsRequest.setQtpUnit(unitInfo.getName());
		goodsRequest.setCustassemCountry(country.getName());
		

		if (DERP_SYS.MERCHANDISEINFO_ISRECORD_1.equals(merchandiseInfo.getIsRecord())) {
			goodsRequest.setIsRecord(1); //1：是，2：否(金融)备案	
		}else {
			goodsRequest.setIsRecord(2);// 1：是，2：否(金融)备案	
		}	
		if (DERP_SYS.MERCHANDISEINFO_ISGROUP_1.equals(merchandiseInfo.getIsGroup())) {
			goodsRequest.setIsCombined(1);//是否组合(1-是，0-否)
		}else {
			goodsRequest.setIsCombined(0);//是否组合(1-是，0-否)
		}
		
		goodsRequest.setSourceCode("10");//物流企业  必填 卓志：10亚晋：20
		JSONObject fromObject = JSONObject.fromObject(goodsRequest);
		fromObject.put("method", "epass.add.goods.push");
		String result="";
		try {
			result = FinanceUtils.getFianceSysGoods(fromObject);
			LOGGER.info("----------向金融推送商品报文json:--------"+fromObject.toString());
			JSONObject jSONObject = JSONObject.fromObject(result);
			String status = jSONObject.getString("status");
			if (!"1".equals(status)) {
				Object notes = jSONObject.get("notes");
				if (notes!=null) {
					throw new RuntimeException(notes.toString());
				}else {
					throw new RuntimeException("推送接口失败");
				}
				
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		
		
		LOGGER.info("----------向金融推送商品档案--------结束");

		
	}


}
