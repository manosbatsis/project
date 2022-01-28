package com.topideal.service.api.sale.impl;

import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.dao.sale.OrderDao;
import com.topideal.dao.sale.OrderItemDao;
import com.topideal.entity.vo.sale.OrderItemModel;
import com.topideal.entity.vo.sale.OrderModel;
import com.topideal.json.api.v3_4.ESaleOrderCancelStatusJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.MerchantShopRelMongoDao;
import com.topideal.mongo.dao.MerchantShopShipperMongoDao;
import com.topideal.mongo.entity.MerchantShopRelMongo;
import com.topideal.mongo.entity.MerchantShopShipperMongo;
import com.topideal.service.api.sale.DSOrderCancelStatusService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单取消状态回推接口
 */
@Service
public class DSOrderCancelStatusServiceImpl implements DSOrderCancelStatusService {
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DSOrderCancelStatusServiceImpl.class);
	@Autowired
	private OrderDao orderDao;// 电商订单
	@Autowired
	private OrderItemDao orderItemDao;// 电商订单商品
	@Autowired
	private RMQProducer rocketMQProducer;//
	// 商家店铺Mongo
	@Autowired
	private MerchantShopRelMongoDao merchantShopRelMongoDao ;
	@Autowired
	private MerchantShopShipperMongoDao merchantShopShipperMongoDao;
	/**
	 * 保存订单取消信息
	 */
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203120600,model=DERP_LOG_POINT.POINT_12203120600_Label,keyword="orderCode")
	public boolean saveOrderCancelInfo(String json,String keys,String topics,String tags) throws Exception {
		JSONObject jsonData = JSONObject.fromObject(json);
		
		// JSON对象转实体
		ESaleOrderCancelStatusJson rootJson = (ESaleOrderCancelStatusJson) JSONObject.toBean(jsonData, ESaleOrderCancelStatusJson.class);
		
		String orderCode=rootJson.getOrderCode();// 订单号
		String status =rootJson.getStatus();//90 取消
		if (!"90".equals(status)) {
			LOGGER.error("订单取消状态不是90,订单号orderCode"+orderCode);
			throw new RuntimeException("订单取消状态不是90,订单号:orderCode"+orderCode);			
		}
		// 电商订单
		OrderModel orderModel = new OrderModel();
		orderModel.setMerchantId(rootJson.getMerchantId());
		orderModel.setExternalCode(orderCode);// 外部单号
		orderModel = orderDao.searchByModel(orderModel);
		if (orderModel==null) {
			LOGGER.error(DERP.MQ_FAILTYPE_01 + "电商订单不存在,订单编码orderCode"+orderCode);
			throw new RuntimeException(DERP.MQ_FAILTYPE_01 + "电商订单不存在,订单编码orderCode"+orderCode);			
		}
		//电商订单已发货
		if ("4".equals(orderModel.getStatus())) {
			LOGGER.error("电商订单已发货,订单编码orderCode"+orderCode);
			throw new RuntimeException("电商订单已发货,订单编码 orderCode"+orderCode);			
		}
		//电商订单已取消
		if ("5".equals(orderModel.getStatus())) {
			LOGGER.error("电商订单已取消,订单编码orderCode"+orderCode);
			throw new RuntimeException("电商订单已取消,订单编码orderCode"+orderCode);			
		}	
		if (DERP_ORDER.ORDER_STATUS_027.equals(orderModel.getStatus())) {
			LOGGER.error("电商订单处理中不能取消,订单编码orderCode"+orderCode);
			throw new RuntimeException("电商订单处理中不能取消,订单编码orderCode"+orderCode);			
		}
		// 获取事业部
		/*查询所有启用的店铺*/
		Map<String, Object> merchantShopRelMap = new HashMap<>();
		merchantShopRelMap.put("shopCode",orderModel.getShopCode());
		merchantShopRelMap.put("status", DERP_SYS.MERCHANTSHOPREL_STATUS_1);//'状态(1使用中,0已禁用)'
		merchantShopRelMap.put("dataSourceCode", DERP.DATASOURCE_1);	// 数据来源1-跨境宝
		List<MerchantShopRelMongo> merchantShopRelList = merchantShopRelMongoDao.findAll(merchantShopRelMap);
		if (merchantShopRelList.size()==0) {
			throw new RuntimeException("店铺编码在商家店铺关联表没有查询到该店铺编码:" + orderModel.getShopCode());
		}
		MerchantShopRelMongo merchantShopRelMongo = merchantShopRelList.get(0);
		// 跨境宝：需以“商家（货主公司）+店铺id”查询店铺信息表维护的货主事业部，若找不到时，报错预警；
		Map<String, Object> shopShipperParams = new HashMap<String, Object>();
		shopShipperParams.put("merchantId", orderModel.getMerchantId()) ;
		shopShipperParams.put("shopId", merchantShopRelMongo.getShopId()) ;	// 店铺ID
		List<MerchantShopShipperMongo> merchantShopShipperList = merchantShopShipperMongoDao.findAll(shopShipperParams);
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode())) {// 外部店货主信息不能为空
			if(merchantShopShipperList==null ||merchantShopShipperList.size()==0){
				throw new RuntimeException("货主id:"+orderModel.getMerchantId()+"店铺id:"+merchantShopRelMongo.getShopId()+"在店铺货主表没找到对应信息");
			}
		}
		Long buId=null;
		String buName=null;
		// 如果是单主店取商家店铺关系表 事业部
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_DZD.equals(merchantShopRelMongo.getStoreTypeCode())){
			buId = merchantShopRelMongo.getBuId();//拿开店事业部
			buName = merchantShopRelMongo.getBuName();
		}
		
		
		// 外部店就直接查货主事业部（外部店无开店事业部）或者 为单主店但是开店事业部无库存
		if(DERP_SYS.MERCHANTSHOPREL_STORETYPE_WBD.equals(merchantShopRelMongo.getStoreTypeCode())){
			MerchantShopShipperMongo merchantShopShipperMongo = merchantShopShipperList.get(0);
			buId = merchantShopShipperMongo.getBuId();//拿开店事业部
			buName = merchantShopShipperMongo.getBuName();
		}
		
		
		// 根据电商订单id查询电商订单商品
		OrderItemModel orderItemModel = new OrderItemModel();
		orderItemModel.setOrderId(orderModel.getId());
		List<OrderItemModel> itemList = orderItemDao.list(orderItemModel);
		// 存放释放冻结商品
		InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now = sdf.format(new Date());
		List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		
		List<OrderItemModel>updateBuItemList=new ArrayList<>();
		for (OrderItemModel item : itemList) {
			// 修改事业部
			OrderItemModel updateBuItem=new OrderItemModel ();
			updateBuItem.setBuId(buId);
			updateBuItem.setBuName(buName);
			updateBuItem.setId(item.getId());
			updateBuItemList.add(updateBuItem);
			
			InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
			inventoryFreezeGoodsListJson.setGoodsId(String.valueOf(item.getGoodsId()));
			inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
			inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
			inventoryFreezeGoodsListJson.setDivergenceDate(now);
			inventoryFreezeGoodsListJson.setNum(item.getNum());
			inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
		}
		
		//释放并减少冻结量	
		inventoryFreezeRootJson.setMerchantId(orderModel.getMerchantId().toString());
		inventoryFreezeRootJson.setMerchantName(orderModel.getMerchantName());
		inventoryFreezeRootJson.setDepotId(orderModel.getDepotId().toString());
		inventoryFreezeRootJson.setDepotName(orderModel.getDepotName());
		inventoryFreezeRootJson.setOrderNo(orderModel.getCode());
		inventoryFreezeRootJson.setBusinessNo(orderModel.getExternalCode());
		inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_007);
		inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_007);
		Timestamp createDate = orderModel.getCreateDate();
		String createDateStr = sdf.format(createDate);
		inventoryFreezeRootJson.setSourceDate(createDateStr);// 单据生成日期
		inventoryFreezeRootJson.setOperateType("1");	// 解冻	
		inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
		String jsonMQ = JSONObject.fromObject(inventoryFreezeRootJson).toString();
		rocketMQProducer.send(jsonMQ, MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());		
		// 把电商订单改为以取消5	
		OrderModel oMdoel =new OrderModel();
		oMdoel.setId(orderModel.getId());
		oMdoel.setStatus("5");//单据状态：1:待申报 2.待放行,3:待发货,4:已发货,5:已取消',
		orderDao.modify(oMdoel);	
		
		//
		for (OrderItemModel updateBuItem : updateBuItemList) {
			orderItemDao.modify(updateBuItem);
		}
		
		
		return true;
	}

}
