package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.dao.system.*;
import com.topideal.entity.vo.system.*;
import com.topideal.mongo.dao.BrandParentMongoDao;
import com.topideal.mongo.entity.BrandParentMongo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xddf.usermodel.HasShapeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.reporting.BuFinanceSaleShelfDao;
import com.topideal.dao.reporting.SaleDataDao;
import com.topideal.dao.reporting.SaleTargetAchievementDao;
import com.topideal.dao.reporting.SaleTargetDao;
import com.topideal.entity.vo.reporting.BuFinanceSaleShelfModel;
import com.topideal.entity.vo.reporting.SaleDataModel;
import com.topideal.entity.vo.reporting.SaleTargetAchievementModel;
import com.topideal.entity.vo.reporting.SaleTargetModel;
import com.topideal.service.SaleTargetAchievementService;

import net.sf.json.JSONObject;

@Service
public class SaleTargetAchievementServiceImpl implements SaleTargetAchievementService{

	public static final Logger LOGGER = LoggerFactory.getLogger(SaleTargetAchievementServiceImpl.class);
	@Autowired
	private BuFinanceSaleShelfDao buFinanceSaleShelfDao ;
	@Autowired
	private BusinessUnitDao businessUnitDao ;
	@Autowired
	private SaleDataDao saleDataDao ;
	@Autowired
	private SaleTargetAchievementDao saleTargetAchievementDao ;
	@Autowired
	private MerchandiseInfoDao merchandiseInfoDao ;
	@Autowired
	private CommbarcodeDao commbarcodeDao ;
	@Autowired
	private SaleTargetDao saleTargetDao ;
	@Autowired
	private MerchantShopRelDao merchantShopRelDao ;
	@Autowired
	private CustomerInfoDao customerInfoDao;
	@Autowired
	private ExchangeRateDao exchangeRateDao;
	@Autowired
	private BrandParentDao brandParentDao;
	
	private static final String[] shopUnityIdArr = {"SK191119181600031", "SK190814110900014", "ZY0000134", "100046572"} ; 

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201502050, model = DERP_LOG_POINT.POINT_13201502050_Label)
	public void saveSummaryReport(String json, String keys, String topics, String tags) throws Exception {
		
		JSONObject jsonData = JSONObject.fromObject(json);
		Map<String, Object> jsonMap = jsonData;
		Integer buId = null ;
		
		 //查询所有事业部,若指定了事业部则只查本事业部
		BusinessUnitModel buModel = new BusinessUnitModel() ;
		
		if(jsonMap.get("buId") != null) {
			buId = (Integer) jsonMap.get("buId");
			buModel.setId(Long.valueOf(buId));
		}
		
		String months = (String) jsonMap.get("month");//月份
		
		//计算要刷新的月份
		if(StringUtils.isEmpty(months)){
			//若没有指定月份则取当前时间前一天日期近二个月月份,定时器刷新未关账的，本月、上月
			months = TimeUtils.getLastTwoMonthsByNow();
		}
		
		//循环事业部、月份生成报表
		List<BusinessUnitModel> buList = businessUnitDao.list(buModel);
		
		String[] montharr = months.split(",");
		
		for (String month : montharr) {
			Map<String, Object> delMap = new HashMap<String, Object>();
            delMap.put("month", month);
            
            if(buId != null) {
            	delMap.put("buId", buId);
            }
            
            saleDataDao.deleteByMap(delMap) ;
            saleTargetAchievementDao.deleteByMap(delMap) ;
            
            
            for (BusinessUnitModel buTempModel : buList) {
            	// 统计销售数据
            	saveSummarySaleData(month, buTempModel) ;
            	
            	// 计算销售达成率
            	saveGxSaleTargetAchievement(month, buTempModel) ;
            	
            	// 计算电商达成率
            	saveDsSaleTargetAchievement(month, buTempModel) ;
            	
            	// 计算店铺达成率
            	saveDpSaleTargetAchievement(month, buTempModel) ;
            }
		}
		
	}

	/**
	 * 计算电商店铺达成率
	 * @param month
	 * @param buTempModel
	 * @throws SQLException 
	 */
	private void saveDpSaleTargetAchievement(String month, BusinessUnitModel buTempModel) throws SQLException {
		
		/*
		 * 3.1查询特定店铺唯一ID对应店铺编码
		 */
		
		StringBuffer shopCodeSB = new StringBuffer() ;
		for (String shopUnityId : shopUnityIdArr) {
			MerchantShopRelModel relModel = new MerchantShopRelModel() ;
			relModel.setShopunifyId(shopUnityId);
			
			List<MerchantShopRelModel> shopList = merchantShopRelDao.list(relModel);
			
			for (MerchantShopRelModel merchantShopRelModel : shopList) {
				if(shopCodeSB.length() > 0) {
					shopCodeSB.append(",") ;
				}
				
				shopCodeSB.append(merchantShopRelModel.getShopCode()) ;
			}
		}
		
		/*
		 * 3.2取销售计划表有记录的店铺
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("buId", buTempModel.getId()) ;
		queryMap.put("month", month) ;
		queryMap.put("type", DERP_REPORT.SALE_TARGET_TYPE_3) ;
		
		List<String> shopCodeList = saleTargetDao.getHasShopData(queryMap) ;
		
		Map<String, Object> countMap = new HashMap<String, Object>() ;
		countMap.put("buId", buTempModel.getId()) ;
		countMap.put("month", month) ;
		
		for (String shopCode : shopCodeList) {
			//按标准条码统计集合
			Map<String, SaleTargetAchievementModel> achievementMap = new HashMap<String, SaleTargetAchievementModel>() ;
			
			SaleTargetModel querySaleTargetModel = new SaleTargetModel() ;
			querySaleTargetModel.setBuId(buTempModel.getId());
			querySaleTargetModel.setMonth(month);
			querySaleTargetModel.setType(DERP_REPORT.SALE_TARGET_TYPE_3);
			querySaleTargetModel.setShopCode(shopCode);
			
			List<SaleTargetModel> saleTargetList = saleTargetDao.list(querySaleTargetModel);
			
			for (SaleTargetModel saleTarget : saleTargetList) {
				SaleTargetAchievementModel tempAchievemet = achievementMap.get(saleTarget.getCommbarcode());
				
				if(tempAchievemet == null) {
					tempAchievemet = new SaleTargetAchievementModel () ;
					tempAchievemet.setCommbarcode(saleTarget.getCommbarcode());
					tempAchievemet.setBuId(saleTarget.getBuId());
					tempAchievemet.setBuName(saleTarget.getBuName());
					tempAchievemet.setMonth(saleTarget.getMonth());
					tempAchievemet.setShopCode(shopCode);
					tempAchievemet.setShopName(saleTarget.getShopName());
					tempAchievemet.setNum(0);
				}
				
				achievementMap.put(saleTarget.getCommbarcode(), tempAchievemet) ;
			}
			
			/*
			 * 3.3 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“电商订单”
			 */
			countMap.put("types", "3") ;
			
			if(!"social".equals(shopCode)) {
				countMap.put("shopCode", shopCode) ;
				countMap.put("notInshopCode", null) ;
			}else {
				countMap.put("shopCode", null) ;
				countMap.put("notInshopCode", shopCodeSB.toString()) ;
			}
			
			List<Map<String, Object>> countMapList = saleDataDao.countByMap(countMap);
			for (Map<String, Object> dsCountMap : countMapList) {
				String commbarcode = getResultMapVal(dsCountMap.get("commbarcode"), String.class) ;
				Long buId = getResultMapVal(dsCountMap.get("bu_id"), Long.class) ;
				String buName = getResultMapVal(dsCountMap.get("bu_name"), String.class) ;
				BigDecimal num = getResultMapVal(dsCountMap.get("num"), BigDecimal.class) ;
				String shopName = getResultMapVal(dsCountMap.get("shop_name"), String.class) ;
				
				if(num == null) {
					num = new BigDecimal(0) ;
				}
				
				SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
				
				if(tempAchievemet == null) {
					tempAchievemet = new SaleTargetAchievementModel () ;
					tempAchievemet.setCommbarcode(commbarcode);
					tempAchievemet.setBuId(buId);
					tempAchievemet.setBuName(buName);
					tempAchievemet.setMonth(month);
					tempAchievemet.setShopCode(shopCode);
					tempAchievemet.setShopName(shopName) ;
					
					if("social".equals(shopCode)) {
						tempAchievemet.setShopCode("social");
						tempAchievemet.setShopName("其他") ;
					}
				}
				
				tempAchievemet.setNum(num.intValue());
				
				achievementMap.put(commbarcode, tempAchievemet) ;
			}
			
			/*
			 * 3.4 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“电商订单退货”
			 *       减去退货量
			 */
			countMap.put("types", "5") ;
			List<Map<String, Object>> countTHMapList = saleDataDao.countByMap(countMap);
			for (Map<String, Object> dsCountMap : countTHMapList) {
				String commbarcode = getResultMapVal(dsCountMap.get("commbarcode"), String.class) ;
				Long buId = getResultMapVal(dsCountMap.get("bu_id"), Long.class) ;
				String buName = getResultMapVal(dsCountMap.get("bu_name"), String.class) ;
				BigDecimal num = getResultMapVal(dsCountMap.get("num"), BigDecimal.class) ;
				String shopName = getResultMapVal(dsCountMap.get("shop_name"), String.class) ;
				
				if(num == null) {
					num = new BigDecimal(0) ;
				}
				
				SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
				
				if(tempAchievemet == null) {
					tempAchievemet = new SaleTargetAchievementModel () ;
					tempAchievemet.setCommbarcode(commbarcode);
					tempAchievemet.setBuId(buId);
					tempAchievemet.setBuName(buName);
					tempAchievemet.setMonth(month);
					tempAchievemet.setShopName(shopName) ;
					tempAchievemet.setShopCode(shopCode);
					tempAchievemet.setNum(0);
					
					if("social".equals(shopCode)) {
						tempAchievemet.setShopCode("social");
						tempAchievemet.setShopName("其他") ;
					}
				}
				
				Integer tempNum = tempAchievemet.getNum();
				tempNum -= num.intValue() ;
				
				tempAchievemet.setNum(tempNum);
				
				achievementMap.put(commbarcode, tempAchievemet) ;
			}
			
			countMap = new HashMap<String, Object>() ;
			countMap.put("buId", buTempModel.getId()) ;
			countMap.put("month", month) ;
			countMap.put("shopCode", shopCode) ;
			for (String commbarcode : achievementMap.keySet()) {
				
				//以事业部+标准条码的维度汇总销售目标表中的店铺计划量
				countMap.put("commbarcode", commbarcode) ;
				countMap.put("type", "3") ;
				
				Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
				
				if(saleTargetMap == null) {
					continue ;
				}
				
				BigDecimal shopNum = getResultMapVal(saleTargetMap.get("shop_num"), BigDecimal.class) ;
				
				SaleTargetAchievementModel saleTargetAchievementModel = achievementMap.get(commbarcode);
				
				//计算达成率
				Integer num = saleTargetAchievementModel.getNum();
				
				if(num == null) {
					num = 0 ;
				}
				
				Double rate = 0.0 ;
				
				if(shopNum.compareTo(BigDecimal.ZERO) != 0) {
					rate = new BigDecimal(num).divide(shopNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				
				CommbarcodeModel queryModel = new CommbarcodeModel() ;
				queryModel.setCommbarcode(commbarcode); 
				queryModel = commbarcodeDao.searchByModel(queryModel) ;
				
				if(queryModel == null) {
					LOGGER.error("标准条码：" + commbarcode + " 查询标准条码表不存在");
					continue ;
				}
				
				
				saleTargetAchievementModel.setNum(num);
				saleTargetAchievementModel.setRate(rate);
				saleTargetAchievementModel.setType(DERP_REPORT.SALE_TARGET_TYPE_3);
				saleTargetAchievementModel.setBrandParent(queryModel.getCommBrandParentName());
				saleTargetAchievementModel.setGoodsName(queryModel.getCommGoodsName());
				saleTargetAchievementModel.setCreateDate(TimeUtils.getNow());
				
				saleTargetAchievementDao.save(saleTargetAchievementModel) ;
			}
		}
		
	}

	/**
	 * 计算电商平台达成率
	 * @param month
	 * @param buTempModel
	 * @throws SQLException 
	 */
	private void saveDsSaleTargetAchievement(String month, BusinessUnitModel buTempModel) throws SQLException {
		
		/*
		 * 2.1取销售计划表有记录的电商平台
		 */
		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("buId", buTempModel.getId()) ;
		queryMap.put("month", month) ;
		queryMap.put("type", DERP_REPORT.SALE_TARGET_TYPE_2) ;
		
		List<String> platformList = saleTargetDao.getHasPlatformData(queryMap) ;
		
		Map<String, Object> countMap = new HashMap<String, Object>() ;
		countMap.put("buId", buTempModel.getId()) ;
		countMap.put("month", month) ;
		
		for (String platformCode : platformList) {
			//按标准条码统计集合
			Map<String, SaleTargetAchievementModel> achievementMap = new HashMap<String, SaleTargetAchievementModel>() ;
			
			SaleTargetModel querySaleTargetModel = new SaleTargetModel() ;
			querySaleTargetModel.setBuId(buTempModel.getId());
			querySaleTargetModel.setMonth(month);
			querySaleTargetModel.setType(DERP_REPORT.SALE_TARGET_TYPE_2);
			querySaleTargetModel.setStorePlatformName(platformCode);
			
			List<SaleTargetModel> saleTargetList = saleTargetDao.list(querySaleTargetModel);
			
			for (SaleTargetModel saleTarget : saleTargetList) {
				SaleTargetAchievementModel tempAchievemet = achievementMap.get(saleTarget.getCommbarcode());
				
				if(tempAchievemet == null) {
					tempAchievemet = new SaleTargetAchievementModel () ;
					tempAchievemet.setCommbarcode(saleTarget.getCommbarcode());
					tempAchievemet.setBuId(saleTarget.getBuId());
					tempAchievemet.setBuName(saleTarget.getBuName());
					tempAchievemet.setMonth(saleTarget.getMonth());
					tempAchievemet.setStorePlatformName(platformCode);
					tempAchievemet.setNum(0);
				}
				
				achievementMap.put(saleTarget.getCommbarcode(), tempAchievemet) ;
			}
			
			/*
			 * 2.2 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“电商订单”
			 */
			countMap.put("types", "3") ;
			countMap.put("storePlatformCode", platformCode) ;
			
			List<Map<String, Object>> countMapList = saleDataDao.countByMap(countMap);
			for (Map<String, Object> dsCountMap : countMapList) {
				String commbarcode = getResultMapVal(dsCountMap.get("commbarcode"), String.class) ;
				Long buId = getResultMapVal(dsCountMap.get("bu_id"), Long.class) ;
				String buName = getResultMapVal(dsCountMap.get("bu_name"), String.class) ;
				BigDecimal num = getResultMapVal(dsCountMap.get("num"), BigDecimal.class) ;
				
				if(num == null) {
					num = new BigDecimal(0) ;
				}
				
				SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
				
				if(tempAchievemet == null) {
					tempAchievemet = new SaleTargetAchievementModel () ;
					tempAchievemet.setCommbarcode(commbarcode);
					tempAchievemet.setBuId(buId);
					tempAchievemet.setBuName(buName);
					tempAchievemet.setMonth(month);
					tempAchievemet.setStorePlatformName(platformCode);
				}
				
				tempAchievemet.setNum(num.intValue());
				
				achievementMap.put(commbarcode, tempAchievemet) ;
			}
			
			/*
			 * 2.3 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“电商订单退货”
			 *       减去退货量
			 */
			countMap.put("types", "5") ;
			List<Map<String, Object>> countTHMapList = saleDataDao.countByMap(countMap);
			for (Map<String, Object> dsCountMap : countTHMapList) {
				String commbarcode = getResultMapVal(dsCountMap.get("commbarcode"), String.class) ;
				Long buId = getResultMapVal(dsCountMap.get("bu_id"), Long.class) ;
				String buName = getResultMapVal(dsCountMap.get("bu_name"), String.class) ;
				BigDecimal num = getResultMapVal(dsCountMap.get("num"), BigDecimal.class) ;
				
				if(num == null) {
					num = new BigDecimal(0) ;
				}
				
				SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
				
				if(tempAchievemet == null) {
					tempAchievemet = new SaleTargetAchievementModel () ;
					tempAchievemet.setCommbarcode(commbarcode);
					tempAchievemet.setBuId(buId);
					tempAchievemet.setBuName(buName);
					tempAchievemet.setMonth(month);
					tempAchievemet.setStorePlatformName(platformCode);
					tempAchievemet.setNum(0);
				}
				
				Integer tempNum = tempAchievemet.getNum();
				tempNum -= num.intValue() ;
				
				tempAchievemet.setNum(tempNum);
				
				achievementMap.put(commbarcode, tempAchievemet) ;
			}
			
			countMap = new HashMap<String, Object>() ;
			countMap.put("buId", buTempModel.getId()) ;
			countMap.put("month", month) ;
			countMap.put("storePlatformName", platformCode) ;
			for (String commbarcode : achievementMap.keySet()) {
				
				//以事业部+标准条码的维度汇总销售目标表中的电商计划量
				countMap.put("commbarcode", commbarcode) ;
				countMap.put("type", "2") ;
				
				Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
				
				if(saleTargetMap == null) {
					continue ;
				}
				
				BigDecimal storePlatformNum = getResultMapVal(saleTargetMap.get("store_platform_num"), BigDecimal.class) ;
				
				SaleTargetAchievementModel saleTargetAchievementModel = achievementMap.get(commbarcode);
				
				//计算达成率
				Integer num = saleTargetAchievementModel.getNum();
				
				if(num == null) {
					num = 0 ;
				}
				
				Double rate = 0.0 ;
				
				if(storePlatformNum.compareTo(BigDecimal.ZERO) != 0) {
					rate = new BigDecimal(num).divide(storePlatformNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				
				CommbarcodeModel queryModel = new CommbarcodeModel() ;
				queryModel.setCommbarcode(commbarcode); 
				queryModel = commbarcodeDao.searchByModel(queryModel) ;
				
				if(queryModel == null) {
					LOGGER.error("标准条码：" + commbarcode + " 查询标准条码表不存在");
					continue ;
				}
				
				
				saleTargetAchievementModel.setNum(num);
				saleTargetAchievementModel.setRate(rate);
				saleTargetAchievementModel.setType(DERP_REPORT.SALE_TARGET_TYPE_2);
				saleTargetAchievementModel.setBrandParent(queryModel.getCommBrandParentName());
				saleTargetAchievementModel.setGoodsName(queryModel.getCommGoodsName());
				saleTargetAchievementModel.setCreateDate(TimeUtils.getNow());
				
				saleTargetAchievementDao.save(saleTargetAchievementModel) ;
			}
		}
	}
	
	/**
	 * 计算销售达成率
	 * @param month
	 * @param buTempModel
	 * @throws SQLException 
	 */
	private void saveGxSaleTargetAchievement(String month, BusinessUnitModel buTempModel) throws SQLException {
		
		//按标准条码统计集合
		Map<String, SaleTargetAchievementModel> achievementMap = new HashMap<String, SaleTargetAchievementModel>() ;
		
		//查询所有销售类型计划
		SaleTargetModel querySaleTargetModel = new SaleTargetModel() ;
		querySaleTargetModel.setBuId(buTempModel.getId());
		querySaleTargetModel.setMonth(month);
		querySaleTargetModel.setType(DERP_REPORT.SALE_TARGET_TYPE_1);
		
		List<SaleTargetModel> saleTargetList = saleTargetDao.list(querySaleTargetModel);
		
		for (SaleTargetModel saleTarget : saleTargetList) {
			SaleTargetAchievementModel tempAchievemet = achievementMap.get(saleTarget.getCommbarcode());
			
			if(tempAchievemet == null) {
				tempAchievemet = new SaleTargetAchievementModel () ;
				tempAchievemet.setCommbarcode(saleTarget.getCommbarcode());
				tempAchievemet.setBuId(saleTarget.getBuId());
				tempAchievemet.setBuName(saleTarget.getBuName());
				tempAchievemet.setMonth(saleTarget.getMonth());
				tempAchievemet.setToBNum(0);
				tempAchievemet.setToCNum(0);
			}
			
			achievementMap.put(saleTarget.getCommbarcode(), tempAchievemet) ;
		}
		
		/**
		 * To B销量/完成率
		 */
		
		/*
		 * 1.1 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“购销-实销实结、购销-整批结算
		 */
		
		Map<String, Object> countMap = new HashMap<String, Object>() ;
		countMap.put("buId", buTempModel.getId()) ;
		countMap.put("month", month) ;
		countMap.put("types", "9,10") ;
		List<Map<String, Object>> saleGXCountMapList = saleDataDao.countByMap(countMap) ;
		
		for (Map<String, Object> saleDataCountMap : saleGXCountMapList) {
			String commbarcode = getResultMapVal(saleDataCountMap.get("commbarcode"), String.class) ;
			Long buId = getResultMapVal(saleDataCountMap.get("bu_id"), Long.class) ;
			String buName = getResultMapVal(saleDataCountMap.get("bu_name"), String.class) ;
			BigDecimal num = getResultMapVal(saleDataCountMap.get("num"), BigDecimal.class) ;
			
			if(num == null) {
				num = new BigDecimal(0) ;
			}
			
			SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
			
			if(tempAchievemet == null) {
				tempAchievemet = new SaleTargetAchievementModel () ;
				tempAchievemet.setCommbarcode(commbarcode);
				tempAchievemet.setBuId(buId);
				tempAchievemet.setBuName(buName);
				tempAchievemet.setMonth(month);
			}
			
			tempAchievemet.setToBNum(num.intValue());
			
			achievementMap.put(commbarcode, tempAchievemet) ;
		}
		
		/*
		 * 1.2 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“销售退货入库
		 */
		countMap.put("types", "6") ;
		List<Map<String, Object>> saleGXTHCountMapList = saleDataDao.countByMap(countMap) ;
		for (Map<String, Object> saleDataCountMap : saleGXTHCountMapList) {
			String commbarcode = getResultMapVal(saleDataCountMap.get("commbarcode"), String.class) ;
			Long buId = getResultMapVal(saleDataCountMap.get("bu_id"), Long.class) ;
			String buName = getResultMapVal(saleDataCountMap.get("bu_name"), String.class) ;
			BigDecimal num = getResultMapVal(saleDataCountMap.get("num"), BigDecimal.class) ;
			
			if(num == null) {
				num = new BigDecimal(0) ;
			}
			
			SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
			
			if(tempAchievemet == null) {
				tempAchievemet = new SaleTargetAchievementModel () ;
				tempAchievemet.setCommbarcode(commbarcode);
				tempAchievemet.setBuId(buId);
				tempAchievemet.setBuName(buName);
				tempAchievemet.setMonth(month);
			}
			
			Integer toBNum = tempAchievemet.getToBNum();
			if(toBNum == null) {
				toBNum = 0 ;
			}
			toBNum -= num.intValue() ;
			
			tempAchievemet.setToBNum(toBNum);
			
			achievementMap.put(commbarcode, tempAchievemet) ;
		}
		
		/*
		 * 1.3 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“电商出库量、账单出库
		 */
		countMap.put("types", "3,7") ;
		List<Map<String, Object>> saleDSCountMapList = saleDataDao.countByMap(countMap) ;
		for (Map<String, Object> saleDataCountMap : saleDSCountMapList) {
			String commbarcode = getResultMapVal(saleDataCountMap.get("commbarcode"), String.class) ;
			Long buId = getResultMapVal(saleDataCountMap.get("bu_id"), Long.class) ;
			String buName = getResultMapVal(saleDataCountMap.get("bu_name"), String.class) ;
			BigDecimal num = getResultMapVal(saleDataCountMap.get("num"), BigDecimal.class) ;
			
			if(num == null) {
				num = new BigDecimal(0) ;
			}
			
			SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
			
			if(tempAchievemet == null) {
				tempAchievemet = new SaleTargetAchievementModel () ;
				tempAchievemet.setCommbarcode(commbarcode);
				tempAchievemet.setBuId(buId);
				tempAchievemet.setBuName(buName);
				tempAchievemet.setMonth(month);
			}
			
			tempAchievemet.setToCNum(num.intValue());
			
			achievementMap.put(commbarcode, tempAchievemet) ;
		}
		
		/*
		 * 1.4 以事业部+标准条码的维度汇总“销售数据表”中单据类型为“退货入库量、账单入库量
		 */
		countMap.put("types", "5,8") ;
		List<Map<String, Object>> saleDSTHCountMapList = saleDataDao.countByMap(countMap) ;
		for (Map<String, Object> saleDataCountMap : saleDSTHCountMapList) {
			String commbarcode = getResultMapVal(saleDataCountMap.get("commbarcode"), String.class) ;
			Long buId = getResultMapVal(saleDataCountMap.get("bu_id"), Long.class) ;
			String buName = getResultMapVal(saleDataCountMap.get("bu_name"), String.class) ;
			BigDecimal num = getResultMapVal(saleDataCountMap.get("num"), BigDecimal.class) ;
			
			if(num == null) {
				num = new BigDecimal(0) ;
			}
			
			SaleTargetAchievementModel tempAchievemet = achievementMap.get(commbarcode);
			
			if(tempAchievemet == null) {
				tempAchievemet = new SaleTargetAchievementModel () ;
				tempAchievemet.setCommbarcode(commbarcode);
				tempAchievemet.setBuId(buId);
				tempAchievemet.setBuName(buName);
				tempAchievemet.setMonth(month);
			}
			
			Integer toCNum = tempAchievemet.getToCNum();
			if(toCNum == null) {
				toCNum = 0 ;
			}
			toCNum -= num.intValue() ;
			
			tempAchievemet.setToCNum(toCNum);
			
			achievementMap.put(commbarcode, tempAchievemet) ;
		}
		
		/*
		 * 1.5 以事业部+标准条码的维度汇总集合
		 */
		countMap = new HashMap<String, Object>() ;
		countMap.put("buId", buTempModel.getId()) ;
		countMap.put("month", month) ;
		for (String commbarcode : achievementMap.keySet()) {
			
			//以事业部+标准条码的维度汇总销售目标表中的购销计划量
			countMap.put("commbarcode", commbarcode) ;
			countMap.put("type", "1") ;
			
			Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
			
			if(saleTargetMap == null) {
				continue ;
			}
			
			BigDecimal toBTargetNum = getResultMapVal(saleTargetMap.get("to_b_num"), BigDecimal.class) ;
			BigDecimal toCTargetNum = getResultMapVal(saleTargetMap.get("to_c_num"), BigDecimal.class) ;
			
			SaleTargetAchievementModel saleTargetAchievementModel = achievementMap.get(commbarcode);
			
			//计算达成率
			Integer toBNum = saleTargetAchievementModel.getToBNum();
			Integer toCNum = saleTargetAchievementModel.getToCNum();
			
			if(toBNum == null) {
				toBNum = 0 ;
			}
			
			if(toCNum == null) {
				toCNum = 0 ;
			}
			
			Integer total = toBNum + toCNum ;
			BigDecimal totalTargetNum = toBTargetNum.add(toCTargetNum);
			
			Double toBRate = 0.0 ;
			Double toCRate = 0.0 ;
			Double rate = 0.0 ;
			
			if(toBTargetNum.compareTo(BigDecimal.ZERO) != 0) {
				toBRate = new BigDecimal(toBNum).divide(toBTargetNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			
			if(toCTargetNum.compareTo(BigDecimal.ZERO) != 0) {
				toCRate = new BigDecimal(toCNum).divide(toCTargetNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			
			if(totalTargetNum.compareTo(BigDecimal.ZERO) != 0) {
				rate = new BigDecimal(total).divide(totalTargetNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
				
			CommbarcodeModel queryModel = new CommbarcodeModel() ;
			queryModel.setCommbarcode(commbarcode); 
			queryModel = commbarcodeDao.searchByModel(queryModel) ;
			
			if(queryModel == null) {
				LOGGER.error("标准条码：" + commbarcode + " 查询标准条码表不存在");
				continue ;
			}
			
			saleTargetAchievementModel.setToBRate(toBRate);
			saleTargetAchievementModel.setToCRate(toCRate);
			saleTargetAchievementModel.setNum(total);
			saleTargetAchievementModel.setRate(rate);
			saleTargetAchievementModel.setType(DERP_REPORT.SALE_TARGET_TYPE_1);
			saleTargetAchievementModel.setBrandParent(queryModel.getCommBrandParentName());
			saleTargetAchievementModel.setGoodsName(queryModel.getCommGoodsName());
			saleTargetAchievementModel.setCreateDate(TimeUtils.getNow());
			
			saleTargetAchievementDao.save(saleTargetAchievementModel) ;
		}
	}

	/**
	 * 按月份+公司+事业部+商品货号+标准品牌+单据类型+客户名称+平台名称+店铺编码+仓库名称
	 * 
	 * 统计业务经销存销售已上架表销售数据
	 * @param month
	 * @param buTempModel
	 * @throws SQLException 
	 */
	private void saveSummarySaleData(String month, BusinessUnitModel buTempModel) throws SQLException {
		
		String lastDateStr = TimeUtils.getLastDayOfMonth(month) ;
		Timestamp lastDate = TimeUtils.parse(lastDateStr, "yyyy-MM-dd");
		List<Date> dateList = TimeUtils.getAllTheDateOftheMonth(lastDate);
		
		List<SaleDataModel> saleDataModelList = new ArrayList<SaleDataModel>() ;

		Map<Long, BrandParentModel> brandParentModelMap = new HashMap<>();
		Map<String, ExchangeRateModel> cnyRateMap = new HashMap<>();
		Map<String, ExchangeRateModel> hkRateMap = new HashMap<>();

		Map<String, Object> queryMap = new HashMap<String, Object>() ;
		queryMap.put("month", month) ;
		queryMap.put("buId", buTempModel.getId()) ;

		//事业部
		BusinessUnitModel businessUnitModel = businessUnitDao.searchById(buTempModel.getId());

		for (Date reportDate : dateList) {
			queryMap.put("reportDate", TimeUtils.format(reportDate, "yyyy-MM-dd")) ;
			
			List<BuFinanceSaleShelfModel> saleDataCountList = buFinanceSaleShelfDao.getSaleDataCountList(queryMap) ;
			
			for (BuFinanceSaleShelfModel buFinanceSaleShelfModel : saleDataCountList) {
				//根据客户id查询客户档案中是否为内部公司字段，若客户为内部公司，则不统计
				Long customerId = buFinanceSaleShelfModel.getCustomerId();
				CustomerInfoModel customerInfoModel = null;
				if (customerId != null) {
					customerInfoModel = customerInfoDao.searchById(customerId);
					// 内部客户数据也要存
					/*if (customerInfoModel != null && DERP_SYS.CUSTOMERINFO_TYPE_1.equals(customerInfoModel.getType())) {
						continue;
					}*/
					
				}
				if (customerInfoModel == null ) customerInfoModel=new  CustomerInfoModel();

				MerchandiseInfoModel queryModel = new MerchandiseInfoModel() ;
				queryModel.setGoodsNo(buFinanceSaleShelfModel.getGoodsNo());
				queryModel.setMerchantId(buFinanceSaleShelfModel.getMerchantId());
				
				queryModel = merchandiseInfoDao.searchByModel(queryModel) ;
				
				if(queryModel == null) {
					LOGGER.error("货号：" + buFinanceSaleShelfModel.getGoodsNo() + "在商家" + buFinanceSaleShelfModel.getMerchantName() + "主体下不存在");
					continue ;
				}

				SaleDataModel saleDataModel = new SaleDataModel() ;

				BrandParentModel brandParent = brandParentModelMap.get(queryModel.getId());
				if (brandParent == null) {
					brandParent = brandParentDao.getBrandParentByGoodsId(queryModel.getId());
				}

				if (brandParent != null) {
					brandParentModelMap.put(queryModel.getId(), brandParent);
					saleDataModel.setBrandParent(brandParent.getName());
					saleDataModel.setBrandParentId(brandParent.getId());
					saleDataModel.setParentBrandId(brandParent.getSuperiorParentBrandId());
					saleDataModel.setParentBrandCode(brandParent.getSuperiorParentBrandCode());
					saleDataModel.setParentBrandName(brandParent.getSuperiorParentBrand());
				}

				saleDataModel.setBuId(buFinanceSaleShelfModel.getBuId());
				saleDataModel.setBuName(buFinanceSaleShelfModel.getBuName());
				saleDataModel.setDepartmentId(businessUnitModel.getDepartmentId());
				saleDataModel.setDepartmentName(businessUnitModel.getDepartmentName());
				saleDataModel.setCommbarcode(buFinanceSaleShelfModel.getCommbarcode());
				saleDataModel.setGoodsNo(buFinanceSaleShelfModel.getGoodsNo());
				saleDataModel.setMerchantId(buFinanceSaleShelfModel.getMerchantId());
				saleDataModel.setMerchantName(buFinanceSaleShelfModel.getMerchantName()) ;
				saleDataModel.setMonth(buFinanceSaleShelfModel.getMonth());
				saleDataModel.setNum(buFinanceSaleShelfModel.getNum());
				saleDataModel.setOutDepotId(buFinanceSaleShelfModel.getOutDepotId());
				saleDataModel.setOutDepotName(buFinanceSaleShelfModel.getOutDepotName());
				saleDataModel.setShopCode(buFinanceSaleShelfModel.getShopCode());
				saleDataModel.setShopName(buFinanceSaleShelfModel.getShopName());
				saleDataModel.setStorePlatformCode(buFinanceSaleShelfModel.getStorePlatformCode());
				saleDataModel.setStorePlatformName(buFinanceSaleShelfModel.getStorePlatformName());
				saleDataModel.setType(buFinanceSaleShelfModel.getOrderType());

				saleDataModel.setCreateDate(TimeUtils.getNow());
				saleDataModel.setSaleCurrency(buFinanceSaleShelfModel.getSaleCurrency());
				saleDataModel.setSaleAmount(buFinanceSaleShelfModel.getSaleAmount());
				saleDataModel.setChannelType(buFinanceSaleShelfModel.getChannelType());

				// 商家id和客户的商家id相同 为内部公司
				if (buFinanceSaleShelfModel.getMerchantId().equals(customerInfoModel.getInnerMerchantId())) {
					saleDataModel.setInnerMerchantType(DERP_REPORT.SALE_DATA_INNERMERCHANTTYPE_1); //客户是否为内部公司
				}else {
					saleDataModel.setInnerMerchantType(DERP_REPORT.SALE_DATA_INNERMERCHANTTYPE_0); //客户是否为内部公司
				}
				
				saleDataModel.setReportDate(new java.sql.Date(reportDate.getTime())) ;
				saleDataModel.setCustomerType(customerInfoModel.getType());
				saleDataModel.setCustomerId(customerInfoModel.getId());
				saleDataModel.setCustomerName(customerInfoModel.getName());
				
				
				//在汇率列表中取该月份的最新一条销售币种兑换人民币的平均汇率，计算人民币的销售金额，公式=销售金额*兑人民币平均汇率，保利2位小数
				String saleCurrency = buFinanceSaleShelfModel.getSaleCurrency();
				if (DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY.equals(saleCurrency)) {
					saleDataModel.setCnyRate(1.0);
					saleDataModel.setCnyAmount(buFinanceSaleShelfModel.getSaleAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				} else {
					ExchangeRateModel rate = cnyRateMap.get(saleCurrency);
					if (rate == null) {
						rate = exchangeRateDao.getLatestRate(buFinanceSaleShelfModel.getMonth(), saleCurrency, DERP_SYS.EXCHANGERATE_CURRENCYCODE_CNY);
					}

					if (rate != null) {
						cnyRateMap.put(saleCurrency, rate);
						saleDataModel.setCnyRate(rate.getAvgRate());
						BigDecimal saleAmount = buFinanceSaleShelfModel.getSaleAmount();
						if (saleAmount==null)saleAmount=new BigDecimal(0);
						BigDecimal cnyAmount = saleAmount.multiply(new BigDecimal(rate.getAvgRate().toString()));
						saleDataModel.setCnyAmount(cnyAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}

				//在汇率列表中取该月份的最新一条销售币种兑换港币的平均汇率，计算港币的销售金额，公式=销售金额*兑港币平均汇率，保利2位小数
				if (DERP_SYS.EXCHANGERATE_CURRENCYCODE_HKD.equals(saleCurrency)) {
					saleDataModel.setHkRate(1.0);
					saleDataModel.setHkAmount(buFinanceSaleShelfModel.getSaleAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
				} else {
					ExchangeRateModel rate = hkRateMap.get(saleCurrency);
					if (rate == null) {
						rate = exchangeRateDao.getLatestRate(buFinanceSaleShelfModel.getMonth(), saleCurrency, DERP_SYS.EXCHANGERATE_CURRENCYCODE_HKD);
					}

					if (rate != null) {
						hkRateMap.put(saleCurrency, rate);
						saleDataModel.setHkRate(rate.getAvgRate());
						BigDecimal saleAmount = buFinanceSaleShelfModel.getSaleAmount();
						if (saleAmount==null)saleAmount=new BigDecimal(0);
						BigDecimal hkAmount = saleAmount.multiply(new BigDecimal(rate.getAvgRate().toString()));
						saleDataModel.setHkAmount(hkAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}
				/**根据以下规则确定销售类型，
				 A：若单据类型为购销实销实结/销售出库购销订单/购销退货，则销售类型为购销B；
				 B：若单据类型为购销整批结算，则销售类型为购销A，
				 C：若单据类型为电商订单、电商订单退货、库位调整单调增、库位调整单调减，则根据店铺编码在店铺档案中查询对应的运营类型（一件代发/POP）
				 D：其他为代销
				 E：若单据类型为采销执行，则销售类型为采销执行，*/
				if (DERP_REPORT.FINANCESALESHELF_ORDERTYPE_10.equals(buFinanceSaleShelfModel.getOrderType())
					|| DERP_REPORT.FINANCESALESHELF_ORDERTYPE_2.equals(buFinanceSaleShelfModel.getOrderType())
					|| DERP_REPORT.FINANCESALESHELF_ORDERTYPE_6.equals(buFinanceSaleShelfModel.getOrderType())) {
					
					saleDataModel.setOrderType(DERP_REPORT.SALE_DATA_ORDERTYPE_2);
				
				} else if (DERP_REPORT.FINANCESALESHELF_ORDERTYPE_9.equals(buFinanceSaleShelfModel.getOrderType())) {
					
					saleDataModel.setOrderType(DERP_REPORT.SALE_DATA_ORDERTYPE_1);
				
				} else if (DERP_REPORT.FINANCESALESHELF_ORDERTYPE_3.equals(buFinanceSaleShelfModel.getOrderType())
					|| DERP_REPORT.FINANCESALESHELF_ORDERTYPE_5.equals(buFinanceSaleShelfModel.getOrderType())
					|| DERP_REPORT.FINANCESALESHELF_ORDERTYPE_14.equals(buFinanceSaleShelfModel.getOrderType())
						) {
					
					String shopCode = buFinanceSaleShelfModel.getShopCode();
					MerchantShopRelModel shopRelModel = new MerchantShopRelModel();
					shopRelModel.setShopCode(shopCode);
					MerchantShopRelModel merchantShopRelModel = merchantShopRelDao.searchByModel(shopRelModel);
					
					if (merchantShopRelModel != null && DERP_SYS.MERCHANTSHOPREL_SHOPTYPE_001.equals(merchantShopRelModel.getShopTypeCode())) {
						
						saleDataModel.setOrderType(DERP_REPORT.SALE_DATA_ORDERTYPE_4);
						
					} else {
						
						saleDataModel.setOrderType(DERP_REPORT.SALE_DATA_ORDERTYPE_3);
						
					}
					
				} else if(DERP_REPORT.FINANCESALESHELF_ORDERTYPE_13.equals(buFinanceSaleShelfModel.getOrderType())){// 目前无采购执行了
					
					saleDataModel.setOrderType(DERP_REPORT.SALE_DATA_ORDERTYPE_6);
					
				}else {
					saleDataModel.setOrderType(DERP_REPORT.SALE_DATA_ORDERTYPE_5);
				}
				
				saleDataModelList.add(saleDataModel) ;
			}
		}
		
		int pageLimit = 1000 ;
		int total = saleDataModelList.size() ;
		int pageNum = total / pageLimit ;

		if(total % pageLimit != 0) {
			pageNum += 1 ;
		}

		for(int page = 1 ; page <= pageNum ; page ++) {

			int start = (page - 1) * pageLimit ;
			int end = page * pageLimit ;

			if(end > total) {
				end = total ;
			}

			List<SaleDataModel> subList = saleDataModelList.subList(start, end);

			saleDataDao.batchSave(subList) ;
		}
	}
	
	/**
	 * 判断传入对象是否为空，为空直接返回null，否则将对象转成指定类型返回
	 * @param org
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T>T getResultMapVal(Object org, Class<T> cls) {
		if(org == null ) {
			return null ;
		}
		
		return (T) org ;
	}
}
