package com.topideal.service.api.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentTypeDao;
import com.topideal.dao.AdjustmentTypeItemDao;
import com.topideal.entity.vo.AdjustmentTypeItemModel;
import com.topideal.entity.vo.AdjustmentTypeModel;
import com.topideal.json.api.v6_3.CCTransferOrderGoodListJson;
import com.topideal.json.api.v6_3.CCTransferOrderRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.TransferOrderService;
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
 * 调拨单回推
 */
@Service
public class TransferOrderServiceImpl implements TransferOrderService{
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TransferOrderServiceImpl.class);
	//类型调整单
	@Autowired
	private AdjustmentTypeDao adjustmentTypeDao;
	//类型调整单商品
	@Autowired
	private AdjustmentTypeItemDao adjustmentTypeItemDao;
	//仓库
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203201500, model = DERP_LOG_POINT.POINT_12203201500_Label,keyword="sourceCode")
	public boolean saveAdjustmentType(String json,String keys,String topics,String tags) throws Exception {
		//解析json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",CCTransferOrderGoodListJson.class);		
		// JSON对象转实体
		CCTransferOrderRootJson rootJson = (CCTransferOrderRootJson) JSONObject.toBean(jsonData, CCTransferOrderRootJson.class, classMap);
		List<CCTransferOrderGoodListJson> goodsList = rootJson.getGoodsList();// 商品信息
		//调整单信息(货号变更出入仓库必须一样)
		String sourceCode = rootJson.getSourceCode();//来源单据号
		String adjustmentTypeName = rootJson.getAdjustmentTypeName();//类型调整名称
		Long inDepotId = rootJson.getInDepotId();// 入仓仓库id
		String inDepotName = rootJson.getInDepotName();//入仓仓库名称
		String inDepotCode =rootJson.getInDepotCode();//入仓仓库编码
		String inDepotType = rootJson.getInDepotType();//入仓仓库类型
		String inDepotIsTopBooks = rootJson.getInDepotIsTopBooks();//入仓是否代销仓


/*		Long OutDepotId = rootJson.getOutDepotId();// 出仓仓库id
		String outDepotName = rootJson.getOutDepotName();//出仓仓库名称
		String OutDepotCode =rootJson.getOutDepotCode();//出仓仓库编码
		String OutDepotType = rootJson.getOutDepotType();//出仓仓库类型
		String OutDepotIsTopBooks = rootJson.getOutDepotIsTopBooks();//出仓是否代销仓
		*/
		
		String codeTimeStr = rootJson.getCodeTime();//单据日期   格式：yyyy-mm-dd HH:mi:ss
		Timestamp codeTime= null;
		if (StringUtils.isNotBlank(codeTimeStr)) {
			if (codeTimeStr.length()==10) {
				codeTime=Timestamp.valueOf(codeTimeStr+" 00:00:00");
			}else {
				codeTime=Timestamp.valueOf(codeTimeStr);
			}
			
		}
		
		
		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> depotInfoMap = new HashMap<>();
		depotInfoMap.put("depotId", inDepotId);//// 调入仓库id
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);
		if (depotInfoMongo == null) {
			LOGGER.error("未查到调入仓库信息,订单编号" + sourceCode);
			throw new RuntimeException("未查到调入仓库信息,订单编号" + sourceCode);
		}
		
		// 批次效期强校验：1-是 0-否
		if (depotInfoMongo.getBatchValidation().matches(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1 + "|" + DERP_SYS.DEPOTINFO_BATCHVALIDATION_2)) {
			for (CCTransferOrderGoodListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBathNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + sourceCode);
					throw new RuntimeException(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + sourceCode);
				}
				
			}
		}
		
		
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(codeTime);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(codeTime);//归属月份
		
		
		Long merchantId = rootJson.getMerchantId();//商家id
		String merchantName = rootJson.getMerchantName();//商家名称
		String topidealCode = rootJson.getTopidealCode();//卓志编码
		
		AdjustmentTypeModel adjustmentTypeModel = new AdjustmentTypeModel();
		adjustmentTypeModel.setSourceCode(sourceCode);
		adjustmentTypeModel.setMerchantId(merchantId);
		adjustmentTypeModel = adjustmentTypeDao.searchByModel(adjustmentTypeModel);
		if (adjustmentTypeModel!=null) {
			LOGGER.error("调拨单回推接口,生成的类型调整单已经存在:来源单据sourceCode:"+sourceCode);
			throw new RuntimeException("调拨单回推接口,生成的类型调整单已经存在:来源单据sourceCode:"+sourceCode);
		}

		//如果商家是卓烨，商品标准品牌为”美赞臣“报错
		/*if("0000134".equals(topidealCode)) {
			for (CCTransferOrderGoodListJson goods : goodsList) {
				String goodsNo = goods.getGoodsNo();
				InventoryLocationMappingMongo oriGoodsNoMappingModel = inventoryLocationMappingMongoDao
						.getOriGoodsNoMappingModel(topidealCode, goodsNo);

				if(oriGoodsNoMappingModel != null) {
					LOGGER.error("商品货号:"+goodsNo +"的母品牌为“美赞臣”");
					throw new RuntimeException("商品货号:"+goodsNo +"的母品牌为“美赞臣”");
				}
			}
		}*/
			
		// 系统当前日期
		Date date1 = new Date();						
		//类型调整单数据注入
		AdjustmentTypeModel adjustmentType =new AdjustmentTypeModel();
		adjustmentType.setAdjustmentTypeName(adjustmentTypeName);
//		adjustmentType.setCode(CodeGeneratorUtil.getNo("LXTZO"));
		adjustmentType.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LXTZO));
		adjustmentType.setMerchantId(merchantId);
		adjustmentType.setMerchantName(merchantName);
		adjustmentType.setSourceCode(sourceCode);
		adjustmentType.setModel(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_2);
		adjustmentType.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020); //待调整
//		adjustmentType.setAdjustmentTime(new Timestamp(new Date().getTime()));
		adjustmentType.setDepotId(inDepotId);
		adjustmentType.setDepotName(inDepotName);
		//单据时间		
		adjustmentType.setCodeTime(codeTime);
		adjustmentType.setPushTime(codeTime);// 归属日期
		adjustmentType.setSource(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_01);
		adjustmentType.setCreateUsername("接口回传");
		//保存类型调整单
		adjustmentTypeDao.save(adjustmentType);
		
		//库货号变更  这个是变更后的信息 变更后的商品增 变更前的商品减					
		// 推送库存的mq			
		/*InvetAddOrSubtractRootJson addMQModel = new InvetAddOrSubtractRootJson();
		addMQModel.setBusinessNo(sourceCode);// 业务单号
		addMQModel.setMerchantId(String.valueOf(merchantId));//商家ID
		addMQModel.setMerchantName(merchantName);//商家名称
		addMQModel.setTopidealCode(topidealCode);//商家编码
		addMQModel.setDepotId(String.valueOf(inDepotId));//仓库ID
		addMQModel.setDepotName(inDepotName);//仓库名称
		addMQModel.setDepotCode(inDepotCode);//仓库编码
		addMQModel.setDepotType(inDepotType);//仓库类型
		addMQModel.setOrderNo(adjustmentType.getCode());//订单号
		addMQModel.setIsTopBooks(inDepotIsTopBooks);//是否代销仓
		addMQModel.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0014);//LETZD("0014","类型调整单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
		addMQModel.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0017);//HHBG("0017","货号变更"), //单据类型
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		addMQModel.setSourceDate(now);//单据时间
		// 获取当前年月
		
		addMQModel.setOwnMonth(yearsMonths);//归属月份
		addMQModel.setDivergenceDate(transferStr);//出入时间*/
								
			
		//商品信息
		List<InvetAddOrSubtractGoodsListJson> itemMQList = new ArrayList<>();//用于存储库存调整的商品
		for(int i=0;i<goodsList.size();i++){  
			CCTransferOrderGoodListJson goods = goodsList.get(i); 
			if(goods==null){
				continue;
			}
			String barcode = goods.getBarcode();// 商品条形码
			String oldBarcode = goods.getOldBarcode();// 原商品条形码
			Long goodsId =goods.getGoodsId();//商品id
			String goodsNo = goods.getGoodsNo();//商品货号
			String goodsName = goods.getGoodsName();//商品名称
			String goodsCode =goods.getGoodsCode();//商品编码
			Long oldGoodsId = goods.getOldGoodsId();//调整前商品id
			String oldGoodsNo = goods.getOldGoodsNo();//调整前商品货号
			String oldGoodsName = goods.getOldGoodsName();//调整前商品名称
			String oldGoodsCode = goods.getOldGoodsCode();//调整前商品编码
			Integer num = goods.getNum();//数量  (说明 跨境宝传来的值48.0 的字符类型   要经过强转)
			String bathNo = goods.getBathNo();// 批次号
			String productionDateStr = goods.getProductionDate();
			String outTallyingUnit = goods.getOutTallyingUnit();//P：托盘，C：箱 , B：件 目前出入的单位一样
			String inTallyingUnit = goods.getInTallyingUnit();//P：托盘，C：箱 , B：件
			Timestamp productionDate=null;
			String productionMQDate=null;
			if (StringUtils.isNotBlank(productionDateStr)) {
				if (productionDateStr.length()==10) {
					productionDate= Timestamp.valueOf(productionDateStr+" 00:00:00");
					productionMQDate=productionDateStr;
				}else {
					productionDate= Timestamp.valueOf(productionDateStr);
					productionMQDate=productionDateStr.substring(0,10);
				}
				
			}
			String overdueDateStr = goods.getOverdueDate();
			Timestamp overdueDate=null;
			String overdueMQDate=null;
			if (StringUtils.isNotBlank(overdueDateStr)) {
				if (overdueDateStr.length()==10) {
					overdueDate= Timestamp.valueOf(overdueDateStr+" 00:00:00");
					overdueMQDate=overdueDateStr;
				}else {
					overdueDate= Timestamp.valueOf(overdueDateStr);
					overdueMQDate=overdueDateStr.substring(0,10);
				}
			}
			//商品数据注入
			AdjustmentTypeItemModel adjustmentTypeItem = new AdjustmentTypeItemModel();
			adjustmentTypeItem.setBarcode(barcode);// 商品条形码
			adjustmentTypeItem.setOldBarcode(oldBarcode);// 原商品条形码
			adjustmentTypeItem.setAdjustTotal(num);
			adjustmentTypeItem.setCreateDate(TimeUtils.getNow());
			adjustmentTypeItem.setGoodsId(goodsId);
			adjustmentTypeItem.setGoodsCode(goodsCode);
			adjustmentTypeItem.setGoodsName(goodsName);
			adjustmentTypeItem.setGoodsNo(goodsNo);
			adjustmentTypeItem.setOldGoodsNo(oldGoodsNo);
			adjustmentTypeItem.setProductionDate(productionDate);// 变更后的生产日期
			adjustmentTypeItem.setOverdueDate(overdueDate);// 变更后的失效日期
			adjustmentTypeItem.setOldProductionDate(productionDate);// 变更前的生产日期
			adjustmentTypeItem.setOldOverdueDate(overdueDate);// 变更前的失效日期
			adjustmentTypeItem.setTAdjustmentTypeId(adjustmentType.getId());
			if ("P".equals(outTallyingUnit)) {
				adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_00);//P：托盘，C：箱 , B：件
			}else if ("C".equals(outTallyingUnit)) {
				adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_01);//P：托盘，C：箱 , B：件
			}else if ("B".equals(outTallyingUnit)) {
				adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_02);//P：托盘，C：箱 , B：件
			}
			adjustmentTypeItem.setIsDamage(DERP.ISDAMAGE_0);// 0好品 1 坏品
			adjustmentTypeItem.setOldBatchNo(bathNo);
			//保存商品信息
			adjustmentTypeItemDao.save(adjustmentTypeItem);
			/*
			 * 库存调整
			 */
			// 入的货号 增  出的货号减(此处为入)
			/*InvetAddOrSubtractGoodsListJson itemMQModel2 = new InvetAddOrSubtractGoodsListJson();
			itemMQModel2.setBarcode(barcode);// 商品条形码
			itemMQModel2.setGoodsId(String.valueOf(goods.getGoodsId()));//商品ID
			itemMQModel2.setGoodsName(goods.getGoodsName());//商品名称
			itemMQModel2.setGoodsNo(goods.getGoodsNo());//商品货号
			itemMQModel2.setBatchNo(bathNo);//批次号
			itemMQModel2.setProductionDate(productionMQDate);//生产日期
			itemMQModel2.setOverdueDate(overdueMQDate);//失效日期
			itemMQModel2.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品 2 过期）
			itemMQModel2.setNum(num);//数量
			if ("P".equals(outTallyingUnit)) {
				itemMQModel2.setUnit(DERP.INVENTORY_UNIT_0);// 香港仓必填
			}else if ("C".equals(outTallyingUnit)) {
				itemMQModel2.setUnit(DERP.INVENTORY_UNIT_1);// 香港仓必填
			}else if ("B".equals(outTallyingUnit)) {
				itemMQModel2.setUnit(DERP.INVENTORY_UNIT_2);// 香港仓必填
			}


			itemMQModel2.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//字符串 0 调减 1调增

			if (StringUtils.isNotBlank(overdueMQDate)) {
				Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
				String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
				itemMQModel2.setIsExpire(isExpire);//是否过期（0是 1否	)
			}else {
				itemMQModel2.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
			}

			itemMQList.add(itemMQModel2);

			InvetAddOrSubtractGoodsListJson itemMQModel1 = new InvetAddOrSubtractGoodsListJson();
			itemMQModel1.setBarcode(oldBarcode);
			itemMQModel1.setGoodsId(String.valueOf(oldGoodsId));//商品ID
			itemMQModel1.setGoodsName(oldGoodsName);//商品名称
			itemMQModel1.setGoodsNo(oldGoodsNo);//商品货号
			itemMQModel1.setBatchNo(bathNo);//批次号
			itemMQModel1.setProductionDate(productionMQDate);//生产日期
			itemMQModel1.setOverdueDate(overdueMQDate);//失效日期
			itemMQModel1.setType(DERP_INVENTORY.INITINVENTORY_TYPE_0);//商品分类 （0 好品 1坏品 2 过期）
			itemMQModel1.setNum(goods.getNum());//数量

			itemMQModel1.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//字符串 0 调减 1调增

			if (StringUtils.isNotBlank(overdueMQDate)) {
				Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
				String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
				itemMQModel1.setIsExpire(isExpire);//是否过期（0是 1否	)
			}else {
				itemMQModel1.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
			}

			if ("P".equals(inTallyingUnit)) {
				itemMQModel1.setUnit(DERP.INVENTORY_UNIT_0);// 香港仓必填
			}else if ("C".equals(inTallyingUnit)) {
				itemMQModel1.setUnit(DERP.INVENTORY_UNIT_1);// 香港仓必填
			}else if ("B".equals(inTallyingUnit)) {
				itemMQModel1.setUnit(DERP.INVENTORY_UNIT_2);// 香港仓必填
			}
			itemMQList.add(itemMQModel1);*/
		}
		/*try {
			addMQModel.setGoodsList(itemMQList);// 商品信息
			//库存回推数据
			Map<String, Object> customParam=new HashMap<String, Object>();
			addMQModel.setBackTags(MQPushBackStorageEnum.STORAGE_TRANSFER_ORDER_PUSH_BACK.getTags());//回调标签
			addMQModel.setBackTopic(MQPushBackStorageEnum.STORAGE_TRANSFER_ORDER_PUSH_BACK.getTopic());//回调主题
			customParam.put("code", adjustmentType.getCode());// 电商订单内部单号
			addMQModel.setCustomParam(customParam);////自定义回调参数
			String jsonMQObject = JSONObject.fromObject(addMQModel).toString();

			rocketMQProducer.send(jsonMQObject, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());

		} catch (Exception e) {
			LOGGER.error("----------------------类型调整单[" + adjustmentType.getCode() + "]库存调整失败----------------------");
		}*/
		return true;
	}

}
