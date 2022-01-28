package com.topideal.service.api.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.api.v1_4.OrderStatusUpdateJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.XSOutInventoryUpdateService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * 出库明细回推实现类 更新出库时间，扣减库存
 */
@Service
public class XSOutInventoryUpdateServiceImpl implements XSOutInventoryUpdateService {
	/* 打印日志 */
	private static final Logger logger = LoggerFactory.getLogger(XSOutInventoryUpdateServiceImpl.class);
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售出库单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库订单商品
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	@Autowired
	private SaleDeclareOrderDao saleDeclareOrderDao;
	@Autowired
	private SaleDeclareOrderItemDao saleDeclareOrderItemDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203120001, model = DERP_LOG_POINT.POINT_12203120001_Label,keyword="orderId")
	public boolean saveOutInventoryInfo(String json,String keys,String topics,String tags) throws Exception {
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		// JSON对象转实体
		OrderStatusUpdateJson rootJson = (OrderStatusUpdateJson) JSONObject.toBean(jsonData, OrderStatusUpdateJson.class, classMap);
		String orderCode = rootJson.getOrderId();// 预申报单号
		String deliver = rootJson.getUpdateTime();//必填
		Long merchantId = rootJson.getMerchantId();// 商家ID

		//1.查询预申报单 是否存在
		SaleDeclareOrderModel saleDeclare = new SaleDeclareOrderModel();
		saleDeclare.setCode(orderCode);
		saleDeclare = saleDeclareOrderDao.searchByModel(saleDeclare);
		if(saleDeclare==null){
			logger.error("销售预申报单不存在,单号：" + orderCode );
			throw new RuntimeException("销售预申报单不存在,单号：" + orderCode);
		}

		//获取预申报单对应的所有出库单
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setSaleDeclareOrderCode(orderCode);
		List<SaleOutDepotModel> outDepotList = saleOutDepotDao.list(saleOutDepotModel);
		Set<String> outCodeSet = new HashSet<>();//出库单号
		for(SaleOutDepotModel outDepot : outDepotList){
			if(!DERP_ORDER.SALEOUTDEPOT_STATUS_017.equals(outDepot.getStatus())){
				logger.error("销售预申报单关联的销售出库单：" + outDepot.getCode()+"不为待出库状态" );
				throw new RuntimeException("销售预申报单关联的销售出库单：" + outDepot.getCode()+"不为待出库状态");
			}
			outCodeSet.add(outDepot.getCode());
		}
		if(outCodeSet.size() < 1) {
			logger.error("销售预申报单关联的销售出库单不存在,单号：" + orderCode );
			throw new RuntimeException("销售预申报单关联的销售出库单不存在,单号：" + orderCode);
		}
		//循环逐个销售单出库
		List<InventoryFreezeRootJson> freezeJsonList = new ArrayList<>();
		List<InvetAddOrSubtractRootJson> subtractJsonList = new ArrayList<>();
		for(String outCode : outCodeSet){
			Map<String,Object> retMap = saveSalareOut(outCode,merchantId,deliver);
			freezeJsonList.add((InventoryFreezeRootJson) retMap.get("freezeRootJson"));
			subtractJsonList.add((InvetAddOrSubtractRootJson) retMap.get("subtractRootJson"));
		}
		if(freezeJsonList.size()>0){
			for(InventoryFreezeRootJson freezeJson : freezeJsonList){
				// 推送库存
				SendResult sendResult = rocketMQProducer.send(JSONObject.fromObject(freezeJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
				logger.info("freezeJson = " + JSONObject.fromObject(freezeJson).toString());
				logger.info(sendResult.toString());
			}
		}
		if(subtractJsonList.size()>0){
			for(InvetAddOrSubtractRootJson subtractJson : subtractJsonList){
				// 推送加减库存
				SendResult sendResult = rocketMQProducer.send(JSONObject.fromObject(subtractJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				logger.info("subtractJson = " + JSONObject.fromObject(subtractJson).toString());
				logger.info(sendResult.toString());
			}
		}
		//修改预申报单状态为已出库
		saleDeclare.setStatus(DERP_ORDER.SALEDECLARE_STATUS_008);//出库中
		saleDeclareOrderDao.modify(saleDeclare);
		return true;
	}

	public Map<String,Object> saveSalareOut(String outCode,Long merchantId,String deliver) throws Exception {

		//销售单编码查询销售出库单
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		saleOutDepotModel.setCode(outCode);// 销售出库清单编码
		saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		if (saleOutDepotModel == null) {
			logger.error("销售出库清单不存在,出库单号" + outCode);
			throw new RuntimeException("销售出库清单不存在,出库单号" + outCode);
		}
		// 销售
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		saleOrderModel.setMerchantId(merchantId);
		saleOrderModel.setCode(saleOutDepotModel.getSaleOrderCode());
		saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		if (saleOrderModel == null) {
			logger.error("订单不存在,出库单号" + outCode);
			throw new RuntimeException("订单不存在,出库单号" + outCode);
		}
		if (DERP_ORDER.SALEORDER_STATE_001.equals(saleOrderModel.getState()) || DERP_ORDER.SALEORDER_STATE_018.equals(saleOrderModel.getState())) {
			logger.error("订单状态为“待审核/已出库”,出库单号" + outCode);
			throw new RuntimeException("订单状态为待审核/已出库,出库单号" + outCode);
		}
		if(null == saleOrderModel.getBuId()){
			logger.error("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
		}


		// 根据仓库id查询 仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleOutDepotModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		if (mongo==null ) {
			logger.error("根据销售订单的仓库id没有查询到仓库,订单号:" + outCode);
			throw new RuntimeException("根据销售订单的仓库id没有查询到仓库,订单号:" + outCode);
		}
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
			logger.error("出库仓为海外仓不走该流程" + outCode);
			throw new RuntimeException("出库仓为海外仓不走该流程" + outCode);
		}

		Timestamp deliverDate = TimeUtils.parse(deliver, "yyyy-MM-dd HH:mm:ss");

		//更新出库时间和状态
		saleOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);
		saleOutDepotModel.setDeliverDate(deliverDate);// 发货时间
		saleOutDepotModel.setModifyDate(TimeUtils.getNow());
		saleOutDepotDao.modify(saleOutDepotModel);

		SaleOutDepotItemModel saleOutDepotItemModel = new SaleOutDepotItemModel();
		saleOutDepotItemModel.setSaleOutDepotId(saleOutDepotModel.getId());
		List<SaleOutDepotItemModel> saleOutDepotItemList = saleOutDepotItemDao.list(saleOutDepotItemModel);

		Map<Long, Integer> outGoodsNumMap = new HashMap<>();
		//封装扣减库存商品
		List<InvetAddOrSubtractGoodsListJson> subtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		if(saleOutDepotItemList != null && saleOutDepotItemList.size()>0) {
			for(SaleOutDepotItemModel itemModel : saleOutDepotItemList) {
				Integer currentOutNum = 0;
				//拼装扣减库存报文
				if (itemModel.getTransferNum()!=null&&itemModel.getTransferNum()>0) {
					InvetAddOrSubtractGoodsListJson subtractGoodsJson = new InvetAddOrSubtractGoodsListJson();
					subtractGoodsJson.setGoodsId(String.valueOf(itemModel.getGoodsId()));
					subtractGoodsJson.setGoodsNo(itemModel.getGoodsNo());
					subtractGoodsJson.setCommbarcode(itemModel.getCommbarcode());
					subtractGoodsJson.setBarcode(itemModel.getBarcode());
					subtractGoodsJson.setGoodsName(itemModel.getGoodsName());
					subtractGoodsJson.setOperateType("0");// 字符串 0 调减 1调增
					subtractGoodsJson.setBatchNo(itemModel.getTransferBatchNo());
					subtractGoodsJson.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
					subtractGoodsJson.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
					subtractGoodsJson.setStockLocationTypeId(String.valueOf(saleOrderModel.getStockLocationTypeId()));
					subtractGoodsJson.setStockLocationTypeName(saleOrderModel.getStockLocationTypeName());
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(itemModel.getOverdueDate()!=null){
						isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					subtractGoodsJson.setIsExpire(isExpire);//是否过期（0是 1否）
					subtractGoodsJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
					subtractGoodsJson.setNum(itemModel.getTransferNum());
					subtractGoodsListJsonList.add(subtractGoodsJson);

					currentOutNum = currentOutNum + itemModel.getTransferNum();
				}
				if(itemModel.getWornNum()!=null&&itemModel.getWornNum()>0) {
					InvetAddOrSubtractGoodsListJson subtractGoodsJson = new InvetAddOrSubtractGoodsListJson();
					subtractGoodsJson.setGoodsId(String.valueOf(itemModel.getGoodsId()));
					subtractGoodsJson.setGoodsNo(itemModel.getGoodsNo());
					subtractGoodsJson.setCommbarcode(itemModel.getCommbarcode());
					subtractGoodsJson.setBarcode(itemModel.getBarcode());
					subtractGoodsJson.setGoodsName(itemModel.getGoodsName());
					subtractGoodsJson.setOperateType("0");// 字符串 0 调减 1调增
					subtractGoodsJson.setBatchNo(itemModel.getTransferBatchNo());
					subtractGoodsJson.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
					subtractGoodsJson.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
					subtractGoodsJson.setStockLocationTypeId(String.valueOf(saleOrderModel.getStockLocationTypeId()));
					subtractGoodsJson.setStockLocationTypeName(saleOrderModel.getStockLocationTypeName());
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(itemModel.getOverdueDate()!=null){
						isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					subtractGoodsJson.setIsExpire(isExpire);//是否过期（0是 1否）
					subtractGoodsJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
					subtractGoodsJson.setNum(itemModel.getWornNum());
					subtractGoodsListJsonList.add(subtractGoodsJson);

					currentOutNum = currentOutNum + itemModel.getWornNum();

				}
				Integer outNum = outGoodsNumMap.get(itemModel.getGoodsId());
				if(outNum == null){
					outNum = currentOutNum;
				}else{
					outNum = outNum + currentOutNum;
				}
				outGoodsNumMap.put(itemModel.getGoodsId(),outNum);
			}
		}
		// 库存发货时间
		String deliverDateMq = TimeUtils.format(deliverDate, "yyyy-MM-dd HH:mm:ss");

		//释放并减少冻结量
		InventoryFreezeRootJson freezeRootJson = new InventoryFreezeRootJson();
		freezeRootJson.setMerchantId(saleOrderModel.getMerchantId().toString());
		freezeRootJson.setMerchantName(saleOrderModel.getMerchantName());
		freezeRootJson.setDepotId(saleOrderModel.getOutDepotId().toString());
		freezeRootJson.setDepotName(saleOrderModel.getOutDepotName());
		freezeRootJson.setOrderNo(saleOutDepotModel.getCode());
		freezeRootJson.setBusinessNo(saleOrderModel.getCode());
		freezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		freezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		freezeRootJson.setSourceDate(TimeUtils.formatFullTime());
		freezeRootJson.setOperateType("1");

		SaleOrderItemModel tempsaleOrderItemModel = new SaleOrderItemModel();
		tempsaleOrderItemModel.setOrderId(saleOrderModel.getId());
		List<SaleOrderItemModel> saleOrderItemList = saleOrderItemDao.list(tempsaleOrderItemModel);
		List<InventoryFreezeGoodsListJson> freezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for (SaleOrderItemModel item : saleOrderItemList) {
			Integer outNum  = outGoodsNumMap.get(item.getGoodsId());
			if(outNum == null || outNum.intValue() <= 0){
				continue;
			}
			InventoryFreezeGoodsListJson freezeGoodsJson = new InventoryFreezeGoodsListJson();
			freezeGoodsJson.setGoodsId(String.valueOf(item.getGoodsId()));
			freezeGoodsJson.setGoodsNo(item.getGoodsNo());
			freezeGoodsJson.setGoodsName(item.getGoodsName());
			freezeGoodsJson.setDivergenceDate(deliverDateMq);
			freezeGoodsJson.setNum(outNum);
			freezeGoodsListJsonList.add(freezeGoodsJson);
		}
		freezeRootJson.setGoodsList(freezeGoodsListJsonList);

		//扣减销售出库库存量
		InvetAddOrSubtractRootJson subtractRootJson = new InvetAddOrSubtractRootJson();
		subtractRootJson.setMerchantId(saleOrderModel.getMerchantId().toString());
		subtractRootJson.setMerchantName(saleOrderModel.getMerchantName());
		subtractRootJson.setTopidealCode(saleOrderModel.getTopidealCode());// 销售订单的卓志编码
		// 事业部
		subtractRootJson.setBuId(String.valueOf(saleOrderModel.getBuId()));
		subtractRootJson.setBuName(saleOrderModel.getBuName());
		subtractRootJson.setDepotId(mongo.getDepotId().toString());
		subtractRootJson.setDepotName(mongo.getName());
		subtractRootJson.setDepotCode(mongo.getCode());
		subtractRootJson.setDepotType(mongo.getType());
		subtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		subtractRootJson.setOrderNo(saleOutDepotModel.getCode());
		subtractRootJson.setBusinessNo(saleOrderModel.getCode());
		subtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		subtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		subtractRootJson.setSourceDate(TimeUtils.formatFullTime());
		subtractRootJson.setDivergenceDate(deliverDateMq);
		subtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));// 获取当前年月
		subtractRootJson.setGoodsList(subtractGoodsListJsonList);
		subtractRootJson.setBackTags(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTags());//回调标签
		subtractRootJson.setBackTopic(MQPushBackOrderEnum.SALE_OUT_DEPOT_PUSH_BACK.getTopic());//回调主题
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		customParam.put("code", saleOutDepotModel.getCode());//出库单号
		subtractRootJson.setCustomParam(customParam);//自定义回调参数

		// 修改销售出库单
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setState(DERP_ORDER.SALEORDER_STATE_027);
		sModel.setId(saleOrderModel.getId());
		saleOrderDao.modify(sModel);

		Map<String,Object> retMap = new HashMap<>();
		retMap.put("freezeRootJson",freezeRootJson);
		retMap.put("subtractRootJson",subtractRootJson);
		return retMap;
	}
	/**出库明细状态更新-业务处理方法-新end**************************************************/
}
