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
import com.topideal.json.api.v6_6.CCBigCargoTallyOriginGoodsListJson;
import com.topideal.json.api.v6_6.CCBigCargoTallyRootJson;
import com.topideal.json.api.v6_6.CCBigCargoTallyTargeGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.service.api.BigCargoTallyService;
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
 * 大货理货接口
 */
@Service
public class BigCargoTallyServiceImpl implements BigCargoTallyService{
	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BigCargoTallyServiceImpl.class);
	//类型调整单
	@Autowired
	private AdjustmentTypeDao adjustmentTypeDao;
	//类型调整单商品
	@Autowired
	private AdjustmentTypeItemDao adjustmentTypeItemDao;


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203202500, model = DERP_LOG_POINT.POINT_12203202500_Label,keyword="orderId")
	public boolean saveBigCargoTallyInfo(String json,String keys,String topics,String tags) throws Exception {
		//解析json
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("originGoodsList",CCBigCargoTallyOriginGoodsListJson.class);	
		classMap.put("targeGoodsList",CCBigCargoTallyTargeGoodsListJson.class);
		// JSON对象转实体
		CCBigCargoTallyRootJson rootJson = (CCBigCargoTallyRootJson) JSONObject.toBean(jsonData, CCBigCargoTallyRootJson.class, classMap);
		String orderId = rootJson.getOrderId();// 来源单据号
		Long merchantId = rootJson.getMerchantId();// 商家id
		String merchantName = rootJson.getMerchantName();//商家名称
		String topidealCode = rootJson.getTopidealCode();//卓志编码	
		Long depotId = rootJson.getDepotId();//仓库id
		String depotName = rootJson.getDepotName();//仓库名称
		String depotCode = rootJson.getDepotCode();//仓库编码
		String depotType = rootJson.getDepotType();//仓库类型
		String isTopBooks = rootJson.getIsTopBooks();//是否代销仓
		String batchValidation = rootJson.getBatchValidation();// 是否强校验 1是 0 否
		
		String bomFinishTimeStr=rootJson.getBomFinishTime();// 发货时间待定
		Timestamp bomFinishTime= null;
		if (StringUtils.isNotBlank(bomFinishTimeStr)) {
			if (bomFinishTimeStr.length()==10) {
				bomFinishTime=Timestamp.valueOf(bomFinishTimeStr+" 00:00:00");
			}else {
				bomFinishTime=Timestamp.valueOf(bomFinishTimeStr);
			}
			
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String deliveryTimeStr = formatter.format(bomFinishTime);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(bomFinishTime);//归属月份

		List<CCBigCargoTallyOriginGoodsListJson> originGoodsList = rootJson.getOriginGoodsList();// 原商品批次
		List<CCBigCargoTallyTargeGoodsListJson> targeGoodsList = rootJson.getTargeGoodsList();// 目标商品批次

		Date date1 = new Date();// 系统当前时间	
		// 海外仓订单校验
		if (!DERP_SYS.DEPOTINFO_TYPE_C.equals(depotType)) {
			LOGGER.error("大货理货接口只接收仓库类型是海外仓的订单,订单编号" + orderId);
			throw new RuntimeException("大货理货接口只接收仓库类型是海外仓的订单,订单编号" + orderId);
		}
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(batchValidation)) {
			for (CCBigCargoTallyOriginGoodsListJson originGoods : originGoodsList) {
				String bathNo = originGoods.getBathNo();
				String productionDate = originGoods.getProductionDate();
				String overdueDate = originGoods.getOverdueDate();
				if (StringUtils.isBlank(bathNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error("大货理货接口 仓库设置了批次效期强校验,批次效期不能为空,订单编号" + orderId);
					throw new RuntimeException("大货理货接口 仓库设置了批次效期强校验,批次效期不能为空,订单编号" + orderId);
				}
				
			}

		}

		if (batchValidation.matches(DERP_SYS.DEPOTINFO_BATCHVALIDATION_1 + "|" + DERP_SYS.DEPOTINFO_BATCHVALIDATION_2)) {
			for (CCBigCargoTallyTargeGoodsListJson targeGoods : targeGoodsList) {
				String bathNo = targeGoods.getBathNo();
				String productionDate = targeGoods.getProductionDate();
				String overdueDate = targeGoods.getOverdueDate();
				if (StringUtils.isBlank(bathNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error("大货理货接口 仓库设置了批次效期强校验,批次效期不能为空,订单编号" + orderId);
					throw new RuntimeException("大货理货接口 仓库设置了批次效期强校验,批次效期不能为空,订单编号" + orderId);
				}
			}
		}

		// 查询类型调整单是否存在
		AdjustmentTypeModel adjustmentTypeModel =new AdjustmentTypeModel();
		adjustmentTypeModel.setSourceCode(orderId);// 单据来源
		adjustmentTypeModel = adjustmentTypeDao.searchByModel(adjustmentTypeModel);
		if (adjustmentTypeModel!=null) {
			LOGGER.error("类型调整单已经存在,来源单号:sourceCode"+orderId);
			throw new RuntimeException("类型调整单已经存在,来源单号:sourceCode"+orderId);			
		}

		AdjustmentTypeModel adjustmentType =new AdjustmentTypeModel();
		adjustmentType.setAdjustmentTypeName("大货理货");
//		adjustmentType.setCode(CodeGeneratorUtil.getNo("LXTZO"));
		adjustmentType.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_LXTZO));
		adjustmentType.setMerchantId(merchantId);
		adjustmentType.setMerchantName(merchantName);
		adjustmentType.setSourceCode(orderId);
		adjustmentType.setModel(DERP_STORAGE.ADJUSTMENTTYPE_MODEL_4);
		adjustmentType.setStatus(DERP_STORAGE.ADJUSTMENTTYPE_STATUS_020); //待调整
//		adjustmentType.setAdjustmentTime(new Timestamp(new Date().getTime()));// 当前时间
		adjustmentType.setDepotId(depotId);
		adjustmentType.setDepotName(depotName);			
		adjustmentType.setPushTime(Timestamp.valueOf(deliveryTimeStr));// 发货时间 归属时间
		adjustmentType.setSource(DERP_STORAGE.ADJUSTMENTTYPE_SOURCE_01);
		adjustmentType.setCreateUsername("接口回传");
		//保存类型调整单
		adjustmentTypeDao.save(adjustmentType);
		
		//商品信息  用于存储 目标商品
		List<InvetAddOrSubtractGoodsListJson> itemtargeAndOriginMQList = new ArrayList<>();//用于存储库存调整的商品
		// 原商品批次 调减
		for (CCBigCargoTallyOriginGoodsListJson originGoods : originGoodsList) {

			String tallyingUnit = originGoods.getUnit();
			String isDamaged = originGoods.getIsDamaged();
			String bathNo = originGoods.getBathNo();
			String productionDateStr = originGoods.getProductionDate();
			String overdueDateStr = originGoods.getOverdueDate();
			
			AdjustmentTypeItemModel adjustmentTypeItem = new AdjustmentTypeItemModel();
			adjustmentTypeItem.setBarcode(originGoods.getBarcode());// 商品条形码
			adjustmentTypeItem.setAdjustTotal(originGoods.getNum());
			adjustmentTypeItem.setCreateDate(TimeUtils.getNow());
			adjustmentTypeItem.setGoodsId(originGoods.getGoodsId());
			adjustmentTypeItem.setGoodsCode(originGoods.getGoodsCode());
			adjustmentTypeItem.setGoodsName(originGoods.getGoodsName());
			adjustmentTypeItem.setGoodsNo(originGoods.getGoodsNo());
			adjustmentTypeItem.setOldBatchNo(bathNo);
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
			

			adjustmentTypeItem.setProductionDate(productionDate);// 变更后的生产日期
			adjustmentTypeItem.setOverdueDate(overdueDate);// 变更后的失效日期
			
			adjustmentTypeItem.setTAdjustmentTypeId(adjustmentType.getId());			
			adjustmentTypeItem.setTallyingUnit(tallyingUnit);// api 传值是  00：托盘01：箱02：件 和我们数据对应
			adjustmentTypeItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_0);//调整类型 0 调减 1 调增
			adjustmentTypeItem.setIsDamage(isDamaged);;//是否坏品 0 好品 1坏品  
			
			//保存商品信息
			adjustmentTypeItemDao.save(adjustmentTypeItem);


			/*InvetAddOrSubtractGoodsListJson itemMQModel = new InvetAddOrSubtractGoodsListJson();
			itemMQModel.setBarcode(adjustmentTypeItem.getBarcode());// 商品条形码
			itemMQModel.setGoodsId(String.valueOf(adjustmentTypeItem.getGoodsId()));//商品ID
			itemMQModel.setGoodsName(adjustmentTypeItem.getGoodsName());//商品名称
			itemMQModel.setGoodsNo(adjustmentTypeItem.getGoodsNo());//商品货号
			itemMQModel.setBatchNo(bathNo);//批次号
			itemMQModel.setProductionDate(productionMQDate);//生产日期
			itemMQModel.setOverdueDate(overdueMQDate);//失效日期
			itemMQModel.setType(isDamaged);//商品分类 （0 好品 1坏品 2 过期）
			itemMQModel.setNum(adjustmentTypeItem.getAdjustTotal());//数量
			itemMQModel.setUnit(tallyingUnit);
			// api 传值是  00：托盘01：箱02：件 和我们数据对应
			if (StringUtils.isNotBlank(tallyingUnit)) {
				if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_0);// 库存 单位	字符串 0 托盘 1箱  2 件
				}else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_1);// 库存 单位	字符串 0 托盘 1箱  2 件
				}else if (DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_2);// 库存 单位	字符串 0 托盘 1箱  2 件
				}

			}

			itemMQModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//字符串 0 调减 1调增



			// 如果number不等于空说明事项日期不为空 , 如果number=-1 说明 当前时间小于 失效日期 没有过期 如果 number=0 说明时间相等,如果时间大于0说明当前时间大于失效日期 说明失效
			//如果失效日期为null 或者 当前时间小于失效时间  都是未过期品
			Integer number=null;
			if (StringUtils.isNotBlank(overdueMQDate)) {
				Date date2=formatter.parse(overdueMQDate+" 00:00:00");
				number=date1.compareTo(date2);
			}
			if (number==null||number==-1) {
				itemMQModel.setIsExpire(DERP.ISEXPIRE_1);//是否过期  （0 是 1 否）
			}else {
				itemMQModel.setIsExpire(DERP.ISEXPIRE_0);//是否过期  （0 是 1 否）
			}
			itemtargeAndOriginMQList.add(itemMQModel);*/


		}


		// 目标商品批次调增
		for (CCBigCargoTallyTargeGoodsListJson targeGoods : targeGoodsList) {
			String tallyingUnit = targeGoods.getUnit();
			String isDamaged = targeGoods.getIsDamaged();
			String bathNo = targeGoods.getBathNo();
			String productionDateStr = targeGoods.getProductionDate();
			String overdueDateStr = targeGoods.getOverdueDate();
			
			AdjustmentTypeItemModel adjustmentTypeItem = new AdjustmentTypeItemModel();
			adjustmentTypeItem.setBarcode(targeGoods.getBarcode());// 商品条形码
			adjustmentTypeItem.setAdjustTotal(targeGoods.getNum());
			adjustmentTypeItem.setCreateDate(TimeUtils.getNow());
			adjustmentTypeItem.setGoodsId(targeGoods.getGoodsId());
			adjustmentTypeItem.setGoodsCode(targeGoods.getGoodsCode());
			adjustmentTypeItem.setGoodsName(targeGoods.getGoodsName());
			adjustmentTypeItem.setGoodsNo(targeGoods.getGoodsNo());
			adjustmentTypeItem.setOldBatchNo(bathNo);


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
			
			adjustmentTypeItem.setProductionDate(productionDate);// 生产日期
			adjustmentTypeItem.setOverdueDate(overdueDate);// 失效日期			
			
			
			adjustmentTypeItem.setTAdjustmentTypeId(adjustmentType.getId());
			
			adjustmentTypeItem.setTallyingUnit(tallyingUnit);// api 传值是  00：托盘01：箱02：件 和我们数据对应
			adjustmentTypeItem.setType(DERP_STORAGE.ADJUSTMENT_TYPE_1);//调整类型 0 调减 1 调增
			adjustmentTypeItem.setIsDamage(isDamaged);//是否坏品 0 好品 1坏品  



			//保存商品信息
			adjustmentTypeItemDao.save(adjustmentTypeItem);

			// 推送库存
			/*InvetAddOrSubtractGoodsListJson itemMQModel = new InvetAddOrSubtractGoodsListJson();
			itemMQModel.setBarcode(adjustmentTypeItem.getBarcode());// 商品条形码
			itemMQModel.setGoodsId(String.valueOf(adjustmentTypeItem.getGoodsId()));//商品ID
			itemMQModel.setGoodsName(adjustmentTypeItem.getGoodsName());//商品名称
			itemMQModel.setGoodsNo(adjustmentTypeItem.getGoodsNo());//商品货号
			itemMQModel.setBatchNo(bathNo);//批次号
			itemMQModel.setProductionDate(productionMQDate);//生产日期
			itemMQModel.setOverdueDate(overdueMQDate);//失效日期
			itemMQModel.setType(isDamaged);//商品分类 （0 好品 1坏品 2 过期）
			itemMQModel.setNum(adjustmentTypeItem.getAdjustTotal());//数量


			// api 传值是  00：托盘01：箱02：件 和我们数据对应
			if (StringUtils.isNotBlank(tallyingUnit)) {
				if (DERP.ORDER_TALLYINGUNIT_00.equals(tallyingUnit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_0);// 库存 单位	字符串 0 托盘 1箱  2 件
				}else if (DERP.ORDER_TALLYINGUNIT_01.equals(tallyingUnit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_1);// 库存 单位	字符串 0 托盘 1箱  2 件
				}else if (DERP.ORDER_TALLYINGUNIT_02.equals(tallyingUnit)) {
					itemMQModel.setUnit(DERP.INVENTORY_UNIT_2);// 库存 单位	字符串 0 托盘 1箱  2 件
				}

			}

			itemMQModel.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//字符串 0 调减 1调增
			if (StringUtils.isNotBlank(overdueMQDate)) {
				Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
				String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
				itemMQModel.setIsExpire(isExpire);//是否过期（0是 1否	)
			}else {
				itemMQModel.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
			}
			itemtargeAndOriginMQList.add(itemMQModel);*/

		}


		//库货号变更  这个是变更后的信息 变更后的商品增 变更前的商品减
		// 推送库存的mq
		/*InvetAddOrSubtractRootJson addMQModel = new InvetAddOrSubtractRootJson();
		addMQModel.setBusinessNo(orderId);// 业务单号
		addMQModel.setMerchantId(String.valueOf(merchantId));//商家ID
		addMQModel.setMerchantName(merchantName);//商家名称
		addMQModel.setTopidealCode(topidealCode);//商家编码
		addMQModel.setDepotId(String.valueOf(depotId));//仓库ID
		addMQModel.setDepotName(depotName);//仓库名称
		addMQModel.setDepotCode(depotCode);//仓库编码
		addMQModel.setDepotType(depotType);//仓库类型
		addMQModel.setOrderNo(adjustmentType.getCode());//订单号
		addMQModel.setIsTopBooks(isTopBooks);//是否代销仓
		addMQModel.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0014);//LETZD("0014","类型调整单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
		addMQModel.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0019);//DHLH("0019","大货理货"), //单据类型
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());
		addMQModel.setSourceDate(now);//单据时间
		// 获取当前年月
		addMQModel.setOwnMonth(yearsMonths);//归属月份
		addMQModel.setDivergenceDate(deliveryTimeStr);//出入时间

		try {
			addMQModel.setGoodsList(itemtargeAndOriginMQList);// 商品信息
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
