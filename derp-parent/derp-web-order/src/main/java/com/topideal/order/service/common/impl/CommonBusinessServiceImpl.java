package com.topideal.order.service.common.impl;

import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.bill.OperateLogDao;
import com.topideal.dao.purchase.*;
import com.topideal.entity.dto.bill.OperateLogDTO;
import com.topideal.entity.vo.bill.OperateLogModel;
import com.topideal.entity.vo.purchase.*;
import com.topideal.mongo.dao.BusinessUnitMongoDao;
import com.topideal.mongo.dao.ExchangeRateMongoDao;
import com.topideal.mongo.dao.FixedCostPriceMongoDao;
import com.topideal.mongo.dao.MerchantInfoMongoDao;
import com.topideal.mongo.entity.BusinessUnitMongo;
import com.topideal.mongo.entity.ExchangeRateMongo;
import com.topideal.mongo.entity.FixedCostPriceMongo;
import com.topideal.mongo.entity.MerchantInfoMongo;
import com.topideal.order.service.common.CommonBusinessService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/***
 * 公共业务service
 * @author gy
 *
 */
@Service
public class CommonBusinessServiceImpl implements CommonBusinessService{

	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;
//	@Autowired
//	private PurchaseAnalysisDao purchaseAnalysisDao;
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao ;
	@Autowired
	private ExchangeRateMongoDao exchangeRateMongoDao;
	@Autowired
	private PurchaseSdOrderDao purchaseSdOrderDao ;
	@Autowired
	private PurchaseSdOrderSditemDao purchaseSdOrderSditemDao ;
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	@Autowired
	private MerchantInfoMongoDao merchantMongoDao;
	@Autowired
	private OperateLogDao operateLogDao;
	@Autowired
	private BusinessUnitMongoDao businessUnitMongoDao;
	@Autowired
	private FixedCostPriceMongoDao fixedCostPriceMongoDao;

	/**
	 * 采购入库自动勾稽
	 * @param purchaseWarehouseCode
	 * @throws DerpException 经分销业务异常，调用方捕获该异常，修改对应勾稽状态
	 * @throws Exception
	 */
//	@Override
//	public void saveAutoPurchaseAnalysis(String purchaseWarehouseCode) throws Exception {
//
//		/**根据采购入库单号查询*/
//		PurchaseWarehouseModel warehouseQueryModel = new PurchaseWarehouseModel() ;
//		warehouseQueryModel.setCode(purchaseWarehouseCode);
//
//		warehouseQueryModel = purchaseWarehouseDao.searchByModel(warehouseQueryModel) ;
//
//		if(warehouseQueryModel == null) {
//			throw new RuntimeException("勾稽失败，根据入库单号：" + purchaseWarehouseCode + " 查询采购入库单不存在") ;
//		}
//
//
//		/**根据货号汇总采购入库明细*/
//		PurchaseWarehouseItemModel queryWarehouseItemModel = new PurchaseWarehouseItemModel() ;
//		queryWarehouseItemModel.setWarehouseId(warehouseQueryModel.getId());
//
//		List<PurchaseWarehouseItemModel> wareItemList = purchaseWarehouseItemDao.list(queryWarehouseItemModel);
//
//		Set<String> goodsNoWarehouseItemSet = wareItemList.stream().map(PurchaseWarehouseItemModel::getGoodsNo).collect(Collectors.toSet());
//
//
//		/**根据货号汇总采购勾稽明细*/
//		List<PurchaseAnalysisModel> analysisList = new ArrayList<PurchaseAnalysisModel>() ;
//		List<String> purchaseCodeList = purchaseWarehouseDao.getWarehouseListById(warehouseQueryModel.getId());
//
//		for (String purchaseCode : purchaseCodeList) {
//
//			PurchaseAnalysisModel queryPurchaseAnalysis = new PurchaseAnalysisModel() ;
//			queryPurchaseAnalysis.setOrderCode(purchaseCode);
//
//			List<PurchaseAnalysisModel> tempAnalysisList = purchaseAnalysisDao.list(queryPurchaseAnalysis);
//
//			//过滤已存在采购入库勾稽记录
//			tempAnalysisList = tempAnalysisList.stream().filter(tempAnalysis -> tempAnalysis.getWarehouseId() == null)
//			.collect(Collectors.toList()) ;
//
//			analysisList.addAll(tempAnalysisList) ;
//		}
//
//		Map<String, List<PurchaseAnalysisModel>> analysisMap = analysisList.stream().collect(Collectors.groupingBy(PurchaseAnalysisModel::getGoodsNo));
//
//
//		/**汇总采购数量，根据货号比对采购入库量*/
//		for (String goodsNo : analysisMap.keySet()) {
//			List<PurchaseAnalysisModel> tempAnalysisList = analysisMap.get(goodsNo);
//
//			Integer purchaseNum = tempAnalysisList.stream().map(PurchaseAnalysisModel::getPurchaseNum).reduce(Integer::sum).get();
//
//			PurchaseWarehouseItemModel tempItemModel = new PurchaseWarehouseItemModel() ;
//			tempItemModel.setGoodsNo(goodsNo);
//			tempItemModel.setWarehouseId(warehouseQueryModel.getId());
//
//			queryWarehouseItemModel = purchaseWarehouseItemDao.searchByModel(tempItemModel) ;
//
//			if(queryWarehouseItemModel == null) {
//				continue ;
//			}
//
//			if(queryWarehouseItemModel.getTallyingNum() > purchaseNum) {
//				throw new DerpException("勾稽失败，采购入库单：" + warehouseQueryModel.getCode() + " 货号：" + goodsNo + "采购入库数量大于采购数量", warehouseQueryModel) ;
//			}
//		}
//
//
//		/**根据采购入库商品信息查找对应的采购订单商品信息，查询对应商品是否一致的情况*/
//		for (String warehouseGoodsNo : goodsNoWarehouseItemSet) {
//			boolean flag = analysisMap.keySet().contains(warehouseGoodsNo);
//
//			if(!flag) {
//				throw new DerpException("勾稽失败，未找到货号：" + warehouseGoodsNo + "对应采购订单待入库商品信息", warehouseQueryModel) ;
//			}
//
//			Map<String, Object> queryMap = new HashMap<String, Object>() ;
//			queryMap.put("warehouseCode", purchaseWarehouseCode) ;
//			queryMap.put("goodsNo", warehouseGoodsNo) ;
//
//			List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodBatchByWarehouseCodeAndGoodsNo(queryMap);
//
//			List<PurchaseAnalysisModel> tempAnlsisList = analysisMap.get(warehouseGoodsNo);
//
//			/**采购入库按批次勾稽*/
//			for (PurchaseAnalysisModel analysisModel : tempAnlsisList) {
//
//				if(analysisModel.getWarehouseNum() != null
//						&& analysisModel.getPurchaseNum().compareTo(analysisModel.getWarehouseNum()) == 0) {
//					continue ;
//				}
//
//				for (PurchaseWarehouseBatchModel batchModel : batchList) {
//
//					Integer tallyNum = batchModel.getExpireNum() + batchModel.getNormalNum() + batchModel.getWornNum() ;
//
//					if(tallyNum == 0) {
//						continue ;
//					}
//
//					analysisModel.setWarehouseId(warehouseQueryModel.getId());
//					analysisModel.setWarehouseCode(warehouseQueryModel.getCode());
//					analysisModel.setBatchNo(batchModel.getBatchNo());
//					analysisModel.setProductionDate(batchModel.getProductionDate());
//					analysisModel.setOverdueDate(batchModel.getOverdueDate());
//					analysisModel.setIsGroup(DERP_ORDER.PURCHASEANALYSIS_ISGROUP_0);
//					analysisModel.setTallyingUnit(batchModel.getTallyingUnit());
//					analysisModel.setRelDate(TimeUtils.getNow());
//
//					/**若采购数量小于批次入库数量*/
//					if(analysisModel.getPurchaseNum() <= tallyNum) {
//
//						analysisModel.setWarehouseNum(analysisModel.getPurchaseNum());
//						analysisModel.setModifyDate(TimeUtils.getNow());
//
//						tallyNum -= analysisModel.getPurchaseNum() ;
//
//						batchModel.setExpireNum(0);
//						batchModel.setWornNum(0);
//						batchModel.setNormalNum(tallyNum);
//
//						purchaseAnalysisDao.modify(analysisModel) ;
//
//						break ;
//
//					}
//					/**若采购数量大于批次入库数量，按批次入库数量分拆多条*/
//					else {
//
//						PurchaseAnalysisModel targetModel = new PurchaseAnalysisModel() ;
//
//						BeanUtils.copyProperties(analysisModel, targetModel);
//						analysisModel.setPurchaseNum(analysisModel.getPurchaseNum() - tallyNum);
//
//						targetModel.setId(null);
//						targetModel.setWarehouseNum(tallyNum);
//						targetModel.setPurchaseNum(tallyNum);
//						targetModel.setCreateDate(TimeUtils.getNow());
//
//						purchaseAnalysisDao.save(targetModel) ;
//
//						continue ;
//					}
//
//				}
//
//				/**分配完成后，若存在未分配采购采购量，生成带采购订单信息未勾稽记录*/
//				if(analysisModel.getPurchaseNum() > 0) {
//
//					PurchaseAnalysisModel targetModel = new PurchaseAnalysisModel() ;
//
//					targetModel.setId(analysisModel.getId());
//					targetModel.setPurchaseNum(analysisModel.getPurchaseNum());
//					targetModel.setModifyDate(TimeUtils.getNow());
//
//					purchaseAnalysisDao.modify(targetModel) ;
//				}
//			}
//
//		}
//
//		PurchaseWarehouseModel warehouseModel = new PurchaseWarehouseModel();
//		warehouseModel.setId(warehouseQueryModel.getId());
//		warehouseModel.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_2);
//		purchaseWarehouseDao.modify(warehouseModel);
//
//	}

	/**
	 * 保存汇率
	 *
	 * @throws SQLException
	 */
	@Override
	public void saveRate(PurchaseOrderModel purchaseOrder, Timestamp date) throws SQLException {
		/**
		 * 取对应商家信息维护的结算币种
		 */
		String dateStr = TimeUtils.format(date, "yyyy-MM-dd");
		String accountCurrency = purchaseOrder.getTgtCurrency();
		String currency = purchaseOrder.getCurrency();
		Double rate = null;

		/**缺少本位币时，先回填本位币*/
		if(StringUtils.isBlank(accountCurrency)) {
			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			queryMap.put("merchantId", purchaseOrder.getMerchantId()) ;

			MerchantInfoMongo merchantInfoMongo = merchantMongoDao.findOne(queryMap) ;

			if(merchantInfoMongo != null) {
				accountCurrency = merchantInfoMongo.getAccountCurrency();

				PurchaseOrderModel updateModel = new PurchaseOrderModel();
				updateModel.setId(purchaseOrder.getId());
				updateModel.setTgtCurrency(accountCurrency);
				updateModel.setModifyDate(TimeUtils.getNow());
				purchaseOrderDao.modify(updateModel);

			}
		}

		/**
		 * 1、判断若为非“卓烨主体+母婴美赞臣组 / 母婴美赞臣分销组 ”的采购单入库时，维持现有的更新逻辑，即：
		 * （1）校验采购单中的采购币种与该主体在公司档案中维护的核算币种若一致时，则默认汇率为1，且更新本位币单价tgt_price等于采购不含税单价price、本位币金额tgt_amount等于采购不含税总金额amount；
		 * （2）若校验采购单中的采购币种与该主体在公司档案中维护的核算币种若不一致时，则以采购币种做为原币，核算币种做为本币，入库日期做为汇率日期的条件查询汇率列表，获取即期汇率；
		 * （3）得出本位币单价tgt_price=即期汇率*采购不含税单价price，本位币金额tgt_amount=即期汇率*采购不含税总金额amount；
		 * （4）若查询无复核条件的即期汇率则更新本位币单价和金额均失败；
		 * （5）单价均保留8位小数，金额保留2位小数；
		 *
		 * 2、判断若为“卓烨主体+母婴美赞臣组”的采购单入库时，调整现有的更新逻辑如下：
		 * （1）根据采购入库的 “卓烨+母婴美赞臣组+条形码” 查询 “固定成本价盘表” ，找到入库月份 ≥ 价盘表中已审核状态的生效年月且审核日期为最新的一个固定成本单价 做为本位币单价tgt_price；
		 * （2）tgt_amount=本位币单价tgt_price*采购单中的采购数量；汇率rate为空无需更新；
		 * （3）若获取不到则更新本位币单价和金额均失败；
		 * （4）单价均保留8位小数，金额保留2位小数；
		 */

		//获取卓烨贸易 公司信息
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("code", "ERP26143500022");
		MerchantInfoMongo merchantMongo = merchantMongoDao.findOne(params);

		//获取母婴美赞臣组 事业部信息
		params.clear();
		params.put("code", "E080600");
		BusinessUnitMongo MYbusinessUnitMongo = businessUnitMongoDao.findOne(params);
		//母婴美赞臣分销组
		params.clear();
		params.put("code", "E080602");
		BusinessUnitMongo FXbusinessUnitMongo = businessUnitMongoDao.findOne(params);

		PurchaseOrderItemModel quertItemModel = new PurchaseOrderItemModel();
		quertItemModel.setPurchaseOrderId(purchaseOrder.getId());

		List<PurchaseOrderItemModel> itemList = purchaseOrderItemDao.list(quertItemModel);
		if(merchantMongo.getMerchantId().equals(purchaseOrder.getMerchantId()) && (MYbusinessUnitMongo.getBusinessUnitId().equals(purchaseOrder.getBuId())
				|| FXbusinessUnitMongo.getBusinessUnitId().equals(purchaseOrder.getBuId()))){
			Map<String,Object> fixedPriceParam = new HashMap<String,Object>();
			fixedPriceParam.put("merchantId",purchaseOrder.getMerchantId());
			fixedPriceParam.put("buId",purchaseOrder.getBuId());
			fixedPriceParam.put("status","1");
			for (PurchaseOrderItemModel purchaseOrderItemModel : itemList){
				fixedPriceParam.put("barcode", purchaseOrderItemModel.getBarcode());
				List<FixedCostPriceMongo> fixedCostPriceMongoList = fixedCostPriceMongoDao.findAll(fixedPriceParam);
				FixedCostPriceMongo fixedCostPriceMongo = null;
				if(fixedCostPriceMongoList != null && fixedCostPriceMongoList.size() > 0){
					//找到入库月份 ≥ 价盘表中已审核状态的生效年月且审核日期为最新的一个固定成本单价配置
					fixedCostPriceMongoList = fixedCostPriceMongoList.stream().filter(f ->TimeUtils.parse(f.getEffectiveDate(),"yyyy-MM").getTime() <= TimeUtils.parse(dateStr,"yyyy-MM").getTime())
							.sorted(Comparator.comparing(FixedCostPriceMongo :: getAuditDate).reversed()).collect(Collectors.toList());
					if(fixedCostPriceMongoList != null && fixedCostPriceMongoList.size() > 0){
						fixedCostPriceMongo = fixedCostPriceMongoList.get(0);
					}
				}

				if(fixedCostPriceMongo != null){
					//固定成本单价 做为本位币单价tgt_price
					BigDecimal tgtPrice = fixedCostPriceMongo.getFixedCost().setScale( 8, BigDecimal.ROUND_HALF_UP);
					BigDecimal tgtAmount = tgtPrice.multiply(new BigDecimal(purchaseOrderItemModel.getNum())).setScale(2, BigDecimal.ROUND_HALF_UP);

					purchaseOrderItemModel.setTgtAmount(tgtAmount);
					purchaseOrderItemModel.setTgtPrice(tgtPrice);
					purchaseOrderItemModel.setModifyDate(TimeUtils.getNow());

					purchaseOrderItemDao.modify(purchaseOrderItemModel);

				}
			}

		}else{
			if (accountCurrency.equals(currency)) {
				rate = 1.00;
			} else {
				Map<String, Object> queryRateMap = new HashMap<String, Object>();
				queryRateMap.put("origCurrencyCode", purchaseOrder.getCurrency());
				queryRateMap.put("tgtCurrencyCode", accountCurrency);
				queryRateMap.put("effectiveDate", TimeUtils.format(date, "yyyy-MM-dd"));

				ExchangeRateMongo rateMongo = exchangeRateMongoDao.findOne(queryRateMap);

				// 设置表体本位币单价，金额
				if (rateMongo != null && rateMongo.getRate() != null) {
					rate = rateMongo.getRate();
				}
			}

			if (rate != null) {

				for (PurchaseOrderItemModel purchaseOrderItemModel : itemList) {
					BigDecimal amount = purchaseOrderItemModel.getAmount();
					Integer num = purchaseOrderItemModel.getNum();

					BigDecimal tgtAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

					BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);

					purchaseOrderItemModel.setTgtAmount(tgtAmount);
					purchaseOrderItemModel.setTgtPrice(tgtPrice);
					purchaseOrderItemModel.setModifyDate(TimeUtils.getNow());

					purchaseOrderItemDao.modify(purchaseOrderItemModel);
				}
			}
		}

		PurchaseOrderModel model = new PurchaseOrderModel();
		model.setId(purchaseOrder.getId());
		model.setRate(rate); // 汇率
		model.setModifyDate(TimeUtils.getNow());
		purchaseOrderDao.modify(model);

		/**采购SD回填 汇率*/
		PurchaseSdOrderModel querySdModel = new PurchaseSdOrderModel() ;
		querySdModel.setPurchaseCode(purchaseOrder.getCode());
		querySdModel.setType(DERP_ORDER.PURCHASE_SD_ORDER_TYPE_1);

		List<PurchaseSdOrderModel> sdList = purchaseSdOrderDao.list(querySdModel);

		if(!sdList.isEmpty() && rate != null) {

			for (PurchaseSdOrderModel purchaseSdOrderModel : sdList) {
				purchaseSdOrderModel.setRate(rate);
				purchaseSdOrderModel.setInboundDate(date);
				purchaseSdOrderModel.setIsSyn("0");
				purchaseSdOrderModel.setModifyDate(TimeUtils.getNow());

				purchaseSdOrderDao.modify(purchaseSdOrderModel) ;

				PurchaseSdOrderSditemModel sdQueryItem = new PurchaseSdOrderSditemModel() ;
				sdQueryItem.setOrderId(purchaseSdOrderModel.getId());

				List<PurchaseSdOrderSditemModel> sdItemList = purchaseSdOrderSditemDao.list(sdQueryItem);

				for(PurchaseSdOrderSditemModel tempItemModel : sdItemList) {

					Integer num = tempItemModel.getNum();
					BigDecimal amount = tempItemModel.getAmount();

					BigDecimal tgtAmount = amount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

					BigDecimal tgtPrice = tgtAmount.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);


					BigDecimal sdAmount = tempItemModel.getSdAmount();

					BigDecimal tgtSdAmount = sdAmount.multiply(new BigDecimal(rate)).setScale(2, BigDecimal.ROUND_HALF_UP);

					BigDecimal tgtSdPrice = tgtSdAmount.divide(new BigDecimal(num), 8, BigDecimal.ROUND_HALF_UP);

					tempItemModel.setSdTgtAmount(tgtSdAmount);
					tempItemModel.setSdTgtPrice(tgtSdPrice);
					tempItemModel.setTgtAmount(tgtAmount);
					tempItemModel.setTgtPrice(tgtPrice);
					tempItemModel.setModifyDate(TimeUtils.getNow());

					purchaseSdOrderSditemDao.modify(tempItemModel) ;

				}
			}

		}
	}

	@Override
	public void modifyCorrelationstatus(PurchaseWarehouseModel purchaseWarehouseModel) throws SQLException {

		PurchaseWarehouseModel warehouseModel = new PurchaseWarehouseModel();
		warehouseModel.setId(purchaseWarehouseModel.getId());
		warehouseModel.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_3);
		warehouseModel.setModifyDate(TimeUtils.getNow());
		purchaseWarehouseDao.modify(warehouseModel);

	}

	@Override
	public void saveLog(User user, String module, String code, String operateAction, String result, String remark) throws SQLException {

		OperateLogModel saveModel = new OperateLogModel() ;

		saveModel.setModule(module);
		saveModel.setCreateDate(TimeUtils.getNow());
		saveModel.setOperateDate(TimeUtils.getNow());
		saveModel.setOperateId(user.getId());
		saveModel.setOperater(user.getName());
		saveModel.setOperateRemark(remark);
		saveModel.setOperateResult(result);
		saveModel.setOperateAction(operateAction);
		saveModel.setRelCode(code);

		operateLogDao.save(saveModel) ;

	}

	@Override
	public List<OperateLogDTO> getOperateLogList(OperateLogModel queryModel) throws SQLException {

		List<OperateLogModel> logList = operateLogDao.list(queryModel);
		List<OperateLogDTO> dtoList = new ArrayList<OperateLogDTO>() ;

		for (OperateLogModel operateLogModel : logList) {

			OperateLogDTO logDto = new OperateLogDTO() ;

			BeanUtils.copyProperties(operateLogModel, logDto);

			dtoList.add(logDto) ;
		}

		return dtoList;
	}


}
