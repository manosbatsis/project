//package com.topideal.service.order.impl;
//
//import com.alibaba.rocketmq.client.producer.SendResult;
//import com.topideal.common.constant.DERP_INVENTORY;
//import com.topideal.common.enums.MQInventoryEnum;
//import com.topideal.common.enums.MQPushBackOrderEnum;
//import com.topideal.common.system.mq.RMQProducer;
//import com.topideal.common.tools.TimeUtils;
//import com.topideal.dao.sale.*;
//import com.topideal.entity.vo.sale.*;
//import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
//import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
//import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
//import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
//import com.topideal.mongo.dao.DepotInfoMongoDao;
//import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
//import com.topideal.mongo.dao.MerchantInfoMongoDao;
//import com.topideal.mongo.entity.DepotInfoMongo;
//import com.topideal.mongo.entity.MerchandiseInfoMogo;
//import com.topideal.mongo.entity.MerchantInfoMongo;
//import com.topideal.service.api.sale.impl.DSB2COrderServiceImpl;
//import com.topideal.service.order.GetBusinessDataPushInventoryService;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.*;
///**
// * 获取业务端数据推送库存 
// */
//@Service
//public class GetBusinessDataPushInventoryServiceImpl implements GetBusinessDataPushInventoryService{
//	/*打印日志*/
//	private static final Logger LOGGER = LoggerFactory.getLogger(DSB2COrderServiceImpl.class);
//	@Autowired
//	private OrderDao orderDao;//电商订单
//	@Autowired
//	private WayBillDao wayBillDao;// 运单表	
//	@Autowired
//	private WayBillItemDao wayBillItemDao;// 运单表体
//	@Autowired
//	private DepotInfoMongoDao depotInfoMongoDao;//仓库
//	@Autowired
//	private MerchantInfoMongoDao merchantInfoMongoDao;// 商家信息	
//	@Autowired
//	private RMQProducer rocketMQProducer;//MQ
//	//销售出库清单
//	@Autowired
//	SaleOutDepotDao saleOutDepotDao;
//	// 销售出库表体
//	@Autowired
//	private SaleOutDepotItemDao saleOutDepotItemDao;
//
//	@Autowired
//	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
//	
//	@Override
//	public boolean getInfo(String json,String keys,String topics,String tags) throws Exception {
//		// 获取json对象
//		JSONObject jsonData = JSONObject.fromObject(json);
//		String tag = (String) jsonData.get("tag");
//		String externalCode = (String) jsonData.get("externalCode");
//		if ("000".equals(tag)) {
//			getOrderInfoPushInventory(json);
//		}else if("001".equals(tag)){
//			pushInventoryForSaleOutOrder(json);
//		}
//		
//		return true;
//	}
//
//	
//	private void pushInventoryForSaleOutOrder(String json) throws Exception {
//		JSONObject jsonData = JSONObject.fromObject(json);
//		String tag = (String) jsonData.get("tag");
//		String externalCode = (String) jsonData.get("externalCode");
//		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
//		if(StringUtils.isNotBlank(externalCode)){
//			saleOutDepotModel.setCode(externalCode);
//		}
//		saleOutDepotModel.setStatus("027");
//		saleOutDepotModel.setSaleType("2");
//		List<SaleOutDepotModel> list = saleOutDepotDao.list(saleOutDepotModel);
//		for(SaleOutDepotModel saleOutDepot:list){
//			SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
//			saleOutDepotItemModel.setSaleOutDepotId(saleOutDepot.getId());
//			List<SaleOutDepotItemModel> saleOutDepotItemList = saleOutDepotItemDao.list(saleOutDepotItemModel);
//			/*
//			 * 释放并减少冻结量
//			 */
//			//叠加商品出库数量（用于扣减库存）
//			Map<Long,SaleOutDepotItemModel> inventoryItem = new HashMap<Long,SaleOutDepotItemModel>();
//			for(SaleOutDepotItemModel saleOutDepotItem : saleOutDepotItemList){
//				if(inventoryItem.containsKey(saleOutDepotItem.getGoodsId())){
//					SaleOutDepotItemModel siModel = inventoryItem.get(saleOutDepotItem.getGoodsId());
//					saleOutDepotItem.setTransferNum(saleOutDepotItem.getTransferNum()+siModel.getTransferNum());
//				}
//				inventoryItem.put(saleOutDepotItem.getGoodsId(), saleOutDepotItem);
//			}
//			InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			String now = sdf.format(new Date());
//			inventoryFreezeRootJson.setMerchantId(saleOutDepot.getMerchantId().toString());
//			inventoryFreezeRootJson.setMerchantName(saleOutDepot.getMerchantName());
//			inventoryFreezeRootJson.setDepotId(saleOutDepot.getOutDepotId().toString());
//			inventoryFreezeRootJson.setDepotName(saleOutDepot.getOutDepotName());
//			inventoryFreezeRootJson.setOrderNo(saleOutDepot.getCode());
//			inventoryFreezeRootJson.setBusinessNo(saleOutDepot.getSaleOrderCode());
//			//inventoryFreezeRootJson.setSource(SourceStatusEnum.XSCK.getKey());
//			inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_003);
//			//inventoryFreezeRootJson.setSourceType(InventoryStatusEnum.XSCK.getKey());
//			inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
//			inventoryFreezeRootJson.setSourceDate(now);
//			inventoryFreezeRootJson.setOperateType("1");
//			List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
//			for(Map.Entry<Long,SaleOutDepotItemModel>entry1:inventoryItem.entrySet()){
//				SaleOutDepotItemModel saleOutDepotItem = entry1.getValue();
//				InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
//				if(StringUtils.isNotBlank(saleOutDepotItem.getOriginalGoodsNo())){
//					inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(saleOutDepotItem.getOriginalGoodsId()));//原货号
//					inventoryFreezeGoodsListJson.setGoodsNo(saleOutDepotItem.getOriginalGoodsNo());
//					Map<String, Object> merchandiseParam = new HashMap<>();
//					merchandiseParam.put("merchandiseId", saleOutDepotItem.getOriginalGoodsId());
//					MerchandiseInfoMogo mogoDaoOne = merchandiseInfoMogoDao.findOne(merchandiseParam);
//
//					inventoryFreezeGoodsListJson.setGoodsName(mogoDaoOne.getName());
//				}else{
//					inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(saleOutDepotItem.getGoodsId()));
//					inventoryFreezeGoodsListJson.setGoodsName(saleOutDepotItem.getGoodsName());
//					inventoryFreezeGoodsListJson.setGoodsNo(saleOutDepotItem.getOriginalGoodsNo());
//				}
//
//				Timestamp deliverDate = saleOutDepot.getDeliverDate();
//				inventoryFreezeGoodsListJson.setDivergenceDate(sdf.format(deliverDate));
//				inventoryFreezeGoodsListJson.setNum(saleOutDepotItem.getTransferNum());
//				inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
//			}
//			inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
//			LOGGER.info("inventoryFreezeRootJson:{}",JSONObject.fromObject(inventoryFreezeRootJson).toString());
//			SendResult sendResult = rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
//			LOGGER.info("库存解冻推送成功，msgId："+sendResult.getMsgId());
//			//避免数据过多会导致锁库
//			Thread.sleep(100);
//			
//			/*
//			 * 扣减销售出库库存量
//			 */
//			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
//			String now1 = sdf.format(new Date());
//			invetAddOrSubtractRootJson.setMerchantId(saleOutDepot.getMerchantId().toString());
//			invetAddOrSubtractRootJson.setMerchantName(saleOutDepot.getMerchantName());
//			Map<String, Object> merchantMap = new HashMap<>();
//			merchantMap.put("merchantId", saleOutDepot.getMerchantId());// 卓志编码
//			MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantMap);//商家信息							
//			if (merchantInfoMongo==null) {	
//				LOGGER.error("没有查到对应的商家信息" + saleOutDepot.getCode());
//				throw new RuntimeException("商家不存在,订单编号" + saleOutDepot.getCode());
//			}
//			String topidealCode = merchantInfoMongo.getTopidealCode();
//			invetAddOrSubtractRootJson.setTopidealCode(topidealCode);
//			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
//			depotInfo_params.put("depotId", saleOutDepot.getOutDepotId());// 根据仓库id
//			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
//			invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
//			invetAddOrSubtractRootJson.setDepotName(mongo.getName());
//			invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
//			invetAddOrSubtractRootJson.setDepotType(mongo.getType());
//			invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
//			invetAddOrSubtractRootJson.setOrderNo(saleOutDepot.getCode());
//			invetAddOrSubtractRootJson.setBusinessNo(saleOutDepot.getSaleOrderCode());
//		//	invetAddOrSubtractRootJson.setSource(SourceStatusEnum.XSCK.getKey());
//			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_003);
//			//invetAddOrSubtractRootJson.setSourceType(InventoryStatusEnum.XSCK.getKey());
//			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
//			invetAddOrSubtractRootJson.setSourceDate(now1);
//			Timestamp deliverDate = saleOutDepot.getDeliverDate();
//			invetAddOrSubtractRootJson.setDivergenceDate(sdf.format(deliverDate));
//			// 获取当前年月
//			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM");
//			invetAddOrSubtractRootJson.setOwnMonth(sdf3.format(deliverDate));
//			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
//			for(Map.Entry<Long,SaleOutDepotItemModel>entry1:inventoryItem.entrySet()){
//				SaleOutDepotItemModel saleOutDepotItem = entry1.getValue();
//				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
//				invetAddOrSubtractGoodsListJson.setGoodsId(saleOutDepotItem.getGoodsId().toString());
//				invetAddOrSubtractGoodsListJson.setGoodsName(saleOutDepotItem.getGoodsName());
//				invetAddOrSubtractGoodsListJson.setGoodsNo(saleOutDepotItem.getGoodsNo());
//				invetAddOrSubtractGoodsListJson.setBarcode(saleOutDepotItem.getBarcode());
//				invetAddOrSubtractGoodsListJson.setType("0");
//				invetAddOrSubtractGoodsListJson.setNum(saleOutDepotItem.getTransferNum());
//				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
//				invetAddOrSubtractGoodsListJson.setBatchNo(saleOutDepotItem.getTransferBatchNo());
//				invetAddOrSubtractGoodsListJson.setProductionDate(TimeUtils.formatFullTime(saleOutDepotItem.getProductionDate()));
//				invetAddOrSubtractGoodsListJson.setOverdueDate(TimeUtils.formatFullTime(saleOutDepotItem.getOverdueDate()));
//				if(saleOutDepotItem.getOverdueDate() != null){
//					Timestamp currentDate = new Timestamp(new Date().getTime());
//					if(currentDate.before(saleOutDepotItem.getOverdueDate())){
//						//currentDate 时间比 item.getOverdueDate() 时间早(未过期)
//						invetAddOrSubtractGoodsListJson.setIsExpire("1");
//					}else{
//						invetAddOrSubtractGoodsListJson.setIsExpire("0");
//					}
//				}
//				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
//			}
//			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
//			//库存回推数据
//			Map<String, Object> customParam=new HashMap<String, Object>();
//			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.CRAWLER_STOCK_OUT_PUSH_BACK.getTags());//回调标签
//			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.CRAWLER_STOCK_OUT_PUSH_BACK.getTopic());//回调主题		
//			customParam.put("code", saleOutDepot.getCode());// 销售订单内部单号
//			invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
//			LOGGER.info("invetAddOrSubtractRootJson:{}",JSONObject.fromObject(invetAddOrSubtractRootJson).toString());
//			SendResult sendResult1 = rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
//			LOGGER.info("库存扣减推送成功，msgId："+sendResult1.getMsgId());
//			//避免数据过多会导致锁库
//			Thread.sleep(100);
//		}
//	}
//
//
//	/**
//	 * 获取电商订单出库中数据推送库存
//	 * @throws SQLException 
//	 */
//	public void getOrderInfoPushInventory (String json) throws Exception{
//		JSONObject jsonData = JSONObject.fromObject(json);
//		String tag = (String) jsonData.get("tag");
//		String externalCode = (String) jsonData.get("externalCode");
//		// 获取所有出库中的订单
//		OrderModel orderModel =new OrderModel();
//		if (StringUtils.isNotBlank(externalCode)) {
//			orderModel.setExternalCode(externalCode);
//		}
//		orderModel.setStatus("027");
//		List<OrderModel> orderList = orderDao.list(orderModel);		
//		
//		for (OrderModel order : orderList) {
//			// 获取所有出库中的运单
//			WayBillModel wayBillModel = new WayBillModel();
//			wayBillModel.setOrderId(order.getId());			
//			wayBillModel = wayBillDao.searchByModel(wayBillModel);
//			
//			//根据运单信息获取运单表体
//			WayBillItemModel wayBillItemModel = new WayBillItemModel();
//			wayBillItemModel.setBillId(wayBillModel.getId());			 
//			List<WayBillItemModel> wayBillItemList = wayBillItemDao.list(wayBillItemModel);
//			
//			
//			// 根据仓库id查询仓库信息
//			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
//			depotInfo_params.put("depotId", order.getDepotId());// 根据仓库id
//			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
//			if (mongo==null) {	
//				LOGGER.error("没有查到对应的仓库信息" + order.getCode());
//				throw new RuntimeException("没有查到对应的仓库信息" + order.getCode());
//			}
//			
//			Map<String, Object> merchantMap = new HashMap<>();
//			merchantMap.put("merchantId", order.getMerchantId());// 卓志编码
//			MerchantInfoMongo merchantInfoMongo = merchantInfoMongoDao.findOne(merchantMap);// 调入仓库信息								
//			if (merchantInfoMongo==null) {	
//				LOGGER.error("没有查到对应的商家信息" + order.getCode());
//				throw new RuntimeException("商家不存在,订单编号" + order.getCode());
//			}
//			
//			
//			//扣减销售出库库存量
//			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String now1 = sdf.format(new Date());
//			invetAddOrSubtractRootJson.setMerchantId(order.getMerchantId().toString());
//			invetAddOrSubtractRootJson.setMerchantName(order.getMerchantName());
//			invetAddOrSubtractRootJson.setTopidealCode(merchantInfoMongo.getTopidealCode());			
//			invetAddOrSubtractRootJson.setDepotId(order.getDepotId().toString());
//			invetAddOrSubtractRootJson.setDepotName(order.getDepotName());
//			invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
//			invetAddOrSubtractRootJson.setDepotType(mongo.getType());
//			invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
//			invetAddOrSubtractRootJson.setOrderNo(order.getCode());
//			invetAddOrSubtractRootJson.setBusinessNo(order.getExternalCode());// 外部单号
//			//invetAddOrSubtractRootJson.setSource(SourceStatusEnum.DSDD.getKey());
//			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
//			//invetAddOrSubtractRootJson.setSourceType(InventoryStatusEnum.DSDDCK.getKey());
//			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);
//			invetAddOrSubtractRootJson.setSourceDate(now1);			
//			Timestamp deliverDate = order.getDeliverDate();
//			String deliverDateStr = sdf.format(deliverDate);			
//			invetAddOrSubtractRootJson.setDivergenceDate(deliverDateStr);
//			// 获取当前年月		
//			invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));
//			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
//			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
//			for (WayBillItemModel item : wayBillItemList) {
//				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
//				invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
//				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
//				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
//				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
//				invetAddOrSubtractGoodsListJson.setType("0");// 好品
//				invetAddOrSubtractGoodsListJson.setNum(item.getNum());
//				invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
//				invetAddOrSubtractGoodsListJson.setIsExpire("1");// 写死未过期				
//				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
//			}		
//			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
//			//库存回推数据
//			Map<String, Object> customParam=new HashMap<String, Object>();
//			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTags());//回调标签
//			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.LOADINF_DELIVRIES_PUSH_BACK_2_1.getTopic());//回调主题		
//			customParam.put("code", order.getCode());// 电商订单内部单号
//			invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
//			rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
//
//									
//		}
//
//	}
//	
//	
//	
//}
