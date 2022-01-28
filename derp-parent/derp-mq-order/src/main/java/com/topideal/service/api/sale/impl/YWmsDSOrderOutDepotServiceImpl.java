package com.topideal.service.api.sale.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topideal.common.constant.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.enums.PushYwmsTypeEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.ApolloUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.dao.sale.WayBillDao;
import com.topideal.dao.sale.WayBillItemDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.entity.vo.sale.WayBillItemModel;
import com.topideal.entity.vo.sale.WayBillModel;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.json.pushapi.ywms.sale.pushback.DeliveryOrder;
import com.topideal.json.pushapi.ywms.sale.pushback.Item;
import com.topideal.json.pushapi.ywms.sale.pushback.Items;
import com.topideal.json.pushapi.ywms.sale.pushback.OrderLines;
import com.topideal.json.pushapi.ywms.sale.pushback.OrderPushBackRoot;
import com.topideal.json.pushapi.ywms.sale.pushback.Package;
import com.topideal.json.pushapi.ywms.sale.pushback.Packages;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.YWmsDSOrderOutDepotService;

@Service
public class YWmsDSOrderOutDepotServiceImpl implements YWmsDSOrderOutDepotService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(YWmsDSOrderOutDepotServiceImpl.class);
	
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao ;
	// 电商订单
	@Autowired
	private OrderDao orderDao;
	// 电商订单商品
	@Autowired
	private OrderItemDao orderItemDao;
	// 运单表
	@Autowired
	private WayBillDao wayBillDao;
	// 运单表体
	@Autowired
	private WayBillItemDao wayBillItemDao;
	
	@Autowired
	private RMQProducer rocketMQProducer;// mq
	//商品信息MongoDB
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;
	
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_13203127900, model = DERP_LOG_POINT.POINT_13203127900_Label,keyword="orderCode")
	public void saveStatus(String json, String keys, String topics, String tags) throws Exception {
		/**
		 * 处理api传过来的json
		 */
		JSONObject apiJson = JSONObject.parseObject(json) ;
		String jsonStr = apiJson.getString("jsonStr") ;

		OrderPushBackRoot root = JSONObject.parseObject(jsonStr, OrderPushBackRoot.class) ;

		if(root != null) {
			Packages packages = root.getRequest().getPackages();
			OrderLines orderLines = root.getRequest().getOrderLines();
			DeliveryOrder deliveryOrder = root.getRequest().getDeliveryOrder();
			String deliveryOrderCode = deliveryOrder.getDeliveryOrderCode();
			String warehouseCode = deliveryOrder.getWarehouseCode();

			Map<String, Object> queryMap = new HashMap<String, Object>() ;
			
			queryMap.put("code", warehouseCode) ;
			DepotInfoMongo depot = depotInfoMongoDao.findOne(queryMap);
			if(depot == null) {
				LOGGER.error("仓库编码未查询到经分销系统已备案仓库");
				throw new RuntimeException("仓库编码未查询到经分销系统已备案仓库") ;
			}
			if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depot.getBatchValidation())) {//批次效期强校验
				LOGGER.error("该仓库为批次强校验仓库，订单编号"+deliveryOrderCode+"仓库编码："+warehouseCode);
				throw new RuntimeException("该仓库为批次强校验仓库，订单编号"+deliveryOrderCode+"仓库编码："+warehouseCode) ;
			}

			OrderModel orderModel = new OrderModel();
			orderModel.setCode(deliveryOrderCode);
			orderModel = orderDao.searchByModel(orderModel);
			if(orderModel==null){
				LOGGER.error("没查询到电商订单,订单编号" + deliveryOrderCode);
				throw new RuntimeException("没查询到电商订单,订单编号" + deliveryOrderCode);
			}
			if(!DERP_ORDER.ORDER_STATUS_3.equals(orderModel.getStatus())){
				LOGGER.error("电商订单状态不为待发货,订单编号" + deliveryOrderCode);
				throw new RuntimeException("电商订单状态不为待发货,订单编号" + deliveryOrderCode);
			}

			//保存外部订单号
			try {
				OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
				orderExternalCodeModel.setExternalCode(orderModel.getExternalCode());
				orderExternalCodeModel.setOrderSource(1);		// 订单来源  1:电商订单, 2:装载交运 3:销售出库
				orderExternalCodeDao.save(orderExternalCodeModel);
			} catch (Exception e) {
				LOGGER.error("电商出库-发货单回推接口外部单号来源表已经存在 外部单号：" + orderModel.getExternalCode() + "  保存失败");
				throw new RuntimeException("电商出库-发货单回推接口外部单号来源表已经存在 外部单号：" + orderModel.getExternalCode() + "  保存失败");
			}

			if(!PushYwmsTypeEnum.JYCK.getType().equals(deliveryOrder.getOrderType())) {
				LOGGER.error("出库单类型不为JYCK");
				throw new RuntimeException("出库单类型不为JYCK") ;
			}
			
			if(!"DELIVERED".equals(deliveryOrder.getStatus())) {
				LOGGER.error("出库单状态不为DELIVERED");
				throw new RuntimeException("出库单状态不为DELIVERED") ;
			}
			// 根据电商订单id查询运单信息表
			WayBillModel wayBillModel = new WayBillModel();
			wayBillModel.setOrderId(orderModel.getId());
			wayBillModel = wayBillDao.searchByModel(wayBillModel);
			if (null != wayBillModel) {
				LOGGER.error("运单信息已经存在,订单号" + deliveryOrderCode);
				throw new RuntimeException("运单信息已经存在,订单号" + deliveryOrderCode);
			}
			Map<String,Integer> checkGoodsNumMap = new HashMap<>();// 判断报文中某个商品数量与订单中是否一致
			// 判断 电商订单的商品数量和众邦云仓电商出库接口 商品数量是否不同  如果不同拦截
			List<Package> packageList = packages.getPackage();
			String expressCode="";//运单号
			String logisticsCode="";//物流公司编码
			Double weight=0.0;// 包裹重量
			for (Package packageModel : packageList) {
				expressCode=packageModel.getExpressCode();//运单号
				logisticsCode = packageModel.getLogisticsCode();
				String weightStr = packageModel.getWeight();
				if(StringUtils.isNotBlank(weightStr)){
					weight = weight+Double.valueOf(weightStr);
				}

				Items items = packageModel.getItems();
				List<Item> itemList = items.getItem();
				for (int i = 0; i < itemList.size(); i++) {
					Item item = itemList.get(i);
					String quantityStr = item.getQuantity();// 商品数量
					String itemCode = item.getItemCode();//商品编码对应我们系统商品货号

					if(StringUtils.isBlank(quantityStr)){
						LOGGER.error("订单号deliveryOrderCode:"+deliveryOrderCode+",商品编码:"+itemCode+"商品数量为空");
						throw new RuntimeException("订单号deliveryOrderCode:"+deliveryOrderCode+",商品编码:"+itemCode+"商品数量为空");
					}
					Integer quantity =Integer.valueOf(quantityStr);// 报文给的数量
					// 根据电商订单号和 商品货号查询电商订单表体
					OrderItemModel orderItemModel = new OrderItemModel();
					orderItemModel.setOrderId(orderModel.getId());// 电商订单id
					orderItemModel.setGoodsNo(itemCode);// 商品编码对应我们系统商品货号
					List<OrderItemModel> orderItemList = orderItemDao.list(orderItemModel);
					if (orderItemList==null||orderItemList.size()==0) {
						LOGGER.error("没有查询到对应的电商订单表体的商品,订单号deliveryOrderCode:"+deliveryOrderCode+",商品编码:"+itemCode);
						throw new RuntimeException("没有查询到对应的电商订单表体的商品,订单号deliveryOrderCode:"+deliveryOrderCode+",商品编码:"+itemCode);
					}
					if(checkGoodsNumMap.containsKey(itemCode)){	// 商品货号+报文给的数量
						Integer num = checkGoodsNumMap.get(itemCode);
						checkGoodsNumMap.put(itemCode,quantity+num);
					}else{
						checkGoodsNumMap.put(itemCode,quantity);
					}
				}
			}
			for (Package packageModel : packageList) {
				Items items = packageModel.getItems();
				List<Item> itemList = items.getItem();
				for (int i = 0; i < itemList.size(); i++) {
					Item item = itemList.get(i);
					OrderItemModel orderItemModel = new OrderItemModel();
					orderItemModel.setOrderId(orderModel.getId());// 电商订单id
					orderItemModel.setGoodsNo(item.getItemCode());// 商品编码对应我们系统商品货号
					List<OrderItemModel> orderItemList = orderItemDao.list(orderItemModel);
					int orderItemNum=0;
					for (OrderItemModel orderItemNumModel : orderItemList) {
						orderItemNum=orderItemNumModel.getNum()+orderItemNum;
					}
					Integer checkNum = checkGoodsNumMap.get(item.getItemCode());
					if (orderItemNum!=checkNum) {
						LOGGER.error("电商订单的商品数量和众邦云仓电商出库接口回推的商品数量不同,订单号deliveryOrderCode:"+deliveryOrderCode+",商品编码:"+ item.getItemCode());
						throw new RuntimeException("电商订单的商品数量和众邦云仓电商出库接口回推的商品数量不同,订单号deliveryOrderCode:"+deliveryOrderCode+",商品编码："+item.getItemCode());
					}
				}
			}

			// 修改电商订单
			OrderModel oModel = new OrderModel();
			oModel.setId(orderModel.getId());
			oModel.setWayBillNo(expressCode);// 运单号
			oModel.setStatus(DERP_ORDER.ORDER_STATUS_027);// 单据状态 027-出库中
			oModel.setDeliverDate(TimeUtils.parse(deliveryOrder.getOrderConfirmTime(), "yyyy-MM-dd HH:mm:ss"));// 发货时间
			oModel.setLogisticsName(logisticsCode);// 物流企业编码
			oModel.setWeight(weight);// 包裹重量
			oModel.setModifyDate(TimeUtils.getNow());
			orderDao.modify(oModel);
			// 新增运单
			WayBillModel wModel = new WayBillModel();
			wModel.setOrderId(orderModel.getId());// 电商订单ID
			wModel.setWayBillNo(expressCode);// 运单号
			wModel.setDeliverDate(TimeUtils.parse(deliveryOrder.getOrderConfirmTime(), "yyyy-MM-dd HH:mm:ss"));// 发货时间
			wModel.setLogisticsCode(logisticsCode);// 物流公司代码
			String logisticsNameLabel = DERP_ORDER.getLabelByKey(DERP.logisticsNameList, logisticsCode);
			wModel.setLogisticsName(logisticsNameLabel);// 物流公司名称
			wayBillDao.save(wModel);

			// 根据电商订单号和 商品货号查询电商订单表体
			OrderItemModel itemModel = new OrderItemModel();
			itemModel.setOrderId(orderModel.getId());// 电商订单id
			List<OrderItemModel> itemModelList = orderItemDao.list(itemModel);
			// 新增运单表体
			// 减库存
			List<WayBillItemModel> itemList = new ArrayList<WayBillItemModel>();
			for (OrderItemModel orderItemModel : itemModelList) {
				// 运单表体
				WayBillItemModel wayBillItemModel = new WayBillItemModel();
				wayBillItemModel.setBillId(wModel.getId());// 运单号id
				wayBillItemModel.setGoodsId(orderItemModel.getGoodsId());// 商品ID
				wayBillItemModel.setGoodsNo(orderItemModel.getGoodsNo());// 商品货号
				wayBillItemModel.setGoodsName(orderItemModel.getGoodsName());// 商品名称
				wayBillItemModel.setGoodsCode(orderItemModel.getGoodsCode());// 商品编号
				wayBillItemModel.setBarcode(orderItemModel.getBarcode());// 商品条形码
				wayBillItemModel.setNum(orderItemModel.getNum());// 数量
				wayBillItemModel.setPrice(orderItemModel.getPrice());// 单价
				wayBillItemModel.setBatchNo(orderItemModel.getBatchNo());// 批次号
				//事业部
				wayBillItemModel.setBuId(orderItemModel.getBuId());
				wayBillItemModel.setBuName(orderItemModel.getBuName());
				wayBillItemDao.save(wayBillItemModel);
				// 推库存商品
				itemList.add(wayBillItemModel);
			}
			//扣减出库库存量
			InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now1 = sdf.format(new Date());
			invetAddOrSubtractRootJson.setMerchantId(orderModel.getMerchantId().toString());
			invetAddOrSubtractRootJson.setMerchantName(orderModel.getMerchantName());
			invetAddOrSubtractRootJson.setTopidealCode(ApolloUtil.ywmsTopidealCode);
			Map<String, Object> depotInfo_params = new HashMap<String, Object>();
			depotInfo_params.put("depotId", orderModel.getDepotId());// 根据仓库id
			DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
			invetAddOrSubtractRootJson.setDepotId(orderModel.getDepotId().toString());
			invetAddOrSubtractRootJson.setDepotName(orderModel.getDepotName());
			invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
			invetAddOrSubtractRootJson.setDepotType(mongo.getType());
			invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
			invetAddOrSubtractRootJson.setOrderNo(orderModel.getCode());
			invetAddOrSubtractRootJson.setBusinessNo(orderModel.getExternalCode());// 外部单号
			invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
			invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);
			invetAddOrSubtractRootJson.setSourceDate(now1);
			invetAddOrSubtractRootJson.setStorePlatformName(orderModel.getStorePlatformName());	// 电商平台名称
			invetAddOrSubtractRootJson.setShopCode(orderModel.getShopCode());// 编码
			invetAddOrSubtractRootJson.setDivergenceDate(deliveryOrder.getOrderConfirmTime());
			// 获取当前年月
			invetAddOrSubtractRootJson.setOwnMonth(deliveryOrder.getOrderConfirmTime().substring(0, 7));
			// 加减库存数据
			List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			//解冻数据
			List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
			SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
			for (WayBillItemModel item : itemList) {

				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
				invetAddOrSubtractGoodsListJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//0 好品 默认好品
				invetAddOrSubtractGoodsListJson.setNum(item.getNum());
				invetAddOrSubtractGoodsListJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJson.setIsExpire("1");//是否过期（0是 1否）默认没过期
				if (StringUtils.isNotBlank(item.getBuName()) && item.getBuId() != null) {
					invetAddOrSubtractGoodsListJson.setBuId(String.valueOf(item.getBuId()));// 事业部
					invetAddOrSubtractGoodsListJson.setBuName(item.getBuName());
				}
				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
				
				// 封装解冻数据
				InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
				inventoryFreezeGoodsListJson.setGoodsId(item.getGoodsId().toString());
				inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
				inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
				inventoryFreezeGoodsListJson.setDivergenceDate(deliveryOrder.getOrderConfirmTime());
				inventoryFreezeGoodsListJson.setNum(item.getNum());
				inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
				
			}
			
			//加减库存
			invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);

			//库存回推数据
			Map<String, Object> customParam=new HashMap<String, Object>();
			invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.YWMS_DSORDER_OUTDEPOT_PUSH_BACK.getTags());//回调标签
			invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.YWMS_DSORDER_OUTDEPOT_PUSH_BACK.getTopic());//回调主题
			customParam.put("code", orderModel.getCode());// 电商订单内部单号
			invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
			
			// 封装解冻数据
			//释放并减少冻结量
			InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
			String now = sdf.format(new Date());
			inventoryFreezeRootJson.setMerchantId(orderModel.getMerchantId().toString());
			inventoryFreezeRootJson.setMerchantName(orderModel.getMerchantName());
			inventoryFreezeRootJson.setDepotId(orderModel.getDepotId().toString());
			inventoryFreezeRootJson.setDepotName(orderModel.getDepotName());
			inventoryFreezeRootJson.setOrderNo(orderModel.getCode());
			inventoryFreezeRootJson.setBusinessNo(orderModel.getExternalCode());// 外部订单号
			inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
			inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_008);
			inventoryFreezeRootJson.setSourceDate(now);
			inventoryFreezeRootJson.setOperateType("1");

			inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
			rocketMQProducer.send(JSONObject.toJSONString(inventoryFreezeRootJson), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			
			
			
			try {
				rocketMQProducer.send(JSONObject.toJSONString(invetAddOrSubtractRootJson),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
						MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			} catch (Exception e) {
				LOGGER.error(
						"----------------------" + orderModel.getCode() + "众邦云仓电商出库减少库存失败----------------------");
			}
		}else {
			throw new RuntimeException("参数有误") ;
		}
	}
}
