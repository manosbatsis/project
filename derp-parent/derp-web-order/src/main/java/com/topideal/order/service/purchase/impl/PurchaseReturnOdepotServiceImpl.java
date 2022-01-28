package com.topideal.order.service.purchase.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.PurchaseReturnItemDao;
import com.topideal.dao.purchase.PurchaseReturnOdepotDao;
import com.topideal.dao.purchase.PurchaseReturnOdepotItemDao;
import com.topideal.dao.purchase.PurchaseReturnOrderDao;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotDTO;
import com.topideal.entity.dto.purchase.PurchaseReturnOdepotExportDTO;
import com.topideal.entity.vo.purchase.PurchaseReturnItemModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotItemModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOdepotModel;
import com.topideal.entity.vo.purchase.PurchaseReturnOrderModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.purchase.PurchaseReturnOdepotService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class PurchaseReturnOdepotServiceImpl implements PurchaseReturnOdepotService{

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(PurchaseReturnOdepotServiceImpl.class);

	@Autowired
	PurchaseReturnOdepotDao purchaseReturnOdepotDao ;
	@Autowired
	PurchaseReturnOrderDao purchaseReturnOrderDao ;
	@Autowired
	PurchaseReturnItemDao purchaseReturnItemDao ;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao ;
	@Autowired
	private PurchaseReturnOdepotItemDao purchaseReturnOdepotItemDao ;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao ;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao ;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;

	@SuppressWarnings("unchecked")
	@Override
	public PurchaseReturnOdepotDTO listPurchaseReturnOdepotPage(PurchaseReturnOdepotDTO dto, User user) throws SQLException {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);

				return dto ;
			}

			dto.setBuIds(buIds);
		}

		return purchaseReturnOdepotDao.getListByPage(dto);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> importPurchaseReturnOdepot(List<Map<String, String>> data, User user) throws Exception {

		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		boolean flag = true ;

		//采购退货订单表体集合
		Map<String, Map<String,Object>> cacheMap = new HashMap<String, Map<String,Object>>() ;
		Map<String, Integer> conutMap = new HashMap<String, Integer>() ;

		for(int j = 1; j <= data.size(); j++) {

			Map<String, String> rows = data.get(j - 1) ;
			String purchaseReturnCode = rows.get("采购退货单号");

			if(checkIsNullOrNot(j, purchaseReturnCode, "采购退货单号不存在", resultList)) {
				flag &= false ;
				failure += 1;
				continue;
			}
			purchaseReturnCode = purchaseReturnCode.trim() ;

			PurchaseReturnOrderModel purchaseReturnOrderModel = new PurchaseReturnOrderModel() ;
			purchaseReturnOrderModel.setCode(purchaseReturnCode);
			purchaseReturnOrderModel = purchaseReturnOrderDao.searchByModel(purchaseReturnOrderModel) ;

			if(purchaseReturnOrderModel == null) {
				setErrorMsg(j, "采购退货单不存在", resultList);
				flag &= false ;
				failure += 1;
				continue;
			}

			PurchaseReturnOdepotModel  purchaseReturnOdepotModel = new PurchaseReturnOdepotModel() ;
			purchaseReturnOdepotModel.setPurchaseReturnCode(purchaseReturnCode);
			purchaseReturnOdepotModel = purchaseReturnOdepotDao.searchByModel(purchaseReturnOdepotModel) ;

			if(purchaseReturnOdepotModel != null) {
				setErrorMsg(j, "采购退货单已存在对应采购退货出库单", resultList);
				flag &= false ;
				failure += 1;
				continue;
			}

			String returnOutDateStr = rows.get("出库时间");

			if(checkIsNullOrNot(j, returnOutDateStr, "出库时间不能为空", resultList)) {
				flag &= false ;
				failure += 1;
				continue;
			}

			if(!isDate(returnOutDateStr)) {
				setErrorMsg(j, "出库时间格式不正确 yyyy-MM-dd", resultList);
				flag &= false ;
				failure += 1;
				continue;
			}

			if(TimeUtils.daysBetween(TimeUtils.parse(returnOutDateStr, "yyyy-MM-dd"), new Date()) < 0) {
				setErrorMsg(j, "出库时间不可超过当前时间", resultList);
				flag &= false ;
				failure += 1;
				continue;
			}

			// 判断归属日期是否小于 关账日期/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(purchaseReturnOrderModel.getOutDepotId());
			closeAccountsMongo.setBuId(purchaseReturnOrderModel.getBuId());

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (Timestamp.valueOf(returnOutDateStr + " 00:00:00").getTime() < Timestamp
						.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j, "出库时间必须大于关账日期/月结日期", resultList);
					flag &= false ;
					failure += 1;
					continue;
				}
			}

			String goodsNo = rows.get("商品货号");

			if(checkIsNullOrNot(j, goodsNo, "商品货号不能为空", resultList)) {
				flag &= false ;
				failure += 1;
				continue;
			}
			goodsNo = goodsNo.trim() ;

			String returnNumStr = rows.get("退出数量");

			if(checkIsNullOrNot(j, returnNumStr, "退出数量不能为空", resultList)) {
				flag &= false ;
				failure += 1;
				continue;
			}
			returnNumStr = returnNumStr.trim() ;

			if(!StrUtils.stringReg(returnNumStr, "[0-9]*")) {
				setErrorMsg(j, "退出数量非数字类型", resultList);
				flag &= false ;
				failure += 1;
				continue;
			}

			PurchaseReturnItemModel itemModel = new PurchaseReturnItemModel() ;
			itemModel.setOrderId(purchaseReturnOrderModel.getId());
			itemModel.setGoodsNo(goodsNo);
			itemModel = purchaseReturnItemDao.searchByModel(itemModel) ;

			if(itemModel == null) {
				setErrorMsg(j, "商品货号不存在于采购退货订单中", resultList);
				flag &= false ;
				failure += 1;
				continue;
			}

			String key =  purchaseReturnCode + "_" + goodsNo;
			Integer tempNum = conutMap.get(key) ;

			if(tempNum == null) {
				tempNum = 0;
			}

			Integer returnNum = Integer.valueOf(returnNumStr);
			conutMap.put(key, returnNum + tempNum) ;

			//查询出库仓是否批次强校验
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("depotId", purchaseReturnOrderModel.getOutDepotId()) ;
			DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);

			String batchValidation = "" ;
			if(depot != null) {
				batchValidation = depot.getBatchValidation();
			}

			String batchNo = rows.get("批次号");
			String productionDate = rows.get("生产日期");
			String overdueDate = rows.get("失效日期");
			if(DERP_SYS.BATCHVALIDATION_1.equals(batchValidation)) {

				if(checkIsNullOrNot(j, batchNo, "出仓仓库为批次强校验，批次号必填", resultList)) {
					flag &= false ;
					failure += 1;
					continue;
				}
				batchNo = batchNo.trim() ;

				if(checkIsNullOrNot(j, productionDate, "出仓仓库为批次强校验，生产日期必填", resultList)) {
					flag &= false ;
					failure += 1;
					continue;
				}
				productionDate = productionDate.trim() ;

				if(!isDate(productionDate)) {
					setErrorMsg(j, "生产日期格式不正确 yyyy-MM-dd", resultList);
					flag &= false ;
					failure += 1;
					continue;
				}

				if(checkIsNullOrNot(j, overdueDate, "出仓仓库为批次强校验，失效日期必填", resultList)) {
					flag &= false ;
					failure += 1;
					continue;
				}
				overdueDate = overdueDate.trim() ;

				if(!isDate(overdueDate)) {
					setErrorMsg(j, "失效日期格式不正确 yyyy-MM-dd", resultList);
					flag &= false ;
					failure += 1;
					continue;
				}
			}

			PurchaseReturnOdepotItemModel item = new PurchaseReturnOdepotItemModel() ;
			item.setBarcode(itemModel.getBarcode());
			item.setGoodsId(itemModel.getGoodsId());
			item.setGoodsName(itemModel.getGoodsName());
			item.setGoodsNo(itemModel.getGoodsNo());
			item.setNum(returnNum);
			item.setBatchNo(batchNo);
			item.setCreateDate(TimeUtils.getNow());

			if(StringUtils.isNotBlank(productionDate)) {
				item.setProductionDate(TimeUtils.parse(productionDate, "yyyy-MM-dd"));
			}

			if(StringUtils.isNotBlank(overdueDate)) {
				item.setOverdueDate(TimeUtils.parse(overdueDate, "yyyy-MM-dd"));
			}

			Map<String, Object> purchaseReturnOdepotMap = cacheMap.get(purchaseReturnCode);

			if(purchaseReturnOdepotMap == null) {
				purchaseReturnOdepotMap = new HashMap<String, Object>() ;
			}

			PurchaseReturnOdepotModel tempModel = (PurchaseReturnOdepotModel)purchaseReturnOdepotMap.get("purchaseReturnOdepot") ;
			if(tempModel == null) {
				tempModel = new PurchaseReturnOdepotModel() ;
				tempModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGTC));
				tempModel.setBuId(purchaseReturnOrderModel.getBuId());
				tempModel.setBuName(purchaseReturnOrderModel.getBuName());
				tempModel.setSupplierId(purchaseReturnOrderModel.getSupplierId());
				tempModel.setSupplierName(purchaseReturnOrderModel.getSupplierName());
				tempModel.setCurrency(purchaseReturnOrderModel.getCurrency());
				tempModel.setMerchantId(user.getMerchantId());
				tempModel.setMerchantName(user.getMerchantName());
				tempModel.setOutDepotId(purchaseReturnOrderModel.getOutDepotId());
				tempModel.setOutDepotName(purchaseReturnOrderModel.getOutDepotName());
				tempModel.setPurchaseReturnId(purchaseReturnOrderModel.getId());
				tempModel.setPurchaseReturnCode(purchaseReturnCode);
				tempModel.setStatus(DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_001);
				tempModel.setSupplierId(purchaseReturnOrderModel.getSupplierId());
				tempModel.setSupplierName(purchaseReturnOrderModel.getSupplierName());
				tempModel.setCreateDate(TimeUtils.getNow());
				tempModel.setCreater(user.getId());
				tempModel.setCreateName(user.getName());
				tempModel.setReturnOutDate(TimeUtils.parse(returnOutDateStr, "yyyy-MM-dd"));
				tempModel.setTallyingUnit(purchaseReturnOrderModel.getTallyingUnit());
				tempModel.setPoNo(purchaseReturnOrderModel.getPoNo());

				purchaseReturnOdepotMap.put("purchaseReturnOdepot", tempModel) ;
			}

			List<PurchaseReturnOdepotItemModel> itemList = (List<PurchaseReturnOdepotItemModel>)purchaseReturnOdepotMap.get("itemList") ;
			if(itemList == null) {
				itemList = new ArrayList<PurchaseReturnOdepotItemModel>() ;
			}

			itemList.add(item) ;
			purchaseReturnOdepotMap.put("itemList", itemList) ;

			cacheMap.put(purchaseReturnCode, purchaseReturnOdepotMap) ;

			success ++ ;
			flag &= true ;
		}

		if(flag) {
			for(int j = 1; j <= data.size(); j++) {
				Map<String, String> rows = data.get(j - 1) ;
				String purchaseReturnCode = rows.get("采购退货单号");
				String goodsNo = rows.get("商品货号");

				String key =  purchaseReturnCode + "_" + goodsNo;

				PurchaseReturnOrderModel purchaseReturnOrderModel = new PurchaseReturnOrderModel() ;
				purchaseReturnOrderModel.setCode(purchaseReturnCode);
				purchaseReturnOrderModel = purchaseReturnOrderDao.searchByModel(purchaseReturnOrderModel) ;

				PurchaseReturnItemModel itemModel = new PurchaseReturnItemModel() ;
				itemModel.setOrderId(purchaseReturnOrderModel.getId());
				itemModel.setGoodsNo(goodsNo);
				itemModel = purchaseReturnItemDao.searchByModel(itemModel) ;

				Integer returnNum = conutMap.get(key);

				if(itemModel.getReturnNum().intValue() != returnNum.intValue()) {
					setErrorMsg(j, "退出数量需必须等于采购退货订单中的数量,退货订单数量为" + itemModel.getReturnNum(), resultList);
					flag &= false ;
					success = 0 ;
					failure += 1;
					continue;
				}
			}
		}

		if(flag) {
			for(String purchaseReturnCode : cacheMap.keySet()) {
				Map<String, Object> purchaseReturnOdepotMap = cacheMap.get(purchaseReturnCode);

				PurchaseReturnOdepotModel tempModel = (PurchaseReturnOdepotModel)purchaseReturnOdepotMap.get("purchaseReturnOdepot") ;
				List<PurchaseReturnOdepotItemModel> itemList = (List<PurchaseReturnOdepotItemModel>)purchaseReturnOdepotMap.get("itemList") ;

				Long id = purchaseReturnOdepotDao.save(tempModel);

				/*
				 * ---------------------查询采购退货订单，设置汇率、本位币开始---------------------------------------------------
				 */
				PurchaseReturnOrderModel queryOrderModel = new PurchaseReturnOrderModel() ;
				queryOrderModel.setCode(purchaseReturnCode);
				queryOrderModel = purchaseReturnOrderDao.searchByModel(queryOrderModel) ;

				Map<String, Object> queryMerchantMap = new HashMap<String, Object>() ;

				queryMerchantMap.put("merchantId", tempModel.getMerchantId()) ;

				MerchantInfoMongo merchantInfoMongo = merchantMongoDao.findOne(queryMerchantMap) ;

				String accountCurrency = null ;
				Double rate = null ;
				if(merchantInfoMongo != null) {
					accountCurrency = merchantInfoMongo.getAccountCurrency();

					if(StringUtils.isNotBlank(accountCurrency)) {

						if(accountCurrency.equals(queryOrderModel.getCurrency())) {
							rate = 1.00 ;
						}else {
							Map<String, Object> queryRateMap = new HashMap<String, Object>() ;
							queryRateMap.put("origCurrencyCode", queryOrderModel.getCurrency()) ;
							queryRateMap.put("tgtCurrencyCode", accountCurrency) ;
							queryRateMap.put("effectiveDate", TimeUtils.format(tempModel.getReturnOutDate(), "yyyy-MM-dd")) ;

							ExchangeRateMongo rateMongo = exchangeRateMongoDao.findOne(queryRateMap);

							if(rateMongo != null) {
								rate = rateMongo.getRate();
							}
						}
					}

				}

				queryOrderModel.setTgtCurrency(accountCurrency);
				queryOrderModel.setModifyDate(TimeUtils.getNow());
				if(rate != null) {
					queryOrderModel.setRate(new BigDecimal(rate));
				}
				purchaseReturnOrderDao.modify(queryOrderModel) ;

				PurchaseReturnItemModel queryModel = new PurchaseReturnItemModel() ;
				queryModel.setOrderId(queryOrderModel.getId());

				List<PurchaseReturnItemModel> returnItemList = purchaseReturnItemDao.list(queryModel);

				//校验采购订单表体
				for (PurchaseReturnItemModel purchaseReturnItemModel : returnItemList) {
					if(rate == null) {
						continue ;
					}

					//设置本位单价、金额
					BigDecimal returnAmount = purchaseReturnItemModel.getReturnAmount();
					Integer returnNum = purchaseReturnItemModel.getReturnNum();

					BigDecimal tgtAmount = returnAmount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

					BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(returnNum), 8, BigDecimal.ROUND_HALF_UP);

					purchaseReturnItemModel.setTgtReturnAmount(tgtAmount);
					purchaseReturnItemModel.setTgtReturnPrice(tgtPrice);
					purchaseReturnItemModel.setModifyDate(TimeUtils.getNow());

					purchaseReturnItemDao.modify(purchaseReturnItemModel) ;
				}

				/*
				 * ---------------------查询采购退货订单，设置汇率、本位币结束---------------------------------------------------
				 */

				for (PurchaseReturnOdepotItemModel item : itemList) {

					item.setOdepotOrderId(id);

					purchaseReturnOdepotItemDao.save(item) ;
				}
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", success);
		map.put("pass", pass);
		map.put("failure", failure);
		map.put("message", resultList);
		return map;
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

	/**
	 * 判断是否yyyy-MM-dd
	 * @param date
	 * @return
	 */
	private boolean isDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
	}

	@Override
	public PurchaseReturnOdepotDTO getDTOById(Long id) {
		return purchaseReturnOdepotDao.getDTOById(id);
	}

	@Override
	public List<PurchaseReturnOdepotItemModel> getItemListByOrderId(Long id) throws SQLException {

		PurchaseReturnOdepotItemModel itemModel = new PurchaseReturnOdepotItemModel() ;
		itemModel.setOdepotOrderId(id) ;

		return purchaseReturnOdepotItemDao.list(itemModel);
	}
	@Override
	public List<PurchaseReturnOdepotExportDTO> getDetailsByExport(PurchaseReturnOdepotDTO dto, User user) {

		if(dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if(buIds.isEmpty()) {
				return new ArrayList<PurchaseReturnOdepotExportDTO>();
			}

			dto.setBuIds(buIds);
		}

		return purchaseReturnOdepotDao.getDetailsByExport(dto);
	}
	@Override
	public boolean auditPurchaseReturnOrder(List<Long> list, User user) throws SQLException {

		for (Long id : list) {
			PurchaseReturnOdepotModel tempModel = purchaseReturnOdepotDao.searchById(id) ;

			if(tempModel == null) {
				throw new RuntimeException("采购退货出库单不存在") ;
			}

			if(!DERP_ORDER.PURCHASERETURNORDERODEPOT_STATUS_001.equals(tempModel.getStatus())) {
				throw new RuntimeException("采购退货出库单："+tempModel.getCode()+" 非待审核状态") ;
			}

			// 判断出库日期是否小于 关账日期/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(tempModel.getOutDepotId());
			closeAccountsMongo.setBuId(tempModel.getBuId());

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}

			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (tempModel.getReturnOutDate().getTime() < Timestamp
						.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("采购退货出库单："+tempModel.getCode()+" 出库日期必须大于 关账日期/月结日期") ;
				}
			}

		}

		//推库存
		for (Long id : list) {

			//修改状态为出库中
			PurchaseReturnOdepotModel updateModel = new PurchaseReturnOdepotModel() ;
			updateModel.setId(id);
			updateModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_027);
			int rows = purchaseReturnOdepotDao.modify(updateModel);

			if(rows == 0) {
				continue ;
			}

			PurchaseReturnOdepotModel tempModel = purchaseReturnOdepotDao.searchById(id) ;

			PurchaseReturnOdepotItemModel queryItemModel = new PurchaseReturnOdepotItemModel() ;
			queryItemModel.setOdepotOrderId(id);
			List<PurchaseReturnOdepotItemModel> itemList = purchaseReturnOdepotItemDao.list(queryItemModel) ;

			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", tempModel.getOutDepotId());// 根据仓库id
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params); 	// 获取仓库信息

			//加减库存json
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();

			invetAddOrSubtractRootJson.setMerchantId(String.valueOf(tempModel.getMerchantId()));
			invetAddOrSubtractRootJson.setMerchantName(tempModel.getMerchantName());
			invetAddOrSubtractRootJson.setTopidealCode(user.getTopidealCode());
			invetAddOrSubtractRootJson.setDepotId(String.valueOf(tempModel.getOutDepotId()));
			invetAddOrSubtractRootJson.setDepotName(tempModel.getOutDepotName());
			invetAddOrSubtractRootJson.setDepotCode(depot.getCode());
			invetAddOrSubtractRootJson.setDepotType(depot.getType());
			invetAddOrSubtractRootJson.setIsTopBooks(depot.getIsTopBooks());
			invetAddOrSubtractRootJson.setOrderNo(tempModel.getCode());
			invetAddOrSubtractRootJson.setBusinessNo(tempModel.getPurchaseReturnCode());
			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0018);
			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0030);
			invetAddOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());	// 单据时间
			invetAddOrSubtractRootJson.setDivergenceDate(TimeUtils.format(tempModel.getReturnOutDate(), "yyyy-MM-dd HH:mm:ss"));	// 出库时间
			invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.format(tempModel.getReturnOutDate(), "yyyy-MM")); // 归属月份
			invetAddOrSubtractRootJson.setBuId(String.valueOf(tempModel.getBuId()));
			invetAddOrSubtractRootJson.setBuName(tempModel.getBuName());

			//回调mq
			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTopic());
			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.PURCHASE_RETURN_ODEPOT_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			customParam.put("code", tempModel.getCode());	// 订单code
			invetAddOrSubtractRootJson.setCustomParam(customParam);

			//设置加减表体
			Map<String, Object> merchandiseMap = new HashMap<String, Object>() ;

			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			for (PurchaseReturnOdepotItemModel item : itemList) {

				merchandiseMap.put("goodsNo", item.getGoodsNo());
				merchandiseMap.put("merchantId", tempModel.getMerchantId());
				merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);

				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
				invetAddOrSubtractGoodsListJson.setBarcode(merchandise.getBarcode());
				invetAddOrSubtractGoodsListJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);	// 商品分类 （0 好品 1坏品 ）
				invetAddOrSubtractGoodsListJson.setNum(item.getNum());	//数量
				invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setIsExpire(DERP.ISEXPIRE_1);

				if(StringUtils.isNotBlank(item.getBatchNo())) {
					invetAddOrSubtractGoodsListJson.setBatchNo(item.getBatchNo());
				}

				if(item.getProductionDate() != null) {
					invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.format(item.getProductionDate(), "yyyy-MM-dd"));
				}

				if(item.getOverdueDate() != null) {
					invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.format(item.getOverdueDate(), "yyyy-MM-dd"));;

					//判断过期
					if(TimeUtils.daysBetween(tempModel.getReturnOutDate(), item.getOverdueDate()) < 0) {
						invetAddOrSubtractGoodsListJson.setIsExpire(DERP.ISEXPIRE_0);
					}

				}

				if(DERP.ORDER_TALLYINGUNIT_00.equals(tempModel.getTallyingUnit())) {
					invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_0);
				}else if(DERP.ORDER_TALLYINGUNIT_01.equals(tempModel.getTallyingUnit())) {
					invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_1);
				}else if(DERP.ORDER_TALLYINGUNIT_02.equals(tempModel.getTallyingUnit())) {
					invetAddOrSubtractGoodsListJson.setUnit(DERP.INVENTORY_UNIT_2);
				}

				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}

			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

			//加库存
			JSONObject addjson = JSONObject.fromObject(invetAddOrSubtractRootJson);
			try {
				rocketMQProducer.send(addjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			} catch (Exception e) {
				LOGGER.error("--------采购退货出库单扣减库存消息发送失败-------", addjson.toString());
				LOGGER.error("--------采购退货出库单扣减库存失败-------", e);
			}
		}

		return true;
	}

}
