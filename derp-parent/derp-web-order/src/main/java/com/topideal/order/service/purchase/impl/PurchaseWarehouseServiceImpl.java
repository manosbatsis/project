package com.topideal.order.service.purchase.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.exception.DerpException;
import com.topideal.common.system.auth.User;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.StrUtils;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.dto.common.ImportErrorMessage;
import com.topideal.entity.dto.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.*;
import com.topideal.mongo.entity.*;
import com.topideal.order.service.common.CommonBusinessService;
import com.topideal.order.service.purchase.PurchaseWarehouseService;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 采购入库单service实现类
 */
@Service
public class PurchaseWarehouseServiceImpl implements PurchaseWarehouseService {

	/* 打印日志 */
	protected Logger LOGGER = LoggerFactory.getLogger(PurchaseWarehouseServiceImpl.class);

	// 采购入库
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;
	// 采购入库表体
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao;
	// 采购订单
	@Autowired
	private PurchaseOrderDao purchaseOrderDao;
	// 采购订单表体
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao;
	// 采购入库批次表
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;
	// 采购入库关联采购订单表
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	// 采购入库分析差异表
//	@Autowired
//	private PurchaseAnalysisDao purchaseAnalysisDao;
	// 商品
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 仓库
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	// 仓库 附表
	@Autowired
	private DepotScheduleMongoDao depotScheduleMongoDao;
	// mq
	@Autowired
	private RMQProducer rocketMQProducer;
	// 采购关联入库
	@Autowired
	private PurchaseCorrelationDao purchaseCorrelationDao;
	// 商家信息
	@Autowired
	private MerchantInfoMongoDao merchantInfoMongoDao;
	@Autowired
	private FinanceCloseAccountsMongoDao financeCloseAccountsMongoDao;// 财务经销存关账mongdb
	@Autowired
	private UserBuRelMongoDao userBuRelMongoDao;
	@Autowired
	private CommonBusinessService commonBusinessService;
	@Autowired
	private DeclareOrderDao declareOrderDao;
	@Autowired
	private DeclarePurchaseRelDao declarePurchaseRelDao;
	@Autowired
	private DeclareOrderItemDao declareOrderItemDao;

	/**
	 * 列表（分页）
	 *
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PurchaseWarehouseDTO listPurchaseWarehousePage(PurchaseWarehouseDTO dto, User user) throws SQLException {

		if (dto.getBuId() == null) {
			List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

			if (buIds.isEmpty()) {
				dto.setList(new ArrayList<>());
				dto.setTotal(0);

				return dto;
			}

			dto.setBuIds(buIds);
		}

		dto = purchaseWarehouseDao.getListByPage(dto);
		List<PurchaseWarehouseDTO> list = dto.getList();
		for (PurchaseWarehouseDTO d : list) {
			// 根据预申报单id获取采购订单编码集合
			List<String> resultList = purchaseWarehouseDao.getWarehouseListById(d.getId());
			if (!resultList.isEmpty()) {
				String code = "";
				for (String tempCode : resultList) {
					code += tempCode + ",";
				}
				// 采购订单编码
				d.setPurchaseCode(code.substring(0, code.length() - 1));
			}

		}
		dto.setList(list);
		return dto;
	}

	/**
	 * 根据id获取详情
	 *
	 * @param id
	 * @return
	 */
	@Override
	public PurchaseWarehouseModel searchDetail(Long id) throws SQLException {
		PurchaseWarehouseModel model = new PurchaseWarehouseModel();
		model.setId(id);
		return purchaseWarehouseDao.getDetails(model);
	}

	/**
	 * 导入关联采购单
	 *
	 * @param data
	 * @param user
	 * @return map
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> importRelation(List<Map<String, String>> data, User user) throws Exception {
//
//		Map<String, Object> returnMap = new HashMap<String, Object>();
//
//		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
//		List<Long> pushInventoryOrders = new ArrayList<Long>();
//
//		int success = 0;
//		int pass = 0;
//		int failure = 0;
//
//		for (int j = 1; j <= data.size(); j++) {
//			Map<String, String> map = data.get(j - 1);
//			String isArticulation = map.get("是否勾稽");
//			if (checkIsNullOrNot(j, isArticulation, "是否勾稽不能为空", resultList)) {
//				failure += 1;
//				continue;
//			}
//
//			if (!"否".equals(isArticulation) && !"是".equals(isArticulation)) {
//				setErrorMsg(j, "是否勾稽只能为'是'或者'否'", resultList);
//				failure += 1;
//				continue;
//			}
//
//			if ("否".equals(isArticulation)) {
//
//				String warehouseCode = map.get("入库单号");
//				if (checkIsNullOrNot(j, warehouseCode, "入库单号不能为空", resultList)) {
//					failure += 1;
//					continue;
//				}
//				warehouseCode = warehouseCode.trim();
//
//				String goodsNo = map.get("商品货号");
//				if (checkIsNullOrNot(j, goodsNo, "商品货号不能为空", resultList)) {
//					failure += 1;
//					continue;
//				}
//				goodsNo = goodsNo.trim();
//
//				String numStr = map.get("未勾稽数量");
//				if (checkIsNullOrNot(j, numStr, "未勾稽数量不能为空", resultList)) {
//					failure += 1;
//					continue;
//				} else if (!StringUtils.isNumeric(numStr)) {
//					setErrorMsg(j, "未勾稽数量不为数值", resultList);
//					failure += 1;
//					continue;
//				}
//				numStr = numStr.trim();
//
//				int num = Integer.parseInt(numStr);
//				if (num <= 0) {
//					setErrorMsg(j, "未勾稽数量必须大于0", resultList);
//					failure += 1;
//					continue;
//				}
//
//				String purchaseCode = map.get("采购订单号");
//				if (checkIsNullOrNot(j, numStr, "采购订单号不能为空", resultList)) {
//					failure += 1;
//					continue;
//				}
//				purchaseCode = purchaseCode.trim();
//
//				PurchaseWarehouseModel purchaseWarehouse = new PurchaseWarehouseModel();
//				purchaseWarehouse.setCode(warehouseCode);
//				purchaseWarehouse.setMerchantId(user.getMerchantId());
//				purchaseWarehouse = purchaseWarehouseDao.searchByModel(purchaseWarehouse);
//				if (purchaseWarehouse == null) {
//					setErrorMsg(j, "入库单号不存在", resultList);
//					failure += 1;
//					continue;
//				}
//
//				// 单号发货时间不为空则检查月结、关账时间。
//		        FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
//		        closeAccountsMongo.setMerchantId(purchaseWarehouse.getMerchantId());
//		        closeAccountsMongo.setBuId(purchaseWarehouse.getBuId());
//		        closeAccountsMongo.setDepotId(purchaseWarehouse.getDepotId());
//		        String maxMonth = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
//
//		        if(purchaseWarehouse.getInboundDate() != null &&
//		        		StringUtils.isNotBlank(maxMonth)) {
//		            //获取最大关账月份下一个月1日时间
//		            String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxMonth + "-01 00:00:00"));
//		            String maxCloseNextMonthDate = nextMonth + "-01 00:00:00";
//		            // 关账下个月日期大于 入库日期
//		            if (purchaseWarehouse.getInboundDate().getTime()
//		            		< Timestamp.valueOf(maxCloseNextMonthDate).getTime()) {
//		            	setErrorMsg(j, "此入库单入库时间必须大于关账日期/月结日期", resultList);
//						failure += 1;
//						continue;
//		            }
//		        }
//
//				PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
//				purchaseOrder.setCode(purchaseCode);
//				purchaseOrder.setMerchantId(user.getMerchantId());
//				purchaseOrder = purchaseOrderDao.searchByModel(purchaseOrder);
//				if (purchaseOrder == null) {
//					setErrorMsg(j, "采购订单号不存在", resultList);
//					failure += 1;
//					continue;
//				}
//
//				// 采购订单状态不为 已审核
//				if (!DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())) {
//					setErrorMsg(j, "采购订单状态不为 已审核", resultList);
//					failure += 1;
//					continue;
//				}
//
//				Map<String, Object> merchandiseInfo_params = new HashMap<String, Object>();
//				merchandiseInfo_params.put("goodsNo", goodsNo);
//				merchandiseInfo_params.put("merchantId", user.getMerchantId());
//				merchandiseInfo_params.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
//				MerchandiseInfoMogo merchandiseInfo = merchandiseInfoMogoDao.findOne(merchandiseInfo_params);
//				if (merchandiseInfo == null) {
//					setErrorMsg(j, "该商品不存在", resultList);
//					failure += 1;
//					continue;
//				}
//
//				PurchaseWarehouseItemModel purchaseWarehouseItem = new PurchaseWarehouseItemModel();
//				purchaseWarehouseItem.setGoodsId(merchandiseInfo.getMerchandiseId());
//				purchaseWarehouseItem.setWarehouseId(purchaseWarehouse.getId());
//				purchaseWarehouseItem = purchaseWarehouseItemDao.searchByModel(purchaseWarehouseItem);
//				if (purchaseWarehouseItem == null) {
//					setErrorMsg(j, "入库单不存在该商品", resultList);
//					failure += 1;
//					continue;
//				}
//
//				PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel();
//				queryItemModel.setPurchaseOrderId(purchaseOrder.getId());
//				queryItemModel.setGoodsNo(goodsNo);
//				queryItemModel = purchaseOrderItemDao.searchByModel(queryItemModel);
//
//				if (queryItemModel == null) {
//					setErrorMsg(j, "采购订单不存在该商品", resultList);
//					failure += 1;
//					continue;
//				}
//
//				// 获取勾稽过的数量
//				PurchaseAnalysisModel purchaseAnalysis = new PurchaseAnalysisModel();
//				purchaseAnalysis.setOrderCode(purchaseOrder.getCode());
//				purchaseAnalysis.setGoodsNo(goodsNo);
//				List<PurchaseAnalysisModel> analysisList = purchaseAnalysisDao.getArticulationList(purchaseAnalysis);
//
//				int warehouseNum = 0;// 已勾稽的数量
//				for (PurchaseAnalysisModel analysisModel : analysisList) {
//					warehouseNum += analysisModel.getWarehouseNum() == null ? 0 : analysisModel.getWarehouseNum();
//				}
//
//				int unWarehouseNum = 0;// 未勾稽的数量
//				unWarehouseNum = queryItemModel.getNum() - warehouseNum;
//				if (num > unWarehouseNum) {
//					setErrorMsg(j, "未勾稽数量不能大于系统未勾稽数量，系统未勾稽数量为：" + unWarehouseNum, resultList);
//					failure += 1;
//					continue;
//				}
//
//				// 插入采购关联入库勾稽明细
//				PurchaseCorrelationModel correlation = new PurchaseCorrelationModel();
//				correlation.setWarehouseCode(warehouseCode);// 入库单编码
//				correlation.setGoodsNo(goodsNo);// 商品货号
//				correlation.setNum(num);// 勾稽数量
//				correlation.setCreater(user.getId());// 创建人
//				correlation.setPurchaseCode(purchaseCode);// 采购订单
//				purchaseCorrelationDao.save(correlation);
//
//				// 新增勾稽关系
// 				WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
//				relModel.setWarehouseId(purchaseWarehouse.getId());
//				relModel.setPurchaseOrderId(purchaseOrder.getId());
//				relModel = warehouseOrderRelDao.searchByModel(relModel);
//
//				/** 若不存在，新增关联关系 */
//				if (relModel == null) {
//					relModel = new WarehouseOrderRelModel();
//					relModel.setWarehouseId(purchaseWarehouse.getId());
//					relModel.setPurchaseOrderId(purchaseOrder.getId());
//					relModel.setCreateDate(TimeUtils.getNow());
//					relModel.setCreater(user.getId());
//
//					warehouseOrderRelDao.save(relModel);
//				}
//
//				String batchNo = map.get("批次号");
//
//				Map<String, Object> queryMap = new HashMap<String, Object>();
//				queryMap.put("batchNo", batchNo);
//				queryMap.put("id", purchaseWarehouseItem.getId());
//				// 根据入库单商品获取批次
//				List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch3(queryMap);
//
//				if (batchList.isEmpty()) {
//					setErrorMsg(j, "批次信息不存在", resultList);
//					failure += 1;
//					continue;
//				}
//
//				if (batchList.size() > 1 && StringUtils.isBlank(batchNo)) {
//					setErrorMsg(j, "货号：" + goodsNo + "系统存在批次信息，导入批次信息不能为空", resultList);
//					failure += 1;
//					continue;
//				}
//
//				PurchaseAnalysisModel queryAnalysisModel = new PurchaseAnalysisModel();
//				queryAnalysisModel.setGoodsNo(goodsNo);
//				queryAnalysisModel.setOrderId(purchaseOrder.getId());
//
//				List<PurchaseAnalysisModel> tempAnalysisList = purchaseAnalysisDao.list(queryAnalysisModel);
//				tempAnalysisList = tempAnalysisList.stream().filter(temp -> temp.getWarehouseId() == null)
//						.collect(Collectors.toList());
//
//				PurchaseWarehouseBatchModel purchaseWarehouseBatchModel = batchList.get(0);
//
//				Integer tallyNum = purchaseWarehouseBatchModel.getExpireNum()
//						+ purchaseWarehouseBatchModel.getNormalNum() + purchaseWarehouseBatchModel.getWornNum();
//
//				/**查询该采购订单，批次，货号是否存在已勾稽记录*/
//				PurchaseAnalysisModel queryTempAnalysisModel = new PurchaseAnalysisModel() ;
//				queryTempAnalysisModel.setWarehouseId(purchaseWarehouse.getId());
//				queryTempAnalysisModel.setGoodsNo(goodsNo);
//				queryTempAnalysisModel.setBatchNo(batchNo);
//
//				List<PurchaseAnalysisModel> tempBatchAnalysisList = purchaseAnalysisDao.list(queryTempAnalysisModel) ;
//
//				for (PurchaseAnalysisModel tempModel : tempBatchAnalysisList) {
//					tallyNum -= tempModel.getWarehouseNum() ;
//				}
//
//				if (tallyNum < num) {
//					setErrorMsg(j, "货号：" + goodsNo + "批次数量少于勾稽数量", resultList);
//					failure += 1;
//					continue;
//				}
//
//				/** 理货数量是否大于勾稽数量，新增临时model */
//				PurchaseAnalysisModel tempSaveModel = new PurchaseAnalysisModel();
//
//				if (tempAnalysisList.isEmpty()) {
//					PurchaseAnalysisModel saveModel = new PurchaseAnalysisModel();
//					saveModel.setBarcode(queryItemModel.getBarcode());
//					saveModel.setBuId(purchaseOrder.getBuId());
//					saveModel.setBuName(purchaseOrder.getBuName());
//					saveModel.setCreateDate(TimeUtils.getNow());
//					saveModel.setCreater(user.getId());
//					saveModel.setGoodsId(queryItemModel.getGoodsId());
//					saveModel.setGoodsName(queryItemModel.getGoodsName());
//					saveModel.setGoodsNo(queryItemModel.getGoodsNo());
//					saveModel.setIsEnd(DERP_ORDER.PURCHASEANALYSIS_ISEND_0);
//					saveModel.setIsGroup(DERP_ORDER.PURCHASEANALYSIS_ISGROUP_0);
//					saveModel.setMerchantId(purchaseOrder.getMerchantId());
//					saveModel.setMerchantName(purchaseOrder.getMerchantName());
//					saveModel.setOrderId(purchaseOrder.getId());
//					saveModel.setOrderCode(purchaseOrder.getCode());
//					saveModel.setPurchaseNum(num);
//					saveModel.setSupplierId(purchaseOrder.getSupplierId());
//					saveModel.setSupplierName(purchaseOrder.getSupplierName());
//					saveModel.setTallyingUnit(purchaseWarehouseBatchModel.getTallyingUnit());
//
//					if (tallyNum - num > 0) {
//						BeanUtils.copyProperties(saveModel, tempSaveModel);
//
//						tempSaveModel.setPurchaseNum(tempSaveModel.getPurchaseNum() - num);
//					}
//
//					saveModel.setBatchNo(batchNo);
//					saveModel.setOverdueDate(purchaseWarehouseBatchModel.getOverdueDate());
//					saveModel.setProductionDate(purchaseWarehouseBatchModel.getProductionDate());
//					saveModel.setRelDate(TimeUtils.getNow());
//					saveModel.setWarehouseCode(warehouseCode);
//					saveModel.setWarehouseId(relModel.getWarehouseId());
//					saveModel.setWarehouseNum(num);
//
//					purchaseAnalysisDao.save(saveModel);
//				} else {
//					PurchaseAnalysisModel updateAnalysisModel = tempAnalysisList.get(0);
//
//					if (tallyNum - num > 0) {
//						BeanUtils.copyProperties(updateAnalysisModel, tempSaveModel);
//
//						tempSaveModel.setId(null);
//						tempSaveModel.setPurchaseNum(tempSaveModel.getPurchaseNum() - num);
//						tempSaveModel.setCreateDate(TimeUtils.getNow());
//					}
//
//					updateAnalysisModel.setIsGroup(DERP_ORDER.PURCHASEANALYSIS_ISGROUP_0);
//					updateAnalysisModel.setPurchaseNum(num);
//					updateAnalysisModel.setBatchNo(batchNo);
//					updateAnalysisModel.setOverdueDate(purchaseWarehouseBatchModel.getOverdueDate());
//					updateAnalysisModel.setProductionDate(purchaseWarehouseBatchModel.getProductionDate());
//					updateAnalysisModel.setRelDate(TimeUtils.getNow());
//					updateAnalysisModel.setWarehouseCode(warehouseCode);
//					updateAnalysisModel.setWarehouseId(relModel.getWarehouseId());
//					updateAnalysisModel.setWarehouseNum(num);
//					updateAnalysisModel.setModifyDate(TimeUtils.getNow());
//
//					purchaseAnalysisDao.modify(updateAnalysisModel);
//
//				}
//
//				if (tempSaveModel.getOrderId() != null) {
//					purchaseAnalysisDao.save(tempSaveModel);
//				}
//
//				success++;
//
//			} else {
//				setErrorMsg(j, "是否勾稽为'是'，不作任何操作", resultList);
//				failure += 1;
//			}
//
//			// 根据商家勾稽百分比设定判断订单是否完结
//			String purchaseCode = map.get("采购订单号");
//			PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
//			purchaseOrder.setCode(purchaseCode.trim());
//			purchaseOrder.setMerchantId(user.getMerchantId());
//			purchaseOrder = purchaseOrderDao.searchByModel(purchaseOrder);
//
//			String warehouseCode = map.get("入库单号");
//			PurchaseWarehouseModel purchaseWarehouseModel = new PurchaseWarehouseModel();
//			purchaseWarehouseModel.setCode(warehouseCode);
//			purchaseWarehouseModel.setMerchantId(user.getMerchantId());
//			purchaseWarehouseModel = purchaseWarehouseDao.searchByModel(purchaseWarehouseModel);
//
//			if (purchaseOrder == null) {
//				continue;
//			}
//
//			// 已完结订单不作处理
//			if (DERP_ORDER.PURCHASEORDER_STATUS_007.equals(purchaseOrder.getStatus())) {
//				continue;
//			}
//
//			purchaseOrder = purchaseOrderDao.getDetails(purchaseOrder);
//			double total = 0.0;
//			int index = 0;
//
//			for (PurchaseOrderItemModel pModel : purchaseOrder.getItemList()) {
//				// 根据采购订单id和商品信息查询勾稽入库数量
//				PurchaseAnalysisModel pAnalysis = new PurchaseAnalysisModel();
//				pAnalysis.setOrderId(purchaseOrder.getId());// 采购订单id
//				pAnalysis.setGoodsId(pModel.getGoodsId());// 商品id
//				List<PurchaseAnalysisModel> purchaseAnalysisList = purchaseAnalysisDao.list(pAnalysis);
//				// 每一个商品对应的勾稽入库数量
//				Integer warehouseNum = 0;
//				for (PurchaseAnalysisModel paModel : purchaseAnalysisList) {
//					if (null != paModel.getWarehouseNum() && paModel.getWarehouseNum() != 0) {
//						PurchaseWarehouseModel pWarehouse = purchaseWarehouseDao.searchById(paModel.getWarehouseId());
//						// 已入仓的才会加入百分比计算
//						if (pWarehouse.getState().equals(DERP_ORDER.PURCHASEWAREHOUSE_STATE_012)) {
//							warehouseNum += paModel.getWarehouseNum();
//						}
//					}
//				}
//				// 总勾稽入库数量/采购数量
//				double temp = (double) warehouseNum / pModel.getNum();
//				total += temp;
//				index++;
//			}
//
//			// 根据采购单的商家信息在mongo中获取商家的勾稽百分比
//			double articulationPercent = 1.00;
//
//			Map<String, Object> queryMap = new HashMap<String, Object>();
//			queryMap.put("merchantId", purchaseOrder.getMerchantId());
//			queryMap.put("name", purchaseOrder.getMerchantName());
//			MerchantInfoMongo merchantInfo = merchantInfoMongoDao.findOne(queryMap);
//
//			if (merchantInfo != null) {
//				articulationPercent = merchantInfo.getArticulationPercent();
//			}
//
//			double result = 0.0;
//			if (index != 0) {
//				result = total / index;
//			}
//			// 自动完结
//			if (result >= articulationPercent) {
//				purchaseOrder.setStatus(DERP_ORDER.PURCHASEORDER_STATUS_007);// 已完结
//				purchaseOrder.setEndDate(TimeUtils.getNow());// 完结时间
//				purchaseOrder.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
//				purchaseOrderDao.modify(purchaseOrder);
//				PurchaseAnalysisModel purchaseAnalysisTemp = new PurchaseAnalysisModel();
//				purchaseAnalysisTemp.setOrderId(purchaseOrder.getId());// 采购订单id
//				List<PurchaseAnalysisModel> analysisListTemp = purchaseAnalysisDao.list(purchaseAnalysisTemp);
//				// 加上完结入库时间
//				for (PurchaseAnalysisModel pModel : analysisListTemp) {
//					pModel.setIsEnd(DERP_ORDER.PURCHASEORDER_ISEND_1);// 是否完结
//					pModel.setEndDate(TimeUtils.getNow());// 完结入库时间
//					purchaseAnalysisDao.modify(pModel);
//				}
//
//			}
//
//			Integer analysisTotalNum = 0;
//			Integer warehouseTotalNum = 0;
//
//			PurchaseWarehouseItemModel warehouseItemModel = new PurchaseWarehouseItemModel();
//			warehouseItemModel.setWarehouseId(purchaseWarehouseModel.getId());
//			List<PurchaseWarehouseItemModel> itemList = purchaseWarehouseItemDao.list(warehouseItemModel);
//
//			for (PurchaseWarehouseItemModel purchaseWarehouseItemModel : itemList) {
//				Integer tallyingNum = purchaseWarehouseItemModel.getTallyingNum();
//				warehouseTotalNum += tallyingNum;
//			}
//
//			PurchaseAnalysisModel purchaseAnalysisModel = new PurchaseAnalysisModel();
//			purchaseAnalysisModel.setWarehouseId(purchaseWarehouseModel.getId());
//			List<PurchaseAnalysisModel> analysisList = purchaseAnalysisDao.list(purchaseAnalysisModel);
//			for (PurchaseAnalysisModel tempModel : analysisList) {
//				if (tempModel.getWarehouseNum() != null) {
//					Integer analysisNum = tempModel.getWarehouseNum();
//					analysisTotalNum += analysisNum;
//				}
//			}
//
//			// 设置勾稽状态
//			PurchaseWarehouseModel warehouseModel = new PurchaseWarehouseModel();
//			warehouseModel.setId(purchaseWarehouseModel.getId());
//			if (warehouseTotalNum.compareTo(analysisTotalNum) == 0) {
//				warehouseModel.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_2);
//
//				// 推库存集合
//				pushInventoryOrders.add(purchaseWarehouseModel.getId());
//
//			} else {
//				warehouseModel.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_1);
//			}
//			purchaseWarehouseDao.modify(warehouseModel);
//
//		}
//
//		List<InvetAddOrSubtractRootJson> jsonList = new ArrayList<InvetAddOrSubtractRootJson>();
//
//		for (Long warehouseId : pushInventoryOrders) {
//
//			PurchaseWarehouseModel purchaseWarehouseModel = new PurchaseWarehouseModel();
//			purchaseWarehouseModel.setId(warehouseId);
//			purchaseWarehouseModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);// 入库中
//			purchaseWarehouseModel.setModifyDate(TimeUtils.getNow());
//			purchaseWarehouseDao.modify(purchaseWarehouseModel);
//
//			// 根据id获取信息
//			PurchaseWarehouseModel model = purchaseWarehouseDao.searchById(warehouseId);
//
//			/**
//			 * 增加库存
//			 *
//			 * @author zhanghx
//			 */
//			InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
//			inventoryRoot.setBackTopic(MQPushBackOrderEnum.MANUAL_ANALYSIS_PUSH_BACK.getTopic());
//			inventoryRoot.setBackTags(MQPushBackOrderEnum.MANUAL_ANALYSIS_PUSH_BACK.getTags());
//			Map<String, Object> customParam = new HashMap<String, Object>();
//			inventoryRoot.setCustomParam(customParam);
//			// 增加库存
//
//			inventoryRoot.setMerchantId(model.getMerchantId().toString());
//			inventoryRoot.setMerchantName(model.getMerchantName());
//			inventoryRoot.setBusinessNo(model.getCode());
//			inventoryRoot.setTopidealCode(user.getTopidealCode());
//			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
//			depotInfo_params.put("depotId", model.getDepotId());// 根据仓库id
//			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
//			if (mongo != null) {
//				inventoryRoot.setDepotCode(mongo.getCode());
//				inventoryRoot.setDepotType(mongo.getType());
//				inventoryRoot.setIsTopBooks(mongo.getIsTopBooks());
//			}
//			inventoryRoot.setDepotId(model.getDepotId().toString());
//			inventoryRoot.setDepotName(model.getDepotName());
//			inventoryRoot.setOrderNo(model.getCode());
//			inventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
//			inventoryRoot.setSourceType(InventoryStatusEnum.CGRK.getKey());
//			inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
//			inventoryRoot.setBuId(model.getBuId().toString());
//			inventoryRoot.setBuName(model.getBuName());
//
//			// 获取当前年月
//			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
//			inventoryRoot.setOwnMonth(sdf2.format(model.getInboundDate()));
//			inventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(model.getInboundDate()));
//			int depot_flag = 0;
//			if (mongo != null && mongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
//				depot_flag = 1;
//			}
//			List<InvetAddOrSubtractGoodsListJson> itemList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
//			List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch(warehouseId);
//			for (PurchaseWarehouseBatchModel bModel : batchList) {
//
//				// 坏货数量
//				if (bModel.getWornNum() != null && bModel.getWornNum() > 0) {
//					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag);
//					listJson.setNum(bModel.getWornNum());
//					listJson.setType(DERP.ISDAMAGE_1);// 商品类型（0 好品 1坏品）
//
//					if (bModel.getOverdueDate() != null) {
//						Timestamp inboundDate = model.getInboundDate();
//						int daysBetween = TimeUtils.daysBetween(bModel.getOverdueDate(), inboundDate);
//
//						if (daysBetween > 0) {
//							listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
//						} else {
//							listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
//						}
//
//					} else {
//						listJson.setIsExpire(DERP.ISEXPIRE_1);
//					}
//
//					itemList.add(listJson);
//				}
//
//				// 过期数量
//				if (bModel.getExpireNum() != null && bModel.getExpireNum() > 0) {
//					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag);
//					listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
//					listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
//					listJson.setNum(bModel.getExpireNum());
//					itemList.add(listJson);
//				}
//				// 正常数量
//				else if (bModel.getNormalNum() != null && bModel.getNormalNum() > 0) {
//					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag);
//					listJson.setNum(bModel.getNormalNum());
//					listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
//					listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
//					itemList.add(listJson);
//				}
//
//			}
//
//			inventoryRoot.setGoodsList(itemList);
//
//			jsonList.add(inventoryRoot);
//
//
//		}
//
//		pushInventory(jsonList);
//
//		returnMap.put("success", success);
//		returnMap.put("pass", pass);
//		returnMap.put("failure", failure);
//		returnMap.put("message", resultList);
		return null;
	}

	/**
	 * 确认入库
	 *
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	/**
	 * 确认入库
	 *
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<InvetAddOrSubtractRootJson> confirmDepot(List<Long> ids, Long userId, String name, String topidealCode)
			throws Exception {
		Map<String, Integer> auditedNumMap = new HashMap<>();
		for (Long id : ids) {
			// 根据id获取信息
			PurchaseWarehouseModel model = purchaseWarehouseDao.searchById(id);

			if (!model.getState().equals(DERP_ORDER.PURCHASEWAREHOUSE_STATE_011)) {
				throw new RuntimeException("状态必须为待入仓");
			}

			if(StringUtils.isNotBlank(model.getExtraCode())){
				throw new DerpException("采购入库单：" + model.getCode() + "非手动导入订单，审核失败") ;
			}
			// 根据仓库id获取仓库信息
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", model.getDepotId());
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);

			if (depot == null) {
				throw new RuntimeException("对应的仓库为空，请检查");
			}

			depotInfo_params.clear();
			depotInfo_params.put("depotId", depot.getDepotId());
			depotInfo_params.put("merchantId", model.getMerchantId());
			DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotInfo_params);

			if (depotMerchantRel == null) {
				throw new RuntimeException("商家仓库关联不存在");
			}

			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())
					|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation())) {

				List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch(model.getId());
				for (PurchaseWarehouseBatchModel batch : batchList) {
					if (StringUtils.isBlank(batch.getBatchNo()) || batch.getOverdueDate() == null
							|| batch.getProductionDate() == null) {

						throw new RuntimeException("采购入库单:" + model.getCode() + "仓库批次效期入库强校验，批次信息存在为空，不能确认入仓");

					}
				}

			}

			// 查询采购单
			WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
			relModel.setWarehouseId(model.getId());
			List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(relModel);
			relModel = relList.get(0);
			PurchaseOrderModel orderModel = purchaseOrderDao.searchById(relModel.getPurchaseOrderId());
//
//			PurchaseOrderModel orderModel = new PurchaseOrderModel();
//			orderModel.setCode(model.getDeclareOrderCode());
//			orderModel = purchaseOrderDao.searchByModel(orderModel);
//
			if (orderModel == null) {
				throw new RuntimeException("采购订单号不存在");
			}
			//是否关联销售预申报单
			DeclarePurchaseRelModel declareRel = new DeclarePurchaseRelModel();
			declareRel.setPurchaseOrderId(orderModel.getId());
			List<DeclarePurchaseRelModel> declareRelList =  declarePurchaseRelDao.list(declareRel);
			if(declareRelList != null  && declareRelList.size() > 0 && model.getDeclareOrderId() == null) {
				throw new RuntimeException("采购入库订单:"+model.getCode()+"对应的采购订单已生成预申报单，关联预申报单不能为空");
			}
			if(StringUtils.isNotBlank(model.getDeclareOrderCode())){
				PurchaseWarehouseDTO warehouseDTO = new PurchaseWarehouseDTO();
				warehouseDTO.setPurchaseCode(orderModel.getCode());
				warehouseDTO.setDeclareOrderCode(model.getDeclareOrderCode());
				List<PurchaseWarehouseDTO> queryWarehouseList = purchaseWarehouseDao.listDTO(warehouseDTO);
				if(queryWarehouseList != null && queryWarehouseList.size() > 1){
					throw new RuntimeException("采购单号："+orderModel.getCode()+",预申报订单号"+model.getDeclareOrderCode()+"存在多条采购入单");
				}
			}

			String address = "";// 目的地名称
			if (StringUtils.isNotBlank(orderModel.getDestinationName())
					&& StrUtils.stringReg(orderModel.getDestinationName(), "[0-9]*")) {
				Map<String, Object> scheduleMap = new HashMap<String, Object>();
				scheduleMap.put("depotScheduleId", Long.valueOf(orderModel.getDestinationName()));
				DepotScheduleMongo schedule = depotScheduleMongoDao.findOne(scheduleMap);
				address = schedule == null ? address : schedule.getAddress();
			}

			// 查询最大已关账月份
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(model.getMerchantId());
			closeAccountsMongo.setDepotId(model.getDepotId());
			closeAccountsMongo.setBuId(model.getBuId());

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";

				if (model.getInboundDate() != null && model.getInboundDate().getTime() < Timestamp.valueOf(maxCloseAccountsMonth).getTime()) {
					throw new RuntimeException("入仓日期必须大于关账月份/月结月份");
				}
			}
			PurchaseWarehouseItemModel itemModel = new PurchaseWarehouseItemModel();
			itemModel.setWarehouseId(id);
			List<PurchaseWarehouseItemModel> itemList = purchaseWarehouseItemDao.list(itemModel);
			for(PurchaseWarehouseItemModel item : itemList){
				if(item.getPurchaseItemId() == null){
					throw new DerpException("采购入库单:"+model.getCode()+"，商品信息关联的采购明细id为空，请及时维护") ;
				}
				//根据采购明细id获取已入库数量
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("purchaseItemId", item.getPurchaseItemId());
				paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
				List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
				Integer warehouseNum = 0;
				if(numList != null && numList.size() > 0){
					BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
					warehouseNum = queryNum.intValue();
				}
				String purchaseKey =item.getPurchaseItemId().toString();
				if(auditedNumMap.containsKey(purchaseKey)) {
					warehouseNum = warehouseNum + auditedNumMap.get(purchaseKey);//该商品累计的数量 + 已入库的数量

					Integer totalNumSum = auditedNumMap.get(purchaseKey) + item.getTallyingNum();
					auditedNumMap.put(purchaseKey, totalNumSum);//累计导入数量
				}else {
					auditedNumMap.put(purchaseKey, item.getTallyingNum());
				}
				PurchaseOrderItemModel purchaseOrderItemModel = purchaseOrderItemDao.searchById(item.getPurchaseItemId());
				Integer totalNum = purchaseOrderItemModel.getNum() - warehouseNum;	// 减去后的数量
				if(item.getTallyingNum()>totalNum){
					throw new RuntimeException("采购订单号:"+orderModel.getCode()+"货号:"+item.getGoodsNo()+"剩余入库量为:"+ totalNum);
				}
				if(model.getDeclareOrderId() != null ){
					DeclareOrderItemModel declareItemModel = new DeclareOrderItemModel();
					declareItemModel.setDeclareOrderId(model.getDeclareOrderId());
					declareItemModel.setPurchaseItemId(item.getPurchaseItemId());
					declareItemModel = declareOrderItemDao.searchByModel(declareItemModel);

					Integer totalDeclareNum = declareItemModel.getNum() - item.getTallyingNum();	// 减去后的数量
					if(totalDeclareNum != 0){
						throw new RuntimeException("预申报单:"+model.getDeclareOrderCode()+"货号:"+item.getGoodsNo()+"（好品数量+坏品数量）必须等于申报数量");
					}
				}
			}
		}

		List<InvetAddOrSubtractRootJson> jsonList = new ArrayList<InvetAddOrSubtractRootJson>();
		for (Long id : ids) {

			// 根据id获取信息
			PurchaseWarehouseModel model = purchaseWarehouseDao.searchById(id);
			// 查询采购单
			WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
			relModel.setWarehouseId(model.getId());
			List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(relModel);
			relModel = relList.get(0);
			PurchaseOrderModel orderModel = purchaseOrderDao.searchById(relModel.getPurchaseOrderId());
			/**
			 * 增加库存
			 *
			 * @author zhanghx
			 */
			InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
			inventoryRoot.setBackTopic(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTopic());
			inventoryRoot.setBackTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTags());
			Map<String, Object> customParam = new HashMap<String, Object>();
			inventoryRoot.setCustomParam(customParam);
			// 增加库存

			inventoryRoot.setMerchantId(model.getMerchantId().toString());
			inventoryRoot.setMerchantName(model.getMerchantName());
			inventoryRoot.setBusinessNo(model.getDeclareOrderCode());
			inventoryRoot.setTopidealCode(topidealCode);
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", model.getDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
			if (mongo != null) {
				inventoryRoot.setDepotCode(mongo.getCode());
				inventoryRoot.setDepotType(mongo.getType());
				inventoryRoot.setIsTopBooks(mongo.getIsTopBooks());
			}
			inventoryRoot.setDepotId(model.getDepotId().toString());
			inventoryRoot.setDepotName(model.getDepotName());
			inventoryRoot.setOrderNo(model.getCode());
			inventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
			inventoryRoot.setSourceType(InventoryStatusEnum.CGRK.getKey());
			inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
			inventoryRoot.setBuId(model.getBuId().toString());
			inventoryRoot.setBuName(model.getBuName());

			// 获取当前年月
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
			inventoryRoot.setOwnMonth(sdf2.format(model.getInboundDate()));
			inventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(model.getInboundDate()));
			int depot_flag = 0;
			if (mongo != null && mongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
				depot_flag = 1;
			}
			List<InvetAddOrSubtractGoodsListJson> itemList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			List<PurchaseWarehouseBatchModel> batchList = purchaseWarehouseBatchDao.getGoodsAndBatch(id);
			for (PurchaseWarehouseBatchModel bModel : batchList) {

				// 坏货数量
				if (bModel.getWornNum() != null && bModel.getWornNum() > 0) {
					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag,topidealCode);
					listJson.setNum(bModel.getWornNum());
					listJson.setType(DERP.ISDAMAGE_1);// 商品类型（0 好品 1坏品）

					if (bModel.getOverdueDate() != null) {
						Timestamp inboundDate = model.getInboundDate();
						int daysBetween = TimeUtils.daysBetween(bModel.getOverdueDate(), inboundDate);

						if (daysBetween > 0) {
							listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
						} else {
							listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
						}

					} else {
						listJson.setIsExpire(DERP.ISEXPIRE_1);
					}
					listJson.setStockLocationTypeId(orderModel.getStockLocationTypeId() == null ?"" :orderModel.getStockLocationTypeId().toString());
					listJson.setStockLocationTypeName(orderModel.getStockLocationTypeName());
					itemList.add(listJson);
				}

				// 过期数量
				if (bModel.getExpireNum() != null && bModel.getExpireNum() > 0) {
					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag,topidealCode);
					listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
					listJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
					listJson.setNum(bModel.getExpireNum());
					listJson.setStockLocationTypeId(orderModel.getStockLocationTypeId() == null ?"" :orderModel.getStockLocationTypeId().toString());
					listJson.setStockLocationTypeName(orderModel.getStockLocationTypeName());
					itemList.add(listJson);
				}
				// 正常数量
				else if (bModel.getNormalNum() != null && bModel.getNormalNum() > 0) {
					InvetAddOrSubtractGoodsListJson listJson = generateTransportGoods(bModel, depot_flag,topidealCode);
					listJson.setNum(bModel.getNormalNum());
					listJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品 ）
					listJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
					listJson.setStockLocationTypeId(orderModel.getStockLocationTypeId() == null ?"" :orderModel.getStockLocationTypeId().toString());
					listJson.setStockLocationTypeName(orderModel.getStockLocationTypeName());
					itemList.add(listJson);
				}

			}

			inventoryRoot.setGoodsList(itemList);

			jsonList.add(inventoryRoot);

		}

		return jsonList;
	}

	/**
	 * 导入入库单
	 *
	 * @param data
	 * @param user
	 * @return map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> saveImportWarehouse(List<Map<String, String>> data, User user) throws Exception {

		// 存储入库单
		Map<String, List<Map<String, Object>>> warehouseMap = new HashMap<String, List<Map<String, Object>>>();
		List<ImportErrorMessage> resultList = new ArrayList<ImportErrorMessage>();
		Map<String , Integer> checkGoodsNum = new HashMap<String, Integer>();//记录采购单号+商品货号+单价维度
		Map<String , Integer> checkDeclareGoodsNum = new HashMap<String, Integer>();//记录预申报蛋+采购单号+商品货号+单价维度
		Map<String , PurchaseWarehouseModel> purchaseWareHouseMap = new HashMap<String , PurchaseWarehouseModel>() ;
		Map<String , List<PurchaseWarehouseItemDTO>> purchaseWarehouseItemMap = new HashMap<String , List<PurchaseWarehouseItemDTO>>();
		int success = 0;
		int pass = 0;
		int failure = 0;

		for (int j = 1; j <= data.size(); j++) {

			Map<String, String> map = data.get(j - 1) ;
			// 必填字段的校验
			String orderCode = map.get("采购订单号");
			if (checkIsNullOrNot(j, orderCode, "采购订单号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			orderCode = orderCode.trim();

			// 获取采购订单(根据订单编码、商家id)
			PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
			purchaseOrder.setCode(orderCode);
			purchaseOrder.setMerchantId(user.getMerchantId());
			purchaseOrder = purchaseOrderDao.searchByModel(purchaseOrder);
			if (purchaseOrder == null) {
				setErrorMsg(j, "采购订单不存在", resultList);
				failure += 1;
				continue;
			}

			// 采购订单状态不为 已审核
			if (!(DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrder.getStatus())
					|| DERP_ORDER.PURCHASEORDER_STATUS_005.equals(purchaseOrder.getStatus())
					)) {
				setErrorMsg(j, "该采购订单状态非已审核或部分入库状态", resultList);
				failure += 1;
				continue;
			}

			//检查采购订单是否“待红冲”或“已红冲”
			if (StringUtils.isNotBlank(purchaseOrder.getWriteOffStatus())) {
				setErrorMsg(j, "关联采购订单：" + purchaseOrder.getCode() + DERP_ORDER.getLabelByKey(DERP_ORDER.purchaseOrder_writeOffStatusList, purchaseOrder.getWriteOffStatus()), resultList);
				failure += 1;
				continue;
			}

			boolean isRelate = userBuRelMongoDao.isUserRelateBu(user.getId(), purchaseOrder.getBuId());
			if (!isRelate) {
				setErrorMsg(j, "用户无权限操作该事业部", resultList);
				failure += 1;
				continue;
			}

			String inboundDate = map.get("入库日期");
			if (checkIsNullOrNot(j, inboundDate, "入库日期不能为空", resultList)) {
				failure += 1;
				continue;
			}

			if (!isDate(inboundDate)) {
				setErrorMsg(j, "入库日期格式不正确，正确为：yyyy-MM-dd", resultList);
				failure += 1;
				continue;
			}

			if (TimeUtils.daysBetween(TimeUtils.parse(inboundDate, "yyyy-MM-dd"), new Date()) < 0) {
				setErrorMsg(j, "入库日期不可超过当前时间", resultList);
				failure += 1;
				continue;
			}

			String goodsNo = map.get("商品货号");
			if (checkIsNullOrNot(j, goodsNo, "商品货号不能为空", resultList)) {
				failure += 1;
				continue;
			}
			goodsNo = goodsNo.trim();

			String purchasePrice = map.get("采购单价");

			// 校验商品货号是否在采购货号里面
			Long purchaseOrderId = purchaseOrder.getId();

			String declareOrderCode = map.get("关联预申报单号");
			DeclarePurchaseRelModel declareRel = new DeclarePurchaseRelModel();
			declareRel.setPurchaseOrderId(purchaseOrder.getId());
			List<DeclarePurchaseRelModel> declareRelList =  declarePurchaseRelDao.list(declareRel);
			if(declareRelList != null && declareRelList.size() > 0 && StringUtils.isBlank(declareOrderCode)){
				setErrorMsg(j, "采购订单:"+orderCode+"已生成预申报单，关联预申报单不能为空", resultList);
				failure += 1;
				continue ;
			}
			Long inDepotId = null;
			DeclareOrderDTO declareDTO = null;
			DeclareOrderItemModel declareOrderItemModel = null;
			if(StringUtils.isNotBlank(declareOrderCode)) {
				declareOrderCode = declareOrderCode.trim();
				declareDTO = new DeclareOrderDTO();
				declareDTO.setCode(declareOrderCode);
				declareDTO.setPurchaseCode(orderCode);
				List<DeclareOrderDTO> declareList = declareOrderDao.listDTO(declareDTO);
				if(declareList == null || declareList.size() == 0) {
					setErrorMsg(j, "采购订单关联预申报单:"+declareOrderCode+"不存在", resultList);
					failure += 1;
					continue ;
				}
				declareDTO = declareList.get(0);

				PurchaseWarehouseDTO warehouseDTO = new PurchaseWarehouseDTO();
				warehouseDTO.setPurchaseCode(orderCode);
				warehouseDTO.setDeclareOrderCode(declareOrderCode);
				List<PurchaseWarehouseDTO> queryWarehouseList = purchaseWarehouseDao.listDTO(warehouseDTO);
				if(queryWarehouseList != null && queryWarehouseList.size() > 0){
					setErrorMsg(j, "采购单号："+orderCode+",预申报订单号"+declareOrderCode+"已存在采购入库单", resultList);
					failure += 1;
					continue;
				}
				inDepotId = declareDTO.getDepotId();
			}


			String inDepotCode = map.get("入仓仓库自编码");
			if(StringUtils.isBlank(inDepotCode) && StringUtils.isBlank(declareOrderCode)){
				setErrorMsg(j, "关联预申报单号为空时入库仓库必填", resultList);
				failure += 1;
				continue;
			}
			DepotInfoMongo inDepot = null;
			if(StringUtils.isNotBlank(inDepotCode)){
				Map<String, Object> depotInfoParams = new HashMap<String, Object>();
				depotInfoParams.put("depotCode", inDepotCode);
				depotInfoParams.put("status", DERP_SYS.DEPOTINFO_STATUS_1);

				inDepot = depotInfoMongoDao.findOne(depotInfoParams);
				if(inDepot == null){
					setErrorMsg(j, "入仓仓库自编码："+inDepotCode+"未找到启用仓库", resultList);
					failure += 1;
					continue;
				}
				if(declareDTO != null && declareDTO.getDepotId().intValue() != inDepot.getDepotId().intValue()){
					setErrorMsg(j, "预申报单号："+declareDTO.getCode()+"入库仓与导入模板入库仓不一致", resultList);
					failure += 1;
					continue;
				}

				depotInfoParams.clear();
				depotInfoParams.put("depotId", inDepot.getDepotId());
				depotInfoParams.put("merchantId", user.getMerchantId());
				DepotMerchantRelMongo depotMerchantRel = depotMerchantRelMongoDao.findOne(depotInfoParams);

				if (depotMerchantRel == null) {
					setErrorMsg(j, "商家仓库关联不存在", resultList);
					failure += 1;
					continue;
				}

				inDepotId = inDepot.getDepotId();
			}else{
				Map<String, Object> depotInfoParams = new HashMap<String, Object>();
				depotInfoParams.put("depotId", declareDTO.getDepotId());
				depotInfoParams.put("status", DERP_SYS.DEPOTINFO_STATUS_1);

				inDepot = depotInfoMongoDao.findOne(depotInfoParams);
			}
			String tallyingUnit = map.get("海外仓理货单位");
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepot.getType()) && StringUtils.isBlank(tallyingUnit)){
				setErrorMsg(j, "入库仓编码" + inDepotCode+"为海外仓，理货单位不能为空", resultList);
				failure += 1;
				continue;
			}
			// 查询商品
			Map<String, Object> merchandiseMap = new HashMap<String, Object>();
			merchandiseMap.put("goodsNo", goodsNo);
			merchandiseMap.put("merchantId", user.getMerchantId());
			merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);
			List<MerchandiseInfoMogo> merchandises = merchandiseInfoMogoDao.findMerchandiseByDepotId(merchandiseMap,inDepotId);
			if (merchandises == null || merchandises.size() < 1) {
				setErrorMsg(j, "未找到关联入库仓的对应商品,货号：" + goodsNo+"", resultList);
				failure += 1;
				continue;
			}
			MerchandiseInfoMogo merchandise = merchandises.get(0);

			PurchaseOrderItemModel purchaseOrderItemModel = new PurchaseOrderItemModel();
			purchaseOrderItemModel.setBarcode(merchandise.getBarcode());
			purchaseOrderItemModel.setPurchaseOrderId(purchaseOrderId);
//			purchaseOrderItemModel.setPrice(new BigDecimal(purchasePrice));
			List<PurchaseOrderItemModel> tempItemList = purchaseOrderItemDao.list(purchaseOrderItemModel);
			if(tempItemList == null || tempItemList.size() < 1){
				setErrorMsg(j, "货号：" + goodsNo+" 对应的条形码在采购订单不存在" , resultList);
				failure += 1;
				continue;
			}
			if(tempItemList != null && tempItemList.size() > 1 ){
				if(StringUtils.isBlank(purchasePrice)){
					setErrorMsg(j, "货号：" + goodsNo+" 对应的条形码在存在多条" , resultList);
					failure += 1;
					continue;
				}else{
					purchasePrice = purchasePrice.trim();
					for(PurchaseOrderItemModel temp: tempItemList){
						if(temp.getPrice().compareTo(new BigDecimal(purchasePrice)) == 0){
							purchaseOrderItemModel = temp;
							break;
						}
					}
					if (purchaseOrderItemModel == null) {
						setErrorMsg(j, "导入的商品货号对应的条形码+单价必须为对应采购订单中的条形码+单价", resultList);
						failure += 1;
						continue;
					}
				}
			}else if(tempItemList != null &&  tempItemList.size() == 1){
				purchaseOrderItemModel = tempItemList.get(0);
			}

			purchasePrice = purchaseOrderItemModel.getPrice().toString();
			purchasePrice = purchasePrice.trim();
			if(declareDTO != null){
				declareOrderItemModel = new DeclareOrderItemModel();
				declareOrderItemModel.setDeclareOrderId(declareDTO.getId());
				declareOrderItemModel.setGoodsNo(goodsNo);
				declareOrderItemModel.setPurchaseId(purchaseOrderId);
				declareOrderItemModel.setPurchasePrice(new BigDecimal(purchasePrice));
				declareOrderItemModel = declareOrderItemDao.searchByModel(declareOrderItemModel);
				if (declareOrderItemModel == null) {
					setErrorMsg(j, "导入的采购单号+商品货号+采购单价必须为对应预申报订单中的采购单号+商品货号+采购单价", resultList);
					failure += 1;
					continue;
				}
			}

			String normalNumStr = map.get("入库正常品量");
			if ( !StrUtils.stringReg(normalNumStr, "[0-9]*")) {
				setErrorMsg(j, "入库正常品量不正确", resultList);
				failure += 1;
				continue;
			}
			normalNumStr = StringUtils.isBlank(normalNumStr) ? "0":normalNumStr.trim();

			String wornNumStr = map.get("入库残次品量");
			if (!StrUtils.stringReg(wornNumStr, "[0-9]*")) {
				setErrorMsg(j, "入库残次品量不正确", resultList);
				failure += 1;
				continue;
			}
			wornNumStr =StringUtils.isBlank(wornNumStr) ? "0": wornNumStr.trim();

			int normalNum = Integer.parseInt(normalNumStr);
			int wornNum = Integer.parseInt(wornNumStr);
			if (normalNum <= 0 && wornNum <= 0) {
				setErrorMsg(j, "入库正常品量、入库残次品量两个必须有一个量大于0，且均不能小于0", resultList);
				failure += 1;
				continue;
			}
			Integer totalGoodsNum = normalNum+wornNum;	// 导入进来的好品+坏品数量

			//根据采购id、商品货号、单价 获取已入库数量
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("purchaseOrderId", purchaseOrder.getId());
			paramMap.put("goodsNo",goodsNo);
			paramMap.put("purchasePrice",purchasePrice);
			paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
			List<Map<String, Object>> numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
			Integer warehouseNum = 0;
			if(numList != null && numList.size() > 0){
				BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
				warehouseNum = queryNum.intValue();
			}
			String purchaseKey =orderCode+goodsNo+purchasePrice;
			if(checkGoodsNum.containsKey(purchaseKey)) {
				warehouseNum = warehouseNum + checkGoodsNum.get(purchaseKey);//当前导入文件 该商品累计的数量 + 已入库的数量

				Integer totalNumSum = checkGoodsNum.get(purchaseKey) + totalGoodsNum;
				checkGoodsNum.put(purchaseKey, totalNumSum);//累计导入数量
			}else {
				checkGoodsNum.put(purchaseKey, totalGoodsNum);
			}

			Integer totalNum = purchaseOrderItemModel.getNum() - warehouseNum;	// 减去后的数量
			if(totalGoodsNum>totalNum){
				setErrorMsg(j, "采购订单号:"+orderCode+"货号:"+merchandise.getGoodsNo()+"剩余入库量为:"+ totalNum, resultList);
				failure += 1;
				continue ;
			}
			if(declareOrderItemModel != null ){
				paramMap.clear();
				paramMap.put("declareOrderId", declareDTO.getId());
				paramMap.put("purchaseOrderId", purchaseOrder.getId());
				paramMap.put("goodsNo",goodsNo);
				paramMap.put("purchasePrice",purchasePrice);
				paramMap.put("state",DERP_ORDER.PURCHASEWAREHOUSE_STATE_012);
				numList = purchaseWarehouseItemDao.getWarehouseItem(paramMap);
				Integer declareWarehouseNum = 0;
				if(numList != null && numList.size() > 0){
					BigDecimal queryNum = (BigDecimal) numList.get(0).get("num");//当前商品已入库数量
					declareWarehouseNum = queryNum.intValue();
				}
				String declareKey = orderCode+goodsNo+purchasePrice+declareOrderCode;
				if(checkDeclareGoodsNum.containsKey(declareKey)) {
					declareWarehouseNum = declareWarehouseNum + checkDeclareGoodsNum.get(declareKey);//当前导入文件 该商品累计的数量 + 已入库的数量

					Integer totalNumSum = checkDeclareGoodsNum.get(declareKey) + totalGoodsNum;
					checkDeclareGoodsNum.put(declareKey, totalNumSum);//累计导入数量
				}else {
					checkDeclareGoodsNum.put(declareKey, totalGoodsNum);
				}

				Integer totalDeclareNum = declareOrderItemModel.getNum() - declareWarehouseNum;	// 减去后的数量
				if(totalGoodsNum>totalDeclareNum){
					setErrorMsg(j, "预申报单:"+declareOrderCode+"货号:"+merchandise.getGoodsNo()+"剩余入库量为:"+ totalDeclareNum, resultList);
					failure += 1;
					continue ;
				}
			}

			String batchNo = map.get("批次号");
			String productionDateStr = map.get("生产日期");
			String overdueDateStr = map.get("效期至");
			Date overdueDate = null;
			int expireNum = 0;
			// 若填入仓库是批次强校验仓库，批次效期信息必填
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepot.getBatchValidation())
					|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepot.getBatchValidation())) {

				if (checkIsNullOrNot(j, batchNo, "批次号不能为空", resultList)) {
					failure += 1;
					continue;
				}

				if (checkIsNullOrNot(j, productionDateStr, "生产日期不能为空", resultList)) {
					failure += 1;
					continue;
				}

				if (checkIsNullOrNot(j, overdueDateStr, "效期至日期不能为空", resultList)) {
					failure += 1;
					continue;
				}
			}

			if (StringUtils.isNotBlank(batchNo)) {
				batchNo = batchNo.trim();
			}
			if(StringUtils.isNotBlank(productionDateStr) && !isYmdDate(productionDateStr)){
				setErrorMsg(j, "生产日期格式不正确,正确格式为:yyyy-MM-dd", resultList);
				failure += 1;
				continue;
			}

			if (StringUtils.isNotBlank(overdueDateStr)) {
				if(!isYmdDate(overdueDateStr)) {
					setErrorMsg(j, "效期至日期格式不正确,正确格式为:yyyy-MM-dd", resultList);
					failure += 1;
					continue;
				}

				// 判断是否过期品
				Date inboundDateD = TimeUtils.parseDay(inboundDate);
				overdueDate = TimeUtils.parseDay(overdueDateStr);
				int expireDayBetween = TimeUtils.daysBetween(overdueDate, inboundDateD);
				if (expireDayBetween > 0) {
					expireNum = normalNum;
					normalNum=0;
				}
			}

			// 判断归属日期是否小于 关账日期/月结日期
			FinanceCloseAccountsMongo closeAccountsMongo = new FinanceCloseAccountsMongo();
			closeAccountsMongo.setMerchantId(user.getMerchantId());
			closeAccountsMongo.setDepotId(purchaseOrder.getDepotId());
			closeAccountsMongo.setBuId(purchaseOrder.getBuId());

			String maxdate = financeCloseAccountsMongoDao.findFinanceCloseAccount(closeAccountsMongo, DERP.CLOSEACCOUNTFLAG3);
			String maxCloseAccountsMonth = "";
			if (StringUtils.isNotBlank(maxdate)) {
				// 获取该月份下月时间
				String nextMonth = TimeUtils.getNextMonth(Timestamp.valueOf(maxdate + "-01 00:00:00"));
				maxCloseAccountsMonth = nextMonth + "-01 00:00:00";
			}
			if (StringUtils.isNotBlank(maxCloseAccountsMonth)) {
				// 关账下个月日期大于 入库日期
				if (Timestamp.valueOf(inboundDate + " 00:00:00").getTime() < Timestamp
						.valueOf(maxCloseAccountsMonth).getTime()) {
					setErrorMsg(j, "归属日期必须大于关账日期/月结日期", resultList);
					failure += 1;
					continue;
				}
			}
			if(!purchaseWareHouseMap.containsKey(orderCode +"_"+declareOrderCode+"_"+ inDepot.getDepotId()+"_"+inboundDate)){
				PurchaseWarehouseModel purchaseWarehouse = new PurchaseWarehouseModel();
				purchaseWarehouse.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));// 入库单号
				purchaseWarehouse.setDepotId(inDepot.getDepotId());// 仓库iD
				purchaseWarehouse.setDepotName(inDepot.getName());// 仓库名称
				purchaseWarehouse.setCreater(user.getId());// 创建人
				purchaseWarehouse.setMerchantId(user.getMerchantId());// 商家id
				purchaseWarehouse.setMerchantName(user.getMerchantName());// 商家名称
				purchaseWarehouse.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_011);// 待入仓
				if(declareDTO != null){
					purchaseWarehouse.setDeclareOrderId(declareDTO.getId());
					purchaseWarehouse.setDeclareOrderCode(declareDTO.getCode());
				}
				purchaseWarehouse.setContractNo(purchaseOrder.getContractNo());// 合同号
				purchaseWarehouse.setInboundDate(TimeUtils.parse(inboundDate, "yyyy-MM-dd"));
				purchaseWarehouse.setBusinessModel(purchaseOrder.getBusinessModel()); // 设置业务主体
				purchaseWarehouse.setBuId(purchaseOrder.getBuId()); // 事业部
				purchaseWarehouse.setBuName(purchaseOrder.getBuName());
				purchaseWarehouse.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_1);
				purchaseWarehouse.setCurrency(purchaseOrder.getCurrency());
				if(inDepot!=null){
					if(DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepot.getType())){
						purchaseWarehouse.setTallyingUnit(tallyingUnit);// 海外仓理货单位
					}
				}
				purchaseWarehouse.setCreater(user.getId());
				purchaseWarehouse.setCreateDate(TimeUtils.getNow());
				purchaseWareHouseMap.put(orderCode+"_"+declareOrderCode+"_"+ inDepot.getDepotId()+"_"+inboundDate, purchaseWarehouse);
			}

			PurchaseWarehouseItemDTO itemDTO = new PurchaseWarehouseItemDTO();
			itemDTO.setGoodsId(merchandise.getMerchandiseId());// 商品id
			itemDTO.setGoodsNo(merchandise.getGoodsNo());// 商品货号
			itemDTO.setGoodsName(merchandise.getName());// 商品名称
			itemDTO.setPurchaseItemId(purchaseOrderItemModel.getId());// 采购表体id

			Double grossWeight = merchandise.getGrossWeight();// 毛重
			Double netWeight = merchandise.getNetWeight();// 净重

			if (grossWeight != null) {
				grossWeight = grossWeight * totalGoodsNum;
			}

			if (netWeight != null) {
				netWeight = netWeight * totalGoodsNum;
			}

			itemDTO.setGrossWeight(grossWeight);
			itemDTO.setNetWeight(netWeight);

			if (merchandise != null) {
				itemDTO.setLength(merchandise.getLength());// 长
				itemDTO.setWidth(merchandise.getWidth());// 宽
				itemDTO.setVolume(merchandise.getVolume());// 体积
				itemDTO.setHeight(merchandise.getHeight());// 高
				itemDTO.setBarcode(merchandise.getBarcode());// 条形码
			}
			if(inDepot!=null){
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepot.getType())){
					itemDTO.setTallyingUnit(purchaseOrder.getTallyingUnit());;// 海外仓理货单位
				}
			}

			itemDTO.setPurchaseNum(purchaseOrderItemModel.getNum());// 应收数量
			itemDTO.setTallyingNum(totalGoodsNum);// 实收数量
			itemDTO.setNormalNum(normalNum);
			itemDTO.setLackNum(wornNum);
			itemDTO.setExpireNum(expireNum);
			itemDTO.setBatchNo(batchNo);
			itemDTO.setOverdueDate(overdueDateStr);
			itemDTO.setProductionDate(productionDateStr);

			List<PurchaseWarehouseItemDTO> itemList = purchaseWarehouseItemMap.get(orderCode+"_"+declareOrderCode+"_"+ inDepot.getDepotId()+"_"+inboundDate);
			if(itemList == null) {
				itemList = new ArrayList<PurchaseWarehouseItemDTO>() ;
			}
			itemList.add(itemDTO) ;
			purchaseWarehouseItemMap.put(orderCode+"_"+declareOrderCode+"_"+ inDepot.getDepotId()+"_"+inboundDate, itemList) ;

		}
		if (failure == 0) {
			for (String key : purchaseWareHouseMap.keySet()) {
				PurchaseWarehouseModel purchaseWarehouse = purchaseWareHouseMap.get(key);
				String purchaseCode = key.split("_")[0];
				List<PurchaseWarehouseItemDTO> list = purchaseWarehouseItemMap.get(key);
				if (list == null || list.size() < 1) {
					continue;
				}
				if (purchaseWarehouse.getDeclareOrderId() == null) {
					continue;
				}
				DeclareOrderItemModel item = new DeclareOrderItemModel();
				item.setDeclareOrderId(purchaseWarehouse.getDeclareOrderId());
				item.setPurchase(purchaseCode);
				List<DeclareOrderItemModel> declareItemList = declareOrderItemDao.list(item);

				Map<Long, List<PurchaseWarehouseItemDTO>> itemMap = list.stream().collect(Collectors.groupingBy(PurchaseWarehouseItemDTO::getPurchaseItemId));
				for (DeclareOrderItemModel tempDecalreItem : declareItemList) {
					List<PurchaseWarehouseItemDTO> outItemList = itemMap.get(tempDecalreItem.getPurchaseItemId());
					if (outItemList == null) {
						setErrorMsg(null, "预申报单:" + purchaseWarehouse.getDeclareOrderCode() + "采购订单：" + tempDecalreItem.getPurchase() + "导入商品数量与预申报单不一致", resultList);
						failure += 1;
						continue;
					}
					Integer tallyingNum = outItemList.stream().filter(o -> o.getTallyingNum() != null).mapToInt(PurchaseWarehouseItemDTO::getTallyingNum).sum();

					Integer stayNum = tempDecalreItem.getNum() - tallyingNum;
					if (stayNum.intValue() != 0) {
						setErrorMsg(null, "预申报单:" + purchaseWarehouse.getDeclareOrderCode() + "采购订单：" + tempDecalreItem.getPurchase() + "商品货号：" + tempDecalreItem.getGoodsNo() + "导入数量与申报数量不一致", resultList);
						failure += 1;
						continue;
					}
				}
			}
		}

		// 若全部校验通过才进行导入
		if (failure == 0) {
			for (String key : purchaseWareHouseMap.keySet()){
				PurchaseWarehouseModel purchaseWarehouse = purchaseWareHouseMap.get(key);
				Long warehouseId = purchaseWarehouseDao.save(purchaseWarehouse);

				// 新增入库单与采购单关系
				String purchaseCode = key.split("_")[0];
				PurchaseOrderModel purchaseOrder = new PurchaseOrderModel();
				purchaseOrder.setCode(purchaseCode);
				purchaseOrder = purchaseOrderDao.searchByModel(purchaseOrder);
				WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
				relModel.setWarehouseId(warehouseId);
				relModel.setPurchaseOrderId(purchaseOrder.getId());
				warehouseOrderRelDao.save(relModel);

				if (purchaseOrder.getRate() == null) {
					commonBusinessService.saveRate(purchaseOrder,TimeUtils.parseFullTime(purchaseWarehouse.getInboundDate() + " 00:00:00"));
				}

				if(warehouseId != null) {
					List<PurchaseWarehouseItemDTO> list = purchaseWarehouseItemMap.get(key);
					Map<Long, List<PurchaseWarehouseItemDTO>> itemMap = list.stream().collect(Collectors.groupingBy(i->i.getPurchaseItemId()));
					for(Long itemKey: itemMap.keySet()){
						List<PurchaseWarehouseItemDTO> items = itemMap.get(itemKey);
						Integer normalNum = items.stream().filter(i->i.getNormalNum() != null).mapToInt(PurchaseWarehouseItemDTO::getNormalNum).sum();
						Integer tallyNum = items.stream().filter(i->i.getTallyingNum() != null).mapToInt(PurchaseWarehouseItemDTO::getTallyingNum).sum();

						PurchaseWarehouseItemDTO purchaseWarehouseItemDTO = items.get(0);
						PurchaseWarehouseItemModel purchaseWarehouseItemModel = new PurchaseWarehouseItemModel();
						BeanUtils.copyProperties(purchaseWarehouseItemDTO,purchaseWarehouseItemModel);
						purchaseWarehouseItemModel.setNormalNum(normalNum);
						purchaseWarehouseItemModel.setTallyingNum(tallyNum);
						purchaseWarehouseItemModel.setWarehouseId(warehouseId);
						Long itemId = purchaseWarehouseItemDao.save(purchaseWarehouseItemModel);

						Map<String, List<PurchaseWarehouseItemDTO>> itemBatchMap = items.stream().collect(Collectors.groupingBy(
								i->(StringUtils.isBlank(i.getBatchNo()) ? "":i.getBatchNo())+"_"
								+(StringUtils.isBlank(i.getProductionDate())? "":i.getProductionDate())+"_"+(StringUtils.isBlank(i.getOverdueDate()) ? "":i.getOverdueDate())));
						for(String batchKey : itemBatchMap.keySet()){
							List<PurchaseWarehouseItemDTO> batchItems = itemBatchMap.get(batchKey);
							Integer norNum = batchItems.stream().filter(i->i.getNormalNum() != null).mapToInt(PurchaseWarehouseItemDTO::getNormalNum).sum();
							Integer lackNum = batchItems.stream().filter(i->i.getLackNum() != null).mapToInt(PurchaseWarehouseItemDTO::getLackNum).sum();
							Integer expireNum = batchItems.stream().filter(i->i.getExpireNum() != null).mapToInt(PurchaseWarehouseItemDTO::getExpireNum).sum();

							purchaseWarehouseItemDTO = batchItems.get(0);
							PurchaseWarehouseBatchModel batchModel = new PurchaseWarehouseBatchModel();
							batchModel.setItemId(itemId);// 表体id
							batchModel.setGoodsId(purchaseWarehouseItemDTO.getGoodsId());// 商品id
							batchModel.setCreater(user.getId());// 创建人
							batchModel.setBatchNo(purchaseWarehouseItemDTO.getBatchNo());
							batchModel.setCreateDate(TimeUtils.getNow());
							if(StringUtils.isNotBlank(purchaseWarehouseItemDTO.getProductionDate())){
								Date productionDate = TimeUtils.parseDay(purchaseWarehouseItemDTO.getProductionDate());
								batchModel.setProductionDate(productionDate);
							}
							if(StringUtils.isNotBlank(purchaseWarehouseItemDTO.getOverdueDate())){
								Date overdueDate = TimeUtils.parseDay(purchaseWarehouseItemDTO.getOverdueDate());
								batchModel.setOverdueDate(overdueDate);
							}
							batchModel.setWornNum(lackNum);// 坏货数量
							batchModel.setExpireNum(expireNum);// 过期数量
							batchModel.setNormalNum(norNum);// 正常数量

							purchaseWarehouseBatchDao.save(batchModel);
							success++;
						}
					}

				}
				commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_21, purchaseWarehouse.getCode(), "导入", null, null);
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
	 * 入库明细导出
	 *
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<PurchaseWarehouseExportDTO> getExportDetails(List ids, PurchaseWarehouseDTO dto, User user) throws SQLException {

		List<PurchaseWarehouseExportDTO> list = null;

		if (ids != null && !ids.isEmpty()) {
			list = purchaseWarehouseDao.getExportDetails(ids);
		} else {

			if (dto.getBuId() == null) {
				List<Long> buIds = userBuRelMongoDao.getBuListByUser(user.getId());

				if (buIds.isEmpty()) {
					return new ArrayList<PurchaseWarehouseExportDTO>();
				}

				dto.setBuIds(buIds);
			}

			list = purchaseWarehouseDao.getExportDetailsByDTO(dto);
		}

		for (PurchaseWarehouseExportDTO exportDto : list) {
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", Long.valueOf(exportDto.getDepotId()));
			DepotInfoMongo depotInfo = depotInfoMongoDao.findOne(depotInfo_params);
			if (depotInfo != null) {
				exportDto.setDepotCode(depotInfo.getCode());
				if (depotInfo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_D)) {
					exportDto.setWarehouseType("THRK");
				} else {
					exportDto.setWarehouseType("CGRK");
				}
			}
			WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
			relModel.setWarehouseId(exportDto.getWarehouseId());
			List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(relModel);
			List<String> purchaseCode = new ArrayList<>();
			for(WarehouseOrderRelModel  rel :relList){
				PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(rel.getPurchaseOrderId());
				if(purchaseOrderModel != null){
					purchaseCode.add(purchaseOrderModel.getCode());
				}
			}
			if(purchaseCode.size() > 0){
				exportDto.setPurchaseCode(StringUtils.join(purchaseCode,","));
			}

			List<PurchaseWarehouseItemExportDTO> itemList = exportDto.getItemList();

			for (PurchaseWarehouseItemExportDTO item : itemList) {
				// 查询商品
				Map<String, Object> merchandiseMap = new HashMap<String, Object>();
				merchandiseMap.put("merchandiseId", item.getGoodsId());
				merchandiseMap.put("status", DERP_SYS.MERCHANDISEINFO_STATUS_1);

				MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(merchandiseMap);

				item.setCommbarcode(merchandise.getCommbarcode());
				if(purchaseCode.size() > 0){
					item.setPurchaseCode(StringUtils.join(purchaseCode,","));
				}
			}

			exportDto.setItemList(itemList);
		}
		return list;
	}

	/**
	 * 入库勾稽明细导出
	 *
	 * @param ids
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<PurchaseWarehouseExportBean> getPurchaseExportDetails(List ids, Long userId) throws SQLException {
		List<PurchaseWarehouseExportBean> result = new ArrayList<PurchaseWarehouseExportBean>();
		// 根据勾选的入库单查询商品信息
		List<PurchaseWarehouseExportBean> resultList = purchaseWarehouseDao.getItemDetails(ids);
		List<PurchaseCorrelationModel> list = purchaseCorrelationDao.getDetailsByIds(ids);
		if (list != null && list.size() > 0) {
			for (PurchaseWarehouseExportBean exportBean : resultList) {

				// 根据采购入库单编码和货号获取商品信息和批次信息
				Map<String, Object> queryMap = new HashMap<String, Object>();
				queryMap.put("warehouseCode", exportBean.getWarehouseCode());
				queryMap.put("goodsNo", exportBean.getGoodsNo());
				List<PurchaseWarehouseBatchModel> batchModelList = purchaseWarehouseBatchDao
						.getGoodBatchByWarehouseCodeAndGoodsNo(queryMap);

				Map<String, Integer> batchNumMap = new HashMap<String, Integer>();
				Map<String, PurchaseWarehouseBatchModel> batchMap = new HashMap<String, PurchaseWarehouseBatchModel>();
				for (PurchaseWarehouseBatchModel batchModel : batchModelList) {
					int num = batchModel.getWornNum() + batchModel.getExpireNum() + batchModel.getNormalNum();
					batchNumMap.put(batchModel.getBatchNo(), num);
					batchMap.put(batchModel.getBatchNo(), batchModel);
				}

				PurchaseCorrelationModel correlation = new PurchaseCorrelationModel();
				correlation.setWarehouseCode(exportBean.getWarehouseCode());// 入库单编码
				correlation.setGoodsNo(exportBean.getGoodsNo());// 商品货号
				List<PurchaseCorrelationModel> correlationList = purchaseCorrelationDao.list(correlation);
				int num = 0;
				for (PurchaseCorrelationModel correlationModel : correlationList) {

					// 已勾稽的信息
					PurchaseWarehouseExportBean bean = new PurchaseWarehouseExportBean();
					bean.setWarehouseCode(exportBean.getWarehouseCode());// 入库单编码
					bean.setGoodsNo(exportBean.getGoodsNo());// 商品货号
					bean.setIsArticulation("是");// 是否勾稽
					bean.setNum(correlationModel.getNum());// 勾稽数量
					bean.setNum2(0);// 未勾稽数量
					bean.setPurchaseCode(correlationModel.getPurchaseCode());// 采购订单

					for (String batchNo : batchMap.keySet()) {
						if (batchNumMap.get(batchNo) > 0 && batchNumMap.get(batchNo) >= correlationModel.getNum()) {
							bean.setBatchNo(batchNo); // 批次号
							PurchaseWarehouseBatchModel batchModel = batchMap.get(batchNo);

							if (batchModel.getProductionDate() != null) {
								bean.setProductionDate(new Timestamp(batchModel.getProductionDate().getTime())); // 生产日期
							}

							if (batchModel.getOverdueDate() != null) {
								bean.setOverdueDate(new Timestamp(batchModel.getOverdueDate().getTime())); // 失效日期
							}

							batchNumMap.put(batchNo, batchNumMap.get(batchNo) - correlationModel.getNum());
							break;
						}
					}

					result.add(bean);
					num += correlationModel.getNum();
				}

				if (exportBean.getNum2() - num > 0) {

					int unTallyNum = exportBean.getNum2() - num;

					while (unTallyNum > 0) {

						for (String batchNo : batchMap.keySet()) {
							// 未勾稽的信息
							PurchaseWarehouseExportBean bean = new PurchaseWarehouseExportBean();
							bean.setWarehouseCode(exportBean.getWarehouseCode());// 入库单编码
							bean.setGoodsNo(exportBean.getGoodsNo());// 商品货号
							bean.setIsArticulation("否");// 是否勾稽
							if (batchNumMap.get(batchNo) > 0) {
								bean.setBatchNo(batchNo); // 批次号
								PurchaseWarehouseBatchModel batchModel = batchMap.get(batchNo);

								if (batchModel.getProductionDate() != null) {
									bean.setProductionDate(new Timestamp(batchModel.getProductionDate().getTime())); // 生产日期
								}

								if (batchModel.getOverdueDate() != null) {
									bean.setOverdueDate(new Timestamp(batchModel.getOverdueDate().getTime())); // 失效日期
								}

								if (unTallyNum > batchNumMap.get(batchNo)) {
									bean.setNum2(batchNumMap.get(batchNo));
									unTallyNum -= batchNumMap.get(batchNo);
									batchNumMap.put(batchNo, 0);
								} else {
									bean.setNum2(unTallyNum);
									batchNumMap.put(batchNo, batchNumMap.get(batchNo) - unTallyNum);
									unTallyNum = 0;
								}
								result.add(bean);
							}
						}

					}
				}
			}
		}
		// 第一次导出
		else {
			for (PurchaseWarehouseExportBean exportBean : resultList) {

				// 根据采购入库单编码和货号获取商品信息和批次信息
				Map<String, Object> queryMap = new HashMap<String, Object>();
				queryMap.put("warehouseCode", exportBean.getWarehouseCode());
				queryMap.put("goodsNo", exportBean.getGoodsNo());
				List<PurchaseWarehouseBatchModel> batchModelList = purchaseWarehouseBatchDao
						.getGoodBatchByWarehouseCodeAndGoodsNo(queryMap);

				for (PurchaseWarehouseBatchModel batchModel : batchModelList) {
					// 未勾稽的信息
					PurchaseWarehouseExportBean bean = new PurchaseWarehouseExportBean();
					bean.setWarehouseCode(exportBean.getWarehouseCode());// 入库单编码
					bean.setGoodsNo(exportBean.getGoodsNo());// 商品货号
					bean.setIsArticulation("否");// 是否勾稽
					bean.setNum2(batchModel.getExpireNum() + batchModel.getWornNum() + batchModel.getNormalNum());// 勾稽数量
					bean.setBatchNo(batchModel.getBatchNo()); // 批次号

					if (batchModel.getProductionDate() != null) {
						bean.setProductionDate(new Timestamp(batchModel.getProductionDate().getTime())); // 生产日期
					}

					if (batchModel.getOverdueDate() != null) {
						bean.setOverdueDate(new Timestamp(batchModel.getOverdueDate().getTime())); // 失效日期
					}

					result.add(bean);
				}
			}
		}

		return result;
	}

	/**
	 * 入库勾稽关联导出校验仓库类型
	 *
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	@Override
	public String checkWarehouseDepotType(List<Long> ids) throws SQLException {
		String result = "";
		List<PurchaseWarehouseModel> list = purchaseWarehouseDao.getListByIds(ids);
		for (PurchaseWarehouseModel pModel : list) {
			// 获取仓库信息
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", pModel.getDepotId());// 仓库id
			DepotInfoMongo depot = depotInfoMongoDao.findOne(depotInfo_params);
			if (depot != null) {
				if (DERP_SYS.DEPOTINFO_TYPE_D.equals(depot.getType())) {
					result = "入库勾稽关联导出仓库类型不能包含中转仓";
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 根据id删除(支持批量)
	 *
	 * @param ids
	 * @return
	 */
	public void delBatchByIds(List<Long> ids) throws SQLException {
		// 检查入库单状态
		for (Long id : ids) {
			PurchaseWarehouseModel warehouseModel = purchaseWarehouseDao.searchById(id);
			if (DERP_ORDER.PURCHASEWAREHOUSE_STATE_012.equals(warehouseModel.getState())
					|| DERP_ORDER.PURCHASEWAREHOUSE_STATE_028.equals(warehouseModel.getState())) {
				throw new DerpException("采购入库单：" + warehouseModel.getCode() + "不为【待入库】，删除失败") ;
			}

			if(StringUtils.isNotBlank(warehouseModel.getExtraCode())){
				throw new DerpException("采购入库单：" + warehouseModel.getCode() + "非手动导入订单，删除失败") ;
			}
		}
		// 修改状态为已删除
		for (Long id : ids) {
			PurchaseWarehouseModel warehouseModel = purchaseWarehouseDao.searchById(id);
			// 判断状态是否为待入仓
			if (DERP_ORDER.PURCHASEWAREHOUSE_STATE_011.equals(warehouseModel.getState())) {

				WarehouseOrderRelModel queryRel = new WarehouseOrderRelModel() ;
				queryRel.setWarehouseId(warehouseModel.getId());

				List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(queryRel);

				List<Long> relIds = relList.stream().map(WarehouseOrderRelModel::getId).collect(Collectors.toList());

				if(!relIds.isEmpty()){
					warehouseOrderRelDao.delete(relIds) ;
				}

				warehouseModel.setState(DERP.DEL_CODE_006);// 已删除
				warehouseModel.setModifyDate(TimeUtils.getNow());
				purchaseWarehouseDao.modify(warehouseModel);
			}
		}
	}

	@Override
	public PurchaseWarehouseDTO searchDTODetail(Long id) throws SQLException {
		PurchaseWarehouseDTO dto = new PurchaseWarehouseDTO();
		dto.setId(id);
		dto = purchaseWarehouseDao.getDTODetails(dto);
		WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
		relModel.setWarehouseId(id);
		List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(relModel);
		List<String> purchaseCodes = new ArrayList<>();
		if(relList != null && relList.size() > 0){
			for(WarehouseOrderRelModel rel : relList){
				PurchaseOrderModel purchaseOrderModel = purchaseOrderDao.searchById(rel.getPurchaseOrderId());
				if(purchaseOrderModel != null){
					purchaseCodes.add(purchaseOrderModel.getCode());
				}
			}
		}
		dto.setPurchaseCode(StringUtils.join(purchaseCodes,","));
		return dto;
	}

	/**
	 * 判断输入字段是否为空
	 *
	 * @param index
	 * @param content
	 * @param msg
	 * @param resultList
	 * @return
	 */
	private boolean checkIsNullOrNot(int index, String content, String msg, List<ImportErrorMessage> resultList) {

		if (StringUtils.isBlank(content)) {
			ImportErrorMessage message = new ImportErrorMessage();
			message.setRows(index + 1);
			message.setMessage(msg);
			resultList.add(message);

			return true;

		} else {
			return false;
		}

	}

	/**
	 * 错误时，设置错误内容
	 *
	 * @param index
	 * @param msg
	 * @param resultList
	 */
	private void setErrorMsg(Integer index, String msg, List<ImportErrorMessage> resultList) {
		ImportErrorMessage message = new ImportErrorMessage();
		if(index != null){
			message.setRows(index + 1);
		}
		message.setMessage(msg);
		resultList.add(message);
	}

	/**
	 * 确认入库构建传输对象
	 *
	 * @throws Exception
	 */
	private InvetAddOrSubtractGoodsListJson generateTransportGoods(PurchaseWarehouseBatchModel bModel, int depot_flag,String topidealCode)
			throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
		listJson.setGoodsId(bModel.getGoodsId().toString());
		listJson.setGoodsNo(bModel.getGoodsNo());
		listJson.setGoodsName(bModel.getGoodsName());
		listJson.setBatchNo(bModel.getBatchNo());
		listJson.setBarcode(bModel.getBarcode());
		listJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增


		if (bModel.getProductionDate() != null) {
			listJson.setProductionDate(sdf.format(bModel.getProductionDate()));
		}

		if (bModel.getOverdueDate() != null) {
			listJson.setOverdueDate(sdf.format(bModel.getOverdueDate()));
		}

		// 海外仓必填
		if (depot_flag == 1) {
			if (bModel.getTallyingUnit() != null) {
				// 托盘
				if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
					listJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
				} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
					listJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
				} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
					listJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
				}
			} else {
				listJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
			}
		}

		return listJson;
	}

	/**
	 * 判断是否yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public boolean isDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
	}

	@Override
	public void pushInventory(List<InvetAddOrSubtractRootJson> jsonList, User user) throws SQLException {

		for (InvetAddOrSubtractRootJson invetAddOrSubtractRootJson : jsonList) {

			String warehouseCode = invetAddOrSubtractRootJson.getOrderNo();

			PurchaseWarehouseModel queryModel = new PurchaseWarehouseModel();
			queryModel.setCode(warehouseCode);
			queryModel = purchaseWarehouseDao.searchByModel(queryModel);

//			if (!DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_2.equals(queryModel.getCorrelationStatus())) {
//				continue;
//			}

			PurchaseWarehouseModel updateModel = new PurchaseWarehouseModel();
			updateModel.setId(queryModel.getId());
			updateModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);

			purchaseWarehouseDao.modify(updateModel);

			try {
				SendResult k = rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				System.out.println(k);
			} catch (Exception e) {
				LOGGER.error("----------------------" + warehouseCode + "采购入库确认增加库存失败----------------------");
			}
			commonBusinessService.saveLog(user, DERP_ORDER.OPERATE_LOG_MODULE_21, warehouseCode, "确认入仓", null, null);
		}

	}
	/**
	 * 判断是否yyyy-MM-dd
	 *
	 * @param date
	 * @return
	 */
	public boolean isYmdDate(String date) {
		Pattern p = Pattern.compile("\\d{4}\\-\\d{1,2}\\-\\d{1,2}");
		return p.matcher(date).matches();
	}
}
