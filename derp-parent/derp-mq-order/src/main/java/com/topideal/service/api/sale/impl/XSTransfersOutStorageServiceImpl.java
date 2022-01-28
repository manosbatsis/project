/*package com.topideal.service.api.sale.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.CodeGeneratorUtil;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.sale.SaleOrderDao;
import com.topideal.dao.sale.SaleOrderItemDao;
import com.topideal.dao.sale.SaleOutDepotDao;
import com.topideal.dao.sale.SaleOutDepotItemDao;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.sale.SaleOrderItemModel;
import com.topideal.entity.vo.sale.SaleOrderModel;
import com.topideal.entity.vo.sale.SaleOutDepotItemModel;
import com.topideal.entity.vo.sale.SaleOutDepotModel;
import com.topideal.json.api.v2_6.SaleTransfersOutStorageGoodsListJson;
import com.topideal.json.api.v2_6.SaleTransfersOutStorageRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.json.inventory.j02.InventoryFreezeGoodsListJson;
import com.topideal.json.inventory.j02.InventoryFreezeRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.MerchantBuRelMongo;
import com.topideal.service.api.sale.XSTransfersOutStorageService;

import net.sf.json.JSONObject;
*//**
 * 调拨出库接口
 *//*
@Service
public class XSTransfersOutStorageServiceImpl implements XSTransfersOutStorageService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XSTransfersOutStorageServiceImpl.class);
	
	@Autowired 
	private SaleOrderDao  saleOrderDao;// 销售订单
	@Autowired 
	private SaleOutDepotDao saleOutDepotDao;// 销售出库表
	@Autowired 
	private SaleOutDepotItemDao saleOutDepotItemDao;//销售出库订单商品
	@Autowired
	private SaleOrderItemDao saleOrderItemDao;	// 销售订单表体
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;//仓库
	// 电商订单外部单号来源
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	// 公司事业部信息
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	
	@Override
	@SystemServiceLog(point="12203120900",model="调拨出库回推接口",keyword="orderCode")
	public Map<String,List<String>> saveTransfersOutStorageInfo(String json,String keys,String topics,String tags) throws Exception {
		//用于存储库存解冻集合
		List<String> invetFreezeList = new ArrayList<String>();
		//用于存储库存扣减集合
		List<String> invetAddOrSubList = new ArrayList<String>();
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",SaleTransfersOutStorageGoodsListJson.class);
		// JSON对象转实体
		SaleTransfersOutStorageRootJson rootJson = (SaleTransfersOutStorageRootJson) JSONObject.toBean(jsonData, SaleTransfersOutStorageRootJson.class, classMap);
		String orderCode = rootJson.getOrderCode();// 订单号	
		String outExternalCode = rootJson.getCode();// 调拨单号(入库单和出库单)外部单号
		String transfer = rootJson.getTransferDate();
		Timestamp transferDate=null;//调拨时间
		if (StringUtils.isNotBlank(transfer)) {			
			if (transfer.length()==10) {
				transferDate=Timestamp.valueOf(transfer+" 00:00:00");	//调拨时间
			}else {
				transferDate=Timestamp.valueOf(transfer);//调拨时间
			}
		}
		try {
			OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
			orderExternalCodeModel.setExternalCode(orderCode);
			orderExternalCodeModel.setOrderSource(3);	// 订单来源  1:电商订单, 2:装载交运 3:销售出库
			orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("调拨出库外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
			throw new RuntimeException("调拨出库外部单号来源表已经存在 单号：" + orderCode + "  保存失败");
		}
		
		//格式化日期
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(transferDate);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(transferDate);//归属月份
		//获取是否菜鸟字段(1-是，0-否)
		String isRookie = rootJson.getIsRookie();
		//销售(非菜鸟)
		SaleOrderModel saleOrderModel= new SaleOrderModel();
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
			LOGGER.error("没有查询到对应的订单,订单号:"+orderCode);
			throw new RuntimeException("没有查询到对应的订单,订单号:"+orderCode);
		}
		if (DERP_ORDER.SALEORDER_STATE_001.equals(saleOrderModel.getState())) {
			LOGGER.error("订单状态为“待审核”,订单编号" + saleOrderModel.getCode());
			throw new RuntimeException("订单状态为“待审核”,订单编号" + saleOrderModel.getCode());
		}
        if(null == saleOrderModel.getBuId()){
        	LOGGER.error("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
			throw new RuntimeException("销售订单编号" + saleOrderModel.getCode()+"事业部的值为空");
        }    
		//获取仓库信息
		Map<String, Object> depotInfo_params = new HashMap<String, Object>();
		depotInfo_params.put("depotId", saleOrderModel.getOutDepotId());// 根据仓库id
		DepotInfoMongo mongo = depotInfoMongoDao.findOne(depotInfo_params);
		// 批次效期强校验：1-是 0-否
		if ("1".equals(mongo.getBatchValidation())) {
			for (SaleTransfersOutStorageGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(mongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
				}
				
			}
		}
		// 根据销售出库清单编码code 和  sale_order_code 销售出库清单编码查询销售出库清单
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
		// 新增向销售出库
		SaleOutDepotModel sOutDepotModel =new SaleOutDepotModel();		
		sOutDepotModel.setSaleOrderId(saleOrderModel.getId());//销售订单id
		sOutDepotModel.setMerchantId(saleOrderModel.getMerchantId());//商家ID
		sOutDepotModel.setPoNo(saleOrderModel.getPoNo());//PO号
		sOutDepotModel.setOutDepotId(saleOrderModel.getOutDepotId());//调出仓库id
		sOutDepotModel.setOutDepotName(saleOrderModel.getOutDepotName());	//调出仓库名称		
		sOutDepotModel.setCustomerId(saleOrderModel.getCustomerId());//客户id(供应商)
		sOutDepotModel.setCustomerName(saleOrderModel.getCustomerName());//客户名称
		sOutDepotModel.setSaleType(saleOrderModel.getBusinessModel());//销售类型 1购销 2代销'		
		sOutDepotModel.setDeliverDate(transferDate);//发货时间
		sOutDepotModel.setStatus(DERP_ORDER.SALEOUTDEPOT_STATUS_027);//017,待出库 ,018,已出库,027:出库中
//		sOutDepotModel.setCode(CodeGeneratorUtil.getNo("XSCK"));// 出库清单编号 
		sOutDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_XSCK));// 出库清单编号 
		sOutDepotModel.setMerchantName(saleOrderModel.getMerchantName());//商家名称		
		sOutDepotModel.setSaleOrderCode(saleOrderModel.getCode());//销售订单编号
		sOutDepotModel.setLbxNo(saleOrderModel.getLbxNo());//LBX单号
		sOutDepotModel.setOutExternalCode(outExternalCode);
		// 事业部
		sOutDepotModel.setBuId(saleOrderModel.getBuId());
		sOutDepotModel.setBuName(saleOrderModel.getBuName());
		// 新增
		saleOutDepotDao.save(sOutDepotModel);			
		List<SaleOutDepotItemModel> itemList = new ArrayList<SaleOutDepotItemModel>();
		Map<Long,Integer> totalOutNum = new HashMap<Long,Integer>();
		for (SaleTransfersOutStorageGoodsListJson goodsListJson : rootJson.getGoodsList()) {
			//获取销售订单商品信息
			SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
			saleOrderItemModel.setOrderId(saleOrderModel.getId());
			saleOrderItemModel.setGoodsId(goodsListJson.getGoodsId());
			saleOrderItemModel = saleOrderItemDao.searchByModel(saleOrderItemModel);
			if(saleOrderItemModel == null){
				LOGGER.error("根据接口商品,没有查到销售订单商品,订单号:" + orderCode+",商品id:"+goodsListJson.getGoodsId());
				throw new RuntimeException("根据接口商品,没有查到销售订单商品,订单号:" + orderCode+",商品id:"+goodsListJson.getGoodsId());
			}
			// 新增销售出库表体
			SaleOutDepotItemModel itemModel = new SaleOutDepotItemModel();
			itemModel.setSaleOutDepotId(sOutDepotModel.getId());//销售出库ID
			// 根据商品货号查询商品
			itemModel.setGoodsId(goodsListJson.getGoodsId());//商品id
			itemModel.setGoodsCode(goodsListJson.getGoodsCode());//商品编码
			itemModel.setGoodsName(goodsListJson.getGoodsName());//商品名称
			itemModel.setGoodsNo(goodsListJson.getGoodsNo());//商品货号
			itemModel.setTransferNum(goodsListJson.getSalesNum());//调拨数量（正常品数量）
			itemModel.setWornNum(goodsListJson.getWornNum());//坏品数量
			itemModel.setTransferBatchNo(goodsListJson.getBatchNo());//调拨批次
			itemModel.setBarcode(goodsListJson.getBarcode());//条形码
			itemModel.setCommbarcode(goodsListJson.getCommbarcode()); // 标准条码
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
			// 新增销售出库表体
			saleOutDepotItemDao.save(itemModel);
			itemList.add(itemModel);
			Integer totalOutDepotNum = saleOutDepotDao.getTotalNumByOrderGoods(saleOrderModel.getId(),itemModel.getGoodsId());
			totalOutNum.put(itemModel.getId(), totalOutDepotNum);
		}
		SaleOrderItemModel saleOrderItemModel = new SaleOrderItemModel();
		saleOrderItemModel.setOrderId(saleOrderModel.getId());
		List<SaleOrderItemModel> saleOrderItemModels = saleOrderItemDao.list(saleOrderItemModel);
		//释放并减少冻结量
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		InventoryFreezeRootJson inventoryFreezeRootJson = new InventoryFreezeRootJson();
		inventoryFreezeRootJson.setMerchantId(saleOrderModel.getMerchantId().toString());
		inventoryFreezeRootJson.setMerchantName(saleOrderModel.getMerchantName());
		inventoryFreezeRootJson.setDepotId(saleOrderModel.getOutDepotId().toString());
		inventoryFreezeRootJson.setDepotName(saleOrderModel.getOutDepotName());
		inventoryFreezeRootJson.setOrderNo(sOutDepotModel.getCode());
		inventoryFreezeRootJson.setBusinessNo(saleOrderModel.getCode());
		inventoryFreezeRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		inventoryFreezeRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		inventoryFreezeRootJson.setSourceDate(now);
		inventoryFreezeRootJson.setOperateType("1");
		List<InventoryFreezeGoodsListJson> inventoryFreezeGoodsListJsonList = new ArrayList<InventoryFreezeGoodsListJson>();
		for (SaleOrderItemModel itemModel : saleOrderItemModels) {
			InventoryFreezeGoodsListJson inventoryFreezeGoodsListJson = new InventoryFreezeGoodsListJson();
			inventoryFreezeGoodsListJson.setGoodsId(itemModel.getGoodsId().toString());
			inventoryFreezeGoodsListJson.setGoodsName(itemModel.getGoodsName());
			inventoryFreezeGoodsListJson.setGoodsNo(itemModel.getGoodsNo());
			inventoryFreezeGoodsListJson.setDivergenceDate(transferStr);
			inventoryFreezeGoodsListJson.setNum(itemModel.getNum());
			inventoryFreezeGoodsListJsonList.add(inventoryFreezeGoodsListJson);
		}
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
		//放到解冻的集合
		invetFreezeList.add(JSONObject.fromObject(inventoryFreezeRootJson).toString());
		//扣减销售出库库存量
		String now1 = sdf.format(new Date());
		InvetAddOrSubtractRootJson invetAddOrSubtractRootJson = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractRootJson.setMerchantId(sOutDepotModel.getMerchantId().toString());
		invetAddOrSubtractRootJson.setMerchantName(sOutDepotModel.getMerchantName());
		invetAddOrSubtractRootJson.setTopidealCode(saleOrderModel.getTopidealCode());
		// 事业部
		invetAddOrSubtractRootJson.setBuId(String.valueOf(sOutDepotModel.getBuId()));
		invetAddOrSubtractRootJson.setBuName(sOutDepotModel.getBuName());

		invetAddOrSubtractRootJson.setDepotId(mongo.getDepotId().toString());
		invetAddOrSubtractRootJson.setDepotName(mongo.getName());
		invetAddOrSubtractRootJson.setDepotCode(mongo.getCode());
		invetAddOrSubtractRootJson.setDepotType(mongo.getType());
		invetAddOrSubtractRootJson.setIsTopBooks(mongo.getIsTopBooks());
		invetAddOrSubtractRootJson.setOrderNo(sOutDepotModel.getCode());
		invetAddOrSubtractRootJson.setBusinessNo(saleOrderModel.getCode());
		invetAddOrSubtractRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_002);
		invetAddOrSubtractRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_003);
		invetAddOrSubtractRootJson.setSourceDate(now1);
		invetAddOrSubtractRootJson.setDivergenceDate(transferStr);// 归属时间
		// 获取当前年月			 
		invetAddOrSubtractRootJson.setOwnMonth(yearsMonths);// 归属月份
		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		for (SaleOutDepotItemModel item : itemList) {
			if (item.getTransferNum() != null && item.getTransferNum().intValue() > 0) {
				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
				invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
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
				}else {
					invetAddOrSubtractGoodsListJson.setIsExpire("1");
				}

				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}

			if (item.getWornNum() != null && item.getWornNum().intValue() > 0) {
				InvetAddOrSubtractGoodsListJson invetAddOrSubtractGoodsListJson = new InvetAddOrSubtractGoodsListJson();
				invetAddOrSubtractGoodsListJson.setGoodsId(item.getGoodsId().toString());
				invetAddOrSubtractGoodsListJson.setGoodsName(item.getGoodsName());
				invetAddOrSubtractGoodsListJson.setGoodsNo(item.getGoodsNo());
				invetAddOrSubtractGoodsListJson.setBarcode(item.getBarcode());
				invetAddOrSubtractGoodsListJson.setCommbarcode(item.getCommbarcode());
				invetAddOrSubtractGoodsListJson.setType("1");
				invetAddOrSubtractGoodsListJson.setNum(item.getWornNum());
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
				}else {
					invetAddOrSubtractGoodsListJson.setIsExpire("1");
				}

				invetAddOrSubtractGoodsListJsonList.add(invetAddOrSubtractGoodsListJson);
			}
		}
		invetAddOrSubtractRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractRootJson.setBackTags(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_1.getTags());//回调标签
		invetAddOrSubtractRootJson.setBackTopic(MQPushBackOrderEnum.TRANSFER_OUT_STIRAGE_PUSH_BACK_2_1.getTopic());//回调主题		
		customParam.put("code", saleOrderModel.getCode());//销售订单内部单号
		invetAddOrSubtractRootJson.setCustomParam(customParam);////自定义回调参数
		//放到扣减库存的集合中
		invetAddOrSubList.add(JSONObject.fromObject(invetAddOrSubtractRootJson).toString());
		// 修改销售出库单
		SaleOrderModel sModel = new SaleOrderModel();
		sModel.setState(DERP_ORDER.SALEORDER_STATE_027);
		sModel.setId(saleOrderModel.getId());
		saleOrderDao.modify(sModel);
		//返回需要推送库存的json字符串
		Map<String,List<String>> inventoryMap = new HashMap<String,List<String>>();
		inventoryMap.put("invetFreezeList", invetFreezeList);
		inventoryMap.put("invetAddOrSubList", invetAddOrSubList);
		return inventoryMap;
	}
	*//**
	 * 推送库存MQ-解冻、扣减库存
	 * @param invetAddOrSubList
	 * @throws Exception
	 *//*
	@Override
	public void pushInventoryMQ(Map<String,List<String>> inventoryMap) throws Exception{
		//库存解冻
		List<String> invetFreezeList= inventoryMap.get("invetFreezeList");
		if(invetFreezeList != null && invetFreezeList.size()>0){
			for(String jsonStr:invetFreezeList){
				rocketMQProducer.send(jsonStr, MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
				//避免数据过多会导致锁库
				Thread.sleep(100);
			}
		}
		//库存扣减
		List<String> invetAddOrSubList= inventoryMap.get("invetAddOrSubList");
		if(invetAddOrSubList!= null && invetAddOrSubList.size()>0){
			//推送库存MQ-扣减库存
			for(String jsonStr:invetAddOrSubList){
				rocketMQProducer.send(jsonStr, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				//避免数据过多会导致锁库
				Thread.sleep(100);
			}
		}
	}
}
*/