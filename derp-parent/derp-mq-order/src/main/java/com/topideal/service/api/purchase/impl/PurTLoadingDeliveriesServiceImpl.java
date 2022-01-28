package com.topideal.service.api.purchase.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.api.v2_3.SaleLoadingDeliveriesMQGoodsListJson;
import com.topideal.json.api.v2_3.SaleLoadingDeliveriesMQRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.service.api.purchase.PurTLoadingDeliveriesService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 装载交运-采购退货回推接口实现类
 */
@Service
public class PurTLoadingDeliveriesServiceImpl implements PurTLoadingDeliveriesService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(PurTLoadingDeliveriesServiceImpl.class);
	// 采购退货订单
	@Autowired
	private PurchaseReturnOrderDao purchaseReturnOrderDao;
	// 采购退货出库单
	@Autowired
	private PurchaseReturnOdepotDao purchaseReturnOdepotDao;
	// 采购退货出库单表体
	@Autowired
	private PurchaseReturnOdepotItemDao purchaseReturnOdepotItemDao;
	// MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private PurchaseReturnItemDao purchaseReturnItemDao;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203117300, model = DERP_LOG_POINT.POINT_12203117300_Label, keyword = "orderCode")
	public boolean saveLoadingDeliveriesInfo(String json, String keys, String topics, String tags) throws Exception {
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);

		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("goodsList", SaleLoadingDeliveriesMQGoodsListJson.class);
		// JSON对象转实体
		SaleLoadingDeliveriesMQRootJson rootJson = (SaleLoadingDeliveriesMQRootJson) JSONObject.toBean(jsonData,
				SaleLoadingDeliveriesMQRootJson.class, classMap);

		String orderCode = rootJson.getOrderCode();// 订单号或者LBX号
		String wayBillNo = rootJson.getWayBillNo();// 运单号非必填
		String blNo = rootJson.getBlNo();// 提单号非必填
		String deliver = rootJson.getDeliverDate();// 发货时间 非必填

		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(6); // 订单来源 6: 采购退货
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("采购退货来源表已经存在 单号" + orderCode + "  保存失败");
			throw new RuntimeException("采购退货来源表已经存在 单号" + orderCode + "  保存失败");
		}

		// 采购退货
		PurchaseReturnOrderModel purchaseReturn = new PurchaseReturnOrderModel();
		purchaseReturn.setCode(orderCode);
		purchaseReturn.setMerchantId(rootJson.getMerchantId());
		purchaseReturn = purchaseReturnOrderDao.searchByModel(purchaseReturn);
		if (purchaseReturn == null) {
			LOGGER.error("订单不存在,订单编号" + orderCode);
			throw new RuntimeException("订单不存在,订单编号" + orderCode);
		}
		if (DERP_ORDER.PURCHASERETURNORDER_STATUS_001.equals(purchaseReturn.getStatus())) {
			LOGGER.error("订单状态为“待审核”,订单编号" + orderCode);
			throw new RuntimeException("订单状态为“待审核”,订单编号" + orderCode);
		}
		if (null == purchaseReturn.getBuId()) {
			LOGGER.error("采购退货单编号" + purchaseReturn.getCode() + "事业部的值为空");
			throw new RuntimeException("采购退货单编号" + purchaseReturn.getCode() + "事业部的值为空");
		}
		// 根据仓库id查询 仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", purchaseReturn.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		if (mongo == null) {
			LOGGER.error("根据采购退货单的仓库id没有查询到仓库,订单号:" + orderCode);
			throw new RuntimeException("根据采购退货单的仓库id没有查询到仓库,订单号" + orderCode);
		}
		// 批次效期强校验：1-是 0-否
		if ("1".equals(mongo.getBatchValidation())) {
			for (SaleLoadingDeliveriesMQGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo) || StringUtils.isBlank(productionDate)
						|| StringUtils.isBlank(overdueDate)) {
					LOGGER.error(mongo.getName() + ",设置了批次效期强校验" + "批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName() + ",设置了批次效期强校验" + "批次效和期不能为空,订单号:" + orderCode);
				}

			}
		}
		Timestamp deliverDate = TimeUtils.parse(deliver, "yyyy-MM-dd HH:mm:ss");
		// 查询采购退货出库单
		PurchaseReturnOdepotModel purReturn = new PurchaseReturnOdepotModel();
		purReturn.setPurchaseReturnCode(orderCode);
		purReturn.setMerchantId(rootJson.getMerchantId());
		purReturn = purchaseReturnOdepotDao.searchByModel(purReturn);
		if (purReturn != null) {
			LOGGER.error("采购退货出库已经存在,订单号" + orderCode);
			throw new RuntimeException("采购退货出库已经存在,订单号" + orderCode);
		}
		String saleOrderTallyingUnit = purchaseReturn.getTallyingUnit();// 理货单位

		PurchaseReturnOdepotModel purReturnOutModel = new PurchaseReturnOdepotModel();
		purReturnOutModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGTC));
		purReturnOutModel.setPurchaseReturnCode(purchaseReturn.getCode());
		purReturnOutModel.setPurchaseReturnId(purchaseReturn.getId());
		purReturnOutModel.setOutDepotId(purchaseReturn.getOutDepotId());
		purReturnOutModel.setOutDepotName(purchaseReturn.getOutDepotName());
		purReturnOutModel.setReturnOutDate(deliverDate);
		purReturnOutModel.setMerchantId(purchaseReturn.getMerchantId());
		purReturnOutModel.setMerchantName(purchaseReturn.getMerchantName());
		purReturnOutModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_027);
		purReturnOutModel.setSupplierId(purchaseReturn.getSupplierId());
		purReturnOutModel.setSupplierName(purchaseReturn.getSupplierName());
		purReturnOutModel.setCurrency(purchaseReturn.getCurrency());
		purReturnOutModel.setBuName(purchaseReturn.getBuName());
		purReturnOutModel.setBuId(purchaseReturn.getBuId());
		purReturnOutModel.setPoNo(purchaseReturn.getPoNo());
		purReturnOutModel.setBlNo(blNo);
		purReturnOutModel.setWayBillNo(wayBillNo);
		purReturnOutModel.setTallyingUnit(saleOrderTallyingUnit);
		// 新增采购退货出库
		purchaseReturnOdepotDao.save(purReturnOutModel);

		//获取采购退商品信息
		PurchaseReturnItemModel queryModel = new PurchaseReturnItemModel();
		queryModel.setOrderId(purchaseReturn.getId());
		List<PurchaseReturnItemModel> returnItemList = purchaseReturnItemDao.list(queryModel);
		List<Long> returnItemGoodsIdMap = returnItemList.stream().map(PurchaseReturnItemModel::getGoodsId).collect(Collectors.toList());
		List<PurchaseReturnOdepotItemModel> itemList = new ArrayList<PurchaseReturnOdepotItemModel>();
		for (SaleLoadingDeliveriesMQGoodsListJson goodsListJson : rootJson.getGoodsList()) {
			if(!returnItemGoodsIdMap.contains(goodsListJson.getGoodsId())){
				LOGGER.error("订单号" + orderCode+",商品货号：" + goodsListJson.getGoodsNo() +" 在采购退订单不存在");
				throw new RuntimeException("订单号" + orderCode+",商品货号：" + goodsListJson.getGoodsNo() +" 在采购退订单不存在");
			}
			// 新增销采购退货出库表体
			PurchaseReturnOdepotItemModel itemModel = new PurchaseReturnOdepotItemModel();
			itemModel.setOdepotOrderId(purReturnOutModel.getId());
			itemModel.setGoodsId(goodsListJson.getGoodsId());// 商品id
			itemModel.setGoodsName(goodsListJson.getGoodsName());// 商品名称
			itemModel.setGoodsNo(goodsListJson.getGoodsNo());// 商品货号
			itemModel.setBarcode(goodsListJson.getBarcode());// 货品条形码
			itemModel.setNum(goodsListJson.getNum());// 数量
			itemModel.setBatchNo(goodsListJson.getBatchNo());// 批次

			String productionDate = goodsListJson.getProductionDate();// 生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				itemModel.setProductionDate(TimeUtils.StringToDate(productionDate));// 生产日期
			}
			String overdueDate = goodsListJson.getOverdueDate();// 失效日期
			if (StringUtils.isNotBlank(overdueDate)) {
				itemModel.setOverdueDate(TimeUtils.StringToDate(overdueDate));// 失效日期
			}

			// 新增采购退货出库表体
			purchaseReturnOdepotItemDao.save(itemModel);
			itemList.add(itemModel);
		}

		// 库存发货时间
		String deliverDateMq = TimeUtils.format(deliverDate, "yyyy-MM-dd HH:mm:ss");
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now1 = sdf.format(new Date());
		invetAddOrSubtractRootJson.setMerchantId(purchaseReturn.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(purchaseReturn.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(rootJson.getTopidealCode());// 卓志编码
		// 事业部
		invetAddOrSubtractRootJson.setBuId(String.valueOf(purchaseReturn.getBuId()));
		invetAddOrSubtractRootJson.setBuName(purchaseReturn.getBuName());
		invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(mongo.getName());
		invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(mongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(purReturnOutModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(purchaseReturn.getCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0018);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0030);
		invetAddOrSubtractRootJson.setSourceDate(now1);
		invetAddOrSubtractRootJson.setDivergenceDate(deliverDateMq);
		// 获取当前年月
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));

		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		for (PurchaseReturnOdepotItemModel item : itemList) {
			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
			invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
			invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
			invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
			invetAddOrSubtractGoodsListJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);// 商品分类 （0 好品 1坏品） 字符串
			invetAddOrSubtractGoodsListJson.setNum(item.getNum());
			invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
			invetAddOrSubtractGoodsListJson.setStockLocationTypeId(String.valueOf(purchaseReturn.getStockLocationTypeId()));
			invetAddOrSubtractGoodsListJson.setStockLocationTypeName(purchaseReturn.getStockLocationTypeName());
			if (item.getProductionDate() != null) {
				invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(item.getProductionDate()));
			}
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				String addeUnit = null;
				if (DERP.ORDER_TALLYINGUNIT_00.equals(saleOrderTallyingUnit)) {
					addeUnit = DERP.INVENTORY_UNIT_0;
				} else if (DERP.ORDER_TALLYINGUNIT_01.equals(saleOrderTallyingUnit)) {
					addeUnit = DERP.INVENTORY_UNIT_1;
				} else if (DERP.ORDER_TALLYINGUNIT_02.equals(saleOrderTallyingUnit)) {
					addeUnit = DERP.INVENTORY_UNIT_2;
				}
				invetAddOrSubtractGoodsListJson.setUnit(addeUnit);// 字符串 0 托盘 1箱 2 件
			}
			// 0 过期 1 未过期
			if (item.getOverdueDate() != null) {
				Date overdueDateTimestamp = item.getOverdueDate();
				invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));
				String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());// 判断是否过期是否过期（0是 1否）
				invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);// 是否过期（0是 1否）
			} else {
				invetAddOrSubtractGoodsListJson.setIsExpire("1");
			}
			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

		// 回调mq
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTopic());
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTags());
		Map<String, Object> customParam = new HashMap<String, Object>();
		customParam.put("code", purReturnOutModel.getCode()); // 订单code
		invetAddOrSubtractRootJson.setCustomParam(customParam);

		// 修改采购退货订单状态为出库中、本位币、汇率
		Map<String, Object> queryMerchantMap = new HashMap<String, Object>();
		queryMerchantMap.put("merchantId", purchaseReturn.getMerchantId());
		MerchantInfoMongo merchantInfoMongo = merchantMongoDao.findOne(queryMerchantMap);

		String accountCurrency = null;
		Double rate = null;
		if (merchantInfoMongo != null) {
			accountCurrency = merchantInfoMongo.getAccountCurrency();

			if (StringUtils.isNotBlank(accountCurrency)) {

				if (accountCurrency.equals(purchaseReturn.getCurrency())) {
					rate = 1.00;
				} else {
					Map<String, Object> queryRateMap = new HashMap<String, Object>();
					queryRateMap.put("origCurrencyCode", purchaseReturn.getCurrency());
					queryRateMap.put("tgtCurrencyCode", accountCurrency);
					queryRateMap.put("effectiveDate", TimeUtils.format(deliverDate, "yyyy-MM-dd"));

					ExchangeRateMongo rateMongo = exchangeRateMongoDao.findOne(queryRateMap);

					if (rateMongo != null) {
						rate = rateMongo.getRate();
					}
				}
			}

		}

		PurchaseReturnOrderModel updateModel = new PurchaseReturnOrderModel();
		updateModel.setId(purchaseReturn.getId());
		updateModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_027);
		updateModel.setTgtCurrency(accountCurrency);
		updateModel.setModifyDate(TimeUtils.getNow());
		if (rate != null) {
			updateModel.setRate(new BigDecimal(rate));
		}

		purchaseReturnOrderDao.modify(updateModel);

		for (PurchaseReturnItemModel purchaseReturnItemModel : returnItemList) {
			if (rate == null) {
				continue;
			}

			// 设置本位单价、金额
			BigDecimal returnAmount = purchaseReturnItemModel.getReturnAmount();
			Integer returnNum = purchaseReturnItemModel.getReturnNum();

			BigDecimal tgtAmount = returnAmount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

			BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(returnNum), 8, BigDecimal.ROUND_HALF_UP);

			purchaseReturnItemModel.setTgtReturnAmount(tgtAmount);
			purchaseReturnItemModel.setTgtReturnPrice(tgtPrice);
			purchaseReturnItemModel.setModifyDate(TimeUtils.getNow());

			purchaseReturnItemDao.modify(purchaseReturnItemModel);
		}

		// 推送加减库存
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		return true;
	}
}
