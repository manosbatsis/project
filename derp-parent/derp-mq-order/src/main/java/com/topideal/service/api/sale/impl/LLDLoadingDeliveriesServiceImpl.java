package com.topideal.service.api.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.MaterialOrderDao;
import com.topideal.dao.sale.MaterialOrderItemDao;
import com.topideal.dao.sale.MaterialOutDepotDao;
import com.topideal.dao.sale.MaterialOutDepotItemDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.MaterialOrderItemModel;
import com.topideal.entity.vo.sale.MaterialOrderModel;
import com.topideal.entity.vo.sale.MaterialOutDepotItemModel;
import com.topideal.entity.vo.sale.MaterialOutDepotModel;
import com.topideal.json.api.v2_3.SaleLoadingDeliveriesMQGoodsListJson;
import com.topideal.json.api.v2_3.SaleLoadingDeliveriesMQRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.LLDLoadingDeliveriesService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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
 * 装载交运回推接口实现类 —— 领料单
 */
@Service
public class LLDLoadingDeliveriesServiceImpl implements LLDLoadingDeliveriesService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LLDLoadingDeliveriesServiceImpl.class);
	// 领料订单
	@Autowired
	private MaterialOrderDao materialOrderDao;
	// 领料出库单
	@Autowired
	private MaterialOutDepotDao materialOutDepotDao;
	// 领料出库订单商品
	@Autowired
	private MaterialOutDepotItemDao materialOutDepotItemDao;
	// 领料订单表体
	@Autowired
	private MaterialOrderItemDao materialOrderItemDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203120002, model = DERP_LOG_POINT.POINT_12203120002_Label,keyword="orderCode")
	public boolean saveLoadingDeliveriesInfo(String json,String keys,String topics,String tags) throws Exception {
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);

		Map classMap = new HashMap();
		classMap.put("goodsList",SaleLoadingDeliveriesMQGoodsListJson.class);
		// JSON对象转实体
		SaleLoadingDeliveriesMQRootJson rootJson = (SaleLoadingDeliveriesMQRootJson) JSONObject.toBean(jsonData, SaleLoadingDeliveriesMQRootJson.class, classMap);

		String orderCode = rootJson.getOrderCode();// 订单号或者LBX号
		String type = rootJson.getType();// 服务类型 业务类型10：B2B 20：B2B2C 必填
		String wayBillNo = rootJson.getWayBillNo();// 运单号非必填
		String logisticsName = rootJson.getLogisticsName();// 物流企业名称非必填
		String blNo = rootJson.getBlNo();// 提单号非必填
		String deliver = rootJson.getDeliverDate();// 发货时间 非必填
		String topidealCode = rootJson.getTopidealCode();

		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_8));	// 订单来源  1:电商订单, 2:装载交运 3:领料出库
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("领料外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
			throw new RuntimeException("领料外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
		}

		// 领料
		MaterialOrderModel materialOrderModel = new MaterialOrderModel();
		materialOrderModel.setMerchantId(rootJson.getMerchantId());
		materialOrderModel.setCode(orderCode);
		materialOrderModel = materialOrderDao.searchByModel(materialOrderModel);
		if (materialOrderModel == null) {
			LOGGER.error("订单不存在,订单编号" + orderCode);
			throw new RuntimeException("订单不存在,订单编号" + orderCode);
		}
		if (DERP_ORDER.MATERIALORDER_STATE_001.equals(materialOrderModel.getState())) {
			LOGGER.error("订单状态为“待审核”,订单编号" + orderCode);
			throw new RuntimeException("订单状态为“待审核”,订单编号" + orderCode);
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

		//领料单编码查询领料出库清单
		MaterialOutDepotModel materialOutDepotModel = new MaterialOutDepotModel();
		materialOutDepotModel.setMaterialOrderCode(orderCode);// 领料单编码
		materialOutDepotModel = materialOutDepotDao.searchByModel(materialOutDepotModel);
		if (materialOutDepotModel != null) {
			LOGGER.error("领料出库清单已经存在,订单号" + orderCode);
			throw new RuntimeException("领料出库清单已经存在,订单号" + orderCode);
		}
		String saleOrderTallyingUnit = materialOrderModel.getTallyingUnit();// 领料单中的理货单位
		for(SaleLoadingDeliveriesMQGoodsListJson goodsJson : rootJson.getGoodsList()) {
			//批次效期强校验：1-是 0-否
			if("1".equals(mongo.getBatchValidation())) {
				String batchNo = goodsJson.getBatchNo();
				String productionDate = goodsJson.getProductionDate();
				String overdueDate = goodsJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + orderCode);
				}
			}
			//香港仓-检查领料订单中的理货单位跟接口回推的单位是否一致 理货单位(00-托盘，01-箱，02-件)
			String tallyingUnit = goodsJson.getTallyingUnit();
			if("c".equals(mongo.getType())) {
				if (!saleOrderTallyingUnit.equals(tallyingUnit)) {
					LOGGER.error("香港仓 领料订单中的理货单位和接口回推的理货单位不同,订单号:" + orderCode);
					throw new RuntimeException("香港仓 领料订单中的理货单位和接口回推的理货单位不同,订单号:" + orderCode);
				}
			}
		}

		// 查询领料单商品
		MaterialOrderItemModel tempMaterialOrderItemModel = new MaterialOrderItemModel();
		tempMaterialOrderItemModel.setOrderId(materialOrderModel.getId());
		List<MaterialOrderItemModel> materialOrderItemList = materialOrderItemDao.list(tempMaterialOrderItemModel);
		Map<Long, MaterialOrderItemModel> kwGoodsMap = new HashMap<>();//库位商品 key=库位商品id value=单据商品
		for(MaterialOrderItemModel model:materialOrderItemList) {
			kwGoodsMap.put(model.getGoodsId(),model);
		}

		Timestamp deliverDate = null;// 发货时间
		if (StringUtils.isNotBlank(deliver)) {
			if (deliver.length() == 10) {
				deliverDate = Timestamp.valueOf(deliver + " 00:00:00"); // 发货时间
			} else {
				deliverDate = Timestamp.valueOf(deliver);// 发货时间
			}
		}

		// 新增领料出库单表头
		MaterialOutDepotModel mOutDepotModel = new MaterialOutDepotModel();
		mOutDepotModel.setMaterialOrderId(materialOrderModel.getId());// 领料订单id
		mOutDepotModel.setMerchantId(materialOrderModel.getMerchantId());// 商家ID
		mOutDepotModel.setPoNo(materialOrderModel.getPoNo());// PO号
		mOutDepotModel.setOutDepotId(materialOrderModel.getOutDepotId());// 调出仓库id
		mOutDepotModel.setOutDepotName(materialOrderModel.getOutDepotName()); // 调出仓库名称
		mOutDepotModel.setCustomerId(materialOrderModel.getCustomerId());// 客户id(供应商)
		mOutDepotModel.setCustomerName(materialOrderModel.getCustomerName());// 客户名称
		mOutDepotModel.setDeliverDate(deliverDate);// 发货时间
		mOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);// '状态 017,待出库 ,018,已出库,027:出库中
		mOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LLCK));// 出库清单编号自生成
		mOutDepotModel.setMerchantName(materialOrderModel.getMerchantName());// 商家名称
		mOutDepotModel.setMaterialOrderCode(materialOrderModel.getCode());// 领料订单编号
		mOutDepotModel.setLogisticsName(logisticsName);// 物流企业名称
		mOutDepotModel.setBlNo(blNo);// 提单号物流企业名称
		mOutDepotModel.setWayBillNo(wayBillNo);// 运单号
		mOutDepotModel.setImportMode(type);// 进口模式 10：BBC;20：BC 30：保留; 40：CC
		mOutDepotModel.setOrderSource("2");// 1手工导入 2.接口回传
		// 事业部
		mOutDepotModel.setBuId(materialOrderModel.getBuId());
		mOutDepotModel.setBuName(materialOrderModel.getBuName());
		// 新增
		materialOutDepotDao.save(mOutDepotModel);


		List<InvetAddOrSubtractGoodsListJson> subtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		for (SaleLoadingDeliveriesMQGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				// 获取领料订单商品信息
				MaterialOrderItemModel orderItemModel = kwGoodsMap.get(goodsListJson.getGoodsId());
				String tallyingUnit = goodsListJson.getTallyingUnit();

				// 新增领料出库表体
				MaterialOutDepotItemModel itemModel = new MaterialOutDepotItemModel();
				itemModel.setMaterialOutDepotId(mOutDepotModel.getId());// 领料出库ID
				itemModel.setGoodsId(goodsListJson.getGoodsId());// 商品id
				itemModel.setGoodsCode(goodsListJson.getGoodsCode());//商品编码
				itemModel.setGoodsName(goodsListJson.getGoodsName());//商品名称
				itemModel.setGoodsNo(goodsListJson.getGoodsNo());//商品货号
				itemModel.setTransferNum(goodsListJson.getNum());//数量
				itemModel.setTransferBatchNo(goodsListJson.getBatchNo());//批次
				itemModel.setBarcode(goodsListJson.getBarcode());//货品条形码
				itemModel.setCommbarcode(goodsListJson.getCommbarcode());//标准条码
				itemModel.setMaterialNum(orderItemModel.getNum());
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
					itemModel.setTallyingUnit(saleOrderTallyingUnit);//领料订单理货单位
				}
				String productionDate = goodsListJson.getProductionDate();//生产日期
				if (StringUtils.isNotBlank(productionDate)) {
					if (productionDate.length()==10) {
						itemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
					}else {
						itemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
					}
				}
				String overdueDate = goodsListJson.getOverdueDate();//生产日期
				if (StringUtils.isNotBlank(overdueDate)) {
					if (overdueDate.length()==10) {
						itemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
					}else {
						itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
					}
				}
				//新增领料出库表体
				materialOutDepotItemDao.save(itemModel);

				//库存加减接口商品实体
				InvetAddOrSubtractGoodsListJson subtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				subtractGoodsListJson.setGoodsId(String.valueOf(itemModel.getGoodsId()));
				subtractGoodsListJson.setGoodsName(itemModel.getGoodsName());
				subtractGoodsListJson.setGoodsNo(itemModel.getGoodsNo());
				subtractGoodsListJson.setBarcode(itemModel.getBarcode());
				subtractGoodsListJson.setType("0");
				subtractGoodsListJson.setNum(itemModel.getTransferNum());//出库数量
				subtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
				subtractGoodsListJson.setBatchNo(itemModel.getTransferBatchNo());//批次
			    subtractGoodsListJson.setStockLocationTypeId(String.valueOf(materialOrderModel.getStockLocationTypeId()));
			    subtractGoodsListJson.setStockLocationTypeName(materialOrderModel.getStockLocationTypeName());
				//计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if(itemModel.getOverdueDate()!=null){
					isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
				}
				if(DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
					String addeUnit=null;
					if ("00".equals(saleOrderTallyingUnit)) {
						addeUnit="0";
					}else if("01".equals(saleOrderTallyingUnit)){
						addeUnit="1";
					}else if("02".equals(saleOrderTallyingUnit)){
						addeUnit="2";
					}
					subtractGoodsListJson.setUnit(addeUnit);// 领料订单 单位	字符串 0 托盘 1箱  2 件
				}
				subtractGoodsListJson.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
				subtractGoodsListJson.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
				subtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）
				subtractGoodsListJsonList.add(subtractGoodsListJson);
		}

        //拼装解冻商品数据
		List<InventoryFreezeGoodsListJson> freezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for (MaterialOrderItemModel item : materialOrderItemList) {
			InventoryFreezeGoodsListJson freezeGoodsJson = new InventoryFreezeGoodsListJson();
			freezeGoodsJson.setGoodsId(String.valueOf(item.getGoodsId()));
			freezeGoodsJson.setGoodsNo(item.getGoodsNo());
			freezeGoodsJson.setGoodsName(item.getGoodsName());
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(mongo.getType())) {
				String freezeUnit=null;
				if ("00".equals(saleOrderTallyingUnit)) {
					freezeUnit="0";
				}else if("01".equals(saleOrderTallyingUnit)){
					freezeUnit="1";
				}else if("02".equals(saleOrderTallyingUnit)){
					freezeUnit="2";
				}
				freezeGoodsJson.setUnit(freezeUnit);// 领料订单 单位	字符串 0 托盘 1箱  2 件
			}
			String deliverDate1 = null;// 发货时间
			if (StringUtils.isNotBlank(deliver)) {
				if (deliver.length() == 10) {
					deliverDate1 = deliver + " 00:00:00"; // 发货时间
				} else {
					deliverDate1 = deliver;// 发货时间
				}
			}
			freezeGoodsJson.setDivergenceDate(deliverDate1);
			freezeGoodsJson.setNum(item.getNum());
			freezeGoodsListJsonList.add(freezeGoodsJson);
		}
		//释放并减少冻结量
		InventoryFreezeRootJson freezeRootJson = new InventoryFreezeRootJson();
		freezeRootJson.setMerchantId(materialOrderModel.getMerchantId().toString());
		freezeRootJson.setMerchantName(materialOrderModel.getMerchantName());
		freezeRootJson.setDepotId(materialOrderModel.getOutDepotId().toString());
		freezeRootJson.setDepotName(materialOrderModel.getOutDepotName());
		freezeRootJson.setOrderNo(mOutDepotModel.getCode());
		freezeRootJson.setBusinessNo(materialOrderModel.getCode());
		freezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0021);
		freezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033);
		freezeRootJson.setSourceDate(TimeUtils.formatFullTime());
		freezeRootJson.setOperateType("1");
		freezeRootJson.setGoodsList(freezeGoodsListJsonList);

		rocketMQProducer.send(JSONObject.fromObject(freezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());

		//扣减领料出库库存量
		InvetAddOrSubtractRootJson subtractRootJson = new InvetAddOrSubtractRootJson();
		subtractRootJson.setMerchantId(materialOrderModel.getMerchantId().toString());
		subtractRootJson.setMerchantName(materialOrderModel.getMerchantName());
		subtractRootJson.setTopidealCode(materialOrderModel.getTopidealCode());// 领料订单的卓志编码
		// 事业部
		subtractRootJson.setBuId(String.valueOf(materialOrderModel.getBuId()));
		subtractRootJson.setBuName(materialOrderModel.getBuName());
		subtractRootJson.setDepotId(mongo.getDepotId().toString());
		subtractRootJson.setDepotName(mongo.getName());
		subtractRootJson.setDepotCode(mongo.getCode());
		subtractRootJson.setDepotType(mongo.getType());
		subtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		subtractRootJson.setOrderNo(mOutDepotModel.getCode());
		subtractRootJson.setBusinessNo(materialOrderModel.getCode());
		subtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0021);
		subtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0033);
		subtractRootJson.setSourceDate(TimeUtils.formatFullTime());
		String deliverDate1 = null;// 发货时间
		if (StringUtils.isNotBlank(deliver)) {
			if (deliver.length() == 10) {
				deliverDate1 = deliver + " 00:00:00"; // 发货时间
			} else {
				deliverDate1 = deliver;// 发货时间
			}
		}
		subtractRootJson.setDivergenceDate(deliverDate1);//发货时间
		subtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));//归属月份
		subtractRootJson.setGoodsList(subtractGoodsListJsonList);
		subtractRootJson.setBackTags(MQPushBackOrderEnum.MATERIAL_OUT_DEPOT_PUSH_BACK.getTags());//回调标签
		subtractRootJson.setBackTopic(MQPushBackOrderEnum.MATERIAL_OUT_DEPOT_PUSH_BACK.getTopic());//回调主题
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		customParam.put("code", materialOrderModel.getCode());// 领料订单单号
		subtractRootJson.setCustomParam(customParam);////自定义回调参数

		rocketMQProducer.send(JSONObject.fromObject(subtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		// 修改领料单
		MaterialOrderModel mModel = new MaterialOrderModel();
		mModel.setState(DERP_ORDER.MATERIALORDER_STATE_027);//004:出库中
		mModel.setId(materialOrderModel.getId());
		materialOrderDao.modify(mModel);
		return true;
	}
}
