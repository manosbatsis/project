package com.topideal.service.api.purchase.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.*;
import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.*;
import com.topideal.entity.vo.purchase.*;
import com.topideal.json.api.v1_3.PurchaseIntoWarehouseStatusJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.purchase.PurchaseIntoWarehouseStatusService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进仓单状态回推
 *
 * @author zhanghx 2018/7/16
 */
@Service
public class PurchaseIntoWarehouseStatusServiceImpl implements PurchaseIntoWarehouseStatusService {

	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(PurchaseIntoWarehouseStatusServiceImpl.class);

	@Autowired
	private PurchaseOrderDao purchaseOrderDao;// 采购单
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;// 入库单与采购单关系表
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao;// 采购入库单
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao;// 采购入库单批次
	@Autowired
	private DeclareOrderDao declareOrderDao;// 预申报单
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库
	@Autowired
	private RMQProducer rocketMQProducer;// mq

	// 进仓状态回推
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203110200, model = DERP_LOG_POINT.POINT_12203110200_Label, keyword = "entInboundId")
	public boolean saveIntoWarehouseStatusInfo(String json, String keys, String topics, String tags) throws Exception {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		PurchaseIntoWarehouseStatusJson rootJson = (PurchaseIntoWarehouseStatusJson) JSONObject.toBean(jsonData, PurchaseIntoWarehouseStatusJson.class);
		String inboundDate = rootJson.getInboundDate();// 进仓单生效日期,格式:yyyy-MM-dd HH:mm:ss
		String entInboundId = rootJson.getEntInboundId();// 企业入仓编号
		Long merchantId = Long.valueOf(rootJson.getMerchantId());// 商家id

		// 进仓状态1：成功；2：失败。若失败不处理
		if (!"1".equals(rootJson.getStatus())) {
			return true;
		}

		/**1.查询预申报单是否存在、判断预申报状态*/
		DeclareOrderModel declareModel = new DeclareOrderModel();
		declareModel.setCode(entInboundId);// 企业入仓编号
		declareModel.setMerchantId(merchantId);
		declareModel = declareOrderDao.searchByModel(declareModel);
		if(declareModel==null){
			logger.info("采购预申报单不存在");
			throw new RuntimeException("采购预申报单不存在");
		}
		if(!DERP_ORDER.DECLAREORDER_STATUS_004.equals(declareModel.getStatus())){
			logger.info("采购预申报单非待入仓状态");
			throw new RuntimeException("采购预申报单非待入仓状态");
		}

		//3.查询预申报单关联的所有出库单
		PurchaseWarehouseModel warehouseModel = new PurchaseWarehouseModel();
		warehouseModel.setDeclareOrderCode(declareModel.getCode());
		List<PurchaseWarehouseModel> warehouseList = purchaseWarehouseDao.list(warehouseModel);
		if (warehouseList==null || warehouseList.size()<=0) {
			logger.info("未找到采购入库单" + declareModel.getCode());
			throw new RuntimeException("未找到采购入库单" + declareModel.getCode());
		}

		//校验批次效期强校验
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", declareModel.getDepotId());// 根据仓库id
		DepotInfoMongo depotMongo = depotInfoMongoDao.findOne(depotInfo_params);
		if(DERP_SYS.BATCHVALIDATION_1.equals(depotMongo.getBatchValidation())
				|| DERP_SYS.BATCHVALIDATION_2.equals(depotMongo.getBatchValidation())) {
			for(PurchaseWarehouseModel wModel : warehouseList){
				List<PurchaseWarehouseBatchModel> batchModelList = purchaseWarehouseBatchDao.getGoodsAndBatch(wModel.getId());
				for (PurchaseWarehouseBatchModel bModel : batchModelList) {
					if(StringUtils.isBlank(bModel.getBatchNo()) || bModel.getProductionDate() == null || bModel.getOverdueDate() == null) {
						logger.info(depotMongo.getName() + ",设置了入库批次效期强校验" + "批次和效期不能为空,订单号:" + entInboundId);
						throw new RuntimeException(depotMongo.getName() + ",设置了入库批次效期强校验" + "批次和效期不能为空,订单号:" + entInboundId);
					}
				}
			}
		}

		/**4.循环出库单拼装入库单报文*/
		List<InvetAddOrSubtractRootJson> rootJsonList = new ArrayList<>();
		for(PurchaseWarehouseModel wModel : warehouseList){
			//判断入库单状态
			if(!DERP_ORDER.PURCHASEWAREHOUSE_STATE_011.equals(wModel.getState())) {
				logger.info("采购入库单非待入仓状态,订单编号" + entInboundId);
				throw new RuntimeException("采购入库单非待入仓状态,订单编号" + entInboundId);
			}
			//修改采购入库单为入库中
			PurchaseWarehouseModel purchaseWarehouseModel = new PurchaseWarehouseModel();
			purchaseWarehouseModel.setId(wModel.getId());
			purchaseWarehouseModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);// 入库中
			purchaseWarehouseModel.setInboundDate(TimeUtils.parseFullTime(inboundDate));// 进仓单生效日期
			purchaseWarehouseModel.setModifyDate(TimeUtils.getNow());
			purchaseWarehouseDao.modify(purchaseWarehouseModel);

			//拼装报文
			InvetAddOrSubtractRootJson subtractRootJson = createJson(declareModel,wModel,depotMongo,inboundDate);
			rootJsonList.add(subtractRootJson);
		}

		//推送库存
		if(rootJsonList!=null&&rootJsonList.size()>0){
			for(InvetAddOrSubtractRootJson subtractRootJson : rootJsonList){
				try {
					SendResult sendResult = rocketMQProducer.send(JSONObject.fromObject(subtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
					logger.info("subtractJson = " + JSONObject.fromObject(subtractRootJson).toString());
					logger.info(sendResult.toString());
				} catch (Exception e) {
					logger.error("----------------------" + subtractRootJson.getOrderNo() + "采购订单入库增加库存失败----------------------");
				}
			}
		}
		return true;
	}

	private InvetAddOrSubtractRootJson createJson(DeclareOrderModel declareModel,PurchaseWarehouseModel wModel,DepotInfoMongo depotMongo,String inboundDate) throws Exception {

		InvetAddOrSubtractRootJson rootJson = new InvetAddOrSubtractRootJson();
		rootJson.setBackTopic(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTopic());
		rootJson.setBackTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK.getTags());
		rootJson.setCustomParam(new HashMap<String, Object>());
		rootJson.setMerchantId(String.valueOf(wModel.getMerchantId()));
		rootJson.setMerchantName(wModel.getMerchantName());
		rootJson.setBusinessNo(wModel.getDeclareOrderCode());
		rootJson.setTopidealCode(declareModel.getTopidealCode());// 都是取的订单中的卓志编码
		rootJson.setDepotId(depotMongo.getDepotId().toString());
		rootJson.setDepotCode(depotMongo.getCode());
		rootJson.setDepotName(depotMongo.getName());
		rootJson.setDepotType(depotMongo.getType());
		rootJson.setIsTopBooks(depotMongo.getIsTopBooks());
		rootJson.setOrderNo(wModel.getCode());
		rootJson.setSource(SourceStatusEnum.CGRK.getKey());
		rootJson.setSourceType(InventoryStatusEnum.CGRK.getKey());
		rootJson.setSourceDate(TimeUtils.formatFullTime());
		rootJson.setOwnMonth(inboundDate.substring(0, 7));
		rootJson.setDivergenceDate(inboundDate);
		rootJson.setBuId(wModel.getBuId().toString());
		rootJson.setBuName(wModel.getBuName());

		//查询采购订单
		WarehouseOrderRelModel relModel = new WarehouseOrderRelModel();
		relModel.setWarehouseId(wModel.getId());
		relModel = warehouseOrderRelDao.searchByModel(relModel);
		PurchaseOrderModel orderModel = purchaseOrderDao.searchById(relModel.getPurchaseOrderId());
		//查询入库单商品、批次
		List<PurchaseWarehouseBatchModel> batchModelList = purchaseWarehouseBatchDao.getGoodsAndBatch(wModel.getId());
		List<InvetAddOrSubtractGoodsListJson> goodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for (PurchaseWarehouseBatchModel bModel : batchModelList) {
			// 坏货数量
			if(bModel.getWornNum() != null && bModel.getWornNum() > 0) {
				InvetAddOrSubtractGoodsListJson goodsJson = new InvetAddOrSubtractGoodsListJson();
				goodsJson.setGoodsId(String.valueOf(bModel.getGoodsId()));
				goodsJson.setGoodsNo(bModel.getGoodsNo());
				goodsJson.setBarcode(bModel.getBarcode());
				goodsJson.setGoodsName(bModel.getGoodsName());
				goodsJson.setStockLocationTypeId(String.valueOf(orderModel.getStockLocationTypeId()));//库位类型
				goodsJson.setStockLocationTypeName(orderModel.getStockLocationTypeName());
				if(bModel.getProductionDate() != null) {
					goodsJson.setProductionDate(TimeUtils.formatDay(bModel.getProductionDate()));
				}
				if (bModel.getOverdueDate() != null) {
					goodsJson.setOverdueDate(TimeUtils.formatDay(bModel.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(bModel.getOverdueDate());// 判断是否过期是否过期（0是 1否）
					goodsJson.setIsExpire(isExpire);// 是否过期（0是 1否）
				} else {
					goodsJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
				}
				goodsJson.setBatchNo(bModel.getBatchNo());
				goodsJson.setType(DERP.ISDAMAGE_1);// 商品类型（0 好品 1坏品）
				goodsJson.setNum(bModel.getWornNum());
				// 海外仓必填
				if(depotMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
					if (bModel.getTallyingUnit() != null) {
						if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
						} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
						} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
						}
					} else {
						goodsJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
					}
				}
				goodsJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增
				goodsList.add(goodsJson);
			}
			// 过期数量
			if(bModel.getExpireNum() != null && bModel.getExpireNum() > 0) {
				InvetAddOrSubtractGoodsListJson goodsJson = new InvetAddOrSubtractGoodsListJson();
				goodsJson.setGoodsId(String.valueOf(bModel.getGoodsId()));
				goodsJson.setGoodsNo(bModel.getGoodsNo());
				goodsJson.setBarcode(bModel.getBarcode());
				goodsJson.setGoodsName(bModel.getGoodsName());
				goodsJson.setStockLocationTypeId(String.valueOf(orderModel.getStockLocationTypeId()));//库位类型
				goodsJson.setStockLocationTypeName(orderModel.getStockLocationTypeName());
				if(bModel.getProductionDate() != null) {
					goodsJson.setProductionDate(TimeUtils.formatDay(bModel.getProductionDate()));
				}
				if(bModel.getOverdueDate() != null) {
					goodsJson.setOverdueDate(TimeUtils.formatDay(bModel.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(bModel.getOverdueDate());// 判断是否过期是否过期（0是 1否）
					goodsJson.setIsExpire(isExpire);// 是否过期（0是 1否）
				} else {
					goodsJson.setIsExpire(DERP.ISEXPIRE_0);// 是否过期（0是 1否）
				}
				goodsJson.setBatchNo(bModel.getBatchNo());
				goodsJson.setType(DERP.ISDAMAGE_1);// 商品类型（0 好品 1坏品）
				goodsJson.setNum(bModel.getExpireNum());
				// 海外仓必填
				if(depotMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
					if (bModel.getTallyingUnit() != null) {
						if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
						} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
						} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
						}
					} else {
						goodsJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
					}
				}
				goodsJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增
				goodsList.add(goodsJson);
			}
			// 正常数量
			if (bModel.getNormalNum() != null && bModel.getNormalNum() > 0) {
				InvetAddOrSubtractGoodsListJson goodsJson = new InvetAddOrSubtractGoodsListJson();
				goodsJson.setGoodsId(String.valueOf(bModel.getGoodsId()));
				goodsJson.setGoodsNo(bModel.getGoodsNo());
				goodsJson.setBarcode(bModel.getBarcode());
				goodsJson.setGoodsName(bModel.getGoodsName());
				goodsJson.setStockLocationTypeId(String.valueOf(orderModel.getStockLocationTypeId()));//库位类型
				goodsJson.setStockLocationTypeName(orderModel.getStockLocationTypeName());
				if(bModel.getProductionDate() != null) {
					goodsJson.setProductionDate(TimeUtils.formatDay(bModel.getProductionDate()));
				}
				if (bModel.getOverdueDate() != null) {
					goodsJson.setOverdueDate(TimeUtils.formatDay(bModel.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(bModel.getOverdueDate());// 判断是否过期是否过期（0是 1否）
					goodsJson.setIsExpire(isExpire);// 是否过期（0是 1否）
				} else {
					goodsJson.setIsExpire(DERP.ISEXPIRE_1);// 是否过期（0是 1否）
				}
				goodsJson.setBatchNo(bModel.getBatchNo());
				goodsJson.setType(DERP.ISDAMAGE_0);// 商品类型（0 好品 1坏品）
				goodsJson.setNum(bModel.getNormalNum());
				// 海外仓必填
				if(depotMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)) {
					if (bModel.getTallyingUnit() != null) {
						if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_0);// 0 托盘 1箱 2 件
						} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_1);// 0 托盘 1箱 2 件
						} else if (bModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
							goodsJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
						}
					} else {
						goodsJson.setUnit(DERP.INVENTORY_UNIT_2);// 0 托盘 1箱 2 件
					}
				}
				goodsJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增
				goodsList.add(goodsJson);
			}
		}

		rootJson.setGoodsList(goodsList);
		return rootJson;
	}

}
