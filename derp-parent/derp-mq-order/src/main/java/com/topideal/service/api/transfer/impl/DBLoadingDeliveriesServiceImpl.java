package com.topideal.service.api.transfer.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.transfer.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.api.v5_4.EpassLoadinfDelivriesGoodsListJson;
import com.topideal.json.api.v5_4.EpassLoadinfDelivriesRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.api.transfer.DBLoadingDeliveriesService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

/**
 * 装载交运回推接口实现类
 * 
 * @author 杨创 2018/6/5
 */
@Service
public class DBLoadingDeliveriesServiceImpl implements DBLoadingDeliveriesService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DBLoadingDeliveriesServiceImpl.class);

	@Autowired
	private TransferOrderDao transferOrderDao;// 调拨单
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;// 调拨单商品
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;// 调拨出库
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;// 调拨出库单商品
	@Autowired
	private TransferInDepotDao transferInDepotDao;// 调拨入库单
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao;// 调拨入库单表体
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品
	@Autowired
	private RMQProducer rocketMQProducer;// MQ;
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203130500, model = DERP_LOG_POINT.POINT_12203130500_Label, keyword = "orderCode")
	public boolean saveLoadingDeliveriesInfo(String json, String keys, String topics, String tags) throws Exception {
		/**
		 * 说明:电商订单只接受 服务类型为20 的单 其他的只接受 服务类型为10 的单
		 */
		// 获取json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList", EpassLoadinfDelivriesGoodsListJson.class);

		EpassLoadinfDelivriesRootJson delivriesRoot = (EpassLoadinfDelivriesRootJson) JSONObject.toBean(jsonData,
				EpassLoadinfDelivriesRootJson.class, classMap);

		String orderCode = delivriesRoot.getOrderCode();// 订单号或者LBX号
		String type = delivriesRoot.getType();// 服务类型 业务类型10：B2B 20：B2B2C 必填
		String isRookie = delivriesRoot.getIsRookie();// 获取是否菜鸟字段(1-是，0-否)
		Long merchantId = delivriesRoot.getMerchantId();// 企业入仓编号
		String topidealCode = delivriesRoot.getTopidealCode();// 企业入仓编号
		String merchantName = delivriesRoot.getMerchantName();// 企业入仓编号
		String wayBillNo = delivriesRoot.getWayBillNo();// 运单号 (运单号在推服务类型是20时必传，在服务类型是10时非必填)
		String deliver = delivriesRoot.getDeliverDate();// 发货时间 非必填 yyyy-MM-dd HH:mm:ss
		
		// 只接受 服务类型为10 的单
		if(!"10".equals(type)) {
			LOGGER.error("调拨(装载交运接口)只接收(服务类型)状态是10 B2B的单" + orderCode);
			throw new RuntimeException("调拨(装载交运接口)只接收(服务类型)状态是10 B2B的单" + orderCode);
		}
		
		TransferOrderModel transferOrderModel = new TransferOrderModel();
		transferOrderModel.setMerchantId(merchantId);
		if(isRookie.equals("1")){// 是否菜鸟字段(1-是，0-否)
			transferOrderModel.setLbxNo(orderCode);
		}else{
			transferOrderModel.setCode(orderCode);
		}
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
		if (transferOrderModel == null) {
			LOGGER.error("订单不存在,订单编号" + orderCode);
			throw new RuntimeException("订单不存在,订单编号" + orderCode);
		}

		//检查调拨单状态
		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)) { //DTJ("013","待提交"),
			LOGGER.error("调拨单状态为待提交,推送失败" + orderCode);
			throw new RuntimeException("调拨单状态为待提交,推送失败" + orderCode);
		}

		//查询调拨单商品
		TransferOrderItemModel orderItemModel = new TransferOrderItemModel();
		orderItemModel.setTransferOrderId(transferOrderModel.getId());
		List<TransferOrderItemModel> itemList = transferOrderItemDao.list(orderItemModel);
		Map<Long, TransferOrderItemModel> goodsMap = new HashMap<>();//key=原商品id value=调拨单商品实体
		Map<Long, TransferOrderItemModel> kwGoodsMap = new HashMap<>();//key=库位商品id value=调拨单商品实体  以出定入用
		for(TransferOrderItemModel model:itemList) {
			goodsMap.put(model.getOutGoodsId(), model);
			//库位商品id为key
			kwGoodsMap.put(model.getOutGoodsId(), model);
		}

		Timestamp deliverDate = null;// 发货时间
		if(deliver.length() == 10) {
			deliverDate = Timestamp.valueOf(deliver + " 00:00:00"); // 发货时间
		}else{
			deliverDate = Timestamp.valueOf(deliver);// 发货时间
		}

		// 向电商订单唯一标识表插入数据
		OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
		orderExternalCodeModel.setExternalCode(transferOrderModel.getCode());
		orderExternalCodeModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
		try {
			Long id = orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			throw new RuntimeException("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
		}

		// 根据调拨单查询调拨出库单
		TransferOutDepotModel trODepotModel = new TransferOutDepotModel();
		trODepotModel.setTransferOrderId(transferOrderModel.getId());
		trODepotModel = transferOutDepotDao.getByModel(trODepotModel);
		if(null != trODepotModel) {
			LOGGER.error("调拨出库单已存在,订单编号" + orderCode);
			throw new RuntimeException("调拨出库单已存在,订单编号" + orderCode);
		}

		//根据仓库id到mongoDB中查询调出仓库信息
		Map<String, Object> outDepotInfoMap = new HashMap<>();
		outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());// 调出仓库id
		DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);//调出仓库信息
		if (outDepotInfoMongo == null) {
			LOGGER.error("未查到调出仓信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调出仓信息,订单编号" + orderCode);
		}

		//根据仓库id到mongoDB中查询调入仓库信息
		Map<String, Object> inDepotInfoMap = new HashMap<>();
		inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());//调入仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);
		if (inDepotInfoMongo == null) {
			LOGGER.error("未查到调入仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调入仓库信息,订单编号" + orderCode);
		}
		
		// 批次效期强校验：1-是 0-否
		if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
			for (EpassLoadinfDelivriesGoodsListJson goodsListJson : delivriesRoot.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
				}
			}
		}

		Map<String, Object> inRelDepotParam = new HashMap<>();
		inRelDepotParam.put("merchantId", merchantId);
		inRelDepotParam.put("depotId", inDepotInfoMongo.getDepotId());
		DepotMerchantRelMongo inRelDepot = depotMerchantRelMongoDao.findOne(inRelDepotParam);
		if (inRelDepot == null) {
			LOGGER.error("该商家下未查到调入仓库信息,订单编号" + orderCode);
			throw new RuntimeException("该商家下未查到调入仓库信息,订单编号" + orderCode);
		}

		// 新增生成调拨出库单
		TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
		transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());// 调拨订单ID
		transferOutDepotModel.setTransferOrderCode(transferOrderModel.getCode());// 调拨订单编号
		transferOutDepotModel.setMerchantId(transferOrderModel.getMerchantId());// 商家ID
		transferOutDepotModel.setMerchantName(transferOrderModel.getMerchantName());// 商家名称
		transferOutDepotModel.setContractNo(transferOrderModel.getContractNo());// 合同号
		transferOutDepotModel.setInDepotId(transferOrderModel.getInDepotId());// 调入仓库id
		transferOutDepotModel.setInDepotName(transferOrderModel.getInDepotName());// 调入仓库名称
		transferOutDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());// 调出仓库id
		transferOutDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());// 调出仓库名称
		transferOutDepotModel.setTransferDate(deliverDate);// 发货时间
		transferOutDepotModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);//CKZ("027","出库中") // 状态015.待出仓,016.已出仓 027-出库中 028-入库中
//		transferOutDepotModel.setCode(CodeGeneratorUtil.getNo("DBCK")); // 调拨出单号
		transferOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK)); // 调拨出单号
		transferOutDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());// 调入客户id
		transferOutDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());// 调入客户名称
		transferOutDepotModel.setWayBillNo(wayBillNo);// 运单号
		transferOutDepotModel.setLbxNo(transferOrderModel.getLbxNo());
		transferOutDepotModel.setBuId(transferOrderModel.getBuId());
		transferOutDepotModel.setBuName(transferOrderModel.getBuName());
		transferOutDepotModel.setCreateName("接口回传");
		transferOutDepotModel.setDataSource(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_2);
		// 新增调拨出库单
		transferOutDepotDao.save(transferOutDepotModel);

		List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();// 释放冻结商品列表
		List<InvetAddOrSubtractGoodsListJson> addOrSubtractGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();// 扣减库存商品列表
		for(EpassLoadinfDelivriesGoodsListJson goods : delivriesRoot.getGoodsList()) {
			if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&StringUtils.isBlank(goods.getTallyingUnit())){
				LOGGER.error(goods.getGoodsNo()+"的理货单位为空");
				throw new RuntimeException(goods.getGoodsNo()+"的理货单位为空");
			}
			String orderTallyingUnit = transferOrderModel.getTallyingUnit();
			if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&!goods.getTallyingUnit().equals(orderTallyingUnit)){
				LOGGER.error(goods.getGoodsNo()+"的理货单位"+goods.getTallyingUnit()+"与调拨单的理货单位"+orderTallyingUnit+"不一致");
				throw new RuntimeException(goods.getGoodsNo()+"的理货单位"+goods.getTallyingUnit()+"与调拨单的理货单位"+orderTallyingUnit+"不一致");
			}
            //查询商品
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goods.getGoodsId());
			MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
			// 新增调拨出库单对应的商品
			TransferOutDepotItemModel outDepotItemModel = new TransferOutDepotItemModel();
			outDepotItemModel.setTransferDepotId(transferOutDepotModel.getId());// 调拨出库ID
			outDepotItemModel.setOutGoodsId(goods.getGoodsId());// 调出商品id
			outDepotItemModel.setOutGoodsCode(goods.getGoodsCode());// 调出商品编码
			outDepotItemModel.setOutGoodsName(goods.getGoodsName());// 调出商品名称
			outDepotItemModel.setOutGoodsNo(goods.getGoodsNo());// 调出商品货号
			outDepotItemModel.setOutCommbarcode(outMerchandise.getCommbarcode());// 调出商品标准条码
			outDepotItemModel.setTransferNum(goods.getNum());// 调拨数量
			outDepotItemModel.setTransferBatchNo(goods.getBatchNo());// 调拨批次
			outDepotItemModel.setTallyingUnit(goods.getTallyingUnit());//理货单位
			outDepotItemModel.setBarcode(goods.getBarcode());
			String productionDate = goods.getProductionDate();
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length() == 10) {
					outDepotItemModel.setProductionDate(Timestamp.valueOf(productionDate + " 00:00:00"));// 生产日期
				} else {
					outDepotItemModel.setProductionDate(Timestamp.valueOf(productionDate));// 生产日期
				}
			}
			String overdueDate = goods.getOverdueDate();
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length() == 10) {
					outDepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate + " 00:00:00"));// 失效日期
				} else {
					outDepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));// 失效日期
				}
			}
			transferOutDepotItemDao.save(outDepotItemModel);

			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(outDepotItemModel.getOverdueDate()!=null){
				isExpire = TimeUtils.isNotIsExpireByDate(outDepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}
			//库存单位   0 托盘 1箱 2 件
			String unit = null;
			if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)){
				if(goods.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)){
					unit = DERP.INVENTORY_UNIT_0;
				}else if(goods.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)){
					unit = DERP.INVENTORY_UNIT_1;
				}else if(goods.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)){
					unit = DERP.INVENTORY_UNIT_2;
				}
			}

			// 扣减库存商品
			InvetAddOrSubtractGoodsListJson addOrSubtractGood = new InvetAddOrSubtractGoodsListJson();
			addOrSubtractGood.setGoodsId(String.valueOf(outDepotItemModel.getOutGoodsId()));
			addOrSubtractGood.setGoodsName(outDepotItemModel.getOutGoodsName());
			addOrSubtractGood.setGoodsNo(outDepotItemModel.getOutGoodsNo());
			addOrSubtractGood.setBarcode(outDepotItemModel.getBarcode());
			addOrSubtractGood.setBatchNo(outDepotItemModel.getTransferBatchNo());
			addOrSubtractGood.setProductionDate(TimeUtils.formatDay(outDepotItemModel.getProductionDate()));
			addOrSubtractGood.setOverdueDate(TimeUtils.formatDay(outDepotItemModel.getOverdueDate()));
			addOrSubtractGood.setType(DERP.ISDAMAGE_0);// 商品分类 （0 好品 1坏品） 字符串
			addOrSubtractGood.setIsExpire(isExpire);// 是否过期（0 是 1 否）
			addOrSubtractGood.setNum(outDepotItemModel.getTransferNum());
			addOrSubtractGood.setUnit(unit);//单位 字符串 0 托盘 1箱 2 件
			addOrSubtractGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 操作类型（调增\调减） 字符串 0 调减 1调增
			addOrSubtractGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
			addOrSubtractGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
			addOrSubtractGoodsList.add(addOrSubtractGood);
		}
		//拼装解冻数据
		for(TransferOrderItemModel transferOrderItemModel : itemList) {
			 // 释放冻结商品
			InventoryFreezeGoodsListJson freezeGoods = new InventoryFreezeGoodsListJson();
			freezeGoods.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
			freezeGoods.setGoodsName(transferOrderItemModel.getOutGoodsName());
			freezeGoods.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
			freezeGoods.setDivergenceDate(TimeUtils.formatFullTime(deliverDate));
			freezeGoods.setNum(transferOrderItemModel.getTransferNum());// 释放冻结量取调拨单里的数量
			if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)){
				if(transferOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)){
					freezeGoods.setUnit(DERP.INVENTORY_UNIT_0);// 单位 字符串 0 托盘 1箱 2 件
	        	}else if(transferOrderModel.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)){
	        		freezeGoods.setUnit(DERP.INVENTORY_UNIT_1);
	        	}else{
	        		freezeGoods.setUnit(DERP.INVENTORY_UNIT_2);
	        	}
			}
			freezeGoodList.add(freezeGoods);			
		}
		
		// 释放冻结库存
		InventoryFreezeRootJson freezeRootJson = new InventoryFreezeRootJson();
		freezeRootJson.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
		freezeRootJson.setMerchantName(transferOrderModel.getMerchantName());
		freezeRootJson.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
		freezeRootJson.setDepotName(outDepotInfoMongo.getName());
		freezeRootJson.setOrderNo(transferOutDepotModel.getCode());
		freezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		freezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		freezeRootJson.setSourceDate(TimeUtils.formatFullTime());
		freezeRootJson.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 冻增\冻减 字符串 （0冻结，1解冻）
		freezeRootJson.setBusinessNo(transferOrderModel.getCode());
		freezeRootJson.setGoodsList(freezeGoodList);
		JSONObject jsonFree = JSONObject.fromObject(freezeRootJson);

		// 拼装扣减库存接口参数
		InvetAddOrSubtractRootJson addOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		addOrSubtractRootJson.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
		addOrSubtractRootJson.setMerchantName(transferOrderModel.getMerchantName());
		addOrSubtractRootJson.setTopidealCode(transferOrderModel.getTopidealCode());
		addOrSubtractRootJson.setDepotType(outDepotInfoMongo.getType());
		addOrSubtractRootJson.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
		addOrSubtractRootJson.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
		addOrSubtractRootJson.setDepotCode(outDepotInfoMongo.getCode());
		addOrSubtractRootJson.setDepotName(outDepotInfoMongo.getName());
		addOrSubtractRootJson.setOrderNo(transferOutDepotModel.getCode());
		addOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		addOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		addOrSubtractRootJson.setSourceDate(TimeUtils.formatFullTime());
		addOrSubtractRootJson.setOwnMonth(TimeUtils.formatMonth(deliverDate));
		addOrSubtractRootJson.setDivergenceDate(TimeUtils.formatFullTime(deliverDate));
		addOrSubtractRootJson.setBusinessNo(transferOrderModel.getCode());
		addOrSubtractRootJson.setGoodsList(addOrSubtractGoodsList);
		addOrSubtractRootJson.setBuId(String.valueOf(transferOrderModel.getBuId()));
		addOrSubtractRootJson.setBuName(transferOrderModel.getBuName());
		addOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());//回调参数
		addOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
		addOrSubtractRootJson.setCustomParam(new HashMap<String, Object>());
		JSONObject jsonRequest = JSONObject.fromObject(addOrSubtractRootJson);

		boolean flag = false;
		// 调拨入仓仓库是 在对应商家下的“以出定入”标识为“是”的，如果调出库仓库为香港仓时，不走以出定入
		//（查询调拨入库单是否存在，若存在则不保存调拨入库单）
		if(DERP_SYS.DEPOTMERCHANTREL_ISOUTDEPENDIN_1.equals(inRelDepot.getIsOutDependIn())
				&& !DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfoMongo.getType())) {
			OrderExternalCodeModel transferInDepotExistModel = new OrderExternalCodeModel();
			transferInDepotExistModel.setExternalCode(transferOrderModel.getCode());
			transferInDepotExistModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_4));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
			try {
				orderExternalCodeDao.save(transferInDepotExistModel);
				//查询入库单是否存在
				TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
				transferInDepotModel.setTransferOrderId(transferOrderModel.getId());
				transferInDepotModel = transferInDepotDao.searchByModel(transferInDepotModel);
				if (transferInDepotModel == null){ //不存在
					flag = true;
				}
			} catch (Exception e) {
				LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			}
		}

		// 生成调拨入库单
		if(flag) {
			for(EpassLoadinfDelivriesGoodsListJson goodsJson : delivriesRoot.getGoodsList()) {
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())
						|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())) {
					String batchNo = goodsJson.getBatchNo();
					String productionDate = goodsJson.getProductionDate();
					String overdueDate = goodsJson.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						LOGGER.error("已出定入入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验，"+"批次和效期不能为空,订单号:" + orderCode);
						throw new RuntimeException("已出定入入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验，"+"批次效和期不能为空,订单号:" + orderCode);
					}
				}
				//检查报文商品在单据中是否存在
				TransferOrderItemModel orderItem = goodsMap.get(goodsJson.getGoodsId());
				if(orderItem == null) {
					LOGGER.error("调拨单未找到商品,订单编号" + orderCode+",货号"+goodsJson.getGoodsId());
					throw new RuntimeException("调拨单未找到商品,订单编号" + orderCode+",货号"+goodsJson.getGoodsId());
				}
			}
			// 向调拨入库单中插入数据
			TransferInDepotModel inDepotModel = new TransferInDepotModel();
			inDepotModel.setTransferOrderId(transferOrderModel.getId());// 调拨订单ID
			inDepotModel.setTransferOrderCode(transferOrderModel.getCode());// 调拨订单编号
			inDepotModel.setMerchantId(transferOrderModel.getMerchantId());// 商家ID
			inDepotModel.setMerchantName(transferOrderModel.getMerchantName());// 商家名称
			inDepotModel.setContractNo(transferOrderModel.getContractNo());// 合同号
			inDepotModel.setInDepotId(transferOrderModel.getInDepotId());// 调入仓库id
			inDepotModel.setInDepotName(transferOrderModel.getInDepotName());// 调入仓库名称
			inDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());// 调出仓库id
			inDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());// 调出仓库名称
			inDepotModel.setTransferDate(deliverDate);// 调入时间(发货时间)
			inDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028);//RKZ("028","入库中"),// 状态 011.待入仓,012.已入仓,028.入库中
//			inDepotModel.setCode(CodeGeneratorUtil.getNo("DBRK"));
			inDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));
			inDepotModel.setSource(DERP_ORDER.TRANSFERINDEPOT_SOURCE_2);// 1-调拨入自动生成 2-接口回推生成
			inDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());
			inDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());
			inDepotModel.setLbxNo(transferOrderModel.getLbxNo());// LBX号
			inDepotModel.setBuId(transferOrderModel.getBuId());
			inDepotModel.setBuName(transferOrderModel.getBuName());
			inDepotModel.setCreateName("接口回传");
			// 调拨入库单插入数据
			transferInDepotDao.save(inDepotModel);

			//查询出库生成入库单表体和加库存报文
			TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
			outDepotModel.setTransferOrderId(transferOrderModel.getId());
			outDepotModel = transferOutDepotDao.searchByModel(outDepotModel);
			TransferOutDepotItemModel outDepotItem = new TransferOutDepotItemModel();
			outDepotItem.setTransferDepotId(outDepotModel.getId());
			List<TransferOutDepotItemModel> outItemList = transferOutDepotItemDao.list(outDepotItem);
			Set<String> existGoodsSet = new HashSet<>();
			List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();// 加库存商品
			for(TransferOutDepotItemModel outItem : outItemList){
				TransferOrderItemModel orderItem = kwGoodsMap.get(outItem.getOutGoodsId());
				//调拨入库单表体
				TransferInDepotItemModel InDepotItemModel = new TransferInDepotItemModel();
				InDepotItemModel.setTransferDepotId(inDepotModel.getId());// 调拨入库ID
				InDepotItemModel.setInGoodsId(orderItem.getInGoodsId());// 调入商品id
				InDepotItemModel.setInGoodsName(orderItem.getInGoodsName());// 调入商品名称
				InDepotItemModel.setInGoodsNo(orderItem.getInGoodsNo());// 调入商品货号
				InDepotItemModel.setInGoodsCode(orderItem.getInGoodsCode());// 调入商品编码
				InDepotItemModel.setInCommbarcode(orderItem.getInCommbarcode());// 调入商品标准条码
				//调出仓为香港仓、理货单位不为件时调入数量取调拨单里的数量(不考虑商品批次、效期，若同一货号存在多条则只新增一条商品数据)
				// 00-托盘 01-箱 02-件
				if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&!outItem.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)){
					if (existGoodsSet.contains(outItem.getOutGoodsNo())) {
						continue;
					} else {
						InDepotItemModel.setTransferNum(orderItem.getInTransferNum());// 调拨数量
						InDepotItemModel.setTallyingUnit(orderItem.getInUnit()); //调入商品理货单位
						existGoodsSet.add(outItem.getOutGoodsNo());
					}
				}else{
					InDepotItemModel.setTransferNum(outItem.getTransferNum());// 调拨数量
					InDepotItemModel.setTallyingUnit(outItem.getTallyingUnit());// 理货单位
				}
				InDepotItemModel.setProductionDate(outItem.getProductionDate());// 生产日期
				InDepotItemModel.setOverdueDate(outItem.getOverdueDate());// 失效日期
				InDepotItemModel.setTransferBatchNo(outItem.getTransferBatchNo());// 调拨批次
				InDepotItemModel.setBarcode(orderItem.getInBarcode());
				transferInDepotItemDao.save(InDepotItemModel);

				// 拼装加库存商品
				InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();
				aSGood.setGoodsId(String.valueOf(InDepotItemModel.getInGoodsId()));//原商品
				aSGood.setGoodsNo(InDepotItemModel.getInGoodsNo());
				aSGood.setGoodsName(InDepotItemModel.getInGoodsName());
				aSGood.setBarcode(InDepotItemModel.getBarcode());
				aSGood.setProductionDate(TimeUtils.formatDay(InDepotItemModel.getProductionDate()));
				aSGood.setOverdueDate(TimeUtils.formatDay(InDepotItemModel.getOverdueDate()));
				aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);// 商品分类 （0 好品 1坏品） 字符串
				aSGood.setNum(InDepotItemModel.getTransferNum());
				//aSGood.setUnit(unit);//单位 字符串 0 托盘 1箱 2 件
				aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 操作类型（调增\调减） 字符串 0 调减 1调增
				aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				//调出仓为香港仓、理货单位不为件时，加库存不传商品批次
				if(outDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&!outItem.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)) {
					String unit = orderItem.getInUnit();
					switch (unit) {
						case DERP.ORDER_TALLYINGUNIT_00 : unit = DERP.INVENTORY_UNIT_0; break;
						case DERP.ORDER_TALLYINGUNIT_01 : unit = DERP.INVENTORY_UNIT_1; break;
						default: unit = DERP.INVENTORY_UNIT_2; break;
					}
					aSGood.setUnit(unit);
				} else {
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(InDepotItemModel.getOverdueDate()!=null){
						isExpire = TimeUtils.isNotIsExpireByDate(InDepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					aSGood.setIsExpire(isExpire);// 是否过期（0 是 1 否）
					aSGood.setBatchNo(InDepotItemModel.getTransferBatchNo());
				}
				subGoodsList.add(aSGood);
			}

			// 拼装加库存接口参数
			InvetAddOrSubtractRootJson subInventoryRoot = new InvetAddOrSubtractRootJson();
			subInventoryRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
			subInventoryRoot.setMerchantName(transferOrderModel.getMerchantName());
			subInventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
			subInventoryRoot.setDepotType(inDepotInfoMongo.getType());
			subInventoryRoot.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
			subInventoryRoot.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
			subInventoryRoot.setDepotCode(inDepotInfoMongo.getCode());
			subInventoryRoot.setDepotName(inDepotInfoMongo.getName());
			subInventoryRoot.setOrderNo(inDepotModel.getCode());
			subInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
			subInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010); //DBRK("0010","调拨入库"),
			subInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
			subInventoryRoot.setOwnMonth(TimeUtils.formatMonth(deliverDate));
			subInventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(deliverDate));
			subInventoryRoot.setBusinessNo(transferOrderModel.getCode());
			subInventoryRoot.setGoodsList(subGoodsList);
			subInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
			subInventoryRoot.setBuName(transferOrderModel.getBuName());
			//回调参数
			subInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
			subInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
			subInventoryRoot.setCustomParam(new HashMap<String, Object>());
			// 加库存
			JSONObject subjson = JSONObject.fromObject(subInventoryRoot);
			rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		}
		 // 把释放冻结放到最下方防止已出定入出现异常接口报错
		rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),
				MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		// 把加减库存放到最下方防止已出定入出现异常接口报错
		SendResult sendResult = rocketMQProducer.send(jsonRequest.toString(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
				MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		return true;
	}

}
