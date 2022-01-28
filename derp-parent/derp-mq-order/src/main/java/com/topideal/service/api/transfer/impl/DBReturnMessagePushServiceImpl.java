/*package com.topideal.service.api.transfer.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.transfer.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.api.v5_5.EpassReturnMessagePushGoodsListJson;
import com.topideal.json.api.v5_5.EpassReturnMessagePushRootJson;
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
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.service.api.transfer.DBReturnMessagePushService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*//**
 * 退运单信息推送
 * 
 * @author YUCAIFU
 *//*
@Service
public class DBReturnMessagePushServiceImpl implements DBReturnMessagePushService {
	 打印日志 
	private static final Logger LOGGER = LoggerFactory.getLogger(DBReturnMessagePushServiceImpl.class);
	@Autowired
	private TransferOrderDao transferOrderDao;// 调拨单
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;// 调拨单商品
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;// 调拨出库单
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;// 调拨出库单商品
	@Autowired
	private TransferInDepotDao transferInDepotDao;// 调拨入库单
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao; // 调拨入库单表体
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	// 说明 目前用到退运信息的只有调拨出库
	@Override
	@SystemServiceLog(point = "12203130700", model = "退运单信息推送接口-调拨",keyword="orderCode")
	public boolean saveReturnMessagePushInfo(String json,String keys,String topics,String tags) throws Exception {

		*//**
		 * 对于退运信息 目前只对状态是70的操作
		 *//*

		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",EpassReturnMessagePushGoodsListJson.class);
		
		EpassReturnMessagePushRootJson returnRoot = (EpassReturnMessagePushRootJson) JSONObject.toBean(jsonData, EpassReturnMessagePushRootJson.class,classMap);
		String orderCode = returnRoot.getOrderCode();// 订单号
		String isRookie = returnRoot.getIsRookie();//获取是否菜鸟字段(1-是，0-否)
		Long merchantId = returnRoot.getMerchantId();// 商家id
		String topidealCode = returnRoot.getTopidealCode();// 企业入仓编号
		String merchantName = returnRoot.getMerchantName();// 企业入仓编号
		String status = returnRoot.getStatus();// 10：制单30：提交70：成功90：作废 必填
		if (!"70".equals(status)) {
			LOGGER.error("退运信息只接收状态是70 的单orderCode:" + orderCode);
			throw new RuntimeException("退运信息只接收状态是70 的单orderCode:" + orderCode);
		}
		// 来自调拨 (非菜鸟)tag=2
		TransferOrderModel transferOrderModel = new TransferOrderModel();
		if(isRookie.equals("1")) {
			transferOrderModel.setLbxNo(orderCode);
		}else{
			transferOrderModel.setCode(orderCode);
		}
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
		if (transferOrderModel == null) {
			LOGGER.error("没有查询到对应的订单,订单号" + orderCode);
			throw new RuntimeException("没有查询到对应的订单,订单号" + orderCode);
		}
		
		//查询调拨入商品
		TransferOrderItemModel orderItemModel = new TransferOrderItemModel();
		orderItemModel.setTransferOrderId(transferOrderModel.getId());
		List<TransferOrderItemModel> itemList = transferOrderItemDao.list(orderItemModel);
		Map<Long, TransferOrderItemModel> goodMap1 = new HashMap<Long, TransferOrderItemModel>();
		for(TransferOrderItemModel model:itemList){
			goodMap1.put(model.getOutGoodsId(), model);
		}
		
		//检查调拨单状态
		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)){ //DTJ("013","待提交"),
			LOGGER.error("调拨单状态为待提交,推送失败"+orderCode);
			throw new RuntimeException("调拨单状态为待提交,推送失败"+orderCode);
		}
				
		
		String transfer = jsonData.getString("transferDate");// 调出日期(发货日期) 必填
		Timestamp transferDate = null;// 发货时间
		if (transfer.length() == 10) {
			transferDate = Timestamp.valueOf(transfer + " 00:00:00"); // 发货时间
		} else {
			transferDate = Timestamp.valueOf(transfer);// 发货时间
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(transferDate);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(transferDate);//归属月份

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
		TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
		transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());
		transferOutDepotModel = transferOutDepotDao.getByModel(transferOutDepotModel);
		if (transferOutDepotModel!=null) {
			LOGGER.info("调拨出库单已经存在,订单号orderCode"+orderCode);
			throw new RuntimeException("调拨出库单已经存在,订单号orderCode"+orderCode);
		}

		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> outDepotInfoMap = new HashMap<>();
		outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());// 调出仓库id
		DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);// 调出仓库信息
		if (outDepotInfoMongo == null) {
			LOGGER.error("未查到调出仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调出仓库信息,订单编号" + orderCode);
		}

		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> inDepotInfoMap = new HashMap<>();
		inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());//// 调入仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);
		if (inDepotInfoMongo == null) {
			LOGGER.error("未查到调入仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调入仓库信息,订单编号" + orderCode);
		}
		
		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
			for (EpassReturnMessagePushGoodsListJson goodsListJson : returnRoot.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error("出库仓"+outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException("出库仓"+outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
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

		// 新增调拨出库单
		TransferOutDepotModel trOutDepotModel = new TransferOutDepotModel();
		trOutDepotModel.setTransferOrderId(transferOrderModel.getId());// 调拨订单ID
		trOutDepotModel.setMerchantId(transferOrderModel.getMerchantId());// 商家ID
		trOutDepotModel.setMerchantName(transferOrderModel.getMerchantName());// 商家名称
		trOutDepotModel.setContractNo(transferOrderModel.getContractNo());// 合同号
		trOutDepotModel.setInDepotId(transferOrderModel.getInDepotId());// 调入仓库id
		trOutDepotModel.setInDepotName(transferOrderModel.getInDepotName());// 调入仓库名称
		trOutDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());// 调出仓库id
		trOutDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());// 调出仓库名称
		trOutDepotModel.setTransferDate(transferDate);// 调出时间
		trOutDepotModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);//CKZ("027","出库中"), // 状态 015.待出仓 ,016.已出仓 027.出库中,
//		trOutDepotModel.setCode(CodeGeneratorUtil.getNo("DBCK"));// 调拨出单号
		trOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK));// 调拨出单号
		trOutDepotModel.setTransferOrderCode(transferOrderModel.getCode());// 调拨订单编号
		trOutDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());// 调入客户id
		trOutDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());// 调入客户名称
		trOutDepotModel.setReturnStatus(DERP_ORDER.TRANSFEROUTDEPOT_RETURNSTATUS_70);
		// trOutDepotModel.setWayBillNo(wayBillNo);//运单号
		trOutDepotModel.setLbxNo(transferOrderModel.getLbxNo());// lbx号
		trOutDepotModel.setBuId(transferOrderModel.getBuId());
		trOutDepotModel.setBuName(transferOrderModel.getBuName());
		trOutDepotModel.setCreateName("接口回传");
		// 新增调出出库单
		transferOutDepotDao.save(trOutDepotModel);
		
		List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();//释放冻结商品列表
		List<InvetAddOrSubtractGoodsListJson> addOrSubtractGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//扣减库存商品列表
		List<EpassReturnMessagePushGoodsListJson> goodsList = returnRoot.getGoodsList();
		for(EpassReturnMessagePushGoodsListJson goods : goodsList) {
			
			//检查报文里的商品调拨单中是否存在
			TransferOrderItemModel orderItem = goodMap1.get(goods.getGoodsId());
			if(orderItem == null) {
				LOGGER.error("调拨单未找到商品,订单编号" + orderCode+",货号"+goods.getGoodsId());
				throw new RuntimeException("调拨单未找到商品,订单编号" + orderCode+",货号"+goods.getGoodsId());
			}
			
			// 新增调拨出库表体
			TransferOutDepotItemModel itemModel = new TransferOutDepotItemModel();
			itemModel.setTransferDepotId(trOutDepotModel.getId());// 调拨出库单ID
			itemModel.setOutGoodsId(goods.getGoodsId());// 调出商品id
			itemModel.setOutGoodsCode(goods.getGoodsCode());// 调出商品编码
			itemModel.setOutGoodsNo(goods.getGoodsNo());// 调出商品货号
			itemModel.setOutGoodsName(goods.getGoodsName());// 商品名称
			itemModel.setOutCommbarcode(orderItem.getOutCommbarcode());// 商品标准条码
			itemModel.setTransferNum(goods.getNum());// 调拨数量
			itemModel.setTransferBatchNo(goods.getBatchNo());// 调拨批次
			itemModel.setBarcode(orderItem.getOutBarcode());
			String productionDate = goods.getProductionDate();
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length()==10) {	
					itemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
                }else {
                	itemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
    			}
			}
			String overdueDate = goods.getOverdueDate();
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length()==10) {	
					itemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
                }else {
                	itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
    			}
			}
			
			// 新增调拨出库单表体
			transferOutDepotItemDao.save(itemModel);
			
			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(itemModel.getOverdueDate()!=null){
				isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}
			
			//扣减库存商品
			InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();
			aSGood.setGoodsId(String.valueOf(itemModel.getOutGoodsId()));
			aSGood.setGoodsName(itemModel.getOutGoodsName());
			aSGood.setGoodsNo(itemModel.getOutGoodsNo());
			aSGood.setBarcode(itemModel.getBarcode());
			aSGood.setBatchNo(itemModel.getTransferBatchNo());
			aSGood.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
			aSGood.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
			aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
			aSGood.setIsExpire(isExpire);//是否过期（0 是 1 否）
			aSGood.setNum(itemModel.getTransferNum());
			//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
			aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
			
			addOrSubtractGoodsList.add(aSGood);
		}	
		
		//释放冻结商品
		for(TransferOrderItemModel model:itemList){
			InventoryFreezeGoodsListJson freezeGood = new InventoryFreezeGoodsListJson();
			freezeGood.setGoodsId(String.valueOf(model.getOutGoodsId()));
			freezeGood.setGoodsName(model.getOutGoodsName());
			freezeGood.setGoodsNo(model.getOutGoodsNo());
			freezeGood.setDivergenceDate(transferStr);
			freezeGood.setNum(model.getTransferNum());
			//fgood.setUnit("2");//	单位	字符串 0 托盘 1箱  2 件
			freezeGoodList.add(freezeGood);
		}
		
		//释放冻结库存
		InventoryFreezeRootJson freezeRoot = new InventoryFreezeRootJson();
		freezeRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
		freezeRoot.setMerchantName(transferOrderModel.getMerchantName());
		freezeRoot.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
		freezeRoot.setDepotName(outDepotInfoMongo.getName());
		freezeRoot.setOrderNo(trOutDepotModel.getCode());
		freezeRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		freezeRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		freezeRoot.setSourceDate(TimeUtils.formatFullTime());
		freezeRoot.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
		freezeRoot.setBusinessNo(transferOrderModel.getCode());
		freezeRoot.setGoodsList(freezeGoodList);
		JSONObject jsonFree = JSONObject.fromObject(freezeRoot);
		
		//拼装扣减库存接口参数
		InvetAddOrSubtractRootJson adSubRootJson = new InvetAddOrSubtractRootJson();
		adSubRootJson.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
		adSubRootJson.setMerchantName(transferOrderModel.getMerchantName());
		adSubRootJson.setTopidealCode(transferOrderModel.getTopidealCode());
		adSubRootJson.setDepotType(outDepotInfoMongo.getType());
		adSubRootJson.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
		adSubRootJson.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
		adSubRootJson.setDepotCode(outDepotInfoMongo.getCode());
		adSubRootJson.setDepotName(outDepotInfoMongo.getName());
		adSubRootJson.setOrderNo(trOutDepotModel.getCode());
		adSubRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		adSubRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		adSubRootJson.setSourceDate(TimeUtils.formatFullTime());			  
		adSubRootJson.setOwnMonth(yearsMonths);
		adSubRootJson.setDivergenceDate(transferStr);
		adSubRootJson.setBusinessNo(transferOrderModel.getCode());
		adSubRootJson.setBuId(String.valueOf(transferOrderModel.getBuId()));
		adSubRootJson.setBuName(transferOrderModel.getBuName());
		adSubRootJson.setGoodsList(addOrSubtractGoodsList);
		//回调参数
		adSubRootJson.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
		adSubRootJson.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
		adSubRootJson.setCustomParam(new HashMap<String, Object>());
		//减库存
		JSONObject jsonRequest = JSONObject.fromObject(adSubRootJson);
		
		*//************************ 以出定入 ************************************//*
	    if(isRookie.equals("0")){//非菜鸟
			
			Boolean flag= false;
			//调拨入仓仓库是 在对应商家下的“以出定入”标识为“是”的, 如果调出库仓库为香港仓时，不走以出定入
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

			if(flag) {
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())) {
					for (EpassReturnMessagePushGoodsListJson goodsListJson : returnRoot.getGoodsList()) {
						String batchNo = goodsListJson.getBatchNo();
						String productionDate = goodsListJson.getProductionDate();
						String overdueDate = goodsListJson.getOverdueDate();
						if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
							LOGGER.error("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
							throw new RuntimeException("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
						}

					}
				}

				// 根据调拨单查询调拨入库单
				TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
				transferInDepotModel.setTransferOrderCode(orderCode);
				transferInDepotModel = transferInDepotDao.getByModel(transferInDepotModel);			
				if (transferInDepotModel!=null) {
					LOGGER.info("调拨入库单已经存在(调拨已出定入流程),订单号orderCode"+orderCode);
					throw new RuntimeException("调拨入库单已经存在(调拨已出定入流程),订单号orderCode"+orderCode);
				}
				// 新增调拨入库单
				TransferInDepotModel trInDepotModel = new TransferInDepotModel();
				// 向调拨单和调拨入库单中插入数据
				trInDepotModel.setTransferOrderId(transferOrderModel.getId());//调拨订单ID
				trInDepotModel.setTransferOrderCode(transferOrderModel.getCode());//调拨订单编号
				trInDepotModel.setMerchantId(transferOrderModel.getMerchantId());//商家ID
				trInDepotModel.setMerchantName(transferOrderModel.getMerchantName());//商家名称
				trInDepotModel.setContractNo(transferOrderModel.getContractNo());//合同号
				trInDepotModel.setInDepotId(transferOrderModel.getInDepotId());//调入仓库id
				trInDepotModel.setInDepotName(transferOrderModel.getInDepotName());//调入仓库名称
				trInDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());//调出仓库id
				trInDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());//调出仓库名称
				trInDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
				trInDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户名称
				trInDepotModel.setTransferDate(transferDate);//调入时间
				trInDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028);//RKZ("028","入库中"),//状态(暂定011待入仓,012 已入仓 028.入库中)
//				trInDepotModel.setCode(CodeGeneratorUtil.getNo("DBRK"));//调拨单号(跨境宝推来的)
				trInDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));//调拨单号(跨境宝推来的)
				trInDepotModel.setSource(DERP_ORDER.TRANSFERINDEPOT_SOURCE_2);//单据来源 1-调拨入自动生成 2-接口回推生成
				trInDepotModel.setLbxNo(transferOrderModel.getLbxNo());
				trInDepotModel.setBuName(transferOrderModel.getBuName());
				trInDepotModel.setBuId(transferOrderModel.getBuId());
				trInDepotModel.setCreateName("接口回传");
				// 生成调拨入库表
				transferInDepotDao.save(trInDepotModel);
				List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//加库存商品
				
				// 以出定入 新增调拨入库表体
				for(EpassReturnMessagePushGoodsListJson goods : goodsList) {
					TransferOrderItemModel item = goodMap.get(goods.getGoodsId());
					if (item==null) {
						LOGGER.info("调拨商品不存在(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
						throw new RuntimeException("调拨商品不存在(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
					}
					//"以出定入"以报文中商品的标准条码关联去判断是否跟调拨单的调入商品信息一致，取调拨订单中调入商品货号
					Map<String, Object> goodsParam = new HashMap<>();
					goodsParam.put("merchandiseId", goods.getGoodsId());
					MerchandiseInfoMogo merchandise = merchandiseInfoMogoDao.findOne(goodsParam);
					if (merchandise == null) {
						LOGGER.info("调拨商品不存在(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
						throw new RuntimeException("调拨商品不存在(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
					}

					TransferOrderItemModel itemModel = new TransferOrderItemModel();
					itemModel.setTransferOrderId(transferOrderModel.getId());
					itemModel.setOutGoodsId(goods.getGoodsId());
					TransferOrderItemModel orderItem = transferOrderItemDao.searchByModel(itemModel);

					if (orderItem == null) {
						LOGGER.info("调拨商品不存在(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
						throw new RuntimeException("调拨商品不存在(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
					}

					if (!orderItem.getInCommbarcode().equals(merchandise.getCommbarcode())) {
						LOGGER.info("调拨商品与调拨订单的调入商品不一致(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
						throw new RuntimeException("调拨商品与调拨订单的调入商品不一致(调拨已出定入流程),订单号orderCode"+orderCode+"货号："+goods.getGoodsNo());
					}


					//调入商品
					TransferInDepotItemModel transferInDepotItemModel =new TransferInDepotItemModel();
					transferInDepotItemModel.setTransferDepotId(trInDepotModel.getId());// 调拨入库ID					
					transferInDepotItemModel.setInGoodsId(orderItem.getInGoodsId());//调入商品id
					transferInDepotItemModel.setTransferNum(goods.getNum());//调拨数量
					transferInDepotItemModel.setInGoodsCode(orderItem.getInGoodsCode());
					transferInDepotItemModel.setInGoodsNo(orderItem.getInGoodsNo());
					transferInDepotItemModel.setInGoodsName(orderItem.getInGoodsName());
					transferInDepotItemModel.setInCommbarcode(orderItem.getInCommbarcode());
					transferInDepotItemModel.setTransferBatchNo(goods.getBatchNo());
					transferInDepotItemModel.setBarcode(orderItem.getInBarcode());
					String productionDate = goods.getProductionDate();
					if (StringUtils.isNotBlank(productionDate)) {
						if (productionDate.length()==10) {	
							transferInDepotItemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
		                }else {
		                	transferInDepotItemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
		    			}
					}
					String overdueDate = goods.getOverdueDate();
					if (StringUtils.isNotBlank(overdueDate)) {
						if (overdueDate.length()==10) {	
							transferInDepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
		                }else {
		                	transferInDepotItemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
		    			}
					}	
					
					// 新增调拨入库单商品
					transferInDepotItemDao.save(transferInDepotItemModel);	
					
					//计算是否过期 字符串 （0 是 1 否）
					String isExpire = DERP.ISEXPIRE_1;
					if(transferInDepotItemModel.getOverdueDate()!=null){
						isExpire = TimeUtils.isNotIsExpireByDate(transferInDepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
					}
					
					//拼装加库存商品
					InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
					good.setGoodsId(String.valueOf(transferInDepotItemModel.getInGoodsId()));
					good.setGoodsName(transferInDepotItemModel.getInGoodsName());
					good.setGoodsNo(transferInDepotItemModel.getInGoodsNo());
					good.setBarcode(transferInDepotItemModel.getBarcode());
					good.setBatchNo(transferInDepotItemModel.getTransferBatchNo());
					good.setProductionDate(TimeUtils.formatDay(transferInDepotItemModel.getProductionDate()));
					good.setOverdueDate(TimeUtils.formatDay(transferInDepotItemModel.getOverdueDate()));
					good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
					good.setIsExpire(isExpire);//是否过期（0 是 1 否）
					good.setNum(transferInDepotItemModel.getTransferNum());
					//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
					good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
					subGoodsList.add(good);
				}	
				
				//拼装加库存接口参数
				InvetAddOrSubtractRootJson subRequest = new InvetAddOrSubtractRootJson();
				subRequest.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
				subRequest.setMerchantName(transferOrderModel.getMerchantName());
				subRequest.setTopidealCode(transferOrderModel.getTopidealCode());
				subRequest.setDepotType(inDepotInfoMongo.getType());
				subRequest.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
				subRequest.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
				subRequest.setDepotCode(inDepotInfoMongo.getCode());
				subRequest.setDepotName(inDepotInfoMongo.getName());
				subRequest.setOrderNo(trInDepotModel.getCode());
				subRequest.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
				subRequest.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010); //DBRK("0010","调拨入库"),
				subRequest.setSourceDate(TimeUtils.formatFullTime());
				subRequest.setOwnMonth(yearsMonths);
				subRequest.setDivergenceDate(transferStr);
				subRequest.setBusinessNo(transferOrderModel.getCode());
				subRequest.setGoodsList(subGoodsList);
				subRequest.setBuId(String.valueOf(transferOrderModel.getBuId()));
				subRequest.setBuName(transferOrderModel.getBuName());
				//回调参数
				subRequest.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
				subRequest.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
				subRequest.setCustomParam(new HashMap<String, Object>());
				
				//加库存
				JSONObject subjson = JSONObject.fromObject(subRequest);
				rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

			}// 已入定出结束
		}
	    
	    // 把释放冻结放到最下方防止已出定入出现异常接口报错
		rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
	    // 把减库存放到最下方防止已出定入出现异常接口报错
		rocketMQProducer.send(jsonRequest.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		return true;
	}

}
*/