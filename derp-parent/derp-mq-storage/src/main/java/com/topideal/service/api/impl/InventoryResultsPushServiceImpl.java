package com.topideal.service.api.impl;

import com.topideal.api.smurfs.SmurfsUtils;
import com.topideal.common.constant.*;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackStorageEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.TakesStockDao;
import com.topideal.dao.TakesStockResultItemDao;
import com.topideal.dao.TakesStockResultsDao;
import com.topideal.entity.vo.TakesStockModel;
import com.topideal.entity.vo.TakesStockResultItemModel;
import com.topideal.entity.vo.TakesStockResultsModel;
import com.topideal.json.api.v6_1.CCInventoryResultsGoodsListJson;
import com.topideal.json.api.v6_1.CCInventoryResultsRootJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.InventoryResultsPushService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 仓储盘点结果回推
 *
 * @author 杨创 2018/7/14
 */
@Service
public class InventoryResultsPushServiceImpl implements InventoryResultsPushService {

	/* 打印日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(InventoryResultsPushServiceImpl.class);

	@Autowired
	private TakesStockDao takesStockDao;// 盘点指令表
	@Autowired
	private TakesStockResultsDao takesStockResultsDao;// 盘点结果表
	@Autowired
	private TakesStockResultItemDao takesStockResultItemDao;// 盘点结果商品表
	@Autowired
	private DepotInfoMongoDao depotInfoMongoDao;// 仓库mongodb
	@Autowired
	private RMQProducer rocketMQProducer;// MQ


	/**
	 * 仓储盘点结果回推
	 */
	@Override
	@SystemServiceLog(point = DERP_LOG_POINT.POINT_12203201300, model = DERP_LOG_POINT.POINT_12203201300_Label,keyword ="inventoryCode")
	public boolean saveInventoryResultsPushInfo(String json,String keys,String topics,String tags) throws Exception {

		/**
		 * 说明 盘点结果回推会存在和盘点指令对不上的单 也要进行保存
		 */
		JSONObject jsonData = JSONObject.fromObject(json);
		Map classMap = new HashMap();
		classMap.put("goodsList",CCInventoryResultsGoodsListJson.class);
		CCInventoryResultsRootJson rootJson = (CCInventoryResultsRootJson) JSONObject.toBean(jsonData, CCInventoryResultsRootJson.class,classMap);

		String inventoryCode = rootJson.getInventoryCode();// 盘点单号
		String orderCode = rootJson.getOrderCode();// 订单号
		Long merchantId = rootJson.getMerchantId();// 商家id
		String merchantName = rootJson.getMerchantName();// 商家名称
		String topidealCode = rootJson.getTopidealCode();// 卓志编码
		String status = rootJson.getStatus();// 10：制单30：提交70：成功90：作废  // 转换成我们的值  状态 009-待确认 010-已确认 021-已作废'
		String depoCode = rootJson.getDepoCode();// 仓库编码
		String serverType = rootJson.getServerType();// 服务类型10：个性盘点
		// 20：自主盘点
		String yModel = rootJson.getModel();// 业务场景10：客服盘点申请20：仓库自行盘点30：前端盘点申请
		String dismissRemark = rootJson.getDismissRemark();// 驳回原因
		//String email = rootJson.getEmail();// 商家邮箱
		String inventoryResultEmail=rootJson.getInventoryResultEmail();// 盘点结果邮箱
		List<CCInventoryResultsGoodsListJson> goodsList= rootJson.getGoodsList();
		String update = rootJson.getUpdateDate();// 日期格式：yyyy-mm-dd 录入日期
		Timestamp updateDate = null;
		if (update.length() == 10) {
			updateDate = Timestamp.valueOf(update + " 00:00:00");// 订单时间
			// 对应交易时间
		} else {
			updateDate = Timestamp.valueOf(update);// 订单时间 对应交易时间
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String transferStr = formatter.format(updateDate);// 归属日期
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		String yearsMonths = sdf1.format(updateDate);//归属月份


		// 根据订单号查询盘点指令表
		TakesStockModel takesStockModel = new TakesStockModel();
		if (StringUtils.isNotBlank(orderCode)) {
			takesStockModel.setMerchantId(merchantId);// 商家id
			takesStockModel.setCode(orderCode);// 订单号
			takesStockModel = takesStockDao.searchByModel(takesStockModel);
		}

		if (takesStockModel == null) {
			takesStockModel = new TakesStockModel();
		}
		Map<String, Object> depotInfoMap = new HashMap<>();
		depotInfoMap.put("code", depoCode);// 调出仓库id
		depotInfoMap.put("isTopBooks", DERP_SYS.DEPOTINFO_ISTOPBOOKS_0);
		depotInfoMap.put("isValetManage",DERP_SYS.DEPOTINFO_IS_VALET_MANAGE_0);// 订单查询非代客管理的仓库
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);// 调出仓库信息
		if (depotInfoMongo == null) {
			LOGGER.error("MQ消费仓储 根据仓库编号查询不到对应仓库,订单编号" + inventoryCode);
			throw new RuntimeException("MQ消费仓储 根据仓库编号查询不到对应仓库,订单编号" + inventoryCode);
		}
		if (DERP_SYS.DEPOTINFO_TYPE_C.equals(depotInfoMongo.getType())) {
			LOGGER.error("op盘点结果不接收海外仓仓库订单" + inventoryCode);
			throw new RuntimeException("op盘点结果不接收海外仓仓库订单" + inventoryCode);

		}


		// 批次效期强校验：1-是 0-否
		if (DERP_SYS.DEPOTINFO_BATCHVALIDATION_1.equals(depotInfoMongo.getBatchValidation())) {
			for (CCInventoryResultsGoodsListJson goodsListJson : rootJson.getGoodsList()) {
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
			for (CCInventoryResultsGoodsListJson goods : rootJson.getGoodsList()) {
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

		// 根据来源单号查询盘点结果单
		TakesStockResultsModel takesStockResultsModel = new TakesStockResultsModel();
		takesStockResultsModel.setSourceCode(inventoryCode);// 来源单号
		takesStockResultsModel = takesStockResultsDao.searchByModel(takesStockResultsModel);
		if (takesStockResultsModel != null) {
			//YQR("010","已确认"), CLZ("022","处理中"),
			if (DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010.equals(takesStockResultsModel.getStatus())
					||DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022.equals(takesStockResultsModel.getStatus())) {
				LOGGER.error("op盘点结果状态是处理中或者是已入库订单号" + orderCode);
				throw new RuntimeException("op盘点结果状态是处理中或者是已入库订单号" + orderCode);
			}
		}

		//如果商家是卓烨，商品标准品牌为”美赞臣“报错
		/*if("0000134".equals(topidealCode)) {
			for (CCInventoryResultsGoodsListJson goods : goodsList) {
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
		// 如果盘点结果不存在就新增一条 如果存在就修改 并推库存mq
		if (takesStockResultsModel == null && "70".equals(status)) {
			// 盘点结果 要加商家 id 商家名称 和 盘点状态
			// 盘点结果商品 要加 系统数量 实盘数量 差异数量
			// 新增盘点结果和盘点结果所对应的商品

			TakesStockResultsModel takesModel = new TakesStockResultsModel();
//			takesModel.setCode(CodeGeneratorUtil.getNo("PDJG"));// 盘点结果单号
			takesModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PDJG));// 盘点结果单号
			takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022);//CLZ("022","处理中")
			takesModel.setDepotId(depotInfoMongo.getDepotId());// 盘点仓库id
			takesModel.setDepotName(depotInfoMongo.getName());// 盘点仓库名称
			takesModel.setTakesStockId(takesStockModel.getId());// 盘点指令id
			takesModel.setTakesStockCode(takesStockModel.getCode());// 盘点指令单号
			takesModel.setServerType(serverType);// 服务类型
			takesModel.setModel(yModel);// 业务场景
			takesModel.setDismissRemark(dismissRemark);// 驳回原因
			takesModel.setMerchantId(merchantId);
			takesModel.setMerchantName(merchantName);
			takesModel.setSourceTime(updateDate);// 归属月份
			takesModel.setSourceCode(inventoryCode);// 来源单号
			takesModel.setSourceType(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_1);//来源状态 1.op盘点结果 2 ofc盘点结果'
			takesModel.setInConfirmUsername("接口回传");
			takesModel.setInConfirmTime(TimeUtils.getNow());
			takesModel.setCreateUsername("接口回传");// 创建人
			// 新增盘点结果表
			takesStockResultsDao.save(takesModel);


			List PDList=new ArrayList<>();// 盘盈时增加库存量    盘亏时减少库存量
			// 存储 经分销盘点结果邮件商品商品信息json数组
			JSONArray sendMailJSONArray = new JSONArray();
			for (CCInventoryResultsGoodsListJson goods : goodsList) {
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
				Integer realqty =goods.getRealqty();// 实盘数量
				itemModel.setSystemNum(amount);// 系统数量
				itemModel.setRealQty(realqty);// 实盘数量
				/*
				 * 开发说明： 1、盘盈：接口盘点结果回推字段“系统数量”小于“实盘数量”，盘盈数量=实盘数量-系统数量
				 * 2、盘亏：接口盘点结果回推字段“系统数量”大于“实盘数量”，盘亏数量=|实盘数量-系统数量|
				 */
				// 实盘数量- 系统数量
				Integer num = realqty - amount;
				Integer absNum = Math.abs(num);
				if (num > 0) {
					itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1); //  盘盈1
					itemModel.setSurplusNum(absNum);
				} else {
					itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_2);// 调整类型  盘亏2
					itemModel.setDeficientNum(absNum);
				}


				String productionDate = goods.getProductionDate();
				String productionMQDate=null;
				if (StringUtils.isNotBlank(productionDate)) {
					if (productionDate.length() == 10) {
						itemModel.setProductionDate(Timestamp.valueOf(productionDate + " 00:00:00"));
						productionMQDate=productionDate;
					} else {
						itemModel.setProductionDate(Timestamp.valueOf(productionDate));
						productionMQDate=productionDate.substring(0,10);
					}
				}
				String overdueDate = goods.getOverdueDate();
				String overdueMQDate= null;
				if (StringUtils.isNotBlank(overdueDate)) {
					if (overdueDate.length() == 10) {
						itemModel.setOverdueDate(Timestamp.valueOf(overdueDate + " 00:00:00"));
						overdueMQDate=overdueDate;
					} else {
						itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));
						overdueMQDate=overdueDate.substring(0,10);
					}
				}

				takesStockResultItemDao.save(itemModel);


				/*InvetAddOrSubtractGoodsListJson goodsMQ = new  InvetAddOrSubtractGoodsListJson();
				// 向推送库存mq中保存商品
				goodsMQ.setBarcode(goods.getBarcode());// 商品条形码
				goodsMQ.setGoodsId(String.valueOf(goods.getGoodsId()));//商品ID
				goodsMQ.setGoodsName(goods.getGoodsName());//商品名称
				goodsMQ.setGoodsNo(goods.getGoodsNo());//商品货号
				goodsMQ.setBatchNo(goods.getBatchNo());//批次号
				goodsMQ.setProductionDate(productionMQDate);//生产日期
				goodsMQ.setOverdueDate(overdueMQDate);//失效日期
				goodsMQ.setNum(absNum);//数量
				//goodsMQ.setUnit(unit);//单位
				goodsMQ.setType(goods.getIsDamage());//商品分类 （0 好品 1坏品 ）
				*//***********************************判断过期品******************************//*

				if (StringUtils.isNotBlank(overdueMQDate)) {
					Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
					String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
					goodsMQ.setIsExpire(isExpire);//是否过期（0是 1否	)
				}else {
					goodsMQ.setIsExpire(DERP.ISEXPIRE_1);//是否过期  （0 是 1 否）
				}

				// 盘盈
				if (num > 0) {
					goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//0 调减 1调增
				}else {
					goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//0 调减 1调增
				}
				// 如果数量是0 不推送库存
				if (0!=num) {
					PDList.add(goodsMQ);
				}*/

			}

			// 推送库存的盘盈
			/*InvetAddOrSubtractRootJson invetAddOrSubtractPD= new InvetAddOrSubtractRootJson();
			invetAddOrSubtractPD.setBusinessNo(inventoryCode);// 业务单号
			invetAddOrSubtractPD.setMerchantId(String.valueOf(merchantId));//商家ID
			invetAddOrSubtractPD.setMerchantName(merchantName);//商家名称
			invetAddOrSubtractPD.setTopidealCode(topidealCode);//商家编码
			invetAddOrSubtractPD.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));//仓库ID
			invetAddOrSubtractPD.setDepotName(depotInfoMongo.getName());//仓库名称
			invetAddOrSubtractPD.setDepotType(depotInfoMongo.getType());//仓库类型
			invetAddOrSubtractPD.setDepotCode(depotInfoMongo.getCode());//仓库编码
			invetAddOrSubtractPD.setOrderNo(takesModel.getCode());// 盘点结果单
			invetAddOrSubtractPD.setIsTopBooks(depotInfoMongo.getIsTopBooks());
			invetAddOrSubtractPD.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0013);//PDJGD("0013","盘点结果单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
			invetAddOrSubtractPD.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015);//PDJGD("0015","盘点结果单"), //单据类型
			String now = sdf1.format(new Date());
			invetAddOrSubtractPD.setSourceDate(now);//单据时间

			invetAddOrSubtractPD.setDivergenceDate(transferStr);	//出入时间
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
			LOGGER.info("仓储退运信息 推送库存盘盈MQ 请求报文" + PDJson);
			// 推送盘盈
			if (PDList.size()>0) {
				rocketMQProducer.send(PDJson, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
			}*/
			return true;
		}

		// 如果 yModel 30：前端盘点申请 不用只制单不推送库存
		if (takesStockResultsModel == null && "30".equals(status)&& DERP_STORAGE.TAKESSTOCKRESULT_MODEL_30.equals(yModel)) {
			// 盘点结果 要加商家 id 商家名称 和 盘点状态
			// 盘点结果商品 要加 系统数量 实盘数量 差异数量
			// 新增盘点结果和盘点结果所对应的商品
			TakesStockResultsModel takesModel = new TakesStockResultsModel();
//			takesModel.setCode(CodeGeneratorUtil.getNo("PDJG"));// 盘点结果单号
			takesModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PDJG));// 盘点结果单号
			takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_024);//待确认
			takesModel.setDepotId(depotInfoMongo.getDepotId());// 盘点仓库id
			takesModel.setDepotName(depotInfoMongo.getName());// 盘点仓库名称
			takesModel.setTakesStockId(takesStockModel.getId());// 盘点指令id
			takesModel.setTakesStockCode(takesStockModel.getCode());// 盘点指令单号
			takesModel.setServerType(serverType);// 服务类型
			takesModel.setModel(yModel);// 业务场景
			takesModel.setDismissRemark(dismissRemark);// 驳回原因
			takesModel.setMerchantId(merchantId);
			takesModel.setMerchantName(merchantName);
			takesModel.setSourceTime(updateDate);// 归属月份
			takesModel.setSourceCode(inventoryCode);// 来源单号
			takesModel.setSourceType(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_1);//来源状态 1.op盘点结果 2 ofc盘点结果'
			takesModel.setCreateUsername("接口回传");// 创建人
			// 新增盘点结果表
			takesStockResultsDao.save(takesModel);


			for (CCInventoryResultsGoodsListJson goods : goodsList) {
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
				itemModel.setSystemNum(amount);// 系统数量
				itemModel.setRealQty(realqty);// 实盘数量
				/*
				 * 开发说明： 1、盘盈：接口盘点结果回推字段“系统数量”小于“实盘数量”，盘盈数量=实盘数量-系统数量
				 * 2、盘亏：接口盘点结果回推字段“系统数量”大于“实盘数量”，盘亏数量=|实盘数量-系统数量|
				 */
				// 实盘数量- 系统数量
				Integer num =realqty -amount;
				Integer absNum = Math.abs(num);
				if (num > 0) {
					itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1); // 1 盘盈
					itemModel.setSurplusNum(absNum);
				} else {
					itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_2);// 调整类型 2 盘亏
					itemModel.setDeficientNum(absNum);
				}


				String productionDate = goods.getProductionDate();
				if (StringUtils.isNotBlank(productionDate)) {
					if (productionDate.length() == 10) {
						itemModel.setProductionDate(Timestamp.valueOf(productionDate + " 00:00:00"));
					} else {
						itemModel.setProductionDate(Timestamp.valueOf(productionDate));
					}
				}
				String overdueDate = goods.getOverdueDate();
				if (StringUtils.isNotBlank(overdueDate)) {
					if (overdueDate.length() == 10) {
						itemModel.setOverdueDate(Timestamp.valueOf(overdueDate + " 00:00:00"));
					} else {
						itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));
					}
				}

				takesStockResultItemDao.save(itemModel);

			}
			return true;
		}


		//如果 yModel 10：客服盘点申请   20：仓库自行盘点 状态是30 也要 推送库存

		if (takesStockResultsModel == null && "30".equals(status)) {

			if (DERP_STORAGE.TAKESSTOCKRESULT_MODEL_10.equals(yModel) || DERP_STORAGE.TAKESSTOCKRESULT_MODEL_20.equals(yModel)) {
				// 盘点结果 要加商家 id 商家名称 和 盘点状态
				// 盘点结果商品 要加 系统数量 实盘数量 差异数量
				// 新增盘点结果和盘点结果所对应的商品
				TakesStockResultsModel takesModel = new TakesStockResultsModel();
//				takesModel.setCode(CodeGeneratorUtil.getNo("PDJG"));// 盘点结果单号
				takesModel.setCode(SmurfsUtils.getID(DERP.UNIQUEID_PREFIX_PDJG));// 盘点结果单号
				takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022); //CLZ("022","处理中"), // 状态 待确认 009   YQR("010","已确认"),
				takesModel.setDepotId(depotInfoMongo.getDepotId());// 盘点仓库id
				takesModel.setDepotName(depotInfoMongo.getName());// 盘点仓库名称
				takesModel.setTakesStockId(takesStockModel.getId());// 盘点指令id
				takesModel.setTakesStockCode(takesStockModel.getCode());// 盘点指令单号
				takesModel.setServerType(serverType);// 服务类型
				takesModel.setModel(yModel);// 业务场景
				takesModel.setDismissRemark(dismissRemark);// 驳回原因
				takesModel.setMerchantId(merchantId);
				takesModel.setMerchantName(merchantName);
				takesModel.setSourceTime(updateDate);// 归属月份
				takesModel.setSourceCode(inventoryCode);// 来源单号
				takesModel.setSourceType(DERP_STORAGE.TAKESSTOCKRESULT_SOURCETYPE_1);//来源状态 1.op盘点结果 2 ofc盘点结果'
				takesModel.setInConfirmUsername("接口回传");
				takesModel.setInConfirmTime(TimeUtils.getNow());
				takesModel.setCreateUsername("接口回传");// 创建人
				// 新增盘点结果表
				takesStockResultsDao.save(takesModel);


				List PDList=new ArrayList<>();// 盘盈时增加库存量    盘亏时减少库存量
				// 存储 经分销盘点结果邮件商品商品信息json数组
				JSONArray sendMailJSONArray = new JSONArray();
				for (CCInventoryResultsGoodsListJson goods : goodsList) {
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
					Integer realqty =goods.getRealqty();// 实盘数量
					itemModel.setSystemNum(amount);// 系统数量
					itemModel.setRealQty(realqty);// 实盘数量
					/*
					 * 开发说明： 1、盘盈：接口盘点结果回推字段“系统数量”小于“实盘数量”，盘盈数量=实盘数量-系统数量
					 * 2、盘亏：接口盘点结果回推字段“系统数量”大于“实盘数量”，盘亏数量=|实盘数量-系统数量|
					 */
					// 实盘数量- 系统数量
					Integer num = realqty - amount;
					Integer absNum = Math.abs(num);
					if (num > 0) {
						itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1); //  盘盈1
						itemModel.setSurplusNum(absNum);
					} else {
						itemModel.setType(DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_2);// 调整类型  盘亏2
						itemModel.setDeficientNum(absNum);
					}


					String productionDate = goods.getProductionDate();
					String productionMQDate=null;
					if (StringUtils.isNotBlank(productionDate)) {
						if (productionDate.length() == 10) {
							itemModel.setProductionDate(Timestamp.valueOf(productionDate + " 00:00:00"));
							productionMQDate=productionDate;
						} else {
							itemModel.setProductionDate(Timestamp.valueOf(productionDate));
							productionMQDate=productionDate.substring(0,10);
						}
					}
					String overdueDate = goods.getOverdueDate();
					String overdueMQDate= null;
					if (StringUtils.isNotBlank(overdueDate)) {
						if (overdueDate.length() == 10) {
							itemModel.setOverdueDate(Timestamp.valueOf(overdueDate + " 00:00:00"));
							overdueMQDate=overdueDate;
						} else {
							itemModel.setOverdueDate(Timestamp.valueOf(overdueDate));
							overdueMQDate=overdueDate.substring(0,10);
						}
					}

					takesStockResultItemDao.save(itemModel);


					/*InvetAddOrSubtractGoodsListJson goodsMQ = new  InvetAddOrSubtractGoodsListJson();
					// 向推送库存mq中保存商品
					goodsMQ.setBarcode(goods.getBarcode());// 商品条形码
					goodsMQ.setGoodsId(String.valueOf(goods.getGoodsId()));//商品ID
					goodsMQ.setGoodsName(goods.getGoodsName());//商品名称
					goodsMQ.setGoodsNo(goods.getGoodsNo());//商品货号
					goodsMQ.setBatchNo(goods.getBatchNo());//批次号
					goodsMQ.setProductionDate(productionMQDate);//生产日期
					goodsMQ.setOverdueDate(overdueMQDate);//失效日期
					goodsMQ.setNum(absNum);//数量
					//goodsMQ.setUnit(unit);//单位
					goodsMQ.setType(goods.getIsDamage());//商品分类 （0 好品 1坏品 ）
					*//***********************************判断过期品******************************//*
					if (StringUtils.isNotBlank(overdueMQDate)) {
						Timestamp exTtime = Timestamp.valueOf(overdueMQDate+" 00:00:00");
						String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
						goodsMQ.setIsExpire(isExpire);//是否过期（0是 1否	)
					}else {
						goodsMQ.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
					}

					// 盘盈
					if (num > 0) {
						goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//0 调减 1调增
					}else {
						goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//0 调减 1调增
					}
					// 如果数量是0 不推送库存
					if (0!=num) {
						PDList.add(goodsMQ);
					}*/

				}

				// 推送库存的盘盈
				/*InvetAddOrSubtractRootJson invetAddOrSubtractPD= new InvetAddOrSubtractRootJson();
				invetAddOrSubtractPD.setBusinessNo(inventoryCode);// 业务单号
				invetAddOrSubtractPD.setMerchantId(String.valueOf(merchantId));//商家ID
				invetAddOrSubtractPD.setMerchantName(merchantName);//商家名称
				invetAddOrSubtractPD.setTopidealCode(topidealCode);//商家编码
				invetAddOrSubtractPD.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));//仓库ID
				invetAddOrSubtractPD.setDepotName(depotInfoMongo.getName());//仓库名称
				invetAddOrSubtractPD.setDepotType(depotInfoMongo.getType());//仓库类型
				invetAddOrSubtractPD.setDepotCode(depotInfoMongo.getCode());//仓库编码
				invetAddOrSubtractPD.setOrderNo(takesModel.getCode());// 盘点结果单
				invetAddOrSubtractPD.setIsTopBooks(depotInfoMongo.getIsTopBooks());
				invetAddOrSubtractPD.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0013);//PDJGD("0013","盘点结果单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
				invetAddOrSubtractPD.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015); //PDJGD("0015","盘点结果单"), //单据类型
				String now = sdf1.format(new Date());
				invetAddOrSubtractPD.setSourceDate(now);//单据时间

				invetAddOrSubtractPD.setDivergenceDate(transferStr);	//出入时间
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
				LOGGER.info("仓储退运信息 推送库存盘盈MQ 请求报文" + PDJson);
				// 推送盘盈
				if (PDList.size()>0) {
					rocketMQProducer.send(PDJson, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				}*/

				return true;

			}
		}

		// 如果退运结果不为null 推送70 成功
		if (takesStockResultsModel != null && "70".equals(status)) {// 如果盘点
			// 结果存在就修改
			if (DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010.equals(takesStockResultsModel.getStatus())
					|| DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022.equals(takesStockResultsModel.getStatus())
					|| DERP_STORAGE.TAKESSTOCKRESULT_STATUS_021.equals(takesStockResultsModel.getStatus())) {
				LOGGER.error("盘点结果 已经入库,订单号orderCode" + orderCode);
				throw new RuntimeException("盘点结果 已经入库,订单号 orderCode" + orderCode);
			}

			// 根据判断结果id 查询盘点结果商品
			TakesStockResultItemModel takesStockResultItemModel = new TakesStockResultItemModel();
			takesStockResultItemModel.setTakesStockResultId(takesStockResultsModel.getId());
			Long count = takesStockResultItemDao.countNoExistBu(takesStockResultsModel.getId());
			if (count == 0) {
				List<TakesStockResultItemModel> itemList = takesStockResultItemDao.list(takesStockResultItemModel);

				List PDList=new ArrayList<>();// 盘点
				// 存储 经分销盘点结果邮件商品商品信息json数组
				JSONArray sendMailJSONArray = new JSONArray();
				for (TakesStockResultItemModel itemModel : itemList) {
					String type = itemModel.getType();// 1盘盈 2盘亏
					InvetAddOrSubtractGoodsListJson goodsMQ = new  InvetAddOrSubtractGoodsListJson();
					// 向推送库存mq中保存商品
					goodsMQ.setBarcode(itemModel.getBarcode());// 商品条形码
					goodsMQ.setGoodsId(String.valueOf(itemModel.getGoodsId()));//商品ID
					goodsMQ.setGoodsName(itemModel.getGoodsName());//商品名称
					goodsMQ.setGoodsNo(itemModel.getGoodsNo());//商品货号
					goodsMQ.setBatchNo(itemModel.getBatchNo());//批次号
					goodsMQ.setBuId(String.valueOf(itemModel.getBuId()));
					goodsMQ.setStockLocationTypeName(itemModel.getStockLocationTypeName());
					goodsMQ.setStockLocationTypeId(String.valueOf(itemModel.getStockLocationTypeId()));
					goodsMQ.setBuName(itemModel.getBuName());
					Date productionDate = itemModel.getProductionDate();
					DateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
					String productionDateStr=null;
					if (productionDate !=null) {
						productionDateStr = dft.format(productionDate);
						goodsMQ.setProductionDate(productionDateStr);//生产日期
					}
					Date overdueDate = itemModel.getOverdueDate();
					String overdueMQDateStr=null;
					if (overdueDate !=null) {
						overdueMQDateStr = dft.format(overdueDate);
						goodsMQ.setOverdueDate(overdueMQDateStr);//失效日期
					}
					goodsMQ.setType(itemModel.getIsDamage());//商品分类 （0 好品 1坏品 2 过期）
					//goodsMQ.setUnit(unit);//单位
					//goodsMQ.setIsExpire("");//是否过期  （0 是 1 否）
					goodsMQ.setType(itemModel.getIsDamage());//商品分类 （0 好品 1坏品 ）

					/***********************************判断过期品******************************/
					if (StringUtils.isNotBlank(overdueMQDateStr)) {
						Timestamp exTtime = Timestamp.valueOf(overdueMQDateStr+" 00:00:00");
						String isExpire = TimeUtils.isNotIsExpire(exTtime);//判断是否过期是否过期（0是 1否）
						goodsMQ.setIsExpire(isExpire);//是否过期（0是 1否	)
					}else {
						goodsMQ.setIsExpire(DERP.ISEXPIRE_1);//是否过期（0是 1否）
					}


					// 盘盈
					if (DERP_STORAGE.TAKESSTOCKRESULTITEM_TYPE_1.equals(type)) {
						goodsMQ.setNum(itemModel.getSurplusNum());//数量
						goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_1);//0 调减 1调增
					}else {
						goodsMQ.setNum(itemModel.getDeficientNum());//数量
						goodsMQ.setOperateType(DERP_INVENTORY.INVENTORY_OPERATETYPE_0);//0 调减 1调增
					}
					// 实盘数量减系统数量如果等于0 就不推送库存
					int num=itemModel.getRealQty()-itemModel.getSystemNum();
					if (0!=num) {
						PDList.add(goodsMQ);
					}

				}

				// 推送库存的盘点

				InvetAddOrSubtractRootJson invetAddOrSubtractPD= new InvetAddOrSubtractRootJson();
				invetAddOrSubtractPD.setBusinessNo(inventoryCode);// 业务单号
				invetAddOrSubtractPD.setMerchantId(String.valueOf(merchantId));//商家ID
				invetAddOrSubtractPD.setMerchantName(merchantName);//商家名称
				invetAddOrSubtractPD.setTopidealCode(topidealCode);//商家编码
				invetAddOrSubtractPD.setDepotId(String.valueOf(depotInfoMongo.getDepotId()));//仓库ID
				invetAddOrSubtractPD.setDepotName(depotInfoMongo.getName());//仓库名称
				invetAddOrSubtractPD.setDepotType(depotInfoMongo.getType());//仓库类型
				invetAddOrSubtractPD.setDepotCode(depotInfoMongo.getCode());//仓库编码
				invetAddOrSubtractPD.setOrderNo(takesStockResultsModel.getCode());// 盘点结果单
				invetAddOrSubtractPD.setIsTopBooks(depotInfoMongo.getIsTopBooks());
				invetAddOrSubtractPD.setSource(DERP_INVENTORY.INVENTORY_SOURCE_0013);//PDJGD("0013","盘点结果单"), //单据1采购  2 调拨 3 销售 4库存调整单5类型调整
				invetAddOrSubtractPD.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_0015); //PDJGD("0015","盘点结果单"), //单据类型
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String now = sdf.format(new Date());
				invetAddOrSubtractPD.setSourceDate(now);//单据时间

				invetAddOrSubtractPD.setDivergenceDate(transferStr);	//出入时间
				// 获取当前年月
				invetAddOrSubtractPD.setOwnMonth(yearsMonths);
				invetAddOrSubtractPD.setGoodsList(PDList);
				//库存回推数据
				Map<String, Object> customParam=new HashMap<String, Object>();
				invetAddOrSubtractPD.setBackTags(MQPushBackStorageEnum.STORAGE_RESULTS_PUSH_BACK.getTags());//回调标签
				invetAddOrSubtractPD.setBackTopic(MQPushBackStorageEnum.STORAGE_RESULTS_PUSH_BACK.getTopic());//回调主题
				customParam.put("code", takesStockResultsModel.getCode());// 盘点结果内部单号
				invetAddOrSubtractPD.setCustomParam(customParam);////自定义回调参数
				String PDJson = JSONObject.fromObject(invetAddOrSubtractPD).toString();
				LOGGER.info("仓储退运信息 推送库存盘盈MQ 请求报文" + PDJson);
				// 推送盘点
				if (PDList.size()>0) {
					rocketMQProducer.send(PDJson, MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				}
			}

			// 修改为盘点结果表为已提交
			TakesStockResultsModel takesModel = new TakesStockResultsModel();
			takesModel.setId(takesStockResultsModel.getId());

			if ("70".equals(status)) {// 已确认
				takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022); //CLZ("022","处理中"),
				takesModel.setInConfirmUsername("接口回传");
				takesModel.setInConfirmTime(TimeUtils.getNow());
				takesModel.setSourceTime(updateDate);// 归属时间
				// 修改盘点结果表
				takesStockResultsDao.modify(takesModel);
			}
			return true;
		}

		// 如果退运结果不为null 推送90
		if (takesStockResultsModel != null && "90".equals(status)) {// 如果盘点
			// 结果存在就修改
			String tag = null;
			// 已确认  或者已作废 //YQR("010","已确认"), YZF("021","已作废"),
			if (takesStockResultsModel.getStatus().equals(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_010)
					||takesStockResultsModel.getStatus().equals(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_022)
					||takesStockResultsModel.getStatus().equals(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_021)) {
				LOGGER.error("盘点结果 已经入库,订单号orderCode,不能取消" + orderCode);
				throw new RuntimeException("盘点结果 已经入库,订单号 orderCode,不能取消" + orderCode);
			}

			TakesStockResultsModel takesModel = new TakesStockResultsModel();
			takesModel.setId(takesStockResultsModel.getId());
			takesModel.setStatus(DERP_STORAGE.TAKESSTOCKRESULT_STATUS_021); //YZF("021","已作废")
			// 修改盘点结果表
			takesStockResultsDao.modify(takesModel);
			return true;
		}

		throw new RuntimeException("盘点结果接口没有执行任何操作,订单号 orderCode," + orderCode);

	}



}
