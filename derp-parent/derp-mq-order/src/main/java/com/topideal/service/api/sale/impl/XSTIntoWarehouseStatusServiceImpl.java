package com.topideal.service.api.sale.impl;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.topideal.common.constant.DERP_INVENTORY;
import com.topideal.common.constant.DERP_LOG_POINT;
import com.topideal.common.constant.DERP_ORDER;
import com.topideal.common.enums.MQInventoryEnum;
import com.topideal.common.enums.MQPushBackOrderEnum;
import com.topideal.common.system.annotation.SystemServiceLog;
import com.topideal.common.system.mq.RMQProducer;
import com.topideal.common.tools.TimeUtils;
import com.topideal.dao.sale.*;
import com.topideal.entity.vo.sale.*;
import com.topideal.json.api.v2_2.SaleReturnIntoWarehouseStatusJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractGoodsListJson;
import com.topideal.json.inventory.j01.InvetAddOrSubtractRootJson;
import com.topideal.mongo.dao.DepotInfoMongoDao;
import com.topideal.mongo.dao.MerchandiseInfoMogoDao;
import com.topideal.mongo.entity.DepotInfoMongo;
import com.topideal.service.api.sale.XSTIntoWarehouseStatusService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 进仓单状态回推
 */
@Service
public class XSTIntoWarehouseStatusServiceImpl implements XSTIntoWarehouseStatusService{
	/*打印日志*/
	private static final Logger logger = LoggerFactory.getLogger(XSTIntoWarehouseStatusServiceImpl.class);
	@Autowired
	private SaleReturnOrderDao saleReturnOrderDao;// 销售退货表头
	@Autowired
	private SaleReturnIdepotDao saleReturnIdepotDao;//销售退货出入库		
	@Autowired
	private SaleReturnIdepotItemDao saleReturnIdepotItemDao;//销售退货出入库商品
	@Autowired
	private SaleReturnOdepotDao saleReturnOdepotDao;// 销售退货出库单 
	@Autowired
	private SaleReturnOdepotItemDao saleReturnOdepotItemDao;//销售退货出库单
	//销售退预申报
	@Autowired
	private SaleReturnDeclareOrderDao saleReturnDeclareOrderDao;
	@Autowired 
	private DepotInfoMongoDao depotInfoMongoDao;// mongoDB仓库
	@Autowired
	private RMQProducer rocketMQProducer;//MQ
	//商品信息MongoDB
	@Autowired
	private MerchandiseInfoMogoDao merchandiseInfoMogoDao ;

	//进仓单状态回推
	@Override
	@SystemServiceLog(point= DERP_LOG_POINT.POINT_12203120200,model=DERP_LOG_POINT.POINT_12203120200_Label,keyword="entInboundId")
	public boolean saveIntoWarehouseStatusInfo(String json,String keys,String topics,String tags) throws Exception {
		// 实例化json对象
		JSONObject jsonData = JSONObject.fromObject(json);
		// JSON对象转实体
		SaleReturnIntoWarehouseStatusJson rootJson = (SaleReturnIntoWarehouseStatusJson) JSONObject.toBean(jsonData, SaleReturnIntoWarehouseStatusJson.class);
		String entInboundId = rootJson.getEntInboundId();// 企业入仓编号

		saveIntoWarehouseStatusInfo_new(rootJson);
		return true;
	}

	/**进仓状态-新
	 * */
	public void saveIntoWarehouseStatusInfo_new(SaleReturnIntoWarehouseStatusJson rootJson) throws Exception {
		String entInboundId = rootJson.getEntInboundId();// 企业入仓编号
		String status = rootJson.getStatus();//进仓状态1：成功；2：失败
		String inboundDate = rootJson.getInboundDate();

		//成功才接收
		if (!"1".equals(status)) {
			logger.error("只接收成功状态，结束,订单编号"+entInboundId+",报文状态："+status);
			return;
		}
        //1.查询预申报单 是否存在
		SaleReturnDeclareOrderModel declareOrderModel = new SaleReturnDeclareOrderModel();
		declareOrderModel.setCode(entInboundId);
		declareOrderModel = saleReturnDeclareOrderDao.searchByModel(declareOrderModel);
		if(declareOrderModel==null){
			logger.error("销售退预申报单不存在,单号：" + entInboundId );
			throw new RuntimeException("销售退预申报单不存在,单号：" + entInboundId);
		}

		//2.获取预申报单对应的所有入库单、出库单
		SaleReturnIdepotModel returnIdepotModel = new SaleReturnIdepotModel();
		returnIdepotModel.setReturnDeclareOrderId(declareOrderModel.getId());
		returnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_011);
		List<SaleReturnIdepotModel> returnIdepotList = saleReturnIdepotDao.list(returnIdepotModel);

		SaleReturnOdepotModel returnOdepotModel = new SaleReturnOdepotModel();
		returnOdepotModel.setReturnDeclareOrderId(declareOrderModel.getId());
		returnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_015);
		List<SaleReturnOdepotModel> returnOdepotList = saleReturnOdepotDao.list(returnOdepotModel);

		List<InvetAddOrSubtractRootJson> subtractJsonList = new ArrayList<>();//扣减库存报文
		//循环入库单生成报文
		if(returnIdepotList!=null&&returnIdepotList.size()>0){
			for(SaleReturnIdepotModel returnIdepot : returnIdepotList){
				InvetAddOrSubtractRootJson subtractRootJson = saveReturnInDepot_new(inboundDate,returnIdepot);
				subtractJsonList.add(subtractRootJson);
			}
		}

		//循环入库单生成报文
		if(returnOdepotList!=null&&returnOdepotList.size()>0){
			for(SaleReturnOdepotModel returnOdepot : returnOdepotList){
				InvetAddOrSubtractRootJson subtractRootJson = saveReturnOutDepot_new(inboundDate,returnOdepot);
				subtractJsonList.add(subtractRootJson);
			}
		}

		if(subtractJsonList.size()>0){
			for(InvetAddOrSubtractRootJson subtractJson : subtractJsonList){
				// 推送加减库存
				SendResult sendResult = rocketMQProducer.send(JSONObject.fromObject(subtractJson).toString(), MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTopic(),MQInventoryEnum.INVENTORY_QUANTITY_ADD_LOWER.getTags());
				logger.info("subtractJson = " + JSONObject.fromObject(subtractJson).toString());
				logger.info(sendResult.toString());
			}
		}

	}
	//拼装入库单扣减库存报文
	public InvetAddOrSubtractRootJson saveReturnInDepot_new(String inboundDate,SaleReturnIdepotModel returnIdepot) throws Exception {

		Timestamp inboundDateTime=null;
		String inboundDateStr=null;
		if (StringUtils.isNotBlank(inboundDate)) {
			if (inboundDate.length() == 10) {
				inboundDateTime = Timestamp.valueOf(inboundDate + " 00:00:00"); // 发货时间
				inboundDateStr=inboundDate + " 00:00:00";
			} else {
				inboundDateTime = Timestamp.valueOf(inboundDate);// 发货时间
				inboundDateStr=inboundDate;
			}
		}
		//查询销售退货订单
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
		saleReturnOrderModel.setCode(returnIdepot.getOrderCode());// 销售退货订单编码
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);

		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> depotInfoMap= new HashMap<>();
		depotInfoMap.put("depotId", returnIdepot.getInDepotId());//调入仓库id
		DepotInfoMongo inDepotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);
		if (inDepotInfoMongo ==null) {
			logger.error("销售退货入库仓库mongodb未查到仓库信息,订单编号"+returnIdepot.getCode());
			throw new RuntimeException("销售退货入库仓库mongodb未查到仓库信息,订单编号"+returnIdepot.getCode());
		}
		//判断入库单状态
		if (!DERP_ORDER.SALERETURNIDEPOT_STATUS_011.equals(returnIdepot.getStatus())) {
			logger.error("销售退货入库单不是待入库状态,订单编号" + returnIdepot.getCode());
			throw new RuntimeException("销售退货入库单不是待入库状态,订单编号" + returnIdepot.getCode());
		}

		// 把销售退货入库清单状态改为入库中
		SaleReturnIdepotModel sReturnIdepotModel = new  SaleReturnIdepotModel();
		sReturnIdepotModel.setId(returnIdepot.getId());// 销售退货入库id
		sReturnIdepotModel.setReturnInDate(TimeUtils.parseFullTime(inboundDateStr));
		sReturnIdepotModel.setStatus(DERP_ORDER.SALERETURNIDEPOT_STATUS_028);// 状态 "011","待入仓"  "012","已入仓"',028:入库中
		// 修改销售退货入库单
		saleReturnIdepotDao.modify(sReturnIdepotModel);

		//修改销售退货订单状态为入库中
		SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_028);//状态： 028-入库中
		saleReturnOrderDao.modify(saleReturnOrderModel1);

		//------------------扣减库存------------------------
		//查询销售退货入库单表体
		SaleReturnIdepotItemModel saleReturnIdepotItemModel = new SaleReturnIdepotItemModel();
		saleReturnIdepotItemModel.setSreturnIdepotId(returnIdepot.getId());
		List<SaleReturnIdepotItemModel> inItemList = saleReturnIdepotItemDao.list(saleReturnIdepotItemModel);

		// 增加库存
		InvetAddOrSubtractRootJson inRootJson = new InvetAddOrSubtractRootJson();
		inRootJson.setMerchantId(String.valueOf(returnIdepot.getMerchantId()));
		inRootJson.setMerchantName(returnIdepot.getMerchantName());
		inRootJson.setTopidealCode(saleReturnOrderModel.getTopidealCode());
		inRootJson.setDepotId(String.valueOf(inDepotInfoMongo.getDepotId()));
		inRootJson.setDepotName(inDepotInfoMongo.getName());
		inRootJson.setDepotCode(inDepotInfoMongo.getCode());
		inRootJson.setDepotType(inDepotInfoMongo.getType());
		inRootJson.setIsTopBooks(inDepotInfoMongo.getIsTopBooks());
		inRootJson.setOrderNo(returnIdepot.getCode());
		inRootJson.setBusinessNo(saleReturnOrderModel.getCode());
		inRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_005);
		inRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_005);
		inRootJson.setSourceDate(TimeUtils.formatFullTime());
		inRootJson.setDivergenceDate(inboundDateStr);// 出入库
		inRootJson.setBuId(String.valueOf(returnIdepot.getBuId()));// 事业部
		inRootJson.setBuName(returnIdepot.getBuName());
		inRootJson.setOwnMonth(TimeUtils.formatMonth(inboundDateTime));// 归属月份

		List<InvetAddOrSubtractGoodsListJson> invetAddOrSubtractGoodsListJsonList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		for (SaleReturnIdepotItemModel item : inItemList) {

			//坏货数量
			if(item.getWornNum()!=null && item.getWornNum()>0){
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
				listJson.setGoodsId(String.valueOf(item.getInGoodsId()));
				listJson.setGoodsName(item.getInGoodsName());
				listJson.setGoodsNo(item.getInGoodsNo());
				listJson.setBarcode(item.getBarcode());// 条形码
				listJson.setBatchNo(item.getReturnBatchNo());
				listJson.setStockLocationTypeId(String.valueOf(saleReturnOrderModel.getStockLocationTypeId()));
				listJson.setStockLocationTypeName(saleReturnOrderModel.getStockLocationTypeName());
				if (item.getProductionDate()!=null) {
					listJson.setProductionDate(sdf3.format(item.getProductionDate()));
				}
				if (item.getOverdueDate()!=null) {
					listJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					listJson.setIsExpire(isExpire);//是否过期（0是 1否）
				}else{
					listJson.setIsExpire("1");//是否过期（0是 1否）
				}
				listJson.setType("1");//商品类型（0 好品 1坏品）
				listJson.setNum(item.getWornNum());
				listJson.setOperateType("1");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJsonList.add(listJson);
			}
			//过期数量
			if(item.getExpireNum()!=null && item.getExpireNum()>0){
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
				listJson.setBarcode(item.getBarcode());// 条形码
				listJson.setGoodsId(String.valueOf(item.getInGoodsId()));
				listJson.setGoodsName(item.getInGoodsName());
				listJson.setGoodsNo(item.getInGoodsNo());
				listJson.setBatchNo(item.getReturnBatchNo());
				listJson.setStockLocationTypeId(String.valueOf(saleReturnOrderModel.getStockLocationTypeId()));
				listJson.setStockLocationTypeName(saleReturnOrderModel.getStockLocationTypeName());
				if (item.getProductionDate()!=null) {
					listJson.setProductionDate(sdf3.format(item.getProductionDate()));
				}
				if (item.getOverdueDate()!=null) {
					listJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					listJson.setIsExpire(isExpire);//是否过期（0是 1否）
				}else {
					listJson.setIsExpire("0");//是否过期（0是 1否）
				}

				listJson.setType("1");//商品类型（0 好品 1坏品 ）
				listJson.setNum(item.getExpireNum());
				listJson.setOperateType("1");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJsonList.add(listJson);
			}
			//正常数量
			if(item.getReturnNum()!=null && item.getReturnNum()>0){
				//正常数量
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
				listJson.setBarcode(item.getBarcode());// 条形码
				listJson.setGoodsId(String.valueOf(item.getInGoodsId()));
				listJson.setGoodsName(item.getInGoodsName());
				listJson.setGoodsNo(item.getInGoodsNo());
				listJson.setBatchNo(item.getReturnBatchNo());
				listJson.setStockLocationTypeId(String.valueOf(saleReturnOrderModel.getStockLocationTypeId()));
				listJson.setStockLocationTypeName(saleReturnOrderModel.getStockLocationTypeName());
				if (item.getProductionDate()!=null) {
					listJson.setProductionDate(sdf3.format(item.getProductionDate()));
				}
				if (item.getOverdueDate()!=null) {
					listJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					listJson.setIsExpire(isExpire);//是否过期（0是 1否）
				}else{
					listJson.setIsExpire("1");//是否过期（0是 1否）
				}
				listJson.setType("0");//商品类型（0 好品 1坏品）
				listJson.setNum(item.getReturnNum());
				listJson.setOperateType("1");// 字符串 0 调减 1调增
				invetAddOrSubtractGoodsListJsonList.add(listJson);
			}
		}
		inRootJson.setGoodsList(invetAddOrSubtractGoodsListJsonList);
		inRootJson.setBackTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK_2.getTags());//回调标签
		inRootJson.setBackTopic(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK_2.getTopic());//回调主题
		//库存回推数据
		Map<String, Object> customParam=new HashMap<String, Object>();
		customParam.put("code", saleReturnOrderModel.getCode());// 销售退货订单内部单号
		customParam.put("tags", "1");//1 调增 , 0 调减
		inRootJson.setCustomParam(customParam);//自定义回调参数

        return inRootJson;
	}
	/**拼装出库单扣减库存报文
	 * */
	public InvetAddOrSubtractRootJson saveReturnOutDepot_new(String inboundDate,SaleReturnOdepotModel returnOdepot) throws Exception {
		Timestamp inboundDateTime=null;
		String inboundDateStr=null;
		if (StringUtils.isNotBlank(inboundDate)) {
			if (inboundDate.length() == 10) {
				inboundDateTime = Timestamp.valueOf(inboundDate + " 00:00:00"); // 发货时间
				inboundDateStr=inboundDate + " 00:00:00";
			} else {
				inboundDateTime = Timestamp.valueOf(inboundDate);// 发货时间
				inboundDateStr=inboundDate;
			}
		}

		//查询销售退货订单
		SaleReturnOrderModel saleReturnOrderModel = new SaleReturnOrderModel();
		saleReturnOrderModel.setCode(returnOdepot.getOrderCode());// 销售退货订单编码
		saleReturnOrderModel = saleReturnOrderDao.searchByModel(saleReturnOrderModel);

		// 根据仓库id到mongoDB中查询 仓库信息
		Map<String, Object> depotInfoMap= new HashMap<>();
		depotInfoMap.put("depotId", returnOdepot.getOutDepotId());//调入仓库id
		DepotInfoMongo depotInfoMongo = depotInfoMongoDao.findOne(depotInfoMap);
		if (depotInfoMongo ==null) {
			logger.error("销售退货入库仓库mongodb未查到仓库信息,订单编号"+returnOdepot.getCode());
			throw new RuntimeException("销售退货入库仓库mongodb未查到仓库信息,订单编号"+returnOdepot.getCode());
		}
		//判断出库单状态
		if (!DERP_ORDER.SALERETURNODEPOT_STATUS_015.equals(returnOdepot.getStatus())) {
			logger.error("销售退货出库单不是待出库状态,订单编号" + returnOdepot.getCode());
			throw new RuntimeException("销售退货出库单不是待出库状态,订单编号" + returnOdepot.getCode());
		}

		//把销售退货出库单改为出库中
		SaleReturnOdepotModel sReturnOdepotModel = new  SaleReturnOdepotModel();
		sReturnOdepotModel.setId(returnOdepot.getId());//销售退货出库id
		sReturnOdepotModel.setReturnOutDate(TimeUtils.parseFullTime(inboundDateStr));
		sReturnOdepotModel.setStatus(DERP_ORDER.SALERETURNODEPOT_STATUS_027);//	015:待出仓,016:已出仓,027:出库中
		saleReturnOdepotDao.modify(sReturnOdepotModel);

		//修改销售退货订单状态为入库中
		SaleReturnOrderModel saleReturnOrderModel1 = new SaleReturnOrderModel();
		saleReturnOrderModel1.setId(saleReturnOrderModel.getId());
		saleReturnOrderModel1.setStatus(DERP_ORDER.SALERETURNORDER_STATUS_028);//状态： 028-入库中
		saleReturnOrderDao.modify(saleReturnOrderModel1);

		//查询销售退货出库单表体
		SaleReturnOdepotItemModel saleReturnOdepotItemModel = new SaleReturnOdepotItemModel();
		saleReturnOdepotItemModel.setSreturnOdepotId(returnOdepot.getId());// 销售出库单id
		List<SaleReturnOdepotItemModel> outItemlist = saleReturnOdepotItemDao.list(saleReturnOdepotItemModel);
		//扣减销售出库库存量
		InvetAddOrSubtractRootJson outRootJson = new InvetAddOrSubtractRootJson();
		outRootJson = new InvetAddOrSubtractRootJson();
		outRootJson.setMerchantId(returnOdepot.getMerchantId().toString());
		outRootJson.setMerchantName(returnOdepot.getMerchantName());
		outRootJson.setTopidealCode(saleReturnOrderModel.getTopidealCode());
		outRootJson.setDepotId(depotInfoMongo.getDepotId().toString());
		outRootJson.setDepotName(depotInfoMongo.getName());
		outRootJson.setDepotCode(depotInfoMongo.getCode());
		outRootJson.setDepotType(depotInfoMongo.getType());
		outRootJson.setIsTopBooks(depotInfoMongo.getIsTopBooks());
		outRootJson.setOrderNo(returnOdepot.getCode());
		outRootJson.setBusinessNo(saleReturnOrderModel.getCode());
		outRootJson.setSource(DERP_INVENTORY.INVENTORY_SOURCE_004);
		outRootJson.setSourceType(DERP_INVENTORY.INVENTORY_SOURCETYPE_006);
		outRootJson.setSourceDate(TimeUtils.formatFullTime());
		outRootJson.setBuId(String.valueOf(saleReturnOrderModel.getBuId()));// 事业部
		outRootJson.setBuName(saleReturnOrderModel.getBuName());
		outRootJson.setDivergenceDate(inboundDateStr);//出入时间
		outRootJson.setOwnMonth(TimeUtils.formatMonth(inboundDateTime));//归属月份

		//拼装扣减库存商品报文
		List<InvetAddOrSubtractGoodsListJson> subtractGoodsList = new ArrayList<InvetAddOrSubtractGoodsListJson>();
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
		for (SaleReturnOdepotItemModel item : outItemlist) {

			//坏货数量
			if(item.getWornNum()!=null && item.getWornNum()>0){
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
				listJson.setBarcode(item.getBarcode());// 条形码
				listJson.setGoodsId(String.valueOf(item.getOutGoodsId()));
				listJson.setGoodsName(item.getOutGoodsName());
				listJson.setGoodsNo(item.getOutGoodsNo());
				listJson.setBatchNo(item.getReturnBatchNo());
				listJson.setStockLocationTypeId(String.valueOf(saleReturnOrderModel.getStockLocationTypeId()));
				listJson.setStockLocationTypeName(saleReturnOrderModel.getStockLocationTypeName());
				if (item.getProductionDate()!=null) {
					listJson.setProductionDate(sdf3.format(item.getProductionDate()));
				}
				if (item.getOverdueDate()!=null) {
					listJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					listJson.setIsExpire(isExpire);//是否过期（0是 1否）
				}else {
					listJson.setIsExpire("1");//是否过期（0是 1否）
				}
				listJson.setType("1");//商品类型（0 好品 1坏品）
				listJson.setNum(item.getWornNum());
				listJson.setOperateType("0");// 字符串 0 调减 1调增
				subtractGoodsList.add(listJson);
			}
			//过期数量
			if(item.getExpireNum()!=null && item.getExpireNum()>0){
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
				listJson.setBarcode(item.getBarcode());// 条形码
				listJson.setGoodsId(String.valueOf(item.getOutGoodsId()));
				listJson.setGoodsName(item.getOutGoodsName());
				listJson.setGoodsNo(item.getOutGoodsNo());
				listJson.setBatchNo(item.getReturnBatchNo());
				listJson.setStockLocationTypeId(String.valueOf(saleReturnOrderModel.getStockLocationTypeId()));
				listJson.setStockLocationTypeName(saleReturnOrderModel.getStockLocationTypeName());
				if (item.getProductionDate()!=null) {
					listJson.setProductionDate(sdf3.format(item.getProductionDate()));
				}
				if (item.getOverdueDate()!=null) {
					listJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					listJson.setIsExpire(isExpire);//是否过期（0是 1否）
				}else {
					listJson.setIsExpire("0");//是否过期（0是 1否）
				}

				listJson.setType("1");//商品类型（0 好品 1坏品 ）
				listJson.setNum(item.getExpireNum());
				listJson.setOperateType("0");// 字符串 0 调减 1调增
				subtractGoodsList.add(listJson);
			}
			//正常数量
			if(item.getReturnNum()!=null && item.getReturnNum()>0){
				InvetAddOrSubtractGoodsListJson listJson = new InvetAddOrSubtractGoodsListJson();
				listJson.setBarcode(item.getBarcode());// 条形码
				listJson.setGoodsId(String.valueOf(item.getOutGoodsId()));
				listJson.setGoodsName(item.getOutGoodsName());
				listJson.setGoodsNo(item.getOutGoodsNo());
				listJson.setBatchNo(item.getReturnBatchNo());
				listJson.setStockLocationTypeId(String.valueOf(saleReturnOrderModel.getStockLocationTypeId()));
				listJson.setStockLocationTypeName(saleReturnOrderModel.getStockLocationTypeName());
				if (item.getProductionDate()!=null) {
					listJson.setProductionDate(sdf3.format(item.getProductionDate()));
				}
				if (item.getOverdueDate()!=null) {
					listJson.setOverdueDate(sdf3.format(item.getOverdueDate()));
					String isExpire = TimeUtils.isNotIsExpireByDate(item.getOverdueDate());//判断是否过期是否过期（0是 1否）
					listJson.setIsExpire(isExpire);//是否过期（0是 1否）
				}else{
					listJson.setIsExpire("1");//是否过期（0是 1否）
				}
				listJson.setType("0");//商品类型（0 好品 1坏品）
				listJson.setNum(item.getReturnNum());
				listJson.setOperateType("0");// 字符串 0 调减 1调增
				subtractGoodsList.add(listJson);
			}
		}
		outRootJson.setGoodsList(subtractGoodsList);
		//库存回推数据
		Map<String, Object> customParam2=new HashMap<String, Object>();
		outRootJson.setBackTags(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK_2.getTags());//回调标签
		outRootJson.setBackTopic(MQPushBackOrderEnum.WAREHOUSE_STATUS_PUSH_BACK_2.getTopic());//回调主题
		customParam2.put("code", saleReturnOrderModel.getCode());// 销售退货订单内部单号
		customParam2.put("tags", "0");//1 调增 , 0 调减
		outRootJson.setCustomParam(customParam2);

		return outRootJson;
	}

}
