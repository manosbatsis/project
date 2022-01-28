package com.topideal.service.api.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.DERP;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_STORAGE;
import com.topideal.common.constant.DERP_SYS;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.dao.TakesStockDao;
import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.dao.TakesStockResultsDao;
import com.topideal.entity.vo.TakesStockResultItemModel;
import com.topideal.entity.vo.TakesStockResultsModel;
import com.topideal.json.api.v6_5.OfcCCInventoryResultsGoodsListJson;
import com.topideal.json.api.v6_5.OfcCCInventoryResultsRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.OfcInventoryResultsPushService;
import net.sf.json.JSONArray;
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
 * ofc仓储盘点结果回推
 * 
 * @author 杨创 2018/12/13
 */
@Service
public class OfcInventoryResultsPushServiceImpl implements OfcInventoryResultsPushService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OfcInventoryResultsPushServiceImpl.class);

	@Autowired
	private TakesStockDao takesStockDao;// 盘点指令表
	@Autowired
	private TakesStockResultsDao takesStockResultsDao;// 盘点结果表
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;// 盘点结果商品表
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb

	/**
	 * ofc仓储盘点结果回推
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203201301, model = DERP_LOG_POINT.POINT_12203201301_Label, keyword = "inventoryCode")
	public boolean saveOfcInventoryResultsPushInfo(String json, String keys, String topics, String tags)
			throws Exception {

		/**
		 * 说明 ofc盘点结果回推会存在和盘点指令对不上的单 也要进行保存
		 */
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList", OfcCCInventoryResultsGoodsListJson.class);
		OfcCCInventoryResultsRootJson rootJson = (OfcCCInventoryResultsRootJson) JSONObject.toBean(jsonData,
				OfcCCInventoryResultsRootJson.class, classMap);

		String inventoryCode = rootJson.getInventoryCode();// 盘点单号
		String orderCode = rootJson.getOrderCode();// 订单号
		Long merchantId = rootJson.getMerchantId();// 商家id
		String merchantName = rootJson.getMerchantName();// 商家名称
		String topidealCode = rootJson.getTopidealCode();// 卓志编码
		String status = rootJson.getStatus();// 00 初始化 20 已发布 30 审核中 40 审核通过 60
												// 审核未通 99 完成 90 取消 (默认99)
		String depoCode = rootJson.getDepoCode();// 仓库编码
		Long depoId = rootJson.getDepoId();// 仓库id
		String depoName = rootJson.getDepoName();// 仓库名称
		String depoType = rootJson.getDepoType();// 仓库类型
		String isTopBooks = rootJson.getIsTopBooks();// 是否是代销仓
		String serverType = rootJson.getServerType();
		String yModel = rootJson.getModel();
		String inventoryResultEmail = rootJson.getInventoryResultEmail();//// 盘点结果邮箱
		// 商品
		List<OfcCCInventoryResultsGoodsListJson> goodsList = rootJson.getGoodsList();
		// 只接收status 状态是99 完成的单
		if (!"99".equals(status)) {
			LOGGER.error("ofc盘点结果只接收状态是状态是99(完成)的单,来源单据号inventoryCode:" + inventoryCode);
			throw new RuntimeException("ofc盘点结果只接收状态是状态是99(完成)的单,来源单据号inventoryCode" + inventoryCode);
		}		
							
		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> depotInfoMap = new HashMap<>();
		depotInfoMap.put("depotId", depoId);//// 调入仓库id
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);
		if (depotInfoMongo == null) {
			LOGGER.error("未查到调入仓库信息,订单编号" + inventoryCode);
			throw new RuntimeException("未查到调入仓库信息,订单编号" + inventoryCode);
		}
		
		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotInfoMongo.getBatchValidation())) {
			for (OfcCCInventoryResultsGoodsListJson goodsListJson : rootJson.getGoodsList()) {
				String batchNo = goodsListJson.getBatchNo();
				String productionDate = goodsListJson.getProductionDate();
				String overdueDate = goodsListJson.getOverdueDate();
				if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
					LOGGER.error(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次和效期不能为空,订单号:" + inventoryCode);
					throw new RuntimeException(depotInfoMongo.getName()+",设置了批次效期强校验"+"批次效和期不能为空,订单号:" + inventoryCode);
				}
				
			}
		}

		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_2.equals(depotInfoMongo.getBatchValidation())) {
			for (OfcCCInventoryResultsGoodsListJson goods : rootJson.getGoodsList()) {
				Integer amount = goods.getAmount();// 系统数量
				Integer realqty =goods.getRealqty();// 实盘数量
				Integer num = realqty - amount;
				String batchNo = goods.getBatchNo();
				String productionDate = goods.getProductionDate();
				String overdueDate = goods.getOverdueDate();
				if (num > 0) {
					if (StringUtils.isBlank(batchNo)||StringUtils.isBlank(productionDate)||StringUtils.isBlank(overdueDate)) {
						LOGGER.error(depotInfoMongo.getName()+",设置了入库强校验/出库弱校验"+"批次和效期不能为空,订单号:" + inventoryCode);
						throw new RuntimeException(depotInfoMongo.getName()+",设置了入库强校验/出库弱校验"+"批次效和期不能为空,订单号:" + inventoryCode);
					}
				}
			}

		}
		
		String galFinishTimeStr = rootJson.getGalFinishTime();
		Timestamp galFinishTime=null;
		if (StringUtils.isNotBlank(galFinishTimeStr)) {
			if (galFinishTimeStr.length()==10) {
				galFinishTime=Timestamp.valueOf(galFinishTimeStr+" 00:00:00");//推送时间 
			}else {
				galFinishTime=Timestamp.valueOf(galFinishTimeStr);//推送时间 
			}
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(galFinishTime);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(galFinishTime);//归属月份
		// 系统当前日期
		Date date1 = new Date();	
		
		// 根据盘点指令单号和商家查询盘点结果表
		TakesStockResultsModel takesStockResultsModel = new TakesStockResultsModel();
		takesStockResultsModel.setSourceCode(inventoryCode);// 来源单据号
		takesStockResultsModel.setMerchantId(merchantId);
		takesStockResultsModel = takesStockResultsDao.searchByModel(takesStockResultsModel);
		if (takesStockResultsModel!=null) {
			LOGGER.error("ofc盘点结果结果单已经存在,来源单据号:" + inventoryCode);
			throw new RuntimeException("ofc盘点结果结果单已经存在,来源单据号:" + inventoryCode);
		}

		//如果商家是卓烨，商品标准品牌为”美赞臣“报错
		/*if("0000134".equals(topidealCode)) {
			for (OfcCCInventoryResultsGoodsListJson goods : goodsList) {
				String goodsNo = goods.getGoodsNo();
				InventoryLocationMappingMongo oriGoodsNoMappingModel = inventoryLocationMappingMongoDao
						.getOriGoodsNoMappingModel(topidealCode, goodsNo);

				if(oriGoodsNoMappingModel != null) {
					LOGGER.error("商品货号:"+goodsNo +"的母品牌为“美赞臣”");
					throw new RuntimeException("商品货号:"+goodsNo +"的母品牌为“美赞臣”");
				}
			}
		}
*/

		TakesStockResultsModel takesModel = new TakesStockResultsModel();
//		takesModel.setCode(CodeGeneratorUtil.getNo("PDJG"));// 盘点结果单号
		takesModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PDJG));// 盘点结果单号
		takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022);//022-入库中
		takesModel.setDepotId(depoId);// 盘点仓库id
		takesModel.setDepotName(depoName);// 盘点仓库名称	
		takesModel.setServerType(serverType);// 服务类型
		takesModel.setModel(yModel);// 业务场景		
		takesModel.setMerchantId(merchantId);
		takesModel.setMerchantName(merchantName);
		takesModel.setSourceTime(galFinishTime);//归属时间
		takesModel.setSourceCode(inventoryCode);//来源单据号
		takesModel.setSourceType(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_2);//来源状态 1.op盘点结果 2 ofc盘点结果'
		takesModel.setCreateUsername("接口回传");	// 创建人
		// 新增盘点结果表
		takesStockResultsDao.save(takesModel);

		List PDList = new ArrayList<>();// 盘盈时增加库存量 盘亏时减少库存量
		// 存储 经分销盘点结果邮件商品商品信息json数组
		JSONArray sendMailJSONArray = new JSONArray();
		for (OfcCCInventoryResultsGoodsListJson goods : goodsList) {
			// 推送的MQ
			TakesStockResultItemModel itemModel = new TakesStockResultItemModel();
			itemModel.setTakesStockResultId(takesModel.getId());
			itemModel.setGoodsId(goods.getGoodsId());
			itemModel.setGoodsCode(goods.getGoodsCode());
			itemModel.setGoodsNo(goods.getGoodsNo());
			itemModel.setGoodsName(goods.getGoodsName());
			itemModel.setBarcode(goods.getBarcode());
			itemModel.setBatchNo(goods.getBatchNo());
			itemModel.setIsDamage(goods.getIsDamage());
			Integer amount = goods.getAmount();// 系统数量
			Integer realqty = goods.getRealqty();// 实盘数量
			itemModel.setRealQty(realqty);// 实盘数量
			itemModel.setSystemNum(amount);// 系统数量
			/*
			 * 开发说明： 1、盘盈：接口盘点结果回推字段“系统数量”小于“实盘数量”，盘盈数量=实盘数量-系统数量
			 * 2、盘亏：接口盘点结果回推字段“系统数量”大于“实盘数量”，盘亏数量=|实盘数量-系统数量|
			 */
			// 实盘数量- 系统数量
			Integer num = realqty - amount;
			Integer absNum = Math.abs(num);
			if (num > 0) {
				itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1); // 盘盈1
				itemModel.setSurplusNum(absNum);
			} else {
				itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_2);// 调整类型 盘亏2
				itemModel.setDeficientNum(absNum);
			}			
			String productionDate = goods.getProductionDate();
			String productionMQDate = null;
			if (StringUtils.isNotBlank(productionDate)) {
				if (productionDate.length() == 10) {
					itemModel.setProductionDate(Timestamp.valueOf(productionDate + " 00:00:00"));
					productionMQDate = productionDate;
				} else {
					itemModel.setProductionDate(Timestamp.valueOf(productionDate));
					productionMQDate = productionDate.substring(0, 10);
				}
			}
			String overdueDate = goods.getOverdueDate();
			String overdueMQDate = null;
			if (StringUtils.isNotBlank(overdueDate)) {
				if (overdueDate.length() == 10) {
					itemModel.setOverdueDate(Timestamp.valueOf(overdueDate + " 00:00:00"));
					overdueMQDate = overdueDate;
				} else {
					itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));
					overdueMQDate = overdueDate.substring(0, 10);
				}
			}
			itemModel.setDifferencesNum(Integer.valueOf(goods.getDiffqty()));//差异数量
			itemModel.setReasonCode(goods.getReasonCode());// 盘点原因代码
			itemModel.setReasonDes(goods.getReasonDes());// 原因描述
			String unit = goods.getUnit();//P：托盘，C：箱 , B：件
			String tallyUnit=null;//字符串 0 托盘 1箱  2 件
			if ("P".equals(unit)) {
				tallyUnit=DERP.ORDER_TALLYINGUNIT_00;
			}else if ("C".equals(unit)) {
				tallyUnit=DERP.ORDER_TALLYINGUNIT_01;
			}else if ("B".equals(unit)) {
				tallyUnit=DERP.ORDER_TALLYINGUNIT_02;
			}
			itemModel.setTallyUnit(tallyUnit);// 单位
			// 新增表体
			takesStockResultItemDao.save(itemModel);
			/*InvetAddOrSubtractGoodsListJson goodsMQ = new InvetAddOrSubtractGoodsListJson();
			// 向推送库存mq中保存商品
			goodsMQ.setBarcode(goods.getBarcode());// 商品条形码
			goodsMQ.setGoodsId(String.valueOf(goods.getGoodsId()));// 商品ID
			goodsMQ.setGoodsName(goods.getGoodsName());// 商品名称
			goodsMQ.setGoodsNo(goods.getGoodsNo());// 商品货号
			goodsMQ.setBatchNo(goods.getBatchNo());// 批次号
			goodsMQ.setProductionDate(productionMQDate);// 生产日期
			goodsMQ.setOverdueDate(overdueMQDate);// 失效日期
			goodsMQ.setNum(absNum);// 数量
			goodsMQ.setType(goods.getIsDamage());// 商品分类 （0 好品 1坏品 ）	
			//推过来的 P：托盘，C：箱 , B：件
			if (StringUtils.isNotBlank(unit)) {				
				if ("P".equals(unit)) {
					goodsMQ.setUnit(DERP.INVENTORY_UNIT_0);// 库存 单位	字符串 0 托盘 1箱  2 件
				}else if ("C".equals(unit)) {
					goodsMQ.setUnit(DERP.INVENTORY_UNIT_1);// 库存 单位	字符串 0 托盘 1箱  2 件
				}else if ("B".equals(unit)) {
					goodsMQ.setUnit(DERP.INVENTORY_UNIT_2);// 库存 单位	字符串 0 托盘 1箱  2 件
				}
				
			}
			
			*//*********************************** 判断过期品 ******************************//*
			if (StringUtils.isNotBlank(overdueMQDate)) {
				Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
				String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
				goodsMQ.setIsExpire(isExpire);//是否过期（0是 1否	)
			}else {
				goodsMQ.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
			}
			
			// 盘盈
			if (num > 0) {
				goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);// 0 调减 1调增
			} else {
				goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);// 0 调减 1调增
			}
			if (0!=num) {
				PDList.add(goodsMQ);
			}*/
			
			
		}

		// 推送库存的盘盈
		/*InvetAddOrSubtractRootJson invetAddOrSubtractPD = new InvetAddOrSubtractRootJson();
		invetAddOrSubtractPD.setBusinessNo(inventoryCode);// 业务单号
		invetAddOrSubtractPD.setMerchantId(String.valueOf(merchantId));// 商家ID
		invetAddOrSubtractPD.setMerchantName(merchantName);// 商家名称
		invetAddOrSubtractPD.setTopidealCode(topidealCode);// 商家编码
		invetAddOrSubtractPD.setDepotId(String.valueOf(depoId));// 仓库ID
		invetAddOrSubtractPD.setDepotName(depoName);// 仓库名称
		invetAddOrSubtractPD.setDepotType(depoType);// 仓库类型
		invetAddOrSubtractPD.setDepotCode(depoCode);// 仓库编码
		invetAddOrSubtractPD.setOrderNo(takesModel.getCode());// 盘点结果单
		invetAddOrSubtractPD.setIsTopBooks(isTopBooks);
		invetAddOrSubtractPD.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0013); //PDJGD("0013","盘点结果单"),
		invetAddOrSubtractPD.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015);// 单据类型 PDJGD("0015","盘点结果单"),
		String now = sdf1.format(new Date());
		invetAddOrSubtractPD.setSourceDate(now);// 单据时间

		invetAddOrSubtractPD.setDivergenceDate(transferStr); // 出入时间
		// 获取当前年月
		invetAddOrSubtractPD.setOwnMonth(yearsMonths);
		invetAddOrSubtractPD.setGoodsList(PDList);
		
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		invetAddOrSubtractPD.setBackTags(MQPushBackStorageEnum.STORAGE_RESULTS_PUSH_BACK.getTags());//回调标签
		invetAddOrSubtractPD.setBackTopic(MQPushBackStorageEnum.STORAGE_RESULTS_PUSH_BACK.getTopic());//回调主题		
		customParam.put("code", takesModel.getCode());// 盘点结果内部单号
		invetAddOrSubtractPD.setCustomParam(customParam);////自定义回调参数		
		String PDJson = JSONObject.fromObject(invetAddOrSubtractPD).toString();
		LOGGER.info("Ofc仓储退运信息 推送库存盘盈MQ 请求报文" + PDJson);
		// 推送盘点
		if (PDList.size()>0) {
			rocketMQProducer.send(PDJson, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),
					MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
		}*/

		return true;

	}
	

}
