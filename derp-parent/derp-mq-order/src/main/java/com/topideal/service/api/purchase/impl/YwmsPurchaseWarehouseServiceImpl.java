package com.topideal.service.api.purchase.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.enums.PushYwmsTypeEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.purchase.PurchaseOrderDao;
import com.topideal.dao.purchase.PurchaseOrderItemDao;
import com.topideal.dao.purchase.PurchaseWarehouseBatchDao;
import com.topideal.dao.purchase.PurchaseWarehouseDao;
import com.topideal.dao.purchase.PurchaseWarehouseItemDao;
import com.topideal.dao.purchase.WarehouseOrderRelDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.purchase.PurchaseOrderItemModel;
import com.topideal.entity.vo.purchase.PurchaseOrderModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseBatchModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseItemModel;
import com.topideal.entity.vo.purchase.PurchaseWarehouseModel;
import com.topideal.entity.vo.purchase.WarehouseOrderRelModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.pushapi.ywms.purchase.pushback.EntryOrder;
import com.topideal.json.pushapi.ywms.purchase.pushback.OrderLine;
import com.topideal.json.pushapi.ywms.purchase.pushback.OrderLines;
import com.topideal.json.pushapi.ywms.purchase.pushback.PurchasePushBackRoot;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.api.purchase.YwmsPurchaseWarehouseService;
import com.topideal.service.common.CommonBusinessService;

@Service
public class YwmsPurchaseWarehouseServiceImpl implements YwmsPurchaseWarehouseService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YwmsPurchaseWarehouseServiceImpl.class);
	
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;

	@Autowired
	private PurchaseOrderDao purchaseOrderDao ;
	
	@Autowired
	private WarehouseOrderRelDao warehouseOrderRelDao;
	
	@Autowired
	private PurchaseWarehouseDao purchaseWarehouseDao ;
	
	@Autowired
	private PurchaseWarehouseItemDao purchaseWarehouseItemDao ;
	
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
	
	@Autowired
	private PurchaseOrderItemDao purchaseOrderItemDao ;
	
	@Autowired
	private PurchaseWarehouseBatchDao purchaseWarehouseBatchDao ;
	
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao ;
	
	@Autowired
	private RMQProducer rocketMQProducer;// mq
	
	@Autowired
	private CommonBusinessService commonBusinessService ;
	
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203117800, model = DERP_LOG_POINT.POINT_12203117800_Label,keyword="orderCode")
	public InvetAddOrSubtractRootJson saveStatus(String json, String keys, String topics, String tags) throws Exception {
		
		/**
		 * 处理api传过来的json
		 */
		JSONObject apiJson = JSONObject.parseObject(json) ;
		String jsonStr = apiJson.getString("jsonStr") ;
		
		PurchasePushBackRoot root = JSONObject.parseObject(jsonStr, PurchasePushBackRoot.class) ;
		
		if(root != null) {
			EntryOrder entryOrder = root.getRequest().getEntryOrder();
			OrderLines orderLines = root.getRequest().getOrderLines();
			
			if(!ApolloUtil.ywmsOwnerCode.equals(entryOrder.getOwnerCode())) {
				throw new RuntimeException("货主编码与商家主体对应关系不一致") ;
			}
			
			String warehouseCode = entryOrder.getWarehouseCode();
			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			
			queryMap.put("code", warehouseCode) ;
			DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);
			if(depot == null) {
				throw new RuntimeException("仓库编码未查询到经分销系统已备案仓库") ;
			}
			
			String entryOrderCode = entryOrder.getEntryOrderCode();
			PurchaseOrderModel purchaseOrderModel = new PurchaseOrderModel() ;
			purchaseOrderModel.setCode(entryOrderCode); 
			purchaseOrderModel = purchaseOrderDao.searchByModel(purchaseOrderModel) ;
			if(purchaseOrderModel == null) {
				throw new RuntimeException("采购订单号：" + entryOrderCode + "， 不存在") ;
			}
			
			if(!DERP_ORDER.PURCHASEORDER_STATUS_003.equals(purchaseOrderModel.getStatus())) {
				throw new RuntimeException("采购订单号：" + entryOrderCode + "， 状态非已审核") ;
			}
			
			//保存外部订单号
			try {
				OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel() ;
				orderExternalCodeModel.setExternalCode(entryOrderCode);
				orderExternalCodeModel.setOrderSource(7);
				orderExternalCodeModel.setCreateDate(TimeUtils.getNow());
				
				orderExternalCodeDao.save(orderExternalCodeModel) ;
			} catch (Exception e) {
				LOGGER.error("众邦云仓采购入库-回推接口外部单号来源表已经存在 外部单号：" + entryOrderCode + "  保存失败");
				throw new RuntimeException("众邦云仓采购入库-回推接口外部单号来源表已经存在 外部单号：" + entryOrderCode + "  保存失败");
			}
			
			if(!PushYwmsTypeEnum.CGRK.getType().equals(entryOrder.getEntryOrderType())) {
				throw new RuntimeException("入库单类型非CGRK-采购入库") ;
			}
			
			if(!"FULFILLED".equals(entryOrder.getStatus())) {
				throw new RuntimeException("入库单状态非FULFILLED") ;
			}
			
			WarehouseOrderRelModel queryRelModel = new WarehouseOrderRelModel() ;
			queryRelModel.setPurchaseOrderId(purchaseOrderModel.getId());
			List<WarehouseOrderRelModel> relList = warehouseOrderRelDao.list(queryRelModel) ;
			
			if(!relList.isEmpty()) {
				throw new RuntimeException("采购订单：" + entryOrderCode + " 已存在采购入库单") ;
			}
			
			//生成采购入库单
			PurchaseWarehouseModel warehouseModel = new PurchaseWarehouseModel() ;
			warehouseModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_CGRK));
			warehouseModel.setInboundDate(TimeUtils.parse(entryOrder.getOperateTime(), "yyyy-MM-dd HH:mm:ss"));
			warehouseModel.setExtraCode(entryOrder.getEntryOrderId());
			warehouseModel.setState(DERP_ORDER.PURCHASEWAREHOUSE_STATE_028);
			warehouseModel.setCorrelationStatus(DERP_ORDER.PURCHASEWAREHOUSE_CORRELATION_STATUS_1);
			warehouseModel.setMerchantId(purchaseOrderModel.getMerchantId());
			warehouseModel.setMerchantName(purchaseOrderModel.getMerchantName());
			warehouseModel.setBuId(purchaseOrderModel.getBuId());
			warehouseModel.setBuName(purchaseOrderModel.getBuName());
			warehouseModel.setDepotId(depot.getDepotId());
			warehouseModel.setDepotName(depot.getName());
			warehouseModel.setContractNo(purchaseOrderModel.getContractNo());//合同号
			warehouseModel.setCreateDate(TimeUtils.getNow());
			String tallyingUnit="";
			if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())){
				tallyingUnit=purchaseOrderModel.getTallyingUnit();
				warehouseModel.setTallyingUnit(tallyingUnit);;// 海外仓理货单位
			}
			
			Long warehouseId = purchaseWarehouseDao.save(warehouseModel);
			
			//生成入库单关联关系
			WarehouseOrderRelModel relModel = new WarehouseOrderRelModel() ;
			relModel.setPurchaseOrderId(purchaseOrderModel.getId());
			relModel.setWarehouseId(warehouseId);
			relModel.setCreateDate(TimeUtils.getNow());
			
			warehouseOrderRelDao.save(relModel) ;
			
			//生成表体、批次信息
			List<OrderLine> orderLineList = orderLines.getOrderLine();
			//加减库存商品信息json
			List<InvetAddOrSubtractGoodsListJson> goodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			
			for (OrderLine orderLine : orderLineList) {
				
				Map<String, Object> goodQueryMap = new HashMap<>() ;
				goodQueryMap.put("goodsNo", orderLine.getItemCode()) ;
				goodQueryMap.put("merchantId", purchaseOrderModel.getMerchantId()) ;
				MerchandiseInfoMogo merchandisinfo = merchandiseInfoMogoDao.findOne(goodQueryMap);
				
				PurchaseWarehouseItemModel item = new PurchaseWarehouseItemModel() ;
				
				item.setWarehouseId(warehouseId);
				item.setWarehouseCode(warehouseModel.getCode());
				item.setGoodsId(merchandisinfo.getMerchandiseId());
				item.setGoodsName(merchandisinfo.getName());
				item.setGoodsNo(merchandisinfo.getGoodsNo());
				item.setBarcode(merchandisinfo.getBarcode());
				item.setLength(0.0);
				item.setWidth(0.0);
				item.setHeight(0.0);
				item.setVolume(0.0);
				item.setGrossWeight(0.0);
				item.setGrossWeight(0.0);
				item.setTallyingNum(Integer.valueOf(orderLine.getActualQty()));
				item.setCreateDate(TimeUtils.getNow());
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(depot.getType())) {
					item.setTallyingUnit(tallyingUnit);//海外仓理货单位
				}
				
				PurchaseOrderItemModel queryItemModel = new PurchaseOrderItemModel() ;
				queryItemModel.setPurchaseOrderId(purchaseOrderModel.getId());
				queryItemModel.setGoodsNo(orderLine.getItemCode());
				
				queryItemModel = purchaseOrderItemDao.searchByModel(queryItemModel) ;
				if(queryItemModel != null) {
					item.setPurchaseNum(queryItemModel.getNum());
				}
				
				//校验批次强校验
				if((DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())
						|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depot.getBatchValidation()))
						&& StringUtils.isBlank(orderLine.getBatchCode())) {
					throw new RuntimeException("入库仓为批次效期强校验，批次信息不能为空") ;
				}
				
				PurchaseWarehouseBatchModel batchModel = new PurchaseWarehouseBatchModel() ;
				
				batchModel.setBarcode(merchandisinfo.getBarcode());
				batchModel.setGoodsId(merchandisinfo.getMerchandiseId()) ;
				batchModel.setBatchNo(orderLine.getBatchCode());
				batchModel.setExpireNum(0);
					
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
					
				listJson.setBarcode(merchandisinfo.getBarcode());// 条形码
				listJson.setGoodsId(merchandisinfo.getMerchandiseId().toString());
				listJson.setGoodsName(merchandisinfo.getName());
				listJson.setGoodsNo(merchandisinfo.getGoodsNo());
				listJson.setBatchNo(orderLine.getBatchCode());
				listJson.setIsExpire(DERP.ISEXPIRE_1);
				listJson.setNum(Integer.valueOf(orderLine.getActualQty()));
				listJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);
				
				if("ZP".equals(orderLine.getInventoryType())) {
					batchModel.setNormalNum(Integer.valueOf(orderLine.getActualQty()));
					batchModel.setWornNum(0);
					
					listJson.setType(DERP.ISDAMAGE_0);
					
				}else {
					batchModel.setNormalNum(0);
					batchModel.setWornNum(Integer.valueOf(orderLine.getActualQty()));
					
					listJson.setType(DERP.ISDAMAGE_1);
				}
				
				if(orderLine.getProductDate() != null) {
					batchModel.setProductionDate(TimeUtils.parse(orderLine.getProductDate(), "yyyy-MM-dd"));
					listJson.setProductionDate(TimeUtils.format(batchModel.getProductionDate(), "yyyy-MM-dd")) ;
				}
				
				if(orderLine.getExpireDate() != null) {
					batchModel.setOverdueDate(TimeUtils.parse(orderLine.getExpireDate(), "yyyy-MM-dd"));
					listJson.setOverdueDate(TimeUtils.format(batchModel.getOverdueDate(), "yyyy-MM-dd"));
				}
				
				batchModel.setCreateDate(TimeUtils.getNow());
				
				goodsList.add(listJson);
				
				
				Long itemId = purchaseWarehouseItemDao.save(item);
				
				batchModel.setItemId(itemId);
				purchaseWarehouseBatchDao.save(batchModel) ;
			}
			
			//commonBusinessService.saveAutoPurchaseAnalysis(warehouseModel.getCode());
			
			//组装加减库存json
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String now = sdf.format(date);
			
			InvetAddOrSubtractRootJson addandSubInventoryRoot = new InvetAddOrSubtractRootJson();
			addandSubInventoryRoot.setBackTopic(MQPushBackOrderEnum.YWMS_PURCHASE_WAREHOUSE_PUSH_BACK.getTopic());
			addandSubInventoryRoot.setBackTags(MQPushBackOrderEnum.YWMS_PURCHASE_WAREHOUSE_PUSH_BACK.getTags());
			addandSubInventoryRoot.setCustomParam(new HashMap<String, Object>());
			addandSubInventoryRoot.setMerchantId(String.valueOf(purchaseOrderModel.getMerchantId()));
			addandSubInventoryRoot.setMerchantName(purchaseOrderModel.getMerchantName());
			addandSubInventoryRoot.setBusinessNo(purchaseOrderModel.getCode());
			addandSubInventoryRoot.setTopidealCode(ApolloUtil.ywmsTopidealCode);
			addandSubInventoryRoot.setDepotId(depot.getDepotId().toString());
			addandSubInventoryRoot.setDepotCode(depot.getCode());
			addandSubInventoryRoot.setDepotName(depot.getName());
			addandSubInventoryRoot.setDepotType(depot.getType());
			addandSubInventoryRoot.setIsTopBooks(depot.getIsTopBooks());
			addandSubInventoryRoot.setOrderNo(warehouseModel.getCode());
			addandSubInventoryRoot.setSource(SourceStatusEnum.CGRK.getKey());
			addandSubInventoryRoot.setSourceType(InventoryStatusEnum.CGRK.getKey());
			addandSubInventoryRoot.setSourceDate(now);
			addandSubInventoryRoot.setOwnMonth(entryOrder.getOperateTime().substring(0, 7));
			addandSubInventoryRoot.setDivergenceDate(entryOrder.getOperateTime());
			addandSubInventoryRoot.setBuId(purchaseOrderModel.getBuId().toString());
			addandSubInventoryRoot.setBuName(purchaseOrderModel.getBuName());
			addandSubInventoryRoot.setGoodsList(goodsList);
			
			return addandSubInventoryRoot ;
			
		}else {
			throw new RuntimeException("参数有误") ;
		}
	}

	@Override
	public void pushInventory(InvetAddOrSubtractRootJson invetAddOrSubtractRootJson) {
		try {
			rocketMQProducer.send(JSONObject.toJSONString(invetAddOrSubtractRootJson),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		} catch (Exception e) {
			LOGGER.error(
					"----------------------" + invetAddOrSubtractRootJson.getOrderNo() + "众邦云仓采购入库增加库存失败----------------------");
		}
	}

}
