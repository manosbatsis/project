package com.topideal.order.service.purchase.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.common.SdPurchaseConfigDao;
import com.topideal.dao.common.SdPurchaseConfigItemDao;
import com.topideal.dao.common.SdTypeConfigDao;
import com.topideal.dao.purchase.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.purchase.PurchaseSdOrderDTO;
import com.topideal.entity.dto.purchase.PurchaseSdOrderPageDTO;
import com.topideal.entity.vo.common.SdPurchaseConfigItemModel;
import com.topideal.entity.vo.common.SdPurchaseConfigModel;
import com.topideal.entity.vo.common.SdTypeConfigModel;
import com.topideal.entity.vo.purchase.*;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseSdOrderService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseSdOrderServiceImpl implements PurchaseSdOrderService {
	private static final Logger LOGGER = Logger.getLogger(PurchaseSdOrderServiceImpl.class) ;

	@Autowired
	private PurchaseSdOrderDao purchaseSdOrderDao ;
	@Autowired
	private PurchaseSdOrderSditemDao purchaseSdOrderSditemDao ;
	@Autowired
    private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	@Autowired
	private SdPurchaseConfigItemDao sdPurchaseConfigItemDao ;
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	@Autowired
	private CommonBusinessService commonBusinessService ;
	@Autowired
	private SdPurchaseConfigDao sdPurchaseConfigDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao ;
	@Autowired
	private SdTypeConfigDao sdTypeConfigDao ;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private CommbarcodeMongoDao commbarcodeMongoDao ;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao ;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private CustomerMerchantRelMongoDao customerMerchantRelMongoDao ;
	@Autowired
	private MerchantDepotBuRelMongoDao merchantDepotBuRelMongoDao ;
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao ;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao ;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao ;

	@SuppressWarnings("unchecked")
	@Override
	public PurchaseSdOrderPageDTO getPurchaseSdOrderPageList(PurchaseSdOrderPageDTO dto) throws SQLException {

		//po号换行处理
		if(StringUtils.isNotBlank(dto.getPoNos())
				&& dto.getPoNos().contains("\r\n")) {
			String poNos = dto.getPoNos() ;

			poNos = poNos.replaceAll("\r\n", "&") ;

			dto.setPoNos(poNos);
		}

		dto = purchaseSdOrderDao.getPurchaseSdOrderPageList(dto) ;

		List<PurchaseSdOrderPageDTO> list = (List<PurchaseSdOrderPageDTO>)dto.getList();

		for (PurchaseSdOrderPageDTO purchaseSdOrderPageDTO : list) {

			if(purchaseSdOrderPageDTO.getInboundDate() == null) {
				purchaseSdOrderPageDTO.setEditAble("1");

				continue ;
			}

			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(purchaseSdOrderPageDTO.getMerchantId());
			closeAccountsMongo.setDepotId(purchaseSdOrderPageDTO.getDepotId());
			closeAccountsMongo.setBuId(purchaseSdOrderPageDTO.getBuId());

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (purchaseSdOrderPageDTO.getInboundDate().getTime() < Timestamp
						.valueOf(maxCloseAccountsMonth).getTime()) {
					purchaseSdOrderPageDTO.setEditAble("0");
					continue;
				}
			}

			purchaseSdOrderPageDTO.setEditAble("1");

		}

		dto.setList(list);

		return dto;
	}

	@Override
	public PurchaseSdOrderDTO searchDTOById(Long id) {
		return purchaseSdOrderDao.searchDTOById(id);
	}

	@Override
	public List<PurchaseSdOrderSditemModel> getSdItemList(Long id) throws SQLException {

		PurchaseSdOrderSditemModel queryModel = new PurchaseSdOrderSditemModel() ;
		queryModel.setOrderId(id);

		return purchaseSdOrderSditemDao.list(queryModel) ;
	}

	@Override
	public List<PurchaseSdOrderDTO> listDTO(PurchaseSdOrderPageDTO dto) {
		return purchaseSdOrderDao.listDTO(dto);
	}

	@Override
	public Map<String, Object> getAmountAdjustmentDetail(Long id) throws SQLException {

		PurchaseSdOrderDTO dto = purchaseSdOrderDao.searchDTOById(id);

		Map<String,Object> map = new HashMap<String, Object>() ;

		BigDecimal totalAmount = new BigDecimal(0) ;
		BigDecimal totalSdAmount = new BigDecimal(0) ;

		PurchaseSdOrderSditemModel querySdItemModel = new PurchaseSdOrderSditemModel() ;
		querySdItemModel.setOrderId(id);

		List<PurchaseSdOrderSditemModel> itemList = purchaseSdOrderSditemDao.list(querySdItemModel);

		for (PurchaseSdOrderSditemModel purchaseSdOrderSditemModel : itemList) {

			if(purchaseSdOrderSditemModel.getAmount() != null) {
				totalAmount = totalAmount.add(purchaseSdOrderSditemModel.getAmount()) ;
			}

			if(purchaseSdOrderSditemModel.getSdAmount() != null) {
				totalSdAmount = totalSdAmount.add(purchaseSdOrderSditemModel.getSdAmount()) ;
			}
		}

		map.put("order", dto) ;
		map.put("itemList", itemList) ;
		map.put("totalSdAmount", totalSdAmount) ;
		map.put("totalAmount", totalAmount) ;

		return map;
	}

	@Override
	public void saveAmountAdjustment(String id, String itemList) throws Exception {

		PurchaseSdOrderModel sdOrder = purchaseSdOrderDao.searchById(Long.valueOf(id));

		JSONArray itemArray = JSONArray.fromObject(itemList);

		BigDecimal totalSdAmount = new BigDecimal(0) ;

		for (Object object : itemArray) {
			JSONObject itemJson = JSONObject.fromObject(object);

			long itemId = itemJson.getLong("id");

			PurchaseSdOrderSditemModel item = purchaseSdOrderSditemDao.searchById(itemId);

			double sdAmount = itemJson.getDouble("sdAmount");

			if(!DERP_ORDER.PURCHASE_SD_ORDER_TYPE_3.equals(sdOrder.getType())
					&& sdAmount < 0) {
				throw new DerpException("SD金额不能为负数") ;
			}

			BigDecimal sdAmountBd = new BigDecimal(sdAmount);
			BigDecimal sdPrice = null;

			if(item.getNum() != null) {
				sdPrice = sdAmountBd.divide(new BigDecimal(item.getNum()), 8, BigDecimal.ROUND_HALF_UP) ;
			}

			totalSdAmount = totalSdAmount.add(sdAmountBd) ;

			item.setSdAmount(sdAmountBd);
			item.setModifyDate(TimeUtils.getNow());
			item.setSdPrice(sdPrice);

			purchaseSdOrderSditemDao.modify(item) ;
		}

		PurchaseSdOrderModel updateModel = new PurchaseSdOrderModel() ;
		updateModel.setId(Long.valueOf(id));
		updateModel.setSdAmount(totalSdAmount);
		updateModel.setIsSyn("0");
		updateModel.setModifyDate(TimeUtils.getNow());

		purchaseSdOrderDao.modify(updateModel) ;

		/**若为采购SD*/
		if(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1.equals(sdOrder.getType())) {
			PurchaseOrderModel queryPurchaseOrder = new PurchaseOrderModel() ;
			queryPurchaseOrder.setCode(sdOrder.getPurchaseCode());

			queryPurchaseOrder = purchaseOrderDao.searchByModel(queryPurchaseOrder) ;

			List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(queryPurchaseOrder.getId());

			for (PurchaseWarehouseModel purchaseWarehouseModel : warehouseList) {
				commonBusinessService.saveRate(queryPurchaseOrder, purchaseWarehouseModel.getInboundDate());
			}
		}
		/**若为调整SD*/
		else if(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_3.equals(sdOrder.getType())) {

			Double rate = sdOrder.getRate() ;

			if(rate == null) {
				Map<String, Object> queryRateMap = new HashMap<String, Object>();
				queryRateMap.put("origCurrencyCode", sdOrder.getCurrency());
				queryRateMap.put("tgtCurrencyCode", sdOrder.getTgtCurrency());
				queryRateMap.put("effectiveDate", TimeUtils.format(sdOrder.getInboundDate(), "yyyy-MM-dd"));

				ExchangeRateMongo rateMongo = exchangeRateMongoDao.findOne(queryRateMap);

				// 设置表体本位币单价，金额
				if (rateMongo != null && rateMongo.getRate() != null) {

					updateModel = new PurchaseSdOrderModel() ;
					updateModel.setId(Long.valueOf(id));
					updateModel.setRate(rateMongo.getRate());
					updateModel.setIsSyn("0");
					updateModel.setModifyDate(TimeUtils.getNow());

					purchaseSdOrderDao.modify(updateModel) ;

					rate = rateMongo.getRate() ;

				}
			}

			if(rate != null) {

				PurchaseSdOrderSditemModel queryItemModel = new PurchaseSdOrderSditemModel() ;
				queryItemModel.setOrderId(sdOrder.getId());

				List<PurchaseSdOrderSditemModel> sdItemList = purchaseSdOrderSditemDao.list(queryItemModel);

				for (PurchaseSdOrderSditemModel purchaseSdOrderSditemModel : sdItemList) {

					BigDecimal sdAmount = purchaseSdOrderSditemModel.getSdAmount();
					BigDecimal tgtSdAmount = sdAmount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

					purchaseSdOrderSditemModel.setSdTgtAmount(tgtSdAmount);
					purchaseSdOrderSditemModel.setModifyDate(TimeUtils.getNow());

					purchaseSdOrderSditemDao.modify(purchaseSdOrderSditemModel) ;

				}
			}

		}

	}

	@Override
	public void saveSdOrder(String id, User user) throws Exception {
		PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel();
		purchaseOrderModel.setId(Long.valueOf(id));
		purchaseOrderModel = purchaseOrderDao.getDetails(purchaseOrderModel);
		if(purchaseOrderModel == null) {
			throw new RuntimeException("采购订单不存在") ;
		}

		//根据商家事业部供应商查询是否存在采购SD配置
		SdPurchaseConfigModel querySdConfigModel = new SdPurchaseConfigModel() ;
		querySdConfigModel.setMerchantId(purchaseOrderModel.getMerchantId());
		querySdConfigModel.setSupplierId(purchaseOrderModel.getSupplierId());
		querySdConfigModel.setBuId(purchaseOrderModel.getBuId());
		querySdConfigModel.setStatus(DERP_ORDER.SDPURCHASE_STATUS_1);

		querySdConfigModel = sdPurchaseConfigDao.getLastestModel(querySdConfigModel) ;

		if(querySdConfigModel == null) {
			throw new DerpException("该供应商不存在SD配置");
		}
		//获取单比例配置
		List<SdPurchaseConfigItemModel> sdConfigItemList = new ArrayList<>();
		SdPurchaseConfigItemModel queryItemModel = new SdPurchaseConfigItemModel() ;
		queryItemModel.setConfigId(querySdConfigModel.getId());
		queryItemModel.setSdConfigSimpleType(DERP_ORDER.SDTYPECONFIG_TYPE_1);
		List<SdPurchaseConfigItemModel> simpleConfigItemList = sdPurchaseConfigItemDao.list(queryItemModel) ;
		if(simpleConfigItemList != null && simpleConfigItemList.size() > 0){
			sdConfigItemList.addAll(simpleConfigItemList);
		}

		Map<String,CommbarcodeMongo> commbarcodeMap = new HashMap<>();
		for (PurchaseOrderItemModel itemModel : purchaseOrderModel.getItemList()) {
			if(!commbarcodeMap.containsKey(itemModel.getBarcode())){//相同条形码对应的配置不重复添加
				Map<String, Object> goodsQueryMap = new HashMap<String, Object>() ;
				goodsQueryMap.put("merchandiseId", itemModel.getGoodsId()) ;
				MerchandiseInfoMogo goods = merchandiseInfoMogoDao.findOne(goodsQueryMap);

				goodsQueryMap.clear();
				goodsQueryMap.put("commbarcode", goods.getCommbarcode()) ;
				CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(goodsQueryMap);
				commbarcodeMap.put(itemModel.getBarcode(),commbarcode);


				//获取多比例配置
				queryItemModel = new SdPurchaseConfigItemModel() ;
				queryItemModel.setConfigId(querySdConfigModel.getId());
				queryItemModel.setSdConfigSimpleType(DERP_ORDER.SDTYPECONFIG_TYPE_2);
				queryItemModel.setCommbarcode(goods.getCommbarcode());
				List<SdPurchaseConfigItemModel> multipleConfigItemList = sdPurchaseConfigItemDao.list(queryItemModel) ;
				if(multipleConfigItemList != null && multipleConfigItemList.size() > 0){
					sdConfigItemList.addAll(multipleConfigItemList);
				}
			}
		}


		Map<Long,PurchaseSdOrderModel> orderMap = new HashMap<>();
		Map<Long,List<PurchaseSdOrderSditemModel>> orderItemMap = new HashMap<>();
		for(SdPurchaseConfigItemModel itemConfig : sdConfigItemList){
			Long key = itemConfig.getSdConfigId();
			if(!orderMap.containsKey(key)){
				//检查采购单+采购SD类型是否已生成过SD单，若已生成，提示错误
				PurchaseSdOrderModel querySdOrder = new PurchaseSdOrderModel() ;
				querySdOrder.setPurchaseCode(purchaseOrderModel.getCode());
				querySdOrder.setSdTypeName(itemConfig.getSdConfigName());
				querySdOrder.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1);
				List<PurchaseSdOrderModel> sdList = purchaseSdOrderDao.list(querySdOrder);
				if(!sdList.isEmpty()) {
					LOGGER.info("采购订单："+ purchaseOrderModel.getCode() +" SD类型:"+itemConfig.getSdConfigName()+"已存在SD单，跳过");
					continue ;
				}

				PurchaseSdOrderModel purchaseSdOrderModel = new PurchaseSdOrderModel() ;
				purchaseSdOrderModel.setBuId(purchaseOrderModel.getBuId());
				purchaseSdOrderModel.setBuName(purchaseOrderModel.getBuName());
				purchaseSdOrderModel.setDepotId(purchaseOrderModel.getDepotId());
				purchaseSdOrderModel.setDepotName(purchaseOrderModel.getDepotName());
				purchaseSdOrderModel.setMerchantId(purchaseOrderModel.getMerchantId());
				purchaseSdOrderModel.setMerchantName(purchaseOrderModel.getMerchantName());
				purchaseSdOrderModel.setSupplierId(purchaseOrderModel.getSupplierId());
				purchaseSdOrderModel.setSupplierName(purchaseOrderModel.getSupplierName());
				purchaseSdOrderModel.setCurrency(purchaseOrderModel.getCurrency());
				purchaseSdOrderModel.setTgtCurrency(purchaseOrderModel.getTgtCurrency());
				purchaseSdOrderModel.setPoNo(purchaseOrderModel.getPoNo());
				purchaseSdOrderModel.setPurchaseCode(purchaseOrderModel.getCode());
				purchaseSdOrderModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGSD));
				purchaseSdOrderModel.setCreateDate(TimeUtils.getNow());
				purchaseSdOrderModel.setCreateName(user.getName());
				purchaseSdOrderModel.setCreater(user.getId());
				purchaseSdOrderModel.setSdPurchaseConfigId(itemConfig.getConfigId());
				purchaseSdOrderModel.setSdSimpleName(itemConfig.getSdConfigSimpleName());
				purchaseSdOrderModel.setSdTypeName(itemConfig.getSdConfigName());
				purchaseSdOrderModel.setCreateDate(TimeUtils.getNow());
				purchaseSdOrderModel.setSdAmount(new BigDecimal(0));
				purchaseSdOrderModel.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1);
				purchaseSdOrderModel.setIsSyn("0");
				purchaseSdOrderModel.setStatus("001");
				//获取入库时间
				List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(purchaseOrderModel.getId());
				Timestamp inboundDate = null;
				if(warehouseList != null && warehouseList.size() > 0){
					PurchaseWarehouseModel purchaseWarehouseModel = warehouseList.get(0);
					inboundDate = purchaseWarehouseModel.getInboundDate() ;
				}
				purchaseSdOrderModel.setInboundDate(inboundDate);

				orderMap.put(key, purchaseSdOrderModel);
			}

			for (PurchaseOrderItemModel itemModel : purchaseOrderModel.getItemList()) {
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
				if(purchaseOrderModel.getRate() != null){
					BigDecimal tgtAmount = itemModel.getAmount().multiply(new BigDecimal(purchaseOrderModel.getRate())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
					BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(itemModel.getNum()), 8, BigDecimal.ROUND_HALF_UP);

					BigDecimal tgtSdAmount = sdItem.getSdAmount().multiply(new BigDecimal(purchaseOrderModel.getRate())).setScale(2, BigDecimal.ROUND_HALF_UP) ;
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
		for(Long key : orderMap.keySet()){
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

		List<PurchaseWarehouseModel> warehouseList = warehouseOrderRelDao.getInboundDateById(purchaseOrderModel.getId());

		for (PurchaseWarehouseModel purchaseWarehouseModel : warehouseList) {
			commonBusinessService.saveRate(purchaseOrderModel, purchaseWarehouseModel.getInboundDate());
		}

	}

	@Override
	public List<PurchaseSdOrderPageDTO> getExportSdOrder(PurchaseSdOrderPageDTO dto) throws SQLException {

		//po号换行处理
		if(StringUtils.isNotBlank(dto.getPoNos())
				&& dto.getPoNos().contains("\r\n")) {
			String poNos = dto.getPoNos() ;

			poNos = poNos.replaceAll("\r\n", "&") ;

			dto.setPoNos(poNos);
		}

		List<PurchaseSdOrderPageDTO> exportList = purchaseSdOrderDao.getExportSdOrder(dto);

		for (PurchaseSdOrderPageDTO purchaseSdOrderPageDTO : exportList) {

			Long orderId = purchaseSdOrderPageDTO.getId();

			PurchaseSdOrderSditemModel sdItem = new PurchaseSdOrderSditemModel() ;
			sdItem.setOrderId(orderId);

			List<PurchaseSdOrderSditemModel> sdItemList = purchaseSdOrderSditemDao.list(sdItem);

			if(!sdItemList.isEmpty() &&
					sdItemList.get(0).getNum() == null) {
				continue ;
			}

			Integer totalNum = sdItemList.stream().map(PurchaseSdOrderSditemModel::getNum).reduce(Integer::sum).get();

			purchaseSdOrderPageDTO.setTotalNum(totalNum);
		}

		return exportList;
	}

	@Override
	public Map<String, Object> importOrder(List<List<Map<String, String>>> data, User user) throws Exception {

		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		Map<String, PurchaseSdOrderModel> orderMap = new HashMap<String, PurchaseSdOrderModel>() ;
		Map<String, List<PurchaseSdOrderSditemModel>> itemMap = new HashMap<String, List<PurchaseSdOrderSditemModel>>() ;

		/**表头*/
		List<Map<String, String>> orderData = data.get(0);

		for (int i = 1 ; i <= orderData.size() ; i++) {

			Map<String, String> map = orderData.get(i - 1);

			String index = map.get("序号");

			if(checkIsNullOrNot(i, index, "序号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			index = index.trim() ;

			String buCode = map.get("事业部编码");
			if (checkIsNullOrNot(i, buCode, "事业部编码不能为空", resultList)) {
				failure += 1;
				continue;
			}
			buCode = buCode.trim() ;

			String depotCode = map.get("仓库编码");
			if (checkIsNullOrNot(i, depotCode, "仓库编码不能为空", resultList)) {
				failure += 1;
				continue;
			}
			depotCode = depotCode.trim() ;

			// 判断仓库是否存在
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("depotCode", depotCode);
			DepotInfoMongo depot = depotInfoMongoDao.findOne(params);
			if (depot == null) {
				setErrorMsg(i, "该仓库不存在", resultList);
				failure += 1;
				continue;
			}

			Map<String, Object> relParams = new HashMap<String, Object>() ;
			relParams.put("buCode", buCode);
			relParams.put("merchantId", user.getMerchantId());
			relParams.put("status", DERP_SYS.MERCHANT_BU_REL_STATUS_1) ;
			MerchantBuRelMongo merchantBuRelMongo = merchantBuRelMongoDao.findOne(relParams);
			if(merchantBuRelMongo == null) {
				setErrorMsg(i, "公司事业部关联不存在或已禁用", resultList);
				failure += 1;
				continue;
			}

			relParams = new HashMap<String, Object>() ;
			relParams.put("buId", merchantBuRelMongo.getBuId());
			relParams.put("merchantId", user.getMerchantId());
			relParams.put("depotId", depot.getDepotId()) ;
			MerchantDepotBuRelMongo merchantDepotBuRelMongo = merchantDepotBuRelMongoDao.findOne(relParams);
			if(merchantDepotBuRelMongo == null) {
				setErrorMsg(i, "入库仓库未绑定该事业部", resultList);
				failure += 1;
				continue;
			}

			String supplierCode = map.get("供应商编码");
			if (checkIsNullOrNot(i, supplierCode, "供应商编码不能为空", resultList)) {
				failure += 1;
				continue;
			}
			supplierCode = supplierCode.trim() ;

			String currency = map.get("币种");
			if (checkIsNullOrNot(i, currency, "币种不能为空", resultList)) {
				failure += 1;
				continue;
			}
			currency = currency.trim() ;
			if (StringUtils.isBlank(DERP.getLabelByKey(DERP.currencyCodeList, currency))) {
				setErrorMsg(i, "币种输入值无效", resultList);
				failure += 1;
				continue;
			}

			String attributionDate = map.get("调整归属日期") ;
			if (checkIsNullOrNot(i, attributionDate, "调整归属日期不能为空", resultList)) {
				failure += 1;
				continue;
			}
			attributionDate = attributionDate.trim() ;

			if(!isValidDate(attributionDate)) {
				setErrorMsg(i, "该归属时间格式有误，格式为：yyyy-MM-dd", resultList);
				failure += 1;
				continue;
			}

			if(TimeUtils.daysBetween(TimeUtils.parse(attributionDate, "yyyy-MM-dd"), new Date()) < 0) {
				setErrorMsg(i, "归属时间不可超过当前时间", resultList);
				failure += 1;
				continue;
			}

			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(depot.getDepotId());
			closeAccountsMongo.setBuId(merchantBuRelMongo.getBuId());

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (Timestamp.valueOf(attributionDate + " 00:00:00").getTime() < Timestamp
						.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(i, "归属时间必须大于关账日期", resultList);
					failure += 1;
					continue;
				}
			}

			String remarks = map.get("调整备注") ;
			if (checkIsNullOrNot(i, remarks, "调整备注不能为空", resultList)) {
				failure += 1;
				continue;
			}
			remarks = remarks.trim() ;

			// 判断供应商是否存在
			params = new HashMap<String, Object>();
			params.put("code", supplierCode) ;
			params.put("merchantId", user.getMerchantId()) ;
			params.put("status", DERP_SYS.CUSTOMERINFO_STATUS_1) ;
			CustomerMerchantRelMongo customer = customerMerchantRelMongoDao.findOne(params);
			if (customer == null) {
				setErrorMsg(i, "该供应商不存在", resultList);
				failure += 1;
				continue;
			}

			boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), merchantBuRelMongo.getBuId());
			if(!isRelate) {
				setErrorMsg(i, "用户无权限操作该事业部", resultList);
				failure += 1;
				continue;
			}

			String sdTypeName = map.get("SD类型") ;
			if (checkIsNullOrNot(i, sdTypeName, "SD类型不能为空", resultList)) {
				failure += 1;
				continue;
			}
			sdTypeName = sdTypeName.trim() ;

			SdTypeConfigModel queryConfigModel = new SdTypeConfigModel() ;

			queryConfigModel.setStatus(DERP_ORDER.SDTYPECONFIG_STATUS_1);
			queryConfigModel.setSdTypeName(sdTypeName);

			queryConfigModel = sdTypeConfigDao.searchByModel(queryConfigModel) ;

			if(queryConfigModel == null) {
				setErrorMsg(i, "SD类型不存在或未启用", resultList);
				failure += 1;
				continue;
			}

			Map<String, Object> merchantMap = new HashMap<String, Object>() ;
			merchantMap.put("merchantId", user.getMerchantId()) ;

			MerchantInfoMongo merchant = merchantInfoMongoDao.findOne(merchantMap);

			PurchaseSdOrderModel sdModel = new PurchaseSdOrderModel() ;
			sdModel.setBuId(merchantBuRelMongo.getBuId());
			sdModel.setBuName(merchantBuRelMongo.getBuName());
			sdModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGSD));
			sdModel.setCurrency(currency);
			sdModel.setTgtCurrency(merchant.getAccountCurrency());
			sdModel.setSupplierId(customer.getCustomerId());
			sdModel.setSupplierName(customer.getName());
			sdModel.setInboundDate(TimeUtils.parse(attributionDate, "yyyy-MM-dd"));
			sdModel.setMerchantId(user.getMerchantId());
			sdModel.setMerchantName(user.getMerchantName());
			sdModel.setSdTypeName(sdTypeName);
			sdModel.setSdSimpleName(queryConfigModel.getSdSimpleName());
			sdModel.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_3);
			sdModel.setDepotId(depot.getDepotId());
			sdModel.setDepotName(depot.getName());
			sdModel.setCreateDate(TimeUtils.getNow());
			sdModel.setCreateName(user.getName());
			sdModel.setCreater(user.getId());
			sdModel.setIsSyn("0");
			sdModel.setSdAmount(new BigDecimal(0));
			sdModel.setRemarks(remarks);

			String poNo = map.get("PO号") ;
			if(StringUtils.isNotBlank(poNo)) {

				poNo = poNo.trim() ;

				PurchaseOrderModel queryModel = new PurchaseOrderModel() ;
				queryModel.setPoNo(poNo);
				queryModel.setMerchantId(user.getMerchantId());

				List<PurchaseOrderModel> purchaseOrderList = purchaseOrderDao.list(queryModel);

				if(purchaseOrderList.isEmpty()) {
					setErrorMsg(i, "根据PO号没有查询到采购订单", resultList);
					failure += 1;
					continue;
				}

				purchaseOrderList = purchaseOrderList.stream()
				.sorted(Comparator.comparing(PurchaseOrderModel::getCreateDate).reversed())
				.collect(Collectors.toList()) ;

				PurchaseOrderModel purchaseOrderModel = purchaseOrderList.get(0);

				sdModel.setPurchaseCode(purchaseOrderModel.getCode());
				sdModel.setPoNo(poNo);

			}

			if(currency.equals(merchant.getAccountCurrency())) {
				sdModel.setRate(1.0);
			}else {
				Map<String, Object> queryRateMap = new HashMap<String, Object>();
				queryRateMap.put("origCurrencyCode", currency);
				queryRateMap.put("tgtCurrencyCode", merchant.getAccountCurrency());
				queryRateMap.put("effectiveDate", attributionDate);

				ExchangeRateMongo rateMongo = exchangeRateMongoDao.findOne(queryRateMap);

				// 设置表体本位币单价，金额
				if (rateMongo != null && rateMongo.getRate() != null) {
					sdModel.setRate(rateMongo.getRate());
				}
			}

			orderMap.put(index, sdModel) ;
		}

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("success", success);
		returnMap.put("pass", pass);
		returnMap.put("failure", failure);
		returnMap.put("message", resultList);

		if(failure > 0) {
			return returnMap ;
		}

		/**表体*/
		List<Map<String, String>> itemData = data.get(1);

		for (int j = 1 ; j <= itemData.size() ; j++) {
			Map<String, String> map = itemData.get(j - 1);

			String index = map.get("序号");

			if(checkIsNullOrNot(j, index, "序号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			index = index.trim() ;

			String goodsNo = map.get("商品货号");

			if(checkIsNullOrNot(j, goodsNo, "商品货号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			goodsNo = goodsNo.trim() ;

			String amountStr = map.get("调整SD金额");

			if(checkIsNullOrNot(j, amountStr, "调整SD金额不能为空", resultList)) {
				failure += 1;
				continue;
			}
			amountStr = amountStr.trim() ;

			if(amountStr.indexOf(".") > -1 &&
					doubleyn(amountStr, 2)) {
				setErrorMsg(j, "调整金额应是2位小数的数字", resultList);
				failure += 1;
				continue;
			}

			BigDecimal sdItemSdAmount = new BigDecimal(amountStr);

			PurchaseSdOrderModel sdOrder = orderMap.get(index);

			if(sdOrder == null) {
				setErrorMsg(j, "【SD金额调整】序号：" + index + "不存在于【表头信息】", resultList);
				failure += 1;
				continue;
			}

			// 根据商品货号获取商品id
			Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();

			merchandiseInfo_params.put("goodsNo", goodsNo);
			merchandiseInfo_params.put("merchantId", sdOrder.getMerchantId());
			merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

			Map<String, Object> depotMerchantRelMap = new HashMap<String, Object>() ;
			depotMerchantRelMap.put("merchantId", sdOrder.getMerchantId()) ;
			depotMerchantRelMap.put("depotId", sdOrder.getDepotId()) ;
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotMerchantRelMap);

			if(depotMerchantRel == null) {
				setErrorMsg(j, "商家仓库关联不存在", resultList);
				failure += 1;
				continue;
			}

			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_1.equals(depotMerchantRel.getProductRestriction())) {
				merchandiseInfo_params.put("isRecord", DERP_SYS.MERCHANDISEINFO_ISRECORD_1);//是否备案 1-是
			}

			if (DERP_SYS.DEPOTMERCHANTREL_PRODUCTRESTRICTION_2.equals(depotMerchantRel.getProductRestriction())) {
				merchandiseInfo_params.put("outDepotFlag", DERP_SYS.MERCHANDISEINFO_OUTDEPOTFLAG_0);
			}

			MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
			if (merchandiseInfo == null) {
				setErrorMsg(j, "商品货号不存在", resultList);
				failure += 1;
				continue;
			}

			Map<String, Object> commbarcodeMap = new HashMap<String, Object>() ;
			commbarcodeMap.put("commbarcode", merchandiseInfo.getCommbarcode()) ;

			CommbarcodeMongo commbarcode = commbarcodeMongoDao.findOne(commbarcodeMap);

			SdTypeConfigModel queryConfigModel = new SdTypeConfigModel() ;

			queryConfigModel.setStatus(DERP_ORDER.SDTYPECONFIG_STATUS_1);
			queryConfigModel.setSdTypeName(sdOrder.getSdTypeName());

			queryConfigModel = sdTypeConfigDao.searchByModel(queryConfigModel) ;

			PurchaseSdOrderSditemModel sdItem = new PurchaseSdOrderSditemModel() ;
			sdItem.setBarcode(merchandiseInfo.getBarcode());
			sdItem.setGoodsId(merchandiseInfo.getMerchandiseId());
			sdItem.setGoodsNo(merchandiseInfo.getGoodsNo());
			sdItem.setGoodsName(merchandiseInfo.getName());
			sdItem.setSdSimpleName(sdOrder.getSdSimpleName());
			sdItem.setSdTypeName(sdOrder.getSdTypeName());
			sdItem.setSdAmount(sdItemSdAmount);
			sdItem.setCreateDate(TimeUtils.getNow());

			if(sdOrder.getRate() != null) {
				sdItem.setSdTgtAmount(sdItemSdAmount.multiply(new BigDecimal(sdOrder.getRate()))) ;
			}

			if(DERP_ORDER.SDTYPECONFIG_TYPE_2.equals(queryConfigModel.getType())) {
				sdItem.setCommbarcode(commbarcode.getCommbarcode());
				sdItem.setBrandParent(commbarcode.getCommBrandParentName());
			}

			List<PurchaseSdOrderSditemModel> sdItemList = itemMap.get(index);

			if(sdItemList == null) {
				sdItemList = new ArrayList<PurchaseSdOrderSditemModel>() ;
			}

			BigDecimal sdAmount = sdOrder.getSdAmount();
			sdAmount = sdAmount.add(sdItemSdAmount) ;

			sdOrder.setSdAmount(sdAmount);
			orderMap.put(index, sdOrder) ;

			sdItemList.add(sdItem) ;
			itemMap.put(index, sdItemList) ;
		}

		returnMap.put("success", success);
		returnMap.put("pass", pass);
		returnMap.put("failure", failure);
		returnMap.put("message", resultList);

		if(failure > 0) {
			return returnMap ;
		}

		for (String index : orderMap.keySet()) {
			PurchaseSdOrderModel purchaseSdOrderModel = orderMap.get(index);

			Long orderId = purchaseSdOrderDao.save(purchaseSdOrderModel);

			List<PurchaseSdOrderSditemModel> sdItemList = itemMap.get(index);

			for (PurchaseSdOrderSditemModel sdItem : sdItemList) {
				sdItem.setOrderId(orderId);

				purchaseSdOrderSditemDao.save(sdItem) ;
			}
		}

		returnMap = new HashMap<String, Object>();
		returnMap.put("success", orderMap.size());
		returnMap.put("pass", pass);
		returnMap.put("failure", failure);
		returnMap.put("message", resultList);

		return returnMap;
	}

	/**
	 * 判断输入字段是否为空
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index , String content ,
			String msg ,List<ImportErrorMessage> resultList ) {

		if(StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true ;

		}else {
			return false ;
		}

	}

	/**
	 * 错误时，设置错误内容
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(int index , String msg ,List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		message.setRows(index + 1);
		message.setMessage(msg);
		resultList.add(message);
	}

	private boolean isValidDate(String str) {
	      boolean convertSuccess=true;
	       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	       try {
	          format.setLenient(false);
	          format.parse(str);
	       } catch (Exception e) {
	           convertSuccess=false;
	       }
	       return convertSuccess;
	}

	/**
	 * 判断小数位
	 * @param str
	 * @param dousize
	 * @return
	 */
	private boolean doubleyn(String str, int dousize) {

		int fourplace = str.trim().length() - str.trim().indexOf(".") - 1;
		if (fourplace <= dousize) {
			return false;
		} else {
			return true;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void delSdOrder(String ids) throws Exception{

		List<Long> idList = (List<Long>)StrUtils.parseIds(ids);

		for (Long id : idList) {

			PurchaseSdOrderModel tempSdModel = purchaseSdOrderDao.searchById(id);

			if(tempSdModel == null) {
				throw new DerpException("根据ID：" + id + "查询SD单不存在") ;
			}

			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(tempSdModel.getMerchantId());
			closeAccountsMongo.setDepotId(tempSdModel.getDepotId());
			closeAccountsMongo.setBuId(tempSdModel.getBuId());
			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG1);
			String maxCloseAccountsMonth = "";

			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}

			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (tempSdModel.getInboundDate() != null
						&& tempSdModel.getInboundDate().getTime() < Timestamp
						.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new DerpException("采购SD单:" + tempSdModel.getCode() + " 入库月份已关帐，不予删除") ;
				}
			}

			PurchaseSdOrderModel sdModel = new PurchaseSdOrderModel() ;

			sdModel.setId(id);
			sdModel.setStatus(DERP.DEL_CODE_006);
			sdModel.setModifyDate(TimeUtils.getNow());

			purchaseSdOrderDao.modify(sdModel) ;

		}

	}
}
