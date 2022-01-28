package com.topideal.report.service.reporting.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.auth.User;
import com.topideal.dao.reporting.SaleTargetAchievementDao;
import com.topideal.dao.reporting.SaleTargetDao;
import com.topideal.dao.system.BusinessUnitDao;
import com.topideal.entity.dto.SaleTargetAchievementDTO;
import com.topideal.entity.vo.system.BusinessUnitModel;
import com.topideal.mongo.dao.UserBuRelMongoDao;
import com.topideal.report.service.reporting.SaleTargetAchievementService;
import com.topideal.report.shiro.ShiroUtils;

@Service
public class SaleTargetAchievementServiceImpl implements SaleTargetAchievementService{

	@Autowired
	BusinessUnitDao businessUnitDao ;
	@Autowired
	SaleTargetAchievementDao saleTargetAchievementDao ;
	@Autowired
	SaleTargetDao saleTargetDao ;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	
	@Override
	public List<BusinessUnitModel> getAllBuUnit() throws SQLException {
		return businessUnitDao.list(new BusinessUnitModel());
	}

	@SuppressWarnings("unchecked")
	@Override
	public SaleTargetAchievementDTO getListByPage(User user,SaleTargetAchievementDTO dto) {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);
				return dto;
			}
			
			dto.setBuIds(buIds);
		}
		
		List<SaleTargetAchievementDTO> listDto = saleTargetAchievementDao.listGroupDto(dto) ;
		Integer total = saleTargetAchievementDao.countGroupDto(dto) ;
		
		if(DERP_REPORT.SALE_TARGET_TYPE_1.equals(dto.getType())) {
			
			String month = "" ;
			if(StringUtils.isNotBlank(dto.getStartMonth())
					&& StringUtils.isNotBlank(dto.getEndMonth())) {
				month = dto.getStartMonth() + "至" + dto.getEndMonth() ;
			}else{
				month = dto.getMonth() ;
			}
			
			for (SaleTargetAchievementDTO saleTargetAchievementDTO : listDto) {
				saleTargetAchievementDTO.setMonth(month);
				
				if(StringUtils.isNotBlank(dto.getStartMonth())
						&& StringUtils.isNotBlank(dto.getEndMonth())) {
					Map<String, Object> countMap = new HashMap<String, Object>() ;
					countMap.put("buId", saleTargetAchievementDTO.getBuId()) ;
					countMap.put("commbarcode", saleTargetAchievementDTO.getCommbarcode()) ;
					countMap.put("type", "1") ;
					countMap.put("startMonth", dto.getStartMonth()) ;
					countMap.put("endMonth", dto.getEndMonth()) ;
					Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
					
					if(saleTargetMap != null) {
						BigDecimal toBTargetNum = getResultMapVal(saleTargetMap.get("to_b_num"), BigDecimal.class) ;
						BigDecimal toCTargetNum = getResultMapVal(saleTargetMap.get("to_c_num"), BigDecimal.class) ;
						BigDecimal totalNum = toBTargetNum.add(toCTargetNum) ;
						
						BigDecimal toBBD = new BigDecimal(saleTargetAchievementDTO.getToBNum());
						BigDecimal toCBD = new BigDecimal(saleTargetAchievementDTO.getToCNum());
						BigDecimal numBD = new BigDecimal(saleTargetAchievementDTO.getNum());
						
						if(toBTargetNum.compareTo(BigDecimal.ZERO) != 0) {
							dto.setToBRate(toBBD.divide(toBTargetNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue());
						}else {
							dto.setToBRate(0.00);
						}
						
						if(toCTargetNum.compareTo(BigDecimal.ZERO) != 0) {
							dto.setToCRate(toCBD.divide(toCTargetNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue());
						}else {
							dto.setToCRate(0.00);
						}
						
						if(totalNum.compareTo(BigDecimal.ZERO) != 0) {
							dto.setRate(numBD.divide(totalNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue());
						}else {
							dto.setRate(0.00);
						}
						
					}
				}
				
			}
			
			dto.setList(listDto);
			dto.setTotal(total);
			
		}else if(DERP_REPORT.SALE_TARGET_TYPE_2.equals(dto.getType())) {
			
			List<String> exsitplatform = saleTargetAchievementDao.getExsitplatform();
			Map<String, Map<String, Object>> resultCommbarMap = new LinkedHashMap<String, Map<String, Object>>() ;
			
			for (SaleTargetAchievementDTO saleTargetAchievementDTO : listDto) {
				
				Map<String, Object> countMap = new HashMap<String, Object>() ;
				countMap.put("buId", saleTargetAchievementDTO.getBuId()) ;
				countMap.put("commbarcode", saleTargetAchievementDTO.getCommbarcode()) ;
				countMap.put("type", "2") ;
				countMap.put("storePlatformName", saleTargetAchievementDTO.getStorePlatformName()) ;
				
				String month = "" ;
				if(StringUtils.isNotBlank(dto.getStartMonth())
						&& StringUtils.isNotBlank(dto.getEndMonth())) {
					
					countMap.put("startMonth", dto.getStartMonth()) ;
					countMap.put("endMonth", dto.getEndMonth()) ;
					
					month = dto.getStartMonth() + "至" + dto.getEndMonth() ;
				}else{
					
					countMap.put("month", dto.getMonth()) ;
					
					month = dto.getMonth() ;
				}
				saleTargetAchievementDTO.setMonth(month);
				
				Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
				
				String commbarcode = saleTargetAchievementDTO.getCommbarcode();	
				String buName = saleTargetAchievementDTO.getBuName() ;
				
				String key = commbarcode + "__" + buName + "__" + month;
				
				Map<String, Object> goodsMap = resultCommbarMap.get(key);
				
				if(goodsMap == null) {
					goodsMap = new HashMap<String, Object>() ;
					
					goodsMap.put("buName", buName) ;
					goodsMap.put("commbarcode", commbarcode) ;
					goodsMap.put("month", month) ;
					goodsMap.put("goodsName", saleTargetAchievementDTO.getGoodsName()) ;
					goodsMap.put("brandParent", saleTargetAchievementDTO.getBrandParent()) ;
					goodsMap.put("createDate", saleTargetAchievementDTO.getCreateDate()) ;
				}
				
				String storePlatformName = saleTargetAchievementDTO.getStorePlatformName();
				storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformName) ;
				
				if(goodsMap.get(storePlatformName + "_num") == null) {
					
					Integer num = saleTargetAchievementDTO.getNum() ;
					Double rate = saleTargetAchievementDTO.getRate();
					if(StringUtils.isNotBlank(dto.getStartMonth())
							&& StringUtils.isNotBlank(dto.getEndMonth()) && saleTargetMap != null) {
						BigDecimal targetNum = getResultMapVal(saleTargetMap.get("store_platform_num"), BigDecimal.class) ;
						
						BigDecimal numBD = new BigDecimal(saleTargetAchievementDTO.getNum());
						
						if(targetNum.compareTo(BigDecimal.ZERO) != 0) {
							rate = numBD.divide(targetNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
						}else {
							rate = 0.00 ;
						}
						
					}
					
					goodsMap.put(storePlatformName + "_num", num) ;
					goodsMap.put(storePlatformName + "_rate", rate) ;
				}
				
				resultCommbarMap.put(key, goodsMap) ;
			}
			
			for (Map<String, Object> goodsMap : resultCommbarMap.values()) {
				
				for (String storePlatformName : exsitplatform) {
					storePlatformName = DERP.getLabelByKey(DERP.storePlatformList, storePlatformName) ;
					String key = storePlatformName + "_num" ;
					
					if (!goodsMap.containsKey(key)) {
						goodsMap.put(storePlatformName + "_num", 0) ;
						goodsMap.put(storePlatformName + "_rate", 0) ;
					}
				}
				
			}
			
			List<Map<String, Object>> list = new ArrayList<>(resultCommbarMap.values()) ;
			List<Map<String, Object>> subList = null ;
			
			if(!list.isEmpty() && list.size() > dto.getPageSize()) {
				
				int end = dto.getBegin() + dto.getPageSize() ;
				
				if(end > list.size()) {
					end = list.size() ;
				}
				
				subList = list.subList(dto.getBegin(), end);
			}else {
				subList = list ;
			}
			
			dto.setList(subList);
			dto.setTotal(list.size());
			
		}else if(DERP_REPORT.SALE_TARGET_TYPE_3.equals(dto.getType())) {
			
			List<String> shopNameList = saleTargetAchievementDao.getExsitShopName();
			Map<String, Map<String, Object>> resultCommbarMap = new LinkedHashMap<String, Map<String, Object>>() ;
			
			for (SaleTargetAchievementDTO saleTargetAchievementDTO : listDto) {
				
				Map<String, Object> countMap = new HashMap<String, Object>() ;
				countMap.put("buId", saleTargetAchievementDTO.getBuId()) ;
				countMap.put("commbarcode", saleTargetAchievementDTO.getCommbarcode()) ;
				countMap.put("type", "3") ;
				countMap.put("shopCode", saleTargetAchievementDTO.getShopCode()) ;
				
				String month = "" ;
				if(StringUtils.isNotBlank(dto.getStartMonth())
						&& StringUtils.isNotBlank(dto.getEndMonth())) {
					
					countMap.put("startMonth", dto.getStartMonth()) ;
					countMap.put("endMonth", dto.getEndMonth()) ;
					
					month = dto.getStartMonth() + "至" + dto.getEndMonth() ;
				}else{
					
					countMap.put("month", dto.getMonth()) ;
					
					month = dto.getMonth() ;
				}
				saleTargetAchievementDTO.setMonth(month);
				
				Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
				
				String commbarcode = saleTargetAchievementDTO.getCommbarcode();	
				String buName = saleTargetAchievementDTO.getBuName() ;
				
				String key = commbarcode + "__" + buName + "__" + month;
				
				Map<String, Object> goodsMap = resultCommbarMap.get(key);
				
				if(goodsMap == null) {
					goodsMap = new HashMap<String, Object>() ;
					
					goodsMap.put("buName", buName) ;
					goodsMap.put("commbarcode", commbarcode) ;
					goodsMap.put("month", month) ;
					goodsMap.put("goodsName", saleTargetAchievementDTO.getGoodsName()) ;
					goodsMap.put("brandParent", saleTargetAchievementDTO.getBrandParent()) ;
					goodsMap.put("createDate", saleTargetAchievementDTO.getCreateDate()) ;
				}
				
				String shopName = saleTargetAchievementDTO.getShopName();
				
				if(goodsMap.get(shopName + "_num") == null) {
					
					Integer num = saleTargetAchievementDTO.getNum() ;
					Double rate = saleTargetAchievementDTO.getRate();
					if(StringUtils.isNotBlank(dto.getStartMonth())
							&& StringUtils.isNotBlank(dto.getEndMonth()) && saleTargetMap != null) {
						BigDecimal targetNum = getResultMapVal(saleTargetMap.get("shop_num"), BigDecimal.class) ;
						
						BigDecimal numBD = new BigDecimal(saleTargetAchievementDTO.getNum());
						
						if(targetNum.compareTo(BigDecimal.ZERO) != 0) {
							rate = numBD.divide(targetNum, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
						}else {
							rate = 0.00 ;
						}
						
					}
					
					goodsMap.put(shopName + "_num", num) ;
					goodsMap.put(shopName + "_rate", rate) ;
				}
				
				resultCommbarMap.put(key, goodsMap) ;
			}
			
			for (Map<String, Object> goodsMap : resultCommbarMap.values()) {
				
				for (String shopName : shopNameList) {
					String key = shopName + "_num" ;
					
					if (!goodsMap.containsKey(key)) {
						goodsMap.put(shopName + "_num", 0) ;
						goodsMap.put(shopName + "_rate", 0) ;
					}
				}
				
			}
			
			List<Map<String, Object>> list = new ArrayList<>(resultCommbarMap.values()) ;
			List<Map<String, Object>> subList = null ;
			
			if(!list.isEmpty() && list.size() > dto.getPageSize()) {
				
				int end = dto.getBegin() + dto.getPageSize() ;
				
				if(end > list.size()) {
					end = list.size() ;
				}
				
				subList = list.subList(dto.getBegin(), end);
			}else {
				subList = list ;
			}
			
			dto.setList(subList);
			dto.setTotal(list.size());
			
		}
		
		return dto ;
	}

	@Override
	public List<SaleTargetAchievementDTO> getExportList(User user,SaleTargetAchievementDTO dto) {
		
		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());
			
			if(buIds.isEmpty()) {
				return new ArrayList<SaleTargetAchievementDTO> () ;
			}
			
			dto.setBuIds(buIds);
		}
		
		List<SaleTargetAchievementDTO> exportList = saleTargetAchievementDao.getExportList(dto);
		
		for (SaleTargetAchievementDTO saleTargetAchievementDTO : exportList) {
			
			Map<String, Object> countMap = new HashMap<String, Object>() ;
			countMap.put("buId", saleTargetAchievementDTO.getBuId()) ;
			countMap.put("commbarcode", saleTargetAchievementDTO.getCommbarcode()) ;
			
			String month = null ;
			if(StringUtils.isNotBlank(dto.getStartMonth())
					&& StringUtils.isNotBlank(dto.getEndMonth())) {
				
				month = dto.getStartMonth() + "至" + dto.getEndMonth() ;
				
				countMap.put("startMonth", dto.getStartMonth()) ;
				countMap.put("endMonth", dto.getEndMonth()) ;
			}else {
				
				month = dto.getMonth() ;
				
				countMap.put("month", dto.getMonth()) ;
			}
			saleTargetAchievementDTO.setMonth(month);
			
			if(DERP_REPORT.SALE_TARGET_TYPE_1.equals(saleTargetAchievementDTO.getType())) {
				countMap.put("type", "1") ;
				Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
				
				if(saleTargetMap != null) {
					BigDecimal toBTargetNum = getResultMapVal(saleTargetMap.get("to_b_num"), BigDecimal.class) ;
					BigDecimal toCTargetNum = getResultMapVal(saleTargetMap.get("to_c_num"), BigDecimal.class) ;
					String barcodes = getResultMapVal(saleTargetMap.get("barcodes"), String.class) ;
					
					if(toBTargetNum == null) {
						toBTargetNum = new BigDecimal(0) ;
					}
					
					if(toCTargetNum == null) {
						toCTargetNum = new BigDecimal(0) ;
					}
					
					BigDecimal targetNum = toBTargetNum.add(toCTargetNum) ;
					
					Integer toBNum = saleTargetAchievementDTO.getToBNum();
					Double toBRate = 0.00 ;
					
					if(toBTargetNum.compareTo(BigDecimal.ZERO) != 0) {
						toBRate = new BigDecimal(toBNum).divide(toBTargetNum, 4, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					}
					
					Integer toCNum = saleTargetAchievementDTO.getToCNum();
					Double toCRate = 0.00 ;
					
					if(toCTargetNum.compareTo(BigDecimal.ZERO) != 0) {
						toCRate = new BigDecimal(toCNum).divide(toCTargetNum, 4, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					}
					
					Integer num = saleTargetAchievementDTO.getNum();
					Double rate = 0.00 ;
					
					if(targetNum.compareTo(BigDecimal.ZERO) != 0) {
						rate = new BigDecimal(num).divide(targetNum, 4, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					}
					
					saleTargetAchievementDTO.setToBTarget(toBTargetNum.intValue());
					saleTargetAchievementDTO.setToCTarget(toCTargetNum.intValue());
					saleTargetAchievementDTO.setTarget(toBTargetNum.intValue() + toCTargetNum.intValue());
					saleTargetAchievementDTO.setBarcodes(barcodes);
					saleTargetAchievementDTO.setToBRate(toBRate);
					saleTargetAchievementDTO.setToCRate(toCRate);
					saleTargetAchievementDTO.setRate(rate);
				}
				
			}else if(DERP_REPORT.SALE_TARGET_TYPE_2.equals(saleTargetAchievementDTO.getType())) {
				countMap.put("type", "2") ;
				countMap.put("storePlatformName", saleTargetAchievementDTO.getStorePlatformName()) ;
				Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
				
				if(saleTargetMap != null) {
					BigDecimal storePlatformNum = getResultMapVal(saleTargetMap.get("store_platform_num"), BigDecimal.class) ;
					String barcodes = getResultMapVal(saleTargetMap.get("barcodes"), String.class) ;
					
					if(storePlatformNum == null) {
						storePlatformNum = new BigDecimal(0) ;
					}
					saleTargetAchievementDTO.setTarget(storePlatformNum.intValue());
					saleTargetAchievementDTO.setBarcodes(barcodes);
					
					Integer num = saleTargetAchievementDTO.getNum();
					Double rate = 0.00 ;
					
					if(storePlatformNum.compareTo(BigDecimal.ZERO) != 0) {
						rate = new BigDecimal(num).divide(storePlatformNum, 4, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					}
					
					saleTargetAchievementDTO.setRate(rate);

				}
				
			}else if(DERP_REPORT.SALE_TARGET_TYPE_3.equals(saleTargetAchievementDTO.getType())) {
				countMap.put("type", "3") ;
				countMap.put("shopCode", saleTargetAchievementDTO.getShopCode()) ;
				Map<String, Object> saleTargetMap = saleTargetDao.countByMap(countMap) ;
				
				if(saleTargetMap != null) {
					BigDecimal shopNum = getResultMapVal(saleTargetMap.get("shop_num"), BigDecimal.class) ;
					String barcodes = getResultMapVal(saleTargetMap.get("barcodes"), String.class) ;
					
					if(shopNum == null) {
						shopNum = new BigDecimal(0) ;
					}
					saleTargetAchievementDTO.setTarget(shopNum.intValue());
					saleTargetAchievementDTO.setBarcodes(barcodes);
					
					Integer num = saleTargetAchievementDTO.getNum();
					Double rate = 0.00 ;
					
					if(shopNum.compareTo(BigDecimal.ZERO) != 0) {
						rate = new BigDecimal(num).divide(shopNum, 4, BigDecimal.ROUND_HALF_UP)
								.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() ;
					}
					
					saleTargetAchievementDTO.setRate(rate);

				}
				
			}
		}
		
		return exportList ;
	}
	
	/**
	 * 判断传入对象是否为空，为空直接返回null，否则将对象转成指定类型返回
	 * @param arg
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T>T getResultMapVal(Object org, Class<T> cls) {
		if(org == null ) {
			return null ;
		}
		
		return (T) org ;
	}

	@Override
	public List<String> getExsitplatform() {
		
		List<String> exsitList = saleTargetAchievementDao.getExsitplatform();
		List<String> returnList = new ArrayList<String>() ;
		
		for (String platform : exsitList) {
			returnList.add(DERP.getLabelByKey(DERP.storePlatformList, platform)) ;
		}
		
		return returnList;
	}

	@Override
	public List<String> getExsitShopName() {
		
		List<String> exsitList = saleTargetAchievementDao.getExsitShopName();
		List<String> returnList = new ArrayList<String>() ;
		
		boolean socialExsit = false ;
		
		for (String shopName : exsitList) {
			if(!"其他".equals(shopName)) {
				returnList.add(shopName) ;
			}else {
				socialExsit = true ;
			}
		}
		
		if(socialExsit) {
			returnList.add("其他") ;
		}
		
		return returnList;
	}
}
