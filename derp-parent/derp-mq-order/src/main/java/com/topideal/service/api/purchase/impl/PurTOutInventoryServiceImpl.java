package com.topideal.service.api.purchase.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.api.v5_8.ESOutInventoryGoodsListJson;
import com.topideal.json.api.v5_8.ESOutInventoryRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.purchase.PurTOutInventoryService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 出库明细回推实现类
 */
@Service
public class PurTOutInventoryServiceImpl implements PurTOutInventoryService {
	// 打印日志
	private static final Logger LOGGER = LoggerFactory.getLogger(PurTOutInventoryServiceImpl.class);
	// 采购退货订单
	@Autowired
	private PurchaseReturnOrderDao purchaseReturnOrderDao;
	// 采购退货出库单
	@Autowired
	private PurchaseReturnOdepotDao purchaseReturnOdepotDao;
	// 采购退货出库单表体
	@Autowired
	private PurchaseReturnOdepotItemDao purchaseReturnOdepotItemDao;
	// 仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private PurchaseReturnItemDao purchaseReturnItemDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203117200, model = DERP_LOG_POINT.POINT_12203117200_Label, keyword = "orderCode")
	public boolean savePurTOutInventoryInfo(String json, String keys, String topics, String tags) throws Exception {
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);

		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("goodsList", ESOutInventoryGoodsListJson.class);
		// JSON对象转实体
		ESOutInventoryRootJson rootJson = (ESOutInventoryRootJson) JSONObject.toBean(jsonData,
				ESOutInventoryRootJson.class, classMap);

		String orderCode = rootJson.getOrderCode();// 订单号或者LBX号

		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(6); // 订单来源 1:电商订单, 2:装载交运 3:销售出库 6: 采购退货
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("采购退货来源表已经存在 单号：" + orderCode + "  保存失败");
			throw new RuntimeException("采购退货来源表已经存在 单号：" + orderCode + "  保存失败");
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
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
			LOGGER.error("出库仓为海外仓不走该流程" + orderCode);
			throw new RuntimeException("出库仓为海外仓不走该流程" + orderCode);
		}
		// 批次效期强校验：1-是 0-否
		if ("1".equals(mongo.getBatchValidation())) {
			for (ESOutInventoryGoodsListJson goodsListJson : rootJson.getGoodsList()) {
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

		// 查询采购退货出库单
		PurchaseReturnOdepotModel purReturn = new PurchaseReturnOdepotModel();
		purReturn.setPurchaseReturnCode(orderCode);
		purReturn = purchaseReturnOdepotDao.searchByModel(purReturn);
		if (purReturn != null) {
			LOGGER.error("采购退货出库已经存在,订单号" + orderCode);
			throw new RuntimeException("采购退货出库已经存在,订单号" + orderCode);
		}
		PurchaseReturnOdepotModel purReturnOutModel = new PurchaseReturnOdepotModel();
		purReturnOutModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGTC));
		purReturnOutModel.setPurchaseReturnCode(purchaseReturn.getCode());
		purReturnOutModel.setPurchaseReturnId(purchaseReturn.getId());
		purReturnOutModel.setOutDepotId(purchaseReturn.getOutDepotId());
		purReturnOutModel.setOutDepotName(purchaseReturn.getOutDepotName());
		purReturnOutModel.setMerchantId(purchaseReturn.getMerchantId());
		purReturnOutModel.setMerchantName(purchaseReturn.getMerchantName());
		purReturnOutModel.setStatus(DERP_ORDER.PURCHASERETURNORDER_STATUS_015);
		purReturnOutModel.setSupplierId(purchaseReturn.getSupplierId());
		purReturnOutModel.setSupplierName(purchaseReturn.getSupplierName());
		purReturnOutModel.setCurrency(purchaseReturn.getCurrency());
		purReturnOutModel.setBuName(purchaseReturn.getBuName());
		purReturnOutModel.setBuId(purchaseReturn.getBuId());
		purReturnOutModel.setPoNo(purchaseReturn.getPoNo());
		purReturnOutModel.setExtraCode(orderCode);
		// 新增采购退货出库
		purchaseReturnOdepotDao.save(purReturnOutModel);

		//获取采购退商品信息
		PurchaseReturnItemModel queryModel = new PurchaseReturnItemModel();
		queryModel.setOrderId(purchaseReturn.getId());
		List<PurchaseReturnItemModel> returnItemList = purchaseReturnItemDao.list(queryModel);
		List<Long> returnItemGoodsIdMap = returnItemList.stream().map(PurchaseReturnItemModel::getGoodsId).collect(Collectors.toList());
		for (ESOutInventoryGoodsListJson goods : rootJson.getGoodsList()) {
			if(!returnItemGoodsIdMap.contains(goods.getGoodsId())){
				LOGGER.error("订单号" + orderCode+",商品货号：" + goods.getGoodsNo() +" 在采购退订单不存在");
				throw new RuntimeException("订单号" + orderCode+",商品货号：" + goods.getGoodsNo() +" 在采购退订单不存在");
			}
			// 新增销采购退货出库表体
			PurchaseReturnOdepotItemModel itemModel = new PurchaseReturnOdepotItemModel();
			itemModel.setOdepotOrderId(purReturnOutModel.getId());
			itemModel.setGoodsId(goods.getGoodsId());// 商品id
			itemModel.setGoodsName(goods.getGoodsName());// 商品名称
			itemModel.setGoodsNo(goods.getGoodsNo());// 商品货号
			itemModel.setBarcode(goods.getBarcode());// 货品条形码
			itemModel.setNum(goods.getNum());// 数量
			itemModel.setBatchNo(goods.getBatchNo());// 批次
			if ("1".equals(goods.getStockType())) {
				LOGGER.error("采购退货只能是好品商品id:" + goods.getGoodsId());
				throw new RuntimeException("采购退货只能是好品商品id:" + goods.getGoodsId());
			}

			String productionDate = goods.getProductionDate();// 生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				itemModel.setProductionDate(TimeUtils.StringToDate(productionDate));// 生产日期
			}
			String overdueDate = goods.getOverdueDate();// 生产日期
			if (StringUtils.isNotBlank(overdueDate)) {
				itemModel.setOverdueDate(TimeUtils.StringToDate(overdueDate));// 失效日期
			}
			itemModel.setRemark(goods.getRemark());
			// 新增采购退货出库表体
			purchaseReturnOdepotItemDao.save(itemModel);
		}

		return true;
	}
}
