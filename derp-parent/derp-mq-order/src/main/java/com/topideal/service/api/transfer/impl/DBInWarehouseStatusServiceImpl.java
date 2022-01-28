package com.topideal.service.api.transfer.impl;

import com.topideal.common.constant.*;
import com.topideal.common.enums.InventoryStatusEnum;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.enums.SourceStatusEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.transfer.*;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.api.v5_3.EpassWarehouseStatusRootJson;
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
import com.topideal.service.api.transfer.DBInWarehouseStatusService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * 进仓单状态回推
 * @author yucaifu

 */
@Service
public class DBInWarehouseStatusServiceImpl implements DBInWarehouseStatusService{
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DBInWarehouseStatusServiceImpl.class);
	
	@Autowired
	private TransferOrderDao transferOrderDao;// 调拨单
	@Autowired
	private TransferInDepotDao transferInDepotDao;// 调拨入库单
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;// 调拨出库单
	@Autowired
	private TransferInDepotItemDao transferInDepotItemDao;// 调拨入库单表体
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;

	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品

	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	//进仓单状态回推
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203130200,model=DERP_LOG_POINT.POINT_12203130200_Label,keyword="entInboundId")
	public boolean saveIntoWarehouseStatusInfo(String json,String keys,String topics,String tags) throws Exception {

		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		EpassWarehouseStatusRootJson statusRootJson = (EpassWarehouseStatusRootJson) JSONObject.toBean(jsonData, EpassWarehouseStatusRootJson.class);
		
		String status = statusRootJson.getStatus();//进仓状态1：成功；2：失败
		String entInboundId = statusRootJson.getEntInboundId();// 企业入仓编号（调拨单号/LbxNo）
		Long merchantId = statusRootJson.getMerchantId();// 企业入仓编号
		String topidealCode = statusRootJson.getTopidealCode();// 卓志编码
		String merchantName = statusRootJson.getMerchantName();// 商家名称
		String isRookie = statusRootJson.getIsRookie();//是否菜鸟字段(1-是，0-否)
		String inboundDate = statusRootJson.getInboundDate(); //进仓单生效日期 yyyy-MM-dd HH:mm:ss
		
		// 判断订单是否来自调拨  tag="2"  调拨模块(非菜鸟)
		TransferOrderModel transferOrderModel = new TransferOrderModel();
		if(isRookie.equals("1")){ //是菜鸟
			  transferOrderModel.setLbxNo(entInboundId);//调拨单LBX号
		}else{
			transferOrderModel.setCode(entInboundId);//企业入仓编号
		}
		transferOrderModel.setMerchantId(merchantId);
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);
      
        if (transferOrderModel==null) {
        	LOGGER.error("没有查到对应的订单,订单编号"+entInboundId);
			throw new RuntimeException("没有查到对应的订单,订单编号"+entInboundId);
		}
        //检查调拨单状态
  		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)){
  			LOGGER.error("调拨单状态为待提交,推送失败"+transferOrderModel.getCode());
  			throw new RuntimeException("调拨单状态为待提交,推送失败"+transferOrderModel.getCode());
  		}
		if (!"1".equals(status)) {
			LOGGER.error("调拨单状态不是1成功"+transferOrderModel.getCode());
  			throw new RuntimeException("调拨单状态不是1成功"+transferOrderModel.getCode());
		}
		 // 根据仓库id到mongoDB中查询 仓库信息
 		Map<String, Object> outDepotInfoMap = new HashMap<>();
 		outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());// 调出仓库id
 		DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);// 调出仓库信息
 		if (outDepotInfoMongo == null) {
 			LOGGER.error("未查到调出仓库信息,订单编号" + entInboundId);
 			throw new RuntimeException("未查到调出仓库信息,订单编号" + entInboundId);
 		}
        	
 		// 根据仓库id到mongoDB中查询 仓库信息
 		Map<String, Object> inDepotInfoMap = new HashMap<>();
 		inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());//// 调入仓库id
 		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);
 		if (inDepotInfoMongo == null) {
 			LOGGER.error("未查到调入仓库信息,订单编号" + entInboundId);
 			throw new RuntimeException("未查到调入仓库信息,订单编号" + entInboundId);
 		}
     		
     	//针对同关区调入菜鸟仓，不需要下推调拨指令给跨境宝，调入结果通过调拨入库接收，不需要通过理货确认及入库申报	(页面调拨订单新增出入库仓库是保税仓的时候必填菜鸟仓是保税仓)
        if (inDepotInfoMongo.getName().contains("菜鸟")) {
        	if (StringUtils.isBlank(transferOrderModel.getIsSameArea())) {
        		String outCustomsNo = outDepotInfoMongo.getCustomsNo();//出仓库海关编码
				String inCustomsNo =  inDepotInfoMongo.getCustomsNo();// 入仓库海关编码
				if (outCustomsNo==null||inCustomsNo==null) {
					LOGGER.info("isSameArea为空,调拨出仓库或者调拨入仓库海关编码是空,订单号entInboundId"+entInboundId);
					throw new RuntimeException("入库仓是菜鸟仓 isSameArea为空,调拨出库或者调拨入海关编码是空,订单号entInboundId"+entInboundId);		
				}
				if (String.valueOf(outCustomsNo).equals(String.valueOf(inCustomsNo))) {
					LOGGER.info("入库仓是菜鸟仓 isSameArea为空,调出仓库的海关编码和调入仓库海关编码相同(同关区 不走改流程),订单号entInboundId"+entInboundId);
					throw new RuntimeException("入库仓是菜鸟仓 isSameArea为空,调出仓库的海关编码和调入仓库海关编码相同(同关区 不走改流程),订单号entInboundId"+entInboundId);
				}
			}else {
				if ("1".equals(transferOrderModel.getIsSameArea())) {
					LOGGER.error("同关区调入菜鸟,调入结果通过调拨入库接收，不需要通过理货确认,入库申报,进仓状态的流程" + entInboundId);
		 			throw new RuntimeException("同关区调入菜鸟,调入结果通过调拨入库接收，不需要通过理货确认,入库申报,进仓状态的流程" + entInboundId);
				
				}
			}
        }

		Map<String, Object> depotMerchantRelParam = new HashMap<String, Object>();
		depotMerchantRelParam.put("merchantId", merchantId);
		depotMerchantRelParam.put("depotId", transferOrderModel.getOutDepotId());
		DepotMerchantRelMongo depotMerchantRelInfo = depotMerchantRelMongoDao.findOne(depotMerchantRelParam);
		if (depotMerchantRelInfo == null) {
			LOGGER.error("未查到该商家下调出仓库信息,订单编号" + entInboundId);
			throw new RuntimeException("未查到该商家下调出仓库信息,订单编号" + entInboundId);
		}

		if("1".equals(status)) {						
				
			// 根据理货单号 和 调拨单号  查询  调拨入库单(如果不存在 就抛异常)
			TransferInDepotModel transferInDepotModel =new TransferInDepotModel();
			transferInDepotModel.setTransferOrderId(transferOrderModel.getId());
			transferInDepotModel = transferInDepotDao.getByModel(transferInDepotModel);// 调拨入库单
			if (null==transferInDepotModel) {
				LOGGER.error("没有对应的调拨入库单,订单编号"+entInboundId);
				throw new RuntimeException("没有对应的调拨入库单,订单编号"+entInboundId);
			}
			if (DERP_ORDER.TRANSFERINDEPOT_STATUS_012.equals(transferInDepotModel.getStatus()) //YRC("012","已入仓"),
					||DERP_ORDER.TRANSFERINDEPOT_STATUS_028.equals(transferInDepotModel.getStatus())) { //RKZ("028","入库中"),
				LOGGER.error("调拨入库单已入仓,订单编号" + entInboundId);
				throw new RuntimeException("调拨入库单已入仓,订单编号" + entInboundId);
			}

			// 根据调拨入库单 id 查询调拨入库单商品
			TransferInDepotItemModel transferInDepotItemModel=new TransferInDepotItemModel();
			transferInDepotItemModel.setTransferDepotId(transferInDepotModel.getId());
			List<TransferInDepotItemModel> itemList = transferInDepotItemDao.list(transferInDepotItemModel);

			// 校验检查该入库仓库若为批次效期强校验，或入库强校验，批次效期必填
			if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())
					|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())) {
				for (TransferInDepotItemModel goods : itemList) {// 商品
					String batchNo = goods.getTransferBatchNo();
					Date productionDate = goods.getProductionDate();
					Date overdueDate = goods.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||productionDate==null||overdueDate==null) {
						LOGGER.error(inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
						throw new RuntimeException(inDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
					}
				}
			}

			List<InvetAddOrSubtractGoodsListJson> goodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
			// 遍历商品 推MQ
			for(TransferInDepotItemModel item:itemList) {
				//库存单位 默认为件  0 托盘 1箱 2 件
				String unit = null;
				if(inDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&StringUtils.isNotBlank(item.getTallyingUnit())){
						if(item.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)){
							unit = DERP.INVENTORY_UNIT_0;
						}else if(item.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)){
							unit = DERP.INVENTORY_UNIT_1;
						}else if(item.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)){
							unit = DERP.INVENTORY_UNIT_2;
						}
				 }

				//好品
				if(item.getTransferNum()!=null&&item.getTransferNum().intValue()>0){
					InvetAddOrSubtractGoodsListJson addOrSubtractGoods = new InvetAddOrSubtractGoodsListJson();
					addOrSubtractGoods.setGoodsId(String.valueOf(item.getInGoodsId()));
					addOrSubtractGoods.setGoodsNo(item.getInGoodsNo());
					addOrSubtractGoods.setGoodsName(item.getInGoodsName());
					addOrSubtractGoods.setBarcode(item.getBarcode());
					addOrSubtractGoods.setBatchNo(item.getTransferBatchNo());
					addOrSubtractGoods.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					addOrSubtractGoods.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					if (item.getProductionDate()!=null) {
						addOrSubtractGoods.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
					}
					if (item.getOverdueDate()!=null) {
						addOrSubtractGoods.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
						String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
						addOrSubtractGoods.setIsExpire(isExpire);//是否过期（0是 1否)
					}else {
						addOrSubtractGoods.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
					}
					addOrSubtractGoods.setType(DERP.ISDAMAGE_0);//商品分类 （0 好品 1坏品）	字符串
					addOrSubtractGoods.setNum(item.getTransferNum());
					addOrSubtractGoods.setUnit(unit);//单位	字符串 0 托盘 1箱  2 件
					addOrSubtractGoods.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
					goodsList.add(addOrSubtractGoods);
				}
				//坏品
				if(item.getWornNum()!=null&&item.getWornNum().intValue()>0){
					InvetAddOrSubtractGoodsListJson addOrSubtractGoods = new InvetAddOrSubtractGoodsListJson();
					addOrSubtractGoods.setGoodsId(String.valueOf(item.getInGoodsId()));
					addOrSubtractGoods.setGoodsName(item.getInGoodsName());
					addOrSubtractGoods.setGoodsNo(item.getInGoodsNo());
					addOrSubtractGoods.setBarcode(item.getBarcode());
					addOrSubtractGoods.setBatchNo(item.getTransferBatchNo());
					addOrSubtractGoods.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					addOrSubtractGoods.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					if (item.getProductionDate()!=null) {
						addOrSubtractGoods.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
					}
					if (item.getOverdueDate()!=null) {
						addOrSubtractGoods.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
						String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
						addOrSubtractGoods.setIsExpire(isExpire);//是否过期（0是 1否)
					}else {
						addOrSubtractGoods.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
					}
					addOrSubtractGoods.setType(DERP.ISDAMAGE_1);//商品分类 （0 好品 1坏品）	字符串
					addOrSubtractGoods.setNum(item.getWornNum());
					addOrSubtractGoods.setUnit(unit);//单位	字符串 0 托盘 1箱  2 件
					addOrSubtractGoods.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
					goodsList.add(addOrSubtractGoods);
				}
				//过期品
				if(item.getExpireNum()!=null&&item.getExpireNum().intValue()>0){
					InvetAddOrSubtractGoodsListJson addOrSubtractGoods = new InvetAddOrSubtractGoodsListJson();
					addOrSubtractGoods.setGoodsId(String.valueOf(item.getInGoodsId()));
					addOrSubtractGoods.setGoodsName(item.getInGoodsName());
					addOrSubtractGoods.setGoodsNo(item.getInGoodsNo());
					addOrSubtractGoods.setBarcode(item.getBarcode());
					addOrSubtractGoods.setBatchNo(item.getTransferBatchNo());
					addOrSubtractGoods.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					addOrSubtractGoods.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					if (item.getProductionDate()!=null) {
						addOrSubtractGoods.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
					}
					if (item.getOverdueDate()!=null) {
						addOrSubtractGoods.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
						String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
						addOrSubtractGoods.setIsExpire(isExpire);//是否过期（0是 1否)
					}else {
						addOrSubtractGoods.setIsExpire(DERP.ISEXPIRE_0);//是否过期（0 是 1 否）
					}
					addOrSubtractGoods.setType(DERP.ISDAMAGE_0);//商品分类 （0 好品 1坏品）	字符串
					addOrSubtractGoods.setNum(item.getExpireNum());
					addOrSubtractGoods.setUnit(unit);//单位	字符串 0 托盘 1箱  2 件
					addOrSubtractGoods.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
					goodsList.add(addOrSubtractGoods);
				}
			}
			//拼装加库存接口参数
			InvetAddOrSubtractRootJson addOrSubtractRoot = new InvetAddOrSubtractRootJson();
			addOrSubtractRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
			addOrSubtractRoot.setMerchantName(transferOrderModel.getMerchantName());
			addOrSubtractRoot.setTopidealCode(transferOrderModel.getTopidealCode());
			addOrSubtractRoot.setDepotType(inDepotInfoMongo.getType());
			addOrSubtractRoot.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
			addOrSubtractRoot.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
			addOrSubtractRoot.setDepotCode(inDepotInfoMongo.getCode());
			addOrSubtractRoot.setDepotName(inDepotInfoMongo.getName());
			addOrSubtractRoot.setOrderNo(transferInDepotModel.getCode());
			addOrSubtractRoot.setSource(SourceStatusEnum.DBDD.getKey());
			addOrSubtractRoot.setSourceType(InventoryStatusEnum.DBRK.getKey());
			addOrSubtractRoot.setSourceDate(TimeUtils.formatFullTime());//单据时间
			if(inboundDate.length()==10) inboundDate = inboundDate+" 00:00:00";
			addOrSubtractRoot.setOwnMonth(TimeUtils.formatMonthForStr(inboundDate));//归属月份
			addOrSubtractRoot.setDivergenceDate(inboundDate);//出入库时间
			addOrSubtractRoot.setBusinessNo(transferOrderModel.getCode());
			addOrSubtractRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
			addOrSubtractRoot.setBuName(transferOrderModel.getBuName());
			addOrSubtractRoot.setGoodsList(goodsList);
			//回调参数
			addOrSubtractRoot.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
			addOrSubtractRoot.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
			addOrSubtractRoot.setCustomParam(new HashMap<String, Object>());

			//修改调拨入库单 状态
			TransferInDepotModel tInDepotModel =new TransferInDepotModel();
			tInDepotModel.setId(transferInDepotModel.getId());
			tInDepotModel.setTransferDate(TimeUtils.parseFullTime(inboundDate));
			tInDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028); //RKZ("028","入库中"),
			transferInDepotDao.modify(tInDepotModel);

			//====================== 判断是否以入定出（当入仓仓库为海外仓时不走以入定出）==================

			if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType()) &&
					DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1.equals(depotMerchantRelInfo.getIsInDependOut())) {

				// 根据理货单号 和 调拨单号  查询  调拨出库单(如果不存在 就抛异常)
				TransferOutDepotModel transferOutDepotModel =new TransferOutDepotModel();
				transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());
				transferOutDepotModel = transferOutDepotDao.getByModel(transferOutDepotModel);
				if (null==transferOutDepotModel) {
					LOGGER.error("(调拨以入定出流程)没有找到对应的调拨出库单,订单编号"+entInboundId);
					throw new RuntimeException("(调拨以入定出流程)没有找到对应的调拨出库单,订单编号"+entInboundId);
				}
				//调拨出库单为“已出库”或者“出库中”则不修改
				boolean flag = true;
				if (DERP_ORDER.TRANSFEROUTDEPOT_STATUS_016.equals(transferOutDepotModel.getStatus())
						||DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027.equals(transferOutDepotModel.getStatus())) {
					LOGGER.error("调拨入库单已出库,订单编号" + entInboundId);
					flag = false;
//					throw new RuntimeException("调拨入库单已出库,订单编号" + entInboundId);
				}

				if (flag) {
					// 根据调拨出库单 id 查询调拨出库单商品
					TransferOutDepotItemModel transferOutDepotItemModel=new TransferOutDepotItemModel();
					transferOutDepotItemModel.setTransferDepotId(transferOutDepotModel.getId());
					List<TransferOutDepotItemModel> outItemList = transferOutDepotItemDao.list(transferOutDepotItemModel);

					// 批次效期强校验：1-是 0-否
					if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
						for (TransferOutDepotItemModel goods : outItemList) {
							String batchNo = goods.getTransferBatchNo();
							Date productionDate = goods.getProductionDate();
							Date overdueDate = goods.getOverdueDate();
							if (StringUtils.isBlank(batchNo)||productionDate==null||overdueDate==null) {
								LOGGER.error(outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
								throw new RuntimeException(outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + entInboundId);
							}
						}
					}

					//调拨出库商品扣减库存
					List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
					//释放冻结商品列表
					List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();
					// 遍历商品 推MQ
					for(TransferOutDepotItemModel item : outItemList) {
						//库存单位 默认为件  0 托盘 1箱 2 件
						String unit = null;
						if(inDepotInfoMongo.getType().equals(DERP_SYS.DEPOTINFO_TYPE_C)&&StringUtils.isNotBlank(item.getTallyingUnit())){
							if(item.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_00)){
								unit = DERP.INVENTORY_UNIT_0;
							}else if(item.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_01)){
								unit = DERP.INVENTORY_UNIT_1;
							}else if(item.getTallyingUnit().equals(DERP.ORDER_TALLYINGUNIT_02)){
								unit = DERP.INVENTORY_UNIT_2;
							}
						}

						//好品
						if(item.getTransferNum()!=null&&item.getTransferNum().intValue()>0){
							InvetAddOrSubtractGoodsListJson addOrSubtractGoods = new InvetAddOrSubtractGoodsListJson();
							addOrSubtractGoods.setGoodsId(String.valueOf(item.getOutGoodsId()));
							addOrSubtractGoods.setGoodsName(item.getOutGoodsName());
							addOrSubtractGoods.setGoodsNo(item.getOutGoodsNo());
							addOrSubtractGoods.setBarcode(item.getBarcode());
							addOrSubtractGoods.setBatchNo(item.getTransferBatchNo());
							addOrSubtractGoods.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
							addOrSubtractGoods.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
							if (item.getProductionDate()!=null) {
								addOrSubtractGoods.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
							}
							if (item.getOverdueDate()!=null) {
								addOrSubtractGoods.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
								//判断是否过期是否过期（0是 1否）
								String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());
								addOrSubtractGoods.setIsExpire(isExpire);
							}else {
								addOrSubtractGoods.setIsExpire(DERP.ISEXPIRE_1);
							}
							addOrSubtractGoods.setType(DERP.ISDAMAGE_0);//商品分类 （0 好品 1坏品）	字符串
							addOrSubtractGoods.setNum(item.getTransferNum());
							addOrSubtractGoods.setUnit(unit);//单位	字符串 0 托盘 1箱  2 件
							addOrSubtractGoods.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
							subGoodsList.add(addOrSubtractGoods);
						}
						//坏品
						if(item.getWornNum()!=null&&item.getWornNum().intValue()>0){
							InvetAddOrSubtractGoodsListJson addOrSubtractGoods = new InvetAddOrSubtractGoodsListJson();
							addOrSubtractGoods.setGoodsId(String.valueOf(item.getOutGoodsId()));
							addOrSubtractGoods.setGoodsName(item.getOutGoodsName());
							addOrSubtractGoods.setGoodsNo(item.getOutGoodsNo());
							addOrSubtractGoods.setBarcode(item.getBarcode());
							addOrSubtractGoods.setBatchNo(item.getTransferBatchNo());
							addOrSubtractGoods.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
							addOrSubtractGoods.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
							if (item.getProductionDate()!=null) {
								addOrSubtractGoods.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
							}
							if (item.getOverdueDate()!=null) {
								addOrSubtractGoods.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
								String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
								addOrSubtractGoods.setIsExpire(isExpire);//是否过期（0是 1否)
							}else {
								addOrSubtractGoods.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
							}
							addOrSubtractGoods.setType(DERP.ISDAMAGE_1);//商品分类 （0 好品 1坏品）	字符串
							addOrSubtractGoods.setNum(item.getWornNum());
							addOrSubtractGoods.setUnit(unit);//单位	字符串 0 托盘 1箱  2 件
							addOrSubtractGoods.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）字符串 0 调减 1调增
							subGoodsList.add(addOrSubtractGoods);
						}
						//过期品
						if(item.getExpireNum()!=null&&item.getExpireNum().intValue()>0){
							InvetAddOrSubtractGoodsListJson addOrSubtractGoods = new InvetAddOrSubtractGoodsListJson();
							addOrSubtractGoods.setGoodsId(String.valueOf(item.getOutGoodsId()));
							addOrSubtractGoods.setGoodsName(item.getOutGoodsName());
							addOrSubtractGoods.setGoodsNo(item.getOutGoodsNo());
							addOrSubtractGoods.setBarcode(item.getBarcode());
							addOrSubtractGoods.setBatchNo(item.getTransferBatchNo());
							addOrSubtractGoods.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
							addOrSubtractGoods.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
							if (item.getProductionDate()!=null) {
								addOrSubtractGoods.setProductionDate(TimeUtils.formatDay(item.getProductionDate()));
							}
							if (item.getOverdueDate()!=null) {
								addOrSubtractGoods.setOverdueDate(TimeUtils.formatDay(item.getOverdueDate()));
								String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
								addOrSubtractGoods.setIsExpire(isExpire);//是否过期（0是 1否)
							}else {
								addOrSubtractGoods.setIsExpire(DERP.ISEXPIRE_0);//是否过期（0 是 1 否）
							}
							addOrSubtractGoods.setType(DERP.ISDAMAGE_0);//商品分类 （0 好品 1坏品）	字符串
							addOrSubtractGoods.setNum(item.getExpireNum());
							addOrSubtractGoods.setUnit(unit);//单位	字符串 0 托盘 1箱  2 件
							addOrSubtractGoods.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
							subGoodsList.add(addOrSubtractGoods);
						}
					}

					//查询调拨商品
					TransferOrderItemModel orderItemModel = new TransferOrderItemModel();
					orderItemModel.setTransferOrderId(transferOrderModel.getId());
					List<TransferOrderItemModel> orderItems = transferOrderItemDao.list(orderItemModel);
					for (TransferOrderItemModel transferOrderItemModel : orderItems) {
						//释放冻结商品
						InventoryFreezeGoodsListJson fgood = new InventoryFreezeGoodsListJson();
						fgood.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
						fgood.setGoodsName(transferOrderItemModel.getOutGoodsName());
						fgood.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
						fgood.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
						fgood.setGoodsName(transferOrderItemModel.getOutGoodsName());
						fgood.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
						fgood.setDivergenceDate(inboundDate);
						fgood.setNum(transferOrderItemModel.getTransferNum());
						//fgood.setUnit("2");//	单位	字符串 0 托盘 1箱  2 件
						freezeGoodList.add(fgood);
					}

					//释放冻结库存
					InventoryFreezeRootJson freezeAddLower = new InventoryFreezeRootJson();
					freezeAddLower.setMerchantId(String.valueOf(merchantId));
					freezeAddLower.setMerchantName(merchantName);
					freezeAddLower.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
					freezeAddLower.setDepotName(outDepotInfoMongo.getName());
					freezeAddLower.setOrderNo(transferOutDepotModel.getCode());// 解冻取调拨出库单号
					freezeAddLower.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
					freezeAddLower.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
					freezeAddLower.setSourceDate(TimeUtils.formatFullTime());
					freezeAddLower.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
					freezeAddLower.setBusinessNo(transferOrderModel.getCode());
					freezeAddLower.setGoodsList(freezeGoodList);
					JSONObject jsonFree = JSONObject.fromObject(freezeAddLower);
					//拼装减库存接口参数
					InvetAddOrSubtractRootJson subInventoryRoot = new InvetAddOrSubtractRootJson();
					subInventoryRoot.setMerchantId(String.valueOf(transferOutDepotModel.getMerchantId()));
					subInventoryRoot.setMerchantName(String.valueOf(transferOutDepotModel.getMerchantName()));
					subInventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
					subInventoryRoot.setDepotType(outDepotInfoMongo.getType());
					subInventoryRoot.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
					subInventoryRoot.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
					subInventoryRoot.setDepotCode(outDepotInfoMongo.getCode());
					subInventoryRoot.setDepotName(outDepotInfoMongo.getName());
					subInventoryRoot.setOrderNo(transferOutDepotModel.getCode());
					subInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
					subInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
					subInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
					subInventoryRoot.setOwnMonth(TimeUtils.formatMonthForStr(inboundDate));
					subInventoryRoot.setDivergenceDate(inboundDate);
					subInventoryRoot.setBusinessNo(transferOrderModel.getCode());
					subInventoryRoot.setGoodsList(subGoodsList);
					subInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
					subInventoryRoot.setBuName(transferOrderModel.getBuName());
					//回调参数
					subInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
					subInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
					subInventoryRoot.setCustomParam(new HashMap<String, Object>());

					//修改调拨出库单状态为“出库中”
					TransferOutDepotModel tOutModel = new TransferOutDepotModel();
					tOutModel.setId(transferOutDepotModel.getId());
					tOutModel.setTransferDate(TimeUtils.parseFullTime(inboundDate));
					tOutModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);
					transferOutDepotDao.modify(tOutModel);

					//减库存
					JSONObject subjson = JSONObject.fromObject(subInventoryRoot);
					rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
					rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				}
			}

			//推送增加库存消息
			JSONObject jsonRequest = JSONObject.fromObject(addOrSubtractRoot);
			rocketMQProducer.send(jsonRequest.toString(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		}
		return true;
	}

}
