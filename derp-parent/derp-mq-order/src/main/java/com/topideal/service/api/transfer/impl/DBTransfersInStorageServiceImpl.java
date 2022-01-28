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
import com.topideal.json.api.v5_6.EpassTransferInStirageGoodsListJson;
import com.topideal.json.api.v5_6.EpassTransferInStirageRootJson;
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
import com.topideal.service.api.transfer.DBTransfersInStorageService;
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
 * 调拨入库
 * @author YUCAIFU
 */
@Service
public class DBTransfersInStorageServiceImpl implements DBTransfersInStorageService{
	
	/*打印日志*/
	private static final Logger LOGGER = LoggerFactory.getLogger(DBTransfersInStorageServiceImpl.class);
	
	@Autowired

	private TransferOrderDao transferOrderDao;//调拨订单
	@Autowired
	private TransferInDepotDao transferInDepotDao;//调拨入库表
	@Autowired
	private TransferInDepotItemDao  transferInDepotItemDao;//调拨入库商品
	@Autowired
	private TransferOutDepotDao transferOutDepotDao;// 调拨出库单
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb
	@Autowired
	private RMQProducer rocketMQProducer;//MQ;
	@Autowired
	private OrderExternalCodeDao orderExternalCodeDao;
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao;
	@Autowired
	private DepotMerchantRelMongoDao depotMerchantRelMongoDao;
	@Autowired
	private TransferOrderItemDao transferOrderItemDao;
	@Autowired
	private TransferOutDepotItemDao transferOutDepotItemDao;
	@Autowired
	private MerchantBuRelMongoDao merchantBuRelMongoDao;

	// 调拨入库
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203130800,model=DERP_LOG_POINT.POINT_12203130800_Label,keyword="orderCode")
	public boolean saveTransfersInStorage(String json,String keys,String topics,String tags) throws Exception {
		LOGGER.info("调拨入库-调拨,消息接收"+json);
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",EpassTransferInStirageGoodsListJson.class);
		EpassTransferInStirageRootJson inStirageRoot = (EpassTransferInStirageRootJson) JSONObject.toBean(jsonData, EpassTransferInStirageRootJson.class,classMap);
		
		String orderCode = inStirageRoot.getOrderCode();// 订单号
		String isRookie = inStirageRoot.getIsRookie();//获取是否菜鸟字段(1-是，0-否)
		Long merchantId = inStirageRoot.getMerchantId();// 企业入仓编号
		String topidealCode = inStirageRoot.getTopidealCode();// 企业入仓编号
		String merchantName = inStirageRoot.getMerchantName();// 企业入仓编号
		String inExternalCode = inStirageRoot.getCode();//调拨入库单编码 外部单号
		String remark = inStirageRoot.getRemark();//备注
		String transferDateStr = inStirageRoot.getTransferDate();
		
		TransferOrderModel transferOrderModel = new TransferOrderModel();
		transferOrderModel.setMerchantId(merchantId);
		if(isRookie.equals("1")){
			transferOrderModel.setLbxNo(orderCode);//  企业调拨单号 
		}else{
			transferOrderModel.setCode(orderCode);// order_id 企业调拨单号 
		}
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);	
		if(transferOrderModel==null) {
			LOGGER.error("订单不存在,订单编号"+orderCode);
			throw new RuntimeException("订单不存在,订单编号"+orderCode);
		}
		//检查调拨单状态
		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)){ //DTJ("013","待提交"),
			LOGGER.error("调拨单状态为待提交,推送失败"+orderCode);
			throw new RuntimeException("调拨单状态为待提交,推送失败"+orderCode);
		}

		Map<String, Object> depotParam = new HashMap<>();
		depotParam.put("depotId", transferOrderModel.getInDepotId());
		DepotInfoMongo inDepot = depotInfoMongoDao.findOne(depotParam);
		if(inDepot == null){
			LOGGER.error("调拨订单调入仓库不存在，单号："+orderCode);
			throw new RuntimeException("调拨订单调入仓库不存在，单号："+orderCode);
		}
		depotParam.put("depotId", transferOrderModel.getOutDepotId());
		DepotInfoMongo outDepot = depotInfoMongoDao.findOne(depotParam);
		if(outDepot == null){
			LOGGER.error("调拨订单调出仓库不存在，单号："+orderCode);
			throw new RuntimeException("调拨订单调出仓库不存在，单号："+orderCode);
		}

		//保税仓/海外仓 调入 合作外仓，拦截掉接口回传的调拨入库单
		if ((DERP_SYS.DEPOTINFO_TYPE_A.equals(outDepot.getType()) || DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepot.getType()))
		&& DERP_SYS.DEPOTINFO_ISVDEPOTTYPE_01.equals(inDepot.getISVDepotType())) {
			LOGGER.error("调拨订单为“保税仓/海外仓”调入“合作外仓”，拦截不生成调拨入库单，单号："+orderCode);
			throw new RuntimeException("调拨订单为“保税仓/海外仓”调入“合作外仓”，拦截不生成调拨入库单，单号"+orderCode);
		}

		Timestamp transferDate=null;//发货时间
		if (transferDateStr.length()==10) {
			transferDate=Timestamp.valueOf(transferDateStr+" 00:00:00");	//调拨时间
		}else {
			transferDate=Timestamp.valueOf(transferDateStr);//调拨时间
		}

		// 向电商订单唯一标识表插入数据
		OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
		orderExternalCodeModel.setExternalCode(transferOrderModel.getCode());
		orderExternalCodeModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_4));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
		try {
			Long id = orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			throw new RuntimeException("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
		}

		TransferInDepotModel transferInDepotModel = new TransferInDepotModel();
		transferInDepotModel.setTransferOrderId(transferOrderModel.getId());
		transferInDepotModel = transferInDepotDao.getByModel(transferInDepotModel);
		if (null!=transferInDepotModel) {
			LOGGER.error("调拨入库单已经存在,结束运行 ,订单编码"+orderCode);
			throw new RuntimeException("调拨入库单已经存在,结束运行,订单编码"+orderCode);
		}
		// 根据仓库id到mongoDB中查询 调入仓库信息
		Map<String, Object> inDepotInfoMap = new HashMap<>();
		inDepotInfoMap.put("depotId", transferOrderModel.getInDepotId());// 调出仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(inDepotInfoMap);// 调入仓库信息
		if(inDepotInfoMongo == null) {
			LOGGER.error("未查到调入仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调入仓库信息,订单编号" + orderCode);
		}

		// 根据仓库id到mongoDB中查询 调出仓库信息
		Map<String, Object> outDepotInfoMap = new HashMap<>();
		outDepotInfoMap.put("depotId", transferOrderModel.getOutDepotId());// 调出仓库id
		DepotInfoMongo outDepotInfoMongo = depotInfoMongoDao.findOne(outDepotInfoMap);// 调出仓库信息
		if(outDepotInfoMongo == null) {
			LOGGER.error("未查到调出仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到调出仓库信息,订单编号" + orderCode);
		}

		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())
				|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())) {
			for (EpassTransferInStirageGoodsListJson goodsListJson : inStirageRoot.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验，"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException("入库仓"+inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验，"+"批次和效期不能为空,订单号:" + orderCode);
				}
				
			}
		}

		Map<String, Object> outRelDepotParam = new HashMap<>();
		outRelDepotParam.put("merchantId", merchantId);
		outRelDepotParam.put("depotId", outDepotInfoMongo.getDepotId());
		DepotMerchantRelMongo outRelDepot = depotMerchantRelMongoDao.findOne(outRelDepotParam);
		if (outRelDepot == null) {
			LOGGER.error("未查到该商家下调出仓库信息,订单编号" + orderCode);
			throw new RuntimeException("未查到该商家下调出仓库信息,订单编号" + orderCode);
		}

		//查询调拨商品
		TransferOrderItemModel orderItemModel = new TransferOrderItemModel();
		orderItemModel.setTransferOrderId(transferOrderModel.getId());
		List<TransferOrderItemModel> itemList = transferOrderItemDao.list(orderItemModel);
		Map<Long, TransferOrderItemModel> goodMap = new HashMap<>();//key=原商品id value=调拨单商品实体
		Map<Long, TransferOrderItemModel> kwGoodsMap = new HashMap<>();//key=库位商品id value=调拨单商品实体  以出定入用
		for(TransferOrderItemModel model : itemList) {
			goodMap.put(model.getInGoodsId(), model);
			//库位商品id为key
			kwGoodsMap.put(model.getInGoodsId(), model);
		}

		//调拨入库单中插入数据
		TransferInDepotModel tModel =new TransferInDepotModel();
		tModel.setTransferOrderId(transferOrderModel.getId());//调拨订单ID
		tModel.setTransferOrderCode(transferOrderModel.getCode());//调拨订单编号
		tModel.setMerchantId(transferOrderModel.getMerchantId());//商家ID
		tModel.setMerchantName(transferOrderModel.getMerchantName());//商家名称
		tModel.setContractNo(transferOrderModel.getContractNo());//合同号
		tModel.setInDepotId(transferOrderModel.getInDepotId());//调入仓库id
		tModel.setInDepotName(transferOrderModel.getInDepotName());//调入仓库名称
		tModel.setOutDepotId(transferOrderModel.getOutDepotId());//调出仓库id
		tModel.setOutDepotName(transferOrderModel.getOutDepotName());//调出仓库名称
		tModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
		tModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户名称
		tModel.setTransferDate(transferDate);//调入时间
		tModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_028);//RKZ("028","入库中"), //状态(暂定011待入仓,012 已入仓 028入库中)
//		tModel.setCode(CodeGeneratorUtil.getNo("DBRK"));//调拨单号(跨境宝推来的)
		tModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));//调拨单号(跨境宝推来的)
		tModel.setSource(DERP_ORDER.TRANSFERINDEPOT_SOURCE_2);//单据来源 1-调拨入自动生成 2-接口回推生成
		tModel.setLbxNo(transferOrderModel.getLbxNo());
		tModel.setBuId(transferOrderModel.getBuId());
		tModel.setBuName(transferOrderModel.getBuName());
		tModel.setCreateName("接口回传");
		tModel.setInExternalCode(inExternalCode);
		// 生成调拨入库表
		Long inDepotId = transferInDepotDao.save(tModel);
		// 商品
		List<InvetAddOrSubtractGoodsListJson> subGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//加库存商品
		for(EpassTransferInStirageGoodsListJson goods : inStirageRoot.getGoodsList()) {
			//查询商品
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("merchandiseId", goods.getGoodsId());
			MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(paramMap);

			TransferInDepotItemModel itemModel =new TransferInDepotItemModel();
			itemModel.setTransferDepotId(inDepotId);// 调拨出库id
			itemModel.setInGoodsId(goods.getGoodsId());//调入商品id
			itemModel.setInGoodsNo(goods.getGoodsNo());
			itemModel.setInGoodsName(goods.getGoodsName());
			itemModel.setInGoodsCode(goods.getGoodsCode());
			itemModel.setBarcode(goods.getBarcode());
			itemModel.setInCommbarcode(inMerchandise.getCommbarcode());
			itemModel.setTransferBatchNo(goods.getBatchNo());
			itemModel.setTransferNum(goods.getSalesNum());//正常数量
			itemModel.setWornNum(goods.getWornNum());//坏货数量
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
			// 新增调拨入库单商品
			transferInDepotItemDao.save(itemModel);

			//计算是否过期 字符串 （0 是 1 否）
			String isExpire = DERP.ISEXPIRE_1;
			if(itemModel.getOverdueDate()!=null){
				isExpire = TimeUtils.isNotIsExpireByDate(itemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
			}
			//好品
			if(itemModel.getTransferNum()!=null&&itemModel.getTransferNum().intValue()>0) {
				//拼装加库存商品
				InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
				good.setGoodsId(String.valueOf(itemModel.getInGoodsId()));
				good.setGoodsNo(itemModel.getInGoodsNo());
				good.setGoodsName(itemModel.getInGoodsName());
				good.setBarcode(itemModel.getBarcode());
				good.setBatchNo(itemModel.getTransferBatchNo());
				good.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
				good.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
				good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
				good.setIsExpire(isExpire);//是否过期（0 是 1 否）
				good.setNum(itemModel.getTransferNum());
				//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
				good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
				good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				subGoodsList.add(good);
			}
			//坏品
			if(itemModel.getWornNum()!=null&&itemModel.getWornNum().intValue()>0) {
				//拼装加库存商品
				InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
				good.setGoodsId(String.valueOf(itemModel.getInGoodsId()));
				good.setGoodsNo(itemModel.getInGoodsNo());
				good.setGoodsName(itemModel.getInGoodsName());
				good.setBarcode(itemModel.getBarcode());
				good.setBatchNo(itemModel.getTransferBatchNo());
				good.setProductionDate(TimeUtils.formatDay(itemModel.getProductionDate()));
				good.setOverdueDate(TimeUtils.formatDay(itemModel.getOverdueDate()));
				good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
				good.setIsExpire(isExpire);//是否过期（0 是 1 否）
				good.setNum(itemModel.getWornNum());
				//good.setUnit("2");//单位	字符串 0 托盘 1箱  2 件
				good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//操作类型（调增\调减）	字符串 0 调减 1调增
				good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
				good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
				subGoodsList.add(good);
			}
		}	
		
		//拼装加库存接口参数
		InvetAddOrSubtractRootJson inventoryRoot = new InvetAddOrSubtractRootJson();
		inventoryRoot.setMerchantId(String.valueOf(transferOrderModel.getMerchantId()));
		inventoryRoot.setMerchantName(transferOrderModel.getMerchantName());
		inventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
		inventoryRoot.setDepotType(inDepotInfoMongo.getType());
		inventoryRoot.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
		inventoryRoot.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
		inventoryRoot.setDepotCode(inDepotInfoMongo.getCode());
		inventoryRoot.setDepotName(inDepotInfoMongo.getName());
		inventoryRoot.setOrderNo(tModel.getCode());
		inventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
		inventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0010); //DBRK("0010","调拨入库"),
		inventoryRoot.setSourceDate(TimeUtils.formatFullTime());
		inventoryRoot.setOwnMonth(TimeUtils.format(transferDate, "yyyy-MM"));
		inventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(transferDate));
		inventoryRoot.setBusinessNo(transferOrderModel.getCode());
		inventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
		inventoryRoot.setBuName(transferOrderModel.getBuName());
		inventoryRoot.setGoodsList(subGoodsList);
		//回调参数
		inventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTopic());
		inventoryRoot.setBackTags(MQPushBackOrderEnum.DB_INDEPOT_BACK.getTags());
		inventoryRoot.setCustomParam(new HashMap<String, Object>());

		boolean flag = false;
		InvetAddOrSubtractRootJson subInventoryRoot = new InvetAddOrSubtractRootJson();
		InventoryFreezeRootJson freezeAddLower = new InventoryFreezeRootJson();
		//以入定出（查询调拨出库单是否存在，若存在则不保存调拨出库单）
		if (DERP_SYS.DEPOTMERCHANTREL_ISINDEPENDOUT_1.equals(outRelDepot.getIsInDependOut())
				&& !DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType())) {
			OrderExternalCodeModel transferOutDepotExistModel = new OrderExternalCodeModel();
			transferOutDepotExistModel.setExternalCode(transferOrderModel.getCode());
			// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
			transferOutDepotExistModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5));
			try {
				orderExternalCodeDao.save(transferOutDepotExistModel);
				//查询调拨出库单是否存在
				TransferOutDepotModel transferOutDepotModel = new TransferOutDepotModel();
				transferOutDepotModel.setTransferOrderId(transferOrderModel.getId());
				transferOutDepotModel = transferOutDepotDao.searchByModel(transferOutDepotModel);
				if (transferOutDepotModel == null){
					flag = true;
				}
			} catch (Exception e) {
				LOGGER.error("电商订单外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			}
		}

		if (flag) {
			for(EpassTransferInStirageGoodsListJson goodsJson : inStirageRoot.getGoodsList()) {
			     // 批次效期强校验：1-是 0-否
			    if(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
			    	String batchNo = goodsJson.getBatchNo();
					String productionDate = goodsJson.getProductionDate();
					String overdueDate = goodsJson.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						LOGGER.error("出库仓"+outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
						throw new RuntimeException("出库仓"+outDepotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					}
				}
				//检查报文商品在单据中是否存在
				TransferOrderItemModel orderItem = goodMap.get(goodsJson.getGoodsId());
				if(orderItem == null) {
					LOGGER.error("调拨单未找到商品,订单编号" + orderCode+",货号"+goodsJson.getGoodsId());
					throw new RuntimeException("调拨单未找到商品,订单编号" + orderCode+",货号"+goodsJson.getGoodsId());
				}
			}
			//生成调拨出库单
			TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
			outDepotModel.setTransferOrderId(transferOrderModel.getId());//调拨订单ID
			outDepotModel.setTransferOrderCode(transferOrderModel.getCode());//调拨订单编号
			outDepotModel.setMerchantId(transferOrderModel.getMerchantId());
			outDepotModel.setMerchantName(transferOrderModel.getMerchantName());//商家名称
			outDepotModel.setContractNo(transferOrderModel.getContractNo());//合同号
			outDepotModel.setInDepotId(transferOrderModel.getInDepotId());//调入仓库id
			outDepotModel.setInDepotName(transferOrderModel.getInDepotName());//调入仓库名称
			outDepotModel.setOutDepotId(transferOrderModel.getOutDepotId());//调出仓库id
			outDepotModel.setOutDepotName(transferOrderModel.getOutDepotName());//调出仓库名称
			outDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());//调入客户id
			outDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());//调入客户名称
			outDepotModel.setLbxNo(transferOrderModel.getLbxNo());//LBX号
			outDepotModel.setBuId(transferOrderModel.getBuId());
			outDepotModel.setBuName(transferOrderModel.getBuName());
			outDepotModel.setCreateName("接口回传");
			outDepotModel.setTransferDate(transferDate);//调出时间
			outDepotModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_027);//CKZ("027","出库中"), //状态  '状态 015.待出仓   ,016.已出仓 027出库中',
			outDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK));//调拨出单号(对应调拨出库接口的调拨单号)
			outDepotModel.setOutExternalCode(inExternalCode);
			outDepotModel.setDataSource(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_2);
			//新增
			transferOutDepotDao.save(outDepotModel);

			List<InventoryFreezeGoodsListJson> freezeGoodList = new ArrayList<InventoryFreezeGoodsListJson>();//释放冻结商品列表
			List<InvetAddOrSubtractGoodsListJson> outSubGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//减库存商品

			//复制入库单生成出库单表体和减库存报文
			TransferInDepotModel inDepotModel = new TransferInDepotModel();
			inDepotModel.setTransferOrderId(transferOrderModel.getId());
			inDepotModel = transferInDepotDao.searchByModel(inDepotModel);
			TransferInDepotItemModel inDepotItem = new TransferInDepotItemModel();
			inDepotItem.setTransferDepotId(inDepotModel.getId());
			List<TransferInDepotItemModel> inItemList = transferInDepotItemDao.list(inDepotItem);
			for(TransferInDepotItemModel inItem : inItemList) {
				//调拨单商品
				TransferOrderItemModel orderItem = kwGoodsMap.get(inItem.getInGoodsId());
				//查询调出库位商品
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchandiseId", orderItem.getOutGoodsId());
				MerchandiseInfoMogo outMerchandise = merchandiseInfoMogoDao.findOne(paramMap);

				TransferOutDepotItemModel outDepotItemModel = new TransferOutDepotItemModel();
				outDepotItemModel.setTransferDepotId(outDepotModel.getId());
				outDepotItemModel.setOutGoodsId(orderItem.getOutGoodsId());
				outDepotItemModel.setOutGoodsNo(orderItem.getOutGoodsNo());
				outDepotItemModel.setOutGoodsCode(orderItem.getOutGoodsCode());
				outDepotItemModel.setOutGoodsName(orderItem.getOutGoodsName());
				outDepotItemModel.setBarcode(orderItem.getOutBarcode());
				outDepotItemModel.setOutCommbarcode(orderItem.getOutCommbarcode());
				outDepotItemModel.setTransferBatchNo(inItem.getTransferBatchNo());
				outDepotItemModel.setTransferNum(inItem.getTransferNum());
				outDepotItemModel.setWornNum(inItem.getWornNum());
				outDepotItemModel.setProductionDate(inItem.getProductionDate());//生产日期
				outDepotItemModel.setOverdueDate(inItem.getOverdueDate());//失效日期
				transferOutDepotItemDao.save(outDepotItemModel);

				//计算是否过期 字符串 （0 是 1 否）
				String isExpire = DERP.ISEXPIRE_1;
				if(outDepotItemModel.getOverdueDate()!=null){
					isExpire = TimeUtils.isNotIsExpireByDate(outDepotItemModel.getOverdueDate());//判断是否过期是否过期（0是 1否）
				}
				//好品
				if(outDepotItemModel.getTransferNum() != null && outDepotItemModel.getTransferNum().intValue()>0) {
					InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
					good.setGoodsId(String.valueOf(outDepotItemModel.getOutGoodsId()));//原商品
					good.setGoodsNo(outDepotItemModel.getOutGoodsNo());
					good.setGoodsName(outDepotItemModel.getOutGoodsName());
					good.setBarcode(outDepotItemModel.getBarcode());
					good.setBatchNo(outDepotItemModel.getTransferBatchNo());
					good.setProductionDate(TimeUtils.formatDay(outDepotItemModel.getProductionDate()));
					good.setOverdueDate(TimeUtils.formatDay(outDepotItemModel.getOverdueDate()));
					good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品）	字符串
					good.setIsExpire(isExpire);//是否过期（0 是 1 否）
					good.setNum(outDepotItemModel.getTransferNum());
					good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
					good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					outSubGoodsList.add(good);
				}
                //坏品
				if(outDepotItemModel.getWornNum() != null && outDepotItemModel.getWornNum().intValue()>0) {
					InvetAddOrSubtractGoodsListJson good = new InvetAddOrSubtractGoodsListJson();
					good.setGoodsId(String.valueOf(outDepotItemModel.getOutGoodsId()));//原商品
					good.setGoodsNo(outDepotItemModel.getOutGoodsNo());
					good.setGoodsName(outDepotItemModel.getOutGoodsName());
					good.setBarcode(outDepotItemModel.getBarcode());
					good.setBatchNo(outDepotItemModel.getTransferBatchNo());
					good.setProductionDate(TimeUtils.formatDay(outDepotItemModel.getProductionDate()));
					good.setOverdueDate(TimeUtils.formatDay(outDepotItemModel.getOverdueDate()));
					good.setType(DERP_INVENTORY.INITINVENTORY_TYPE_1);//商品分类 （0 好品 1坏品）	字符串
					good.setIsExpire(isExpire);//是否过期（0 是 1 否）
					good.setNum(outDepotItemModel.getWornNum());
					good.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//操作类型（调增\调减）	字符串 0 调减 1调增
					good.setStockLocationTypeId(String.valueOf(transferOrderModel.getStockLocationTypeId()));
					good.setStockLocationTypeName(transferOrderModel.getStockLocationTypeName());
					outSubGoodsList.add(good);
				}
			}
			//拼装解冻报文
			for(TransferOrderItemModel transferOrderItemModel : itemList) {
				//释放冻结商品
				InventoryFreezeGoodsListJson fgood = new InventoryFreezeGoodsListJson();
				fgood.setGoodsId(String.valueOf(transferOrderItemModel.getOutGoodsId()));
				fgood.setGoodsNo(transferOrderItemModel.getOutGoodsNo());
				fgood.setGoodsName(transferOrderItemModel.getOutGoodsName());
				fgood.setDivergenceDate(TimeUtils.formatFullTime(transferDate));
				fgood.setNum(transferOrderItemModel.getTransferNum());
				//fgood.setUnit("2");//	单位	字符串 0 托盘 1箱  2 件
				freezeGoodList.add(fgood);
			}

			//释放冻结库存
			freezeAddLower.setMerchantId(String.valueOf(merchantId));
			freezeAddLower.setMerchantName(merchantName);
			freezeAddLower.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
			freezeAddLower.setDepotName(outDepotInfoMongo.getName());
			freezeAddLower.setOrderNo(outDepotModel.getCode());// 解冻取调拨出库单号
			freezeAddLower.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
			freezeAddLower.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
			freezeAddLower.setSourceDate(TimeUtils.formatFullTime());
			freezeAddLower.setOperateType(DERP_INVENTORY.INVENTORYFREEZE_OPERATETYPE_1);//冻增\冻减	字符串 （0冻结，1解冻）
			freezeAddLower.setBusinessNo(transferOrderModel.getCode());
			freezeAddLower.setGoodsList(freezeGoodList);

			//拼装减库存接口参数
			subInventoryRoot.setMerchantId(String.valueOf(outDepotModel.getMerchantId()));
			subInventoryRoot.setMerchantName(String.valueOf(outDepotModel.getMerchantName()));
			subInventoryRoot.setTopidealCode(transferOrderModel.getTopidealCode());
			subInventoryRoot.setDepotType(outDepotInfoMongo.getType());
			subInventoryRoot.setIsTopBooks(outDepotInfoMongo.getIsTopBooks());
			subInventoryRoot.setDepotId(String.valueOf(outDepotInfoMongo.getDepotId()));
			subInventoryRoot.setDepotCode(outDepotInfoMongo.getCode());
			subInventoryRoot.setDepotName(outDepotInfoMongo.getName());
			subInventoryRoot.setOrderNo(outDepotModel.getCode());
			subInventoryRoot.setSource(DERP_INVENTORY.INVENTORY_SOURCE_009); //DBDD("009","调拨订单"),
			subInventoryRoot.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0011); //DBCK("0011","调拨出库"),
			subInventoryRoot.setSourceDate(TimeUtils.formatFullTime());
			subInventoryRoot.setOwnMonth(TimeUtils.format(transferDate, "yyyy-MM"));
			subInventoryRoot.setDivergenceDate(TimeUtils.formatFullTime(transferDate));
			subInventoryRoot.setBusinessNo(transferOrderModel.getCode());
			subInventoryRoot.setBuId(String.valueOf(transferOrderModel.getBuId()));
			subInventoryRoot.setBuName(transferOrderModel.getBuName());
			subInventoryRoot.setGoodsList(outSubGoodsList);
			//回调参数
			subInventoryRoot.setBackTopic(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTopic());
			subInventoryRoot.setBackTags(MQPushBackOrderEnum.DB_OUTDEPOT_BACK.getTags());
			subInventoryRoot.setCustomParam(new HashMap<String, Object>());
		}

		//加库存
		JSONObject addjson = JSONObject.fromObject(inventoryRoot);
		rocketMQProducer.send(addjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		if (flag) {
			//把减库存放到最下方防止已入定出出现异常接口报错
			JSONObject jsonFree = JSONObject.fromObject(freezeAddLower);
			JSONObject subjson = JSONObject.fromObject(subInventoryRoot);
			rocketMQProducer.send(jsonFree.toString(), MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_FREEZE_ADD_LOWER.getTags());
			rocketMQProducer.send(subjson.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		}
		return true;
	}

}
