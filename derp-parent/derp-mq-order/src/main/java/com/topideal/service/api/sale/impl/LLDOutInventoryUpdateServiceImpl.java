package com.topideal.service.api.sale.impl;

import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.MaterialOrderDao;
import com.topideal.dao.sale.MaterialOrderItemDao;
import com.topideal.dao.sale.MaterialOutDepotDao;
import com.topideal.dao.sale.MaterialOutDepotItemDao;
import com.topideal.entity.vo.sale.MaterialOrderItemModel;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.entity.vo.sale.MaterialOutDepotItemModel;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;
import com.topideal.json.api.v1_4.OrderStatusUpdateJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.LLDOutInventoryUpdateService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 出库明细回推实现类 —— 领料单 更新出库时间，扣减库存
 */
@Service
public class LLDOutInventoryUpdateServiceImpl implements LLDOutInventoryUpdateService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LLDOutInventoryUpdateServiceImpl.class);
	// 领料订单
	@Autowired
	private MaterialOrderDao materialOrderDao;
	// 领料单表体
	@Autowired
	private MaterialOrderItemDao materialOrderItemDao;
	// 领料出库单
	@Autowired
	private MaterialOutDepotDao materialOutDepotDao;
	// 领料出库订单商品
	@Autowired
	private MaterialOutDepotItemDao materialOutDepotItemDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品信息

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203120004, model = DERP_LOG_POINT.POINT_12203120004_Label,keyword="orderId")
	public boolean saveOutInventoryInfo(String json,String keys,String topics,String tags) throws Exception {
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		
		Map classMap = new HashMap();
		// JSON对象转实体
		OrderStatusUpdateJson rootJson = (OrderStatusUpdateJson) JSONObject.toBean(jsonData, OrderStatusUpdateJson.class, classMap);
				
		String orderCode = rootJson.getOrderId();// 订单号或者LBX号
		String deliver = rootJson.getUpdateTime();//必填
		Long merchantId = rootJson.getMerchantId();// 商家ID	

		// 领料 
		MaterialOrderModel materialOrderModel = new MaterialOrderModel();
		materialOrderModel.setMerchantId(merchantId);
		materialOrderModel.setCode(orderCode);
		materialOrderModel = materialOrderDao.searchByModel(materialOrderModel);
		if (materialOrderModel == null) {
			LOGGER.error("订单不存在,订单编号" + orderCode);
			throw new RuntimeException("订单不存在,订单编号" + orderCode);
		}
		if (!DERP_ORDER.MATERIALORDER_STATE_017.equals(materialOrderModel.getState())) {
			LOGGER.error("订单状态不为“待出库”,订单编号" + orderCode);
			throw new RuntimeException("订单状态不为“待出库”,订单编号" + orderCode);
		}
		
		if(null == materialOrderModel.getBuId()){
			LOGGER.error("领料订单编号" + materialOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("领料订单编号" + materialOrderModel.getCode()+"事业部的值为空");
		}
		// 根据仓库id查询 仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", materialOrderModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		if (mongo==null ) {
			LOGGER.error("根据领料订单的仓库id没有查询到仓库,订单号:" + orderCode);
			throw new RuntimeException("根据领料订单的仓库id没有查询到仓库,订单号:" + orderCode);
		}
		if (DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_04.equals(mongo.getISVDepotType())) {
			LOGGER.error("出库仓ISV仓库类型为海外仓不走该流程" + orderCode);
			throw new RuntimeException("出库仓ISV仓库类型为海外仓不走该流程" + orderCode);
		}
		
		
		Timestamp deliverDate = TimeUtils.parse(deliver, "yyyy-MM-dd HH:mm:ss");
		//领料单编码查询领料出库单
		MaterialOutDepotModel materialOutDepotModel = new MaterialOutDepotModel();
		materialOutDepotModel.setMaterialOrderCode(orderCode);// 领料出库清单编码
		materialOutDepotModel = materialOutDepotDao.searchByModel(materialOutDepotModel);
		if (materialOutDepotModel == null) {
			LOGGER.error("领料出库清单不存在,订单号" + orderCode);
			throw new RuntimeException("领料出库清单不存在,订单号" + orderCode);
		}		
		//更新出库时间和状态
		materialOutDepotModel.setStatus(DERP_ORDER.MATERIALORDER_STATE_027);
		materialOutDepotModel.setDeliverDate(deliverDate);// 发货时间
		materialOutDepotModel.setModifyDate(TimeUtils.getNow());
		materialOutDepotDao.modify(materialOutDepotModel);
		
		MaterialOutDepotItemModel materialOutDepotItemModel = new MaterialOutDepotItemModel();
		materialOutDepotItemModel.setMaterialOutDepotId(materialOutDepotModel.getId());
       List<MaterialOutDepotItemModel> materialOutDepotItemList = materialOutDepotItemDao.list(materialOutDepotItemModel);
		List<InvetAddOrSubtractGoodsListJson> subtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		if(materialOutDepotItemList!=null && materialOutDepotItemList.size()>0) {
			for(MaterialOutDepotItemModel itemModel : materialOutDepotItemList) {
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
					subtractGoodsJson.setStockLocationTypeId(String.valueOf(materialOrderModel.getStockLocationTypeId()));
					subtractGoodsJson.setStockLocationTypeName(materialOrderModel.getStockLocationTypeName());
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(itemModel.getOverdueDate()!=null){
						isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					subtractGoodsJson.setIsExpire(isExpire);//是否过期（0是 1否）
					subtractGoodsJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
					subtractGoodsJson.setNum(itemModel.getTransferNum());
					subtractGoodsListJsonList.add(subtractGoodsJson);
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
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(itemModel.getOverdueDate()!=null){
						isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					subtractGoodsJson.setIsExpire(isExpire);//是否过期（0是 1否）
					subtractGoodsJson.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
					subtractGoodsJson.setNum(itemModel.getWornNum());
					subtractGoodsListJsonList.add(subtractGoodsJson);
				}
			}
		}

		// 库存发货时间
		String deliverDateMq = TimeUtils.format(deliverDate, "yyyy-MM-dd HH:mm:ss");
		
		//释放并减少冻结量
		InventoryFreezeRootJson freezeRootJson = new InventoryFreezeRootJson();
		freezeRootJson.setMerchantId(materialOrderModel.getMerchantId().toString());
		freezeRootJson.setMerchantName(materialOrderModel.getMerchantName());
		freezeRootJson.setDepotId(materialOrderModel.getOutDepotId().toString());
		freezeRootJson.setDepotName(materialOrderModel.getOutDepotName());
		freezeRootJson.setOrderNo(materialOutDepotModel.getCode());
		freezeRootJson.setBusinessNo(materialOrderModel.getCode());
		freezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0021);
		freezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033);
		freezeRootJson.setSourceDate(TimeUtils.formatFullTime());
		freezeRootJson.setOperateType("1");
		// 查询领料单商品
		MaterialOrderItemModel tempMaterialOrderItemModel = new MaterialOrderItemModel();
		tempMaterialOrderItemModel.setOrderId(materialOrderModel.getId());
		List<MaterialOrderItemModel> MaterialOrderItemList = materialOrderItemDao.list(tempMaterialOrderItemModel);
		List<InventoryFreezeGoodsListJson> freezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for (MaterialOrderItemModel item : MaterialOrderItemList) {
			InventoryFreezeGoodsListJson freezeGoodsJson = new InventoryFreezeGoodsListJson();
			freezeGoodsJson.setGoodsId(String.valueOf(item.getGoodsId()));
			freezeGoodsJson.setGoodsNo(item.getGoodsNo());
			freezeGoodsJson.setGoodsName(item.getGoodsName());
			freezeGoodsJson.setDivergenceDate(deliverDateMq);
			freezeGoodsJson.setNum(item.getNum());
			freezeGoodsListJsonList.add(freezeGoodsJson);
		}
		freezeRootJson.setGoodsList(freezeGoodsListJsonList);
		//扣减领料出库库存量
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setMerchantId(materialOrderModel.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(materialOrderModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(materialOrderModel.getTopidealCode());// 领料订单的卓志编码
		// 事业部
		invetAddOrSubtractRootJson.setBuId(String.valueOf(materialOrderModel.getBuId()));
		invetAddOrSubtractRootJson.setBuName(materialOrderModel.getBuName());
		invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(mongo.getName());
		invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(mongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(materialOutDepotModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(materialOrderModel.getCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0021);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033);
		invetAddOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());
		invetAddOrSubtractRootJson.setDivergenceDate(deliverDateMq);
		invetAddOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));// 获取当前年月
		invetAddOrSubtractRootJson.setGoodsList(subtractGoodsListJsonList);
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.MATERIAL_OUT_DEPOT_PUSH_BACK.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.MATERIAL_OUT_DEPOT_PUSH_BACK.getTopic());//回调主题		
		customParam.put("code", materialOrderModel.getCode());// 领料订单内部单号
		invetAddOrSubtractRootJson.setCustomParam(customParam);//自定义回调参数		
		// 推送库存
		rocketMQProducer.send(JSONObject.fromObject(freezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		// 推送加减库存
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		
		// 修改领料出库单
		MaterialOrderModel sModel = new MaterialOrderModel();
		sModel.setState(DERP_ORDER.MATERIALORDER_STATE_027);
		sModel.setId(materialOrderModel.getId());
		materialOrderDao.modify(sModel);
		return true;
	}
}
