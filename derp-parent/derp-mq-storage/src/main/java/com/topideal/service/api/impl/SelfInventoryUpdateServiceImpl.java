package com.topideal.service.api.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.AdjustmentTypeDao;
import com.topideal.dao.AdjustmentTypeItemDao;
import com.topideal.entity.vo.AdjustmentTypeItemModel;
import com.topideal.entity.vo.AdjustmentTypeModel;
import com.topideal.json.api.v6_2.CCSelfInventoryUpdateGoodsListJson;
import com.topideal.json.api.v6_2.CCSelfInventoryUpdateRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.SelfInventoryUpdateService;
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
 * 自营库存更新
 */
@Service
public class SelfInventoryUpdateServiceImpl implements SelfInventoryUpdateService{
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(SelfInventoryUpdateServiceImpl.class);
	//类型调整单
	@Autowired
	private AdjustmentTypeDao adjustmentTypeDao;
	//类型调整单商品
	@Autowired
	private AdjustmentTypeItemDao adjustmentTypeItemDao;
	//MQ
	@Autowired
	private RMQProducer rocketMQProducer;
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb

	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203201400, model = DERP_LOG_POINT.POINT_12203201400_Label,keyword="sourceCode")
	public boolean saveAdjustmentType(String json,String keys,String topics,String tags) throws Exception {
		//解析json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",CCSelfInventoryUpdateGoodsListJson.class);
		CCSelfInventoryUpdateRootJson rootJson = (CCSelfInventoryUpdateRootJson)JSONObject.toBean(jsonData, CCSelfInventoryUpdateRootJson.class,classMap);
		//调整单信息
		String sourceCode = rootJson.getSourceCode();//来源单据号
		Long merchantId = rootJson.getMerchantId();//商家id
		String merchantName = rootJson.getMerchantName();//商家名称
		String topidealCode = rootJson.getTopidealCode();//卓志编码					
		Long depotId = rootJson.getDepotId();//仓库id
		String depotName = rootJson.getDepotName();//仓库名称
		String depotCode = rootJson.getDepotCode();//仓库编码
		String depotType = rootJson.getDepotType();//仓库类型
		String depotIsTopBooks = rootJson.getDepotIsTopBooks();//是否代销仓
		String orderType = rootJson.getOrderType();// 调整类型 05：好坏品互转   06：效期调整    	
		String adjustmentTypeName = rootJson.getAdjustmentTypeName();//类型调整名称
		String codeTimeStr = rootJson.getCodeTime();//单据日期   格式：yyyy-mm-dd HH:mi:ss	
		List<CCSelfInventoryUpdateGoodsListJson> goodsList = rootJson.getGoodsList();// 商品信息
		Timestamp codeTime=null;//调拨时间
		if (StringUtils.isNotBlank(codeTimeStr)) {
			if (codeTimeStr.length()==10) {
				codeTime=Timestamp.valueOf(codeTimeStr+" 00:00:00");	//调拨时间
			}else {
				codeTime=Timestamp.valueOf(codeTimeStr);//调拨时间
			}
		}
		String pushTimeStr = jsonData.getString("pushTime");//推送时间   格式：yyyy-mm-dd HH:mi:ss
		Timestamp pushTime=null;
		if (StringUtils.isNotBlank(pushTimeStr)) {
			if (pushTimeStr.length()==10) {
				pushTime=Timestamp.valueOf(pushTimeStr+" 00:00:00");//推送时间 
			}else {
				pushTime=Timestamp.valueOf(pushTimeStr);//推送时间 
			}
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(pushTime);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(pushTime);//归属月份
		
		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> depotInfoMap = new HashMap<>();
		depotInfoMap.put("depotId", depotId);//// 调入仓库id
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);
		if (depotInfoMongo == null) {
			LOGGER.error("未查到调入仓库信息,订单编号" + sourceCode);
			throw new RuntimeException("未查到调入仓库信息,订单编号" + sourceCode);
		}
		
		// 批次效期强校验：1-是 0-否
		if ("05".equals(orderType)) {
			if (depotInfoMongo.getBatchValidation().matches(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1 + "|" + DERP_SYS.DEPOTINFO_BATCHVALIDATION_2)) {
				for (CCSelfInventoryUpdateGoodsListJson goodsListJson : rootJson.getGoodsList()) {
					String batchNo = goodsListJson.getGoodsBatch();
					String productionDate = goodsListJson.getProductionDate();
					String overdueDate = goodsListJson.getOverdueDate();
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						LOGGER.error(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + sourceCode);
						throw new RuntimeException(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + sourceCode);
					}

				}
			}
		}

		if ("06".equals(orderType)) {
			for (CCSelfInventoryUpdateGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getGoodsBatch();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				String oldProductionDate = goodsListJson.getOldProductionDate();//原生产日期
				String oldOverdueDate = goodsListJson.getOldOverdueDate();//原失效日期

				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)
						||StringUtils.isBlank(oldProductionDate)||StringUtils.isBlank(oldOverdueDate)) {
					LOGGER.error(depotInfoMongo.getName()+",效期调整设置了批次效期强校验"+"批次和效期和原始效期不能为空,订单号:" + sourceCode);
					throw new RuntimeException(depotInfoMongo.getName()+",效期调整设置了批次效期强校验"+"批次和效期和原始效期不能为空,订单号:" + sourceCode);
				}
			}
		}
		
		
		// 查询类型调整单是否存在
		AdjustmentTypeModel adjustmentTypeModel =new AdjustmentTypeModel();
		adjustmentTypeModel.setSourceCode(sourceCode);// 单据来源
		adjustmentTypeModel.setMerchantId(merchantId);// 商家
		adjustmentTypeModel = adjustmentTypeDao.searchByModel(adjustmentTypeModel);
		if (adjustmentTypeModel!=null) {
			LOGGER.error("类型调整单已经存在,来源单号:sourceCode"+sourceCode);
			throw new RuntimeException("类型调整单已经存在,来源单号:sourceCode"+sourceCode);			
		}

		//如果商家是卓烨，商品标准品牌为”美赞臣“报错
		/*if("0000134".equals(topidealCode)) {
			for (CCSelfInventoryUpdateGoodsListJson goods : goodsList) {
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
		// 好坏品互转   涉及调龙生
		if ("05".equals(orderType)) {						
			// 加的map
			Map<String, Object> addMap= new  HashMap<>();
			// 减的map
			Map<String, Object> subMap= new  HashMap<>();
			//  生成类型调整单前 校验 好坏品的数量是否相等 如果不能进行拦截
			for(int i=0;i<goodsList.size();i++){
				CCSelfInventoryUpdateGoodsListJson goods = goodsList.get(i); 
				if(goods==null){
					continue;
				}
				String checkStoreQty = goods.getStoreQty();//调整数量（正数为增加，负数为减少）
				Integer num = goods.getNum();
				Long goodsId = goods.getGoodsId();
				String goodsBatch = goods.getGoodsBatch();
				String key =goodsBatch+String.valueOf(goodsId);
				if (checkStoreQty.contains("-")) {										
					if (subMap.containsKey(key)) {
						Integer num1 = (Integer) subMap.get(key);
						subMap.put(key, num1+num);
					}else {
						subMap.put(key, num);
					}
				}else {
					if (addMap.containsKey(key)) {
						Integer num1 = (Integer) addMap.get(key);
						addMap.put(key, num1+num);
					}else {						
						addMap.put(key, num);
					}					
				}								
			}
						
			if (addMap.size()!=subMap.size()||addMap.size()==0||subMap.size()==0) {
				LOGGER.error("类型调整单好坏品互转加的数量和减的数量不相同,来源单号:sourceCode"+sourceCode);
				throw new RuntimeException("类型调整单好坏品互转加的数量和减的数量不相同,来源单号:sourceCode"+sourceCode);
			}
			
			for (String subkey: subMap.keySet()) {
				if (!addMap.containsKey(subkey)) {					
					LOGGER.error("类型调整单好坏品互转加的数量和减的数量不相同,来源单号:sourceCode"+sourceCode);
					throw new RuntimeException("类型调整单好坏品互转加的数量和减的数量不相同,来源单号:sourceCode"+sourceCode);					
				}	
				Integer subkeyNum = (Integer) subMap.get(subkey);
				for (String addkey : addMap.keySet()) {
					if (subkey.equals(addkey)) {
						Integer addkeyNum = (Integer) addMap.get(addkey);						
						if (!subkeyNum.equals(addkeyNum)) {
							LOGGER.error("类型调整单好坏品互转加的数量和减的数量不相同,来源单号:sourceCode"+sourceCode);
							throw new RuntimeException("类型调整单好坏品互转加的数量和减的数量不相同,来源单号:sourceCode"+sourceCode);									
						}												
					}
					
				}
				
			}
			
			
			//类型调整单数据注入
			AdjustmentTypeModel adjustmentType =new AdjustmentTypeModel();		
//			adjustmentType.setCode(CodeGeneratorUtil.getNo("LXTZO"));// 调整单号 自生成
			adjustmentType.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LXTZO));// 调整单号 自生成
			adjustmentType.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020);//YTZ("019","已调整"),DTZ("020","待调整"),CLZ("022","处理中"),
			if ("05".equals(orderType)) {
				adjustmentType.setModel(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_1);//1:好坏品 互转    3:效期调整
			}		
			adjustmentType.setAdjustmentTypeName(adjustmentTypeName);//类型调整名称
			adjustmentType.setSourceCode(sourceCode);//来源单据号		
			adjustmentType.setMerchantId(merchantId);// 商家id
			adjustmentType.setMerchantName(merchantName);// 商家名称		
			adjustmentType.setDepotId(depotId);// 仓库id
			adjustmentType.setDepotName(depotName);// 仓库名称
			adjustmentType.setCodeTime(codeTime);//调拨时间
			adjustmentType.setPushTime(pushTime);// 推送时间
			adjustmentType.setSource(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_01);
			adjustmentType.setCreateUsername("接口回传");
//			adjustmentType.setAdjustmentTime(new Timestamp(new Date().getTime()));// 调整时间是当前时间
			//保存类型调整单
			adjustmentTypeDao.save(adjustmentType);
			
			// 获取系统当前时间保存到 调整时间里面
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String now1 = sf.format(new Date());//
			
			
				
			
			//新增类型调整单商品	
			List<InvetAddOrSubtractGoodsListJson> goodsMQList = new ArrayList();
			for(int i=0;i<goodsList.size();i++){  
                CCSelfInventoryUpdateGoodsListJson goods = goodsList.get(i); 
				if(goods==null){
					continue;
				}
				Long goodsId = goods.getGoodsId();//商品id
				String goodsNo = goods.getGoodsNo();//商品货号
				String goodsName = goods.getGoodsName();//商品名称
				String goodsCode = goods.getGoodsCode();//商品编码
				String barcode = goods.getBarcode();//商品条码
				String goodsBatch = goods.getGoodsBatch();//商品批次号
				String isDamageStr = goods.getIsDamage();//是否坏品  1：好品，2：坏品
				String tallyingUnit= goods.getTallyingUnit();// 理货单位
				String isDamage=null;
				// 好品 0 好品  1 坏品  ( 跨境宝1坏品 2好品)
				if ("2".equals(isDamageStr)) {
					isDamage = DERP.ISDAMAGE_0;
				}
				// 坏品
				if ("1".equals(isDamageStr)) {
					isDamage = DERP.ISDAMAGE_1;
				}
				String storeQty = goods.getStoreQty();//调整数量（正数为增加，负数为减少）
				String productionDateStr = goods.getProductionDate();//生产日期
				Timestamp productionDate=null;// 生产日期
				String productionMQDate=null;
				if (StringUtils.isNotBlank(productionDateStr)) {
					if (productionDateStr.length()==10) {
						productionDate=Timestamp.valueOf(productionDateStr+" 00:00:00");//生产日期
						productionMQDate=productionDateStr;
					}else {
						productionDate=Timestamp.valueOf(productionDateStr);//生产日期
						productionMQDate=productionDateStr.substring(0,10);
					}				
				}
				String overdueDateStr = goods.getOverdueDate();//失效日期
				Timestamp overdueDate=null;// 失效日期
				String overdueMQDate=null;
				if (StringUtils.isNotBlank(overdueDateStr)) {
					if (overdueDateStr.length()==10) {
						overdueDate=Timestamp.valueOf(overdueDateStr+" 00:00:00");//生产日期
						overdueMQDate=overdueDateStr;
					}else {
						overdueDate=Timestamp.valueOf(overdueDateStr);//生产日期
						overdueMQDate=overdueDateStr.substring(0,10);
					}				
				}
				String oldProductionDateStr = goods.getOldProductionDate();//原生产日期
				Timestamp oldProductionDate=null;// 原生产日期
				if (StringUtils.isNotBlank(oldProductionDateStr)) {
					if (oldProductionDateStr.length()==10) {
						oldProductionDate=Timestamp.valueOf(oldProductionDateStr+" 00:00:00");//原生产日期
					}else {
						oldProductionDate=Timestamp.valueOf(oldProductionDateStr);//原生产日期
					}				
				}
				
				String oldOverdueDateStr = goods.getOldOverdueDate();//原失效日期
				Timestamp oldOverdueDate=null;// 原生产日期
				if (StringUtils.isNotBlank(oldOverdueDateStr)) {
					if (oldProductionDateStr.length()==10) {
						oldOverdueDate=Timestamp.valueOf(oldOverdueDateStr+" 00:00:00");//原失效日期
					}else {
						oldOverdueDate=Timestamp.valueOf(oldOverdueDateStr);//原失效日期
					}				
				}
				
				Integer num = goods.getNum();//数量（api端已经转化 绝对值）
				
				//商品数据注入
				AdjustmentTypeItemModel adjustmentTypeItem = new AdjustmentTypeItemModel();
				adjustmentTypeItem.setTAdjustmentTypeId(adjustmentType.getId());//类型调整id
				adjustmentTypeItem.setAdjustTotal(num);//总调整数量
				adjustmentTypeItem.setGoodsId(goodsId);//商品id
				adjustmentTypeItem.setGoodsCode(goodsCode);//商品编码
				adjustmentTypeItem.setGoodsNo(goodsNo);//商品货号
				adjustmentTypeItem.setGoodsName(goodsName);//商品名称
				adjustmentTypeItem.setBarcode(barcode);//商品条形码					
				adjustmentTypeItem.setOldBatchNo(goodsBatch);//原批次号
				adjustmentTypeItem.setOldGoodsNo(goodsNo);//调整前货号				
				//调整后 商品增加 调整前端商品减少 (不用这个逻辑了)
				// + 为调整后 ,-为调整前
				if(DERP.ISDAMAGE_0.equals(isDamage)){//好品
					if(storeQty.contains("-")){//是否带负号
						adjustmentTypeItem.setOldGoodType("好品-");//调整前商品类型       	
						adjustmentTypeItem.setIsDamage(isDamage);
						adjustmentTypeItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_0);
					}else{
						adjustmentTypeItem.setNewGoodType("好品+");//调整后商品类型	
						adjustmentTypeItem.setIsDamage(isDamage);
						adjustmentTypeItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_1);
					}
				}else{// 如果是坏品 
					if(storeQty.contains("-")){//是否带负号
						adjustmentTypeItem.setOldGoodType("坏品-");//调整前商品类型  	
						adjustmentTypeItem.setIsDamage(isDamage);
						adjustmentTypeItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_0);
					}else{
						adjustmentTypeItem.setNewGoodType("坏品+");//调整后商品类型	
						adjustmentTypeItem.setIsDamage(isDamage);
						adjustmentTypeItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_1);
					}
				}						
				adjustmentTypeItem.setOverdueDate(overdueDate);//失效日期			
				adjustmentTypeItem.setProductionDate(productionDate);	// 生产日期			
				adjustmentTypeItem.setOldOverdueDate(oldOverdueDate);// 原生产日期				
				adjustmentTypeItem.setOldProductionDate(oldProductionDate);// 原失效日期
				// 海外仓理货单位
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType)) {
					//P：托盘，C：箱 , B：件
					if ("P".equals(tallyingUnit)) {
						adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_00);
					}else if ("C".equals(tallyingUnit)) {						
						adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_01);
					}else if ("B".equals(tallyingUnit)) {						
						adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_02);
					}
					
				}
				//保存商品信息
				adjustmentTypeItemDao.save(adjustmentTypeItem);
				/*
				 * 库存调整
				 */
				/*InvetAddOrSubtractGoodsListJson goodsMQ = new  InvetAddOrSubtractGoodsListJson();
				goodsMQ.setBarcode(barcode);// 商品条形码
				goodsMQ.setGoodsId(String.valueOf(goodsId));// 商品id
				goodsMQ.setGoodsName(goodsName);// 商品名称
				goodsMQ.setGoodsNo(goodsNo);// 商品货号
				goodsMQ.setBatchNo(goodsBatch);// 商品批次
				//String production = sdf2.format(productionDate);				
				goodsMQ.setProductionDate(productionMQDate);//生产日期						
				//String overdue = sdf2.format(overdueDate);				
				goodsMQ.setOverdueDate(overdueMQDate);// 失效日期				
							
				goodsMQ.setNum(num);// 数量
				goodsMQ.setType(isDamage);//商品分类 （0 好品 1坏品 ）
				if (storeQty.contains("-")) {
					goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 字符串 0 调减 1调增
				}else {
					goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 字符串 0 调减 1调增
				}			

				if (StringUtils.isNotBlank(overdueMQDate)) {;
					Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
					String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
					goodsMQ.setIsExpire(isExpire);//是否过期（0是 1否	)
				}else {
					goodsMQ.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
				}
				
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType)) {
					//P：托盘，C：箱 , B：件
					if ("P".equals(tallyingUnit)) {
						goodsMQ.setUnit(DERP.INVENTORY_UNIT_0);
					}else if ("C".equals(tallyingUnit)) {						
						goodsMQ.setUnit(DERP.INVENTORY_UNIT_1);
					}else if ("B".equals(tallyingUnit)) {						
						goodsMQ.setUnit(DERP.INVENTORY_UNIT_2);
					}
					
				}
				
				goodsMQList.add(goodsMQ);*/
			}
			
			// 推仓储MQ json			
			/*InvetAddOrSubtractRootJson addOrSubtractModel= new InvetAddOrSubtractRootJson();
			addOrSubtractModel.setBusinessNo(sourceCode);// 业务单号
			addOrSubtractModel.setMerchantId(String.valueOf(merchantId));// 商家id
			addOrSubtractModel.setMerchantName(merchantName);// 商家名称
			addOrSubtractModel.setTopidealCode(topidealCode);// 卓志编码
			addOrSubtractModel.setDepotId(String.valueOf(depotId));// 仓库id
			addOrSubtractModel.setDepotName(depotName);// 仓库名称		
			addOrSubtractModel.setDepotType(depotType);// 仓库类型
			addOrSubtractModel.setDepotCode(depotCode);// 仓库编码
			addOrSubtractModel.setIsTopBooks(depotIsTopBooks);// 是否是代销仓库
			addOrSubtractModel.setOrderNo(adjustmentType.getCode()); // 单据来源			
			addOrSubtractModel.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0014);//LETZD("0014","类型调整单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
			addOrSubtractModel.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0016);//HHHZ("0016","好坏互转"), //单据类型
			addOrSubtractModel.setSourceDate(now1);// 单据时间		
			
			addOrSubtractModel.setOwnMonth(yearsMonths);//归属月份
			addOrSubtractModel.setDivergenceDate(transferStr);//出入时间					
			addOrSubtractModel.setGoodsList(goodsMQList);// 把商品保存到  实体中
			
			Map<String, Object> customParam=new HashMap<String, Object>();
			addOrSubtractModel.setBackTags(MQPushBackStorageEnum.STORAGE_SELF_INVENTORY_PUSH_BACK.getTags());//回调标签
			addOrSubtractModel.setBackTopic(MQPushBackStorageEnum.STORAGE_SELF_INVENTORY_PUSH_BACK.getTopic());//回调主题		
			customParam.put("code", adjustmentType.getCode());// 类型调整单内部单号
			addOrSubtractModel.setCustomParam(customParam);////自定义回调参数
			String jsonMQStr = JSONObject.fromObject(addOrSubtractModel).toString();
			rocketMQProducer.send(jsonMQStr, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());*/
		}
		
		// 效期调整   
		if ("06".equals(orderType)) {
			//类型调整单数据注入
			AdjustmentTypeModel adjustmentType =new AdjustmentTypeModel();		
//			adjustmentType.setCode(CodeGeneratorUtil.getNo("LXTZO"));// 调整单号 自生成
			adjustmentType.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LXTZO));// 调整单号 自生成
			adjustmentType.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_022);//YTZ("019","已调整"),DTZ("020","待调整"),CLZ("022","处理中"),
			
			if ("06".equals(orderType)) {
				adjustmentType.setModel(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_3);//1:好坏品 互转    3:效期调整
			}
			adjustmentType.setAdjustmentTypeName(adjustmentTypeName);//类型调整名称
			adjustmentType.setSourceCode(sourceCode);//来源单据号		
			adjustmentType.setMerchantId(merchantId);// 商家id
			adjustmentType.setMerchantName(merchantName);// 商家名称		
			adjustmentType.setDepotId(depotId);// 仓库id
			adjustmentType.setDepotName(depotName);// 仓库名称
			adjustmentType.setCodeTime(codeTime);//调整时间
			adjustmentType.setPushTime(pushTime);// 推送时间
			adjustmentType.setSource(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_01);
			adjustmentType.setCreateUsername("接口回传");
			adjustmentType.setAdjustmentTime(new Timestamp(new Date().getTime()));// 调整时间
			//保存类型调整单
			adjustmentTypeDao.save(adjustmentType);
			// 获取系统当前时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String now = sdf.format(new Date());
			
			//新增类型调整单商品
			List<InvetAddOrSubtractGoodsListJson> xQTZMQList = new ArrayList();
			for(int i=0;i<goodsList.size();i++){  
                CCSelfInventoryUpdateGoodsListJson goods = goodsList.get(i); 
				if(goods==null){
					continue;
				}
				
				Long goodsId = goods.getGoodsId();//商品id
				String goodsNo = goods.getGoodsNo();//商品货号
				String goodsName = goods.getGoodsName();//商品名称
				String goodsCode = goods.getGoodsCode();//商品编码
				String barcode = goods.getBarcode();//商品条码
				String goodsBatch = goods.getGoodsBatch();//商品批次号
				String isDamageStr = goods.getIsDamage();//是否坏品  1：坏品，2：好品
				String tallyingUnit = goods.getTallyingUnit();//P：托盘，C：箱 , B：件
				String isDamage=null;
				if ("2".equals(isDamageStr)) {
					isDamage=DERP.ISDAMAGE_0;
				}
				if ("1".equals(isDamageStr)) {
					isDamage=DERP.ISDAMAGE_1;
				}
				String storeQty = goods.getStoreQty();//调整数量（正数为增加，负数为减少）
				String productionDateStr = goods.getProductionDate();//生产日期
				Timestamp productionDate=null;// 生产日期
				String productionMQDate=null;
				if (StringUtils.isNotBlank(productionDateStr)) {
					if (productionDateStr.length()==10) {
						productionDate=Timestamp.valueOf(productionDateStr+" 00:00:00");//生产日期
						productionMQDate=productionDateStr;
					}else {
						productionDate=Timestamp.valueOf(productionDateStr);//生产日期
						productionMQDate=productionDateStr.substring(0,10);
					}				
				}
				String overdueDateStr = goods.getOverdueDate();//失效日期
				Timestamp overdueDate=null;// 失效日期
				String overdueMQDate=null;
				if (StringUtils.isNotBlank(overdueDateStr)) {
					if (overdueDateStr.length()==10) {
						overdueDate=Timestamp.valueOf(overdueDateStr+" 00:00:00");//生产日期
						overdueMQDate=overdueDateStr;
					}else {
						overdueDate=Timestamp.valueOf(overdueDateStr);//生产日期
						overdueMQDate=overdueDateStr.substring(0,10);
					}				
				}
				String oldProductionDateStr = goods.getOldProductionDate();//原生产日期
				Timestamp oldProductionDate=null;// 原生产日期
				String oldProductionMQDate=null;
				if (StringUtils.isNotBlank(oldProductionDateStr)) {
					if (oldProductionDateStr.length()==10) {
						oldProductionDate=Timestamp.valueOf(oldProductionDateStr+" 00:00:00");//原生产日期
						oldProductionMQDate=oldProductionDateStr;
					}else {
						oldProductionDate=Timestamp.valueOf(oldProductionDateStr);//原生产日期
						oldProductionMQDate=oldProductionDateStr.substring(0,10);
					}				
				}
				
				String oldOverdueDateStr = goods.getOldOverdueDate();//原失效日期
				Timestamp oldOverdueDate=null;// 原生产日期
				String oldOverdueMQDate=null;
				if (StringUtils.isNotBlank(oldOverdueDateStr)) {
					if (oldOverdueDateStr.length()==10) {
						oldOverdueDate=Timestamp.valueOf(oldOverdueDateStr+" 00:00:00");//原失效日期
						oldOverdueMQDate=oldOverdueDateStr;
					}else {
						oldOverdueDate=Timestamp.valueOf(oldOverdueDateStr);//原失效日期
						oldOverdueMQDate=oldOverdueDateStr.substring(0,10);
					}				
				}
				
				Integer num = goods.getNum();//数量（api端已经转化绝对值）			
				//商品数据注入
				AdjustmentTypeItemModel adjustmentTypeItem = new AdjustmentTypeItemModel();
				adjustmentTypeItem.setTAdjustmentTypeId(adjustmentType.getId());//类型调整id
				adjustmentTypeItem.setAdjustTotal(Integer.valueOf(num.toString()));//总调整数量
				adjustmentTypeItem.setGoodsId(goodsId);//商品id
				adjustmentTypeItem.setGoodsCode(goodsCode);//商品编码
				adjustmentTypeItem.setGoodsNo(goodsNo);//商品货号
				adjustmentTypeItem.setGoodsName(goodsName);//商品名称
				adjustmentTypeItem.setBarcode(barcode);//商品条形码					
				adjustmentTypeItem.setOldBatchNo(goodsBatch);//原批次号
				adjustmentTypeItem.setOldGoodsNo(goodsNo);//调整前货号						
				adjustmentTypeItem.setOverdueDate(overdueDate);//失效日期			
				adjustmentTypeItem.setProductionDate(productionDate);				
				adjustmentTypeItem.setOldOverdueDate(oldOverdueDate);				
				adjustmentTypeItem.setOldProductionDate(oldProductionDate);
				adjustmentTypeItem.setIsDamage(isDamage);
				
				// 海外仓理货单位
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType)) {
					//P：托盘，C：箱 , B：件
					if ("P".equals(tallyingUnit)) {
						adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_00);
					}else if ("C".equals(tallyingUnit)) {						
						adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_01);
					}else if ("B".equals(tallyingUnit)) {						
						adjustmentTypeItem.setTallyingUnit(DERP.ORDER_TALLYINGUNIT_02);
					}
					
				}
				//保存商品信息
				adjustmentTypeItemDao.save(adjustmentTypeItem);
				// 向推送库存mq中保存商品								
				InvetAddOrSubtractGoodsListJson goodsMQ = new  InvetAddOrSubtractGoodsListJson();
				goodsMQ.setBarcode(barcode);// 商品条形码
				goodsMQ.setGoodsId(String.valueOf(goodsId));//商品ID
				goodsMQ.setGoodsName(goodsName);//商品名称
				goodsMQ.setGoodsNo(goodsNo);//商品货号
				goodsMQ.setBatchNo(goodsBatch);//批次号
				goodsMQ.setProductionDate(productionMQDate);//生产日期
				goodsMQ.setOverdueDate(overdueMQDate);//失效日期				
				goodsMQ.setNum(num);//数量

				if (StringUtils.isNotBlank(overdueMQDate)) {
					Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
					String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
					goodsMQ.setIsExpire(isExpire);//是否过期（0是 1否	)
				}else {
					goodsMQ.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
				}
				goodsMQ.setType(isDamage);//商品分类 （0 好品 1坏品 ）
				//goodsMQ.setUnit(unit);//单位
				
				// 调整后的生产日期加
				goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//0 调减 1调增
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType)) {
					//P：托盘，C：箱 , B：件
					if ("P".equals(tallyingUnit)) {
						goodsMQ.setUnit(DERP.INVENTORY_UNIT_0);
					}else if ("C".equals(tallyingUnit)) {						
						goodsMQ.setUnit(DERP.INVENTORY_UNIT_1);
					}else if ("B".equals(tallyingUnit)) {						
						goodsMQ.setUnit(DERP.INVENTORY_UNIT_2);
					}
					
				}
				
				xQTZMQList.add(goodsMQ);
				InvetAddOrSubtractGoodsListJson goodsMQ2 = new  InvetAddOrSubtractGoodsListJson();	
				goodsMQ2.setBarcode(barcode);// 商品条形码
				goodsMQ2.setGoodsId(String.valueOf(goodsId));//商品ID
				goodsMQ2.setGoodsName(goodsName);//商品名称
				goodsMQ2.setGoodsNo(goodsNo);//商品货号
				goodsMQ2.setBatchNo(goodsBatch);//批次号
				goodsMQ2.setProductionDate(oldProductionMQDate);//生产日期
				goodsMQ2.setOverdueDate(oldOverdueMQDate);//失效日期
				goodsMQ2.setType(isDamage);//商品分类 （0 好品 1坏品 2 过期）
				goodsMQ2.setNum(num);//数量

				if (StringUtils.isNotBlank(oldOverdueDateStr)) {
					Timestamp exTtime = Timestamp.valueOf(oldOverdueMQDate+" 00:00:00");
					String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
					goodsMQ2.setIsExpire(isExpire);//是否过期（0是 1否	)
				}else {
					goodsMQ2.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
				}
				goodsMQ2.setType(isDamage);//商品分类 （0 好品 1坏品 ）
											
				// 调整前的生产日期减
				goodsMQ2.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//0 调减 1调增
				if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType)) {
					//P：托盘，C：箱 , B：件
					if ("P".equals(tallyingUnit)) {
						goodsMQ2.setUnit(DERP.INVENTORY_UNIT_0);
					}else if ("C".equals(tallyingUnit)) {						
						goodsMQ2.setUnit(DERP.INVENTORY_UNIT_1);
					}else if ("B".equals(tallyingUnit)) {						
						goodsMQ2.setUnit(DERP.INVENTORY_UNIT_2);
					}
					
				}
				
				xQTZMQList.add(goodsMQ2);
			}
								
			// 推送库存 效期调整// 原生产日期的减  变更后的生产日期加
			InvetAddOrSubtractRootJson invetAddOrSubtractPD= new InvetAddOrSubtractRootJson();
			invetAddOrSubtractPD.setBusinessNo(sourceCode);// 业务单号
			invetAddOrSubtractPD.setMerchantId(String.valueOf(merchantId));//商家ID
			invetAddOrSubtractPD.setMerchantName(merchantName);//商家名称
			invetAddOrSubtractPD.setTopidealCode(topidealCode);//商家编码
			invetAddOrSubtractPD.setDepotId(String.valueOf(depotId));//仓库ID
			invetAddOrSubtractPD.setDepotName(depotName);//仓库名称
			invetAddOrSubtractPD.setDepotType(depotType);//仓库类型
			invetAddOrSubtractPD.setDepotCode(depotCode);//仓库编码
			invetAddOrSubtractPD.setOrderNo(adjustmentType.getCode());// 类型调整单code
			invetAddOrSubtractPD.setIsTopBooks(depotIsTopBooks);
			invetAddOrSubtractPD.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0014);//LETZD("0014","类型调整单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
			invetAddOrSubtractPD.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0018);//XQTZ("0018","效期调整"), //效期变更
			
			invetAddOrSubtractPD.setSourceDate(now);//单据时间
			invetAddOrSubtractPD.setDivergenceDate(transferStr);	//出入时间				
			// 获取当前年月			  
			invetAddOrSubtractPD.setOwnMonth(yearsMonths);			
			invetAddOrSubtractPD.setGoodsList(xQTZMQList);	
			//库存回推数据
			Map<String, Object> customParam=new HashMap<String, Object>();
			invetAddOrSubtractPD.setBackTags(MQPushBackStorageEnum.STORAGE_SELF_INVENTORY_PUSH_BACK.getTags());//回调标签
			invetAddOrSubtractPD.setBackTopic(MQPushBackStorageEnum.STORAGE_SELF_INVENTORY_PUSH_BACK.getTopic());//回调主题		
			customParam.put("code", adjustmentType.getCode());// 类型调整单内部单号
			invetAddOrSubtractPD.setCustomParam(customParam);////自定义回调参数
			String jsonMQStr = JSONObject.fromObject(invetAddOrSubtractPD).toString();
			LOGGER.info("自营库存更新推送库存 请求报文" + jsonMQStr);
			// 自营库存更新推送库存			
			rocketMQProducer.send(jsonMQStr, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
						
		}
		
		return true;
	}

}
