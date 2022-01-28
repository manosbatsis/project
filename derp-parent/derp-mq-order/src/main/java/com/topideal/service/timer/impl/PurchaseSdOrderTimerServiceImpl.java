package com.topideal.service.timer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdPurchaseConfigDao;
import com.topideal.dao.common.SdPurchaseConfigItemDao;
import com.topideal.dao.common.SdTypeConfigDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseSdOrderDao;
import com.topideal.dao.purchase.PurchaseSdOrderSditemDao;
import com.topideal.dao.purchase.WarehouseOrderRelDao;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.entity.vo.purchase.*;
import com.topideal.mongo.dao.CommbarcodeMongoDao;
import com.topideal.mongo.dao.FinanceCloseAccountsMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.CommbarcodeMongo;
import com.topideal.mongo.entity.FinanceCloseAccountsMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.timer.PurchaseSdOrderTimerService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PurchaseSdOrderTimerServiceImpl implements PurchaseSdOrderTimerService{

	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao ;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private PurchaseSdOrderDao purchaseSdOrderDao ;
	@Autowired
	private SdPurchaseConfigDao sdPurchaseConfigDao ;
	@Autowired
	private SdPurchaseConfigItemDao sdPurchaseConfigItemDao ;
	@Autowired
	private SdTypeConfigDao sdTypeConfigDao ;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao ;
	@Autowired
	private PurchaseSdOrderSditemDao purchaseSdOrderSditemDao ;
    @Autowired
    private MerchantInfoMongoDao merchantInfoMongoDao;

	private static final Logger LOG = Logger.getLogger(PurchaseSdOrderTimerServiceImpl.class) ;

	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_13201110020,model=DERP_LOG_POINT.POINT_13201110020_Label)
	public void savePurchaseSdOrder(String json, String keys, String topics, String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		String merchantIds = (String) jsonData.get("merchantIds");
		/***
		 * 查询嘉宝、健太、卓烨、宝信创建日期为最近3个月的【已审核】采购订单
		 */
		List<Long> merchantIdList = getMerchantIds(merchantIds);
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("merchantIdList", merchantIdList);
		List<PurchaseOrderModel> purchaseList = purchaseOrderDao.getCreateSdOrder(paramMap) ;

		Set<String> cacheMap = new HashSet<String>() ;
		Map<String,SdPurchaseConfigModel> sdConfigMap = new HashMap<>();
		Map<String,Timestamp> purchaseInboundDateMap = new HashMap<>();
		/**获取入库日期、在途起始日期归属月份未关帐*/
		for (PurchaseOrderModel purchaseOrderModel : purchaseList) {
			List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(purchaseOrderModel.getId());

			Timestamp tempTime = null;

			/**优先获取入库时间*/
			for (PurchaseWarehouseModel purchaseWarehouseModel : warehouseList) {
				tempTime = purchaseWarehouseModel.getInboundDate() ;
			}

			/**入库时间为空，则取在途起始日期*/
			if(tempTime == null) {
				tempTime = purchaseOrderModel.getCargoCuttingDate() ;
			}else{
				purchaseInboundDateMap.put(purchaseOrderModel.getCode(),tempTime);
			}

			/**在途起始日期为空，跳过*/
			if(tempTime == null) {
				LOG.info("采购订单："+ purchaseOrderModel.getCode() +"入库时间、在途起始日期为空，跳过");
				continue ;
			}

			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(purchaseOrderModel.getMerchantId());
			closeAccountsMongo.setDepotId(purchaseOrderModel.getDepotId());
			closeAccountsMongo.setBuId(purchaseOrderModel.getBuId());

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";

			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}

			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (tempTime.getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					LOG.info("采购订单："+ purchaseOrderModel.getCode() +"入库时间、在途起始日期小于关账日期，跳过");
					continue;
				}
			}
			//根据商家事业部供应商查询是否存在采购SD配置
			String configKey = purchaseOrderModel.getMerchantId()+"_"+purchaseOrderModel.getSupplierId()+"_"+purchaseOrderModel.getBuId();
			SdPurchaseConfigModel querySdConfigModel = sdConfigMap.get(configKey) ;
			if(querySdConfigModel == null){
				querySdConfigModel = new SdPurchaseConfigModel() ;
				querySdConfigModel.setMerchantId(purchaseOrderModel.getMerchantId());
				querySdConfigModel.setSupplierId(purchaseOrderModel.getSupplierId());
				querySdConfigModel.setBuId(purchaseOrderModel.getBuId());
				querySdConfigModel.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);

				querySdConfigModel = sdPurchaseConfigDao.getLastestModel(querySdConfigModel) ;
				if(querySdConfigModel == null) {
					LOG.info("采购订单："+ purchaseOrderModel.getCode() +"商家事业部供应商查询不存在采购SD配置，跳过");
					continue ;
				}
				sdConfigMap.put(configKey, querySdConfigModel);
			}
			cacheMap.add(purchaseOrderModel.getCode()) ;

		}
		//根据采购code记录采购订单
		Map<String,PurchaseOrderModel> purchaseMap = new HashMap<>();
		//根据采购code_sd类型记录sd配置
		Map<String,List<SdPurchaseConfigItemModel>> sdConfigItemMap = new HashMap<>();
		//记录条形码对应的标准条码信息
		Map<String,CommbarcodeMongo> commbarcodeMap = new HashMap<>();
		for (String code : cacheMap) {
			//记录标准条码
			List<String> checkCommbarcode = new ArrayList<>();

			PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
			queryModel.setCode(code);
			PurchaseOrderModel purchaseModel = purchaseOrderDao.getDetails(queryModel);

			purchaseMap.put(purchaseModel.getCode(),purchaseModel);
			//根据商家事业部供应商查询是否存在采购SD配置
			SdPurchaseConfigModel querySdConfigModel = sdConfigMap.get(purchaseModel.getMerchantId()+"_"+purchaseModel.getSupplierId()+"_"+purchaseModel.getBuId()) ;
			//获取单比例配置
			List<SdPurchaseConfigItemModel> sdConfigItemList = new ArrayList<>();
			SdPurchaseConfigItemModel queryItemModel = new SdPurchaseConfigItemModel() ;
			queryItemModel.setConfigId(querySdConfigModel.getId());
			queryItemModel.setSdConfigSimpleType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
			List<SdPurchaseConfigItemModel> simpleConfigItemList = sdPurchaseConfigItemDao.list(queryItemModel) ;
			if(simpleConfigItemList != null && simpleConfigItemList.size() > 0){
				sdConfigItemList.addAll(simpleConfigItemList);
			}
			for (PurchaseOrderItemModel itemModel : purchaseModel.getItemList()) {
				Map<String, Object> goodsQueryMap = new HashMap<String, Object>() ;
				goodsQueryMap.put("merchandiseId", itemModel.getGoodsId()) ;
				MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsQueryMap);

				goodsQueryMap.clear();
				goodsQueryMap.put("commbarcode", goods.getCommbarcode()) ;
				CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(goodsQueryMap);
				commbarcodeMap.put(itemModel.getBarcode(),commbarcode);
				if(!checkCommbarcode.contains(commbarcode.getCommbarcode())){//相同标准条码对应的配置不重复添加
					//获取多比例配置
					queryItemModel = new SdPurchaseConfigItemModel() ;
					queryItemModel.setConfigId(querySdConfigModel.getId());
					queryItemModel.setSdConfigSimpleType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
					queryItemModel.setCommbarcode(goods.getCommbarcode());
					List<SdPurchaseConfigItemModel> multipleConfigItemList = sdPurchaseConfigItemDao.list(queryItemModel) ;
					if(multipleConfigItemList != null && multipleConfigItemList.size() > 0){
						sdConfigItemList.addAll(multipleConfigItemList);
					}
					checkCommbarcode.add(commbarcode.getCommbarcode());
				}
			}
			sdConfigItemMap.put(code, sdConfigItemList);
		}
		//根据采购code_sd类型记录sd单据表头信息
		Map<String,PurchaseSdOrderModel> orderMap = new HashMap<>();
		//根据采购code_sd类型记录sd单据表体信息
		Map<String,List<PurchaseSdOrderSditemModel>> orderItemMap = new HashMap<>();

		for(String code : sdConfigItemMap.keySet()){
			PurchaseOrderModel queryOrderModel = purchaseMap.get(code);
			List<SdPurchaseConfigItemModel> sdConfigItemList = sdConfigItemMap.get(code);
			for(SdPurchaseConfigItemModel itemConfig : sdConfigItemList){
				String key = code + "_" + itemConfig.getSdConfigId();
				if(!orderMap.containsKey(key)){
					//检查采购单+采购SD类型是否已生成过SD单，若已生成，提示错误
					PurchaseSdOrderModel querySdOrder = new PurchaseSdOrderModel() ;
					querySdOrder.setPurchaseCode(queryOrderModel.getCode());
					querySdOrder.setSdTypeName(itemConfig.getSdConfigName());
					querySdOrder.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1);
					List<PurchaseSdOrderModel> sdList = purchaseSdOrderDao.list(querySdOrder);
					if(!sdList.isEmpty()) {
						LOG.info("采购订单："+ queryOrderModel.getCode() +" SD类型:"+itemConfig.getSdConfigName()+"已存在SD单，跳过");
						continue ;
					}

					PurchaseSdOrderModel purchaseSdOrderModel = new PurchaseSdOrderModel() ;
					purchaseSdOrderModel.setBuId(queryOrderModel.getBuId());
					purchaseSdOrderModel.setBuName(queryOrderModel.getBuName());
					purchaseSdOrderModel.setDepotId(queryOrderModel.getDepotId());
					purchaseSdOrderModel.setDepotName(queryOrderModel.getDepotName());
					purchaseSdOrderModel.setMerchantId(queryOrderModel.getMerchantId());
					purchaseSdOrderModel.setMerchantName(queryOrderModel.getMerchantName());
					purchaseSdOrderModel.setSupplierId(queryOrderModel.getSupplierId());
					purchaseSdOrderModel.setSupplierName(queryOrderModel.getSupplierName());
					purchaseSdOrderModel.setCurrency(queryOrderModel.getCurrency());
					purchaseSdOrderModel.setTgtCurrency(queryOrderModel.getTgtCurrency());
					purchaseSdOrderModel.setPoNo(queryOrderModel.getPoNo());
					purchaseSdOrderModel.setPurchaseCode(queryOrderModel.getCode());
					purchaseSdOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGSD));
					purchaseSdOrderModel.setCreateDate(TimeUtils.getNow());
					purchaseSdOrderModel.setCreateName("系统创建");
					purchaseSdOrderModel.setSdPurchaseConfigId(itemConfig.getConfigId());
					purchaseSdOrderModel.setSdSimpleName(itemConfig.getSdConfigSimpleName());
					purchaseSdOrderModel.setSdTypeName(itemConfig.getSdConfigName());
					purchaseSdOrderModel.setCreateDate(TimeUtils.getNow());
					purchaseSdOrderModel.setSdAmount(new BigDecimal(0));
					purchaseSdOrderModel.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1);
					purchaseSdOrderModel.setIsSyn("0");
					purchaseSdOrderModel.setStatus("001");
					//获取入库时间
					purchaseSdOrderModel.setInboundDate(purchaseInboundDateMap.get(queryOrderModel.getCode()));

					orderMap.put(key, purchaseSdOrderModel);
				}

				for (PurchaseOrderItemModel itemModel : queryOrderModel.getItemList()) {
					CommbarcodeMongo commbarcode = commbarcodeMap.get(itemModel.getBarcode());
					if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(itemConfig.getSdConfigSimpleType()) && !itemConfig.getCommbarcode().equals(commbarcode.getCommbarcode())) {
						continue;
					}
					BigDecimal sdAmountBd = itemModel.getAmount().multiply(itemConfig.getProportion());
					BigDecimal sdPrice = sdAmountBd.divide(new BigDecimal(itemModel.getNum()), 8, BigDecimal.ROUND_HALF_UP);

					PurchaseSdOrderSditemModel sdItem = new PurchaseSdOrderSditemModel() ;
					sdItem.setAmount(itemModel.getAmount());
					sdItem.setBarcode(itemModel.getBarcode());
					sdItem.setSdSimpleName(itemConfig.getSdConfigSimpleName());
					sdItem.setSdTypeName(itemConfig.getSdConfigName());
					sdItem.setGoodsId(itemModel.getGoodsId());
					sdItem.setGoodsName(itemModel.getGoodsName());
					sdItem.setGoodsNo(itemModel.getGoodsNo());
					sdItem.setNum(itemModel.getNum());
					sdItem.setPrice(itemModel.getPrice());
					sdItem.setCreateDate(TimeUtils.getNow());
					sdItem.setSdAmount(sdAmountBd.setScale(2,BigDecimal.ROUND_HALF_UP));
					sdItem.setSdPrice(sdPrice);
					sdItem.setPurchaseItemId(itemModel.getId());
					if(queryOrderModel.getRate() != null){
						BigDecimal tgtAmount = itemModel.getAmount().multiply(new BigDecimal(queryOrderModel.getRate())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
						BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(itemModel.getNum()), 8, BigDecimal.ROUND_HALF_UP);

						BigDecimal tgtSdAmount = sdItem.getSdAmount().multiply(new BigDecimal(queryOrderModel.getRate())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
						BigDecimal tgtSdPrice = tgtSdAmount.divide(new BigDecimal(itemModel.getNum()), 8, BigDecimal.ROUND_HALF_UP);

						sdItem.setTgtAmount(tgtAmount);
						sdItem.setTgtPrice(tgtPrice);
						sdItem.setSdTgtAmount(tgtSdAmount);
						sdItem.setSdTgtPrice(tgtSdPrice);
					}

					if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(itemConfig.getSdConfigSimpleType())) {
						sdItem.setCommbarcode(commbarcode.getCommbarcode());
						sdItem.setBrandParent(commbarcode.getCommBrandParentName());
					}

					List<PurchaseSdOrderSditemModel> itemList = orderItemMap.get(key);
					if(itemList == null){
						itemList = new ArrayList<>();
					}
					itemList.add(sdItem);
					orderItemMap.put(key, itemList);
				}
			}
		}
		for(String key : orderMap.keySet()){
			PurchaseSdOrderModel purchaseSdOrderModel = orderMap.get(key);
			List<PurchaseSdOrderSditemModel> sdOrderItemList = orderItemMap.get(key);
			BigDecimal totalSdAmount = sdOrderItemList.stream().map(PurchaseSdOrderSditemModel :: getSdAmount ).reduce(BigDecimal.ZERO, BigDecimal::add);
			purchaseSdOrderModel.setSdAmount(totalSdAmount);
			Long num = purchaseSdOrderDao.save(purchaseSdOrderModel) ;

			for(PurchaseSdOrderSditemModel sdItemModel : sdOrderItemList){
				sdItemModel.setOrderId(num);
				purchaseSdOrderSditemDao.save(sdItemModel) ;
			}
		}

//		for (String code : cacheMap) {
//
//			PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
//			queryModel.setCode(code);
//
//			PurchaseOrderModel purchaseModel = purchaseOrderDao.getDetails(queryModel);
//			//根据商家事业部供应商查询是否存在采购SD配置
//			SdPurchaseConfigModel querySdConfigModel = sdConfigMap.get(purchaseModel.getMerchantId()+"_"+purchaseModel.getSupplierId()+"_"+purchaseModel.getBuId()) ;
//
//			/**获取入库时间*/
////			List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(purchaseModel.getId());
//
////			if(!warehouseList.isEmpty()) {
////				PurchaseWarehouseModel purchaseWarehouseModel = warehouseList.get(0);
////				inboundDate = purchaseWarehouseModel.getInboundDate() ;
////			}
//			Timestamp inboundDate = purchaseInboundDateMap.get(purchaseModel.getCode()) ;
//
//			/**获取根据订单获取汇率*/
//			Double rate = purchaseModel.getRate();
//
//			//查询SD配置表体
//			SdPurchaseConfigItemModel querySdConfigItemModel = new SdPurchaseConfigItemModel() ;
//			querySdConfigItemModel.setConfigId(querySdConfigModel.getId());
//
//			List<SdPurchaseConfigItemModel> sdConfigItemList = sdPurchaseConfigItemDao.list(querySdConfigItemModel);
//			Map<String, List<PurchaseSdOrderSditemModel>> resultMap = new HashMap<String, List<PurchaseSdOrderSditemModel>>();
//			for (SdPurchaseConfigItemModel sdConfigItem : sdConfigItemList) {
//
//				SdTypeConfigModel queryConfigModel = new SdTypeConfigModel() ;
//				queryConfigModel.setSdTypeName(sdConfigItem.getSdConfigName());
//				queryConfigModel = sdTypeConfigDao.searchByModel(queryConfigModel) ;
//
//				List<PurchaseOrderItemModel> itemList = purchaseModel.getItemList();
//				List<PurchaseSdOrderSditemModel> sdItemList = new ArrayList<PurchaseSdOrderSditemModel>() ;
//				String key = querySdConfigModel.getId() + "_" + queryConfigModel.getSdTypeName() + "_" +queryConfigModel.getSdSimpleName() ;
//				for (PurchaseOrderItemModel purchaseItem : itemList) {
//
//					BigDecimal sdAmount = purchaseItem.getAmount();
//					BigDecimal proportion = sdConfigItem.getProportion();
//
//					sdAmount = sdAmount.multiply(proportion).setScale(2, BigDecimal.ROUND_HALF_UP) ;
//
//					BigDecimal sdPrice = sdAmount.divide(new BigDecimal(purchaseItem.getNum()), 8, BigDecimal.ROUND_HALF_UP);
//
//					PurchaseSdOrderSditemModel sdItem = new PurchaseSdOrderSditemModel() ;
//					sdItem.setAmount(purchaseItem.getAmount());
//					sdItem.setBarcode(purchaseItem.getBarcode());
//					sdItem.setGoodsId(purchaseItem.getGoodsId());
//					sdItem.setGoodsName(purchaseItem.getGoodsName());
//					sdItem.setGoodsNo(purchaseItem.getGoodsNo());
//					sdItem.setNum(purchaseItem.getNum());
//					sdItem.setPrice(purchaseItem.getPrice());
//					sdItem.setTgtPrice(purchaseItem.getTgtPrice());
//					sdItem.setCreateDate(TimeUtils.getNow());
//					sdItem.setSdAmount(sdAmount);
//					sdItem.setSdPrice(sdPrice);
//					sdItem.setSdTypeName(queryConfigModel.getSdTypeName());
//					sdItem.setSdSimpleName(queryConfigModel.getSdSimpleName());
//					sdItem.setTgtAmount(purchaseItem.getTgtAmount());
//					sdItem.setTgtPrice(purchaseItem.getTgtPrice());
//
//					if(rate != null) {
////						BigDecimal tgtAmount = purchaseItem.getAmount().multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;
////						BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(purchaseItem.getNum()), 8, BigDecimal.ROUND_HALF_UP);
//
//						BigDecimal tgtSdAmount = sdAmount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP) ;
//						BigDecimal tgtSdPrice = tgtSdAmount.divide(new BigDecimal(purchaseItem.getNum()), 8, BigDecimal.ROUND_HALF_UP);
//
//						sdItem.setSdTgtAmount(tgtSdAmount);
//						sdItem.setSdTgtPrice(tgtSdPrice);
//
//					}
//
//					if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(sdConfigItem.getSdConfigSimpleType())) {
//
//						Map<String, Object> goodsQueryMap = new HashMap<String, Object>() ;
//						goodsQueryMap.put("merchandiseId", purchaseItem.getGoodsId()) ;
//
//						MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsQueryMap);
//
//						goodsQueryMap.clear();
//						goodsQueryMap.put("commbarcode", goods.getCommbarcode()) ;
//
//						CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(goodsQueryMap);
//
//						if(commbarcode == null ||
//								!commbarcode.getCommbarcode().equals(sdConfigItem.getCommbarcode())) {
//							continue ;
//						}
//
//						sdItem.setCommbarcode(commbarcode.getCommbarcode());
//						sdItem.setBrandParent(commbarcode.getCommBrandParentName());
//					}
//					sdItem.setPurchaseItemId(purchaseItem.getId());
//
//					if (resultMap.containsKey(key)) {
//						sdItemList = resultMap.get(key);
//					}
//					sdItemList.add(sdItem) ;
//				}
//
//				if(sdItemList.isEmpty()) {
//					continue ;
//				}
//				resultMap.put(key, sdItemList);
//
//			}
//			for (String key : resultMap.keySet()) {
//				String sdTypeId = key.split("_")[0];
//				String sdType = key.split("_")[1];
//				String sdTypeName = key.split("_")[2];
//
//				//检查采购单+采购SD类型是否已生成过SD单，若已生成，提示错误
//				PurchaseSdOrderModel querySdOrder = new PurchaseSdOrderModel() ;
//				querySdOrder.setPurchaseCode(code);
//				querySdOrder.setSdTypeName(sdType);
//				querySdOrder.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1);
//				List<PurchaseSdOrderModel> sdList = purchaseSdOrderDao.list(querySdOrder);
//				if(!sdList.isEmpty()) {
//					LOG.info("采购订单："+ code +" SD类型"+sdType+"已存在SD单，跳过");
//					continue ;
//				}
//
//				PurchaseSdOrderModel purchaseSdOrderModel = new PurchaseSdOrderModel() ;
//
//				purchaseSdOrderModel.setBuId(purchaseModel.getBuId());
//				purchaseSdOrderModel.setBuName(purchaseModel.getBuName());
//				purchaseSdOrderModel.setDepotId(purchaseModel.getDepotId());
//				purchaseSdOrderModel.setDepotName(purchaseModel.getDepotName());
//				purchaseSdOrderModel.setMerchantId(purchaseModel.getMerchantId());
//				purchaseSdOrderModel.setMerchantName(purchaseModel.getMerchantName());
//				purchaseSdOrderModel.setSupplierId(purchaseModel.getSupplierId());
//				purchaseSdOrderModel.setSupplierName(purchaseModel.getSupplierName());
//				purchaseSdOrderModel.setCurrency(purchaseModel.getCurrency());
//				purchaseSdOrderModel.setTgtCurrency(purchaseModel.getTgtCurrency());
//				purchaseSdOrderModel.setPoNo(purchaseModel.getPoNo());
//				purchaseSdOrderModel.setPurchaseCode(purchaseModel.getCode());
//				purchaseSdOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGSD));
//				purchaseSdOrderModel.setCreateDate(TimeUtils.getNow());
//				purchaseSdOrderModel.setCreateName("系统自动生成");
//				purchaseSdOrderModel.setSdPurchaseConfigId(querySdConfigModel.getId());
//				purchaseSdOrderModel.setSdSimpleName(sdTypeName);
//				purchaseSdOrderModel.setSdTypeName(sdType);
//				purchaseSdOrderModel.setCreateDate(TimeUtils.getNow());
//				purchaseSdOrderModel.setSdAmount(new BigDecimal(0));
//				purchaseSdOrderModel.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1);
//				purchaseSdOrderModel.setIsSyn("0");
//				purchaseSdOrderModel.setInboundDate(inboundDate);
//
//				if(rate != null) {
//					purchaseSdOrderModel.setRate(rate);
//				}
//				Long num = purchaseSdOrderDao.save(purchaseSdOrderModel);
//
//				List<PurchaseSdOrderSditemModel> itemList = resultMap.get(key);
//				for (PurchaseSdOrderSditemModel itemModel : itemList) {
//					itemModel.setOrderId(num);
//					itemModel.setCreateDate(TimeUtils.getNow());
//					purchaseSdOrderSditemDao.save(itemModel);
//
//				}
//			}
//		}
	}
	/**
	 * 获取商家id集合
	 * @return
	 */
	private List<Long> getMerchantIds(String merchantIds){
		List<Long> merchantIdList = new ArrayList<Long>();
		Map<String,Object> merchantMap = new HashMap<String,Object>();
		MerchantInfoMongo merchantInfo = new MerchantInfoMongo();
		if(StringUtils.isBlank(merchantIds)) {//默认获取健太、嘉宝、卓烨、宝信
			merchantMap.put("code", "ERP31194100049");//健太
			merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
			merchantIdList.add(merchantInfo.getMerchantId());

			merchantMap.clear();
			merchantMap.put("code", "ERP16114000043");//嘉宝
			merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
			merchantIdList.add(merchantInfo.getMerchantId());

			merchantMap.clear();
			merchantMap.put("code", "ERP26143500022");//卓烨贸易
			merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
			merchantIdList.add(merchantInfo.getMerchantId());

			merchantMap.clear();
			merchantMap.put("code", "ERP31194100022");//宝信
			merchantInfo = merchantInfoMongoDao.findOne(merchantMap);
			merchantIdList.add(merchantInfo.getMerchantId());
		}else {
			String[] merchantIdArr = merchantIds.split(",");
			for(String merchantId : merchantIdArr) {
				merchantIdList.add(Long.valueOf(merchantId));
			}
		}

		return merchantIdList;
	}
}
