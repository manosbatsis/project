/*package com.topideal.service.api.sale.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.*;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.CodeGeneratorUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.entity.vo.sale.SaleOutDepotItemModel;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.json.api.v2_4.SaleReturnMessagePushGoodsListJson;
import com.topideal.json.api.v2_4.SaleReturnMessagePushRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.service.api.sale.XSReturnMessagePushService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

*//**
 * 退运单信息推送
 *//*
@Service
public class XSReturnMessagePushServiceImpl implements XSReturnMessagePushService {
	 打印日志 
	private static final Logger LOGGER = LoggerFactory.getLogger(XSReturnMessagePushServiceImpl.class);
	// 销售订单
	@Autowired
	private SaleOrderDao saleOrderDao;
	// 销售出库清单
	@Autowired
	private SaleOutDepotDao saleOutDepotDao;
	// 销售出库清单对应的商品
	@Autowired
	private SaleOutDepotItemDao saleOutDepotItemDao;
	// 销售订单表体
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;
	// 销售出库分析表
	@Autowired
	private SaleAnalysisDao saleAnalysisDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	@Override
	@SystemServiceLog(point = "12203120700", model = "退运单信息推送接口",keyword="orderCode")
	public boolean saveReturnMessagePushInfo(String json,String keys,String topics,String tags) throws Exception {
		
		
		*//**
		 * 对于退运信息 目前只对状态是70的操作
		 *//*
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",SaleReturnMessagePushGoodsListJson.class);
		// JSON对象转实体
		SaleReturnMessagePushRootJson rootJson = (SaleReturnMessagePushRootJson) JSONObject.toBean(jsonData, SaleReturnMessagePushRootJson.class, classMap);
		
		String orderCode = rootJson.getOrderCode();// 订单号
		String status = rootJson.getStatus();// 10：制单30：提交70：成功90：作废 必填
		String transfer = rootJson.getTransferDate();// 调出日期(发货日期) 必填
		//获取是否菜鸟字段(1-是，0-否)
		String isRookie = rootJson.getIsRookie();
		
		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(3);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("退运单信息推送外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
			throw new RuntimeException("退运单信息推送外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
		}

		if (!"70".equals(status)) {
			LOGGER.error("退运信息只接收状态是70 的单orderCode:" + orderCode);
			throw new RuntimeException("退运信息只接收状态是70 的单orderCode:" + orderCode);
		}
		
		//获取销售订单
		SaleOrderModel saleOrderModel = new SaleOrderModel();
		if ("0".equals(isRookie)) {// 销售订单 (非菜鸟)
			saleOrderModel.setCode(orderCode);
			saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		}else if("1".equals(isRookie)){// 销售订单 (菜鸟)
			saleOrderModel.setLbxNo(orderCode);
			saleOrderModel = saleOrderDao.searchByModel(saleOrderModel);
		}else{
			LOGGER.error("isRookie有误,isRookie:" + isRookie);
			throw new RuntimeException("isRookie有误,isRookie:" + isRookie);
		}
		if (saleOrderModel == null) {
			LOGGER.error("没有查询到对应的订单,订单号:" + orderCode);
			throw new RuntimeException("没有查询到对应的订单,订单号:" + orderCode);
		}
		if(null == saleOrderModel.getBuId()){
			LOGGER.error("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
		}

		// 根据销售订单 出库仓库id 查询仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleOrderModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		
		if (mongo==null ) {
			LOGGER.error("根据销售订单的仓库id没有查询到仓库,销售订单code:" + saleOrderModel.getCode());
			throw new RuntimeException("根据销售订单的仓库id没有查询到仓库,销售订单code:" + saleOrderModel.getCode());
		}
		// 批次效期强校验：1-是 0-否
		if ("1".equals(mongo.getBatchValidation())) {
			for (SaleReturnMessagePushGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
				}
				
			}
		}		
		
		
		
		
		Timestamp transferDate = null;// 出入库时间		
		if (transfer.length() == 10) {
			transferDate = Timestamp.valueOf(transfer + " 00:00:00"); // 出入库时间			
		} else {
			transferDate = Timestamp.valueOf(transfer);// 出入库时间			
		}
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(transferDate);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(transferDate);//归属月份
		
		SaleOutDepotModel saleOutDepotModel = new SaleOutDepotModel();
		if ("0".equals(isRookie)) {// 销售订单 (非菜鸟)
			// 根据销售出库清单编码code 和 sale_order_code 销售出库清单编码查询销售出库清单
			saleOutDepotModel.setSaleOrderCode(orderCode);
			saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		}else{// 来自销售 (菜鸟)
			// 根据销售出库清单编码LBX号 销售出库清单编码查询销售出库清单
			saleOutDepotModel.setLbxNo(orderCode);
			saleOutDepotModel = saleOutDepotDao.searchByModel(saleOutDepotModel);
		}
		if(saleOutDepotModel != null){
			LOGGER.error("销售出库清单已经存在,订单号:" + orderCode);
			throw new RuntimeException("销售出库清单已经存在,订单号:" + orderCode);
		}
		// 新增 销售出库清单表体
		SaleOutDepotModel sOutDepotModel = new SaleOutDepotModel();
		sOutDepotModel.setSaleOrderId(saleOrderModel.getId());// 销售订单id
		sOutDepotModel.setMerchantId(saleOrderModel.getMerchantId());// 商家ID
		sOutDepotModel.setPoNo(saleOrderModel.getPoNo());// PO号
		sOutDepotModel.setOutDepotId(saleOrderModel.getOutDepotId());// 调出仓库id
		sOutDepotModel.setOutDepotName(saleOrderModel.getOutDepotName());// 调出仓库名称
		sOutDepotModel.setCustomerId(saleOrderModel.getCustomerId());// 客户id(供应商)
		sOutDepotModel.setCustomerName(saleOrderModel.getCustomerName());// 客户名称
		sOutDepotModel.setSaleType(saleOrderModel.getBusinessModel());// 销售类型 1购销  2代销'
		sOutDepotModel.setDeliverDate(transferDate);// 发货日期
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);// '状态 017,待出库 ,018,已出库,027:出库中
//		sOutDepotModel.setCode(CodeGeneratorUtil.getNo("XSCK"));// 自生成
		sOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));// 自生成
		sOutDepotModel.setMerchantName(saleOrderModel.getMerchantName());// 商家名称
		sOutDepotModel.setSaleOrderCode(saleOrderModel.getCode());// 销售订单编号
		sOutDepotModel.setLbxNo(saleOrderModel.getLbxNo());// LBX单号
		sOutDepotModel.setReturnStatus(status);// '退运状态 70：成功90：作废'
		// 事业部
		sOutDepotModel.setBuId(saleOrderModel.getBuId());
		sOutDepotModel.setBuName(saleOrderModel.getBuName());
		// 新增
		saleOutDepotDao.save(sOutDepotModel);
		List<SaleOutDepotItemModel> itemList = new ArrayList<SaleOutDepotItemModel>();
		Map<Long,Integer> totalOutNum = new HashMap<Long,Integer>();
		for (SaleReturnMessagePushGoodsListJson goodsListJson : rootJson.getGoodsList()) {
			// 获取销售订单商品信息
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(saleOrderModel.getId());
			saleOrderItemModel.setGoodsId(goodsListJson.getGoodsId());
			saleOrderItemModel = saleOrderItemDao.searchByModel(saleOrderItemModel);
			if (saleOrderItemModel==null) {
				LOGGER.error("根据销售订单id和商品id,在销售订单表体中没有找到商品: 商品货号" + goodsListJson.getGoodsNo());
				throw new RuntimeException("根据销售订单id和商品id,在销售订单表体中没有找到商品: 商品货号" + goodsListJson.getGoodsNo());
			}
			// 新增销售出库清单表体
			SaleOutDepotItemModel itemModel = new SaleOutDepotItemModel();
			itemModel.setSaleOutDepotId(sOutDepotModel.getId());// 销售出库ID
			itemModel.setGoodsId(goodsListJson.getGoodsId());// 商品id
			itemModel.setGoodsCode(goodsListJson.getGoodsCode());// 商品编码
			itemModel.setGoodsName(goodsListJson.getGoodsName());// 商品名称
			itemModel.setGoodsNo(goodsListJson.getGoodsNo());// 商品货号
			itemModel.setTransferNum(goodsListJson.getNum());// 调拨数量
			itemModel.setTransferBatchNo(goodsListJson.getBatchNo());// 批次
			itemModel.setBarcode(goodsListJson.getBarcode());//条形码
			itemModel.setCommbarcode(goodsListJson.getCommbarcode());// 标准条码
			itemModel.setSaleNum(saleOrderItemModel.getNum());
			
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
			
			
			
			// 新增
			saleOutDepotItemDao.save(itemModel);
			itemList.add(itemModel);
			Integer totalOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(saleOrderModel.getId(),itemModel.getGoodsId());
			totalOutNum.put(itemModel.getId(), totalOutDepotNum);
			//生成差异分析信息
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
			saleAnalysisModel = saleAnalysisDao.searchByModel(saleAnalysisModel);
			if(saleAnalysisModel != null){
				int transferNum = itemModel.getTransferNum();
				if(saleAnalysisModel.getOutDepotNum() != null){
					transferNum = transferNum+=saleAnalysisModel.getOutDepotNum();
				}
				saleAnalysisModel.setIsEnd("0");
				saleAnalysisModel.setSaleOutDepotCode(sOutDepotModel.getCode());
				saleAnalysisModel.setSaleOutDepotId(sOutDepotModel.getId());
				saleAnalysisModel.setOutDepotNum(transferNum);
				saleAnalysisModel.setSurplus(saleOrderItemModel.getNum()-transferNum);
				saleAnalysisDao.modify(saleAnalysisModel);
			}else{
				saleAnalysisModel = new SaleAnalysisModel();
				saleAnalysisModel.setCreateDate(TimeUtils.getNow());
				saleAnalysisModel.setCustomerId(saleOrderModel.getCustomerId());
				saleAnalysisModel.setCustomerName(saleOrderModel.getCustomerName());
				saleAnalysisModel.setEndDate(TimeUtils.getNow());
				saleAnalysisModel.setGoodsId(itemModel.getGoodsId());
				saleAnalysisModel.setGoodsName(itemModel.getGoodsName());
				saleAnalysisModel.setGoodsNo(itemModel.getGoodsNo());
				saleAnalysisModel.setIsEnd("0");
				saleAnalysisModel.setOrderCode(saleOrderModel.getCode());
				saleAnalysisModel.setOrderId(saleOrderModel.getId());
				saleAnalysisModel.setOutDepotNum(itemModel.getTransferNum());
				saleAnalysisModel.setSaleNum(saleOrderItemModel.getNum());
				saleAnalysisModel.setSaleOutDepotCode(sOutDepotModel.getCode());
				saleAnalysisModel.setSaleOutDepotId(sOutDepotModel.getId());
				saleAnalysisModel.setSurplus(saleOrderItemModel.getNum() - itemModel.getTransferNum());
				saleAnalysisModel.setMerchantId(saleOrderModel.getMerchantId());
				saleAnalysisModel.setMerchantName(saleOrderModel.getMerchantName());
				saleAnalysisModel.setSaleType(saleOrderModel.getBusinessModel());//销售类型 1购销 2代销
				saleAnalysisDao.save(saleAnalysisModel);
			}
		}
		//释放并减少冻结量
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String now = sdf.format(new Date());
		InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
		inventoryFreezeRootJson.setMerchantId(saleOrderModel.getMerchantId().toString());
		inventoryFreezeRootJson.setMerchantName(saleOrderModel.getMerchantName());
		inventoryFreezeRootJson.setDepotId(saleOrderModel.getOutDepotId().toString());
		inventoryFreezeRootJson.setDepotName(saleOrderModel.getOutDepotName());
		inventoryFreezeRootJson.setOrderNo(sOutDepotModel.getCode());// 销售出库清单的 code 作为释放冻结的单据来源
		inventoryFreezeRootJson.setBusinessNo(saleOrderModel.getCode());
		inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		inventoryFreezeRootJson.setSourceDate(now);
		inventoryFreezeRootJson.setOperateType("1");
		List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for (SaleOutDepotItemModel item : itemList) {
			InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
			inventoryFreezeGoodsListJson.setGoodsId(item.getGoodsId().toString());
			inventoryFreezeGoodsListJson.setGoodsName(item.getGoodsName());
			inventoryFreezeGoodsListJson.setGoodsNo(item.getGoodsNo());
			inventoryFreezeGoodsListJson.setDivergenceDate(transferStr);
			
			 * 释放库存逻辑：
			 * A、累计出库量（含当前出库量）<=销售订单量，释放冻结量=当前销售出库单量。
			 * B、累计出库量（含当前出库量）>销售订单量，累计出库量（不包含当前出库量）<销售订单量，
			 *   释放冻结量=当前销售订单量-累计销售出库量（不含当前出库单量）。
			 * C、累计出库量（含当前出库量）>销售订单量，累计出库量（不包含当前出库量）>=销售订单量,不用释放
			 
			//获取销售订单的商品的累计出库数量
			Integer totalOutDepotNum = totalOutNum.get(item.getId());
			// 获取销售订单商品信息
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(saleOrderModel.getId());
			saleOrderItemModel.setGoodsId(item.getGoodsId());
			saleOrderItemModel = saleOrderItemDao.searchByModel(saleOrderItemModel);
			if(totalOutDepotNum<=saleOrderItemModel.getNum()){//A
				inventoryFreezeGoodsListJson.setNum(item.getTransferNum());
			}else if(totalOutDepotNum > saleOrderItemModel.getNum()){
				if(totalOutDepotNum-item.getTransferNum() < saleOrderItemModel.getNum()){//B
					inventoryFreezeGoodsListJson.setNum(saleOrderItemModel.getNum() - (totalOutDepotNum-item.getTransferNum()));
				}
			}
			inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
		}
		inventoryFreezeRootJson.setGoodsList(inventoryFreezeGoodsListJsonList);
		rocketMQProducer.send(JSONObject.fromObject(inventoryFreezeRootJson).toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		//扣减销售出库库存量		
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setMerchantId(sOutDepotModel.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(sOutDepotModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(saleOrderModel.getTopidealCode());
		
		invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(mongo.getName());
		invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(mongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(sOutDepotModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(saleOrderModel.getCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		invetAddOrSubtractRootJson.setSourceDate(now);// 单据日期
		invetAddOrSubtractRootJson.setDivergenceDate(transferStr);// 业务单据的出入库或审核时间
		// 事业部
		invetAddOrSubtractRootJson.setBuId(String.valueOf(sOutDepotModel.getBuId()));
		invetAddOrSubtractRootJson.setBuName(sOutDepotModel.getBuName());		
		
		// 获取当前年月
				  
		invetAddOrSubtractRootJson.setOwnMonth(yearsMonths);
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		for (SaleOutDepotItemModel item : itemList) {
			InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
			invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
			invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
			invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
			invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
			invetAddOrSubtractGoodsListJson.setType("0");
			invetAddOrSubtractGoodsListJson.setNum(item.getTransferNum());
			invetAddOrSubtractGoodsListJson.setOperateType("0");// 字符串 0 调减 1调增
			invetAddOrSubtractGoodsListJson.setBatchNo(item.getTransferBatchNo());
			if (item.getProductionDate()!=null) {
				Date productionDateTimestamp  = item.getProductionDate();
				invetAddOrSubtractGoodsListJson.setProductionDate(sdf3.format(productionDateTimestamp));
			}
			if (item.getOverdueDate()!=null) {
				Date overdueDateTimestamp = item.getOverdueDate();
				invetAddOrSubtractGoodsListJson.setOverdueDate(sdf3.format(overdueDateTimestamp));	
				String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
				invetAddOrSubtractGoodsListJson.setIsExpire(isExpire);//是否过期（0是 1否）				
			}else{
				invetAddOrSubtractGoodsListJson.setIsExpire("1");
			}
			invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
		}
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.RETURN_MESSAGE_PUSH_BACK_2.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.RETURN_MESSAGE_PUSH_BACK_2.getTopic());//回调主题		
		customParam.put("code", saleOrderModel.getCode());// 销售订单内部单号
		invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
		
		rocketMQProducer.send(JSONObject.fromObject(invetAddOrSubtractRootJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		
		//判断是否能自动完结(不能影响其他流程的运转)
		boolean flag = true;//是否完结    true：完结    false：未完结
		try{
			//获取销售订单所有的商品信息
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(saleOrderModel.getId());
			List<SaleOrderItemModel> sItemList = saleOrderItemDao.list(saleOrderItemModel);
			//获取销售订单对应的所有出库单
			SaleOutDepotModel saleOutDepot = new SaleOutDepotModel();
			saleOutDepot.setSaleOrderId(saleOrderModel.getId());
			List<SaleOutDepotModel> saleOutDepotList = saleOutDepotDao.list(saleOutDepot);
			//遍历，计算销售订单中所有商品与对应的出库单（1对多，数量累加）出库的数量一致
			for(SaleOrderItemModel item:sItemList){
				Integer outNum = 0;//某商品出库总量
				for(SaleOutDepotModel saleOutDepot1:saleOutDepotList){
					SaleOutDepotItemModel saleOutDepotItem = new SaleOutDepotItemModel();
					saleOutDepotItem.setSaleOutDepotId(saleOutDepot1.getId());
					saleOutDepotItem.setGoodsId(item.getGoodsId());
					List<SaleOutDepotItemModel> saleOutDepotItemList = saleOutDepotItemDao.list(saleOutDepotItem);
					for(SaleOutDepotItemModel sodItem:saleOutDepotItemList){
						outNum +=sodItem.getTransferNum();
					}
				}
				if(item.getNum() != outNum){
					flag = false;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			flag = false;
		}
		// 修改销售订单 为已出库saleOrderModel
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setId(saleOrderModel.getId());
		if(flag){
			sModel.setState(StatusEnum.YWJ.getKey());//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
			//修改差异分析表状态
			SaleAnalysisModel saleAnalysisModel = new SaleAnalysisModel();
			saleAnalysisModel.setOrderId(saleOrderModel.getId());
			List<SaleAnalysisModel> saleAnalysisList = saleAnalysisDao.list(saleAnalysisModel);
			for(SaleAnalysisModel saleAnalysis :saleAnalysisList){
				saleAnalysis.setIsEnd("1");
				saleAnalysis.setEndDate(TimeUtils.getNow());
				saleAnalysisDao.modify(saleAnalysis);
			}
		}else{
			sModel.setState(StatusEnum.YCK.getKey());//订单状态:001:待审核,002:审核中,003:已审核,006:已删除,007:已完结,018:已出库
		}
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setState(DERP_ORDER.SALEORDER_STATE_027);
		sModel.setId(saleOrderModel.getId());
		saleOrderDao.modify(sModel);
		return true;
	}
}
*/