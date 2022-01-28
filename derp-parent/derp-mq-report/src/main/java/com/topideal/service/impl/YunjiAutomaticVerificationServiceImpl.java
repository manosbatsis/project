package com.topideal.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.topideal.common.constant.DERP_LOG_POINT;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_REPORT;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.automatic.YunjiAutomaticVerificationDao;
import com.topideal.dao.order.BillOutinDepotItemDao;
import com.topideal.dao.order.MerchandiseContrastDao;
import com.topideal.dao.order.MerchandiseContrastItemDao;
import com.topideal.dao.order.SaleOutDepotItemDao;
import com.topideal.dao.order.SaleReturnIdepotItemDao;
import com.topideal.dao.order.YunjiAccountDataDao;
import com.topideal.dao.order.YunjiDeliveryDetailDao;
import com.topideal.dao.order.YunjiReturnDetailDao;
import com.topideal.dao.system.DepotInfoDao;
import com.topideal.entity.vo.automatic.YunjiAutomaticVerificationModel;
import com.topideal.entity.vo.order.BillOutinDepotItemModel;
import com.topideal.entity.vo.order.MerchandiseContrastItemModel;
import com.topideal.entity.vo.order.MerchandiseContrastModel;
import com.topideal.entity.vo.order.YunjiAccountDataModel;
import com.topideal.entity.vo.order.YunjiDeliveryDetailModel;
import com.topideal.entity.vo.order.YunjiReturnDetailModel;
import com.topideal.entity.vo.system.DepotInfoModel;
import com.topideal.service.YunjiAutomaticVerificationService;

import net.sf.json.JSONObject;

@Service
public class YunjiAutomaticVerificationServiceImpl implements YunjiAutomaticVerificationService {

	/**
	 * 打印日志
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(YunjiAutomaticVerificationServiceImpl.class);

	@Autowired
	private YunjiDeliveryDetailDao yunjiDeliveryDetailDao;

	@Autowired
	private YunjiReturnDetailDao yunjiReturnDetailDao;

	@Autowired
	private YunjiAccountDataDao yunjiAccountDataDao;

	@Autowired
	private DepotInfoDao depotInfoDao;

	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;

	@Autowired
	private MerchandiseContrastDao merchandiseContrastDao;

	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;

	@Autowired
	private YunjiAutomaticVerificationDao yunjiAutomaticVerificationDao;
	
	@Autowired
	private BillOutinDepotItemDao billOutinDepotItemDao ;
	
	@Autowired
	private MerchandiseContrastItemDao merchandiseContrastItemDao ;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13201502020, model = DERP_LOG_POINT.POINT_13201502020_Label)
	public void saveSummaryReport(String json, String keys, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);

		String settleId = null;
		String skuNo = null;
		;
		Long merchantId = null;

		if (jsonData.get("settleId") != null) {
			settleId = jsonData.getString("settleId");
		}

		if (jsonData.get("skuNo") != null) {
			skuNo = jsonData.getString("skuNo");
		}

		if (jsonData.get("merchantId") != null) {
			merchantId = jsonData.getLong("merchantId");
		}

		/**
		 * 1.1 根据结算ID，云集SKU统计状态为未校验的结算明细
		 */
		List<YunjiAutomaticVerificationModel> yunjiAutoVeriList = saveYunjiAutoVeri(settleId, skuNo, merchantId);

		/**
		 * 1.2 统计系统量,并保存自动校验对象
		 */
		saveSystemAccount(yunjiAutoVeriList);
	}

	/**
	 * 统计系统量,并保存自动校验对象
	 * 
	 * @param yunjiAutoVeriList
	 * @throws SQLException
	 */
	private void saveSystemAccount(List<YunjiAutomaticVerificationModel> yunjiAutoVeriList) throws SQLException {

		// 获取云集代销仓
		DepotInfoModel depotInfo = new DepotInfoModel();
		depotInfo.setDepotCode("ERPWMS_360_0407");
		depotInfo = depotInfoDao.searchByModel(depotInfo);
		
		// 获取云集代销仓
		DepotInfoModel depotTHInfo = new DepotInfoModel();
		depotTHInfo.setDepotCode("YZTH001");
		depotTHInfo = depotInfoDao.searchByModel(depotTHInfo);

		// 获取系统有差异记录
		List<YunjiAutomaticVerificationModel> differentList = yunjiAutomaticVerificationDao.getDifferentList();
		yunjiAutoVeriList.addAll(differentList);

		for (YunjiAutomaticVerificationModel veriModel : yunjiAutoVeriList) {

			String skuNo = veriModel.getSkuNo();

			MerchandiseContrastModel merchandiseContrastModel = new MerchandiseContrastModel();
			merchandiseContrastModel.setCrawlerGoodsNo(skuNo);
			merchandiseContrastModel.setMerchantId(veriModel.getMerchantId());
			merchandiseContrastModel.setStatus(DERP_ORDER.MERCHANDISECONTRAST_STATUS_0);
			merchandiseContrastModel.setType(DERP_ORDER.MERCHANDISECONTRAST_TYPE_2);

			merchandiseContrastModel = merchandiseContrastDao.searchByModel(merchandiseContrastModel);

			if (merchandiseContrastModel == null) {

				YunjiAutomaticVerificationModel queryModel = new YunjiAutomaticVerificationModel();
				queryModel.setSettleId(veriModel.getSettleId());
				queryModel.setSkuNo(veriModel.getSkuNo());
				queryModel.setMerchantId(veriModel.getMerchantId());
				queryModel = yunjiAutomaticVerificationDao.searchByModel(queryModel);

				if (queryModel == null) {
					veriModel.setGoodsNo("未维护");
					veriModel.setResult("爬虫商品对照表未维护");
					veriModel.setDeliveryDifferent(veriModel.getPlatformDeliveryAccount());
					veriModel.setReturnDifferent(veriModel.getPlatformReturnAccount());
					veriModel.setCreateDate(TimeUtils.getNow());

					yunjiAutomaticVerificationDao.save(veriModel);
				}

				continue;
			}

			veriModel.setResult("");

			String goodsNos = null ;
			MerchandiseContrastItemModel queryItemModel = new MerchandiseContrastItemModel() ;
			queryItemModel.setContrastId(merchandiseContrastModel.getId());
			
			List<MerchandiseContrastItemModel> itemList = merchandiseContrastItemDao.list(queryItemModel);
			goodsNos = itemList.stream().map(MerchandiseContrastItemModel::getGoodsNo).collect(Collectors.joining(","));
			
			// 查询系统销售出库量，销售退入库量
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("vipsBillNo", veriModel.getSettleId());
			queryMap.put("merchantId", veriModel.getMerchantId());
			queryMap.put("depotId", depotInfo.getId());
			queryMap.put("goodsNos", goodsNos);
			queryMap.put("platformSku", skuNo) ;

			Integer systemDeliveryAccount = saleOutDepotItemDao.getYJVeriSaleOutDepotAccount(queryMap);
			Integer systemReturnAccount = saleReturnIdepotItemDao.getYJVeriSaleRetunIdepotAccount(queryMap);
			
			//查询系统账单出库量销售出库量
			queryMap.put("billSource", DERP_ORDER.BILLOUTINDEPOT_BILL_SOURCE_1) ;
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_XSC) ;
			BillOutinDepotItemModel xscItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap);
			Integer systemDeliveryNewAccount = 0 ;
			
			if(xscItem != null) {
				systemDeliveryNewAccount = xscItem.getNum() ;
			}
			
			//查询系统账单出库量客退入库量
			queryMap.put("processingType", DERP_ORDER.PROCESSINGTYPE_KTR) ;
			queryMap.put("depotId", depotTHInfo.getId());
			BillOutinDepotItemModel ktrItem = billOutinDepotItemDao.getAutoVeriOutinDepotAccount(queryMap); ;
			Integer systemReturnNewAccount = 0 ;
			
			if(ktrItem != null) {
				systemReturnNewAccount = ktrItem.getNum() ;
			}
			
			if (systemDeliveryAccount == null) {
				systemDeliveryAccount = 0;
			}

			if (systemReturnAccount == null) {
				systemReturnAccount = 0;
			}
			
			if(systemDeliveryNewAccount != null) {
				systemDeliveryAccount += systemDeliveryNewAccount ;
			}
			
			if (systemReturnNewAccount != null) {
				systemReturnAccount += systemReturnNewAccount;
			}
			
			veriModel.setSystemDeliveryAccount(systemDeliveryAccount);
			veriModel.setSystemReturnAccount(systemReturnAccount);

			YunjiAutomaticVerificationModel queryModel = new YunjiAutomaticVerificationModel();
			queryModel.setSettleId(veriModel.getSettleId());
			queryModel.setSkuNo(veriModel.getSkuNo());
			queryModel.setMerchantId(veriModel.getMerchantId());

			queryModel = yunjiAutomaticVerificationDao.searchByModel(queryModel);

			if (queryModel != null) {
				
				Map<String, Object> queryPlatformMap = new HashMap<>() ;
				queryPlatformMap.put("settleId", queryModel.getSettleId()) ;
				queryPlatformMap.put("skuNo", queryModel.getSkuNo()) ;
				queryPlatformMap.put("merchantId", queryModel.getMerchantId()) ;
				
				
				Map<String, Object> deliveryMap = yunjiDeliveryDetailDao.getDeliveryAccount(queryPlatformMap);
				BigDecimal deliveryBD = judgeIsNullOrNotReturnObj(deliveryMap.get("qty"), BigDecimal.class);
				
				if(deliveryBD != null) {
					veriModel.setPlatformDeliveryAccount(deliveryBD.intValue());
				}
				
				Map<String, Object> returnMap = yunjiReturnDetailDao.getReturnAccount(queryPlatformMap) ;
				BigDecimal returnBD = judgeIsNullOrNotReturnObj(returnMap.get("qty"), BigDecimal.class);
				
				if(returnBD != null) {
					veriModel.setPlatformReturnAccount(returnBD.intValue());
				}
				
			}

			Integer xscContrastNum = 1 ;
			Integer ktrContrastNum = 1 ;
			
			if(xscItem != null) {
				xscContrastNum = xscItem.getContrastNum() ;
			}
			
			if(ktrItem != null) {
				ktrContrastNum = ktrItem.getContrastNum() ; 
			}
			
			// 设置差异
			Integer deliveryDifferent = veriModel.getPlatformDeliveryAccount() * xscContrastNum - systemDeliveryAccount;
			Integer returnDifferent = veriModel.getPlatformReturnAccount() * ktrContrastNum - systemReturnAccount;

			// 设置原因
			StringBuffer result = new StringBuffer();
			if (deliveryDifferent != 0) {
				YunjiDeliveryDetailModel queryDeliveryModel = new YunjiDeliveryDetailModel();
				queryDeliveryModel.setSettleId(veriModel.getSettleId());
				queryDeliveryModel.setSkuNo(veriModel.getSkuNo());
				queryDeliveryModel.setIsUsed(DERP_REPORT.YJ_ACCOUNT_DATA_ISUSED_0);

				List<YunjiDeliveryDetailModel> deliveryList = yunjiDeliveryDetailDao.list(queryDeliveryModel);
				if (!deliveryList.isEmpty()) {
					String deliveryReason = deliveryList.get(0).getReason();
					if (StringUtils.isNotBlank(deliveryReason)) {
						result.append(deliveryReason + ";");
					}
				}
			}

			if (returnDifferent != 0) {
				YunjiReturnDetailModel queryReturnModel = new YunjiReturnDetailModel();
				queryReturnModel.setSettleId(veriModel.getSettleId());
				queryReturnModel.setSkuNo(veriModel.getSkuNo());
				queryReturnModel.setIsUsed(DERP_REPORT.YJ_ACCOUNT_DATA_ISUSED_0);

				List<YunjiReturnDetailModel> returnList = yunjiReturnDetailDao.list(queryReturnModel);
				if (!returnList.isEmpty()) {
					String returnReason = returnList.get(0).getReason();
					if (StringUtils.isNotBlank(returnReason)) {
						result.append(returnReason + ";");
					}

				}
			}

			veriModel.setResult(result.toString());
			veriModel.setDeliveryDifferent(deliveryDifferent);
			veriModel.setReturnDifferent(returnDifferent);
			veriModel.setGoodsNo(goodsNos.toString());

			// 有则更新，无则新增
			if (queryModel == null) {
				veriModel.setId(null);
				veriModel.setCreateDate(TimeUtils.getNow());

				yunjiAutomaticVerificationDao.save(veriModel);
			} else {
				veriModel.setId(queryModel.getId());
				veriModel.setModifyDate(TimeUtils.getNow());

				yunjiAutomaticVerificationDao.modify(veriModel);
			}

		}

	}

	/**
	 * 根据结算ID，云集SKU统计状态未为校验的结算明细
	 * 
	 * @param settleId
	 * @param skuNo
	 * @return
	 * @throws SQLException
	 */
	private List<YunjiAutomaticVerificationModel> saveYunjiAutoVeri(String settleId, String skuNo, Long merchantId)
			throws SQLException {

		List<YunjiAutomaticVerificationModel> veriList = new ArrayList<YunjiAutomaticVerificationModel>();

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("settleId", settleId);
		queryMap.put("skuNo", skuNo);
		queryMap.put("merchantId", merchantId);

		// 结算ID与账单月份,结算日期缓存集
		Map<String, Map<String, Object>> settleCacheMap = new HashMap<String, Map<String, Object>>();
		// 结算ID+sku为key缓存自动校验对象
		Map<String, YunjiAutomaticVerificationModel> veriCacheMap = new HashMap<String, YunjiAutomaticVerificationModel>();

		// 校验发货明细
		List<Map<String, Object>> deliveryMapList = yunjiDeliveryDetailDao.getYjVeriDetail(queryMap);
		for (Map<String, Object> map : deliveryMapList) {

			YunjiAutomaticVerificationModel veriModel = new YunjiAutomaticVerificationModel();

			String settleIdTemp = judgeIsNullOrNotReturnObj(map.get("settle_id"), String.class);
			String skuNoTemp = judgeIsNullOrNotReturnObj(map.get("sku_no"), String.class);
			Long merchantIdTemp = judgeIsNullOrNotReturnObj(map.get("merchant_id"), Long.class);
			String merchantNameTemp = judgeIsNullOrNotReturnObj(map.get("merchant_name"), String.class);

			Map<String, Object> cacheMap = settleCacheMap.get(settleIdTemp);
			if (cacheMap == null) {
				YunjiAccountDataModel accountDataModel = new YunjiAccountDataModel();
				accountDataModel.setSettleId(settleIdTemp);

				accountDataModel = yunjiAccountDataDao.searchByModel(accountDataModel);

				if (accountDataModel == null) {
					LOGGER.error("云集结算订单为空，结算ID为：" + settleIdTemp);
					continue;
				}

				Date settleDate = accountDataModel.getSettleDate();
				Date businessEndDate = accountDataModel.getBusinessEndDate();

				String month = null;
				if (businessEndDate != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String temp = sdf.format(businessEndDate);
					month = temp.substring(0, temp.lastIndexOf("-"));
				}

				cacheMap = new HashMap<String, Object>();
				cacheMap.put("month", month);
				cacheMap.put("settleDate", settleDate);

				settleCacheMap.put(settleIdTemp, cacheMap);
			}

			String month = (String) cacheMap.get("month");
			Date settleDate = (Date) cacheMap.get("settleDate");

			BigDecimal deliveryAccountBD = judgeIsNullOrNotReturnObj(map.get("qty"), BigDecimal.class);
			Integer deliveryAccount = 0;
			if (deliveryAccountBD != null) {
				deliveryAccount = deliveryAccountBD.intValue();
			}

			veriModel.setSettleDate(settleDate);
			veriModel.setMonth(month);
			veriModel.setMerchantId(merchantIdTemp);
			veriModel.setMerchantName(merchantNameTemp);
			veriModel.setSettleId(settleIdTemp);
			veriModel.setSkuNo(skuNoTemp);
			veriModel.setPlatformDeliveryAccount(deliveryAccount);
			veriModel.setPlatformReturnAccount(0);

			veriCacheMap.put(settleIdTemp + "_" + skuNoTemp, veriModel);

			Map<String, Object> paramsMap = new HashMap<String, Object>() ;
			paramsMap.put("settleId", settleIdTemp) ;
			paramsMap.put("skuNo", skuNoTemp) ;
			paramsMap.put("merchantId", merchantIdTemp) ;
			// 设置为已校验
			yunjiDeliveryDetailDao.changeVeriStatus(paramsMap);
		}

		// 校验退货明细
		List<Map<String, Object>> returnMapList = yunjiReturnDetailDao.getYjVeriDetail(queryMap);
		for (Map<String, Object> returnMap : returnMapList) {
			String settleIdTemp = judgeIsNullOrNotReturnObj(returnMap.get("settle_id"), String.class);
			String skuNoTemp = judgeIsNullOrNotReturnObj(returnMap.get("sku_no"), String.class);
			Long merchantIdTemp = judgeIsNullOrNotReturnObj(returnMap.get("merchant_id"), Long.class);
			String merchantNameTemp = judgeIsNullOrNotReturnObj(returnMap.get("merchant_name"), String.class);

			YunjiAutomaticVerificationModel veriModel = veriCacheMap.get(settleIdTemp + "_" + skuNoTemp);

			if (veriModel == null) {

				Map<String, Object> cacheMap = settleCacheMap.get(settleIdTemp);
				if (cacheMap == null) {
					YunjiAccountDataModel accountDataModel = new YunjiAccountDataModel();
					accountDataModel.setSettleId(settleIdTemp);

					accountDataModel = yunjiAccountDataDao.searchByModel(accountDataModel);

					if (accountDataModel == null) {
						LOGGER.error("云集结算订单为空，结算ID为：" + settleIdTemp);
						continue;
					}

					Date settleDate = accountDataModel.getSettleDate();
					Date businessEndDate = accountDataModel.getBusinessEndDate();

					String month = null;
					if (businessEndDate != null) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						String temp = sdf.format(businessEndDate);
						month = temp.substring(0, temp.lastIndexOf("-"));
					}

					cacheMap = new HashMap<String, Object>();
					cacheMap.put("month", month);
					cacheMap.put("settleDate", settleDate);

					settleCacheMap.put(settleIdTemp, cacheMap);
				}

				String month = (String) cacheMap.get("month");
				Date settleDate = (Date) cacheMap.get("settleDate");

				veriModel = new YunjiAutomaticVerificationModel();
				veriModel.setSettleDate(settleDate);
				veriModel.setMonth(month);
				veriModel.setMerchantId(merchantIdTemp);
				veriModel.setMerchantName(merchantNameTemp);
				veriModel.setSettleId(settleIdTemp);
				veriModel.setSkuNo(skuNoTemp);
				veriModel.setPlatformDeliveryAccount(0);
			}

			BigDecimal returnAccountBD = judgeIsNullOrNotReturnObj(returnMap.get("qty"), BigDecimal.class);
			Integer returnAccount = 0;
			if (returnAccountBD != null) {
				returnAccount = returnAccountBD.intValue();
			}
			veriModel.setPlatformReturnAccount(returnAccount);

			veriCacheMap.put(settleIdTemp + "_" + skuNoTemp, veriModel);
			
			Map<String, Object> paramsMap = new HashMap<String, Object>() ;
			paramsMap.put("settleId", settleIdTemp) ;
			paramsMap.put("skuNo", skuNoTemp) ;
			paramsMap.put("merchantId", merchantIdTemp) ;

			// 设置为已校验
			yunjiReturnDetailDao.changeVeriStatus(paramsMap);
		}

		veriList.addAll(veriCacheMap.values());

		return veriList;
	}

	@SuppressWarnings({ "unchecked" })
	private <T> T judgeIsNullOrNotReturnObj(Object obj, Class<T> clazz) {
		if (obj == null) {
			return null;
		}

		return (T) obj;
	}

}
