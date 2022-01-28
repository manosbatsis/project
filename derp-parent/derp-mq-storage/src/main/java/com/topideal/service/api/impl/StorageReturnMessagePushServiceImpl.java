package com.topideal.service.api.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.AdjustmentInventoryDao;
import com.topideal.dao.AdjustmentInventoryItemDao;
import com.topideal.entity.vo.AdjustmentInventoryItemModel;
import com.topideal.entity.vo.AdjustmentInventoryModel;
import com.topideal.json.api.v6_4.CCReturnMessageGoodsListJson;
import com.topideal.json.api.v6_4.CCReturnMessageRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.StorageReturnMessagePushService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 仓储退运信息
 * 
 * @author 杨创 2018/7/14
 */
@Service
public class StorageReturnMessagePushServiceImpl implements StorageReturnMessagePushService {
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StorageReturnMessagePushServiceImpl.class);
	@Autowired
	private AdjustmentInventoryDao adjustmentInventoryDao;// 库存调整单
	@Autowired
	private AdjustmentInventoryItemDao AdjustmentInventoryItemDao;// 库存调整单商品
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb
	
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203200700, model = DERP_LOG_POINT.POINT_12203200700_Label,keyword="orderCode")
	public boolean saveStorageReturnMessagePushInfo(String json,String keys,String topics,String tags) throws Exception {

		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",CCReturnMessageGoodsListJson.class);
		CCReturnMessageRootJson rootJson =(CCReturnMessageRootJson)JSONObject.toBean(jsonData, CCReturnMessageRootJson.class,classMap);	
		List<CCReturnMessageGoodsListJson> goodsList = rootJson.getGoodsList();
		String orderCode = rootJson.getOrderCode();// 订单号
		String serveTypes = rootJson.getServeTypes();// 服务类型
																// 10：退运服务（默认）20：销毁服务
																// 30：跨关区转出转关服务(说明销毁服务20是对应仓储的mq,其他是对应订单mq)
		String status = rootJson.getStatus();// 单据状态10：制单30：提交70：成功90：作废
		Long merchantId = rootJson.getMerchantId();// 商家id
		String merchantName = rootJson.getMerchantName();// 商家名称
		String topidealCode = rootJson.getTopidealCode();// 卓志编码
		String depotCode = rootJson.getDepotCode();// 仓库编码

		String transfer = rootJson.getTransferDate();// 订单交易时间
		Timestamp transferDate = null;
		String transferStr=null;
		if (transfer.length() == 10) {
			transferDate = Timestamp.valueOf(transfer + " 00:00:00");// 订单时间	
			transferStr=transfer+ " 00:00:00";
		} else {
			transferDate = Timestamp.valueOf(transfer);// 订单时间 对应交易时间
			transferStr=transfer;
		}
		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> depotInfoMap = new HashMap<>();
		depotInfoMap.put("code", depotCode);// 调出仓库编码
		depotInfoMap.put("isTopBooks", DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);// 是否是代销仓 0 否 1是
		depotInfoMap.put("isValetManage",DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);// 订单查询非代客管理的仓库
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);// 调出仓库信息
		if (depotInfoMap == null) {
			LOGGER.error("根据调拨订单的调出仓库编码code和是否是代销仓isTopBooks查询mongodb未查到仓库信息,订单编号" + orderCode);
			throw new RuntimeException("根据调拨订单的调出仓库编码code和是否是代销仓isTopBooks查询mongodb未查到仓库信息,订单编号" + orderCode);
		}
		if (!"70".equals(status)) {
			LOGGER.error("仓储退运信息只接收状态是70的单,订单号" + orderCode);
			throw new RuntimeException("仓储退运信息只接收状态是70的单,订单号" + orderCode);	
		}
		
		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotInfoMongo.getBatchValidation())) {
			for (CCReturnMessageGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + orderCode);
					throw new RuntimeException(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + orderCode);
				}
				
			}
		}		
		
		AdjustmentInventoryModel adjustmentInventoryModel = new AdjustmentInventoryModel();
		adjustmentInventoryModel.setSourceCode(orderCode);// 来源单号
		adjustmentInventoryModel = adjustmentInventoryDao.searchByModel(adjustmentInventoryModel);

		if (adjustmentInventoryModel != null) {
			LOGGER.error("库存调整单已经存在: 订单号" + orderCode);
			throw new RuntimeException("库存调整单已经存在: 订单号" + orderCode);
		}

		//如果商家是卓烨，商品标准品牌为”美赞臣“报错
		/*if("0000134".equals(topidealCode)) {
			for (CCReturnMessageGoodsListJson goods : goodsList) {
				String goodsNo = goods.getGoodsNo();
				InventoryLocationMappingMongo oriGoodsNoMappingModel = inventoryLocationMappingMongoDao
						.getOriGoodsNoMappingModel(topidealCode, goodsNo);

				if(oriGoodsNoMappingModel != null) {
					LOGGER.error("商品货号:"+goodsNo +"的母品牌为“美赞臣”");
					throw new RuntimeException("商品货号:"+goodsNo +"的母品牌为“美赞臣”");
				}
			}
		}*/

		// 新增库存调整单
		AdjustmentInventoryModel adjustmentModel = new AdjustmentInventoryModel();
//		adjustmentModel.setCode(CodeGeneratorUtil.getNo("KCTZ"));// 库存调整单号
		adjustmentModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_KCTZ));// 库存调整单号
																	// 自生成
		adjustmentModel.setStatus(DERP_STORAGE.ADJUSTMENTINVENTORY_STATUS_020);// 状态 020待调整
		adjustmentModel.setModel(DERP_STORAGE.ADJUSTMENTINVENTORY_MODEL_1);// 1销毁 2 月结损益 3 其他入
		// adjustmentModel.setAdjustmentTypeName("销毁");//类型调整名称
		adjustmentModel.setSourceCode(orderCode);// 来源单据号
		adjustmentModel.setMerchantId(merchantId);// 商家id
		adjustmentModel.setMerchantName(merchantName);// 商家名称
		adjustmentModel.setDepotId(depotInfoMongo.getDepotId());// 盘点仓库id
		adjustmentModel.setDepotName(depotInfoMongo.getName());// 盘点仓库名称
		
		// 获取系统当前时间
		Date date1 = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		String nowStr = formatter.format(date1);// 系统当前时间字符串
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(transferDate);//归属月份跨境传来 
		adjustmentModel.setMonths(yearsMonths);							
		adjustmentModel.setSourceTime(transferDate);// 归属日期
//		adjustmentModel.setAdjustmentTime(Timestamp.valueOf(nowStr));// 调整时间(系统当前时间)
		adjustmentModel.setSource(DERP_STORAGE.ADJUSTMENTINVENTORY_SOURCE_01); //JKHC("01", "接口回传"),
		// 新增库存调整
		adjustmentInventoryDao.save(adjustmentModel);							
		// 新增库存调整单商品
		List<InvetAddOrSubtractGoodsListJson> itemMQList = new ArrayList<>();
		for (CCReturnMessageGoodsListJson goods : goodsList) {			
			// 新增库存调整单商品		
			AdjustmentInventoryItemModel itemModel = new AdjustmentInventoryItemModel();
			itemModel.setTAdjustmentInventoryId(adjustmentModel.getId());// 库存调整id
			itemModel.setGoodsId(goods.getGoodsId());// 商品id
			itemModel.setGoodsCode(goods.getGoodsCode());// 商品编码
			itemModel.setGoodsNo(goods.getGoodsNo());// 商品货号
			itemModel.setGoodsName(goods.getGoodsName());// 商品名称
			itemModel.setBarcode(goods.getBarcode());// 商品条形码
			itemModel.setCommbarcode(goods.getCommbarcode()); //标准条码
			itemModel.setType(DERP_STORAGE.ADJUSTMENT_TYPE_0);// 调整类型0 调减 1 增
			itemModel.setOldBatchNo(goods.getBatchNo());// 原始批次号				
			String productionDateStr = goods.getProductionDate();// 生产日期
			Date productionDate = null;
			String productionMQDate=null;
			if (StringUtils.isNotBlank(productionDateStr)) {
				if (productionDateStr.length()==10) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					productionDate=sdf.parse(productionDateStr);
					productionMQDate=productionDateStr;
				}else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					productionDate=sdf.parse(productionDateStr);
					productionMQDate=productionDateStr.substring(0,10);
				}
				
			}	
			
			String overdueDateStr = goods.getOverdueDate();// 失效日期
			Date overdueDate = null;
			String overdueMQDate=null;
			if (StringUtils.isNotBlank(overdueDateStr)) {
				if (overdueDateStr.length()==10) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					overdueDate=sdf.parse(overdueDateStr);
					overdueMQDate=overdueDateStr;
				}else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					overdueDate=sdf.parse(overdueDateStr);
					overdueMQDate=overdueDateStr.substring(0,10);
				}
				
			}												
			itemModel.setIsDamage(goods.getStockType());// 是否坏品// （0 好品 1坏品 2 过期）														
			itemModel.setProductionDate(productionDate);//生成日期 退运信息没有生效时间
			itemModel.setOverdueDate(overdueDate);//失效日期 退运信息没有失效时间
			itemModel.setAdjustTotal(goods.getNum());// 总调整数量
			// 新增库存调整单商品
			AdjustmentInventoryItemDao.save(itemModel);
			
			// 向推送库存mq中保存商品
			/*InvetAddOrSubtractGoodsListJson itemMQModel = new InvetAddOrSubtractGoodsListJson();
			itemMQModel.setBarcode(goods.getBarcode());// 商品条形码
			itemMQModel.setGoodsId(String.valueOf(goods.getGoodsId()));//商品ID
			itemMQModel.setGoodsName(goods.getGoodsName());//商品名称
			itemMQModel.setGoodsNo(goods.getGoodsNo());//商品货号
			itemMQModel.setBatchNo(goods.getBatchNo());//批次号
			itemMQModel.setProductionDate(productionMQDate);//生产日期
			itemMQModel.setOverdueDate(overdueMQDate);//失效日期

			if (StringUtils.isNotBlank(overdueMQDate)) {
				Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
				String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
				itemMQModel.setIsExpire(isExpire);//是否过期（0是 1否	)
			}else {
				itemMQModel.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
			}
			itemMQModel.setType(goods.getStockType());//商品分类 （0 好品 1坏品 ）			
			itemMQModel.setNum(goods.getNum());//数量
			//itemMQModel.setUnit(unit);// 香港仓必填
			itemMQModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//字符串 0 调减 1调增
			itemMQList.add(itemMQModel);*/
							
		}

		// 推送库存的mq			
		/*InvetAddOrSubtractRootJson addMQModel = new InvetAddOrSubtractRootJson();
		addMQModel.setBusinessNo(orderCode);// 业务单号
		addMQModel.setMerchantId(String.valueOf(merchantId));//商家ID
		addMQModel.setMerchantName(merchantName);//商家名称
		addMQModel.setTopidealCode(topidealCode);//商家编码
		addMQModel.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));//仓库ID
		addMQModel.setDepotName(depotInfoMongo.getName());//仓库名称
		addMQModel.setDepotCode(depotInfoMongo.getCode());//仓库编码
		addMQModel.setDepotType(depotInfoMongo.getType());//仓库类型
		addMQModel.setOrderNo(adjustmentModel.getCode());//订单号
		addMQModel.setIsTopBooks(depotInfoMongo.getIsTopBooks());//是否代销仓
		addMQModel.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0012);//KCTZD("0012","库存调整单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
		addMQModel.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0013);//XH("0013","销毁"), 单据类型
		addMQModel.setSourceDate(nowStr);//单据时间
		// 获取当前年月
				
		addMQModel.setOwnMonth(yearsMonths);//归属月份
		addMQModel.setDivergenceDate(transferStr);//出入时间
		addMQModel.setGoodsList(itemMQList);// 商品信息
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		addMQModel.setBackTags(MQPushBackStorageEnum.STORAGE_RETURN_MESSAGE_PUSH_BACK.getTags());//回调标签
		addMQModel.setBackTopic(MQPushBackStorageEnum.STORAGE_RETURN_MESSAGE_PUSH_BACK.getTopic());//回调主题		
		customParam.put("code", adjustmentModel.getCode());// 电商订单内部单号
		addMQModel.setCustomParam(customParam);////自定义回调参数
		
		String jsonMQObject = JSONObject.fromObject(addMQModel).toString();
		LOGGER.info("仓储退运信息 推送库存MQ 请求报文"+jsonMQObject);
		// 推送
		rocketMQProducer.send(jsonMQObject.toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		*/
		return true;
	}

}
