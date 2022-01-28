package com.topideal.service.api.transfer.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.purchase.OrderExternalCodeDao;
import com.topideal.dao.transfer.*;
import com.topideal.entity.vo.purchase.OrderExternalCodeModel;
import com.topideal.entity.vo.transfer.*;
import com.topideal.json.api.v5_8.ESOutInventoryGoodsListJson;
import com.topideal.json.api.v5_8.ESOutInventoryRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.DepotMerchantRelMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.dao.MerchantBuRelMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.mongo.entity.DepotMerchantRelMongo;
import com.topideal.mongo.entity.MerchandiseInfoMogo;
import com.topideal.service.api.transfer.DBOutInventoryService;
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
 * 杨创
 */
@Service
public class DBOutInventoryServiceImpl implements DBOutInventoryService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DBOutInventoryServiceImpl.class);
	
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
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203137100,model=DERP_LOG_POINT.POINT_12203137100_Label,keyword="orderCode")
	public boolean saveDBOutInventoryInfo(String json,String keys,String topics,String tags) throws Exception {
		System.out.println("出库明细-调拨："+json);
		// 将字符串转成json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",ESOutInventoryGoodsListJson.class);
		
		ESOutInventoryRootJson rootJson = (ESOutInventoryRootJson) JSONObject.toBean(jsonData, ESOutInventoryRootJson.class,classMap);
		
		String orderCode = rootJson.getOrderCode();// 调拨单号		
		Long merchantId = rootJson.getMerchantId();// 企业入仓编号
		String externalCode = rootJson.getExternalCode();
		String deliver = rootJson.getDeliverDate();
		
		TransferOrderModel transferOrderModel =new TransferOrderModel();// 调拨单表
		transferOrderModel.setCode(orderCode);//订单号
		transferOrderModel.setMerchantId(merchantId);
		transferOrderModel = transferOrderDao.searchByModel(transferOrderModel);	
		if(transferOrderModel ==null) {
			LOGGER.error("订单在不存在"+orderCode);
			throw new RuntimeException("订单在所有订单模块都不存在"+orderCode);
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
		//检查调拨单状态
		if(transferOrderModel.getStatus().equals(DERP_ORDER.TRANSFERORDER_STATUS_013)){ //DTJ("013","待提交"),
			LOGGER.error("调拨单状态为待提交,推送失败"+orderCode);
			throw new RuntimeException("调拨单状态为待提交,推送失败"+orderCode);
		}		
		Timestamp deliverDate = TimeUtils.parse(deliver, "yyyy-MM-dd HH:mm:ss");//发货时间

		// 向电商订单唯一标识表插入数据
		OrderExternalCodeModel orderExternalCodeModel = new OrderExternalCodeModel();
		orderExternalCodeModel.setExternalCode(transferOrderModel.getCode());
		orderExternalCodeModel.setOrderSource(Integer.valueOf(DERP_ORDER.ORDEREXTERNALCODE_ORDERSOURCE_5));	// 订单来源  1:电商订单, 2:装载交运 3:销售出库 4:调拨入库 5:调拨出库
		try {
			Long id = orderExternalCodeDao.save(orderExternalCodeModel);
		} catch (Exception e) {
			LOGGER.error("外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
			throw new RuntimeException("外部单号来源表已经存在 单号：" + transferOrderModel.getCode() + "  保存失败");
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
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(outDepotInfoMongo.getType())) {
			LOGGER.error("出库仓为海外仓不走该流程" + orderCode);
			throw new RuntimeException("出库仓为海外仓不走该流程" + orderCode);
		}
		
		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(outDepotInfoMongo.getBatchValidation())) {
			for (ESOutInventoryGoodsListJson goodsListJson : rootJson.getGoodsList()) {
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
		tModel.setStatus(DERP_ORDER.TRANSFEROUTDEPOT_STATUS_015);//CKZ("027","出库中"), //状态  '状态 015.待出仓   ,016.已出仓 027出库中',
		tModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBCK));//调拨出单号(对应调拨出库接口的调拨单号)
		tModel.setOutExternalCode(externalCode);
		tModel.setDataSource(DERP_ORDER.TRANSFEROUTDEPOT_DATASOURCE_2);
		//新增
		transferOutDepotDao.save(tModel);
		// 库存发货时间
		String deliverDateMq = TimeUtils.format(deliverDate, "yyyy-MM-dd HH:mm:ss");

		for(ESOutInventoryGoodsListJson goods : rootJson.getGoodsList()) {
			//新增调拨出库表体
			TransferOutDepotItemModel itemModel = new  TransferOutDepotItemModel();
			itemModel.setTransferDepotId(tModel.getId());//调拨出库ID
			itemModel.setOutGoodsId(goods.getGoodsId());// 调出商品id
			itemModel.setOutGoodsNo(goods.getGoodsNo());//调出商品货号
			itemModel.setOutGoodsName(goods.getGoodsName());//调出商品名称
			itemModel.setOutGoodsCode(goods.getGoodsCode());//调出商品编码
			if ("0".equals(goods.getStockType())) {
				itemModel.setTransferNum(goods.getNum());//好品数量
			}else {
				itemModel.setWornNum(goods.getNum());//坏品数量
			}
			itemModel.setTransferBatchNo(goods.getBatchNo());//调拨批次
			itemModel.setBarcode(goods.getBarcode());
			itemModel.setOutCommbarcode(goods.getCommbarcode());
			String productionDate = goods.getProductionDate();//生产日期
			if (StringUtils.isNotBlank(productionDate)) {
				itemModel.setProductionDate(TimeUtils.StringToDate(productionDate));//生产日期
			}
			String overdueDate = goods.getOverdueDate();//生产日期
			if (StringUtils.isNotBlank(overdueDate)) {
				itemModel.setOverdueDate(TimeUtils.StringToDate(overdueDate));//失效日期
			}
			//新增商品
			transferOutDepotItemDao.save(itemModel);
		}

		boolean flag = false;
		//调拨入仓仓库是 在对应商家下的“以出定入”标识为“是”的，需进行以出定入（查询调拨入库单是否存在，若存在则不保存调拨入库单）
		//如果调出库仓库为香港仓时，不走以出定入
		if(DERP_SYS.DEPOTMERCHANTREL_ISOUTDEPENDIN_1.equals(inRelDepot.getIsOutDependIn())) {
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
		// 已出定入
		if (flag) {
			if (DERP_SYS.DEPOTINFO_TYPE_C.equals(inDepotInfoMongo.getType())) {
				LOGGER.error("入库仓为海外仓不走该流程" + orderCode);
				throw new RuntimeException("入库仓为海外仓不走该流程" + orderCode);
			}

			for(ESOutInventoryGoodsListJson goodsJson : rootJson.getGoodsList()) {
				if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(inDepotInfoMongo.getBatchValidation())
						|| DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(inDepotInfoMongo.getBatchValidation())) {
					String batchNo = goodsJson.getBatchNo();
					String productionDate = goodsJson.getProductionDate();
					String overdueDate = goodsJson.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						LOGGER.error(inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验，"+"批次和效期不能为空,订单号:" + orderCode);
						throw new RuntimeException(inDepotInfoMongo.getName()+",设置了批次效期强校验，或入库强校验，"+"批次和效期不能为空,订单号:" + orderCode);
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
			inDepotModel.setStatus(DERP_ORDER.TRANSFERINDEPOT_STATUS_011);//RKZ("028","入库中"),// 状态  011.待入仓  ,012.已入仓 028.入库中'
			inDepotModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_DBRK));
			inDepotModel.setSource(DERP_ORDER.TRANSFERINDEPOT_SOURCE_2);// 1-调拨入自动生成 2-接口回推生成
			inDepotModel.setInCustomerId(transferOrderModel.getInCustomerId());
			inDepotModel.setInCustomerName(transferOrderModel.getInCustomerName());
			inDepotModel.setLbxNo(transferOrderModel.getLbxNo());// LBX号
			inDepotModel.setBuName(transferOrderModel.getBuName());
			inDepotModel.setBuId(transferOrderModel.getBuId());
			inDepotModel.setCreateName("接口回传");
			inDepotModel.setInExternalCode(externalCode);
			// 调拨入库单插入数据
			transferInDepotDao.save(inDepotModel);

			List<InvetAddOrSubtractGoodsListJson> insubGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();//加库存商品
			//复制出库单生成入库单表体和加库存报文
			TransferOutDepotModel outDepotModel = new TransferOutDepotModel();
			outDepotModel.setTransferOrderId(transferOrderModel.getId());
			outDepotModel = transferOutDepotDao.searchByModel(outDepotModel);
			TransferOutDepotItemModel outDepotItem = new TransferOutDepotItemModel();
			outDepotItem.setTransferDepotId(outDepotModel.getId());
			List<TransferOutDepotItemModel> outItemList = transferOutDepotItemDao.list(outDepotItem);
			for(TransferOutDepotItemModel outItem : outItemList) {
				//调拨单商品
				TransferOrderItemModel orderItem = kwGoodsMap.get(outItem.getOutGoodsId());
				//查询调入库位商品
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("merchandiseId", orderItem.getInGoodsId());
				MerchandiseInfoMogo inMerchandise = merchandiseInfoMogoDao.findOne(paramMap);

				TransferInDepotItemModel inDepotItemModel = new TransferInDepotItemModel();
				inDepotItemModel.setTransferDepotId(inDepotModel.getId());// 调拨入库ID
				inDepotItemModel.setInGoodsId(orderItem.getInGoodsId());//库位商品
				inDepotItemModel.setInGoodsNo(orderItem.getInGoodsNo());// 调入商品货号
				inDepotItemModel.setInGoodsName(orderItem.getInGoodsName());//调入商品名称
				inDepotItemModel.setInGoodsCode(orderItem.getInGoodsCode());// 调入商品编码
				inDepotItemModel.setInCommbarcode(inMerchandise.getCommbarcode());//调入商品标准条码
				inDepotItemModel.setBarcode(inMerchandise.getBarcode());
				inDepotItemModel.setTransferBatchNo(outItem.getTransferBatchNo());// 调拨批次
				inDepotItemModel.setTransferNum(outItem.getTransferNum());// 调拨数量
				inDepotItemModel.setWornNum(outItem.getWornNum());// 坏品数量
				inDepotItemModel.setProductionDate(outItem.getProductionDate());//生产日期
				inDepotItemModel.setOverdueDate(outItem.getOverdueDate());//失效日期
				transferInDepotItemDao.save(inDepotItemModel);
			}

		}

		return true;
	}

}
