package com.topideal.service.api.transfer.impl;

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
import com.topideal.json.api.v5_7.EpassTransferOutStirageGoodsListJson;
import com.topideal.json.api.v5_7.EpassTransferOutStirageRootJson;
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
import com.topideal.service.api.transfer.DBTransfersOutStorageService;
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
 * 调拨出库接口
 * @author yuciafu
 */
@Service
public class DBTransfersOutStorageServiceImpl implements DBTransfersOutStorageService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DBTransfersOutStorageServiceImpl.class);
	
	@Autowired
	private TransferOrderDao transferOrderDao;// 调拨单表
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;// 调拨单商品
	@Autowired 
	private TransferOutDepotDao transferOutDepotDao;//调拨出库单
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	@Autowired 
	private TransferInDepotDao transferInDepotDao;// 调拨入库单
	@Autowired 
	private TransferInDepotItemDao transferInDepotItemDao;// 调拨入库单表体
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;// 商品
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;
	
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203130900,model=DERP_LOG_POINT.POINT_12203130900_Label,keyword="orderCode")
	public boolean saveTransfersOutStorageInfo(String json,String keys,String topics,String tags) throws Exception {
		System.out.println("调拨出库消息接收-调拨："+json);
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",EpassTransferOutStirageGoodsListJson.class);
		
		EpassTransferOutStirageRootJson outStirageRoot = (EpassTransferOutStirageRootJson) JSONObject.toBean(jsonData, EpassTransferOutStirageRootJson.class,classMap);
		
		String orderCode = outStirageRoot.getOrderCode();// 调拨单号		
		String isRookie = outStirageRoot.getIsRookie();//获取是否菜鸟字段(1-是，0-否)
		Long merchantId = outStirageRoot.getMerchantId();// 企业入仓编号
		String topidealCode = outStirageRoot.getTopidealCode();// 企业入仓编号
		String merchantName = outStirageRoot.getMerchantName();// 企业入仓编号
		String outcode = outStirageRoot.getCode();// 调拨出库单号
		String yModel = outStirageRoot.getModel();//业务场景 账册内货权转移/账册内货权转移调仓 必填			
		String serveTypes = outStirageRoot.getServeTypes();//服务类型 E0：区内调拨调出服务F0：区内调拨调入服务G0：库内调拨服务'必填,
		String transferDateStr = outStirageRoot.getTransferDate();
		
		TransferOrderModel transferOrderModel =new TransferOrderModel();// 调拨单表
		transferOrderModel.setMerchantId(merchantId);
        if(isRookie.equals("1")){
        	transferOrderModel.setLbxNo(orderCode);//LBX号
        }else{
        	transferOrderModel.setCode(orderCode);//订单号
        }
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);	
		if(transferOrderModel ==null) {
			LOGGER.error("订单在不存在"+orderCode);
			throw new RuntimeException("订单在所有订单模块都不存在"+orderCode);
		}

		//检查调拨单状态
		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)){ //DTJ("013","待提交"),
			LOGGER.error("调拨单状态为待提交,推送失败"+orderCode);
			throw new RuntimeException("调拨单状态为待提交,推送失败"+orderCode);
		}
		
		Timestamp transferDate=null;//调拨时间
		if(transferDateStr.length()==10) {
			transferDate=Timestamp.valueOf(transferDateStr+" 00:00:00");	//调拨时间
		}else {
			transferDate=Timestamp.valueOf(transferDateStr);//调拨时间
		}

		// 向电商订单唯一标识表插入数据
		OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
		orderExternalCodeModel.setExternalCode(transferOrderModel.getCode());
		orderExternalCodeModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
		try {
			Long id = orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("调拨单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			throw new RuntimeException("电调拨单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
		}
		
		// 根据调拨单号(对应调拨出库单code)  号查询调拨出库单
		TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
		transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());
		transferOutDepotModel = transferOutDepotDao.getByModel(transferOutDepotModel);
		if (null!=transferOutDepotModel) {
			LOGGER.error("调拨出库单已经存在,结束运行,调拨单号"+orderCode);
			System.out.println("调拨出库单已经存在,结束运行,调拨单号"+orderCode);
			throw new RuntimeException("调拨出库单已经存在,结束运行,调拨单号"+orderCode);
		}

		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> outDepotInfoMap = new HashMap<>();
		outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());// 调出仓库id
		DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);// 调入仓库信息
		if(outDepotInfoMongo == null) {
			LOGGER.error("未查到调出仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调出仓库信息,订单编号" + orderCode);
		}
		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> inDepotInfoMap = new HashMap<>();
		inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());// 调入仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);// 调入仓库信息
		if(inDepotInfoMongo == null) {
			LOGGER.error("未查到调入仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调入仓库信息,订单编号" + orderCode);
		}
				
		//  调拨出库回推接口（注：该处不接收业务场景为20：账册内货号变更
		if ("20".equals(yModel)) {
			LOGGER.error("调拨出库不接收业务场景为20：账册内货号变更 数据"+orderCode);
			throw new RuntimeException("调拨出库不接收业务场景为20：账册内货号变更 数据"+orderCode);
		}	
		
		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
			for (EpassTransferOutStirageGoodsListJson goodsListJson : outStirageRoot.getGoodsList()) {
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
		//查询调拨商品
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

		//向调拨出库表中插入数据
		TransferOutDepotModel tModel = new TransferOutDepotModel();
		tModel.setTransferOrderId(transferOrderModel.getId());//调拨订单ID
		tModel.setTransferOrderCode(transferOrderModel.getCode());//调拨订单编号
		tModel.setMerchantId(transferOrderModel.getMerchantId());
		tModel.setMerchantName(transferOrderModel.getMerchantName());//商家名称
		tModel.setContractNo(transferOrderModel.getContractNo());//合同号
		tModel.setInDepotId(transferOrderModel.getInDepotId());//调入仓库id
		tModel.setInDepotName(transferOrderModel.getInDepotName());//调入仓库名称
		tModel.setOutDepotId(transferOrderModel.getOutDepotId());//调出仓库id
		tModel.setOutDepotName(transferOrderModel.getOutDepotName());//调出仓库名称
		tModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
		tModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户名称
		tModel.setLbxNo(transferOrderModel.getLbxNo());//LBX号
		tModel.setBuName(transferOrderModel.getBuName());
		tModel.setBuId(transferOrderModel.getBuId());
		tModel.setCreateName("接口回传");
		tModel.setTransferDate(transferDate);//调出时间
		tModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);//CKZ("027","出库中"), //状态  '状态 015.待出仓   ,016.已出仓 027出库中',
//		tModel.setCode(CodeGeneratorUtil.getNo("DBCK"));//调拨出单号(对应调拨出库接口的调拨单号)
		tModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK));//调拨出单号(对应调拨出库接口的调拨单号)
		tModel.setOutExternalCode(outcode);
		tModel.setDataSource(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_2);

		//新增
		transferOutDepotDao.save(tModel);
		
		List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();//释放冻结商品列表
		List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//加库存商品
		for(EpassTransferOutStirageGoodsListJson goods : outStirageRoot.getGoodsList()) {
			//查询商品
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goods.getGoodsId());
			MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(paramMap);
			//新增调拨出库表体
			TransferOutDepotItemModel itemModel = new  TransferOutDepotItemModel();
			itemModel.setTransferDepotId(tModel.getId());//调拨出库ID
			itemModel.setOutGoodsId(goods.getGoodsId());// 调出商品id
			itemModel.setOutGoodsCode(goods.getGoodsCode());//调出商品编码
			itemModel.setOutGoodsName(goods.getGoodsName());//调出商品名称
			itemModel.setOutGoodsNo(goods.getGoodsNo());//调出商品货号
			itemModel.setTransferNum(goods.getSalesNum());//好品数量
			itemModel.setWornNum(goods.getWornNum());//坏品数量
			itemModel.setTransferBatchNo(goods.getBatchNo());//调拨批次
			itemModel.setBarcode(goods.getBarcode());
			itemModel.setOutCommbarcode(outMerchandise.getCommbarcode());
			String productionDate = goods.getProductionDate();//
			if (StringUtils.isNotBlank(productionDate)) {
				if(productionDate.length()==10) {
					itemModel.setProductionDate(Timestamp.valueOf(productionDate+" 00:00:00"));//生产日期
				}else{
					itemModel.setProductionDate(Timestamp.valueOf(productionDate));//生产日期
				}
			}
			String overdueDate = goods.getOverdueDate();
			if (StringUtils.isNotBlank(overdueDate)) {
				if(overdueDate.length()==10) {
					itemModel.setOverdueDate(Timestamp.valueOf(overdueDate+" 00:00:00"));//失效日期
				}else{
					itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));//失效日期
				}
			}
			//新增商品
			transferOutDepotItemDao.save(itemModel);

			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(itemModel.getOverdueDate()!=null){
				isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}

			if (itemModel.getTransferNum() != null && itemModel.getTransferNum().intValue() > 0) {
				//拼装减库存商品
				InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
				good.setGoodsId(String.valueOf(itemModel.getOutGoodsId()));
				good.setGoodsName(itemModel.getOutGoodsName());
				good.setGoodsNo(itemModel.getOutGoodsNo());
				good.setBarcode(itemModel.getBarcode());
				good.setBatchNo(itemModel.getTransferBatchNo());
				good.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
				good.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
				good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
				good.setIsExpire(isExpire);//是否过期（0 是 1 否）
				good.setNum(itemModel.getTransferNum());
				//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
				good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
				good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				subGoodsList.add(good);
			}
			if (itemModel.getWornNum() != null && itemModel.getWornNum().intValue() > 0) {
				//拼装减库存商品
				InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
				good.setGoodsId(String.valueOf(itemModel.getOutGoodsId()));
				good.setGoodsName(itemModel.getOutGoodsName());
				good.setGoodsNo(itemModel.getOutGoodsNo());
				good.setBarcode(itemModel.getBarcode());
				good.setBatchNo(itemModel.getTransferBatchNo());
				good.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
				good.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
				good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
				good.setIsExpire(isExpire);//是否过期（0 是 1 否）
				good.setNum(itemModel.getWornNum());
				//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
				good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
				good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				subGoodsList.add(good);
			}
		}
		
		for (TransferOrderItemModel transferOrderItemModel : itemList) {
			//释放冻结商品
			InventoryFreezeGoodsListJson fgood = new InventoryFreezeGoodsListJson();
			fgood.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
			fgood.setGoodsName(transferOrderItemModel.getOutGoodsName());
			fgood.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
			fgood.setDivergenceDate(TimeUtils.formatFullTime(transferDate));
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
		freezeAddLower.setOrderNo(tModel.getCode());// 解冻取调拨出库单号
		freezeAddLower.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		freezeAddLower.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		freezeAddLower.setSourceDate(TimeUtils.formatFullTime());
		freezeAddLower.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
		freezeAddLower.setBusinessNo(transferOrderModel.getCode());
		freezeAddLower.setGoodsList(freezeGoodList);
		JSONObject jsonFree = JSONObject.fromObject(freezeAddLower);
		
		//拼装减库存接口参数
		InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
		inventoryRoot.setMerchantId(String.valueOf(tModel.getMerchantId()));
		inventoryRoot.setMerchantName(String.valueOf(tModel.getMerchantName()));
		inventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
		inventoryRoot.setDepotType(outDepotInfoMongo.getType());
		inventoryRoot.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
		inventoryRoot.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
		inventoryRoot.setDepotCode(outDepotInfoMongo.getCode());
		inventoryRoot.setDepotName(outDepotInfoMongo.getName());
		inventoryRoot.setOrderNo(tModel.getCode());
		inventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		inventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
		inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
		inventoryRoot.setOwnMonth(TimeUtils.format(transferDate, "yyyy-MM"));
		inventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(transferDate));
		inventoryRoot.setBusinessNo(transferOrderModel.getCode());
		inventoryRoot.setGoodsList(subGoodsList);
		inventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
		inventoryRoot.setBuName(transferOrderModel.getBuName());
		//回调参数
		inventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
		inventoryRoot.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
		inventoryRoot.setCustomParam(new HashMap<String, Object>());
		//减库存
		JSONObject subjson = JSONObject.fromObject(inventoryRoot);
		// 把减库存放到最下方防止已出定入出现异常接口报错

		boolean flag = false;
		//调拨入仓仓库是 在对应商家下的“以出定入”标识为“是”的，需进行以出定入（查询调拨入库单是否存在，若存在则不保存调拨入库单）
		//如果调出库仓库为香港仓时，不走以出定入
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
				LOGGER.error("调拨入库单已经存在调拨单号：" + transferOrderModel.getCode() + "  保存失败");
			}
		}
		// 已出定入
		if (flag) {
			for (EpassTransferOutStirageGoodsListJson goodsJson : outStirageRoot.getGoodsList()) {
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())
						|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())) {
					String batchNo = goodsJson.getBatchNo();
					String productionDate = goodsJson.getProductionDate();
					String overdueDate = goodsJson.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						LOGGER.error(inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验,"+"批次和效期不能为空,订单号:" + orderCode);
						throw new RuntimeException(inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验,"+"批次和效期不能为空,订单号:" + orderCode);
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
			inDepotModel.setTransferDate(transferDate);// 调入时间(调拨时间)
			inDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028);//RKZ("028","入库中"),// 状态  011.待入仓  ,012.已入仓 028.入库中'
//			inDepotModel.setCode(CodeGeneratorUtil.getNo(DERP.UNIQUEID_PREFIX_DBRK));
			inDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));
			inDepotModel.setSource(DERP_ORDER.TRANSFERINDEPOT_SOURCE_2);// 1-调拨入自动生成 2-接口回推生成
			inDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());
			inDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());
			inDepotModel.setLbxNo(transferOrderModel.getLbxNo());// LBX号
			inDepotModel.setBuName(transferOrderModel.getBuName());
			inDepotModel.setBuId(transferOrderModel.getBuId());
			inDepotModel.setCreateName("接口回传");
			inDepotModel.setInExternalCode(outcode);
			// 调拨入库单插入数据
			transferInDepotDao.save(inDepotModel);

			List<InvetAddOrSubtractGoodsListJson> insubGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//加库存商品
			//查询出库生成入库单表体和加库存报文
			TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
			outDepotModel.setTransferOrderId(transferOrderModel.getId());
			outDepotModel = transferOutDepotDao.searchByModel(outDepotModel);
			TransferOutDepotItemModel outDepotItem = new TransferOutDepotItemModel();
			outDepotItem.setTransferDepotId(outDepotModel.getId());
			List<TransferOutDepotItemModel> outItemList = transferOutDepotItemDao.list(outDepotItem);
            for(TransferOutDepotItemModel outItem : outItemList){
            	//调拨单商品
				TransferOrderItemModel orderItem = kwGoodsMap.get(outItem.getOutGoodsId());
				// 调拨入库单表体
				TransferInDepotItemModel inDepotItemModel = new TransferInDepotItemModel();
				inDepotItemModel.setTransferDepotId(inDepotModel.getId());// 调拨入库ID
				inDepotItemModel.setTransferNum(outItem.getTransferNum());// 调拨数量
				inDepotItemModel.setWornNum(outItem.getWornNum());// 坏品数量
				inDepotItemModel.setInGoodsId(orderItem.getInGoodsId());// 调入商品id
				inDepotItemModel.setInGoodsName(orderItem.getInGoodsName());// 调入商品名称
				inDepotItemModel.setInGoodsNo(orderItem.getInGoodsNo());// 调入商品货号
				inDepotItemModel.setInGoodsCode(orderItem.getInGoodsCode());// 调入商品编码
				inDepotItemModel.setBarcode(orderItem.getInBarcode());
				inDepotItemModel.setInCommbarcode(orderItem.getInCommbarcode());// 调入商品标准条码
				inDepotItemModel.setTransferBatchNo(outItem.getTransferBatchNo());// 调拨批次
				inDepotItemModel.setProductionDate(outItem.getProductionDate());//生产日期
				inDepotItemModel.setOverdueDate(outItem.getOverdueDate());//失效日期
				transferInDepotItemDao.save(inDepotItemModel);

				//计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if(inDepotItemModel.getOverdueDate()!=null){
					isExpire = TimeUtils.isNotIsExpireByDate(inDepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
				}
				if (inDepotItemModel.getTransferNum() != null && inDepotItemModel.getTransferNum().intValue() > 0) {
					//拼装加库存商品
					InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();
					aSGood.setGoodsId(String.valueOf(inDepotItemModel.getInGoodsId()));//原商品
					aSGood.setGoodsNo(inDepotItemModel.getInGoodsNo());
					aSGood.setGoodsName(inDepotItemModel.getInGoodsName());
					aSGood.setBarcode(inDepotItemModel.getBarcode());
					aSGood.setCommbarcode(inDepotItemModel.getInCommbarcode());
					aSGood.setBatchNo(inDepotItemModel.getTransferBatchNo());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
					aSGood.setIsExpire(isExpire);//是否过期（0 是 1 否）
					aSGood.setNum(inDepotItemModel.getTransferNum());
					//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
					aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）字符串 0 调减 1调增
					aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					insubGoodsList.add(aSGood);
				}
                //坏品
				if (inDepotItemModel.getWornNum() != null && inDepotItemModel.getWornNum().intValue() > 0) {
					//拼装加库存商品
					InvetAddOrSubtractGoodsListJson aSGood = new InvetAddOrSubtractGoodsListJson();
					aSGood.setGoodsId(String.valueOf(inDepotItemModel.getInGoodsId()));//原商品
					aSGood.setGoodsNo(inDepotItemModel.getInGoodsNo());
					aSGood.setGoodsName(inDepotItemModel.getInGoodsName());
					aSGood.setBarcode(inDepotItemModel.getBarcode());
					aSGood.setCommbarcode(inDepotItemModel.getInCommbarcode());
					aSGood.setBatchNo(inDepotItemModel.getTransferBatchNo());
					aSGood.setProductionDate(TimeUtils.formatDay(inDepotItemModel.getProductionDate()));
					aSGood.setOverdueDate(TimeUtils.formatDay(inDepotItemModel.getOverdueDate()));
					aSGood.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
					aSGood.setIsExpire(isExpire);//是否过期（0 是 1 否）
					aSGood.setNum(inDepotItemModel.getWornNum());
					//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
					aSGood.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）字符串 0 调减 1调增
					aSGood.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					aSGood.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					insubGoodsList.add(aSGood);
				}
			}

			//拼装加库存接口参数
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
			subInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009);//DBDD("009","调拨订单"),
			subInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010); //DBRK("0010","调拨入库"),
			subInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
			subInventoryRoot.setOwnMonth(TimeUtils.format(transferDate, "yyyy-MM"));
			subInventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(transferDate));
			subInventoryRoot.setBusinessNo(transferOrderModel.getCode());
			subInventoryRoot.setGoodsList(insubGoodsList);
			subInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
			subInventoryRoot.setBuName(transferOrderModel.getBuName());
			//回调参数
			subInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
			subInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
			subInventoryRoot.setCustomParam(new HashMap<String, Object>());
			//加库存
			JSONObject addSubjson = JSONObject.fromObject(subInventoryRoot);
			rocketMQProducer.send(addSubjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		}
		// 把释放冻结存放到最下方防止已出定入出现异常接口报错
		rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
		// 把减库存放到最下方防止已出定入出现异常接口报错
		rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		return true;
	}

}
