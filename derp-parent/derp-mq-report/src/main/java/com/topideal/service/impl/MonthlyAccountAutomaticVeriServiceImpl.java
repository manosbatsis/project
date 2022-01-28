package com.topideal.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.automatic.MonthlyAccountAutomaticVerificationDao;
import com.topideal.dao.automatic.MonthlyAccountAutomaticVerificationItemDao;
import com.topideal.dao.inventory.BuInventoryDao;
import com.topideal.dao.inventory.MonthlyAccountDao;
import com.topideal.dao.reporting.BuInventorySummaryDao;
import com.topideal.dao.reporting.InventoryRealTimeSnapshotDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.dao.system.DepotMerchantRelDao;
import com.topideal.dao.system.MerchantInfoDao;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationItemModel;
import com.topideal.entity.vo.automatic.MonthlyAccountAutomaticVerificationModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.entity.vo.system.DepotMerchantRelModel;
import com.topideal.entity.vo.system.MerchantInfoModel;
import com.topideal.mongo.dao.FileTaskMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.service.MonthlyAccountAutomaticVeriService;

import net.sf.json.JSONObject;

@Service
public class MonthlyAccountAutomaticVeriServiceImpl implements MonthlyAccountAutomaticVeriService {

	@Autowired
	private MerchantInfoDao merchantInfoDao;
	@Autowired
	private MonthlyAccountDao monthlyAccountDao;
	@Autowired
	private BuInventoryDao buInventoryDao;
	@Autowired
	private BuInventorySummaryDao buInventorySummaryDao;
	@Autowired
	private MonthlyAccountAutomaticVerificationDao monthlyAccountAutomaticVerificationDao;
	@Autowired
	private MonthlyAccountAutomaticVerificationItemDao monthlyAccountAutomaticVerificationItemDao;
    @Autowired
    private DepotInfoDao depotInfoDao;
    @Autowired
    private FileTaskMongoDao fileTaskDao;
    @Autowired
    private DepotMerchantRelDao depotMerchantRelDao;
	@Autowired
	private InventoryRealTimeSnapshotDao inventoryRealTimeSnapshotDao ;

	@Autowired
    private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;

	/**
	 * 打印日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MonthlyAccountAutomaticVeriServiceImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201502012, model = DERP_LOG_POINT.POINT_13201502012_Label)
	public void saveAutoVeriReport(String json, String keys, String topics, String tags) throws Exception {

		Integer merchantId = null ;
		String month = null ;
		Integer depotId = null ;
		
		try {
			JSONObject jsonData = JSONObject.fromObject(json);
			Map<String, Object> jsonMap = jsonData;
			
			if(jsonMap.get("merchantId") != null) {
            	merchantId = (Integer) jsonMap.get("merchantId");// 商家Id
            }
            
            if(jsonMap.get("month") != null) {
            	month = (String) jsonMap.get("month"); // 报表月份
            }

			if (jsonMap.get("depotId") != null) {
				depotId = (Integer) jsonMap.get("depotId");
			}

			// 计算要刷新的月份
			if (StringUtils.isEmpty(month)) {
				// 若没有指定月份则刷新上个月份
				month = TimeUtils.getLastMonth(new Date());
			}

			// 查询所有商家,若指定了商家则只查本商家
			MerchantInfoModel merchantInfoModel = new MerchantInfoModel();
			if (merchantId != null && merchantId.longValue() > 0) {
				merchantInfoModel.setId(Long.valueOf(merchantId));
			}
			List<MerchantInfoModel> merchantList = merchantInfoDao.list(merchantInfoModel);

			for (MerchantInfoModel merchant : merchantList) {
				Map<String, MonthlyAccountAutomaticVerificationModel> cacheMap = new HashMap<String, MonthlyAccountAutomaticVerificationModel>();

				Map<String, Object> params = new HashMap<>();
				params.put("merchantId", merchant.getId());
				params.put("month", month);
				params.put("depotId", depotId);

				monthlyAccountAutomaticVerificationDao.deleteByMap(params);

				monthlyAccountAutomaticVerificationItemDao.deleteByMap(params);

				/**
				 * 月结按条码统计
				 */
				List<Map<String, Object>> monthlyList = monthlyAccountDao.getMonthListGroupByBarcode(params);
				for (Map<String, Object> monthlyMap : monthlyList) {
					Long tempMerchantId = judgeIsNullOrNotReturnObj(monthlyMap.get("merchant_id"), Long.class);
					Long tempDepotId = judgeIsNullOrNotReturnObj(monthlyMap.get("depot_id"), Long.class);
					String tempBarcode = judgeIsNullOrNotReturnObj(monthlyMap.get("barcode"), String.class);
					String tempGoodsName = judgeIsNullOrNotReturnObj(monthlyMap.get("goods_name"), String.class);
					BigDecimal tempSurplusNum = judgeIsNullOrNotReturnObj(monthlyMap.get("surplus_num"),
							BigDecimal.class);
					String tempTpye = judgeIsNullOrNotReturnObj(monthlyMap.get("type"), String.class);

					String key = tempMerchantId + "__" + tempDepotId + "__" + tempBarcode;

					MonthlyAccountAutomaticVerificationModel tempModel = cacheMap.get(key);

					if (tempModel == null) {
						tempModel = new MonthlyAccountAutomaticVerificationModel();
						tempModel.setMerchantId(tempMerchantId);
						tempModel.setMerchantName(merchant.getName());
						tempModel.setMonth(month);
						tempModel.setDepotId(tempDepotId);
						tempModel.setBarcode(tempBarcode);
						tempModel.setGoodsName(tempGoodsName);

						DepotInfoModel depot = depotInfoDao.searchById(tempDepotId);
						tempModel.setDepotName(depot.getName());

					}
					
					Integer monthlyAccountNormalNum=0;
					if (tempModel.getMonthlyAccountNormalNum()!=null) {
						monthlyAccountNormalNum = tempModel.getMonthlyAccountNormalNum();
					}
					
					Integer monthlyAccountWornNum=0;
					if (tempModel.getMonthlyAccountWornNum()!=null) {
						monthlyAccountWornNum = tempModel.getMonthlyAccountWornNum();
					}
					
					if (DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(tempTpye)) {
						tempModel.setMonthlyAccountNormalNum(monthlyAccountNormalNum 
								+ tempSurplusNum.intValue());
					} else if (DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(tempTpye)) {
						tempModel.setMonthlyAccountWornNum(monthlyAccountWornNum 
								+ tempSurplusNum.intValue());
					}

					if (tempModel.getMonthlyAccountSurplusNum() == null) {
						tempModel.setMonthlyAccountSurplusNum(tempSurplusNum.intValue());
					} else {
						tempModel.setMonthlyAccountSurplusNum(
								tempModel.getMonthlyAccountSurplusNum() + tempSurplusNum.intValue());
					}

					cacheMap.put(key, tempModel);
				}

				/**
				 * 事业部库存按条码统计
				 */
				List<Map<String, Object>> buInventoryList = buInventoryDao.getMonthlyAutoVeriListGroupByBarcode(params);
				for (Map<String, Object> buInventoryMap : buInventoryList) {

					Long tempMerchantId = judgeIsNullOrNotReturnObj(buInventoryMap.get("merchant_id"), Long.class);
					Long tempDepotId = judgeIsNullOrNotReturnObj(buInventoryMap.get("depot_id"), Long.class);
					String tempBarcode = judgeIsNullOrNotReturnObj(buInventoryMap.get("barcode"), String.class);
					String tempGoodsName = judgeIsNullOrNotReturnObj(buInventoryMap.get("goods_name"), String.class);
					String tempGoodsNo = judgeIsNullOrNotReturnObj(buInventoryMap.get("goods_no"), String.class);
					BigDecimal tempOkNum = judgeIsNullOrNotReturnObj(buInventoryMap.get("ok_num"), BigDecimal.class);
					BigDecimal tempWornNum = judgeIsNullOrNotReturnObj(buInventoryMap.get("worn_num"),
							BigDecimal.class);
					BigDecimal tempSurplusNum = judgeIsNullOrNotReturnObj(buInventoryMap.get("surplus_num"),
							BigDecimal.class);
					
					//InventoryLocationMappingMongo oriGoodsNoMappingModel = inventoryLocationMappingMongoDao.getOriGoodsNoMappingModel(merchant.getTopidealCode(), tempGoodsNo);

					/*
					 * if(oriGoodsNoMappingModel != null) {
					 * 
					 * String originalGoodsNo = oriGoodsNoMappingModel.getOriginalGoodsNo();
					 * 
					 * Map<String, Object> goodsParams = new HashMap<String, Object>() ;
					 * goodsParams.put("goodsNo", originalGoodsNo) ; goodsParams.put("merchantId",
					 * merchant.getId()) ;
					 * 
					 * MerchandiseInfoMogo merchandiseInfo =
					 * merchandiseInfoMogoDao.findOne(goodsParams);
					 * 
					 * tempBarcode = merchandiseInfo.getBarcode() ;
					 * 
					 * }
					 */
					String key = tempMerchantId + "__" + tempDepotId + "__" + tempBarcode;

					MonthlyAccountAutomaticVerificationModel tempModel = cacheMap.get(key);

					if (tempModel == null) {
						tempModel = new MonthlyAccountAutomaticVerificationModel();
						tempModel.setMerchantId(tempMerchantId);
						tempModel.setMerchantName(merchant.getName());
						tempModel.setMonth(month);
						tempModel.setDepotId(tempDepotId);
						tempModel.setBarcode(tempBarcode);
						tempModel.setGoodsName(tempGoodsName);

						DepotInfoModel depot = depotInfoDao.searchById(tempDepotId);
						tempModel.setDepotName(depot.getName());
					}

					if (tempOkNum != null) {
						
						Integer buInventoryNormalNum = tempModel.getBuInventoryNormalNum();
						buInventoryNormalNum = buInventoryNormalNum == null ? 0 : buInventoryNormalNum ;
						
						tempModel.setBuInventoryNormalNum(buInventoryNormalNum + tempOkNum.intValue());
					}

					if (tempWornNum != null) {
						
						Integer buInventoryWornNum = tempModel.getBuInventoryWornNum();
						buInventoryWornNum = buInventoryWornNum == null ? 0 : buInventoryWornNum ;
						
						tempModel.setBuInventoryWornNum(buInventoryWornNum + tempWornNum.intValue());
					}

					if (tempSurplusNum != null) {
						
						Integer buInventorySurplusNum = tempModel.getBuInventorySurplusNum();
						buInventorySurplusNum = buInventorySurplusNum == null ? 0 : buInventorySurplusNum ;
						
						tempModel.setBuInventorySurplusNum(buInventorySurplusNum + tempSurplusNum.intValue());
					}

					cacheMap.put(key, tempModel);
				}

				/**
				 * 事业部业务经销存按条码统计
				 */
				List<Map<String, Object>> buInventorySummaryList = buInventorySummaryDao
						.getMonthlyAutoVeriListGroupByBarcode(params);
				for (Map<String, Object> buInventorySummaryMap : buInventorySummaryList) {
					Long tempMerchantId = judgeIsNullOrNotReturnObj(buInventorySummaryMap.get("merchant_id"),
							Long.class);
					Long tempDepotId = judgeIsNullOrNotReturnObj(buInventorySummaryMap.get("depot_id"), Long.class);
					String tempBarcode = judgeIsNullOrNotReturnObj(buInventorySummaryMap.get("barcode"), String.class);
					String tempGoodsName = judgeIsNullOrNotReturnObj(buInventorySummaryMap.get("goods_name"),
							String.class);
					String tempGoodsNo = judgeIsNullOrNotReturnObj(buInventorySummaryMap.get("goods_no"), String.class);
					BigDecimal tempMonthEndNormalNum = judgeIsNullOrNotReturnObj(
							buInventorySummaryMap.get("month_end_normal_num"), BigDecimal.class);
					BigDecimal tempMonthEndDamagedNum = judgeIsNullOrNotReturnObj(
							buInventorySummaryMap.get("month_end_damaged_num"), BigDecimal.class);
					BigDecimal tempMonthEndNum = judgeIsNullOrNotReturnObj(buInventorySummaryMap.get("month_end_num"),
							BigDecimal.class);
					
					//InventoryLocationMappingMongo oriGoodsNoMappingModel = inventoryLocationMappingMongoDao.getOriGoodsNoMappingModel(merchant.getTopidealCode(), tempGoodsNo);

					/*
					 * if(oriGoodsNoMappingModel != null) {
					 * 
					 * String originalGoodsNo = oriGoodsNoMappingModel.getOriginalGoodsNo();
					 * 
					 * Map<String, Object> goodsParams = new HashMap<String, Object>() ;
					 * goodsParams.put("goodsNo", originalGoodsNo) ; goodsParams.put("merchantId",
					 * merchant.getId()) ;
					 * 
					 * MerchandiseInfoMogo merchandiseInfo =
					 * merchandiseInfoMogoDao.findOne(goodsParams);
					 * 
					 * tempBarcode = merchandiseInfo.getBarcode() ;
					 * 
					 * }
					 */

					String key = tempMerchantId + "__" + tempDepotId + "__" + tempBarcode;

					MonthlyAccountAutomaticVerificationModel tempModel = cacheMap.get(key);

					if (tempModel == null) {
						tempModel = new MonthlyAccountAutomaticVerificationModel();
						tempModel.setMerchantId(tempMerchantId);
						tempModel.setMerchantName(merchant.getName());
						tempModel.setMonth(month);
						tempModel.setDepotId(tempDepotId);
						tempModel.setBarcode(tempBarcode);
						tempModel.setGoodsName(tempGoodsName);

						DepotInfoModel depot = depotInfoDao.searchById(tempDepotId);
						tempModel.setDepotName(depot.getName());
					}

					if (tempMonthEndNormalNum != null) {
						
						Integer buInventorySummaryNormalNum = tempModel.getBuInventorySummaryNormalNum();
						buInventorySummaryNormalNum = buInventorySummaryNormalNum == null ? 0 : buInventorySummaryNormalNum ;
						
						tempModel.setBuInventorySummaryNormalNum(buInventorySummaryNormalNum + tempMonthEndNormalNum.intValue());
					}

					if (tempMonthEndDamagedNum != null) {
						
						Integer buInventorySummaryWornNum = tempModel.getBuInventorySummaryWornNum();
						buInventorySummaryWornNum = buInventorySummaryWornNum == null ? 0 : buInventorySummaryWornNum ;
						
						tempModel.setBuInventorySummaryWornNum(buInventorySummaryWornNum + tempMonthEndDamagedNum.intValue());
					}

					if (tempMonthEndNum != null) {
						
						Integer buInventorySummaryEndNum = tempModel.getBuInventorySummaryEndNum();
						buInventorySummaryEndNum = buInventorySummaryEndNum == null ? 0 :buInventorySummaryEndNum ;
						
						tempModel.setBuInventorySummaryEndNum(buInventorySummaryEndNum + tempMonthEndNum.intValue());
					}

					cacheMap.put(key, tempModel);
				}

				/**
				 * 保存月结自动校验对象，存在差异生成导出明细
				 */
				for (String key : cacheMap.keySet()) {

					MonthlyAccountAutomaticVerificationModel model = cacheMap.get(key);

					model = veriModelSetNullVal(model);
					
					//判断是否统计存货跌价，统计库存现存量
					DepotMerchantRelModel depotMerchantRelModel = new DepotMerchantRelModel() ;
					
					depotMerchantRelModel.setDepotId(model.getDepotId());
					depotMerchantRelModel.setMerchantId(model.getMerchantId());
					
					depotMerchantRelModel = depotMerchantRelDao.searchByModel(depotMerchantRelModel) ;
					
					if(depotMerchantRelModel != null &&
							DERP_SYS.DEPOTMERCHANTREL_ISINVERTORYFALLINGPRICE_1.equals(depotMerchantRelModel.getIsInvertoryFallingPrice())) {
						
						String nextMonthFirstDay = TimeUtils.getNextMonthFirstDay(month);
						
						Map<String, Object> inventoryParams = new HashMap<>();
						inventoryParams.put("merchantId", model.getMerchantId());
						inventoryParams.put("createDate", nextMonthFirstDay);
						inventoryParams.put("depotId", model.getDepotId());
						inventoryParams.put("barcode", model.getBarcode()) ;
						
						Integer inventoryNum = 0 ;
						
						List<Map<String, Object>> inventoryMapList = inventoryRealTimeSnapshotDao.getMonthlyAutoVeriListGroupByBarcode(inventoryParams) ;
						for (Map<String, Object> inventoryMap : inventoryMapList) {
							
							String stockType = judgeIsNullOrNotReturnObj(inventoryMap.get("stock_type") , String.class) ;
							
							if(DERP_INVENTORY.INITINVENTORY_TYPE_0.equals(stockType)) {
								BigDecimal qty = judgeIsNullOrNotReturnObj(inventoryMap.get("qty") , BigDecimal.class) ;
								
								if(qty != null) {
									inventoryNum += qty.intValue() ;
									model.setInventoryRealTimeNormalQty(qty.intValue());
								}else {
									model.setInventoryRealTimeNormalQty(0);
								}
								
							}else if(DERP_INVENTORY.INITINVENTORY_TYPE_1.equals(stockType)) {
								BigDecimal qty = judgeIsNullOrNotReturnObj(inventoryMap.get("qty") , BigDecimal.class) ;
								
								if(qty != null) {
									inventoryNum += qty.intValue() ;
									model.setInventoryRealTimeWornQty(qty.intValue());
								}else {
									model.setInventoryRealTimeWornQty(0);
								}
							}
						}
						
						model.setInventoryRealTimeQty(inventoryNum);
						
					}else {
						model.setInventoryRealTimeNormalQty(0);
						model.setInventoryRealTimeQty(0);
						model.setInventoryRealTimeWornQty(0);
					}

					List<Integer> normalList = new ArrayList<Integer>();
					normalList.add(model.getMonthlyAccountNormalNum());
					normalList.add(model.getBuInventoryNormalNum());
					normalList.add(model.getBuInventorySummaryNormalNum());

					List<Integer> wornList = new ArrayList<Integer>();
					wornList.add(model.getMonthlyAccountWornNum());
					wornList.add(model.getBuInventoryWornNum());
					wornList.add(model.getBuInventorySummaryWornNum());
					
					List<Integer> totalList = new ArrayList<Integer>();
					totalList.add(model.getMonthlyAccountSurplusNum());
					totalList.add(model.getBuInventorySurplusNum());
					totalList.add(model.getBuInventorySummaryEndNum());

					if (!isSameNum(normalList)) {
						model.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0);

						MonthlyAccountAutomaticVerificationItemModel itemModel = new MonthlyAccountAutomaticVerificationItemModel();
						itemModel.setBarcode(model.getBarcode());
						itemModel.setMerchantId(model.getMerchantId());
						itemModel.setMerchantName(model.getMerchantName());
						itemModel.setMonth(model.getMonth());
						itemModel.setDepotId(model.getDepotId());
						itemModel.setDepotName(model.getDepotName());
						itemModel.setBuInventorySummaryEndNum(model.getBuInventorySummaryNormalNum());
						itemModel.setBuInventorySurplusNum(model.getBuInventoryNormalNum());
						itemModel.setMonthlyAccountSurplusNum(model.getMonthlyAccountNormalNum());
						itemModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);
						itemModel.setCreateDate(TimeUtils.getNow());
						itemModel.setModifyDate(TimeUtils.getNow());
						itemModel.setGoodsName(model.getGoodsName());

						monthlyAccountAutomaticVerificationItemDao.save(itemModel);
					}

					if (!isSameNum(wornList)) {
						model.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0);

						MonthlyAccountAutomaticVerificationItemModel itemModel = new MonthlyAccountAutomaticVerificationItemModel();
						itemModel.setBarcode(model.getBarcode());
						itemModel.setMerchantId(model.getMerchantId());
						itemModel.setMerchantName(model.getMerchantName());
						itemModel.setMonth(model.getMonth());
						itemModel.setDepotId(model.getDepotId());
						itemModel.setDepotName(model.getDepotName());
						itemModel.setBuInventorySummaryEndNum(model.getBuInventorySummaryWornNum());
						itemModel.setBuInventorySurplusNum(model.getBuInventoryWornNum());
						itemModel.setMonthlyAccountSurplusNum(model.getMonthlyAccountWornNum());
						itemModel.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);
						itemModel.setCreateDate(TimeUtils.getNow());
						itemModel.setModifyDate(TimeUtils.getNow());
						itemModel.setGoodsName(model.getGoodsName());

						monthlyAccountAutomaticVerificationItemDao.save(itemModel);
					}
					
					if (!isSameNum(totalList)) {
						model.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_0);
						
						MonthlyAccountAutomaticVerificationItemModel itemModel = new MonthlyAccountAutomaticVerificationItemModel();
						itemModel.setBarcode(model.getBarcode());
						itemModel.setMerchantId(model.getMerchantId());
						itemModel.setMerchantName(model.getMerchantName());
						itemModel.setMonth(model.getMonth());
						itemModel.setDepotId(model.getDepotId());
						itemModel.setDepotName(model.getDepotName());
						itemModel.setBuInventorySummaryEndNum(model.getBuInventorySummaryEndNum());
						itemModel.setBuInventorySurplusNum(model.getBuInventorySurplusNum());
						itemModel.setMonthlyAccountSurplusNum(model.getMonthlyAccountSurplusNum());
						itemModel.setCreateDate(TimeUtils.getNow());
						itemModel.setModifyDate(TimeUtils.getNow());
						itemModel.setGoodsName(model.getGoodsName());
						
						monthlyAccountAutomaticVerificationItemDao.save(itemModel);
					}

					if (model.getStatus() == null) {
						model.setStatus(DERP_REPORT.BUSINESSFINANCEAUTOMATICVERIFICATION_STATUS_1);
					}

					model.setCreateDate(TimeUtils.getNow());

					monthlyAccountAutomaticVerificationDao.save(model);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RuntimeException();
		}
	}

	@SuppressWarnings({ "unchecked" })
	private <T> T judgeIsNullOrNotReturnObj(Object obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		}

		return (T) obj;
	}

	/**
	 * 比较数字是否相等
	 */
	private boolean isSameNum(List<Integer> nums) {
		for (int i = 1; i < nums.size(); i++) {
			if (nums.get(i).intValue() != nums.get(0).intValue()) {
				return false;
			}
		}
		return true;
	}

	private MonthlyAccountAutomaticVerificationModel veriModelSetNullVal(
			MonthlyAccountAutomaticVerificationModel model) {

		if (model.getBuInventoryNormalNum() == null) {
			model.setBuInventoryNormalNum(0);
		}

		if (model.getBuInventorySurplusNum() == null) {
			model.setBuInventorySurplusNum(0);
		}

		if (model.getBuInventoryWornNum() == null) {
			model.setBuInventoryWornNum(0);
		}

		if (model.getMonthlyAccountNormalNum() == null) {
			model.setMonthlyAccountNormalNum(0);
		}

		if (model.getMonthlyAccountWornNum() == null) {
			model.setMonthlyAccountWornNum(0);
		}

		if (model.getMonthlyAccountSurplusNum() == null) {
			model.setMonthlyAccountSurplusNum(0);
		}

		if (model.getBuInventorySummaryEndNum() == null) {
			model.setBuInventorySummaryEndNum(0);
		}

		if (model.getBuInventorySummaryNormalNum() == null) {
			model.setBuInventorySummaryNormalNum(0);
		}

		if (model.getBuInventorySummaryWornNum() == null) {
			model.setBuInventorySummaryWornNum(0);
		}

		return model;
	}

	@Override
	public void delByTaskType(Map<String, Object> delTaskMap) {
        fileTaskDao.remove(delTaskMap);
    }
}
